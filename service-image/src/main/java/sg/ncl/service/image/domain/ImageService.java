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

    String removeImage(String teamId, String imageName, Claims claims);

    Map<String, String> getSavedImages(String teamId);

    Map<String, String> getGlobalImages();
}
