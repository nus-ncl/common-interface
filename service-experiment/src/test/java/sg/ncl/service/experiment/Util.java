package sg.ncl.service.experiment;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import sg.ncl.service.experiment.data.jpa.ExperimentEntity;
import sg.ncl.service.experiment.data.jpa.ExperimentRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Desmond.
 */
public class Util {

    public static ExperimentEntity getExperimentsEntity() {
        final ExperimentEntity entity = new ExperimentEntity();
        entity.setId(Long.parseLong(RandomStringUtils.randomNumeric(10)));
        entity.setUserId(RandomStringUtils.randomAlphanumeric(20));
        entity.setTeamId(RandomStringUtils.randomAlphanumeric(20));
        entity.setName(RandomStringUtils.randomAlphanumeric(20));
        entity.setDescription(RandomStringUtils.randomAlphanumeric(20));
        entity.setNsFile(RandomStringUtils.randomAlphanumeric(20));
        entity.setIdleSwap(Integer.parseInt(RandomStringUtils.randomNumeric(5)));
        entity.setMaxDuration(Integer.parseInt(RandomStringUtils.randomNumeric(5)));
        return entity;
    }

    public static boolean isListEqual(List<ExperimentEntity> one, List<ExperimentEntity> two) {
        ArrayList<ExperimentEntity> cp = new ArrayList<>(one);
        for (ExperimentEntity twoIterator : two) {
            if (!cp.remove(twoIterator)) {
                return false;
            }
        }
        return cp.isEmpty();
    }

    public static void addExperiments(int numEntries, String userId, ExperimentRepository experimentRepository) {
        for (int i = 0; i < numEntries; i++) {
            ExperimentEntity experimentEntity = Util.getExperimentsEntity();

            if (i % 2 == 0) {
                experimentEntity.setUserId(userId);
            }

            experimentRepository.save(experimentEntity);
        }
    }
}
