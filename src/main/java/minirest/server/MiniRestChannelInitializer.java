package minirest.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import minidi.container.MiniDi;
import minirest.handler.SimpleProcessingHandler;

import javax.inject.Inject;

@MiniDi
public class MiniRestChannelInitializer extends ChannelInitializer<SocketChannel> {


    SimpleProcessingHandler simpleProcessingHandler;

    @Inject
    public MiniRestChannelInitializer(SimpleProcessingHandler simpleProcessingHandler) {
        this.simpleProcessingHandler = simpleProcessingHandler;
    }

    @Override
    public void initChannel(SocketChannel ch) {
        ch.pipeline()
                .addLast("decoder", new HttpRequestDecoder())   // 1
                .addLast("encoder", new HttpResponseEncoder())  // 2
                .addLast("aggregator", new HttpObjectAggregator(512 * 1024))    // 3
                .addLast(simpleProcessingHandler);
    }
}
