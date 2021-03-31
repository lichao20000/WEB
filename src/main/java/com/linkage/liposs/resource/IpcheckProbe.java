package com.linkage.liposs.resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import I_Ip_Check.IPing_Result;
import I_Ip_Check.Ping_Config;

import com.linkage.litms.common.corba.WebCorba;

public class IpcheckProbe {

	private static Logger m_logger = LoggerFactory.getLogger(IpcheckProbe.class);

	private boolean m_isBind = false;
	
	private I_Ip_Check.I_ObjectManager  m_forwardalarm = null;

	private WebCorba corba = null;

	private String gather_id = null;

	public IpcheckProbe(String gather_id) {
		this.gather_id = gather_id;
		if (m_forwardalarm == null) {
			BindForward();
		}
	}
	public void clearService(){
		m_forwardalarm = null;
		if(corba !=null) corba.removeKey(gather_id, "IpCheck", "0");
	}
	/**
	 * 重新绑定corba
	 * 
	 * @return boolean
	 */
	public boolean Rebind() {
		if (corba == null) {
			corba = new WebCorba("IpCheck", gather_id, "0");
		}
		if (m_forwardalarm == null) {
			if (!BindForward())
				return false;
			return true;
		} return true;
	}

	 
	/**
	 * 绑定FowardAlarm
	 * 
	 * @return boolean
	 */
	private boolean BindForward() {
		try {
			if (corba == null) {
				corba = new WebCorba("IpCheck", gather_id, "0");
				//logger.debug("corba================aaaaa============="+corba);
			}else{
				corba.removeKey(gather_id,"IpCheck","0");
				corba = new WebCorba("IpCheck", gather_id, "0");
			}
			//logger.debug("\n getIDLCorba ........\n");
			m_forwardalarm = (I_Ip_Check.I_ObjectManager ) corba.getIDLCorba("IpCheck");
			//logger.debug("m_forwardalarm==================="+m_forwardalarm);
			m_logger.debug(m_forwardalarm.toString());
			m_isBind = true;
		} catch (Exception ex) {
			m_forwardalarm = null;
			m_logger.debug("m_forwardalarm is nil, bind fail");
			m_isBind = false;
			ex.printStackTrace();
		}
		return m_isBind;
	}
	/**
	 * 
	 * @param m_name
	 * @return
	 */
	public IPing_Result[] I_IPingCheck(Ping_Config[] ping_Configlist){
		
		try{
			
		if(m_forwardalarm != null)
			  return m_forwardalarm.I_IPingCheck(ping_Configlist);
		}catch(Exception e){
			this.clearService();
			e.printStackTrace();
			try{
				BindForward();
				if(m_forwardalarm != null)
					 return m_forwardalarm.I_IPingCheck(ping_Configlist);
			}catch(Exception ee){
				this.clearService();
				ee.printStackTrace();
			}
		}
		return null;
	}
	
	
	public static void main(String[] args) {
	}


	
	
}
