package com.ai.itms.sms.involve;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ai.itms.sms.packet.SMGPResponsePacket;
import com.ai.itms.sms.util.Common;


public class TSMGP_ACTIVE_TEST_RESP  extends SMGPResponsePacket{

  /**
   * Logger for this class
   */
  private static Logger log = LoggerFactory.getLogger(TSMGP_ACTIVE_TEST_RESP.class);
  
  
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
          log.debug("返回包命令字解析后=" + lCommand_ID + "，实际=" + FinalDef.CMD_SMGP_LOGIN_RESP);
      }

      byte[] seqid = new byte[4];
      System.arraycopy(packet, 8, seqid, 0, 4);
      this.lSerial_ID = Common.bytes4ToInt(seqid);
      if (log.isDebugEnabled()) {
          log.debug("返回包序列号解析后为:" + lSerial_ID);
      }
  
  }
  
}
