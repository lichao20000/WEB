<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>�û���Ϣ����</title>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript" SRC="/Js/CheckFormForm.js"></SCRIPT>
<script type="text/javascript"
	src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<script type="text/javascript">

$(function() {
	gwShare_change("city", "-1");
});
function gwShare_change(type, selectvalue) {
	switch (type) {
	case "city":
		var url = "<s:url value='/gtms/stb/resource/userMessage!getCityNextChild.action'/>";
		$.post(url, {}, function(ajax) {
			gwShare_parseMessage(ajax, $("select[@name='gwShare_cityId']"),
					selectvalue);
			$("select[@name='cityId']").html(
					"<option value='-1'>==����ѡ������==</option>");
		});
		break;
	case "cityid":
		var url = "<s:url value='/gtms/stb/resource/userMessage!getCityNext.action'/>";
		var cityId = $("select[@name='gwShare_cityId']").val();
		if ("-1" == cityId) {
			$("select[@name='gwShare_cityId']").html(
					"<option value='-1'>==����ѡ������==</option>");
			break;
		}
		$.post(url, {
			citynext : cityId
		}, function(ajax) {
			gwShare_parseMessage(ajax, $("select[@name='citynext']"),
					selectvalue);
			$("select[@name='cityId']").html(
					"<option value='-1'>==����ѡ������==</option>");
		});
		break;
	default:
		alert("δ֪��ѯѡ�");
		break;
	}
}
function gwShare_parseMessage(ajax, field, selectvalue) {
	var flag = true;
	if ("" == ajax) {
		return;
	}
	var lineData = ajax.split("#");
	if (!typeof (lineData) || !typeof (lineData.length)) {
		return false;
	}
	field.html("");
	option = "<option value='-1' selected>==��ѡ��==</option>";
	field.append(option);
	for ( var i = 0; i < lineData.length; i++) {
		var oneElement = lineData[i].split("$");
		var xValue = oneElement[0];
		var xText = oneElement[1];
		if (selectvalue == xValue) {
			flag = false;
			//����ÿ��value��text��ǵ�ֵ����һ��option����
			option = "<option value='"+xValue+"' selected>==" + xText
					+ "==</option>";
		} else {
			//����ÿ��value��text��ǵ�ֵ����һ��option����
			option = "<option value='"+xValue+"'>==" + xText
					+ "==</option>";
		}
		try {
			field.append(option);
		} catch (e) {
			alert("�豸�ͺż���ʧ�ܣ�");
		}
	}
	if (flag) {
		field.attr("value", "-1");
	}
}
function add()
{
		var gwShare_cityId=$.trim($("select[@name='gwShare_cityId']").val());
		var citynext=$.trim($("select[@name='citynext']").val());
		
		if(gwShare_cityId == "-1"){
		    alert("��ѡ������");
		    return false;
		}
		var platformType=$.trim($("select[@name='platformType']").val());
		if(""==platformType||"-1"==platformType)
		{
		alert("��ѡ��ҵ��ƽ̨!");
		return false;
		}
		var userGroupID=$.trim($("select[@name='userGroupID']").val());
		if(""==userGroupID||"-1"==userGroupID)
		{
		alert("��ѡ���û���!");
		return false;
		}
		var stbpwd=$.trim($("input[@name='stbpwd']").val());
		/* if(""==stbpwd)
		{
		alert("�������벻��Ϊ��!");
		return false;
		} */
		var stbuser=$.trim($("input[@name='stbuser']").val());
		/* if(""==stbuser)
		{
		alert("�����˺Ų���Ϊ��!");
		return false;
		} */
		var stbuptyle=$.trim($("select[@name='stbuptyle']").val());
		if(""==stbuptyle||"-1"==stbuptyle)
		{
		alert("��ѡ�����з�ʽ!");
		return false;
		}
		var stbaccessStyle=$.trim($("select[@name='stbaccessStyle']").val());
		if(""==stbaccessStyle||"-1"==stbaccessStyle)
		{
		alert("��ѡ����뷽ʽ!");
		return false;
		}
		var iptvBindPhone=$.trim($("input[@name='iptvBindPhone']").val());
		var dealDate=$.trim($("input[@name='endtime']").val());
		var MAC=$.trim($("input[@name='MAC']").val());
		var servaccount=$.trim($("input[@name='servaccount']").val());
		if(""==servaccount)
		{
		alert("����дҵ���˺�!");
		return false;
		}
		var servpwd=$.trim($("input[@name='servpwd']").val());
		if(""==servpwd)
		{
		alert("����дҵ������!");
		return false;
		}
		var url = "<s:url value='/gtms/stb/resource/userMessage!add.action'/>";
		$.post(url, {
			iptvBindPhone : iptvBindPhone,
			endtime : dealDate,
			citynext : citynext,
			gwShare_cityId:gwShare_cityId,
			platformType:platformType,
			userGroupID:userGroupID,
			stbuser:stbuser,
			stbpwd:stbpwd,
			MAC:MAC,
			stbaccessStyle:stbaccessStyle,
			servaccount:servaccount,
			servpwd:servpwd,
			stbuptyle:stbuptyle
		}, function(ajax) {
			if (ajax == "�ɹ�") {
				alert("��ӳɹ�!");
			} else if(ajax == "MacΪ�ջ�У��ʧ��")
				{
				alert("mac��ַֻ����A-F��ĸ���������!");
				}else {
					alert(ajax);
				}
			});
}

</script>
</head>

