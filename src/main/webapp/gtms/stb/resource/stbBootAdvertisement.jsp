<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage"%>
<%@taglib prefix="ms" uri="/ailk-itms-web-tags"%>
<%@page import="com.linkage.litms.LipossGlobals"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=GBK">
		<link href="<s:url value="/css2/global.css"/>" rel="stylesheet"
			type="text/css">
		<link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet"
			type="text/css">
		<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="../../../Js/My97DatePicker/WdatePicker.js"></script>		<lk:res />
<SCRIPT LANGUAGE="JavaScript">
//ϵͳ����
    //����
	function ExecMod(){
	
		//������
		var taskName =  $("#taskName").val();
		
		var cityNameJudge =  $("#cityNameJudge").val();
		if(""==trim(taskName)){
			alert("����������Ϊ�գ�");
			return false; 
		}
		//����
		var cityId = $("select[@name='cityId']").val();
		//�¼�����
		var cityIds = "";
		$("input[@name='cityIDS'][@checked]").each(function(){ 
			cityIds += $(this).val()+",";
	    });
		if(cityIds.length>0){
	    	$("input[@name='cityIds']").attr("value",cityIds.substring(0,cityIds.length-1));
	    }
		
	    var startPic = $("input[@id='startFile']").val();
	    var bootPic = $("input[@id='bootFile']").val();
	    var authPic = $("input[@id='authFile']").val();
	    if(checkChinese(startPic)){
	    	alert("����ͼƬ���Ʋ��ɺ�������");
	    	return false;
	    }
	    
	    if(checkChinese(bootPic)){
	    	alert("�����������Ʋ��ɺ�������");
	    	return false;
	    }
	    
	    if(checkChinese(authPic)){
	    	alert("��֤ͼƬ���Ʋ��ɺ�������");
	    	return false;
	    }
	    if(cityNameJudge=="cq_dx"){
	    if(-1!=startPic.lastIndexOf(".jpg")||-1!=startPic.lastIndexOf(".jpeg")||-1!=startPic.lastIndexOf(".png")||-1!=startPic.lastIndexOf(".JPG")||-1!=startPic.lastIndexOf(".JPEG")||-1!=startPic.lastIndexOf(".PNG")){
			if(-1!=bootPic.lastIndexOf(".zip")){
				if(-1!=authPic.lastIndexOf(".jpg")||-1!=authPic.lastIndexOf(".jpeg")||-1!=authPic.lastIndexOf(".png")||-1!=authPic.lastIndexOf(".JPG")||-1!=authPic.lastIndexOf(".JPEG")||-1!=authPic.lastIndexOf(".PNG")){
				}
				else{
					alert("��֤ͼƬ��ʽ����ȷ");
					return false;
				}
			}
			else{
				alert("����������ʽ����ȷ");
				return false;
			}
		}
	    else{
	    	alert("����ͼƬ��ʽ����ȷ");
	    	return false;
	    }
	    }
	    var groupidS ="";
	    $("input[@name='groupIDS'][@checked]").each(function(){ 
	    	groupidS += $(this).val()+",";
	    });
	 /*  //����chenxj
		var vendorId = $("select[@name='vendorId']").val(); */
		//$("input[@name='vendor_Id']").attr("value",vendorId);
		var vendorId = $("select[@name='vendorId']").val();
		 if((""==cityId||"-1"==cityId)&&(groupidS=="")&&(vendorId==""||vendorId=="-1"))
			 {
			 alert("���ء����顢���̲���ͬʱΪ��");
			 return false;
			 }
		/* if(""==cityId||"-1"==cityId){
			alert("��ѡ������");
			return false;
		}
		 if(groupidS==""){
				alert("��ѡ�����!");
				return false;
			} */
		    if(groupidS.length>0){
		    	$("input[@name='groupids']").attr("value",groupidS.substring(0,groupidS.length-1));
		    }
		/* if(vendorId==""||vendorId=="-1")
			{
			alert("��ѡ����!");
			return false;
			} */
			if(vendorId!=""&&vendorId!="-1"){
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
			alert("��ѡ������汾!");
			return false;
		}
	    if(deviceTypeIds.length>0){
	    	$("input[@name='deviceTypeIds']").attr("value",deviceTypeIds.substring(0,deviceTypeIds.length-1));
	    }
			}
	    //chenxj
		var vendorId = $("input[@name='vendorId']").val();
		var deviceModelIds = $("input[@name='deviceModelIds']").val();
		var deviceTypeIds = $("input[@name='deviceTypeIds']").val();
		
	    /* if(vendorId == "" || deviceModelIds == "" || deviceTypeIds == ""){
			alert("������ð汾");
			return false;
		} */
	    var Invalidtime= $("input[@name='Invalidtime']").val();
	    if(Invalidtime=="")
	    	{
	    	alert("��ѡ��ʧЧʱ��!");
	    	return false;
	    	}
	    //�ֶ�����ҵ���ʺ�
	    //custSG
	    //�����ϴ�ͼƬ
	    var isPicPass  =  checkPic();
	    if(isPicPass){
	    	$("#doButton").attr("disabled",true);
		    
			$("form[@name='batchexform']").attr("action","stbBootAdvertisement!importConfig.action");
			$("form[@name='batchexform']").submit();
	    }else{
	    	return false;
	    }
	    
	}
	
	//����У��
	function checkChinese(str){
		var regTest = /^[\u4e00-\u9fa5]+$/;
		var flag = false;
		if(str != null && $.trim(str) != ""){
			if(str.indexOf("\\") != -1){
				var strArr = str.split("\\");
				str = strArr[strArr.length-1];
			}
			//alert(str);
	    	for(var i=0 ; i<str.length ; i++){
				var word = str.substring(i,i+1);
				if(regTest.test(word)){
					flag = true ; 
					break;
				}
			}
	    }
		return flag;
	}
	
    //�ֶ�����ɣе�ַ��
	function inputIPbyHand(){
		var isCheckPass= true ;
		var isWrite = false;
	    var ipcheckAdd = document.getElementsByName("isOpenIP");
		var ipcheck = "0";
		var ipSG = "";
		if(ipcheckAdd[1].checked)
		{
			alert("checked");
			ipcheck = "1";
			var IPduanList = $("tr[id*=setIPduan]");
			IPduanList.each(
					function(i)
					{
						isWrite = true;
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
		}else{
			isWrite = true;
			return true;
		}
		if(isCheckPass && isWrite)// 
		{
			ipSG = ipSG.substring(0,ipSG.length-1);
			$("input[@name='ipSG']").attr("value",ipSG);
			$("input[@name='ipCheck']").attr("value",ipcheck);
			return true;
		}
		else if(!isWrite){
			alert("���ֹ�����IP��ַ�λ�����������IP��ַ��!");
			return false;
		}
		else{
			return false;
		}
	}
	//�ֶ�����MAC
	function inputMACbyHand(){
		 var isCheckMACPass= true ;
		 var isWrite = false;
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
							isWrite = true;
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
			}else{
				isWrite = true;
				return true;
			}
			if(isCheckMACPass && isWrite)
			{
				macSG = macSG.substring(0,macSG.length-1);
				$("input[@name='macSG']").attr("value",macSG);
				$("input[@name='macCheck']").attr("value",maccheck);
				return true;
			}else if(!isWrite){
				alert("���ֹ�����MAC��ַ�λ�����������MAC��ַ��!");
				return false;
			}else{
				return false;
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
			return true;
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
			return true;
		}else
		{
			alert("��֧�ֺ�׺Ϊxls���ļ�");
			return false;
		}
    }
    //�����ʺ���Ϣ
	function inputCustomerByHand(){
		var isCheckCustomerPass= true ;
	    var customerCheckAdd = document.getElementsByName("isAddCustomer");
		var custcheck = "0";
		var custSG = "";
		if(customerCheckAdd[1].checked)
		{
			custcheck = "1";
			var MACduanList = $("tr[id*=setCustomerInfo]");
			MACduanList.each(
					function(j)
					{	
						var custInfo = $("input[name=customerInfomation]",this.innerHTML).val();
						if(trim(custInfo)=='' || custInfo=='undefined')
						{
							isCheckCustomerPass = false;
							return ;
						}
						else
						{
							custSG += custInfo + ";";
						}
					}
			);
		}
		if(isCheckCustomerPass)
		{
			if(""==custSG){
				
			}else{
				custSG = custSG.substring(0,custSG.length-1);
			}
			$("input[@name='custSG']").attr("value",custSG);
			$("input[@name='custCheck']").attr("value",custcheck);
			return true;
		}else{
			return false;
		}
	}
    //�����ʺ���Ϣ
    function importCustomer(){
    	var myFileCustomer = $("input[@name='uploadCustomer']").val();
		if(""==myFileCustomer){	
			alert("��ѡ���ļ�!");
			return false;
		}
		var filetCustomer = myFileCustomer.split(".");
		if(filetCustomer.length<2)
		{
			alert("��ѡ���ļ�!");
			return false;
		}
		if("xls" == filetCustomer[filetCustomer.length-1])
		{
			var file2 = myFileCustomer.split("\\");
			var fileName2 = file2[file2.length-1];
			$("input[@name='uploadFileName4Customer']").attr("value",fileName2);
			return true;
		}else
		{
			alert("��֧�ֺ�׺Ϊxls���ļ�");
			return false;
		}
    }
    
    /**
    *����ͼƬ
    **/
    
    
    function checkPic(){
    	var bootPic = $("input[@name='bootFile']").val();
    	if(""==bootPic){
    		
    	}else{
    		var bootfilet = bootPic.split(".");
        	if(bootfilet.length<2){
        		alert("����ͼƬ�����ϴ�!");
        		return false;
        	}
            
       		var bootfile = bootPic.split("\\");
       		var bootfileName = bootfile[bootfile.length-1];
       		$("input[@name='bootFileName']").attr("value",bootfileName);
    	}
    	
   		var startPic = $("input[@name='startFile']").val();
    	if(""==startPic){
    		
    	}else{
    		var startfilet = startPic.split(".");
        	if(startfilet.length<2){
        		alert("�������������ϴ�!");
        		return false;
        	}
            
        	var startfile = startPic.split("\\");
       		var startfileName = startfile[startfile.length-1];
       		$("input[@name='startFileName']").attr("value",startfileName);
    	}
    	
   		var authPic = $("input[@name='authFile']").val();
    	if(""==authPic){
    		
    	}else{
    		var authfilet = authPic.split(".");
        	if(authfilet.length<2){
        		alert("��֤ͼƬ�����ϴ�!");
        		return false;
        	}
       		var authfile = authPic.split("\\");
       		var authfileName = authfile[authfile.length-1];
       		$("input[@name='authFileName']").attr("value",authfileName);
    	}
    	
    	//�µ��޸ģ����߲���ͬʱΪ��
    	if(""==bootPic&&""==startPic&&""==authPic){
    		alert("��������������֤����ͼƬ����ͬʱΪ�գ�");
    		return false;
    	}
    	return true;
    }
    
	function softdivcl()
	{
		$("div[@id='div_css']").hide();
	}
	// ����excelģ��
	function toExportIP()
	{
		$("form[@name='batchexform']").attr("action","openDeviceShowPic!downloadTemplate.action");
		$("form[@name='batchexform']").submit();
	}
	// ����excelģ��
	function toExportMAC()
	{
		$("form[@name='batchexform']").attr("action","openDeviceShowPic!downloadTemplate.action");
		$("form[@name='batchexform']").submit();
	}
	function toExportCust()
	{
		$("form[@name='batchexform']").attr("action","openDeviceShowPic!downloadTemplateCust.action");
		$("form[@name='batchexform']").submit();
	}
	//ȥ���ո�
	function trim(str){
      return str.replace(/(^\s*)|(\s*$)/g,"");
	}
	
	function viewAddEdition(){ 
		//alert(vendorList);
		var page = "<s:url value='/gtms/stb/resource/openDeviceShowPic!initForEdition.action'/>";
		window.open(page,"","left=20,top=20,width=800,height=600,resizable=no,scrollbars=yes");
    }
</SCRIPT>

	</head>
	<body>
		<TABLE width="98%" align="center" class="querytable">
			<TR>
				<TD>
					<IMG height=20 src="<s:url value='/images/bite_2.gif'/>" width=24>
					����ǰ��λ�ã����������������
				</TD>
			</TR>
		</TABLE>
		<br>
		<s:form action="openDeviceShowPic!importConfig.action" method="post" enctype="multipart/form-data" name="batchexform" onsubmit="">
			<input type="hidden" name="cityIds" value=""/>
			<!-- 
			<input type="hidden" name="deviceModelIds" value=""/>
			<input type="hidden" name="deviceTypeIds"   value="">
			 -->
			 <%if(!"cq_dx".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))){ %>
			 <input type="hidden" name="cityNameJudge"   value="cq_dx">
			 <%}else {%>
			 <input type="hidden" name="cityNameJudge"   value="">
			 <%} %>
			 <input type="hidden" name="groupids"   value="">
			<input type="hidden" name="macSG" value="">
			<input type="hidden" name="ipSG"   value="">
			<input type="hidden" name="custSG"   value="">
			<input type="hidden" name="ipCheck"  value="">
			<input type="hidden" name="custCheck"  value="">
			<input type="hidden" name="macCheck"   value="">
			<input type="hidden" name="btnValue4MAC"   value="">
			<input type="hidden" name="btnValue4IP"   value="">
			<input type="hidden" name="btnValue4Customer"   value="">
			
			<input type="hidden" name="bootFileName"   value="">
			<input type="hidden" name="startFileName"   value="">
			<input type="hidden" name="authFileName"   value="">
			
			<!-- chenxj -->
			<!-- <input type="hidden" name="vendorId" value="" id="vendorId"/> -->
			<input type="hidden" name="deviceModelIds" value="" id="deviceModelIds"/>
			<input type="hidden" name="deviceTypeIds"  value="" id="deviceTypeIds"/>
			
			
			<table class="querytable" width="98%" align="center">
				<tr>
					<td colspan="4" class="title_1">
						���������������
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
							value="cityId" cssClass="bk" theme="simple" onchange="getNextCity();"></s:select>
					</TD>
			    </TR>
			    <TR>
					<TD class="title_2" align="center" width="15%">
						���ط�Χ����
					</TD>
					<TD width="85%" colspan="3">
						<div id="adaptNextCity">
							 ��ѡ������
						</div>
					</TD>
				</TR>
				<ms:inArea areaCode="jl_dx" notInMode="true">
				<TR>
					<TD class="title_2" align="center" width="15%">
						����
					</TD>
					<TD width="85%" colspan="3">
						<div id="groupid">
							 
						</div>
					</TD>
				</TR>
				</ms:inArea>
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
						Ҫ���õİ汾
					</TD>
					<TD width="85%" colspan="3">
						<div id="softVersion">
							  ��ѡ���ͺ�
						</div>
					</TD>
				</TR>
				 <%if("cq_dx".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))
						 ||"sx_lt".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))){ %>
				<TR id="startPic">
					<TD width="15%"  class="title_2">����ͼƬ</TD>
					<TD width="35%" colspan="3">
						<s:file label="�ϴ�" theme="simple" size="50"  id="startFile" name="startFile" ></s:file>&nbsp;&nbsp;&nbsp;<font color="red">��ʽΪ(jpg/jpeg/png)</font>
					</TD>
				</TR>
				<TR id="bootPic">
					<TD width="15%"  class="title_2">��������</TD>
					<TD width="35%" colspan="3">
						<s:file label="�ϴ�" theme="simple" size="50" id="bootFile" name="bootFile" ></s:file>&nbsp;&nbsp;&nbsp;<font color="red">��ʽΪ(zip)</font>
					</TD>
				</TR>
				<TR id="aurthPic">
					<TD width="15%"  class="title_2">��֤ͼƬ</TD>
					<TD width="35%" colspan="3">
						<s:file label="�ϴ�" theme="simple" size="50" id="authFile" name="authFile" ></s:file>&nbsp;&nbsp;&nbsp;<font color="red">��ʽΪ(jpg/jpeg/png)</font>
					</TD>
				</TR>
				<%}else{ %>
				<TR id="startPic">
					<%if("jl_dx".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))){ %>
						<TD width="15%"  class="title_2">����ͼƬ</TD>
					<%}else{ %>
						<TD width="15%"  class="title_2">����ͼƬ</TD>
					<%} %>
					<TD width="35%" colspan="3">
						<s:file label="�ϴ�" theme="simple" size="50"  id="startFile" name="startFile" ></s:file>&nbsp;&nbsp;&nbsp; 
					</TD>
				</TR>
				<TR id="bootPic">
					<%if("jl_dx".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))){ %>
						<TD width="15%"  class="title_2">����ͼƬ</TD>
					<%}else{ %>
						<TD width="15%"  class="title_2">��������</TD>
					<%} %>
					<TD width="35%" colspan="3">
						<s:file label="�ϴ�" theme="simple" size="50" id="bootFile" name="bootFile" ></s:file>&nbsp;&nbsp;&nbsp; 
					</TD>
				</TR>
				<TR id="aurthPic">
					<TD width="15%"  class="title_2">��֤ͼƬ</TD>
					<TD width="35%" colspan="3">
						<s:file label="�ϴ�" theme="simple" size="50" id="authFile" name="authFile" ></s:file>&nbsp;&nbsp;&nbsp; 
					</TD>
				</TR>
				<%} %>
				<ms:inArea areaCode="jl_dx" notInMode="true">
				<TR>
					<TD class=column align="right">���ȼ�</TD>
					<TD colspan=3>
						<select name="priority" class="bk" >
							<option value="1">���ȼ�1</option>
							<option value="2">���ȼ�2</option>
							<option value="3">���ȼ�3</option>
						</select>
					</TD>
				</TR>
				</ms:inArea>
				<TR id="aurthPic">
					<TD width="15%"  class="title_2">����ʧЧʱ��</TD>
					<TD width="35%" colspan="3">
						<input type="text" name="Invalidtime" id="Invalidtime" readonly
								value="<s:property value='endtime'/>"> <img
								name="shortDateimg"
								onClick="WdatePicker({el:document.batchexform.Invalidtime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
								src="../../../images/dateButton.png" width="15" height="12"
								border="0" alt="ѡ��" />
					</TD>
				</TR>
				<!-- <TR id="aurthPic">
					<TD width="15%"  class="title_2">��������</TD>
					<TD width="35%" colspan="3">
					<input type="checkbox" value="1" name="isLocked"/>&nbsp;
					</TD>
				</TR> -->
				<TR>
					<td colspan="4" class="foot" align="right">
						<div class="right">
							<button id="doButton" onclick="ExecMod()">
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
   var customerCount = 1;
 //ȫѡ
	function selectAllModel(checkOBJ){
		if(checkOBJ.checked){
			 $("input[@name='deviceModelId']").attr("checked",true); 
		}
 		else{
 			$("input[@name='deviceModelId']").attr("checked",false);
 		}
	}
 
	function selectAllCity1(checkOBJ){
		if(checkOBJ.checked){
			 $("input[@name='cityIDS']").attr("checked",true); 
		}
 		else{
 			$("input[@name='cityIDS']").attr("checked",false);
 		}
	}
	
	function selectAllgroupid(checkOBJ){
		if(checkOBJ.checked){
			 $("input[@name='groupids']").attr("checked",true); 
		}
 		else{
 			$("input[@name='groupids']").attr("checked",false);
 		}
	}
	/**
	**���ݳ��̻�ȡ�豸�ͺţ����Ը�ѡ�����ʽ���ֳ���
	**/ 
   function getDeviceModel(){
		var vendorId = $("select[@name='vendorId']").val();
		var url = "<s:url value='/gtms/stb/resource/stbBootAdvertisement!getDeviceModel.action'/>";
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
	
   /**
	**�������ػ�ȡ�¼����أ����Ը�ѡ�����ʽ���ֳ���
	**/ 
  function getNextCity(){
		var cityId = $("select[@name='cityId']").val();
		var url = "<s:url value='/gtms/stb/resource/stbBootAdvertisement!getNextCity.action'/>";
		//if(cityId!="-1" && cityId != "00"){
		if(cityId!="-1"&&cityId!="00"){	
			$("div[@id='adaptNextCity']").html("");
			$.post(url,{
				cityId:cityId
			},function(ajax){
				if(ajax!=""){
					var lineData = ajax.split("#");
					if(typeof(lineData)&&typeof(lineData.length)){
						var checkboxtxt = "<input type='checkbox' name='allCity' onclick='javascript:selectAllCity1(this);'>ȫѡ &nbsp;&nbsp;&nbsp;";
						$("div[@id='adaptNextCity']").append(checkboxtxt);
						for(var i=0;i<lineData.length;i++){
							var oneElement = lineData[i].split("$");
							var xValue = oneElement[0];
							var xText = oneElement[1];
							
							if(cityId != "00"){
								var checkboxtxt = "&nbsp;&nbsp;&nbsp;<input type='checkbox' name='cityIDS' value='"+xValue+"'>"+xText+"  ";
								$("div[@id='adaptNextCity']").append(checkboxtxt);
							}else if(cityId == "00"){
								if(xValue.length<=3){
									var checkboxtxt = "&nbsp;&nbsp;&nbsp;<input type='checkbox' name='cityIDS' value='"+xValue+"'>"+xText+"  ";
									$("div[@id='adaptNextCity']").append(checkboxtxt);
								}
							}
						}
					}else{
						$("div[@id='adaptNextCity']").append("������û���¼����أ�");
					}
				}else{
					$("div[@id='adaptNextCity']").append("������û���¼����أ�");
				}
			});
		}else if(cityId == "00"){
			$("div[@id='adaptNextCity']").html("");
		}else{
			$("div[@id='adaptNextCity']").html("��ѡ������");
		}
	}
		//��ʼ����
		$(function() {
			var url = "<s:url value='/gtms/stb/resource/stbBootAdvertisement!getGroupId.action'/>";
				$("div[@id='groupid']").html("");
				$.post(url,{
				},function(ajax){
					if(ajax!=""){
						var lineData = ajax.split("#");
						if(typeof(lineData)&&typeof(lineData.length)){
							//var checkboxtxt = "<input type='checkbox' name='groupid' onclick='javascript:selectAllgroupid(this);'>ȫѡ &nbsp;&nbsp;&nbsp;";
							//$("div[@id='groupid']").append(checkboxtxt);
							for(var i=0;i<lineData.length;i++){
								var oneElement = lineData[i].split("$");
								var xValue = oneElement[0];
								var xText = oneElement[1];
								var checkboxtxt = "&nbsp;&nbsp;&nbsp;<input type='checkbox' name='groupIDS' value='"+xValue+"'>"+xText+"  ";
								$("div[@id='groupid']").append(checkboxtxt);
								}
							}
						}else{
							$("div[@id='groupid']").append("û�з��飡");
						}
				});
	});
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
	**�����豸�ͺŻ�ȡ����汾�����Ը�ѡ�����ʽ���ֳ���
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
		var url = "<s:url value='/gtms/stb/resource/stbBootAdvertisement!getSoftVersion.action'/>";
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
						$("div[@id='softVersion']").append("���ͺ�û�ж�Ӧ����汾��");
					}
				}else{
					$("div[@id='softVersion']").append("���ͺ�û�ж�Ӧ����汾��");
				}
			});
		}else{
			$("div[@id='softVersion']").html("��ѡ���ͺ�");
		}
	}
	//ȷ���Ƿ�Ҫ�ֶ���ӻ�����������
   function clkipCheck()
	{
		var ipch = document.getElementsByName("isOpenIP");
		if(ipch[0].checked)
		{
			$("tr[id*=setIPduan]").attr("style","display: none");
			$("tr[id=importIPAdd]").attr("style","display: none");
			$("button[name=addIP]").attr("style","display: none");  // �ֹ����IP��ַ�ΰ�ť
			$("button[name=importIP]").attr("style","display: none"); // ��������IP��ַ�ΰ�ť				
		}
		if(ipch[1].checked)
		{
			$("tr[id*=setIPduan]").attr("style","display: inline");
			$("button[name=addIP]").attr("style","display: inline");  // �ֹ����IP��ַ�ΰ�ť
			$("button[name=importIP]").attr("style","display: inline"); // ��������IP��ַ�ΰ�ť
		}
	}
	
