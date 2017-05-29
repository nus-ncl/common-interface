package sg.ncl.service.data.logic;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriUtils;
import sg.ncl.common.AvScannerProperties;
import sg.ncl.service.data.domain.AvScannerService;
import sg.ncl.service.transmission.DirectoryProperties;
import sg.ncl.service.transmission.util.HttpUtils;
import xyz.capybara.clamav.ClamavClient;
import xyz.capybara.clamav.commands.scan.result.ScanResult;
import xyz.capybara.clamav.configuration.Platform;
import xyz.capybara.clamav.exceptions.ClamavException;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * The class that scans a uploaded file and determines if it is malicious
 * The class also has a scheduled scan that scan the files within data repository folders
 *
 * Implementation utilizes ClamAv
 *
 * Java ClamAv client library
 * https://github.com/cdarras/clamav-client
 *
 * Created by teye
 */
@Service
@Slf4j
@EnableConfigurationProperties(AvScannerProperties.class)
public class AvScannerImpl implements AvScannerService {

    private final AvScannerProperties avScannerProperties;
    private final ClamavClient client;
    private final DirectoryProperties directoryProperties;

    private static final String UTF_ENCODING = "UTF-8";

    @Inject
    AvScannerImpl(@NotNull final AvScannerProperties avScannerProperties, @NotNull final DirectoryProperties directoryProperties) {
        this.avScannerProperties = avScannerProperties;
        this.directoryProperties = directoryProperties;
        client = new ClamavClient(avScannerProperties.getHost(), avScannerProperties.getPort(), Platform.UNIX);
    }

    //
    // return true if file is malicious
    // TODO: add authentication
    public boolean scan(String subDirKey, String preDir, String fileName) {
        try {
            log.info("Initiating ClamAV... {}", client.version());

            Path directoryPath = HttpUtils.getPath(directoryProperties, subDirKey, preDir);
            Path fullPath = Paths.get(directoryPath.toString() + "/" + fileName);

            if (Files.exists(fullPath)) {
                log.info("File path EXIST now scanning...{}", fullPath);
            } else {
                log.info("Error: File path does not exist: {}", fullPath);
            }

            ScanResult result = client.scan(fullPath, false);

            if (result.getStatus().equals(ScanResult.Status.OK)) {
                log.info("File: " + fullPath + " is ok");
                return false;
            } else if (result.getStatus().equals(ScanResult.Status.VIRUS_FOUND)) {
                log.info("File: " + fullPath + " is a virus");
                return true;
            } else {
                log.info("Error: running clamav");
            }

        } catch (ClamavException e) {
            e.printStackTrace();
        }
        return  false;
    }
}
