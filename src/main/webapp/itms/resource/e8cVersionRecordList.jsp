<%@page import="com.linkage.litms.LipossGlobals"%>
<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>e8-c�淶�汾��ѯ�б�</title>
<link href="<s:url value="/css3/css_s.css"/>" rel="stylesheet"
	type="text/css" />
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript"
	src="../../Js/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript"
	src="<s:url value="/Js/jQuerySplitPage-linkage.js"/>"></script>

<script type="text/javascript">
	$(function() {
		parent.dyniframesize();
	});

	$(function(){
		$("input[@name='button']",parent.document).attr("disabled", false);
		$("input[@name='button']",parent.document).css("backgroundColor","#0066ff");
		$("#QueryData",parent.document).html("");
	});
</script>
</head>
<body>
	<div class="content">
		<div>
			<table width="100%" border="0" cellspacing="0" cellpadding="0" class="it_table">
				<tr>
				   <th>�豸����</th>
				   <th>�豸�ͺ�</th>
				   <th>����汾</th>
				   <th>��������</th>
				   <th>������Ա</th>
				   <th>����ʱ��</th>
				   <th>�ļ�����</th>

				</tr>
				<s:if test="rlist!=null">
					<s:if test="rlist.size()>0">
						<s:iterator value="rlist">
							<tr>
								<td><s:property value="vendor_name"/></td>
								<td><s:property value="device_model"/></td>
								<td><s:property value="software_version"/></td>
								<td><s:property value="operType"/></td>
								<td><s:property value="operUsername"/></td>
								<td><s:property value="time"/></td>
								<td><s:property value="fileName"/></td>
							</tr>
						</s:iterator>
					</s:if>
					<s:else>
					<tr>
						<td colspan="9" align="left">
							<font color="red">û����ع淶�汾��¼��Ϣ!</font>
						</td>
					</tr>
					</s:else>
				</s:if>
				<s:else>
					<tr>
						<td colspan="9" align="left">
							<font color="red">ϵͳû��ƥ�䵽��Ӧ��Ϣ!</font>
						</td>
					</tr>
				</s:else>
				<tr>
					<td colspan="9" align="right">
						<lk:pages
							url="/itms/resource/E8cVersionQuery!getFileOperRecordList.action"
							styleClass="" showType="" isGoTo="true" changeNum="true" />
					</td>
				</tr>
				<tr STYLE="display: none">
					<td colspan="2">
						<iframe id="childFrm" name="childFrm" src=""></iframe>
					</td>
				</tr>
			</table>
		</div>
	</div>
</body>
<!-- <div id="wall" class="wall" style="width:2500;height:590; margin: -1000;display: none" ></div> -->
</html>