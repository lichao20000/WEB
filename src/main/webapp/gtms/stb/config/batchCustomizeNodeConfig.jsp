<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage"%>
<%@ page import="com.linkage.litms.LipossGlobals" %>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=GBK">
		<link href="<s:url value="/css2/global.css"/>" rel="stylesheet"
			type="text/css">
		<link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet"
			type="text/css">
		<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
		<lk:res />
<SCRIPT LANGUAGE="JavaScript">
//ϵͳ����
   var instAreaName = '<%= LipossGlobals.getLipossProperty("InstArea.ShortName")%>';
    //����
	function ExecMod(){
		var btnValue4IP = $("input[@name='checkIPButtonValue']").val(); // �����ж�ʹ�������ύ��ʽ
		var btnValue4MAC = $("input[@name='checkMACButtonValue']").val(); // �����ж�ʹ�������ύ��ʽ
		//�����ύ����������IP��MAC��ַ�θ�ֵ
		if("byBatchIP"==btnValue4IP){
			$("input[@name='btnValue4IP']").attr("value",btnValue4IP);
			$("input[@name='ipCheck']").attr("value","1");
			importIP();
		}else{
			inputIPbyHand();
		}
		if("byBatchMAC"==btnValue4MAC){
			$("input[@name='btnValue4MAC']").attr("value",btnValue4MAC);
			$("input[@name='macCheck']").attr("value","1");
			importMAC();
		}else{
			inputMACbyHand();
		}
		//������
		var taskName =  $("#taskName").val();
		//����
		var cityId = $("select[@name='cityId']").val();
		if(""==trim(taskName)){
			alert("����������Ϊ�գ�");
			return false; 
		} 
		if(""==cityId||"-1"==cityId){
			alert("��ѡ������");
			return false;
		}
		//����
		var vendorId = $("select[@name='vendorId']").val();
		//$("input[@name='vendor_Id']").attr("value",vendorId);
		//�ͺ�
		var deviceModelIds = "";
		$("input[@name='deviceModelId'][@checked]").each(function(){ 
	    	deviceModelIds += $(this).val()+",";
	    });
	    if(deviceModelIds==""){
			alert("��ѡ�������ͺ�!");
			return false;
		}
	    if(deviceModelIds.length>0){
	    	$("input[@name='deviceModelIds']").attr("value",deviceModelIds.substring(0,deviceModelIds.length-1));
	    }
	    
		//�汾
		var deviceTypeIds ="";
		
	    $("input[@name='deviceTypeId'][@checked]").each(function(){ 
	    	deviceTypeIds += $(this).val()+",";
	    });
	    if(deviceTypeIds==""){
			alert("��ѡ�������汾!");
			return false;
		}
	    if(deviceTypeIds.length>0){
	    	$("input[@name='deviceTypeIds']").attr("value",deviceTypeIds.substring(0,deviceTypeIds.length-1));
	    }
	    if(!checkForm()){
	    	return false;
	    }
		var paramNodePath = ""; 
		var paramValue = "";
		var paramType = "";
		$("#fatherDiv div").each(function(i){
 			//��������	
 			paramNodePath += "," + $("#paramNodePath"+i).val();
 			paramValue    += "," + $("#paramValue"+i).val();
 			paramType     += "," + $("#paramType"+i).val();
         });
 	 	$("input[@name='paramNodePath']").attr("value",paramNodePath);
 	 	$("input[@name='paramValue']").attr("value",paramValue);
 		$("input[@name='paramType']").attr("value",paramType);
	    
		$("form[@name='batchexform']").attr("action","batchCustomNodeConfig!importConfig.action");
		$("form[@name='batchexform']").submit();
	}
    //�ֶ�����ɣе�ַ��
	function inputIPbyHand(){
		var isCheckPass= true ;
	    var ipcheckAdd = document.getElementsByName("isOpenIP");
		var ipcheck = "0";
		var ipSG = "";
		if(ipcheckAdd[1].checked)
		{
			ipcheck = "1";
			var IPduanList = $("tr[id*=setIPduan]");
			IPduanList.each(
					function(i)
					{
						var startip = $("input[name=start_ip]",this.innerHTML).val();
						var endip = $("input[name=end_ip]",this.innerHTML).val();
						if(!checkIP(startip) || !checkIP(endip))
						{
							isCheckPass = false;
							return;
						}
						else
						{
							if(convertIP(startip)>convertIP(endip))
							{
								alert("��ʼIP���ܴ�����ֹIP");
								isCheckPass = false;
								return;
							}
							ipSG += startip + ',' + endip + ';';
						}
					}
			);
		}
		if(isCheckPass)
		{
			ipSG = ipSG.substring(0,ipSG.length-1);
			$("input[@name='ipSG']").attr("value",ipSG);
			$("input[@name='ipCheck']").attr("value",ipcheck);
		}
	}
	//�ֶ�����MAC
	function inputMACbyHand(){
		 var isCheckMACPass= true ;
		    var macCheckAdd = document.getElementsByName("isOpenMAC");
			var maccheck = "0";
			var macSG = "";
			if(macCheckAdd[1].checked)
			{
				maccheck = "1";
				var MACduanList = $("tr[id*=setMACduan]");
				MACduanList.each(
						function(j)
						{	
							var startmac = $("input[name=start_mac]",this.innerHTML).val();
							var endmac   = $("input[name=end_mac]",this.innerHTML).val();
							if(!checkMAC(startmac) || !checkMAC(endmac))
							{
								isCheckMACPass = false;
								return ;
							}
							else
							{
								macSG += startmac + ',' + endmac + ';';
							}
						}
				);
			}
			if(isCheckMACPass)
			{
				macSG = macSG.substring(0,macSG.length-1);
				$("input[@name='macSG']").attr("value",macSG);
				$("input[@name='macCheck']").attr("value",maccheck);
			}
	}
    //����ɣе�ַ��
    function importIP(){
    	var myFile = $("input[@name='uploadIP']").val();
		if(""==myFile){	
			alert("��ѡ���ļ�!");
			return false;
		}
		var filet = myFile.split(".");
		if(filet.length<2)
		{
			alert("��ѡ���ļ�!");
			return false;
		} 
		if("xls" == filet[filet.length-1])
		{
			var file = myFile.split("\\");
			var fileName = file[file.length-1];
			$("input[@name='uploadFileName4IP']").attr("value",fileName);
		}else
		{
			alert("��֧�ֺ�׺Ϊxls���ļ�");
			return false;
		}
		
    }
    //У�鵼��MAC��ַ�ļ�
    function importMAC(){
    	var myFileMAC = $("input[@name='uploadMAC']").val();
		if(""==myFileMAC){	
			alert("��ѡ���ļ�!");
			return false;
		}
		var filetMAC = myFileMAC.split(".");
		if(filetMAC.length<2)
		{
			alert("��ѡ���ļ�!");
			return false;
		}
		if("xls" == filetMAC[filetMAC.length-1])
		{
			var file2 = myFileMAC.split("\\");
			var fileName2 = file2[file2.length-1];
			$("input[@name='uploadFileName4MAC']").attr("value",fileName2);
		}else
		{
			alert("��֧�ֺ�׺Ϊxls���ļ�");
			return false;
		}
    }
    function checkForm(){
		var result = true;
		$("#fatherDiv div").each(function(i){
 			//��������
 			var tempPath = $("#paramNodePath"+i).val();
 			var tempValue = $("#paramValue"+i).val();
 			var tempType = $("#paramType"+i).val();
 			if(tempPath==""){
 				alert("��"+(i+1)+"�������ڵ�·��Ϊ�գ�");
 				result = false;
 				return false;
 			}
 			else if(tempValue==""){
 				alert("��"+(i+1)+"������ֵΪ�գ�");
 				result = false;
 				return false;
 			}
 			else if(tempType==""){
 				alert("��"+(i+1)+"����������Ϊ�գ�");
 				result = false;
 				return false;
 			}
         });
		return result;
     }
    
    
	function softdivcl()
	{
		$("div[@id='div_css']").hide();
	}
	// ����excelģ��
	function toExport()
	{
		$("form[@name='batchexform']").attr("action","batchCustomNodeConfig!downloadTemplate.action");
		$("form[@name='batchexform']").submit();
	}
	//ȥ���ո�
	function trim(str){
     return str.replace(/(^\s*)|(\s*$)/g,"");
	}
