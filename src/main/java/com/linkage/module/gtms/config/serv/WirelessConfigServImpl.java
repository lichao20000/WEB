package com.linkage.module.gtms.config.serv;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.util.DateTimeUtil;
import com.linkage.module.gtms.config.dao.WirelessConfigDAO;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.share.act.FileUploadAction;
import com.linkage.module.gwms.util.CreateObjectFactory;
import com.linkage.module.gwms.util.SocketUtil;
import com.linkage.module.gwms.util.StringUtil;

public class WirelessConfigServImpl implements WirelessConfigServ {
	private static Logger logger = LoggerFactory
			.getLogger(WirelessConfigServImpl.class);
	private WirelessConfigDAO dao;
	// 回传消息
	private String msg = null;
	// 查询条件
	private String importQueryField = "username";

	public String getUserInfo(String deviceId) {
		logger.debug("getUserInfo({})", deviceId);
		return dao.getUserInfo(deviceId);
	}

	public String doConfig(String deviceId, String gwType) {
		logger.debug("doConfig({})", deviceId);
		String res = "0";
		try {
			dao.doConfig(deviceId);
			res = "1";
		} catch (Exception e) {
			logger.warn("更新ssid2Status失败");
			res = "-1";
		}
		if ("1".equals(res)) {
			if (1 == CreateObjectFactory.createPreProcess(gwType).processDeviceStrategy(
					new String[] { deviceId }, "1111", new String[] { "1" })) {
				logger.debug("调用后台预读模块成功");
				res = "1";
			} else {
				logger.warn("调用后台预读模块失败");
				res = "-4";
			}
		}
		return res;
	}

	/**
	 * 各个返回值 1该业务用户存在 0该用户不存在 -1该业务用户不存在,但是用户存在 -2用户未绑定设备
	 */
	@SuppressWarnings("unchecked")
	public String getServUserInfo(String username, String gwType) {
		logger.debug("getServUserInfo({},{})", username, gwType);
		String flag = "";
		String userId = "";
		String deviceId = "";
		String usernameChoose = "";
		Map map = null;
		List list = dao.isExists(username, gwType);
		if (null != list) {
			if (list.size() == 1) {
				flag = "1";
				map = (Map) list.get(0);
				userId = StringUtil.getStringValue(map.get("user_id"));
				deviceId = StringUtil.getStringValue(map.get("device_id"));
				usernameChoose = StringUtil.getStringValue(map.get("username"));
				if (StringUtil.IsEmpty(userId)) {
					flag = "-1";
				}
				if (StringUtil.IsEmpty(deviceId)) {
					flag = "-2";
				}
			} else if (list.size() > 1) {
				StringBuffer flagtParam = new StringBuffer();;
				flagtParam.append("2,");
				for(int i =0;i<list.size();i++){
					map = (Map) list.get(i);
					usernameChoose = StringUtil.getStringValue(map.get("username"));
					flagtParam.append("<span style='display:inline-block;width:132px' ><input type='radio' class=jianbian name='LOID' value='"+usernameChoose+"' />"+usernameChoose+"</span>");
				}
				flag = flagtParam.toString();
			} else {
				flag = "0";
			}
		}
		return flag;
	}

	public String sendSSIDSheet(String netUsername, String cityId,
			String gwType, String netNum, String loidParam) {
		logger.debug("sendSSIDSheet({},{})", netUsername, gwType);
		
		DateTimeUtil dt = new DateTimeUtil();
		String loid;
		if(!"".equals(loidParam)){
			loid = loidParam;
		}else{
			loid = dao.getLoid(netUsername, gwType);
		}
		StringBuffer bssSheet = new StringBuffer("");
		bssSheet.append("6").append("|||");
		bssSheet.append("50").append("|||");
		bssSheet.append("1").append("|||"); // 开户
		bssSheet.append(dt.getYYYYMMDDHHMMSS()).append("|||");
		bssSheet.append(cityId).append("|||");
		bssSheet.append(netUsername).append("|||");
		bssSheet.append(netNum).append("|||");
		bssSheet.append(loid);
		bssSheet.append("LINKAGE");

		return this.sendSheet(bssSheet.toString(), gwType);
	}
	
