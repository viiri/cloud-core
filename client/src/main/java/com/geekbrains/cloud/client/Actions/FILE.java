package com.geekbrains.cloud.client.Actions;

import com.geekbrains.cloud.client.GeekCloudClientDecoderState;
import com.geekbrains.cloud.client.GeekCloudClient;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;

public class FILE implements IAct {
	@Override
	public GeekCloudClientDecoderState doAct(ChannelHandlerContext ctx, List<String> args) {
		Channel ch = ctx.channel();
		GeekCloudClientDecoderState result = GeekCloudClientDecoderState.RESPONSE;

		try {
			FileOutputStream fileOutputStream = new FileOutputStream(".\\client\\storage\\" + args.get(0));

			ch.attr(GeekCloudClient.CURRENT_FILENAME).set(args.get(0));
			ch.attr(GeekCloudClient.CURRENT_FILELENGTH).set(Long.parseLong(args.get(1)));
			ch.attr(GeekCloudClient.CURRENT_FILESTREAM).set(fileOutputStream);

			result = GeekCloudClientDecoderState.DATA;
		} catch(FileNotFoundException e) {
			e.printStackTrace();
		}

		return result;
	}
}
