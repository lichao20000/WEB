package com.linkage.module.ids.bio;

import java.io.StringReader;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.module.ids.dao.ReportPeroidUpateDAO;

public class ReportPeroidUpateBIO {
	private static Logger logger = LoggerFactory
			.getLogger(ReportPeroidUpateBIO.class);
	
	private  ReportPeroidUpateDAO dao;
	public void importReportTask(String acc_oid, long currTime,
			String reporttimelist, String targettimelist) {
		logger.warn("ReportPeroidUpateBIO");
		dao.importReportTask(acc_oid,currTime,reporttimelist,targettimelist);
	}

	public void insertTaskDev(long currTime, List<String> deviceList) {
		logger.warn("ReportPeroidUpateBIO");
		dao.insertTaskDev(currTime,deviceList);
	}

	public String getSendXMLStr(long currTime, String reporttimelist,
			String targettimelist, List<String> deviceList) {
		logger.warn("getSendXMLStr()");
		
		Document document = DocumentHelper.createDocument();
		document.setXMLEncoding("GBK");
		Element root = document.addElement("root");
		// 接口调用唯一ID
		root.addElement("CmdID").addText("123456789012345");
		root.addElement("CmdType").addText("CX_01");
		root.addElement("ClientType").addText("5");
		root.addElement("CmdID").addText("123456789012345");
		Element param = root.addElement("Param");
		param.addElement("taskId").addText(currTime+"");
		param.addElement("reportPeriod").addText(""+reporttimelist);
		param.addElement("targetPeriod").addText(""+targettimelist);
		Element devs = param.addElement("devs");
		for(String deviceSN : deviceList){
			devs.addElement("dev").addText(deviceSN);
		}
		logger.warn("发送的参数{}",document.asXML());
		return document.asXML();
	}

	public void updateTaskDev(String taskid, String deviceSn, String result) {
		logger.warn("ReportPeroidUpateBIO");
		dao.updateTaskDev(taskid,deviceSn,result);
	}

	public String getResultMeg(String resultString) {
		logger.warn("getResultMeg===={}",resultString);
		String message = "";
		String taskid = "";
		String deviceSn = "";
		String result = "";
		SAXReader reader = new SAXReader();
		Document document = null;
		try {
			document = reader.read(new StringReader(resultString));
			Element root2 = document.getRootElement();
			List<Element> elements = root2.elements();
			Element CmdID = elements.get(0);
			Element RstCode = elements.get(1); // RstCode
			Element RstMsg = elements.get(2);
			Element taskId = elements.get(3);
			Element devs = elements.get(4);
			if (!"CmdID".equals(CmdID.getName())
					|| !"RstCode".equals(RstCode.getName())
					|| !"RstMsg".equals(RstMsg.getName())
					|| !"devs".equals(devs.getName())) {
				logger.error("xml格式异常");
				message = "定制失败";
			} else {
				taskid = taskId.getTextTrim();
				List<Element> devList = devs.elements();

				for (Element dev : devList) {
					deviceSn = dev.getTextTrim();
					result = dev.attributeValue("result");
					this.updateTaskDev(taskid, deviceSn, result);
				}
				message = "定制成功";
			}

		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return message;
	}

	public List getTaskInfo(int curPage_splitPage, int num_splitPage,
			String starttime1, String endtime1) {
		// TODO Auto-generated method stub
		return dao.getTaskInfo(curPage_splitPage, num_splitPage,
				starttime1, endtime1);
	}

	public int getTaskInfoCount(int num_splitPage, String starttime1,
			String endtime1) {
		// TODO Auto-generated method stub
		return dao.getTaskInfoCount(num_splitPage,
				starttime1, endtime1);
	}

	public List getTaskInfoExl(String starttime1, String endtime1) {
		
		return dao.getTaskInfoExl(starttime1,endtime1);
	}

	public List getDevInfo(int curPage_splitPage, int num_splitPage,
			String taskId) {
		// TODO Auto-generated method stub
		return dao.getDevInfo(curPage_splitPage,num_splitPage,taskId);
	}

	public int getDevInfoCount(int num_splitPage, String taskId) {
		// TODO Auto-generated method stub
		return dao.getDevInfoCount(num_splitPage,taskId);
	}

	public List getDevInfoExcel(String taskId) {
		// TODO Auto-generated method stub
		return dao.getDevInfoExcel(taskId);
	}

	public void setDao(ReportPeroidUpateDAO dao) {
		this.dao = dao;
	}

}
