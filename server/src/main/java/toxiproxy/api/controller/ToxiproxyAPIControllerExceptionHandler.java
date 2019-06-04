package toxiproxy.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;

@ControllerAdvice(assignableTypes = ToxiproxyAPIController.class)
public class ToxiproxyAPIControllerExceptionHandler {

  @ExceptionHandler({HttpClientErrorException.NotFound.class})
  public ResponseEntity handleNotFoundException(Exception exception) {
    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
  }

}
