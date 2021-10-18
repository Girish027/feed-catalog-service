package feed.catalog.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class ProductServiceInterceptor implements HandlerInterceptor
{
    @Autowired
    private TokenValidatorWithOKTA tokenValidatorWithOKTA;

    @Override
    public boolean preHandle (HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        if(tokenValidatorWithOKTA.validateSession(request)) {
            return true;
        }
        else {
            response.getWriter().write("Invalid user details");
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return false;
        }
    }
}
