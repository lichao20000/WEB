<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%
		request.setCharacterEncoding("GBK");
		String dev_ip = request.getParameter("dev_ip");
		String key = request.getParameter("key");

		Flux_Config flux_con = new Flux_Config();
		flux_con.setRequest(request);
		Cursor cursor = flux_con.getDevPortInfo();
		Map result = cursor.getNext();

		String ifindex = (String)result.get("ifindex");
		String ifdescr = (String)result.get("ifdescr");
		String ifname = (String)result.get("ifname");
		String ifnamedefined = (String)result.get("ifnamedefined");
		String iftype = (String)result.get("iftype");
		String ifspeed = (String)result.get("ifspeed");
		String ifmtu = (String)result.get("ifmtu");
		String ifhighspeed = (String)result.get("ifhighspeed");
		String ifinoctetsbps_max = (String)result.get("ifinoctetsbps_max");
		double num_ifinoctetsbps_max = Double.parseDouble(ifinoctetsbps_max);
		String ifoutoctetsbps_max = (String)result.get("ifoutoctetsbps_max");
		double num_ifoutoctetsbps_max = Double.parseDouble(ifoutoctetsbps_max);
		String ifindiscardspps_max = (String)result.get("ifindiscardspps_max");
		double num_ifindiscardspps_max = Double.parseDouble(ifindiscardspps_max);
		String ifoutdiscardspps_max = (String)result.get("ifoutdiscardspps_max");
		double num_ifoutdiscardspps_max = Double.parseDouble(ifoutdiscardspps_max);
		String ifinerrorspps_max = (String)result.get("ifinerrorspps_max");
		double num_ifinerrorspps_max =Double.parseDouble(ifinerrorspps_max);
		String ifouterrorspps_max = (String)result.get("ifouterrorspps_max");
		double num_ifouterrorspps_max = Double.parseDouble(ifouterrorspps_max);
		String warningnum = (String)result.get("warningnum");
		String warninglevel = (String)result.get("warninglevel");
		int num_warninglevel = Integer.parseInt(warninglevel);
		String reinstatelevel = (String)result.get("reinstatelevel");
		int num_reinstatelevel = Integer.parseInt(reinstatelevel);
		String overper = (String)result.get("overper");
		String overnum = (String)result.get("overnum");
		String com_day = (String)result.get("com_day");
		String overlevel = (String)result.get("overlevel");
		int num_overlevel = Integer.parseInt(overlevel);
		String reinoverlevel = (String)result.get("reinoverlevel");
		int num_reinoverlevel = Integer.parseInt(reinoverlevel);
		String intbflag = (String)result.get("intbflag");
		String ifinoctets = (String)result.get("ifinoctets");
		String inoperation = (String)result.get("inoperation");
		int num_inoperation = Integer.parseInt(inoperation);
		String inwarninglevel = (String)result.get("inwarninglevel");
		int num_inwarninglevel = Integer.parseInt(inwarninglevel);
		String inreinstatelevel = (String)result.get("inreinstatelevel");
		int num_inreinstatelevel = Integer.parseInt(inreinstatelevel);
		String outtbflag = (String)result.get("outtbflag");
		String ifoutoctets = (String)result.get("ifoutoctets");
		String outoperation = (String)result.get("outoperation");
		int num_outoperation = Integer.parseInt(outoperation);
		String outwarninglevel = (String)result.get("outwarninglevel");
		int num_outwarninglevel = Integer.parseInt(outwarninglevel);
		String outreinstatelevel = (String)result.get("outreinstatelevel");
		int num_outreinstatelevel = Integer.parseInt(outreinstatelevel);
		String gatherflag = (String)result.get("gatherflag");
		int num_gatherflag = Integer.parseInt(gatherflag);
		String intodb = (String)result.get("intodb");
		int num_intodb = Integer.parseInt(intodb);
%>
<%@ include file="../head.jsp"%>
<SCRIPT LANGUAGE="JavaScript">
<!--
function showpage(uniformid)
{
	switch (uniformid)
	{
		case 1:
		{
			document.all("td1").className="button_onblue";
			document.all("td2").className="button_outblue";
			document.all("td3").className="button_outblue";
			document.all("test1").style.display="";
			document.all("test2").style.display="none";
			document.all("test3").style.display="none";

			break;
		}
		case 2:
		{
			document.all("td1").className="button_outblue";
			document.all("td2").className="button_onblue";
			document.all("td3").className="button_outblue";
			document.all("test1").style.display="none";
			document.all("test2").style.display="";
			document.all("test3").style.display="none";
			break;
		}	
		case 3:
		{
			document.all("td1").className="button_outblue";
			document.all("td2").className="button_outblue";
			document.all("td3").className="button_onblue";
			document.all("test1").style.display="none";
			document.all("test2").style.display="none";
			document.all("test3").style.display="";

			break;
		}	
	}
}

