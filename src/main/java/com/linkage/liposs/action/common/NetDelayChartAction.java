package com.linkage.liposs.action.common;

import org.jfree.chart.JFreeChart;

import com.linkage.liposs.buss.util.PingTest;
import com.opensymphony.xwork2.ActionSupport;

/**
 * 
 * @author 刘伟(5153)
 * @version 1.0
 * @since 2007-08-21
 * @category 首页子页面显示网络时延时序图片的逻辑控制action类
 */
public class NetDelayChartAction extends ActionSupport {

	private static final long serialVersionUID = -4178628042196599149L;

	public PingTest pingTest;

	private String device_ip = "";

	private JFreeChart chart;

	public String execute() {
		return SUCCESS;
	}

	public String netDelay() {
		chart = pingTest.getGraphicsCursor(device_ip);
		return SUCCESS;
	}

	public void setPingTest(PingTest pingTest) {
		this.pingTest = pingTest;
	}

	public void setDevice_ip(String device_ip) {
		this.device_ip = device_ip;
	}

	public JFreeChart getChart() {
		return chart;
	}
}
