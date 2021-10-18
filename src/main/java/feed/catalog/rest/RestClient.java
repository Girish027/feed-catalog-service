package feed.catalog.rest;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.Optional;

import static feed.catalog.utility.Constants.*;

/**
 * Created by palak.data on 8/13/2019.
 */

@Slf4j
@Service
@Data
public class RestClient {

    @Value("${feed.exporter-type}")
    private String exporterTypes;

    @Value("${environment.catalog.host}")
    private String catalogHost;

    @Value("${environment.catalog.port}")
    private String catalogPort;

    private HttpURLConnection getConnection(String urlValue) throws IOException {
        HttpURLConnection connection = null;
        connection = (HttpURLConnection) (new URL(urlValue)).openConnection();
        return connection;
    }

    public String getRequest(String urlValue) throws Exception {
        StringBuffer response = new StringBuffer();
        try {
            HttpURLConnection connection = getConnection(urlValue);
            connection.setRequestMethod("GET");
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));
            String inputLine = "";
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
        } catch (IOException e) {
            throw new IOException("Error while generating response from catalog API" + e);
        }
        return response.toString();
    }

    public String getParameterString(String exporterTypes) {
        String requestParam = "";
        StringBuffer params = new StringBuffer();
        try {
            if (exporterTypes.isEmpty()) {
                throw new Exception("exporter-types not defined");
            }
            if (exporterTypes.contains(",")) {
                String[] token = exporterTypes.split(",");
                for (String types : token) {
                    params.append("exporter-type=" + types + "&");
                }
                requestParam = params.substring(0, params.length() - 1);
            } else {
                params.append(exporterTypes);
            }
        } catch (Exception ex) {
            log.error("exporter-types not defined");
            new Exception("exporter-types not defined" + ex);
        }
        return requestParam;
    }

    public String contructURL(String host, String port, String urlPath, String urlQuery) {
        UriComponents uriComponents = UriComponentsBuilder.newInstance()
                .scheme("http")
                .host(host).port(port)
                .path(urlPath).query(urlQuery).build(true);

        URI uri = uriComponents.toUri();
        return uri.toString();
    }

    public ResponseEntity<String> postRequest(String url, MultiValueMap<String, String> map ,MediaType optionalMediaType)
    {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        MediaType mediaType = optionalMediaType;
        MediaType media = Optional.ofNullable(mediaType).orElse(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setContentType(media);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

        return response;
    }
}
