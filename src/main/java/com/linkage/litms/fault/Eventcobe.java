package com.linkage.litms.fault;
/**
 * 重新绑定corba
 * liuli 2007-3-15
 * @return boolean
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.common.corba.WebCorba;
public class Eventcobe {
	private static Logger m_logger = LoggerFactory.getLogger(Eventcobe.class);

	private static boolean m_isBind = false;
	

	private static EventRule.EventRuleObserver  m_forwardalarm = null;

	private static WebCorba corba = null;

	private String gather_id = null;

	public Eventcobe(String gather_id) {
		this.gather_id = gather_id;
			BindForward();
	}
	public void clearService(){
		m_forwardalarm = null;
		corba = null;
	}
	/**
	 * 重新绑定corba
	 * 
	 * @return boolean
	 */
	public boolean Rebind() {
		try{	
			corba.refreshCorba("java");
			m_forwardalarm = (EventRule.EventRuleObserver ) corba.getIDLCorba("Engine");
		}catch(Exception e1){
			m_logger.warn(gather_id+"   Corba Bulid Failed!!!!\n\t");
			e1.printStackTrace();
			return false;
		}
		
		return true;
	}

	 
	/**
	 * 绑定Engine
	 * 
	 * @return boolean
	 */
	private boolean BindForward() {
		try {

			corba = new WebCorba("Engine", gather_id, "0");
		
			m_forwardalarm = (EventRule.EventRuleObserver ) corba.getIDLCorba("Engine");

			m_logger.debug(m_forwardalarm.toString());
			m_isBind = true;
		} catch (Exception ex) {
			try{	
				corba.refreshCorba("java");
				m_forwardalarm = (EventRule.EventRuleObserver ) corba.getIDLCorba("Engine");
			}catch(Exception e1){
				m_logger.warn(gather_id+"   Corba Bulid Failed!!!!\n\t");
				e1.printStackTrace();
			}
		}

		return m_isBind;
	}
	/**
	 * 
	 * @param m_name
	 * @return
	 */
	 
		public boolean winkRuleAdd(String ruleIDS){
			try{
				m_logger.warn("m_forwardalarm :"+m_forwardalarm);
			if(m_forwardalarm != null)
				m_forwardalarm.winkRuleAdd(ruleIDS);
			}catch(Exception e){
				try{	
					if(this.Rebind()&&m_forwardalarm != null)
						m_forwardalarm.winkRuleAdd(ruleIDS);
				}catch(Exception e1){
					this.clearService();
					e.printStackTrace();
					return false;
				}
			}
			return true;
		}
		/**
		 * 
		 * @param m_name
		 * @return
		 */
		public boolean winkRuleDelete(String ruleIDS){
			try{
				m_logger.warn("m_forwardalarm :"+m_forwardalarm);
			if(m_forwardalarm != null)
				 m_forwardalarm.winkRuleDelete(ruleIDS);
			}catch(Exception e){
				try{	
					if(this.Rebind()&&m_forwardalarm != null)
						m_forwardalarm.winkRuleDelete(ruleIDS);
				}catch(Exception e1){
					this.clearService();
					e.printStackTrace();
					return false;
				}
			}
			return true;
		}
	
		/**
		 * 
		 * @param m_name
		 * @return
		 */
		public boolean winkRuleModify(String ruleIDS){
			try{
				m_logger.warn("m_forwardalarm :"+m_forwardalarm);
			if(m_forwardalarm != null)
				m_forwardalarm.winkRuleModify(ruleIDS);
			}catch(Exception e){
				try{	
					if(this.Rebind()&&m_forwardalarm != null)
						m_forwardalarm.winkRuleModify(ruleIDS);
				}catch(Exception e1){
					this.clearService();
					e.printStackTrace();
					return false;
				}
			}
			return true;
		}
		/**
		 * 
		 * @param m_name
		 * @return
		 */
		public boolean relateRuleAdd(String ruleIDS){
			try{
				m_logger.warn("m_forwardalarm :"+m_forwardalarm);
			if(m_forwardalarm != null)
				 m_forwardalarm.relateRuleAdd(ruleIDS);
			}catch(Exception e){
				try{	
					if(this.Rebind()&&m_forwardalarm != null)
						m_forwardalarm.relateRuleAdd(ruleIDS);
				}catch(Exception e1){
					this.clearService();
					e.printStackTrace();
					return false;
				}
			}
			return true;
		}
		/**
		 * 
		 * @param m_name
		 * @return
		 */
		public boolean relateRuleDelete(String ruleIDS){
			try{
				m_logger.warn("m_forwardalarm :"+m_forwardalarm);
			if(m_forwardalarm != null)
				 m_forwardalarm.relateRuleDelete(ruleIDS);
			}catch(Exception e){
				try{	
					if(this.Rebind()&&m_forwardalarm != null)
						m_forwardalarm.relateRuleDelete(ruleIDS);
				}catch(Exception e1){
					this.clearService();
					e.printStackTrace();
					return false;
				}
			}
			return true;
		}
		/**
		 * 
		 * @param m_name
		 * @return
		 */
		public boolean relateRuleModify(String ruleIDS){
			try{
				m_logger.warn("m_forwardalarm :"+m_forwardalarm);
			if(m_forwardalarm != null)
				 m_forwardalarm.relateRuleModify(ruleIDS);
			}catch(Exception e){
				try{	
					if(this.Rebind()&&m_forwardalarm != null)
						m_forwardalarm.relateRuleModify(ruleIDS);
				}catch(Exception e1){
					this.clearService();
					e.printStackTrace();
					return false;
				}
			}
			return true;
		}
	

		
		/**
		 * 
		 * @param m_name
		 * @return
		 */
		public boolean filterRuleAdd(String ruleIDS){
			try{
				m_logger.warn("m_forwardalarm :"+m_forwardalarm+"ruleIDS="+ruleIDS);
			if(m_forwardalarm != null)
				 m_forwardalarm.filterRuleAdd(ruleIDS);
			}catch(Exception e){
				try{	
					if(this.Rebind()&&m_forwardalarm != null)
						m_forwardalarm.filterRuleAdd(ruleIDS);
				}catch(Exception e1){
					this.clearService();
					e.printStackTrace();
					return false;
				}
			}
			return true;
		}
		/**
		 * 
		 * @param m_name
		 * @return
		 */
		public boolean filterRuleDelete(String ruleIDS){
			try{
				m_logger.warn("m_forwardalarm :"+m_forwardalarm);
			if(m_forwardalarm != null)
				 m_forwardalarm.filterRuleDelete(ruleIDS);
			}catch(Exception e){
				try{	
					if(this.Rebind()&&m_forwardalarm != null)
						m_forwardalarm.filterRuleDelete(ruleIDS);
				}catch(Exception e1){
					this.clearService();
					e.printStackTrace();
					return false;
				}
			}
			return true;
		}
	/**
	 * 
	 * @param m_name
	 * @return
	 */
	public boolean filterRuleModify(String ruleIDS){
		try{
			m_logger.warn("m_forwardalarm :"+m_forwardalarm);
		if(m_forwardalarm != null)
		  m_forwardalarm.filterRuleModify(ruleIDS);
		}catch(Exception e){
			try{	
				if(this.Rebind()&&m_forwardalarm != null)
					m_forwardalarm.filterRuleModify(ruleIDS);
			}catch(Exception e1){
				this.clearService();
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}
	public static void main(String[] args) {
	}

}