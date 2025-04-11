package com.demo.departments.demoDepartments.persistence.model;


import com.demo.departments.demoDepartments.persistence.model.base.PersistenceModel;
import com.demo.departments.demoDepartments.persistence.model.security.Role;
import com.demo.departments.demoDepartments.persistence.utils.mapping.MappingAttribute;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Person extends PersistenceModel {

    @ToString.Exclude
    @Column(length = 16)
    private String password;

    @Column(name = "first_name", length = 50)
    private String firstName;

    @Column(name = "last_name", length = 50)
    private String lastName;

    @Column(name = "birthday")
    private LocalDate birthday;

    @ToString.Exclude
    @Builder.Default
    @MappingAttribute(
            path = "addresses",
            targetEntity = Address.class,
            description = "Person addresses"
    )
    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Address> addresses = new HashSet<>();

    @ToString.Exclude
    @Builder.Default
    @MappingAttribute(
            path = "contacts",
            targetEntity = Contact.class,
            description = "Person contacts"
    )
    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Contact> contacts = new HashSet<>();

    @ToString.Exclude
    @Builder.Default
    @MappingAttribute(
            path = "roles",
            withSubAttributes = true,
            targetEntity = Role.class,
            description = "Roles for current person"
    )
    @OneToMany(mappedBy = "person", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private Set<Role> roles = new HashSet<>();


    public void addRole(Role role) {
        if (role != null) {
            this.roles.add(role);
            role.setPerson(this);
        }
    }
    
    public void addRoles(Set<Role> roles) {
        this.roles.clear();
        if (roles != null) {
            for (Role role : roles) {
                addRole(role);
            }
        }
    }

    public void clearRoles(){
        for (Role role : roles) {
            role.setPerson(null);
        }
        this.roles.clear();
    }

    public void removeRole(Role role) {
        this.roles.remove(role);
        role.setPerson(null);
    }

    public void setAddress(Address address) {
        if (address != null) {
            this.addresses.add(address);
            address.addPerson(this);
        }
    }

    public void removeAddress(Address address){
        this.addresses.remove(address);
        address.addPerson(null);
    }

    public void setContact(Contact contact) {
        if (contact != null) {
            this.contacts.add(contact);
            contact.addPerson(this);
        }
    }

    public void removeContact(Contact contact) {
        this.contacts.remove(contact);
        contact.addPerson(null);
    }

    public Person(String password, String firstName, String lastName, LocalDate birthday) {
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthday = birthday;
    }
}