</SCRIPT>

	</head>
	<body>
		<TABLE width="98%" align="center" class="querytable">
			<TR>
				<TD>
					<IMG height=20 src="<s:url value='/images/bite_2.gif'/>" width=24>
					����ǰ��λ�ã��������ƽڵ�����
				</TD>
			</TR>
		</TABLE>
		<br>
		<s:form action="batchCustomNodeConfig!initBatchConfig.action" method="post" enctype="multipart/form-data" name="batchexform" onsubmit="">
			<input type="hidden" name="deviceModelIds" value=""/>
			<input type="hidden" name="paramNodePath"   value="">
			<input type="hidden" name="paramValue"   value="">
			<input type="hidden" name="paramType"   value="">
			<input type="hidden" name="deviceTypeIds"   value="">
			<input type="hidden" name="macSG" value="">
			<input type="hidden" name="ipSG"   value="">
			<input type="hidden" name="ipCheck"   value="">
			<input type="hidden" name="macCheck"   value="">
			<input type="hidden" name="btnValue4MAC"   value="">
			<input type="hidden" name="btnValue4IP"   value="">
			<table class="querytable" width="98%" align="center">
				<tr>
					<td colspan="4" class="title_1">
						�������ƽڵ�����
					</td>
				</tr>
				<TR>
					<TD class="title_2" align="center" width="15%">��������</TD>
					<TD colspan="3"> <input type="text" id="taskName" name="taskName" width="500"> </TD>
				</TR>
				<TR>
					<TD class="title_2" align="center" width="15%">
						����
					</TD>
				<TD width="85%" colspan="3">
						<s:select list="cityList" name="cityId" headerKey="-1"
							headerValue="��ѡ������" listKey="city_id" listValue="city_name"
							value="cityId" cssClass="bk" theme="simple"></s:select>
					</TD>
			    </TR>
				<TR>
					<TD class="title_2" align="center" width="15%">
						����
					</TD>
					<TD width="85%" colspan="3">
						<s:select list="vendorList" name="vendorId" headerKey="-1"
							headerValue="��ѡ����" listKey="vendor_id" listValue="vendor_add"
							value="vendorId" cssClass="bk" onchange="getDeviceModel();"
							theme="simple">
					</s:select>
					</TD>
				</TR>
				<TR>
					<TD class="title_2" align="center" width="15%">
						�ͺ�
					</TD>
					<TD width="85%" colspan="3">
						<div id="adaptModelType">
							 ��ѡ����
						</div>
					</TD>
				</TR>
				<TR>
					<TD class="title_2" align="center" width="15%">
						�����汾
					</TD>
					<TD width="85%" colspan="3">
						<div id="softVersion">
							  ��ѡ���ͺ�
						</div>
					</TD>
				</TR>
				<TR>
					<TD class="title_2" align="center" width="15%">
						����IP��ַ��
					</TD>
					<TD width="85%" colspan="3">
						<input type="radio" id="isOpenIP" name="isOpenIP" value="0" checked="checked" onclick="clkipCheck();">��</input>&nbsp;&nbsp;
						<input type="radio" id="isOpenIP" name="isOpenIP" value="1" onclick="clkipCheck();">��</input>&nbsp;&nbsp;&nbsp;
						<button name="addIP" onclick="addIPAddress()"  style="display:none">�ֶ�����IP��ַ��</button>&nbsp;&nbsp;
						<button name="importIP" onclick="batchAddIPAddress()"  style="display:none">��������IP��ַ��</button>&nbsp;&nbsp;
						<input type="hidden" id="checkIPButtonValue" name="checkIPButtonValue" value="" />
					</TD>
				</TR>
				<tr id="importIPAdd" style="display: none">
					<td class="title_2" align="center" width="15%">
						��ѡ���ļ�
					</td>
					<td width="85%" colspan="3">
						<s:file label="�ϴ�" theme="simple" name="uploadIP"></s:file><font color="red">*</font>
						xls��ʽ�ĵ�
						<a href="javascript:void(0);" onclick="toExport();"><font color="red">����ģ��</font></a>
						<input type="hidden" name="uploadFileName4IP" value=""/>
					</td>
				</tr>
				<TR id="nodeAddIP">
					<TD class="title_2" align="center" width="15%">
						����MAC��ַ��
					</TD>
					<TD width="85%" colspan="3">
						<input type="radio" id="isOpenMAC" name="isOpenMAC" value="0" checked="checked" onclick="clkMACCheck();">��</input>&nbsp;&nbsp;
						<input type="radio" id="isOpenMAC" name="isOpenMAC" value="1" onclick="clkMACCheck();">��</input>&nbsp;&nbsp;&nbsp;
						<button name="addMAC" onclick="addMACAddress()"  style="display:none">�ֶ�����MAC��ַ��</button>&nbsp;&nbsp;
						<button name="importMAC" onclick="batchAddMACAddress()"  style="display:none">��������MAC��ַ��</button>&nbsp;&nbsp;
						<input type="hidden" id="checkMACButtonValue" name="checkMACButtonValue" value="" />
					</TD>
				</TR>
				<tr id="importMACAdd" style="display: none">
					<td class="title_2" align="center" width="15%">
						��ѡ���ļ�
					</td>
					<td width="85%" colspan="3">
						<s:file label="�ϴ�" theme="simple" name="uploadMAC"></s:file><font color="red">*</font>
						xls��ʽ�ĵ�
						<a href="javascript:void(0);" onclick="toExport();"><font color="red">����ģ��</font></a>
						<input type="hidden" name="uploadFileName4MAC" value=""/>
					</td>
				</tr>
				<TR id="nodeAddMAC">
					<TD class="title_2" align="center" width="15%">
						�ڵ�
					</TD>
					<TD width="85%" colspan="3">
						<button onclick="doAdd()">���ӽڵ�</button>&nbsp;&nbsp;
					</TD>
				</TR>
				<TR>
					<TD colspan="4">
						<div id="fatherDiv">
							<div id="paramAddDiv0">
								<table width="100%">
									<TR>
										<td  width="15%" class="title_2">
											<span >
												<a href="javascript:doDelete()">
													ɾ��  
												 </a>
											</span>
										  | &nbsp;	�����ڵ�·��
										</td>
										<td width="85%" colspan="3" >
											<input type="text" id="paramNodePath0" name="paramNodePath0" style="width:800px;"> 
										</td>
									</TR>
									<TR>
										<td   width="15%" class="title_2">
											����ֵ
										</td>
										<td  width="35%" >
											<input type="text" id="paramValue0" name="paramValue0" value=""/>
										</td>
										<td  class="title_2" width="15%">
											��������
										</td>
										<td  width="35%" >
											<select name="paramType0" id="paramType0" cssClass="bk">
											 	<option value="-1">==��ѡ��==</option>
											 	<option value="1">string</option>
											 	<option value="2">int</option>
											 	<option value="3">unsignedInt</option>
											 	<option value="4">boolean</option>
											 </select>
										</td>
									</TR>
									</table>
								</div>
						</div>
					</TD>
				</TR>
				<TR>
					<td colspan="4" class="foot" align="right">
						<div class="right">
							<button onclick="ExecMod()">
								����
							</button>
						</div>
					</td>
				</TR>
			</table>
		</s:form>
		<br>  
		<br>
		<div id="divDetail"
			style="position: absolute; z-index: 255; top: 200px; left: 350px; width: 40%; border-width: 1; border-style: ridge; background-color: #eeeeee; padding-top: 10px; display: none"></div>
		<div id="div_css" style="position: absolute; z-index: 255; top: 200px; left: 250px; width: 55%; border-width: 1; border-style: ridge; background-color: #eeeeee; padding-top: 10px; display: none">
			<table class="querytable" align="center" width="100%">
				<tr>
					<td width="30%" id="ventd" class="title_2" align="center"> 
					</td>
					<td id="softVershow" class="title_2" align="center">	
					</td>
				</tr>
				<tr>
					<td colspan="2" class="title_2" align="center">
						<button name="softdivtbn" onclick="softdivcl()">
						�ر�
						</button>
					</td>
				</tr>
			</table>				
		</div>
	</body>
