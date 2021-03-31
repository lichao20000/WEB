<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@page import="java.util.ArrayList"%>
<%@ page import="com.linkage.litms.common.filter.SelectCityFilter"%>
<%@page import="com.linkage.litms.common.database.Cursor"%>
<jsp:useBean id="DeviceAct" scope="request" class="com.linkage.litms.resource.DeviceAct"/>
<jsp:useBean id="city" scope="request"
	class="com.linkage.module.gwms.dao.tabquery.CityDAO" />
<%request.setCharacterEncoding("GBK");
            Map fields = null;
//            int offset;
//            String strClr = "";
            Map cityAllMap = city.getCityIdCityNameMap();

            //----------------------属地过滤 add by YYS 2006-10-12 ---------------
           // String city_id = curUser.getCityId();
           // SelectCityFilter CityFilter = new SelectCityFilter(request);
           // String strCityList = CityFilter.getAllSubCitiesBox(city_id, false,city_id, "", true);
            //-------------------------------------------------------------------
     
            ArrayList list = new ArrayList();
            list.clear();
            list = city.getCityList(request);
            
            String strData = "";
            String strCityList  = "";
            strCityList = DeviceAct.getCityListSelf(false,"","parent_id",request);
            
            //String stroffset = request.getParameter("offset");
           // if (stroffset == null)
                //offset = 1;
            //else
               // offset = Integer.parseInt(stroffset);

            String strBar = String.valueOf(list.get(0));
            Cursor cursor = (Cursor) list.get(1);
            fields = cursor.getNext();

            if (fields == null) {
                strData = "<TR><TD COLSPAN=5 HEIGHT=30 CLASS=column>系统没有属地资源</TD></TR>";
            } else {
                int i = 1;
                while (fields != null) {
                   // if ((i % 2) == 0)
                       // strClr = "#e7e7e7";
                   // else
                        //strClr = "#FFFFFF";

                    strData += "<TR>";
                    strData += "<TD CLASS=column1>" + fields.get("city_id")
                            + "</TD>";
                    strData += "<TD CLASS=column2>" + fields.get("city_name")
                            + "</TD>";
                    strData += "<TD CLASS=column2>"
                            + (cityAllMap.get(fields.get("parent_id")) == null ? ""
                                    : cityAllMap.get(fields.get("parent_id")))
                            + "</TD>";
                    strData += "<TD CLASS=column1 align='right'>" + fields.get("staff_id")
                            + "</TD>";
                    strData += "<TD CLASS=column2 align='center'> <A HREF=UpdateCityForm.jsp?city_id="
                            + fields.get("city_id")
                            + " onclick='return Edit(this.href);'>编辑</A> | <A HREF=CitySave.jsp?action=delete&city_id="
                            + (String) fields.get("city_id")
                            + " onclick='return delWarn();'>删除</A></TD>";
                    strData += "</TR>";
                    i++;
                    fields = cursor.getNext();
                }
                strData += "<TR><TD COLSPAN=5 align=right CLASS=green_foot>"
                        + strBar + "</TD></TR>";
            }

            //clear
            fields = null;
            cursor = null;
            list.clear();
            strBar = null;
            cityAllMap = null;
%>
<%@ include file="../head.jsp"%>
<SCRIPT LANGUAGE="JavaScript">
<!--
function Add(){
	document.frm.reset();
	document.frm.action.value="add";
	actLabel.innerHTML = '添加';
	cityLabel.innerHTML = '';
}

function Edit(page){
	document.all("childFrm").src = page;
	return false;
}

function delWarn(){
	if(confirm("真的要删除该属地吗？\n本操作所删除的不能恢复！！！")){
		return true;
	}
	else{
		return false;
	}
}

