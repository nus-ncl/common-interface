package sg.ncl.service.experiment.logic;

import sg.ncl.service.experiment.data.jpa.ExperimentEntity;
import sg.ncl.service.experiment.domain.Experiment;

import java.util.List;

/**
 * Created by Desmond.
 */
public interface ExperimentService {

    ExperimentEntity save(Experiment experiment);

    List<ExperimentEntity> get();

    List<ExperimentEntity> findByUser(String userId);

    List<ExperimentEntity> findByTeam(String teamId);

//  String createNsFile(String filename, String contents);

    void createExperimentInDeter(ExperimentEntity experimentEntity);

    String deleteExperiment(final Long id);

}
