package com.employeestracker.datajpa;

import com.employeestracker.entity.DepartmentEntity;
import com.employeestracker.entity.ProjectEntity;
import com.employeestracker.entity.ProjectPositionEntity;
import com.employeestracker.entity.UserEntity;
import com.employeestracker.repository.DepartmentRepository;
import com.employeestracker.repository.ProjectPositionRepository;
import com.employeestracker.repository.ProjectRepository;
import com.employeestracker.repository.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:tc:postgresql:11.1:///integration-tests-db",
        "spring.datasource.driver-class-name=org.testcontainers.jdbc.ContainerDatabaseDriver",
        "spring.jpa.hibernate.ddl-auto=create",
        "spring.liquibase.enabled=false"
})
class RepositoryTest {

    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private ProjectPositionRepository positionRepository;


    @BeforeAll
    void setUpData() {
        DepartmentEntity departmentEntity = new DepartmentEntity(1L, "java");
        ProjectEntity projectEntity = new ProjectEntity(
                1L,
                "test",
                LocalDate.now().minusDays(10),
                null,
                null);

        UserEntity userEntity = new UserEntity(
                1L,
                "test",
                "test",
                "test@email",
                "test",
                "test",
                departmentEntity,
                null);

        ProjectPositionEntity positionEntity = new ProjectPositionEntity(1L,
                LocalDate.now().minusDays(13),
                LocalDate.now().plusDays(5),
                "test",
                "test",
                projectEntity,
                userEntity);

        projectRepository.save(projectEntity);
        departmentRepository.save(departmentEntity);
        userRepository.save(userEntity);
        positionRepository.save(positionEntity);
    }

    @Test
    void findAvailableUsers_one_user_available() {
        List<UserEntity> availableUsers = userRepository.findAvailableUsers(LocalDate.now(), LocalDate.now().plusDays(10));
        assertEquals(1, availableUsers.size());
    }


    @Test
    void findAvailableUsers_no_available_users() {
        List<UserEntity> availableUsers = userRepository.findAvailableUsers(LocalDate.now(), LocalDate.now().plusDays(4));
        assertEquals(0, availableUsers.size());
    }

    @Test
    void findActiveProjectPositionByUserId_one_active_project_position() {
        ProjectPositionEntity activeProjectPosition = positionRepository.findActiveProjectPositionByUserId(1L, LocalDate.now().plusDays(4));
        assertNotNull(activeProjectPosition);
    }

    @Test
    void findActiveProjectPositionByUserId_no_active_project_position() {
        ProjectPositionEntity activeProjectPosition = positionRepository.findActiveProjectPositionByUserId(1L, LocalDate.now().plusDays(30));
        assertNull(activeProjectPosition);
    }


}
