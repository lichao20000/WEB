<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>Insert title here</title>
<link href="<s:url value="../css/css_green.css"/>" rel="stylesheet"
	type="text/css">
<script type="text/javascript"
	src="<s:url value="/Js/jquery.js"/>"></script>

<script type="text/javascript"
	src="<s:url value="/Js/jQeuryExtend-linkage.js"/>"></script>
<script type="text/javascript"
	src="<s:url value="/Js/jquery.blockUI.js"/>"></script>
<script type="text/javascript">
//划分子网
function gocut()
{
	var li = $("#link").attr("checked")==true?1:0;
	var sublen=$("#sublen").attr("value");
	if(sublen==-1)
	{
		alert("请选择掩码位数或者子网个数！");
		return;
	}
	$.ajax({ type: "POST",
			url: "<s:url value="/hgwipMgSys/unAssignIP!assignSubNet.action"/>",
			data: {"attr":"<s:property value="attr"/>","link":li,"sublen":sublen},
			success:
				function(data)
				{
					if(data=="ok")
					{
						alert("划分成功！");
						parent.left.reload("<s:property value="attr"/>");
					}
					else if(data=="no")
					{
						alert("该网络不能划分子网！");
					}
					else if(data=="over")
					{
						alert("划分子网络层次过多！");
					}
					else
					{
						alert("划分失败，请重新划分或者稍后再试！");
					}
				},
			erro:function(xmlR,msg,other){alert(msg);}});
}
//分配ip到地市
function assignCity()
{
	if($("select[@name='city']").val()=="")
	{
		alert("请选择地市!");
		$("select[@name='city']").focus();
		return;

	}
	$.ajax({ type: "POST",
			url: "<s:url value="/hgwipMgSys/unAssignIP!assignIPCity.action"/>",
			data: {"attr":"<s:property value="attr"/>",
			"city_id":$("select[@name='city']").attr("value"),
			"countryName":encodeURIComponent($("select[@name='country']").val()),
			"purpose1Name":encodeURIComponent($("select[@name='purpose1']").attr("value")),
			"purpose2Name":encodeURIComponent($("select[@name='purpose2']").attr("value")),
			"purpose3Name":encodeURIComponent($("select[@name='purpose3']").attr("value")),
			"comment":encodeURIComponent($("textarea[@name='comment']").text())},
			success:
				function(data)
				{
					if(data=="ok")
					{
						alert("分配IP成功！");
						parent.left.reload("<s:property value="attr"/>");
					}
					else
					{
						alert("分配IP失败，请重新分配或者稍后再试！");
					}
				},
			erro:function(xmlR,msg,other){alert(msg);}});
}
//分配ip到专线用户
function assignUser()
{
	if($("input[@name='usernamezw']").val()=="")
	{
		alert("请填写用户名!");
		$("input[@name='usernamezw']").focus();
		return;
	}
	if($("input[@name='usernameyw']").val()=="")
	{
		alert("请填写用户名称（英文）!");
		$("input[@name='usernameyw']").focus();
		return;
	}
	if($("input[@name='netnamee']").val()=="")
	{
		alert("请填写用户网络英文名");
		$("input[@name='netnamee']").focus();
		return;
	}
	if($("select[@name='rwaddr']").val()==-1)
	{
		alert("请填写日入网地点");
		$("select[@name='rwaddr']").focus();
		return;
	}
	if($("input[@name='managerhandle']").val()=="")
	{
		alert("请填写用户网络管理负责人Handle");
		$("input[@name='managerhandle']").focus();
		return;
	}
	if($("input[@name='technamep']").val()=="")
	{
		alert("请填写用户网络技术负责人姓名拼音");
		$("input[@name='technamep']").focus();
		return;
	}
	if($("input[@name='techphone']").val()=="")
	{
		alert("请填写用户网络技术负责人电话");
		$("input[@name='techphone']").focus();
		return;
	}
	if($("input[@name='techemail']").val()=="")
	{
		alert("请填写用户网络技术负责人E-mail");
		$("input[@name='techemail']").focus();
		return;
	}
	if($("input[@name='techaddre']").val()=="")
	{
		alert("请填写用户网络技术负责人英文通信地址");
		$("input[@name='techaddre']").focus();
		return;
	}
	$.ajax({ type: "POST",
			url: "<s:url value="/hgwipMgSys/unAssignIP!assignIPUser.action"/>",
			data: {"attr":"<s:property value="attr"/>",
			"addrnum":"<s:property value="addrnum"/>",
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
						alert("分配IP成功！");
					}
					else
					{
						alert("分配IP失败，请重新分配或者稍后再试！");
					}
				},
			erro:function(xmlR,msg,other){alert(msg);}});
}
$(function(){
	//修改子网长度，动态改变子网个数
	$().ajaxStart($.blockUI).ajaxStop($.unblockUI);
	$("select[@name='sublen']").bind("change",function(){
		var index = $(this).attr("selectedIndex");
		if(typeof index =='undefined')
		{
		$("select[@name='sbcnt']").attr("selectedIndex",0);
		}
		else
		{
		$("select[@name='sbcnt']").attr("selectedIndex",index);
		}
		if($(this).val()==-1)
		{
		$("#subnetmask").attr("value","");
		return;
		}
		$.ajax({ type: "POST",
			url: "<s:url value="/hgwipMgSys/ipMg!getNtMk.action"/>",
			data: "netMaskLen="+$(this).attr("value"),
			success:
				function(data)
				{
					$("#subnetmask").attr("value",data);
				},
			erro:function(xmlR,msg,other){alert(msg);}});
	});
	//动态改变子网个数，修改长度
		$("select[@name='sbcnt']").bind("change",function(){
		var index = $(this).attr("selectedIndex");
		if(typeof index =='undefined')
		{
		$("select[@name='sublen']").attr("selectedIndex",0);
		}
		else
		{
		$("select[@name='sublen']").attr("selectedIndex",index);
		}
		if($(this).val()==-1)
		{
		$("#subnetmask").attr("value","");
		return;
		}
		$.ajax({ type: "POST",
			url: "<s:url value="/hgwipMgSys/ipMg!getNtMk.action"/>",
			data: "netMaskLen="+$(this).attr("value"),
			success:
				function(data)
				{
					$("#subnetmask").attr("value",data);
				},
			erro:function(xmlR,msg,other){alert(msg);}});
	});
	//获取地县的属地列表
	$("select[@name='city']").bind("change",function(){
		$.ajax({ type: "POST",
			url: "<s:url value="/hgwipMgSys/unAssignIP!getCountry.action"/>",
			data: {"city_id":$(this).attr("value")},
			success:
				function(data)
				{
					$("select[@name='country']").html(data);
				},
			erro:function(xmlR,msg,other){alert(msg);}});
	});
	//获取专线地县的属地列表
	$("select[@name='rwaddr']").bind("change",function(){
		$.ajax({ type: "POST",
			url: "<s:url value="/hgwipMgSys/unAssignIP!getCountry.action"/>",
			data: {"city_id":$(this).attr("value")},
			success:
				function(data)
				{
					$("select[@name='usercountry']").html(data);
				},
			erro:function(xmlR,msg,other){alert(msg);}});
	});
	/**
	临时注释掉，因为江苏不需要资料切换
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
	**/
});
function checkF()
{
	if($("input[@name='assignInfo']").val()==null||$("input[@name='assignInfo']").val()=="")
	{
		alert("请填写用户名称！");
		$("input[@name='assignInfo']").focus();
		return false;
	}
	if($("input[@name='appfile']").val()==null||$("input[@name='appfile']").val()=="")
	{
		alert("请选择上传的文件路径！");
		$("input[@name='appfile']").focus();
		return false;
	}
	return true;
}
</script>
<STYLE type="text/css">
form {
	margin-top: 0px;
}
</STYLE>
</head>
<body>
<s:if test="act=='cut'">
	<table width="98%" border=0 align="center" cellpadding="2"	cellspacing="1"  bgcolor=#999999>
		<tr bgcolor="#FFFFFF">
			<td colspan="2"><label>掩码位数:<select name="sublen"
				id="sublen">
				<option value="-1">==请选择==</option>
				<s:property value="icnt" escapeHtml="false" />
			</select><label for="">子网个数:</label><select name="sbcnt">
				<option value="-1">==请选择==</option>
				<s:property value="subcnt" escapeHtml="false" />
			</select><label>子网掩码:</label><input type="text" id="subnetmask" readonly /></td>
		</tr>
		<tr class="green_foot">
			<td colspan="2"><input type="checkbox" name="link" value="1"
				id="link" /><label for="link">级联划分</label></td>
		</tr>
		<tr class="green_foot">
			<td colspan="2">
			<button onclick="gocut();">确定划分</button>
			</td>
		</tr>
	</table>
