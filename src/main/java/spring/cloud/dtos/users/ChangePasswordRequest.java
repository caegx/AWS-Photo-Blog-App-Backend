package spring.cloud.dtos.users;

public record ChangePasswordRequest (
    String oldPassword,
    String newPassword
){
}
