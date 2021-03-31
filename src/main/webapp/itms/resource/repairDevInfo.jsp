<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ include file="/timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<%@ include file="/toolbar.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK" />
<title>终端独立档案</title>
<link href="<s:url value="/css/uploadAndParse.css"/>" rel="stylesheet"
	type="text/css"></link>
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
</head>

<body>
	<div class="it_main">
		<div class="tanchu">
			<div class="yw_tit">终端独立档案</div>
			<s:if test="devDetailMap != null ">
				<div class="content">
					<div class="itms_tit">
						<h2>终端信息</h2>
					</div>
					<table width="100%" border="0" cellspacing="0" cellpadding="0"
						class="it_table">
						<tr>
							<th>设备序列号</th>
							<th>返修厂家</th>
							<th>发往区县</th>
							<th>保内/保外</th>
							<th>设备型号</th>
							<th>硬件版本</th>
							<th>软件版本</th>
							<th>发往地市</th>
							<th>使用地区</th>
						</tr>
						<tr>
							<td><s:property value="devDetailMap.device_serialnumber" /></td>
							<td><s:property value="devDetailMap.repair_vendor" /></td>
							<td><s:property value="devDetailMap.send_city" /></td>
							<td><s:property value="devDetailMap.insurance_status" /></td>
							<td><s:property value="devDetailMap.device_model" /></td>
							<td><s:property value="devDetailMap.hardwareversion" /></td>
							<td><s:property value="devDetailMap.softwareversion" /></td>
							<td><s:property value="devDetailMap.city_id" /></td>
							<td><s:property value="devDetailMap.city_area" /></td>
						</tr>
					</table>
				</div>
				<div class="content">
					<div class="itms_tit">
						<h2>终端状态</h2>
					</div>
					<table width="100%" border="0" cellspacing="0" cellpadding="0"
						class="it_table">
						<tr>
							<th>返修出厂日期</th>
							<th>生产日期</th>
							<th>最新绑定时间</th>
							<th>终端状态</th>
							<th>最新解绑时间</th>
						</tr>
						<tr>
							<td><s:property value="devDetailMap.manufacture_date" /></td>
							<td><s:property value="devDetailMap.production_date" /></td>
							<td><s:property value="devDetailMap.recent_binddate" /></td>
							<td><s:property value="devDetailMap.bind_status" /></td>
							<td><s:property value="devDetailMap.recent_unbinddate" /></td>
						</tr>
						<tr>
							<th>3月内合格</th>
							<th>绑定LOID</th>
							<th>LOID状态</th>
							<th>增加业务</th>
							<th>到货测试</th>
						</tr>
						<tr>
							<td><s:property value="devDetailMap.qualified_status" /></td>
							<td><s:property value="devDetailMap.bind_loid" /></td>
							<td><s:property value="devDetailMap.loid_status" /></td>
							<td><s:property value="devDetailMap.add_business" /></td>
							<td><s:property value="devDetailMap.is_test" /></td>
						</tr>
					</table>
				</div>
			</s:if>
			<s:else>
				<div class="content">
					<div class="itms_tit">
						<h2>未查询到相应终端档案信息</h2>
					</div>
				</div>
			</s:else>
		</div>
	</div>
</body>
<%@ include file="/foot.jsp"%>
</html>
