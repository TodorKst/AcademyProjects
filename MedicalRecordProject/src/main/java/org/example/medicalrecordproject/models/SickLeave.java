package org.example.medicalrecordproject.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.sql.Date;
import java.sql.Timestamp;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "sick_leave_records", schema = "medical_record")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SickLeave extends BaseEntity {

    @NotNull(message = "Start date is required")
    @Column(name = "start_date", nullable = false)
    private Date startDate;

    @NotNull(message = "End date is required")
    @Column(name = "end_date", nullable = false)
    private Date endDate;

    @Column(name = "created_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp createdAt;

    @NotNull(message = "Medical visit is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medical_visit_id")
    @JsonManagedReference
    private MedicalVisit medicalVisit;
}
