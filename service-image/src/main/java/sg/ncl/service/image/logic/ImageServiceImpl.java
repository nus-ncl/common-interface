package sg.ncl.service.image.logic;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sg.ncl.adapter.deterlab.AdapterDeterLab;
import sg.ncl.service.image.data.jpa.ImageEntity;
import sg.ncl.service.image.data.jpa.ImageRepository;
import sg.ncl.service.image.domain.Image;
import sg.ncl.service.image.domain.ImageService;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by dcsyeoty on 28-Oct-16.
 */
@Service
@Slf4j
public class ImageServiceImpl implements ImageService {

    private final AdapterDeterLab adapterDeterLab;
    private final ImageRepository imageRepository;

    @Inject
    ImageServiceImpl(@NotNull final AdapterDeterLab adapterDeterLab, @NotNull ImageRepository imageRepository) {
        this.adapterDeterLab = adapterDeterLab;
        this.imageRepository = imageRepository;
    }

    @Transactional
    @Override
    public List<Image> getAll() {
        return imageRepository.findAll().stream().collect(Collectors.toList());
    }

    @Transactional
    @Override
    public Image getImage(Long id) {
        if (id == null) {
            return null;
        }
        return imageRepository.findOne(id);
    }

    @Transactional
    @Override
    public Image addImage(Image image) {
        final ImageEntity entity = new ImageEntity();
        entity.setImageName(image.getImageName());
        entity.setDescription(image.getDescription());
        entity.setNodeId(image.getNodeId());
        entity.setTeamId(image.getTeamId());
        entity.setVisibility(image.getVisibility());
        final ImageEntity saved = imageRepository.save(entity);
        log.info("Image created: {}", saved);
        return saved;
    }
}
