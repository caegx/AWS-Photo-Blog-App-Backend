package spring.cloud.dtos;

public record ChangePasswordRequest (
    String oldPassword,
    String newPassword
){
}
