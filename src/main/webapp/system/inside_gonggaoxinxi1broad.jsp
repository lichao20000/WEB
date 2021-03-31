<%--
Author		: liuli
Date		: 2006-10-13
Desc		: 手工录入公告信息
--%>
<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@page import="com.linkage.litms.common.database.DataSetBean"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.linkage.litms.common.database.QueryPage"%>
<%@page import="com.linkage.litms.common.database.Cursor"%>
<%@ page import="com.linkage.commons.db.DBUtil" %>
<jsp:useBean id="CityDAO" scope="request" class="com.linkage.module.gwms.dao.tabquery.CityDAO"/>
<%
    request.setCharacterEncoding("GBK");
    String city_id = curUser.getCityId();
    String sql = "select city_id,city_name from tab_city where parent_id='"+ city_id + "' or city_id='" + city_id + "'";
    Cursor cursor1 = DataSetBean.getCursor(sql);
    Map fields1 = cursor1.getNext();
    Map map = new HashMap();
    map = CityDAO.getCityIdCityNameMap();
   
    String stroffset = request.getParameter("offset");
    int pagelen = 15;
	String mySql ="select * from  tab_broad_info";
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
    if(stroffset == null) offset = 1;
    else offset = Integer.parseInt(stroffset);
    fields = cursor.getNext();
    if(fields == null){
	strData = "<TR><TD COLSPAN=6 HEIGHT=30 CLASS=column>系统没有相关的公告信息</TD></TR>";
    }
    else{
	int i=1;
	while(fields != null){
		strData += "<TR>";
		strData += "<TD CLASS=column1 align=center>"+ fields.get("titletype") + "</TD>";
		strData += "<TD CLASS=column2 align=center>"+ fields.get("title") + "</TD>";
		
		String[] temCity = ((String)fields.get("city_id")).split(","); 
		String temCityNames = "";
		for(int j=0;j<temCity.length;j++){
			if(temCityNames.equals(""))
				temCityNames = (String)map.get(temCity[j]);
			else
				temCityNames += "," + map.get(temCity[j]);
		}
	    strData += "<TD class=column2 align=center>" + temCityNames + "</TD>";
		//String temD = fields.get("broad_time")+"";		
		//strData += "<TD CLASS=column2 align=center>"+temD.substring(0,temD.indexOf(" "))+"</TD>";
		strData += "<TD CLASS=column1 align=center>"+ fields.get("content") + "</TD>";
		strData += "<TD CLASS=column2 align=center><A HREF=\"javascript:Edit('"+(String)fields.get("city_id")+"','"+(String)fields.get("titletype")+"','"+(String)fields.get("title")+"','"+(String)fields.get("content")+"');\">编辑</A> | <A HREF=inside_gonggaoxinxi1broadsava.jsp?action=delete&id="+ (String)fields.get("id") +" onclick='return delWarn();'>删除</A></TD>";
		strData += "</TR>";
		i++;
		fields = cursor.getNext();
	}
	strData += "<TR><TD COLSPAN=6 align=right CLASS=column>" + strBar + "</TD></TR>";
    }
    strBar = null;
%>
<%@ include file="../head.jsp"%>
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
	actLabel.innerHTML = '添加';
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
	actLabel.innerHTML = '编辑';
}

function delWarn(){
	if(confirm("真的要删除该属地吗？\n本操作所删除的不能恢复！！！")){
		return true;
	}
	else{
		return false;
	}
}
</SCRIPT>
<%@ include file="../toolbar.jsp"%>
<body onload="loadSelectAll('kind')">
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
<TR><TD HEIGHT=20>&nbsp;</TD></TR>
<TR><TD>
    <FORM METHOD="POST" ACTION="inside_gonggaoxinxi1broadsava.jsp" NAME="frm" onSubmit="return CheckForm()">
	<TABLE width="95%" border=0 cellspacing=0 cellpadding=0 align="center">
		<TR>
			<TD bgcolor=#000000>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
					<TR>
				     <td colspan=6 class="blue_title">
				     <div align="center">公告信息显示</div>
				     </td>
					</TR>
					<TR>
						<TH>公告类型</TH>
						<TH>公告标题</TH>
						<TH>属　地</TH>
						<TH>公告内容</TH>
						<TH>操作[默认增加]</TH>
					</TR>
					<%out.println(strData);%>
				</TABLE>
			</TD>
		</TR>
	</TABLE>
	<BR>
	<TABLE width="95%" border=0 align="center" cellpadding=0 cellspacing=0 bgcolor="#999999" class="text">
		<TR>
			<TD bgcolor=#000000>
              <TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
                <TR bgcolor="#FFFFFF">
				<td colspan=2 class="blue_title">
				<div align="center">添加新公告</div>
				</td>
				</TR>
			    <TR bgcolor="#FFFFFF">
				<TD align="center"  width="180">属地:<br>
				<INPUT TYPE="checkbox" id="checkbox" onclick="selectAll('kind')">全选</TD>
				<TD bgcolor="#FFFFFF">
				<%
				   while (fields1 != null) {
				   out.println("<input id='kind' type=checkbox name='kind' value="+fields1.get("city_id")+">  "+ fields1.get("city_name".toLowerCase()));
			       fields1 = cursor1.getNext();
					}
                 %>
				</TD>
				</TR>  
				<TR bgcolor="#FFFFFF">
				<TD align="center">公告类型：</TD>                           
                  <TD> 
                    <select name="service_year" class=bk>
                            <option value="请选择公告类型">请选择公告类型</option>
							<option value="公告">公告</option>
							<option value="新增功能">新增功能</option>
							<option value="提示">提示</option>
					</select>
                  </TD>
                  </TR> 
                  <TR bgcolor="#FFFFFF">
                  <TD align="center">标题：</TD>
                  <TD>
                  <input type="hidden" name="city_id_old">
                  <INPUT TYPE="text" NAME="biaoti" maxlength=50 class=bk>
                  </TD>
                </TR>       
                <TR bgcolor="#FFFFFF"> 
                  <TD align="center">内容:</TD>
                  <TD> 
                  	<textarea name=content col="70" rows="10"></textarea>                
                  </TD>     
                </TR>                                    										
       		     <TR>
					<TD colspan=2 align=center class="blue_foot">
					 <div> 
					 <INPUT TYPE="submit" value=" 保 存 " class=btn>&nbsp;&nbsp;
					 <INPUT TYPE="hidden" name="action" value="add">
					 <input type="button" name="close" value=" 关 闭 " onclick="javascript:window.close();" class="btn">
                     &nbsp;&nbsp;&nbsp;&nbsp; </div>                     				
					</TD>
				</TR>
              </TABLE>
			</TD>
		</TR>
	</TABLE>
	</FORM>
</TD></TR>
<TR><TD HEIGHT=20>&nbsp;<IFRAME ID=childFrm SRC="" STYLE="display:none"></IFRAME></TD></TR>
</TABLE>
</body>
<%@ include file="../foot.jsp"%>