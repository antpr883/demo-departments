package com.demo.departments.demoDepartments.persistence.model.security;


import com.demo.departments.demoDepartments.persistence.model.Person;
import com.demo.departments.demoDepartments.persistence.model.base.PersistenceModel;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;

@Data
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Role extends PersistenceModel {

    @Column(unique = true, length = 50, nullable = false)
    private String role;

    @Builder.Default
    @ToString.Exclude
    @OneToMany(mappedBy = "role", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private Set<Permissions> permissions = new HashSet<>();

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "person_id")
    private Person person;

    public void setPermission(Permissions permission){
        if (permission != null) {
            this.permissions.add(permission);
            permission.setRole(this);
        }
    }
    public void removePermission(Permissions permission){
        this.permissions.remove(permission);
        permission.setRole(null);
    }

    public void removePerson() {
        if (this.person != null) {
            Person tempPerson = this.person;
            this.person = null;
            tempPerson.getRoles().remove(this);
        }
    }
}