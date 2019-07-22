package com.geekbrains.cloud.client;

import com.geekbrains.cloud.client.Actions.*;

import java.util.HashMap;

import static com.geekbrains.cloud.common.protocol.GeekCloudProtocol.*;

public class GeekCloudClientActions {
	private static final HashMap<String, IAct> code2act;

	static {
		code2act = new HashMap<>();
		code2act.put(S_ERRC, new ERRC());
		code2act.put(S_FILE, new FILE());
		code2act.put(S_LIST, new LIST());
	}

	public static IAct getActByCode(String code) {
		return code2act.getOrDefault(code, new NOOP());
	}
}
