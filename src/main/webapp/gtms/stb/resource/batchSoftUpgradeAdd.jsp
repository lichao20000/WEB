<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage"%>
<%@taglib prefix="ms" uri="/ailk-itms-web-tags"%>
<%@ page import="com.linkage.litms.LipossGlobals" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<link href="<s:url value="/css2/global.css"/>" rel="stylesheet" type="text/css">
<link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>

<SCRIPT LANGUAGE="JavaScript">		
	//系统类型
	var instAreaName = '<%= LipossGlobals.getLipossProperty("InstArea.ShortName")%>';
	
	var ipDuanCount = 1;
	var macDuanCount = 1;
	var customerCount = 1;
	
	function vendorChange(){
		var vendorId = $("select[@name='vendorId']").val();
		var url = "<s:url value='/gtms/stb/resource/stbSoftUpgrade!getTargetVersion.action'/>";
		$("tr[@id='trdevicetype']").hide();
		$("div[@id='devicetype']").html("请选择厂商");
		if(vendorId!="-1"){
			$("div[@id='targetVersion']").html("");
			$.post(url,{
				vendorId:vendorId
			},function(ajax){
				if(ajax!=""){
					var lineData = ajax.split("#");
					if(typeof(lineData)&&typeof(lineData.length)){
						$("div[@id='targetVersion']").append("<select name='pathId' class='bk' style='width: 400px' onchange='targetVersionChange()'>");
						$("select[@name='pathId']").append("<option value='-1' selected>==请选择==</option>");
						for(var i=0;i<lineData.length;i++){
							var oneElement = lineData[i].split("$");
							var xValue = oneElement[0];
							var xText = oneElement[1];
							option = "<option value='"+xValue+"'>=="+xText+"==</option>";
							$("select[@name='pathId']").append(option);
						}
						$("div[@id='devicetype']").html("请选择目标版本");
					}else{
						$("div[@id='targetVersion']").append("该厂商没有可升级的版本文件！");
					}
				}else{
					$("div[@id='targetVersion']").append("该厂商没有可升级的版本文件！");
				}
			});
		}else{
			$("div[@id='targetVersion']").html("请选择厂商");
		}
	}
	
	function targetVersionChange(){
		var pathId = $("select[@name='pathId']").val();
		var url = "<s:url value='/gtms/stb/resource/stbSoftUpgrade!checkVersionByTarget.action'/>";
		$("tr[@id='trdevicetype']").show();
		if(pathId!="-1"){
			$.post(url,{
				pathId:pathId
			},function(ajax){
				if(ajax!=""){
					var lineData = ajax.split("|");
					var innerHV = "";
					if(typeof(lineData)&&typeof(lineData.length)){
						//按照硬件版本型号分组
						for(var i=0;i<lineData.length;i++){
							var oneElement = lineData[i].split('\1');
							innerHV += "<input type=checkbox name='" + oneElement[0] +"' value='"+ oneElement[1] + "' onclick='hardVersionCLK(this)' />" + oneElement[0];
						}
						
						$("div[@id='devicetype']").html(innerHV);
					}else{
						$("div[@id='devicetype']").html("请选择目标版本");
					}
				}else{
					$("div[@id='devicetype']").html("请选择目标版本");
				}
			});
		}else{
			$("tr[@id='trdevicetype']").hide();
			$("div[@id='devicetype']").html("请选择目标版本");
		}
	}
	
	function devicetypeIdClick(name1,name2){
		var obj1 = document.getElementById(name1);
		var obj2 = document.getElementById(name2);
		var index = obj1.selectedIndex;
		var xValue = obj1.options[index].value;
		var xText = obj1.options[index].text;
		if(xValue!="-1"){
			obj1.options.remove(index);
			obj2.add(new Option(xText,xValue));
		}
	}
	
	function addeditVersion(isAdd,id,vendorId,softwareversion,versionDesc,versionPath,deviceModelIds){
		$("table[@id='addedit']").show();
		$("input[@name='isAdd']").val(isAdd);
		$("input[@name='pathId']").val(id);
		$("input[@name='softwareversion']").val(softwareversion);
		$("input[@name='versionDesc']").val(versionDesc);
		$("select[@name='vendorId']").val(vendorId);
		vendorChange();
		setTimeout('deviceModelIdchecked("'+deviceModelIds+'")', 1000);
    	$("input[@name='versionPath']").val(versionPath);
	}
	
	function deviceModelIdchecked(deviceModelIds){
		var deviceModelId = deviceModelIds.split(",");
    	for(var i = 0;i<deviceModelId.length;i++){
	        $("input[@name='deviceModelId'][@value='"+deviceModelId[i]+"']").attr("checked",true);
    	}
	}
	
	// 定制
	function ExecMod()
	{
		var btnValue = $("input[@name='checkButtonValue']").val(); // 用于判断使用哪种提交方式
		var btnValue4MAC = $("input[@name='checkMACButtonValue']").val(); // 用于判断使用哪种提交方式
		var btnValue4Cust =  $("input[@name='checkCustomerButtonValue']").val(); // 用于判断使用哪种提交方式
		
		if("hn_lt"!=instAreaName)
		{
			//根据提交的类型来给IP或MAC地址段赋值
			if("byBatchIP"==btnValue){
				$("input[@name='btnValue4IP']").attr("value",btnValue);
				$("input[@name='ipCheck']").attr("value","1");
				if(!importIP()){
					return;
				}
			}else{
				if(!inputIPbyHand()){
					return;
				}
			}
			
			if("byBatchMAC"==btnValue4MAC){
				$("input[@name='btnValue4MAC']").attr("value",btnValue4MAC);
				$("input[@name='macCheck']").attr("value","1");
				if(!importMAC()){
					return;
				}
			}else{
				if(!inputMACbyHand()){
					return;
				}
			}
			
			if("byBatchCust"==btnValue4Cust){
				$("input[@name='btnValue4Customer']").attr("value",btnValue4Cust);
				$("input[@name='custCheck']").attr("value","1");
				if(!importCustomer()){
					return;
				}
			}else{
				if(!inputCustomerByHand()){
					return;
				}
			}
		}
		
		var vendorId = $("select[@name='vendorId']").val();
		var cityId = $("select[@name='cityId']").val();
		var pathId = $("select[@name='pathId']").val();
		var strategyType = $("select[@name='strategyType']").val();
		if(vendorId=="-1"){
			alert("请选择厂商！");
			return;
		}
		if("hn_lt"==instAreaName && "-1"==cityId){
			alert("请选择属地!");
			return;
		}
		if(pathId==undefined||pathId=="-1"){
			alert("请选择目标版本!");
			return;
		}
		
        var isCheckPass = true;
        var str ="";
        str = $("#choseSoftV").attr("value");
		if(str==""||str=="-1" || str=="undefined" || str == undefined){
			alert("请选择要升级的版本!");
			return;
		}
		
		if(isCheckPass)
		{
			$("#doButton").attr("disabled",true);
			$("form[@name='batchexform']").attr("action","stbSoftUpgrade!batchSoftUpgradebak.action?devicetypeId="+str);
			$("form[@name='batchexform']").submit();
		}
	}
	
	
	// 解析上传的excel
	function importIP1(){
		var vendorId = $("select[@name='vendorId']").val();
		var pathId = $("select[@name='pathId']").val();
		if(vendorId=="-1"){
			alert("请选择厂商！");
			return false;
		}
		if(pathId==undefined||pathId=="-1"){
			alert("请选择目标版本!");
			return false;
		}
		
        var str ="";
        str = $("#choseSoftV").attr("value");
		if(str==""||str=="-1"){
			alert("请选择要升级的版本!");
			return false;
		}
		var ipcheckA = document.getElementsByName("isIpcheck");
		var ipcheck = "0";
		if(ipcheckA[1].checked){
			ipcheck = "1";
		}
			
		str = str.substring(0,str.length-1);
		
		var myFile = $("input[@name='upload']").val();
		if(""==myFile){
			alert("请选择文件!");
			return false;
		}
		
		var filet = myFile.split(".");
		if(filet.length<2){
			alert("请选择文件!");
			return false;
		}
		
		if("xls" == filet[filet.length-1])
		{
			var file = myFile.split("\\");
			var fileName = file[file.length-1];
			$("input[@name='uploadFileName']").attr("value",fileName);
			$("form[@name='batchexform']").attr("action","stbSoftUpgrade!batchImportIP.action?devicetypeId="+str+"&ipcheck="+ipcheck);
			$("form[@name='batchexform']").submit();
			return true;
		}
		else
		{
			alert("仅支持后缀为xls的文件");
			return false;
		}
	}
	
	 //手动导入ＩＰ地址段
	function inputIPbyHand(){
		var isCheckPass= true ;
	    var ipcheckAdd = document.getElementsByName("isIpcheck");
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
							return false;
						}
						else
						{
							if(convertIP(startip)>convertIP(endip))
							{
								alert("起始IP不能大于终止IP");
								isCheckPass = false;
								return false;
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
		return isCheckPass;
	}
	 
	//手动导入MAC
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
								return  false;
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
			return isCheckMACPass;
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
			$("input[@name='uploadFileName']").attr("value",fileName);
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
							return false;
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
		}
		return isCheckCustomerPass;
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
	
	function checkIP(ip)
	{
		var exp=/^((\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.){3}(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/;
		var rev = false;
		if(null == ip.match(exp)){
			alert("IP地址格式不正确");
		}else{
			rev = true;
		}
		return rev;
	}
	
	function convertIP(ip)
	{
		var ipArray = ip.split(".");
		var newIP = "";
		for(var i=0;i<ipArray.length;i++)
		{
			if(ipArray[i].length==1){
				ipArray[i] = "00"+ipArray[i];
			}else if(ipArray[i].length==2){
				ipArray[i] = "0"+ipArray[i];
			}
			newIP += ipArray[i] + ".";
		}
		return newIP.substring(0,newIP.length-1);
	}
	
	function cancerTask(taskId,version_path,softwareversion)
	{
		if("hn_lt"!=instAreaName)
		{
			var	width=310;    
			var height=150; 
			var url="<s:url value='/gtms/stb/resource/stbSoftUpgrade!validateCurUser.action'/>";
			url=url+"?versionPath="+version_path;
			url=url+"&softwareversion="+softwareversion;
			var returnVal=window.showModalDialog(url ,'','dialogWidth='+width+'px;dialogHeight='+ height+'px;resizable=no;help=no;center=yes;status=no;scroll=yes;edge=raised');
			if(returnVal=="closewin" || typeof(returnVal)=='undefined'){
				return;
			}
			if(returnVal.charAt(0)!="1"){
				alert("用户密码验证失败");
				return;
			}
		}
		
		var url = "<s:url value='/gtms/stb/resource/stbSoftUpgrade!cancerTask.action'/>";
	    $.post(url,{
			taskId:taskId
		},function(ajax){
	    	var s = ajax.split(",");
	    	if(s[0]=="1"){
	    		alert(s[1]);
	    		var strpage = "<s:url value='/gtms/stb/resource/stbSoftUpgrade!init.action'/>";
	    		window.location.href=strpage;
	    	}else if(s[0]=="0"){
	    		alert(s[1]);
	    	}else{
	    		alert("失效任务失败！");
	    	}
	    });
	}
	
	function validTask(taskId,version_path,softwareversion)
	{
		if("hn_lt"!=instAreaName)
		{
			var	width=310;    
			var height=150; 
			var url="<s:url value='/gtms/stb/resource/stbSoftUpgrade!validateCurUser.action'/>";
			url=url+"?versionPath="+version_path;
			url=url+"&softwareversion="+softwareversion;
			var returnVal=window.showModalDialog(url ,'','dialogWidth='+width+'px;dialogHeight='+ height+'px;resizable=no;help=no;center=yes;status=no;scroll=yes;edge=raised');
			if(returnVal=="closewin" || typeof(returnVal)=='undefined'){
				return;
			}
			if(returnVal.charAt(0)!="1"){
				alert("用户密码验证失败");
				return;
			}	
		}
		
		var url = "<s:url value='/gtms/stb/resource/stbSoftUpgrade!validTask.action'/>";
	    $.post(url,{
			taskId:taskId
		},function(ajax){
	    	var s = ajax.split(",");
	    	if(s[0]=="1"){
	    		alert(s[1]);
	    		var strpage = "<s:url value='/gtms/stb/resource/stbSoftUpgrade!init.action'/>";
	    		window.location.href=strpage;
	    	}else if(s[0]=="0"){
	    		alert(s[1]);
	    	}else{
	    		alert("激活任务失败！");
	    	}
	    });
	}
	
	function deleteTask(taskId,version_path,softwareversion)
	{
		if("hn_lt"!=instAreaName){
			var	width=310;    
			var height=150; 
			var url="<s:url value='/gtms/stb/resource/stbSoftUpgrade!validateCurUser.action'/>";
			url=url+"?versionPath="+version_path;
			url=url+"&softwareversion="+softwareversion;
			var returnVal=window.showModalDialog(url ,'','dialogWidth='+width+'px;dialogHeight='+ height+'px;resizable=no;help=no;center=yes;status=no;scroll=yes;edge=raised');
			if(returnVal=="closewin" || typeof(returnVal)=='undefined'){
				return;
			}
			if(returnVal.charAt(0)!="1"){
				alert("用户密码验证失败");
				return;
			}
		}
		
		var url = "<s:url value='/gtms/stb/resource/stbSoftUpgrade!deleteTask.action'/>";
	    $.post(url,{
			taskId:taskId
		},function(ajax){
			var s = ajax.split(",");
	    	if(s[0]=="1"){
	    		alert(s[1]);
	    		var strpage = "<s:url value='/gtms/stb/resource/stbSoftUpgrade!init.action'/>";
	    		window.location.href=strpage;
	    	}else if(s[0]=="0"){
	    		alert(s[1]);
	    	}else{
	    		alert("删除任务失败！");
	    	}
	    });
	}
	
	function viewTask(taskId){ 
		$("div[@id='divDetail']").show();
	    $("div[@id='divDetail']").html("正在查询，请稍等....");
	    var url = "<s:url value='/gtms/stb/resource/stbSoftUpgrade!getCountSoftupTaskResult.action'/>";
		$.post(url,{
			taskId:taskId
		},function(ajax){
			$("div[@id='divDetail']").html("");
			$("div[@id='divDetail']").append(ajax);
		});
    }
    
    function CloseDetail(){
		$("div[@id='divDetail']").hide();
	}
    
    //点击硬件型号的处理方式
	function hardVersionCLK(obj)
	{
    	var tempMV = obj.value.split("#");
    	if(obj.checked)
		{
			//保存选择的硬件版本和软件版本格式为：B600v2(ker,ker);B700()
			var choseYStr = obj.name + "(";
			var chosedYStr = document.getElementById("devicecheckdev").innerHTML;
			var choseID = "";
			var tempHtml = "";
			for(var i=0;i<tempMV.length;i++)
			{
				var tempIDK = tempMV[i].split("$");
				//组装硬件版本和软件版本
				choseYStr = choseYStr + tempIDK[1] + ",";
				//组装型号ID
				choseID = choseID + tempIDK[0] + ",";
				//生成多选框
				tempHtml = tempHtml + "<input type=checkbox checked=true name='softVersionCheck' value='" + tempIDK[0] + "' title='" + tempIDK[1] + "' onclick='softVersionCLK(this)' />" + tempIDK[1];
			}
			//设备硬件型号
			choseYStr = choseYStr.substring(0,choseYStr.length-1) + ")";
			if(null != chosedYStr && undefined != chosedYStr)
			{
				if(chosedYStr.length > 0)
				{
					document.getElementById("devicecheckdev").innerHTML += ";" + choseYStr;
				}
				else
				{
					document.getElementById("devicecheckdev").innerHTML = choseYStr;
				}
			}
			//保存型号ID
			document.getElementById("choseSoftV").value += choseID;
			//设置多选框列表
			$("div[@id='div_css']").show();
			$("td[@id=softVershow]").html("");
			$("td[@id=softVershow]").append(tempHtml);
		}
    	//取消的时候
		else
		{
			//删除保存的硬件版本和软件版本
			var chosedYStr = document.getElementById("devicecheckdev").innerHTML;
			var chosedYSA = chosedYStr.split(";");
			var tempChose = "";
			for(var i=0;i<chosedYSA.length;i++)
			{
				if(obj.name != chosedYSA[i].substring(0,obj.name.length))
				{
					tempChose += chosedYSA[i] + ";" ;
				}
			}
			document.getElementById("devicecheckdev").innerHTML = tempChose.substring(0,tempChose.length-1);
			//删除保存的型号ID
			var chosedID = document.getElementById("choseSoftV").value;
			var idArray = chosedID.split(",");
			var tempID = "";
			for(var i=0;i<idArray.length-1;i++)
			{
				var idcount = 0;
				for(var j=0;j<tempMV.length;j++)
				{
					
					if(tempMV[j].split("$")[0] == idArray[i])
					{
						idcount++;
					}
				}
				if(idcount == 0)
				{
					tempID += idArray[i] + ",";
				}
			}
			document.getElementById("choseSoftV").value = tempID;
			//设置多选框列表
			var tempHtml = "";
			for(var i=0;i<tempMV.length;i++)
			{
				var tempIDK = tempMV[i].split("$");
				//生成多选框
				tempHtml = tempHtml + "<input type=checkbox disabled name='softVersionCheck' value='" + tempIDK[0] + "' title='" + tempIDK[1] + "' onclick='softVersionCLK(this)' />" + tempIDK[1];
			}
			$("div[@id='div_css']").show();
			$("td[@id=softVershow]").html("");
			$("td[@id=softVershow]").append(tempHtml);
		}
	}
    
	//点击软件版本的处理方式
	function softVersionCLK(obj)
	{
		var softids = '';
		var softnames = '';
		$("input[name='softVersionCheck']").each(function(i, el) {
			if($(this).attr("checked")){
				softids = softids + $(this).val() + ",";
				softnames = softnames + $(this).attr("title") + ",";
			}
		});
		$("#choseSoftV").attr("value",softids);
		$("#devicecheckdev").attr("innerHTML",softnames.substring(0,softnames.length-1));
	}
	
	function clkipCheck()
	{
		var ipch = document.getElementsByName("isIpcheck");
		if(ipch[0].checked)
		{
			$("tr[id*=setIPduan]").attr("style","display: none");
			$("button[name=addIPduan]").attr("style","display: none");  // 手工添加IP地址段按钮
			
			if("jx_dx"==instAreaName){
				$("button[name=importIPduan]").attr("style","display: none"); // 批量导入IP地址段按钮				
			}
			$("input[@name='checkButtonValue']").attr("value","byHand");
		}
		if(ipch[1].checked)
		{
			$("tr[id*=setIPduan]").attr("style","display: inline");
			$("button[name=addIPduan]").attr("style","display: inline");  // 手工添加IP地址段按钮
			
			if("jx_dx"==instAreaName){
				$("button[name=importIPduan]").attr("style","display: inline"); // 批量导入IP地址段按钮
			}
		}
	}
	
	function softdivcl()
	{
		$("div[@id='div_css']").hide();
	}
	
	function viewdetailTask(taskid)
	{
		var page = "<s:url value='/gtms/stb/resource/stbSoftUpgrade!queryTaskDetailById.action'/>?taskId="+taskid;
		window.open(page,"","left=20,top=20,width=800,height=600,resizable=no,scrollbars=yes");
	}
	
	// 手动添加IP地址段
	function addIPDuanFun()
	{
		if("jx_dx"==instAreaName){
			$("tr[id=importIPduan0]").attr("style","display: none"); // 隐藏文件上传
		}
		$("input[@name='checkButtonValue']").attr("value","byHand"); // 用于判断 提交数据时使用哪种方式提交
		
		var innerH = "<tr id='setIPduan" +ipDuanCount +"' style='display: inline'><td class='title_2' align='center' width='15%'>起始IP</td><td width='35%' ><input type='text' name='start_ip' maxlength='15'/></td><td class='title_2' align='center' width='15%'>终止IP</td><td width='35%'><input type='text' name='end_ip' maxlength='15'/></td></tr>";
		ipDuanCount++;
		$("#nodeAddIP").before(innerH);
	}
	
	function queryTask()
	{
		var querycityId = $("select[name=querycityId]").val();
		var queryvendorId = $("select[name=queryvendorId]").val();
		var queryVaild = $("select[name=queryVaild]").val();
		var url = "<s:url value='/gtms/stb/resource/stbSoftUpgrade!init.action'/>?queryCityId=" + querycityId+"&queryVendorId="+queryvendorId+"&queryVaild="+queryVaild;
	    window.location.href=url;
	}
	
	// 批量导入IP地址段
	function importIPDuanFun(){
		ipDuanCount = 1;
		$("input[@name='checkButtonValue']").attr("value","byBatchIP"); // 用于判断 提交数据时使用哪种方式提交
		$("tr[id*=setIPduan]").remove();
		$("tr[id=importIPduan0]").attr("style","display: inline");  // 显示文件上传
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
			$("input[@name='checkMACButtonValue']").attr("value","byHandMAC");
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
				$("input[@name='checkCustomerButtonValue']").attr("value","byHandCum");
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
		if(null == ip.match(exp)){
			alert("IP地址格式不正确");
		}else{
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
			if(ipArray[i].length==1){
				ipArray[i] = "00"+ipArray[i];
			}else if(ipArray[i].length==2){
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
	
	// 下载excel模板
	function toExport()
	{
		$("form[@name='batchexform']").attr("action","stbSoftUpgrade!downloadIPduanTemplate.action");
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
</SCRIPT>

</head>

<body>
	<TABLE width="98%" align="center" class="querytable">
		<TR>
			<TD>
				<IMG height=20 src="<s:url value='/images/bite_2.gif'/>" width=24>
				您当前的位置：批量策略版本升级任务管理
			</TD>
		</TR>
	</TABLE>
	<br>

	<s:form action="" method="post" enctype="multipart/form-data" name="batchexform">
		<input type="hidden" name="macSG" value="">
		<input type="hidden" name="ipSG"   value="">
		<input type="hidden" name="custSG"   value="">
		<input type="hidden" name="ipCheck"  value="">
		<input type="hidden" name="custCheck"  value="">
		<input type="hidden" name="macCheck"   value="">
		<input type="hidden" name="btnValue4MAC"   value="">
		<input type="hidden" name="btnValue4IP"   value="">
		<input type="hidden" name="btnValue4Customer"   value="">
		<table class="querytable" width="98%" align="center">
			<tr>
				<td colspan="4" class="title_1">批量策略版本升级任务定制</td>
			</tr>
			<TR>
				<TD class="title_2" align="center" width="15%">厂商</TD>
				<TD width="85%" colspan="3">
					<s:select list="vendorList" name="vendorId" headerKey="-1"
						headerValue="请选择厂商" listKey="vendor_id" listValue="vendor_add"
						value="vendorId" cssClass="bk" onchange="vendorChange()"
						theme="simple">
					</s:select> <font color="red">*</font>
				</TD>
			</TR>
			<TR>
				<TD class="title_2" align="center" width="15%">属地</TD>
				<TD width="85%" colspan="3">
					<s:select list="cityList" name="cityId" headerKey="-1"
						headerValue="请选择属地" listKey="city_id" listValue="city_name"
						value="cityId" cssClass="bk" theme="simple">
					</s:select>
				</TD>
			</TR>
			<TR>
				<TD class="title_2" align="center" width="15%">目标版本</TD>
				<TD width="85%" colspan="3">
					<div id="targetVersion">请选择厂商</div>
				</TD>
			</TR>
			<tr>
				<td colspan="1" width="15%" align="center" class="title_2">要升级的版本</td>
				<TD width="85%" colspan="3">
					<div id="devicetype">请选择厂商</div>
				</TD>
			</tr>
			<tr id="devicemodeliddiv" style="display: none">
				<td colspan="1" width="15%" align="center" class="title_2">软件版本</td>
				<td width="85%" colspan="3">
					<input type="hidden" id="choseSoftV" value=""/>
					<div id="softVersion">
						<table>
							<tr>
								<td>请选择软件版本</td>
							</tr>
							<tr>
								<td id="softVcheckD"></td>
							</tr>
						</table>
					</div>
				</td>
			</tr>
			<tr>
				<td colspan="1" width="15%" align="center" class="title_2">已选择版本</td>
				<td width="85%" colspan="3"><span id="devicecheckdev"></span></td>
			</tr>
			<ms:inArea areaCode="hn_lt" notInMode="true">
			<tr>
				<td class="title_2" align="center" width="15%">启用IP地址段</td>
				<td width="85%" colspan="3">
					<input type="radio" checked name="isIpcheck" id="isIpcheck" value="0" checked="checked" onclick="clkipCheck()"/>否
					<input type="radio" name="isIpcheck" id="isIpcheck" value="1" onclick="clkipCheck()"/>是
					<button name="addIPduan" style="display:none" onclick="addIPDuanFun()">手动添加IP地址段</button>
					<ms:inArea areaCode="jx_dx">
					<button name="importIPduan" style="display:none" onclick="importIPDuanFun()">批量导入IP地址段</button>
					</ms:inArea>
					<input type="hidden" name="checkButtonValue" name="checkButtonValue" value="" />
				</td>
			</tr>
			<tr id="setIPduan0" style="display: none">
				<td class="title_2" align="center" width="15%">起始IP</td>
				<td width="35%" >
					<input type="text" name="start_ip" maxlength="15"/>
				</td>
				<td class="title_2" align="center" width="15%">终止IP</td>
				<td width="35%">
					<input type="text" name="end_ip" maxlength="15"/>
				</td>
			</tr>
			<ms:inArea areaCode="jx_dx">
			<tr id="importIPduan0" style="display: none">
				<td class="title_2" align="center" width="15%">请选择文件</td>
				<td width="85%" colspan="3">
					<s:file label="上传" theme="simple" name="upload"></s:file><font color="red">*</font>
					xls格式文档
					<a href="javascript:void(0);" onclick="toExport();"><font color="red">下载模板</font></a>
					<input type="hidden" name="uploadFileName" value=""/>
				</td>
			</tr>
			</ms:inArea>
			<TR id="nodeAddIP">
				<TD class="title_2" align="center" width="15%">启用MAC地址段</TD>
				<TD width="85%" colspan="3">
					<input type="radio" id="isOpenMAC" name="isOpenMAC" value="0" checked="checked" onclick="clkMACCheck();"/>否
					<input type="radio" id="isOpenMAC" name="isOpenMAC" value="1" onclick="clkMACCheck();"/>是
					<button name="addMAC" onclick="addMACAddress()"  style="display:none">手动添加MAC地址段</button>
					<button name="importMAC" onclick="batchAddMACAddress()"  style="display:none">批量导入MAC地址段</button>
					<input type="hidden" id="checkMACButtonValue" name="checkMACButtonValue" value="" />
				</TD>
			</TR>
			<tr id="importMACAdd" style="display: none">
				<td class="title_2" align="center" width="15%">请选择文件</td>
				<td width="85%" colspan="3">
					<s:file label="上传" theme="simple" name="uploadMAC"></s:file><font color="red">*</font>
					xls格式文档
					<a href="javascript:void(0);" onclick="toExportMAC();"><font color="red">下载模板</font></a>
					<input type="hidden" name="uploadFileName4MAC" value=""/>
				</td>
			</tr>
				
			<TR id="addCustomer">
				<TD class="title_2" align="center" width="15%">业务帐号列表</TD>
				<TD width="85%" colspan="3">
					<input type="radio" id="isAddCustomer" name="isAddCustomer" value="0" checked="checked" onclick="clkCustomerCheck();"/>否
					<input type="radio" id="isAddCustomer" name="isAddCustomer" value="1" onclick="clkCustomerCheck();"/>是
					<button name="addCustomerInfo" onclick="addCustomer()"  style="display:none">手动添加帐号信息</button>
					<button name="importCustomerInfo" onclick="batchAddCustomer()"  style="display:none">批量导入帐号信息</button>
					<input type="hidden" id="checkCustomerButtonValue" name="checkCustomerButtonValue" value="" />
				</TD>
			</TR>
			<tr id="importCustomer" style="display: none">
				<td class="title_2" align="center" width="15%">请选择文件</td>
				<td width="85%" colspan="3">
					<s:file label="上传" theme="simple" name="uploadCustomer"></s:file><font color="red">*</font>
					xls格式文档
					<a href="javascript:void(0);" onclick="toExportCust();"><font color="red">下载模板</font></a>
					<input type="hidden" name="uploadFileName4Customer" value=""/>
				</td>
			</tr>
			</ms:inArea>
			<tr id="IPduanH">
				<TD class="title_2" align="center" width="15%">策略方式</td>
				<ms:inArea areaCode="hn_lt" notInMode="true">
				<TD width="85%" colspan="3">
					<SELECT name="strategyType" class="bk">
						<option value="4">机顶盒重启或恢复出厂设置</option>
						<option value="4">周期事件</option>
					</SELECT>
				</td>
				</ms:inArea>
				<ms:inArea areaCode="hn_lt" notInMode="false">
				<TD width="85%" colspan="3">
					<SELECT name="strategyType" class="bk">
						<option value="4">机顶盒下次连接</option>
					</SELECT>
				</td>
				</ms:inArea>
			</tr>
			<tr>
				<td colspan="4" class="foot" align="right">
					<div class="right">
						<button id="doButton" onclick="ExecMod()">定制</button>
					</div>
				</td>
			</tr>
		</table>
		</s:form>
		
		<br>
		<br>
		<ms:inArea areaCode="hn_lt" notInMode="true">
		<table width="98%" class="querytable" align="center">
			<tr>
				<td class="title_2" align="center">属地</td>
				<td width="15%">
					<s:select list="cityList" name="querycityId" listKey="city_id" 
						listValue="city_name" value="queryCityId" cssClass="bk" theme="simple">
					</s:select>
				</td>
				<td class="title_2" align="center">厂商</td>
				<td width="15%">
					<s:select list="vendorList" name="queryvendorId" headerKey="-1"
						headerValue="全部" listKey="vendor_id" listValue="vendor_add"
						value="queryVendorId" cssClass="bk" theme="simple">
					</s:select>
				</td>
				<td class="title_2" align="center">状态</td>
				<td width="15%">
					<s:select name="queryVaild" value="queryVaild" list="#{'1':'已激活','0':'已失效'}" cssClass="bk" theme="simple" headerKey="-1" headerValue="全部" >
					</s:select>
				</td>
				<td class="title_2" align="center"><button onclick="queryTask()">查询</button></td>
			</tr>
		</table>
		
		<input type="hidden" name="showType" value="3">
		<table width="98%" class="listtable" align="center">
			<thead>
				<tr>
					<th align="center" width="10%">属地</th>
					<th align="center" width="10%">厂商</th>
					<th align="center" width="15%">目标版本</th>
					<th align="center" width="10%">定制人</th>
					<th align="center" width="10%">订制时间</th>
					<th align="center" width="10%">更新时间</th>
					<th align="center" width="10%">状态</th>
					<th align="center" width="15%">操作</th>
				</tr>
			</thead>
			<s:if test="tasklist!=null">
				<s:if test="tasklist.size()>0">
					<tbody>
						<s:iterator value="tasklist">
							<tr>
								<td align="center"><s:property value="city_name" /></td>
								<td align="center"><s:property value="vendor_add" /></td>
								<td align="center"><s:property value="softwareversion" /></td>
								<td align="center"><s:property value="acc_loginname" /></td>
								<td align="center"><s:property value="record_time" /></td>
								<td align="center"><s:property value="update_time" /></td>
								<s:if test='valid=="1"'>
									<td align="center">已激活</td>
									<td align="center">
										<s:if test='showType!=1 && (accoid==acc_oid||areaId=="1")'>
											<button name="cancerButton"
												onclick="javascript:cancerTask('<s:property value="task_id" />','<s:property value="version_path" />','<s:property value="softwareversion" />')">
												失效
											</button>
											<s:if test="showType!=2">
												<button name="cancerButton"
													onclick="javascript:deleteTask('<s:property value="task_id" />','<s:property value="version_path" />','<s:property value="softwareversion" />')">
													删除
												</button>
											</s:if>
										</s:if>
										<button name="viewButton"
											onclick="javascript:viewTask('<s:property value="task_id"/>')">
											查看结果
										</button>
										<button name="viewDetailButton"
											onclick="javascript:viewdetailTask('<s:property value="task_id"/>')">
											查看详细
										</button>
									</td>
								</s:if>
								<s:else>
									<td align="center">已失效</td>
									<td align="center">
										<s:if test='showType!=1 && (accoid==acc_oid||areaId=="1")'>
											<button name="cancerButton"
												onclick="javascript:validTask('<s:property value="task_id" />','<s:property value="version_path" />','<s:property value="softwareversion" />')">
												激活
											</button>
											<s:if test="showType!=2">
												<button name="cancerButton"
													onclick="javascript:deleteTask('<s:property value="task_id" />','<s:property value="version_path" />','<s:property value="softwareversion" />')">
													删除
												</button>
											</s:if>
										</s:if>
										<button name="viewButton"
											onclick="javascript:viewTask('<s:property value="task_id"/>')">
											查看结果
										</button>
										<button name="viewDetailButton"
											onclick="javascript:viewdetailTask('<s:property value="task_id"/>')">
											查看详细
										</button>
									</td>
								</s:else>

							</tr>
						</s:iterator>
					</tbody>
					<tfoot>
						<tr bgcolor="#FFFFFF">
							<td colspan="8" align="right">
								<lk:pages url="/gtms/stb/resource/stbSoftUpgrade!init.action"
									styleClass="" showType="" isGoTo="true" changeNum="false" />
							</td>
						</tr>
					</tfoot>
				</s:if>
				<s:else>
					<tbody>
						<tr>
							<td colspan="8">
								<font color="red">没有定制的任务</font>
							</td>
						</tr>
					</tbody>
				</s:else>
			</s:if>
			<s:else>
				<tbody>
					<tr>
						<td colspan="8">
							<font color="red">没有定制的任务</font>
						</td>
					</tr>
				</tbody>
			</s:else>
		</table>
		</ms:inArea>
		
		<div id="divDetail"
			style="position: absolute; z-index: 255; top: 200px; left: 350px; width: 40%; border-width: 1; border-style: ridge; background-color: #eeeeee; padding-top: 10px; display: none"></div>
		<div id="div_css" style="position: absolute; z-index: 255; top: 200px; left: 350px; width: 40%; border-width: 1; border-style: ridge; background-color: #eeeeee; padding-top: 10px; display: none">
			<table class="querytable" align="center" width="100%" style="table-layout: fixed">
				<tr>
					<td width="120px" class="title_2" align="center">请选择软件版本型号</td>
					<td id="softVershow" class="title_2" align="center"></td>
				</tr>
				<tr>
					<td colspan="2" class="title_2" align="center">
						<button align="center" name="softdivtbn" onclick="softdivcl()">
						关闭
						</button>
					</td>
				</tr>
			</table>
		</div>
	</body>