package minirest;

import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpVersion;
import org.junit.jupiter.api.Test;

import java.nio.charset.Charset;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ServerTest {

    @Test
    void should_dispatch_request_to_resource_class_given_path_annotation() {
        EmbeddedChannel channel = new EmbeddedChannel(new SimpleProcessingHandler());
        FullHttpRequest httpRequest = new DefaultFullHttpRequest(
                HttpVersion.HTTP_1_1, HttpMethod.GET, "/book/content");

        channel.writeInbound(httpRequest);
        FullHttpResponse httpResponse = channel.readOutbound();
        String httpResponseContent = httpResponse.content().toString(Charset.defaultCharset());
        assertEquals("This is a book.", httpResponseContent);
    }
}
