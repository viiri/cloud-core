package com.geekbrains.cloud.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;

import static com.geekbrains.cloud.common.protocol.GeekCloudConsts.CHUNK_SIZE;

public class GeekCloudServerInitializer extends ChannelInitializer<SocketChannel> {
	private final SslContext sslCtx;

	public GeekCloudServerInitializer(SslContext sslCtx) {
		this.sslCtx = sslCtx;
	}

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
				ChannelPipeline p = ch.pipeline();
				if(sslCtx != null)
					p.addLast(sslCtx.newHandler(ch.alloc()));

				p.addLast("idler", new IdleStateHandler(GeekCloudServer.READ_TIMEOUT, 0, 0));
				p.addLast("decoder", new LengthFieldBasedFrameDecoder(CHUNK_SIZE, 0, 4));
				p.addLast("encoder", new LengthFieldPrepender(4));
				p.addLast("chunker", new ChunkedWriteHandler());
				p.addLast("server", new GeekCloudServerHandler());
	}
}
