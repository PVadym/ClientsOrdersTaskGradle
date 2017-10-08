package ua.com.cashup.application.errors;

/**
 * Created by Вадим on 06.10.2017.
 */
public class ApplicationError {

    private String message;

    public ApplicationError(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
