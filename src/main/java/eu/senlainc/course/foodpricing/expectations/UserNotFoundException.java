package eu.senlainc.course.foodpricing.expectations;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String message) {
        super(message);
    }
}