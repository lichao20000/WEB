<%@ include file="../timelater.jsp"%>
<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ include file="../head.jsp"%>
<html>
<title>�����豸ͳ��</title>
<style type="text/css">
<!--
.STYLE1 {
	font-size: 14px;
	font-weight: bold;
}
-->
</style>
<%@ include file="../toolbar.jsp"%>
<SCRIPT LANGUAGE="JavaScript" SRC="../Js/jquery.js"></SCRIPT>
<script language="JavaScript">
<!--
function checkForm(){
	document.configForm.action = "<s:url value="/Report/onlineDevStatConfig.action"/>";
	
	document.configForm.submit();
	
}

function selectAll(elmID){
	t_obj = document.all(elmID);
	if(!t_obj) return;
	obj = event.srcElement;
	if(obj.checked){
		if(typeof(t_obj) == "object" ) {
			if(typeof(t_obj.length) != "undefined") {
				for(var i=0; i<t_obj.length; i++){
					t_obj[i].checked = true;
				}
			} else {
				t_obj.checked = true;
			}
		}
	
	}else{
		if(typeof(t_obj) == "object" ) {
			if(typeof(t_obj.length) != "undefined") {
				for(var i=0; i<t_obj.length; i++){
					t_obj[i].checked = false;
				}
			} else {
				t_obj.checked = false;
			}
		}
	}
}

//-->
</script>
<body>
<form name="configForm" actin="" method="post">
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%" align=center>
	<TR>
		<TD HEIGHT=20>&nbsp;</TD>
	</TR>
	<tr>
		<td align="center">
		<table width="95%" height="30" border="0" cellspacing="0"
			cellpadding="0" class="green_gargtd">
			<tr>
				<td width="162" align="center" class="title_bigwhite">ͳһ���ÿ���ָ��</td>
				<td>&nbsp;</td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td align="center">
		<table width="95%" border="0" cellspacing="1" cellpadding="2"
			bgcolor="#999999">
			<tr>
				<TH colspan="2">ͳһ���ÿ���ָ��</TH>
			</tr>
			<tr bgcolor=#ffffff>
				<td class=column align=center width="30%">��ѡ�񿼺˵�<br>
				      <input type="checkbox" name="checkbox" value="checkbox" onClick="selectAll('time_point')">ȫѡ
				</td>
				<td width="70%">
				<input type="checkbox" name="time_point" value="0" <s:property value="timeBean.time0"/>>0 ��
				<input type="checkbox" name="time_point" value="1" <s:property value="timeBean.time1"/>>1 ��
				<input type="checkbox" name="time_point" value="2" <s:property value="timeBean.time2"/>>2 ��
				<input type="checkbox" name="time_point" value="3" <s:property value="timeBean.time3"/>>3 ��
				<input type="checkbox" name="time_point" value="4" <s:property value="timeBean.time4"/>>4 ��
				<input type="checkbox" name="time_point" value="5" <s:property value="timeBean.time5"/>>5 ��
				<br>
				<input type="checkbox" name="time_point" value="6" <s:property value="timeBean.time6"/>>6 ��
				<input type="checkbox" name="time_point" value="7" <s:property value="timeBean.time7"/>>7 ��
				<input type="checkbox" name="time_point" value="8" <s:property value="timeBean.time8"/>>8 ��
				<input type="checkbox" name="time_point" value="9" <s:property value="timeBean.time9"/>>9 ��
				<input type="checkbox" name="time_point" value="10" <s:property value="timeBean.time10"/>>10��
				<input type="checkbox" name="time_point" value="11" <s:property value="timeBean.time11"/>>11��
				<br>
				<input type="checkbox" name="time_point" value="12" <s:property value="timeBean.time12"/>>12��
				<input type="checkbox" name="time_point" value="13" <s:property value="timeBean.time13"/>>13��
				<input type="checkbox" name="time_point" value="14" <s:property value="timeBean.time14"/>>14��
				<input type="checkbox" name="time_point" value="15" <s:property value="timeBean.time15"/>>15��
				<input type="checkbox" name="time_point" value="16" <s:property value="timeBean.time16"/>>16��
				<input type="checkbox" name="time_point" value="17" <s:property value="timeBean.time17"/>>17��
				<br>
				<input type="checkbox" name="time_point" value="18" <s:property value="timeBean.time18"/>>18��
				<input type="checkbox" name="time_point" value="19" <s:property value="timeBean.time19"/>>19��
				<input type="checkbox" name="time_point" value="20" <s:property value="timeBean.time20"/>>20��
				<input type="checkbox" name="time_point" value="21" <s:property value="timeBean.time21"/>>21��
				<input type="checkbox" name="time_point" value="22" <s:property value="timeBean.time22"/>>22��
				<input type="checkbox" name="time_point" value="23" <s:property value="timeBean.time23"/>>23��
				</td>
			</tr>
			<tr bgcolor=#ffffff>
				<td class=column align=center>��ѡ������<br>
                    <input type="checkbox" name="checkbox2" value="checkbox" onClick="selectAll('city_id')">ȫѡ
				</td>
				<td>
					<s:iterator value="cityList">
					<input type="checkbox" name="city_id" value="<s:property value="city_id"/>" <s:property value="checked"/>><s:property value="city_name"/>
					</s:iterator>
				</td>
			</tr>
			<tr bgcolor=#ffffff>
				<td class=column colspan="2" align=right>
					<input type="button" value=" �� �� " onclick="checkForm()"/>	
					<input type="reset" value=" �� �� " />
				</td>
			</tr>
			<tr>
				<td  colspan="2" height="20">				</td>
			</tr>
			<tr>
				<td class=column colspan="2"><span class="STYLE1">��ע��</span><br> 
			      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;*<span class="STYLE1">���˵�����</span>			      <br>
			       &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;*&nbsp;&nbsp;&nbsp;������Ӧ���ڶ��û�ָ��ľ��忼�ˣ��������ɾ��忼��ָ������ݡ�<br>
			       &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;*&nbsp;&nbsp;&nbsp;���ݾ�����ͳ�ƺ�Ϊ�Ը���ָ����п��˵Ĵ����������ṩ�ο������û��ġ��ؼ������˵㡣 <br>
			       &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;*<span class="STYLE1">Ѳ�������</span>			      <br>
			       &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;*&nbsp;&nbsp;&nbsp;������Ӧ���ڶ��û�ָ����ճ����ˣ���Ҫ����־����ʽ�������û�����������ģ�����ϸ��Ϣ�С�
			      <br>
	            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;*&nbsp;&nbsp;&nbsp;���û��ġ��ճ������˵㡣 </td>
		  </tr>
		</table>
		</td>
	</tr>
</TABLE>
</form>
<%@ include file="../foot.jsp"%>
</body>
</html>