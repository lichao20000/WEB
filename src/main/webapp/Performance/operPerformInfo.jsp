<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@page import="com.linkage.litms.resource.DeviceAct"%>
<%//������Ϣ

			DeviceAct deviceAct = new DeviceAct();
			String strCityList = deviceAct.getCityListSelf(true, "", "",
					request);

			String strVendorList = deviceAct.getVendorList(true, "", "");
%>
<%@ include file="../head.jsp"%>
<title>ҵ�����ܲ�ѯ</title>
<SCRIPT LANGUAGE="JavaScript" id="idParentFun"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript" SRC="../Js/prototype.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript" SRC="../Js/visualman.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript">
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

function checkForm(param){
var j = 0;
idLayerView.innerHTML = "";
    var iStart;
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

    if(checkType=="0"){
	   if (document.frm.city_id.selectedIndex==0) 
			{
				window.alert("��ѡ�����أ�");
				return false;
			}
	 
	   if (document.frm.vendor_id.selectedIndex==0) 
			{
				window.alert("��ѡ���̣�");
				return false;
			}
			
	   if (document.frm.devicetype_id.selectedIndex==0) 
			{
				window.alert("��ѡ���豸�ͺţ�");
				return false;
			}
	
		if (document.frm.softwareversion.selectedIndex==0) 
			{
				window.alert("��ѡ���豸�汾��");
				return false;
			}
	}else
	 if(checkType=="1"){
		 var hguser=document.all("hguser").value;
	     var telephone= document.all("telephone").value;
	         if(""==hguser&&""==telephone)
			   {
			      alert("����д�û�����绰���룡");
			      document.all("hguser").focus();
			      return false;
			   }
	 }else
	   if(checkType=="2"){
		   var serialnumber=document.all("serialnumber").value;
		   var loopback_ip=document.all("loopback_ip").value;
	           if(serialnumber=="" && loopback_ip == "")
				   {
					      alert("�������豸���кŻ����豸������");
					      document.all("serialnumber").focus();
					      return false;
				   }
	   
	   }
	
	var devices = document.getElementsByName("device_id");
	if(devices.length==0){
	        window.alert("�޿�ѡ�豸��");
			return false;
	}
	var device_id_str ="";
	for(var i=0;i<devices.length;i++){
	  
	   if(devices[i].checked){
	     if(""==device_id_str)
	     {
	       device_id_str=devices[i].value;
	     }
	     else
	     {
	       device_id_str+=","+devices[i].value;
	     }	     
	     j++;
	   }	  
	}
	document.all("device_ids").value=device_id_str;
	 if(j==0){
	     
	     window.alert("��ѡ���豸��");
		 return false;
	}
		
    if(!IsNull(document.frm.start.value,"����")){
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
		idLayerView.innerHTML = "������������......";
		["tbContainer"].each(Element.hide);
		document.frm.action="operPerformData_SD.jsp";
	}
	else
	{
	    ["tbContainer"].each(Element.show);
	    ["idLayerView"].each(Element.hide);
	    document.frm.action="operPerformData_picture.jsp";
		  
	}
	document.frm.submit();
	return true;
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
		page = "getdevice_model.jsp?city_id="+document.frm.city_id.value+"&vendor_id="+document.frm.vendor_id.value+"&devicetype_id="+document.frm.devicetype_id.value+ "&softwareversion=" + document.frm.softwareversion.value + "&flag=paramInstanceadd_Config&refresh="+Math.random();	
		document.all("childFrm").src = page;
		
	}
	if(param == "vendor_id")
	{	
		page = "getdevice_model_from.jsp?vendor_id="+document.frm.vendor_id.value;
		document.all("childFrm").src = page;	
	}
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
      alert("����д�û�����绰���룡");
      document.all("hguser").focus();
   }
   else if(1==checkType)
   {
      var page="";
      page="getdevice_model.jsp?hguser="+hguser+"&telephone="+telephone+"&flag=paramInstanceadd_Config&refresh="+Math.random();
      document.all("childFrm1").src = page;
   }
}

