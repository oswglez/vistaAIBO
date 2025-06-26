package com.expectra.roombooking.controller;

import com.expectra.roombooking.dto.AuthResponseDTO;
import com.expectra.roombooking.dto.LoginRequestDTO;
import com.expectra.roombooking.dto.UserContextDTO;
import com.expectra.roombooking.model.User;
import com.expectra.roombooking.repository.RoleRepository;
import com.expectra.roombooking.repository.UserRepository;
import com.expectra.roombooking.service.AuthorizationService;
import com.expectra.roombooking.service.AuthService;
import com.expectra.roombooking.service.UserService;
import com.expectra.roombooking.service.UserContextService;
import com.expectra.roombooking.service.UserHotelRoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Endpoints for user authentication and authorization")
@Slf4j
@CrossOrigin(origins = "*")
public class AuthController {

    private final UserService userService;
    private final AuthService authService;
    private final AuthorizationService authorizationService;
    private final UserContextService userContextService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserHotelRoleService userHotelRoleService;

  //  @Autowired
    public AuthController(
        UserService userService,
        AuthService authService,
        AuthorizationService authorizationService,
        UserContextService userContextService,
        UserRepository userRepository,
        RoleRepository roleRepository,
        PasswordEncoder passwordEncoder,
        UserHotelRoleService userHotelRoleService
    ) {
        this.userService = userService;
        this.authService = authService;
        this.authorizationService = authorizationService;
        this.userContextService = userContextService;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.userHotelRoleService = userHotelRoleService;
    }

