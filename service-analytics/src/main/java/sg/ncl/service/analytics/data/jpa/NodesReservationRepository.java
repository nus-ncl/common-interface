package sg.ncl.service.analytics.data.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.ZonedDateTime;

public interface NodesReservationRepository extends JpaRepository<NodesReservationEntity, Long> {

    @Query("SELECT CASE WHEN count(n) > 0 THEN true ELSE false END FROM NodesReservation n " +
                "WHERE n.projectId = :projectId AND (n.startDate <= :endDate OR n.endDate >= :startDate)")
    boolean existsByOverlappedDates(@Param("projectId") Long projectId,
                                    @Param("startDate") ZonedDateTime startDate,
                                    @Param("endDate") ZonedDateTime endDate);
}
