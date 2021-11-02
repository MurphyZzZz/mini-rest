package minirest.handler;

import lombok.val;
import minirest.annotations.GET;
import minirest.annotations.POST;
import minirest.annotations.PUT;
import minirest.annotations.Path;
import minirest.exception.GetContentException;
import minirest.exception.RequestMethodHandlerException;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import static minirest.handler.ParameterHandler.getRequestParameters;
import static minirest.handler.UriHandler.getMatchedUri;
import static minirest.handler.UriHandler.isUriMatchTemplate;

public interface Content {
    default String getContent(String methodNameInRequest, String uri){
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

                Object[] args = getRequestParameters(method, separateUri);

                String content = requestMethodHandler(method, methodNameInRequest, args);
                if (content != null) {
                    return content;
                } else {
                    return subResourceHandler(methodNameInRequest, uri, method, separateUri, args);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new GetContentException("Some thing went wrong when getting content.");
        }
        return "nothing found";
    }

    private String subResourceHandler(String methodNameInRequest, String uri, Method method, String separateUri, Object[] args) throws IllegalAccessException, InvocationTargetException {
        val subResource = (Content) method.invoke(this, args);
        val newUri = uri.replace(separateUri, "");
        return subResource.getContent(methodNameInRequest, newUri);
    }

    private String requestMethodHandler(Method method, String methodNameInRequest, Object[] args)  {
        List<Class<? extends Annotation>> requestMethodClz = List.of(
                GET.class, POST.class, PUT.class
        );
        try {
            for (Class<? extends Annotation> clz: requestMethodClz) {
                if (method.isAnnotationPresent(clz) && methodNameInRequest.equals(clz.getSimpleName())) {
                    return (String) method.invoke(this, args);
                }
            }
        } catch (InvocationTargetException | IllegalAccessException exception) {
            throw new RequestMethodHandlerException();
        }
        return null;
    }
}
