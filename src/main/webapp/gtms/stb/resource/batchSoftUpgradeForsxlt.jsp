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
<%--  <link href="<s:url value="/css/css_green.css"/>" rel="stylesheet" type="text/css">
 --%><script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<style type="text/css">
.sxlt_query{font-size: 12px;background-color: #DFDFFF;color:whit;CURSOR: hand;text-align: center;}
.green_foot{background-color: #E1EEEE !important;}
</style>
<SCRIPT LANGUAGE="JavaScript">		
	//ϵͳ����
	var instAreaName = '<%= LipossGlobals.getLipossProperty("InstArea.ShortName")%>';
	
	var ipDuanCount = 1;
	var macDuanCount = 1;
	var customerCount = 1;
	
	 
	function vendorChange(){
		var vendorId = $("select[@name='vendorId']").val();
		var url = "<s:url value='/gtms/stb/resource/stbSoftUpgrade!getTargetVersion.action'/>";
		$("tr[@id='trdevicetype']").hide();
		$("div[@id='devicetype']").html("��ѡ����");
		if(vendorId!="-1"){
			$("div[@id='targetVersion']").html("");
			$.post(url,{
				vendorId:vendorId
			},function(ajax){
				if(ajax!=""){
					var lineData = ajax.split("#");
					if(typeof(lineData)&&typeof(lineData.length)){
						$("div[@id='targetVersion']").append("<select name='pathId' class='bk' style='width: 400px' onchange='targetVersionChange()'>");
						$("select[@name='pathId']").append("<option value='-1' selected>==��ѡ��==</option>");
						for(var i=0;i<lineData.length;i++){
							var oneElement = lineData[i].split("$");
							var xValue = oneElement[0];
							var xText = oneElement[1];
							option = "<option value='"+xValue+"'>=="+xText+"==</option>";
							$("select[@name='pathId']").append(option);
						}
						$("div[@id='devicetype']").html("��ѡ��Ŀ��汾");
					}else{
						$("div[@id='targetVersion']").append("�ó���û�п������İ汾�ļ���");
					}
				}else{
					$("div[@id='targetVersion']").append("�ó���û�п������İ汾�ļ���");
				}
			});
		}else{
			$("div[@id='targetVersion']").html("��ѡ����");
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
						innerHV = "<table>";
						//����Ӳ���汾�ͺŷ���
						for(var i=0;i<lineData.length;i++){
							var oneElement = lineData[i].split('\1');
							if(i%6==0){
								innerHV=innerHV+"<tr style='border:0px'>";
							}
							innerHV += "<td style='border:0px'><input type=checkbox name='" + oneElement[0] +"' value='"+ oneElement[1] + "' onclick='hardVersionCLK(this)' />" + oneElement[0]+"</td>";
							if( (i!=0 && (i+1)%6==0) || (i==lineData.length-1)){
								innerHV=innerHV+"</tr>";
							}
						}
						innerHV = innerHV+"</table>";
						$("div[@id='devicetype']").html(innerHV);
					}else{
						$("div[@id='devicetype']").html("��ѡ��Ŀ��汾");
					}
				}else{
					$("div[@id='devicetype']").html("��ѡ��Ŀ��汾");
				}
			});
		}else{
			$("tr[@id='trdevicetype']").hide();
			$("div[@id='devicetype']").html("��ѡ��Ŀ��汾");
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
	
	// ����
	function ExecMod()
	{
		
		if(instAreaName != "sx_lt"){
			if(sxSoftCitys==""){
				alert("��ѡ������");
				return false;
			}
		}else{
			var taskType = $("input[name='taskType']:checked").val();
			if(taskType == 1){
				
			}else{
				if(sxSoftCitys==""){
					alert("��ѡ������");
					return false;
				}
			}
		}
		
		var vendorId = $("select[@name='vendorId']").val();
		var cityId = "";
		//ɽ����ͨ���ؿ�ѡ��
		if(instAreaName == "sd_lt"){
			//input_cityId
			$("input[@name='input_cityId'][@checked]").each(function(){ 
				var cid = $(this).val();
				if(cid == "00"){
					cityId = "00";
					return false;
				}else{
					cityId = cityId + cid +",";
				}
		    });
			$("#hidden_input_cid").val(cityId);
		}else{
			cityId = $("select[@name='cityId']").val();
		}
		var pathId = $("select[@name='pathId']").val();
		var strategyType = $("select[@name='strategyType']").val();
		if(vendorId=="-1"){
			alert("��ѡ���̣�");
			return;
		}
		if(pathId==undefined||pathId=="-1"){
			alert("��ѡ��Ŀ��汾!");
			return;
		}
		
        var str = $("#choseSoftV").attr("value");
		if(str==""||str=="-1" || str=="undefined" || str == undefined){
			alert("��ѡ��Ҫ�����İ汾!");
			return;
		}
		
		$("#doButton").attr("disabled",true);
		$("form[@name='batchexform']").attr("action","stbSoftUpgrade!batchSoftUpgradeSxlt.action?devicetypeId="+str);
		$("form[@name='batchexform']").submit();
	}
	
	
	// �����ϴ���excel
	function importIP1(){
		var vendorId = $("select[@name='vendorId']").val();
		var pathId = $("select[@name='pathId']").val();
		if(vendorId=="-1"){
			alert("��ѡ���̣�");
			return false;
		}
		if(pathId==undefined||pathId=="-1"){
			alert("��ѡ��Ŀ��汾!");
			return false;
		}
		
        var str ="";
        str = $("#choseSoftV").attr("value");
		if(str==""||str=="-1"){
			alert("��ѡ��Ҫ�����İ汾!");
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
			alert("��ѡ���ļ�!");
			return false;
		}
		
		var filet = myFile.split(".");
		if(filet.length<2){
			alert("��ѡ���ļ�!");
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
			alert("��֧�ֺ�׺Ϊxls���ļ�");
			return false;
		}
	}
	
	 //�ֶ�����ɣе�ַ��
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
								alert("��ʼIP���ܴ�����ֹIP");
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
			$("input[@name='uploadFileName']").attr("value",fileName);
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
	
	function checkIP(ip)
	{
		var exp=/^((\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.){3}(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/;
		var rev = false;
		if(null == ip.match(exp)){
			alert("IP��ַ��ʽ����ȷ");
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
		if("hn_lt"!=instAreaName && 'sx_lt'!=instAreaName)
		{
			var	width=310;    
			var height=150; 
			var url="<s:url value='/gtms/stb/resource/stbSoftUpgrade!validateCurUser.action'/>";
			//url=url+"?versionPath="+version_path;
			//url=url+"&softwareversion="+softwareversion;
			//�޸ı������룬yl
			var obj = new Object();
			obj.versionPath = version_path;
			obj.softwareversion = softwareversion
			var returnVal=window.showModalDialog(url ,'','dialogWidth='+width+'px;dialogHeight='+ height+'px;resizable=no;help=no;center=yes;status=no;scroll=yes;edge=raised');
			if(returnVal=="closewin" || typeof(returnVal)=='undefined'){
				return;
			}
			if(returnVal.charAt(0)!="1"){
				alert("�û�������֤ʧ��");
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
	    		if("hn_lt"==instAreaName){
	    			var showType=$("input[name=showType]").val();
	    			strpage+="?showType="+showType;
	    		}
	    		
	    		window.location.href=strpage;
	    	}else if(s[0]=="0"){
	    		alert(s[1]);
	    	}else{
	    		alert("ʧЧ����ʧ�ܣ�");
	    	}
	    });
	}
	
	function validTask(taskId,version_path,softwareversion)
	{
		if("hn_lt"!=instAreaName && 'sx_lt'!=instAreaName)
		{
			var	width=310;    
			var height=150; 
			var url="<s:url value='/gtms/stb/resource/stbSoftUpgrade!validateCurUser.action'/>";
			//url=url+"?versionPath="+version_path;
			//url=url+"&softwareversion="+softwareversion;
			//�޸��Ա�������,yl
			var obj = new Object();
			obj.versionPath = version_path;
			obj.softwareversion = softwareversion;
			var returnVal=window.showModalDialog(url ,obj,'dialogWidth='+width+'px;dialogHeight='+ height+'px;resizable=no;help=no;center=yes;status=no;scroll=yes;edge=raised');
			if(returnVal=="closewin" || typeof(returnVal)=='undefined'){
				return;
			}
			if(returnVal.charAt(0)!="1"){
				alert("�û�������֤ʧ��");
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

	    		if("hn_lt"==instAreaName){
	    			var showType=$("input[name=showType]").val();
	    			strpage+="?showType="+showType;
	    		}

	    		window.location.href=strpage;
	    	}else if(s[0]=="0"){
	    		alert(s[1]);
	    	}else{
	    		alert("��������ʧ�ܣ�");
	    	}
	    }); 
	}
	
	function deleteTask(taskId,version_path,softwareversion)
	{
		if("hn_lt"!=instAreaName && 'sx_lt'!=instAreaName){
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
				alert("�û�������֤ʧ��");
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
	    		if("hn_lt"==instAreaName){
	    			var showType=$("input[name=showType]").val();
	    			strpage+="?showType="+showType;
	    		}
	    		window.location.href=strpage;
	    	}else if(s[0]=="0"){
	    		alert(s[1]);
	    	}else{
	    		alert("ɾ������ʧ�ܣ�");
	    	}
	    });
	}
	
	function viewTask(taskId,importType,taskType){ 
		$("div[@id='divDetail']").show();
	    $("div[@id='divDetail']").html("���ڲ�ѯ�����Ե�....");
	    var url = "<s:url value='/gtms/stb/resource/stbSoftUpgrade!getCountSoftupTaskResult.action'/>";
		$.post(url,{
			taskId:taskId,
			taskImportType:importType,
			taskType : taskType
		},function(ajax){
			$("div[@id='divDetail']").html("");
			$("div[@id='divDetail']").append(ajax);
		});
    }
    
    function CloseDetail(){
		$("div[@id='divDetail']").hide();
	}
    
    //���Ӳ���ͺŵĴ���ʽ
	function hardVersionCLK(obj)
	{
    	var tempMV = obj.value.split("#");
    	if(obj.checked)
		{
			//����ѡ���Ӳ���汾������汾��ʽΪ��B600v2(ker,ker);B700()
			var choseYStr = obj.name + "(";
			var chosedYStr = document.getElementById("devicecheckdev").innerHTML;
			var choseID = "";
			var tempHtml = "";
			for(var i=0;i<tempMV.length;i++)
			{
				var tempIDK = tempMV[i].split("$");
				//��װӲ���汾������汾
				choseYStr = choseYStr + tempIDK[1] + ",";
				if( (i!=0 && (i+1)%3==0) && (i!=tempMV.length-1)){
					choseYStr = choseYStr + "<br>"
				}
				
				if((i==tempMV.length-1)){
					choseYStr = choseYStr.substring(0,choseYStr.length-1) + ");<br>";
				}
				//��װ�ͺ�ID
				choseID = choseID + tempIDK[0] + ",";
				//���ɶ�ѡ��
				if(instAreaName == 'sd_lt'){
					tempHtml = tempHtml + "<input type=checkbox checked=true name='softVersionCheck' value='" + tempIDK[0] 
					+ "' title='" + tempIDK[1] + "' onclick='softVersionCLK4SDLT(this)' fname='" + $(obj).attr("name") + "' />" + tempIDK[1];
				}else{
					
					if(i%2==0){
						tempHtml=tempHtml+"<tr style='border:0px'>";
					}
					tempHtml = tempHtml + "<td style='border:0px;text-align: left'><input type=checkbox checked=true name='softVersionCheck' value='" + tempIDK[0] 
					+ "' title='" + tempIDK[1] + "' onclick='softVersionCLK(this)' fname='" + $(obj).attr("name") + "' />" + tempIDK[1]+"</td>";
					if( (i!=0 && (i+1)%2==0) || (i==tempMV.length-1)){
						tempHtml=tempHtml+"</tr>";
					}
				}
				
			}
			if(null != chosedYStr && undefined != chosedYStr)
			{
				if(chosedYStr.length > 0)
				{
					document.getElementById("devicecheckdev").innerHTML += choseYStr;
				}
				else
				{
					document.getElementById("devicecheckdev").innerHTML = choseYStr;
				}
			}
			//�����ͺ�ID
			document.getElementById("choseSoftV").value += choseID;
			//���ö�ѡ���б�
			$("div[@id='div_css']").show();
			$("td[@id=softVershow]").html("");
			if(instAreaName == 'sd_lt'){
				$("td[@id=softVershow]").append(tempHtml);
			}else{
				$("td[@id=softVershow]").append("<table>"+tempHtml+"</table>");
			}
			if("sd_lt" == instAreaName){
				$("#devicecheckdev4sdlt").attr("value", $("#devicecheckdev").html());
				$("#choseSoftV4sdlt").attr("value", $("#choseSoftV").val());
				allSel(true);
			}
		}
    	//ȡ����ʱ��
		else
		{
			//ɾ�������Ӳ���汾������汾
			var chosedYStr = document.getElementById("devicecheckdev").innerHTML;
			var chosedYSA = chosedYStr.split(";<br>");
			var tempChose = "";
			for(var i=0;i<chosedYSA.length;i++)
			{
				if(chosedYSA[i]!='' && obj.name != chosedYSA[i].substring(0,obj.name.length))
				{
					tempChose += chosedYSA[i] + ";<br>" ;
				}
			}
			document.getElementById("devicecheckdev").innerHTML = tempChose;
			//ɾ��������ͺ�ID
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
			//���ö�ѡ���б�
			var tempHtml = "";
			for(var i=0;i<tempMV.length;i++)
			{
				var tempIDK = tempMV[i].split("$");
				//���ɶ�ѡ��
				if(instAreaName == 'sd_lt'){
					tempHtml = tempHtml + "<input type=checkbox name='softVersionCheck' value='" + tempIDK[0] 
					+ "' title='" + tempIDK[1] + "' onclick='softVersionCLK4SDLT(this)' fname='" + $(obj).attr("name") + "' />" + tempIDK[1];
				}else{
					if(i%2==0){
						tempHtml=tempHtml+"<tr style='border:0px'>";
					}
					tempHtml = tempHtml + "<td style='border:0px;text-align: left'><input type=checkbox name='softVersionCheck' value='" + tempIDK[0] 
					+ "' title='" + tempIDK[1] + "' onclick='softVersionCLK(this)' fname='" + $(obj).attr("name") + "' />" + tempIDK[1]+"</td>";
					if( (i!=0 && (i+1)%2==0) || (i==tempMV.length-1)){
						tempHtml=tempHtml+"</tr>";
					}
				}
				/* tempHtml = tempHtml + "<input type=checkbox disabled name='softVersionCheck' value='" + tempIDK[0] + "' title='" 
				+ tempIDK[1]  + "' onclick='softVersionCLK(this)' fname='" + $(obj).attr("name") + "' />" +  tempIDK[1]; */
			}
			$("div[@id='div_css']").show();
			$("td[@id=softVershow]").html("");
			if(instAreaName == 'sd_lt'){
				$("td[@id=softVershow]").append(tempHtml);
			}else{
				$("td[@id=softVershow]").append("<table>"+tempHtml+"</table>");
			}
			if("sd_lt" == instAreaName){
				allSel(false);
			}
		}
    	if("sd_lt" == instAreaName){
			$("input[name='currSelVersion']").val(obj.name); 
		}
	}
    
	//�������汾�Ĵ���ʽ
	function softVersionCLK(obj)
	{
		var fname = $(obj).attr("fname");
		var softnames = $("#devicecheckdev").attr("innerHTML");
		var nameArr = new Array();
		if(softnames!=undefined && softnames!=""){
			nameArr=softnames.split(";<br>");
		}
		
		var softids = $("#choseSoftV").attr("value");
		var idArr=new Array();
		if(softids!=undefined && softids!=""){
		 	idArr = softids.split(",");
		}else{
			softids="";
		}
		var idstr = '';
		var namestr = '';

		for (var i = 0; i < nameArr.length; i++) {
			if (nameArr[i]!='' && nameArr[i].indexOf(fname) == -1) {
				namestr += nameArr[i] + ";<br>";
			}
		}
		for (var j = 0; j < idArr.length - 1; j ++) {
			if (idArr[j]!=($(obj).attr("value"))) {
				idstr += idArr[j] + ",";
			}
		}

		var thisname = "";
		var count=0;
		$("input[name='softVersionCheck']").each(function(i, el) {
			if($(this).attr("checked")){
				var flag=false;
				for (var j = 0; j < idArr.length - 1; j ++) {
					if (idArr[j]==$(this).val()) {
						flag=true;
						 break;
					}
				}
				if(!flag){
					idstr += $(this).val() + ",";
				}
				thisname += $(this).attr("title") + ",";
				count++;
				if( count%3==0){
					thisname = thisname + "<br>"
				}
				
			}
		});
		$("#choseSoftV").attr("value", idstr);
		if (thisname == "") {
			$("[name='" + fname+ "']").click();
		}
		else {
			
			if(thisname.substr(thisname.length-1,1)==","){
				thisname=thisname.substring(0, thisname.length-1);
			}else{
				thisname=thisname.substring(0, thisname.length-5);
			}
			
			var innerHTML=namestr + fname + "(" + thisname +");<br>"
			
			$("#devicecheckdev").attr("innerHTML", innerHTML);
			$("input[name='"+fname+"']").attr("checked",true);
		}
	}
	
	//�������汾�Ĵ���ʽ
	//���ɽ����ͨ
	function softVersionCLK4SDLT(obj)
	{
		var fname = $(obj).attr("fname");
		var softnames = $("#devicecheckdev").attr("innerHTML");
		var softids = $("#choseSoftV").attr("value");
		if(obj.checked){
			if(softnames == null || softnames == "" || softnames == undefined){
				var deviceSoftNames = fname + "(" + $(obj).attr("title") + ")";
				$("#devicecheckdev").attr("innerHTML", deviceSoftNames);
				$("#choseSoftV").attr("value", $(obj).val() + ",");
				var hardObj = $("input[name='"+fname+"']");
				if(!hardObj.is(":checked")){
					hardObj.attr("checked",true);
				}
			}else{
				var nameArr = softnames.split(";");
				var idArr = softids.split(",");
				var idstr = '';
				var namestr = '';

				for (var i = 0; i < nameArr.length; i++) {
					if (nameArr[i].indexOf(fname) == -1) {
						namestr += nameArr[i] + ";";
					}
					if (i == nameArr.length - 1 && namestr != "") {
						namestr = namestr.substring(0, namestr.length - 1);//ֻ��һ����ʱ��ȥ��ĩβ�ķֺ�;
					}
				}
				for (var j = 0; j < idArr.length - 1; j ++) {
					if (idArr[j].indexOf($(obj).attr("value")) == -1) {
						idstr += idArr[j] + ",";
					}
				}
				var thisname = "";
				$("input[name='softVersionCheck']").each(function(i, el) {
					if($(this).attr("checked")){
						if (softids.indexOf($(this).val()) == -1) {
							idstr += $(this).val() + ",";
						}
						thisname += $(this).attr("title") + ",";
					}
				});
				namestr = namestr == "" ? namestr : namestr + ";";
				$("#choseSoftV").attr("value", idstr);
				if (thisname == "") {
					$("[name='" + fname+ "']").click();
				}
				else {
					$("#devicecheckdev").attr("innerHTML", namestr + fname + "(" + thisname.substring(0, thisname.length-1)+")");
				}
			}
		}else{

			if(softnames == null || softnames == "" || softnames == undefined){
				var deviceSoftNames = fname + "(" + $(obj).attr("title") + ")";
				$("#devicecheckdev").attr("innerHTML", deviceSoftNames);
				$("#choseSoftV").attr("value", $(obj).val() + ",");
				var hardObj = $("input[name='"+fname+"']");
				if(!hardObj.is(":checked")){
					hardObj.attr("checked",true);
				}
			}else{
				var nameArr = softnames.split(";");
				
				var idArr = softids.split(",");
				
				var idstr = '';
				var namestr = '';

				for (var i = 0; i < nameArr.length; i++) {
					if (nameArr[i].indexOf(fname) == -1) {
						namestr += nameArr[i] + ";";
					}
					if (i == nameArr.length - 1 && namestr != "") {
						namestr = namestr.substring(0, namestr.length - 1);//ֻ��һ����ʱ��ȥ��ĩβ�ķֺ�;
					}
				}
				for (var j = 0; j < idArr.length - 1; j ++) {
					if (idArr[j].indexOf($(obj).attr("value")) == -1) {
						idstr += idArr[j] + ",";
					}
				}
				var thisname = "";
				$("input[name='softVersionCheck']").each(function(i, el) {
					if($(this).attr("checked")){
						if (softids.indexOf($(this).val()) == -1) {
							idstr += $(this).val() + ",";
						}
						thisname += $(this).attr("title") + ",";
					}
				});
				namestr = namestr == "" ? namestr : namestr + ";";
				$("#choseSoftV").attr("value", idstr);
				if (thisname == "") {
					$("[name='" + fname+ "']").click();
				}
				else {
					$("#devicecheckdev").attr("innerHTML", namestr + fname + "(" + thisname.substring(0, thisname.length-1)+")");
				}
			}
		}
		
	}
	
	function clkipCheck()
	{
		var ipch = document.getElementsByName("isIpcheck");
		if(ipch[0].checked)
		{
			$("tr[id*=setIPduan]").attr("style","display: none");
			$("button[name=addIPduan]").attr("style","display: none");  // �ֹ����IP��ַ�ΰ�ť
			
			if("jx_dx"==instAreaName){
				$("button[name=importIPduan]").attr("style","display: none"); // ��������IP��ַ�ΰ�ť				
			}
			$("input[@name='checkButtonValue']").attr("value","byHand");
		}
		if(ipch[1].checked)
		{
			$("tr[id*=setIPduan]").attr("style","display: inline");
			$("button[name=addIPduan]").attr("style","display: inline");  // �ֹ����IP��ַ�ΰ�ť
			
			if("jx_dx"==instAreaName){
				$("button[name=importIPduan]").attr("style","display: inline"); // ��������IP��ַ�ΰ�ť
			}
		}
	}
	
	function softdivcl()
	{
		$("div[@id='div_css']").hide();
	}
	
	function viewdetailTask(taskid,taskType)
	{
		var page = "<s:url value='/gtms/stb/resource/stbSoftUpgrade!queryTaskDetailById.action'/>?taskId="+taskid+"&taskType="+taskType;
		window.open(page,"","left=20,top=20,width=800,height=600,resizable=no,scrollbars=yes");
	}
	
	// �ֶ����IP��ַ��
	function addIPDuanFun()
	{
		if("jx_dx"==instAreaName){
			$("tr[id=importIPduan0]").attr("style","display: none"); // �����ļ��ϴ�
		}
		$("input[@name='checkButtonValue']").attr("value","byHand"); // �����ж� �ύ����ʱʹ�����ַ�ʽ�ύ
		
		var innerH = "<tr id='setIPduan" +ipDuanCount +"' style='display: inline'><td class='title_2' align='center' width='15%'>��ʼIP</td><td width='35%' ><input type='text' name='start_ip' maxlength='15'/></td><td class='title_2' align='center' width='15%'>��ֹIP</td><td width='35%'><input type='text' name='end_ip' maxlength='15'/></td></tr>";
		ipDuanCount++;
		$("#nodeAddIP").before(innerH);
	}
	
	function queryTask()
	{
		var querycityId = $("select[name=querycityId]").val();
		var queryvendorId = $("select[name=queryvendorId]").val();
		var queryVaild = $("select[name=queryVaild]").val();
		var queryTaskType = $("select[name=taskType]").val();
		var url = "<s:url value='/gtms/stb/resource/stbSoftUpgrade!init.action'/>?"
						+"queryCityId="+querycityId
						+"&queryVendorId="+queryvendorId
						+"&queryVaild="+queryVaild
						+"&taskType="+queryTaskType;
		if("hn_lt"==instAreaName){
			var showType=$("input[name=showType]").val();
			url+="&showType="+showType;
		}
	    window.location.href=url;
	}
	
	// ��������IP��ַ��
	function importIPDuanFun(){
		ipDuanCount = 1;
		$("input[@name='checkButtonValue']").attr("value","byBatchIP"); // �����ж� �ύ����ʱʹ�����ַ�ʽ�ύ
		$("tr[id*=setIPduan]").remove();
		$("tr[id=importIPduan0]").attr("style","display: inline");  // ��ʾ�ļ��ϴ�
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
			$("input[@name='checkMACButtonValue']").attr("value","byHandMAC");
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
				$("input[@name='checkCustomerButtonValue']").attr("value","byHandCum");
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
		if(null == ip.match(exp)){
			alert("IP��ַ��ʽ����ȷ");
		}else{
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
			if(ipArray[i].length==1){
				ipArray[i] = "00"+ipArray[i];
			}else if(ipArray[i].length==2){
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
	
	// ����excelģ��
	function toExport()
	{
		$("form[@name='batchexform']").attr("action","stbSoftUpgrade!downloadIPduanTemplate.action");
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
	
	
	var sxSoftCitys = "";
	function ChkAllCity(){
		if ($('#cityAll').attr('checked')) {
			sxSoftCitys = "";
			$("input:checkbox[name='city']").each(function() { // ����name=test�Ķ�ѡ��
				$(this).attr("checked",true);
				sxSoftCitys = sxSoftCitys+""+$(this).val()+",";
			});
		}
		else{
			sxSoftCitys = "";
			$("input:checkbox[name='city']").each(function() { // ����name=test�Ķ�ѡ��
				$(this).attr("checked",false);
			});
		}
		$("#sxSoftCitys").val(sxSoftCitys);
	}
	
	function ChkCity(){
		sxSoftCitys = "";
		var i = 0;
		$("input:checkbox[name='city']:checked").each(function() { // ����name=test�Ķ�ѡ��
			i++;
			sxSoftCitys = sxSoftCitys+""+$(this).val()+",";  // ÿһ����ѡ�����ֵ
		});
		if(i>=40){
			$("#cityAll").attr("checked",true);
		}
		$("#sxSoftCitys").val(sxSoftCitys);
	}
	
	function ChkTaskType(obj){
		var selVal = obj.value;
		var mywidth = document.body.clientWidth*0.85;
		if(selVal == 0){
			$("#task_type_city").show();
			$(".task_type_import").hide();
			$("#targetVersion").width(mywidth);
		}else{
			$("#task_type_city").hide();
			$(".task_type_import").show();
			$("#myiframe").width(mywidth);			
		}
	}
	
	function parse(){
		var vendorId = $("select[name='vendorId']").val();
		if(null == vendorId || vendorId == "" || vendorId == undefined || vendorId == '-1'){
			alert("����ѡ����");
			return;
		}
		
		var fileName = $("input[name='gwShare_fileName']").val();
		if(null == fileName || fileName == "" || fileName == undefined){
			alert("����ѡ���ļ��ϴ�");
			return;
		}
		
		$("#parse_rslt").show();
		$("#parse_rslt #parse_rslt_show").html("���ڷ����ļ��У����Ե�....");
		var url = "<s:url value='/gtms/stb/resource/stbSoftUpgrade!parseFile.action'/>";
	    $.post(url,{
	    	vendorId:vendorId,
	    	fileName : fileName
		},function(ajax){
			var s = ajax.split(":");
	    	if(s[0]=="1"){
	    		 var taskTempId = s[1];
	    		 var importType = s[2];
	    		 var num = s[3];
	    		 if(importType == "servAccount"){
	    			 $("input[name='custCheck']").val("1");
	    			 $("input[name='devsnCheck']").val("");
	    		 }else{
	    			 $("input[name='custCheck']").val("");
	    			 $("input[name='devsnCheck']").val("1");
	    		 }
	    		 $("#taskTempId").val(taskTempId);
	    		 $("#parse_rslt #parse_rslt_show").html("�����ļ������ɹ�,��ִ�е�����Ϊ:" + num);
	    	}else{ 
	    		$("#parse_rslt #parse_rslt_show").html("��Ǹ�������ļ�����ʧ��,ԭ��" + s[1]);
	    	}
	    });
	}
   
</SCRIPT>

</head>

<body>
	<TABLE width="98%" align="center" class="querytable">
		<TR>
			<TD>
				<IMG height=20 src="<s:url value='/images/bite_2.gif'/>" width=24>
				����ǰ��λ�ã��������԰汾�����������
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
		<input type="hidden" name="devsnCheck"   value="">
		<input type="hidden" name="btnValue4MAC"   value="">
		<input type="hidden" name="btnValue4IP"   value="">
		<input type="hidden" name="btnValue4Customer"   value="">
		<input type="hidden" name="sxSoftCitys" id="sxSoftCitys"  />
		<input type="hidden" name="taskId" id="taskTempId"  />
		<table class="querytable" width="98%" align="center">
			<tr>
				<td colspan="4" class="title_1">�������԰汾����������</td>
			</tr>
			<TR>
				<TD class="title_2" align="center" width="15%">����</TD>
				<TD width="85%" colspan="3">
					<s:select list="vendorList" name="vendorId" headerKey="-1"
						headerValue="��ѡ����" listKey="vendor_id" listValue="vendor_add"
						value="vendorId" cssClass="bk" onchange="vendorChange()"
						theme="simple">
					</s:select> <font color="red">*</font>
				</TD>
			</TR>
			 <%if("sx_lt".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))){%>
			 <TR>
				<TD class="title_2" align="center" width="15%">������ʽ</TD>
				<TD width="85%" colspan="3">
					 <INPUT type="radio" value="0" name="taskType" checked="true" onclick='ChkTaskType(this)'>����������
					 <INPUT type="radio" value="1" name="taskType" onclick='ChkTaskType(this)'>��������
				</TD>
			</TR>
			 <%} %>
			<TR id='task_type_city'>
				<TD class="title_2" align="center" width="15%">����</TD>
				<TD width="85%" colspan="3">
				<%if("sd_lt".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))) {%>
				  <div id="sd_lt_city_checkbox">
				   <s:iterator value="cityList" status='stat' > 
				     <s:if test="#stat.getCount()%7==0"><br/></s:if>
				     <input type="checkbox" value="<s:property  value='city_id'/>" name="input_cityId"><s:property  value='city_name'/>
				   </s:iterator>
				   <input type="hidden" name="cityId" id="hidden_input_cid">
				  </div>
				  <%}else if("sx_lt".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))){%>
				  	  <s:if test='cityId=="00"'>
				  	  <div id="cqsoftCity">
						<INPUT onclick="ChkAllCity()" type="checkbox" value="" id="cityAll">ȫѡ
						<INPUT type="checkbox" value="00" name="city" onclick="ChkCity()">ʡ����
						<INPUT type="checkbox" value="0355" name="city" onclick="ChkCity()">����
						<INPUT type="checkbox" value="0352" name="city" onclick="ChkCity()">��ͬ
						<INPUT type="checkbox" value="0356" name="city" onclick="ChkCity()">����
						<INPUT type="checkbox" value="0354" name="city" onclick="ChkCity()">����
						<INPUT type="checkbox" value="0357" name="city" onclick="ChkCity()">�ٷ�
						<INPUT type="checkbox" value="0358" name="city" onclick="ChkCity()">����
						<INPUT type="checkbox" value="0349" name="city" onclick="ChkCity()">˷��
						<INPUT type="checkbox" value="0351" name="city" onclick="ChkCity()">̫ԭ
						<INPUT type="checkbox" value="0350" name="city" onclick="ChkCity()">����
						<INPUT type="checkbox" value="0353" name="city" onclick="ChkCity()">��Ȫ
						<INPUT type="checkbox" value="0359" name="city" onclick="ChkCity()">�˳�
						</div>
						</s:if>
						<s:elseif test='cityId==""'>
						<INPUT type="text" name="city" onclick="ChkCity()">��ǰ�˺�û�з��ϵĵ���
						</s:elseif>
						<s:else>
						<INPUT type="checkbox" value='<s:property value="cityId"/>' name="city" onclick="ChkCity()"><s:property value="cituName"/>
						</s:else>
                  <%}else{ %>
                    <s:select list="cityList" name="cityId" headerKey="-1"
						headerValue="��ѡ������" listKey="city_id" listValue="city_name"
						value="cityId" cssClass="bk" theme="simple">
					</s:select>
                  <%} %>
				</TD>
			</TR>
		<%if("sx_lt".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))){%>
			<tr class="task_type_import" bgcolor="#FFFFFF"  style="display:none">
				<td align="right" width="15%">�ύ�ļ�</td>
				<td width="85%" colspan="3">
					<div id="importUsername">
						<iframe id="myiframe" name="gwShare_loadForm" FRAMEBORDER=0 SCROLLING=NO src="<s:url value="/gtms/stb/share/FileUploadForsxlt.jsp"/>" height="30" width="100%">
						</iframe>
						<%-- <iframe name="gwShare_loadForm" FRAMEBORDER=0 SCROLLING=NO src="<s:url value="/gtms/stb/share/FileUpload.jsp"/>" height="30" width="100%">
						</iframe>  --%>
						<input type="hidden" name=gwShare_fileName value=""/>
					</div>
				</td>
			</tr>
			<tr class="task_type_import" bgcolor="#FFFFFF" style="display:none">
					<td CLASS="green_foot" align="right">ע������</td>
					<td  CLASS="green_foot" colspan="3">
					 <div>
					    1����Ҫ������ļ���ʽ����Excel���ı��ļ�����txt��ʽ �� <br>
					    2���ļ��ĵ�һ��Ϊ�����У������û��˺š������ߡ��豸���кš���<br>
					    3���ļ�ֻ��һ�С� <br>
					    4���ļ�������Ҫ̫�࣬����Ӱ�����ܡ�
					 </div>
					</td>
			</tr>
			<tr id='parse_rslt' bgcolor="#FFFFFF" style="display:none">
			  <td CLASS="green_foot" align="right">�������</td>
			  <td id='parse_rslt_show' CLASS="green_foot" colspan="3" style='color:red'></td>
			</tr>
			<tr  class="task_type_import" bgcolor="#FFFFFF" style="display:none" >
			 <td CLASS="green_foot" align="right" colspan=3>
			   <input type='button' value=" ���� " class="jianbian" onclick="parse()"/>
			 </td>
			</tr>
			<%}%>
			<TR>
				<TD class="title_2" align="center" width="15%">Ŀ��汾</TD>
				<TD width="85%" colspan="3">
					<div id="targetVersion">��ѡ����</div>
				</TD>
			</TR>
			<tr>
				<td colspan="1" width="15%" align="center" class="title_2">Ҫ�����İ汾</td>
				<TD width="85%" colspan="3">
					<div id="devicetype">��ѡ����</div>
				</TD>
			</tr>
			<tr id="devicemodeliddiv" style="display: none">
				<td colspan="1" width="15%" align="center" class="title_2">����汾</td>
				<td width="85%" colspan="3">
					<input type="hidden" id="choseSoftV" value=""/>
					<div id="softVersion">
						<table>
							<tr><td>��ѡ������汾</td></tr>
							<tr><td id="softVcheckD"></td></tr>
						</table>
					</div>
				</td>
			</tr>
			<tr>
				<td colspan="1" width="15%" align="center" class="title_2">��ѡ��汾</td>
				<td colspan="3"><div id="devicecheckdev" width='85%'></div></td>
			</tr>
			
			<ms:inArea areaCode="hn_lt" notInMode="false">
			<tr>
				<td colspan="1" width="15%" align="center" class="title_2">�Ƿ�У����������</td>
				<td colspan="3">
					<select name='check_net' class='bk' style='width: 40px'>
						<option value='1'>��</option>
						<option value='0' selected>��</option>
					</select>
				</td>
			</tr>
			</ms:inArea>
			
			<ms:inArea areaCode="hn_lt" notInMode="true">
			<%if(!"hlj_dx".equals(LipossGlobals.getLipossProperty("InstArea.ShortName")) &&
					!"jx_dx".equals(LipossGlobals.getLipossProperty("InstArea.ShortName")) &&
					!"sd_lt".equals(LipossGlobals.getLipossProperty("InstArea.ShortName")) &&
					!"sx_lt".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))){ %>
			<tr id="ipAddrStart">
				<td class="title_2" align="center" width="15%">����IP��ַ��</td>
				<td width="85%" colspan="3">
					<input type="radio" checked name="isIpcheck" id="isIpcheck" value="0" checked="checked" onclick="clkipCheck()"/>��
					<input type="radio" name="isIpcheck" id="isIpcheck" value="1" onclick="clkipCheck()"/>��
					<button name="addIPduan" style="display:none" onclick="addIPDuanFun()">�ֶ����IP��ַ��</button>
					<ms:inArea areaCode="jx_dx">
					<button name="importIPduan" style="display:none" onclick="importIPDuanFun()">��������IP��ַ��</button>
					</ms:inArea>
					<input type="hidden" name="checkButtonValue" name="checkButtonValue" value="" />
				</td>
			</tr>
			<tr id="setIPduan0" style="display: none">
				<td class="title_2" align="center" width="15%">��ʼIP</td>
				<td width="35%" >
					<input type="text" name="start_ip" maxlength="15"/>
				</td>
				<td class="title_2" align="center" width="15%">��ֹIP</td>
				<td width="35%">
					<input type="text" name="end_ip" maxlength="15"/>
				</td>
			</tr>
			<%} %>
			<ms:inArea areaCode="jx_dx">
			<tr id="importIPduan0" style="display: none">
				<td class="title_2" align="center" width="15%">��ѡ���ļ�</td>
				<td width="85%" colspan="3">
					<s:file label="�ϴ�" theme="simple" name="upload"></s:file><font color="red">*</font>
					xls��ʽ�ĵ�
					<a href="javascript:void(0);" onclick="toExport();"><font color="red">����ģ��</font></a>
					<input type="hidden" name="uploadFileName" value=""/>
				</td>
			</tr>
			</ms:inArea>
			
			<%if(!"hlj_dx".equals(LipossGlobals.getLipossProperty("InstArea.ShortName")) &&
					!"jx_dx".equals(LipossGlobals.getLipossProperty("InstArea.ShortName")) &&
					!"nx_dx".equals(LipossGlobals.getLipossProperty("InstArea.ShortName")) &&
					!"sd_lt".equals(LipossGlobals.getLipossProperty("InstArea.ShortName")) &&
					!"sx_lt".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))){ %>
			<TR id="nodeAddIP">
				<TD class="title_2" align="center" width="15%">����MAC��ַ��</TD>
				<TD width="85%" colspan="3">
					<input type="radio" id="isOpenMAC" name="isOpenMAC" value="0" checked="checked" onclick="clkMACCheck();"/>��
					<input type="radio" id="isOpenMAC" name="isOpenMAC" value="1" onclick="clkMACCheck();"/>��
					<button name="addMAC" onclick="addMACAddress()"  style="display:none">�ֶ����MAC��ַ��</button>
					<button name="importMAC" onclick="batchAddMACAddress()"  style="display:none">��������MAC��ַ��</button>
					<input type="hidden" id="checkMACButtonValue" name="checkMACButtonValue" value="" />
				</TD>
			</TR>
			<tr id="importMACAdd" style="display: none">
				<td class="title_2" align="center" width="15%">��ѡ���ļ�</td>
				<td width="85%" colspan="3">
					<s:file label="�ϴ�" theme="simple" name="uploadMAC"></s:file><font color="red">*</font>
					xls��ʽ�ĵ�
					<a href="javascript:void(0);" onclick="toExportMAC();"><font color="red">����ģ��</font></a>
					<input type="hidden" name="uploadFileName4MAC" value=""/>
				</td>
			</tr>
				
			<TR id="addCustomer">
				<TD class="title_2" align="center" width="15%">ҵ���ʺ��б�</TD>
				<TD width="85%" colspan="3">
					<input type="radio" id="isAddCustomer" name="isAddCustomer" value="0" checked="checked" onclick="clkCustomerCheck();"/>��
					<input type="radio" id="isAddCustomer" name="isAddCustomer" value="1" onclick="clkCustomerCheck();"/>��
					<button name="addCustomerInfo" onclick="addCustomer()"  style="display:none">�ֶ�����ʺ���Ϣ</button>
					<button name="importCustomerInfo" onclick="batchAddCustomer()"  style="display:none">���������ʺ���Ϣ</button>
					<input type="hidden" id="checkCustomerButtonValue" name="checkCustomerButtonValue" value="" />
				</TD>
			</TR>
			<tr id="importCustomer" style="display: none">
				<td class="title_2" align="center" width="15%">��ѡ���ļ�</td>
				<td width="85%" colspan="3">
					<s:file label="�ϴ�" theme="simple" name="uploadCustomer"></s:file><font color="red">*</font>
					xls��ʽ�ĵ�
					<a href="javascript:void(0);" onclick="toExportCust();"><font color="red">����ģ��</font></a>
					<input type="hidden" name="uploadFileName4Customer" value=""/>
				</td>
			</tr>
			<%} %>
			</ms:inArea>
			<tr id="IPduanH">
				<TD class="title_2" align="center" width="15%">���Է�ʽ</td>
				<ms:inArea areaCode="hn_lt,sd_lt" notInMode="true">
				<TD width="85%" colspan="3">
					<SELECT name="strategyType" class="bk">
						<option value="4">������������ָ���������</option>
						<option value="4">�����¼�</option>
					</SELECT>
				</td>
				</ms:inArea>
				<ms:inArea areaCode="hn_lt,sd_lt" notInMode="false">
				<TD width="85%" colspan="3">
					<SELECT name="strategyType" class="bk">
						<option value="4">�������´�����</option>
					</SELECT>
				</td>
				</ms:inArea>
			</tr>
			<tr>
				<td colspan="4" class="foot" align="right">
					<div class="right">
						<!-- <button id="doButton" onclick="ExecMod()">����</button> -->
						<input type="button" class='bk'  id="doButton" onclick="ExecMod()" value='����'>
					</div>
				</td>
			</tr>
		</table>
		</s:form>
		
		<br>
		<br>
		<table width="98%" class="querytable" align="center">
			<tr>
			 <td class="title_2" align="center">����</td>
			 <td width="15%">
					<s:select list="cityList" name="querycityId" listKey="city_id" 
						listValue="city_name" value="queryCityId" cssClass="bk" theme="simple">
					</s:select>
			</td>
				<td class="title_2" align="center">����</td>
				<td width="15%">
					<s:select list="vendorList" name="queryvendorId" headerKey="-1"
						headerValue="ȫ��" listKey="vendor_id" listValue="vendor_add"
						value="queryVendorId" cssClass="bk" theme="simple">
					</s:select>
				</td>
				<td class="title_2" align="center">״̬</td>
				<td width="15%">
					<s:select name="queryVaild" value="queryVaild" list="#{'1':'�Ѽ���','0':'��ʧЧ'}" cssClass="bk" theme="simple" headerKey="-1" headerValue="ȫ��" >
					</s:select>
				</td>
				<%if(LipossGlobals.inArea("sx_lt")){ %>
				<td class="title_2" align="center">����</td>
				<td width="15%">
				    <s:select name="taskType" value="taskType" list="#{'1':'����������ѯ','0':'����������ѯ'}" cssClass="bk" theme="simple" headerKey="-1" headerValue="ȫ��" >
					</s:select>
				</td>
				<%} %>
				<td class="title_2" align="center"><button onclick="queryTask()">��ѯ</button></td>
			</tr>
		</table>
		
		<input type="hidden" name="showType" value="<s:property value='showType' />">
		<table width="98%" class="listtable" align="center">
			<thead>
				<tr>
				    <%if(LipossGlobals.inArea("sx_lt")){ %>
				     <!-- ֻ�а�����������ʱ�����Ҫ��ʾ������Ϣ�������̵���������ʱ��û��������Ϣ -->
                      <s:if test="taskType != 1">
					   <th align="center" width="10%">����</th>
					  </s:if>
					<%}else{ %>
					 <th align="center" width="10%">����</th>
					<%}%>
					<th align="center" width="10%">����</th>
					<th align="center" width="15%">Ŀ��汾</th>
					<th align="center" width="10%">������</th>
					<th align="center" width="10%">����ʱ��</th>
					<th align="center" width="10%">����ʱ��</th>
					<th align="center" width="10%">״̬</th>
					<th align="center" width="15%">����</th>
				</tr>
			</thead>
			<s:if test="tasklist!=null && tasklist.size()>0">
				<tbody>
					<s:iterator value="tasklist">
						<tr>
						    <%if(LipossGlobals.inArea("sx_lt")){ %>
						     <s:if test="taskType != 1">
						      <td align="center"><s:property value="city_name" /></td>
						     </s:if>
							<%}else{ %>
							  <td align="center"><s:property value="city_name" /></td>
							<%} %>
							<td align="center"><s:property value="vendor_add" /></td>
							<td align="center"><s:property value="softwareversion" /></td>
							<td align="center"><s:property value="acc_loginname" /></td>
							<td align="center"><s:property value="record_time" /></td>
							<td align="center"><s:property value="update_time" /></td>
							<s:if test='valid=="1"'>
								<td align="center">�Ѽ���</td>
								<td align="center">
									<s:if test='showType!=1 && (accoid==acc_oid||areaId=="1")'>
										<button name="cancerButton"
											onclick="javascript:cancerTask('<s:property value="task_id" />','<s:property value="version_path" />','<s:property value="softwareversion" />')">
											ʧЧ
										</button>
										<s:if test="showType!=2">
											<button name="cancerButton"
												onclick="javascript:deleteTask('<s:property value="task_id" />','<s:property value="version_path" />','<s:property value="softwareversion" />')">
												ɾ��
											</button>
										</s:if>
									</s:if>
									<button name="viewButton"
										onclick="javascript:viewTask('<s:property value="task_id"/>','<s:property value="import_type"/>','<s:property value="type"/>')">
										�鿴���
									</button>
									<button name="viewDetailButton"
										onclick="javascript:viewdetailTask('<s:property value="task_id"/>','<s:property value="type"/>')">
										�鿴��ϸ
									</button>
								</td>
							</s:if>
							<s:else>
								<td align="center">��ʧЧ</td>
								<td align="center">
									<s:if test='showType!=1 && (accoid==acc_oid||areaId=="1")'>
										<button name="cancerButton"
											onclick="javascript:validTask('<s:property value="task_id" />','<s:property value="version_path" />','<s:property value="softwareversion" />')">
											����
										</button>
										<s:if test="showType!=2">
											<button name="cancerButton"
												onclick="javascript:deleteTask('<s:property value="task_id" />','<s:property value="version_path" />','<s:property value="softwareversion" />')">
												ɾ��
											</button>
										</s:if>
									</s:if>
									<button name="viewButton"
										onclick="javascript:viewTask('<s:property value="task_id"/>','<s:property value="import_type"/>','<s:property value="type"/>')">
										�鿴���
									</button>
									<button name="viewDetailButton"
										onclick="javascript:viewdetailTask('<s:property value="task_id"/>','<s:property value="type"/>')">
										�鿴��ϸ
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
							<font color="red">û�ж��Ƶ�����</font>
						</td>
					</tr>
				</tbody>
			</s:else>
		</table>
		
		<div id="divDetail"
			style="position: absolute; z-index: 255; top: 200px; left: 350px; width: 40%; border-width: 1; border-style: ridge; background-color: #eeeeee; padding-top: 10px; display: none"></div>
		<div id="div_css" style="position: absolute; z-index: 255; top: 200px; left: 300px; width: 55%; border-width: 1; border-style: ridge; background-color: #eeeeee; padding-top: 10px; display: none">
			<table class="querytable" align="center" width="100%" style="table-layout: fixed;text-align:left !important" >
				<%if("sd_lt".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))){%>
				 <tr>
				   <td width="120px" class="title_2" align="left" colspan=2>��ѡ������汾�ͺ�</td>
				 </tr>
				 <tr>
				   <td width="120px" class="title_2" align="left" colspan=2>
				    <input type="checkbox" checked="checked" name="all_options" onclick="allCheckSel(true)">ȫѡ&nbsp;&nbsp;
				    <input type="checkbox" name="all_options_no" onclick="allCheckSel(false)">ȫ��ѡ
				    <!-- ��ŵ�ǰ�������Ӳ���汾 -->
					<input type="hidden" name="currSelVersion" value="" >
				   </td>
				 </tr>
				  <tr>
					<td id="softVershow" class="title_2" style="text-align:left !important" colspan=2></td>
				</tr>
				<%}else{%>
				  <tr>
					<td width="130px" class="title_2" align="center">��ѡ������汾�ͺ�</td>
					<td id="softVershow" class="title_2" align="center"></td>
				</tr>
				<%}%>
				<tr>
					<td colspan="2" class="title_2" align="center">
						<button align="center" name="softdivtbn" onclick="softdivcl()">
						�ر�
						</button>
					</td>
				</tr>
			</table>
		</div>
	</body>
