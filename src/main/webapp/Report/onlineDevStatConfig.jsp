<%@ include file="../timelater.jsp"%>
<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ include file="../head.jsp"%>
<html>
<title>在线设备统计</title>
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
				<td width="162" align="center" class="title_bigwhite">统一配置考核指标</td>
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
				<TH colspan="2">统一配置考核指标</TH>
			</tr>
			<tr bgcolor=#ffffff>
				<td class=column align=center width="30%">请选择考核点<br>
				      <input type="checkbox" name="checkbox" value="checkbox" onClick="selectAll('time_point')">全选
				</td>
				<td width="70%">
				<input type="checkbox" name="time_point" value="0" <s:property value="timeBean.time0"/>>0 点
				<input type="checkbox" name="time_point" value="1" <s:property value="timeBean.time1"/>>1 点
				<input type="checkbox" name="time_point" value="2" <s:property value="timeBean.time2"/>>2 点
				<input type="checkbox" name="time_point" value="3" <s:property value="timeBean.time3"/>>3 点
				<input type="checkbox" name="time_point" value="4" <s:property value="timeBean.time4"/>>4 点
				<input type="checkbox" name="time_point" value="5" <s:property value="timeBean.time5"/>>5 点
				<br>
				<input type="checkbox" name="time_point" value="6" <s:property value="timeBean.time6"/>>6 点
				<input type="checkbox" name="time_point" value="7" <s:property value="timeBean.time7"/>>7 点
				<input type="checkbox" name="time_point" value="8" <s:property value="timeBean.time8"/>>8 点
				<input type="checkbox" name="time_point" value="9" <s:property value="timeBean.time9"/>>9 点
				<input type="checkbox" name="time_point" value="10" <s:property value="timeBean.time10"/>>10点
				<input type="checkbox" name="time_point" value="11" <s:property value="timeBean.time11"/>>11点
				<br>
				<input type="checkbox" name="time_point" value="12" <s:property value="timeBean.time12"/>>12点
				<input type="checkbox" name="time_point" value="13" <s:property value="timeBean.time13"/>>13点
				<input type="checkbox" name="time_point" value="14" <s:property value="timeBean.time14"/>>14点
				<input type="checkbox" name="time_point" value="15" <s:property value="timeBean.time15"/>>15点
				<input type="checkbox" name="time_point" value="16" <s:property value="timeBean.time16"/>>16点
				<input type="checkbox" name="time_point" value="17" <s:property value="timeBean.time17"/>>17点
				<br>
				<input type="checkbox" name="time_point" value="18" <s:property value="timeBean.time18"/>>18点
				<input type="checkbox" name="time_point" value="19" <s:property value="timeBean.time19"/>>19点
				<input type="checkbox" name="time_point" value="20" <s:property value="timeBean.time20"/>>20点
				<input type="checkbox" name="time_point" value="21" <s:property value="timeBean.time21"/>>21点
				<input type="checkbox" name="time_point" value="22" <s:property value="timeBean.time22"/>>22点
				<input type="checkbox" name="time_point" value="23" <s:property value="timeBean.time23"/>>23点
				</td>
			</tr>
			<tr bgcolor=#ffffff>
				<td class=column align=center>请选择属地<br>
                    <input type="checkbox" name="checkbox2" value="checkbox" onClick="selectAll('city_id')">全选
				</td>
				<td>
					<s:iterator value="cityList">
					<input type="checkbox" name="city_id" value="<s:property value="city_id"/>" <s:property value="checked"/>><s:property value="city_name"/>
					</s:iterator>
				</td>
			</tr>
			<tr bgcolor=#ffffff>
				<td class=column colspan="2" align=right>
					<input type="button" value=" 定 制 " onclick="checkForm()"/>	
					<input type="reset" value=" 重 置 " />
				</td>
			</tr>
			<tr>
				<td  colspan="2" height="20">				</td>
			</tr>
			<tr>
				<td class=column colspan="2"><span class="STYLE1">备注：</span><br> 
			      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;*<span class="STYLE1">考核点配置</span>			      <br>
			       &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;*&nbsp;&nbsp;&nbsp;此配置应用于对用户指标的具体考核，用于生成具体考核指标的数据。<br>
			       &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;*&nbsp;&nbsp;&nbsp;数据经整合统计后为对各项指标进行考核的达标情况报表提供参考。是用户的“关键”考核点。 <br>
			       &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;*<span class="STYLE1">巡检点配置</span>			      <br>
			       &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;*&nbsp;&nbsp;&nbsp;此配置应用于对用户指标的日常考核，主要以日志的形式出现在用户健康档案等模块的详细信息中。
			      <br>
	            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;*&nbsp;&nbsp;&nbsp;是用户的“日常”考核点。 </td>
		  </tr>
		</table>
		</td>
	</tr>
</TABLE>
</form>
<%@ include file="../foot.jsp"%>
</body>
</html>