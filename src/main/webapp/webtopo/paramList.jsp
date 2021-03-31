<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ include file="../head.jsp"%>
<%
String device_id = request.getParameter("device_id");
%>
<SCRIPT LANGUAGE="JavaScript" SRC="../Js/prototype.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript">
var device_id ="<%=device_id%>";
//刷新时间 单位分钟
var refreshTime =10;
//刷新定时器
var timeId;

window.onload = function()
{
 timeId = setInterval(getData,refreshTime*60*1000);
}

//获取展示的数据
function getData()
{
 //发起查询DLS状态的请求
 //doDLSRequestAct();
 doPPOEAndIPAct();
 
}

function doWLanAct()
{
  //alert("doWLanAct");
  var url ="paramList_data.jsp";
  var Container="WLAN";
  var paras = "type=4";
  paras +="&device_id="+device_id;
  paras += "&tt=" + new Date().getTime();
  
  CreateAct(url,paras,Container);
}

function doDLSRequestAct()
{ 
  //alert("doDLSRequestAct");
  var url ="paramList_data.jsp";
  var Container="DLS";
  var paras = "type=1";
  paras +="&device_id="+device_id;
  paras += "&tt=" + new Date().getTime();
  
  CreateAct(url,paras,Container);
}

function doPPOEAndIPAct()
{
  //alert("doPPOEAndIPAct");
  var url ="paramList_data.jsp";
  var paras = "type=2";
  paras +="&device_id="+device_id;
  paras += "&tt=" + new Date().getTime();
  var Container="PPOEAndIP";
  CreateAct(url,paras,Container);
}

function doLanAct()
{
  //alert("doLanAct");
  var url ="paramList_data.jsp";
  var paras = "type=3";
  paras +="&device_id="+device_id;
  paras += "&tt=" + new Date().getTime();
  var Container="LAN";
  CreateAct(url,paras,Container);
}



//根据URL和参数发起AJAX请求
function CreateAct(url,pars,Container){
     //alert("wp");
	//pars = encodeURI(encodeURI(pars));
	var myAjax
		= new Ajax.Request(
							url,
							{
								//encoding:"GBK",
								method:"post",
								parameters:pars,
								onFailure:showError,
								onSuccess:function(req){
									showData(Container,req);
								},
								onLoading:function(req){
									showLoading(Container,req);
								},
								onException:function(req){
									doException(Container,req);
								}
							 }
						  );
}


/*
//注册回调函数
Ajax.Responders.register({
	onCreate:function(){
		refreshBottun();
	},
	onComplete:function(){
		if(Ajax.activeRequestCount == 0){
			closeMsgDlg();
		}
	}
});
*/
//AJAX回调函数
//根据容器名称及每个容器的标识位class
//将结果内容插入容器
function showData(Container,req){    
	if(Container=="DLS"){	
		$("idLayerView").innerHTML = req.responseText;		
		$("DLS").innerHTML = $("DLS_").innerHTML;			
	}else if(Container=="PPOEAndIP"){
		$("idLayerView1").innerHTML = req.responseText;
		$("PPOE").innerHTML = $("PPPOE_").innerHTML;
		$("IP").innerHTML = $("IP_").innerHTML;
		$("ATM").innerHTML = $("ATM_").innerHTML;	
		//发起Lan数据的查询请求
        doLanAct();	
	}
	else if(Container=="LAN")
	{
	   $("idLayerView2").innerHTML = req.responseText;
	   $("LAN").innerHTML = $("LAN_").innerHTML;
	   //发起Wlan数据的查询请求
       doWLanAct();
	}
	else if(Container=="WLAN")
	{
	   $("idLayerView3").innerHTML = req.responseText;
	   $("WLAN").innerHTML = $("WLAN_").innerHTML;
	   //发起DLS请求
	   doDLSRequestAct();
	}
}

