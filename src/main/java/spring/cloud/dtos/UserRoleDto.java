package spring.cloud.dtos;

import spring.cloud.entities.Role;


public record UserRoleDto (
    String name,
    String email,
    Role role
){
}
