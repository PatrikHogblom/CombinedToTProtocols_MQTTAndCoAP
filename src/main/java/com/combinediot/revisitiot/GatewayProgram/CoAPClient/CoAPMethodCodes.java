package com.combinediot.revisitiot.GatewayProgram.CoAPClient;

import java.util.HashMap;
import java.util.Map;

public enum CoAPMethodCodes {
	
	 /* 
	    +------+--------+
		| Code | Name   |
		+------+--------+
		| 0.00 | EMPTY  |
		+------+--------+
		| 0.01 | GET    |
		| 0.02 | POST   |
		| 0.03 | PUT    |
		| 0.04 | DELETE |
		+------+--------+
	*/
	
	EMPTY(0),
	GET(1),
	POST(2),
	PUT(3),
	DELETE(4);
	
	
	
	private Integer coapCode;
	private static final Map<Integer, CoAPMethodCodes> list = new HashMap<>();
	
	private CoAPMethodCodes(Integer coapCode) {
		this.coapCode = coapCode;
	}
	
	public Integer getInteger() {
		return this.coapCode;
	}
	
	static {
		for(CoAPMethodCodes env: CoAPMethodCodes.values()) {
			list.put(env.getInteger(), env);
		}
	}
	
	public static CoAPMethodCodes CodeType(Integer coapCode) {
		return list.get(coapCode);
	}

}
