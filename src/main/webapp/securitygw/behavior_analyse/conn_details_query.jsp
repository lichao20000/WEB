<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<title>江苏电信网管专家系统</title>
<link href="<s:url value="/model_vip/css/liulu.css"/>" rel="stylesheet" type="text/css">
<link href="<s:url value="/model_vip/css/tablecss.css"/>" rel="stylesheet" type="text/css">
<link href="<s:url value="/model_vip/css/css_blue.css"/>" rel="stylesheet" type="text/css">
<link href="../css/menu_list.css" rel="stylesheet" type="text/css">
<style type="text/css">
<!--
@import url(../css/css_ico.css);
body {
	margin-left: 0px;
	margin-top: 0px;
	margin-right: 0px;
	margin-bottom: 0px;
}
a{margin-bottom:-1px;}
-->
</style>

<script type="text/javascript"
	src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/edittable.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/jQuerySplitPage-linkage.js"/>"></script>
<script type="text/javascript" src="<s:url value="/securitygw/js/Calendar.js"/>"></script>
<script language="JavaScript" type="text/JavaScript">
<!--
 $(function(){
               var curPage = "<s:property value="curPage_splitPage"/>";
               var num = "<s:property value="num_splitPage"/>";
               var maxPage = "<s:property value="maxPage_splitPage"/>";
               var paramList = "<s:property value="paramList_splitPage"/>";

            //初始化翻页按钮
            $.initPage(
              "<s:url value="/securitygw/PBDetailAction.action"/>",
              "#toolbar",
              curPage,
              num,
              maxPage,
              paramList);
       });
