package com.hospital.app.entities.account;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hospital.app.entities.EntityLayout;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@Entity
@Table(name = "Specializes")
@AllArgsConstructor
@NoArgsConstructor
public class Specialize extends EntityLayout {
    //Attributes
    private String name;
    @Column(unique = true, nullable = false)
    private String slug;
    private byte[] description;
    //Relationships
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "specialize")
    private List<Dentist> dentists;
}
