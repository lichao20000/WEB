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
 //发起查询PPPOE状态的请求 
 doPPOEAndIPAct();
 
}

function doPPOEAndIPAct()
{  
  var url ="paramList_data.jsp";
  var paras = "type=2";
  paras +="&device_id="+device_id;
  paras += "&tt=" + new Date().getTime();
  var Container="PPOEAndIP";
  
  $("PPPOE").innerHTML = "正在获取数据，请稍候...";
  $("IP").innerHTML = "正在获取数据，请稍候...";
  $("ATM").innerHTML = "正在获取数据，请稍候...";
  
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

//AJAX回调函数
//根据容器名称及每个容器的标识位class
//将结果内容插入容器
function showData(Container,req)
{    
	$("idLayerView1").innerHTML = req.responseText;
	$("PPPOE").innerHTML = $("PPPOE_").innerHTML;
	$("IP").innerHTML = $("IP_").innerHTML;
	$("ATM").innerHTML = $("ATM_").innerHTML;
	iframeAutoFit();
}

function iframeAutoFit(){
	try{
		if(window!=parent){
			var a = parent.document.getElementsByTagName("IFRAME");
			for(var i=0; i<a.length; i++){
				if(a[i].contentWindow==window){
					var h1=0, h2=0;
					a[i].parentNode.style.height = a[i].offsetHeight +"px";
					a[i].style.height = "10px";
					if(document.documentElement&&document.documentElement.scrollHeight)
						h1=document.documentElement.scrollHeight;
					if(document.body) h2=document.body.scrollHeight;
					var h=Math.max(h1, h2);
					if(document.all) {h += 4;}
					if(window.opera) {h += 1;}
					a[i].style.height = a[i].parentNode.style.height = h +"px";
				}
			}
		}
	}
	catch (ex){}
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
<TABLE width="100%" border=0 cellspacing=0 cellpadding=0 align="center">
	<TR>
		<TD>
		<TABLE border=0 cellspacing=1 cellpadding=1 width="100%" height="100%">			
			<TR height=30>
				<TH class=yellow_title>PPPOE状态和数据</TH>
			</TR>
			<TR height=50>
				<TD>
				<table class=text cellSpacing=0 cellPadding=0 align=center bgColor=black border=0 width="100%"
							bordercolorlight="#000000" bordercolordark="#FFFFFF">							
							<tr><td class=column><DIV id="PPPOE" align=center></DIV></td></tr>
				</table>
				</TD>
			</TR>
			<TR height=30>
				<TH class=yellow_title>IP地址数据</TH>
			</TR>
			<TR height=50>
				<TD>
				<table class=text cellSpacing=0 cellPadding=0 align=center bgColor=black border=0 width="100%"
							bordercolorlight="#000000" bordercolordark="#FFFFFF">							
							<tr><td class=column><DIV id="IP" align=center></DIV></td></tr>
				</table>
				</TD>
			</TR>
			<TR height=30>
				<TH class=yellow_title>ATM VC状态和数据</TH>
			</TR>
			<TR height=50>
				<TD>
				<table class=text cellSpacing=0 cellPadding=0 align=center bgColor=black border=0 width="100%"
							bordercolorlight="#000000" bordercolordark="#FFFFFF">							
							<tr><td class=column><DIV id="ATM" align=center></DIV></td></tr>
				</table>
				</TD>
			</TR>			
		</TABLE>		
		<DIV id="idLayerView1" style="overflow:auto;width:80%;display:none;">idLayerView1</div>			
		</TD></TR>	
</TABLE>
<SCRIPT LANGUAGE="JavaScript">
getData();
</SCRIPT>
