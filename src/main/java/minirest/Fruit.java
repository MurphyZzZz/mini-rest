package minirest;

import lombok.val;
import minirest.annotations.GET;
import minirest.annotations.Path;
import minirest.annotations.RequestMethod;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;


@Path("/fruit")
public class Fruit implements Content{



    @Override
    public String getContent(String methodName, String uri) {
        try {
            val separateUri = uri.substring(0, uri.indexOf("/", 1));
            val declaredMethods = this.getClass().getDeclaredMethods();
            for (Method method: declaredMethods) {
                val declaredAnnotations = method.getDeclaredAnnotations();
                String annotationValue = "";
                boolean flag = true;
                for (Annotation annotation: declaredAnnotations) {
                    if (annotation.annotationType() == GET.class && method.getAnnotation(GET.class).value().equals(separateUri)) {
                        annotationValue = method.getAnnotation(GET.class).value();
                        break;
                    } else if (annotation.annotationType() == Path.class){
                        annotationValue = method.getAnnotation(Path.class).value();
                        break;
                    } else {
                        flag = false;
                        break;
                    }
                }
                if (flag) {
                    if (method.getReturnType() == String.class && annotationValue.equals(separateUri)) {
                        return (String) method.invoke(this);
                    } else {
                        val subResource = (Content) method.invoke(this);
                        val newUri = uri.replace(separateUri, "");
                        return subResource.getContent(methodName, newUri);
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "nothing found";
    }

    @GET("/quantity")
    public String getQuantity(){
        return "5";
    }

    @Path("/apple")
    public Apple getApple() {
        return new Apple();
    }

}
