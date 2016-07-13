package sg.ncl.service.experiment.logic;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sg.ncl.adapter.deterlab.AdapterDeterlab;
import sg.ncl.adapter.deterlab.ConnectionProperties;
import sg.ncl.adapter.deterlab.data.jpa.DeterlabUserRepository;
import sg.ncl.service.experiment.data.jpa.ExperimentEntity;
import sg.ncl.service.experiment.data.jpa.ExperimentRepository;
import sg.ncl.service.experiment.domain.Experiment;
import sg.ncl.service.experiment.exceptions.UserIdNotFound;
import sg.ncl.service.experiment.web.ExperimentInfo;

import javax.inject.Inject;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Desmond.
 */
@Service
public class ExperimentService {

    private static final Logger logger = LoggerFactory.getLogger(ExperimentService.class);
    private final ExperimentRepository experimentRepository;

    @Inject
    protected ExperimentService(final ExperimentRepository experimentRepository) {
        this.experimentRepository = experimentRepository;
    }

    public ExperimentEntity save(ExperimentInfo experimentInfo) {
        logger.info("Save experiment");
        String fileName = craftFileName(experimentInfo);

        // check experiment name is unique
        long countName = experimentRepository.countByName(experimentInfo.getName());
        if (countName > 0) {
            logger.warn("Experiment name is in use");
            return null;
        }

        createNsFile(fileName, experimentInfo.getNsFileContents());

        ExperimentEntity experimentEntity = experimentRepository.save(setupEntity(experimentInfo, fileName));
        logger.info("Experiment saved");

        String returnResult = this.createExperimentInDeter(experimentEntity);
        if (returnResult == "done") {
            return experimentEntity;
        }

        else {
            return null;
        }
    }

    private String craftDate() {
        Date date = new Date();
        return new SimpleDateFormat("yyyyMMdd").format(date);
    }

    private String craftFileName(Experiment experiment) {
        String fileName = experiment.getNsFile() + ".ns";
        return experiment.getUserId() + "_" + experiment.getTeamId() + "_" + craftDate() + "_" + fileName;
    }

    private ExperimentEntity setupEntity(Experiment experiment, String fileName) {
        ExperimentEntity experimentEntity = new ExperimentEntity();

        experimentEntity.setUserId(experiment.getUserId());
        experimentEntity.setTeamId(experiment.getTeamId());
        experimentEntity.setName(experiment.getName());
        experimentEntity.setDescription(experiment.getDescription());
        experimentEntity.setNsFile(fileName);
        experimentEntity.setIdleSwap(experiment.getIdleSwap());
        experimentEntity.setMaxDuration(experiment.getMaxDuration());
        return experimentEntity;
    }

//    private String writeJsonString(ExperimentEntity experimentEntity) {
//        String jsonString = "";
//
//        try {
//            ObjectMapper mapper = new ObjectMapper();
//            mapper.registerModule(new JavaTimeModule());
//            jsonString = mapper.writeValueAsString(experimentEntity);
//
//        } catch (Exception e) {
////            throw e;
//
//        } finally {
//            return jsonString;
//        }
//    }

    public List<ExperimentEntity> get() {
        logger.info("Get all experiments");

        final List<ExperimentEntity> result = new ArrayList<>();
        for (ExperimentEntity experimentEntity : experimentRepository.findAll()) {
            result.add(experimentEntity);
        }
        return result;
    }

    public List<ExperimentEntity> findByUser(String userId) {
        logger.info("Find user by user id");
        if (userId == null || userId.isEmpty()) {
            throw new UserIdNotFound();
        }

        List<ExperimentEntity> result = experimentRepository.findByUserId(userId);
        return result;
    }

    public String createNsFile(String filename, String contents) {
        logger.info("Create NS file");

        File file;
        FileOutputStream fileOutputStream = null;

        try {
            file = new File(filename);
            fileOutputStream = new FileOutputStream(file);

            // if file doesn't exist, create it
            if (!file.exists()) {
                file.createNewFile();
            }

            // get contents in bytes
            byte[] contentInBytes = contents.getBytes();

            fileOutputStream.write(contentInBytes);
            fileOutputStream.flush();
            fileOutputStream.close();
        }

        catch (IOException e) {
            logger.error("File cannot be created.\n" + e.getMessage());
            filename = "error";
        }

        finally {
            try {
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            }

            catch (IOException e) {
                logger.error("File cannot be created.\n" + e.getMessage());
                filename = "error";
            }
        }

        return filename;
    }

    public String createExperimentInDeter(ExperimentEntity experimentEntity) {
        logger.info("Create experiment in deter");

        String login = experimentEntity.getUserId();
        String maxDuration = experimentEntity.getMaxDuration().toString();
        String idleSwap = experimentEntity.getIdleSwap().toString();
        String description = experimentEntity.getDescription();
        String project = experimentEntity.getTeamId();
        String name = experimentEntity.getName();
        String fileName = experimentEntity.getNsFile();

        StringBuilder command = new StringBuilder();
        command.append("script_wrapper.py");
        command.append(" --server=172.18.178.10");
        command.append(" --login=" + login);
        command.append(" startexp");
        command.append(" -a " + maxDuration);
        command.append(" -l " + idleSwap);
        command.append(" -E " + description);
        command.append(" -p " + project);
        command.append(" -e " + name);
        command.append(" " + fileName);

        try {
            Process process = Runtime.getRuntime().exec(command.toString());
            process.waitFor();
        }

        catch (Exception e) {
            logger.error("Experiment can't be created in deter.\n" + e.getMessage());
            return "error";
        }

        logger.info("Experiment created in deter");
        return "done";
    }
}
