package com.linkage.module.gtms.resource.serv;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jxl.Sheet;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.util.DateTimeUtil;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.module.gtms.resource.dao.QueryVoipPhoneByLoidDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;


public class QueryVoipPhoneByLoidServImp implements QueryVoipPhoneByLoidServ {
	
	private static Logger logger = LoggerFactory
			.getLogger(QueryVoipPhoneByLoidServImp.class);
	
	private QueryVoipPhoneByLoidDAO dao;
	
	public String queryVoipPhoneByLoid(File file, int rowNum){
		
		logger.debug("QueryVoipPhoneByLoidServImp==>queryVoipPhoneByLoid()", file);
		
		Workbook wwb = null;
		Sheet ws = null;
		
		String retMsg = "";
		StringBuffer loidBuffer = new StringBuffer("'");
		
		DateTimeUtil today = new DateTimeUtil();
		String txtFileName = "未关联LOID_"+today.getYYYYMMDDHHMMSS()+".txt";
		String excelFileName = "查询结果文件_"+today.getYYYYMMDDHHMMSS()+".xls";
		
		// 解析Excel文件
		try {
			
			wwb = Workbook.getWorkbook(file);
			
			//总sheet数
			//int sheetNumber = wwb.getNumberOfSheets();
			int sheetNumber = 1;  // 默认取第一页
			
			for (int m=0;m<sheetNumber;m++){
				ws = wwb.getSheet(m);
	       
				//当前页总记录行数和列数
				int rowCount = ws.getRows();       // 行数
				int columeCount = ws.getColumns(); // 列数
				if(rowCount > rowNum+1){
					rowCount = rowNum + 1;
				}
				
				//第一行为字段名，所以行数大于1才执行
				if (rowCount > 1 && columeCount > 0){
					
					// 将LOID进行拼装，拼装后loidBuffer格式为：'loid_1','loid_2','loid_3','loid_4','loid_5','loid_6','
					for (int i=1;i<rowCount;i++){
						String temp = ws.getCell(0, i).getContents().trim();
						if(null != temp && !"".equals(temp)){
							loidBuffer.append(temp).append("','");
						}
					}
				}else {
					logger.warn("上传的Excel文件为空！");
					retMsg = "-1";
					return retMsg;
				}
			}
			
		} catch (Exception e) {
			logger.warn("解析Excel失败！");
			retMsg = "-2";
			return retMsg;
		}
			
		// 组装后loidBuffer格式为：'loid_1','loid_2','loid_3','loid_4','loid_5','loid_6','
		// 根据不同的需要，通过substring(beginIndex, endIndex)进行了不同方式的截取
		List<HashMap<String, String>> infoList = dao.queryVoipPhoneByLoid(loidBuffer
				.toString().substring(0, loidBuffer.toString().length() - 2));
		
		StringBuffer canQueryVoipPhoneLoids = new StringBuffer("");
		
		// 根据LOID查询到cityId以及voip电话号码，并将查询结果写入Excel文件，将没有查询到voip电话号码的LOID写入TXT文件
		if (null != infoList && !"".equals(infoList) && infoList.size() > 0) {
			
			HashMap<String, List<String>> tempMap = new HashMap<String, List<String>>();
			
			
			for(HashMap<String, String> infoMap : infoList){
				
				List<String> tempList = new ArrayList<String>();  // 用于存储cityId，voipPhone
				
				String loid = StringUtil.getStringValue(infoMap, "username");
				String cityId = StringUtil.getStringValue(infoMap, "city_id");
				String voipPhone = StringUtil.getStringValue(infoMap, "voip_phone");
				
				// 根据LOID判断tempMap是否已经存在与当前LOID相关的List，如果没有直接将cityId与voipPhone放到tempList中
				// 然后将tempList放到tempMap中
				if (null == tempMap.get(loid) || "".equals(tempMap.get(loid))) {
					tempList.add(cityId);
					tempList.add(voipPhone);
					tempMap.put(loid, tempList);
				} 
				// 如果tempMap中存在与当前loid相关的List信息，那么将当前的voipPhone继续放到list中
				// 此时list中已经存在旧值，且tempMap中也存在与当前loid相关的map信息，所以需要将map中的旧值去掉，然后加入新的list
				else {
					tempMap.get(loid).add(voipPhone);  // voipPhone继续放到list中，此时与当前loid有关旧的list信息也在其中
				}
			}
			
			// 将查询到的结果写入Excel中 参数代表意思为：  标题    要写入Excel的内容   能查询到VOIP语音电话号码的LOID  Excel文件名
			String mesg = writeExcel(new String[] { "属地", "LOID", "一路语音", "二路语音" },
					tempMap, canQueryVoipPhoneLoids, excelFileName);
			if (!"OK".equals(mesg)) {
				logger.warn("生成Excel文件失败！");
				return "-3,生成Excel文件失败！";
			}else {
				logger.warn("生成Excel文件成功！");
			}
			
			// 将没有查询到VOIP电话号码的LOID写入TXT文件 参数代表意思为：  总LOID ,  能查询到VOIP电话号码的LOID  , TXT文件名
			mesg = writeTxt(loidBuffer.toString().substring(1,
					loidBuffer.toString().length() - 1), canQueryVoipPhoneLoids
					.toString(), txtFileName);
			if (!"OK".equals(mesg)) {
				logger.warn("生成TXT文件失败！");
			} else {
				logger.warn("生成TXT文件成功！");
			}
			
		}
		// 导入的帐号全部没有查询到VOIP语音电话号码
		else {
			logger.warn("导入的所有LOID均没有查询到相关的VOIP语音电话号码！");
			writeTxt(loidBuffer.toString().substring(1,
					loidBuffer.toString().length() - 1), "", txtFileName);
		}
		
		retMsg = "0,"+txtFileName+","+excelFileName;
		return retMsg;
		
	}
	
	
	
