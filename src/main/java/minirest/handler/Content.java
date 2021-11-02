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

public interface Content {
    default String getContent(String methodNameInRequest, String uri){
        try {
            val methods = this.getClass().getMethods();
            for (Method method: methods) {
                if (!isMatchedHandlingMethod(uri, method)) continue;

                String templateUri = method.getAnnotation(Path.class).value();
                String separateUri = getMatchedUri(uri, templateUri);
                Object[] args = getRequestParameters(method, separateUri);

                return getStringContent(methodNameInRequest, uri, method, separateUri, args);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new GetContentException("Some thing went wrong when getting content.");
        }
        return "nothing found";
    }

    private String getStringContent(String methodNameInRequest, String uri, Method method, String separateUri, Object[] args) throws IllegalAccessException, InvocationTargetException {
        String content = requestMethodHandler(method, methodNameInRequest, args);
        if (content != null) {
            return content;
        } else {
            return subResourceHandler(methodNameInRequest, uri, method, separateUri, args);
        }
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
