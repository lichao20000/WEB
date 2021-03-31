<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ page import="com.linkage.litms.LipossGlobals"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<%@taglib prefix="ms" uri="/ailk-itms-web-tags"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>业务查询</title>
<%
	/**
	 * e8-c业务查询
	 * 
	 * @author qixueqi(4174)
	 * @version 1.0
	 * @since 2010-09-08
	 * @category
	 */
%>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript"
	src="<s:url value="/Js/jQuerySplitPage-linkage.js"/>"></script>

<script type="text/javascript">
	$(function() {
		parent.dyniframesize();
	});

	function ToExcel() {
		parent.ToExcel();
	}

	// 重新激活
	function resetData(userId, deviceId, oui, deviceSN, servTypeId, servstauts,serUsername) {
		if (deviceId == "") {
			alert("用户未绑定设备，请先绑定设备，再激活！");
			return;
		}
		if (confirm('重新激活是将该业务置为未做状态，确实要重置吗?')) {
			var url = "<s:url value='/itms/service/bssSheetServSDLT!callPreProcess.action'/>";
			$.post(url, {
				userId : userId,
				deviceSN : deviceSN,
				deviceId : deviceId,
				servTypeId : servTypeId,
				servstauts : servstauts,
				serUsername : serUsername,
				oui : oui
			}, function(ajax) {
				if (ajax == "1") {
					alert("调预读成功！");
					//parent.query();
				} else if (ajax == "-1") {
					alert("参数为空！");
				} else if (ajax == "-2") {
					alert("调预读失败！");
				}
			});
			//$("td[@id='temp123']").html("<font color='red'>未做</font>");
			//$('#resultStr',window.parent.document).html("<font color='red'>02586588146重新激活成功！</font>");
		}
	}

	function DetailDevice(device_id) {
		var gw_type = "";
		var strpage = "<s:url value='/Resource/DeviceShow.jsp'/>?device_id="
				+ device_id;
		window
				.open(strpage, "",
						"left=20,top=20,width=900,height=650,resizable=yes,scrollbars=yes");
	}

	function GoContent(user_id, gw_type) {
		gw_type = window.parent.document.getElementsByName("gw_type")[0].value;
		var strpage = "<s:url value='/Resource/HGWUserRelatedInfo.jsp'/>?user_id="
				+ user_id;
		if (gw_type == "2") {
			strpage = "<s:url value='/Resource/EGWUserRelatedInfo.jsp'/>?user_id="
					+ user_id;
		}
		window
				.open(strpage, "",
						"left=20,top=20,width=900,height=650,resizable=yes,scrollbars=yes");
	}
	
	function GoServInfo(loid, gw_type) {
        var strpage = "<s:url value='/itms/service/bssSheetByHand4HBLT.action'/>?gw_type=1&loid="
                        + loid;
        window.open(strpage, "",
                                        "left=20,top=20,width=900,height=650,resizable=yes,scrollbars=yes");
	}

	// 配置信息	
	function configInfo(userId, oui, deviceSN, deviceId, servTypeId,
			servstauts, wanType, open_status,serUsername) {
		var gw_type = window.parent.document.getElementsByName("gw_type")[0].value;
		//alert("gw_type:" + gw_type);
		//alert("userId"+userId+"oui"+oui+"deviceSN"+deviceSN+"deviceId"+deviceId+"servTypeId"+servTypeId+"servstauts"+servstauts+"wanType"+wanType);
		$('#bssSheetDetail', window.parent.document).hide();
		$('#bssSheetInfo', window.parent.document).hide();
		$('#configInfoEm', window.parent.document).hide();
		$('#configInfo', window.parent.document).hide();
		var url = "<s:url value='/itms/service/bssSheetServSDLT!getConfigInfo.action'/>";
		$.post(url, {
			userId : userId,
			oui : oui,
			deviceSN : deviceSN,
			deviceId : deviceId,
			servTypeId : servTypeId,
			servstauts : servstauts,
			wanType : wanType,
			serUsername : serUsername,
			gw_type : gw_type,
			openstatus : open_status
		}, function(mesg) {
			$('#configInfoEm', window.parent.document).show();
			$('#configInfo', window.parent.document).show();
			$('#configInfo', window.parent.document).html(mesg);
		});
	}

	// BSS工单
	function bssSheet(userId, cityId, servTypeId, type_name, serUsername,realBindPort) {
		// gw_type
		var gw_type = window.parent.document.getElementsByName("gw_type")[0].value;
		$('#bssSheetDetail', window.parent.document).hide();
		$('#bssSheetInfo', window.parent.document).hide();
		$('#configInfoEm', window.parent.document).hide();
		$('#configInfo', window.parent.document).hide();
		var url = "<s:url value='/itms/service/bssSheetServSDLT!getBssSheet.action'/>";
		$.post(url,
				{
					userId : userId,
					cityId : cityId,
					servTypeId : servTypeId,
					type_name : type_name,
					serUsername : serUsername,
					gw_type : gw_type,
					realBindPort:realBindPort,
					netServUp : window.parent.document
							.getElementsByName("netServUp")[0].value
				}, function(mesg) {
					$('#bssSheetInfo', window.parent.document).show();
					$('#bssSheetInfo', window.parent.document).html(mesg);
				});
	}

	function bssSheet2(username) {
		$('#bssSheetDetail', window.parent.document).hide();
		$('#bssSheetInfo', window.parent.document).hide();
		$('#configInfoEm', window.parent.document).hide();
		$('#configInfo', window.parent.document).hide();
		var url = "<s:url value='/itms/service/bssSheetServSDLT!getBssSheet2.action'/>";
		$.post(url, {
			username : username
		}, function(mesg) {
			$('#bssSheetInfo', window.parent.document).show();
			$('#bssSheetInfo', window.parent.document).html(mesg);
		});
	}

	function checkdevice(username) {
		$('#bssSheetDetail', window.parent.document).hide();
		$('#bssSheetInfo', window.parent.document).hide();
		$('#configInfoEm', window.parent.document).hide();
		$('#configInfo', window.parent.document).hide();
		var url = "<s:url value='/itms/service/bssSheetServSDLT!checkdevice.action'/>";
		$.post(url, {
			username : username
		}, function(mesg) {
			//$('#bssSheetInfo',window.parent.document).show();
			//$('#bssSheetInfo',window.parent.document).html(mesg);
			if (mesg == "") {
				alert("该LOID对应设备不存在！");
			} else {
				alert("该LOID对应设备存在！<br>点击确定查看设备详细信息！");
				DetailDevice(mesg);
			}

		});
	}

	function DetailDevice(device_id) {
		var strpage = "../../Resource/DeviceShow.jsp?device_id=" + device_id;

		window
				.open(strpage, "",
						"left=20,top=20,width=900,height=650,resizable=yes,scrollbars=yes");
	}

	// 业务下发
	function sendSheet(userId, deviceId, oui, deviceSN, servTypeId, servstauts, serUsername) {
		if (deviceId == "") {
			alert("用户未绑定设备，请先绑定设备，再下发业务！");
			return;
		}
		if (confirm('确认要下发业务吗?')) {
			var url = "<s:url value='/itms/service/bssSheetServSDLT!callPreProcess.action'/>";
			$.post(url, {
				userId : userId,
				servTypeId : servTypeId,
				deviceId : deviceId,
				oui : oui,
				deviceSN : deviceSN,
				serUsername : serUsername,
				servstauts : servstauts
			}, function(ajax) {
				if (ajax == "1") {
					alert("调预读成功！");
					//parent.query();
				} else if (ajax == "-1") {
					alert("参数为空！");
				} else if (ajax == "-2") {
					alert("调预读失败！");
				}
			});
		}
	}

	function configDetail(user_id, serv_type_id,serUsername) {
		// gw_type
		$('#bssSheetDetail', window.parent.document).hide();
		$('#bssSheetInfo', window.parent.document).hide();
		$('#configInfoEm', window.parent.document).hide();
		$('#configInfo', window.parent.document).hide();
		var url = "<s:url value='/itms/service/bssSheetServSDLT!getBssIssuedConfigDetail.action'/>";
		$.post(url, {
			user_id : user_id,
			serUsername : serUsername,
			serv_type_id : serv_type_id
		}, function(mesg) {
			$('#bssSheetDetail', window.parent.document).show();
			$('#bssSheetDetail', window.parent.document).html(mesg);
		});
	}
