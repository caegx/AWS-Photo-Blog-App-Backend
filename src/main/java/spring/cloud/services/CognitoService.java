package spring.cloud.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.*;
import spring.cloud.config.CognitoConfig;
import spring.cloud.dtos.LoginRequest;
import spring.cloud.dtos.RegisterUserRequest;
import spring.cloud.dtos.UserRoleDto;
import spring.cloud.entities.Role;
import spring.cloud.mappers.UserMapper;
import spring.cloud.repositories.UserRepository;

import java.util.Map;

@Service
@AllArgsConstructor
public class CognitoService {
    private final CognitoIdentityProviderClient cognitoClient;
    private final CognitoConfig cognitoConfig;
    private final UserMapper userMapper;
    private final UserRepository userRepository;

    public AuthenticationResultType login(LoginRequest request) {
        var authRequest = AdminInitiateAuthRequest.builder()
                .userPoolId(cognitoConfig.getUserPoolId())
                .clientId(cognitoConfig.getClientId())
                .authFlow(AuthFlowType.ADMIN_NO_SRP_AUTH)
                .authParameters(Map.of(
                        "USERNAME", request.email(),
                        "PASSWORD", request.password()
                ))
                .build();

        var response = cognitoClient.adminInitiateAuth(authRequest);

        return response.authenticationResult();

    }

    public AuthenticationResultType refreshToken(String refreshToken) {
        var authRequest = AdminInitiateAuthRequest.builder()
                .userPoolId(cognitoConfig.getUserPoolId())
                .clientId(cognitoConfig.getClientId())
                .authFlow(AuthFlowType.REFRESH_TOKEN_AUTH)
                .authParameters(Map.of("REFRESH_TOKEN", refreshToken))
                .build();

        var response = cognitoClient.adminInitiateAuth(authRequest);
        return response.authenticationResult();

    }

    @Transactional
    public UserRoleDto createUser(RegisterUserRequest request) {
        var createUserRequest = AdminCreateUserRequest.builder()
                .userPoolId(cognitoConfig.getUserPoolId())
                .username(request.email())
                .userAttributes(
                        AttributeType.builder()
                                .name("email")
                                .value(request.email())
                                .build(),
                        AttributeType.builder()
                                .name("name")
                                .value(request.name())
                                .build(),
                        AttributeType.builder()
                                .name("email_verified")
                                .value("true")
                                .build()
                )
                .temporaryPassword(request.password())
                .messageAction(MessageActionType.SUPPRESS)
                .build();

        var createResponse = cognitoClient.adminCreateUser(createUserRequest);
        String userId = createResponse.user().username();

        var setPasswordRequest = AdminSetUserPasswordRequest.builder()
                .userPoolId(cognitoConfig.getUserPoolId())
                .username(userId)
                .password(request.password())
                .permanent(true)
                .build();

        cognitoClient.adminSetUserPassword(setPasswordRequest);

        var addToGroupRequest = AdminAddUserToGroupRequest.builder()
                .userPoolId(cognitoConfig.getUserPoolId())
                .username(userId)
                .groupName(Role.USER.name())
                .build();

        cognitoClient.adminAddUserToGroup(addToGroupRequest);

        var user = userMapper.toUser(request);
        userRepository.save(user);

        return new UserRoleDto(request.name(), request.email(), Role.USER);
    }
}
