DELETE FROM medical_record.sick_leave_records;
DELETE FROM medical_record.medical_visit_diagnoses;
DELETE FROM medical_record.medical_visits;
DELETE FROM medical_record.patient_profile;
DELETE FROM medical_record.doctor_specialties;
DELETE FROM medical_record.doctor_profile;
DELETE FROM medical_record.admin;
DELETE FROM medical_record.users;
DELETE FROM medical_record.diagnoses;
DELETE FROM medical_record.specialties;

INSERT INTO medical_record.specialties (id, name) VALUES
                                                      (1, 'Cardiology'),
                                                      (2, 'Neurology'),
                                                      (3, 'Pediatrics'),
                                                      (4, 'Dermatology'),
                                                      (5, 'Orthopedics');

# password is password123 for all users
INSERT INTO medical_record.users (id, username, password, role, created_at, name) VALUES
                                                                                      (1, 'doc1', '$2a$12$GO8Efp6wL13aOaKDXPtwGeBCvUyiC6poekJ4uwvO.Qj2SIol9G89y', 'DOCTOR', CURRENT_TIMESTAMP, 'Dr. Alice'),
                                                                                      (2, 'doc2', '$2a$12$GO8Efp6wL13aOaKDXPtwGeBCvUyiC6poekJ4uwvO.Qj2SIol9G89y', 'DOCTOR', CURRENT_TIMESTAMP, 'Dr. Bob'),
                                                                                      (3, 'pat1', '$2a$12$GO8Efp6wL13aOaKDXPtwGeBCvUyiC6poekJ4uwvO.Qj2SIol9G89y', 'PATIENT', CURRENT_TIMESTAMP, 'John Smith'),
                                                                                      (4, 'pat2', '$2a$12$GO8Efp6wL13aOaKDXPtwGeBCvUyiC6poekJ4uwvO.Qj2SIol9G89y', 'PATIENT', CURRENT_TIMESTAMP, 'Jane Doe'),
                                                                                      (5, 'admin1', '$2a$12$GO8Efp6wL13aOaKDXPtwGeBCvUyiC6poekJ4uwvO.Qj2SIol9G89y', 'ADMIN', CURRENT_TIMESTAMP, 'Super Admin');

INSERT INTO medical_record.admin (id, contact_info) VALUES
    (5, 'admin@hospital.org');

INSERT INTO medical_record.doctor_profile (id, is_gp) VALUES
                                                          (1, 1), -- GP
                                                          (2, 0);

INSERT INTO medical_record.doctor_specialties (doctor_id, specialty_id) VALUES
                                                                            (1, 1),
                                                                            (1, 3),
                                                                            (2, 2),
                                                                            (2, 4);

INSERT INTO medical_record.patient_profile (id, last_insurance_payment, gp_id) VALUES
                                                                                   (3, '2024-12-01', 1),
                                                                                   (4, '2024-11-15', 1);

INSERT INTO medical_record.diagnoses (id, name, description) VALUES
                                                                 (1, 'Flu', 'Influenza virus infection'),
                                                                 (2, 'Sprained Ankle', 'Twisting injury'),
                                                                 (3, 'Migraine', 'Chronic headaches'),
                                                                 (4, 'Eczema', 'Skin inflammation');

INSERT INTO medical_record.medical_visits (id, patient_id, doctor_id, treatment, visit_date, created_at) VALUES
                                                                                                             (1, 3, 1, 'Rest and fluids', NOW(), CURRENT_TIMESTAMP),
                                                                                                             (2, 4, 2, 'Pain relief meds', NOW(), CURRENT_TIMESTAMP);

INSERT INTO medical_record.medical_visit_diagnoses (medical_visit_id, diagnosis_id) VALUES
                                                                                        (1, 1),
                                                                                        (1, 3),
                                                                                        (2, 2);

INSERT INTO medical_record.sick_leave_records (medical_visit_id, start_date, end_date, created_at) VALUES
                                                                                                       (1, '2025-04-01', '2025-04-05', CURRENT_TIMESTAMP),
                                                                                                       (2, '2025-04-03', '2025-04-07', CURRENT_TIMESTAMP);
