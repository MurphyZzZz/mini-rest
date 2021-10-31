package minirest;

import com.google.common.collect.Iterables;
import lombok.val;
import minirest.annotations.GET;
import minirest.annotations.POST;
import minirest.annotations.PUT;
import minirest.annotations.Path;
import minirest.exception.GetContentException;

import java.lang.reflect.Method;
import java.util.Map;

import static minirest.resolver.UriResolver.getMatchedUri;
import static minirest.resolver.UriResolver.getPathVariable;
import static minirest.resolver.UriResolver.isUriMatchTemplate;

public interface Content {
    default String getContent(String methodName, String uri){
        try {
            val declaredMethods = this.getClass().getDeclaredMethods();
            for (Method method: declaredMethods) {
                if (!method.isAnnotationPresent(Path.class)) {
                    continue;
                }
                String templateUri = method.getAnnotation(Path.class).value();
                String separateUri = getMatchedUri(uri, templateUri);
                if (separateUri == null || !isUriMatchTemplate(separateUri, templateUri)) {
                    continue;
                }
                Map<String, String> pathParam = getPathVariable(separateUri, templateUri);
                if (method.isAnnotationPresent(GET.class) && methodName.equals("GET")) {
                    return (String) method.invoke(this, (Object[]) Iterables.toArray(pathParam.values(), String.class));
                } else if (method.isAnnotationPresent(POST.class) && methodName.equals("POST")) {
                    return (String) method.invoke(this, (Object[]) Iterables.toArray(pathParam.values(), String.class));
                } else if (method.isAnnotationPresent(PUT.class) && methodName.equals("PUT")) {
                    return (String) method.invoke(this, (Object[]) Iterables.toArray(pathParam.values(), String.class));
                } else {
                    val subResource = (Content) method.invoke(this, (Object[]) Iterables.toArray(pathParam.values(), String.class));
                    val newUri = uri.replace(separateUri, "");
                    return subResource.getContent(methodName, newUri);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new GetContentException("Some thing went wrong when getting content.");
        }
        return "nothing found";
    }
}
