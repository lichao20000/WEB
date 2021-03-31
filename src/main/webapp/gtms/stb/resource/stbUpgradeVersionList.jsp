<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ include file="/timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<%@ include file="/toolbar.jsp"%>
<%@ page import="com.linkage.litms.LipossGlobals" %>
<%@taglib prefix="ms" uri="/ailk-itms-web-tags"%>

<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css"></link>
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>" type="text/css"></link>
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/jQuerySplitPage-linkage.js"/>"></script>

<script type="text/javascript">
	var instAreaName = '<%= LipossGlobals.getLipossProperty("InstArea.ShortName")%>';
	$(function() {
		parent.dyniframesize();
	});
	
	function addTemp()
	{
		var strpage = "<s:url value='/gtms/stb/resource/stbUpgradeVersionAdd.jsp'/>?operateType=add";
		window.open(strpage, "","left=20,top=20,width=900,height=650,resizable=yes,scrollbars=yes");
	}

	function showTemp(devicetype_id_old,vendorName,deviceModel,sourceDeviceTypeName,
			goalDeviceTypeName,tempName,device_model_id,belongName,hardwareversion)
	{
		var strpage = "<s:url value='/gtms/stb/resource/stbUpgradeVersionUpdate.jsp'/>?"
				+"devicetype_id_old=" + devicetype_id_old 
				+ "&vendorName=" + vendorName 
				+ "&deviceModel=" +deviceModel
				+ "&sourceDeviceTypeName=" +sourceDeviceTypeName
				+ "&goalDeviceTypeName=" +goalDeviceTypeName
				+ "&tempName=" +encodeURI(encodeURI(tempName))
				+ "&device_model_id=" +device_model_id
				+ "&belongName=" +belongName
				+ "&hardwareversion=" +hardwareversion;
		window.open(strpage,"","left=20,top=20,width=900,height=650,resizable=yes,scrollbars=yes");
	}
	
	function showTemp_hnlt(devicetype_id_old,vendorName,deviceModel,sourceDeviceTypeName,
			goalDeviceTypeName,tempName,device_model_id,belongName,hardwareversion,belong,valid)
	{
		var strpage = "<s:url value='/gtms/stb/resource/stbUpgradeVersionUpdate.jsp'/>?"
				+"devicetype_id_old=" + devicetype_id_old 
				+ "&vendorName=" + vendorName 
				+ "&deviceModel=" +deviceModel
				+ "&sourceDeviceTypeName=" +sourceDeviceTypeName
				+ "&goalDeviceTypeName=" +goalDeviceTypeName
				+ "&tempName=" +tempName
				+ "&device_model_id=" +device_model_id
				+ "&belongName=" +belongName
				+ "&hardwareversion=" +hardwareversion
				+ "&belong=" +belong
				+ "&valid=" +valid;
		window.open(strpage,"","left=20,top=20,width=900,height=650,resizable=yes,scrollbars=yes");
	}
	
	function deleteTemp(devicetypeIdOld )
	{
		if (!confirm('请确认是否删除当前升级版本！！')) {
			return false;
		}
		
		var url = "<s:url value='/gtms/stb/resource/stbUpgradeVersion!deleteUpgradeTemp.action'/>"; 
		$.post(url,{
			source_devicetypeId : devicetypeIdOld
		},function(ajax){
			if("1"== ajax){
				alert("删除成功！");
			}else{
				alert("删除失败！");
			}
			//window.location.reload();
			document.URL=location.href;
		});
	}
	
	function deleteTemp_hnlt(devicetype_id_old,devicetype_id_new,temp_id,belong)
	{
		if (!confirm('请确认是否删除当前升级版本！！')) {
			return false;
		}
		
		var url = "<s:url value='/gtms/stb/resource/stbUpgradeVersion!deleteUpgradeTemp.action'/>"; 
		$.post(url,{
			platformId:belong,
			goal_devicetypeId:devicetype_id_old,
			source_devicetypeId:devicetype_id_new,
			tempId:temp_id
		},function(ajax){
			if("1"== ajax){
				alert("删除成功！");
			}else{
				alert("删除失败！");
			}
			window.location.reload();
		});
	}
</script>