<script type="text/javascript">

function allSel(opt){
	 if(opt){
		$("input[name='all_options_no']").attr("checked","");
		 $("input[name='all_options']").attr("checked","checked");
	 }else{
		 $("input[name='all_options_no']").attr("checked","checked");
		 $("input[name='all_options']").attr("checked","");
		 $("input[name='softVersionCheck']").attr("disabled","");
	 }
}

function allCheckSel(opt){
	 if(opt){
		$("input[name='all_options_no']").attr("checked","");
		$("input[name='softVersionCheck']").attr("checked","checked");
		$("input[name='softVersionCheck']").attr("disabled","");
		
		//ȫѡ��ʱ��
		var currentClickHard = $("input[name='currSelVersion']").val();
		if(!$("input[name='"+currentClickHard+"']")[0].checked){
			$("input[name='"+currentClickHard+"']").attr("checked","checked");
			hardVersion_bak($("input[name='"+currentClickHard+"']")[0]);
		} 
	 }else{
		 $("input[name='all_options']").attr("checked","");
		 $("input[name='softVersionCheck']").attr("checked","");
		 //$("input[name='softVersionCheck']").attr("disabled",true);
		 //ȫ��ѡ
		 var currentClickHard = $("input[name='currSelVersion']").val();
			if($("input[name='"+currentClickHard+"']")[0].checked){
				$("input[name='"+currentClickHard+"']").attr("checked","");
				hardVersion_bak($("input[name='"+currentClickHard+"']")[0]);
			} 
	 }
  }
 