<script type="text/javascript">
   var ipDuanCount = 1;
   var macDuanCount = 1;
 //ȫѡ
	function selectAllModel(checkOBJ){
		if(checkOBJ.checked){
			 $("input[@name='deviceModelId']").attr("checked",true); 
		}
 		else{
 			$("input[@name='deviceModelId']").attr("checked",false);
 		}
	}
	/**
	**���ݳ��̻�ȡ�豸�ͺţ����Ը�ѡ�����ʽ���ֳ���
	**/ 
   function getDeviceModel(){
		var vendorId = $("select[@name='vendorId']").val();
		var url = "<s:url value='/gtms/stb/config/batchCustomNodeConfig!getDeviceModel.action'/>";
		if(vendorId!="-1"){
			$("div[@id='adaptModelType']").html("");
			$.post(url,{
				vendorId:vendorId
			},function(ajax){
				if(ajax!=""){
					var lineData = ajax.split("#");
					if(typeof(lineData)&&typeof(lineData.length)){
						var checkboxtxt = "<input type='checkbox' name='allDeviceModel' onclick='javascript:selectAllModel(this);getSoftVersion()'>ȫѡ &nbsp;&nbsp;&nbsp;";
						$("div[@id='adaptModelType']").append(checkboxtxt);
						for(var i=0;i<lineData.length;i++){
							var oneElement = lineData[i].split("$");
							var xValue = oneElement[0];
							var xText = oneElement[1];
							var checkboxtxt = "<input type='checkbox' name='deviceModelId' value='"+xValue+"' onclick='getSoftVersion()'>"+xText+"  ";
							$("div[@id='adaptModelType']").append(checkboxtxt);
						}
					}else{
						$("div[@id='adaptModelType']").append("�ó���û�ж�Ӧ�ͺţ�");
					}
				}else{
					$("div[@id='adaptModelType']").append("�ó���û�ж�Ӧ�ͺţ�");
				}
			});
		}else{
			$("div[@id='adaptModelType']").html("��ѡ����");
		}
	}
   //ȫѡ
	function selectAllSoft(checkOBJ){
		if(checkOBJ.checked){
			 $("input[@name='deviceTypeId']").attr("checked",true); 
		}
		else{
			$("input[@name='deviceTypeId']").attr("checked",false);
		}
	}
   /**
	**�����豸�ͺŻ�ȡ�����汾�����Ը�ѡ�����ʽ���ֳ���
	**/ 
  function getSoftVersion(){
		//var deviceModelId = $("input[@name='deviceModelId']").val();
		var deviceModelIds ="";
		$("input[@name='deviceModelId'][@checked]").each(function(){ 
	    	deviceModelIds += $(this).val()+",";
	    });
		if(deviceModelIds!=""){
			deviceModelIds=deviceModelIds.substring(0,deviceModelIds.length-1);
		}
		var url = "<s:url value='/gtms/stb/config/batchCustomNodeConfig!getSoftVersion.action'/>";
		if(deviceModelIds!=""){
			$("div[@id='softVersion']").html("");
			$.post(url,{
				deviceModelIds : deviceModelIds
			},function(ajax){
				if(ajax!=""){
					var lineData = ajax.split("#");
					if(typeof(lineData)&&typeof(lineData.length)){
						var checkboxSoft ="<input type='checkbox' name='allSoftVersion' onclick='javascript:selectAllSoft(this)'>ȫѡ &nbsp;&nbsp;&nbsp;";
						$("div[@id='softVersion']").append(checkboxSoft);
						for(var i=0;i<lineData.length;i++){
							var oneElement = lineData[i].split("$");
							var xValue = oneElement[0];
							var xText = oneElement[1];
							checkboxSoft = "<input type='checkbox' name='deviceTypeId' value='"+xValue+"'>"+xText+"  ";
							$("div[@id='softVersion']").append(checkboxSoft);
						}
					}else{
						$("div[@id='softVersion']").append("���ͺ�û�ж�Ӧ�����汾��");
					}
				}else{
					$("div[@id='softVersion']").append("���ͺ�û�ж�Ӧ�����汾��");
				}
			});
		}else{
			$("div[@id='softVersion']").html("��ѡ���ͺ�");
		}
	}
	//ȷ���Ƿ�Ҫ�ֶ����ӻ�����������
   function clkipCheck()
	{
		var ipch = document.getElementsByName("isOpenIP");
		if(ipch[0].checked)
		{
			$("tr[id*=setIPduan]").attr("style","display: none");
			$("tr[id=importIPAdd]").attr("style","display: none");
			$("button[name=addIP]").attr("style","display: none");  // �ֹ�����IP��ַ�ΰ�ť
			$("button[name=importIP]").attr("style","display: none"); // ��������IP��ַ�ΰ�ť				
		}
		if(ipch[1].checked)
		{
			$("tr[id*=setIPduan]").attr("style","display: inline");
			$("button[name=addIP]").attr("style","display: inline");  // �ֹ�����IP��ַ�ΰ�ť
			$("button[name=importIP]").attr("style","display: inline"); // ��������IP��ַ�ΰ�ť
		}
	}
	
