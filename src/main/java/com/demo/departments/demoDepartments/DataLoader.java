package com.demo.departments.demoDepartments;

import com.demo.departments.demoDepartments.config.metrics.MetricsConfig;
import com.demo.departments.demoDepartments.persistence.model.*;
import com.demo.departments.demoDepartments.persistence.model.security.Permissions;
import com.demo.departments.demoDepartments.persistence.model.security.Role;
import com.demo.departments.demoDepartments.persistence.repository.*;
import com.demo.departments.demoDepartments.service.*;
import com.demo.departments.demoDepartments.service.dto.PersonDTO;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

@Slf4j
@RequiredArgsConstructor
@Component
@Transactional
public class DataLoader implements ApplicationRunner {

    private final PermissionsService permissionsService;
    private final RoleService roleService;
    private final ContactService contactService;
    private final AddressService addressService;
    private final PersonService personService;
    private final PersonRepository personRepository;
    private final ContactRepository contactRepository;
    private final MetricsConfig metricsConfig;
    private final MeterRegistry meterRegistry;
    
    @PersistenceContext
    private EntityManager entityManager;

    // Sample data arrays
    private static final String[] FIRST_NAMES = {"John", "Jane", "Michael", "Emily", "David", "Sarah", "Robert", "Maria", "Daniel", "Olivia"};
    private static final String[] LAST_NAMES = {"Smith", "Johnson", "Williams", "Brown", "Jones", "Garcia", "Miller", "Davis", "Rodriguez", "Martinez"};
    private static final String[] STREETS = {"123 Main St", "456 Elm Ave", "789 Oak Blvd", "101 Pine Lane", "202 Maple Dr", 
                                           "303 Cedar Ct", "404 Birch Rd", "505 Willow Way", "606 Spruce St", "707 Cherry Ave"};
    private static final String[] CITIES = {"New York", "Los Angeles", "Chicago", "Houston", "Phoenix", 
                                          "Philadelphia", "San Antonio", "San Diego", "Dallas", "San Jose"};
    private static final String[] PROVINCES = {"NY", "CA", "IL", "TX", "AZ", "PA", "TX", "CA", "TX", "CA"};
    private static final String[] COUNTRIES = {"USA", "USA", "USA", "USA", "USA", "USA", "USA", "USA", "USA", "USA"};
    private static final String[] ZIP_CODES = {"10001", "90001", "60601", "77001", "85001", "19101", "78201", "92101", "75201", "95101"};
    private static final String[] PHONE_NUMBERS = {"212-555-1234", "213-555-2345", "312-555-3456", "713-555-4567", "602-555-5678", 
                                                 "215-555-6789", "210-555-7890", "619-555-8901", "214-555-9012", "408-555-0123"};
    private static final String[] ROLES = {"ADMIN", "USER", "MANAGER", "DEVELOPER", "ANALYST", 
                                         "SUPPORT", "TESTER", "DESIGNER", "ARCHITECT", "CONSULTANT"};
    private static final String[][] PERMISSIONS = {
        {"READ_ALL", "WRITE_ALL", "DELETE_ALL"},
        {"READ_OWN", "WRITE_OWN"},
        {"READ_ALL", "WRITE_ALL", "APPROVE_REQUESTS"},
        {"READ_ALL", "WRITE_CODE", "DEBUG_CODE"},
        {"READ_ALL", "GENERATE_REPORTS"},
        {"READ_ALL", "RESPOND_TICKETS"},
        {"READ_ALL", "CREATE_TESTS", "RUN_TESTS"},
        {"READ_ALL", "CREATE_DESIGNS", "MODIFY_UI"},
        {"READ_ALL", "MODIFY_ARCHITECTURE", "APPROVE_CHANGES"},
        {"READ_ALL", "WRITE_REPORTS"}
    };

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("Starting data loading...");
        
        // Only load data if the database is empty
        if (isDatabaseEmpty()) {
            try {
                // Create 10 people with all related data
                for (int i = 0; i < 10; i++) {
                    Person person = createPerson(i);
                    personRepository.save(person);
                    log.info("Created person: {} {}", person.getFirstName(), person.getLastName());
                }
                log.info("Data loading completed successfully.");
                
                // Update metrics after data loading
                updateMetricsGauges();
            } catch (DataIntegrityViolationException e) {
                log.warn("Data already exists in the database. Skipping data loading. Error: {}", e.getMessage());
                
                // Still update metrics for existing data
                updateMetricsGauges();
            }
        } else {
            log.info("Database already contains data. Skipping data loading.");
            
            // Update metrics for existing data
            updateMetricsGauges();
        }
        