function relateDeviceBySerialno()
{
   var serialnumber=document.all("serialnumber").value;
   var loopback_ip=document.all("loopback_ip").value;
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
   if(checkType==2 && serialnumber=="" && loopback_ip == "")
   {
	      alert("�������豸���кŻ����豸������");
	      document.all("serialnumber").focus();
	      return false;
   }
   else if(checkType==2)
   {
	      var page="";
	      page="getdevice_model.jsp?serialnumber="+serialnumber+"&loopback_ip=" + loopback_ip + "&flag=paramInstanceadd_Config&refresh="+Math.random();
	      document.all("childFrm1").src = page;
   }
   
}

function ShowDialog(param)
{
  //�����û�����ѯ
  if(param==1)
  {
     tr1.style.display="none";
     tr2.style.display="none";
     tr3.style.display="";
     tr4.style.display="none";
  }
  
  //�����豸�汾����ѯ
  if(param==0)
  {
     tr1.style.display="";
     tr2.style.display="";
     tr3.style.display="none";
     tr4.style.display="none";
  }
  
  //�����豸���к�����ѯ
  if(param==2)
  {
     tr1.style.display="none";
     tr2.style.display="none";
     tr3.style.display="none";
     tr4.style.display="";
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
	var type=2;
	for(var i=0;i<document.frm.SearchType.length;i++){
		 if(document.frm.SearchType[i].checked){
				type=document.frm.SearchType[i].value;
				document.frm.hidtype.value = type;
				//alert("searchType    "+frm.hidtype.value);
				break;
		  }
	}

	if(type==2){
		document.all("title").innerHTML="���ձ���";	
		document.all("cmdpicture").style.display="none";	
	}else if(type==3){
		document.all("title").innerHTML="���ܱ���";	
		document.all("cmdpicture").style.display="";	
	}else if(type==4){
		document.all("title").innerHTML="���±���";	
		document.all("cmdpicture").style.display="";		
	}
}

//ȫѡ����
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


//���Ʊ���
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
	   alert("��ѡ�����أ�");
	   document.frm.city_id.focus();
	   return false;
    }
   if(checkType==0&&document.frm.vendor_id.value == -1){
	  alert("��ѡ���̣�");
	  document.frm.vendor_id.focus();
	  return false;
	}
	if(checkType==0&&document.frm.devicetype_id.value == -1){
	  alert("��ѡ���豸�ͺţ�");
	  document.frm.devicetype_id.focus();
	  return false;
	}
	if(checkType==0&&document.frm.softwareversion.value == -1){
	  alert("��ѡ���豸�汾��");
	  document.frm.softwareversion.focus();
	  return false;
	}			
	if(checkType==1&&""==hguser&&""==telephone)
	{
	  alert("����д�û�����绰���룡");
	  document.all("hguser").focus();
	  return false;
    }
	else if(!CheckChkBox())
	{
	   alert("��ѡ������豸��");
	    return false;
	}	
	if(!IsNull(document.frm.start.value,"����")){
		document.frm.start.focus();
		document.frm.start.select();
		return false;
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
		
	//��ʽ������
	var param = "?device_ids="+document.all("device_ids").value+"&SearchType="+searchType;	
	document.all("url").value = "/Report/frame/treeview/template/operPerformInfo_Template.jsp"+ param;
	//alert(document.all("url").value+"    "+document.all("url").value.length);
	var page = "../Report/frame/treeview/addNodeTemplate.jsp?tt="+ new Date().getTime();
	var height = 400;
	var width = 500;
	var left = (screen.width-width)/2;
	var top  = (screen.height-height)/2;
	var w = window.open(page,"ss","left="+left+",top="+top+",width="+width+",height="+height+",resizable=yes,scrollbars=no,toolbar=no");
}


