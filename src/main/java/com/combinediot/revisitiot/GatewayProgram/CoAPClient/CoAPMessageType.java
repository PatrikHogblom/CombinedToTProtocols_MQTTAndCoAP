package com.combinediot.revisitiot.GatewayProgram.CoAPClient;

import java.util.HashMap;
import java.util.Map;

public enum CoAPMessageType {
	
/*
    +------+-----------------+
	| Type | Name            |
    +------+-----------------+
    |    0 | CONfirmable     |
    |    1 | NON-confirmable |
    |    2 | ACKnowledgement |
    |    3 | ReSeT           |
    +------+-----------------+
*/
	CON(0),
	NON(1),
	ACK(2),
	RST(3);
	
	private Integer msgType;
	private static final Map<Integer,CoAPMessageType> list = new HashMap<>();
	
	
	private CoAPMessageType(Integer msgType) {
		this.msgType = msgType;
	}
	
	public Integer getInteger() {
		return this.msgType;
	}
	
	
	public static CoAPMessageType TypeName(Integer msgType) {
		return list.get(msgType);
	}
	
	static {
		for(CoAPMessageType env : CoAPMessageType.values()) {
			list.put(env.getInteger(), env);
		}
	}
	
	
	
	

}
