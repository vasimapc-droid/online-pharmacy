INSERT INTO users (full_name, email, password, role, phone, specialty, license_number, pharmacy_name, address, created_at) VALUES
    ('Admin User', 'admin@pharmacy.com', '$2a$10$KYBZhuCfs9/Fbe..Jz3YFeJb7127FNKl8MAXi9XFHCVhp3sY/AA02', 'ADMIN', '9876543210', NULL, NULL, NULL, NULL, NOW());

INSERT INTO users (full_name, email, password, role, phone, specialty, license_number, pharmacy_name, address, created_at) VALUES
    ('Dr. Sharma', 'doctor@pharmacy.com', '$2a$10$UK02mSbaTwgtsMs7HR7.EOfrzMwNy33JAcV1e9N7dzOzF1FhPsAti', 'DOCTOR', '9876543211', 'General Medicine', 'MED12345', NULL, NULL, NOW());

INSERT INTO users (full_name, email, password, role, phone, specialty, license_number, pharmacy_name, address, created_at) VALUES
    ('City Pharmacy', 'pharmacy@pharmacy.com', '$2a$10$p.ROk6jid8MK4TtWnKHeBujzpzK..fYpltmqGUG9.p2I0uoXHhXeG', 'PHARMACY', '9876543212', NULL, NULL, 'City Pharmacy Store', '123 Main Street, Mumbai', NOW());

INSERT INTO users (full_name, email, password, role, phone, created_at) VALUES
    ('Rahul Patient', 'patient@pharmacy.com', '$2a$10$FKoAlY2kTFsw/4eAO8e2HOknOHRt2RSJlamwIO7yGoFnb11J8/vLu', 'PATIENT', '9876543213', NOW());

INSERT INTO users (full_name, email, password, role, phone, created_at) VALUES
    ('Priya Patient', 'priya@pharmacy.com', '$2a$10$IOYpua/JpjgH6MfV1KD3oeRMA/Cw3QiCSVcLYlyOoolCJYEgjF.Bq', 'PATIENT', '9876543214', NOW());