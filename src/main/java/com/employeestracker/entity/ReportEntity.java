package com.employeestracker.entity;

import com.employeestracker.dto.report.ReportType;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.ZonedDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "reports")
public class ReportEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name")
    private String name;

    @CreationTimestamp
    @Column(name = "created")
    private ZonedDateTime created;

    @Column(name = "type", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private ReportType type;

    @Column(name = "file_data", nullable = false, columnDefinition = "bytea")
    private byte[] data;
}
