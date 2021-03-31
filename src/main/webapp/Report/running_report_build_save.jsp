<%--
FileName	: IpLineSave.jsp
Author		: liuli
Date		: 2007-3-06
Note		: 帐号增加、修改、删除操作
--%>
<html>
<%@ page import="java.util.Map,java.util.TreeMap,java.util.List,java.util.Iterator,java.text.SimpleDateFormat,java.util.Date,java.util.Set,com.linkage.litms.common.database.Cursor,com.linkage.litms.common.database.DataSetBean,java.io.BufferedReader,java.io.FileNotFoundException,java.io.FileReader,java.io.IOException"%>
<%@ page import="com.linkage.commons.db.DBUtil" %>
<jsp:useBean id="rrctMgr" scope="request" class="com.linkage.litms.report.RRcTMgr"/>
<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%
	request.setCharacterEncoding("GBK");
	String action_type = request.getParameter("action_type");
	String device_id = request.getParameter("device_id");
	String html = "";
	String suggestion = "";
	int updateRet = -1;
	String time_ = "";

	if ("1".equals(action_type)) {
		String sql = "select * from tab_rrct_report where is_send=0 and device_id='" + device_id + "'";
		// teledb
		if (DBUtil.GetDB() == 3) {
			sql = "select file_path, build_time from tab_rrct_report where is_send=0 and device_id='" + device_id + "'";
		}
		com.linkage.commons.db.PrepareSQL psql = new com.linkage.commons.db.PrepareSQL(sql);
		psql.getSQL();
		Cursor cursor = DataSetBean.getCursor(sql);
		if (cursor.getRecordSize() != 0) {
			html += "<select name='fileList'>";
		    Map fields = cursor.getNext();
			while (fields != null) {
				String file_path = (String)fields.get("file_path");
				long build_time = Long.parseLong((String)fields.get("build_time"));
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
				String time = df.format(new Date(build_time));
				html += "<option value='" + file_path + "'>" + time;
				fields = cursor.getNext();
			}
			html += "</select>";
		}
	} else if ("2".equals(action_type)){
		String file_path = request.getParameter("file_path");
		String sql = "select suggestion from tab_rrct_report where file_path='" + file_path + "'";
		Map map = DataSetBean.getRecord(sql);
		suggestion = map.get("suggestion") == null ? "" : (String)map.get("suggestion");

	    try {
			BufferedReader reader = new BufferedReader(new FileReader(file_path));
			String line = reader.readLine();
			while (line != null) {
				html += line;
				line = reader.readLine();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	} else if ("3".equals(action_type)){
		String file_path = request.getParameter("file_path");
		String suggestion_ = request.getParameter("suggestion");
		String sql = "update tab_rrct_report set suggestion='" + suggestion_ + "', is_suggest=1 where file_path='" + file_path + "'";
		updateRet = DataSetBean.executeUpdate(sql);
	} else if ("4".equals(action_type)){
		//String file_path = request.getParameter("file_path");
		rrctMgr.sendFile(request);
	} else if ("5".equals(action_type)){
		String gather_time = request.getParameter("gather_time");
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		time_ = df.format(new Date(Long.parseLong(gather_time)));
		rrctMgr.gatherNow(request);
	} else if ("6".equals(action_type)){
		//String file_path = request.getParameter("file_path");
		rrctMgr.delFile(request);
	} else if ("7".equals(action_type)){
		//String file_path = request.getParameter("file_path");
		rrctMgr.buildAndSend(request);
	}

	String strlist = "";

%>

<body>
<SPAN ID="child"><%=strlist%></SPAN>
<SPAN ID="child2"><%=html%></SPAN>
<SCRIPT LANGUAGE="JavaScript">
var action_type = "<%=action_type%>";
var suggestion = "<%=suggestion%>";
if (action_type == 3) {
	var updateRet = "<%=updateRet%>";
	if (updateRet >= 0) {
		parent.alert("保存成功 :)");
	} else {
		parent.alert("保存失败 :(");
	}
} else if (action_type == 1) {
	var ret = document.getElementById("child2").innerHTML;
	if (ret != "") {
		parent.document.all("id_filelist").innerHTML = document.getElementById("child2").innerHTML;
		parent.setStatus(action_type, suggestion);
		parent.showId("tr_filelist");
	} else {
		parent.alert("没有未发送的报表文件！");
	}
} else if (action_type == 2) {
	parent.document.all("id_filecontext").innerHTML = document.getElementById("child2").innerHTML;
	parent.setStatus(action_type, suggestion);
} else if (action_type == 4) {
	parent.alert("命令已发送");
} else if (action_type == 5) {
	var file_name_ = "<%=time_%>";
	parent.alert("命令已发送，新产生的报表文件名为" + file_name_ + ".html，请稍后查看");
} else if (action_type == 6) {
	parent.alert("删除成功");
	parent.location=parent.location;
} else if (action_type == 7) {
	parent.alert("命令已发送");
}
</SCRIPT>
</body>
</html>
