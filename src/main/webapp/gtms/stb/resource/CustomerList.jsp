<%@page contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage"%>
<%@taglib prefix="ms" uri="/ailk-itms-web-tags"%>
<%@page import="com.linkage.litms.LipossGlobals" %>
<%
String area = LipossGlobals.getLipossProperty("InstArea.ShortName");
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK" />
<title>零配置BSS工单统计</title>
<lk:res />
<script type="text/javascript">
	$(function() {
		parent.dyniframesize();
	});

	function cusomerDetail(customerId, deviceId, status) {
		var url = "<s:url value='/gtms/stb/resource/stbCustomer!queryCustomerDetail.action'/>?customerId="
				+ customerId + "&deviceId=" + deviceId + "&status=" + status;
		window
				.open(url, "",
						"left=80,top=80,width=800,height=400,resizable=yes,scrollbars=yes");
	}

	function deviceDetail(deviceId) {
		var url = "<s:url value='/gtms/stb/resource/gwDeviceQueryStb!queryZeroDeviceDetail.action'/>?deviceId="
				+ deviceId + "&gw_type=4";
		window
				.open(url, "",
						"left=80,top=80,width=800,height=400,resizable=yes,scrollbars=yes");
	}

	function configInfo(device_id,deviceSn){
		parent.config(device_id,deviceSn);
	}

	function serviceDone(device_id,customer_id,device_sn,oui,serv_account){
		var deviceId = device_id;
		if ("" == deviceId ){
			alert('请先确认是否绑定设备！');
			return false;
		}

		var message = "请确认！业务帐号："+serv_account+"，设备序列号："+device_sn;
		if (!confirm(message+'！是否继续业务下发?')){
			return false;
		}
		var url = "<s:url value='/gtms/stb/resource/stbInst!callPreProcess.action'/>";
		$.post(url,{
			deviceId:deviceId,
			customer_id:customer_id,
			oui:oui,
			deviceSN:device_sn
		},function(ajax){
			if (ajax == "1") {
				alert("调预读成功！");
			} else {
				alert("调预读失败！");
			}
		});
	}


	function GoContent(loidStr, gw_typeStr) {
		var url = "<s:url value='/gtms/stb/resource/stbCustomer!getUserIdByLoid.action'/>";
		$.post(url,{
			loid:loidStr,
			gw_type:gw_typeStr
		},function(ajax){
			if(ajax==""){
				alert("没有查询到用户的 user_id ");
			}else{
				var strpage = "<s:url value='/Resource/HGWUserRelatedInfo.jsp'/>?user_id=" + ajax;
				if (gw_typeStr == "5") {
					strpage = "<s:url value='/Resource/EGWUserRelatedInfo.jsp'/>?user_id=" + ajax;
				}
				window.open(strpage, "", "left=20,top=20,width=900,height=650,resizable=yes,scrollbars=yes");
			}
		});
	}



	function GoServInfo(loid, gw_type) {
        var strpage = "<s:url value='/itms/service/bssSheetByHand4HBLT!execute_stb.action'/>?gw_type=1&loid="
                        + loid;
        window.open(strpage, "",
                                        "left=20,top=20,width=900,height=650,resizable=yes,scrollbars=yes");
	}

	// BSS工单
	function bssSheet(customer_id) {
		var url = "<s:url value='/gtms/stb/resource/stbInst!getBssSheet.action'/>";
		$('#trqueryConfig', window.parent.document).hide();
		$('#bssSheetInfo', window.parent.document).hide();
		$('#tip_loading', window.parent.document).hide();
		$.post(url,{
			customer_id: customer_id
		},function(ajax){
			$('#bssSheetInfo', window.parent.document).show();
			$('#bssSheetInfo', window.parent.document).html(ajax);
		});
	}
