package spring.cloud.services;

import software.amazon.awssdk.services.cognitoidentityprovider.model.AuthenticationResultType;
import spring.cloud.dtos.LoginRequest;
import spring.cloud.dtos.RegisterUserRequest;
import spring.cloud.dtos.UserRoleDto;

public interface CognitoService {
    AuthenticationResultType login(LoginRequest request);

    AuthenticationResultType refreshToken(String refreshToken);

    UserRoleDto createUser(RegisterUserRequest request);
}
