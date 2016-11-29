package sg.ncl.service.image.domain;

import java.util.List;
import java.util.Map;

/**
 * Created by dcsyeoty on 28-Oct-16.
 */
public interface ImageService {

    List<Image> getAll(String teamId, ImageVisibility visibility);

    Image getImage(Long id);

    Image addImage(Image image);

    Map<String, String> getSavedImages(String teamId);
}