</script>
</head>
<body>
	<table class="listtable" id="listTable">
		<caption>BSS业务信息</caption>
		<thead>
			<tr>
				<th>LOID</th>
				<ms:inArea areaCode="cq_dx" notInMode="false">
					<th>电子单号</th>
				</ms:inArea>
				<th>属地</th>
				<ms:inArea areaCode="js_dx" notInMode="false">
					<th>用户工单来源</th>
				</ms:inArea>
				<th>BSS受理时间</th>
				<th>业务类型</th>
				<ms:inArea areaCode="sd_lt" notInMode="false">
					<th>业务状态</th>
				</ms:inArea>
				<ms:inArea areaCode="hb_dx,hb_lt" notInMode="true">
				<th>业务帐号</th>
				<th>BSS终端规格</th>
				</ms:inArea>
				<ms:inArea areaCode="hb_lt" notInMode="false">
				<th>业务帐号</th>
				<th>工单序号</th>
				</ms:inArea>
				<th>设备序列号</th>
				<th>开通状态</th>
				<ms:inArea areaCode="jl_dx,sd_lt" notInMode="true">
					<th>BSS终端类型</th>
				</ms:inArea>
				<ms:inArea areaCode="hb_lt" notInMode="false">
					<th>是否有光猫标识</th>
				</ms:inArea>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
			<s:if test="bssSheetServList!=null">
				<s:if test="bssSheetServList.size()>0">
					<s:iterator value="bssSheetServList">
						<s:if test='open_status=="1"'>
							<tr>
								<%if(!"hb_lt".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))){%>
								<!-- 河北联通点击loid链接到手工工单页面-->
								<td><a
									href="javascript:GoContent('<s:property value="user_id" />',1);">
										<s:property value="username" />
								</a></td>
								<%
								}
								else{
								%>
								<td><a
                                    href="javascript:GoServInfo('<s:property value="username" />',1);">
                                       <s:property value="username" />
                                </a></td>
								<%
								}
								%>
								<ms:inArea areaCode="cq_dx" notInMode="false">
									<td><s:property value="orderid" /></td>
								</ms:inArea>
								<td><s:property value="city_name" /></td>
								<ms:inArea areaCode="js_dx" notInMode="false">
									<td><s:property value="user_type_id" /></td>
								</ms:inArea>
								<td><s:property value="dealdate" /></td>
								<td><s:property value="serv_type" /></td>
								
								<ms:inArea areaCode="sd_lt" notInMode="false">
									<s:if test='serv_status=="2"'>
									<td>停机</td>
									</s:if>
									<s:elseif test='serv_status=="3"'>
									<td>销户</td>
									</s:elseif>
									<s:else>
									<td>正常</td>
									</s:else>
								</ms:inArea>
								
								<ms:inArea areaCode="hb_dx,hb_lt" notInMode="true">
								<td><s:property value="serUsername" /></td>
								<td><s:property value="spec_name" /></td>
								</ms:inArea>
								<ms:inArea areaCode="hb_lt" notInMode="false">
								<td><s:property value="serUsername" /></td>
								<td><s:property value="orderid" /></td>
								</ms:inArea>
								<td><a
									href="javascript:DetailDevice('<s:property value="device_id" />');">
										<s:property value="device_serialnumber" />
								</a></td>
								<td>成功</td>
								<ms:inArea areaCode="jl_dx,sd_lt" notInMode="true">
									<td><s:property value="type_name" /></td>
								</ms:inArea>
								<ms:inArea areaCode="hb_lt" notInMode="false">
								<s:if test='gigabit_port=="1"'>
								<td>是</td>
								</s:if>
								<s:else>
								<td>否</td>
								</s:else>
								</ms:inArea>
								<td>
									<ms:inArea areaCode="cq_dx" notInMode="false">
									<a
									href="javascript:configInfo('<s:property value="user_id" />','<s:property value="oui" />','<s:property value="device_serialnumber" />','<s:property value="device_id" />','<s:property value="serv_type_id" />','<s:property value="serv_status" />','<s:property value="wan_type" />','<s:property value="open_status" />','<s:property value="serUsername" />')">配置信息</a>|
									<s:if test='serv_type_id!="18"'>
										<a
											href="javascript:bssSheet('<s:property value="user_id" />','<s:property value="city_id" />','<s:property value="serv_type_id" />','<s:property value="type_name" />','<s:property value="serUsername" />','<s:property value="real_bind_port" />')">BSS工单</a>
									</s:if>
									</ms:inArea>
									<ms:inArea areaCode="cq_dx" notInMode="true">
									<s:if test='serv_type_id=="14"'>
									<a
									href="javascript:configInfo('<s:property value="user_id" />','<s:property value="oui" />','<s:property value="device_serialnumber" />','<s:property value="device_id" />','<s:property value="serv_type_id" />','<s:property value="serv_status" />','<s:property value="wan_type" />','<s:property value="open_status" />','<s:property value="username" />')">配置信息</a>|
									</s:if>
									<s:else>
									<a
									href="javascript:configInfo('<s:property value="user_id" />','<s:property value="oui" />','<s:property value="device_serialnumber" />','<s:property value="device_id" />','<s:property value="serv_type_id" />','<s:property value="serv_status" />','<s:property value="wan_type" />','<s:property value="open_status" />','<s:property value="serUsername" />')">配置信息</a>|
									</s:else>
									<s:if test='serv_type_id!="18"'>
										<a
											href="javascript:bssSheet('<s:property value="user_id" />','<s:property value="city_id" />','<s:property value="serv_type_id" />','<s:property value="type_name" />','<s:property value="serUsername" />','<s:property value="real_bind_port" />')">BSS工单</a>
									</s:if> 
									</ms:inArea>
									<%if("sd_lt".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))){%>
										<s:if test='serv_type_id=="10" || serv_type_id=="11" || serv_type_id=="14"'>
										| <a
												href="javascript:configDetail('<s:property value="user_id" />','<s:property value="serv_type_id" />','<s:property value="serUsername" />')">配置模版</a>
										</s:if>
									<%
									}
									%>
								</td>
							</tr>
						</s:if>
						<s:elseif test='open_status=="0"'>
							<tr>
								<%if(!"hb_lt".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))){%>
								<!-- 河北联通点击loid链接到手工工单页面-->
								<td><a
									href="javascript:GoContent('<s:property value="user_id" />',1);">
										<font color="#008000"><s:property value="username" /> </font>
								</a></td>
								<%
								}
								else{
								%>
								<td><a
                                    href="javascript:GoServInfo('<s:property value="username" />',1);">
                                       <font color="#008000"><s:property value="username" /> </font>
                                </a></td>
								<%
								}
								%>
								<ms:inArea areaCode="cq_dx" notInMode="false">
									<td><s:property value="orderid" /></td>
								</ms:inArea>
								<td><font color="#008000"><s:property
											value="city_name" /> </font></td>
								<ms:inArea areaCode="js_dx" notInMode="false">
									<td><font color="#008000"><s:property
												value="user_type_id" /> </font></td>
								</ms:inArea>
								<td><font color="#008000"><s:property
											value="dealdate" /> </font></td>
								<td><font color="#008000"><s:property
											value="serv_type" /> </font></td>
								
								<ms:inArea areaCode="sd_lt" notInMode="false">
									<s:if test='serv_status=="2"'>
									<td><font color="#008000">停机</font></td>
									</s:if>
									<s:elseif test='serv_status=="3"'>
									<td><font color="#008000">销户</font></td>
									</s:elseif>
									<s:else>
									<td><font color="#008000">正常</font></td>
									</s:else>
								</ms:inArea>			
											
											
								<ms:inArea areaCode="hb_dx,hb_lt" notInMode="true">
								<td><font color="#008000"><s:property
												value="serUsername" /></td>
								<td><font color="#008000"><s:property
											value="spec_name" /></font></td>
								</ms:inArea>
								<ms:inArea areaCode="hb_lt" notInMode="false">
								<td><font color="#008000"><s:property
												value="serUsername" /></td>
								<td><font color="#008000"><s:property
											value="orderid" /></font></td>
								</ms:inArea>
								<td><a
									href="javascript:DetailDevice('<s:property value="device_id" />');">
										<font color="#008000"><s:property
												value="device_serialnumber" /> </font>
								</a></td>
								<td><font color="#008000">未做</font></td>
								<ms:inArea areaCode="jl_dx,sd_lt" notInMode="true">
									<td><s:property value="type_name" /></td>
								</ms:inArea>
								<ms:inArea areaCode="hb_lt" notInMode="true">
								<s:if test='gigabit_port=="1"'>
								<td>是</td>
								</s:if>
								<s:elseif test='gigabit_port=="0"'>
								<td>否</td>
								</s:elseif>
								</ms:inArea>
								<td>
									<ms:inArea areaCode="cq_dx" notInMode="true">
									<s:if test='serv_type_id=="14"'>
									<a
									href="javascript:configInfo('<s:property value="user_id" />','<s:property value="oui" />','<s:property value="device_serialnumber" />','<s:property value="device_id" />','<s:property value="serv_type_id" />','<s:property value="serv_status" />','<s:property value="wan_type" />','<s:property value="open_status" />','<s:property value="username" />')">配置信息</a>|
									</s:if>
									<s:else>
									<a
									href="javascript:configInfo('<s:property value="user_id" />','<s:property value="oui" />','<s:property value="device_serialnumber" />','<s:property value="device_id" />','<s:property value="serv_type_id" />','<s:property value="serv_status" />','<s:property value="wan_type" />','<s:property value="open_status" />','<s:property value="serUsername" />')">配置信息</a>|
									</s:else>
									<a
									href="javascript:bssSheet('<s:property value="user_id" />','<s:property value="city_id" />','<s:property value="serv_type_id" />','<s:property value="type_name" />','<s:property value="serUsername" />','<s:property value="real_bind_port" />')">BSS工单</a>|
									<s:if test='device_id != null&&device_id != ""'>
										<a
											href="javascript:sendSheet('<s:property value="user_id" />','<s:property value="device_id" />','<s:property value="oui" />','<s:property value="device_serialnumber" />','<s:property value="serv_type_id" />','<s:property value="serv_status" />','<s:property value="serUsername" />')">业务下发</a>|
									</s:if> 
									</ms:inArea>
									<ms:inArea areaCode="cq_dx" notInMode="false">
									<a
									href="javascript:configInfo('<s:property value="user_id" />','<s:property value="oui" />','<s:property value="device_serialnumber" />','<s:property value="device_id" />','<s:property value="serv_type_id" />','<s:property value="serv_status" />','<s:property value="wan_type" />','<s:property value="open_status" />','<s:property value="serUsername" />')">配置信息</a>|

									<a
									href="javascript:bssSheet('<s:property value="user_id" />','<s:property value="city_id" />','<s:property value="serv_type_id" />','<s:property value="type_name" />','<s:property value="serUsername" />','<s:property value="real_bind_port" />')">BSS工单</a>|
									<s:if test='device_id != null&&device_id != ""'>
										<a
											href="javascript:sendSheet('<s:property value="user_id" />','<s:property value="device_id" />','<s:property value="oui" />','<s:property value="device_serialnumber" />','<s:property value="serv_type_id" />','<s:property value="serv_status" />','<s:property value="serUsername" />')">业务下发</a>|
									</s:if> 
									</ms:inArea>
										<%if("sd_lt".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))){%>
										<s:if test='serv_type_id=="10" || serv_type_id=="11" || serv_type_id=="14"'>
										| <a
												href="javascript:configDetail('<s:property value="user_id" />','<s:property value="serv_type_id" />','<s:property value="serUsername" />')">配置模版</a>
										</s:if>
									<%
									}
									%>
							</td>
							</tr>
						</s:elseif>
						<s:elseif test='open_status=="-1"'>
							<tr>
								<%if(!"hb_lt".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))){%>
								<!-- 河北联通点击loid链接到手工工单页面-->
								<td><a
									href="javascript:GoContent('<s:property value="user_id" />',1);">
										<font color="red"><s:property value="username" /> </font>
								</a></td>
								<%
								}
								else{
								%>
								<td><a
                                    href="javascript:GoServInfo('<s:property value="username" />',1);">
                                       <font color="red"><s:property value="username" /> </font>
                                </a></td>
								<%
								}
								%>
								<ms:inArea areaCode="cq_dx" notInMode="false">
									<td><font color="red"><s:property value="orderid" /></font></td>
								</ms:inArea>
								<td><font color="red"><s:property value="city_name" /></font></td>
								<ms:inArea areaCode="js_dx" notInMode="false">
								<td><font color="red"><s:property
												value="user_type_id" /> </font></td>
								</ms:inArea>
								<td><font color="red"><s:property value="dealdate" />
								</font></td>
								<td><font color="red"><s:property value="serv_type" />
								</font></td>
								
								<ms:inArea areaCode="sd_lt" notInMode="false">
									<s:if test='serv_status=="2"'>
									<td><font color="red">停机</font></td>
									</s:if>
									<s:elseif test='serv_status=="3"'>
									<td><font color="red">销户</font></td>
									</s:elseif>
									<s:else>
									<td><font color="red">正常</font></td>
									</s:else>
								</ms:inArea>		
								
								
								<ms:inArea areaCode="hb_dx,hb_lt" notInMode="true">
								<td><font color="red"><s:property
												value="serUsername" /></font></td>
								<td><font color="red"><s:property value="spec_name" />
								</font></td>
								</ms:inArea>
								<ms:inArea areaCode="hb_lt" notInMode="false">
								<td><font color="red"><s:property
												value="serUsername" /></font></td>
								<td><font color="red"><s:property value="orderid" />
								</font></td>
								</ms:inArea>
								<td><a
									href="javascript:DetailDevice('<s:property value="device_id" />');">
										<font color="red"><s:property
												value="device_serialnumber" /> </font>
								</a></td>
								<td><font color="red">失败</font></td>
								<ms:inArea areaCode="jl_dx,sd_lt" notInMode="true">
									<td><s:property value="type_name" /> <input type='hidden'
										value='<s:property value="user_id" />|<s:property value="device_id" />|<s:property value="oui" />|<s:property value="device_serialnumber" />|<s:property value="serv_type_id" />|<s:property value="serv_status" />'
										name="resetParam" /></td>
								</ms:inArea>
								<ms:inArea areaCode="hb_lt" notInMode="true">
								<s:if test='gigabit_port=="1"'>
								<td>是</td>
								</s:if>
								<s:elseif test='gigabit_port=="0"'>
								<td>否</td>
								</s:elseif>
								</ms:inArea>
								<td>
									<ms:inArea areaCode="cq_dx" notInMode="true">
									<s:if test='serv_type_id=="14"'>
									<a
									href="javascript:configInfo('<s:property value="user_id" />','<s:property value="oui" />','<s:property value="device_serialnumber" />','<s:property value="device_id" />','<s:property value="serv_type_id" />','<s:property value="serv_status" />','<s:property value="wan_type" />','<s:property value="open_status" />','<s:property value="username" />')">配置信息</a>|
									</s:if>
									<s:else>
									<a
									href="javascript:configInfo('<s:property value="user_id" />','<s:property value="oui" />','<s:property value="device_serialnumber" />','<s:property value="device_id" />','<s:property value="serv_type_id" />','<s:property value="serv_status" />','<s:property value="wan_type" />','<s:property value="open_status" />','<s:property value="serUsername" />')">配置信息</a>|
									</s:else>
									<a
									href="javascript:bssSheet('<s:property value="user_id" />','<s:property value="city_id" />','<s:property value="serv_type_id" />','<s:property value="type_name" />','<s:property value="serUsername" />','<s:property value="real_bind_port" />')">BSS工单</a>|
									<a
									href="javascript:resetData('<s:property value="user_id" />','<s:property value="device_id" />','<s:property value="oui" />','<s:property value="device_serialnumber" />','<s:property value="serv_type_id" />','<s:property value="serv_status" />','<s:property value="serUsername" />')">重新激活</a>									
									</ms:inArea>
									<ms:inArea areaCode="cq_dx" notInMode="false">
									<a
									href="javascript:configInfo('<s:property value="user_id" />','<s:property value="oui" />','<s:property value="device_serialnumber" />','<s:property value="device_id" />','<s:property value="serv_type_id" />','<s:property value="serv_status" />','<s:property value="wan_type" />','<s:property value="open_status" />','<s:property value="serUsername" />')">配置信息</a>|
									<a
									href="javascript:bssSheet('<s:property value="user_id" />','<s:property value="city_id" />','<s:property value="serv_type_id" />','<s:property value="type_name" />','<s:property value="serUsername" />','<s:property value="real_bind_port" />')">BSS工单</a>|
									<a
									href="javascript:resetData('<s:property value="user_id" />','<s:property value="device_id" />','<s:property value="oui" />','<s:property value="device_serialnumber" />','<s:property value="serv_type_id" />','<s:property value="serv_status" />','<s:property value="serUsername" />')">重新激活</a>									
									</ms:inArea>
										<%if("sd_lt".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))){%>
										<s:if test='serv_type_id=="10" || serv_type_id=="11" || serv_type_id=="14"'>
										| <a
												href="javascript:configDetail('<s:property value="user_id" />','<s:property value="serv_type_id" />','<s:property value="serUsername" />')">配置模版</a>
										</s:if>
									<%
									}
									%>
								</td>
							</tr>
						</s:elseif>
						<s:else>
							<tr>
								<%if(!"hb_lt".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))){%>
								<!-- 河北联通点击loid链接到手工工单页面-->
								<td><a
									href="javascript:GoContent('<s:property value="user_id" />',1);">
										<s:property value="username" />
								</a></td>
								<%
								}
								else{
								%>
								<td><a
                                    href="javascript:GoServInfo('<s:property value="username" />',1);">
                                       <s:property value="username" />
                                </a></td>
								<%
								}
								%>
								<ms:inArea areaCode="cq_dx" notInMode="false">
									<td><s:property value="orderid" /></td>
								</ms:inArea>
								<td><s:property value="city_name" /></td>
								<ms:inArea areaCode="js_dx" notInMode="false">
									<td><s:property value="user_type_id" /></td>
								</ms:inArea>
								<td></td>
								<td></td>
								<ms:inArea areaCode="hb_dx,hb_lt" notInMode="true">
								<td></td>
								<td><s:property value="spec_name" /></td>
								</ms:inArea>
								<ms:inArea areaCode="hb_lt" notInMode="false">
								<td></td>
								<td><s:property value="orderid" /></td>
								</ms:inArea>
								<td><a
									href="javascript:DetailDevice('<s:property value="device_id" />');">
										<s:property value="device_serialnumber" />
								</a></td>
								<td></td>
								<ms:inArea areaCode="jl_dx,sd_lt" notInMode="true">
									<td align="center"><s:property value="type_name" /></td>
								</ms:inArea>
								<ms:inArea areaCode="hb_lt" notInMode="true">
								<s:if test='gigabit_port=="1"'>
								<td align="center">是</td>
								</s:if>
								<s:elseif test='gigabit_port=="0"'>
								<td td align="center">否</td>
								</s:elseif>
								</ms:inArea>
								<td><a
									href="javascript:bssSheet2('<s:property value="username" />')">BSS工单</a>

									 <!--<s:if test='device_serialnumber==""||device_serialnumber==null'>
								<a
									href="javascript:checkdevice('<s:property value="username" />')">检测设备</a>
							</s:if>--></td>
							</tr>
						</s:else>
					</s:iterator>
				</s:if>
				<s:else>
					<tr>
						<ms:inArea areaCode="jl_dx">
							<td colspan=11>系统没有该用户的业务信息!</td>
						</ms:inArea>
						<ms:inArea areaCode="jl_dx" notInMode="true">
							<td colspan=11>系统没有该用户的业务信息!</td>
						</ms:inArea>
					</tr>
				</s:else>
			</s:if>
			<s:else>
				<tr>
					<ms:inArea areaCode="jl_dx">
						<td colspan=11>系统没有此用户!</td>
					</ms:inArea>
					<ms:inArea areaCode="jl_dx" notInMode="true">
						<td colspan=11>系统没有此用户!</td>
					</ms:inArea>
				</tr>
			</s:else>
		</tbody>
		<tfoot>
			<tr>
					<ms:inArea areaCode="cq_dx,hb_lt" notInMode="true">
					<td colspan="10" align="right">
						<%--<a href="#" onclick="batchReset()" 
				id=batchBut>批量激活</a>--%> &nbsp;&nbsp;&nbsp;&nbsp; <lk:pages
							url="/itms/service/bssSheetServSDLT!getBssSheetServInfo.action"
							styleClass="" showType="" isGoTo="true" changeNum="true" />
					</td>
					</ms:inArea>
					<ms:inArea areaCode="cq_dx,hb_lt" notInMode="false">
					<td colspan="11" align="right">
						<%--<a href="#" onclick="batchReset()" 
				id=batchBut>批量激活</a>--%> &nbsp;&nbsp;&nbsp;&nbsp; <lk:pages
							url="/itms/service/bssSheetServSDLT!getBssSheetServInfo.action"
							styleClass="" showType="" isGoTo="true" changeNum="true" />
					</td>
					</ms:inArea>
			</tr>
			<s:if test='#session.isReport=="1"'>
				<tr>
					<ms:inArea areaCode="jl_dx">
						<td colspan="10" align="right"><IMG
							SRC="/itms/images/excel.gif" BORDER='0' ALT='导出列表'
							style='cursor: hand' onclick="ToExcel()"></td>
					</ms:inArea>
					<ms:inArea areaCode="jl_dx" notInMode="true">
						<td colspan="11" align="right"><IMG
							SRC="/itms/images/excel.gif" BORDER='0' ALT='导出列表'
							style='cursor: hand' onclick="ToExcel()"></td>
					</ms:inArea>
				</tr>
			</s:if>
		</tfoot>
	</table>
</body>
<script>
	function batchReset() {
		var url = "bssSheetServBatchReset.jsp";
		window
				.open(url, "",
						"left=20,top=20,width=900,height=600,resizable=no,scrollbars=yes");
	}
	$(document).ready(function(){
		parent.loading();
	})
</script>

</html>
