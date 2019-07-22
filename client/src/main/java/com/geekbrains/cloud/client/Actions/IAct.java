package com.geekbrains.cloud.client.Actions;

import com.geekbrains.cloud.client.GeekCloudClientDecoderState;
import io.netty.channel.ChannelHandlerContext;

import java.util.List;

@FunctionalInterface
public interface IAct {
	GeekCloudClientDecoderState doAct(ChannelHandlerContext ctx, List<String> args);
}
