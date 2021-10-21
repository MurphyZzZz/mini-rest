package minirest;

import container.Container;
import minirest.annotations.Path;
import org.reflections.Reflections;

import java.util.HashMap;
import java.util.Map;

public class ResourceResolver {
    final String packageName = "minirest";
    private final Reflections reflections = new Reflections(packageName);
    Container container = new Container();

    // 1. find resource class
    // 2. go into deep to search for response (composite)

    // uri is the first resource uri
    private Map<String, Class<?>> scanPackages(String uri) {
        Map<String, Class<?>> pathUriAndClass = new HashMap<>();
        reflections.getTypesAnnotatedWith(Path.class).forEach(
                (Class<?> it) -> pathUriAndClass.put(it.getAnnotation(Path.class).value(), it)
        );
        Class<?> resourceClz = pathUriAndClass.get(uri);
        // need to refactor
        container.lunch();
        Object resource = container.getBean(resourceClz);

        return pathUriAndClass;
    }
}
