package com.hospital.core.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;


import java.util.Date;

@Data
@MappedSuperclass
public abstract class EntityLayout {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Date createdAt;
    @JsonIgnore
    @Temporal(TemporalType.TIMESTAMP)
    private Date  deletedAt;
}
