package minirest;

import lombok.val;
import minirest.annotations.GET;
import minirest.annotations.Path;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;


@Path("/apple")
public class Apple implements Content {



    @Override
    public String getContent(String methodName, String uri) {
        try {
            val separateUri = uri;
            val declaredMethods = this.getClass().getDeclaredMethods();
            for (Method method: declaredMethods) {
                val declaredAnnotations = method.getDeclaredAnnotations();
                String annotationValue = "";
                for (Annotation annotation: declaredAnnotations) {
                    if (annotation.annotationType() == GET.class && method.getAnnotation(GET.class).value().equals(separateUri)) {
                        annotationValue = method.getAnnotation(GET.class).value();
                    } else {
                        annotationValue = method.getAnnotation(Path.class).value();
                    }
                }
                if (method.getReturnType() == String.class && annotationValue.equals(separateUri)) {
                    return (String) method.invoke(this);
                } else {
                    val subResource = (Content) method.invoke(this);
                    val newUri = uri.replace(separateUri, "");
                    return subResource.getContent(methodName, newUri);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "nothing found";
    }

    @GET("/name")
    public String getFruitName(){
        return "Apple";
    }
}
