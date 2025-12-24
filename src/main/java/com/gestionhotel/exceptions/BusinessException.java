package main.java.com.gestionhotel.exceptions;

public class BusinessException extends HotelException {

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }
}
