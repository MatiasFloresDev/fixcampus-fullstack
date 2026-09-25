package pe.edu.upc.fixcampus.exception;

import org.springframework.context.MessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import java.util.Locale;
import java.util.Map;

@RestControllerAdvice
public class ErrorAdvice {
    private final MessageSource messages;
    public ErrorAdvice(MessageSource messages) { this.messages = messages; }

    @ExceptionHandler(ApiException.class)
    ResponseEntity<?> business(ApiException ex, Locale locale) {
        return error(ex.status, ex.code, locale);
    }
    @ExceptionHandler({MethodArgumentNotValidException.class, HttpMessageNotReadableException.class})
    ResponseEntity<?> validation(Exception ex, Locale locale) { return error(400, "validation", locale); }
    @ExceptionHandler(DataIntegrityViolationException.class)
    ResponseEntity<?> duplicate(Exception ex, Locale locale) { return error(409, "duplicate", locale); }
    @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
    ResponseEntity<?> concurrent(Exception ex, Locale locale) { return error(409, "concurrent", locale); }
    private ResponseEntity<?> error(int status, String code, Locale locale) {
        return ResponseEntity.status(status).body(Map.of("message", messages.getMessage(code,null,locale), "code",code));
    }
}
