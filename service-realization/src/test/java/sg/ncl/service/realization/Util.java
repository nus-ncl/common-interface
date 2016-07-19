package sg.ncl.service.realization;

import org.apache.commons.lang3.RandomStringUtils;
import sg.ncl.service.realization.data.jpa.RealizationEntity;
import sg.ncl.service.realization.data.jpa.RealizationRepository;
import sg.ncl.service.realization.domain.RealizationState;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Desmond.
 */
public class Util {

    public static RealizationEntity getRealizationEntity() {
        final RealizationEntity entity = new RealizationEntity();
        entity.setId(Long.parseLong(RandomStringUtils.randomNumeric(10)));
        entity.setExperimentId(Long.parseLong(RandomStringUtils.randomNumeric(10)));
        entity.setUserId(RandomStringUtils.randomAlphanumeric(20));
        entity.setTeamId(RandomStringUtils.randomAlphanumeric(20));
        entity.setNumberOfNodes(Integer.parseInt(RandomStringUtils.randomNumeric(5)));
        entity.setState(RealizationState.WARNING);
        entity.setIdleMinutes(Long.parseLong(RandomStringUtils.randomNumeric(10)));
        entity.setRunningMinutes(Long.parseLong(RandomStringUtils.randomNumeric(10)));
        return entity;
    }

    public static boolean isListEqual(List<RealizationEntity> one, List<RealizationEntity> two) {
        ArrayList<RealizationEntity> cp = new ArrayList<>(one);
        for (RealizationEntity twoIterator : two) {
            if (!cp.remove(twoIterator)) {
                return false;
            }
        }
        return cp.isEmpty();
    }

    public static void addExperiments(int numEntries, String userId, RealizationRepository realizationRepository) {
        for (int i = 0; i < numEntries; i++) {
            RealizationEntity realizationEntity = Util.getRealizationEntity();

            if (i % 2 == 0) {
                realizationEntity.setUserId(userId);
            }

            realizationRepository.save(realizationEntity);
        }
    }
}
