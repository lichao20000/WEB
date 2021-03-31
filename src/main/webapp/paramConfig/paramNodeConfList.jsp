<%@ page contentType="text/html; charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>节点参数列表</title>
<style type="text/css">
	.col{
		width: 25%;
		font-size: 14px;
		background-color: #F4F4FF;
	}
	.col1{
		width: 25%;
		font-size: 14px;
	}
</style>
</head>
<body>
<form name="selectForm" method="post" action="<s:url value='/gtms/config/paramNodeCfg!doConfigAll.action'/>" target="dataForm">
	<input type="hidden" name="devSn" value="">
	<input type="hidden" name="devId" value="">
	<input type="hidden" name="cityId" value="">
	<input type="hidden" name="onlineStatus" value="">
	<input type="hidden" name="vendorId" value="">
	<input type="hidden" name="deviceModelId" value="">
	<input type="hidden" name="devicetypeId" value="">
	<input type="hidden" name="bindType" value="">
	<input type="hidden" name="devSerialnumber" value="">
	<input type="hidden" name="conf_type_id" value="">
	<input type="hidden" name="do_type" value="">
	<input type="hidden" name="taskId" value="" >
	<input type="hidden" name="currTime" value="" >
	<table width="100%" border="1" cellspacing="0" cellpadding="0"  style="line-height: 22px;">
		<tr bgcolor="#FFFFFF">
			<td colspan="1" class="col">选择</td>
			<td colspan="2" class="col">语音参数</td>
			<td colspan="3" class="col">参数修改</td>
		</tr>
		<s:if test="paramNodeList.size()>0">
			 <s:iterator value="paramNodeList">
				<tr bgcolor="#FFFFFF">
					<td colspan="1" class="col">
							<input type="checkbox" name="select_checkbox" value="<s:property value='node_id'/>" />
					</td>
					<td colspan="2" class="col1">
						<s:property value="node_name"/>
					</td>
					<td colspan="3" class="col1">
						<s:if test="input_type!=1">
							<select name="select_param_name" id="<s:property value='node_id'/>">
							</select>
							<script type="text/javascript">
								$(function(){
									var remark = "<s:property value='remark'/>";
									var nodeId = "<s:property value='node_id'/>";
									var remarkArr = remark.split("#");
									var value = "";
									var type = "";
									for(var i = 0;i < remarkArr.length;i++){
										var index = remarkArr[i].indexOf(":");
										value = remarkArr[i].substring(0,index);
										type = remarkArr[i].substring(index+1,remarkArr[i].length);
										$("select[@id='" + nodeId + "']").append("<option value='"+ nodeId + "#" +type + "'>" + value + "</option>");
									}
								});
							</script>
						</s:if>
						<s:else>
							<input id="<s:property value='node_id'/>" type="text" name="input_param_name" value="0" >
							<s:property value="remark"/>
						</s:else>
					</td>
				</tr>
			</s:iterator>
		</s:if>
		<s:else>
			<tr>
				<td colspan=6 >
					<font color="red">没有满足条件的数据！</font>
				</td>
			</tr>
		</s:else>
	</table>
	</form>
</body>
</html>