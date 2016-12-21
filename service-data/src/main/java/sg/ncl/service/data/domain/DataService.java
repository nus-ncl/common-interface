package sg.ncl.service.data.domain;

import io.jsonwebtoken.Claims;
import sg.ncl.service.transmission.web.ResumableInfo;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by jng on 17/10/16.
 */
public interface DataService {

    Data createDataset(Data data);

    Data updateDataset(Long id, Data data, Claims claims);

    Data deleteDataset(Long id, Claims claims) throws UnsupportedEncodingException;

    Data getDataset(Long id);

    List<Data> getDatasets();

    List<Data> findByVisibility(DataVisibility visibility);

    DataResource findResourceById(Long did, Long rid, Claims claims);

    Data createResource(Long id, DataResource dataResource, Claims claims);

    Data deleteResource(Long did, Long rid, Claims claims);

    String checkChunk(String resumableIdentifier, String resumableChunkNumber);

    String addChunk(ResumableInfo resumableInfo, String resumableChunkNumber, Long id, Claims claims) throws UnsupportedEncodingException;

    void downloadResource(HttpServletResponse response, Long did, Long rid, Claims claims);

    String createRequest(Long id, String reason, Claims claims);

}
