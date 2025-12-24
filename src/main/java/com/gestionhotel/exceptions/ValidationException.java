package main.java.com.gestionhotel.exceptions;

public class ValidationException extends HotelException {

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
