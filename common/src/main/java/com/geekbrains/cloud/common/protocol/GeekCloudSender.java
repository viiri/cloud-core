package com.geekbrains.cloud.common.protocol;

import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.handler.stream.ChunkedFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.geekbrains.cloud.common.protocol.GeekCloudProtocol.R_ERRC;
import static com.geekbrains.cloud.common.protocol.GeekCloudProtocol.S_SEND;

public class GeekCloudSender {
	private final Channel ch;
	private static final byte[] EMPTY = { 0 };

	public GeekCloudSender(Channel ch) {
		this.ch = ch;
	}

	public Channel getCh() {
		return ch;
	}

	public void send(byte[] code) {
		ch.write(Unpooled.copiedBuffer(code));
		ch.write(Unpooled.copiedBuffer(EMPTY));
		ch.flush();
	}

	public void send(byte[] code, String arg) {
		ch.write(Unpooled.copiedBuffer(code));
		ch.write(Unpooled.copiedBuffer(arg.getBytes()));
		ch.write(Unpooled.copiedBuffer(EMPTY));
		ch.flush();
	}

	public void send(byte[] code, List<String> args) {
		ch.write(Unpooled.copiedBuffer(code));
		args.forEach(s -> { ch.write(Unpooled.copiedBuffer(s.getBytes())); });
		ch.write(Unpooled.copiedBuffer(EMPTY));
		ch.flush();
	}

	public void sendErrorCode(int errorCode, String msg) {
		ch.write(Unpooled.copiedBuffer(R_ERRC));
		ch.write(Unpooled.copiedBuffer(Integer.toString(errorCode).getBytes()));
		ch.write(Unpooled.copiedBuffer(msg.getBytes()));
		ch.write(Unpooled.copiedBuffer(EMPTY));
		ch.flush();
	}

	public void sendCmd(String line) throws InterruptedException, IOException {
		String[] parts = line.split(" ", 2);
		byte[] cmd = GeekCloudProtocol.getCmdByName(parts[0].toLowerCase());

		if(cmd == null) {
			System.out.println("Invalid command!");
		} else {
			if(parts.length < 2) {
				send(cmd);
			} else {
				if(Arrays.equals(cmd, S_SEND.getBytes())) {
					sendFile(parts[1]);
				} else {
					send(cmd, parts[1]);
				}
			}
		}
	}

	private void sendFile(String arg) throws IOException {
		File file = new File(".\\client\\storage\\" + arg);

		if(file.exists()) {
			List<String> fileInfo = new ArrayList<>();

			fileInfo.add(file.getName());
			fileInfo.add(Long.toString(file.length()));
			send(S_SEND.getBytes(), fileInfo);

			ch.write(new ChunkedFile(file, GeekCloudConsts.CHUNK_SIZE - 4));
			ch.flush();
		} else {
			System.out.println("File not found.");
		}
	}
}
