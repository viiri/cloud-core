package com.geekbrains.cloud.server.Actions;

import com.geekbrains.cloud.common.protocol.GeekCloudSender;
import com.geekbrains.cloud.server.GeekCloudServer;
import com.geekbrains.cloud.server.GeekCloudServerDecoderState;
import com.geekbrains.cloud.server.GeekCloudServerState;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

import java.util.List;

public class QUIT implements ICmd {
	@Override
	public GeekCloudServerDecoderState doCmd(ChannelHandlerContext ctx, List<String> args) throws Exception {
		Channel ch = ctx.channel();
		GeekCloudSender sender = new GeekCloudSender(ch);

		ch.attr(GeekCloudServer.STATE).set(GeekCloudServerState.AUTH_NONE);
		sender.sendErrorCode(221, "Bye!");

		return GeekCloudServerDecoderState.REQUEST;
	}
}
