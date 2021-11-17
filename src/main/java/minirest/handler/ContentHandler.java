package minirest.handler;

import container.Container;
import container.MiniDi;
import io.netty.handler.codec.http.FullHttpRequest;
import lombok.val;
import minirest.annotations.Path;
import minirest.exception.ResourceException;

import javax.inject.Inject;
import java.nio.charset.StandardCharsets;
import java.util.Collection;

import static minirest.handler.UriHandler.findNextSubString;

@MiniDi
public class ContentHandler {

    Container container;

    @Inject
    public ContentHandler(Container container) {
        this.container = container;
    }

    public String getContent(FullHttpRequest msg) {
        String uri = msg.uri();
        String separateUri = findNextSubString(uri);
        String requestBody = msg.content().toString(StandardCharsets.UTF_8);
        Collection<Class<?>> classes = container.getAllBeans();
        for (Class<?> clz : classes) {
            if (clz.isAnnotationPresent(Path.class) && clz.getAnnotation(Path.class).value().equals(separateUri)) {
                if (container.getBeanInstance(clz) instanceof Content) {
                    final Content content = ((Content) container.getBeanInstance(clz));
                    val newUri = uri.replace(separateUri, "");
                    return content.getContent(msg.method().name(), newUri, requestBody);
                } else {
                    throw new ResourceException();
                }
            }
        }
        return null;
    }
}
