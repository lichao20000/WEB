<%@ include file="../timelater.jsp"%>
<%@ include file="../head.jsp"%>
<%@ include file="../toolbar.jsp"%>
<%@ page import="com.linkage.litms.common.filter.SelectCityFilter"%>
<%@page import="com.linkage.litms.common.util.DateTimeUtil"%>
<%@ page contentType="text/html;charset=gbk"%>
<%
request.setCharacterEncoding("GBK");
//��ʼ����ǰʱ�� 
DateTimeUtil dateUtil = new DateTimeUtil();   
String time = dateUtil.getTime();
String today = dateUtil.getYear()+"-"+dateUtil.getMonth()+"-"+dateUtil.getDay();
//�豸����
String city_id = curUser.getCityId();
SelectCityFilter City = new SelectCityFilter(request);
String strCityList = City.getSelfAndNextLayer(false, city_id, "");
%>

<SCRIPT LANGUAGE="JavaScript">
<!--
var t_type = "day";
var obj_type = {"day":"��","week":"��","month":"��","year":"��"};
function changeTimeType(_value){
	document.all("start_day").value = "";
	document.all("spanStr").innerHTML = obj_type[_value];
	if(_value == "week")
		t_type = "day";
	else
		t_type = _value;
}
function checkForm(){
	if(document.all("start_day").value == ""){
		alert("��ѡ��ʱ��!");
		return false;
	}
	return true;
}
//-->
</SCRIPT>
<link href="../css/css_green.css" rel="stylesheet" type="text/css">
<FORM name="frm" action="OnlineNetStateResult.jsp" onsubmit="return checkForm()">
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
<TR><TD HEIGHT=20>&nbsp;</TD></TR>
<TR><TD>
	<TABLE width="95%" border=0 cellspacing=0 cellpadding=0 align="center">
		<TR>
			<TD>
				<TABLE width="100%" height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
					<TR>
						<TD width="164" align="center" class="title_bigwhite">
							������Ԫͳ��
						</TD>
					</TR>
				</TABLE>
			</TD>
		</TR>
		
		<TR>
			<TD bgcolor=#999999>
			<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
				<TR><TH colspan="4">������Ԫͳ�Ʋ�ѯ(<span id="spanStr">��</span>)</TH></TR>
				<tr bgcolor="#FFFFFF">
	               <td align="right" width="15%">
	                    <select name="time_type" onchange="changeTimeType(this.value)">
							<option value="day">��</option>
							<option value="week">��</option>
							<option value="month">��</option>
							<option value="year">��</option>
	               </td>
	               <td>
	                    <input type="text" name="start_day" class=bk value="<%=today%>" size=10 readonly>
	                    <input type="button" value="��" class=jianbian onClick="showCalendar(t_type)">
	               </td>

					<td align="right" width="20%">
							����
					 </td>
					<TD class="">
						<%=strCityList%>
					</TD>
	            </tr>
				<TR bgcolor="#ffffff"><TD colspan="4" align="right" class="green_foot">
					<input type="submit" value=" �� ѯ " class="btn"></TD>
				</TR>										
			</TABLE>
			</TD>
		</TR>	
	</TABLE>
</TD>
</TR>
</TABLE>
</FORM>
<%@ include file="../foot.jsp"%>