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
                    strData += "<TD CLASS=column2 align='center'> <A HREF=UpdateCityForm.jsp?city_id="
                            + fields.get("city_id")
                            + " onclick='return Edit(this.href);'>�༭</A> | <A HREF=CitySave.jsp?action=delete&city_id="
                            + (String) fields.get("city_id")
                            + " onclick='return delWarn();'>ɾ��</A></TD>";
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
	actLabel.innerHTML = '���';
	cityLabel.innerHTML = '';
}

function Edit(page){
	document.all("childFrm").src = page;
	return false;
}

function delWarn(){
	if(confirm("���Ҫɾ����������\n��������ɾ���Ĳ��ָܻ�������")){
		return true;
	}
	else{
		return false;
	}
}

function CheckForm(){
	var obj = document.frm;
	if(!IsNull(obj.city_id.value,'���ر���')){
		obj.city_id.focus();
		obj.city_id.select();
		return false;
	}
	else if(!IsNull(obj.city_name.value,'��������')){
		obj.city_name.focus();
		obj.city_name.select();
		return false;
	}
	/*else if(!IsNull(obj.sno.value,'ȫʡͳһ����')){
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
				������Դ
			</td>
			<td>
				<img src="../images/attention_2.gif" width="15" height="12">
				��'<font color="#FF0000">*</font>'�ı�������д��ѡ��
			</td>
			<td align="right">
				<A HREF='javascript:Add();'>����&nbsp;&nbsp;</A>
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
		<TABLE width="98%" border=0 cellspacing=0 cellpadding=0 align="center">
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
						<TD colspan=3><INPUT TYPE="text" NAME="staff_id" maxlength=30
							class=bk size=20 value="<%=user.getAccount()%>"></TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right">��ע</TD>
						<TD colspan=3><INPUT TYPE="text" NAME="remark" maxlength=100
							class=bk size=50></TD>
					</TR>
					<TR>
						<TD colspan="4" align="right" class=green_foot><INPUT TYPE="submit"
							value=" �� �� " class=btn>&nbsp;&nbsp; <INPUT TYPE="hidden"
							name="action" value="add"> <INPUT TYPE="reset" value=" �� д "
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
