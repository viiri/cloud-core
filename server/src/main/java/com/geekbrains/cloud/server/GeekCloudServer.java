package com.geekbrains.cloud.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;
import io.netty.util.AttributeKey;

import javax.net.ssl.SSLException;
import java.io.FileOutputStream;
import java.security.cert.CertificateException;

public class GeekCloudServer {
	static final boolean SSL = System.getProperty("ssl") != null;
	static final int PORT = Integer.parseInt(System.getProperty("port", SSL ? "8992" : "8023"));
	static final int READ_TIMEOUT = Integer.parseInt(System.getProperty("readTimeout", "60"));

	public static final AttributeKey<GeekCloudServerState> STATE = AttributeKey.valueOf("state");
	public static final AttributeKey<String> CURRENT_USER = AttributeKey.valueOf("user");
	public static final AttributeKey<String> CURRENT_FILENAME = AttributeKey.valueOf("fileName");
	public static final AttributeKey<Long> CURRENT_FILELENGTH = AttributeKey.valueOf("fileLength");
	public static final AttributeKey<FileOutputStream> CURRENT_FILESTREAM = AttributeKey.valueOf("fileStream");

	public static void main(String[] args) throws CertificateException, InterruptedException, SSLException {
		final SslContext sslCtx;
		if(SSL) {
			SelfSignedCertificate ssc = new SelfSignedCertificate();
			sslCtx = SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey()).build();
		} else {
			sslCtx = null;
		}

		EventLoopGroup bossGroup = new NioEventLoopGroup(1);
		EventLoopGroup workerGroup = new NioEventLoopGroup();

		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workerGroup)
					.channel(NioServerSocketChannel.class)
					.option(ChannelOption.SO_BACKLOG, 100)
					.handler(new LoggingHandler(LogLevel.INFO))
					.childHandler(new GeekCloudServerInitializer(sslCtx));

			ChannelFuture f = b.bind(PORT).sync();
			f.channel().closeFuture().sync();
		} finally {
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}
}
