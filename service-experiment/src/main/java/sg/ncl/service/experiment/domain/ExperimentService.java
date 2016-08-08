package sg.ncl.service.experiment.domain;

import sg.ncl.service.experiment.data.jpa.ExperimentEntity;

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
