package com.ai.itms.sms;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ai.itms.sms.involve.SMGP3;
import com.ai.itms.sms.involve.TSMGP_RESP;
import com.ai.itms.sms.involve.TSMGP_SUBMIT;

public class sendMessage {

	private static Logger logger = LoggerFactory.getLogger(sendMessage.class);


	public static boolean send(String ip,int port,String username,String password,String serivceCode,String mobile,String msgcode){
		try {
			SMGP3 smgp = new SMGP3();
			int socketID = smgp.SMGP_Connect(ip, port, username, password, (byte) 0);
			logger.debug("连接ISMG返回值：" + socketID);
			if (socketID > 0) {
				int test_ret = smgp.SMGPActiveTest(socketID);
				logger.debug("测试链路返回值：" + test_ret);
				if (test_ret == 0) {
					TSMGP_SUBMIT submit = new TSMGP_SUBMIT();
					submit.cMsgType = 6;
					submit.cNeedReport = 1;
					submit.cPriority = 3;
					submit.sServiceID = "PC2P";
					submit.sFeeType = "00";
					submit.sFeeCode = "0";
					submit.sFixedFee = "0";
					submit.sValidTime = "";
					submit.sAtTime = "";
					submit.sSrcTermID = serivceCode;
					submit.sChargeTermID = "";
					submit.sDestTermID = mobile;
					submit.ucMsgFormat = 15;
					submit.sMsgContent = "【ITMS】您的验证码是"+msgcode+",在5分钟内有效。如非本人操作请忽略本短信";
					submit.sReserve = "";
					TSMGP_RESP resp = new TSMGP_RESP();
					int submit_ret = smgp.SMGP_Submit(socketID, submit, resp);
					logger.warn("发送MT返回值：" + submit_ret + " --- seqid:" + resp.getLSerial_ID() + " msgid="+ resp.sMsgID);
					smgp.SMGP_Disconnect(socketID);
					if (submit_ret==0 || submit_ret==196615) {
						return true;
					}else {
						return false;
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	
	
	public static void main(String args[]) throws Exception {
		SMGP3 smgp = new SMGP3();
		int socketID = smgp.SMGP_Connect("136.255.250.193", 8891, "ITSM", "sj0ADdYD", (byte) 0);
		logger.debug("连接ISMG返回值：" + socketID);

				if (socketID > 0) {

					int test_ret = smgp.SMGPActiveTest(socketID);

					logger.info("测试链路返回值：" + test_ret);

					if (test_ret == 0) {
						TSMGP_SUBMIT submit = new TSMGP_SUBMIT();
						submit.cMsgType = 6;
						submit.cNeedReport = 1;
						submit.cPriority = 0;
						submit.sServiceID = "20006";
						submit.sFeeType = "00";
						submit.sFeeCode = "0";
						submit.sFixedFee = "0";
						submit.sValidTime = "";
						submit.sAtTime = "";
						submit.sSrcTermID = "10659205002682";
						submit.sChargeTermID = "15690995319";
						submit.sDestTermID = "15690995319";
						submit.ucMsgFormat = 15;
						submit.sMsgContent = "ITMStest,验证码111111,收到请回复。。。。。。。。。。。。。。";
						submit.sReserve = "";
						TSMGP_RESP resp = new TSMGP_RESP();
						int submit_ret = smgp.SMGP_Submit(socketID, submit, resp);
						logger.info("发送MT返回值：" + submit_ret + " --- seqid:" + resp.getLSerial_ID() + " msgid="
								+ resp.sMsgID);
					}
				}
				smgp.SMGP_Disconnect(socketID);
	}

}