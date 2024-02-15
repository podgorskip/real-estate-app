package app.estateagency.exceptions;

import app.estateagency.dto.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import java.util.stream.Collectors;

@ControllerAdvice
public class RestControllerAdvice {
    private final Logger log = LogManager.getLogger(RestControllerAdvice.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseEntity<Response> handleInvalidArgumentException(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();

        String message = bindingResult.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining(", "));
        log.error(message);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response(false, HttpStatus.BAD_REQUEST, message));
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseBody
    public ResponseEntity<Response> handleAuthenticationException(AuthenticationException e) {
        if (e.getMessage().equals("Invalid token")) {

            String message = "Invalid token provided";
            log.error(message);

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new Response(false, HttpStatus.UNAUTHORIZED, message));
        } else {

            String message = "Failed to authenticate user";
            log.error(message);

            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new Response(false, HttpStatus.FORBIDDEN, message));
        }
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseBody
    public ResponseEntity<Response> handleIllegalArgumentException(IllegalArgumentException e) {
        log.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response(false, HttpStatus.BAD_REQUEST, e.getMessage()));
    }

    @ExceptionHandler(AuthorizationException.class)
    @ResponseBody
    public ResponseEntity<Response> handleAuthorizationException(AuthorizationException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new Response(false, HttpStatus.UNAUTHORIZED, e.getMessage()));
    }

}
