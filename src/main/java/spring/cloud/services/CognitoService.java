package spring.cloud.services;

import software.amazon.awssdk.services.cognitoidentityprovider.model.AuthenticationResultType;
import spring.cloud.dtos.users.LoginRequest;
import spring.cloud.dtos.users.RegisterUserRequest;
import spring.cloud.dtos.users.UserRoleDto;
import spring.cloud.entities.User;

public interface CognitoService {
    AuthenticationResultType login(LoginRequest request);

    AuthenticationResultType refreshToken(String refreshToken);

    UserRoleDto createUser(RegisterUserRequest request);

    User getCurrentUser();
}
