<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=GBK">
		<link href="<s:url value="/css2/global.css"/>" rel="stylesheet"
			type="text/css">
		<link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet"
			type="text/css">
		<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
		type="text/css">
		<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
		<lk:res />
<SCRIPT LANGUAGE="JavaScript">
//ϵͳ����
    //����
	function ExecMod(){
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
			alert("��ѡ������汾!");
			return false;
		}
	    if(deviceTypeIds.length>0){
	    	$("input[@name='deviceTypeIds']").attr("value",deviceTypeIds.substring(0,deviceTypeIds.length-1));
	    }
	}

	function softdivcl()
	{
		$("div[@id='div_css']").hide();
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

	<br>
		<s:form action="openDeviceShowPic!importConfig.action" method="post"  onsubmit="">
			<input type="hidden" name="deviceModelIds" value=""/>
			<input type="hidden" name="deviceTypeIds"   value="">
			<table class="querytable" width="98%" align="center">
				<tr>
					<td colspan="4" class="title_1" align="left">
						ѡ������汾
					</td>
				</tr>
				<TR>
					<TD class="title_2" align="center" width="20%">
						����
					</TD>
					<TD width="30%">
						<s:select list="vendorList" name="vendorId" headerKey="-1"
							headerValue="����" listKey="vendor_id" listValue="vendor_add"
							value="vendorId" cssClass="bk" onchange="getDeviceModel();"
							theme="simple">
					</s:select>
					</TD>
					<TD class="title_2" align="center" width="20%">
						�ͺ�
					</TD>
					<TD width="30%" >
						<select  name="deviceModelIds"  cssClass="bk"  id="deviceModelIds" >
						<option value="-1">����</option>
						</select>
					</TD>
				</TR>
				<TR>
					<TD class="title_2" align="center" width="20%">
						Ӳ���汾
					</TD>
					<TD width="30%">
						<input type='text' maxlength="100" id="hardVersion" />
					</TD>
					<TD class="title_2" align="center" width="20%">
						����汾
					</TD>
					<TD width="30%" >
						<input type='text' maxlength="100" id="softVersion" />
						&nbsp;<font color="#FF0000">֧�ֺ�ƥ��</font>
					</TD>
				</TR>
				<TR>
					<TD class="title_2" align="center" width="20%">
						�Ƿ�֧�ֿ������
					</TD>
					<TD width="30%">
						<select disabled="disabled" name="bootadv">
						<option value="1">֧��</option>
						</select>
					</TD>
					<TD width="50%" colspan="2" align="right">
					<input type="button" value="��ѯ" onclick="searchEdition()"/>
					</TD>
				</TR>
			</table>
		</s:form>

		<%-- <table class="listtable" width="98%" align="center">
	<!--  <caption>
				�汾�ļ�·������
			</caption>-->
	<thead>
		<tr>
			<th align="center" width="25%">����</th>
			<th align="center" width="25%">����</th>
			<th align="center" width="25%">�����ͺ�</th>
			<th align="center" width="25%">Ӳ��</th>
			<th align="center" width="25%">���</th>
		</tr>
	</thead>
	<s:set var="gwType" value='<s:property value="gwType" />' />
	<tbody>
		<s:iterator value="deviceList">
			<tr bgcolor="#FFFFFF">
				<td><s:property value="devicetype_id" /></td>
				<td><s:property value="vendor_add" /></td>
				<td><s:property value="device_model" /></td>
				<td><s:property value="hardwareversion" /></td>
				<td><s:property value="softwareversion" /></td>
			</tr>
		</s:iterator>
	</tbody>
	<tfoot>
		<tr bgcolor="#FFFFFF">
			<td colspan="7" align="right"><lk:pages
				url="/gtms/stb/resource/deviceVersion.action" styleClass="" showType=""
				isGoTo="true" changeNum="true" /></td>
		</tr>

	</tfoot>
</table> --%>



<FORM NAME="mainForm" id="mainForm" METHOD="post" ACTION=""
			target="dataForm">
		</FORM>
		<!-- չʾ���part -->
		<TABLE width="98%" border=0 cellspacing=0 cellpadding=0 align="center">
			<TR>
				<TD bgcolor=#999999 id="idData"><iframe id="dataForm"
					name="dataForm" height="0" frameborder="0" scrolling="no"
					width="100%" src=""></iframe></TD>
			</TR>
		</TABLE>
		<br>

		<div>
		<table width="100%"><tr><td align="center">
		<table class="listtable" id="editionTable" width="98%">
		<thead>
		<tr>
			<th align="center" width="10%">����</th>
			<th align="center" width="15%">����</th>
			<th align="center" width="25%">�����ͺ�</th>
			<th align="center" width="25%">Ӳ��</th>
			<th align="center" width="25%">���</th>
		</tr>
		</thead>
		<tbody id="editionTbody">

		</tbody>
		<tfoot>
		<tr bgcolor="#FFFFFF">
			<td colspan="5" align="right">
			<input type="button" value="ȡ��" onclick="cancleAddEdition()"/>&nbsp;
			<input type="button" value="ȷ��" onclick="confirmAddEdition()"/>
			</td>
		</tr>
		</tfoot>
		</table>
		</td>
		</tr>
		</table>
		</div>

	</body>
<script type="text/javascript">

//** iframe�Զ���Ӧҳ�� **//
//������ϣ������ҳ��߶��Զ������߶ȵ�iframe�����Ƶ��б�
//�ö��Ű�ÿ��iframe��ID�ָ�. ����: ["myframe1", "myframe2"]������ֻ��һ�����壬���ö��š�
//����iframe��ID
var iframeids=["dataForm"]

//����û����������֧��iframe�Ƿ�iframe���� yes ��ʾ���أ�no��ʾ������
var iframehide="yes"

function dyniframesize()
{
	var dyniframe=new Array()
	for (i=0; i<iframeids.length; i++)
	{
		if (document.getElementById)
		{
			//�Զ�����iframe�߶�
			dyniframe[dyniframe.length] = document.getElementById(iframeids[i]);
			if (dyniframe[i] && !window.opera)
			{
   			dyniframe[i].style.display="block"
   			//����û����������NetScape
   			if (dyniframe[i].contentDocument && dyniframe[i].contentDocument.body.offsetHeight)
    				dyniframe[i].height = dyniframe[i].contentDocument.body.offsetHeight;
    			//����û����������IE
   			else if (dyniframe[i].Document && dyniframe[i].Document.body.scrollHeight)
    				dyniframe[i].height = dyniframe[i].Document.body.scrollHeight;
 			 }
 		}
		//�����趨�Ĳ���������֧��iframe�����������ʾ����
		if ((document.all || document.getElementById) && iframehide=="no")
		{
			var tempobj=document.all? document.all[iframeids[i]] : document.getElementById(iframeids[i])
  		tempobj.style.display="block"
		}
	}
}

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

 // window.onload = getDeviceModel;
  function Init(){
		var form = document.getElementById("mainForm");
		form.action = "<s:url value='/gtms/stb/resource/stbDeviceTypeInfo!editionList.action?bootadv=1'/>";
		form.submit();
	}

	    $(function(){
	    	getDeviceModel();
	    	Init();
	    	dyniframesize();
	    	toHidden();
	    });

	function toHidden(){
		$("#editionTable").css("display","none");
	}


   function getDeviceModel(){
		var vendorId = $("select[@name='vendorId']").val();
		var url = "<s:url value='/gtms/stb/resource/openDeviceShowPic!getDeviceModel.action'/>";

		$.post(url,{
			vendorId:vendorId
			},function(ajax){
				if(ajax!=""){
					var lineData = ajax.split("#");
					if(typeof(lineData)&&typeof(lineData.length)){
						$("select[@id='deviceModelIds']").empty();
						var optionValue = "<option value='-1' >����</option>  ";
						$("select[@id='deviceModelIds']").append(optionValue);
						for(var i=0;i<lineData.length;i++){
							var oneElement = lineData[i].split("$");
							var xValue = oneElement[0];
							var xText = oneElement[1];
							var optionValue = "<option value='"+xValue+"' >"+xText+"</option>  ";
							$("select[@id='deviceModelIds']").append(optionValue);
						}
					}else{
						$("select[@id='deviceModelIds']").empty();
						var optionValue = "<option value='-2' >�ó���û�ж�Ӧ�ͺţ�</option>  ";
						$("select[@id='deviceModelIds']").append(optionValue);
					}
				}else{
					$("select[@id='deviceModelIds']").empty();
					var optionValue = "<option value='-2' >�ó���û�ж�Ӧ�ͺţ�</option>  ";
					$("select[@id='deviceModelIds']").append(optionValue);
				}
			});
	}

   /**
	**����������Ϣ�������ð汾
	**/

	//ȫ��trim
	function trimAll()
	{
		var inputs = document.getElementsByTagName("input");
		for(var i=0;i<inputs.length;i++)
		{
			var input = inputs[i];
			if(/text/gi.test(input.type))
			{
				input.value = trim(input.value);
			}
		}
	}

   function searchEdition(){
	   trimAll();

	   var vendor = $("select[@name='vendorId']").val();
	   var device_model = $("select[@id='deviceModelIds']").val();
	   var hard_version = $("input[@id='hardVersion']").val();
	   var soft_version = $("input[@id='softVersion']").val();
	   var bootadv = $("select[@name='bootadv']").val();

		var form = document.getElementById("mainForm");
		form.action = "<s:url value="/gtms/stb/resource/stbDeviceTypeInfo!editionList.action"/>?gw_type=4&vendor=" + vendor + "&device_model=" + device_model + "&hard_version="+hard_version+ "&soft_version="+soft_version+ "&bootadv="+bootadv;
		form.submit();


	  // curPage_splitPage, num_splitPage,startTime,endTime;
	  // window.location.href="<s:url value='/gtms/stb/resource/stbDeviceTypeInfo!editionList.action'/>?vendor=" + vendor + "&device_model=" + device_model + "&hard_version="+hard_version+ "&soft_version="+soft_version+ "&bootadv="+bootadv;
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
		var url = "<s:url value='/gtms/stb/resource/openDeviceShowPic!getSoftVersion.action'/>";
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


	function cancleAddEdition(){
		if(confirm("ȷ��ȡ��?")){
			var tableT = document.getElementById("editionTable");
			var rows = tableT.rows.length;
			for(var i=1 ; i<rows-1 ; i++){
				tableT.deleteRow(1);
			}
			//window.dataForm.closeWin();
			window.close();
		}
	}

	function confirmAddEdition(){
		var tableT = document.getElementById("editionTable");
		var rows = tableT.rows.length;
		if(rows <= 2){
			alert("��������ð汾");
			return;
		}

		if(confirm("ȷ�������Щ�汾?")){
			var arr_vendor_id = new Array();
			var arr_devicetype_id = new Array();
			var arr_device_model = new Array();

			for(var i=1 ; i<rows-1 ; i++){
				var content = tableT.rows[i].cells[1].id;
				var data = content.split("##");

				arr_devicetype_id[i-1] = data[0] ;
				arr_vendor_id[i-1] = data[1] ;
				arr_device_model[i-1] = data[2] ;
			}


			for(var i=1 ; i<rows-1 ; i++){
				var vendor_id_i = arr_vendor_id[i-1];
				for(var j=1 ; j<rows-1 ; j++){
					if(i != j){
						var vendor_id_j = arr_vendor_id[j-1];
						if(vendor_id_i == vendor_id_j && vendor_id_i != "" && vendor_id_j != ""){
							arr_device_model[i-1] += (","+arr_device_model[j-1]);
							arr_devicetype_id[i-1] += (","+arr_devicetype_id[j-1]);
							arr_vendor_id[j-1] = "";

						}
					}
				}
			}

			var arr_vendor_idN = new Array();
			var arr_devicetype_idN = new Array();
			var arr_device_modelN = new Array();
			var count = 0;

			for(var i=0 ; i<arr_vendor_id.length ; i++){
				var vendor_id_i = arr_vendor_id[i];
				if(vendor_id_i != ""){
					arr_vendor_idN[count] = vendor_id_i;
					arr_devicetype_idN[count] = arr_devicetype_id[i];
					arr_device_modelN[count] = arr_device_model[i];
					count++;
				}
			}
			//alert(arr_device_modelN);

			for(var i=0 ; i<arr_device_modelN.length ; i++){
				var flagCheck = false;
				var dataT = arr_device_modelN[i];
				var str = "";
				if(dataT.length > 1 && dataT.indexOf(",") != -1){
					var data = dataT.split(",");
					for(var j=0 ; j<data.length ; j++){
						for(var k=0 ; k<data.length ; k++){
							if(j != k){
								if(data[j] == data[k]){
									flagCheck = true;
									data[j] = "";
								}
							}
						}
						if(data[j] != ""){
							str += (data[j]+",");
						}
					}
				}
				if(str != "" && flagCheck == true){
					str = str.substring(0,str.length-1);
					arr_device_modelN[i] = str;
				}
			}

			//alert(arr_device_modelN);

			var devicetype_id_str = "";
			var vendor_id_str = "";
			var device_model_id_str = "";

			for(var i=0 ; i<arr_vendor_idN.length ; i++){
				if(i > 0){
					vendor_id_str += ("#"+arr_vendor_idN[i]);
					devicetype_id_str += ("#"+arr_devicetype_idN[i]);
					device_model_id_str += ("#"+arr_device_modelN[i]);
				}else{
					vendor_id_str += (arr_vendor_idN[i]);
					devicetype_id_str += (arr_devicetype_idN[i]);
					device_model_id_str += (arr_device_modelN[i]);
				}
			}

			//<input type="hidden" name="vendorId" value=""/>
			//<input type="hidden" name="deviceModelIds" value=""/>
			//<input type="hidden" name="deviceTypeIds"  value=""/>

			window.opener.document.getElementById("vendorId").value = vendor_id_str;
			window.opener.document.getElementById("deviceModelIds").value = device_model_id_str;
			window.opener.document.getElementById("deviceTypeIds").value = devicetype_id_str;

			window.close();
		}
	}

</script>
