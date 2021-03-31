package com.linkage.module.itms.service.bio;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.util.StringUtil;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.util.SocketUtil;
import com.linkage.module.itms.service.dao.OperateByHistoryCQOldDAO;


public class OperateByHistoryCQOldBIO {
	
	private static Logger logger = LoggerFactory
			.getLogger(OperateByHistoryCQOldBIO.class);
	
	private OperateByHistoryCQOldDAO dao;
	
	public List<Map> getOperateByHistoryInfo(String starttime, String endtime,
			String username,String ACCOUNT_NAME,String SERIAL_NUMBER, int curPage_splitPage, int num_splitPage) {
		logger.debug("OperateByHistoryServImp->getOperateByHistoryInfo");
		List<Map> listHistory = dao.getOperateByHistoryInfoBe2015( starttime, endtime, username,ACCOUNT_NAME,SERIAL_NUMBER, curPage_splitPage, num_splitPage);
		List<Map> list = dao.getOperateByHistoryInfo( starttime, endtime, username,ACCOUNT_NAME,SERIAL_NUMBER, curPage_splitPage, num_splitPage);
		list.addAll(listHistory);
		return list;
	}

	/*public int countOperateByHistoryInfo(String starttime, String endtime,
			String username,String ACCOUNT_NAME,String SERIAL_NUMBER,int curPage_splitPage, int num_splitPage) {
		logger.debug("OperateByHistoryServImp->countOperateByHistoryInfo");
		return dao.countOperateByHistoryInfo(starttime, endtime, username, ACCOUNT_NAME,SERIAL_NUMBER, curPage_splitPage, num_splitPage);
	}*/
	
	public Map<String,String> getOperateMessage(String bss_sheet_id, String isHistory){
		logger.debug("OperateByHistoryServImp->getOperateMessage");
		if("no".equals(isHistory)){
			logger.warn("正在查询工单号为{}的（2015年之后）",bss_sheet_id);
			return dao.getOperateMessage(bss_sheet_id);
		}
		else{
			logger.warn("正在查询工单号为{}的（2015年之前）",bss_sheet_id);
			return dao.getOperateMessageBe2015(bss_sheet_id);
		}
	}
	
	public String send(String content, String bss_sheet_id, String isHistory){
		
		String server = Global.G_ITMS_Sheet_Server;
		int port = Global.G_ITMS_Sheet_Port;
		logger.warn("server："+server);
		logger.warn("port："+port);
		
		if(content.indexOf("auth_password")!=-1||content.indexOf("passwd")!=-1){
			if("no".equals(isHistory)){
				logger.warn("正在查询工单号为{}的密码（2015年之后）",bss_sheet_id);
				String pass = dao.getOperateMessagePass(bss_sheet_id);
				logger.warn("密码为："+pass);
				content = content.replace("******", pass);
			}
			else{
				logger.warn("正在查询工单号为{}的（2015年之前）",bss_sheet_id);
				String pass = dao.getOperateMessageBe2015Pass(bss_sheet_id);
				logger.warn("密码为："+pass);
				content = content.replace("******", pass);
			}
		}
		
		logger.warn("工单发送："+content);
		String retResult = SocketUtil
				.sendStrMesgCQ(server, port, content + "\n");
		if(null == retResult||StringUtil.IsEmpty(retResult)){
			logger.warn("工单返回为空");
			retResult = "工单返回结果为空";
		}
		logger.warn("工单返回："+retResult);
		return retResult;
	}

	public OperateByHistoryCQOldDAO getDao() {
		return dao;
	}

	public void setDao(OperateByHistoryCQOldDAO dao) {
		this.dao = dao;
	}

	
}
