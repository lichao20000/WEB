<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.linkage.litms.resource.DeviceAct"%>
<%//属地信息
			DeviceAct deviceAct = new DeviceAct();
			String strCityList = deviceAct.getCityListSelf(true, "", "",
					request);
			String strVendorList = deviceAct.getVendorList(true, "", "");
%>
<%@ include file="../head.jsp"%>
<LINK REL="stylesheet" HREF="../css/listview.css" TYPE="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="../Js/prototype.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript" src="../Js/edittable.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript" SRC="../Js/visualman.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript">
function CheckChkBox() {
		var oSelect = document.all("device_id");
		if(oSelect != null ){
			if(typeof(oSelect.length) != "undefined") {
				for(var i=0; i<oSelect.length; i++) {
					if(oSelect[i].checked) {
						return true;
					}
				}
				return false;
			}
			else {			   
				if(oSelect.checked) {
					return true;
				}
				return false;
			}
		} 
		return false;
	}
	
function checkForm(param){
	var iStart;
	var hguser=document.all("hguser").value;
	var telephone= document.all("telephone").value;
    var checkradios = document.all("checkType");
	var checkType = "";
    for(var i=0;i<checkradios.length;i++)
	{
	   if(checkradios[i].checked)
	   {
		  checkType = checkradios[i].value;
		  break;
	   }
	}
   if(checkType==0&&document.frm.city_id.value == -1){
	   alert("请选择属地！");
	   document.frm.city_id.focus();
	   return false;
    }
   if(checkType==0&&document.frm.vendor_id.value == -1){
	  alert("请选择厂商！");
	  document.frm.vendor_id.focus();
	  return false;
	}
	if(checkType==0&&document.frm.devicetype_id.value == -1){
	  alert("请选择设备型号！");
	  document.frm.devicetype_id.focus();
	  return false;
	}
	if(checkType==0&&document.frm.softwareversion.value == -1){
	  alert("请选择设备版本！");
	  document.frm.softwareversion.focus();
	  return false;
	}			
	if(checkType==1&&""==hguser&&""==telephone)
	{
	  alert("请填写用户名或电话号码！");
	  document.all("hguser").focus();
	  return false;
    }
	else if(!CheckChkBox())
	{
	   alert("请选择具体设备！");
	    return false;
	}	
	if(!IsNull(document.frm.start.value,"日期")){
		document.frm.start.focus();
		document.frm.start.select();
		return false;
	}
	else if (!document.frm.netType[0].checked && !document.frm.netType[1].checked){
		alert("请选择一种性能类型");
		return false;
	}	
	else{  
	    
		if (document.frm.netType[0].checked && !document.frm.netType[1].checked){
			document.frm.typeList.value = "1";
		}
		if (!document.frm.netType[0].checked && document.frm.netType[1].checked){
			document.frm.typeList.value = "2";
		}
		if (document.frm.netType[0].checked && document.frm.netType[1].checked){
			document.frm.typeList.value = "1,2";
		}
		
		
		var objs = document.getElementsByName("device_id");		
		var device_id_str="";
		for(var i=0;i<objs.length;i++)
		{
		  if(objs[i].checked)
		  {
		     if(""==device_id_str)
		     {
		        device_id_str=objs[i].value;
		     }
		     else
		     {
		        device_id_str+=","+objs[i].value;
		     }
		  }
		  
		}
		document.all("device_ids").value=device_id_str;
		
		//iStart = DateToDesMonth(document.frm.start.value);
		iStart = DateToDes(document.frm.start.value);
		document.frm.hidstart.value = iStart;
		if("select"==param)
		{
		  idLayerView.style.width = document.body.clientWidth;
		  idLayerView.style.display = "";
		  idLayerView.innerHTML = "正在载入数据......";
		  ["tbContainer"].each(Element.hide);
		  document.frm.action="NetPMReportData_SD.jsp";
		}
		else
		{
		   ["tbContainer"].each(Element.show);
		   ["idLayerView"].each(Element.hide);
		    document.frm.action="NetPMReportData_Picture.jsp";
		  
		}
		document.frm.submit();
		return true;
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

function searchType(){    
	var type=0;
	for(var i=0;i<document.frm.SearchType.length;i++){
		 if(document.frm.SearchType[i].checked){
				type=document.frm.SearchType[i].value;
				document.frm.hidtype.value = type;
				//alert("searchType    "+frm.hidtype.value);
				break;
		  }
	}

	if(type==1){
		document.all("title").innerHTML="〖小时报表〗";
		document.all("hour").style.display="";
		document.all("dateDesc").innerHTML="";
		//document.frm.action = "NetPMReportData_SD.jsp";
	}else if(type==2){
		document.all("title").innerHTML="〖日报表〗";
		document.all("hour").style.display="none";
		document.all("dateDesc").innerHTML="";
		//document.frm.action = "NetPMReportDataDay_SD.jsp";
	}else if(type==3){
		document.all("title").innerHTML="〖周报表〗";
		document.all("hour").style.display="none";
		document.all("dateDesc").innerHTML=" （当前日期所在周数据需在下周统计）";
		//document.frm.action = "NetPMReportDataWeek_SD.jsp";
	}else if(type==4){
		document.all("title").innerHTML="〖月报表〗";
		document.all("hour").style.display="none";
		document.all("dateDesc").innerHTML=" （当前日期所在月数据需在下月统计）";
		//document.frm.action = "NetPMReportDataMonth_SD.jsp";
	}

}

function ShowDialog(param)
{   
  //根据用户来查询
  if(param==1)
  {
     tr1.style.display="none";
     tr2.style.display="none";
     tr3.style.display="";
     tr4.style.display="none";
  }
  
  //根据设备版本来查询
  if(param==0)
  {
     tr1.style.display="";
     tr2.style.display="";
     tr3.style.display="none";
     tr4.style.display="none";
  }
  
  //根据设备序列号来查询
  if(param==2)
  {
     tr1.style.display="none";
     tr2.style.display="none";
     tr3.style.display="none";
     tr4.style.display="";
  }  
}


function showChild(param)
{
	var page ="";		
	if(param == "city_id"){
		document.frm.vendor_id.value = "-1";
	}
	if(param == "devicetype_id"){
	    page = "getdevice_version.jsp?vendor_id="+document.frm.vendor_id.value + "&devicetype_id="+ encodeURIComponent(document.frm.devicetype_id.value);
				document.all("childFrm").src = page;
	}
	if(param == "softwareversion")
	{	
		page = "getdevice_model.jsp?city_id="+document.frm.city_id.value+"&vendor_id="+document.frm.vendor_id.value+"&devicetype_id="+document.frm.devicetype_id.value+ "&softwareversion=" + document.frm.softwareversion.value + "&refresh="+Math.random();	
		//alert("url:"+page);
		document.all("childFrm").src = page;
	}
	if(param == "vendor_id")
	{
		page = "getdevice_model_from.jsp?vendor_id="+document.frm.vendor_id.value;
		document.all("childFrm").src = page;	
	}
}


function queryDevByUser(){
	document.all("deviceByusername").innerHTML = "正在载入设备，请稍候.....";
	document.all("childFrm").src= "SD_GetDevByUser.jsp?username=" + document.frm.username.value +"&refresh="+(new Date()).getTime();
	
}

function relateDevice()
{
   var hguser=document.all("hguser").value;
   var telephone= document.all("telephone").value;
   var checkradios = document.all("checkType");
   var checkType = "";
   for(var i=0;i<checkradios.length;i++)
   {
     if(checkradios[i].checked)
	 {
	   checkType = checkradios[i].value;
	   break;
	 }
   }
   //alert("hguser:"+hguser+"   telephone:"+telephone+"   checkType:"+checkType);
   if(1==checkType&&""==hguser&&""==telephone)
   {
      alert("请填写用户名或电话号码！");
      document.all("hguser").focus();
   }
   else if(1==checkType)
   {
      var page="";
      page="getdevice_model.jsp?hguser="+hguser+"&telephone="+telephone+"&needFilter=false&refresh="+Math.random();
      document.all("childFrm").src = page;
   }
   
}


function relateDeviceBySerialno()
{
   var serialnumber=document.all("serialnumber").value;
   var checkradios = document.all("checkType");
   var checkType = "";
   for(var i=0;i<checkradios.length;i++)
   {
     if(checkradios[i].checked)
	 {
	   checkType = checkradios[i].value;
	   break;
	 }
   }
   //alert("hguser:"+hguser+"   telephone:"+telephone+"   checkType:"+checkType);
   if(checkType==2 && serialnumber=="")
   {
      alert("请填写设备序列号！");
      document.all("serialnumber").focus();
   }
   else if(checkType==2)
   {
      var page="";
      page="getdevice_model.jsp?serialnumber="+serialnumber+"&needFilter=false&refresh="+Math.random();
      document.all("childFrm").src = page;
   }
   
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


//全选操作
function selectAll(elmID){
	t_obj = document.getElementsByName(elmID);
	if(!t_obj) return;
	obj = event.srcElement;

	if(obj.checked){
		for(var i=0; i<t_obj.length; i++){
			t_obj[i].checked = true;
		}
	}
	else{
		for(var i=0; i<t_obj.length; i++){
			t_obj[i].checked = false;
		}
	}
}



//定制报表
function subscribe_to(){
    var iStart;
	var hguser=document.all("hguser").value;
	var telephone= document.all("telephone").value;
    var checkradios = document.all("checkType");
	var checkType = "";
    for(var i=0;i<checkradios.length;i++)
	{
	   if(checkradios[i].checked)
	   {
		  checkType = checkradios[i].value;
		  break;
	   }
	}
   if(checkType==0&&document.frm.city_id.value == -1){
	   alert("请选择属地！");
	   document.frm.city_id.focus();
	   return false;
    }
   if(checkType==0&&document.frm.vendor_id.value == -1){
	  alert("请选择厂商！");
	  document.frm.vendor_id.focus();
	  return false;
	}
	if(checkType==0&&document.frm.devicetype_id.value == -1){
	  alert("请选择设备型号！");
	  document.frm.devicetype_id.focus();
	  return false;
	}
	if(checkType==0&&document.frm.softwareversion.value == -1){
	  alert("请选择设备版本！");
	  document.frm.softwareversion.focus();
	  return false;
	}			
	if(checkType==1&&""==hguser&&""==telephone)
	{
	  alert("请填写用户名或电话号码！");
	  document.all("hguser").focus();
	  return false;
    }
	else if(!CheckChkBox())
	{
	   alert("请选择具体设备！");
	    return false;
	}	
	if(!IsNull(document.frm.start.value,"日期")){
		document.frm.start.focus();
		document.frm.start.select();
		return false;
	}
	else if (!document.frm.netType[0].checked && !document.frm.netType[1].checked){
		alert("请选择一种性能类型");
		return false;
	}	
    
    if (document.frm.netType[0].checked && !document.frm.netType[1].checked){
			document.frm.typeList.value = "1";
		}
		if (!document.frm.netType[0].checked && document.frm.netType[1].checked){
			document.frm.typeList.value = "2";
		}
		if (document.frm.netType[0].checked && document.frm.netType[1].checked){
			document.frm.typeList.value = "1,2";
		}
		
		
		var objs = document.getElementsByName("device_id");
		var device_id_str="";
		for(var i=0;i<objs.length;i++)
		{
		  if(objs[i].checked)
		  {
		     if(""==device_id_str)
		     {
		        device_id_str=objs[i].value;
		     }
		     else
		     {
		        device_id_str+=","+objs[i].value;
		     }
		  }
		  
		}
		document.all("device_ids").value=device_id_str;	
		
		objs = document.getElementsByName("SearchType");
		var searchType ="";
		for(var i=0;i<objs.length;i++)
		{
		  if(objs[i].checked)
		  {
		    searchType=objs[i].value;
		    break;
		  }
		}	
		
	//格式化参数
	var param = "?device_ids="+document.all("device_ids").value+"&class1="+document.frm.typeList.value+"&SearchType="+searchType;	
	document.all("url").value = "/Report/frame/treeview/template/NetPMReportForm_Template.jsp"+ param;
	//alert(document.all("url").value+"    "+document.all("url").value.length);
	//return false;	

	var page = "../Report/frame/treeview/addNodeTemplate.jsp?tt="+ new Date().getTime();
	var height = 400;
	var width = 500;
	var left = (screen.width-width)/2;
	var top  = (screen.height-height)/2;
	var w = window.open(page,"ss","left="+left+",top="+top+",width="+width+",height="+height+",resizable=yes,scrollbars=no,toolbar=no");
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
	var url = "NetPMReportChart.jsp";
	var pars = "device_id=" + device_id;
	pars += "&device_name=" + device_name;
	pars += "&loopback_ip=" + loopback_ip;
	pars += "&hidstart=" + starttime;
	pars += "&SearchType=" + frm.hidtype.value;	
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
   ["idLayerView"].each(Element.hide);
   ["tbContainer"].each(Element.hide);
   alert("设备没有配置性能！");
  
}

//AJAX回调函数
function showMRTG(index,req){      
	$("sp_mrtg" + index).innerHTML = req.responseText;
		
}

function showMRTGLoading(req,index){    
	$("sp_mrtg" + index).innerHTML = "<img src=../images/loading.gif>";	
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
</SCRIPT>
<%@ include file="../toolbar.jsp"%>

<FORM METHOD=POST ACTION="NetPMReportData_SD.jsp" NAME="frm"
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
				<td><img src="../images/attention_2.gif" width="15" height="12">&nbsp;查询方式：
				<input type="radio" value="0" onclick="ShowDialog(this.value)"
					name="checkType" checked>按属地和型号&nbsp;&nbsp; <input type="radio"
					value="1" onclick="ShowDialog(this.value)" name="checkType">按用户&nbsp;&nbsp;
				<input type="radio" value="2" onclick="ShowDialog(this.value)"
					name="checkType">按设备</td>
			</tr>
		</table>
		</td>
	</TR>
	<TR>
		<TD>
		<TABLE width="100%" border=0 cellspacing=0 cellpadding=0
			align="center">
			<TR>
				<TD bgcolor=#999999>
				<TABLE class=text cellSpacing=1 cellPadding=1 width="100%"
					align=center bgColor=#999999 border=0>
					<TR>
						<TH colspan=4>性能查询 --<span id="title">〖日报表〗</span></TH>
					</TR>
					<TR bgcolor="#FFFFFF" id="tr1" STYLE="display:">
						<TD align="right" width="10%">属地:</TD>
						<TD align="left" width="30%"><%=strCityList%></TD>
						<TD align="right" width="10%">厂商:</TD>
						<TD align="left" width="30%"><%=strVendorList%></TD>
					</TR>
					<TR bgcolor="#FFFFFF" id="tr2" STYLE="display:">
						<TD align="right" width="10%">设备型号:</TD>
						<TD width="30%">
						<div id="div_devicetype"><select name=devicetype_id class="bk">
							<option value="-1">--请先选择厂商--</option>
						</select></div>
						</TD>
						<TD align="right" width="10%">设备版本:</TD>
						<TD width="30%">
						<div id="div_deviceversion"><select name="device_version"
							class="bk">
							<option value="-1">--请先选择设备型号--</option>
						</select></div>
						</TD>
					</TR>
					<TR bgcolor="#FFFFFF" id="tr3" STYLE="display:none">
						<TD align="right" width="10%">用户名:</TD>
						<TD width="30%"><input type="text" name="hguser" value="" class=bk>
						</TD>
						<TD align="right" width="10%">用户电话号码:</TD>
						<TD width="30%"><input type="text" name="telephone" value=""
							class=bk> <input type="button" class=btn value="查询"
							onclick="relateDevice()"></TD>
					</TR>
					<TR bgcolor="#FFFFFF" id="tr4" STYLE="display:none">
						<TD align="right" width="10%">设备序列号:</TD>
						<TD width="30%"><input type="text" name="serialnumber" value=""
							class=bk></TD>
						<TD align="right" width="10%">设备域名或IP:</TD>
						<TD width="30%"><input type="text" name="loopback_ip" value=""
							class=bk> <input type="button" class=btn value=" 查 询 "
							onclick="relateDeviceBySerialno()"></TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD align="right" width="10%">设备列表: <br>
						<INPUT TYPE="checkbox" onclick="selectAll('device_id')"
							name="device"> 全选</TD>
						<TD colspan="3">
						<div id="div_device"
							style="width:95%; height:100px; z-index:1; top: 100px; overflow:scroll">
						<span>请选择设备！</span></div>
						</TD>
					</TR>
					<TR class=blue_trOut onmouseout="className='blue_trOut'"
						bgColor=#ffffff>
						<TD class="" align=right>报表类型:</TD>
						<TD class=""><INPUT TYPE="radio" NAME="SearchType" CLASS="bk"
							VALUE="1" onClick="javascript:searchType();"> 小时报表 <INPUT
							TYPE="radio" NAME="SearchType" CLASS="bk" VALUE="2"
							onClick="javascript:searchType();" checked> 日报表 <INPUT
							TYPE="radio" NAME="SearchType" CLASS="bk" VALUE="3"
							onClick="javascript:searchType();"> 周报表 <INPUT TYPE="radio"
							NAME="SearchType" CLASS="bk" VALUE="4"
							onClick="javascript:searchType();"> 月报表 <INPUT TYPE="hidden"
							name="hidtype" value="2"></TD>
						<TD class="" align=right>性能类型:</TD>
						<TD class=""><input type="checkbox" name="netType" value="1" checked>
						CPU利用率 <input type="checkbox" name="netType" value="2"> 内存利用率</TD>
					</TR>
					<TR class=blue_trOut onmouseout="className='blue_trOut'"
						bgColor=#ffffff>
						<TD class="" width=180 align=right>日期:</TD>
						<TD class="" colspan="3"><INPUT TYPE="text" NAME="start" class=bk>
						<INPUT TYPE="button" value="▼" class=jianbian
							onclick="showCalendar('day',event)"> &nbsp; <font color="#FF0000">*&nbsp;&nbsp;<span
							id="dateDesc"></span> </font> <INPUT TYPE="hidden"
							name="hidstart"> <INPUT TYPE="hidden" name="hidend"> <select
							name="hour" class=bk STYLE="display:none">
							<%for (int i = 0; i < 24; i++) {

				%>
							<option value="<%=i%>"><%=i%> 点-- <%=i + 1%> 点</option>
							<%}

		%>
						</select></TD>
					</TR>

					<TR class=green_foot>
						<TD colspan=4 align=right><input type="button" name="subscribe"
							onclick="subscribe_to()" value="订阅此报表" class="jianbian">&nbsp;&nbsp;<INPUT
							TYPE="button" name="cmdpicture" onClick="checkForm('picture')"
							value="图形显示" class="jianbian">&nbsp;&nbsp; <INPUT TYPE="button"
							name="cmdOK" value=" 查 询 " class="jianbian"
							onClick="checkForm('select')"> <input type="hidden"
							name="typeList" value=""><input type="hidden" name="device_ids"
							value=""><input type="hidden" id="url" name="url" value="" /></TD>
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


