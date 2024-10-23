package com.hospital.app.entities.blog;

import com.hospital.app.entities.EntityLayout;
import com.hospital.app.entities.account.Dentist;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@Entity
@Table(name = "Blogs")
@AllArgsConstructor
@NoArgsConstructor
public class Blog extends EntityLayout {
    //Attributes
    @Column(columnDefinition = "TEXT")
    private String poster;
    @Column(columnDefinition = "LONGTEXT")
    private String content;
    private String title;
    private String slug;
    @ColumnDefault("FALSE")
    private Boolean isConfirmed;
    //Relationships
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(referencedColumnName = "id",name = "dentistId",nullable = false)
    private Dentist dentist;
}
