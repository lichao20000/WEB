<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage"%>
<%@taglib prefix="ms" uri="/ailk-itms-web-tags"%>
<%@ page import="com.linkage.litms.LipossGlobals" %>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=GBK">
		<link href="<s:url value="/css2/global.css"/>" rel="stylesheet"
			type="text/css">
		<link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet"
			type="text/css">
		<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
		<SCRIPT LANGUAGE="JavaScript">
		
		//ϵͳ����
		var instAreaName = '<%= LipossGlobals.getLipossProperty("InstArea.ShortName")%>';
		
	$(function(){
		
	});
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
					/**var lineData = ajax.split("#");
					if(typeof(lineData)&&typeof(lineData.length)){
						
						for(var i=0;i<lineData.length;i++){
							var oneElement = lineData[i].split("$");
							var xValue = oneElement[0];
							var xText = oneElement[1];
							obj.add(new Option("=="+xText+"==",xValue));
						}*/
					//modify by wufei begin
					var lineData = ajax.split("|");
					var innerHV = "";
					if(typeof(lineData)&&typeof(lineData.length)){
						//����Ӳ���汾�ͺŷ���
						for(var i=0;i<lineData.length;i++){
							var oneElement = lineData[i].split('\1');
							innerHV += "<input type=checkbox name='" + oneElement[0] +"' value='"+ oneElement[1] + "' onclick='hardVersionCLK(this)' />" + oneElement[0];
						}
						
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
	function ExecMod(){
		
		var vendorId = $("select[@name='vendorId']").val();
		var cityId = $("select[@name='cityId']").val();
		var pathId = $("select[@name='pathId']").val();
		var strategyType = $("select[@name='strategyType']").val();
		var taskDesc = $("input[@name='taskDesc']").val();
		if(vendorId=="-1"){
			alert("��ѡ���̣�");
			return;
		}
		if(cityId=="-1"){
			alert("��ѡ�����أ�");
			return;
		}
		if(pathId==undefined||pathId=="-1"){
			alert("��ѡ��Ŀ��汾!");
			return;
		}
        var isCheckPass = true; 
        var str ="";
        str = $("#choseSoftV").attr("value");
		if(str==""||str=="-1"){
			alert("��ѡ��Ҫ�����İ汾!");
			return;
		}
		if(taskDesc==""){
			alert("������������Ϊ��!");
			return;
		}
		if(isCheckPass)
		{
			str = str.substring(0,str.length-1);
			$("input[@name='devicetypeId']").attr("value",str);
			$("#doButton").attr("disabled",true);
			$("form[@name='batchexform']").attr("action","stbSoftUpgrade!batchSoftUpgradeForHblt.action");
			$("form[@name='batchexform']").submit();
		}
	}
	
	
	function cancerTask(taskId,version_path,softwareversion){
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
	    		alert("ʧЧ����ʧ�ܣ�");
	    	}
	    });
	}
	
	function validTask(taskId,version_path,softwareversion){
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
	    		alert("��������ʧ�ܣ�");
	    	}
	    });
	}
	
	function deleteTask(taskId,version_path,softwareversion){
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
	    		alert("ɾ������ʧ�ܣ�");
	    	}
	    });
	}
	function viewTask(taskId){ 
		$("div[@id='divDetail']").show();
	    $("div[@id='divDetail']").html("���ڲ�ѯ�����Ե�....");
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
				//��װ�ͺ�ID
				choseID = choseID + tempIDK[0] + ",";
				//���ɶ�ѡ��
				tempHtml = tempHtml + "<input type=checkbox checked=true name='" + tempIDK[1] + "' value='" + tempIDK[0] + "' title='" + obj.name + "' onclick='softVersionCLK(this)' />" + tempIDK[1];
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
			//���ö�ѡ���б�
			$("div[@id='div_css']").show();
			$("td[@id=softVershow]").html("");
			$("td[@id=softVershow]").append(tempHtml);
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
			//���ö�ѡ���б�
			var tempHtml = "";
			for(var i=0;i<tempMV.length;i++)
			{
				var tempIDK = tempMV[i].split("$");
				//���ɶ�ѡ��
				tempHtml = tempHtml + "<input type=checkbox disabled name='" + tempIDK[1] + "' value='" + tempIDK[0] + "' onclick='softVersionCLK(this)' />" + tempIDK[1];
			}
			$("div[@id='div_css']").show();
			$("td[@id=softVershow]").html("");
			$("td[@id=softVershow]").append(tempHtml);
		}
	}
	//�������汾�Ĵ���ʽ
	function softVersionCLK(obj)
	{
		//alert();
		var chosedSV = $("#devicecheckdev").attr("innerHTML");
		var chosedID = $("#choseSoftV").val();
		var hardxh = obj.title;
		var tempArray = chosedSV.split(";");
		var chosedSV1= "";
		//�ж��Ƿ�ѡ��
		if(obj.checked)
		{
			//����Ӳ������汾��ʾ
			var tempHtml = "";
			for(var i=0;i<tempArray.length;i++)
			{
				if(obj.title == tempArray[i].substring(0,obj.title.length))
				{
					var re = new RegExp(obj.name);
					if(null == re.exec(tempArray[i]))
					{
						tempHtml = tempArray[i].substring(0,tempArray[i].length -1) + "," + obj.name + ")";
					}
					else
					{
						tempHtml = tempArray[i];
					}
					chosedSV1 += tempHtml + ";"
				}
				else
				{
					chosedSV1 += tempArray[i] + ";"
				}
			}
			$("#devicecheckdev").attr("innerHTML",chosedSV1.substring(0,chosedSV1.length-1));
			//����ѡ�������汾
			var re = new RegExp(obj.value);
			if(null == re.exec(chosedID))
			{
				$("#choseSoftV").attr("value",chosedID + obj.value +",");
			}
		}
		else
		{
			//����Ӳ������汾��ʾ
			var tempHtml = "";
			for(var i=0;i<tempArray.length;i++)
			{
				if(obj.title == tempArray[i].substring(0,obj.title.length))
				{
					var re = new RegExp(obj.name);
					if(null == re.exec(tempArray[i]))
					{
						tempHtml = tempArray[i];
					}
					else
					{
						var reret = re.exec(tempArray[i]);
						tempHtml = tempArray[i].substring(0,reret.index) + tempArray[i].substring(reret.lastIndex+1,tempArray[i].length);
					}
					if(tempHtml.length -1 != obj.title.length)
					{
						chosedSV1 += tempHtml + ";";
					}
					else
					{
						$("input[name=" + obj.title +"]").click();
					}
				}
				else
				{
					chosedSV1 += tempArray[i] + ";";
				}
			}
			$("#devicecheckdev").attr("innerHTML",chosedSV1.substring(0,chosedSV1.length-1));
			//����ѡ�������汾
			var re = new RegExp(obj.value);
			var reret = re.exec(chosedID);
			if(null != reret)
			{
				$("#choseSoftV").attr("value",chosedID.substring(0,reret.index) + chosedID.substring(reret.lastIndex + 1,chosedID.length));
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
	
	function queryTask()
	{
		var querycityId = $("select[name=querycityId]").val();
		var queryvendorId = $("select[name=queryvendorId]").val();
		var queryVaild = $("select[name=queryVaild]").val();
		var url = "<s:url value='/gtms/stb/resource/stbSoftUpgrade!init.action'/>?queryCityId=" + querycityId+"&queryVendorId="+queryvendorId+"&queryVaild="+queryVaild;
	    window.location.href=url;
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
					����ǰ��λ�ã��������԰汾�����������
				</TD>
			</TR>
		</TABLE>
		<br>

		<s:form action="" method="post" enctype="multipart/form-data" name="batchexform">
			<input type="hidden" name="macSG" value="">
			<input type="hidden" name="ipSG"   value="">
			<input type="hidden" name="custSG"   value="">
			<input type="hidden" name="ipCheck"  value="0">
			<input type="hidden" name="custCheck"  value="">
			<input type="hidden" name="macCheck"   value="">
			<input type="hidden" name="btnValue4MAC"   value="">
			<input type="hidden" name="btnValue4IP"   value="">
			<input type="hidden" name="btnValue4Customer"   value="">
			<input type="hidden" name="devicetypeId"   value="">
		<table class="querytable" width="98%" align="center">
			<tr>
				<td colspan="4" class="title_1">
					�������԰汾����������
				</td>
			</tr>
			<TR>
				<TD class="title_2" align="center" width="15%">
					����
				</TD>
				<TD width="85%" colspan="3">
					<s:select list="vendorList" name="vendorId" headerKey="-1"
						headerValue="��ѡ����" listKey="vendor_id" listValue="vendor_add"
						value="vendorId" cssClass="bk" onchange="vendorChange()"
						theme="simple"></s:select>
					<font color="red">*</font>
				</TD>
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
					Ŀ��汾
				</TD>
				<TD width="85%" colspan="3">
					<div id="targetVersion">
						��ѡ����
					</div>
				</TD>
			</TR>
			<tr>
				<td colspan="1" width="15%" align="center" class="title_2">
					Ҫ�����İ汾
				</td>
				<TD width="85%" colspan="3">
					<div id="devicetype">
						��ѡ����
					</div>
				</TD>
			</tr>
			<tr id="devicemodeliddiv" style="display: none">
				<td colspan="1" width="15%" align="center" class="title_2">
					����汾
				</td>
				<td width="85%" colspan="3">
					<input type="hidden" id="choseSoftV" value=""/>
					<div id="softVersion">
						<table>
							<tr>
								<td>
									��ѡ������汾
								</td>
							</tr>
							<tr>
								<td id="softVcheckD">
								</td>
							</tr>
						</table>
					</div>
				</td>
			</tr>
			<tr>
				<td colspan="1" width="15%" align="center" class="title_2">
					��ѡ��汾
				</td>
				<td width="85%" colspan="3">
					<span id="devicecheckdev">
					</span>
				</td>
			</tr>
			<tr>
				<td class="title_2" align="center" width="15%">
					����IP��ַ��
				</td>
				<td width="85%" colspan="3">
					<input type="radio" name="isOpenIp" value="0" checked="checked" onclick="clkipCheck()" disabled="disabled"/>��
					<input type="radio" name="isOpenIp" value="1" onclick="clkipCheck()" disabled="disabled"/>��
				</td>
			</tr>
			
			<tr id="IPduanH">
				<TD class="title_2" align="center" width="15%">
					��������
				</td>
				<TD width="85%" colspan="3">
					<input type="text" name="taskDesc" class="bk" value=""/>
				</td>
			</tr>
			<tr id="IPduanH">
				<TD class="title_2" align="center" width="15%">
					���Է�ʽ
				</td>
				<TD width="85%" colspan="3">
					<SELECT name="strategyType" class="bk">
						<option value="4">
							�������´�����
						</option>
					</SELECT>
				</td>
			</tr>
			<tr>
				<td colspan="4" class="foot" align="right">
					<div class="right">
						<button id="doButton" onclick="ExecMod()">
							����
						</button>
					</div>
				</td>
			</tr>
		</table>
		</s:form>
		
		<br>
		<br>
		<table width="98%" class="querytable" align="center" style="display: none">
			<tr>
				<td class="title_2" align="center">����</td>
				<td width="15%">
					<s:select list="cityList" name="querycityId" listKey="city_id" listValue="city_name"
						value="queryCityId" cssClass="bk" theme="simple"></s:select>
				</td>
				<td class="title_2" align="center">����</td>
				<td width="15%">
					<s:select list="vendorList" name="queryvendorId" headerKey="-1"
						headerValue="ȫ��" listKey="vendor_id" listValue="vendor_add"
						value="queryVendorId" cssClass="bk" theme="simple"></s:select>
				</td>
				<td class="title_2" align="center">״̬</td>
				<td width="15%">
					<s:select name="queryVaild" value="queryVaild" list="#{'1':'�Ѽ���','0':'��ʧЧ'}" cssClass="bk" theme="simple" headerKey="-1" headerValue="ȫ��" >
						<!--  <option value="-1">All</option>
						<option value="1">��Ч</option>
						<option value="0">ʧЧ</option>-->
					</s:select>
				</td>
				<td class="title_2" align="center"><button onclick="queryTask()">��ѯ</button></td>
				
			</tr>
		</table>
		<table width="98%" class="listtable" align="center" style="display: none">
			<thead>
				<tr>
					<th align="center" width="10%">
						����
					</th>
					<th align="center" width="10%">
						����
					</th>
					<th align="center" width="15%">
						Ŀ��汾
					</th>
					<th align="center" width="10%">
						������
					</th>
					<th align="center" width="10%">
						����ʱ��
					</th>
					<th align="center" width="10%">
						����ʱ��
					</th>
					<th align="center" width="10%">
						״̬
					</th>
					<th align="center" width="15%">
						����
					</th>
				</tr>
			</thead>
			<s:if test="tasklist!=null">
				<s:if test="tasklist.size()>0">
					<tbody>
						<s:iterator value="tasklist">
							<tr>
								<td align="center">
									<s:property value="city_name" />
								</td>
								<td align="center">
									<s:property value="vendor_add" />
								</td>
								<td align="center">
									<s:property value="softwareversion" />
								</td>
								<td align="center">
									<s:property value="acc_loginname" />
								</td>
								<td align="center">
									<s:property value="record_time" />
								</td>
								<td align="center">
									<s:property value="update_time" />
								</td>
								<s:if test='valid=="1"'>
									<td align="center">
										�Ѽ���
									</td>
									<td align="center">
										<s:if test='accoid==acc_oid||areaId=="1"'>
											<button name="cancerButton"
												onclick="javascript:cancerTask('<s:property value="task_id" />','<s:property value="version_path" />','<s:property value="softwareversion" />')">
												ʧЧ
											</button>
											<button name="cancerButton"
												onclick="javascript:deleteTask('<s:property value="task_id" />','<s:property value="version_path" />','<s:property value="softwareversion" />')">
												ɾ��
											</button>
										</s:if>
										<button name="viewButton"
											onclick="javascript:viewTask('<s:property value="task_id"/>')">
											�鿴���
										</button>
										<button name="viewDetailButton"
											onclick="javascript:viewdetailTask('<s:property value="task_id"/>')">
											�鿴��ϸ
										</button>
									</td>
								</s:if>
								<s:else>
									<td align="center">
										��ʧЧ
									</td>
									<td align="center">
										<s:if test='accoid==acc_oid||areaId=="1"'>
											<button name="cancerButton"
												onclick="javascript:validTask('<s:property value="task_id" />','<s:property value="version_path" />','<s:property value="softwareversion" />')">
												����
											</button>
											<button name="cancerButton"
												onclick="javascript:deleteTask('<s:property value="task_id" />','<s:property value="version_path" />','<s:property value="softwareversion" />')">
												ɾ��
											</button>
										</s:if>
										<button name="viewButton"
											onclick="javascript:viewTask('<s:property value="task_id"/>')">
											�鿴���
										</button>
										<button name="viewDetailButton"
											onclick="javascript:viewdetailTask('<s:property value="task_id"/>')">
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
		<div id="div_css" style="position: absolute; z-index: 255; top: 200px; left: 350px; width: 40%; border-width: 1; border-style: ridge; background-color: #eeeeee; padding-top: 10px; display: none">
			<table class="querytable" align="center" width="100%">
				<tr>
					<td width="120px" class="title_2" align="center">
						��ѡ������汾�ͺ�
					</td>
					<td id="softVershow" class="title_2" align="center">
					</td>
				</tr>
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