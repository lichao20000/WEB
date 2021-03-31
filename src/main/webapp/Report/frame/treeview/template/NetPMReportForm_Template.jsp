<%@ include file="../../../../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.linkage.litms.common.filter.SelectCityFilter"%>
<%@ page import="com.linkage.litms.common.database.*"%>
<%@ page import="com.linkage.litms.common.util.FormUtil"%>
<%@ page import="com.linkage.litms.performance.GeneralNetPerf"%>
<%@ page import="com.linkage.litms.resource.DeviceAct"%>
<SCRIPT LANGUAGE="JavaScript" SRC="../../../../Js/visualman.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript" SRC="../../../../Js/prototype.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript" src="../../../../Js/edittable.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript" src="../../../../Js/Calendar.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript" src="../../../../Js/CheckForm.js"></SCRIPT>
<%
	String type=request.getParameter("type");
	String start=request.getParameter("start");
	String hour=request.getParameter("hour");
	String chk_val;
	try
	{
		chk_val=java.net.URLDecoder.decode(request.getParameter("chk_val"),"UTF-8");
	} catch (Exception e)
	{
		chk_val=request.getParameter("chk_val");
	}
	String device_id=request.getParameter("device_id");
	String vendor_id=request.getParameter("vendor_id");



%>
<link rel="stylesheet" href="../../../../css/css_green.css"
	type="text/css">
<LINK REL="stylesheet" HREF="../../../../css/listview.css"
	TYPE="text/css">
<LINK REL="stylesheet" HREF="../../../../css/liulu.css" TYPE="text/css">
<script type="text/javascript">

//$(function(){
//	//查询
//	$("input[@name='cmdOK']").click(function(){
//		alert()
//		$("#idLayerView").html("<img src='../images/loading.gif'>正在载入数据，请等待......");
//		$.post(
//			"NetPMReportData_V3.jsp",
//			{
//				type:$("input[@name='type']").val(),
//				start:$("input[@name='start']").val(),
//				hour:$("input[@name='hour']").val(),
//				chk_val:encodeURIComponent($("input[@name='chk_val']").val()),
//				device_id:$("input[@name='device_id']").val(),
//				vendor_id:$("input[@name='vendor_id']").val()
//			},
//			function(data){
//				$("#idLayerView").html(data);
//			}
//		);
//	});
//});
//





function checkForm(param){
if("select"==param)
{
	idLayerView.style.width = document.body.clientWidth;
	idLayerView.style.display = "";
	idLayerView.innerHTML = "正在载入数据......";
	["tbContainer"].each(Element.hide);
	
	var param = "?device_id="+document.all("device_id").value+"&type="+document.all("type").value+"&start="+document.frm.start.value
		+"&hour="+document.frm.hour.value+"&vendor_id="+document.frm.vendor_id.value+"&chk_val="+document.all("chk_val").value;	

//	document.frm.action="../../../../Performance/NetPMReportData_V3.jsp";
	document.all("childFrm").src = "./NetPmData.jsp" + param;
}
else
{
	["tbContainer"].each(Element.show);
	["idLayerView"].each(Element.hide);
	document.frm.action="../../../../Performance/NetPMReportData_Picture.jsp";		  
}
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
   //alert("doMrtg:"+deviceLength);
   CreatMrtgSpan(deviceLength);  
   showTableContainer(true);   
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
	var url = "../../../../Performance/NetPMReportChart.jsp";
	var pars = "device_id=" + device_id;
	pars += "&device_name=" + device_name;
	pars += "&loopback_ip=" + loopback_ip;
	pars += "&hidstart=" + starttime;	
	pars += "&SearchType=" + frm.SearchType.value;	
	pars += "&class1=" + class1;	
	pars += "&hour=" + frm.hour.value;	
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
  //alert("showNOData");
  $("sp_mrtg0").innerHTML="没有性能数据";
}

