package com.combinediot.revisitiot.GatewayProgram.CoAPClient;


public enum CoAPOptionExtended {
	
	ZERO_BITS(0),
	EIGHT_BITS(8),
	SIXTEEN_BITS(16);
	
	private Integer coapExtendedOption;
	
	private CoAPOptionExtended(Integer coapExtendedOption) {
		this.coapExtendedOption = coapExtendedOption;
	}
	
	   public static Integer getOptionExtendedCode(CoAPOptionExtended optionExtended, Integer coapExtendedOption) throws Exception {
	        switch(optionExtended) 
	        {
	            case ZERO_BITS: 
	                return coapExtendedOption;
	            case EIGHT_BITS: 
	                return 13;
	            case SIXTEEN_BITS: 
	                return 14;
	            default: 
	                throw new Exception("Four bit code could not be represented from: " + optionExtended);
	        }
	    }
	
	   public static CoAPOptionExtended getOptionExtended(Integer coapExtendedOption) throws Exception{
	        if(coapExtendedOption > 65804) {
	            throw new Exception("Couldnt get OptionExtended from number: " + coapExtendedOption);
	        }
	        return (coapExtendedOption >= 0 && coapExtendedOption <= 12) ? CoAPOptionExtended.ZERO_BITS
	        	  :(coapExtendedOption >= 12 && coapExtendedOption <= 268) ? CoAPOptionExtended.EIGHT_BITS
	              :CoAPOptionExtended.SIXTEEN_BITS;
	    }
}
	
