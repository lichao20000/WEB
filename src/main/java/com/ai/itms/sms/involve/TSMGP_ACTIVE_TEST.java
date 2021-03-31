package com.ai.itms.sms.involve;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ai.itms.sms.packet.SMGPRequestBody;


/**
 * 
 * @author YC
 * @version 1.0
 */
public class TSMGP_ACTIVE_TEST implements SMGPRequestBody {

	/**
	 * Logger for this class
	 */
	private static Logger log = LoggerFactory.getLogger(TSMGP_ACTIVE_TEST.class);

	public TSMGP_ACTIVE_TEST() 
	{			 
	}
	public byte[] getRequestBody() {
		return new byte[0];
	}

}