function CheckChange(param)
{
	if (param == 1)
	{
		if (frm.checkbox_1.checked)
		{
			frm.text_1.disabled = false;
			frm.text_1.className = "";
			frm.text_1.value = frm.ifinoctetsbps_max.value;
			
		}
		else
		{
			frm.text_1.disabled = true;
			frm.text_1.className = "bk";
			frm.text_1.value = -1;
		}
	}

	if (param == 2)
	{
		if (frm.checkbox_2.checked)
		{
			frm.text_2.disabled = false;
			frm.text_2.className = "";
			frm.text_2.value = frm.ifoutoctetsbps_max.value;
		}
		else
		{
			frm.text_2.disabled = true;
			frm.text_2.className = "bk";
			frm.text_2.value = -1;
		}
	}

	if (param == 3)
	{
		if (frm.checkbox_3.checked)
		{
			frm.text_3.disabled = false;
			frm.text_3.className = "";
			frm.text_3.value = frm.ifindiscardspps_max.value;
		}
		else
		{
			frm.text_3.disabled = true;
			frm.text_3.className = "bk";
			frm.text_3.value = -1;
		}
	}

	if (param == 4)
	{
		if (frm.checkbox_4.checked)
		{
			frm.text_4.disabled = false;
			frm.text_4.className = "";
			frm.text_4.value = frm.ifoutdiscardspps_max.value;
		}
		else
		{
			frm.text_4.disabled = true;
			frm.text_4.className = "bk";
			frm.text_4.value = -1;
		}
	}

	if (param == 5)
	{
		if (frm.checkbox_5.checked)
		{
			frm.text_5.disabled = false;
			frm.text_5.className = "";
			frm.text_5.value = frm.ifinerrorspps_max.value;
		}
		else
		{
			frm.text_5.disabled = true;
			frm.text_5.className = "bk";
			frm.text_5.value = -1;
		}
	}

	if (param == 6)
	{
		if (frm.checkbox_6.checked)
		{
			frm.text_6.disabled = false;
			frm.text_6.className = "";
			frm.text_6.value = frm.ifouterrorspps_max.value;
		}
		else
		{
			frm.text_6.disabled = true;
			frm.text_6.className = "bk";
			frm.text_6.value = -1;
		}
	}

	if (param == 7)
	{
		if (frm.checkbox_7.checked)
		{
			frm.text_8.disabled = false;
			frm.text_9.disabled = false;
			frm.text_10.disabled = false;
			frm.text_8.className = "";
			frm.text_8.value = frm.overper.value;
			frm.text_9.className = "";
			frm.text_9.value = frm.overper.value;
			frm.text_10.className = "";
			frm.text_10.value = frm.com_day.value;
			frm.select_3.disabled = false;
			frm.select_4.disabled = false;
		}
		else
		{
			frm.text_8.disabled = true;
			frm.text_9.disabled = true;
			frm.text_10.disabled = true;
			frm.text_8.className = "bk";
			frm.text_8.value = "-1";
			frm.text_9.className = "bk";
			frm.text_9.value = "-1";
			frm.text_10.className = "bk";
			frm.text_10.value = "-1";
			frm.select_3.disabled = true;
			frm.select_4.disabled = true;
		}
	}

	if (param == 8)
	{
		if (frm.checkbox_8.checked)
		{
			frm.text_11.disabled = false;
			frm.text_11.className = "";
			frm.text_11.value = frm.ifinoctets.value;
			frm.select_5.disabled = false;
			frm.select_6.disabled = false;
			frm.select_7.disabled = false;
			frm.checkbox_8.value = 1;
		}
		else
		{
			frm.text_11.disabled = true;
			frm.text_11.className = "bk";
			frm.text_11.value = -1;
			frm.select_5.disabled = true;
			frm.select_6.disabled = true;
			frm.select_7.disabled = true;
		}
	}

	if (param == 9)
	{
		if (frm.checkbox_9.checked)
		{
			frm.text_12.disabled = false;
			frm.text_12.className = "";
			frm.text_12.value = frm.ifoutoctets.value;
			frm.select_8.disabled = false;
			frm.select_9.disabled = false;
			frm.select_10.disabled = false;
			frm.checkbox_9.value = 1;
		}
		else
		{
			frm.text_12.disabled = true;
			frm.text_12.className = "bk";
			frm.text_12.value = -1;
			frm.select_8.disabled = true;
			frm.select_9.disabled = true;
			frm.select_10.disabled = true;
		}
	}
}

var isCall=0;
var iTimerID;

function CallPro()
{	
	switch(parseInt(isCall,10))
	{
		case 1:
		{
			window.alert("修改端口配置成功！");
			window.clearInterval(iTimerID);	
			
			isCall = 0
			location.reload();
			break;
		}
		case -1:
		{
			window.alert("操作失败，请重新操作！");
			window.clearInterval(iTimerID);
			
			isCall=0;
			break;
		}
	}
}

