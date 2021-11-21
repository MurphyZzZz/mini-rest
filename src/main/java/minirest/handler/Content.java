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
import static minirest.handler.UriHandler.isSubstringMatched;

public abstract class Content {
    Object resource;
    Class<?> resourceClz;

    public Content(Object resource, Class<?> resourceClz) {
        this.resource = resource;
        this.resourceClz = resourceClz;
    }

    List<Class<? extends Annotation>> requestMethodClz = List.of(GET.class, POST.class, PUT.class);

    public String getContent(String methodNameInRequest, String uri, String requestBody) {
        try {
            val methods = resourceClz.getMethods();
            for (Method method : methods) {
                if (!isMatchedHandlingMethod(uri, method)) continue;

                String templateUri = method.getAnnotation(Path.class).value();
                String separateUri = getMatchedUri(uri, templateUri);
                Object[] args = getRequestParameters(method, separateUri, requestBody);

                return getStringContent(methodNameInRequest, uri, method, separateUri, args, requestBody);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new GetContentException("Some thing went wrong when getting content.");
        }
        return "Nothing found, something may go wrong.";
    }

    private String getStringContent(String methodNameInRequest, String uri, Method method, String separateUri, Object[] args, String requestBody) throws IllegalAccessException, InvocationTargetException {
        if (requestMethodClz.stream().anyMatch(method::isAnnotationPresent)) {
            return requestMethodHandler(method, methodNameInRequest, args);
        }
        return subResourceHandler(methodNameInRequest, uri, method, separateUri, args, requestBody);
    }

    private boolean isMatchedHandlingMethod(String uri, Method method) {
        if (!isAnnotationPathPresent(method)) return false;
        if (isUriMatchTemplateUrl(uri, method) == null) return false;
        return true;
    }

    private String isUriMatchTemplateUrl(String uri, Method method) {
        String templateUri = method.getAnnotation(Path.class).value();
        String separateUri = getMatchedUri(uri, templateUri);
        if (separateUri == null || !isSubstringMatched(separateUri, templateUri)) {
            return null;
        }
        return templateUri;
    }

    private boolean isAnnotationPathPresent(Method method) {
        return method.isAnnotationPresent(Path.class);
    }

    private String subResourceHandler(String methodNameInRequest, String uri, Method method, String separateUri, Object[] args, String requestBody) throws IllegalAccessException, InvocationTargetException {
        Resource subResource = new Resource(method.invoke(resource, args), method.invoke(resource, args).getClass());
        String newUri = getNewUri(uri, separateUri);
        return subResource.getContent(methodNameInRequest, newUri, requestBody);
    }

    private String getNewUri(String uri, String separateUri) {
        String subPath;
        if (separateUri.indexOf("?") > 0)
            subPath = separateUri.substring(0, separateUri.indexOf("?"));
        else
            subPath = separateUri;
        return uri.replace(subPath, "");
    }

    private String requestMethodHandler(Method method, String methodNameInRequest, Object[] args) {
        try {
            for (Class<? extends Annotation> clz : requestMethodClz) {
                if (methodNameInRequest.equals(clz.getSimpleName())) {
                    return (String) method.invoke(resource, args);
                }
            }
        } catch (InvocationTargetException | IllegalAccessException exception) {
            throw new RequestMethodHandlerException();
        }
        throw new GetContentException("No content found.");
    }
}
