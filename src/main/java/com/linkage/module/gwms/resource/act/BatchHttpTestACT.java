
package com.linkage.module.gwms.resource.act;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.LipossGlobals;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.resource.bio.BatchHttpTestBIO;
import com.linkage.module.gwms.resource.utils.DateFilterTest;
import com.linkage.module.gwms.share.act.FileUploadAction;
import com.linkage.module.gwms.share.bio.GwDeviceQueryBIO;
import com.linkage.module.gwms.util.StringUtil;
import com.opensymphony.xwork2.ActionSupport;

/**
 * 批量Http业务质量下载测试
 * 
 * @author fanjm
 */
public class BatchHttpTestACT extends ActionSupport implements SessionAware
{

	/** log */
	private static Logger logger = LoggerFactory.getLogger(BatchHttpTestACT.class);
	private String deviceIds;
	private String ajax;
	private BatchHttpTestBIO bio;
	// session
	private Map session;
	private String softStrategyHTML;
	private String goal_devicetype_id;
	private String softStrategy_type;
	private String param;
	
	private String strRename;
	private String urlParameter;
	private File file1;
	private String response;
	private String path;
	private String gw_type;
	private GwDeviceQueryBIO gwDeviceQueryBio;
	private String fileName;
	private String importQueryField;
	private String gwShare_queryType;
	private String starttime;
	private String endtime;
	
	private String taskId;
	private List taskList ; 
	private String http_url = LipossGlobals.getLipossProperty("testSpeedDownUrl"); 
	private String report_url  = LipossGlobals.getLipossProperty("testSpeedReportUrl"); 
	private String fileName_st ;
	private String gwShare_fileName = null;
	private String task_desc;
	private String type;
	private String instArea=Global.instAreaShortName;

	// 公共界面 增加operType
	// 目前 吉林联通 批量测速 限制一次的批量导入数量  operType : 1
	// 可拓展 若拓展 增加注释维护对应关系，避免重复。
	private String operType="";
	/**
	 * 测速任务超过三个标识
	 */
	private int taskNumFalg=0;
	/**
	 * 局点名称
	 */
	private String instAreaName;
	/**
	 * 任务名称
	 */
	private String task_name;
	private String speed_frequency;
	private String startDate;
	private String endDate;
	//开始时间段
	private String BEGIN_TIME;
	//结束时间段
	private String END_TIME;
	//总次数
	private String TOTAL_TIMES;
	//最小触发周期
	private String PERIOD;

	public String init()
	{
		return "success";
	}
	
	
	
	/**
	 * 批量测速
	 * 
	 * @author fanjm
	 * @date 2017-6-20
	 * @return String
	 */
	public String speedTest()
	{
		logger.debug("speedTest begin()");
		try
		{
			UserRes curUser = (UserRes) session.get("curUser");
			if (true == StringUtil.IsEmpty(deviceIds))
			{
				logger.debug("任务中没有设备");
			}
			long accoid = curUser.getUser().getId();
			
			/**
			 *  河北联通增加测速用户创建测速任务限制（非admin用户不得超过三个）
			 */
			instAreaName = Global.instAreaShortName;
			if (Global.HBLT.equals(instAreaName)
					&& accoid != 1)
			{
				logger.warn("河北测速任务数量校验开始");
				int tasknum = bio.countTasks(accoid);
				if (tasknum >= 3)
				{
					logger.warn("用户为非admin用户，且测速任务已超过三个，不允许继续创建任务！");
					taskNumFalg=1;
					return "result";
				}
				logger.warn("用户为非admin用户，测速任务不超过三个，允许继续创建任务！");
			}
			insertTask(param, curUser.getAreaId(), accoid, gw_type, task_desc);
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
			logger.warn("Exception="+e.getMessage());
			return "result";
		}
		return "result";
	}


	/**
	 * 批量测速
	 *
	 * @author fanjm
	 * @date 2017-6-20
	 * @return String
	 */
	public String speedTestAHLT()
	{
		logger.debug("speedTestAHLT begin()");
		try
		{
			UserRes curUser = (UserRes) session.get("curUser");
			if (true == StringUtil.IsEmpty(deviceIds))
			{
				logger.debug("任务中没有设备");
			}
			long accoid = curUser.getUser().getId();

			insertTaskAHLT(param, curUser.getAreaId(), accoid,task_name,speed_frequency,startDate,endDate,gw_type, task_desc);

		}
		catch (Exception e)
		{
			e.printStackTrace();
			logger.warn("Exception="+e.getMessage());
			return "result";
		}
		return "result";
	}
	
