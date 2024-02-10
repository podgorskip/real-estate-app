package app.estateagency.exceptions;

import app.estateagency.dto.response.Response;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import java.util.stream.Collectors;

@ControllerAdvice
public class RestControllerAdvice {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseEntity<Response> handleInvalidArgumentException(MethodArgumentNotValidException e) {
        Response response = new Response();

        BindingResult bindingResult = e.getBindingResult();

        String message = bindingResult.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining(", "));

        response.setSuccess(false);
        response.setStatus(HttpStatus.BAD_REQUEST);
        response.setResponse(message);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}
