package com.linkage.litms.fault;
/**
 * 重新绑定corba
 * liuli 2007-3-15
 * @return boolean
 */
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.corba.WebCorba;
import com.linkage.module.gwms.Global;

public class TrapProbe {
	private static Logger m_logger = LoggerFactory.getLogger(TrapProbe.class);
	private  boolean m_isBind = false;	
	private  I_TrapProbe.O_TrapProbe  m_forwardalarm = null;
	private  WebCorba corba = null;
	private String gather_id = null;
	//add by suixz(5253) 2008-4-15 北京网通修改接口
	private static final String BJWT="bj_wt";
	public TrapProbe(String gather_id) {
		this.gather_id = gather_id;
		if (m_forwardalarm == null) {
			BindForward();
		}
	}
	public void clearService(){
		m_forwardalarm = null;
		if(corba !=null) corba.removeKey(gather_id, "TrapProbe", "0");
	}
	/**
	 * 重新绑定corba
	 * @return boolean
	 */
	public boolean Rebind() {
		if (corba == null) {
			corba = new WebCorba("TrapProbe", gather_id, "0");
		}
		if (m_forwardalarm == null) {
			if (!BindForward())
				return false;
			return true;
		} return true;
	}	 
	/**
	 * 绑定FowardAlarm
	 * @return boolean
	 */
	private boolean BindForward() {
		try {
			if (corba == null) {
				corba = new WebCorba("TrapProbe", gather_id, "0");				
			}else{
				corba.removeKey(gather_id,"TrapProbe","0");
				corba = new WebCorba("TrapProbe", gather_id, "0");
			}
			m_forwardalarm = (I_TrapProbe.O_TrapProbe ) corba
					.getIDLCorba("TrapProbe");

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
	 * @param m_name
	 * @return
	 */	 
		public boolean I_NotifyEvent(String m_name){
			//add by suixz(5253) 2008-4-15 北京网通修改接口
			String shortName = Global.instAreaShortName;
			try{
				m_logger.debug("m_forwardalarm :"+m_forwardalarm);
				
			if (m_forwardalarm != null)
					{
						//modify by suixz(5253) 2008-4-15 北京网通修改接口
						if(BJWT.equals(shortName)){
							return m_forwardalarm.I_NotifyEvent_BJWT(m_name, gather_id);
						}else{
							return m_forwardalarm.I_NotifyEvent(m_name);
						}
					}		
			    return false;
			}catch(Exception e){
				this.clearService();
				e.printStackTrace();
				try{
					BindForward();
					if(m_forwardalarm != null){
						//modify by suixz(5253) 2008-4-15 北京网通修改接口
						if(BJWT.equals(shortName)){
							return m_forwardalarm.I_NotifyEvent_BJWT(m_name, gather_id);
						}else{
							return m_forwardalarm.I_NotifyEvent(m_name);
						}
					}					
					    return false;
					}catch(Exception ee){				
						this.clearService();
						ee.printStackTrace();
						return false;
					}	
			}
		}
	/**
	 * @param m_name
	 * @return
	 */
	public boolean I_NotifySevRuler(String m_name){
		//add by suixz(5253) 2008-4-15 北京网通修改接口
		String shortName = Global.instAreaShortName;
		try{
			m_logger.debug("m_forwardalarm :"+m_forwardalarm);
			//modify by suixz(5253) 2008-4-15 北京网通修改接口
		if(m_forwardalarm != null){
			if(BJWT.equals(shortName)){
				return m_forwardalarm.I_NotifySevRuler_BJWT(m_name, gather_id);
			}else{
				return m_forwardalarm.I_NotifySevRuler(m_name);		
			}
		}
		    return false;
		}catch(Exception e){
			this.clearService();
			e.printStackTrace();
			try{
				BindForward();
				//modify by suixz(5253) 2008-4-15 北京网通修改接口
				if(m_forwardalarm != null){
					if(BJWT.equals(shortName)){
						return m_forwardalarm.I_NotifySevRuler_BJWT(m_name, gather_id);
					}else{
						return m_forwardalarm.I_NotifySevRuler(m_name);		
					}
				}
				    return false;
				}catch(Exception ee){				
					this.clearService();
					ee.printStackTrace();
					return false;
				}
		}
	}
	public static void main(String[] args) {
	}

}