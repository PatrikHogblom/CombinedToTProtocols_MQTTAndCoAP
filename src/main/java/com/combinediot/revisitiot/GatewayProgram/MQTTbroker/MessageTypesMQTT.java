package com.combinediot.revisitiot.GatewayProgram.MQTTbroker;
import java.util.HashMap;
import java.util.Map;

public enum MessageTypesMQTT {
	
	RESERVED(0),
	CONNECT(1),
	CONNACK(2),
	PUBLISH(3),
	PUBACK(4),
	PUBREC5(5),
	PUBREL(6),
	PUBCOMP(7),
	SUBSCRIBE(8),
	SUBACK(9),
	UNSUBCRIBE(10),
	UNSUBACK(11),
	PINGREQ(12),
	PINGRESP(13),
	DISCONNECT(14),
	Reserved(15);
	
	private Integer messageType;
	private static final Map<Integer, MessageTypesMQTT> table = new HashMap<>();
	
	private MessageTypesMQTT(Integer messageType) {
		this.messageType = messageType;
	}
	
	public Integer getInteger() {
		return this.messageType;
	}

	static {
		for (MessageTypesMQTT env : MessageTypesMQTT.values()) {
				table.put(env.getInteger(), env);
			}
	}

	public static MessageTypesMQTT CodeName(Integer methodType) {
			return table.get(methodType);
	    }
	}

