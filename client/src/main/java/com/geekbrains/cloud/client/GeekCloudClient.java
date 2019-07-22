package com.geekbrains.cloud.client;


import com.geekbrains.cloud.common.protocol.GeekCloudSender;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;
import io.netty.util.AttributeKey;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

public class GeekCloudClient {
	private static final boolean SSL = System.getProperty("ssl") != null;
	static final String HOST = System.getProperty("host", "127.0.0.1");
	static final int PORT = Integer.parseInt(System.getProperty("port", SSL ? "8992" : "8023"));

	public static final AttributeKey<String> CURRENT_FILENAME = AttributeKey.valueOf("fileName");
	public static final AttributeKey<Long> CURRENT_FILELENGTH = AttributeKey.valueOf("fileLength");
	public static final AttributeKey<FileOutputStream> CURRENT_FILESTREAM = AttributeKey.valueOf("fileStream");

	public static void main(String[] args) throws Exception {
		final SslContext sslCtx;
		if(SSL) {
			SelfSignedCertificate ssc = new SelfSignedCertificate();
			sslCtx = SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey()).build();
		} else {
			sslCtx = null;
		}

		EventLoopGroup group = new NioEventLoopGroup();

		try {
			Bootstrap b = new Bootstrap();
			b.group(group);
			b.channel(NioSocketChannel.class);
			b.option(ChannelOption.SO_KEEPALIVE, true);
			b.handler(new GeekCloudClientInitializer(sslCtx));

			Channel ch = b.connect(HOST, PORT).sync().channel();
			GeekCloudSender sender = new GeekCloudSender(ch);

			BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
			for(; ; ) {
				String line = in.readLine();
				if(line == null)
					break;

				sender.sendCmd(line);

				if("quit".equals(line.toLowerCase())) {
					ch.closeFuture(); //.sync();
					break;
				}
			}
		} finally {
			group.shutdownGracefully();
		}

	}
}