package sg.ncl.service.analytics.data.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.ZonedDateTime;
import java.util.List;

public interface NodesReservationRepository extends JpaRepository<NodesReservationEntity, Long> {

    @Query(value = "SELECT CASE WHEN count(n) > 0 THEN true ELSE false END FROM NodesReservationEntity n " +
                   "WHERE n.projectId = :projectId AND NOT (n.startDate > :endDate OR n.endDate < :startDate)")
    boolean existsByOverlappedDates(@Param("projectId") Long projectId,
                                    @Param("startDate") ZonedDateTime startDate,
                                    @Param("endDate") ZonedDateTime endDate);

    @Query(value = "SELECT NEW sg.ncl.service.analytics.data.jpa.NodesReservationEntry(p.projectName, n.startDate, n.endDate, n.noNodes) " +
                    "FROM NodesReservationEntity n, ProjectDetailsEntity p WHERE n.projectId = p.id " +
                    "AND NOT (n.startDate > :endDate OR n.endDate < :startDate)")
    List<NodesReservationEntry> findNodesReservationOverlappedDates(@Param("startDate") ZonedDateTime startDate,
                                                                    @Param("endDate") ZonedDateTime endDate);

    @Query(value = "SELECT NEW sg.ncl.service.analytics.data.jpa.NodeUsageEntry(n.id, n.startDate, n.endDate, n.noNodes) " +
                    "FROM NodesReservationEntity n WHERE n.projectId = :projectId " +
                    "AND (n.endDate > :currentDate)")
    List<NodeUsageEntry> getNodesReserveByProject(@Param("projectId") Long projectId,
                                                  @Param("currentDate") ZonedDateTime currentDate);

    @Query(value = "SELECT CASE WHEN count(n) > 0 THEN true ELSE false END FROM NodesReservationEntity n " +
                    "WHERE n.projectId = :projectId AND NOT (n.startDate > :endDate OR n.endDate < :startDate  ) AND (n.id <> :reserveId)")
    boolean existsByOverlappedDatesNotSameReservId(@Param("projectId") Long projectId,
                                                @Param("reserveId") Long reserveId,
                                                @Param("startDate") ZonedDateTime startDate,
                                                @Param("endDate") ZonedDateTime endDate);
}
