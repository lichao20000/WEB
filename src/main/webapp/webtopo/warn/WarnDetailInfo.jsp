<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>�澯����</title>
<%
	/**
	 * WebTopoʵʱ�澯�Ƹ澯������ҳ��
	 * <li>REQ: GZDX-REQ-20080402-ZYX-001
	 * <li>REQ: HBDX-REQ-20080220-WANGBING-001
	 *
	 * @author	�ι���
	 * @version 1.0
	 * @since	2008-4-14
	 * @category	WebTopo/ʵʱ�澯��/�澯����
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
				alert("��Ϣ����ɹ�");
			}else{
				alert("��Ϣ����ʧ��");
			}
		}
	}
	// ȷ�ϸ澯
	function ackAlarm(){
		if(!confirm("�Ƿ�Ҫȷ�ϸ澯?"))
				return;
		opener.ackAlarm('<s:property value="serialNo"/>','<s:property value="gatherId"/>','<s:property value="createTime" />');
		document.frm.submit();
 		window.close();
	}
	// ����澯
	function clearAlarm(){
		if(!confirm("�Ƿ�Ҫ����澯?"))
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
		<td width="162" align="center" class="title_bigwhite">�澯����</td>
	</tr>
</table>
<table id="myTable" width="90%" border=0 align="center" cellpadding="1"
	cellspacing="1" class="table" bgcolor="#000000">
	<tr>
		<th colspan="4">�澯����</th>
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
		<td class="column text" nowrap>���к�</td>
		<td class="column4"><s:property value="#rt.serialno " /></td>
		<td class="column text" nowrap>����������</td>
		<td class="column4"><s:property value="#rt.creatortype " /></td>
	</tr>
	<tr>
		<td class="column text" nowrap>����������</td>
		<td class="column4"><s:property value="#rt.creatorname " /></td>
		<td class="column text" nowrap>����ʱ��</td>
		<td class="column4"><s:property value="#rt.createtime " /></td>
	</tr>
	<tr>
		<td class="column text" nowrap>�澯����</td>
		<td class="column4"><s:property value="#rt.displaytitle " /></td>
		<td class="column text" nowrap>�澯�ȼ�</td>
		<td class="column4"><s:property value="#rt.severity " /></td>

	</tr>
	<tr>
		<td class="column text" nowrap>�澯����</td>
		<td colspan="3" class="column4"><s:property value="#rt.displaystring " /></td>
	</tr>
	<tr>
		<td class="column text" nowrap>�豸�ͺ�</td>
		<td class="column4"><s:property value="#rt.device_model " /></td>
		<td class="column text" nowrap>�豸����</td>
		<td class="column4"><s:property value="#rt.sourcename " /></td>
	</tr>
	<tr>
		<td class="column text" nowrap>�豸����</td>
		<td class="column4"><s:property value="#rt.device_type " /></td>
		<td class="column text" nowrap>�豸IP</td>
		<td class="column4"><s:property value="#rt.sourceip " /></td>
	</tr>
	<tr>
		<td class="column text" nowrap>�豸��ϸ��ַ</td>
		<td colspan="3" class="column4"><s:property value="#rt.device_addr " /></td>
	</tr>
	<tr>
		<td class="column text" nowrap>���ش���</td>
		<td class="column4"><s:property value="#rt.filttimes " /></td>
		<td class="column text" nowrap>���һ�θ澯����ʱ��</td>
		<td class="column4"><s:property value="#rt.lastfiltercreatetime" /></td>
	</tr>
	<tr>
		<td class="column text" nowrap>������</td>
		<td class="column4"><s:property value="#rt.operaccount " /></td>
		<td class="column text" nowrap>�ͻ�����</td>
		<td class="column4"><s:property value="#rt.customer_name " /></td>
	</tr>
	<tr>
		<td class="column text" nowrap>ȷ��״̬</td>
		<td class="column4"><s:property value="#rt.activestatus " /></td>
		<td class="column text" nowrap>ȷ��ʱ��</td>
		<td class="column4"><s:property value="#rt.acktime " /></td>
	</tr>
	<tr>
		<td class="column text" nowrap>���״̬</td>
		<td class="column4"><s:property value="#rt.clearstatus " /></td>
		<td class="column text" nowrap>���ʱ��</td>
		<td class="column4"><s:property value="#rt.cleartime " /></td>
	</tr>
	<tr>
		<td class="column text" nowrap>�澯��ʱ</td>
		<td class="column4"><s:property value="#rt.spendtimestr " /></td>
		<td class="column text" nowrap></td>
		<td class="column4"></td>
	</tr>

	<!-- SysLog -->
	<tr>
		<td class="column text" nowrap rowspan="<s:property value="sysLogList.size()+2"/>">SysLog�ļ�</td>
		<td class="column text">�ļ�����</td>
		<td class="column text">�ļ���С(Byte)</td>
		<td class="column text">����ʱ��</td>
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
		<td class="column4" colspan="3">��ǰû��SysLog�ļ�</td>
      	</s:if>
      	<s:else>
      	<td class="column4" colspan="3">�� <font color="red"><s:property value="sysLogList.size()"/></font> ��SysLog�ļ�</td>
      	</s:else>
	</tr>
	<!-- SysLog -->

	<tr>
		<td class="column text" nowrap>�澯����˵��</td>
		<td colspan="3" class="column4">
			<textarea name="remark" cols="60" rows="2"><s:property value="#rt.remark " /></textarea>
		</td>
	</tr>
	<tr>
		<td class="column text" nowrap>����ԭ��</td>
		<td colspan="3" class="column4">
			<textarea name="warnReason" cols="60" rows="2" ><s:property value="#rt.warnreason " /></textarea>
		</td>
	</tr>
	<tr>
		<td class="column text" nowrap>�������</td>
		<td colspan="3" class="column4">
			<textarea name="warnResove" cols="60" rows="2" ><s:property value="#rt.warnresove " /></textarea>
		</td>
	</tr>
	</s:iterator>
	<tr>
		<td colspan="4" class="foot" align="right">
			<s:if test="resultList==null || resultList.size() == 0"><font color="red">�޸ø澯������Ϣ</font></s:if>
			<s:else>
			<input type="submit" class="jianbian" value="��  ��">&nbsp;
			<s:if test="buttonflg == null ||( buttonflg != null && buttonflg != 'false')">
			<input type="button" class="jianbian" onclick="ackAlarm();" value="ȷ�ϸ澯">&nbsp;
			<input type="button" class="jianbian" onclick="clearAlarm();" value="����澯">
			</s:if>
			</s:else>
		</td>
	</tr>
</table>
</form>
</body>
</html>
