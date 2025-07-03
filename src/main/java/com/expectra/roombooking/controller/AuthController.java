package com.expectra.roombooking.controller;

import com.expectra.roombooking.dto.UserContextDTO;
import com.expectra.roombooking.dto.UserResponseDTO;
import com.expectra.roombooking.model.User;
import com.expectra.roombooking.repository.RoleRepository;
import com.expectra.roombooking.repository.UserRepository;
import com.expectra.roombooking.service.AuthorizationService;
import com.expectra.roombooking.service.UserContextService;
import com.expectra.roombooking.service.UserHotelRoleService;
import com.expectra.roombooking.service.UserService;
import com.expectra.roombooking.service.OAuth0UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.interfaces.RSAPrivateKey;
import java.security.KeyFactory;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.Date;
import com.expectra.roombooking.util.JwtUtil;
import org.springframework.core.env.Environment;

@RestController
@Data
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Endpoints for OAuth0 user authentication and authorization")
@Slf4j
@CrossOrigin(origins = "*")
public class AuthController {

    private final UserService userService;
    private final AuthorizationService authorizationService;
    private final UserContextService userContextService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserHotelRoleService userHotelRoleService;
    private final OAuth0UserService oAuth0UserService;
    private final Environment env;

    public AuthController(
        UserService userService,
        AuthorizationService authorizationService,
        UserContextService userContextService,
        UserRepository userRepository,
        RoleRepository roleRepository,
        UserHotelRoleService userHotelRoleService,
        OAuth0UserService oAuth0UserService,
        Environment env
    ) {
        this.userService = userService;
        this.authorizationService = authorizationService;
        this.userContextService = userContextService;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userHotelRoleService = userHotelRoleService;
        this.oAuth0UserService = oAuth0UserService;
        this.env = env;
    }

