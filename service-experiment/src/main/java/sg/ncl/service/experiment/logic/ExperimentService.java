package sg.ncl.service.experiment.logic;

import sg.ncl.service.experiment.data.jpa.ExperimentEntity;
import sg.ncl.service.experiment.domain.Experiment;

import java.util.List;

/**
 * Created by Desmond.
 */
public interface ExperimentService {

    public ExperimentEntity save(Experiment experiment);

    public List<ExperimentEntity> get();

    public List<ExperimentEntity> findByUser(String userId);

    public List<ExperimentEntity> findByTeam(String teamId);

//    public String createNsFile(String filename, String contents);

    public void createExperimentInDeter(ExperimentEntity experimentEntity);

    public String deleteExperiment(final Long id);

}
