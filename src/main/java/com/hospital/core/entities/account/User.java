package com.hospital.core.entities.account;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hospital.core.entities.EntityLayout;
import com.hospital.core.entities.reward.RewardPoint;
import com.hospital.core.entities.work.Appointment;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;
import java.util.List;


@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@Entity
@Table(name = "Users")
@AllArgsConstructor
@NoArgsConstructor
public class User extends EntityLayout implements UserDetails {
    //Attributes
    @Column(unique = true,nullable = false)
    private String username;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String fullName;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    @Column(unique = true,nullable = false)
    private String phoneNumber;
    @Column(nullable = false)
    private String address;
    @Column(unique = true)
    private String email;
    @Enumerated(EnumType.STRING)
    @JsonIgnore
    private Role role;
    @Column(columnDefinition = "LONGTEXT")
    @JsonIgnore
    private String tokenResetPassword;
    @ColumnDefault("FALSE")
    private boolean isActiveTwoFactorAuthentication;
    @JsonIgnore
    private String codeTwoFactorAuthentication;
    @JsonIgnore
    @Temporal(TemporalType.TIMESTAMP)
    private Date codeTFAExpirationAt;
    //Relationships
    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "user", orphanRemoval = true)
    private Accountant accountant;
    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "user", orphanRemoval = true)
    private Dentist dentist;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "user", orphanRemoval = true)
    private RewardPoint rewardPoint;
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<Appointment> appointments;
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<Token> tokens;

    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(this.role.name()));
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return true;
    }

    @JsonIgnore
    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }
}
