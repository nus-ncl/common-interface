package sg.ncl.service.analytics.data.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.ZonedDateTime;
import java.util.List;

public interface DataPublicDownloadRepository extends JpaRepository<DataPublicDownloadEntity, Long> {

    @Query(value =
            "SELECT NEW sg.ncl.service.analytics.data.jpa.DataDownloadStatistics(d.dataId, COUNT(DISTINCT d.publicUserId)) " +
                    "FROM DataPublicDownloadEntity d GROUP BY d.dataId")
    List<DataDownloadStatistics> findDownloadCount();

    @Query(value =
            "SELECT NEW sg.ncl.service.analytics.data.jpa.DataDownloadStatistics(d.dataId, COUNT(DISTINCT d.publicUserId)) " +
                    "FROM DataPublicDownloadEntity d WHERE d.downloadDate <= :endDate GROUP BY d.dataId")
    List<DataDownloadStatistics> findDownloadCountByDownloadDateBefore(@Param("endDate") ZonedDateTime endDate);

    @Query(value =
            "SELECT NEW sg.ncl.service.analytics.data.jpa.DataDownloadStatistics(d.dataId, COUNT(DISTINCT d.publicUserId)) " +
                    "FROM DataPublicDownloadEntity d WHERE d.downloadDate >= :startDate GROUP BY d.dataId")
    List<DataDownloadStatistics> findDownloadCountByDownloadDateAfter(@Param("startDate") ZonedDateTime startDate);

    @Query(value =
            "SELECT NEW sg.ncl.service.analytics.data.jpa.DataDownloadStatistics(d.dataId, COUNT(DISTINCT d.publicUserId)) " +
                    "FROM DataPublicDownloadEntity d WHERE d.dataId = :dataId GROUP BY d.dataId")
    List<DataDownloadStatistics> findDownloadCountByDataId(@Param("dataId") Long dataId);

    @Query(value =
            "SELECT NEW sg.ncl.service.analytics.data.jpa.DataDownloadStatistics(d.dataId, COUNT(DISTINCT d.publicUserId)) " +
                    "FROM DataPublicDownloadEntity d WHERE d.downloadDate >= :startDate AND d.downloadDate <= :endDate GROUP BY d.dataId")
    List<DataDownloadStatistics> findDownloadCountByDownloadDateBetween(@Param("startDate") ZonedDateTime startDate, @Param("endDate") ZonedDateTime endDate);

    @Query(value =
            "SELECT NEW sg.ncl.service.analytics.data.jpa.DataDownloadStatistics(d.dataId, COUNT(DISTINCT d.publicUserId)) " +
                    "FROM DataPublicDownloadEntity d WHERE d.dataId = :dataId AND d.downloadDate <= :endDate GROUP BY d.dataId")
    List<DataDownloadStatistics> findDownloadCountByDataIdAndDownloadDateBefore(@Param("dataId") Long dataId, @Param("endDate") ZonedDateTime endDate);

    @Query(value =
            "SELECT NEW sg.ncl.service.analytics.data.jpa.DataDownloadStatistics(d.dataId, COUNT(DISTINCT d.publicUserId)) " +
                    "FROM DataPublicDownloadEntity d WHERE d.dataId = :dataId AND d.downloadDate >= :startDate GROUP BY d.dataId")
    List<DataDownloadStatistics> findDownloadCountByDataIdAndDownloadDateAfter(@Param("dataId") Long dataId, @Param("startDate") ZonedDateTime startDate);

    @Query(value =
            "SELECT NEW sg.ncl.service.analytics.data.jpa.DataDownloadStatistics(d.dataId, COUNT(DISTINCT d.publicUserId)) " +
                    "FROM DataPublicDownloadEntity d WHERE d.dataId = :dataId AND d.downloadDate >= :startDate AND d.downloadDate <= :endDate GROUP BY d.dataId")
    List<DataDownloadStatistics> findDownloadCountByDataIdAndDownloadDateBetween(@Param("dataId") Long dataId, @Param("startDate") ZonedDateTime startDate, @Param("endDate") ZonedDateTime endDate);

}
