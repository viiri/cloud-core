package com.geekbrains.cloud.common.protocol;

import java.util.HashMap;

public class GeekCloudProtocol {
	public static final String S_NOOP = "NOOP";
	public static final String S_USER = "USER";
	public static final String S_PASS = "PASS";
	public static final String S_LIST = "LIST";
	public static final String S_RECV = "RECV";
	public static final String S_SEND = "SEND";
	public static final String S_QUIT = "QUIT";

	public static final String S_ERRC = "ERRC";
	public static final String S_FILE = "FILE";

	public static final byte[] R_ERRC = S_ERRC.getBytes();
	public static final byte[] R_FILE = S_FILE.getBytes();
	public static final byte[] R_LIST = S_LIST.getBytes();

	private static final HashMap<String, byte[]> cmd2code;

	static {
		cmd2code = new HashMap<>();
		cmd2code.put("user", S_USER.getBytes());
		cmd2code.put("pass", S_PASS.getBytes());
		cmd2code.put("list", S_LIST.getBytes());
		cmd2code.put("recv", S_RECV.getBytes());
		cmd2code.put("send", S_SEND.getBytes());
		cmd2code.put("quit", S_QUIT.getBytes());
	}

	public static byte[] getCmdByName(String cmd) {
		return cmd2code.getOrDefault(cmd, null);
	}
}
