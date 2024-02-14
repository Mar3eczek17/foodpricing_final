package eu.senlainc.course.foodpricing.expectations;

public class CustomMessagingException extends RuntimeException {

    public CustomMessagingException(String message) {
        super(message);
    }

    public CustomMessagingException(String message, Throwable cause) {
        super(message, cause);
    }
}