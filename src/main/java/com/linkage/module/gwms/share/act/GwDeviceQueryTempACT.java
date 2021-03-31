/**
 * 
 */
package com.linkage.module.gwms.share.act;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.linkage.module.gwms.share.bio.GwDeviceQueryBIO;

/**
 * @author zxj E-mail：qixq@lianchuang.com
 * @version $Revision$
 * @since 2009-12-25
 * @category com.linkage.module.stb.resource.act
 * 
 */
public class GwDeviceQueryTempACT {
	
	/**
	 *  serial
	 */
	private static final long serialVersionUID = 1L;
	
	// 日志记录
	private static Logger logger = LoggerFactory
			.getLogger(GwDeviceQueryTempACT.class);

	public List getDeviceList(long areaId,String param,HttpServletRequest request){
		logger.debug("GwDeviceQueryTempACT=>getDeviceList({},{})",areaId,param);
		ApplicationContext ctx = 
			WebApplicationContextUtils.getWebApplicationContext(request.getSession().getServletContext());
		GwDeviceQueryBIO gwDeviceQueryBIO = (GwDeviceQueryBIO) ctx.getBean("GwDeviceQueryBIO");

		return gwDeviceQueryBIO.getDeviceList("1",areaId, param);
	}
	
}
