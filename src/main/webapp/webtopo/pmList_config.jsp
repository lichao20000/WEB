<SCRIPT LANGUAGE="JavaScript">
<!--

var isCall=0;
var iTimerID;

function CallPro()
{
	switch (parseInt(isCall,10)) 
	{
		case 1:
		{
			window.alert("����豸�Ĳɼ���");
			window.clearInterval(iTimerID);	
			
			isCall=0;
			break;
		}

		case -1:
		{
			window.alert("δ�ܿ�ʼ�ɼ��豸��");
			window.cleraInterval(iTimerID);

			isCall = 0;
			break;
		}

		case 2:
		{
			window.alert("����豸��ֹͣ�ɼ���");
			window.clearInterval(iTimerID);

			isCall =0 ;
			break;
		}

		case -2: 
		{
			window.alert("δ��ֹͣ�ɼ��豸��");
			window.clearInterval(iTimerID);

			isCall =0 ;
			break;
		}

		case 3:
		{
			window.alert("ɾ���豸�ɹ������ú�̨�ɹ���");
			window.clearInterval(iTimerID);

			isCall =0 ;
			break;
		}

		case -3:
		{
			window.alert("ɾ���豸ʧ�ܣ�");
			window.clearInterval(iTimerID);

			isCall =0 ;
			break;
		}

		case -4:
		{
			window.alert("ɾ���豸�ɹ������ú�̨ʧ�ܣ�");
			window.clearInterval(iTimerID);

			isCall =0 ;
			break;
		}
	}
}

function showChild(parname)
{
	if (parname=="company")
	{
		var o = document.all("company");
		var id = o.options[o.selectedIndex].value;
		document.all("childFrm").src = "getPmExpressionModel.jsp?companyID="+ id;	
	}
	else if (parname=="expression")
	{
		var o = document.all("expression");
		var expID = o.options[o.selectedIndex].value;
		document.all("childFrm").src = "getPmDeviceModel.jsp?expressionid=" + expID;
	}
}

function selectAll(oStr){
	var len = document.frm.elements.length;
	var i;
	for (i=0;i<len;i++ )
	{
		if (document.frm.elements[i].name=='select_one') {
			document.frm.elements[i].checked = frm.select_all.checked;
		}
	}
}

function CheckForm(parameter)
{
	var len = document.frm.elements.length;
	var i;
	var flag=false;
	var j = 0;
	var str_checkboxValue = new String();
	var dev_expInfo = new Array();
	var device_id = new String();
	var expressionid = new String();
	var deviceName = new String();
	var deviceIP = new String();
	
	for(i=0; i<len; i++){
		if(document.frm.elements[i].name=='select_one')
		{
			if (document.frm.elements[i].checked)
			{
				flag=true;
				break;
			}
		}		
	}
	if(!flag)
	{
		window.alert("������ѡ��һ̨�豸��");
		return false;
	
	}
	
	if (parameter=="collect") {
		
		for (i=0;i<len;i++)
		{
			if (document.frm.elements[i].name=='select_one')
			{
				if (document.frm.elements[i].checked)
				{
					if (document.frm.elements[i].value.indexOf("lose")>=0)
					{
						alert("�޷���δ���ã��ɼ�ʧ�ܵ��豸���вɼ���");
						return false;
					}
				}
			}
		}
		frm.action="collectStart.jsp";
		frm.submit();
		iTimerID = window.setInterval("CallPro()",1000);
	}

	if (parameter=="stop")
	{
		for (i=0;i<len;i++)
		{
			if (document.frm.elements[i].name=='select_one')
			{
				if (document.frm.elements[i].checked)
				{
					if (document.frm.elements[i].value.indexOf("lose")>=0)
					{
						alert("�޷���δ���ã��ɼ�ʧ�ܵ��豸����ֹͣ��");
						return false;
					}
				}
			}
		}
		frm.action="collectStop.jsp";
		frm.submit();
		iTimerID = window.setInterval("CallPro()",1000);
	}
	
	if (parameter=="editdev")
	{	
		

		for (i=0;i<len;i++)
		{
			if (document.frm.elements[i].name=='select_one')
			{
				if (document.frm.elements[i].checked)
				{
					j++;
					if (j>1)
					{
						alert("ֻ�ܶ�һ̨�豸�༭��");
						j = 0;
						return false;
					}
					str_checkboxValue = document.frm.elements[i].value;
					dev_expInfo = str_checkboxValue.split(",");
					device_id = dev_expInfo[0];
					expressionid = dev_expInfo[1];
				}
			}
		}
		j = 0;
		var page = "pm_Config.jsp?device_id=" + device_id + "&expressionid=" + expressionid;
		var otherpra = "channelmode=0,directories=0,location=0,menubar=0,resizable=no,scrollbars=no,status=0,toolbar=0,height=500,width=700,top=200,left=365";
		window.open(page,"��������",otherpra);
	}

	if (parameter=="editexample")
	{
		var num = 0;
		for (i=0;i<len;i++)
		{
			if (document.frm.elements[i].name=='select_one')
			{
				if (document.frm.elements[i].checked)
				{
					j++;
					
					if (j>1)
					{
						alert("ֻ�ܶ�һ̨�豸�༭��");
						j = 0;
						return false;
					}
					num = i;
					str_checkboxValue = document.frm.elements[i].value;
					dev_expInfo = str_checkboxValue.split(",");
					device_id = dev_expInfo[0];
					expressionid = dev_expInfo[1];
					deviceIP = dev_expInfo[2];
					deviceName = dev_expInfo[3];
				}
			}
		}

		if (document.frm.elements[num].value.indexOf("lose")>=0)
		{
			alert("�豸δ���óɹ�����˲��ܱ༭ʵ��������");
			return false;
		}
		j = 0;
		var page = "pm_indexManager.jsp?device_id=" + device_id + "&expressionid=" + expressionid + "&deviceIP=" + deviceIP + "&deviceName=" + deviceName;
		var otherpra = "channelmode=0,directories=0,location=0,menubar=0,resizable=no,scrollbars=no,status=0,toolbar=0,height=400,width=450,top=200,left=365";
		window.open(page,"ʵ������",otherpra);
	}

	
	if (parameter=="del")
	{
		frm.action = "pm_delSelectDev.jsp";
		frm.submit();
		iTimerID = window.setInterval("CallPro()",1000);
	}
}

