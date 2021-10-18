package feed.catalog.service;

import feed.catalog.rest.RestClient;
import feed.catalog.services.TokenValidatorWithOKTA;
import feed.catalog.utility.Constants;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static feed.catalog.utility.Constants.*;
import static feed.catalog.utils.Constants.accessToken;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
public class SessionValidatorTest
{

    @MockBean
    RestClient restClient ;

    @Test
    public void testAccessTokenAuthenticated() throws Exception {

        HttpServletRequest request = mock(HttpServletRequest.class);
        TokenValidatorWithOKTA tokenValidatorWithOKTA = new TokenValidatorWithOKTA();
        tokenValidatorWithOKTA.setAccessToken(accessToken);
        tokenValidatorWithOKTA.setRestClient(restClient);

        Map<String, String> headers = new HashMap<>();
        headers.put(ACCESS_TOKEN,accessToken);

        Iterator<String> iterator = headers.keySet().iterator();
        Enumeration headerNames = new Enumeration<String>() {
            @Override
            public boolean hasMoreElements() {
                return iterator.hasNext();
            }

            @Override
            public String nextElement() {
                return iterator.next();
            }
        };

        when(restClient.postRequest(any(),any(),any())).thenReturn(ResponseEntity.ok().body( "{\"active\":\"true\"}"));
        when(request.getHeaderNames()).thenReturn(headerNames);
        when(request.getHeader(any())).thenReturn(accessToken);
        Assert.assertTrue(tokenValidatorWithOKTA.validateSession(request));
    }

    @Test
    public void testAccessTokenUnAuthenticated() throws Exception {

        HttpServletRequest request = mock(HttpServletRequest.class);
        TokenValidatorWithOKTA tokenValidatorWithOKTA = new TokenValidatorWithOKTA();
        tokenValidatorWithOKTA.setAccessToken(accessToken);
        tokenValidatorWithOKTA.setRestClient(restClient);

        Map<String, String> headers = new HashMap<>();
        headers.put(ACCESS_TOKEN,accessToken);

        Iterator<String> iterator = headers.keySet().iterator();
        Enumeration headerNames = new Enumeration<String>() {
            @Override
            public boolean hasMoreElements() {
                return iterator.hasNext();
            }

            @Override
            public String nextElement() {
                return iterator.next();
            }
        };

        when(restClient.postRequest(any(),any(),any())).thenReturn(ResponseEntity.ok().body( "{\"active\":\"false\"}"));
        when(request.getHeaderNames()).thenReturn(headerNames);
        when(request.getHeader(any())).thenReturn(accessToken);
        Assert.assertFalse(tokenValidatorWithOKTA.validateSession(request));
    }



    @Test
    public void testSecretKeyAuthenticated() throws Exception {

        TokenValidatorWithOKTA tokenValidatorWithOKTA = new TokenValidatorWithOKTA();
        tokenValidatorWithOKTA.setApplicationSecretKey("72234af1-382c-4577-b8a2-49ee7d21c4bb");

        Map<String, String> headers = new HashMap<>();
        headers.put(Constants.SECRET_KEY,"72234af1-382c-4577-b8a2-49ee7d21c4bb");

        Iterator<String> iterator = headers.keySet().iterator();
        Enumeration headerNames = new Enumeration<String>() {
            @Override
            public boolean hasMoreElements() {
                return iterator.hasNext();
            }

            @Override
            public String nextElement() {
                return iterator.next();
            }
        };

        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeaderNames()).thenReturn(headerNames);
        when(request.getHeader(any())).thenReturn("72234af1-382c-4577-b8a2-49ee7d21c4bb");
        Assert.assertTrue(tokenValidatorWithOKTA.validateSession(request));

    }

    @Test
    public void testSecretKeyUnAuthenticated() throws Exception {

        TokenValidatorWithOKTA tokenValidatorWithOKTA = new TokenValidatorWithOKTA();
        tokenValidatorWithOKTA.setApplicationSecretKey("72234af1-382c-4577-b8a2-49ee7d21c4bb");

        Map<String, String> headers = new HashMap<>();
        headers.put(Constants.SECRET_KEY,"123455");

        Iterator<String> iterator = headers.keySet().iterator();
        Enumeration headerNames = new Enumeration<String>() {
            @Override
            public boolean hasMoreElements() {
                return iterator.hasNext();
            }

            @Override
            public String nextElement() {
                return iterator.next();
            }
        };

        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeaderNames()).thenReturn(headerNames);
        when(request.getHeader(any())).thenReturn("123455");
        Assert.assertFalse(tokenValidatorWithOKTA.validateSession(request));

    }


}
