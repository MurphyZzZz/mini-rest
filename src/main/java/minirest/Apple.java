package minirest;

import lombok.val;
import minirest.annotations.GET;
import minirest.annotations.POST;
import minirest.annotations.PUT;
import minirest.annotations.Path;
import minirest.exception.Content;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;


@Path("/apple")
public class Apple implements Content {



    @Override
    public String getContent(String methodName, String uri) {
        try {
            String separateUri;
            if (uri.indexOf("/", 1) == -1) {
                separateUri = uri;
            } else {
                separateUri = uri.substring(0, uri.indexOf("/", 1));
            }
            val declaredMethods = this.getClass().getDeclaredMethods();
            for (Method method: declaredMethods) {
                if (!method.isAnnotationPresent(Path.class)) {
                    continue;
                }
                String presentedUri = method.getAnnotation(Path.class).value();
                if (presentedUri.equals(separateUri) && method.isAnnotationPresent(GET.class) && methodName.equals("GET")) {
                    return (String) method.invoke(this);
                } else if (presentedUri.equals(separateUri) && method.isAnnotationPresent(POST.class) && methodName.equals("POST")) {
                    return (String) method.invoke(this);
                } else if (presentedUri.equals(separateUri) && method.isAnnotationPresent(PUT.class) && methodName.equals("PUT")) {
                    return (String) method.invoke(this);
                } else if (presentedUri.equals(separateUri)) {
                    val subResource = (Content) method.invoke(this);
                    val newUri = uri.replace(separateUri, "");
                    return subResource.getContent(methodName, newUri);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new GetContentException("nothing found");
        }
        return "nothing found";
    }

    @GET
    @Path("/name")
    public String getFruitName(){
        return "Apple";
    }
}
