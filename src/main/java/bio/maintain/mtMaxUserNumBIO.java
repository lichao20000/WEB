package bio.maintain;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.paramConfig.ParamInfoCORBA;
import com.linkage.litms.paramConfig.ParamInfoDAO;
import com.linkage.litms.resource.DeviceAct;

import dao.maintain.mtMaxUserNumDAO;

public class mtMaxUserNumBIO {

	/** log */
	private static final Logger LOG = LoggerFactory.getLogger(mtMaxUserNumBIO.class);
	private mtMaxUserNumDAO mtMUNDao;

	private List<Map<String, String>> userNumList;
	
	public String getMaxUserNum(String[] devIdsArr, String fromDB) {
		
		StringBuffer bufferHTML = new StringBuffer();
		bufferHTML.append("<table width=\"100%\" border=\"0\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\">");
		
//		bufferHTML.append("<tr><td><table width=\"100%\" height=\"30\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" class=\"green_gargtd\">");
//		bufferHTML.append("<tr><td width=\"162\" align=\"center\" class=\"title_bigwhite\" nowrap>任务定制</td>");
//		bufferHTML.append("<td nowrap><img src=\"../images/attention_2.gif\" width=\"15\" height=\"12\">");
//		bufferHTML.append("</tr>");
//		bufferHTML.append("</table>");
//		bufferHTML.append("</td></tr>");
		
		
		bufferHTML.append("<tr><td bgcolor=#999999>");
		
		bufferHTML.append("<TABLE width=\"100%\" border=0 cellspacing=1 cellpadding=2 align=\"center\">");
		bufferHTML.append("<TR bgcolor=\"#FFFFFF\">");
		bufferHTML.append("<TH width=\"10%\">&nbsp;序号&nbsp;</TH>");
		bufferHTML.append("<TH width=\"10%\">&nbsp;用户账号&nbsp;</TH>");
		bufferHTML.append("<TH width=\"30%\">&nbsp;设备序列号&nbsp;</TH>");
		bufferHTML.append("<TH width=\"15%\">&nbsp;模式&nbsp;</TH>");
		bufferHTML.append("<TH width=\"15%\">&nbsp;最大用户数&nbsp;</TH>");
		bufferHTML.append("<TH width=\"15%\">&nbsp;操作&nbsp;</TH>");
		bufferHTML.append("</TR>");
		
		int num = 0;
		String mode = null;
		String totalNum = null;
		
		for (String device_id : devIdsArr) {
			
			if ("0".equals(fromDB)) {
				
			} else {
				userNumList = mtMUNDao.getMaxUserNumFromDB(device_id);
			}
			
			
			if (userNumList == null || userNumList.isEmpty()) {
				bufferHTML.append("<TR bgcolor=\"#FFFFFF\">");
				bufferHTML.append("<TD width=\"10%\" align='center'>" + (++num) + "</TD>");
				bufferHTML.append("<TD colspan='10'>没查询到设备ID为"+device_id+"相对应的数据！</TD></TR>");
			}else{
				Iterator itor = userNumList.iterator();
				
				//String tmp_user_id = "";
				while(itor.hasNext()){
					Map tmpMap = (Map)itor.next();

					mode = tmpMap.get("mode").toString();
					totalNum = tmpMap.get("total_number").toString();
					
					LOG.debug("mode:" + mode);
					LOG.debug("totalNum:" + totalNum);
					
					bufferHTML.append("<TR bgcolor=\"#FFFFFF\">");
				
					bufferHTML.append("<TD width=\"10%\" align='center'>" + (++num) + "</TD>");
					bufferHTML.append("<TD width=\"10%\" align='center'>" + tmpMap.get("username") + "</TD>");
					bufferHTML.append("<TD width=\"30%\" align='center'>" + tmpMap.get("device_serialnumber") + "</TD>");
					bufferHTML.append("<TD width=\"15%\" align='center'>" + mode + "</TD>");
					bufferHTML.append("<TD width=\"15%\" align='center'>" + totalNum + "</TD>");
						 
					//bufferHTML.append("<TD width=\"15%\" align='center'><label style=\"cursor:hand;font-weight:bold;color:green;\" onclick=\"editMUN(this,'"+device_id+"')\">编辑</label>｜<label style=\"cursor:hand;font-weight:bold;color:green;\" onclick=\"delMUN(this,'"+device_id+"')\">删除</label></TD>");
					bufferHTML.append("<TD width=\"15%\" align='center'><label style=\"cursor:hand;font-weight:bold;color:green;\" onclick=\"editMUN(this,'"+device_id+"','"+mode+"','"+totalNum+"')\">编辑</label></TD>");
					bufferHTML.append("</TR>");
				}
			}
		}
		
		bufferHTML.append("</TABLE>");
		bufferHTML.append("</td></tr>");
		bufferHTML.append("</table>");
		return bufferHTML.toString();
	}


	
	public String editMUN(String deviceId, String mode, String totalNum) {
		
		StringBuffer bufferHTML = new StringBuffer();
		bufferHTML.append("<table width=\"100%\" border=\"0\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\">");
		bufferHTML.append("<tr><td bgcolor=#999999>");
		
		bufferHTML.append("<TABLE width=\"100%\" border=0 cellspacing=1 cellpadding=2 align=\"center\">");
		bufferHTML.append("<TR bgcolor=\"#FFFFFF\">");
		bufferHTML.append("<TH colspan='10'>&nbsp;编辑&nbsp;</TH>");
		bufferHTML.append("</TR>");
		
		bufferHTML.append("<TR bgcolor=\"#FFFFFF\">");
		bufferHTML.append("<TD width=\"10%\" align='right'>模式：</TD>");
		bufferHTML.append("<TD width=\"90%\" align='left'><input type='text' id='modeId' value='" + mode + "'></TD>");
		bufferHTML.append("</TR>");
		
		bufferHTML.append("<TR bgcolor=\"#FFFFFF\">");
		bufferHTML.append("<TD width=\"10%\" align='right'>最大用户上网数：</TD>");
		bufferHTML.append("<TD width=\"90%\" align='left'><input type='text' id='totalNumId' value='" + totalNum + "'></TD>");
		bufferHTML.append("</TR>");
		
		bufferHTML.append("<TR bgcolor=\"#FFFFFF\">");
		bufferHTML.append("<TD colspan='10' align='right'><input type='button' name='' onclick=\"setParams('"+deviceId+ "')\" value=' 修 改 '></TD>");
		bufferHTML.append("</TR>");
		bufferHTML.append("</TABLE>");
		bufferHTML.append("</td></tr>");
		bufferHTML.append("</table>");
		//logger.debug(bufferHTML.toString());
		return bufferHTML.toString();
	}
	