function hardVersion_bak(obj)
{
	var tempMV = obj.value.split("#");
	if(obj.checked)
	{
		//����ѡ���Ӳ���汾������汾��ʽΪ��B600v2(ker,ker);B700()
		var choseYStr = obj.name + "(";
		var chosedYStr = document.getElementById("devicecheckdev").innerHTML;
		var choseID = "";
		var tempHtml = "";
		for(var i=0;i<tempMV.length;i++)
		{
			var tempIDK = tempMV[i].split("$");
			//��װӲ���汾������汾
			choseYStr = choseYStr + tempIDK[1] + ",";
			if(i%5 == 0){
				choseYStr = choseYStr + "<br>"
			}
			//��װ�ͺ�ID
			choseID = choseID + tempIDK[0] + ",";
		} 
		//�豸Ӳ���ͺ�
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
		//�����ͺ�ID
		document.getElementById("choseSoftV").value += choseID;
	}
	//ȡ����ʱ��
	else
	{
		//ɾ�������Ӳ���汾������汾
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
		//ɾ��������ͺ�ID
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
	}
}

/* $(document).ready(function(){
	//var selVal = $("input:radio[name='taskType']:checked").val();
	$("input:radio[name='taskType'][value='0']").attr("checked",true);
	$("#task_type_city").show();
	$(".task_type_import").hide();
}); */

</script>