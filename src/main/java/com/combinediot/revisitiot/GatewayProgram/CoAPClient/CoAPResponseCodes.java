package com.combinediot.revisitiot.GatewayProgram.CoAPClient;

import java.util.HashMap;
import java.util.Map;

/*
 * 

i.e., 2.01 = (msx 3bit)class: 2-> 010 , (max 5 bit)detail = Created: 01 -> 00001, gives us (8bit) 010 00001 -> 65

+------------------+------------------------------+--------------+
| Code             | Description                  |              |
+------------------+------------------------------+--------------+
| 2.01 (65, 0x41)  | Created                      |      Success |
| 2.02 (66, 0x42)  | Deleted                      |              |
| 2.03 (67, 0x43)  | Valid                        |              |
| 2.04 (68, 0x44)  | Changed                      |              |
| 2.05 (69, 0x45)  | Content                      |              |
| 2.31 (95, 0x5F)  | Continue                     |              |
+------------------+------------------------------+--------------+
| 4.00 (128, 0x80) | Bad Request                  | Client Error |
| 4.01 (129, 0x81) | Unauthorized                 |              |
| 4.02 (130, 0x82) | Bad Option                   |              |
| 4.03 (131, 0x83) | Forbidden                    |              |
| 4.04 (132, 0x84) | Not Found                    |              |
| 4.05 (133, 0x85) | Method Not Allowed           |              |
| 4.06 (134, 0x86) | Not Acceptable               |              |
| 4.08 (136, 0x88) | Request Entity Incomplete    |              |
| 4.12 (140, 0x8C) | Precondition Failed          |              |
| 4.13 (141, 0x8D) | Request Entity Too Large     |              |
| 4.15 (143, 0x8F) | Unsupported Content-Format   |              |
+------------------+------------------------------+--------------+
| 5.00 (160, 0xA0) | Internal Server Error        | Server Error |
| 5.01 (161, 0xA1) | Not Implemented              |              |
| 5.02 (162, 0xA2) | Bad Gateway                  |              |
| 5.03 (163, 0xA3) | Service Unavailable          |              |
| 5.04 (164, 0xA4) | Gateway Timeout              |              |
| 5.05 (165, 0xA5) | Proxying Not Supported       |              |
+------------------+------------------------------+--------------+

*/

public enum CoAPResponseCodes {
	
	//Success
	CREATED(65),
	DELETED(66),
	VAILD(67),
	CHANGED(68),
	CONTENT(69),
	CONTINUE(95),
		
	//Client Error
	BAD_REQUEST(128),
	UNAUTHORIZED(129),
	BAD_OPTION(130),
	FORBIDDEN(131),
	NOT_FOUND(132),
	METHOD_NOT_ALLOWED(133),
	NOT_ACCEPTABLE(134),
	REQUEST_ENTITY_INCOMPLETE(136),
	PRECONDITION_FAILED(140),
	REQUEST_ENTITY_TOO_LARGE(141),
	UNSUPPORTED_CONTENT_FORMAT(143),
		
	//Server Error
	INTERNAL_SERVER_ERROR(160),
	NOT_IMPLEMENTED(161),
	BAD_GATEWAY(162),
	SERVICE_UNAVAILABLE(163),
	GATEWAY_TIMEOUT(164),
	PROXYING_NOT_sUPPORTED(165);
		

	private Integer coapCode;
	private static final Map<Integer, CoAPResponseCodes> list = new HashMap<>();
	
	private CoAPResponseCodes(Integer coapCode) {
		this.coapCode = coapCode;
	}
	
	public Integer getInteger() {
		return this.coapCode;
	}
	
	static {
		for(CoAPResponseCodes env: CoAPResponseCodes.values()) {
			list.put(env.getInteger(), env);
		}
	}
	
	public static CoAPResponseCodes CodeType(Integer coapCode) {
		return list.get(coapCode);
	}
}
