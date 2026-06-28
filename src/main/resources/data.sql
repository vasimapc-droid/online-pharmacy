INSERT INTO users (full_name, email, password, role, phone, specialty, license_number, pharmacy_name, address, created_at) VALUES
    ('Admin User', 'admin@pharmacy.com', 'admin123', 'ADMIN', '9876543210', NULL, NULL, NULL, NULL, NOW());

INSERT INTO users (full_name, email, password, role, phone, specialty, license_number, pharmacy_name, address, created_at) VALUES
    ('Dr. Sharma', 'doctor@pharmacy.com', 'doctor123', 'DOCTOR', '9876543211', 'General Medicine', 'MED12345', NULL, NULL, NOW());

INSERT INTO users (full_name, email, password, role, phone, specialty, license_number, pharmacy_name, address, created_at) VALUES
    ('City Pharmacy', 'pharmacy@pharmacy.com', 'pharmacy123', 'PHARMACY', '9876543212', NULL, NULL, 'City Pharmacy Store', '123 Main Street, Mumbai', NOW());

INSERT INTO users (full_name, email, password, role, phone, specialty, license_number, pharmacy_name, address, created_at) VALUES
    ('Rahul Patient', 'patient@pharmacy.com', 'patient123', 'PATIENT', '9876543213', NULL, NULL, NULL, NULL, NOW());

INSERT INTO users (full_name, email, password, role, phone, specialty, license_number, pharmacy_name, address, created_at) VALUES
    ('Priya Patient', 'priya@pharmacy.com', 'patient123', 'PATIENT', '9876543214', NULL, NULL, NULL, NULL, NOW());