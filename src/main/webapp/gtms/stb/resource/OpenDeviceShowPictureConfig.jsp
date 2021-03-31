<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage"%>
<%@taglib prefix="ms" uri="/ailk-itms-web-tags"%>
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
//系统类型
    //定制
	function ExecMod(){
	
		var btnValue4IP = $("input[@name='checkIPButtonValue']").val(); // 用于判断使用哪种提交方式
		var btnValue4MAC = $("input[@name='checkMACButtonValue']").val(); // 用于判断使用哪种提交方式
		var btnValue4Cust =  $("input[@name='checkCustomerButtonValue']").val(); // 用于判断使用哪种提交方式
		//根据提交的类型来给IP或MAC地址段赋值
		if("byBatchIP"==btnValue4IP){
			$("input[@name='btnValue4IP']").attr("value",btnValue4IP);
			$("input[@name='ipCheck']").attr("value","1");
			var isImportIp = importIP();
			if(!isImportIp){
				return false;
			}
		}else{
			var isPassIp = inputIPbyHand();
			if(!isPassIp){
				return false;
			}
		}
		if("byBatchMAC"==btnValue4MAC){
			$("input[@name='btnValue4MAC']").attr("value",btnValue4MAC);
			$("input[@name='macCheck']").attr("value","1");
			var isImportMac = importMAC();
			if(!isImportMac){
				return false;
			}
		}else{
			var isPassMac = inputMACbyHand();
			if(!isPassMac){
				return false;
			}
		}
		
		if("byBatchCust"==btnValue4Cust){
			$("input[@name='btnValue4Customer']").attr("value",btnValue4Cust);
			$("input[@name='custCheck']").attr("value","1");
			var isImportCustomer = importCustomer();
			if(!isImportCustomer){
				return false;
			}
		}else{
			var isPassCustomer = inputCustomerByHand();
			if(!isPassCustomer){
				return false;
			}
		}
		//策略名
		var taskName =  $("#taskName").val();
		//属地
		var cityId = $("select[@name='cityId']").val();
		if(""==trim(taskName)){
			alert("策略名不能为空！");
			return false; 
		} 
		if(""==cityId||"-1"==cityId){
			alert("请选择属地");
			return false;
		}
		//下级属地
		var cityIds = "";
		$("input[@name='cityIDS'][@checked]").each(function(){ 
			cityIds += $(this).val()+",";
	    });
		if(cityIds.length>0){
	    	$("input[@name='cityIds']").attr("value",cityIds.substring(0,cityIds.length-1));
	    }
		
		
		//厂商
		//chenxj
		var vendorId = $("input[@name='vendorId']").val();
		var deviceModelIds = $("input[@name='deviceModelIds']").val();
		var deviceTypeIds = $("input[@name='deviceTypeIds']").val();
		
	    if(vendorId == "" || deviceModelIds == "" || deviceTypeIds == ""){
			alert("请添加配置版本");
			return false;
		}
		
	    var startPic = $("input[@id='startFile']").val();
	    var bootPic = $("input[@id='bootFile']").val();
	    var authPic = $("input[@id='authFile']").val();
	    
	    if(checkChinese(startPic)){
	    	alert("启动图片名称不可含有中文");
	    	return false;
	    }
	    
	    if(checkChinese(bootPic)){
	    	alert("开机图片名称不可含有中文");
	    	return false;
	    }
	    
	    if(checkChinese(authPic)){
	    	alert("认证图片名称不可含有中文");
	    	return false;
	    }
	    
	    
	    
	  //厂商
		/* chenxj
		var vendorId = $("select[@name='vendorId']").val();
		//$("input[@name='vendor_Id']").attr("value",vendorId);
		//型号
		var deviceModelIds = "";
		$("input[@name='deviceModelId'][@checked]").each(function(){ 
	    	deviceModelIds += $(this).val()+",";
	    });
	    if(deviceModelIds==""){
			alert("请选择适用型号!");
			return false;
		}
	    if(deviceModelIds.length>0){
	    	$("input[@name='deviceModelIds']").attr("value",deviceModelIds.substring(0,deviceModelIds.length-1));
	    }
	    
		//版本
		var deviceTypeIds ="";
		
	    $("input[@name='deviceTypeId'][@checked]").each(function(){ 
	    	deviceTypeIds += $(this).val()+",";
	    });
	    if(deviceTypeIds==""){
			alert("请选择软件版本!");
			return false;
		}
	    if(deviceTypeIds.length>0){
	    	$("input[@name='deviceTypeIds']").attr("value",deviceTypeIds.substring(0,deviceTypeIds.length-1));
	    }
	    
	    //chenxj
		var vendorId = $("input[@name='vendorId']").val();
		var deviceModelIds = $("input[@name='deviceModelIds']").val();
		var deviceTypeIds = $("input[@name='deviceTypeIds']").val();
		
	    if(vendorId == "" || deviceModelIds == "" || deviceTypeIds == ""){
			alert("添加配置版本");
			return false;
		}
	    */
	    
	    //手动输入业务帐号
	    //custSG
	    //检验上传图片
	    var isPicPass  =  checkPic();
	    if(isPicPass){
	    	$("#doButton").attr("disabled",true);
		    
			$("form[@name='batchexform']").attr("action","openDeviceShowPic!importConfig.action");
			$("form[@name='batchexform']").submit();
	    }else{
	    	return false;
	    }
	    
	}
	
	//中文校验
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
	
    //手动导入ＩＰ地址段
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
								alert("起始IP不能大于终止IP");
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
			alert("请手工输入IP地址段或者批量导入IP地址段!");
			return false;
		}
		else{
			return false;
		}
	}
	//手动导入MAC
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
				alert("请手工输入MAC地址段或者批量导入MAC地址段!");
				return false;
			}else{
				return false;
			}
	}
    //导入ＩＰ地址段
    function importIP(){
    	var myFile = $("input[@name='uploadIP']").val();
		if(""==myFile){	
			alert("请选择文件!");
			return false;
		}
		var filet = myFile.split(".");
		if(filet.length<2)
		{
			alert("请选择文件!");
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
			alert("仅支持后缀为xls的文件");
			return false;
		}
		
    }
    //校验导入MAC地址文件
    function importMAC(){
    	var myFileMAC = $("input[@name='uploadMAC']").val();
		if(""==myFileMAC){	
			alert("请选择文件!");
			return false;
		}
		var filetMAC = myFileMAC.split(".");
		if(filetMAC.length<2)
		{
			alert("请选择文件!");
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
			alert("仅支持后缀为xls的文件");
			return false;
		}
    }
    //输入帐号信息
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
    //导入帐号信息
    function importCustomer(){
    	var myFileCustomer = $("input[@name='uploadCustomer']").val();
		if(""==myFileCustomer){	
			alert("请选择文件!");
			return false;
		}
		var filetCustomer = myFileCustomer.split(".");
		if(filetCustomer.length<2)
		{
			alert("请选择文件!");
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
			alert("仅支持后缀为xls的文件");
			return false;
		}
    }
    
    /**
    *传入图片
    **/
    
    
    function checkPic(){
    	var bootPic = $("input[@name='bootFile']").val();
    	if(""==bootPic){
    		
    	}else{
    		var bootfilet = bootPic.split(".");
        	if(bootfilet.length<2){
        		alert("开机图片必须上传!");
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
        		alert("启动图片必须上传!");
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
        		alert("认证图片必须上传!");
        		return false;
        	}
       		var authfile = authPic.split("\\");
       		var authfileName = authfile[authfile.length-1];
       		$("input[@name='authFileName']").attr("value",authfileName);
    	}
    	
    	//新的修改，三者不能同时为空
    	if(""==bootPic&&""==startPic&&""==authPic){
    		alert("启动，开机，认证三张图片不能同时为空！");
    		return false;
    	}
    	return true;
    }
    
	function softdivcl()
	{
		$("div[@id='div_css']").hide();
	}
	// 下载excel模板
	function toExportIP()
	{
		$("form[@name='batchexform']").attr("action","openDeviceShowPic!downloadTemplate.action");
		$("form[@name='batchexform']").submit();
	}
	// 下载excel模板
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
	//去掉空格
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
					您当前的位置：机顶盒开机画面定制
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
			<input type="hidden" name="vendorId" value="" id="vendorId"/>
			<input type="hidden" name="deviceModelIds" value="" id="deviceModelIds"/>
			<input type="hidden" name="deviceTypeIds"  value="" id="deviceTypeIds"/>
			
			
			<table class="querytable" width="98%" align="center">
				<tr>
					<td colspan="4" class="title_1">
						机顶盒开机画面定制
					</td>
				</tr>
				<TR>
					<TD class="title_2" align="center" width="15%">任务名称</TD>
					<TD colspan="3"> <input type="text" id="taskName" name="taskName" width="500"> </TD>
				</TR>
				<TR>
					<TD class="title_2" align="center" width="15%">
						属地
					</TD>
				<TD width="85%" colspan="3">
						<s:select list="cityList" name="cityId" headerKey="-1"
							headerValue="请选择属地" listKey="city_id" listValue="city_name"
							value="cityId" cssClass="bk" theme="simple" onchange="getNextCity();"></s:select>
					</TD>
			    </TR>
			    <TR>
					<TD class="title_2" align="center" width="15%">
						属地范围限制
					</TD>
					<TD width="85%" colspan="3">
						<div id="adaptNextCity">
							 请选择属地
						</div>
					</TD>
				</TR>
				<TR>
					<TD class="title_2" align="center" width="15%">
						添加配置版本
					</TD>
					<TD width="85%" colspan="3">
						<button type="button" onclick="javascript:viewAddEdition()">添加版本</button>
					</TD>
				</TR>
				<!-- 
				<TR>
					<TD class="title_2" align="center" width="15%">
						厂商
					</TD>
					<TD width="85%" colspan="3">
						<s:select list="vendorList" name="vendorId" headerKey="-1"
							headerValue="请选择厂商" listKey="vendor_id" listValue="vendor_add"
							value="vendorId" cssClass="bk" onchange="getDeviceModel();"
							theme="simple">
					</s:select>
					</TD>
				</TR>
				 
				<TR>
					<TD class="title_2" align="center" width="15%">
						型号
					</TD>
					<TD width="85%" colspan="3">
						<div id="adaptModelType">
							 请选择厂商
						</div>
					</TD>
				</TR>
				<TR>
					<TD class="title_2" align="center" width="15%">
						要配置的版本
					</TD>
					<TD width="85%" colspan="3">
						<div id="softVersion">
							  请选择型号
						</div>
					</TD>
				</TR>
				-->
				<ms:inArea areaCode="sd_lt" notInMode="false">
					<div style="display:none;">
						<input type="radio" id="isOpenIP" name="isOpenIP" value="0" checked="checked" onclick="" />
						<input type="radio" id="isOpenIP" name="isOpenIP" value="1" onclick=""/>
						<input type="radio" id="isOpenMAC" name="isOpenMAC" value="0" checked="checked" onclick="" />
						<input type="radio" id="isOpenMAC" name="isOpenMAC" value="1" onclick=""/>
					</div>
				</ms:inArea>
				<ms:inArea areaCode="sd_lt" notInMode="true">
				<TR>
					<TD class="title_2" align="center" width="15%">
						启用IP地址段
					</TD>
					<TD width="85%" colspan="3">
						<input type="radio" id="isOpenIP" name="isOpenIP" value="0" checked="checked" onclick="clkipCheck();">否</input>&nbsp;&nbsp;
						<input type="radio" id="isOpenIP" name="isOpenIP" value="1" onclick="clkipCheck();">是</input>&nbsp;&nbsp;&nbsp;
						<button name="addIP" onclick="addIPAddress()"  style="display:none">手动添加IP地址段</button>&nbsp;&nbsp;
						<button name="importIP" onclick="batchAddIPAddress()"  style="display:none">批量导入IP地址段</button>&nbsp;&nbsp;
						<input type="hidden" id="checkIPButtonValue" name="checkIPButtonValue" value="" />
					</TD>
				</TR>
				<tr id="importIPAdd" style="display: none">
					<td class="title_2" align="center" width="15%">
						请选择文件
					</td>
					<td width="85%" colspan="3">
						<s:file label="上传" theme="simple" name="uploadIP"></s:file><font color="red">*</font>
						xls格式文档
						<a href="javascript:void(0);" onclick="toExportIP();"><font color="red">下载模板</font></a>
						<input type="hidden" name="uploadFileName4IP" value=""/>
					</td>
				</tr>
				<TR id="nodeAddIP">
					<TD class="title_2" align="center" width="15%">
						启用MAC地址段
					</TD>
					<TD width="85%" colspan="3">
						<input type="radio" id="isOpenMAC" name="isOpenMAC" value="0" checked="checked" onclick="clkMACCheck();">否</input>&nbsp;&nbsp;
						<input type="radio" id="isOpenMAC" name="isOpenMAC" value="1" onclick="clkMACCheck();">是</input>&nbsp;&nbsp;&nbsp;
						<button name="addMAC" onclick="addMACAddress()"  style="display:none">手动添加MAC地址段</button>&nbsp;&nbsp;
						<button name="importMAC" onclick="batchAddMACAddress()"  style="display:none">批量导入MAC地址段</button>&nbsp;&nbsp;
						<input type="hidden" id="checkMACButtonValue" name="checkMACButtonValue" value="" />
					</TD>
				</TR>
				<tr id="importMACAdd" style="display: none">
					<td class="title_2" align="center" width="15%">
						请选择文件
					</td>
					<td width="85%" colspan="3">
						<s:file label="上传" theme="simple" name="uploadMAC"></s:file><font color="red">*</font>
						xls格式文档
						<a href="javascript:void(0);" onclick="toExportMAC();"><font color="red">下载模板</font></a>
						<input type="hidden" name="uploadFileName4MAC" value=""/>
					</td>
				</tr>
				</ms:inArea>
				
				<TR id="addCustomer">
					<TD class="title_2" align="center" width="15%">
						业务帐号列表
					</TD>
					<TD width="85%" colspan="3">
						<input type="radio" id="isAddCustomer" name="isAddCustomer" value="0" checked="checked" onclick="clkCustomerCheck();">否</input>&nbsp;&nbsp;
						<input type="radio" id="isAddCustomer" name="isAddCustomer" value="1" onclick="clkCustomerCheck();">是</input>&nbsp;&nbsp;&nbsp;
						<button name="addCustomerInfo" onclick="addCustomer()"  style="display:none">手动添加帐号信息</button>&nbsp;&nbsp;
						<button name="importCustomerInfo" onclick="batchAddCustomer()"  style="display:none">批量导入帐号信息</button>&nbsp;&nbsp;
						<input type="hidden" id="checkCustomerButtonValue" name="checkCustomerButtonValue" value="" />
					</TD>
				</TR>
				<tr id="importCustomer" style="display: none">
					<td class="title_2" align="center" width="15%">
						请选择文件
					</td>
					<td width="85%" colspan="3">
						<s:file label="上传" theme="simple" name="uploadCustomer"></s:file><font color="red">*</font>
						xls格式文档
						<a href="javascript:void(0);" onclick="toExportCust();"><font color="red">下载模板</font></a>
						<input type="hidden" name="uploadFileName4Customer" value=""/>
					</td>
				</tr>
				<TR id="startPic">
					<TD width="15%"  class="title_2">启动图片</TD>
					<TD width="35%" colspan="3">
						<s:file label="上传" theme="simple" size="50"  id="startFile" name="startFile" ></s:file>&nbsp;&nbsp;&nbsp;
					</TD>
				</TR>
				<TR id="bootPic">
					<TD width="15%"  class="title_2">开机图片</TD>
					<TD width="35%" colspan="3">
						<s:file label="上传" theme="simple" size="50" id="bootFile" name="bootFile" ></s:file>&nbsp;&nbsp;&nbsp;
					</TD>
				</TR>
				
				<TR id="aurthPic">
					<TD width="15%"  class="title_2">认证图片</TD>
					<TD width="35%" colspan="3">
						<s:file label="上传" theme="simple" size="50" id="authFile" name="authFile" ></s:file>&nbsp;&nbsp;&nbsp;
					</TD>
				</TR>
				<TR id="aurthPic">
					<TD width="15%"  class="title_2">任务锁定</TD>
					<TD width="35%" colspan="3">
					<input type="checkbox" value="1" name="isLocked"/>&nbsp;
					</TD>
				</TR>
				<TR>
					<td colspan="4" class="foot" align="right">
						<div class="right">
							<button id="doButton" onclick="ExecMod()">
								定制
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
						关闭
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
 //全选
	function selectAllModel(checkOBJ){
		if(checkOBJ.checked){
			 $("input[@name='deviceModelId']").attr("checked",true); 
		}
 		else{
 			$("input[@name='deviceModelId']").attr("checked",false);
 		}
	}
 
	function selectAllCity(checkOBJ){
		if(checkOBJ.checked){
			 $("input[@name='deviceModelId']").attr("checked",true); 
		}
 		else{
 			$("input[@name='deviceModelId']").attr("checked",false);
 		}
	}
	
	/**
	**根据厂商获取设备型号，并以复选框的形式表现出来
	**/ 
   function getDeviceModel(){
		var vendorId = $("select[@name='vendorId']").val();
		var url = "<s:url value='/gtms/stb/resource/openDeviceShowPic!getDeviceModel.action'/>";
		if(vendorId!="-1"){
			$("div[@id='adaptModelType']").html("");
			$.post(url,{
				vendorId:vendorId
			},function(ajax){
				if(ajax!=""){
					var lineData = ajax.split("#");
					if(typeof(lineData)&&typeof(lineData.length)){
						var checkboxtxt = "<input type='checkbox' name='allDeviceModel' onclick='javascript:selectAllModel(this);getSoftVersion()'>全选 &nbsp;&nbsp;&nbsp;";
						$("div[@id='adaptModelType']").append(checkboxtxt);
						for(var i=0;i<lineData.length;i++){
							var oneElement = lineData[i].split("$");
							var xValue = oneElement[0];
							var xText = oneElement[1];
							var checkboxtxt = "<input type='checkbox' name='deviceModelId' value='"+xValue+"' onclick='getSoftVersion()'>"+xText+"  ";
							$("div[@id='adaptModelType']").append(checkboxtxt);
						}
					}else{
						$("div[@id='adaptModelType']").append("该厂商没有对应型号！");
					}
				}else{
					$("div[@id='adaptModelType']").append("该厂商没有对应型号！");
				}
			});
		}else{
			$("div[@id='adaptModelType']").html("请选择厂商");
		}
	}
	
   /**
	**根据属地获取下级属地，并以复选框的形式表现出来
	**/ 
  function getNextCity(){
		var cityId = $("select[@name='cityId']").val();
		var url = "<s:url value='/gtms/stb/resource/openDeviceShowPic!getNextCity.action'/>";
		//if(cityId!="-1" && cityId != "00"){
		if(cityId!="-1"){	
			$("div[@id='adaptNextCity']").html("");
			$.post(url,{
				cityId:cityId
			},function(ajax){
				if(ajax!=""){
					var lineData = ajax.split("#");
					if(typeof(lineData)&&typeof(lineData.length)){
						//var checkboxtxt = "<input type='checkbox' name='allCity' onclick='javascript:selectAllCity(this);'>全选 &nbsp;&nbsp;&nbsp;";
						//$("div[@id='adaptNextCity']").append(checkboxtxt);
						
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
						$("div[@id='adaptNextCity']").append("该属地没有下级属地！");
					}
				}else{
					$("div[@id='adaptNextCity']").append("该属地没有下级属地！");
				}
			});
		}else if(cityId == "00"){
			$("div[@id='adaptNextCity']").html("");
		}else{
			$("div[@id='adaptNextCity']").html("请选择属地");
		}
	}
   //全选
	function selectAllSoft(checkOBJ){
		if(checkOBJ.checked){
			 $("input[@name='deviceTypeId']").attr("checked",true); 
		}
		else{
			$("input[@name='deviceTypeId']").attr("checked",false);
		}
	}
   /**
	**根据设备型号获取软件版本，并以复选框的形式表现出来
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
		var url = "<s:url value='/gtms/stb/resource/openDeviceShowPic!getSoftVersion.action'/>";
		if(deviceModelIds!=""){
			$("div[@id='softVersion']").html("");
			$.post(url,{
				deviceModelIds : deviceModelIds
			},function(ajax){
				if(ajax!=""){
					var lineData = ajax.split("#");
					if(typeof(lineData)&&typeof(lineData.length)){
						var checkboxSoft ="<input type='checkbox' name='allSoftVersion' onclick='javascript:selectAllSoft(this)'>全选 &nbsp;&nbsp;&nbsp;";
						$("div[@id='softVersion']").append(checkboxSoft);
						for(var i=0;i<lineData.length;i++){
							var oneElement = lineData[i].split("$");
							var xValue = oneElement[0];
							var xText = oneElement[1];
							checkboxSoft = "<input type='checkbox' name='deviceTypeId' value='"+xValue+"'>"+xText+"  ";
							$("div[@id='softVersion']").append(checkboxSoft);
						}
					}else{
						$("div[@id='softVersion']").append("该型号没有对应软件版本！");
					}
				}else{
					$("div[@id='softVersion']").append("该型号没有对应软件版本！");
				}
			});
		}else{
			$("div[@id='softVersion']").html("请选择型号");
		}
	}
	//确定是否要手动添加还是批量导入
   function clkipCheck()
	{
		var ipch = document.getElementsByName("isOpenIP");
		if(ipch[0].checked)
		{
			$("tr[id*=setIPduan]").attr("style","display: none");
			$("tr[id=importIPAdd]").attr("style","display: none");
			$("button[name=addIP]").attr("style","display: none");  // 手工添加IP地址段按钮
			$("button[name=importIP]").attr("style","display: none"); // 批量导入IP地址段按钮				
		}
		if(ipch[1].checked)
		{
			$("tr[id*=setIPduan]").attr("style","display: inline");
			$("button[name=addIP]").attr("style","display: inline");  // 手工添加IP地址段按钮
			$("button[name=importIP]").attr("style","display: inline"); // 批量导入IP地址段按钮
		}
	}
	
// 手动添加IP地址段
	function addIPAddress()
	{
		$("tr[id=importIPAdd]").attr("style","display: none"); // 隐藏文件上传
		$("input[@name='checkIPButtonValue']").attr("value","byHandIP"); // 用于判断 提交数据时使用哪种方式提交
		
		var innerH = "<tr id='setIPduan" +ipDuanCount +"' style='display: inline'><td class='title_2' align='center' width='15%'>起始IP</td><td width='35%' ><input type='text' name='start_ip' maxlength='15'/></td><td class='title_2' align='center' width='15%'>终止IP</td><td width='35%'><input type='text' name='end_ip' maxlength='15'/></td></tr>";
		ipDuanCount++;
		$("#nodeAddIP").before(innerH);
	}
	// 批量导入IP地址段
	function batchAddIPAddress(){
		ipDuanCount = 1;
		$("input[@name='checkIPButtonValue']").attr("value","byBatchIP"); // 用于判断 提交数据时使用哪种方式提交
		$("tr[id*=setIPduan]").remove();
		$("tr[id=importIPAdd]").attr("style","display: inline");  // 显示文件上传
	}
	//确定是否要手动添加还是批量导入MAC
	   function clkMACCheck()
		{
			var macch = document.getElementsByName("isOpenMAC");
			if(macch[0].checked)
			{
				$("tr[id*=setMACduan]").attr("style","display: none");
				$("tr[id=importMACAdd]").attr("style","display: none");
				$("button[name=addMAC]").attr("style","display: none");  // 手工添加IP地址段按钮
				$("button[name=importMAC]").attr("style","display: none"); // 批量导入IP地址段按钮				
			}
			if(macch[1].checked)
			{
				$("tr[id*=setMACduan]").attr("style","display: inline");
				$("button[name=addMAC]").attr("style","display: inline");  // 手工添加IP地址段按钮
				$("button[name=importMAC]").attr("style","display: inline"); // 批量导入IP地址段按钮
			}
		}
		
	// 手动添加MAC地址段
		function addMACAddress()
		{
			$("tr[id=importMACAdd]").attr("style","display: none"); // 隐藏文件上传
			$("input[@name='checkMACButtonValue']").attr("value","byHandMAC"); // 用于判断 提交数据时使用哪种方式提交
			
			var innerH = "<tr id='setMACduan" +macDuanCount +"' style='display: inline'><td class='title_2' align='center' width='15%'>起始MAC</td><td width='35%' ><input type='text' name='start_mac' maxlength='17'/></td><td class='title_2' align='center' width='15%'>终止MAC</td><td width='35%'><input type='text' name='end_mac' maxlength='17'/></td></tr>";
			macDuanCount++;
			$("#addCustomer").before(innerH);
		}
		// 批量导入MAC地址段
		function batchAddMACAddress(){
			macDuanCount = 1;
			$("input[@name='checkMACButtonValue']").attr("value","byBatchMAC"); // 用于判断 提交数据时使用哪种方式提交
			$("tr[id*=setMACduan]").remove();
			$("tr[id=importMACAdd]").attr("style","display: inline");  // 显示文件上传
		}
		//手工添加
		function addCustomer(){
			if(customerCount>3){
				alert("手工添加帐号过多，请用文件导入形式添加");
				return ;
			}
			$("tr[id=importCustomer]").attr("style","display: none"); // 隐藏文件上传
			$("input[@name='checkCustomerButtonValue']").attr("value","byHandCum"); // 用于判断 提交数据时使用哪种方式提交
			
			var innerH = "<tr id='setCustomerInfo" +customerCount +"' style='display: inline'><td  class='title_2' align='center' width='15%'>业务帐号信息</td><td  colspan=3 width='85%' ><input type='text' name='customerInfomation' maxlength='17'/></td></tr>";
			customerCount++;
			$("#bootPic").before(innerH);
		}
		//批量导入业务帐号
		function batchAddCustomer(){
			customerCount = 1;
			$("input[@name='checkCustomerButtonValue']").attr("value","byBatchCust"); // 用于判断 提交数据时使用哪种方式提交
			$("tr[id*=setCustomerInfo]").remove();
			$("tr[id=importCustomer]").attr("style","display: inline");  // 显示文件上传
		}
		//是否导入帐号
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
		
	//IP 地址验证
	function checkIP(ip)
	{
		var exp=/^((\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.){3}(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/;
		var rev = false;
		if(null == ip.match(exp))
		{
			alert("IP地址格式不正确");
		}
		else
		{
			rev = true;
		}
		return rev;
	}
	//转换IP地址为数字
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
	//校验MAC地址
	function checkMAC(mac){
	  	var macs = new Array();
	  	macs = mac.split(":");
	  	if(macs.length != 6){
				alert("输入的mac地址格式不正确，请以xx:xx:xx:xx:xx:xx的形式输入（xx为16进制数字）!");
				return false;
			}
	   for (var s=0; s<6; s++) {
	   	var num = macs[s];
	   	if(num.length>2){
	   		alert("输入的mac地址格式不正确，请以xx:xx:xx:xx:xx:xx的形式输入（xx为16进制数字）!");
	   		 return false;  
	   	}
	   	var temp = parseInt(macs[s],16);
	   	if(isNaN(temp)){
	   		alert("输入的mac地址格式不正确，请以xx:xx:xx:xx:xx:xx的形式输入（xx为16进制数字）!"); 
	   	  return false; 
	   	}
	   	if(temp < 0 || temp > 255){
	   		alert("输入的mac地址格式不正确，请以xx:xx:xx:xx:xx:xx的形式输入（xx为16进制数字）!"); 
	   		return false;
	   	}
	  }
	  return true;
	}
	
</script>