function CheckChkBox() {
		var oSelect = document.all("device_id");
		if(typeof(oSelect) == "object" ) {
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
	}


//׼��ͼ������
function doMrtg(deviceLength)
{
   CreatMrtgSpan(deviceLength);
   showTableContainer(true); 
   //alert("doMrtg");  
}

//��̬����span��ǩ������mrtgͼչʾ
function CreatMrtgSpan(_length){    
	$("mrtgContainer").innerHTML = "";
	var trStr = "";
	for(var i=1;i<=_length;i++){
		trStr += "<span align=center width='50%' id='sp_mrtg"+ (i-1) +"'></span>";
	}
	$("mrtgContainer").innerHTML = trStr;
	//alert("CreatMrtgSpan:"+$("mrtgContainer").innerHTML);
}

//��tbContainer��idLayerView����ɼ�����
function showTableContainer(flag){
	if(flag){
		["idLayerView"].each(Element.hide);
		["tbContainer"].each(Element.show);
	}else{
		["idLayerView"].each(Element.show);
		["tbContainer"].each(Element.hide);
	}
}

//����MRTGͼ,����ajax����,����showMRTG����ͼ��
function CreateMrgtAct(index,device_id,device_name,loopback_ip,class1,starttime,descr,unit){
	var url = "operPerformDataChart.jsp";
	var pars = "device_id=" + device_id;
	pars += "&device_name=" + device_name;
	pars += "&loopback_ip=" + loopback_ip;
	pars += "&hidstart=" + starttime;
	pars += "&SearchType=" + frm.hidtype.value;	
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
  ["idLayerView"].each(Element.hide);
  ["tbContainer"].each(Element.hide);
  alert("�豸û���������ܣ�");
}

//AJAX�ص�����
function showMRTG(index,req){  
    //alert("showMRTG:"+index);    
	$("sp_mrtg" + index).innerHTML = req.responseText;
		
}

function showMRTGLoading(req,index){    
	$("sp_mrtg" + index).innerHTML = "<img src=../images/loading.gif>";	
}

//������Ϣ
function showError(req,index){
	//����ģʽ
	if(__debug)
		$("debug").innerHTML = req.responseText;
}

function doException(req,index,devInfo){
	$("sp_mrtg" + index).innerHTML = "�����豸"+devInfo[1]+"["+devInfo[2]+"]ͼ��ʧ��!";
}


</SCRIPT>
<%@ include file="../toolbar.jsp"%>
<form name="frm" method="post" ACTION="operPerformData_SD.jsp"
	target="childFrm"><input type="hidden" name="timer" value="" />
<table width="98%" border="0" align="center" cellpadding="0"
	cellspacing="0">
	<TR>
		<TD HEIGHT=20>&nbsp;</TD>
	</TR>
	<tr>
		<td>
		<table width="100%" border="0" cellspacing="0" cellpadding="2">
			<tr>
				<td>

				<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
					<TR>
						<TD>
						<table width="100%" border="0" align="center" cellpadding="0"
							cellspacing="0">
							<tr>
								<td>
								<table width="100%" height="30" border="0" cellspacing="0"
									cellpadding="0" class="green_gargtd">
									<tr>
										<td width="162">
										<div align="center" class="title_bigwhite">ҵ�����ܲ�ѯ</div>
										</td>
										<td><img src="../images/attention_2.gif" width="15"
											height="12"> 
										<input type="radio" value="1" onclick="ShowDialog(this.value)"
											name="checkType" checked>���û�&nbsp;&nbsp; <input type="radio"
											value="2" onclick="ShowDialog(this.value)" name="checkType">���豸
										</td>

									</tr>
								</table>
								</td>
							</tr>
							<tr>
								<td bgcolor="#FFFFFF">
								<table width="100%" border=0 align="center" cellpadding="1"
									cellspacing="1" bgcolor="#999999" class="text">
									<TR>
										<TH colspan=4>���ܲ�ѯ --<span id="title">���ձ���</span></TH>
									</TR>
									<TR bgcolor="#FFFFFF" id="tr1" STYLE="display:none ">
										<TD align="right" width="10%">����:</TD>
										<TD align="left" width="30%"><%=strCityList%></TD>
										<TD align="right" width="10%">����:</TD>
										<TD align="left" width="30%"><%=strVendorList%></TD>
									</TR>
									<TR bgcolor="#FFFFFF" id="tr2" STYLE="display:none">
										<TD align="right" width="10%">�豸�ͺ�:</TD>
										<TD width="30%">
										<div id="div_devicetype"><select name=devicetype_id class="bk">
											<option value="-1">--����ѡ����--</option>
										</select></div>
										</TD>
										<TD align="right" width="10%">�豸�汾:</TD>
										<TD width="30%">
										<div id="div_deviceversion"><select name="device_version"
											class="bk">
											<option value="-1">--����ѡ���豸�ͺ�--</option>
										</select></div>
										</TD>
									</TR>
									<TR bgcolor="#FFFFFF" id="tr3" STYLE="display:">
										<TD align="right" width="10%">�û���:</TD>
										<TD width="30%"><input type="text" name="hguser" value=""
											class=bk></TD>
										<TD align="right" width="10%">�û��绰����:</TD>
										<TD width="30%"><input type="text" name="telephone" value=""
											class=bk> <input type="button" class=btn value="��ѯ"
											onclick="relateDevice()"></TD>
									</TR>
									<TR bgcolor="#FFFFFF" id="tr4" STYLE="display: none">
										<TD align="right" width="10%">�豸���к�:</TD>
										<TD width="30%"><input type="text" name="serialnumber"
											value="" class=bk></TD>
										<TD align="right" width="10%">�豸������IP:</TD>
										<TD width="30%"><input type="text" name="loopback_ip" value=""
											class=bk> <input type="button" class=btn value=" �� ѯ "
											onclick="relateDeviceBySerialno()"></TD>
									</TR>
									<TR bgcolor="#FFFFFF">
										<TD align="right" width="10%">�豸�б�: <br>
										<INPUT TYPE="checkbox" onclick="selectAll('device_id')"
											name="device">ȫѡ</TD>
										<TD colspan="3">
										<div id="div_device"
											style="width: 95%; height: 100px; z-index: 1; top: 100px; overflow: scroll">
										<span>��ѡ���豸��</span></div>
										<div id="pmdiv" style="display: none"></div>
										</TD>
									</TR>

									<TR bgColor=#ffffff>
										<TD align="right" width="10%">��������: <br>
										</TD>
										<TD colspan="3"><INPUT TYPE="radio" NAME="SearchType"
											CLASS="bk" VALUE="2" onClick="javascript:searchType();"> �ձ���
										<INPUT TYPE="radio" NAME="SearchType" CLASS="bk" VALUE="3"
											onClick="javascript:searchType();" checked> �ܱ��� <INPUT
											TYPE="radio" NAME="SearchType" CLASS="bk" VALUE="4"
											onClick="javascript:searchType();"> �±��� <INPUT TYPE="hidden"
											name="hidtype" value="3"></TD>
									</TR>
									<TR class=blue_trOut onmouseout="className='blue_trOut'"
										bgColor=#ffffff>
										<TD class="" width=180 align=right>����: <br>
										</TD>
										<TD class="" colspan=3><INPUT TYPE="text" NAME="start"
											class=bk readonly> <INPUT TYPE="button" value="��"
											class=jianbian onclick="showCalendar('day',event)"> &nbsp; <font
											color="#FF0000">*&nbsp;&nbsp;<span id="dateDesc"></span> </font>
										<INPUT TYPE="hidden" name="hidstart"> <INPUT TYPE="hidden"
											name="hidend"></TD>
									</TR>
									<TR class=green_foot>
										<TD colspan=4 align=right><input type="button"
											name="subscribe" onclick="subscribe_to()" value="���Ĵ˱���"
											class="jianbian">&nbsp;&nbsp;<INPUT TYPE="button"
											name="cmdpicture" value=" ͼ�α��� " class="jianbian"
											onClick="checkForm('picture')">&nbsp;&nbsp;<INPUT
											TYPE="button" name="cmdOK" value=" ���ݱ��� " class="jianbian"
											onClick="checkForm('select')"> <input type="hidden"
											name="url" value=""><input type="hidden" name="device_ids"
											value=""></TD>
									</TR>
								</table>
								</td>
							</tr>
						</table>
						</TD>
					</TR>
					<tr>
						<td height="20"></td>
					</tr>
					<tr>
						<td width="100%" id="idLayerView" style="border: "></td>
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

				<input type="hidden" name="expression_Name"></td>
			</tr>

			<tr>
				<td class=column1>
				<div id="childDiv"></div>
				</td>
			</tr>
			<tr>
				<td height="40"></td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td><IFRAME ID=childFrm name="childFrm"
			STYLE="display:none;width:500;height:500"></IFRAME></td>
	</tr>
<tr>
		<td><IFRAME ID=childFrm1 name="childFrm1"
			STYLE="display:none;width:500;height:500"></IFRAME></td>
	</tr>
	
</table>
</form>
<%@ include file="../foot.jsp"%>
