package com.ai.itms.sms.involve;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ai.itms.sms.packet.SMGPResponsePacket;
import com.ai.itms.sms.util.Common;


/*
unsigned char           sMsgID[MAX_SMGP_MSGID_LEN];                // 短消息标识
unsigned int            lStatus;                                   // 返回结果(参见错误代码)
*/

public class TSMGP_RESP extends SMGPResponsePacket {
	
	private static Logger log = LoggerFactory.getLogger(TSMGP_RESP.class);
	
	public String   sMsgID;  //短消息标识
	public int      lStatus; // 返回结果(参见错误代码)
    private int     length=FinalDef.MAX_SMGP_MSGID_LEN+4;
    
    
    public void parseResponseBody(byte[] packet) {
        
        byte[] length = new byte[4];
        System.arraycopy(packet, 0, length, 0, 4);
        this.lTotal_Length = Common.bytes4ToInt(length);
        if (log.isDebugEnabled()) {
            log.debug("返回包长度解析后为:" + lTotal_Length);
        }

        byte[] commandid = new byte[4];
        System.arraycopy(packet, 4, commandid, 0, 4);
        this.lCommand_ID = Common.bytes4ToInt(commandid);
        if (log.isDebugEnabled()) {
            log.debug("返回包命令字解析后=" + lCommand_ID);
        }

        byte[] seqid = new byte[4];
        System.arraycopy(packet, 8, seqid, 0, 4);
        this.lSerial_ID = Common.bytes4ToInt(seqid);
        if (log.isDebugEnabled()) {
            log.debug("返回包序列号解析后为:" + lSerial_ID);
        }

        
        byte[] msgid = new byte[FinalDef.MAX_SMGP_MSGID_LEN];
        System.arraycopy(packet, 12, msgid, 0, FinalDef.MAX_SMGP_MSGID_LEN);
        this.sMsgID = Common.getHexStr(msgid); 
        
        byte stat[] = new byte[4];
        System.arraycopy(packet, 12+FinalDef.MAX_SMGP_MSGID_LEN, stat, 0, 4);
        lStatus = Common.bytes4ToInt(stat);

        log.warn("TSMGP_RESP消息解析成功,status=" + lStatus+",sequenceID="+lSerial_ID);
    }
    
}
