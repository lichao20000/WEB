<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<%--
/**
 * ip��ַ�����ipȡ��/���յĹ���action
 *
 * @author ��־��(5194) tel:13701409234
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
//ȡ����������
function cancel(url)
{
	if(window.confirm("�ò�������ȡ����������ȷ��ִ�У�"))
	{
		window.location.href="<s:url value="/hgwipMgSys/assignIP!cancelSubNet.action"><s:param value="attr" name="attr"/></s:url>";
	}
}
//�����޸�ip������
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
						alert("�޸�IP�ɹ���");
						parent.left.reload("<s:property value="attr"/>");
					}
					else
					{
						alert("�޸�IPʧ�ܣ������·�������Ժ����ԣ�");
					}
				},
			erro:function(xmlR,msg,other){alert(msg);}});
}
//����ר��ip��ר���û�
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
						alert("�����޸ĳɹ���");
						parent.left.reload("<s:property value="attr"/>");
					}
					else
					{
						alert("�����޸�ʧ�ܣ������±�������Ժ����ԣ�");
					}
				},
			erro:function(xmlR,msg,other){alert(msg);}});
}
//����ip��ַ
function recycleIp(attr)
{
if(window.confirm("�˲���������ո�IP���Ƿ������"))
{
	$.ajax({ type: "POST",
			url: "<s:url value="/hgwipMgSys/assignIP!recycleIp.action"/>",
			data: {"attr":attr},
			success:
				function(data)
				{
					if(data=='ok')
					{
						alert("����ip�ɹ���");
						parent.left.reload("<s:property value="attr"/>");
					}
					else
					{
						alert("����ipʧ�ܣ������»��ջ����Ժ����ԣ�");
					}
				},
			erro:function(xmlR,msg,other){alert(msg);}});
}
}
$(function(){
	$().ajaxStart($.blockUI).ajaxStop($.unblockUI);
//��ȡ���ص������б�
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
//��ȡuser���ص������б�
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
//�л�������Ա
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
	//�л�������Ա
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
	<!--���к�ר�߲�һ��-->

	<p style="margin-left: 1%"><a href="javascript:cancel();"
		class="black">ȡ���������Ļ���</a></p>

</s:if>
<s:else>
	<p style="margin-left: 1%;"><a
		href="javascript:recycleIp('<s:property  value="attr"/>');"
		class="black">���մ�IP��Դ</a></p>
	<s:if test="userstat==0">
		<table width="98%" height="30" border="0" align="center" cellpadding="0" cellspacing="0" class=green_gargtd>
			<tr>
				<td width="162" align="center" class="title_bigwhite">������Ϣ</td>
				<td></td>
			</tr>
		</table>
		<table width="98%" border=0 align="center" cellpadding="2"	cellspacing="1"  bgcolor=#999999>
			<tr bgcolor="#FFFFFF">
				<td width="20%" align=right>IP��ַ:</td>
				<td colspan="3" align=left><input type="text" name="subnet" readonly
					value="<s:property value="subnet"/>"></td>
			</tr>
			<tr bgcolor="#FFFFFF">
				<td width="20%" align=right>��������:</td>
				<td width="30%" align=left><s:set var="oldcity" value="city_id" /><select
					name="city">
					<option value="-1">==��ѡ��==</option>
					<s:iterator value="firstCity" var="f">
						<option value="<s:property value="#f.city_id"/>"
							<s:property value="(#oldcity==#f.city_id)?'selected':''"/>><s:property
							value="city_name" /></option>
					</s:iterator>
				</select></td>
				<td width="20%" align=right>��������:</td>
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
				<td width="20%" align=right>����ʱ��:</td>
				<td colspan="3"align=left><input type="text" name="assigntime" readonly
					value="<s:property value="assigntime"/>" /></td>
			</tr>
			<tr bgcolor="#FFFFFF">
				<td width="20%" align=right>��;1:</td>
				<td colspan="3" align=left><select name="purpose1">
					<s:iterator value="purpose1">
						<option value="<s:property value="value"/>"
							<s:property value="purpose1Name==value?'selected':''"/>><s:property
							value="value" /></option>
					</s:iterator>
				</select></td>
			</tr>
			<tr bgcolor="#FFFFFF">
				<td width="20%" align=right>��;2:</td>
				<td colspan="3" align=left><select name="purpose2">
					<s:iterator value="purpose2">
						<option value="<s:property value="value"/>"
							<s:property value="purpose2Name==value?'selected':''"/>><s:property
							value="value" /></option>
					</s:iterator>
				</select></td>
			</tr>
			<tr bgcolor="#FFFFFF">
				<td width="20%" align=right>��;3:</td>
				<td colspan="3" align=left><select name="purpose3">
					<s:iterator value="purpose3">
						<option value="<s:property value="value"/>"
							<s:property value="purpose3Name==value?'selected':''"/>><s:property
							value="value" /></option>
					</s:iterator>
				</select></td>
			</tr>
			<tr bgcolor="#FFFFFF">
				<td width="20%" align=right>��ע:</td>
				<td colspan="3" align=left><textarea name="comment"><s:property
					value="comment" /></textarea></td>
			</tr>
			<tr bgcolor="#FFFFFF">
				<td colspan="4" align=left><input type="submit" name="�����޸�" value="�����޸�"
					onclick="savecity();" /></td>
			</tr>
		</table>
	</s:if>
	<s:else>
		<s:if test="assign==5">
			<div
				style="margin-left: 1%; width: 98%; color: red; margin-top: 10px; font-size: x-large;">
			<s:if test="approve==1">
			�����ϱ�����ûͨ���������IP��Դ
			</s:if> <s:else>
			�������ϱ�������
			</s:else></div>
		</s:if>
		<s:else>
			<table width="98%" height="30" border="0" align="center" cellpadding="0" cellspacing="0" class=green_gargtd>
			<tr>
				<td width="162" align="center" class="title_bigwhite">������Ϣ</td>
			</tr>
		   </table>
			<table width="98%" border=0 align="center" cellpadding="2"	cellspacing="1"  bgcolor=#999999>
				<tr bgcolor="#FFFFFF">
					<td width="30%" align=right>�û�����:</td>
					<td width="70%" align=left><input type="text" name="usernamezw"
						value='<s:property value="params.usernamezw"/>'><font
						color="#FF3300">**</font></td>
				</tr>
				<tr bgcolor="#FFFFFF">
					<td width="30%" align=right>�û�����(Ӣ��):</td>
					<td width="70%" align=left><input type="text" name="usernameyw"
						value='<s:property value="params.usernameyw"/>'><font
						color="FF3300">**</font></td>
				</tr>
				<tr bgcolor="#FFFFFF">
					<td width="30%" align=right>�û�ƴ�����:</td>
					<td width="70%" align=left><input type="text" name="usernamepyjc"
						value='<s:property value="params.usernamepyjc"/>'></td>
				</tr>
				<tr bgcolor="#FFFFFF">
					<td width="30%" align=right>�û�Ӣ�ļ��:</td>
					<td width="70%" align=left><input type="text" name="usernameywjc"
						value='<s:property value="params.usernameywjc"/>'></td>
				</tr>
				<tr bgcolor="#FFFFFF">
					<td width="30%" align=right>�û���ַ:</td>
					<td width="70%" align=left><input type="text" name="address"
						value='<s:property value="params.address"/>'></td>
				</tr>
				<tr bgcolor="#FFFFFF">
					<td width="30%" align=right>�û���������</td>
					<td width="70%" align=left><input type="text" name="netname"
						value='<s:property value="params.netname"/>'></td>
				</tr>
				<tr bgcolor="#FFFFFF">
					<td width="30%" align=right>�û�����Ӣ����:</td>
					<td width="70%" align=left><input name="netnamee" type="text"
						value='<s:property value="params.netnamee"/>' size="32"
						maxlength="32"> <font color="#FF3300">**</font> <br>
					<font color="#FF3300">������SUZHOU-HONGFA-ELECTRONIC-CORP���ֶ�֮����-����������ʹ�ÿո�</font>
					</td>
				</tr>
				<tr bgcolor="#FFFFFF">
					<td width="30%" align=right>�û���������ƣ�</td>
					<td width="70%" align=left><input type="text" name="netnamejc" maxlength="10"
						value='<s:property value="params.netnamejc"/>'></td>
				</tr>
				<tr bgcolor="#FFFFFF">
					<td width="30%" align=right>�û����뷽ʽ:</td>
					<td width="70%" align=left><select name="cntmode">
						<option value="-1">==��ѡ��==</option>
						<s:iterator value="cntmodeList">
							<option value="<s:property value="value"/>"
								<s:property value="params.cntmode==value?'selected':''"/>>==<s:property
								value="value" />==</option>
						</s:iterator>
					</select></td>
				</tr>
				<tr bgcolor="#FFFFFF">
					<td width="30%" align=right>�û���������:</td>
					<td width="70%" align=left><select name="cntspeed">
						<option value="-1">==��ѡ��==</option>
						<s:iterator value="cntspeedList">
							<option value="<s:property value="value"/>"
								<s:property value="params.cntspeed==value?'selected':''"/>>==<s:property
								value="value" />==</option>
						</s:iterator>
					</select></td>
				</tr>
				<tr bgcolor="#FFFFFF">
					<td width="30%" align=right>������ַ:</td>
					<td width="70%" align=left><input type="text" name="cntaddr"
						value="<s:property value="params.cntaddr"/>" /></td>
				</tr>
				<tr bgcolor="#FFFFFF">
					<td width="30%" align=right>�����û����:</td>
					<td width="70%" align=left><input type="text" name="localun"
						value="<s:property value="params.localun"/>" /></td>
				</tr>
				<tr bgcolor="#FFFFFF">
					<td width="30%" align=right>��������:</td>
					<td width="70%" align=left><input type="text" name="applydate" readonly="true"
						size="10" value='<s:property value="params.applydate"/>'></td>
				</tr>
				<tr bgcolor="#FFFFFF">
					<td width="30%" align=right>�����ص�:</td>
					<td width="70%" align=left><select name="rwaddr">
						<option value="-1">==��ѡ��==</option>
						<s:iterator value="firstCity">
							<option value="<s:property value="city_id"/>"
								<s:property value="rwaddr==city_id?'selected':''"/>>==<s:property
								value="city_name" />==</option>
						</s:iterator>
					</select></td>
				</tr>
				<tr bgcolor="#FFFFFF">
					<td width="30%" align=right>��:</td>
					<td width="70%" align=left><select name="countryName">
						<s:iterator value="countryCity">
							<option value="<s:property value="country_name"/>"
								<s:property value="countryName==country_name?'selected':''"/>>==<s:property
								value="country_name" />==</option>
						</s:iterator>
					</select></td>
				</tr>
				<tr bgcolor="#FFFFFF">
					<td width="30%" align=right>������;��</td>
					<td width="70%" align=left><input type="text" name="purpose"
						value='<s:property value="params.purpose"/>'></td>
				</tr>
				<tr bgcolor="#FFFFFF">
					<td width="30%" align=right>��ע:</td>
					<td width="70%" align=left><textarea name="memo"><s:property
						value="params.memo" /></textarea></td>
					<%--
			</tr>
				<td class="left_tab_back">�û������������:</td>
				<td><input type="radio" name="manainput" checked value="0"
					id="mana0" /><label for="mana0">Handle </label>
					<input type="radio"
					name="manainput" value="1" id="mana1" /><label for="mana1">����</label>
					 </td>
			</tr>
			--%>
				<tr bgcolor="#FFFFFF" name="mana0">
					<td width="30%" align=right>�û������������Handle:</td>
					<td width="70%" align=left><input type="text" name="managerhandle"
						value='<s:property value="params.managerhandle"/>'><font
						color="#FF3300">**</font></td>
				</tr>
				<%--
									<tr name="mana1" style="display: none;">
										<td>�û����������������:</td>
										<td><input type="text" name="managername"
											value='<s:property value="params.managername"/>'></td>
									</tr>
									<tr name="mana1" style="display: none;">
										<td class="left_tab_back">�û����������������ƴ��:</td>
										<td><input type="text" name="managernamep"
											value='<s:property value="params.managernamep"/>'><font
											color="#FF3300">**(���ٰ��������֣��м��ÿո����)</font></td>
									</tr>
									<tr name="mana1" style="display: none;">
										<td class="left_tab_back">�û������������ְ��:</td>
										<td><input type="text" name="managerduty"
											value='<s:property value="params.managerduty"/>'></td>
									</tr>
									<tr name="mana1" style="display: none;">
										<td class="left_tab_back">�û�����������˵绰:</td>
										<td><input type="text" name="managerphone"
											value='<s:property value="params.managerphone"/>'> <font
											color="#FF3300">**</font> <br>
										<font color="#FF3300">������25-6588783������ǰ�治��0����131011111111</font></td>
										</td>
									</tr>

			<tr name="mana1" style="display: none;">
				<td class="left_tab_back">�û������������E-mail:</td>
				<td><input type="text" name="manageremail"
					value='<s:property value="params.managerphone"/>'> <font
					color="#FF3300">**</font> <font color="#FF3300">(��д�û�����������˵��ʼ���ַ)</font></td>
			</tr>
			<tr name="mana1" style="display: none;">
				<td class="left_tab_back">�û�����������˴���:</td>
				<td><input type="text" name="managerfax"
					value='<s:property value="params.managerphone"/>'></td>
			</tr>
			<tr name="mana1" style="display: none;">
				<td class="left_tab_back">�û������������ͨ�ŵ�ַ:</td>
				<td><input type="text" name="manageraddress"
					value='<s:property value="params.manageraddress"/>'></td>
			</tr>
			<tr name="mana1" style="display: none;">
				<td class="left_tab_back">�û������������Ӣ��ͨ�ŵ�ַ:</td>
				<td><input type="text" name="manageraddrE"
					value='<s:property value="params.manageraddrE"/>'> <font
					color="#FF3300">**</font></td>
			</tr>
			<tr name="mana1" style="display: none;">
				<td class="left_tab_back">�û��������������������:</td>
				<td><input type="text" name="managerpc"
					value='<s:property value="params.managerpc"/>'></td>
			</tr>
			<tr>
				<td class="left_tab_back">�û����缼��������:</td>
				<td> <input type="radio" name="techinput" value="0" id="tech0" /><label
					for="tech0"> Handle</label>  <input type="radio"
					name="techinput" value="1" id="tech1" checked /><label for="tech1">
				����</label></td>
			</tr>
			<tr name="tech0" style="display: none;">
				<td class="left_tab_back">�û����缼��������Handle:</td>
				<td><input type="text" name="techhandle"
					value='<s:property value="params.techhandle"/>'><font
					color="#FF3300">**</font></td>
			</tr>
			--%>
				<tr bgcolor="#FFFFFF" name="tech1">
					<td width="30%" align=right>�û����缼������������:</td>
					<td width="70%" align=left><input type="text" name="techname"
						value='<s:property value="params.techname"/>'></td>
				</tr>
				<tr name="tech1" bgcolor="#FFFFFF">
					<td width="30%" align=right>�û����缼������������ƴ��:</td>
					<td width="70%" align=left><input type="text" name="technamep"
						value='<s:property value="params.technamep"/>'> <font
						color="#FF3300">**(���ٰ��������֣��м��ÿո����)</font></td>
				</tr>
				<tr name="tech1" bgcolor="#FFFFFF">
					<td width="30%" align=right>�û����缼��������ְ��:</td>
					<td width="70%" align=left><input type="text" name="techduty"
						value='<s:property value="params.techduty"/>'></td>
				</tr>
				<tr name="tech1" bgcolor="#FFFFFF">
					<td width="30%" align=right>�û����缼�������˵绰:</td>
					<td width="70%" align=left><input type="text" name="techphone"
						value='<s:property value="params.techphone"/>'><font
						color="#FF3300">**</font> <br>
					<font color="#FF3300">������25-6588783������ǰ�治��0����131011111111</font></td>
				</tr>
				<tr name="tech1" bgcolor="#FFFFFF">
					<td width="30%" align=right>�û����缼��������E-mail:</td>
					<td width="70%" align=left><input type="text" name="techemail"
						value='<s:property value="params.techemail"/>'> <font
						color="#FF3300">**</font> <font color="#FF3300">(��д�û����缼�������˵��ʼ���ַ)</font></td>
				</tr>
				<tr name="tech1" bgcolor="#FFFFFF">
					<td width="30%" align=right>�û����缼�������˴���:</td>
					<td width="70%" align=left><input type="text" name="techfax"
						value='<s:property value="params.techfax"/>'></td>
				</tr>
				<tr name="tech1" bgcolor="#FFFFFF">
					<td width="30%" align=right>�û����缼��������ͨ�ŵ�ַ:</td>
					<td width="70%" align=left><input type="text" name="techaddr"
						value='<s:property value="params.techaddr"/>'></td>
				</tr>
				<tr name="tech1" bgcolor="#FFFFFF">
					<td width="30%" align=right>�û����缼��������Ӣ��ͨ�ŵ�ַ:</td>
					<td width="70%" align=left><input type="text" name="techaddre"
						value='<s:property value="params.techaddre"/>'><font
						color="#FF3300">**</font></td>
				</tr>
				<tr name="tech1" bgcolor="#FFFFFF">
					<td width="30%" align=right>�û����缼����������������:</td>
					<td width="70%" align=left><input type="text" name="techpc"
						value="<s:property value="params.techpc"/>"></td>
				</tr>
				<tr bgcolor="#FFFFFF">
					<td colspan="2" align=right><input type="submit" name="�����޸�" value="�����޸�"
						onclick="saveuser();" /></td>
				</tr>
			</table>
		</s:else>
	</s:else>
</s:else>
</body>
</html>
<%@ include file="../foot.jsp"%>
