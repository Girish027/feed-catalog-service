package feed.catalog.services;

import feed.catalog.rest.RestClient;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;

import static feed.catalog.utility.Constants.*;

@Component
@ConfigurationProperties(prefix = "okta")
@Setter
public class TokenValidatorWithOKTA {

    @Value("${application.secret.key}")
    private String applicationSecretKey;
    private String baseUrl;
    private String clientId;
    private String clientSecret;
    private String authorizationServiceId;
    private String accessToken;

    @Autowired
    private RestClient restClient;

    public boolean validateSession(HttpServletRequest httpServletRequest) throws Exception {

        List<String> headerList = Collections.list(httpServletRequest.getHeaderNames());
        if (headerList.contains(ACCESS_TOKEN)) {
            accessToken = httpServletRequest.getHeader(ACCESS_TOKEN);
            String url = baseUrl + OAUTH2 + authorizationServiceId + INTROSPECT;
            MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
            map.add(TOKEN, accessToken);
            map.add(TOKEN_TYPE_HINT, ACCESS_TOKEN);
            map.add(CLIENT_ID, clientId);
            map.add(CLIENT_SECRET, clientSecret);

            ResponseEntity<String> response = restClient.postRequest(url,map, MediaType.APPLICATION_FORM_URLENCODED);
            JSONObject jsonObject = new JSONObject(response.getBody());
            String tokenStatus = jsonObject.getString(ACTIVE);
            if (tokenStatus.equalsIgnoreCase("true")) {
                return true;
            } else {
                return false;
            }
        } else if (headerList.contains(SECRET_KEY)) {
            String secretKey = httpServletRequest.getHeader(SECRET_KEY);
            if (secretKey.equals(applicationSecretKey))
                return true;
            else
                return false;
        } else {
            return false;
        }
    }

}
