package sg.ncl.service.image.logic;

import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sg.ncl.adapter.deterlab.AdapterDeterLab;
import sg.ncl.service.image.data.jpa.ImageEntity;
import sg.ncl.service.image.data.jpa.ImageRepository;
import sg.ncl.service.image.domain.Image;
import sg.ncl.service.image.domain.ImageService;
import sg.ncl.service.image.domain.ImageVisibility;
import sg.ncl.service.team.domain.TeamService;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.stream.Collectors;

import static sg.ncl.common.validation.Validator.checkAdmin;

/**
 * @authors:  Teye, Tran Ly Vu.
 */
@Service
@Slf4j
public class ImageServiceImpl implements ImageService {

    private final AdapterDeterLab adapterDeterLab;
    private final ImageRepository imageRepository;
    private final TeamService teamService;
    @Inject
    ImageServiceImpl(@NotNull final AdapterDeterLab adapterDeterLab,
                     @NotNull ImageRepository imageRepository,
                     @NotNull TeamService teamService) {
        this.adapterDeterLab = adapterDeterLab;
        this.imageRepository = imageRepository;
        this.teamService = teamService;
    }

    /**
     * Get the list of saved images
     * 1st case: get all images in the system
     * 2nd case: get all images that the team shares
     * 3rd case: get all images either shared by the team or by the visibility
     * @param teamId ncl team id
     * @param visibility visibility of the image
     * @return list of saved images
     */
    @Transactional
    @Override
    public List<Image> getAll(String teamId, ImageVisibility visibility) {
        log.info("Getting list of saved images for team: {}, with visibility: {}", teamId, visibility);
        if (teamId == null && visibility == null) {
            return imageRepository.findAll().stream().collect(Collectors.toList());
        }
        if (teamId != null && visibility != null) {
            return imageRepository.findByTeamIdAndVisibility(teamId, visibility).stream().collect(Collectors.toList());
        }
        return imageRepository.findByTeamIdOrVisibility(teamId, visibility).stream().collect(Collectors.toList());
    }

    @Transactional
    @Override
    public Image getImage(Long id) {
        if (id == null) {
            return null;
        }
        return imageRepository.findOne(id);
    }

    @Override
    public Map<String, String> getGlobalImages() {
        Map<String, String> globalImage = new HashMap<>();
        globalImage.put("images", adapterDeterLab.getGlobalImages());
        return globalImage;
    }

    @Override
    public Map<String, String> getSavedImages(String teamId) {
        Map<String, String> result = new HashMap<>();
        result.put(teamId, adapterDeterLab.getSavedImages(teamId));
        return result;
    }

    @Transactional
    @Override
    public Image addImage(Image image, Claims claims) {
        final ImageEntity entity = new ImageEntity();
        entity.setImageName(image.getImageName());
        entity.setDescription(image.getDescription());
        entity.setNodeId(image.getNodeId());
        entity.setTeamId(image.getTeamId());
        entity.setVisibility(image.getVisibility());
        entity.setCurrentOS(image.getCurrentOS());
        final ImageEntity saved = imageRepository.save(entity);

        adapterDeterLab.saveImage(image.getTeamId(), claims.getSubject(), image.getNodeId(), image.getImageName());

        log.info("Image created: {}", saved);
        return saved;
    }

    @Override
    public String removeImage(String teamId, String imageName, Claims claims) {

        boolean imageCreator = true;
        if (teamService.isOwner(teamId, claims.getSubject()) || checkAdmin(claims)) {
            imageCreator = false;
        }

        String userId = claims.getSubject();
        String output = adapterDeterLab.deleteImage(teamId, userId, imageName, imageCreator);

        log.info("Deleting image '{}' from team '{}': Deleting image entity from sio database", imageName, teamId);
        String deterMessage = new JSONObject(output).getString("msg");

        Set<String> success = new HashSet<>();
        success.add("delete image OK from both web and project directory");
        success.add("delete image OK from web but there is unknown error when deleting physical image");

        if (deterMessage != null && success.contains(deterMessage)) {
            ImageEntity image = imageRepository.findByTeamIdAndImageName(teamId, imageName);
            if (image != null) {
                log.info("Deleting image '{}' from team '{}': delete image from sio database", imageName, teamId);
                imageRepository.delete(image);
            }
        }

        return output;
    }
}
