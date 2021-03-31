package com.linkage.litms.init;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.linkage.litms.paramConfig.StrategyThread_OperateDB;

public class StrategyThread extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public void init() throws ServletException {
		//init server home.
		//LipossGlobals.G_ServerHome = getServletContext().getRealPath("");
		//LipossGlobals.All_City_Info = new CityAct().getCityInfoMap();
		//启动操作数据库的线程
		StrategyThread_OperateDB.getInstance();
	}

	public void destroy() {
		//启动操作数据库的线程关闭
		//PingCheck.getInstance().stopThread();
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
