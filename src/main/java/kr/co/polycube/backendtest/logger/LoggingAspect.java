package kr.co.polycube.backendtest.logger;

import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    @Autowired
    private HttpServletRequest request;

    @Before("execution(* kr.co.polycube.backendtest.controller.UserController.*(..))")
    public void logClientAgent() {
        String clientAgent = request.getHeader("User-Agent");
        System.out.println("Client Agent: " + clientAgent);
    }
}
