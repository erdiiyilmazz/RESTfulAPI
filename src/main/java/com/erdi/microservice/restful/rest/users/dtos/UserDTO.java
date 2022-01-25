package com.erdi.microservice.restful.rest.users.dtos;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.erdi.microservice.restful.rest.users.entities.Permission;
import com.erdi.microservice.restful.rest.users.entities.Profile;
import com.erdi.microservice.restful.rest.users.entities.User;

@Data
public class UserDTO implements Serializable {
	
    private Long id;
    private String username;
    private String name;
    private String surname;
    private String gender;
    private java.time.LocalDate birthDate;

    private boolean enabled;

    private LocalDateTime creationDt;
    private LocalDateTime updatedDt;
    private LocalDateTime loginDt;

    private boolean secured;

    private ContactDTO contactDTO;
    private AddressDTO addressDTO;

    // permissions and profiles list
    private List<String> profiles;
    private List<String> permissions;

    public UserDTO() {
        // empty constructor
        profiles = new ArrayList<>();
        permissions = new ArrayList<>();
    }

    public UserDTO(User user) {
        if (user != null) {
            this.id = user.getId();
            this.username = user.getUsername();
            this.name = user.getName();
            this.surname = user.getSurname();
            this.gender = user.getGender().name();
            this.birthDate = user.getBirthDate();
            this.enabled = user.isEnabled();
            this.creationDt = user.getCreationDt();
            this.updatedDt = user.getUpdatedDt();
            this.loginDt = user.getLoginDt();
            this.secured = user.isSecured();

            // contact, if set
            if (user.getContact() != null) {
                this.contactDTO = new ContactDTO(user.getContact());
            }

            // address, if set
            if (user.getAddress() != null) {
                this.addressDTO = new AddressDTO(user.getAddress());
            }

            // Because the permissions can be associated to more than one profiles i'm creating two String arrays
            // with the distinct keys of profiles and permissions.
            profiles = new ArrayList<>();
            permissions = new ArrayList<>();

            for (Profile profile : user.getProfiles()) {
                profiles.add(profile.getProfile());
                for (Permission p : profile.getPermissions()) {
                    String key = p.getPermission();
                    if ((!permissions.contains(key)) && (p.isEnabled())) {
                        // check if permission key exists
                        permissions.add(key);
                    }
                }
            }

        }
    }
}