	/**
	 * 将查询到的数据写到Excel中
	 * 
	 * @param title  标题
	 * @param tempMap
	 * @param loidSB  存放能查询到VOIP语音电话号码的LOID
	 * @return
	 */
	public String writeExcel(String[] title, HashMap<String, List<String>> tempMap,
			StringBuffer canQueryVoipPhoneLoids, String excelFileName) {
		
		logger.debug("QueryVoipPhoneByLoidServImp==>writeExcel()");
		
		FileOutputStream fos = null;
		
        try {
        	
        	String excelFile = LipossGlobals.G_ServerHome
					+ LipossGlobals.getLipossProperty("LoidFilePath") + excelFileName;
        	
        	fos = new FileOutputStream(excelFile);
        	
        	Map cityMap = CityDAO.getCityIdCityNameMap();
        	
        	// 创建一个excel文件，用于存放： 属地    LOID   一路语音  二路语音
			WritableWorkbook workbook = Workbook.createWorkbook(fos); 
			
			WritableSheet ws = workbook.createSheet("Sheet1", 0);//创建sheet
            
            int rowNum = 0;    //要写的行，jxl操作excel时，第一行是从0开始，以此类推
            
            if(title != null) {
                putRow(ws, 0, title);//压入标题
            }
            
         // 将属地，LOID，一路语音，二路语音放入数组，然后将数组方式List中，便于
			for(Object obj : tempMap.keySet()) {
				
				rowNum = rowNum + 1;
				
				String cityName = "";
				String phoneNum1 = "";
				String phoneNum2 = "";
				
			    String loid = (String)obj;
			    
			    canQueryVoipPhoneLoids.append(loid).append(",");
			    
			    List<String> valueList = tempMap.get(loid);
			    
			    if (valueList.size() == 1) {
			    	String cityId = valueList.get(0);
			    	cityName = StringUtil.getStringValue(cityMap, cityId);
				}
			    
			    if (valueList.size() == 2) {
			    	String cityId = valueList.get(0);
			    	cityName = StringUtil.getStringValue(cityMap, cityId);
			    	phoneNum1 = valueList.get(1);
				}
			    
			    if (valueList.size() == 3) {
			    	String cityId = valueList.get(0);
			    	cityName = StringUtil.getStringValue(cityMap, cityId);
			    	phoneNum1 = valueList.get(1);
			    	phoneNum2 = valueList.get(2);
				}
			    
			    Object[] cells = (Object[]) new String[]{cityName, loid, phoneNum1, phoneNum2};
			    putRow(ws, rowNum, cells);    //压一行到sheet
			    
			    cells = null; 
			}
            
            workbook.write();
            workbook.close();    //一定要关闭, 否则没有保存Excel
            
            try {
            	if (null != workbook) {
            		workbook = null;
				}
            	if (null != workbook) {
            		workbook = null;
				}
			} catch (Exception e1) {
				logger.error("exception: msg={}", e1.getMessage());
			}
            
			return "OK";
			
        } catch (Exception e) {
        	logger.warn("写Excel是出现异常！");
        	logger.error("jxl write Excel exception: msg={}", e.getMessage());
        	return "fault";
        }
	}
	
	
	// 写标题(属地  LOID  一路语音  二路语言) 及 内容
    private void putRow(WritableSheet ws, int rowNum, Object[] cells) throws RowsExceededException, WriteException {
    	
    	logger.debug("QueryVoipPhoneByLoidServImp==>putRow()");
    	
        for(int j=0; j<cells.length; j++) {//写一行
            Label cell = new Label(j, rowNum, ""+cells[j]);
            ws.addCell(cell);
            cell = null;
        }
    }
    
    
    /**
     * 将没有查询到VOIP语音电话号码的LOID写入TXT文本
     * 
     * @param loids       所有的LOID
     * @param loidSB      能检索到VOIP语音电话号码的LOID
     * @param fileName1   存放未能查询到VOIP语音电话号码的LOID的文件名
     * 
     * @return
     */
    public String writeTxt(String allLoids, String canQueryVoipPhoneLoids, String txtFileName) {
		
    	logger.debug("QueryVoipPhoneByLoidServImp==>writeTxt()");
    	
    	FileWriter fw = null;
		BufferedWriter bw = null;
		
		try {
			File f = new File(LipossGlobals.G_ServerHome
					+ LipossGlobals.getLipossProperty("LoidFilePath") + txtFileName);
			
			if (f.exists()){
				logger.warn("文件"+txtFileName+"已存在！");
			}else{
				f.createNewFile();
			}
			
			fw = new FileWriter(f);
			bw = new BufferedWriter(fw);
			
		} catch (Exception e) {
			logger.warn("创建文件"+txtFileName+"失败！");
			logger.error("create file "+txtFileName+" fail, mesg({})", e.getMessage());
			return "fail";
		}
		
		allLoids = allLoids.replaceAll("'", "");
		
		if (!"".equals(canQueryVoipPhoneLoids)) {
			
			String[] loidArray = canQueryVoipPhoneLoids.substring(0,
					canQueryVoipPhoneLoids.length() - 1).split(",");
			
	    	for (int i = 0; i < loidArray.length; i++) {
	    		allLoids = allLoids.replaceAll(loidArray[i]+",", "");
			}
	    	
		}
    	
    	String [] allLoidArray = allLoids.split(",");
    	try {
    		bw.write("未查询到语音电话的LOID" + "\r\n");
    		for (int i = 0; i < allLoidArray.length; i++) {
	    		if (null != bw) {
					bw.write(allLoidArray[i]+"\r\n");
				}else {
					logger.warn(" BufferedWriter 为空，写文件失败!");
					return "faile";
				}
    		}
		} catch (Exception e) {
			logger.warn("写文件"+txtFileName+"失败！");
			logger.error("write file "+txtFileName+" fail, mesg({})", e.getMessage());
			return "fail";
		}finally{
			try {
				if(null != bw){
					bw.close();
					bw=null;
				}
			} catch (Exception e) {
				logger.warn("流关闭失败！");
				logger.error("Closes the stream fail, mesg({})", e.getMessage());
			}
			
			try {
				if (null != fw) {
					fw.close();
					fw=null;
				}
			} catch (Exception e) {
				logger.warn("流关闭失败！");
				logger.error("Closes the stream fail, mesg({})", e.getMessage());
			}
		}
    	
    	return "OK";
	}


	
	public QueryVoipPhoneByLoidDAO getDao() {
		return dao;
	}

	
	public void setDao(QueryVoipPhoneByLoidDAO dao) {
		this.dao = dao;
	}
	
	
	
	
}
