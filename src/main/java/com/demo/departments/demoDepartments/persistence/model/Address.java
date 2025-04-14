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
public class Address extends PersistenceModel {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AddressType type;

    @Column(length = 150)
    private String street;

    @Column(length = 20)
    private String postZipCode;

    @Column(name = "province", length = 50)
    private String province;

    @Column(name = "city", length = 50)
    private String city;

    @Column(name = "country", length = 50)
    private String country;


    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "person_id")
    private Person person;

    public void setPerson(Person person) {
        this.person = person;
    }
    
    public void addPerson(Person person) {
        if (person != null) {
            this.person = person;
            if (!person.getAddresses().contains(this)) {
                person.getAddresses().add(this);
            }
        }
    }

    public void removePerson() {
        if (this.person != null) {
            Person tempPerson = this.person;
            this.person = null;
            tempPerson.getAddresses().remove(this);
        }
    }

    public Address(AddressType type, String street, String postZipCode, String province, String city, String country) {
        this.type = type;
        this.street = street;
        this.postZipCode = postZipCode;
        this.province = province;
        this.city = city;
        this.country = country;
    }
}