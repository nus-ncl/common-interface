package sg.ncl.service.experiment.logic;

import org.springframework.stereotype.Service;
import sg.ncl.service.experiment.data.jpa.ExperimentEntity;
import sg.ncl.service.experiment.data.jpa.ExperimentRepository;
import sg.ncl.service.experiment.domain.Experiment;
import sg.ncl.service.experiment.exceptions.UserIdNotFound;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Desmond.
 */
@Service
public class ExperimentService {

    private final ExperimentRepository experimentRepository;

    @Inject
    protected ExperimentService(final ExperimentRepository experimentRepository) {
        this.experimentRepository = experimentRepository;
    }

    public ExperimentEntity save(Experiment experiment) {
        ExperimentEntity experimentEntity = new ExperimentEntity();

        experimentEntity.setUserId(experiment.getUserId());
        experimentEntity.setTeamId(experiment.getTeamId());
        experimentEntity.setName(experiment.getName());
        experimentEntity.setDescription(experiment.getDescription());
        experimentEntity.setNsFile(experiment.getNsFile());
        experimentEntity.setIdleSwap(experiment.getIdleSwap());
        experimentEntity.setMaxDuration(experiment.getMaxDuration());

        return experimentRepository.save(experimentEntity);
    }

    public List<ExperimentEntity> get() {
        final List<ExperimentEntity> result = new ArrayList<>();
        for (ExperimentEntity experimentEntity : experimentRepository.findAll()) {
            result.add(experimentEntity);
        }
        return result;
    }

    public List<ExperimentEntity> findByUser(String userId) {
        if (userId == null || userId.isEmpty()) {
            throw new UserIdNotFound();
        }

        List<ExperimentEntity> result = experimentRepository.findByUserId(userId);
        return result;
    }
}
