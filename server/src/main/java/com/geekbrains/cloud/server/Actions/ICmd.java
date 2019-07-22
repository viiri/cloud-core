package com.geekbrains.cloud.server.Actions;

import com.geekbrains.cloud.server.GeekCloudServerDecoderState;
import io.netty.channel.ChannelHandlerContext;

import java.util.List;

@FunctionalInterface
public interface ICmd {
	GeekCloudServerDecoderState doCmd(ChannelHandlerContext ctx, List<String> args) throws Exception;
}