    @PostMapping("/login")
    @Operation(summary = "User login", description = "Authenticates a user and returns a token")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody LoginRequestDTO loginRequest) {
        log.info("=== Starting login process for user: {} ===", loginRequest.getUsername());
        
        try {
            log.debug("Validating login request for user: {}", loginRequest.getUsername());
            
            // Validate that fields are not empty
            if (loginRequest.getUsername() == null || loginRequest.getUsername().trim().isEmpty()) {
                log.warn("=== Login failed: username is null or empty ===");
                return ResponseEntity.badRequest().build();
            }
            
            if (loginRequest.getPassword() == null || loginRequest.getPassword().trim().isEmpty()) {
                log.warn("=== Login failed: password is null or empty ===");
                return ResponseEntity.badRequest().build();
            }
            
            log.debug("Login request validation passed for user: {}", loginRequest.getUsername());
            
            // Use the new AuthService that handles errors better
            AuthResponseDTO response = authService.authenticate(loginRequest);
            
            log.info("=== Login successful for user: {} with {} roles ===", 
                loginRequest.getUsername(), response.getUserHotelRoles().size());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("=== Login failed for user {}: {} ===", loginRequest.getUsername(), e.getMessage(), e);
            // AuthService throws AuthenticationException which will be handled by GlobalExceptionHandler
            throw e;
        }
    }

    @GetMapping("/me")
    @Operation(summary = "Get current user", description = "Returns information about the current authenticated user")
    public ResponseEntity<User> getCurrentUser(@RequestParam Long userId) {
        try {
            User user = userService.getUserById(userId);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            log.error("Error getting current user: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/hotels")
    @Operation(summary = "Get user hotels", description = "Returns all hotels associated with the user")
    public ResponseEntity<List<Long>> getUserHotels(@RequestParam Long userId) {
        try {
            List<Long> hotelIds = authorizationService.getHotelIdsForUser(userId);
            return ResponseEntity.ok(hotelIds);
        } catch (Exception e) {
            log.error("Error getting user hotels: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/logout")
    @Operation(summary = "User logout", description = "Logs out the current user")
    public ResponseEntity<String> logout(@RequestParam Long userId) {
        try {
            userService.updateLastLogin(userId);
            return ResponseEntity.ok("Logged out successfully");
        } catch (Exception e) {
            log.error("Logout error: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/test-db")
    @Operation(summary = "Test database connection", description = "Tests if the authentication tables exist and have data")
    public ResponseEntity<String> testDatabase() {
        try {
            log.info("Testing database connection and tables...");
            
            // Check if admin user exists
            boolean adminExists = userRepository.findByUsername("admin").isPresent();
            log.info("Admin user exists: {}", adminExists);
            
            // Count total users
            long userCount = userRepository.count();
            log.info("Total users in database: {}", userCount);
            
            // Count total roles
            long roleCount = roleRepository.count();
            log.info("Total roles in database: {}", roleCount);
            
            String result = String.format("Database test completed. Users: %d, Roles: %d, Admin exists: %s", 
                userCount, roleCount, adminExists);
            
            log.info(result);
            return ResponseEntity.ok(result);
            
        } catch (Exception e) {
            log.error("Database test failed: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body("Database test failed: " + e.getMessage());
        }
    }

    @PostMapping("/validate-credentials")
    @Operation(summary = "Validate credentials", description = "Validates user credentials without performing full authentication")
    public ResponseEntity<String> validateCredentials(@RequestBody LoginRequestDTO loginRequest) {
        try {
            log.info("=== Starting credential validation for user: {} ===", loginRequest.getUsername());
            
            // Find user
            log.debug("Searching for user in database: {}", loginRequest.getUsername());
            var userOpt = userRepository.findByUsername(loginRequest.getUsername());
            
            if (userOpt.isEmpty()) {
                log.warn("User not found in database: {}", loginRequest.getUsername());
                return ResponseEntity.ok("User not found");
            }
            
            User user = userOpt.get();
            log.info("User found - ID: {}, Active: {}", user.getUserId(), user.getIsActive());
            
            // Check active status
            if (!user.getIsActive()) {
                log.warn("User account is inactive: {}", loginRequest.getUsername());
                return ResponseEntity.ok("User inactive");
            }
            
            // Verify password
            log.debug("Verifying password for user: {}", loginRequest.getUsername());
            boolean passwordMatches = passwordEncoder.matches(loginRequest.getPassword(), user.getPasswordHash());
            
            if (!passwordMatches) {
                log.warn("Password verification failed for user: {}", loginRequest.getUsername());
                return ResponseEntity.ok("Incorrect password");
            }
            
            log.info("Password verified successfully for user: {}", loginRequest.getUsername());
            
            // Check if user has active roles
            log.debug("Checking active roles for user ID: {}", user.getUserId());
            List<Long> userHotelIds = authorizationService.getHotelIdsForUser(user.getUserId());
            
            if (userHotelIds.isEmpty()) {
                log.warn("User has no active roles: {}", loginRequest.getUsername());
                return ResponseEntity.ok("Valid credentials but no active roles assigned");
            }
            
            log.info("=== Credential validation completed for user: {} - Has {} active role(s) ===", 
                loginRequest.getUsername(), userHotelIds.size());
            
            return ResponseEntity.ok("Valid credentials with " + userHotelIds.size() + " active role(s)");
            
        } catch (Exception e) {
            log.error("=== Error validating credentials for {}: {} ===", 
                loginRequest.getUsername(), e.getMessage(), e);
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/generate-hash")
    @Operation(summary = "Generate password hash", description = "Generates a BCrypt hash for a given password")
    public ResponseEntity<String> generateHash(@RequestParam String password) {
        try {
            String hash = authService.generatePasswordHash(password);
            log.info("Generated hash for password '{}': {}", password, hash);
            return ResponseEntity.ok("Hash: " + hash);
        } catch (Exception e) {
            log.error("Error generating hash: {}", e.getMessage());
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/user-context")
    @Operation(summary = "Get user context", description = "Returns user's current context including selected hotel and available roles")
    public ResponseEntity<UserContextDTO> getUserContext(@RequestParam Long userId, @RequestParam(required = false) Long hotelId) {
        try {
            log.info("=== Starting user context retrieval for user: {} and hotel: {} ===", userId, hotelId);
            
            UserContextDTO context = userContextService.getUserContext(userId, hotelId);
            
            log.info("=== User context retrieved successfully for user: {} - Hotel: {}, Roles: {} ===", 
                userId, context.getCurrentHotelId(), context.getCurrentRoles().size());
            
            return ResponseEntity.ok(context);
            
        } catch (IllegalArgumentException e) {
            log.warn("=== Invalid request for user context - User: {}, Error: {} ===", userId, e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error("=== Error getting user context for user {}: {} ===", userId, e.getMessage(), e);
            return ResponseEntity.status(500).build();
        }
    }

    @PostMapping("/switch-hotel")
    @Operation(summary = "Switch hotel context", description = "Switches the user's current hotel context")
    public ResponseEntity<UserContextDTO> switchHotel(@RequestParam Long userId, @RequestParam Long hotelId) {
        try {
            log.info("=== Starting hotel switch for user: {} to hotel: {} ===", userId, hotelId);
            
            UserContextDTO context = userContextService.getUserContext(userId, hotelId);
            
            log.info("=== Hotel switch completed successfully for user: {} - New hotel: {}, Roles: {} ===", 
                userId, context.getCurrentHotelId(), context.getCurrentRoles().size());
            
            return ResponseEntity.ok(context);
            
        } catch (IllegalArgumentException e) {
            log.warn("=== Invalid hotel switch request - User: {}, Hotel: {}, Error: {} ===", 
                userId, hotelId, e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error("=== Error switching hotel context for user {} to hotel {}: {} ===", 
                userId, hotelId, e.getMessage(), e);
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/check-user-roles")
    @Operation(summary = "Check user roles", description = "Checks if a user has any active roles assigned")
    public ResponseEntity<Object> checkUserRoles(@RequestParam String username) {
        try {
            log.info("=== Starting role check for user: {} ===", username);
            
            // Find user
            log.debug("Searching for user in database: {}", username);
            var userOpt = userRepository.findByUsername(username);
            
            if (userOpt.isEmpty()) {
                log.warn("User not found in database: {}", username);
                return ResponseEntity.ok(Map.of(
                    "username", username,
                    "userExists", false,
                    "hasRoles", false,
                    "message", "User not found"
                ));
            }
            
            User user = userOpt.get();
            log.info("User found - ID: {}, Active: {}", user.getUserId(), user.getIsActive());
            
            // Check if user is active
            if (!user.getIsActive()) {
                log.warn("User account is inactive: {}", username);
                return ResponseEntity.ok(Map.of(
                    "username", username,
                    "userExists", true,
                    "userActive", false,
                    "hasRoles", false,
                    "message", "User account is inactive"
                ));
            }
            
            // Get user's active roles
            log.debug("Checking active roles for user ID: {}", user.getUserId());
            List<Long> userHotelIds = authorizationService.getHotelIdsForUser(user.getUserId());
            log.info("Found {} hotels with active roles for user: {}", userHotelIds.size(), username);
            
            Map<String, Object> response = Map.of(
                "username", username,
                "userExists", true,
                "userActive", true,
                "hasRoles", !userHotelIds.isEmpty(),
                "hotelCount", userHotelIds.size(),
                "hotels", userHotelIds,
                "message", userHotelIds.isEmpty() ? 
                    "User has no active roles" : 
                    "User has " + userHotelIds.size() + " hotel(s) with active roles"
            );
            
            log.info("=== Role check completed for user: {} - Has roles: {} ===", 
                username, !userHotelIds.isEmpty());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("=== Error checking user roles for {}: {} ===", username, e.getMessage(), e);
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/test-endpoint")
    @Operation(summary = "Test endpoint", description = "Simple test endpoint to verify routing")
    public ResponseEntity<String> testEndpoint() {
        log.info("=== Test endpoint called ===");
        return ResponseEntity.ok("Test endpoint working - " + System.currentTimeMillis());
    }
} 