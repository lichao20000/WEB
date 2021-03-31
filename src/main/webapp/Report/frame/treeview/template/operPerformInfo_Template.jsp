<%@ include file="../../../../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<SCRIPT LANGUAGE="JavaScript" SRC="../../../../Js/prototype.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript" src="../../../../Js/Calendar.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript" src="../../../../Js/CheckForm.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript" SRC="../../../../Js/visualman.js"></SCRIPT>
<link rel="stylesheet" href="../../../../css/css_green.css"
	type="text/css">
<LINK REL="stylesheet" HREF="../../../../css/liulu.css" TYPE="text/css">
<%   
   String device_ids = request.getParameter("device_ids");
   String searchType = request.getParameter("SearchType");
%>
<script type="text/javascript">
function checkForm(param){
var iStart;
if(!IsNull(document.frm.start.value,"日期")){
	document.frm.start.focus();
	document.frm.start.select();
	return false;
}
iStart = DateToDes(document.frm.start.value);
document.frm.hidstart.value = iStart;

if("select"==param)
{
	idLayerView.style.width = document.body.clientWidth;
	idLayerView.style.display = "";
	idLayerView.innerHTML = "正在载入数据......";
	["tbContainer"].each(Element.hide);
	document.frm.action="../../../../Performance/operPerformData_SD.jsp";
}
else
{
	["tbContainer"].each(Element.show);
	["idLayerView"].each(Element.hide);
	document.frm.action="../../../../Performance/operPerformData_picture.jsp";		  
}
//alert("submit:"+document.frm.action);
document.frm.submit();
return true;
}


function DateToDes(v){
	if(v != ""){
		pos = v.indexOf("-");
		if(pos != -1 && pos == 4){
			y = parseInt(v.substring(0,pos));
			v = v.substring(pos+1,v.length);
		}
		pos = v.indexOf("-");
		if(pos != -1){
			m = parseInt(v.substring(0,pos));
			v = v.substring(pos+1,v.length);
		}
		if(v.length>0)
			d = parseInt(v);

		dt = new Date(m+"/"+d+"/"+y);
		var s  = dt.getTime();
		return s/1000;
	}
	else
		return 0;
}


window.onload = function(){

 initTime();
}


function initTime(){
	var vDate = new Date();
	lms = vDate.getTime();
	vDate = new Date(lms-3600*24*1000);
	var y  = vDate.getYear();
	var m  = vDate.getMonth()+1;
	var d  = vDate.getDate();
	var h  = vDate.getHours(); 
	var strM = m<10?"0"+m:""+m;
	var strD = d<10?"0"+d:""+d;
	
	document.frm.start.value = y+"-"+m+"-"+d;

}

//准备图形区域
function doMrtg(deviceLength)
{
   CreatMrtgSpan(deviceLength);
   showTableContainer(true); 
   //alert("doMrtg");  
}

//动态创建span标签，用于mrtg图展示
function CreatMrtgSpan(_length){    
	$("mrtgContainer").innerHTML = "";
	var trStr = "";
	for(var i=1;i<=_length;i++){
		trStr += "<span align=center width='50%' id='sp_mrtg"+ (i-1) +"'></span>";
	}
	$("mrtgContainer").innerHTML = trStr;
	//alert("CreatMrtgSpan:"+$("mrtgContainer").innerHTML);
}

//将tbContainer与idLayerView对象可见开关
function showTableContainer(flag){
	if(flag){
		["idLayerView"].each(Element.hide);
		["tbContainer"].each(Element.show);
	}else{
		["idLayerView"].each(Element.show);
		["tbContainer"].each(Element.hide);
	}
}

//生成MRTG图,发起ajax请求,调用showMRTG生成图形
function CreateMrgtAct(index,device_id,device_name,loopback_ip,class1,starttime,descr,unit){
	var url = "../../../../Performance/operPerformDataChart.jsp";
	var pars = "device_id=" + device_id;
	pars += "&device_name=" + device_name;
	pars += "&loopback_ip=" + loopback_ip;
	pars += "&hidstart=" + starttime;
	pars += "&SearchType=" + frm.SearchType.value;	
	pars += "&class1=" + class1;	
	pars += "&descr="+descr;
	pars += "&unit="+unit;	
	pars += "&tt=" + new Date().getTime();
	//alert("index:"+index+"   pars:"+pars);
	
	pars = encodeURI(encodeURI(pars));
	var myAjax
		= new Ajax.Request(
							url,
							{
								//encoding:"GBK",
								method:"post",
								parameters:pars,
								onFailure:showError,
								onSuccess:function(req){
									showMRTG(index,req);
								},
								onLoading:function(req){
									showMRTGLoading(req,index);
								},
								onException:function(req){
									doException(req,index);
								}
							 }
						  );
}

