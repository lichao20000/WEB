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

            //----------------------���ع��� add by YYS 2006-10-12 ---------------
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
                strData = "<TR><TD COLSPAN=5 HEIGHT=30 CLASS=column>ϵͳû��������Դ</TD></TR>";
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
                    		+ "<A HREF=javascript:edit('" + cityInfo + "')>�༭</A> |" 
                            + "<A HREF=javascript:del('" + (String) fields.get("city_id") + "')>ɾ��</A></TD>";
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
	$("#actLabel").innerHTML = '���';
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
	$("#actLabel").innerHTML = '�༭';
	$("#cityLabel").innerHTML = '��' + cityInfo.city_name + '��';
	
	$("#editModal").show();
}

// ����
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
			if(data.indexOf('�ɹ�') != -1){
				$("#editModal").hide();
				queryFrm.submit();
			}else{
				alert(data);
			}
		},
		error:function(e){
			alert("�������쳣");
			console.info("e",e);
		}
	});
}

//ɾ��
function del(city_id)
{
	if(confirm("���Ҫɾ����������\n��������ɾ���Ĳ��ָܻ�������"))
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
				if(data.indexOf('�ɹ�') != -1){
					$("#editModal").hide();
					queryFrm.submit();
				}else{
					alert(data);
				}
			},
			error:function(e){
				alert("�������쳣");
				console.info("e",e);
			}
	   });
	}
}

// У��
function checkForm()
{
	var obj = document.frm;
	
	if(!IsNull(obj.city_id.value,'���ر���')){
		obj.city_id.focus();
		obj.city_id.select();
		return false;
	}
	
	if(!IsNull(obj.city_name.value,'��������')){
		obj.city_name.focus();
		obj.city_name.select();
		return false;
	}
	if(obj.parent_id.value == -1){
		alert("��ѡ���ϼ����أ�");
		return false;
	}
	/*else if(!IsNull(obj.sno.value,'ȫʡͳһ����')){
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
				������Դ
			</td>
			<td>
				<img src="../images/attention_2.gif" width="15" height="12">
				��'<font color="#FF0000">*</font>'�ı�������д��ѡ��
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
					<th colspan="4">���ز�ѯ</th>
				</TR>
				<TR>
					<TD class="column" width='15%' align="right">��������</TD>
					<TD>
						<input type="text" name="city_name" id="city_name" style="width:225px" value="" maxlength=50 class=bk>
					</TD>
				</TR>
				
				<TR>
					<td colspan="4" align="right" class=green_foot>
						<button type="button" onclick="add()" >&nbsp;��&nbsp;&nbsp;��&nbsp;</button>&nbsp;&nbsp;
						<button type="submit">&nbsp;��&nbsp;&nbsp;ѯ&nbsp;</button>
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
						<TH colspan="5">������Դ</TH>
					</TR>
					<TR>
						<!-- <TD>�豸���</TD> -->
						<TD class=green_title2>���ر�ʶ</TD>
						<TD class=green_title2>��������</TD>
						<TD class=green_title2>�ϼ�����</TD>
						<TD class=green_title2>����ԱID</TD>
						<TD class=green_title2>����</TD>
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
							id="actLabel">���</span><span id="cityLabel"></span>������Դ</B></TH>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right">���ر�ʶ</TD>
						<TD colspan=3><input type="hidden" name="city_id_old"> <INPUT
							TYPE="text" NAME="city_id" maxlength=20 class=bk size=20>&nbsp;<font
							color="#FF0000">*</font></TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right">��������</TD>
						<TD colspan=3><INPUT TYPE="text" NAME="city_name" maxlength=50
							class=bk size=20>&nbsp;<font color="#FF0000">*</font></TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right">�ϼ�����</TD>
						<input type="hidden" name="parent_id_old">
						<TD colspan=3><%=strCityList%>&nbsp;<font color="#FF0000">*</font>
						</TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right">����ԱID</TD>
						<TD colspan=3><INPUT TYPE="text" NAME="staff_id" maxlength=30 readonly="readonly"
							class=bk size=20 value="<%=user.getAccount()%>"></TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right">��ע</TD>
						<TD colspan=3><INPUT TYPE="text" NAME="remark" maxlength=100
							class=bk size=50></TD>
					</TR>
					<TR>
						<TD colspan="4" align="right" class=green_foot>
							<INPUT TYPE="hidden" name="action" value=""> 
							<INPUT TYPE="button" onclick="save()" value=" �� �� " class=btn>&nbsp;&nbsp; 
							<INPUT TYPE="reset" value=" �� д " class=btn>
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
