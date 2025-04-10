package com.demo.departments.demoDepartments.persistence.model.security;


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
public class Permissions extends PersistenceModel {

    @Column(length = 50, nullable = false)
    private String permission;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "roles_id", nullable = false)
    private Role role;

    public void setRole(Role role) {
        if (role != null) {
            this.role = role;
        }
    }
    public void removeRole(){
        this.role = null;
    }

    public Permissions(String permission) {
        this.permission = permission;
    }
}
