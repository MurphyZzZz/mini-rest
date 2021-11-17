package minirest.exception;

public class DeserializeContentException extends RuntimeException{
    public DeserializeContentException(String requestBody) {
        super("Unable to deserialize request body: " + requestBody);
    }
}
