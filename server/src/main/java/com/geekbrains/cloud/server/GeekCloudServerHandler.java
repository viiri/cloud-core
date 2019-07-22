package com.geekbrains.cloud.server;

import com.geekbrains.cloud.common.protocol.GeekCloudSender;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import static com.geekbrains.cloud.server.GeekCloudServerActions.getActByCode;
import static java.nio.charset.StandardCharsets.UTF_8;

public class GeekCloudServerHandler extends ReplayingDecoder<GeekCloudServerDecoderState> {
	private List<String> requests = null;
	private int length = 0;

	public GeekCloudServerHandler() {
		super(GeekCloudServerDecoderState.REQUEST);
	}

	@Override
	public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
		GeekCloudSender sender = new GeekCloudSender(ctx.channel());
		sender.sendErrorCode(200, "Server ready.");
		super.channelRegistered(ctx);
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		length = in.readInt();

		switch(state()) {
			case REQUEST:
				if(requests == null)
					requests = new ArrayList<>();

				String request = in.readBytes(length).toString(UTF_8);

				if(length == 1 && request.charAt(0) == 0) {
					state(getActByCode(requests.remove(0)).doCmd(ctx, requests));
					requests = null;
				} else {
					requests.add(request);
				}

				break;
			case DATA:
				ByteBuffer byteBuffer = in.readBytes(length).nioBuffer();

				FileOutputStream fileOutputStream = ctx.channel().attr(GeekCloudServer.CURRENT_FILESTREAM).get();
				FileChannel channel = fileOutputStream.getChannel();

				Long totalLength = ctx.channel().attr(GeekCloudServer.CURRENT_FILELENGTH).get();

				channel.write(byteBuffer);

				if(totalLength > length) {
					ctx.channel().attr(GeekCloudServer.CURRENT_FILELENGTH).set(totalLength - length);
				} else {
					channel.close();
					fileOutputStream.close();
					GeekCloudSender sender = new GeekCloudSender(ctx.channel());
					sender.sendErrorCode(250, "Requested file action okay, completed.");
					state(GeekCloudServerDecoderState.REQUEST);
				}

				break;
			default:
				throw new Error("Shouldn't reach here.");
		}
	}
}