//-->
</SCRIPT>
<html>
<head>
<title>���ܹ���</title>
</head>
<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ include file="../head.jsp"%>
<%@ include file="../toolbar.jsp"%>
<%@ page import= "com.linkage.litms.webtopo.common.*" %>
<body>
<form name="frm" action="" method="post" target="childFrm">
<table width="95%" border="0" align="center" cellpadding="0" cellspacing="0" align="center">
	<tr><td height="20">&nbsp;</td></tr>
	<tr>
		<td bgcolor=#000000>
			<table width="100%"  border="0" cellspacing="1" cellpadding="2">
				<tr>
					<th colspan="2">���ܹ���</th>
				</tr>
				<tr>
					<td width="20%" align="center" class=column1>��ѡ���豸����</td>
					<td width="80%" align="left" class=column2>
					<select name="company" onchange="showChild('company')">
					<option value="0" checked>==��ѡ��==</option>
	<%
		Cursor cursor = null;
		Pm_DevList pmDevList = new Pm_DevList(request);
		cursor = pmDevList.getCompanyID();
		Map map_companyID = cursor.getNext();
		String companyID = new String();
		
		while(map_companyID != null) {
			companyID = companyID + (String)map_companyID.get("company") + ",";
			map_companyID = cursor.getNext();
		}
		cursor.Reset();
		
		companyID = companyID.substring(0,companyID.length()-1);
		cursor = pmDevList.getCompanyName(companyID);
		Map map_companyName = cursor.getNext();
		String vendor_id = null;
		String vendor_name = null;

		while(map_companyName != null) {
			vendor_id = (String)map_companyName.get("vendor_id");
			vendor_name = (String)map_companyName.get("vendor_name");
			out.println("<option value=" + vendor_id + ">" + vendor_name + "</option>");
			map_companyName = cursor.getNext();
		}
		cursor.Reset();
	%>  
					</select>
					</td>
				</tr>
				<tr>	
					<td class=column1 align="center">��ѡ�����ܱ��ʽ</td>
					<td class=column2 align="left">
					<span id=expressionList></span>
					</td>
				</tr>
				<tr>
					<td colspan="2" class=column1 align="left">��������豸</td>
				</tr>
				<tr>
					<td colspan="2" class=column1>
					<span id=pmDevInfo></span>
					</td>
				</tr>
				<tr>
					<td colspan="2"  class=foot align="center"><input name="" type="button" value="  �� �� " class="jianbian" onclick="CheckForm('collect');">&nbsp;&nbsp;&nbsp;<input name="" type="button" value=" ͣ ֹ " class="jianbian" onclick="CheckForm('stop');">&nbsp;&nbsp;&nbsp;<input name="" type="button" value=" �༭�豸 " class="jianbian" onclick="CheckForm('editdev');">&nbsp;&nbsp;&nbsp;<input name="" type="button" value=" �༭ʵ�� " class="jianbian" onclick="CheckForm('editexample');">&nbsp;&nbsp;&nbsp;<input name="" type="button" value=" ɾ  �� " class="jianbian" onclick="CheckForm('del');"></td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
  		<td><IFRAME ID=childFrm name="childFrm" SRC="" STYLE="display:none"></IFRAME></td>
	</tr>
</table>
</form>
<%@ include file="../foot.jsp"%>
</body>
</html>
