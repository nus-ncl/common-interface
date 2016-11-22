package sg.ncl.service.realization.logic;

import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sg.ncl.adapter.deterlab.AdapterDeterLab;
import sg.ncl.service.realization.data.jpa.RealizationEntity;
import sg.ncl.service.realization.data.jpa.RealizationRepository;
import sg.ncl.service.realization.domain.RealizationService;
import sg.ncl.service.realization.domain.RealizationState;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by Desmond.
 */
@Service
@Slf4j
public class RealizationServiceImpl implements RealizationService {

    private final RealizationRepository realizationRepository;
    private final AdapterDeterLab adapterDeterLab;

    @Inject
    RealizationServiceImpl(@NotNull final RealizationRepository realizationRepository, @NotNull final AdapterDeterLab adapterDeterLab) {
        this.realizationRepository = realizationRepository;
        this.adapterDeterLab = adapterDeterLab;
    }

    @Transactional
    public RealizationEntity getById(final Long id) {
        log.info("Get realization by id");

        RealizationEntity realizationEntity = realizationRepository.findOne(id);

        if (realizationEntity != null && realizationEntity.getId() > 0) {
            return realizationEntity;
        } else {
            return null;
        }
    }

    @Transactional
    public RealizationEntity getByExperimentId(final Long experimentId) {
        log.info("Get realization by experiment name");

        RealizationEntity realizationEntity = realizationRepository.findByExperimentId(experimentId);

        if (realizationEntity != null && realizationEntity.getId() > 0) {
            return realizationEntity;
        } else {
            return null;
        }
    }

    /**
     * Retrieves the live status of experiments' status from Deterlab
     *
     * @param teamName     the team whose experiments that are of interest
     * @param experimentId the experiment name to get status
     * @return the realization entity object that contains the updated experiment status and report pulled from Deterlab. Defaults status to not running if there are any connection errors to Deterlab.
     * @implNote DB Sync issue cannot be avoided due to dependency of results from Deterlab before we can update our own DB
     */
    @Transactional
    @Override
    public RealizationEntity getByExperimentId(final String teamName, final Long experimentId) {
        log.info("Get realization for team: {}, expid: {}", teamName, experimentId);

        RealizationEntity realizationEntity = realizationRepository.findByExperimentId(experimentId);

        if (realizationEntity != null && realizationEntity.getId() > 0) {
            log.info("Retrieved realization entity: {}", realizationEntity);
            String result = adapterDeterLab.getExperimentStatus(teamName, realizationEntity.getExperimentName());

            log.info("Retrieved deterlab exp status...Exp: {} State: {}", realizationEntity.getExperimentName(), result);

            JSONObject jsonObjectFromExperiment = new JSONObject(result);
            String status = jsonObjectFromExperiment.getString("status");
            RealizationState realizationState;
            if (status == null) {
                realizationState = RealizationState.NOT_RUNNING;
            } else {
                switch (status) {
                    case "active":
                        realizationState = RealizationState.RUNNING;
                        break;
                    case "activating":
                        realizationState = RealizationState.STARTING;
                        break;
                    case "swapping":
                        realizationState = RealizationState.STOPPING;
                        break;
                    case "error":
                        realizationState = RealizationState.ERROR;
                        break;
                    default:
                        realizationState = RealizationState.NOT_RUNNING;
                        break;
                }
            }
            if (realizationEntity.getState() != realizationState) {
                realizationEntity.setState(realizationState);
                // only set details if state is different
                switch (realizationState) {
                    case RUNNING:
                        String report = jsonObjectFromExperiment.getJSONObject("report").toString();
                        realizationEntity.setDetails(report);
                        break;
                    case STOPPING:
                        realizationEntity.setDetails("");
                        break;
                    default:
                        break;
                }
                log.info("Realization: {} updated to state: {}", realizationEntity, realizationState);
                return realizationRepository.save(realizationEntity);
            }
            return realizationEntity;
        } else {
            log.warn("Get realization fail for team: {}, experiment id: {}", teamName, experimentId);
            return new RealizationEntity();
        }
    }

