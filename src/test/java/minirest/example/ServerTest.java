package minirest.example;

import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpVersion;
import minidi.container.Container;
import minirest.handler.RootResourceHandler;
import minirest.handler.SimpleProcessingHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.charset.Charset;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ServerTest {

    Container container = new Container(this.getClass().getPackageName());
    RootResourceHandler rootResourceHandler = new RootResourceHandler(container);

    @BeforeEach
    void setUp() {
        container.lunch();
    }

    @Test
    void should_dispatch_request_to_resource_class_given_path_annotation() {
        EmbeddedChannel channel = new EmbeddedChannel(new SimpleProcessingHandler(rootResourceHandler));
        FullHttpRequest httpRequest = new DefaultFullHttpRequest(
                HttpVersion.HTTP_1_1, HttpMethod.GET, "/book/content");

        channel.writeInbound(httpRequest);
        FullHttpResponse httpResponse = channel.readOutbound();
        String httpResponseContent = httpResponse.content().toString(Charset.defaultCharset());
        assertEquals("This is a book.", httpResponseContent);
    }

    @Test
    void should_return_correct_content_given_path_annotation_and_query_string() {
        EmbeddedChannel channel = new EmbeddedChannel(new SimpleProcessingHandler(rootResourceHandler));
        FullHttpRequest httpRequest = new DefaultFullHttpRequest(
                HttpVersion.HTTP_1_1, HttpMethod.GET, "/fruit/name?nickName=1");

        channel.writeInbound(httpRequest);
        FullHttpResponse httpResponse = channel.readOutbound();
        String httpResponseContent = httpResponse.content().toString(Charset.defaultCharset());
        assertEquals("My nickName is wonderful fruit!", httpResponseContent);
    }

    @Test
    void should_return_correct_content_given_path_annotation_and_path_param() {
        EmbeddedChannel channel = new EmbeddedChannel(new SimpleProcessingHandler(rootResourceHandler));
        FullHttpRequest httpRequest = new DefaultFullHttpRequest(
                HttpVersion.HTTP_1_1, HttpMethod.GET, "/fruit/type/1");

        channel.writeInbound(httpRequest);
        FullHttpResponse httpResponse = channel.readOutbound();
        String httpResponseContent = httpResponse.content().toString(Charset.defaultCharset());
        assertEquals("This is type 1 - Pear.", httpResponseContent);
    }
}
