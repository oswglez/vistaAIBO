-- Insert test roles for the admin user
-- This script assigns roles to the admin user for testing the role validation

-- First, let's check if the admin user exists and get their ID
-- SELECT user_id FROM users WHERE username = 'admin';

-- Insert roles if they don't exist
INSERT INTO roles (role_name, description, permissions) 
VALUES 
    ('ADMIN', 'Administrator with full access', '["READ", "WRITE", "DELETE", "MANAGE_USERS", "MANAGE_ROLES"]'),
    ('MANAGER', 'Hotel manager with limited administrative access', '["READ", "WRITE", "MANAGE_ROOMS", "VIEW_REPORTS"]'),
    ('STAFF', 'Hotel staff with basic access', '["READ", "WRITE"]'),
    ('VIEWER', 'Read-only access', '["READ"]')
ON CONFLICT (role_name) DO NOTHING;

-- Get the admin user ID (assuming admin user exists)
DO $$
DECLARE
    admin_user_id BIGINT;
    admin_role_id BIGINT;
    manager_role_id BIGINT;
    hotel_id BIGINT;
BEGIN
    -- Get admin user ID
    SELECT user_id INTO admin_user_id FROM users WHERE username = 'admin';
    
    -- Get role IDs
    SELECT role_id INTO admin_role_id FROM roles WHERE role_name = 'ADMIN';
    SELECT role_id INTO manager_role_id FROM roles WHERE role_name = 'MANAGER';
    
    -- Get first hotel ID (assuming at least one hotel exists)
    SELECT hotel_id INTO hotel_id FROM hotels LIMIT 1;
    
    -- If no hotel exists, create a test hotel
    IF hotel_id IS NULL THEN
        INSERT INTO hotels (hotel_name, description, is_active) 
        VALUES ('Test Hotel', 'Test hotel for role validation', true)
        RETURNING hotel_id INTO hotel_id;
    END IF;
    
    -- Assign admin role to admin user for the hotel
    INSERT INTO user_hotel_roles (user_id, hotel_id, role_id, assigned_by, is_active)
    VALUES (admin_user_id, hotel_id, admin_role_id, admin_user_id, true)
    ON CONFLICT (user_id, hotel_id, role_id) DO UPDATE SET 
        is_active = true,
        assigned_at = CURRENT_TIMESTAMP;
    
    -- Also assign manager role for testing multiple roles
    INSERT INTO user_hotel_roles (user_id, hotel_id, role_id, assigned_by, is_active)
    VALUES (admin_user_id, hotel_id, manager_role_id, admin_user_id, true)
    ON CONFLICT (user_id, hotel_id, role_id) DO UPDATE SET 
        is_active = true,
        assigned_at = CURRENT_TIMESTAMP;
    
    RAISE NOTICE 'Roles assigned to admin user (ID: %) for hotel (ID: %)', admin_user_id, hotel_id;
END $$;

-- Verify the assignments
SELECT 
    u.username,
    h.hotel_name,
    r.role_name,
    uhr.is_active,
    uhr.assigned_at
FROM user_hotel_roles uhr
JOIN users u ON uhr.user_id = u.user_id
JOIN hotels h ON uhr.hotel_id = h.hotel_id
JOIN roles r ON uhr.role_id = r.role_id
WHERE u.username = 'admin' AND uhr.is_active = true; 