function showNOData()
{
  $("sp_mrtg0").innerHTML="没有性能数据";
}

//AJAX回调函数
function showMRTG(index,req){  
    //alert("showMRTG:"+index);    
	$("sp_mrtg" + index).innerHTML = req.responseText;
		
}

function showMRTGLoading(req,index){    
	$("sp_mrtg" + index).innerHTML = "<img src=../../../../images/loading.gif>";	
}

//调试信息
function showError(req,index){
	//调试模式
	if(__debug)
		$("debug").innerHTML = req.responseText;
}

function doException(req,index,devInfo){
	$("sp_mrtg" + index).innerHTML = "生成设备"+devInfo[1]+"["+devInfo[2]+"]图形失败!";
}
</script>
<FORM METHOD=POST
	ACTION="../../../../Performance/operPerformData_SD.jsp" NAME="frm"
	target="childFrm">
<TABLE border=0 cellspacing=0 cellpadding=0 width="98%" align="center">
	<tr>
		<td HEIGHT=20>&nbsp;&nbsp;</td>
	</tr>
	<TR>
		<td>
		<table width="100%" align=center height="30" border="0"
			cellspacing="0" cellpadding="0" class="green_gargtd">
			<tr>
				<td width="162" align="center" class="title_bigwhite">设备性能</td>
			</tr>
		</table>
		</td>
	</TR>
	<TR>
		<TD><input
			type="hidden" name="device_ids" value="<%=device_ids %>"> <input
			type="hidden" name="SearchType" value="<%=searchType %>">
		<TABLE width="100%" border=0 cellspacing=0 cellpadding=0
			align="center">
			<TR>
				<TD bgcolor=#999999>
				<TABLE class=text cellSpacing=1 cellPadding=1 width="100%"
					align=center bgColor=#999999 border=0>
					<TR>
						<TH colspan=4>性能查询</TH>
					</TR>
					<TR class=blue_trOut onmouseout="className='blue_trOut'"
						bgColor=#ffffff>
						<TD class="" width=180 align=right>日期: <br>
						</TD>
						<TD class="" colspan=3><INPUT TYPE="text" NAME="start" class=bk
							readonly> <INPUT TYPE="button" value="▼" class=jianbian
							onclick="showCalendar('day',event)"> &nbsp; <font color="#FF0000">*&nbsp;&nbsp;<span
							id="dateDesc"></span> </font> <INPUT TYPE="hidden"
							name="hidstart"> <INPUT TYPE="hidden" name="hidend"></TD>
					</TR>
					<TR class=green_foot>
						<TD colspan=4 align=right><INPUT TYPE="button" name="cmdpicture"
							onClick="checkForm('picture')" value="图形显示" class="jianbian">&nbsp;&nbsp;
						<INPUT TYPE="button" name="cmdOK" value=" 查 询 " class="jianbian"
							onClick="checkForm('select')"></TD>
					</TR>
				</TABLE>
				</TD>
			</TR>
			<tr>
				<td height="20"></td>
			</tr>
			<tr>
				<td width="100%" id="idLayerView" style="border:"></td>
			</tr>
			<tr>
				<td height="20"></td>
			</tr>
			<tr>
				<td>
				<TABLE width="100%" border=0 cellspacing=0 cellpadding=0
					align="center" id="tbContainer" style="display:none"
					bordercolorlight="#000000" bordercolordark="#FFFFFF">
					<TR>
						<TD bgcolor="#999999">
						<TABLE align="center" border=0 cellspacing=1 cellpadding=2
							width="100%" id="tbMrtg" bordercolorlight="#000000"
							bordercolordark="#FFFFFF">
							<TR>
								<TD class=column align=center>
								<div id="mrtgContainer" align=center></div>
								</TD>
							</TR>
						</TABLE>
						</TD>
					</TR>
				</TABLE>
				</td>
			</tr>
		</TABLE>

		</TD>
	</TR>
	<TR>
		<TD HEIGHT=20><IFRAME name="childFrm" ID=childFrm SRC=""
			STYLE="display:none;width:500;height:500" width="100%"></IFRAME></TD>
	</TR>
</TABLE>
</FORM>
