package com.geekbrains.cloud.client;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.stream.ChunkedWriteHandler;

import static com.geekbrains.cloud.common.protocol.GeekCloudConsts.CHUNK_SIZE;

public class GeekCloudClientInitializer extends ChannelInitializer<SocketChannel> {
	private final SslContext sslCtx;

	public GeekCloudClientInitializer(SslContext sslCtx) {
		this.sslCtx = sslCtx;
	}

	@Override
	protected void initChannel(SocketChannel ch) {
		ChannelPipeline p = ch.pipeline();

		if(sslCtx != null)
			p.addLast("ssl", sslCtx.newHandler(ch.alloc(), GeekCloudClient.HOST, GeekCloudClient.PORT));

		p.addLast("decoder", new LengthFieldBasedFrameDecoder(CHUNK_SIZE, 0, 4));
		p.addLast("encoder", new LengthFieldPrepender(4));
		p.addLast("chunker", new ChunkedWriteHandler());
		p.addLast("client", new GeekCloudClientHandler());
	}
}
