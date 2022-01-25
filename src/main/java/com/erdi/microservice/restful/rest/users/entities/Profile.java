package com.erdi.microservice.restful.rest.users.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="profiles")
@Data
@NoArgsConstructor
public class Profile {

    public static final long USER = 1;
    public static final long ADMINISTRATOR = 2;

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO, generator="native")
    @GenericGenerator(name = "native", strategy = "native")
    @Column(name="id")
    private Long id;

    @Column(name="profile", nullable = false)
    private String profile;

    public Profile(Long id, String profile) {
        this.id = id;
        this.profile = profile;
    }

    @ManyToMany(fetch=FetchType.EAGER)
    @JoinTable(name = "permissions_profiles",
            joinColumns = @JoinColumn(name = "profile_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id"))
    private List<Permission> permissions= new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Profile )) return false;
        return id != null && id.equals(((Profile) o).getId());
    }

    @Override
    public int hashCode() {
        return 31;
    }

}
