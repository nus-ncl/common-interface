package sg.ncl.service.experiment.logic;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sg.ncl.adapter.deterlab.AdapterDeterlab;
import sg.ncl.adapter.deterlab.ConnectionProperties;
import sg.ncl.adapter.deterlab.data.jpa.DeterlabUserRepository;
import sg.ncl.service.experiment.data.jpa.ExperimentEntity;
import sg.ncl.service.experiment.data.jpa.ExperimentRepository;
import sg.ncl.service.experiment.domain.Experiment;
import sg.ncl.service.experiment.exceptions.UserIdNotFound;

import javax.inject.Inject;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
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

    private final ExperimentRepository experimentRepository;

    @Autowired
    private final AdapterDeterlab adapterDeterlab;

    @Inject
    protected ExperimentService(final ExperimentRepository experimentRepository, final DeterlabUserRepository deterlabUserRepository, final ConnectionProperties connectionProperties) {
        this.experimentRepository = experimentRepository;
        this.adapterDeterlab = new AdapterDeterlab(deterlabUserRepository, connectionProperties);
    }

    public ExperimentEntity save(Experiment experiment) {
        String fileName = craftFileName(experiment);

        // check experiment name is unique
        long countByUserId = experimentRepository.countByTeamId(experiment.getName());
        if (countByUserId > 0) {
            return new ExperimentEntity();
        }

        ExperimentEntity experimentEntity = experimentRepository.save(setupEntity(experiment, fileName));
        String parametersJson = writeJsonString(experimentEntity);
        adapterDeterlab.createExperiment(parametersJson);

        return experimentEntity;
    }

    private String craftDate() {
        Date date = new Date();
        return new SimpleDateFormat("yyyyMMdd").format(date);
    }

    private String craftFileName(Experiment experiment) {
        String fileName = experiment.getNsFile();
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

    private String writeJsonString(ExperimentEntity experimentEntity) {
        String jsonString = "";

        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            jsonString = mapper.writeValueAsString(experimentEntity);

        } catch (Exception e) {
//            throw e;

        } finally {
            return jsonString;
        }
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

    public String createExperimentInDeter(ExperimentEntity experimentEntity) {
        // call python script to apply for new project
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", experimentEntity.getId());
        jsonObject.put("userId", experimentEntity.getUserId());
        jsonObject.put("teamId", experimentEntity.getTeamId());
        jsonObject.put("name", experimentEntity.getName());
        jsonObject.put("description", experimentEntity.getDescription());
        jsonObject.put("nsFile", experimentEntity.getNsFile());
        jsonObject.put("idleSwap", experimentEntity.getIdleSwap());
        jsonObject.put("maxDuration", experimentEntity.getMaxDuration());

        String resultJson = adapterDeterlab.createExperiment(jsonObject.toString());

        return resultJson;
    }

    private void uploadNsFile(String fileName) {
        String ftpUrl = "ftp://%s:%s@%s/%s;type=i";
        String host = "173.18.178.11";
        String user = "ncl";
        String pass = "deterinavm";
        String filePath = "/nsfile/" + fileName;
        String uploadPath = "/nsfile/ + fileName";

        ftpUrl = String.format(ftpUrl, user, pass, host, uploadPath);
        System.out.println("Upload URL: " + ftpUrl);

        try {
            URL url = new URL(ftpUrl);
            URLConnection conn = url.openConnection();
            OutputStream outputStream = conn.getOutputStream();
            FileInputStream inputStream = new FileInputStream(filePath);

            byte[] buffer = new byte[4096];
            int bytesRead = -1;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            inputStream.close();
            outputStream.close();

            System.out.println("File uploaded");

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
