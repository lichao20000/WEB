<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<%--
/**
 * ip地址管理的ip取消/回收的管理action
 *
 * @author 王志猛(5194) tel:13701409234
 * @version 1.0
 * @since 2007-11-12
 * @category ipmg
 */
 --%>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>for cancel or resum ip</title>
<script type="text/javascript">
</script>
<link href="<s:url value="/css/css_green.css"/>" rel="stylesheet"
	type="text/css">
<script type="text/javascript"
	src="<s:url value="/Js/jquery.js"/>"></script>

<script type="text/javascript"
	src="<s:url value="/Js/jQeuryExtend-linkage.js"/>"></script>
<script type="text/javascript"
	src="<s:url value="/Js/jquery.blockUI.js"/>"></script>
<script type="text/javascript">
//取消子网划分
function cancel(url)
{
	if(window.confirm("该操作将会取消该子网，确认执行？"))
	{
		window.location.href="<s:url value="/hgwipMgSys/assignIP!cancelSubNet.action"><s:param value="attr" name="attr"/></s:url>";
	}
}
//保存修改ip到地市
function savecity()
{
	$.ajax({ type: "POST",
			url: "<s:url value="/hgwipMgSys/assignIP!editUser.action"/>",
			data: {"attr":"<s:property value="attr"/>",
			"city_id":$("select[@name='city']").attr("value"),
			"countryName":encodeURIComponent($("select[@name='countryName']").attr("value")),
			"purpose1Name":encodeURIComponent($("select[@name='purpose1']").attr("value")),
			"purpose2Name":encodeURIComponent($("select[@name='purpose2']").attr("value")),
			"purpose3Name":encodeURIComponent($("select[@name='purpose3']").attr("value")),
			"comment":encodeURIComponent($("textarea[@name='comment']").attr("value"))},
			success:
				function(data)
				{
					if(data=="ok")
					{
						alert("修改IP成功！");
						parent.left.reload("<s:property value="attr"/>");
					}
					else
					{
						alert("修改IP失败，请重新分配或者稍后再试！");
					}
				},
			erro:function(xmlR,msg,other){alert(msg);}});
}
//保存专线ip到专线用户
function saveuser()
{
	$.ajax({ type: "POST",
			url: "<s:url value="/hgwipMgSys/assignIP!editUser.action"/>",
			data: {"attr":"<s:property value="attr"/>",
			"addrnum":$("input[@name='totaladdr']").val(),
			"usernamezw":encodeURIComponent($("input[@name='usernamezw']").val()),
			"usernameyw":encodeURIComponent($("input[@name='usernameyw']").val()),
			"usernamepyjc":encodeURIComponent($("input[@name='usernamepyjc']").val()),
			"usernameywjc":encodeURIComponent($("input[@name='usernameywjc']").val()),
			"address":encodeURIComponent($("input[@name='address']").val()),
			"netname":encodeURIComponent($("input[@name='netname']").val()),
			"netnamee":encodeURIComponent($("input[@name='netnamee']").val()),
			"netnamejc":encodeURIComponent($("input[@name='netnamejc']").val()),
			"cntmode":encodeURIComponent($("select[@name='cntmode']").val()),
			"cntspeed":encodeURIComponent($("select[@name='cntspeed']").val()),
			"cntaddr":encodeURIComponent($("input[@name='cntaddr']").val()),
			"localun":encodeURIComponent($("input[@name='localun']").val()),
			"rwaddr":encodeURIComponent($("select[@name='rwaddr']").val()),
			"usercountry":encodeURIComponent($("select[@name='usercountry']").val()),
			"purpose":encodeURIComponent($("input[@name='purpose']").val()),
			"memo":encodeURIComponent($("textarea[@name='memo']").val()),
			"managerhandle":encodeURIComponent($("input[@name='managerhandle']").val()),
			"managername":encodeURIComponent($("input[@name='managername']").val()),
			"managernamep":encodeURIComponent($("input[@name='managernamep']").val()),
			"managerduty":encodeURIComponent($("input[@name='managerduty']").val()),
			"managerphone":encodeURIComponent($("input[@name='managerphone']").val()),
			"manageremail":encodeURIComponent($("input[@name='manageremail']").val()),
			"managerfax":encodeURIComponent($("input[@name='managerfax']").val()),
			"manageraddress":encodeURIComponent($("input[@name='manageraddress']").val()),
			"manageraddrE":encodeURIComponent($("input[@name='manageraddrE']").val()),
			"managerpc":encodeURIComponent($("input[@name='managerpc']").val()),
			"techhandle":encodeURIComponent($("input[@name='techhandle']").val()),
			"techname":encodeURIComponent($("input[@name='techname']").val()),
			"technamep":encodeURIComponent($("input[@name='technamep']").val()),
			"techduty":encodeURIComponent($("input[@name='techduty']").val()),
			"techphone":encodeURIComponent($("input[@name='techphone']").val()),
			"techemail":encodeURIComponent($("input[@name='techemail']").val()),
			"techfax":encodeURIComponent($("input[@name='techfax']").val()),
			"techaddr":encodeURIComponent($("input[@name='techaddr']").val()),
			"techaddre":encodeURIComponent($("input[@name='techaddre']").val()),
			"techpc":encodeURIComponent($("input[@name='techpc']").val())
			},
			success:
				function(data)
				{
					if(data=="ok")
					{
						alert("保存修改成功！");
						parent.left.reload("<s:property value="attr"/>");
					}
					else
					{
						alert("保存修改失败，请重新保存或者稍后再试！");
					}
				},
			erro:function(xmlR,msg,other){alert(msg);}});
}
//回收ip地址
function recycleIp(attr)
{
if(window.confirm("此操作将会回收该IP，是否继续？"))
{
	$.ajax({ type: "POST",
			url: "<s:url value="/hgwipMgSys/assignIP!recycleIp.action"/>",
			data: {"attr":attr},
			success:
				function(data)
				{
					if(data=='ok')
					{
						alert("回收ip成功！");
						parent.left.reload("<s:property value="attr"/>");
					}
					else
					{
						alert("回收ip失败，请重新回收或者稍后再试！");
					}
				},
			erro:function(xmlR,msg,other){alert(msg);}});
}
}
$(function(){
	$().ajaxStart($.blockUI).ajaxStop($.unblockUI);
//获取地县的属地列表
	$("select[@name='city']").bind("change",function(){
		$.ajax({ type: "POST",
			url: "<s:url value="/hgwipMgSys/unAssignIP!getCountry.action"/>",
			data: {"city_id":$(this).attr("value")},
			success:
				function(data)
				{
					$("select[@name='countryName']").html(data);
				},
			erro:function(xmlR,msg,other){alert(msg);}});
});
//获取user地县的属地列表
	$("select[@name='rwaddr']").bind("change",function(){
		$.ajax({ type: "POST",
			url: "<s:url value="/hgwipMgSys/unAssignIP!getCountry.action"/>",
			data: {"city_id":$(this).attr("value")},
			success:
				function(data)
				{
					$("select[@name='countryName']").html(data);
				},
			erro:function(xmlR,msg,other){alert(msg);}});
});
<%--
//切换管理人员
	$("input[@name='manainput']").bind("click",function(){
		if($(this).attr("value")=='0')
		{
			$("tr[@name='mana0']").show();
			$("tr[@name='mana1']").hide();
		}
		else
		{
			$("tr[@name='mana1']").show();
			$("tr[@name='mana0']").hide();
		}
	});
	//切换技术人员
	$("input[@name='techinput']").bind("click",function(){
		if($(this).attr("value")=='0')
		{
			$("tr[@name='tech0']").show();
			$("tr[@name='tech1']").hide();
		}
		else
		{
			$("tr[@name='tech1']").show();
			$("tr[@name='tech0']").hide();
		}
	});
	--%>
});
</script>
</head>
<body>
<s:action name="getSubDetail" namespace="/hgwipMgSys" executeResult="true"
	id="detail">
	<s:param name="attr" value="attr"></s:param>
