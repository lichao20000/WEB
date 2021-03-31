
<%@page import="com.linkage.module.lims.system.common.database.Cursor"%>
<%@page import="com.linkage.module.lims.system.UserRes"%>
<%@page import="com.linkage.module.lims.system.User"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<link href="<s:url value="/css2/global.css"/>" rel="stylesheet"
	type="text/css">
<link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet"
	type="text/css">
<%@ page contentType="text/html;charset=GBK"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.ArrayList"%>
<%@page
	import="com.linkage.module.lims.system.common.database.DataSetBean"%>
<%@page import="java.util.Map"%>
<%@page
	import="com.linkage.module.lims.system.common.database.QueryPage"%>
<%@ page import="com.linkage.commons.db.DBUtil" %>
<jsp:useBean id="CityUtil" scope="request"
	class="com.linkage.module.lims.stb.util.CityUtil" />
<%
	request.setCharacterEncoding("GBK");
	UserRes curUser = (UserRes) session.getAttribute("curUser");
	String city_id = "";
	if(curUser!=null){
		User user = curUser.getUser();
		city_id = user.getCityId();
	}
	String sql = "select city_id,city_name from tab_city where parent_id='"
			+ city_id + "' or city_id='" + city_id + "'";
	Cursor cursor1 = DataSetBean.getCursor(sql);
	Map fields1 = cursor1.getNext();
	Map map = new HashMap();
	map = CityUtil.getCityIdCityNameMap();
	String stroffset = request.getParameter("offset");
	int pagelen = 15;
	String mySql = "select * from  tab_broad_info";
	// teledb
	if (DBUtil.GetDB() == 3) {
		mySql ="select titletype, title, city_id, content, id from  tab_broad_info";
	}
	com.linkage.commons.db.PrepareSQL psql = new com.linkage.commons.db.PrepareSQL(mySql);
	psql.getSQL();
	ArrayList list = new ArrayList();
	list.clear();
	int offset;
	if (stroffset == null)
		offset = 1;
	else
		offset = Integer.parseInt(stroffset);
	QueryPage qryp = new QueryPage();
	qryp.initPage(mySql, offset, pagelen);
	String strBar = qryp.getPageBar();
	Cursor cursor = DataSetBean.getCursor(mySql, offset, pagelen);
	Map fields = null;
	list.clear();
	String strData = "";
	if (stroffset == null)
		offset = 1;
	else
		offset = Integer.parseInt(stroffset);
	fields = cursor.getNext();
	if (fields == null)
	{
		strData = "<TR><TD COLSPAN=6 HEIGHT=30 >系统没有相关的公告信息</TD></TR>";
	}
	else
	{
		int i = 1;
		while (fields != null)
		{
			strData += "<TR>";
			strData += "<TD align=center>"
			+ fields.get("titletype") + "</TD>";
			strData += "<TD align=center>" + fields.get("title")
			+ "</TD>";
			String[] temCity = ((String) fields.get("city_id")).split(",");
			String temCityNames = "";
			for (int j = 0; j < temCity.length; j++)
			{
		if (temCityNames.equals(""))
			temCityNames = (String) map.get(temCity[j]);
		else
			temCityNames += "," + map.get(temCity[j]);
			}
			strData += "<TD align=center>" + temCityNames + "</TD>";
			//String temD = fields.get("broad_time")+"";		
			//strData += "<TD align=center>"+temD.substring(0,temD.indexOf(" "))+"</TD>";
			strData += "<TD CLASS=column1 align=center>" + fields.get("content")
			+ "</TD>";
			strData += "<TD align=center><A HREF=\"javascript:Edit('"
			+ (String) fields.get("city_id")
			+ "','"
			+ (String) fields.get("titletype")
			+ "','"
			+ (String) fields.get("title")
			+ "','"
			+ (String) fields.get("content")
			+ "');\">编辑</A> | <A HREF=inside_gonggaoxinxi1broadsava.jsp?action=delete&id="
			+ (String) fields.get("id")
			+ " onclick='return delWarn();'>删除</A></TD>";
			strData += "</TR>";
			i++;
			fields = cursor.getNext();
		}
		strData += "<TR><TD COLSPAN=6 align=right CLASS=column>" + strBar
		+ "</TD></TR>";
	}
	strBar = null;
%>
<SCRIPT LANGUAGE="JavaScript">
function selectAll(elmID){
	t_obj = document.all(elmID);
	if(!t_obj) return;
	obj = event.srcElement;

	if(obj.checked){
		for(var i=0; i<t_obj.length; i++){
			t_obj[i].checked = true;
		}
	}
	else{
		for(var i=0; i<t_obj.length; i++){
			t_obj[i].checked = false;
		}
	}
}

function loadSelectAll(elmID){
	t_obj = document.all(elmID);
	if(!t_obj) return;	

	for(var i=0; i<t_obj.length; i++){
		t_obj[i].checked = true;
	}
	
	document.all("checkbox").checked=true;

}

function Add(){
	document.frm.reset();
	document.frm.action.value="add";
	//actLabel.innerHTML = '添加';
}

