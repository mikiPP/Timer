package es.sm2baleares.base.api.controller.exception;

import es.sm2baleares.base.service.exception.AuthenticationException;
import es.sm2baleares.spring.common.api.exception.ApiErrorCatalog;
import es.sm2baleares.spring.common.model.api.error.ApiError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.UUID;

/**
 * Exception controller. Used to manage controller exceptions
 * <p>
 * Exception managed here override the SM2 Spring Commons Exception Controller
 */
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
class CustomExceptionController {

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public ApiError authenticationException(Throwable exception) {

        return new ApiError(createErrorId(), ApiErrorCatalog.UNAUTHORIZED.getCode(), HttpStatus.UNAUTHORIZED,
                exception.getMessage());
    }

    /**
     * Create a new Error ID
     *
     * @return the new error id
     */
    private String createErrorId() {

        return UUID.randomUUID().toString();
    }

    /**
     * Log the Bad Request exception
     *
     * @param errorId      the error id
     * @param errorMessage the error message
     */
    private void logBadRequestException(String errorId, String errorMessage) {

        log.debug("[{}] Bad Client Request - {}", errorId, errorMessage);
    }

    private BindingResult getBindingResult(Exception exception) {

        if (exception instanceof BindException) {

            return ((BindException) exception).getBindingResult();

        } else if (exception instanceof MethodArgumentNotValidException) {

            return ((MethodArgumentNotValidException) exception).getBindingResult();

        } else {

            return null;
        }
    }

    private void logApplicationException(String errorId, String errorMessage, Throwable throwable) {
        log.error("[{}] {}", errorId, errorMessage, throwable);
    }

}
