package sg.ncl.service.image.domain;

import io.jsonwebtoken.Claims;

import java.util.List;
import java.util.Map;

/**
 * @Authors: dcsyeoty, Tran Ly Vu
 */
public interface ImageService {

    List<Image> getAll(String teamId, ImageVisibility visibility);

    Image getImage(Long id);

    Image addImage(Image image, Claims claims);

    Image deleteImage(Image image, Claims claims);

    Map<String, String> getSavedImages(String teamId);

    Map<String, String> getGlobalImages();
}
