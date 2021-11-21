package minirest.handler;

import container.Container;
import container.MiniDi;
import io.netty.handler.codec.http.FullHttpRequest;
import minirest.annotations.Path;
import minirest.exception.ResourceException;

import javax.inject.Inject;
import java.nio.charset.StandardCharsets;
import java.util.Collection;

import static minirest.handler.UriHandler.findNextSubString;

@MiniDi
public class RootResourceHandler {

    Container container;

    @Inject
    public RootResourceHandler(Container container) {
        this.container = container;
    }

    public String getContent(FullHttpRequest msg) {
        String uri = msg.uri();
        String separateUri = findNextSubString(uri);
        Content content = findRootResource(separateUri);
        String newUri = uri.replace(separateUri, "");
        String requestBody = msg.content().toString(StandardCharsets.UTF_8);
        return content.getContent(msg.method().name(), newUri, requestBody);
    }

    private Content findRootResource(String separateUri) {
        Collection<Class<?>> classes = container.getAllBeans();
        for (Class<?> clz : classes) {
            if (clz.isAnnotationPresent(Path.class) && clz.getAnnotation(Path.class).value().equals(separateUri)) {
                return new Resource(container.getBeanInstance(clz), clz);
            }
        }
        throw new ResourceException("Unable find root resource exception.");
    }
}
