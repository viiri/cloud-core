package com.geekbrains.cloud.server.Actions;

import com.geekbrains.cloud.common.protocol.GeekCloudSender;
import com.geekbrains.cloud.server.GeekCloudServer;
import com.geekbrains.cloud.server.GeekCloudServerDecoderState;
import com.geekbrains.cloud.server.GeekCloudServerState;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

import java.util.List;

public class USER implements ICmd {
	@Override
	public GeekCloudServerDecoderState doCmd(ChannelHandlerContext ctx, List<String> args) throws Exception {
		Channel ch = ctx.channel();
		GeekCloudSender sender = new GeekCloudSender(ch);
		String msg = "Password required for " + args.get(0) + ".";

		ch.attr(GeekCloudServer.CURRENT_USER).set(args.get(0));
		ch.attr(GeekCloudServer.STATE).set(GeekCloudServerState.AUTH_USER);

		sender.sendErrorCode(331, msg);

		return GeekCloudServerDecoderState.REQUEST;
	}
}
