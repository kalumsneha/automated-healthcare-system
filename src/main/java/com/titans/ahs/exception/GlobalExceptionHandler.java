package com.titans.ahs.exception;

import com.titans.ahs.exception.entity.ErrorResponse;
import com.titans.ahs.exception.type.DuplicateException;
import com.titans.ahs.exception.type.NotFoundException;
import com.titans.ahs.exception.type.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.TypeMismatchException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
@ResponseStatus
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({ NotFoundException.class,
            TypeMismatchException.class,
            WebExchangeBindException.class,
            MethodArgumentTypeMismatchException.class,
            ConstraintViolationException.class,
    DataIntegrityViolationException.class,
            DuplicateException.class,
    ServiceException.class,
    IllegalArgumentException.class,
    InvalidDataAccessApiUsageException.class})
    public final ResponseEntity<ErrorResponse> exceptionHandler(
            Exception exception, WebRequest webRequest){
        log.info("exceptionHandler of GlobalExceptionHandler");

        HttpHeaders httpHeaders = new HttpHeaders();
        HttpStatus status;
        if(exception instanceof DuplicateException){
            log.info("exceptionHandler of GlobalExceptionHandler throws DuplicateException");
            status = HttpStatus.CONFLICT;
            return this.exceptionHandler(exception, httpHeaders, status, webRequest);
        }
        else if(exception instanceof NotFoundException){
            log.info("exceptionHandler of GlobalExceptionHandler throws NotFoundException");
            status = HttpStatus.NOT_FOUND;
            return this.exceptionHandler(exception, httpHeaders, status, webRequest);
        }
        else if(exception instanceof ServiceException){
            log.info("exceptionHandler of GlobalExceptionHandler throws ServiceException");
            status = HttpStatus.BAD_REQUEST;
            return this.exceptionHandler(exception, httpHeaders, status, webRequest);
        }
        else if(exception instanceof IllegalArgumentException){
            log.info("exceptionHandler of GlobalExceptionHandler throws IllegalArgumentException");
            status = HttpStatus.BAD_REQUEST;
            return this.exceptionHandler(exception, httpHeaders, status, webRequest);
        }
        else if(exception instanceof InvalidDataAccessApiUsageException){
            log.info("exceptionHandler of GlobalExceptionHandler throws InvalidDataAccessApiUsageException");
            status = HttpStatus.BAD_REQUEST;
            return this.exceptionHandler(exception, httpHeaders, status, webRequest);
        }
        else if(exception instanceof TypeMismatchException){
            log.info("exceptionHandler of AccountGlobalExceptionHandler throws TypeMismatchException");
            status = HttpStatus.BAD_REQUEST;
            return exceptionHandler(exception, httpHeaders, status, webRequest);
        }
        else if(exception instanceof WebExchangeBindException){
            log.info("exceptionHandler of AccountGlobalExceptionHandler throws WebExchangeBindException");
            status = HttpStatus.BAD_REQUEST;
            return handleWebExchangeBindException((WebExchangeBindException) exception, httpHeaders, status, webRequest);
        }
        else if(exception instanceof MethodArgumentTypeMismatchException){
            log.info("exceptionHandler of AccountGlobalExceptionHandler throws MethodArgumentTypeMismatchException");
            MethodArgumentTypeMismatchException ex = (MethodArgumentTypeMismatchException) exception;
            status = HttpStatus.BAD_REQUEST;
            String error = ex.getName() + " should be of type " + ex.getRequiredType().getName();
            return exceptionHandler(exception, httpHeaders, status, Collections.singletonList(error), webRequest);
        }
        else{
            log.info("exceptionHandler of GlobalExceptionHandler throws Uncaught Exception");
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            return exceptionHandler(exception, httpHeaders, status, webRequest);
        }
    }

    /** Customize the response for All Exception Types. */
    protected ResponseEntity<ErrorResponse> exceptionHandler(
            Exception employeeException, HttpHeaders httpHeaders,
            HttpStatus httpStatus, WebRequest webRequest){
        List<String> errors = Collections.singletonList(employeeException.getMessage());
        return new ResponseEntity<ErrorResponse>(ErrorResponse.builder()
                .httpStatus(httpStatus)
                .message(employeeException.getMessage())
                .errors(errors)
                .timestamp(LocalDate.now()).build(),
                httpHeaders, httpStatus);

    }

    /** Customize the response for All Exception Types. */
    protected ResponseEntity<ErrorResponse> exceptionHandler(
            Exception employeeException, HttpHeaders httpHeaders,
            HttpStatus httpStatus, List<String> errors, WebRequest webRequest){
        return new ResponseEntity<ErrorResponse>(ErrorResponse.builder()
                .httpStatus(httpStatus)
                .message(employeeException.getLocalizedMessage())
                .errors(errors)
                .timestamp(LocalDate.now()).build(),
                httpHeaders, httpStatus);

    }

    /** Customize the response for WebExchangeBindException. */
    protected ResponseEntity<ErrorResponse> handleWebExchangeBindException(
            WebExchangeBindException ex, HttpHeaders headers,
            HttpStatus status, WebRequest request) {
        List<String> errorMessages = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(contentError -> contentError.getDefaultMessage() + " - " + contentError.getField() + " has an invalid value: " + contentError.getRejectedValue())
                .collect(Collectors.toList());

        return new ResponseEntity<>(new ErrorResponse(status,
                "Invalid Body Request",
                errorMessages,
                LocalDate.now()),
                headers, status);

    }

    /*
    JSR 303 Validation Error (REST API)
    The handleMethodArgumentNotValid handles the MethodArgumentNotValidException
    which is thrown when validation on an argument annotated with @Valid fails
    */

    /**
     * MethodArgumentNotValidException – This exception is thrown when an argument annotated with @Valid failed validation
     * @param ex
     * @param headers
     * @param status
     * @param request
     * @return
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers,
            HttpStatusCode status, WebRequest request) {

        List<String> errors = new ArrayList<String>();
        //Field Errors
        errors.addAll(ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(x -> x.getField() + ": " + x.getDefaultMessage())
                .collect(Collectors.toList()));
        //Global Errors
        errors.addAll(ex.getBindingResult()
                .getGlobalErrors()
                .stream()
                .map(x -> x.getObjectName() + ": " + x.getDefaultMessage())
                .collect(Collectors.toList()));


        return new ResponseEntity<>(new ErrorResponse(status, "Invalid Request Body", errors, LocalDate.now()), HttpStatus.BAD_REQUEST);
    }

    /**
     * Handle HTTP Method not supported for valid path specified
     * @param ex
     * @param headers
     * @param status
     * @param request
     * @return
     */
    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
            HttpRequestMethodNotSupportedException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {
        StringBuilder builder = new StringBuilder();
        builder.append(ex.getMethod());
        builder.append(
                " method is not supported for this request. Supported methods are ");
        ex.getSupportedHttpMethods().forEach(t -> builder.append(t + " "));


        return new ResponseEntity<>(new ErrorResponse(HttpStatus.METHOD_NOT_ALLOWED,
                ex.getLocalizedMessage(), builder.toString(), LocalDate.now()),
                HttpStatus.METHOD_NOT_ALLOWED);

    }

    /**
     * Handle Invalid Path URLs
     * @param ex
     * @param headers
     * @param status
     * @param request
     * @return
     */
    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(
            NoHandlerFoundException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        String error = "No handler found for " + ex.getHttpMethod() + " " + ex.getRequestURL();

        return new ResponseEntity<>(new ErrorResponse(HttpStatus.NOT_FOUND,
                ex.getLocalizedMessage(), error, LocalDate.now()),
                HttpStatus.NOT_FOUND);
    }

    /**
     * Handle HTTP Media Type Not Supported
     * @param ex
     * @param headers
     * @param status
     * @param request
     * @return
     */
    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(
            HttpMediaTypeNotSupportedException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {
        StringBuilder builder = new StringBuilder();
        builder.append(ex.getContentType());
        builder.append(" media type is not supported. Supported media types are ");
        ex.getSupportedMediaTypes().forEach(t -> builder.append(t + ", "));

        return new ResponseEntity<>(new ErrorResponse(HttpStatus.UNSUPPORTED_MEDIA_TYPE,
                ex.getLocalizedMessage(), builder.substring(0, builder.length() - 2), LocalDate.now()),
                HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }

    /**
     * Handle Missing Servlet Parameter Exception
     * @param ex
     * @param headers
     * @param status
     * @param request
     * @return
     */
    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            MissingServletRequestParameterException ex, HttpHeaders headers,
            HttpStatusCode status, WebRequest request) {
        String error = ex.getParameterName() + " parameter is missing";

        return new ResponseEntity<>(new ErrorResponse(HttpStatus.BAD_REQUEST,
                ex.getLocalizedMessage(), error, LocalDate.now()),
                HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex, HttpHeaders headers,
            HttpStatusCode status, WebRequest request) {

        List<String> errors = List.of(ex.getMessage());

        return new ResponseEntity<>(new ErrorResponse(status, "Invalid Request Body", errors, LocalDate.now()), HttpStatus.BAD_REQUEST);

    }

}