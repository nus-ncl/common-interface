package sg.ncl.service.analytics.data.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectDetailsRepository extends JpaRepository<ProjectDetailsEntity, Long> {

    List<ProjectDetailsEntity> findByProjectName(String projectName);
}
