create table medical_record.diagnoses
(
    id          int auto_increment
        primary key,
    name        varchar(100) not null,
    description text         null
);

create table medical_record.specialties
(
    id   int auto_increment
        primary key,
    name varchar(100) not null,
    constraint name
        unique (name)
);

create table medical_record.users
(
    id         int auto_increment
        primary key,
    username   varchar(50)                         not null,
    password   varchar(255)                        not null,
    role       enum ('DOCTOR', 'PATIENT', 'ADMIN') not null,
    created_at timestamp default CURRENT_TIMESTAMP null,
    name       varchar(100)                        null,
    constraint username
        unique (username)
);

create table medical_record.admin
(
    id           int          not null
        primary key,
    contact_info varchar(255) null,
    constraint admin_ibfk_1
        foreign key (id) references medical_record.users (id)
            on delete cascade
);

create table medical_record.doctor_profile
(
    id    int                  not null
        primary key,
    is_gp tinyint(1) default 0 null,
    constraint fk_doctor_user
        foreign key (id) references medical_record.users (id)
            on delete cascade
);

create table medical_record.doctor_specialties
(
    doctor_id    int not null,
    specialty_id int not null,
    primary key (doctor_id, specialty_id),
    constraint fk_doc_spec_doctor
        foreign key (doctor_id) references medical_record.doctor_profile (id)
            on update cascade on delete cascade,
    constraint fk_doc_spec_specialty
        foreign key (specialty_id) references medical_record.specialties (id)
            on update cascade on delete cascade
);

create table medical_record.patient_profile
(
    id                     int  not null
        primary key,
    last_insurance_payment date null,
    gp_id                  int  not null,
    constraint fk_patient_gp
        foreign key (gp_id) references medical_record.doctor_profile (id),
    constraint fk_patient_user
        foreign key (id) references medical_record.users (id)
            on delete cascade
);

create table medical_record.medical_visits
(
    id         int auto_increment
        primary key,
    patient_id int                                 not null,
    doctor_id  int                                 not null,
    treatment  text                                null,
    visit_date datetime                            not null,
    created_at timestamp default CURRENT_TIMESTAMP null,
    constraint fk_visit_doctor
        foreign key (doctor_id) references medical_record.doctor_profile (id)
            on update cascade on delete cascade,
    constraint fk_visit_patient
        foreign key (patient_id) references medical_record.patient_profile (id)
            on update cascade on delete cascade
);

create table medical_record.medical_visit_diagnoses
(
    medical_visit_id int not null,
    diagnosis_id     int not null,
    primary key (medical_visit_id, diagnosis_id),
    constraint fk_mv_diagnosis_diag
        foreign key (diagnosis_id) references medical_record.diagnoses (id)
            on update cascade on delete cascade,
    constraint fk_mv_diagnosis_visit
        foreign key (medical_visit_id) references medical_record.medical_visits (id)
            on update cascade on delete cascade
);

create table medical_record.sick_leave_records
(
    id               int auto_increment
        primary key,
    medical_visit_id int                                 not null,
    start_date       date                                not null,
    end_date         date                                not null,
    created_at       timestamp default CURRENT_TIMESTAMP null,
    constraint fk_sickleave_visit
        foreign key (medical_visit_id) references medical_record.medical_visits (id)
            on update cascade on delete cascade
);

