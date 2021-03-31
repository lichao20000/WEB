/**
 * 
 */
package action.confTaskView;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import bio.confTaskView.StrategyConfigBio;

/**
 * @author ASUS E-mail：qixq@lianchuang.com
 * @version $Revision$
 * @since 2009-3-13
 * @category action.confTaskView
 * 
 */
public class StrategyConfigInterfaceAction {
	
	private static Logger logger = LoggerFactory.getLogger(StrategyConfigInterfaceAction.class);
	
	private String gw_type;

	/**
	 * 商务领航开始执行业务下发
	 * 
	 * @param accOid	login user
	 * @param username 	user
	 * @param deviceId 	device
	 * 
	 * @return String
	 */
	public String bbmsExecute(long accOid, String username, String deviceId,HttpServletRequest request) {

		logger.debug("StrategyConfigInterfaceAction==>bbmsExecute({},{},{},{})", new Object[]{accOid, username, deviceId, request});
		
		ApplicationContext ctx = WebApplicationContextUtils
				.getWebApplicationContext(request.getSession()
						.getServletContext());
		StrategyConfigBio strategyConfigBio = (StrategyConfigBio) ctx
				.getBean("StrategyConfigBio");

		return strategyConfigBio.bbmsBindStrategyConfigRun(accOid, username,
				deviceId, request.getParameter("gw_type"));

	}

	/**
	 * 家庭网关开始执行业务下发
	 * 
	 * @param accOid	login user
	 * @param username 	user
	 * @param deviceId 	device
	 * 
	 * @return String
	 */
	public String itmsExecute(long accOid, String username, String deviceId,HttpServletRequest request) {

		ApplicationContext ctx = WebApplicationContextUtils
				.getWebApplicationContext(request.getSession()
						.getServletContext());
		StrategyConfigBio strategyConfigBio = (StrategyConfigBio) ctx
				.getBean("StrategyConfigBio");

		return strategyConfigBio.itmsBindStrategyConfigRun(accOid, username,null,
				deviceId, request.getParameter("gw_type"));

	}

	public String getGw_type() {
		return gw_type;
	}

	public void setGw_type(String gw_type) {
		this.gw_type = gw_type;
	}
}