function isFloatNumber(strValue){
	var bz = true;
	
	if (strValue.length == 0)
	{
		bz = false;
	}
	else 
	{
		var index = strValue.indexOf("-");
		
		if(index>=0)
			bz = false;
		else{
			index = strValue.indexOf(".");
			var index2 = strValue.lastIndexOf(".");
			
			if(index2>index)
				bz = false;
			else{

				for(var i=0;i<strValue.length;i++){
					var ch = strValue.charAt(i);
					if(ch!='.' && (ch<'0' || ch>'9')){
						bz = false;
						break;
					}
				}
			}
		}
	}
	return bz;
}

function IsNumber(strValue){
	var bz = true;
	if(strValue.length > 0){
		for(var i=0;i<strValue.length;i++){
			var ch=strValue.substring(i,i+1);
			if(ch<'0'||ch>'9'){
				bz = false;
				break;
			}
		}
	}
	else{
		bz = false;
	}
	return bz;
}

function CheckForm()
{
	if (frm.checkbox_1.checked)
	{
		if (!isFloatNumber(frm.text_1.value.trim()))
		{
			alert("输入的端口流入带宽利用率阀值格式不正确！");
			return false;
		}
	}

	if (frm.checkbox_2.checked)
	{
		if (!isFloatNumber(frm.text_2.value.trim()))
		{
			alert("输入的端口流出带宽利用率阀值格式不正确！");
			return false;
		}
	}

	if (frm.checkbox_3.checked)
	{
		if (!isFloatNumber(frm.text_3.value.trim()))
		{
			alert("输入的端口流入丢包率阀值格式不正确！");
			return false;
		}
	}

	if (frm.checkbox_4.checked)
	{
		if (!isFloatNumber(frm.text_4.value.trim()))
		{
			alert("输入的端口流出丢包率阀值格式不正确！");
			return false;
		}
	}
		
	if (frm.checkbox_5.checked)
	{
		if (!isFloatNumber(frm.text_5.value.trim()))
		{
			alert("输入的端口流入错包率阀值格式不正确！");
			return false;
		}
	}

	if (frm.checkbox_6.checked)
	{
		if (!isFloatNumber(frm.text_6.value.trim()))
		{
			alert("输入的端口流出错包率阀值格式不正确！");
			return false;
		}
	}

	if (!IsNumber(frm.text_7.value.trim()))
	{
		alert("输入的超出阀值的次数格式不正确！");
		return false;
	}

	if (frm.checkbox_7.checked)
	{
		if (!IsNumber(frm.text_8.value.trim()))
		{
			alert("输入的超出动态阀值的百分比格式不正确！");
			return false;
		}

		if (frm.text_8.value.trim() > 100)
		{
			alert("输入的超出动态阀值的百分比超出范围！");
			return false;
		}

		if (!IsNumber(frm.text_9.value.trim()))
		{
			alert("输入的超出百分比次数格式不正确");
			return false;
		}

		if (!IsNumber(frm.text_10.value.trim()))
		{
			alert("输入的生成动态阀值的天数格式不正确");
			return false;
		}

		if (frm.text_10.value.trim() >3)
		{
			alert("输入的生成动态阀值的天数格式不正确");
			return false;
		}
	}

	if (frm.checkbox_8.checked)
	{
		if (!isFloatNumber(frm.text_11.value.trim()))
		{
			alert("输入的流入速率变化率阀值格式不正确！");
			return false;
		}
	}

	if (frm.checkbox_9.checked)
	{
		if (!isFloatNumber(frm.text_12.value.trim()))
		{
			alert("输入的流出速率变化率阀值格式不正确！");
			return false;
		}
	}
	var key = frm.key.value;
	frm.action = "action_editPort.jsp?key=" + key;
	frm.submit();
	iTimerID = window.setInterval("CallPro()",1000);
}
</SCRIPT>


<title>编辑端口配置</title>
<form method="post" name="frm" target="childFrm">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td bgcolor="#000000">
			<table width="100%" border="0" cellspacing="1" cellpadding="2">
				<TH>编 辑 端 口 配 置</TH>
				<tr><td class="column"><%@ include file="flux_editPort1.jsp"%></td></tr>
				<tr><td class="column"><%@ include file="flux_editPort2.jsp"%></td></tr>
				<tr>
					<td class="blue_foot" align="right">
						<input type="button" value=" 保 存 " class="jianbian" onclick="javascript:CheckForm();">&nbsp;&nbsp;
						<input type="button" value=" 关 闭 " class="jianbian" onclick="javascript:window.close();">&nbsp;&nbsp;
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>
<input name="key" type="hidden" value="<%= key%>">
<IFRAME ID="childFrm" name="childFrm" STYLE="display:none"></IFRAME>
</form>
