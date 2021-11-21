package minirest.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import minirest.exception.DeserializeContentException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.util.UriTemplate;

import javax.ws.rs.Path;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static minirest.handler.UriHandler.getQueryParams;


public class ParameterHandler {

    static ObjectMapper objectMapper = new ObjectMapper();
    public static Object[] getRequestParameters(Method method, String separateUri, String requestBody) {
        String templateUri = method.getAnnotation(Path.class).value();
        Map<String, String> params = getPathVariable(separateUri, templateUri);
        params.putAll(getQueryParams(separateUri));
        Parameter[] parameters = method.getParameters();
        List<Object> arguments = new ArrayList<>();
        for (Parameter methodPara: parameters) {
            if (methodPara.isAnnotationPresent(RequestBody.class)) {
                try {
                    Object value = objectMapper.readValue(requestBody, methodPara.getType());
                    arguments.add(value);
                    continue;
                } catch (Exception e) {
                    throw new DeserializeContentException(requestBody);
                }
            }
            if (params.get(methodPara.getName()) == null) continue;
            Object value = castValue(methodPara, params.get(methodPara.getName()));
            arguments.add(value);
        }
        return arguments.toArray();
    }

    private static Map<String, String> getPathVariable(String uri, String templateUri) {
        UriTemplate template = new UriTemplate(templateUri);
        return template.match(uri);
    }

    private static Object castValue(Parameter param, String value) {

        if (param.getType().equals(int.class)) {
            return Integer.parseInt(value);
        } else return value;
    }
}
