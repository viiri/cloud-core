package com.geekbrains.cloud.server.Actions;

import com.geekbrains.cloud.common.protocol.GeekCloudSender;
import com.geekbrains.cloud.server.GeekCloudServer;
import com.geekbrains.cloud.server.GeekCloudServerDecoderState;
import com.geekbrains.cloud.server.GeekCloudServerState;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.stream.ChunkedFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.geekbrains.cloud.common.protocol.GeekCloudConsts.CHUNK_SIZE;
import static com.geekbrains.cloud.common.protocol.GeekCloudProtocol.R_FILE;

public class RECV implements ICmd {
	@Override
	public GeekCloudServerDecoderState doCmd(ChannelHandlerContext ctx, List<String> args) throws Exception {
		Channel ch = ctx.channel();
		GeekCloudSender sender = new GeekCloudSender(ch);

		if(ch.attr(GeekCloudServer.STATE).get() == GeekCloudServerState.AUTH_PASS) {
			File file = new File(".\\server\\storage\\" + args.get(0));

			if(file.exists()) {
				List<String> fileInfo = new ArrayList<>();

				fileInfo.add(file.getName());
				fileInfo.add(Long.toString(file.length()));
				sender.send(R_FILE, fileInfo);

				ch.write(new ChunkedFile(file, CHUNK_SIZE - 4));
				sender.sendErrorCode(250, "Requested file action okay, completed.");
			} else {
				sender.sendErrorCode(550, "File not found.");
			}

			ch.flush();
		} else {
			sender.sendErrorCode(530, "Not logged in.");
		}

		return GeekCloudServerDecoderState.REQUEST;
	}
}
