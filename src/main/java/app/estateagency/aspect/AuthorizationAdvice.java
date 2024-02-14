package app.estateagency.aspect;

import app.estateagency.enums.Privilege;
import app.estateagency.exceptions.AuthorizationException;
import app.estateagency.security.DatabaseUserDetails;
import app.estateagency.security.RequiredPrivilege;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import java.util.Objects;

@Aspect
@Component
@RequiredArgsConstructor
public class AuthorizationAdvice {

    @Before("@annotation(app.estateagency.security.RequiredPrivilege)")
    public void authorize(JoinPoint joinPoint) {
        Privilege privilege = extractPrivilege(joinPoint);

        for (Object arg : joinPoint.getArgs()) {
            if (arg instanceof DatabaseUserDetails customUserDetails && isUnauthorized(customUserDetails, privilege)) {
                throw new AuthorizationException("User lacked privilege to perform the task");
            }
        }

    }

    private boolean isUnauthorized(UserDetails userDetails, Privilege privilege) {
        return Objects.isNull(userDetails) || userDetails.getAuthorities().stream().noneMatch(authority -> authority.getAuthority().equals(privilege.name()));
    }

    private Privilege extractPrivilege(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        RequiredPrivilege privilege = methodSignature.getMethod().getAnnotation(RequiredPrivilege.class);

        if (Objects.isNull(privilege)) {
            throw new AuthorizationException("User lacked privilege to perform the task");
        }

        return privilege.value();
    }
}
