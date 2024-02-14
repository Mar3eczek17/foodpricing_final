package eu.senlainc.course.foodpricing.expectations;

import eu.senlainc.course.foodpricing.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final String BATCH_UPLOAD_ERROR_MESSAGE = "Batch upload error: ";
    private static final String PRODUCT_NOT_FOUND_ERROR_MESSAGE = "Product not found: ";
    private static final String USER_NOT_FOUND_ERROR_MESSAGE = "User not found: ";
    private static final String USER_ALREADY_EXISTS_ERROR_MESSAGE = "User already exists: ";
    private static final String CUSTOM_MESSAGING_EXCEPTION_ERROR_MESSAGE = "Custom messaging exception: ";
    private static final String GENERIC_ERROR_MESSAGE = "Error processing the request";

    @ExceptionHandler(BatchUploadException.class)
    public ResponseEntity<ApiResponse> handleBatchUploadException(BatchUploadException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse(BATCH_UPLOAD_ERROR_MESSAGE + e.getMessage()));
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ApiResponse> handleProductNotFoundException(ProductNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse(PRODUCT_NOT_FOUND_ERROR_MESSAGE + e.getMessage()));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiResponse> handleUserNotFoundException(UserNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse(USER_NOT_FOUND_ERROR_MESSAGE + e.getMessage()));
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ApiResponse> handleUserAlreadyExistsException(UserAlreadyExistsException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ApiResponse(USER_ALREADY_EXISTS_ERROR_MESSAGE + e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiResponse> handleException(Exception e) {
        ApiResponse response = new ApiResponse(GENERIC_ERROR_MESSAGE);
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(CustomMessagingException.class)
    public ResponseEntity<ApiResponse> handleCustomMessagingException(CustomMessagingException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse(CUSTOM_MESSAGING_EXCEPTION_ERROR_MESSAGE + e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity.badRequest().body(errors);
    }
}