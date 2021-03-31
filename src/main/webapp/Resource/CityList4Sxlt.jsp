<%@page import="com.alibaba.fastjson.JSONObject"%>
<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@page import="java.util.ArrayList"%>
<%@ page import="com.linkage.litms.common.filter.SelectCityFilter"%>
<%@page import="com.linkage.litms.common.database.Cursor"%>
<jsp:useBean id="DeviceAct" scope="request" class="com.linkage.litms.resource.DeviceAct"/>
<jsp:useBean id="city" scope="request"
	class="com.linkage.module.gwms.dao.tabquery.CityDAO" />
<script type="text/javascript" src="../Js/jquery.js"></script>
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
                    String cityInfo = JSONObject.toJSONString(fields);
                    cityInfo = cityInfo.replace("\"", "\\\"");
                    strData += "<TD CLASS=column2 align='center'>"
                    		+ "<A HREF=javascript:edit('" + cityInfo + "')>编辑</A> |" 
                            + "<A HREF=javascript:del('" + (String) fields.get("city_id") + "')>删除</A></TD>";
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
function add(){
	document.frm.city_id.readOnly = false;
	document.frm.reset();
	document.frm.action.value="add";
	$("#actLabel").innerHTML = '添加';
	$("#cityLabel").innerHTML = '';
	$("#editModal").show();
}

function edit(cityInfo){
	cityInfo = eval("(" + cityInfo + ")")
	
	document.frm.city_id_old.value=cityInfo.city_id;
	document.frm.city_id.value=cityInfo.city_id;
	document.frm.city_id.readOnly = true;
	document.frm.city_name.value=cityInfo.city_name;
	document.frm.parent_id.value=cityInfo.parent_id;
	document.frm.parent_id_old.value=cityInfo.parent_id;
	document.frm.staff_id.value =cityInfo.staff_id;
	document.frm.remark.value =cityInfo.remark;
	document.frm.action.value='update';
	$("#actLabel").innerHTML = '编辑';
	$("#cityLabel").innerHTML = '〖' + cityInfo.city_name + '〗';
	
	$("#editModal").show();
}

// 保存
function save()
{
	if(!checkForm()) {
		return;
	}
	
	$.ajax({
		type:"post",
		url:"CitySave.jsp",
		data: $("#frm").serialize(),
		dataType:'text',
		success:function(data){
			if(data.indexOf('成功') != -1){
				$("#editModal").hide();
				queryFrm.submit();
			}else{
				alert(data);
			}
		},
		error:function(e){
			alert("服务器异常");
			console.info("e",e);
		}
	});
}

//删除
function del(city_id)
{
	if(confirm("真的要删除该属地吗？\n本操作所删除的不能恢复！！！"))
	{
       $.ajax({
			type:"post",
			url:"CitySave.jsp",
			data: {
				action: 'delete',
				city_id: city_id
			},
			dataType:'text',
			success:function(data){
				if(data.indexOf('成功') != -1){
					$("#editModal").hide();
					queryFrm.submit();
				}else{
					alert(data);
				}
			},
			error:function(e){
				alert("服务器异常");
				console.info("e",e);
			}
	   });
	}
}

// 校验
function checkForm()
{
	var obj = document.frm;
	
	if(!IsNull(obj.city_id.value,'属地编码')){
		obj.city_id.focus();
		obj.city_id.select();
		return false;
	}
	
	if(!IsNull(obj.city_name.value,'属地名称')){
		obj.city_name.focus();
		obj.city_name.select();
		return false;
	}
	if(obj.parent_id.value == -1){
		alert("请选择上级属地！");
		return false;
	}
	/*else if(!IsNull(obj.sno.value,'全省统一编码')){
		obj.sno.focus();
		obj.sno.select();
		return false;
	}*/
	return true;
}
</SCRIPT>
<style>
span
{
	position:static;
	border:0;
}
.querytable{
	border-top: solid 1px #999;
	border-right: solid 1px #999;
}
.querytable th, .querytable td{
 	border-bottom: solid 1px #999;
 	border-left: solid 1px #999;
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
				属地资源
			</td>
			<td>
				<img src="../images/attention_2.gif" width="15" height="12">
				带'<font color="#FF0000">*</font>'的表单必须填写或选择
			</td>
		</tr>
	</table>
	</td>
</tr>
<tr>
	<td>
		<form name = "queryFrm" action="CityList4Sxlt.jsp">
			<table class="querytable"  width="98%" border=0 cellspacing=0 cellpadding=0 align="center">
				<TR>
					<th colspan="4">属地查询</th>
				</TR>
				<TR>
					<TD class="column" width='15%' align="right">属地名称</TD>
					<TD>
						<input type="text" name="city_name" id="city_name" style="width:225px" value="" maxlength=50 class=bk>
					</TD>
				</TR>
				
				<TR>
					<td colspan="4" align="right" class=green_foot>
						<button type="button" onclick="add()" >&nbsp;新&nbsp;&nbsp;增&nbsp;</button>&nbsp;&nbsp;
						<button type="submit">&nbsp;查&nbsp;&nbsp;询&nbsp;</button>
					</td>
				</TR>
			</table>
		</form>
	</td>
</tr>
	<TR>
		<TD>
		<FORM NAME="frm" id="frm" METHOD="post" ACTION="CitySave.jsp">
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
		<TABLE width="98%" border=0 cellspacing=0 cellpadding=0 align="center" id="editModal" style="display: none">
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
						<TD colspan=3><INPUT TYPE="text" NAME="staff_id" maxlength=30 readonly="readonly"
							class=bk size=20 value="<%=user.getAccount()%>"></TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right">备注</TD>
						<TD colspan=3><INPUT TYPE="text" NAME="remark" maxlength=100
							class=bk size=50></TD>
					</TR>
					<TR>
						<TD colspan="4" align="right" class=green_foot>
							<INPUT TYPE="hidden" name="action" value=""> 
							<INPUT TYPE="button" onclick="save()" value=" 保 存 " class=btn>&nbsp;&nbsp; 
							<INPUT TYPE="reset" value=" 重 写 " class=btn>
						</TD>
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
