package com.linkage.litms.init;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.Global;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.paramConfig.BatchConfigServlet;
import com.linkage.module.gwms.init.bio.AppInitBIO;
import com.linkage.module.intelspeaker.verconfigfile.InitIntelSpeaker;
import com.linkage.module.itms.resource.util.DeviceTypeUtil;

/**
 * app init
 * 
 * @author wangp
 * @author Alex.Yan (yanhj@lianchuang.com)
 * @version 2.0, Jul 10, 2008
 * @see
 * @since 1.0
 */
public class AppInitServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/** log */
	private static Logger logger = LoggerFactory
			.getLogger(AppInitServlet.class);

	public void init() throws ServletException {
		super.init();

		// init server home.
		LipossGlobals.G_ServerHome = getServletContext().getRealPath("");

		Global.G_ACS4H_CORBA_NAME = this.getInitParameter("ACS4H_CORBA_NAME");
		Global.G_ACS4E_CORBA_NAME = this.getInitParameter("ACS4E_CORBA_NAME");
		logger.warn("ACS4H_CORBA_NAME:{}", Global.G_ACS4H_CORBA_NAME);
		logger.warn("ACS4E_CORBA_NAME:{}", Global.G_ACS4E_CORBA_NAME);

		//Global.GW_CITY_MAP_INFO = new UserInstAct().getGw_city_map_info();
		//logger.warn("GW_CITY_MAP_INFO:{}", Global.GW_CITY_MAP_INFO);
		
		// 启动ping检测任务
		//		PingCheck.getInstance();

		DBInit.GetInstance().initServ_Type_Map();
		DBInit.GetInstance().initOper_Type();
		DBInit.GetInstance().initService();
		DBInit.GetInstance().initUsertype();
		DBInit.GetInstance().initDeviceType();

		//init corba
		//DBInit.GetInstance().initMidwareCorba();
		//		DBInit.GetInstance().initACS4HCorba();
		//		DBInit.GetInstance().initACS4ECorba();

		//启动批量配置监听线程
		BatchConfigServlet.startLitener();

		//init corba.
		AppInitBIO appInitBIO = new AppInitBIO();
		appInitBIO.init();
		DeviceTypeUtil.init();
		//初始化智能音箱相关配置
		new InitIntelSpeaker().init();
	}

	public void destroy() {
		// ping检测线程关闭
		//		PingCheck.getInstance().stopThread();
		super.destroy();
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