// �ֶ����IP��ַ��
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
	//ȷ���Ƿ�Ҫ�ֶ���ӻ�����������MAC
	   function clkMACCheck()
		{
			var macch = document.getElementsByName("isOpenMAC");
			if(macch[0].checked)
			{
				$("tr[id*=setMACduan]").attr("style","display: none");
				$("tr[id=importMACAdd]").attr("style","display: none");
				$("button[name=addMAC]").attr("style","display: none");  // �ֹ����IP��ַ�ΰ�ť
				$("button[name=importMAC]").attr("style","display: none"); // ��������IP��ַ�ΰ�ť				
			}
			if(macch[1].checked)
			{
				$("tr[id*=setMACduan]").attr("style","display: inline");
				$("button[name=addMAC]").attr("style","display: inline");  // �ֹ����IP��ַ�ΰ�ť
				$("button[name=importMAC]").attr("style","display: inline"); // ��������IP��ַ�ΰ�ť
			}
		}
		
	// �ֶ����MAC��ַ��
		function addMACAddress()
		{
			$("tr[id=importMACAdd]").attr("style","display: none"); // �����ļ��ϴ�
			$("input[@name='checkMACButtonValue']").attr("value","byHandMAC"); // �����ж� �ύ����ʱʹ�����ַ�ʽ�ύ
			
			var innerH = "<tr id='setMACduan" +macDuanCount +"' style='display: inline'><td class='title_2' align='center' width='15%'>��ʼMAC</td><td width='35%' ><input type='text' name='start_mac' maxlength='17'/></td><td class='title_2' align='center' width='15%'>��ֹMAC</td><td width='35%'><input type='text' name='end_mac' maxlength='17'/></td></tr>";
			macDuanCount++;
			$("#addCustomer").before(innerH);
		}
		// ��������MAC��ַ��
		function batchAddMACAddress(){
			macDuanCount = 1;
			$("input[@name='checkMACButtonValue']").attr("value","byBatchMAC"); // �����ж� �ύ����ʱʹ�����ַ�ʽ�ύ
			$("tr[id*=setMACduan]").remove();
			$("tr[id=importMACAdd]").attr("style","display: inline");  // ��ʾ�ļ��ϴ�
		}
		//�ֹ����
		function addCustomer(){
			if(customerCount>3){
				alert("�ֹ�����ʺŹ��࣬�����ļ�������ʽ���");
				return ;
			}
			$("tr[id=importCustomer]").attr("style","display: none"); // �����ļ��ϴ�
			$("input[@name='checkCustomerButtonValue']").attr("value","byHandCum"); // �����ж� �ύ����ʱʹ�����ַ�ʽ�ύ
			
			var innerH = "<tr id='setCustomerInfo" +customerCount +"' style='display: inline'><td  class='title_2' align='center' width='15%'>ҵ���ʺ���Ϣ</td><td  colspan=3 width='85%' ><input type='text' name='customerInfomation' maxlength='17'/></td></tr>";
			customerCount++;
			$("#bootPic").before(innerH);
		}
		//��������ҵ���ʺ�
		function batchAddCustomer(){
			customerCount = 1;
			$("input[@name='checkCustomerButtonValue']").attr("value","byBatchCust"); // �����ж� �ύ����ʱʹ�����ַ�ʽ�ύ
			$("tr[id*=setCustomerInfo]").remove();
			$("tr[id=importCustomer]").attr("style","display: inline");  // ��ʾ�ļ��ϴ�
		}
		//�Ƿ����ʺ�
		function clkCustomerCheck()
		{
			var cumInfo = document.getElementsByName("isAddCustomer");
			if(cumInfo[0].checked)
			{
				$("tr[id*=setCustomerInfo]").attr("style","display: none");
				$("tr[id=importCustomer]").attr("style","display: none");
				$("button[name=addCustomerInfo]").attr("style","display: none");  
				$("button[name=importCustomerInfo]").attr("style","display: none");
			}
			if(cumInfo[1].checked)
			{
				$("tr[id*=setCustomerInfo]").attr("style","display: inline");
				//$("button[name=addCustomerInfo]").attr("style","display: inline");  
				$("button[name=importCustomerInfo]").attr("style","display: inline"); 
			}
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