<body>
	<form name="frm" id="frm" method="post" action="" target="dataForm">

		<table>
			<tr>
				<td HEIGHT=20>&nbsp;</td>
			</tr>
			<tr>
				<td>
					<table class="querytable">

						<TR>
							<th colspan="4">����û���Ϣ</th>
						</tr>
						<!-- <TR bgcolor="#FFFFFF">
							<TD class=column align="right">��Ȩ�˺�</TD>
							<TD colspan=3><INPUT TYPE="text" NAME="authUser"
								maxlength=30 class=bk size=20>&nbsp;<font
								color="#FF0000">*</font></TD>
						</TR>
						<TR bgcolor="#FFFFFF">
							<TD class=column align="right">��Ȩ����</TD>
							<TD colspan=3><INPUT TYPE="text" NAME="authPwd" maxlength=30
								class=bk size=20>&nbsp;<font color="#FF0000">*</font></TD>
						</TR> -->
						<TR bgcolor="#FFFFFF">
							<TD class=column align="right">����ʱ��</TD>
							<TD width="35%"><input type="text" name="endtime" readonly
								value="<s:property value='endtime'/>"> <img
								name="shortDateimg"
								onClick="WdatePicker({el:document.frm.endtime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
								src="../../images/dateButton.png" width="15" height="12"
								border="0" alt="ѡ��" /></TD>
						</TR>
						<TR bgcolor="#FFFFFF">
							<TD align="right" class=column width="15%">ƽ̨���ͣ�</TD>
							<TD width="35%" nowrap><s:select list="platformTypeList" name="platformType"
									headerKey="-1" headerValue="��ѡ��ƽ̨����" listKey="platform_id"
									listValue="platform_name" cssClass="bk"></s:select>&nbsp; <font
								color="red"> *</font></TD>
						</TR>
						<TR bgcolor="#FFFFFF">
							<TD align="right" class=column width="15%">�û����飺</TD>
							<TD width="35%" nowrap><s:select list="userGroupIDList" name="userGroupID"
									headerKey="-1" headerValue="��ѡ���û�����" listKey="group_id"
									listValue="group_name" cssClass="bk"></s:select>&nbsp; <font
								color="red"> *</font></TD>
						</TR>
						<TR bgcolor="#FFFFFF">
							<TD class=column align="right">�ֻ�����</TD>
							<TD colspan=3><INPUT TYPE="text" NAME="iptvBindPhone"
								maxlength=30 class=bk size=20></TD>
						</TR>
						<TR bgcolor="#FFFFFF">
							<TD class=column width="15%" align='right'>����</TD>
							<TD align="left" width="35%"><select name="gwShare_cityId"
								class="bk" onchange="gwShare_change('cityid','-1')">
									<option value="-1">==��ѡ��==</option>
							</select> &nbsp; <font
								color="red"> *</font></TD>
						</TR>
						<TR bgcolor="#FFFFFF">
							<TD class=column width="15%" align='right'>�¼�����</TD>
							<TD align="left" width="35%"><select name="citynext"
								class="bk">
									<option value="-1">����ѡ������</option>
							</select></TD>
						</TR>
						<TR bgcolor="#FFFFFF">
							<TD class=column align="right">ҵ���˺�</TD>
							<TD colspan=3><INPUT TYPE="text" NAME="servaccount"
								maxlength=30 class=bk size=20>&nbsp;<font
								color="#FF0000">*</font></TD>
						</TR>
						<TR bgcolor="#FFFFFF">
							<TD class=column align="right">ҵ������</TD>
							<TD colspan=3><INPUT TYPE="text" NAME="servpwd" maxlength=30
								class=bk size=20>&nbsp;<font color="#FF0000">*</font></TD>
						</TR>
						<TR bgcolor="#FFFFFF">
							<TD class=column align="right">MAC��ַ</TD>
							<TD colspan=3><INPUT TYPE="text" NAME="MAC" maxlength=30
								class=bk size=20>&nbsp;&nbsp;&nbsp;&nbsp;<font color="#FF0000">MAC��ַֻ����A-F��ĸ���������</font></TD>
						</TR>
						<TR bgcolor="#FFFFFF">
							<TD class=column align="right">���뷽ʽ</TD>
							<TD colspan=3><select name="stbaccessStyle" class="bk">
									<option value="-1">==��ѡ��==</option>
									<option value="1"  selected = "selected">DHCP</option>
									<option value="2">Static</option>
									<option value="3">PPPoE</option>
									<option value="4">IPoE</option>
							</select>&nbsp;<font color="#FF0000">*</font></TD>
						</TR>
						<TR bgcolor="#FFFFFF">
							<TD class=column align="right">�����˺�</TD>
							<TD colspan=3><INPUT TYPE="text" NAME="stbuser"
								maxlength=30 class=bk size=20></TD>
						</TR>
						<TR bgcolor="#FFFFFF">
							<TD class=column align="right">��������</TD>
							<TD colspan=3><INPUT TYPE="text" NAME="stbpwd" maxlength=30
								class=bk size=20></TD>
						</TR>
						<TR bgcolor="#FFFFFF">
							<TD class=column align="right">���з�ʽ</TD>
							<TD colspan=3><select name="stbuptyle" class="bk">
									<option value="-1">==��ѡ��==</option>
									<option value="FTTH" selected = "selected">FTTH</option>
									<option value="FTTB">FTTB</option>
									<option value="LAN">LAN</option>
									<option value="HGW">HGW</option>
							</select>&nbsp;<font color="#FF0000">*</font></TD>
						</TR>
						<TR>
						<td colspan="4" align="right" class="foot" width="100%">
									<button onclick="add()">&nbsp;�� ��&nbsp;</button>
									<button onclick="javascript:window.close();">&nbsp;�� ��&nbsp;</button>
							</td>
						</TR>
					</table>
				</td>
			</tr>
			<tr>
				<td height="25" id="resultStr"></td>
			</tr>
		</table>
		<br>
	</form>
</body>
</html>
