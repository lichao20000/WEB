<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="java.io.*,javax.servlet.*" %>
<%@ page import="java.net.*"%>
<%@ page import="java.nio.*" %>
<%@ page import="java.util.*" %>
<%@ page import="com.linkage.litms.common.database.*" %>
<%@ page import="java.util.Properties"%> 
<%@ page import="com.jspsmart.upload.*" %>
<%@ page import="com.linkage.module.gwms.Global" %>
<%@ page import="com.linkage.commons.db.DBUtil" %>

<%
	String dir = (String)request.getParameter("dir");
	String name=(String)request.getParameter("name");
	String gwType=(String)request.getParameter("gwType");

	long dir_id;
	if(dir!=null && !dir.equals("")){
		dir_id=Long.parseLong(dir);
	}else{
		out.print("数据错误！");
		return;
	}
  
	String tomcat_url="";
	String server_dir = "";
	String url_name="";
	String sql = "select * from tab_file_server where dir_id="+dir_id;
	// teledb
	if (DBUtil.GetDB() == 3) {
		sql = "select outter_url, inner_url, server_dir from tab_file_server where dir_id="+dir_id;
	}
	System.out.println(sql);

	com.linkage.commons.db.PrepareSQL psql = new com.linkage.commons.db.PrepareSQL(sql);
	psql.getSQL();
	Cursor cursorTmp = DataSetBean.getCursor(sql);
	Map fieldTmp = cursorTmp.getNext();

	while (fieldTmp != null)
	{
		if("nmg_dx".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))
				&& Global.GW_TYPE_STB.equals(gwType)){
			tomcat_url = (String)fieldTmp.get("outter_url");
		}else{
			tomcat_url = (String)fieldTmp.get("inner_url");
		}
		
		server_dir=(String)fieldTmp.get("server_dir");
		url_name=tomcat_url+"/"+server_dir;

		fieldTmp = cursorTmp.getNext();
	}

	url_name = url_name + "/" + name;

	response.setHeader("Content-disposition","attachment;filename="+name);   
	response.setContentType("application/x-download"); 

	URL tric;

	HttpURLConnection httpUrl;

	try 
	{
		tric = new URL(url_name); //	构建一URL对象

		httpUrl = (HttpURLConnection)tric.openConnection(); 
		httpUrl.connect(); 
		
		if("nmg_dx".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))
				&& Global.GW_TYPE_STB.equals(gwType))
		{
			BufferedInputStream bis = new BufferedInputStream(tric.openStream()); 
			BufferedOutputStream bos = new BufferedOutputStream(response.getOutputStream()); 
	 	     
			byte[] b = new byte[1024]; 
			int len = 0; 
			while((len = bis.read(b)) != -1){ 
				bos.write(b,0,len); 
			} 
	 	        
			bis.close(); 
			bos.close(); 
		}
		else
		{
			BufferedReader in = new BufferedReader(new InputStreamReader(tric.openStream()));
			String inputline;

			while((inputline=in.readLine())!=null){
				out.println(inputline);
			}

			in.close();
		}
	}catch (ConnectException e){

	}catch (MalformedURLException e) {

	}catch (IOException e) {

	}finally{

	}
  

 %>



