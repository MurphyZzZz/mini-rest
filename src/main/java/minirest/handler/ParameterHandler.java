package minirest.handler;

import minirest.annotations.Path;
import org.springframework.web.util.UriTemplate;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static minirest.handler.UriHandler.getQueryParams;


public class ParameterHandler {
    public static Object[] getRequestParameters(Method method, String separateUri) {
        String templateUri = method.getAnnotation(Path.class).value();
        Map<String, String> params = getPathVariable(separateUri, templateUri);
        params.putAll(getQueryParams(separateUri));
        Parameter[] parameters = method.getParameters();
        List<Object> arguments = new ArrayList<>();
        for (Parameter param: parameters) {
            Object value = castValue(param, params.get(param.getName()));
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
