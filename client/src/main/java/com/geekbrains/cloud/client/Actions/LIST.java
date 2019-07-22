package com.geekbrains.cloud.client.Actions;

import com.geekbrains.cloud.client.GeekCloudClientDecoderState;
import io.netty.channel.ChannelHandlerContext;

import java.util.List;

public class LIST implements IAct {
	@Override
	public GeekCloudClientDecoderState doAct(ChannelHandlerContext ctx, List<String> args) {
		for(String s: args)
			System.out.println(s);

		return GeekCloudClientDecoderState.RESPONSE;
	}
}
