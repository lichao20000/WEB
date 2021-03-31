
package com.linkage.module.gwms.resource.bio;

import com.linkage.commons.util.DateTimeUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.resource.dao.BatchHttpTestDAO;
import com.linkage.module.gwms.resource.utils.FileUtil;
import com.linkage.module.gwms.share.act.FileUploadAction;
import com.linkage.module.gwms.util.StringUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.List;
import java.util.Map;

public class BatchHttpTestBIO
{
	/** log */
	private static Logger logger = LoggerFactory.getLogger(BatchHttpTestBIO.class);
	private BatchHttpTestDAO dao;

	private String operType="";
	private int maxNum=0;
	/**
	 * @param dao
	 *            the dao to set
	 */
	public void setDao(BatchHttpTestDAO dao)
	{
		this.dao = dao;
	}

	/**
	 * @return the dao
	 */
	public BatchHttpTestDAO getDao()
	{
		return dao;
	}
	
	
	
	/**
	 * 获取文件路径
	 * 
	 * @return
	 */
	public String getFilePath() {
		//获取系统的绝对路径
		String lipossHome = "";
		String a = FileUploadAction.class.getResource("/").getPath();
		try{
			lipossHome = java.net.URLDecoder.decode(a.substring(0,a.lastIndexOf("WEB-INF")-1),"UTF-8");
		}catch(Exception e){
			logger.error(e.getMessage());
			lipossHome = null;
		}
		logger.debug("{}待解析的文件路径",lipossHome);
		return lipossHome + "/temp/";
	}
	
	
	/**
	 * 上传文件，拷贝文件到web目录下的temp/file目录下，命名new DateTimeUtil().getYYYYMMDDHHMMSS() +"_"+ fileName
	 * @param fileName 上传后缓存的文件名
	 * @return 拷贝后的文件的资源路径(http://开头)
	 */
	public String saveUpFile(String fileName){
		
	    logger.warn("saveUpFile(fileName={})", new Object[]{fileName});
		
		String filePath="";
		if (null != fileName) {
			//如果是导入帐号，将导入帐号的文件放到指定的目录底下
			 String targetDirectory="";
			 filePath = "/temp/file";
			 String targetFileName ="";
			 HttpServletRequest request = null;
			 FileInputStream in=null;
			 BufferedReader reader=null;
			 FileOutputStream os=null;
			 BufferedWriter writer=null;
			 try {
				 //将文件存放到指定的路径中
				 request = ServletActionContext.getRequest();
				 targetDirectory = ServletActionContext.getServletContext().getRealPath(filePath);
				 //由于传输的文件名中到后台有中文乱码所以存储到数据库更改文件名
		         targetFileName = new DateTimeUtil().getYYYYMMDDHHMMSS() +"_"+ fileName;
				 File src = new File(getFilePath() + fileName);
		         if(Global.JLLT.equals(Global.instAreaShortName)
						 && !"".equals(operType))
		         {
		         	 //JLLT-REQ-RMS-20200609-JH001【吉林联通RMS测速导入数量控制需求】
					 // 复制文件前N行，N可配置。
					 if(fileName.endsWith(".txt"))
					 {
						 File target = new File(getFilePath()+"file/" + targetFileName);
						 if(!target.exists()){
							 target.createNewFile();
						 }
						 int row = 0;
						 StringBuffer stringBuffer=new StringBuffer();
						 in = new FileInputStream(src);
						 reader = new BufferedReader(new InputStreamReader(in,"UTF-8"));
						 String line = reader.readLine();
						 stringBuffer.append(line+"\r\n");
						 while ((line = reader.readLine()) != null) {
							 row ++;
							 if(!StringUtil.IsEmpty(line)){
								 stringBuffer.append(line+"\r\n");
							 }
							 if(row == maxNum)
							 {
								 break;
							 }
						 }
						 in.close();
						 in=null;
						 reader.close();
						 reader=null;
						 
						 os = new FileOutputStream(target,true);
						 writer = new BufferedWriter(new OutputStreamWriter(os,"UTF-8"));
						 writer.write(stringBuffer.toString());
						 writer.close();
						 writer=null;
						 //writer.flush();
					 }
					 else
					 {
						 FileUtil fileUtil = new FileUtil(maxNum);
						 fileUtil.getDatasInSheet(0,src,getFilePath()+"file/"  + targetFileName);
					 }
				 }
		         else
				 {
					 File target = new File(getFilePath()+"file/" + targetFileName);
					 if(!target.exists()){
						 target.createNewFile();
					 }
					 FileUtils.copyFile(src, target);
				 }
			} catch (IOException e) {
				logger.error("批量导入升级，上传文件时出错:{}",ExceptionUtils.getStackTrace(e));
			}finally{
				try {
					 if(in!=null){
						 in.close();
						 in=null;
					 }
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				 try {
					 if(reader!=null){
						 reader.close();
						 reader=null;
					 }
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				 
				 try {
					 if(writer!=null){
						 writer.close();
						 writer=null;
					 }
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				 
				 try {
					 if(os!=null){
						 os.close();
						 os=null;
					 }
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			 
			if (Global.JLLT.equals(Global.instAreaShortName) || 
					Global.JXLT.equals(Global.instAreaShortName) ||
					Global.ZJLT.equals(Global.instAreaShortName) ||
					Global.AHLT.equals(Global.instAreaShortName))
			{
				filePath = "https://" + request.getLocalAddr() + ":"
						+ request.getLocalPort() + request.getContextPath() + filePath
						+ "/" + targetFileName;
			}
			else
			{
				filePath = "http://" + request.getLocalAddr() + ":"
						+ request.getLocalPort() + request.getContextPath() + filePath
						+ "/" + targetFileName;
			}
		}
		logger.warn("filePath = "+ filePath);
		return filePath;
	}
	
	


	/**
	 * 生成任务表(插入sql) 1.数量小于100的设备ids，直接入任务表和明细表，2.数量大于100的入sql(任务表) 3.导入文件的一律入文件名(任务表)
	 * @param accoid 操作员id
	 * @param report_url 上传url
	 * @param http_url 测速url
	 * @param sql 查询sql
	 * @param deviceId  device_id
	 * @param fileName 文件名
	 * @param param0 参数
	 * @param task_desc 描述
	 */
	public void createHttpTaskSQL(String http_url, String report_url, long accoid, String sql, String[] deviceId, String fileName, String param0, String task_desc,String type)
	{
		
		Object[] param= null;
		//河北联通增加了type字段，用于区分上下行测速
		if (Global.HBLT.equals(Global.instAreaShortName)) {
			param= new Object[18];
		}else {
			param= new Object[17];
		}
		Long task_id  = dao.getMaxTaskID() + 1;
		Long time = new DateTimeUtil().getLongTime();
		String task_name = StringUtil.getStringValue(accoid) + StringUtil.getStringValue(time) + StringUtil.getStringValue((int)(Math.random()*999));
		int task_status = 0;//未执行
		param[0] = task_name;
		param[1] = task_id;
		logger.warn("task_id = "+task_id+",dao.getMaxTaskID()="+dao.getMaxTaskID());
		param[2] = accoid;
		param[3] = time;
		param[4] = task_status;
		param[5] = http_url;
		param[6] = report_url;
		param[7] = sql;
		param[8] = fileName;
		param[16] = task_desc;
		
		if (Global.HBLT.equals(Global.instAreaShortName)) {
			param[17] = type;
		}
		
		if(null!=param0){
			String[] _param = param0.split("\\|");

			param[9] = _param[2].trim();
			param[10] = _param[4].trim();
			param[11] = _param[5].trim();
			param[12] = _param[6].trim();
			param[13] = _param[7].trim();
			param[14] = _param[8].trim();
			param[15] = _param[9].trim();
		}
		
		//入任务表，多个设备也只入一条
		int res = dao.createHttpTaskSQL(param, param0);
		if(res!=1){
			logger.error("插入批量升级任务表执行失败,param={}", param);
		}
		
		//直接传设备id（非文件非sql方式）入任务表需直接入明细表，并将任务表的状态置为完成。
		if(null == fileName && null == sql && res == 1 ){
			String[] task_dev_sql = new String[deviceId.length];
			Long task_id_dev  = task_id;
			Object[] param_dev= new Object[8];
			param_dev[0] = task_id_dev;
			param_dev[4] = 0;
			param_dev[5] = new DateTimeUtil().getLongTime();
			int res_devs[];
			for(int i = 0;i<deviceId.length;i++){
				param_dev[1] = deviceId[i];
				Map map = dao.getOuiSerial((String)param_dev[1]);
				
				param_dev[2] = map.get("oui");
				param_dev[3] = map.get("device_serialnumber");
				param_dev[6] = map.get("city_id");
				param_dev[7] = map.get("wan_type");
				task_dev_sql[i] = dao.createHttpTask_devSQL(param_dev);
			}
			
			//更新任务表状态
			logger.warn("task_dev_sql.length=" + task_dev_sql.length);
			res_devs = dao.batchUpdate(task_dev_sql);
			//dao.updateHttpTask(task_id);
			
			logger.debug("插入批量升级任务明细表执行完成，task_id={}", task_id);
		}
	}

	/**
	 * 生成任务表(插入sql) 1.数量小于100的设备ids，直接入任务表和明细表，2.数量大于100的入sql(任务表) 3.导入文件的一律入文件名(任务表)
	 * @param accoid 操作员id
	 * @param report_url 上传url
	 * @param http_url 测速url
	 * @param sql 查询sql
	 * @param deviceId  device_id
	 * @param fileName 文件名
	 * @param param0 参数
	 * @param task_desc 描述
	 */
	public void createHttpTaskSQLAHLT(String http_url, String report_url, long accoid, String sql, String[] deviceId, String fileName, String param0, String task_desc,String type,String task_name,String speed_frequency,String startDate,String endDate)
	{

		Object[] param= null;

		param= new Object[20];

		Long task_id  = dao.getMaxTaskID() + 1;
		Long time = new DateTimeUtil().getLongTime();
		int task_status = 0;//未执行
		param[0] = task_name;
		param[1] = task_id;
		logger.warn("task_id = "+task_id+",dao.getMaxTaskID()="+dao.getMaxTaskID());
		param[2] = accoid;
		param[3] = time;
		param[4] = task_status;
		param[5] = http_url;
		param[6] = report_url;
		param[7] = sql;
		param[8] = fileName;
		param[16] = task_desc;
		param[17] = Long.parseLong(speed_frequency) ;
		param[18] = new DateTimeUtil(startDate).getLongTime() ;
		param[19] = new DateTimeUtil(endDate).getLongTime();

		if(null!=param0){
			String[] _param = param0.split("\\|");

			param[9] = _param[2].trim();
			param[10] = _param[4].trim();
			param[11] = _param[5].trim();
			param[12] = _param[6].trim();
			param[13] = _param[7].trim();
			param[14] = _param[8].trim();
			param[15] = _param[9].trim();
		}

		//入任务表，多个设备也只入一条
		int res = dao.createHttpTaskSQLAHLT(param, param0);
		if(res!=1){
			logger.error("插入批量升级任务表执行失败,param={}", param);
		}

		//直接传设备id（非文件非sql方式）入任务表需直接入明细表，并将任务表的状态置为完成。
		if(null == fileName && null == sql && res == 1 ){
			String[] task_dev_sql = new String[deviceId.length];
			Long task_id_dev  = task_id;
			Object[] param_dev= new Object[8];
			param_dev[0] = task_id_dev;
			param_dev[4] = 0;
			param_dev[5] = new DateTimeUtil().getLongTime();
			int res_devs[];
			for(int i = 0;i<deviceId.length;i++){
				param_dev[1] = deviceId[i];
				Map map = dao.getOuiSerial((String)param_dev[1]);

				param_dev[2] = map.get("oui");
				param_dev[3] = map.get("device_serialnumber");
				param_dev[6] = map.get("city_id");
				param_dev[7] = map.get("wan_type");
				task_dev_sql[i] = dao.createHttpTask_devSQL(param_dev);
			}

			//更新任务表状态
			logger.warn("task_dev_sql.length=" + task_dev_sql.length);
			res_devs = dao.batchUpdate(task_dev_sql);
			//dao.updateHttpTask(task_id);

			logger.debug("插入批量升级任务明细表执行完成，task_id={}", task_id);
		}
	}

	/**
	 * 根据用户acc_oid获取已经创建的任务数
	 * @param acc_oid
	 * @return
	 */
	public int countTasks(Long acc_oid)
	{
		Map taskes = dao.countTasks(acc_oid);
		return Integer.valueOf(taskes.get("taskes").toString());
	}

	public List getTaskByTime(Long starttimeTemp, Long endtimeTemp) {
		return dao.getTaskByTime(starttimeTemp,endtimeTemp);
	}


	public String getOperType() {
		return operType;
	}

	public void setOperType(String operType) {
		this.operType = operType;
	}

	public int getMaxNum() {
		return maxNum;
	}

	public void setMaxNum(int maxNum) {
		this.maxNum = maxNum;
	}
}