    @GetMapping("/me")
    @Operation(summary = "Get current user", description = "Returns information about the current authenticated user")
    public ResponseEntity<User> getCurrentUser(Authentication authentication) {
        try {
            if (authentication != null && authentication.getPrincipal() instanceof User) {
                User user = (User) authentication.getPrincipal();
                return ResponseEntity.ok(user);
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error getting current user: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/hotels")
    @Operation(summary = "Get user hotels", description = "Returns all hotels associated with the user")
    public ResponseEntity<List<Long>> getUserHotels(Authentication authentication) {
        try {
            if (authentication != null && authentication.getPrincipal() instanceof User) {
                User user = (User) authentication.getPrincipal();
                List<Long> hotelIds = authorizationService.getHotelIdsForUser(user.getUserId());
                return ResponseEntity.ok(hotelIds);
            }
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error("Error getting user hotels: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/test-db")
    @Operation(summary = "Test database connection", description = "Tests if the authentication tables exist and have data")
    public ResponseEntity<String> testDatabase() {
        try {
            log.info("Testing database connection and tables...");
            
            // Count total users
            long userCount = userRepository.count();
            log.info("Total users in database: {}", userCount);
            
            // Count total roles
            long roleCount = roleRepository.count();
            log.info("Total roles in database: {}", roleCount);
            
            String result = String.format("Database test completed. Users: %d, Roles: %d", 
                userCount, roleCount);
            
            log.info(result);
            return ResponseEntity.ok(result);
            
        } catch (Exception e) {
            log.error("Database test failed: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body("Database test failed: " + e.getMessage());
        }
    }

    @GetMapping("/user-context")
    @Operation(summary = "Get user context", description = "Returns user's current context including selected hotel and available roles")
    public ResponseEntity<UserContextDTO> getUserContext(Authentication authentication, @RequestParam(required = false) Long hotelId) {
        try {
            if (authentication != null && authentication.getPrincipal() instanceof User) {
                User user = (User) authentication.getPrincipal();
                log.info("=== Starting user context retrieval for user: {} and hotel: {} ===", user.getUserId(), hotelId);
                
                UserContextDTO context = userContextService.getUserContext(user.getUserId(), hotelId);
                
                log.info("=== User context retrieved successfully for user: {} - Hotel: {}, Roles: {} ===", 
                    user.getUserId(), context.getCurrentHotelId(), context.getCurrentRoles().size());
                
                return ResponseEntity.ok(context);
            }
            return ResponseEntity.badRequest().build();
            
        } catch (IllegalArgumentException e) {
            log.warn("=== Invalid request for user context - Error: {} ===", e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error("=== Error getting user context: {} ===", e.getMessage(), e);
            return ResponseEntity.status(500).build();
        }
    }

    @PostMapping("/switch-hotel")
    @Operation(summary = "Switch hotel context", description = "Switches the user's current hotel context")
    public ResponseEntity<UserContextDTO> switchHotel(Authentication authentication, @RequestParam Long hotelId) {
        try {
            if (authentication != null && authentication.getPrincipal() instanceof User) {
                User user = (User) authentication.getPrincipal();
                log.info("=== Starting hotel switch for user: {} to hotel: {} ===", user.getUserId(), hotelId);
                
                UserContextDTO context = userContextService.getUserContext(user.getUserId(), hotelId);
                
                log.info("=== Hotel switch completed successfully for user: {} - New hotel: {}, Roles: {} ===", 
                    user.getUserId(), context.getCurrentHotelId(), context.getCurrentRoles().size());
                
                return ResponseEntity.ok(context);
            }
            return ResponseEntity.badRequest().build();
            
        } catch (IllegalArgumentException e) {
            log.warn("=== Invalid hotel switch request - Error: {} ===", e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error("=== Error switching hotel context: {} ===", e.getMessage(), e);
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/test-endpoint")
    @Operation(summary = "Test endpoint", description = "Simple test endpoint to verify routing")
    public ResponseEntity<String> testEndpoint() {
        log.info("=== Test endpoint called ===");
        return ResponseEntity.ok("Test endpoint working - " + System.currentTimeMillis());
    }

    @GetMapping("/debug-auth")
    @Operation(summary = "Debug authentication", description = "Temporary endpoint to debug authentication")
    public ResponseEntity<Object> debugAuth(Authentication authentication) {
        log.info("=== Debug auth endpoint called ===");
        
        if (authentication == null) {
            return ResponseEntity.ok(Map.of(
                "message", "No authentication found",
                "timestamp", System.currentTimeMillis()
            ));
        }
        
        if (authentication.getPrincipal() instanceof User) {
            User user = (User) authentication.getPrincipal();
            return ResponseEntity.ok(Map.of(
                "message", "User authenticated successfully",
                "userId", user.getUserId(),
                "username", user.getUsername(),
                "email", user.getEmail(),
                "auth0Id", user.getAuth0Id(),
                "isActive", user.getIsActive(),
                "timestamp", System.currentTimeMillis()
            ));
        }
        
        return ResponseEntity.ok(Map.of(
            "message", "Authentication found but not a User object",
            "principal", authentication.getPrincipal().toString(),
            "timestamp", System.currentTimeMillis()
        ));
    }

    @PostMapping("/login-test")
    @Operation(summary = "Login for testing", description = "Generates a JWT token for testing purposes")
    public ResponseEntity<Map<String, Object>> loginForTesting(@RequestBody Map<String, String> loginRequest) {
        try {
            String email = loginRequest.get("email");
            if (email == null || email.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of(
                    "error", "Email is required",
                    "timestamp", System.currentTimeMillis()
                ));
            }

            log.info("=== Login test requested for email: {} ===", email);

            // Buscar usuario por email
            Optional<User> userOpt = userRepository.findByEmail(email);
            User user;

            if (userOpt.isPresent()) {
                user = userOpt.get();
                log.info("Found existing user: {}", user.getUsername());
            } else {
                // Crear usuario de prueba
                user = new User();
                user.setEmail(email);
                user.setUsername(email.split("@")[0]);
                user.setFirstName("Test");
                user.setLastName("User");
                user.setAuth0Id("test-auth0-id-" + System.currentTimeMillis());
                user.setIsActive(true);
                user = userRepository.save(user);
                log.info("Created test user: {}", user.getUsername());
            }

            // Generar token JWT de prueba
            String privateKeyPath = "vistaAIBO/keys/private_key.pem";
            RSAPrivateKey privateKey = JwtUtil.getPrivateKey(privateKeyPath);
            Algorithm algorithm = Algorithm.RSA256(null, privateKey);

            String token = JWT.create()
                .withKeyId("dev-key-1")
                .withSubject(user.getAuth0Id())
                .withClaim("email", user.getEmail())
                .withClaim("name", user.getFirstName() + " " + user.getLastName())
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + 3600 * 1000))
                .sign(algorithm);

            System.out.println("Token de prueba: " + token);

            return ResponseEntity.ok(Map.of(
                "token", token,
                "tokenType", "Bearer",
                "expiresIn", 3600,
                "user", Map.of(
                    "userId", user.getUserId(),
                    "username", user.getUsername(),
                    "email", user.getEmail(),
                    "firstName", user.getFirstName(),
                    "lastName", user.getLastName(),
                    "isActive", user.getIsActive()
                ),
                "message", "Login successful for testing",
                "timestamp", System.currentTimeMillis()
            ));

        } catch (Exception e) {
            log.error("=== Login test failed: {} ===", e.getMessage(), e);
            return ResponseEntity.status(500).body(Map.of(
                "error", "Login failed: " + e.getMessage(),
                "timestamp", System.currentTimeMillis()
            ));
        }
    }

    @GetMapping("/test-jwt/{userId}")
    public ResponseEntity<?> generateTestJwt(@PathVariable Long userId) {
        try {
            // Obtener los datos reales del usuario
            UserResponseDTO userResponse = userService.getUserById(userId);
            
            String privateKeyPath = "C:/Users/Admin/Expectra/backoffice/vistaAIBO/keys/private_key.pem";
            RSAPrivateKey privateKey = JwtUtil.getPrivateKey(privateKeyPath);
            Algorithm algorithm = Algorithm.RSA256(null, privateKey);

            String token = JWT.create()
                .withKeyId("dev-key-1")
                .withSubject(userId.toString())
                .withClaim("email", userResponse.getEmail())
                .withClaim("name", userResponse.getFirstName() + " " + userResponse.getLastName())
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + 3600 * 1000))
                .sign(algorithm);

            return ResponseEntity.ok(Map.of("token", token));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }
} 