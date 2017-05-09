package sg.ncl.service.data.data.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by dcsjnh on 9/5/2017.
 */
public interface DataCategoryRepository extends JpaRepository<DataCategoryEntity, Long> {

    List<DataCategoryEntity> findByName(String name);

}
