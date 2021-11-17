package minirest.handler;

import container.Container;
import container.MiniDi;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import minirest.exception.GetContentException;

import javax.inject.Inject;
import java.nio.charset.StandardCharsets;

@MiniDi
@ChannelHandler.Sharable
public class SimpleProcessingHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    ContentHandler contentHandler;

    @Inject
    public SimpleProcessingHandler(ContentHandler contentHandler) {
        this.contentHandler = contentHandler;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        System.out.println("Connected!");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) {

        System.out.println("Recieved request!");
        System.out.println("HTTP Method: " + msg.method());
        System.out.println("HTTP Version: " + msg.protocolVersion());
        System.out.println("URI: " + msg.uri());
        System.out.println("Headers: " + msg.headers());
        System.out.println("Trailing headers: " + msg.trailingHeaders());

        String requestBody = msg.content().toString(StandardCharsets.UTF_8);
        System.out.println("Request body: " + requestBody);

        String responseContent = contentHandler.getContent(msg);

        if (msg.method() == HttpMethod.POST) {
            ByteBuf data = msg.content();
            System.out.println("POST/PUT length: " + data.readableBytes());
            System.out.println("POST/PUT as string: ");
            System.out.println("-- DATA --");
            System.out.println(data.toString(StandardCharsets.UTF_8));
            System.out.println("-- DATA END --");
        }

        // Send response back so the browser won't timeout
        if (responseContent == null) {
            throw new GetContentException("Unable to find content.");
        }
        ByteBuf responseBytes = ctx.alloc().buffer();
        responseBytes.writeBytes(responseContent.getBytes());

        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, responseBytes);
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
        response.headers().set(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());

        response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
        ctx.write(response);
    }

    /*
    channel读取完成之后需要输出缓冲流。如果没有这一步，会发现客户端会一直在刷新。
    */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelReadComplete");
        super.channelReadComplete(ctx);
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("exceptionCaught");
        if (null != cause) cause.printStackTrace();
        if (null != ctx) ctx.close();
    }
}
