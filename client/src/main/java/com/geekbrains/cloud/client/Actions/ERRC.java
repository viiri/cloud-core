package com.geekbrains.cloud.client.Actions;

import com.geekbrains.cloud.client.GeekCloudClientDecoderState;
import io.netty.channel.ChannelHandlerContext;

import java.util.List;

public class ERRC implements IAct {
	@Override
	public GeekCloudClientDecoderState doAct(ChannelHandlerContext ctx, List<String> args) {
		System.out.println(args.get(1));

		return GeekCloudClientDecoderState.RESPONSE;
	}
}