<div>
	<table id="updatetable" style="display: none">
		<tr>
			<td>blue</td>
		</tr>
	</table>
	
	<table class="listtable" width="98%" align="center">
		<caption>统计结果</caption>
		<thead>
			<tr>
				<th width="10%">厂商</th>
				<th width="12%">型号</th>
				<% if (LipossGlobals.inArea("hn_lt")||LipossGlobals.inArea("xj_dx")) { %>
					<th>硬件版本</th>
				<% } %>
				<th width="20%">原始版本</th>
				<th width="20%">目标版本</th>
				<th width="8%">软件升级类型</th>
				<% if (LipossGlobals.inArea("hn_lt")) { %>
					<th>所属平台</th>
					<th>是否生效</th>
				<% } %>
				<th width="10%">操作</th>
			</tr>
		</thead>
		<tbody>
			<s:if test="tempList != null ">
				<s:if test="tempList.size() > 0">
					<s:iterator value="tempList">
						<tr align="center">
							<td><s:property value="vendorName" /></td>
							<td><s:property value="deviceModel" /></td>
							<ms:inArea areaCode="hn_lt,xj_dx">
								<td><s:property value="hardwareversion" /></td>
							</ms:inArea>
							<td><s:property value="sourceDeviceTypeName" /></td>
							<td><s:property value="goalDeviceTypeName" /></td>
							<td><s:property value="tempName" /></td>
							<ms:inArea areaCode="hn_lt">
								<td><s:property value="belongName" /></td>
								<td><s:property value="valid" /></td>
							</ms:inArea>
							<td nowrap="nowrap">
								<% if (LipossGlobals.inArea("hn_lt")) { %>
									<input type="button" value="修改" 
										onclick="showTemp_hnlt('<s:property value="devicetype_id_old"/>',
															'<s:property value="vendorName"/>',
															'<s:property value="deviceModel"/>',
															'<s:property value="sourceDeviceTypeName"/>',
															'<s:property value="goalDeviceTypeName"/>',
															'<s:property value="tempName"/>',
															'<s:property value="device_model_id"/>',
															'<s:property value="belongName"/>',
															'<s:property value="hardwareversion"/>',
															'<s:property value="belong"/>',
															'<s:property value="valid"/>')"/>
									<input type="button" value="删除" 
										onclick="deleteTemp_hnlt('<s:property value="devicetype_id_old"/>',
															'<s:property value="devicetype_id_new"/>',
															'<s:property value="temp_id"/>',
															'<s:property value="belong"/>')" />
								<% }else{ %>
									<input type="button" value="修改" 
										onclick="showTemp('<s:property value="devicetype_id_old"/>',
															'<s:property value="vendorName"/>',
															'<s:property value="deviceModel"/>',
															'<s:property value="sourceDeviceTypeName"/>',
															'<s:property value="goalDeviceTypeName"/>',
															'<s:property value="tempName"/>',
															'<s:property value="device_model_id"/>',
															'<s:property value="belongName"/>',
															'<s:property value="hardwareversion"/>')"/>
									<input type="button" value="删除" 
										onclick="deleteTemp('<s:property value="devicetype_id_old"/>')" />
								<% } %>	
							</td>
						</tr>
					</s:iterator>
				</s:if>
				<s:else>
					<tr>
						<% if (LipossGlobals.inArea("hn_lt")) { %>
							<td colspan=9 align=left>没有查询到相关数据！</td>
						<% }else if (LipossGlobals.inArea("xj_dx")) { %>
							<td colspan=7 align=left>没有查询到相关数据！</td>
						<% }else{ %>
							<td colspan=6 align=left>没有查询到相关数据！</td>
						<% } %>	
					</tr>
				</s:else>
			</s:if>
			<s:else>
				<tr>
				<% if (LipossGlobals.inArea("hn_lt")) { %>
					<td colspan=9 align=left>没有查询到相关数据！</td>
				<% }else if (LipossGlobals.inArea("xj_dx")) { %>
					<td colspan=7 align=left>没有查询到相关数据！</td>
				<% }else{ %>
					<td colspan=6 align=left>没有查询到相关数据！</td>
				<% } %>	
				</tr>
			</s:else>
		</tbody>
		<tfoot>
			<tr>
			<% if (!LipossGlobals.inArea("hn_lt") && !LipossGlobals.inArea("jx_dx")&& !LipossGlobals.inArea("xj_dx") ) { %>
				<td align="left" height="15">
					<button onclick="javascript:addTemp();" 
						name="addTemp" style="CURSOR:hand" style="display:" > 新 增 </button>
				</td>
			<% } %>
			<% if (LipossGlobals.inArea("hn_lt")) { %>
				<td colspan="9" align="right" height="15" nowrap="nowrap">
					[ 统计总数 : <s:property value='queryCount' /> ]&nbsp;
					<lk:pages url="/gtms/stb/resource/stbUpgradeVersion!getStbUpgradeTempList.action" 
						showType="" isGoTo="true" changeNum="true" />
				</td>
			<% }else if (LipossGlobals.inArea("xj_dx")) { %>
				<td colspan="7" align="right" height="15" nowrap="nowrap">
					[ 统计总数 : <s:property value='queryCount' /> ]&nbsp;
					<lk:pages url="/gtms/stb/resource/stbUpgradeVersion!getStbUpgradeTempList.action" 
						showType="" isGoTo="true" changeNum="true" />
				</td>
			<% }else{ %>
				<% if (LipossGlobals.inArea("jx_dx")) { %>
				<td colspan="6" align="right" height="15">
				<% }else{ %>
				<td colspan="5" align="right" height="15">
				<%} %>
					[ 统计总数 : <s:property value='queryCount' /> ]&nbsp;
					<lk:pages url="/gtms/stb/resource/stbUpgradeVersion!getStbUpgradeTempList.action" 
						showType="" isGoTo="true" changeNum="true" />
				</td>
			<% } %>	
			</tr>
		</tfoot>
	</table>
</div>
