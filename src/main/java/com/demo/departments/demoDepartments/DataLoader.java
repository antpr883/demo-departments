package com.demo.departments.demoDepartments;


import com.demo.departments.demoDepartments.persistence.model.*;
import com.demo.departments.demoDepartments.persistence.model.security.Permissions;
import com.demo.departments.demoDepartments.persistence.model.security.Role;
import com.demo.departments.demoDepartments.persistence.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
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


    @Override
    public void run(ApplicationArguments args) throws Exception {

        Person person = createPerson();
        Person savedPerson = personRepository.save(person);
        System.out.println("Saved Person: " + savedPerson);
        Optional<Person> byId = personRepository.findById(1L);
        Person person1 = byId.get();
        Set<Address> addresses = person1.getAddresses();
        String a = "";

    }

    public Person createPerson() {
        Person person = Person.builder()
                .password("password123")
                .firstName("John")
                .lastName("Doe")
                .birthday(LocalDate.of(1990, 1, 1))
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