</script>
</head>
<body>

	<table width="100%" class="listtable" align="center"
		style="margin-top: 10px;">
		<thead>
			<tr>
				<th class="title_1">属地</th>
				<s:if test="%{instAreaName=='jx_dx'}">
					<th class="title_1">用户宽带账号</th>
				</s:if>
				<th class="title_1">业务账号</th>
				<ms:inArea areaCode="sx_lt" notInMode="true">
					<th class="title_1">接入帐号</th>
				</ms:inArea>
				<s:if test="%{instAreaName=='jl_dx' || instAreaName=='hb_lt' || instAreaName=='sx_lt'}">
					<th class="title_1">
						<ms:inArea areaCode="sx_lt">
							唯一标识
						</ms:inArea>
						<ms:inArea areaCode="sx_lt" notInMode="true">
							LOID
						</ms:inArea>
					</th>
				</s:if>
				<s:if test="%{instAreaName=='hb_lt' || instAreaName=='sx_lt'}">
					<th class="title_1">工单序号</th>
				</s:if>
				<s:if test="%{instAreaName!='hb_lt' && instAreaName!='hn_lt' && instAreaName!='sx_lt'}">
				<th class="title_1">接入类型</th>
				</s:if>
				<%--山东联通需要展示mac地址 --%>
				<s:if test="%{instAreaName=='sd_lt'}">
					<th class="title_1">接入方式</th>
					<th class="title_1">MAC</th>
				</s:if>
				<s:if test="%{instAreaName=='hn_lt'}">
					<th class="title_1">业务平台类型</th>
					<th class="title_1">用户分组类型</th>
				</s:if>
				<th class="title_1">机顶盒序列号</th>
				<th class="title_1">开户时间</th>
				<s:if test="%{instAreaName=='jl_dx'}">
					<th class="title_1">客户类型</th>
					<th class="title_1">工单mac地址</th>
				</s:if>
				<s:if test="%{instAreaName=='nx_dx'}">
					<th class="title_1">BSS终端类型</th>
				</s:if>
				<s:if test="'hn_lt'!=instArea">
				<th class="title_1">开通状态</th>
				<%--s:if test="%{customerDTO.userStatus==0}"></s:if>
				<s:elseif test="%{customerDTO.userStatus==''}"></s:elseif>
				<s:else--%>
					<th class="title_1">操作</th>
				</s:if>
				<%--/s:else --%>
			</tr>
		</thead>
		<tbody>
			<s:if test="customerList.size()>0">
				<s:iterator var="list" value="customerList">
					<tr>
						<td><s:property value="#list.cityName" /></td>
						<s:if test="%{instAreaName=='jx_dx'}">
					        <td><s:property value="#list.cust_account" /></td>
				        </s:if>
						<td><s:property value="#list.servAccount" /></td>
						<ms:inArea areaCode="sx_lt" notInMode="true">
							<td><s:property value="#list.pppoeUser" /></td>
						</ms:inArea>
						<s:if test="%{instAreaName=='jl_dx'}">
						<td><a href="javascript:GoContent('<s:property value="#list.loid" />',<s:property value="#list.custTypeInt" />);">
							<s:property value="#list.loid" /></a></td>
						</s:if>
						<s:if test="%{instAreaName=='hb_lt'}">
						<td><a href="javascript:GoServInfo('<s:property value="#list.loid" />',1);">
							<s:property value="#list.loid" /></a></td>
						<td><s:property value="#list.order_no" /></td>
						</s:if>
						<s:if test="%{instAreaName=='sx_lt'}">
							<td><s:property value="#list.loid" /></td>
							<td><s:property value="#list.order_no" /></td>
						</s:if>
						<s:if test="%{instAreaName!='hb_lt' && instAreaName!='hn_lt' && instAreaName!='sx_lt'}">
						<td><s:property value="#list.addressingType" /></td>
						</s:if>
						<%--山东联通需要展示mac地址 --%>
						<s:if test="%{instAreaName=='sd_lt'}">
							<td><s:property value="#list.stbuptyle" /></td>
							<td><s:property value="#list.mac" /></td>
						</s:if>
						<s:if test="%{instAreaName=='hn_lt'}">
							<td><s:property value="#list.belongName" /></td>
							<td><s:property value="#list.groupName" /></td>
							<td><s:property value="#list.deviceSN" /></td>
						</s:if>
						<s:if test="%{instAreaName!='hn_lt'}">
						<td>
							<a onclick="deviceDetail('<s:property value="#list.deviceId" />')" style="cursor:hand"><s:property value="#list.deviceSN" /></a>
						</td>
						</s:if>
						<td><s:property value="#list.openUserdate" /></td>
						<%--吉林电信需要展示客户类型 --%>
						<s:if test="%{instAreaName=='jl_dx'}">
							<td><s:property value="#list.custType" /></td>
							<td><s:property value="#list.mac" /></td>
						</s:if>
						<s:if test="%{instAreaName=='nx_dx'}">
							<td class="title_1">机顶盒</td>
						</s:if>
						<s:if test="'hn_lt'!=instArea">
						<td><s:property value="#list.userStatus" /></td>
						<td align="center">
							<s:if test='%{instAreaName == "xj_dx" || instAreaName == "sx_lt"}'>
								<a onclick="serviceDone('<s:property value="#list.deviceId" />','<s:property value="#list.customerId" />','<s:property value="#list.deviceSN" />','<s:property value="#list.oui" />','<s:property value="#list.servAccount" />')" style="cursor: hand"> 业务下发</a>&nbsp;&nbsp;|&nbsp;&nbsp;
								<a onclick="configInfo('<s:property value="#list.deviceId" />','<s:property value="#list.deviceSN" />')" style="cursor:hand">
									<s:if test="#list.userStatus=='成功'">
										配置信息
									</s:if>
									<s:elseif test="#list.userStatus=='失败'">
										失败原因
									</s:elseif>
								</a>&nbsp;&nbsp;|&nbsp;&nbsp;
							</s:if>
							<s:elseif test='%{instAreaName == "nx_dx"}'>
								<a onclick="serviceDone('<s:property value="#list.deviceId" />','<s:property value="#list.customerId" />','<s:property value="#list.deviceSN" />','<s:property value="#list.oui" />','<s:property value="#list.servAccount" />')" style="cursor: hand"> 业务下发</a>&nbsp;|&nbsp;
								<a href="javascript: configInfo('<s:property value="#list.deviceId" />','<s:property value="#list.deviceSN" />')" style="cursor:hand">
									<s:if test="#list.userStatus=='成功'">
										配置信息
									</s:if>
									<s:elseif test="#list.userStatus=='失败'">
										失败原因
									</s:elseif>
								</a>&nbsp;|&nbsp;
								<a href="javascript: bssSheet('<s:property value="#list.customerId" />')">BSS工单</a>&nbsp;|&nbsp;
							</s:elseif>
							<label
								onclick="javascript:cusomerDetail('<s:property value="#list.customerId" />','<s:property value="#list.deviceId" />','<s:property value="customerDTO.userStatus" />');">
									<IMG SRC="../../../images/view.gif" BORDER='0' ALT='详细信息'
									style='cursor: hand'>
							</label>
						</td>
						</s:if>
					</tr>
				</s:iterator>
			</s:if>
			<s:else>
				<tr>
					<s:if test="%{instAreaName=='sd_lt'}">
						<td colspan="10">
							<div style="text-align: center">查询无数据</div>
						</td>
					</s:if>
					<s:elseif test="%{instAreaName=='jx_dx' || instAreaName=='nx_dx' || instAreaName=='sx_lt' }">
						<td colspan="9">
							<div style="text-align: center">查询无数据</div>
						</td>
					</s:elseif>
					<s:elseif test="%{instAreaName=='jl_dx'}">
						<td colspan="11">
							<div style="text-align: center">查询无数据</div>
						</td>
					</s:elseif>
					<s:else>
						<td colspan="8">
							<div style="text-align: center">查询无数据</div>
						</td>
					</s:else>
				</tr>
			</s:else>
		</tbody>
		<tfoot>
			<s:if test="customerList.size()>0">
				<tr>
					<s:if test="%{instAreaName=='sd_lt'}">
						<td class="foot" colspan="10">
							<div style="float: right">
								<lk:pages url="/gtms/stb/resource/stbCustomer!listCustomer.action"
									isGoTo="true" />
							</div>
						</td>
					</s:if>
					<s:elseif test="%{instAreaName=='jx_dx'  || instAreaName=='nx_dx' || instAreaName=='sx_lt'}">
						<td class="foot" colspan="9">
							<div style="float: right">
								<lk:pages url="/gtms/stb/resource/stbCustomer!listCustomer.action"
								isGoTo="true" changeNum="true"/>
								<!--<lk:pages url="/gwms/service/servStrategyLog!goPage.action" styleClass="" showType="" isGoTo="true" changeNum="true"/>-->
							</div>
						</td>
					</s:elseif>
					<s:elseif test="%{instAreaName=='jl_dx'}">
						<td class="foot" colspan="11">
							<div style="float: right">
							<lk:pages url="/gtms/stb/resource/stbCustomer!listCustomer.action"
								isGoTo="true" />
							</div>
						</td>
					</s:elseif>
					<s:else>
						<td class="foot" colspan="8">
							<div style="float: right">
								<lk:pages url="/gtms/stb/resource/stbCustomer!listCustomer.action"
									isGoTo="true"/>
							</div>
						</td>
					</s:else>
				</tr>
			</s:if>
		</tfoot>
		<input type="hidden" id="customerListSize" value="<s:property value="customerList.size()" />"/>
	</table>
</body>
</html>

<script>
<%-- var area = '<%=area%>'
// 2020/04/30 山西联通 父页面resultFrame与div_config 距离调整
if(area == 'sx_lt'){
	// 列表数量
	var customerListSize = $("#customerListSize").val();
	// 距离
	var marginTop = -100 + customerListSize * 20;
	$("#div_config",parent.document).css("margin-top", marginTop + "px");
}
 --%>


</script>
