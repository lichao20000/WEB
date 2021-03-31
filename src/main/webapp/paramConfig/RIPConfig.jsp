<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=gbk"%>
<%@ include file="../head.jsp"%>
<%@ include file="../toolbar.jsp"%>
<%
request.setCharacterEncoding("GBK");
%>
<SCRIPT LANGUAGE="JavaScript">
var did;
var configWay;

function CheckForm(){
 	var oselect = document.all("device_id");
 	if(oselect == null){
		alert("请选择设备！");
		return false;
	 }
	var num = 0;
	if(typeof(oselect.length)=="undefined"){
		if(oselect.checked){
			did = oselect.value;
			num = 1;
		}
 	}else{
		for(var i=0;i<oselect.length;i++){
			if(oselect[i].checked){
			did = oselect[i].value;
			  num++;
			}
		}

 	}
 	if(num ==0){
		alert("请选择设备！");
		return false;
	}
	
	
	var obj=document.all("configway");	
	for(var i=0;i<obj.length;i++)
	{
		if(obj[i].checked)
		{
			configWay=obj[i].value;
			break;	
		}
	}
	if(configWay=="1")
	{
		alert("设备不支持TR09,请换种采集方式！");	
		return false;			
	}		
	return true;
}

function  getPortInfo()
{
	if(CheckForm())
	{
	   showMsgDlg();
	   var page = "RIPConfig_submit.jsp?device_id="+did+"&configWay="+configWay;
	   document.all("childFrm").src = page;
	}
}

function showMsgDlg(){
	w = document.body.clientWidth;
	h = document.body.clientHeight;

	l = (w-250)/2;
	t = (h-60)/2;
	PendingMessage.style.left = l;
	PendingMessage.style.top  = t;
	PendingMessage.style.display="";
}

function closeMsgDlg(){
	PendingMessage.style.display="none";
}
</SCRIPT>
<jsp:include page="/share/selectDeviceJs.jsp">
	<jsp:param name="selectType" value="radio"/>
	<jsp:param name="jsFunctionName" value=""/>
</jsp:include>
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD HEIGHT=20>
			&nbsp;
		</TD>
	</TR>
	<TR>
		<TD>
			<FORM NAME="frm" METHOD="post">
				<table width="98%" border="0" align="center" cellpadding="0" cellspacing="0" class="text">
					<tr>
						<td>
						<table width="100%" height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
							<tr>
								<td width="162" align="center" class="title_bigwhite" nowrap>
									参数实例管理
								</td>
								<td nowrap>
									<img src="../images/attention_2.gif" width="15" height="12">
									配置终端的时间服务器。
								</td>
							</tr>
						</table>
						</td>
					</tr>
					<TR>
						<TH colspan="4" align="center">
							设备查询
						</TH>
					</TR>
					<TR bgcolor="#FFFFFF" >
						<td colspan="4">
							<div id="selectDevice"><span>加载中....</span></div>
						</td>
					</TR>
					<TR>
						<TH colspan="4" align="center">
							RIP 配置
						</TH>
					</TR>
					<tr>
						<td>
							<TABLE width="100%" border=0 cellspacing=0 cellpadding=0 align="center">
								<TR>
									<TD bgcolor=#999999>
										<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
											<TR bgcolor="#FFFFFF" >
											    <TD align="right" width="15%">
													配置方式
												</TD>
												<TD colspan=3 width="85%">
													<input type="radio" name="configway"  class=btn  value="1">TR069&nbsp;
													<input type="radio" name="configway" checked class=btn  value="2">SNMP
												</TD>
											</TR>											
											<TR bgcolor="#FFFFFF">
												<TD colspan="4" align="right" class="green_foot">													
													<INPUT TYPE="button" onclick="getPortInfo()" value=" 获 取 " class=btn >
												</TD>
											</TR>					
										</TABLE>
									</TD>
								</TR>
								<TR>
									<TD HEIGHT=20>&nbsp;</TD>	
								</TR>
								<TR>
									<TD><div id="data"></div></TD>
								</TR>
							</TABLE>
						</td>
					</tr>
				</table>
			</FORM>
		</TD>
	</TR>
	<TR>
		<TD HEIGHT=20>
			&nbsp;
			<IFRAME ID=childFrm name="childFrm" SRC="" STYLE="display:none;width:500;height:500"></IFRAME>			
		</TD>
	</TR>
</TABLE>
<div id="PendingMessage"
	style="position:absolute;z-index:3;top:0px;left:0px;width:250;height:60;border-width:1;border-style:ridge;background-color:#eeeeee;padding-top:10px;display:none">
<center>
<table border="0">
	<tr>
		<td valign="middle"><img src="../images/cursor_hourglas.gif"
			border="0" WIDTH="30" HEIGHT="30"></td>
		<td>&nbsp;&nbsp;</td>
		<td valign="middle"><span id=txtLoading
			style="font-size:14px;font-family: 宋体">载入数据，请稍候······</span></td>
	</tr>
</table>
</center>
</div>
<%@ include file="../foot.jsp"%>
<SCRIPT LANGUAGE="JavaScript">
<!--
//给String类增加trim方法，过滤空格
// Trim() , Ltrim() , RTrim()

String.prototype.Trim = function() 
{ 
return this.replace(/(^\s*)|(\s*$)/g, ""); 
} 
String.prototype.LTrim = function() 
{ 
return this.replace(/(^\s*)/g, ""); 
} 
String.prototype.RTrim = function() 
{ 
return this.replace(/(\s*$)/g, ""); 
} 


//-->
</SCRIPT>