function Edit(city_id,_lg_name,_lg_desc,_lg_title){	
	var city_ids = city_id.split(",");
	var t_obj = frm.kind;
	for(var i=0; i<t_obj.length; i++){
		for(var j=0;j<city_ids.length;j++){
			var tempcity = city_ids[j];
			if(t_obj[i].value==tempcity){
				t_obj[i].checked = true;
				break;
			}
			t_obj[i].checked = false;
		}

	}
		
	frm.service_year.value=_lg_name;
	frm.biaoti.value=_lg_desc;
	frm.content.value=_lg_title;
	document.frm.action.value="edit";
	frm.city_id_old.value=_lg_desc;
	document.getElementById("bordtitle").innerText="编辑公告";
	//actLabel.innerHTML = '编辑';
}

function delWarn(){
	if(confirm("真的要删除该信息吗？\n本操作所删除的不能恢复！！！")){
		return true;
	}
	else{
		return false;
	}
}

function CheckForm(){
  var obj = document.frm;
 
 
  var oselect = document.all("kind");
 if(oselect == null){
	alert("请选择属地！");
	return false;
 }
 var num = 0;
 if(typeof(oselect.length)=="undefined"){
	if(oselect.checked){
		num = 1;
	}
 }else{
	for(var i=0;i<oselect.length;i++){
		if(oselect[i].checked){
		  num++;
		}
	}
 }
 if(num ==0){
	alert("请选择属地！");
	return false;
 }

 if(obj.service_year.value == '请选择公告类型'){
         alert("请选择公告类型!");
		obj.service_year.focus();
		return false;
	}
	else if(!IsNull(obj.biaoti.value,'标题')){
		obj.biaoti.focus();
		return false;
	}
	else{
		return true;
	}
		
}
</SCRIPT>
<body onload="loadSelectAll('kind')">
	<TABLE width="98%" align="center" class="querytable">
		<TR>
			<TD>
				<IMG height=20 src="<s:url value='/images/bite_2.gif'/>" width=24>
				您当前的置：添加新公告
			</TD>
		</TR>
	</TABLE>

	<FORM METHOD="POST" ACTION="inside_gonggaoxinxi1broadsava.jsp"
		NAME="frm" onSubmit="return CheckForm();">
		<TABLE width="98%" class="listtable" align="center">
			<thead>
				<tr>
					<th colspan=6 >
						公告信息显示
					</th>
				</tr>
				<TR>
					<th>
						公告类型
					</th>
					<th>
						公告标题
					</th>
					<th>
						属 地
					</th>
					<th>
						公告内容
					</th>
					<th>
						操作[默认增加]
					</th>
				</TR>
			</thead>
			<tbody>

				<%
				out.println(strData);
				%>
			</tbody>
		</TABLE>
		<BR>
		<TABLE width="98%" class="querytable" align="center">
			<TR>
				<td colspan=2 class="title_1">
					<div align="center" id="bordtitle">
						添加新公告
					</div>
				</td>
			</TR>
			<TR>
				<TD class="title_2" width="180">
					属地：
					<br>
					<INPUT TYPE="checkbox" id="checkbox" onclick="selectAll('kind')">
					全选
				</TD>
				<TD bgcolor="#FFFFFF">
					<%
						while (fields1 != null)
						{
							out.println("<input id='kind' type=checkbox name='kind' value="
							+ fields1.get("city_id") + ">  "
							+ fields1.get("city_name".toLowerCase()));
							fields1 = cursor1.getNext();
						}
					%>
				</TD>
			</TR>
			<TR bgcolor="#FFFFFF">
				<TD class="title_2">
					公告类型：
				</TD>
				<TD>
					<select name="service_year" class='bk'>
						<option value="请选择公告类型">
							请选择公告类型
						</option>
						<option value="公告">
							公告
						</option>
						<option value="新增功能">
							新增功能
						</option>
						<option value="提示">
							提示
						</option>
					</select>
				</TD>
			</TR>
			<TR>
				<TD class="title_2">
					标题：
				</TD>
				<TD>
					<input type="hidden" name="city_id_old">
					<INPUT TYPE="text" NAME="biaoti" maxlength=50 class=bk>
				</TD>
			</TR>
			<TR>
				<TD class="title_2">
					内容：
				</TD>
				<TD>
					<textarea name=content cols="60" rows="6"></textarea>
				</TD>
			</TR>
			<TR>
				<TD colspan=2 align=center class="green_foot">
					<div class="right">
						<INPUT TYPE="submit" value=" 保 存 ">
						&nbsp;&nbsp;
						<INPUT TYPE="hidden" name="action" value="add">
						<input type="button" name="close" value=" 关 闭 "
							onclick="javascript:window.close();" class="btn">
						&nbsp;&nbsp;&nbsp;&nbsp;
					</div>
				</TD>
			</TR>
		</TABLE>
	</FORM>
	<TR>
		<TD HEIGHT=20>
			&nbsp;
			<IFRAME ID=childFrm SRC="" STYLE="display: none"></IFRAME>
		</TD>
	</TR>
</body>
