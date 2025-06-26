-- Verify and assign roles to admin user
-- This script ensures the admin user has proper roles assigned

-- First, let's check the current state
SELECT 'Current state check:' as info;

-- Check if admin user exists
SELECT 'Admin user exists:' as check_type, 
       CASE WHEN EXISTS(SELECT 1 FROM users WHERE username = 'admin') 
            THEN 'YES' ELSE 'NO' END as result;

-- Check if roles exist
SELECT 'Roles exist:' as check_type, 
       CASE WHEN EXISTS(SELECT 1 FROM roles) 
            THEN 'YES' ELSE 'NO' END as result;

-- Check if hotels exist
SELECT 'Hotels exist:' as check_type, 
       CASE WHEN EXISTS(SELECT 1 FROM hotels) 
            THEN 'YES' ELSE 'NO' END as result;

-- Check current admin roles
SELECT 'Current admin roles:' as info;
SELECT 
    u.username,
    h.hotel_name,
    r.role_name,
    uhr.is_active,
    uhr.assigned_at
FROM users u
LEFT JOIN user_hotel_roles uhr ON u.user_id = uhr.user_id
LEFT JOIN hotels h ON uhr.hotel_id = h.hotel_id
LEFT JOIN roles r ON uhr.role_id = r.role_id
WHERE u.username = 'admin';

-- Insert roles if they don't exist
INSERT INTO roles (role_name, description, permissions) 
VALUES 
    ('ADMIN', 'Administrator with full access', '["READ", "WRITE", "DELETE", "MANAGE_USERS", "MANAGE_ROLES"]'),
    ('MANAGER', 'Hotel manager with limited administrative access', '["READ", "WRITE", "MANAGE_ROOMS", "VIEW_REPORTS"]'),
    ('STAFF', 'Hotel staff with basic access', '["READ", "WRITE"]'),
    ('VIEWER', 'Read-only access', '["READ"]')
ON CONFLICT (role_name) DO NOTHING;

-- Create a test hotel if none exists
INSERT INTO hotels (hotel_name, description, is_active) 
VALUES ('Test Hotel', 'Test hotel for role validation', true)
ON CONFLICT DO NOTHING;

-- Assign admin role to admin user
DO $$
DECLARE
    v_admin_user_id BIGINT;
    v_admin_role_id BIGINT;
    v_hotel_id BIGINT;
BEGIN
    -- Obtener el ID del usuario admin
    SELECT user_id INTO v_admin_user_id FROM users WHERE username = 'admin';
    
    -- Obtener el ID del rol admin
    SELECT role_id INTO v_admin_role_id FROM roles WHERE role_name = 'ADMIN';
    
    -- Obtener el ID del primer hotel
    SELECT hotel_id INTO v_hotel_id FROM hotel LIMIT 1;
    
    -- Asignar el rol admin al usuario admin para el hotel
    INSERT INTO user_hotel_roles (user_id, hotel_id, role_id, assigned_by, is_active)
    VALUES (v_admin_user_id, v_hotel_id, v_admin_role_id, v_admin_user_id, true)
    ON CONFLICT (user_id, hotel_id, role_id) DO UPDATE SET 
        is_active = EXCLUDED.is_active,
        assigned_at = CURRENT_TIMESTAMP;
    
    RAISE NOTICE 'Admin role assigned to user % for hotel %', v_admin_user_id, v_hotel_id;
END $$;

-- Final verification
SELECT 'Final verification:' as info;
SELECT 
    u.username,
    h.hotel_name,
    r.role_name,
    uhr.is_active,
    uhr.assigned_at
FROM users u
JOIN user_hotel_roles uhr ON u.user_id = uhr.user_id
JOIN hotels h ON uhr.hotel_id = h.hotel_id
JOIN roles r ON uhr.role_id = r.role_id
WHERE u.username = 'admin' AND uhr.is_active = true; 