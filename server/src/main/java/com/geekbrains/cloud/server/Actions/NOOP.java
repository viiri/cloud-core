package com.geekbrains.cloud.server.Actions;

import com.geekbrains.cloud.common.protocol.GeekCloudSender;
import com.geekbrains.cloud.server.GeekCloudServerDecoderState;
import io.netty.channel.ChannelHandlerContext;

import java.util.List;

public class NOOP implements ICmd {
	@Override
	public GeekCloudServerDecoderState doCmd(ChannelHandlerContext ctx, List<String> args) throws Exception {
		GeekCloudSender sender = new GeekCloudSender(ctx.channel());

		sender.sendErrorCode(500, "Unknown command");

		return GeekCloudServerDecoderState.REQUEST;
	}
}
