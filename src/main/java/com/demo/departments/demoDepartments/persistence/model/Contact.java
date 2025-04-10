package com.demo.departments.demoDepartments.persistence.model;

import com.demo.departments.demoDepartments.persistence.model.base.PersistenceModel;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Contact extends PersistenceModel {

    @Enumerated(EnumType.STRING)
    @Column
    private ContactType contactType;

    @EqualsAndHashCode.Include
    @Column(unique = true, length = 30)
    private String phoneNumber;

    @EqualsAndHashCode.Include
    @Column(unique = true, length = 50)
    private String email;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "person_id")
    private Person person;

    public void addPerson(Person person) {
        if (person != null) {
            this.person = person;
            person.getContacts().add(this);
        }
    }

    public void removePerson(Person person) {
        this.person = null;
        person.getContacts().remove(this);
    }

    public Contact(ContactType contactType, String phoneNumber, String email){
        this.contactType = contactType;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }
}