package sg.ncl.service.experiment.util;

import org.apache.commons.lang3.RandomStringUtils;
import sg.ncl.service.experiment.data.jpa.ExperimentEntity;

/**
 * @author Te Ye
 */
public class TestUtil {

    public static ExperimentEntity getExperimentEntity() {
        final ExperimentEntity entity = new ExperimentEntity();
        entity.setId(Long.parseLong(RandomStringUtils.randomNumeric(10)));
        entity.setUserId(RandomStringUtils.randomAlphanumeric(20));
        entity.setTeamId(RandomStringUtils.randomAlphanumeric(20));
        entity.setTeamName(RandomStringUtils.randomAlphanumeric(20));
        entity.setName(RandomStringUtils.randomAlphanumeric(19));
        entity.setDescription(RandomStringUtils.randomAlphanumeric(20));
        entity.setNsFile(RandomStringUtils.randomAlphanumeric(20));
        entity.setNsFileContent(createNsFileContents());
        entity.setIdleSwap(Integer.parseInt(RandomStringUtils.randomNumeric(5)));
        entity.setMaxDuration(Integer.parseInt(RandomStringUtils.randomNumeric(5)));
        return entity;
    }

    public static String createNsFileContents() {
        StringBuilder sb = new StringBuilder();
        sb.append("set ns [new Simulator]\n");
        sb.append("source tb_compat.tcl\n");
        sb.append("set n0 [$ns node]\n");
        sb.append("\n");
        sb.append("$ns Static\n");
        sb.append("$ns run\n");
        return sb.toString();
    }

}
