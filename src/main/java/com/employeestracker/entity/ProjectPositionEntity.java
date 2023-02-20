package com.employeestracker.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "project_positions")
public class ProjectPositionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "position_start_date")
    private LocalDate startDate;

    @Column(name = "position_end_date")
    private LocalDate endDate;

    @Column(name = "occupation", nullable = false)
    private String occupation;

    @Column(name = "position_title", nullable = false)
    private String positionTitle;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private ProjectEntity projects;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity users;

    @PreRemove
    public void preRemove() {
        this.users.getProjectPositions().clear();
        this.users = null;
    }
}