    @Transactional
    public RealizationEntity save(RealizationEntity realizationEntity) {
        log.info("Save realization");

        RealizationEntity savedRealizationEntity = realizationRepository.save(realizationEntity);

        log.info("Realization saved");
        return savedRealizationEntity;
    }

    /**
     * Starts an experiment in Deterlab (allocate resources and swapped-in).
     * The experiment from Deterlab will generate a report which states the Qualified Name, IP Address, Link Delay .etc
     *
     * @param teamName the team to start the exp for
     * @param expId    the experiment name to start, e.g. demo, Identical experiment names are allow for different teams
     * @param claims   the jwt token
     * @return the realization entity object that contains the updated experiment status and report pulled from Deterlab
     * @implNote DB sync not an issue since experiment status and report will be re-updated when users refresh the page
     */
    @Transactional
    public RealizationEntity startExperimentInDeter(final String teamName, final String expId, final Claims claims) {
        log.info("Starting experiment: {} for team: ", expId, teamName);
        RealizationEntity realizationEntityDb = realizationRepository.findByExperimentId(Long.parseLong(expId));
        String experimentName = realizationEntityDb.getExperimentName();
        String experimentStarterUserId = claims.getSubject();

        String stringFromExperiment = adapterDeterLab.startExperiment(teamName, experimentName, experimentStarterUserId);
        JSONObject jsonObjectFromExperiment = new JSONObject(stringFromExperiment);

        String status = jsonObjectFromExperiment.getString("status");
        RealizationState realizationState;

        switch (status) {
            case "active":
                realizationState = RealizationState.RUNNING;
                break;
            case "activating":
                realizationState = RealizationState.STARTING;
                break;
            default:
                realizationState = RealizationState.NOT_RUNNING;
                break;
        }
        log.info("Start Experiment: {}, Team: {} Status: {}", experimentName, teamName, realizationState);
        // remove set details since it is set when user refreshes the page
        // also ensure spring does not lock start experiment for too long
        realizationEntityDb.setState(realizationState);
        return realizationRepository.save(realizationEntityDb);
    }

    /**
     * Stops an experiment in Deterlab (de-allocate resources and swapped-out).
     * The realization entity report will be clear.
     *
     * @param teamName the team to stop the exp
     * @param expId    the experiment name to stop
     * @param claims   the jwt token
     * @return the realization entity object that contains the updated experiment status and with empty report
     * @implNote DB sync not an issue since experiment status and report will be re-updated when users refresh the page
     */
    @Transactional
    public RealizationEntity stopExperimentInDeter(final String teamName, final String expId, final Claims claims) {
        log.info("Stopping experiment: {} for team: ", expId, teamName);
        RealizationEntity realizationEntityDb = realizationRepository.findByExperimentId(Long.parseLong(expId));
        String experimentName = realizationEntityDb.getExperimentName();
        String experimentStopperUserId = claims.getSubject();

        String resultState = adapterDeterLab.stopExperiment(teamName, experimentName, experimentStopperUserId);
        RealizationState realizationState;
        if (resultState.equals("swapped")) {
            realizationState = RealizationState.NOT_RUNNING;
        } else {
            realizationState = RealizationState.ERROR;
        }
        log.info("Stop Experiment: {}, Team: {} Status: {}", experimentName, teamName, realizationState);
        realizationEntityDb.setState(realizationState);
        realizationEntityDb.setDetails("");
        return realizationRepository.save(realizationEntityDb);
    }

    @Transactional
    public void deleteRealization(final Long realizationId) {
        log.info("Delete realization. {}", realizationId);
        realizationRepository.delete(realizationId);
    }

    /**
     *
     * @param id: team id
     * @return usage in node x hour, or "?"
     */
    public String getUsageStatistics(String id) {
        // current implementation gets usage statistics for current month only
        String end = ZonedDateTime.now().toLocalDate().format(DateTimeFormatter.ofPattern("MM/dd/uu"));
        String start = end.substring(0, 3) + "01" + end.substring(5);
        log.info("Getting usage statistics for team {}, start {}, end {}", id, start, end);
        return adapterDeterLab.getUsageStatistics(id, start, end);
    }
}
