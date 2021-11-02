package minirest.handler;

import container.Container;
import io.netty.handler.codec.http.FullHttpRequest;
import lombok.val;
import minirest.annotations.Path;
import minirest.exception.ResourceException;

import java.util.Collection;

public class ContentHandler {

    public static String getContent(Container container, FullHttpRequest msg, String uri, String separateUri) {
        Collection<Class<?>> classes = container.getAllBeans();
        for (Class<?> clz : classes) {
            if (clz.isAnnotationPresent(Path.class) && clz.getAnnotation(Path.class).value().equals(separateUri)) {
                if (container.getBean(clz) instanceof Content) {
                    final Content content = ((Content) container.getBean(clz));
                    val newUri = uri.replace(separateUri, "");
                    return content.getContent(msg.method().name(), newUri);
                } else {
                    throw new ResourceException();
                }
            }
        }
        return null;
    }
}
