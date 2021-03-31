<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>告警详情</title>
<%
	/**
	 * WebTopo实时告警牌告警规则定义页面
	 * <li>REQ: GZDX-REQ-20080402-ZYX-001
	 * <li>REQ: HBDX-REQ-20080220-WANGBING-001
	 *
	 * @author	段光锐
	 * @version 1.0
	 * @since	2008-4-14
	 * @category	WebTopo/实时告警牌/告警规则
	 *
	 */
%>
<link href="<s:url value="/css/css_green.css"/>" rel="stylesheet" type="text/css">
<!--
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
-->
<script language="javascript" type="text/javascript">
<!--
	function init(){
		var flag = <s:property value='flag'/>;
		var isSave = <s:property value='isSave'/>;
		if(isSave){
			if(flag){
				alert("信息保存成功");
			}else{
				alert("信息保存失败");
			}
		}
	}
	// 确认告警
	function ackAlarm(){
		if(!confirm("是否要确认告警?"))
				return;
		opener.ackAlarm('<s:property value="serialNo"/>','<s:property value="gatherId"/>','<s:property value="createTime" />');
		document.frm.submit();
 		window.close();
	}
	// 清除告警
	function clearAlarm(){
		if(!confirm("是否要清除告警?"))
				return;
		opener.clearAlarm('<s:property value="serialNo"/>','<s:property value="gatherId"/>','<s:property value="createTime" />');
		document.frm.submit();
 		window.close();
	}
-->
</script>
</head>
<body id="myBody" onload="init();">
<form name="frm" method="post" action="<s:url value="/webtopo/warnDetailInfo.action"/>">
<br>
<table width="90%" height="30" border=0 align="center" cellpadding="0"
	cellspacing="0" class="green_gargtd">
	<tr>
		<td width="162" align="center" class="title_bigwhite">告警详情</td>
	</tr>