//暂不添加取得数据时的过虑效果
function showLoading(Container,req){
	//$("sp_mrtg" + index).innerHTML = "生成图形...<a href=\"javascript://\" onclick='refreshMrtg()'>刷新</a>";
	//$("sp_mrtg" + index).innerHTML = "<img src=../images/loading.gif>";
}

//调试信息
function showError(req){
	//调试模式
	if(__debug)
		$("debug").innerHTML = req.responseText;
}

//异常捕捉
function doException(Container,req){

}
</SCRIPT>
<TABLE width="90%" border=0 cellspacing=0 cellpadding=0 align="center">
	<TR>
		<TD>
		<TABLE border=0 cellspacing=1 cellpadding=1 width="100%">			
			<TR>
				<TH class=yellow_title>PPPOE状态和数据</TH>
			</TR>
			<TR>
				<TD>
				<table class=text cellSpacing=0 cellPadding=0 align=center bgColor=black border=0 width="100%"
							bordercolorlight="#000000" bordercolordark="#FFFFFF">							
							<tr><td>
				<DIV id="PPPOE" align=center></DIV>
				</table>
				</TD>
			</TR>
			<TR>
				<TH class=yellow_title>IP地址数据</TH>
			</TR>
			<TR>
				<TD>
				<table class=text cellSpacing=0 cellPadding=0 align=center bgColor=black border=0 width="100%"
							bordercolorlight="#000000" bordercolordark="#FFFFFF">							
							<tr><td>
				<DIV id="IP" align=center></DIV>
				</table>
				</TD>
			</TR>
			<TR>
				<TH class=yellow_title>ATM VC状态和数据</TH>
			</TR>
			<TR>
				<TD>
				<table class=text cellSpacing=0 cellPadding=0 align=center bgColor=black border=0 width="100%"
							bordercolorlight="#000000" bordercolordark="#FFFFFF">							
							<tr><td>
				<DIV id="ATM" align=center></DIV>
				</table>
				</TD>
			</TR>
			<TR>
				<TH class=yellow_title>LAN端口状态和数据</TH>
			</TR>
			<TR>
				<TD>
				<table class=text cellSpacing=0 cellPadding=0 align=center bgColor=black border=0 width="100%"
							bordercolorlight="#000000" bordercolordark="#FFFFFF">							
							<tr><td>
				<DIV id="LAN" align=center></DIV>
				</table>
				</TD>
			</TR>
			<TR>
				<TH class=yellow_title>WLAN端口状态和数据</TH>
			</TR>
			<TR>
				<TD>
				<table class=text cellSpacing=0 cellPadding=0 align=center bgColor=black border=0 width="100%"
							bordercolorlight="#000000" bordercolordark="#FFFFFF">							
							<tr><td>
				<DIV id="WLAN" align=center></DIV>
				</table>
				</TD>
			</TR>
			<TR>
				<TH class=yellow_title>DLS状态和速度</TH>
			</TR>
			<TR>
				<TD>
					<TABLE class=text cellSpacing=0 cellPadding=0 align=center bgColor=black border=0 width="100%"
							bordercolorlight="#000000" bordercolordark="#FFFFFF">							
							<tr><td><DIV id="DLS" align=center></DIV></td></tr>
					</TABLE>			
				</TD>
			</TR>
		</TABLE>		
		<DIV id="idLayerView" style="overflow:auto;width:80%;display:none;">idLayerView</div>		
		<DIV id="idLayerView1" style="overflow:auto;width:80%;display:none;">idLayerView1</div>		
		<DIV id="idLayerView2" style="overflow:auto;width:80%;display:none;">idLayerView2</div>	
		<DIV id="idLayerView3" style="overflow:auto;width:80%;display:none;">idLayerView3</div>	
		</TD></TR>	
</TABLE>
<%@ include file="../foot.jsp"%>
<SCRIPT LANGUAGE="JavaScript">
getData();
</SCRIPT>
