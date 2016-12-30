package sg.ncl.service.analytics.data.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * @author: Tran Ly Vu, James Ng
 *
 * References:
 * [1] http://stackoverflow.com/questions/36328063/how-to-return-a-custom-object-from-a-spring-data-jpa-group-by-query
 * [2] http://stackoverflow.com/questions/10640655/multiple-columns-in-group-by-clause-in-hibernate
 * [3] http://stackoverflow.com/questions/2188186/why-does-hibernate-query-have-compile-error-in-intellij
 */
public interface DataDownloadRepository extends JpaRepository<DataDownloadEntity, Long> {

    @Query(value =
            "SELECT NEW sg.ncl.service.analytics.data.jpa.DataDownloadStatistics(d.dataId, COUNT(DISTINCT d.hashedUserId)) " +
                    "FROM DataDownloadEntity d GROUP BY d.dataId")
    List<DataDownloadStatistics> findDownloadCount();

    @Query(value =
            "SELECT NEW sg.ncl.service.analytics.data.jpa.DataDownloadStatistics(d.dataId, COUNT(DISTINCT d.hashedUserId)) " +
                    "FROM DataDownloadEntity d WHERE d.downloadDate <= :endDate GROUP BY d.dataId")
    List<DataDownloadStatistics> findDownloadCountByDownloadDateBefore(@Param("endDate") ZonedDateTime endDate);

    @Query(value =
            "SELECT NEW sg.ncl.service.analytics.data.jpa.DataDownloadStatistics(d.dataId, COUNT(DISTINCT d.hashedUserId)) " +
                    "FROM DataDownloadEntity d WHERE d.downloadDate >= :startDate GROUP BY d.dataId")
    List<DataDownloadStatistics> findDownloadCountByDownloadDateAfter(@Param("startDate") ZonedDateTime startDate);

    @Query(value =
            "SELECT NEW sg.ncl.service.analytics.data.jpa.DataDownloadStatistics(d.dataId, COUNT(DISTINCT d.hashedUserId)) " +
                    "FROM DataDownloadEntity d WHERE d.dataId = :dataId GROUP BY d.dataId")
    List<DataDownloadStatistics> findDownloadCountByDataId(@Param("dataId") Long dataId);

    @Query(value =
            "SELECT NEW sg.ncl.service.analytics.data.jpa.DataDownloadStatistics(d.dataId, COUNT(DISTINCT d.hashedUserId)) " +
                    "FROM DataDownloadEntity d WHERE d.downloadDate >= :startDate AND d.downloadDate <= :endDate GROUP BY d.dataId")
    List<DataDownloadStatistics> findDownloadCountByDownloadDateBetween(@Param("startDate") ZonedDateTime startDate, @Param("endDate") ZonedDateTime endDate);

    @Query(value =
            "SELECT NEW sg.ncl.service.analytics.data.jpa.DataDownloadStatistics(d.dataId, COUNT(DISTINCT d.hashedUserId)) " +
                    "FROM DataDownloadEntity d WHERE d.dataId = :dataId AND d.downloadDate <= :endDate GROUP BY d.dataId")
    List<DataDownloadStatistics> findDownloadCountByDataIdAndDownloadDateBefore(@Param("dataId") Long dataId, @Param("endDate") ZonedDateTime endDate);

    @Query(value =
            "SELECT NEW sg.ncl.service.analytics.data.jpa.DataDownloadStatistics(d.dataId, COUNT(DISTINCT d.hashedUserId)) " +
                    "FROM DataDownloadEntity d WHERE d.dataId = :dataId AND d.downloadDate >= :startDate GROUP BY d.dataId")
    List<DataDownloadStatistics> findDownloadCountByDataIdAndDownloadDateAfter(@Param("dataId") Long dataId, @Param("startDate") ZonedDateTime startDate);

    @Query(value =
            "SELECT NEW sg.ncl.service.analytics.data.jpa.DataDownloadStatistics(d.dataId, COUNT(DISTINCT d.hashedUserId)) " +
                    "FROM DataDownloadEntity d WHERE d.dataId = :dataId AND d.downloadDate >= :startDate AND d.downloadDate <= :endDate GROUP BY d.dataId")
    List<DataDownloadStatistics> findDownloadCountByDataIdAndDownloadDateBetween(@Param("dataId") Long dataId, @Param("startDate") ZonedDateTime startDate, @Param("endDate") ZonedDateTime endDate);

}