</table>
<table id="myTable" width="90%" border=0 align="center" cellpadding="1"
	cellspacing="1" class="table" bgcolor="#000000">
	<tr>
		<th colspan="4">告警详情</th>
	</tr>
	<s:iterator value="resultList" var="rt" status="status">
	<input type="hidden" name="serialNo" value="<s:property value="#rt.serialno " />">
	<input type="hidden" name="gatherId" value="<s:property value="gatherId" />">
	<input type="hidden" name="subject" value="<s:property value="#rt.displaytitle " />">
	<input type="hidden" name="content" value="<s:property value="#rt.displaystring " />">
	<input type="hidden" name="createTime" value="<s:property value="createTime " />">
	<input type="hidden" name="deviceType" value="<s:property value="#rt.devicetype " />">
	<input type="hidden" name="sourceIP" value="<s:property value="#rt.sourceip " />">
	<input type="hidden" name="createType" value="<s:property value="#rt.creatortypeoriginal " />">
	<input type="hidden" name="sourceName" value="<s:property value="#rt.sourcename " />">
	<input type="hidden" name="firstTime" value="<s:property value="#rt.firsttime " />">
	<input type="hidden" name="buttonflg" value="<s:property value="buttonflg " />">

	<tr>
		<td class="column text" nowrap>序列号</td>
		<td class="column4"><s:property value="#rt.serialno " /></td>
		<td class="column text" nowrap>创建者类型</td>
		<td class="column4"><s:property value="#rt.creatortype " /></td>
	</tr>
	<tr>
		<td class="column text" nowrap>创建者名称</td>
		<td class="column4"><s:property value="#rt.creatorname " /></td>
		<td class="column text" nowrap>创建时间</td>
		<td class="column4"><s:property value="#rt.createtime " /></td>
	</tr>
	<tr>
		<td class="column text" nowrap>告警主题</td>
		<td class="column4"><s:property value="#rt.displaytitle " /></td>
		<td class="column text" nowrap>告警等级</td>
		<td class="column4"><s:property value="#rt.severity " /></td>

	</tr>
	<tr>
		<td class="column text" nowrap>告警内容</td>
		<td colspan="3" class="column4"><s:property value="#rt.displaystring " /></td>
	</tr>
	<tr>
		<td class="column text" nowrap>设备型号</td>
		<td class="column4"><s:property value="#rt.device_model " /></td>
		<td class="column text" nowrap>设备名称</td>
		<td class="column4"><s:property value="#rt.sourcename " /></td>
	</tr>
	<tr>
		<td class="column text" nowrap>设备类型</td>
		<td class="column4"><s:property value="#rt.device_type " /></td>
		<td class="column text" nowrap>设备IP</td>
		<td class="column4"><s:property value="#rt.sourceip " /></td>
	</tr>
	<tr>
		<td class="column text" nowrap>设备详细地址</td>
		<td colspan="3" class="column4"><s:property value="#rt.device_addr " /></td>
	</tr>
	<tr>
		<td class="column text" nowrap>排重次数</td>
		<td class="column4"><s:property value="#rt.filttimes " /></td>
		<td class="column text" nowrap>最后一次告警排重时间</td>
		<td class="column4"><s:property value="#rt.lastfiltercreatetime" /></td>
	</tr>
	<tr>
		<td class="column text" nowrap>操作人</td>
		<td class="column4"><s:property value="#rt.operaccount " /></td>
		<td class="column text" nowrap>客户名称</td>
		<td class="column4"><s:property value="#rt.customer_name " /></td>
	</tr>
	<tr>
		<td class="column text" nowrap>确认状态</td>
		<td class="column4"><s:property value="#rt.activestatus " /></td>
		<td class="column text" nowrap>确认时间</td>
		<td class="column4"><s:property value="#rt.acktime " /></td>
	</tr>
	<tr>
		<td class="column text" nowrap>清除状态</td>
		<td class="column4"><s:property value="#rt.clearstatus " /></td>
		<td class="column text" nowrap>清除时间</td>
		<td class="column4"><s:property value="#rt.cleartime " /></td>
	</tr>
	<tr>
		<td class="column text" nowrap>告警历时</td>
		<td class="column4"><s:property value="#rt.spendtimestr " /></td>
		<td class="column text" nowrap></td>
		<td class="column4"></td>
	</tr>

	<!-- SysLog -->
	<tr>
		<td class="column text" nowrap rowspan="<s:property value="sysLogList.size()+2"/>">SysLog文件</td>
		<td class="column text">文件名称</td>
		<td class="column text">文件大小(Byte)</td>
		<td class="column text">创建时间</td>
	</tr>
	<s:iterator value="sysLogList" var="syslog" status="status">
	<tr>
		<td class="column4">
			<a href="<s:property value="#syslog.URL " />"
			 target="_blank">
				<s:property value="#syslog.file_name " />
			</a>
		</td>
		<td class="column4"><s:property value="#syslog.file_size " /></td>
		<td class="column4"><s:property value="#syslog.time " /></td>
	</tr>
	</s:iterator>
	<tr>
		<s:if test="sysLogList==null||sysLogList.size()==0">
		<td class="column4" colspan="3">当前没有SysLog文件</td>
      	</s:if>
      	<s:else>
      	<td class="column4" colspan="3">共 <font color="red"><s:property value="sysLogList.size()"/></font> 个SysLog文件</td>
      	</s:else>
	</tr>
	<!-- SysLog -->

	<tr>
		<td class="column text" nowrap>告警附加说明</td>
		<td colspan="3" class="column4">
			<textarea name="remark" cols="60" rows="2"><s:property value="#rt.remark " /></textarea>
		</td>
	</tr>
	<tr>
		<td class="column text" nowrap>故障原因</td>
		<td colspan="3" class="column4">
			<textarea name="warnReason" cols="60" rows="2" ><s:property value="#rt.warnreason " /></textarea>
		</td>
	</tr>
	<tr>
		<td class="column text" nowrap>解决方法</td>
		<td colspan="3" class="column4">
			<textarea name="warnResove" cols="60" rows="2" ><s:property value="#rt.warnresove " /></textarea>
		</td>
	</tr>
	</s:iterator>
	<tr>
		<td colspan="4" class="foot" align="right">
			<s:if test="resultList==null || resultList.size() == 0"><font color="red">无该告警详情信息</font></s:if>
			<s:else>
			<input type="submit" class="jianbian" value="保  存">&nbsp;
			<s:if test="buttonflg == null ||( buttonflg != null && buttonflg != 'false')">
			<input type="button" class="jianbian" onclick="ackAlarm();" value="确认告警">&nbsp;
			<input type="button" class="jianbian" onclick="clearAlarm();" value="清除告警">
			</s:if>
			</s:else>
		</td>
	</tr>
</table>
</form>
</body>
</html>