</s:if>
<s:elseif test="act=='give'">
	<table width="98%" height="30" border="0" align="center" cellpadding="0" cellspacing="0" class=green_gargtd>
		<tr>
			<td width="162" align="center" class="title_bigwhite">分配信息</td>
		</tr>
	</table>
	<s:if test="userstat==0">
		<table width="98%"  border=0 align="center" cellpadding="2"	cellspacing="1"  bgcolor=#999999>

			<tr bgcolor="#FFFFFF">
				<td width="20%" align=right>IP地址:</td>
				<td colspan="3" align=left><input type="text" name="subnet" readonly
					value="<s:property value="subnet"/>"></td>
			</tr>
			<tr bgcolor="#FFFFFF">
				<td width="20%" align=right>所属地市:</td>
				<td width="30%" align=left><select name="city">
					<option value="">==请选择==</option>
					<s:iterator value="firstCity">
						<option value="<s:property value="city_id"/>">==<s:property
							value="city_name" />==</option>
					</s:iterator>
				</select></td>
				<td width="20%" align=right>所属县市:</td>
				<td width="30%" align=left><select name="country">
				</select></td>
			</tr>
			<tr bgcolor="#FFFFFF">
				<td width="20%" align=right>用途1:</td>
				<td colspan="3" align=left><select name="purpose1">
					<s:iterator value="purpose1">
						<option value="<s:property value="value"/>"><s:property
							value="value" /></option>
					</s:iterator>
				</select></td>
			</tr>
			<tr bgcolor="#FFFFFF">
				<td width="20%" align=right>用途2:</td>
				<td colspan="3" align=left><select name="purpose2">
					<s:iterator value="purpose2">
						<option value="<s:property value="value"/>"><s:property
							value="value" /></option>
					</s:iterator>
				</select></td>
			</tr>
			<tr bgcolor="#FFFFFF">
				<td width="20%" align=right>用途3:</td>
				<td colspan="3" align=left><select name="purpose3">
					<s:iterator value="purpose3">
						<option value="<s:property value="value"/>"><s:property
							value="value" /></option>
					</s:iterator>
				</select></td>
			</tr>
			<tr bgcolor="#FFFFFF">
				<td width="20%" align=right>备注:</td>
				<td colspan="3" align=left><textarea name="comment"></textarea></td>
			</tr>
			<tr bgcolor="#FFFFFF">
				<td colspan="4" align=right><input type="submit" name="保 存" value="保 存"
					onclick="assignCity();" /></td>
			</tr>
		</table>
	</s:if>
	<s:else>
		<s:if test="allow">
			<table width="98%" border=0 align="center" cellpadding="2"	cellspacing="1"  bgcolor=#999999>
				<tr bgcolor="#FFFFFF">
					<td width="30%" align=right>用户名称:</td>
					<td width="70%" align=left><input type="text" name="usernamezw" value=''><font
						color="#FF3300">**</font></td>
				</tr>
				<tr bgcolor="#FFFFFF">
					<td width="30%" align=right>用户名称(英文):</td>
					<td width="70%" align=left><input type="text" name="usernameyw" value=''><font
						color="#FF3300">**</font></td>
				</tr>
				<tr bgcolor="#FFFFFF">
					<td width="30%" align=right>用户拼音简称:</td>
					<td width="70%" align=left><input type="text" name="usernamepyjc" value=''></td>
				</tr>
				<tr bgcolor="#FFFFFF">
					<td width="30%" align=right>用户英文简称:</td>
					<td width="70%" align=left><input type="text" name="usernameywjc" value=''></td>
				</tr>
				<tr bgcolor="#FFFFFF">
					<td width="30%" align=right>用户地址:</td>
					<td width="70%" align=left><input type="text" name="address" value=''></td>
				</tr>
				<tr bgcolor="#FFFFFF">
					<td width="30%" align=right>用户网络名：</td>
					<td width="70%" align=left><input type="text" name="netname" value=''></td>
				</tr>
				<tr bgcolor="#FFFFFF">
					<td width="30%" align=right>用户网络英文名:</td>
					<td width="70%" align=left><input name="netnamee" type="text" value='' size="32"
						maxlength="32"> <font color="#FF3300">**</font> <br>
					<font color="#FF3300">范例：SUZHOU-HONGFA-ELECTRONIC-CORP（字段之间用-隔开，请勿使用空格）</font>
					</td>
				</tr>
				<tr bgcolor="#FFFFFF">
					<td width="30%" align=right>用户网络名简称：</td>
					<td width="70%" align=left><input type="text" name="netnamejc" maxlength="10"
						value=''></td>
				</tr>
				<tr bgcolor="#FFFFFF">
					<td width="30%" align=right>用户接入方式:</td>
					<td width="70%" align=left><select name="cntmode">
						<option>==请选择==</option>
						<s:iterator value="cntmodeList">
							<option value="<s:property value="value"/>">==<s:property
								value="value" />==</option>
						</s:iterator>
					</select></td>
				</tr>
				<tr bgcolor="#FFFFFF">
					<td width="30%" align=right>用户接入速率:</td>
					<td width="70%" align=left><select name="cntspeed">
						<option>==请选择==</option>
						<s:iterator value="cntspeedList">
							<option value="<s:property value="value"/>">==<s:property
								value="value" />==</option>
						</s:iterator>
					</select></td>
				</tr>
				<tr bgcolor="#FFFFFF">
					<td width="30%" align=right>互联地址:</td>
					<td width="70%" align=left><input type="text" name="cntaddr" value=""></td>
				</tr>
				<tr bgcolor="#FFFFFF">
					<td width="30%" align=right>本地用户编号:</td>
					<td width="70%" align=left><input type="text" name="localun" value=""></td>
				</tr>
				<tr bgcolor="#FFFFFF">
					<td width="30%" align=right>入网地点:</td>
					<td width="70%" align=left><select name="rwaddr">
						<option value="-1">==请选择==</option>
						<s:iterator value="firstCity">
							<option value="<s:property value="city_id"/>">==<s:property
								value="city_name" />==</option>
						</s:iterator>
					</select><font color="#FF3300">**</font></td>
				</tr>
				<tr bgcolor="#FFFFFF">
					<td width="30%" align=right>县:</td>
					<td width="70%" align=left><select name="usercountry"></select></td>
				</tr>
				<tr bgcolor="#FFFFFF">
					<td width="30%" align=right>入网用途:</td>
					<td width="70%" align=left><input type="text" name="purpose" value=''></td>
				</tr>
				<tr bgcolor="#FFFFFF">
					<td width="30%" align=right>备注:</td>
					<td width="70%" align=left><textarea name="memo"></textarea></td>
				</tr>
				<%--
				<tr>
					<td width="30%" align=right>用户网络管理负责人:</td>
					<td><input type="radio" name="manainput" checked value="0"
						id="mana0" /><label for="mana0">Handle</label>
						临时修改江苏这边不需要切换，如果放开，请将上边的js方法也放开
						 <input
						type="radio" name="manainput" value="1" id="mana1" /><label
						for="mana1">资料</label>
						</td>
				</tr>
				--%>

				<tr name="mana0" bgcolor="#FFFFFF">
					<td width="30%" align=right>用户网络管理负责人Handle:</td>
					<td width="70%" align=left><input type="text" name="managerhandle" value=''><font
						color="#FF3300">**</font></td>
				</tr>
				<%--
				<tr name="mana1" style="display: none;">
					<td width="30%" align=right>用户网络管理负责人姓名:</td>
					<td><input type="text" name="managername" value=''></td>
				</tr>
				<tr name="mana1" style="display: none;">
					<td width="30%" align=right>用户网络管理负责人姓名拼音:</td>
					<td><input type="text" name="managernamep" value=''><font
						color="#FF3300">**(至少包含两部分，中间用空格隔开)</font></td>
				</tr>
				<tr name="mana1" style="display: none;">
					<td width="30%" align=right>用户网络管理负责人职务:</td>
					<td><input type="text" name="managerduty" value=''></td>
				</tr>
				<tr name="mana1" style="display: none;">
					<td width="30%" align=right>用户网络管理负责人电话:</td>
					<td><input type="text" name="managerphone" value=''> <font
						color="#FF3300">**</font> <br>
					<font color="#FF3300">范例：25-6588783（区号前面不加0）、131011111111</font></td>
					</td>
				</tr>
				<tr name="mana1" style="display: none;">
					<td width="30%" align=right>用户网络管理负责人E-mail:</td>
					<td><input type="text" name="manageremail" value=''> <font
						color="#FF3300">**</font> <font color="#FF3300">(填写用户网络管理负责人的邮件地址)</font></td>
				</tr>
				<tr name="mana1" style="display: none;">
					<td width="30%" align=right>用户网络管理负责人传真:</td>
					<td><input type="text" name="managerfax" value=''></td>
				</tr>
				<tr name="mana1" style="display: none;">
					<td width="30%" align=right>用户网络管理负责人通信地址:</td>
					<td><input type="text" name="manageraddress" value=''></td>
				</tr>
				<tr name="mana1" style="display: none;">
					<td width="30%" align=right>用户网络管理负责人英文通信地址:</td>
					<td><input type="text" name="manageraddrE" value=''> <font
						color="#FF3300">**</font></td>
				</tr>
				<tr name="mana1" style="display: none;">
					<td width="30%" align=right>用户网络管理负责人邮政编码:</td>
					<td><input type="text" name="managerpc" value=''></td>
				</tr>
				<tr>
					<td width="30%" align=right>用户网络技术负责人:</td>
					<td>
					<input type="radio" name="techinput" value="0" id="tech0" /><label
						for="tech0"> Handle</label>
						 <input type="radio" name="techinput" value="1" id="tech1"
						checked /><label for="tech1"> 资料</label></td>
				</tr>
				<tr name="tech0" style="display: none;">
					<td width="30%" align=right>用户网络技术负责人Handle:</td>
					<td><input type="text" name="techhandle" value=''><font
						color="#FF3300">**</font></td>
				</tr>
				--%>
				<tr name="tech1" bgcolor="#FFFFFF">
					<td width="30%" align=right>用户网络技术负责人姓名:</td>
					<td width="70%" align=left><input type="text" name="techname" value=''></td>
				</tr>
				<tr name="tech1" bgcolor="#FFFFFF">
					<td width="30%" align=right>用户网络技术负责人姓名拼音:</td>
					<td width="70%" align=left><input type="text" name="technamep" value=''> <font
						color="#FF3300">**(至少包含两部分，中间用空格隔开)</font></td>
				</tr>
				<tr name="tech1" bgcolor="#FFFFFF">
					<td width="30%" align=right>用户网络技术负责人职务:</td>
					<td width="70%" align=left><input type="text" name="techduty" value=''></td>
				</tr>
				<tr name="tech1" bgcolor="#FFFFFF">
					<td width="30%" align=right>用户网络技术负责人电话:</td>
					<td width="70%" align=left><input type="text" name="techphone" value=''><font
						color="#FF3300">**</font> <br>
					<font color="#FF3300">范例：25-6588783（区号前面不加0）、131011111111</font></td>
				</tr>
				<tr name="tech1" bgcolor="#FFFFFF">
					<td width="30%" align=right>用户网络技术负责人E-mail:</td>
					<td width="70%" align=left><input type="text" name="techemail" value=''> <font
						color="#FF3300">**</font> <font color="#FF3300">(填写用户网络技术负责人的邮件地址)</font></td>
				</tr>
				<tr name="tech1" bgcolor="#FFFFFF">
					<td width="30%" align=right>用户网络技术负责人传真:</td>
					<td width="70%" align=left><input type="text" name="techfax" value=''></td>
				</tr>
				<tr name="tech1" bgcolor="#FFFFFF">
					<td width="30%" align=right>用户网络技术负责人通信地址:</td>
					<td width="70%" align=left><input type="text" name="techaddr" value=''></td>
				</tr>
				<tr name="tech1" bgcolor="#FFFFFF">
					<td width="30%" align=right>用户网络技术负责人英文通信地址:</td>
					<td width="70%" align=left><input type="text" name="techaddre" value=''><font
						color="#FF3300">**</font></td>
				</tr>
				<tr name="tech1" bgcolor="#FFFFFF">
					<td width="30%" align=right>用户网络技术负责人邮政编码:</td>
					<td width="70%" align=left><input type="text" name="techpc" value=""></td>
				</tr>
				<tr bgcolor="#FFFFFF">
					<td colspan="2" align=right><input type="submit" name="保 存" value="保 存"
						onclick="assignUser();" /></td>
				</tr>
			</table>
		</s:if>
		<s:else>
			<!-- 不允许划分，需要申请 -->
			<form method="post" ENCTYPE="multipart/form-data"
				action="<s:url value="/hgwipMgSys/userappfp.action"/>"	onsubmit="return checkF();">
			<table width="98%" border=1 align="center" cellpadding="0"
				cellspacing="0" class="table">
				<input type="hidden" name="attr" value="<s:property value="attr"/>" />
				<tr>
					<td width="30%" align=right>申请局:</td>
					<td><input type="hidden" value="<s:property value="rwaddr"/>"
						name="rwaddr" /> <s:if test="firstCity!=null">
						<s:iterator value="firstCity">
							<s:if test="city_id==rwaddr">
								<input type="text" value="<s:property value="city_name"/>"
									name="city_name" readonly />
							</s:if>
						</s:iterator>
					</s:if> <s:else>
						<input type="text" value="<s:property value="city_name"/>"
							name="city_name" />
					</s:else></td>
				</tr>
				<tr>
					<td width="30%" align=right>IP地址:</td>
					<td><input type="text" value="<s:property value="subnet"/>"
						name="subnet" readonly /></td>
				</tr>
				<tr>
					<td width="30%" align=right>地址个数:</td>
					<td><input type="text" value="<s:property value="addrnum"/>"
						name="addrnum" readonly /></td>
				</tr>
				<tr>
					<td width="30%" align=right>用户名称:</td>
					<td><input type="text" value="" name="assignInfo" size=40 /><span
						style="color: red">*</span></td>
				</tr>
				<tr>
					<td width="30%" align=right>申请表附件:</td>
					<td><input type="file" name="appfile" /><span
						style="color: red">*</span></td>
				</tr>
				<tr>
					<td width="30%" align=right>附加说明:</td>
					<td><textarea name="comment"></textarea></td>
				</tr>
				<tr>
					<td colspan="2" align= right><input type="submit" name="提交申请" value="提交申请" /></td>
				</tr>
			</table>
			</form>
			<script type="text/javascript">
			var stat ="<s:property value="stat"/>";
			if(stat=="ok")
			{
				alert("申请成功，请等待审批");
				parent.left.reload("<s:property value="attr"/>");
			}
			else if(stat=="fail")
			{
				alert("申请失败，请重新申请，或者稍后在试!");
			}
			</script>
		</s:else>
	</s:else>
</s:elseif>
</body>
</html>
<%@ include file="../foot.jsp"%>
