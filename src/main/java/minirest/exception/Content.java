package minirest.exception;

import java.lang.reflect.InvocationTargetException;

public interface Content {
    String getContent(String methodName, String uri) throws InvocationTargetException, IllegalAccessException;
}