        try {
            // Verify data was loaded properly by retrieving one person with all details
            Set<String> attributes = new HashSet<>(List.of("contacts", "roles.permissions", "addresses"));
            PersonDTO dto = personService.findById(1L, true, attributes);
            String a = "";
        } catch (Exception e) {
            log.warn("Could not verify data: {}", e.getMessage());
        }
    }
    
    /**
     * Check if the database is empty before inserting data
     * @return true if the database is empty
     */
    private boolean isDatabaseEmpty() {
        TypedQuery<Long> query = entityManager.createQuery("SELECT COUNT(p) FROM Person p", Long.class);
        Long count = query.getSingleResult();
        return count == 0;
    }
    
    /**
     * Check if an email already exists in the database
     * @param email the email to check
     * @return true if the email already exists
     */
    private boolean emailExists(String email) {
        TypedQuery<Long> query = entityManager.createQuery(
            "SELECT COUNT(c) FROM Contact c WHERE c.email = :email", Long.class);
        query.setParameter("email", email);
        Long count = query.getSingleResult();
        return count > 0;
    }
    
    /**
     * Check if a phone number already exists in the database
     * @param phoneNumber the phone number to check
     * @return true if the phone number already exists
     */
    private boolean phoneNumberExists(String phoneNumber) {
        TypedQuery<Long> query = entityManager.createQuery(
            "SELECT COUNT(c) FROM Contact c WHERE c.phoneNumber = :phoneNumber", Long.class);
        query.setParameter("phoneNumber", phoneNumber);
        Long count = query.getSingleResult();
        return count > 0;
    }

    public Person createPerson(int index) {
        // Create the person with basic info
        Person person = Person.builder()
                .password("password" + (index + 1))
                .firstName(FIRST_NAMES[index])
                .lastName(LAST_NAMES[index])
                .birthDay(LocalDate.of(1980 + index, (index % 12) + 1, (index % 28) + 1))
                .build();

        // Create multiple addresses for each person (1-3 addresses)
        int numAddresses = (index % 3) + 1;
        for (int i = 0; i < numAddresses; i++) {
            Address address = createAddress(person, index, i);
            person.addAddress(address);
        }

        // Create multiple contacts for each person (1-2 contacts)
        int numContacts = (index % 2) + 1;
        for (int i = 0; i < numContacts; i++) {
            Contact contact = createContact(person, index, i);
            if (contact != null) {
                person.addContact(contact);
            }
        }

        // Create multiple roles for each person (1-2 roles)
        int numRoles = (index % 2) + 1;
        for (int i = 0; i < numRoles; i++) {
            Role role = createRole(person, index, i);
            person.addRole(role);
        }

        return person;
    }

    public Address createAddress(Person person, int personIndex, int addressIndex) {
        // Determine address type based on index - only LOCAL and INTERNATIONAL are available
        AddressType type = addressIndex == 0 ? AddressType.LOCAL : AddressType.INTERNATIONAL;
        
        // Create address with data from our sample arrays, with some variations
        Address address = Address.builder()
                .type(type)
                .street(STREETS[personIndex] + (addressIndex > 0 ? ", Suite " + (addressIndex * 100) : ""))
                .postZipCode(ZIP_CODES[personIndex])
                .city(CITIES[personIndex])
                .province(PROVINCES[personIndex])
                .country(COUNTRIES[personIndex])
                .build();

        address.setPerson(person);
        return address;
    }

    public Contact createContact(Person person, int personIndex, int contactIndex) {
        // Determine contact type based on index
        ContactType type = contactIndex == 0 ? ContactType.PERSONAL : ContactType.WORK;
        
        // Format email based on person name and contact type
        String email = FIRST_NAMES[personIndex].toLowerCase() + "." + 
                      LAST_NAMES[personIndex].toLowerCase() + 
                      (contactIndex > 0 ? ".work" : "") + 
                      "@example.com";
        
        // Check if this email already exists in the database
        if (emailExists(email)) {
            log.warn("Email {} already exists, skipping this contact", email);
            return null;
        }
        
        // Generate phone number with variation
        String phoneNumber = PHONE_NUMBERS[personIndex] + (contactIndex > 0 ? "0" : "");
        
        // Check if this phone number already exists in the database
        if (phoneNumberExists(phoneNumber)) {
            log.warn("Phone number {} already exists, skipping this contact", phoneNumber);
            return null;
        }
        
        // Create contact with appropriate data
        Contact contact = Contact.builder()
                .contactType(type)
                .phoneNumber(phoneNumber)
                .email(email)
                .build();

        contact.setPerson(person);
        return contact;
    }

    public Role createRole(Person person, int personIndex, int roleIndex) {
        // Select role based on indices
        String roleName = roleIndex == 0 ? ROLES[personIndex] : "USER";
        
        Role role = Role.builder()
                .role(roleName)
                .build();

        // Create 1-3 permissions for each role
        int permissionsCount = Math.min(PERMISSIONS[personIndex].length, (roleIndex + 1) * 2);
        for (int i = 0; i < permissionsCount; i++) {
            Permissions permission = createPermission(role, personIndex, i);
            role.addPermission(permission);
        }
        
        role.setPerson(person);
        return role;
    }

    public Permissions createPermission(Role role, int personIndex, int permissionIndex) {
        // Select permission based on indices
        String permissionName = permissionIndex < PERMISSIONS[personIndex].length ? 
                               PERMISSIONS[personIndex][permissionIndex] : 
                               "READ_BASIC";
        
        Permissions permission = Permissions.builder()
                .permission(permissionName)
                .build();

        permission.setRole(role);
        return permission;
    }
    
    /**
     * Update metrics gauges with current database counts
     */
    private void updateMetricsGauges() {
        try {
            // Count entities
            long personCount = entityManager.createQuery("SELECT COUNT(p) FROM Person p", Long.class).getSingleResult();
            long addressCount = entityManager.createQuery("SELECT COUNT(a) FROM Address a", Long.class).getSingleResult();
            long contactCount = entityManager.createQuery("SELECT COUNT(c) FROM Contact c", Long.class).getSingleResult();
            long roleCount = entityManager.createQuery("SELECT COUNT(r) FROM Role r", Long.class).getSingleResult();
            
            // Update metrics gauges
            metricsConfig.getActivePersonsGauge().set((int)personCount);
            metricsConfig.getActiveAddressesGauge().set((int)addressCount);
            metricsConfig.getActiveContactsGauge().set((int)contactCount);
            metricsConfig.getActiveRolesGauge().set((int)roleCount);
            
            log.info("Updated metrics gauges - Persons: {}, Addresses: {}, Contacts: {}, Roles: {}", 
                    personCount, addressCount, contactCount, roleCount);
        } catch (Exception e) {
            log.error("Failed to update metrics gauges: {}", e.getMessage(), e);
        }
    }
}
