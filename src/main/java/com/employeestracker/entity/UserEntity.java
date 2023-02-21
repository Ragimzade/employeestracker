package com.employeestracker.entity;


import lombok.*;

import javax.persistence.*;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "serial", updatable = false, nullable = false)
    private Long id;

    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;

    @Column(name = "email", nullable = false, length = 120)
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "job_title")
    private String jobTitle;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private DepartmentEntity department;

    @OneToMany(mappedBy = "users", fetch = FetchType.EAGER)
    private List<ProjectPositionEntity> projectPositions;

    public String getFullName() {
        return "%s %s".formatted(this.firstName, this.lastName);
    }
}
