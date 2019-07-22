package com.geekbrains.cloud.server.Actions;

import com.geekbrains.cloud.common.protocol.GeekCloudSender;
import com.geekbrains.cloud.server.GeekCloudServer;
import com.geekbrains.cloud.server.GeekCloudServerDecoderState;
import com.geekbrains.cloud.server.GeekCloudServerState;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static com.geekbrains.cloud.common.protocol.GeekCloudProtocol.R_LIST;

public class LIST implements ICmd {
	@Override
	public GeekCloudServerDecoderState doCmd(ChannelHandlerContext ctx, List<String> args) throws Exception {
		Channel ch = ctx.channel();
		GeekCloudSender sender = new GeekCloudSender(ch);
		List<String> list = new ArrayList<>();

		if(ch.attr(GeekCloudServer.STATE).get() == GeekCloudServerState.AUTH_PASS) {
			try(DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get(".\\server\\storage\\"))) {
				for(Path path : directoryStream) {
					list.add(path.getFileName().toString());
				}
				sender.send(R_LIST, list);
			} catch(IOException ignored) {
			}
		} else {
			sender.sendErrorCode(530, "Not logged in.");
		}

		return GeekCloudServerDecoderState.REQUEST;
	}
}
