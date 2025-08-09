package spring.cloud.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import software.amazon.awssdk.awscore.exception.AwsServiceException;
import spring.cloud.dtos.JwtResponse;
import spring.cloud.dtos.LoginRequest;
import spring.cloud.service.CognitoService;

@RestController
@AllArgsConstructor
@RequestMapping("/auth")
public class AuthController {


    private final CognitoService cognitoService;

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(
            @Valid @RequestBody LoginRequest request,
            HttpServletResponse response
    ) {
        var authResult = cognitoService.login(request);

        var cookie = new Cookie("refreshToken", authResult.refreshToken());
        cookie.setHttpOnly(true);
        cookie.setPath("/auth/refresh");
        cookie.setMaxAge(30 * 24 * 60 * 60);
        cookie.setSecure(true);
        response.addCookie(cookie);

        return ResponseEntity.ok(new JwtResponse(authResult.accessToken()));
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtResponse> refresh(
            @CookieValue(value = "refreshToken") String refreshToken
    ) {
        if (refreshToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        var authResult = cognitoService.refreshToken(refreshToken);
        return ResponseEntity.ok(new JwtResponse(authResult.accessToken()));
    }

    @ExceptionHandler(AwsServiceException.class)
    public ResponseEntity<Void> handleAwsServiceException() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
