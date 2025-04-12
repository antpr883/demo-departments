package com.demo.departments.demoDepartments;


import com.cosium.spring.data.jpa.entity.graph.domain2.DynamicEntityGraph;
import com.cosium.spring.data.jpa.entity.graph.domain2.EntityGraph;
import com.demo.departments.demoDepartments.persistence.model.*;
import com.demo.departments.demoDepartments.persistence.model.security.Permissions;
import com.demo.departments.demoDepartments.persistence.model.security.Role;
import com.demo.departments.demoDepartments.persistence.repository.*;
import com.demo.departments.demoDepartments.service.PersonService;
import com.demo.departments.demoDepartments.service.dto.PersonDTO;
import com.demo.departments.demoDepartments.service.impl.PersonServiceImpl;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.processing.Find;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;


@RequiredArgsConstructor
@Component
@Transactional
public class DataLoader implements ApplicationRunner {

    private final RoleRepository roleRepository;
    private final PersonRepository personRepository;
    private final ContactRepository contactRepository;
    private final AddressRepository addressRepository;
    private final PermissionsRepository permissionsRepository;
   private final PersonService personService;

    @Override
    public void run(ApplicationArguments args) throws Exception {

//        Person person = createPerson();
//        Person savedPerson = personRepository.save(person);
//        System.out.println("Saved Person: " + savedPerson);

//        Optional<Person> findById = personService.findById(1L);
//


        Map<String, String> params = new HashMap<>();
        params.put("dto_fields", "id,firstName,contacts,roles,addresses");
        params.put("graph_fields", "contacts,roles.permissions,addresses");

        PersonDTO dto = personService.findByIdFull(1L, params);
        String a = "";



    }

    public Person createPerson() {
        Person person = Person.builder()
                .password("password123")
                .firstName("John")
                .lastName("Doe")
                .birthDay(LocalDate.of(1990, 1, 1))
                .build();

        Address address = createAddress(person);
        Contact contact = createContact(person);
        Role role = createRole(person);

        person.setAddress(address);
        person.setContact(contact);
        person.addRole(role);

        return person;
    }

    public Address createAddress(Person person) {
        Address address = Address.builder()
                .type(AddressType.LOCAL)
                .street("123 Main St")
                .postZipCode("12345")
                .province("Province")
                .province("City")
                .country("Country")
                .build();

        address.setPerson(person);
        return address;
    }

    public Contact createContact(Person person) {
        Contact contact = Contact.builder()
                .contactType(ContactType.PERSONAL)
                .phoneNumber("123-456-7890")
                .email("john.doe@example.com")
                .build();

        contact.setPerson(person);
        return contact;
    }

    public Role createRole(Person person) {
        Role role = Role.builder()
                .role("USER")
                .build();

        Permissions permission = createPermission(role);
        role.setPermission(permission);
        role.setPerson(person);

        return role;
    }

    public Permissions createPermission(Role role) {
        Permissions permission = Permissions.builder()
                .permission("READ_PRIVILEGES")
                .build();

        permission.setRole(role);
        return permission;
    }
}
