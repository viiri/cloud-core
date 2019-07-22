package com.geekbrains.cloud.server.Actions;

import com.geekbrains.cloud.common.protocol.GeekCloudSender;
import com.geekbrains.cloud.server.GeekCloudServer;
import com.geekbrains.cloud.server.GeekCloudServerDecoderState;
import com.geekbrains.cloud.server.GeekCloudServerState;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;

public class SEND implements ICmd {
	@Override
	public GeekCloudServerDecoderState doCmd(ChannelHandlerContext ctx, List<String> args) throws Exception {
		Channel ch = ctx.channel();
		GeekCloudSender sender = new GeekCloudSender(ch);
		GeekCloudServerDecoderState result = GeekCloudServerDecoderState.REQUEST;

		if(ch.attr(GeekCloudServer.STATE).get() == GeekCloudServerState.AUTH_PASS) {

			try {
				FileOutputStream fileOutputStream = new FileOutputStream(".\\server\\storage\\" + args.get(0));

				ch.attr(GeekCloudServer.CURRENT_FILENAME).set(args.get(0));
				ch.attr(GeekCloudServer.CURRENT_FILELENGTH).set(Long.parseLong(args.get(1)));
				ch.attr(GeekCloudServer.CURRENT_FILESTREAM).set(fileOutputStream);

				result = GeekCloudServerDecoderState.DATA;
			} catch(FileNotFoundException e) {
				e.printStackTrace();
			}
		} else {
			sender.sendErrorCode(530, "Not logged in.");
		}

		return result;
	}
}
