<%--

Author		: benyp

Date		: 2007-7-16

Note		:

--%>

<%@ include file="../timelater.jsp"%>

<%@ page contentType="text/html;charset=GBK"%>

<%@ page import="java.io.*,javax.servlet.*" %>

<%@ page import="java.util.*" %>

<%@ page import="java.net.*"%>

<%@ page import="com.linkage.litms.common.database.*" %>

<%@ page import="java.nio.*" %>

<%@ page import="java.util.Properties"%>
<%@ page import="com.linkage.commons.db.DBUtil" %>


<SCRIPT LANGUAGE="JavaScript">

 function dosave(page){

	document.all("childFrm").src = page;

 }

</SCRIPT>

<%

  String dir = (String)request.getParameter("dir");

  String name=(String)request.getParameter("name");

  long dir_id;

  if(dir!=null && !dir.equals("")){

     dir_id=Long.parseLong(dir);

  }else{

    out.print("���ݴ���");

    return;

  }

  String tomcat_url="";

  String server_dir = "";

  String url_name="";

  String sql = "select * from tab_file_server where dir_id="+dir_id;

  // teledb
  if (DBUtil.GetDB() == 3) {
	  sql = "select inner_url, server_dir from tab_file_server where dir_id="+dir_id;
  }
  com.linkage.commons.db.PrepareSQL psql = new com.linkage.commons.db.PrepareSQL(sql);
  psql.getSQL();
  Cursor cursorTmp = DataSetBean.getCursor(sql);

  Map fieldTmp = cursorTmp.getNext();

  while (fieldTmp != null){

			

			tomcat_url = (String)fieldTmp.get("inner_url");

			server_dir=(String)fieldTmp.get("server_dir");

			url_name=tomcat_url+"/"+server_dir;

			fieldTmp = cursorTmp.getNext();


		}

  url_name = url_name + "/" + name;





  URL tric;

  HttpURLConnection httpUrl;

  BufferedReader in;

  

  try {

		tric = new URL(url_name); //	����һURL����

		httpUrl = (HttpURLConnection)tric.openConnection(); 

		httpUrl.connect(); 

		//out.println("<form action='"+url_name+"'>");

		out.println("<form action='saveFile.jsp?dir="+dir+"&name="+name+"' method='post'>");

		out.println("<input type=submit value='�����ļ�' >");

		out.println("<HR>");

		out.println("<br>");

		out.println("�ļ�����Ϊ��");

		out.println("<br>");

		in = new BufferedReader(new InputStreamReader(tric.openStream()));

		String inputline;

		    

	        while((inputline=in.readLine())!=null){

               

               inputline = inputline.replaceAll("<","&lt");

               inputline = inputline.replaceAll(">","&gt");

                

                out.println("<NOBR>"+inputline+"</NOBR>"+"<br>");

                

	           }

            

	        in.close();

       

        

        

	}catch (ConnectException e){

		e.printStackTrace();

		out.println("�ļ������ڣ������ļ�·���Ƿ���ȷ��");

			

	

	}

	catch (MalformedURLException e) {

	    

	    e.printStackTrace();

	}catch (IOException e) {

		// TODO Auto-generated catch block

		e.printStackTrace();

	}finally{

	 

	}





	out.println("</form>");

  

 %>

<HTML>

<head>

<%@ include file="../head.jsp"%>

  <title><%=name %></title>

  <TR><TD HEIGHT=20>&nbsp;<IFRAME name="childFrm" SRC="" STYLE="display:none"></IFRAME></TD></TR>

</head>

</HTML>

