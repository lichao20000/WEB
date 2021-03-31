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
//��������
function gocut()
{
	var li = $("#link").attr("checked")==true?1:0;
	var sublen=$("#sublen").attr("value");
	if(sublen==-1)
	{
		alert("��ѡ������λ����������������");
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
						alert("���ֳɹ���");
						parent.left.reload("<s:property value="attr"/>");
					}
					else if(data=="no")
					{
						alert("�����粻�ܻ���������");
					}
					else if(data=="over")
					{
						alert("�����������ι��࣡");
					}
					else
					{
						alert("����ʧ�ܣ������»��ֻ����Ժ����ԣ�");
					}
				},
			erro:function(xmlR,msg,other){alert(msg);}});
}
//����ip������
function assignCity()
{
	if($("select[@name='city']").val()=="")
	{
		alert("��ѡ�����!");
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
						alert("����IP�ɹ���");
						parent.left.reload("<s:property value="attr"/>");
					}
					else
					{
						alert("����IPʧ�ܣ������·�������Ժ����ԣ�");
					}
				},
			erro:function(xmlR,msg,other){alert(msg);}});
}
//����ip��ר���û�
function assignUser()
{
	if($("input[@name='usernamezw']").val()=="")
	{
		alert("����д�û���!");
		$("input[@name='usernamezw']").focus();
		return;
	}
	if($("input[@name='usernameyw']").val()=="")
	{
		alert("����д�û����ƣ�Ӣ�ģ�!");
		$("input[@name='usernameyw']").focus();
		return;
	}
	if($("input[@name='netnamee']").val()=="")
	{
		alert("����д�û�����Ӣ����");
		$("input[@name='netnamee']").focus();
		return;
	}
	if($("select[@name='rwaddr']").val()==-1)
	{
		alert("����д�������ص�");
		$("select[@name='rwaddr']").focus();
		return;
	}
	if($("input[@name='managerhandle']").val()=="")
	{
		alert("����д�û������������Handle");
		$("input[@name='managerhandle']").focus();
		return;
	}
	if($("input[@name='technamep']").val()=="")
	{
		alert("����д�û����缼������������ƴ��");
		$("input[@name='technamep']").focus();
		return;
	}
	if($("input[@name='techphone']").val()=="")
	{
		alert("����д�û����缼�������˵绰");
		$("input[@name='techphone']").focus();
		return;
	}
	if($("input[@name='techemail']").val()=="")
	{
		alert("����д�û����缼��������E-mail");
		$("input[@name='techemail']").focus();
		return;
	}
	if($("input[@name='techaddre']").val()=="")
	{
		alert("����д�û����缼��������Ӣ��ͨ�ŵ�ַ");
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
						alert("����IP�ɹ���");
					}
					else
					{
						alert("����IPʧ�ܣ������·�������Ժ����ԣ�");
					}
				},
			erro:function(xmlR,msg,other){alert(msg);}});
}
$(function(){
	//�޸��������ȣ���̬�ı���������
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
	//��̬�ı������������޸ĳ���
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
	//��ȡ���ص������б�
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
	//��ȡר�ߵ��ص������б�
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
	��ʱע�͵�����Ϊ���ղ���Ҫ�����л�
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
	**/
});
function checkF()
{
	if($("input[@name='assignInfo']").val()==null||$("input[@name='assignInfo']").val()=="")
	{
		alert("����д�û����ƣ�");
		$("input[@name='assignInfo']").focus();
		return false;
	}
	if($("input[@name='appfile']").val()==null||$("input[@name='appfile']").val()=="")
	{
		alert("��ѡ���ϴ����ļ�·����");
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
			<td colspan="2"><label>����λ��:<select name="sublen"
				id="sublen">
				<option value="-1">==��ѡ��==</option>
				<s:property value="icnt" escapeHtml="false" />
			</select><label for="">��������:</label><select name="sbcnt">
				<option value="-1">==��ѡ��==</option>
				<s:property value="subcnt" escapeHtml="false" />
			</select><label>��������:</label><input type="text" id="subnetmask" readonly /></td>
		</tr>
		<tr class="green_foot">
			<td colspan="2"><input type="checkbox" name="link" value="1"
				id="link" /><label for="link">��������</label></td>
		</tr>
		<tr class="green_foot">
			<td colspan="2">
			<button onclick="gocut();">ȷ������</button>
			</td>
		</tr>
	</table>
</s:if>
<s:elseif test="act=='give'">
	<table width="98%" height="30" border="0" align="center" cellpadding="0" cellspacing="0" class=green_gargtd>
		<tr>
			<td width="162" align="center" class="title_bigwhite">������Ϣ</td>
		</tr>
	</table>
	<s:if test="userstat==0">
		<table width="98%"  border=0 align="center" cellpadding="2"	cellspacing="1"  bgcolor=#999999>

			<tr bgcolor="#FFFFFF">
				<td width="20%" align=right>IP��ַ:</td>
				<td colspan="3" align=left><input type="text" name="subnet" readonly
					value="<s:property value="subnet"/>"></td>
			</tr>
			<tr bgcolor="#FFFFFF">
				<td width="20%" align=right>��������:</td>
				<td width="30%" align=left><select name="city">
					<option value="">==��ѡ��==</option>
					<s:iterator value="firstCity">
						<option value="<s:property value="city_id"/>">==<s:property
							value="city_name" />==</option>
					</s:iterator>
				</select></td>
				<td width="20%" align=right>��������:</td>
				<td width="30%" align=left><select name="country">
				</select></td>
			</tr>
			<tr bgcolor="#FFFFFF">
				<td width="20%" align=right>��;1:</td>
				<td colspan="3" align=left><select name="purpose1">
					<s:iterator value="purpose1">
						<option value="<s:property value="value"/>"><s:property
							value="value" /></option>
					</s:iterator>
				</select></td>
			</tr>
			<tr bgcolor="#FFFFFF">
				<td width="20%" align=right>��;2:</td>
				<td colspan="3" align=left><select name="purpose2">
					<s:iterator value="purpose2">
						<option value="<s:property value="value"/>"><s:property
							value="value" /></option>
					</s:iterator>
				</select></td>
			</tr>
			<tr bgcolor="#FFFFFF">
				<td width="20%" align=right>��;3:</td>
				<td colspan="3" align=left><select name="purpose3">
					<s:iterator value="purpose3">
						<option value="<s:property value="value"/>"><s:property
							value="value" /></option>
					</s:iterator>
				</select></td>
			</tr>
			<tr bgcolor="#FFFFFF">
				<td width="20%" align=right>��ע:</td>
				<td colspan="3" align=left><textarea name="comment"></textarea></td>
			</tr>
			<tr bgcolor="#FFFFFF">
				<td colspan="4" align=right><input type="submit" name="�� ��" value="�� ��"
					onclick="assignCity();" /></td>
			</tr>
		</table>
	</s:if>
	<s:else>
		<s:if test="allow">
			<table width="98%" border=0 align="center" cellpadding="2"	cellspacing="1"  bgcolor=#999999>
				<tr bgcolor="#FFFFFF">
					<td width="30%" align=right>�û�����:</td>
					<td width="70%" align=left><input type="text" name="usernamezw" value=''><font
						color="#FF3300">**</font></td>
				</tr>
				<tr bgcolor="#FFFFFF">
					<td width="30%" align=right>�û�����(Ӣ��):</td>
					<td width="70%" align=left><input type="text" name="usernameyw" value=''><font
						color="#FF3300">**</font></td>
				</tr>
				<tr bgcolor="#FFFFFF">
					<td width="30%" align=right>�û�ƴ�����:</td>
					<td width="70%" align=left><input type="text" name="usernamepyjc" value=''></td>
				</tr>
				<tr bgcolor="#FFFFFF">
					<td width="30%" align=right>�û�Ӣ�ļ��:</td>
					<td width="70%" align=left><input type="text" name="usernameywjc" value=''></td>
				</tr>
				<tr bgcolor="#FFFFFF">
					<td width="30%" align=right>�û���ַ:</td>
					<td width="70%" align=left><input type="text" name="address" value=''></td>
				</tr>
				<tr bgcolor="#FFFFFF">
					<td width="30%" align=right>�û���������</td>
					<td width="70%" align=left><input type="text" name="netname" value=''></td>
				</tr>
				<tr bgcolor="#FFFFFF">
					<td width="30%" align=right>�û�����Ӣ����:</td>
					<td width="70%" align=left><input name="netnamee" type="text" value='' size="32"
						maxlength="32"> <font color="#FF3300">**</font> <br>
					<font color="#FF3300">������SUZHOU-HONGFA-ELECTRONIC-CORP���ֶ�֮����-����������ʹ�ÿո�</font>
					</td>
				</tr>
				<tr bgcolor="#FFFFFF">
					<td width="30%" align=right>�û���������ƣ�</td>
					<td width="70%" align=left><input type="text" name="netnamejc" maxlength="10"
						value=''></td>
				</tr>
				<tr bgcolor="#FFFFFF">
					<td width="30%" align=right>�û����뷽ʽ:</td>
					<td width="70%" align=left><select name="cntmode">
						<option>==��ѡ��==</option>
						<s:iterator value="cntmodeList">
							<option value="<s:property value="value"/>">==<s:property
								value="value" />==</option>
						</s:iterator>
					</select></td>
				</tr>
				<tr bgcolor="#FFFFFF">
					<td width="30%" align=right>�û���������:</td>
					<td width="70%" align=left><select name="cntspeed">
						<option>==��ѡ��==</option>
						<s:iterator value="cntspeedList">
							<option value="<s:property value="value"/>">==<s:property
								value="value" />==</option>
						</s:iterator>
					</select></td>
				</tr>
				<tr bgcolor="#FFFFFF">
					<td width="30%" align=right>������ַ:</td>
					<td width="70%" align=left><input type="text" name="cntaddr" value=""></td>
				</tr>
				<tr bgcolor="#FFFFFF">
					<td width="30%" align=right>�����û����:</td>
					<td width="70%" align=left><input type="text" name="localun" value=""></td>
				</tr>
				<tr bgcolor="#FFFFFF">
					<td width="30%" align=right>�����ص�:</td>
					<td width="70%" align=left><select name="rwaddr">
						<option value="-1">==��ѡ��==</option>
						<s:iterator value="firstCity">
							<option value="<s:property value="city_id"/>">==<s:property
								value="city_name" />==</option>
						</s:iterator>
					</select><font color="#FF3300">**</font></td>
				</tr>
				<tr bgcolor="#FFFFFF">
					<td width="30%" align=right>��:</td>
					<td width="70%" align=left><select name="usercountry"></select></td>
				</tr>
				<tr bgcolor="#FFFFFF">
					<td width="30%" align=right>������;:</td>
					<td width="70%" align=left><input type="text" name="purpose" value=''></td>
				</tr>
				<tr bgcolor="#FFFFFF">
					<td width="30%" align=right>��ע:</td>
					<td width="70%" align=left><textarea name="memo"></textarea></td>
				</tr>
				<%--
				<tr>
					<td width="30%" align=right>�û������������:</td>
					<td><input type="radio" name="manainput" checked value="0"
						id="mana0" /><label for="mana0">Handle</label>
						��ʱ�޸Ľ�����߲���Ҫ�л�������ſ����뽫�ϱߵ�js����Ҳ�ſ�
						 <input
						type="radio" name="manainput" value="1" id="mana1" /><label
						for="mana1">����</label>
						</td>
				</tr>
				--%>

				<tr name="mana0" bgcolor="#FFFFFF">
					<td width="30%" align=right>�û������������Handle:</td>
					<td width="70%" align=left><input type="text" name="managerhandle" value=''><font
						color="#FF3300">**</font></td>
				</tr>
				<%--
				<tr name="mana1" style="display: none;">
					<td width="30%" align=right>�û����������������:</td>
					<td><input type="text" name="managername" value=''></td>
				</tr>
				<tr name="mana1" style="display: none;">
					<td width="30%" align=right>�û����������������ƴ��:</td>
					<td><input type="text" name="managernamep" value=''><font
						color="#FF3300">**(���ٰ��������֣��м��ÿո����)</font></td>
				</tr>
				<tr name="mana1" style="display: none;">
					<td width="30%" align=right>�û������������ְ��:</td>
					<td><input type="text" name="managerduty" value=''></td>
				</tr>
				<tr name="mana1" style="display: none;">
					<td width="30%" align=right>�û�����������˵绰:</td>
					<td><input type="text" name="managerphone" value=''> <font
						color="#FF3300">**</font> <br>
					<font color="#FF3300">������25-6588783������ǰ�治��0����131011111111</font></td>
					</td>
				</tr>
				<tr name="mana1" style="display: none;">
					<td width="30%" align=right>�û������������E-mail:</td>
					<td><input type="text" name="manageremail" value=''> <font
						color="#FF3300">**</font> <font color="#FF3300">(��д�û�����������˵��ʼ���ַ)</font></td>
				</tr>
				<tr name="mana1" style="display: none;">
					<td width="30%" align=right>�û�����������˴���:</td>
					<td><input type="text" name="managerfax" value=''></td>
				</tr>
				<tr name="mana1" style="display: none;">
					<td width="30%" align=right>�û������������ͨ�ŵ�ַ:</td>
					<td><input type="text" name="manageraddress" value=''></td>
				</tr>
				<tr name="mana1" style="display: none;">
					<td width="30%" align=right>�û������������Ӣ��ͨ�ŵ�ַ:</td>
					<td><input type="text" name="manageraddrE" value=''> <font
						color="#FF3300">**</font></td>
				</tr>
				<tr name="mana1" style="display: none;">
					<td width="30%" align=right>�û��������������������:</td>
					<td><input type="text" name="managerpc" value=''></td>
				</tr>
				<tr>
					<td width="30%" align=right>�û����缼��������:</td>
					<td>
					<input type="radio" name="techinput" value="0" id="tech0" /><label
						for="tech0"> Handle</label>
						 <input type="radio" name="techinput" value="1" id="tech1"
						checked /><label for="tech1"> ����</label></td>
				</tr>
				<tr name="tech0" style="display: none;">
					<td width="30%" align=right>�û����缼��������Handle:</td>
					<td><input type="text" name="techhandle" value=''><font
						color="#FF3300">**</font></td>
				</tr>
				--%>
				<tr name="tech1" bgcolor="#FFFFFF">
					<td width="30%" align=right>�û����缼������������:</td>
					<td width="70%" align=left><input type="text" name="techname" value=''></td>
				</tr>
				<tr name="tech1" bgcolor="#FFFFFF">
					<td width="30%" align=right>�û����缼������������ƴ��:</td>
					<td width="70%" align=left><input type="text" name="technamep" value=''> <font
						color="#FF3300">**(���ٰ��������֣��м��ÿո����)</font></td>
				</tr>
				<tr name="tech1" bgcolor="#FFFFFF">
					<td width="30%" align=right>�û����缼��������ְ��:</td>
					<td width="70%" align=left><input type="text" name="techduty" value=''></td>
				</tr>
				<tr name="tech1" bgcolor="#FFFFFF">
					<td width="30%" align=right>�û����缼�������˵绰:</td>
					<td width="70%" align=left><input type="text" name="techphone" value=''><font
						color="#FF3300">**</font> <br>
					<font color="#FF3300">������25-6588783������ǰ�治��0����131011111111</font></td>
				</tr>
				<tr name="tech1" bgcolor="#FFFFFF">
					<td width="30%" align=right>�û����缼��������E-mail:</td>
					<td width="70%" align=left><input type="text" name="techemail" value=''> <font
						color="#FF3300">**</font> <font color="#FF3300">(��д�û����缼�������˵��ʼ���ַ)</font></td>
				</tr>
				<tr name="tech1" bgcolor="#FFFFFF">
					<td width="30%" align=right>�û����缼�������˴���:</td>
					<td width="70%" align=left><input type="text" name="techfax" value=''></td>
				</tr>
				<tr name="tech1" bgcolor="#FFFFFF">
					<td width="30%" align=right>�û����缼��������ͨ�ŵ�ַ:</td>
					<td width="70%" align=left><input type="text" name="techaddr" value=''></td>
				</tr>
				<tr name="tech1" bgcolor="#FFFFFF">
					<td width="30%" align=right>�û����缼��������Ӣ��ͨ�ŵ�ַ:</td>
					<td width="70%" align=left><input type="text" name="techaddre" value=''><font
						color="#FF3300">**</font></td>
				</tr>
				<tr name="tech1" bgcolor="#FFFFFF">
					<td width="30%" align=right>�û����缼����������������:</td>
					<td width="70%" align=left><input type="text" name="techpc" value=""></td>
				</tr>
				<tr bgcolor="#FFFFFF">
					<td colspan="2" align=right><input type="submit" name="�� ��" value="�� ��"
						onclick="assignUser();" /></td>
				</tr>
			</table>
		</s:if>
		<s:else>
			<!-- �������֣���Ҫ���� -->
			<form method="post" ENCTYPE="multipart/form-data"
				action="<s:url value="/hgwipMgSys/userappfp.action"/>"	onsubmit="return checkF();">
			<table width="98%" border=1 align="center" cellpadding="0"
				cellspacing="0" class="table">
				<input type="hidden" name="attr" value="<s:property value="attr"/>" />
				<tr>
					<td width="30%" align=right>�����:</td>
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
					<td width="30%" align=right>IP��ַ:</td>
					<td><input type="text" value="<s:property value="subnet"/>"
						name="subnet" readonly /></td>
				</tr>
				<tr>
					<td width="30%" align=right>��ַ����:</td>
					<td><input type="text" value="<s:property value="addrnum"/>"
						name="addrnum" readonly /></td>
				</tr>
				<tr>
					<td width="30%" align=right>�û�����:</td>
					<td><input type="text" value="" name="assignInfo" size=40 /><span
						style="color: red">*</span></td>
				</tr>
				<tr>
					<td width="30%" align=right>�������:</td>
					<td><input type="file" name="appfile" /><span
						style="color: red">*</span></td>
				</tr>
				<tr>
					<td width="30%" align=right>����˵��:</td>
					<td><textarea name="comment"></textarea></td>
				</tr>
				<tr>
					<td colspan="2" align= right><input type="submit" name="�ύ����" value="�ύ����" /></td>
				</tr>
			</table>
			</form>
			<script type="text/javascript">
			var stat ="<s:property value="stat"/>";
			if(stat=="ok")
			{
				alert("����ɹ�����ȴ�����");
				parent.left.reload("<s:property value="attr"/>");
			}
			else if(stat=="fail")
			{
				alert("����ʧ�ܣ����������룬�����Ժ�����!");
			}
			</script>
		</s:else>
	</s:else>
</s:elseif>
</body>
</html>
<%@ include file="../foot.jsp"%>
