package toxiproxy.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import toxiproxy.api.exceptions.ResourceNotFoundException;

@ControllerAdvice(assignableTypes = ToxiproxyAPIController.class)
public class ToxiproxyAPIControllerExceptionHandler {

  @ExceptionHandler({ResourceNotFoundException.class})
  public ResponseEntity handleNotFoundException(Exception exception) {
    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
  }

}
