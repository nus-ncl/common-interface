package sg.ncl.service.image.data.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author Christopher Zhong
 */
public interface ImageRepository extends JpaRepository<ImageEntity, Long> {

}
