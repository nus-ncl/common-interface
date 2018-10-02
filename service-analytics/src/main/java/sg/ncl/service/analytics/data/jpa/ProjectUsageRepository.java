package sg.ncl.service.analytics.data.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectUsageRepository extends JpaRepository<ProjectUsageEntity, ProjectUsageIdentity> {

}
