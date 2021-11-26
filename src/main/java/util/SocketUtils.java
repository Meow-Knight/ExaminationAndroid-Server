package util;

import common.SocketRequestType;

public class SocketUtils {
	
	public static SocketRequestType getSocketRequestType(String request) {
		for (SocketRequestType socketRequestType : SocketRequestType.values()) {
			if (socketRequestType.getName().equals(request)) {
				return socketRequestType;
			}
		}
		
		return null;
	}
}
