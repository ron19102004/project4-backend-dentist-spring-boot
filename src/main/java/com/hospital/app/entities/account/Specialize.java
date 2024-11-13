package com.hospital.app.entities.account;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
    @Column(nullable = false)
    private String name;
    @Column(unique = true, nullable = false)
    private String slug;
    @Column(columnDefinition = "LONGTEXT")
    private String description;
    //Relationships
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "specialize", fetch = FetchType.LAZY)
    private List<Dentist> dentists;
}
