package sg.ncl.service.data.data.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sg.ncl.service.data.domain.DataVisibility;

import java.util.List;

/**
 * Created by dcszwang on 10/6/2016.
 */
public interface DataRepository extends JpaRepository<DataEntity, Long> {

    List<DataEntity> findByVisibility(DataVisibility visibility);

    List<DataEntity> findByName(String name);

    @Query("SELECT d FROM DataEntity d WHERE d.name LIKE CONCAT('%', :name, '%')")
    List<DataEntity> findDataByName(@Param("name") String name);

    @Query("SELECT d FROM DataEntity d WHERE d.description LIKE CONCAT('%', :description, '%')")
    List<DataEntity> findDataByDescription(@Param("description") String description);

    @Query("SELECT d FROM DataEntity d JOIN d.keywords k WHERE k LIKE CONCAT('%', :keyword, '%')")
    List<DataEntity> findDataByKeyword(@Param("keyword") String keyword);

}
