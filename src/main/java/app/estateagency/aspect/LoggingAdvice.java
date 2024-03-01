package app.estateagency.aspect;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class LoggingAdvice {
    private static final Logger log = LogManager.getLogger(LoggingAdvice.class);

    @Before("execution(* app.estateagency.controllers.*.*(..))")
    public void logEndpointRequest(JoinPoint joinPoint) {
        log.info("Endpoint reached: " + joinPoint.getSignature().toShortString());
    }

    @AfterReturning(pointcut = "execution(* app.estateagency.controllers.*.*(..))", returning = "response")
    public void logEndpointResponse(JoinPoint joinPoint, Object response) {
        log.info("Endpoint response: " + response.toString());
    }

    @AfterThrowing(pointcut = "within(app.estateagency.*)", throwing = "exception")
    public void logException(JoinPoint joinPoint, Throwable exception) {
        log.error("Exception in " + joinPoint.getSignature().toShortString() + ": " + exception.getMessage());
    }

}
