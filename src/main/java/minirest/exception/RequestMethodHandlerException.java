package minirest.exception;

public class RequestMethodHandlerException extends RuntimeException{
    public RequestMethodHandlerException() {
        super("Request method invoke exception.");
    }
}
