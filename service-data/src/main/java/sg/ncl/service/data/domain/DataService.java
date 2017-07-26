package sg.ncl.service.data.domain;

import io.jsonwebtoken.Claims;
import sg.ncl.service.transmission.web.ResumableInfo;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Set;

/**
 * Created by jng on 17/10/16.
 */
public interface DataService {

    Data createDataset(Data data);

    Data updateDataset(Long id, Data data, Claims claims);

    Data deleteDataset(Long id, Claims claims) throws UnsupportedEncodingException;

    Data getDataset(Long id);

    List<Data> getDatasets();

    Set<Data> searchDatasets(String[] keywords);

    List<Data> findByVisibility(DataVisibility visibility);

    DataResource findResourceById(Long did, Long rid, Claims claims);

    Data createResource(Long id, DataResource dataResource, Claims claims);

    Data deleteResource(Long did, Long rid, Claims claims);

    Data updateResource(Long did, DataResource dataResource, Claims claims);

    String checkChunk(String resumableIdentifier, String resumableChunkNumber);

    String addChunk(ResumableInfo resumableInfo, String resumableChunkNumber, Long id, Claims claims) throws UnsupportedEncodingException;

    void downloadResource(HttpServletResponse response, Long did, Long rid, Claims claims);

    List<DataCategory> getCategories();

    DataCategory getCategory(Long id);

    List<DataLicense> getLicenses();

    DataLicense getLicense(Long id);

}