	public String setParams(String deviceId, String mode, String totalNum) {
		ParamInfoCORBA pic = new ParamInfoCORBA();
		String[] params_name = new String[2];
		String[] params_value = new String[2];
		String[] para_type_id = new String[2];
		
		params_name[0] = "InternetGatewayDevice.Services.X_CT-COM_MWBAND.Mode";
		params_value[0]= "1";
		para_type_id[0] = "2";
		
		String gather_id = getGatherIdMap(deviceId);
		if (null == gather_id) {
			return "采集点为空";
		}
		String ior = ParamInfoDAO.getIOR(gather_id);
		if (ior == null) {
			return "IOR为空";
		}
		
		params_name[1] = "InternetGatewayDevice.Services.X_CT-COM_MWBAND.TotalTerminalNumber";
		params_value[1]= totalNum;
		para_type_id[1] = "2";
		
		boolean resultBoo = true;//pic.setParamValue_multi(params_name, params_value, para_type_id, deviceId, gather_id, ior);
		
		if (resultBoo) {
			return "设置成功";
		} else {
			return "设置失败";	
		}
	}
	
	public void setMtMUNDao(mtMaxUserNumDAO mtMUNDao) {
		this.mtMUNDao = mtMUNDao;
	}

	/**
	 * 根据device_id取得gather_id
	 * 
	 * @param device_id
	 * @return
	 */
	private String getGatherIdMap(String device_id) {
		DeviceAct act = new DeviceAct();
		HashMap<String, String> deviceInfo = act.getDeviceInfo(device_id);
		String gather_id = deviceInfo.get("gather_id");
		return gather_id;
	}
	
}
