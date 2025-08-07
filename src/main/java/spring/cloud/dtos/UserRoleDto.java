package spring.cloud.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import spring.cloud.entities.Role;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserRoleDto {
    private Long id;
    private String name;
    private String email;
    private Role role;
}