// �ֶ�����IP��ַ��
	function addIPAddress()
	{
		$("tr[id=importIPAdd]").attr("style","display: none"); // �����ļ��ϴ�
		$("input[@name='checkIPButtonValue']").attr("value","byHandIP"); // �����ж� �ύ����ʱʹ�����ַ�ʽ�ύ
		
		var innerH = "<tr id='setIPduan" +ipDuanCount +"' style='display: inline'><td class='title_2' align='center' width='15%'>��ʼIP</td><td width='35%' ><input type='text' name='start_ip' maxlength='15'/></td><td class='title_2' align='center' width='15%'>��ֹIP</td><td width='35%'><input type='text' name='end_ip' maxlength='15'/></td></tr>";
		ipDuanCount++;
		$("#nodeAddIP").before(innerH);
	}
	// ��������IP��ַ��
	function batchAddIPAddress(){
		ipDuanCount = 1;
		$("input[@name='checkIPButtonValue']").attr("value","byBatchIP"); // �����ж� �ύ����ʱʹ�����ַ�ʽ�ύ
		$("tr[id*=setIPduan]").remove();
		$("tr[id=importIPAdd]").attr("style","display: inline");  // ��ʾ�ļ��ϴ�
	}
	//ȷ���Ƿ�Ҫ�ֶ����ӻ�����������MAC
	   function clkMACCheck()
		{
			var macch = document.getElementsByName("isOpenMAC");
			if(macch[0].checked)
			{
				$("tr[id*=setMACduan]").attr("style","display: none");
				$("tr[id=importMACAdd]").attr("style","display: none");
				$("button[name=addMAC]").attr("style","display: none");  // �ֹ�����IP��ַ�ΰ�ť
				$("button[name=importMAC]").attr("style","display: none"); // ��������IP��ַ�ΰ�ť				
			}
			if(macch[1].checked)
			{
				$("tr[id*=setMACduan]").attr("style","display: inline");
				$("button[name=addMAC]").attr("style","display: inline");  // �ֹ�����IP��ַ�ΰ�ť
				$("button[name=importMAC]").attr("style","display: inline"); // ��������IP��ַ�ΰ�ť
			}
		}
		
	// �ֶ�����MAC��ַ��
		function addMACAddress()
		{
			$("tr[id=importMACAdd]").attr("style","display: none"); // �����ļ��ϴ�
			$("input[@name='checkMACButtonValue']").attr("value","byHandMAC"); // �����ж� �ύ����ʱʹ�����ַ�ʽ�ύ
			
			var innerH = "<tr id='setMACduan" +macDuanCount +"' style='display: inline'><td class='title_2' align='center' width='15%'>��ʼMAC</td><td width='35%' ><input type='text' name='start_mac' maxlength='17'/></td><td class='title_2' align='center' width='15%'>��ֹMAC</td><td width='35%'><input type='text' name='end_mac' maxlength='17'/></td></tr>";
			macDuanCount++;
			$("#nodeAddMAC").before(innerH);
		}
		// ��������MAC��ַ��
		function batchAddMACAddress(){
			macDuanCount = 1;
			$("input[@name='checkMACButtonValue']").attr("value","byBatchMAC"); // �����ж� �ύ����ʱʹ�����ַ�ʽ�ύ
			$("tr[id*=setMACduan]").remove();
			$("tr[id=importMACAdd]").attr("style","display: inline");  // ��ʾ�ļ��ϴ�
		}
 //������ӵ�ʱ������һ��
   var flag = 0;
   function doAdd(){
   	flag++ ;
   	//����paramAddDiv0���Ԫ��
		$("#fatherDiv").append($("#paramAddDiv0").clone(true).attr("id","paramAddDiv"+flag));
		$("#paramAddDiv"+flag).each(function(){
			//����¡�����������ȫ���ÿ�
			$(this).find("input[type='text']").val("");
			$(this).find("select[name='paramType0']").val("");
			//���Ŀ�¡����div�ĸ���������id
			$(this).find("input[name='paramNodePath0']").attr("id","paramNodePath"+flag).attr("name","paramNodePath"+flag);
			$(this).find("input[name='paramValue0']").attr("id","paramValue"+flag).attr("name","paramValue"+flag);
			$(this).find("select[name='paramType0']").attr("id","paramType"+flag).attr("name","paramType"+flag);
		});
	}
	//��̬��ɾ��ɾ����һ��,����ֻɾ�����һ��
	function doDelete(){
		   if($("#fatherDiv div").size()>1){
		   		$("#fatherDiv div:last-child").remove();
		   }else{
		   		alert("�������һ�У��޷���ɾ��");
		   		return;
		   }
		    flag--;
	}
	//IP ��ַ��֤
	function checkIP(ip)
	{
		var exp=/^((\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.){3}(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/;
		var rev = false;
		if(null == ip.match(exp))
		{
			alert("IP��ַ��ʽ����ȷ");
		}
		else
		{
			rev = true;
		}
		return rev;
	}
	//ת��IP��ַΪ����
	function convertIP(ip)
	{
		var ipArray = ip.split(".");
		var newIP = "";
		for(var i=0;i<ipArray.length;i++)
		{
			if(ipArray[i].length==1)
			{
				ipArray[i] = "00"+ipArray[i];
			}
			else if(ipArray[i].length==2)
			{
				ipArray[i] = "0"+ipArray[i];
			}
			newIP += ipArray[i] + ".";
		}
		return newIP.substring(0,newIP.length-1);
	}
	//У��MAC��ַ
	function checkMAC(mac){
	  	var macs = new Array();
	  	macs = mac.split(":");
	  	if(macs.length != 6){
				alert("�����mac��ַ��ʽ����ȷ������xx:xx:xx:xx:xx:xx����ʽ���루xxΪ16�������֣�!");
				return false;
			}
	   for (var s=0; s<6; s++) {
	   	var num = macs[s];
	   	if(num.length>2){
	   		alert("�����mac��ַ��ʽ����ȷ������xx:xx:xx:xx:xx:xx����ʽ���루xxΪ16�������֣�!");
	   		 return false;  
	   	}
	   	var temp = parseInt(macs[s],16);
	   	if(isNaN(temp)){
	   		alert("�����mac��ַ��ʽ����ȷ������xx:xx:xx:xx:xx:xx����ʽ���루xxΪ16�������֣�!"); 
	   	  return false; 
	   	}
	   	if(temp < 0 || temp > 255){
	   		alert("�����mac��ַ��ʽ����ȷ������xx:xx:xx:xx:xx:xx����ʽ���루xxΪ16�������֣�!"); 
	   		return false;
	   	}
	  }
	  return true;
	}
</script>