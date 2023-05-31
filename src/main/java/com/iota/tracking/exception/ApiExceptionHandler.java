package com.iota.tracking.exception;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.springframework.http.HttpStatus.*;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Handle MissingServletRequestParameterException. Triggered when a 'required' request parameter is missing.
     *
     * @param ex      MissingServletRequestParameterException
     * @param headers HttpHeaders
     * @param status  HttpStatus
     * @param request WebRequest
     * @return the ApiError object
     */
    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            MissingServletRequestParameterException ex, HttpHeaders headers,
            HttpStatus status, WebRequest request) {
        String error = ex.getParameterName() + " parameter is missing";
        return buildResponseEntity(new ApiException(BAD_REQUEST, error, ZonedDateTime.now(ZoneId.of("Z"))));
    }

    /**
     * Handle HttpMediaTypeNotSupportedException. This one triggers when JSON is invalid as well.
     *
     * @param ex      HttpMediaTypeNotSupportedException
     * @param headers HttpHeaders
     * @param status  HttpStatus
     * @param request WebRequest
     * @return the ApiError object
     */
    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(
            HttpMediaTypeNotSupportedException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        StringBuilder builder = new StringBuilder();
        builder.append(ex.getContentType());
        builder.append(" media type is not supported. Supported media types are ");
        ex.getSupportedMediaTypes().forEach(t -> builder.append(t).append(", "));
        return buildResponseEntity(new ApiException(HttpStatus.UNSUPPORTED_MEDIA_TYPE, builder.substring(0, builder.length() - 2), ZonedDateTime.now(ZoneId.of("Z"))));
    }

    /**
     * Handle MethodArgumentNotValidException. Triggered when an object fails @Valid validation.
     *
     * @param ex      the MethodArgumentNotValidException that is thrown when @Valid validation fails
     * @param headers HttpHeaders
     * @param status  HttpStatus
     * @param request WebRequest
     * @return the ApiError object
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        ApiException apiError = new ApiException(BAD_REQUEST);
        apiError.setMessage("Validation error");
        System.out.println(ex.getBindingResult().getFieldErrors());
        System.out.println(ex.getBindingResult().getGlobalErrors());
        apiError.addValidationErrors(ex.getBindingResult().getFieldErrors());
        apiError.addValidationError(ex.getBindingResult().getGlobalErrors());
        return buildResponseEntity(apiError);
    }

    /**
     * Handles javax.validation.ConstraintViolationException. Thrown when @Validated fails.
     *
     * @param ex the ConstraintViolationException
     * @return the ApiError object
     */
    @ExceptionHandler(javax.validation.ConstraintViolationException.class)
    protected ResponseEntity<Object> handleConstraintViolation(
            javax.validation.ConstraintViolationException ex) {
        ApiException apiError = new ApiException(BAD_REQUEST);
        apiError.setMessage("Validation error1");
        apiError.addValidationErrors(ex.getConstraintViolations());
        return buildResponseEntity(apiError);
    }

    /**
     * Handles EntityNotFoundException. Created to encapsulate errors with more detail than javax.persistence.EntityNotFoundException.
     *
     * @param ex the EntityNotFoundException
     * @return the ApiError object
     */
    @ExceptionHandler(EntityNotFoundException.class)
    protected ResponseEntity<Object> handleEntityNotFoundException(
            EntityNotFoundException ex) {
        ApiException apiError = new ApiException(NOT_FOUND);
        apiError.setMessage(ex.getMessage());
        return buildResponseEntity(apiError);
    }

    /**
     * Handles EntityExistsException. Created to encapsulate errors with more detail than javax.persistence.EntityNotFoundException.
     *
     * @param ex the EntityExistsException
     * @return the ApiError object
     */
    @ExceptionHandler(EntityExistsException.class)
    protected ResponseEntity<Object> handleEntityExistsException(
            EntityExistsException ex) {
        ApiException apiError = new ApiException(CONFLICT);
        apiError.setMessage(ex.getMessage());
        return buildResponseEntity(apiError);
    }

    /**
     * Handles AuthenticationException. Created to encapsulate errors with more detail than javax.persistence.EntityNotFoundException.
     *
     * @param ex the AuthenticationException
     * @return the ApiError object
     */
    @ExceptionHandler(AuthenticationException.class)
    protected ResponseEntity<Object> handleAuthenticationException(
            AuthenticationException ex) {
        ApiException apiError = new ApiException(UNAUTHORIZED);
        apiError.setMessage(ex.getMessage());
        return buildResponseEntity(apiError);
    }

    /**
     * Handles AuthenticationException. Created to encapsulate errors with more detail than javax.persistence.EntityNotFoundException.
     *
     * @param ex the AuthenticationException
     * @return the ApiError object
     */
    @ExceptionHandler(PaymentRequiredException.class)
    protected ResponseEntity<Object> handleAuthenticationException(
            PaymentRequiredException ex) {
        ApiException apiError = new ApiException(PAYMENT_REQUIRED);
        apiError.setMessage(ex.getMessage());
        return buildResponseEntity(apiError);
    }

    /**
     * Handles CustomUnprocessableEntityException. Created to encapsulate errors with more detail than javax.persistence.EntityNotFoundException.
     *
     * @param ex the CustomUnprocessableEntityException
     * @return the ApiError object
     */
    @ExceptionHandler(UnprocessableEntityException.class)
    protected ResponseEntity<Object> handleCustomUnprocessableEntityException(
            UnprocessableEntityException ex) {
        ApiException apiError = new ApiException(UNAUTHORIZED);
        apiError.setMessage(ex.getMessage());
        return buildResponseEntity(apiError);
    }

    /**
     * Handles BadCredentialsException. Created to encapsulate errors with more detail than javax.persistence.EntityNotFoundException.
     *
     * @param ex the BadCredentialsException
     * @return the ApiError object
     */
    @ExceptionHandler(BadCredentialsException.class)
    protected ResponseEntity<Object> handleBadCredentialsException(
            BadCredentialsException ex) {
        ApiException apiError = new ApiException(UNAUTHORIZED);
        apiError.setMessage(ex.getMessage());
        return buildResponseEntity(apiError);
    }

    /**
     * Handles BadRequestException. Created to encapsulate errors with more detail than javax.persistence.EntityNotFoundException.
     *
     * @param ex the BadRequestException
     * @return the ApiError object
     */
    @ExceptionHandler(BadRequestException.class)
    protected ResponseEntity<Object> handleBadCredentialsException(
            BadRequestException ex) {
        ApiException apiError = new ApiException(BAD_REQUEST);
        apiError.setMessage(ex.getMessage());
        return buildResponseEntity(apiError);
    }

    /**
     * Handle HttpMessageNotReadableException. Happens when request JSON is malformed.
     *
     * @param ex      HttpMessageNotReadableException
     * @param headers HttpHeaders
     * @param status  HttpStatus
     * @param request WebRequest
     * @return the ApiError object
     */
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ServletWebRequest servletWebRequest = (ServletWebRequest) request;
        //logger.info("{} to {}", servletWebRequest.getHttpMethod(), servletWebRequest.getRequest().getServletPath());
        String error = "Malformed JSON request";
        return buildResponseEntity(new ApiException(HttpStatus.BAD_REQUEST, error, ZonedDateTime.now()));
    }

    /**
     * Handle HttpMessageNotWritableException.
     *
     * @param ex      HttpMessageNotWritableException
     * @param headers HttpHeaders
     * @param status  HttpStatus
     * @param request WebRequest
     * @return the ApiError object
     */
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotWritable(HttpMessageNotWritableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String error = "Error writing JSON output";
        return buildResponseEntity(new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, error, ZonedDateTime.now()));
    }

    /**
     * Handle NoHandlerFoundException.
     *
     * @param ex
     * @param headers
     * @param status
     * @param request
     * @return
     */
    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(
            NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ApiException apiError = new ApiException(BAD_REQUEST);
        apiError.setMessage(String.format("Could not find the %s method for URL %s", ex.getHttpMethod(), ex.getRequestURL()));
        apiError.setDebugMessage(ex.getMessage());
        return buildResponseEntity(apiError);
    }

    /**
     * Handle javax.persistence.EntityNotFoundException
     */
    @ExceptionHandler(javax.persistence.EntityNotFoundException.class)
    protected ResponseEntity<Object> handleEntityNotFound(javax.persistence.EntityNotFoundException ex) {
        return buildResponseEntity(new ApiException(HttpStatus.NOT_FOUND, ex.getMessage(), ZonedDateTime.now()));
    }

    /**
     * Handle DataIntegrityViolationException, inspects the cause for different DB causes.
     *
     * @param ex the DataIntegrityViolationException
     * @return the ApiError object
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    protected ResponseEntity<Object> handleDataIntegrityViolation(DataIntegrityViolationException ex,
                                                                  WebRequest request) {
        if (ex.getCause() instanceof ConstraintViolationException) {
            return buildResponseEntity(new ApiException(HttpStatus.CONFLICT, "Database error", ex.getCause()));
        }
        return buildResponseEntity(new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, ex));
    }

    /**
     * Handle Exception, handle generic Exception.class
     *
     * @param ex the Exception
     * @return the ApiError object
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex,
                                                                      WebRequest request) {
        ApiException apiError = new ApiException(BAD_REQUEST);
        apiError.setMessage(String.format("The parameter '%s' of value '%s' could not be converted to type '%s'", ex.getName(), ex.getValue(), ex.getRequiredType().getSimpleName()));
        apiError.setDebugMessage(ex.getMessage());
        return buildResponseEntity(apiError);
    }


    private ResponseEntity<Object> buildResponseEntity(ApiException apiError) {
        return new ResponseEntity<>(apiError, apiError.getHttpStatus());
    }


//    @ExceptionHandler(value = {AuthenticationException.class})
//    public ResponseEntity<Object> handleAuthenticationException(AuthenticationException e){
//        ApiException apiException = new ApiException(e.getMessage(), HttpStatus.UNAUTHORIZED, ZonedDateTime.now(ZoneId.of("Z")));
//        return new ResponseEntity<>(apiException, HttpStatus.UNAUTHORIZED);
//    }
//
//    @ExceptionHandler(value = {BadRequestException.class})
//    public ResponseEntity<Object> handleBadRequestException(BadRequestException e){
//        ApiException apiException = new ApiException(e.getMessage(), HttpStatus.BAD_REQUEST, ZonedDateTime.now(ZoneId.of("Z")));
//        return new ResponseEntity<>(apiException, HttpStatus.BAD_REQUEST);
//    }
//
//    @ExceptionHandler(value = {EntityExistsException.class})
//    public ResponseEntity<Object> handleEntityExistsException(EntityExistsException e){
//        ApiException apiException = new ApiException(e.getMessage(), HttpStatus.CONFLICT, ZonedDateTime.now(ZoneId.of("Z")));
//        return new ResponseEntity<>(apiException, HttpStatus.CONFLICT);
//    }
//
//    @ExceptionHandler(value = {EntityNotFoundException.class})
//    public ResponseEntity<Object> handleEntityNotFoundException(EntityNotFoundException e){
//        ApiException apiException = new ApiException(e.getMessage(), HttpStatus.NOT_FOUND, ZonedDateTime.now(ZoneId.of("Z")));
//        return new ResponseEntity<>(apiException, HttpStatus.NOT_FOUND);
//    }
//
//    @ExceptionHandler(value = {UnprocessableEntityException.class})
//    public ResponseEntity<Object> handleUnprocessableEntityException(UnprocessableEntityException e){
//        ApiException apiException = new ApiException(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY, ZonedDateTime.now(ZoneId.of("Z")));
//        return new ResponseEntity<>(apiException, HttpStatus.UNPROCESSABLE_ENTITY);
//    }
//
//    @ExceptionHandler(BadCredentialsException.class)
//    protected ResponseEntity<Object> handleBadCredentialsException(
//            BadCredentialsException ex) {
//        ApiException apiException = new ApiException(ex.getMessage(), UNAUTHORIZED, ZonedDateTime.now(ZoneId.of("Z")));
//        return new ResponseEntity<>(apiException, HttpStatus.UNPROCESSABLE_ENTITY);
//    }
//    @ExceptionHandler(PaymentRequiredException.class)
//    protected ResponseEntity<Object> handlePaymentRequiredException(
//            PaymentRequiredException ex) {
//        ApiException apiException = new ApiException(ex.getMessage(), PAYMENT_REQUIRED, ZonedDateTime.now(ZoneId.of("Z")));
//        return new ResponseEntity<>(apiException, PAYMENT_REQUIRED);
//    }

}
