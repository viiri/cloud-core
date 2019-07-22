package com.geekbrains.cloud.server.Actions;

import com.geekbrains.cloud.common.protocol.GeekCloudSender;
import com.geekbrains.cloud.server.GeekCloudServer;
import com.geekbrains.cloud.server.GeekCloudServerDecoderState;
import com.geekbrains.cloud.server.GeekCloudServerState;
import com.geekbrains.cloud.server.Util.AuthHandler;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

import java.util.List;

public class PASS implements ICmd {
	@Override
	public GeekCloudServerDecoderState doCmd(ChannelHandlerContext ctx, List<String> args) throws Exception {
		Channel ch = ctx.channel();
		GeekCloudSender sender = new GeekCloudSender(ch);

		if(ctx.channel().attr(GeekCloudServer.STATE).get() == GeekCloudServerState.AUTH_USER) {
			if(AuthHandler.login(ctx.channel().attr(GeekCloudServer.CURRENT_USER).get(), args.get(0))) {
				ch.attr(GeekCloudServer.STATE).set(GeekCloudServerState.AUTH_PASS);
				sender.sendErrorCode(230, "User logged in, proceed.");
			} else {
				ch.attr(GeekCloudServer.STATE).set(GeekCloudServerState.AUTH_NONE);
				sender.sendErrorCode(530, "Invalid username or password.");
			}
		} else {
			sender.sendErrorCode(332, "Need account for login.");
		}

		return GeekCloudServerDecoderState.REQUEST;
	}
}
