package spring.cloud.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import spring.cloud.dtos.RegisterUserRequest;
import spring.cloud.dtos.UserRoleDto;
import spring.cloud.service.CognitoService;

@RestController
@AllArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final CognitoService cognitoService;

    @PostMapping
    public ResponseEntity<UserRoleDto> registerUser(
            @Valid @RequestBody RegisterUserRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body( cognitoService.createUser(request));
    }
}
