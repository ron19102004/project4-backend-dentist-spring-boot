package com.hospital.app.entities.account;

import com.hospital.app.entities.EntityLayout;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
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
    private String slug;
    //Relationships
    @OneToMany(cascade = CascadeType.ALL,mappedBy = "specialize")
    private List<Dentist> dentists;
}
