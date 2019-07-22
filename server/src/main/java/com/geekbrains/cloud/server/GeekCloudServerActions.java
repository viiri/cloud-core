package com.geekbrains.cloud.server;

import com.geekbrains.cloud.server.Actions.*;

import java.util.HashMap;

import static com.geekbrains.cloud.common.protocol.GeekCloudProtocol.*;

public class GeekCloudServerActions {
	private static final HashMap<String, ICmd> code2act;

	static {
		code2act = new HashMap<>();
		code2act.put(S_USER, new USER());
		code2act.put(S_PASS, new PASS());
		code2act.put(S_LIST, new LIST());
		code2act.put(S_RECV, new RECV());
		code2act.put(S_SEND, new SEND());
		code2act.put(S_QUIT, new QUIT());
	}

	public static ICmd getActByCode(String code) {
		return code2act.getOrDefault(code, new NOOP());
	}
}
