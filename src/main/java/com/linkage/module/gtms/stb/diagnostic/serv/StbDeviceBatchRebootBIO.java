/**
 * 
 */

package com.linkage.module.gtms.stb.diagnostic.serv;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.util.DateTimeUtil;
import com.linkage.commons.util.StringUtil;
import com.linkage.module.gtms.stb.diagnostic.dao.StbDeviceBatchRebootDAO;
import com.linkage.module.gtms.stb.utils.SslUtils;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.share.act.FileUploadAction;
import com.linkage.module.gwms.util.corba.DevReboot;


/**
 * @author zxj E-mail：qixq@lianchuang.com
 * @version $Revision$
 * @since 2009-12-25
 * @category com.linkage.module.stb.resource.act
 */
public class StbDeviceBatchRebootBIO
{
	private static Logger logger = LoggerFactory.getLogger(StbDeviceBatchRebootBIO.class);
	private StbDeviceBatchRebootDAO dao;
	
	public String reboot(String deviceId, String gw_type)
	{
		return String.valueOf(DevReboot.reboot(deviceId, gw_type));
	}

	/**
	 * 恢复出厂设置
	 * 
	 * @param deviceId
	 *            ，设备ID
	 * @return 恢复出厂设置结果，never null
	 */
	public String restore(String deviceId, String gw_type)
	{
		return String.valueOf(DevReboot.reset(deviceId, gw_type));
	}
	
	/**
	 * 
	 * @param cityId
	 * @return
	 */
	public String getNextCity(String cityId) {
		List list = CityDAO.getAllNextCityListByCityPidCore(cityId);
		
		StringBuffer bf = new StringBuffer();
		for (int i = 0; i < list.size(); i++) {
			Map map = (Map) list.get(i);
			if(null!=map && "省中心".equals(map.get("city_name"))){
				continue;
			}
			if (i > 0)
				bf.append("#");
			bf.append(map.get("city_id"));
			bf.append("$");
			bf.append(map.get("city_name"));
		}

		return bf.toString();
	}
	