function CheckForm(){
	var obj = document.frm;
	if(!IsNull(obj.city_id.value,'属地编码')){
		obj.city_id.focus();
		obj.city_id.select();
		return false;
	}
	else if(!IsNull(obj.city_name.value,'属地名称')){
		obj.city_name.focus();
		obj.city_name.select();
		return false;
	}
	/*else if(!IsNull(obj.sno.value,'全省统一编码')){
		obj.sno.focus();
		obj.sno.select();
		return false;
	}*/
	else{
		return true;
	}	
}

//-->
</SCRIPT>
<style>
span
{
	position:static;
	border:0;
}
</style>
<%@ include file="../toolbar.jsp"%>
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
<TR><TD HEIGHT=20>&nbsp;</TD></TR>
<tr>
	<td>
	<table width="98%" height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd" align="center">
		<tr>
			<td width="162" align="center" class="title_bigwhite">
				基础资源
			</td>
			<td>
				<img src="../images/attention_2.gif" width="15" height="12">
				带'<font color="#FF0000">*</font>'的表单必须填写或选择
			</td>
			<td align="right">
				<A HREF='javascript:Add();'>增加&nbsp;&nbsp;</A>
			</td>
		</tr>
	</table>
	</td>
</tr>
	<TR>
		<TD>
		<FORM NAME="frm" METHOD="post" ACTION="CitySave.jsp"
			onsubmit="return CheckForm()">
		<TABLE width="98%" border=0 cellspacing=0 cellpadding=0 align="center">
			<TR>
				<TD bgcolor=#999999>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
					<TR>
						<TH colspan="5">属地资源</TH>
					</TR>
					<TR>
						<!-- <TD>设备编号</TD> -->
						<TD class=green_title2>属地标识</TD>
						<TD class=green_title2>属地名称</TD>
						<TD class=green_title2>上级属地</TD>
						<TD class=green_title2>操作员ID</TD>
						<TD class=green_title2>操作</TD>
					</TR>
					<%out.println(strData);%>
				</TABLE>
				</TD>
			</TR>
		</TABLE>
		<BR>
		<TABLE width="98%" border=0 cellspacing=0 cellpadding=0 align="center">
			<TR>
				<TD bgcolor=#999999>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
					<TR>
						<TH align="center" colspan=4 valign="center"><B><span
							id="actLabel">添加</span><span id="cityLabel"></span>属地资源</B></TH>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right">属地标识</TD>
						<TD colspan=3><input type="hidden" name="city_id_old"> <INPUT
							TYPE="text" NAME="city_id" maxlength=20 class=bk size=20>&nbsp;<font
							color="#FF0000">*</font></TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right">属地名称</TD>
						<TD colspan=3><INPUT TYPE="text" NAME="city_name" maxlength=50
							class=bk size=20>&nbsp;<font color="#FF0000">*</font></TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right">上级属地</TD>
						<input type="hidden" name="parent_id_old">
						<TD colspan=3><%=strCityList%>&nbsp;<font color="#FF0000">*</font>
						</TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right">操作员ID</TD>
						<TD colspan=3><INPUT TYPE="text" NAME="staff_id" maxlength=30
							class=bk size=20 value="<%=user.getAccount()%>"></TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right">备注</TD>
						<TD colspan=3><INPUT TYPE="text" NAME="remark" maxlength=100
							class=bk size=50></TD>
					</TR>
					<TR>
						<TD colspan="4" align="right" class=green_foot><INPUT TYPE="submit"
							value=" 保 存 " class=btn>&nbsp;&nbsp; <INPUT TYPE="hidden"
							name="action" value="add"> <INPUT TYPE="reset" value=" 重 写 "
							class=btn></TD>
					</TR>
				</TABLE>
				</TD>
			</TR>
		</TABLE>
		</FORM>
		</TD>
	</TR>
	<TR>
		<TD HEIGHT=20>&nbsp;<IFRAME ID=childFrm SRC="" STYLE="display:none"></IFRAME></TD>
	</TR>
</TABLE>
<%@ include file="../foot.jsp"%>
