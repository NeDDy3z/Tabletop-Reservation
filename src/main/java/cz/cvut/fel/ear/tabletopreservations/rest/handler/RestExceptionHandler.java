package cz.cvut.fel.ear.tabletopreservations.rest.handler;

import cz.cvut.fel.ear.tabletopreservations.exception.NotFoundException;
import cz.cvut.fel.ear.tabletopreservations.exception.ValidationException;
import jakarta.persistence.PersistenceException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class RestExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(RestExceptionHandler.class);

    private static ErrorInfo errorInfo(HttpServletRequest request, Throwable e) {
        return new ErrorInfo(e.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorInfo> handleNotFoundException(HttpServletRequest request, NotFoundException ex) {
        LOG.warn("Resource not found: {}", ex.getMessage());
        return new ResponseEntity<>(errorInfo(request, ex), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorInfo> handleValidationException(HttpServletRequest request, ValidationException ex) {
        LOG.warn("Validation error: {}", ex.getMessage());
        return new ResponseEntity<>(errorInfo(request, ex), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorInfo> handleAccessDeniedException(HttpServletRequest request, AccessDeniedException ex) {
        LOG.warn("Access denied: {}", ex.getMessage());
        return new ResponseEntity<>(errorInfo(request, ex), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(PersistenceException.class)
    public ResponseEntity<ErrorInfo> handlePersistenceException(HttpServletRequest request, PersistenceException ex) {
        LOG.error("Persistence error: {}", ex.getMessage());
        return new ResponseEntity<>(errorInfo(request, ex), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorInfo> handleIllegalArgumentException(HttpServletRequest request, IllegalArgumentException ex) {
        LOG.warn("Invalid argument: {}", ex.getMessage());
        return new ResponseEntity<>(errorInfo(request, ex), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorInfo> handleIllegalStateException(HttpServletRequest request, IllegalStateException ex) {
        LOG.warn("Illegal state: {}", ex.getMessage());
        return new ResponseEntity<>(errorInfo(request, ex), HttpStatus.CONFLICT);
    }
}
