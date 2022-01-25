package com.erdi.microservice.restful.rest.users.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.erdi.microservice.restful.rest.users.entities.Profile;

@NoArgsConstructor
@Data
public class ProfileDTO implements Serializable {

    private Long id;
    private String profile;

    private List<PermissionDTO> permissions = new ArrayList<>();

    public ProfileDTO(Profile profile) {
        this.id = profile.getId();
        this.profile = profile.getProfile();

        // permissions
        profile.getPermissions().stream().forEach(e -> permissions.add(new PermissionDTO(e)));
    }

    public ProfileDTO(Long id, String profile) {
        this.id = id;
        this.profile = profile;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProfileDTO)) return false;
        return id != null && id.equals(((ProfileDTO) o).getId());
    }

    @Override
    public int hashCode() {
        return 31;
    }

}
