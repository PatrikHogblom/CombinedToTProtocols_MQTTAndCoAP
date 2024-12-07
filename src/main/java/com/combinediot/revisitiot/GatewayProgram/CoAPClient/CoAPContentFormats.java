package com.combinediot.revisitiot.GatewayProgram.CoAPClient;

import java.util.HashMap;
import java.util.Map;

/**
     * Content-Formats:
     * +--------------------------+-----+
     * | Media type               | Id. |
     * +--------------------------+-----+
     * | text/plain;charset=utf-8 | 0   |
     * | application/link-format  | 40  |
     * | application/xml          | 41  |
     * | application/octet-stream | 42  |
     * | application/exi          | 47  |
     * | application/json         | 50  |
     * | application/cbor         | 60  |
     * +--------------------------+-----+
     */

public enum CoAPContentFormats {
	
	TEXT_PLAIN(0),
	APP_LINK(40),
	APP_XML(41),
	APP_OCT_STRM(42),
	APP_EXI(47),
	APP_JSON(50),
	APP_CBOR(60);
	
	private Integer coapCode;
	private static final Map<Integer, CoAPContentFormats> list = new HashMap<>();
	
	private CoAPContentFormats(Integer coapCode){
		this.coapCode = coapCode;
	}
	
	public Integer getInteger() {
		return this.coapCode;
	}
	
	static {
		for(CoAPContentFormats env: CoAPContentFormats.values()) {
			list.put(env.getInteger(), env);
		}
	}
	
	public static CoAPContentFormats CodeType(Integer coapCode) {
		return list.get(coapCode);
	}

}