</s:action>
<input type="hidden" name="totaladdr"
	value="<s:property value="#detail.subnetDetail.totaladdr"/>" />

<s:if test="leaf=='false'">
	<!--地市和专线不一样-->

	<p style="margin-left: 1%"><a href="javascript:cancel();"
		class="black">取消该子网的划分</a></p>

</s:if>
<s:else>
	<p style="margin-left: 1%;"><a
		href="javascript:recycleIp('<s:property  value="attr"/>');"
		class="black">回收此IP资源</a></p>
	<s:if test="userstat==0">
		<table width="98%" height="30" border="0" align="center" cellpadding="0" cellspacing="0" class=green_gargtd>
			<tr>
				<td width="162" align="center" class="title_bigwhite">分配信息</td>
				<td></td>
			</tr>
		</table>
		<table width="98%" border=0 align="center" cellpadding="2"	cellspacing="1"  bgcolor=#999999>
			<tr bgcolor="#FFFFFF">
				<td width="20%" align=right>IP地址:</td>
				<td colspan="3" align=left><input type="text" name="subnet" readonly
					value="<s:property value="subnet"/>"></td>
			</tr>
			<tr bgcolor="#FFFFFF">
				<td width="20%" align=right>所属地市:</td>
				<td width="30%" align=left><s:set var="oldcity" value="city_id" /><select
					name="city">
					<option value="-1">==请选择==</option>
					<s:iterator value="firstCity" var="f">
						<option value="<s:property value="#f.city_id"/>"
							<s:property value="(#oldcity==#f.city_id)?'selected':''"/>><s:property
							value="city_name" /></option>
					</s:iterator>
				</select></td>
				<td width="20%" align=right>所属县市:</td>
				<td width="30%" align=left><s:set value="countryName" var="oldCountryName" /><select
					name="countryName">
					<s:iterator value="countryCity" var="c">
						<option value="<s:property value="#c.city_id"/>"
							<s:property value="(#c.city_id==#oldCountryName)?'selected':''"/>><s:property
							value="#c.city_name" /></option>
					</s:iterator>
				</select></td>
			</tr>
			<tr bgcolor="#FFFFFF">
				<td width="20%" align=right>分配时间:</td>
				<td colspan="3"align=left><input type="text" name="assigntime" readonly
					value="<s:property value="assigntime"/>" /></td>
			</tr>
			<tr bgcolor="#FFFFFF">
				<td width="20%" align=right>用途1:</td>
				<td colspan="3" align=left><select name="purpose1">
					<s:iterator value="purpose1">
						<option value="<s:property value="value"/>"
							<s:property value="purpose1Name==value?'selected':''"/>><s:property
							value="value" /></option>
					</s:iterator>
				</select></td>
			</tr>
			<tr bgcolor="#FFFFFF">
				<td width="20%" align=right>用途2:</td>
				<td colspan="3" align=left><select name="purpose2">
					<s:iterator value="purpose2">
						<option value="<s:property value="value"/>"
							<s:property value="purpose2Name==value?'selected':''"/>><s:property
							value="value" /></option>
					</s:iterator>
				</select></td>
			</tr>
			<tr bgcolor="#FFFFFF">
				<td width="20%" align=right>用途3:</td>
				<td colspan="3" align=left><select name="purpose3">
					<s:iterator value="purpose3">
						<option value="<s:property value="value"/>"
							<s:property value="purpose3Name==value?'selected':''"/>><s:property
							value="value" /></option>
					</s:iterator>
				</select></td>
			</tr>
			<tr bgcolor="#FFFFFF">
				<td width="20%" align=right>备注:</td>
				<td colspan="3" align=left><textarea name="comment"><s:property
					value="comment" /></textarea></td>
			</tr>
			<tr bgcolor="#FFFFFF">
				<td colspan="4" align=left><input type="submit" name="保存修改" value="保存修改"
					onclick="savecity();" /></td>
			</tr>
		</table>
	</s:if>
	<s:else>
		<s:if test="assign==5">
			<div
				style="margin-left: 1%; width: 98%; color: red; margin-top: 10px; font-size: x-large;">
			<s:if test="approve==1">
			网段上报审批没通过，请回收IP资源
			</s:if> <s:else>
			网段在上报审批中
			</s:else></div>
		</s:if>
		<s:else>
			<table width="98%" height="30" border="0" align="center" cellpadding="0" cellspacing="0" class=green_gargtd>
			<tr>
				<td width="162" align="center" class="title_bigwhite">分配信息</td>
			</tr>
		   </table>
			<table width="98%" border=0 align="center" cellpadding="2"	cellspacing="1"  bgcolor=#999999>
				<tr bgcolor="#FFFFFF">
					<td width="30%" align=right>用户名称:</td>
					<td width="70%" align=left><input type="text" name="usernamezw"
						value='<s:property value="params.usernamezw"/>'><font
						color="#FF3300">**</font></td>
				</tr>
				<tr bgcolor="#FFFFFF">
					<td width="30%" align=right>用户名称(英文):</td>
					<td width="70%" align=left><input type="text" name="usernameyw"
						value='<s:property value="params.usernameyw"/>'><font
						color="FF3300">**</font></td>
				</tr>
				<tr bgcolor="#FFFFFF">
					<td width="30%" align=right>用户拼音简称:</td>
					<td width="70%" align=left><input type="text" name="usernamepyjc"
						value='<s:property value="params.usernamepyjc"/>'></td>
				</tr>
				<tr bgcolor="#FFFFFF">
					<td width="30%" align=right>用户英文简称:</td>
					<td width="70%" align=left><input type="text" name="usernameywjc"
						value='<s:property value="params.usernameywjc"/>'></td>
				</tr>
				<tr bgcolor="#FFFFFF">
					<td width="30%" align=right>用户地址:</td>
					<td width="70%" align=left><input type="text" name="address"
						value='<s:property value="params.address"/>'></td>
				</tr>
				<tr bgcolor="#FFFFFF">
					<td width="30%" align=right>用户网络名：</td>
					<td width="70%" align=left><input type="text" name="netname"
						value='<s:property value="params.netname"/>'></td>
				</tr>
				<tr bgcolor="#FFFFFF">
					<td width="30%" align=right>用户网络英文名:</td>
					<td width="70%" align=left><input name="netnamee" type="text"
						value='<s:property value="params.netnamee"/>' size="32"
						maxlength="32"> <font color="#FF3300">**</font> <br>
					<font color="#FF3300">范例：SUZHOU-HONGFA-ELECTRONIC-CORP（字段之间用-隔开，请勿使用空格）</font>
					</td>
				</tr>
				<tr bgcolor="#FFFFFF">
					<td width="30%" align=right>用户网络名简称：</td>
					<td width="70%" align=left><input type="text" name="netnamejc" maxlength="10"
						value='<s:property value="params.netnamejc"/>'></td>
				</tr>
				<tr bgcolor="#FFFFFF">
					<td width="30%" align=right>用户接入方式:</td>
					<td width="70%" align=left><select name="cntmode">
						<option value="-1">==请选择==</option>
						<s:iterator value="cntmodeList">
							<option value="<s:property value="value"/>"
								<s:property value="params.cntmode==value?'selected':''"/>>==<s:property
								value="value" />==</option>
						</s:iterator>
					</select></td>
				</tr>
				<tr bgcolor="#FFFFFF">
					<td width="30%" align=right>用户接入速率:</td>
					<td width="70%" align=left><select name="cntspeed">
						<option value="-1">==请选择==</option>
						<s:iterator value="cntspeedList">
							<option value="<s:property value="value"/>"
								<s:property value="params.cntspeed==value?'selected':''"/>>==<s:property
								value="value" />==</option>
						</s:iterator>
					</select></td>
				</tr>
				<tr bgcolor="#FFFFFF">
					<td width="30%" align=right>互联地址:</td>
					<td width="70%" align=left><input type="text" name="cntaddr"
						value="<s:property value="params.cntaddr"/>" /></td>
				</tr>
				<tr bgcolor="#FFFFFF">
					<td width="30%" align=right>本地用户编号:</td>
					<td width="70%" align=left><input type="text" name="localun"
						value="<s:property value="params.localun"/>" /></td>
				</tr>
				<tr bgcolor="#FFFFFF">
					<td width="30%" align=right>申请日期:</td>
					<td width="70%" align=left><input type="text" name="applydate" readonly="true"
						size="10" value='<s:property value="params.applydate"/>'></td>
				</tr>
				<tr bgcolor="#FFFFFF">
					<td width="30%" align=right>入网地点:</td>
					<td width="70%" align=left><select name="rwaddr">
						<option value="-1">==请选择==</option>
						<s:iterator value="firstCity">
							<option value="<s:property value="city_id"/>"
								<s:property value="rwaddr==city_id?'selected':''"/>>==<s:property
								value="city_name" />==</option>
						</s:iterator>
					</select></td>
				</tr>
				<tr bgcolor="#FFFFFF">
					<td width="30%" align=right>县:</td>
					<td width="70%" align=left><select name="countryName">
						<s:iterator value="countryCity">
							<option value="<s:property value="country_name"/>"
								<s:property value="countryName==country_name?'selected':''"/>>==<s:property
								value="country_name" />==</option>
						</s:iterator>
					</select></td>
				</tr>
				<tr bgcolor="#FFFFFF">
					<td width="30%" align=right>入网用途：</td>
					<td width="70%" align=left><input type="text" name="purpose"
						value='<s:property value="params.purpose"/>'></td>
				</tr>
				<tr bgcolor="#FFFFFF">
					<td width="30%" align=right>备注:</td>
					<td width="70%" align=left><textarea name="memo"><s:property
						value="params.memo" /></textarea></td>
					<%--
			</tr>
				<td class="left_tab_back">用户网络管理负责人:</td>
				<td><input type="radio" name="manainput" checked value="0"
					id="mana0" /><label for="mana0">Handle </label>
					<input type="radio"
					name="manainput" value="1" id="mana1" /><label for="mana1">资料</label>
					 </td>
			</tr>
			--%>
				<tr bgcolor="#FFFFFF" name="mana0">
					<td width="30%" align=right>用户网络管理负责人Handle:</td>
					<td width="70%" align=left><input type="text" name="managerhandle"
						value='<s:property value="params.managerhandle"/>'><font
						color="#FF3300">**</font></td>
				</tr>
				<%--
									<tr name="mana1" style="display: none;">
										<td>用户网络管理负责人姓名:</td>
										<td><input type="text" name="managername"
											value='<s:property value="params.managername"/>'></td>
									</tr>
									<tr name="mana1" style="display: none;">
										<td class="left_tab_back">用户网络管理负责人姓名拼音:</td>
										<td><input type="text" name="managernamep"
											value='<s:property value="params.managernamep"/>'><font
											color="#FF3300">**(至少包含两部分，中间用空格隔开)</font></td>
									</tr>
									<tr name="mana1" style="display: none;">
										<td class="left_tab_back">用户网络管理负责人职务:</td>
										<td><input type="text" name="managerduty"
											value='<s:property value="params.managerduty"/>'></td>
									</tr>
									<tr name="mana1" style="display: none;">
										<td class="left_tab_back">用户网络管理负责人电话:</td>
										<td><input type="text" name="managerphone"
											value='<s:property value="params.managerphone"/>'> <font
											color="#FF3300">**</font> <br>
										<font color="#FF3300">范例：25-6588783（区号前面不加0）、131011111111</font></td>
										</td>
									</tr>

			<tr name="mana1" style="display: none;">
				<td class="left_tab_back">用户网络管理负责人E-mail:</td>
				<td><input type="text" name="manageremail"
					value='<s:property value="params.managerphone"/>'> <font
					color="#FF3300">**</font> <font color="#FF3300">(填写用户网络管理负责人的邮件地址)</font></td>
			</tr>
			<tr name="mana1" style="display: none;">
				<td class="left_tab_back">用户网络管理负责人传真:</td>
				<td><input type="text" name="managerfax"
					value='<s:property value="params.managerphone"/>'></td>
			</tr>
			<tr name="mana1" style="display: none;">
				<td class="left_tab_back">用户网络管理负责人通信地址:</td>
				<td><input type="text" name="manageraddress"
					value='<s:property value="params.manageraddress"/>'></td>
			</tr>
			<tr name="mana1" style="display: none;">
				<td class="left_tab_back">用户网络管理负责人英文通信地址:</td>
				<td><input type="text" name="manageraddrE"
					value='<s:property value="params.manageraddrE"/>'> <font
					color="#FF3300">**</font></td>
			</tr>
			<tr name="mana1" style="display: none;">
				<td class="left_tab_back">用户网络管理负责人邮政编码:</td>
				<td><input type="text" name="managerpc"
					value='<s:property value="params.managerpc"/>'></td>
			</tr>
			<tr>
				<td class="left_tab_back">用户网络技术负责人:</td>
				<td> <input type="radio" name="techinput" value="0" id="tech0" /><label
					for="tech0"> Handle</label>  <input type="radio"
					name="techinput" value="1" id="tech1" checked /><label for="tech1">
				资料</label></td>
			</tr>
			<tr name="tech0" style="display: none;">
				<td class="left_tab_back">用户网络技术负责人Handle:</td>
				<td><input type="text" name="techhandle"
					value='<s:property value="params.techhandle"/>'><font
					color="#FF3300">**</font></td>
			</tr>
			--%>
				<tr bgcolor="#FFFFFF" name="tech1">
					<td width="30%" align=right>用户网络技术负责人姓名:</td>
					<td width="70%" align=left><input type="text" name="techname"
						value='<s:property value="params.techname"/>'></td>
				</tr>
				<tr name="tech1" bgcolor="#FFFFFF">
					<td width="30%" align=right>用户网络技术负责人姓名拼音:</td>
					<td width="70%" align=left><input type="text" name="technamep"
						value='<s:property value="params.technamep"/>'> <font
						color="#FF3300">**(至少包含两部分，中间用空格隔开)</font></td>
				</tr>
				<tr name="tech1" bgcolor="#FFFFFF">
					<td width="30%" align=right>用户网络技术负责人职务:</td>
					<td width="70%" align=left><input type="text" name="techduty"
						value='<s:property value="params.techduty"/>'></td>
				</tr>
				<tr name="tech1" bgcolor="#FFFFFF">
					<td width="30%" align=right>用户网络技术负责人电话:</td>
					<td width="70%" align=left><input type="text" name="techphone"
						value='<s:property value="params.techphone"/>'><font
						color="#FF3300">**</font> <br>
					<font color="#FF3300">范例：25-6588783（区号前面不加0）、131011111111</font></td>
				</tr>
				<tr name="tech1" bgcolor="#FFFFFF">
					<td width="30%" align=right>用户网络技术负责人E-mail:</td>
					<td width="70%" align=left><input type="text" name="techemail"
						value='<s:property value="params.techemail"/>'> <font
						color="#FF3300">**</font> <font color="#FF3300">(填写用户网络技术负责人的邮件地址)</font></td>
				</tr>
				<tr name="tech1" bgcolor="#FFFFFF">
					<td width="30%" align=right>用户网络技术负责人传真:</td>
					<td width="70%" align=left><input type="text" name="techfax"
						value='<s:property value="params.techfax"/>'></td>
				</tr>
				<tr name="tech1" bgcolor="#FFFFFF">
					<td width="30%" align=right>用户网络技术负责人通信地址:</td>
					<td width="70%" align=left><input type="text" name="techaddr"
						value='<s:property value="params.techaddr"/>'></td>
				</tr>
				<tr name="tech1" bgcolor="#FFFFFF">
					<td width="30%" align=right>用户网络技术负责人英文通信地址:</td>
					<td width="70%" align=left><input type="text" name="techaddre"
						value='<s:property value="params.techaddre"/>'><font
						color="#FF3300">**</font></td>
				</tr>
				<tr name="tech1" bgcolor="#FFFFFF">
					<td width="30%" align=right>用户网络技术负责人邮政编码:</td>
					<td width="70%" align=left><input type="text" name="techpc"
						value="<s:property value="params.techpc"/>"></td>
				</tr>
				<tr bgcolor="#FFFFFF">
					<td colspan="2" align=right><input type="submit" name="保存修改" value="保存修改"
						onclick="saveuser();" /></td>
				</tr>
			</table>
		</s:else>
	</s:else>
</s:else>
</body>
</html>
<%@ include file="../foot.jsp"%>
