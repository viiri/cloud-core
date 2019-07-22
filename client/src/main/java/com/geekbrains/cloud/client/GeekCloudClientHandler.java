package com.geekbrains.cloud.client;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import static com.geekbrains.cloud.client.GeekCloudClientActions.getActByCode;
import static java.nio.charset.StandardCharsets.UTF_8;

public class GeekCloudClientHandler extends ReplayingDecoder<GeekCloudClientDecoderState> {
	private List<String> responses = null;
	private int length = 0;

	public GeekCloudClientHandler() {
		super(GeekCloudClientDecoderState.RESPONSE);
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		length = in.readInt();

		switch(state()) {
			case RESPONSE:
				if(responses == null)
					responses = new ArrayList<>();

				String response = in.readBytes(length).toString(UTF_8);

				if(length == 1 && response.charAt(0) == 0) {
					state(getActByCode(responses.remove(0)).doAct(ctx, responses));
					responses = null;
				} else {
					responses.add(response);
				}

				break;
			case DATA:
				ByteBuffer byteBuffer = in.readBytes(length).nioBuffer();

				FileOutputStream fileOutputStream = ctx.channel().attr(GeekCloudClient.CURRENT_FILESTREAM).get();
				FileChannel channel = fileOutputStream.getChannel();

				Long totalLength = ctx.channel().attr(GeekCloudClient.CURRENT_FILELENGTH).get();

				channel.write(byteBuffer);

				if(totalLength > length) {
					ctx.channel().attr(GeekCloudClient.CURRENT_FILELENGTH).set(totalLength - length);
				} else {
					channel.close();
					fileOutputStream.close();
					state(GeekCloudClientDecoderState.RESPONSE);
				}

				break;
			default:
				throw new Error("Shouldn't reach here.");
		}
	}
}
