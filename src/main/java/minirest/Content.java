package minirest;

import com.google.common.collect.Iterables;
import lombok.val;
import minirest.annotations.GET;
import minirest.annotations.POST;
import minirest.annotations.PUT;
import minirest.annotations.Path;
import minirest.exception.GetContentException;
import org.springframework.web.util.UriTemplate;

import java.lang.reflect.Method;
import java.util.Map;

public interface Content {
    default String getContent(String methodName, String uri){
        try {
            String separateUri;
            if (uri.indexOf("/", 1) == -1) {
                separateUri = uri;
            }
            val declaredMethods = this.getClass().getDeclaredMethods();
            for (Method method: declaredMethods) {
                if (!method.isAnnotationPresent(Path.class)) {
                    continue;
                }
                String templateUri = method.getAnnotation(Path.class).value();
                separateUri = getMatchedUri(uri, templateUri);
                if (separateUri == null || !isUriMatchTemplate(separateUri, templateUri)) {
                    continue;
                }
                Map<String, String> pathParam = getPathVariable(separateUri, templateUri);
                if (method.isAnnotationPresent(GET.class) && methodName.equals("GET")) {
                    return (String) method.invoke(this, Iterables.toArray(pathParam.values(), String.class));
                } else if (method.isAnnotationPresent(POST.class) && methodName.equals("POST")) {
                    return (String) method.invoke(this, Iterables.toArray(pathParam.values(), String.class));
                } else if (method.isAnnotationPresent(PUT.class) && methodName.equals("PUT")) {
                    return (String) method.invoke(this, Iterables.toArray(pathParam.values(), String.class));
                } else {
                    val subResource = (Content) method.invoke(this, pathParam.keySet());
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
    private boolean isUriMatchTemplate(String uri, String templateUri) {
        UriTemplate template = new UriTemplate(templateUri);
        return template.matches(uri);
    }


    private Map<String, String> getPathVariable(String uri, String templateUri) {
        UriTemplate template = new UriTemplate(templateUri);
        return template.match(uri);
    }

    private String getMatchedUri(String uri, String templateUri) {
        int templateIndex = templateUri.indexOf("/");
        int numOfSlashInTemplateUri = 0;
        while (templateIndex >= 0) {
            numOfSlashInTemplateUri++;
            templateIndex = templateUri.indexOf("/", templateIndex + 1);
        }
        int uriIndex = uri.indexOf("/");
        int numOfSlashInUri = 0;
        while (uriIndex >= 0 && (numOfSlashInUri < numOfSlashInTemplateUri)) {
            numOfSlashInUri += 1;
            uriIndex = uri.indexOf("/", uriIndex + 1);
        }
        if (numOfSlashInUri < numOfSlashInTemplateUri) {
            return null;
        } else {
            if (uriIndex < 0) return uri;
            return uri.substring(0, uriIndex);
        }
    }
}