//-->
</script>
<link href="<s:url value="/model_vip/css/tablecss.css"/>" rel="stylesheet" type="text/css">
<link href="<s:url value="/model_vip/css/menu_list.css"/>" rel="stylesheet" type="text/css">
<style type="text/css">
<!--
.style5 {color: #FF0000}
-->
</style>
</head>

<body onclick="parent.document_click()">
<form name="frm" onSubmit="return checkform();" action="<s:url value="/securitygw/PBDetailAction.action"/>" method="post">
	<input type="hidden" name="deviceid" value="<s:property value="deviceid"/>"/>
<br>
<table width="95%"  border="1" align="center" cellpadding="0" cellspacing="0" class="table">
  <tr  class="tab_title">
    <td colspan="4" class="text_white"><s:property value="device_name"/> --- <s:property value="loopback_ip"/></td>
  </tr>
  <tr>
    <td class="tr_yellow">请选择时间段</td>
    <td class="tr_white">
      <input name="start_time" type="text" readonly="true" class="form_kuang" size="18" value="<s:property value="start_time"/>"/>
      <input name="button_st" type="button" class="jianbian" onClick="showCalendar('all')" value="▼"></td>
	 <td class="tr_white" colspan="2">到
      <input name="end_time" type="text" readonly="true" class="form_kuang" value="<s:property value="end_time"/>" size="18"/>
      <input name="button_et" type="button" class="jianbian" onClick="showCalendar('all')" value="▼"></td>
  </tr>
  <%--
  <tr>
    <td class="tr_yellow">设备ID</td>
    <td class="tr_white" colspan="3">
      <input name="deviceid" type="text" class="form_kuang" size="18" value="<s:property value="deviceid"/>"/></td>
  </tr>--%>
  <tr>
    <td class="tr_yellow">协议类型</td>
    <td class="tr_white" colspan="3">
      <select name="prot_type" class="form_kuang">
		<option value="" selected>(全部)</option>
		<option value="0">http</option>
		<option value="1">ftp</option>
		<option value="2">smtp</option>
		<option value="3">pop3</option>
	  </select></td>
  </tr>
  <tr>
    <td class="tr_yellow" width="150">源用户IP</td>
    <td class="tr_white" width="150">
      <input name="sip" type="text" class="form_kuang" size="18" value="<s:property value="sip"/>"/></td>
    <td class="tr_yellow" width="150">目标用户IP</td>
    <td class="tr_white">
      <input name="tip" type="text" class="form_kuang" size="18" value="<s:property value="tip"/>"/></td>
  </tr>
  <tr>
    <td class="tr_yellow">源端口</td>
    <td class="tr_white">
      <input name="sp" type="text" class="form_kuang" size="18" value="<s:property value="sp"/>"/></td>
    <td class="tr_yellow">目标端口</td>
    <td class="tr_white">
      <input name="tp" type="text" class="form_kuang" size="18" value="<s:property value="tp"/>"/></td>
  </tr>
  <%--
  <tr>
    <td class="tr_yellow">源Mac</td>
    <td class="tr_white">
      <input name="sm" type="text" class="form_kuang" size="18" value="<s:property value="sm"/>"/></td>
    <td class="tr_yellow">目标Mac</td>
    <td class="tr_white">
      <input name="tm" type="text" class="form_kuang" size="18" value="<s:property value="tm"/>"/></td>
  </tr>--%>
  <tr>
    <td colspan="4" class="tr_glory"><div align="right"><span class="text">
        </span><span class="text">
        <input name="Submit" type="submit" class="jianbian" value="查 询">&nbsp;&nbsp;
    </span></div></td>
  </tr>
</table>
</form>
<br>
<table width="95%"  border="1" align="center" cellpadding="0" cellspacing="0" class="table">
  <tr class="tab_title">
    <td colspan="12" class="text"><span class="title_white">用户上网详情</span></td>
  </tr>
  <tr class="trOver_blue">

    <td class="text">源用户IP</td>
    <td class="text">源端口</td>
    <td class="text">目标用户IP</td>
    <td class="text">目标端口</td>
	<%--
    <td class="text">设备id</td>
    <td class="text">源Mac</td>
    <td class="text">目标Mac</td>--%>
    <td class="text">协议类型</td>
    <td class="text">次数</td>
    <td class="text">流量(Byte)</td>
    <td class="text">开始时间</td>
    <td class="text">结束时间</td>
  </tr>
  <s:iterator var="map" value="connDetails" status="st">
				<tr  class="<s:property value="#st.odd==true?'tr_white':'tr_glory'"/>" onmouseover="className='trOver_blue'"
							onmouseout="className='<s:property value="#st.odd==true?'tr_white':'tr_glory'"/>'">
					<td class="text"><s:property value="#map.sip" /></td>
					<td class="text"><s:property value="#map.sp" /></td>
					<td class="text"><s:property value="#map.tip" /></td>
					<td class="text"><s:property value="#map.tp" /></td>
					<%--
					<td class="text"><s:property value="#map.deviceid" /></td>
					<td class="text"><s:property value="#map.sm" /></td>
					<td class="text"><s:property value="#map.tm" /></td>--%>
					<td class="text"><s:property value="#map.type == 0 ? 'http' : (#map.type == 1 ? 'ftp' : (#map.type == 2 ? 'smtp' : (#map.type == 3 ? pop3 : 'unkown'))) " /></td>
					<td class="text"><s:property value="#map.times" /></td>
					<td class="text"><s:property value="#map.flux" /></td>
					<td class="text"><s:property value="#map.stime" /></td>
					<td class="text"><s:property value="#map.etime" /></td>
				</tr>
			</s:iterator>
</table>
<div id="toolbar" width="98%" align="right" style="margin-right: 10px; margin-top: 2px;"></div>
<table width="95%"  border="1" align="center" cellpadding="0" cellspacing="0" class="table">
</table>

</body>
</html>
<SCRIPT LANGUAGE="JavaScript">
<!--
init();
function init() {
	document.forms[0].prot_type.value = "<s:property value="prot_type"/>";
}
function checkform() {
	with (document.forms[0]) {
		if (start_time.value > end_time.value) {
			alert("开始时间不能大于结束时间！");
			return false;
		}
	}
	return true;
}
//-->
</SCRIPT>