	/**
	 * 浙江联通
	 * @return
	 */
	public String speedTestZJLT()
	{
		logger.debug("speedTestZJLT begin()");
		try
		{
			UserRes curUser = (UserRes) session.get("curUser");
			if (true == StringUtil.IsEmpty(deviceIds))
			{
				logger.debug("任务中没有设备");
			}
			long accoid = curUser.getUser().getId();

			insertTaskZJLT(param, curUser.getAreaId(), accoid,task_name,gw_type, task_desc);

		}
		catch (Exception e)
		{
			e.printStackTrace();
			logger.warn("Exception="+e.getMessage());
			return "result";
		}
		return "result";
	}
	
	/**
	 * 上传文件ajax
	 * @return "ajax"
	 */
	public String saveUpFile(){
		if(Global.JLLT.equals(instArea) && !"".equals(operType)){
			String importFileMaxNum = LipossGlobals.getLipossProperty("importFileMaxNum");
			if(!"".equals(importFileMaxNum))
			{
				bio.setOperType(operType);
				bio.setMaxNum(StringUtil.getIntegerValue(importFileMaxNum));
			}
		}
		ajax = bio.saveUpFile(gwShare_fileName);
		return "ajax";
	}
	/**
	 * 插入任务表 文件名不为空则来源为导入，deviceIds不为0来源为直接radio查询，deviceIds也为空则来源为高级查询。
	 * @param param  参数
	 * @param areaId 操作员域id
	 * @param accoid 操作员acc
	 * @param gw_type 系统类型
	 * @param task_desc 任务描述
	 */
	public void insertTask(String param,long areaId,long accoid, String gw_type, String task_desc)
	{
		logger.warn("insertTask({},{})开始", new Object[] {param,gw_type});
		try
		{
			//入任务表-文件查询
			if(!StringUtil.IsEmpty(fileName_st)){
				logger.warn("入任务表-文件名不为空则来源为导入");
				bio.createHttpTaskSQL(http_url, report_url, accoid, null, null, fileName_st, null, task_desc,type);
			}
			//入任务表-设备id列表(来源为直接radio查询,简单查询)
			else if(!"0".equals(deviceIds))
			{
				logger.warn("入任务表-设备id列表(来源为直接radio查询,简单查询)");
				String[] deviceId_array = deviceIds.split(",");
				bio.createHttpTaskSQL(http_url, report_url, accoid, null, deviceId_array, null, null, task_desc,type);
			}
			//入任务表-设备id列表/sql(高级查询)
			else 
			{
				logger.warn("入任务表-设备id列表/sql(高级查询)");
				if(StringUtil.IsEmpty(param))
				{
					logger.warn("param为空");
				}
				String[] _param = param.split("\\|");
				String matchSQL = _param[10];
				long total = StringUtil.getLongValue(_param[11]);
				if(total < 100)
				{
					logger.warn("数量小于100，传deviceID数组");
					List list = gwDeviceQueryBio.getDeviceList(gw_type,areaId, param);
					String[] deviceId_array = new String[list.size()];
					for (int i = 0; i < list.size(); i++)
					{
						Map map = (Map) list.get(i);
						String device_id = StringUtil.getStringValue(map.get("device_id"));
						deviceId_array[0] = device_id;
					}
					bio.createHttpTaskSQL(http_url, report_url, accoid, null, deviceId_array ,null, null, task_desc,type);
				}
				else
				{
					logger.warn("数量大于100，将sql入任务表");
					bio.createHttpTaskSQL(http_url, report_url, accoid, matchSQL.replace("[", "\'"), null ,null, param, task_desc,type);
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			logger.warn("Exception="+e.getMessage());
		}
		
	}


	/**
	 * 插入任务表 文件名不为空则来源为导入，deviceIds不为0来源为直接radio查询，deviceIds也为空则来源为高级查询。
	 * @param param  参数
	 * @param areaId 操作员域id
	 * @param accoid 操作员acc
	 * @param gw_type 系统类型
	 * @param task_desc 任务描述
	 */
	public void insertTaskAHLT(String param,long areaId,long accoid,String task_name,String speed_frequency,String startDate,String endDate, String gw_type, String task_desc)
	{
		logger.warn("insertTaskAHLT({},{})开始", new Object[] {param,gw_type});
		try
		{
			//入任务表-文件查询
			if(!StringUtil.IsEmpty(fileName_st)){
				logger.warn("入任务表-文件名不为空则来源为导入");
				bio.createHttpTaskSQLAHLT(http_url, report_url, accoid, null, null, fileName_st, null, task_desc,type, task_name, speed_frequency, startDate, endDate);
			}
			//入任务表-设备id列表(来源为直接radio查询,简单查询)
			else if(!"0".equals(deviceIds))
			{
				logger.warn("入任务表-设备id列表(来源为直接radio查询,简单查询)");
				String[] deviceId_array = deviceIds.split(",");
				bio.createHttpTaskSQLAHLT(http_url, report_url, accoid, null, deviceId_array, null, null, task_desc,type, task_name, speed_frequency, startDate, endDate);
			}
			//入任务表-设备id列表/sql(高级查询)
			else
			{
				logger.warn("入任务表-设备id列表/sql(高级查询)");
				if(StringUtil.IsEmpty(param))
				{
					logger.warn("param为空");
				}
				String[] _param = param.split("\\|");
				String matchSQL = _param[10];
				long total = StringUtil.getLongValue(_param[11]);
				if(task_desc != null && !"".equals(task_desc)){
					total = Long.parseLong(task_desc);
				}
				if(total < 100)
				{
					logger.warn("数量小于100，传deviceID数组");
					List list = gwDeviceQueryBio.getDeviceList(gw_type,areaId, param);
					String[] deviceId_array = new String[(int) total];
					for (int i = 0; i < total; i++)
					{
						Map map = (Map) list.get(i);
						String device_id = StringUtil.getStringValue(map.get("device_id"));
						deviceId_array[0] = device_id;
					}
					bio.createHttpTaskSQLAHLT(http_url, report_url, accoid, null, deviceId_array ,null, null, task_desc,type, task_name, speed_frequency, startDate, endDate);
				}
				else
				{
					logger.warn("数量大于100，将sql入任务表");
					matchSQL = "select * from ("+(matchSQL.replace("[", "\'"))+") where rownum <= "+total;
					bio.createHttpTaskSQLAHLT(http_url, report_url, accoid, matchSQL, null ,null, param, task_desc,type, task_name, speed_frequency, startDate, endDate);
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			logger.warn("Exception="+e.getMessage());
		}

	}
	
	
	
	/**
	 * 插入任务表 文件名不为空则来源为导入，deviceIds不为0来源为直接radio查询，deviceIds也为空则来源为高级查询。
	 * @param param  参数
	 * @param areaId 操作员域id
	 * @param accoid 操作员acc
	 * @param gw_type 系统类型
	 * @param task_desc 任务描述
	 */
	public void insertTaskZJLT(String param,long areaId,long accoid,String task_name,String gw_type, String task_desc)
	{
		logger.warn("insertTask({},{},{},{},{},{})开始", new Object[] {fileName_st, task_desc,BEGIN_TIME,END_TIME,TOTAL_TIMES,PERIOD});
		try
		{
			//入任务表-文件查询
			if(!StringUtil.IsEmpty(fileName_st)){
				logger.warn("入任务表-文件名不为空则来源为导入");
				bio.createHttpTaskSQL(http_url, report_url, accoid, null, null, fileName_st, null, task_desc,BEGIN_TIME,END_TIME,TOTAL_TIMES,PERIOD);
			}
			//入任务表-设备id列表(来源为直接radio查询,简单查询)
			else if(!"0".equals(deviceIds))
			{
				logger.warn("入任务表-设备id列表(来源为直接radio查询,简单查询)");
				String[] deviceId_array = deviceIds.split(",");
				bio.createHttpTaskSQL(http_url, report_url, accoid, null, deviceId_array, null, null, task_desc,BEGIN_TIME,END_TIME,TOTAL_TIMES,PERIOD);
			}
			//入任务表-设备id列表/sql(高级查询)
			else 
			{
				logger.warn("入任务表-设备id列表/sql(高级查询)");
				if(StringUtil.IsEmpty(param))
				{
					logger.warn("param为空");
				}
				String[] _param = param.split("\\|");
				String matchSQL = _param[10];
				long total = StringUtil.getLongValue(_param[11]);
				if(total < 100)
				{
					logger.warn("数量小于100，传deviceID数组");
					List list = gwDeviceQueryBio.getDeviceList(gw_type,areaId, param);
					String[] deviceId_array = new String[list.size()];
					for (int i = 0; i < list.size(); i++)
					{
						Map map = (Map) list.get(i);
						String device_id = StringUtil.getStringValue(map.get("device_id"));
						deviceId_array[0] = device_id;
					}
					bio.createHttpTaskSQL(http_url, report_url, accoid, null, deviceId_array ,null, null, task_desc,BEGIN_TIME,END_TIME,TOTAL_TIMES,PERIOD);
				}
				else
				{
					logger.warn("数量大于100，将sql入任务表");
					bio.createHttpTaskSQL(http_url, report_url, accoid, matchSQL.replace("[", "\'"), null ,null, param, task_desc,BEGIN_TIME,END_TIME,TOTAL_TIMES,PERIOD);
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			logger.warn("Exception="+e.getMessage());
		}

	}

	/**
	 * 校验时间重叠
	 *
	 * @return
	 */
	public String checkTime() throws ParseException {
		int res = 0;
		Map<String, Long> timeTempMap = DateFilterTest.hasTime(startDate);
		List<Long> starttimes = new ArrayList<Long>();
		List<Long> endtimes = new ArrayList<Long>();
		Long starttimeTemp = timeTempMap.get("starttimeTemp");
		Long endtimeTemp = timeTempMap.get("endtimeTemp");
		List<Map<String,String>> taskList = bio.getTaskByTime(starttimeTemp,endtimeTemp);
		logger.warn("taskSize:{}",taskList.size());
		for (int i = 0; i < taskList.size() ; i++) {
			starttimes.add(Long.parseLong(taskList.get(i).get("startdate")));
			endtimes.add(Long.parseLong(taskList.get(i).get("enddate")));
		}
		boolean fa = DateFilterTest.checkTime(starttimes,endtimes,DateFilterTest.dateToStamp(startDate),DateFilterTest.dateToStamp(endDate));
		if(fa){
			res = 1;
		}
		ajax = res+"";
		return "ajax";
	}
	
	/**
	 * 解析文件（xls格式）
	 * 
	 * @param fileName 文件名
	 * @return
	 * @throws IOException 
	 * @throws BiffException 
	 * @throws  
	 * 		   
	 */
	public List<String> getImportDataByXLS(String fileName) throws BiffException, IOException{
		logger.debug("getImportDataByXLS{}",fileName);
		List<String> list = new ArrayList<String>();
		File f = new File(getFilePath()+fileName);
		Workbook wwb = Workbook.getWorkbook(f);
		Sheet ws = null;
		//总sheet数
		//int sheetNumber = wwb.getNumberOfSheets();
		int sheetNumber = 1;
		for (int m=0;m<sheetNumber;m++){
			ws = wwb.getSheet(m);
			
			//当前页总记录行数和列数
			int rowCount = ws.getRows();
			//int columeCount = ws.getColumns();
			
			if(null!=ws.getCell(0,0).getContents()){
				String line = ws.getCell(0,0).getContents().trim();
				/**
				 * 判断导入文件查询的条件，默认为用户账号，当匹配到设备序列号时，
				 * 则以设备序列号作为条件，否则一律以用户账号作为条件
				 */
				if(null!=line && "设备序列号".equals(line)){
					this.importQueryField = "device_serialnumber";
				}else{
					this.importQueryField = "username";
				}
			}
			
			//取当前页所有值放入list中
			for (int i=1;i<rowCount;i++){
				String temp = ws.getCell(0, i).getContents();
				if(null!=temp && !"".equals(temp)){
					if(!"".equals(ws.getCell(0, i).getContents().trim())){
						list.add(ws.getCell(0, i).getContents().trim());
					}
				}
			}
		}
		f.delete();
		f = null;
		return list;
	}
	
	
	/**
	 * 解析文件（txt格式）
	 * 
	 * @param fileName 文件名
	 * @return
	 * @throws FileNotFoundException 
	 * 		   IOException
	 */
	public List<String> getImportDataByTXT(String fileName) throws FileNotFoundException,IOException{
		logger.debug("getImportDataByTXT{}",fileName);
		List<String> list = new ArrayList<String>();
		BufferedReader in = new BufferedReader(new FileReader(getFilePath()+fileName));
		String line = in.readLine();
		/**
		 * 判断导入文件查询的条件，默认为用户账号，当匹配到设备序列号时，
		 * 则以设备序列号作为条件，否则一律以用户账号作为条件
		 */
		if(null!=line && "设备序列号".equals(line)){
			this.importQueryField = "device_serialnumber";
		}else{
			this.importQueryField = "username";
		}
		//从第二行开始读取数据，并对每行数据去掉首尾空格
		while ((line = in.readLine()) != null) {
			if(!"".equals(line.trim())){
				list.add(line.trim());
			}
		}
		in.close();
		in = null;
		File f = new File(getFilePath()+fileName);
		f.delete();
		return list;
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
//	public String checkUploadLocalFile()
//	{
//	    Map fields = UploadDAO.getSoftFile(strRename);
//
//	    int icode = Integer.parseInt((String) fields.get("num"));
//	    if (icode > 0) {
//	        ajax = "ERROR:文件名重复，请重新上传！";
//	        fields = null;
//	        icode = 0;
//	    }else
//	    {
//	        ajax = "OK";
//	    }
//	    
//        return "ajax";
//	}
	
	/**
	 * <p>
	 * [文件上传到WEB]
	 * </p>
	 * @return
	 */
	public String uploadLocalFile()
	{
//        logger.error("开始!");
	    int index =  urlParameter.indexOf("path");
	   
        String strPath = urlParameter.substring(index + 5,urlParameter.indexOf("&", index));

        //目录
        File localPath = new File(LipossGlobals.G_ServerHome + "/" + strPath);
        if(!localPath.exists())
        {
            if(!localPath.mkdirs())
            {
                logger.error("缓存文件目录:{}创建失败！",LipossGlobals.G_ServerHome + "/" + strPath);
                this.response = "缓存文件目录创建失败！";
                return "response";
            }
        }
        
//        logger.error("-----------------------" + LipossGlobals.G_ServerHome + "/" + strPath);
        
        //缓存到本地
        File localFile = new File(LipossGlobals.G_ServerHome + "/" + strPath + "/" + file1.getName());
        if(localFile.exists())
        {
            if(!localFile.delete())
            {
                logger.error("旧缓存文件:{}删除失败！",LipossGlobals.G_ServerHome + "/" + strPath + "/" + file1.getName());
                this.response = "旧缓存文件删除失败！";
                return "response";
            }
        }
        
//        logger.error("-----------------------" + LipossGlobals.G_ServerHome + "/" + strPath + "/" + file1.getName());
        
        FileInputStream fis = null;
        FileOutputStream fos = null;
        try
        {
            fis = new FileInputStream(file1);
            fos = new FileOutputStream(localFile);
            
//            byte[] tmps = new byte[1024];
            
            int ch = fis.read();
            while(ch != -1)
            {
                fos.write((char)ch);
                fos.flush();
                ch = fis.read();
            }
            fos.flush();
        }
        catch (FileNotFoundException e1)
        {
            //本地文件不存在
            logger.error("本地文件不存在！",e1);
            this.response = "本地文件不存在！";
            return "response";
        }
        catch (IOException e)
        {
            //读取文件失败
            logger.error("读取本地文件失败！",e);
            this.response = "读取本地文件失败！";
            return "response";
        }finally
        {
        	try {
        		if(fos!=null){
        			fos.close();
        			fos=null;
        		}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            try {
            	if(fis!=null){
            		fis.close();
            		fis=null;
            	}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }

        logger.info("WEB文件{}写入成功",localFile.getAbsolutePath());
        
        	
    	//转发文件服务器
        try  
        {   
        	String url = urlParameter.substring(0,urlParameter.indexOf("doUpload.jsp"));
            String[] urls = url.split(";");
            String urlLeft = urlParameter.substring(urlParameter.indexOf("doUpload.jsp"));
            
            StringBuffer response = new StringBuffer();   
            
            HttpClient client = new HttpClient();
            PostMethod method = new PostMethod(urls[0]+(urls.length==1?"":"/")+urlLeft);
            method.setRequestHeader("Content-type", "multipart/form-data");
//            logger.error("urlParameter:{}",urlParameter);
      
            //设置Http Post数据，这里是上传文件   
//            File f= new File(LipossGlobals.G_ServerHome + "/" + strPath + "/" + localFile.getName());
//            FileInputStream fi=new FileInputStream(f);
//            FileInputStream fi=new FileInputStream(localFile);
//            
//            InputStreamRequestEntity fr=new InputStreamRequestEntity(fi);   
//            method.setRequestEntity((RequestEntity)fr);
//            NameValuePair[] data = { new NameValuePair("file1", localFile.getAbsolutePath())};
//            method.setRequestBody(fi);

//            StringPart sp  =   new  StringPart( " TEXT " ,  " testValue " ,  " GBK " );
            
            FilePart fp  =   new  FilePart( " file " ,  localFile.getName() ,  localFile,  null ,  " GBK " );

            method.getParams().setContentCharset( " GBK " );
             MultipartRequestEntity mrp =   new  MultipartRequestEntity( new  Part[]  { fp} , method
                    .getParams());
            method.setRequestEntity(mrp);

            //第一个通过web服务上传
            BufferedReader reader=null;
            try  
            {
                client.executeMethod(method); //这一步就把文件上传了   
                //下面是读取网站的返回网页，例如上传成功之类的
                int statusCode = method.getStatusCode();
                if (statusCode == HttpStatus.SC_OK)   
                {
                    //读取为 InputStream，在网页内容数据量大时候推荐使用     
                    reader = new BufferedReader(     
                            new InputStreamReader(method.getResponseBodyAsStream(),     
                                    "GBK"));     
                    String line;     
                    while ((line = reader.readLine()) != null)   
                    {
                            response.append(line);
                    }
                    String str = response.toString();
                    int ind =  str.indexOf("idMsg");
                    this.response = str.substring(ind + 7,str.indexOf("</SPAN>", ind));
                }else
                {
                    this.response = urls[0]+"传输文件时异常";
                    logger.error("传输出现异常:{}!","传输文件时异常");
                }
            }   
            catch (IOException e)   
            {     
//                System.out.println("执行HTTP Post请求" + urlParameter + "时，发生异常！");     
//                e.printStackTrace();     
                logger.error("传输文件发生异常:{}", e);
                this.response = "执行HTTP Post请求时异常";
            }   
            finally  
            { 
            	try{
            		if(reader!=null){
            			reader.close();
            		}
            	}catch(IOException e){}
                method.releaseConnection();
            }
            
            //其他采用ftp上传
            String newName = urlParameter.substring(urlParameter.indexOf("=", urlParameter.indexOf("fileRename"))+1, urlParameter.indexOf("&", urlParameter.indexOf("fileRename")));
            String seruser = urlParameter.substring(urlParameter.indexOf("=", urlParameter.indexOf("seruser"))+1, urlParameter.indexOf("&", urlParameter.indexOf("seruser")));
            String serpass = urlParameter.substring(urlParameter.indexOf("=", urlParameter.indexOf("serpass"))+1, urlParameter.indexOf("&", urlParameter.indexOf("serpass")));
            for(int i=1;i<urls.length;i++){
            	FTPClient ftp = new FTPClient();
            	int portIndex = urls[i].lastIndexOf(":");
            	String tmpUrl = urls[i].substring(urls[i].indexOf("://")+3, portIndex);
            	//String prot = urls[i].substring(portIndex+1,urls[i].indexOf("/", portIndex));
        		logger.info(tmpUrl);
            	try
        		{
        			int reply;
        			String path = LipossGlobals.getLipossProperty("ftp.ftpDir");
        			int port = StringUtil.getIntegerValue(LipossGlobals
        					.getLipossProperty("ftp.port"));
        			
        			File ftpLocalPath = new File(path);
        			if (!ftpLocalPath.exists())
        			{
        				ftpLocalPath.mkdir();
        			}
        			ftp.connect(tmpUrl, port);// 连接FTP服务器
        			// 如果采用默认端口，可以使用ftp.connect(url)的方式直接连接FTP服务器
        			ftp.login(seruser, serpass);// 登录
        			ftp.setBufferSize(100000);
        			reply = ftp.getReplyCode();
        			if (!FTPReply.isPositiveCompletion(reply))
        			{
        				ftp.disconnect();
        			}
        			ftp.changeWorkingDirectory(path);
        			FileInputStream in = new FileInputStream(localFile);
        			ftp.storeFile(newName, in);
        			in.close();
        			ftp.logout();
        			this.response = this.response + "\n"+urls[i]+"1#FTP文件上传成功!";
        		}
        		catch (Exception e)
        		{
        			this.response = this.response + "\n"+urls[i]+"-1#FTP文件上传失败!";
        		}
            }
         }
         catch (Exception e)   
         {
             logger.error("传输文件发生异常:{}", e);
             this.response = "传输文件发生异常";
         }finally
         {
           //删除缓存文件
             if(localFile.exists())
             {
                 if(localFile.delete())
                 {
                     logger.info("WEB文件{}删除成功",localFile.getAbsolutePath());
                 }else
                 {
                     logger.error("WEB文件{}删除失败",localFile.getAbsolutePath());
                 }
             }
             if(file1.exists())
             {
                 if(file1.delete())
                 {
                     logger.info("WEB文件{}删除成功",file1.getAbsolutePath());
                 }else
                 {
                     logger.error("WEB文件{}删除失败",file1.getAbsolutePath());
                 }
             }
             localFile = null;
             file1 = null;
         }
        
        return "response";
	}




	/**
	 * @return the deviceIds
	 */
	public String getDeviceIds()
	{
		return deviceIds;
	}

	/**
	 * @param deviceIds
	 *            the deviceIds to set
	 */
	public void setDeviceIds(String deviceIds)
	{
		this.deviceIds = deviceIds;
	}

	/**
	 * @return the ajax
	 */
	public String getAjax()
	{
		return ajax;
	}

	/**
	 * @param ajax
	 *            the ajax to set
	 */
	public void setAjax(String ajax)
	{
		this.ajax = ajax;
	}

	/**
	 * @return the bio
	 */
	public BatchHttpTestBIO getBio()
	{
		return bio;
	}

	/**
	 * @param bio
	 *            the bio to set
	 */
	public void setBio(BatchHttpTestBIO bio)
	{
		this.bio = bio;
	}

	/**
	 * @return the session
	 */
	public Map getSession()
	{
		return session;
	}

	/**
	 * @param session
	 *            the session to set
	 */
	public void setSession(Map session)
	{
		this.session = session;
	}

	/**
	 * @return the softStrategyHTML
	 */
	public String getSoftStrategyHTML()
	{
		return softStrategyHTML;
	}

	/**
	 * @param softStrategyHTML
	 *            the softStrategyHTML to set
	 */
	public void setSoftStrategyHTML(String softStrategyHTML)
	{
		this.softStrategyHTML = softStrategyHTML;
	}

	/**
	 * @return the softStrategy_type
	 */
	public String getSoftStrategy_type()
	{
		return softStrategy_type;
	}

	/**
	 * @param softStrategy_type
	 *            the softStrategy_type to set
	 */
	public void setSoftStrategy_type(String softStrategy_type)
	{
		this.softStrategy_type = softStrategy_type;
	}

	/**
	 * @return the param
	 */
	public String getParam()
	{
		return param;
	}

	/**
	 * @param param
	 *            the param to set
	 */
	public void setParam(String param)
	{
		this.param = param;
	}

	/**
	 * @return the gwDeviceQueryBio
	 */
	public GwDeviceQueryBIO getGwDeviceQueryBio()
	{
		return gwDeviceQueryBio;
	}

	/**
	 * @param gwDeviceQueryBio
	 *            the gwDeviceQueryBio to set
	 */
	public void setGwDeviceQueryBio(GwDeviceQueryBIO gwDeviceQueryBio)
	{
		this.gwDeviceQueryBio = gwDeviceQueryBio;
	}

    /**
     * 获取strRename
     * @return String strRename
     */
    public String getStrRename()
    {
        return strRename;
    }

    /**
     * 设置strRename
     * @param String strRename
     */
    public void setStrRename(String strRename)
    {
        this.strRename = strRename;
    }

    /**
     * 获取urlParameter
     * @return String urlParameter
     */
    public String getUrlParameter()
    {
        return urlParameter;
    }

    /**
     * 设置urlParameter
     * @param String urlParameter
     */
    public void setUrlParameter(String urlParameter)
    {
//        logger.error("setUrlParameter:{}",urlParameter);
        this.urlParameter = urlParameter;
    }

    /**
     * 获取file1
     * @return File file1
     */
    public File getFile1()
    {
        return file1;
    }

    /**
     * 设置file1
     * @param File file1
     */
    public void setFile1(File file1)
    {
        this.file1 = file1;
    }

    /**
     * 获取response
     * @return String response
     */
    public String getResponse()
    {
        return response;
    }

    /**
     * 设置response
     * @param String response
     */
    public void setResponse(String response)
    {
        this.response = response;
    }

    /**
     * 获取path
     * @return String path
     */
    public String getPath()
    {
        return path;
    }

    /**
     * 设置path
     * @param String path
     */
    public void setPath(String path)
    {
        this.path = path;
    }

	public String getGw_type() {
		return gw_type;
	}

	public void setGw_type(String gwType) {
		gw_type = gwType;
	}

	
	public String getGoal_devicetype_id()
	{
		return goal_devicetype_id;
	}

	
	public void setGoal_devicetype_id(String goal_devicetype_id)
	{
		this.goal_devicetype_id = goal_devicetype_id;
	}
	public String getImportQueryField() {
		return importQueryField;
	}

	public void setImportQueryField(String importQueryField) {
		this.importQueryField = importQueryField;
	}
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getGwShare_queryType() {
		return gwShare_queryType;
	}

	public void setGwShare_queryType(String gwShareQueryType) {
		gwShare_queryType = gwShareQueryType;
	}

	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}
	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}

	public List getTaskList() {
		return taskList;
	}

	public void setTaskList(List taskList) {
		this.taskList = taskList;
	}

	public String getStarttime() {
		return starttime;
	}

	public String getEndtime() {
		return endtime;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}


	
	public String getHttp_url()
	{
		return http_url;
	}


	
	public void setHttp_url(String http_url)
	{
		this.http_url = http_url;
	}


	
	public String getReport_url()
	{
		return report_url;
	}


	
	public void setReport_url(String report_url)
	{
		this.report_url = report_url;
	}


	
	public void setFileName_st(String fileName_st)
	{
		this.fileName_st = fileName_st;
	}

	
	public String getGwShare_fileName()
	{
		return gwShare_fileName;
	}

	
	public void setGwShare_fileName(String gwShare_fileName)
	{
		this.gwShare_fileName = gwShare_fileName;
	}



	
	public String getTask_desc()
	{
		return task_desc;
	}


	public void setTask_desc(String task_desc)
	{
		this.task_desc = task_desc;
	}

	/**
	 * @return the taskNumFalg
	 */
	public int isTaskNumFalg()
	{
		return taskNumFalg;
	}

	/**
	 * @param taskNumFalg the taskNumFalg to set
	 */
	public void setTaskNumFalg(int taskNumFalg)
	{
		this.taskNumFalg = taskNumFalg;
	}

	/**
	 * @return the instAreaName
	 */
	public String getInstAreaName()
	{
		return instAreaName;
	}

	/**
	 * @param instAreaName the instAreaName to set
	 */
	public void setInstAreaName(String instAreaName)
	{
		this.instAreaName = instAreaName;
	}


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}

	public String getTask_name() {
		return task_name;
	}

	public void setTask_name(String task_name) {
		this.task_name = task_name;
	}

	public String getSpeed_frequency() {
		return speed_frequency;
	}

	public void setSpeed_frequency(String speed_frequency) {
		this.speed_frequency = speed_frequency;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	

	public String getInstArea() {
		return instArea;
	}

	public void setInstArea(String instArea) {
		this.instArea = instArea;
	}

	public String getOperType() {
		return operType;
	}

	public void setOperType(String operType) {
		this.operType = operType;
	}



	
	public String getBEGIN_TIME()
	{
		return BEGIN_TIME;
	}



	
	public void setBEGIN_TIME(String bEGIN_TIME)
	{
		BEGIN_TIME = bEGIN_TIME;
	}



	
	public String getEND_TIME()
	{
		return END_TIME;
	}



	
	public void setEND_TIME(String eND_TIME)
	{
		END_TIME = eND_TIME;
	}



	
	public String getTOTAL_TIMES()
	{
		return TOTAL_TIMES;
	}



	
	public void setTOTAL_TIMES(String tOTAL_TIMES)
	{
		TOTAL_TIMES = tOTAL_TIMES;
	}



	
	public String getPERIOD()
	{
		return PERIOD;
	}



	
	public void setPERIOD(String pERIOD)
	{
		PERIOD = pERIOD;
	}



	
	public String getFileName_st()
	{
		return fileName_st;
	}
}