	/**
	 * 增加任务
	 * @param taskId
	 * @param cityId
	 * @param taskName
	 * @param acc_oid
	 * @param taskTime
	 * @param uploadCustomer
	 * @param uploadFileName4Customer
	 * @param taskType
	 * @param startTime
	 * @return
	 */
	public String importConfig(long taskId, String cityId, String taskName, long acc_oid, long taskTime, File uploadCustomer,
			String uploadFileName4Customer, String taskType, String startTime) {		
		    logger.warn("importConfig(taskId[{}], cityId[{}], taskName[{}], acc_oid[{}], addTime[{}], uploadFileName4Customer[{}]," +
		    		"taskType[{}])",new Object[]{taskId, cityId, taskName, acc_oid, taskTime, uploadFileName4Customer, taskType});
		    String filePath = "";
		    if("2".equals(taskType)){
		    	if (null != uploadCustomer && uploadCustomer.isFile() && uploadCustomer.length() > 0) {
					// 如果是导入帐号，将导入帐号的文件放到指定的目录底下
					String targetDirectory = "";
					filePath = "/accountFile";
					String targetFileName = "";
					HttpServletRequest request = null;
					try {
						// 将文件存放到指定的路径中
						request = ServletActionContext.getRequest();
						targetDirectory = ServletActionContext.getServletContext().getRealPath(filePath);
						// 由于传输的文件名中到后台有中文乱码所以存储到数据库更改文件名
						targetFileName = new DateTimeUtil().getYYYYMMDDHHMMSS() + "_" + uploadFileName4Customer;
						File target = new File(targetDirectory, targetFileName);
						FileUtils.copyFile(uploadCustomer, target);
					} catch (IOException e) {
						e.printStackTrace();
						return "批量导入重启，上传文件时出错!";
					}
					filePath = request.getScheme()+"://" + request.getLocalAddr() + ":"	+ request.getServerPort() + request.getContextPath()
							+ filePath + "/" + targetFileName;
					List<String> accountList = new ArrayList<String>();
					try {
						if (!StringUtil.IsEmpty(filePath) && filePath.endsWith("txt")) {
							accountList = parseTxt(filePath,"accountFile/"+targetFileName);
						} else if (!StringUtil.IsEmpty(filePath) && filePath.endsWith("xls")) {
							accountList = parseExcel(filePath,"accountFile/"+targetFileName);
						}
					} catch (Exception e) {
						 logger.error("importConfig(taskId[{}], uploadFileName4Customer[{}] 解析文件出错 e-[{}])",
								 new Object[]{taskId, uploadFileName4Customer,e});
						 return "解析文件出错,任务定制失败!";
					}
					if(null==accountList || accountList.isEmpty()){
						return "文件内容为空,任务定制失败!";
					}else if(accountList.size()>5000){
						return "文件不可以大于5000条,任务定制失败!";
					}
					int addAccTempRes = dao.addAccTempRes(accountList, taskId);
					if(addAccTempRes!=1){
						logger.error("taskId[{}]-入临时表失败", taskId);
						int delStbDevSnTempRes = dao.deleteStbDevSnTemp(taskId);
						if(delStbDevSnTempRes!=1){
							logger.error("taskId[{}]-删除临时表表失败", taskId);
						}	
						return "任务定制失败!";
					}
					int addRestatDevicesRes = dao.addRestatDevices(taskId);
					if(addRestatDevicesRes!=1){
						logger.error("taskId[{}]-入stb_tab_restart_device表失败", taskId);
						int delStbDevSnTempRes = dao.deleteStbDevSnTemp(taskId);
						if(delStbDevSnTempRes!=1){
							logger.error("taskId[{}]-删除临时表表失败", taskId);
						}
						return "任务定制失败!";
					}
					int deleteStbDevSnTempRes = dao.deleteStbDevSnTemp(taskId);
					if(deleteStbDevSnTempRes!=1){
						logger.error("taskId[{}]-删除临时表表失败", taskId);
					}					
		    	}else{
		    		return "文件为空,任务定制失败!";
		    	}	
		    }    
			
		    long start_time = 0;
			try {
				start_time = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).parse(startTime).getTime()/1000;
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("批量导入重启，处理时间时出错");
				return "任务定制失败！";
			}
			int ier = dao.importConfig(taskId, cityId, taskName, acc_oid, taskTime, filePath, taskType, start_time);
			if (ier == 1)
			{
				return "任务定制成功!";
			}
			else
			{
				return "任务定制失败！";
			}
	}

	public String getFilePath()
	{
		// 获取系统的绝对路径
		String lipossHome = "";
		String a = FileUploadAction.class.getResource("/").getPath();
		try
		{
			lipossHome = java.net.URLDecoder.decode(
					a.substring(0, a.lastIndexOf("WEB-INF") - 1), "UTF-8");
		}
		catch (Exception e)
		{
			logger.error(e.getMessage());
			lipossHome = null;
		}
		logger.debug("{}待解析的文件路径", lipossHome);
		return lipossHome + "/";
	}
	
	
	public StbDeviceBatchRebootDAO getDao() {
		return dao;
	}

	public void setDao(StbDeviceBatchRebootDAO dao) {
		this.dao = dao;
	}

	/**
	 * 解析txt文件
	 * 
	 * @param excelFile
	 * @return
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public List<String> parseTxt(String excelFile,String path){
		logger.warn("start to parse file:[{}]", excelFile);
		// 业务账号合集
		List<String> serList = new ArrayList<String>();
		BufferedReader br = null;
		InputStream input=null;
		try {
			/*URL url = null;
			url = new URL(excelFile);
			if("https".equalsIgnoreCase(url.getProtocol())){
	            SslUtils.ignoreSsl();
	        }
			HttpURLConnection urlCon = (HttpURLConnection) url.openConnection();
			urlCon.setConnectTimeout(5*60*1000);
			urlCon.setReadTimeout(5*60*1000);
			urlCon.connect();
			
			logger.warn("获取连接" + urlCon);
			InputStream in = urlCon.getInputStream();*/
			
			path=getFilePath()+path;
			logger.warn("文件地址"+path);
			File file = new File(path);
			input = new FileInputStream(file);
			
			br = new BufferedReader(new InputStreamReader(input));
			String str = "";

			while (null != (str = br.readLine())) {
				if (!StringUtil.IsEmpty(str))
				{
					serList.add(str.trim());
				}
			}
			logger.warn("@@@serList="+serList);
			return serList;
		} catch (Exception e) {
			logger.warn("excel file:[{}] not exist, pass", excelFile);
			return null;
		}finally{
			try {
				if(input!=null){
					input.close();
					input=null;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			try {
				if(br!=null){
					br.close();
					br=null;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 解析excel文件
	 * 
	 * @param excelFile
	 * @return
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public List<String> parseExcel(String excelFile,String path) throws Exception {
		logger.warn("start to parse file:[{}]", excelFile);
		// 业务账号合集
		List<String> serList = new ArrayList<String>();
		try
		{
			/*URL url = null;
			url = new URL(excelFile);
			if("https".equalsIgnoreCase(url.getProtocol())){
	            SslUtils.ignoreSsl();
	        }
			HttpURLConnection urlCon  = (HttpURLConnection)url.openConnection();
			urlCon.setConnectTimeout(5*60*1000);
			urlCon.setReadTimeout(5*60*1000);
			urlCon.connect();
			logger.warn("获取连接"+urlCon);*/
			
			path=getFilePath()+path;
			logger.warn("文件地址"+path);
			File file = new File(path);
			InputStream input = new FileInputStream(file);
			//创建工作表
	    	HSSFWorkbook workbook = new HSSFWorkbook(input);
	    	//获得sheet
	    	HSSFSheet sheet = workbook.getSheetAt(0);
	    	//获得sheet的总行数
	    	int rowCount = sheet.getLastRowNum();
	    	//循环解析每一行，第一行不取
	    	for(int i=1;i<=rowCount;i++)
	    	{
	    		//获得行对象
	            HSSFRow row = sheet.getRow(i);
	            
	            HSSFCell cell= row.getCell(0);
	            String ser_account = CommonUtil.getCellString(cell);
	            if(null != ser_account)
	            {
	            	serList.add(ser_account.trim());
	            }
	    	}
		}
		catch(Exception e)
		{
			logger.error("parseExcel error: {}", e);
			return null;
		}
		logger.warn("@@@serList="+serList);
		return serList;
	}
	
	/**
	 * @author wangsenbo
	 * @param num_splitPage
	 * @param curPage_splitPage
	 * @date Nov 18, 2010
	 * @param
	 * @return List
	 */
	public List getStbBatchRebootTask(int curPage_splitPage, int num_splitPage, String taskNameQ) {
		return dao.getStbBatchRebootTask(curPage_splitPage, num_splitPage, taskNameQ);
	}

	public int countStbBatchRebootTask(int curPage_splitPage, int num_splitPage, String taskNameQ) {
		return dao.countStbBatchRebootTask(curPage_splitPage, num_splitPage, taskNameQ);
	}
	
	public List getStbBatchRestartDev(int curPage_splitPage, int num_splitPage, String taskIdQ) {
		return dao.getStbBatchRestartDev(curPage_splitPage, num_splitPage, taskIdQ);
	}

	public int countStbBatchRestartDev(int curPage_splitPage, int num_splitPage, String taskIdQ) {
		return dao.countStbBatchRestartDev(curPage_splitPage, num_splitPage, taskIdQ);
	}
	
	public int[] deleteTask(String taskIdD) {
		return dao.deleteTask(taskIdD);
	}
}
