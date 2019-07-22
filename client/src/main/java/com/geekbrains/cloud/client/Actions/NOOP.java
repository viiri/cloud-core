package com.geekbrains.cloud.client.Actions;

import com.geekbrains.cloud.client.GeekCloudClientDecoderState;
import io.netty.channel.ChannelHandlerContext;

import java.util.List;

public class NOOP implements IAct {
	@Override
	public GeekCloudClientDecoderState doAct(ChannelHandlerContext ctx, List<String> args) {
		return GeekCloudClientDecoderState.RESPONSE;
	}
}
