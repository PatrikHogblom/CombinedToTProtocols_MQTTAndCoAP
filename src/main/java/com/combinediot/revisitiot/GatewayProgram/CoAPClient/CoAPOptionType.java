package com.combinediot.revisitiot.GatewayProgram.CoAPClient;

import java.util.HashMap;
import java.util.Map;

/*
+-----+----+---+---+---+----------------+--------+--------+-------------+
| No. | C  | U | N | R | Name           | Format | Length | Default     |
+-----+----+---+---+---+----------------+--------+--------+-------------+
| 1   | x  |   |   | x | If-Match       | opaque | 0-8    | (none)      |
| 3   | x  | x | - |   | Uri-Host       | string | 1-255  | (see note 1)|
| 4   |    |   |   | x | ETag           | opaque | 1-8    | (none)      |
| 5   | x  |   |   |   | If-None-Match  | empty  | 0      | (none)      |
| 7   | x  | x | - |   | Uri-Port       | uint   | 0-2    | (see note 1)|
| 8   |    |   |   | x | Location-Path  | string | 0-255  | (none)      |
| 11  | x  | x | - | x | Uri-Path       | string | 0-255  | (none)      |
| 12  |    |   |   |   | Content-Format | uint   | 0-2    | (none)      |
| 14  |    | x | - |   | Max-Age        | uint   | 0-4    | 60          |
| 15  | x  | x | - | x | Uri-Query      | string | 0-255  | (none)      |
| 17  | x  |   |   |   | Accept         | uint   | 0-2    | (none)      |
| 20  |    |   |   | x | Location-Query | string | 0-255  | (none)      |
| 28  |    |   | x |   | Size2          | uint   | 0-4    | (none)      |
| 35  | x  | x | - |   | Proxy-Uri      | string | 1-1034 | (none)      |
| 39  | x  | x | - |   | Proxy-Scheme   | string | 1-255  | (none)      |
| 60  |    |   | x |   | Size1          | uint   | 0-4    | (none)      |
+-----+----+---+---+---+----------------+--------+--------+-------------+
 
 
 */

public enum CoAPOptionType {
	
	IF_MATCH(1),
	URI_HOST(3),
	ETAG(4),
	IF_NONE_MATCH(5),
	URI_PORT(7),
	LOCATION_PATH(8),
	URI_PATH(11),
	CONTENT_FORMAT(12),
	MAX_AGE(14),
	URI_QUERY(15),
	ACCEPT(17),
	LOCATION_QUERY(20),
	SIZE2(28),
	PROXY_URI(35),
	PROXY_SCHEME(39),
	SIZE1(60);

	private Integer optionCode;
	private static final Map<Integer, CoAPOptionType> list = new HashMap<>();
	
	private CoAPOptionType(Integer coapCode) {
		this.optionCode = coapCode;
	}
	
	public Integer getInteger() {
		return this.optionCode;
	}
	
	static {
		for(CoAPOptionType env: CoAPOptionType.values()) {
			list.put(env.getInteger(), env);
		}
	}
	
	public static CoAPOptionType CodeType(Integer optionCode) {
		return list.get(optionCode);
	}
}