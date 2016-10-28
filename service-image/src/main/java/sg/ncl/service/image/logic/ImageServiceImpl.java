package sg.ncl.service.image.logic;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sg.ncl.adapter.deterlab.AdapterDeterLab;
import sg.ncl.service.image.domain.ImageService;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;

/**
 * Created by dcsyeoty on 28-Oct-16.
 */
@Service
@Slf4j
public class ImageServiceImpl implements ImageService {

    private final AdapterDeterLab adapterDeterLab;

    @Inject
    ImageServiceImpl(@NotNull final AdapterDeterLab adapterDeterLab) {
        this.adapterDeterLab = adapterDeterLab;
    }

    @Transactional
    @Override
    public String getAll() {
        // TODO call adapter deterlab
        return "";
    }

    @Transactional
    @Override
    public String getImage() {
        return null;
    }

    @Transactional
    @Override
    public String addImage() {
        return null;
    }
}