	public String getResultMeg(String resultString) {
		logger.warn("getResultMeg===={}",resultString);
		String rstCode = "";
		String rstMsg = "";
		String message = "";
		SAXReader reader = new SAXReader();
		Document document = null;
		try {
			document = reader.read(new StringReader(resultString));
			Element root2 = document.getRootElement();
			List<Element> elements = root2.elements();
			Element CmdID = elements.get(0);
			Element RstCode = elements.get(1);
			Element RstMsg = elements.get(2);
			Element SN = elements.get(3);
			Element FailureReason = elements.get(4);
			Element SuccStatus = elements.get(5);
			if (!"CmdID".equals(CmdID.getName())
					|| !"RstCode".equals(RstCode.getName())
					|| !"RstMsg".equals(RstMsg.getName())
					|| !"SN".equals(SN.getName())
					|| !"FailureReason".equals(FailureReason.getName())
					|| !"SuccStatus".equals(SuccStatus.getName())) {
				logger.error("xml格式异常");
				message = "接收XML异常";
			} else {
				rstCode = RstCode.getTextTrim();
				rstMsg =  RstMsg.getTextTrim();
				message = rstMsg;
			}
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return message;
	}
	
	public String sendCloseSSIDXML(String username, String loidParam) {
		Document document = DocumentHelper.createDocument();
		document.setXMLEncoding("GBK");
		Element root = document.addElement("root");
		// 接口调用唯一ID
		root.addElement("CmdID").addText("123456789012345");
		root.addElement("CmdType").addText("CX_01");
		root.addElement("ClientType").addText("3");
		Element param = root.addElement("Param");
		param.addElement("UserInfoType").addText("1");
		param.addElement("UserLOID").addText(""+loidParam);
		param.addElement("UserName").addText(""+username);
		return document.asXML();
	}

	public String sendCloseSSIDSheet(String netUsername, String cityId,
			String gwType) {
		logger.debug("sendCloseSSIDSheet({},{})", netUsername, gwType);

		DateTimeUtil dt = new DateTimeUtil();
		StringBuffer bssSheet = new StringBuffer("");
		bssSheet.append("6").append("|||");            
		bssSheet.append("50").append("|||");           
		bssSheet.append("3").append("|||"); // 销户            
		bssSheet.append(dt.getYYYYMMDDHHMMSS()).append("|||");            
		bssSheet.append(cityId).append("|||");             
		bssSheet.append(netUsername);                 
		bssSheet.append("LINKAGE");                 

		return this.sendSheet(bssSheet.toString(), gwType);
	}

	public String SendBathOpenSheet(List<String> usernameList,
			List<String> netNumList, String cityId, String gwType) {
		DateTimeUtil dt = new DateTimeUtil();
		StringBuffer bssSheet = new StringBuffer("");
		String returns = "";
		for (int i = 0; i < usernameList.size(); i++) {
			bssSheet = new StringBuffer("");
			String username = usernameList.get(i);
			String netNum = netNumList.get(i);
			String loid = dao.getLoid(username, gwType);
			bssSheet.append("6").append("|||");
			bssSheet.append("50").append("|||");
			bssSheet.append("1").append("|||"); // 开户
			bssSheet.append(dt.getYYYYMMDDHHMMSS()).append("|||");
			bssSheet.append(cityId).append("|||");
			bssSheet.append(username).append("|||");
			bssSheet.append(netNum).append("|||");
			bssSheet.append(loid);
			bssSheet.append("LINKAGE");

			String tmp = this.sendSheet(bssSheet.toString(), gwType);
			if (tmp.split("|||").length > 2) {
				returns = tmp;
			}
		}
		return returns;
	}

	/**
	 * 向工单接口发送模拟的BSS工单。正常返回工单接口的回单结果，如果过程中出现问题返回null
	 * 
	 * @return String 回单信息
	 */
	public String sendSheet(String bssSheet, String gw_type) {
		logger.debug("sendSheet({})", bssSheet);
		if (StringUtil.IsEmpty(bssSheet)) {
			logger.warn("sendSheet is null");
			return null;
		}

		if ("1".equals(gw_type)) {
			return SocketUtil.sendStrMesg(Global.G_ITMS_Sheet_Server,
					Global.G_ITMS_Sheet_Port, bssSheet + "\n");
		} else {
			return SocketUtil.sendStrMesg(Global.G_BBMS_Sheet_Server,
					Global.G_BBMS_Sheet_Port, bssSheet + "\n");
		}
	}

	public WirelessConfigDAO getDao() {
		return dao;
	}

	public void setDao(WirelessConfigDAO dao) {
		this.dao = dao;
	}

	@Override
	public List getDeviceList(String gw_type, String fileName) {
		logger.debug("getDeviceList({})", new Object[] { fileName });
		if (fileName.length() < 4) {
			this.msg = "上传的文件名不正确！";
			return null;
		}
		String fileName_ = fileName.substring(fileName.length() - 3,
				fileName.length());
		if (!"xls".equals(fileName_) && !"txt".equals(fileName_)) {
			this.msg = "上传的文件格式不正确！";
			return null;
		}
		List<String> dataList = null;
		try {
			if ("txt".equals(fileName_)) {
				dataList = getImportDataByTXT(fileName);
			} else {
				dataList = getImportDataByXLS(fileName);
			}
		} catch (FileNotFoundException e) {
			logger.warn("{}文件没找到！", fileName);
			this.msg = "文件没找到！";
			return null;
		} catch (IOException e) {
			logger.warn("{}文件解析出错！", fileName);
			this.msg = "文件解析出错！";
			return null;
		} catch (Exception e) {
			logger.warn("{}文件解析出错！", fileName);
			this.msg = "文件解析出错！";
			return null;
		}
		logger.warn("dataList = " + dataList);
		if (dataList.size() < 1) {
			this.msg = "文件未解析到合法数据！";
			return null;
		}
		return dataList;
	}

	/**
	 * 解析文件（TXT格式）
	 * 
	 * @param fileName
	 *            文件名
	 * @return
	 * @throws FileNotFoundException
	 *             IOException
	 */
	public List<String> getImportDataByTXT(String fileName)
			throws FileNotFoundException, IOException {
		logger.warn("getImportDataByTXT{}", fileName);
		List<String> list = new ArrayList<String>();
		BufferedReader in = new BufferedReader(new FileReader(getFilePath()
				+ fileName));
		String line = in.readLine();
		// 从第二行开始读取数据，并对每行数据去掉首尾空格
		while ((line = in.readLine()) != null) {
			if (!"".equals(line.trim())) {
				list.add(line.trim());
			}
		}
		in.close();
		in = null;
		// 处理完毕时，则删掉文件
		File f = new File(getFilePath() + fileName);
		f.delete();
		f = null;
		return list;
	}

	/**
	 * 解析文件（xls格式）
	 * 
	 * @param fileName
	 *            文件名
	 * @return
	 * @throws IOException
	 * @throws BiffException
	 * @throws
	 * 
	 */
	public List<String> getImportDataByXLS(String fileName)
			throws BiffException, IOException {
		logger.debug("getImportDataByXLS{}", fileName);
		List<String> list = new ArrayList<String>();
		File f = new File(getFilePath() + fileName);
		Workbook wwb = Workbook.getWorkbook(f);
		Sheet ws = null;
		// 总sheet数
		int sheetNumber = 1;
		for (int m = 0; m < sheetNumber; m++) {
			ws = wwb.getSheet(m);
			if (null != ws.getCell(0, 0).getContents()) {
				String line = ws.getCell(0, 0).getContents().trim();
				/**
				 * 判断导入文件查询的条件，默认为用户账号，当匹配到设备序列号时， 则以设备序列号作为条件，否则一律以用户账号作为条件
				 */
				if (null != line && "宽带帐号".equals(line)) {
					this.importQueryField = "username";
				}
			}
			// 当前页总记录行数和列数
			int rowCount = ws.getRows();
			// 取当前页所有值放入list中
			for (int i = 1; i < rowCount; i++) {
				String usernames = ws.getCell(0, i).getContents();
				String netNums = ws.getCell(1, i).getContents();
				if (null != usernames && !"".equals(usernames)
						&& null != netNums && !"".equals(netNums)) {
					if (!"".equals(ws.getCell(0, i).getContents().trim())
							&& !"".equals(ws.getCell(1, i).getContents().trim())) {
						list.add(ws.getCell(0, i).getContents().trim() + ","
								+ ws.getCell(1, i).getContents().trim());

					}
				}
			}
		}
		f.delete();
		f = null;
		return list;
	}

	/**
	 * 获取文件路径
	 * 
	 * @return
	 */
	public String getFilePath() {
		// 获取系统的绝对路径
		String lipossHome = "";
		String a = FileUploadAction.class.getResource("/").getPath();
		try {
			lipossHome = java.net.URLDecoder.decode(
					a.substring(0, a.lastIndexOf("WEB-INF") - 1), "UTF-8");
		} catch (Exception e) {
			logger.error(e.getMessage());
			lipossHome = null;
		}
		logger.warn("{}待解析的文件路径", lipossHome);
		return lipossHome + "/temp/";
	}

	/**
	 * @return the MSG
	 */
	public String getMsg() {
		return msg;
	}

	/**
	 * @param msg
	 *            the MSG to set
	 */
	public void setMsg(String msg) {
		this.msg = msg;
	}
}
