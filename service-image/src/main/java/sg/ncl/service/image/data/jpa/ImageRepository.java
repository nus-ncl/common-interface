package sg.ncl.service.image.data.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import sg.ncl.service.image.domain.ImageVisibility;

import java.util.List;

/**
 * @authors: Teye, Tran Ly Vu
 */
public interface ImageRepository extends JpaRepository<ImageEntity, Long> {

    List<ImageEntity> findByTeamIdAndVisibility(String teamId, ImageVisibility visibility);

    List<ImageEntity> findByTeamIdOrVisibility(String teamId, ImageVisibility visibility);

    ImageEntity findByTeamIdAndImageName(String teamId, String imageName);
}
