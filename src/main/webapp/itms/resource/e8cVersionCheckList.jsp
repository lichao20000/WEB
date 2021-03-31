<%@page import="com.linkage.litms.LipossGlobals"%>
<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>e8-c规范版本查询列表</title>
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
	
	function getDetailInfo(devicetype_id,specId){
		$("#divData",parent.document).show();
	    var url = "<s:url value='/itms/resource/E8cVersionQuery!getDetailInfo.action'/>";
		$.post(url,{
			deviceTypeId:devicetype_id,
			detailSpecId:specId
		},function(ajax){
			$("#wall",parent.document).show();
			$("#QueryData",parent.document).show();
			$("#QueryData",parent.document).html("");
			$("#QueryData",parent.document).append(ajax);
		});
	}
	function checkDevice(deviceTypeId,is_recent_version){
		if(is_recent_version == "1"){
			alert("该版本文件已审核，请重新操作!");
			return;
		}else{
			if (confirm("是否审核该设备版本？")){
				var url = "<s:url value='/itms/resource/E8cVersionQuery!updateIsCheck.action'/>";
				$.post(url,{
					deviceTypeId:deviceTypeId
				},function(ajax){
					var result = parseInt(ajax);
					if(result > 0){
						alert("审核设备成功");
						// 普通方式提交
						var form = window.parent.document.getElementById("frm");
						form.action = "<s:url value='/itms/resource/E8cVersionQuery!getE8cVersionList.action'/>?operType=2";
						form.submit();
					}else{
						alert("审核设备失败");
					}
				});
			}
		}
	}
</script>
</head>
<body style="font-size: 14px;">
	<form method="post" action="/itms/gtms/resource/fileDownLoad.jsp" id="mainForm" name="mainForm" target="dataForm">
		<input type="hidden" name="fileName" id="fileName" />
		<input type="hidden" name="filePath" id="filepath" />
		<div class="content">
			<div>
				<table width="100%" border="0" cellspacing="0" cellpadding="0" class="it_table">
					<tr>
					   <th>设备厂商</th>
					   <th>设备型号</th>
					   <th>硬件版本</th>
					   <th>软件版本</th>
					   <th>设备类型</th>
					   <th>上行方式</th>
					   <th>是否最新版本</th>
					   <th>是否审核</th>
					   <th width="280">操作</th>
					</tr>
					<s:if test="list!=null">
						<s:if test="list.size()>0">
							<s:iterator value="list">
								<tr>
									<td><s:property value="vendor_name"/></td>
									<td><s:property value="device_model"/></td>
									<td><s:property value="hardware_version"/></td>
									<td><s:property value="software_version"/></td>
									<td><s:property value="rela_dev_type_name"/></td>
									<td><s:property value="access_type"/></td>
									<td>
										<input type="hidden" name="is_normal" value="<s:property value = 'is_normal' />">
										<s:if test="is_normal==1">
											是
										</s:if> 
										<s:if test="is_normal!=1">
											<font color='red'>否</font>
										</s:if>
									</td>
									<td>
										<input type="hidden" name="is_check" value="<s:property value = 'is_check' />">
										<input type="hidden" name="is_recent_version" value="<s:property value = 'is_recent_version' />">
										<s:if test="is_recent_version==1">
											已审核
										</s:if> 
										<%-- <s:if test="is_check==0">
											<font color='red'>未审核</font>
										</s:if>
										<s:if test="is_check==-1">
											<font color='red'>未测试</font>
										</s:if> --%>
										<s:if test="is_recent_version!=1">
											<font color='red'>未审核</font>
										</s:if>
									</td>
									<td>
										<a style="width: 90px;" href="javascript:checkDevice('<s:property value="devicetype_id" />','<s:property value="is_recent_version" />')">审核</a>
										<a class="itta_more" style="width: 90px;" href="javascript:getDetailInfo('<s:property value="devicetype_id" />',
											'<s:property value="spec_id" />')">详细信息</a>
									</td>
								</tr>
							</s:iterator>
						</s:if>
						<s:else>
						<tr>
							<td colspan="9" align="left">
								<font color="red">没有相关规范版本信息!</font>
							</td>
						</tr>
						</s:else>
					</s:if>
					<s:else>
						<tr>
							<td colspan="9" align="left">
								<font color="red">系统没有匹配到相应信息!</font>
							</td>
						</tr>
					</s:else>
					<tr>
						<td colspan="9" align="right">
							<lk:pages
								url="/itms/resource/E8cVersionQuery!getE8cVersionList.action"
								styleClass="" showType="" isGoTo="true" changeNum="true" />
						</td>
					</tr>
					<tr STYLE="display: none">
						<td colspan="2">
							<iframe id="childFrm" name="childFrm" src=""></iframe>
						</td>
					</tr>
				</table>
				<div id="data"  id="dataForm" name="dataForm">
					<iframe id="dataForm" name="dataForm" height="38px;" frameborder="0" scrolling="no" width="100%" src=""></iframe>
				</div>
			</div>
		</div>
	</form>
</body>
<!-- <div id="wall" class="wall" style="width:2500;height:590; margin: -1000;display: none" ></div> -->
</html>