//AJAX回调函数
function showMRTG(index,req){ 
    //alert("showMRTG:"+index);     
	$("sp_mrtg" + index).innerHTML = req.responseText;
	//alert($("sp_mrtg" + index).innerHTML);
		
}

function showMRTGLoading(req,index){
    //alert("showMRTGLoading:"+index);    
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
//----------------------------------------------------------------------------------

// 遍历所有设备checkbox 当某设备有端口被选中后即将设备选中
// 当某设备所有端口都未选中后即将设备未选中
function fixDeviceCheck(){
	//alert("fixDeviceCheck in");
	Dobj = document.getElementsByName("deviceId");
	if(!Dobj) return;
	
	//alert("Dobj.length:"+Dobj.length);
	for(var i=0;i<Dobj.length;i++){
		//deviceId checkbox value格式为 DeviceId+$_$+DeviceName+$_$+DeviceIp
		var id = Dobj[i].value.split("$_$")[0];
		
		var indexCheckbox = document.getElementsByName("IndexId"+id);
		var IndexcheckedCount = 0;
		//alert("indexCheckbox.length:"+indexCheckbox.length);
		for(var j=0;j<indexCheckbox.length;j++){
			if(indexCheckbox[j].checked == true){
				Dobj[i].checked = true;
				break;
			}else{
				IndexcheckedCount++;
			}							
		}
		//所有索引都未选中时
		if(IndexcheckedCount==indexCheckbox.length){
			Dobj[i].checked = false;
		}
	}
}
//add by mym 2007-08-09
//增加全选功能
function checkAll(ui)
{
    cur = document.all("selectAll");
    obj = document.getElementById("outData");
    boxes = obj.getElementsByTagName("input");    

    if (boxes != null)
    {
       for (i=0; i<boxes.length;i++)
       {
           if (boxes[i].type == "checkbox")
           {
               if (ui == "btn")
               {
                   boxes[i].checked = true;
               }
               else if (ui == "box")
               {
                   if (cur.checked == true)
                   {
                      boxes[i].checked = true;
                   }
                   else
                   {
                      boxes[i].checked = false;
                   }
               }
           }
       }
    }
    else
    {
        alert("暂时没有可以选择数据！");
    }
}
</script>
<html>
<body>
<FORM METHOD=POST
	 NAME="frm"
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
				<td width="162" align="center" class="title_bigwhite">定制报表</td>
			</tr>
		</table>
		</td>
	</TR>
	<TR>
		<TD>
			<input type="hidden" name="device_id" value="<%=device_id %>"> 
			<input type="hidden" name="type" value="<%=type %>">
			<input type="hidden" name="chk_val" value="<%=chk_val %>">
			<input type="hidden" name="vendor_id" value="<%=vendor_id %>">

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
						<TD class="" width=180 align=right>日期:</TD>
						<TD class="" colspan=3><INPUT TYPE="text" NAME="start" class=bk> <INPUT
							TYPE="button" value="▼" class=jianbian
							onclick="showCalendar('day',event)"> &nbsp; <font color="#FF0000">*&nbsp;&nbsp;<span
							id="dateDesc"></span> </font> <INPUT TYPE="hidden"
							name="hidstart"> <INPUT TYPE="hidden" name="hidend"> <select
							name="hour" class=bk
							STYLE="<%="1".equals(type)?"display:":"display:none" %>">
							<%for (int i = 0; i < 24; i++) {

				%>
							<option value="<%=i%>"><%=i%> 点-- <%=i + 1%> 点</option>
							<%}

		%>
						</select></TD>
					</TR>
					<TR class=green_foot>
						<TD colspan=4 align=right><INPUT TYPE="button" name="cmdpicture"
							onClick="checkForm('picture')" value="图形显示" class="jianbian" STYLE="display:none;">&nbsp;&nbsp;
						<INPUT TYPE="button" name="cmdOK" value=" 查 询 " onClick="checkForm('select')" class="jianbian"></TD>
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
</body>

<html>