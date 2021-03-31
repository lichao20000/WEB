<%@  page  language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>设备版本查询</title>
<%
	/**
	 * 
	 * 
	 * @author 
	 * @version 1.0
	 * @since 2010-09-08
	 * @category
	 */
%>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript"
	src="<s:url value="/Js/jQuerySplitPage-linkage.js"/>"></script>

<script type="text/javascript">
	
	$(function(){
		parent.dyniframesize();
	});
	
	
	
//编辑设备类型 chenxj " />'
	function editDevice(device_type_id,vendor_id,device_model_id,specversion,hardwareversion,softwareversion,is_check,zeroconf,bootadv)
	{     
 	 if(is_check=="" ){
		  is_check=-2;
  	}

	window.parent.document.getElementsByName("vendor_add")[0].value=vendor_id;
	parent.change_model('deviceModel',device_model_id);
	window.parent.document.getElementsByName("speversion")[0].value=specversion;
	window.parent.document.getElementsByName("hard_version_add")[0].value=hardwareversion;	
	window.parent.document.getElementsByName("soft_version_add")[0].value=softwareversion;	
	window.parent.document.getElementsByName("is_check_add")[0].value=is_check;
	window.parent.document.getElementById("updateId").value=device_type_id;
	
	if(zeroconf == 1){
		window.parent.document.getElementsByName("machineConfig_add")[0].checked = "checked";
	}else{
		window.parent.document.getElementsByName("machineConfig_add")[1].checked = "checked";
	}
	
	if(bootadv == 1){
		window.parent.document.getElementsByName("bootadv")[0].checked = "checked";
	}else{
		window.parent.document.getElementsByName("bootadv")[1].checked = "checked";
	}
	
	
	parent.showAddPart(true);
	
}

	//全选
	function selectAllEdition(checkOBJ){
		if(checkOBJ.checked){
			 var elementsNotChecked = $("input[name='edition']:not(:checked)");
			 //$("input[@name='edition']").attr("checked",true); 
			 for(var i=0 ; i<elementsNotChecked.length ; i++){
				 var element = elementsNotChecked[i];
				 element.click();
			 }
		}
		else{
			//$("input[@name='edition']").attr("checked",false);
			var elementsChecked = $("[name='edition'][checked]");
			 for(var i=0 ; i<elementsChecked.length ; i++){
				 var element = elementsChecked[i];
				 element.click();
			 }
		}
	}
	
	
	
	function selectEdition(checkOBJ,devicetype_id,vendor_add,device_model,hardwareversion,softwareversion,vendor_id,device_model_id){
		
		if(!checkOBJ.checked){
				$("[name='allEdition'][checked]").attr("checked",false);
				var tableT = window.parent.document.getElementById("editionTable"); 
				var rows = tableT.rows.length;
				
				for(var i=1 ; i<rows-1 ; i++){
					var elementId = tableT.rows[i].cells[0].id;
					if(elementId == devicetype_id){
						tableT.deleteRow(i);
					}
				}
				rows = tableT.rows.length;
				if(rows <= 2){
					window.parent.document.getElementById("editionTable").style.display="none"; 
				}
		}else{
			var reFlag = true;
			var tableT = window.parent.document.getElementById("editionTable"); 
			var rows = tableT.rows.length;
			
			for(var i=1 ; i<rows ; i++){
				var elementId = tableT.rows[i].cells[0].id;
				if(elementId == devicetype_id){
					reFlag = false;
				}
			}
			
			if(reFlag){	
				 var tr = window.parent.document.createElement("tr");
			
				 var td_devicetype_id = window.parent.document.createElement("td");
				 td_devicetype_id.innerHTML = "<a onclick='window.parent.dataForm.deleteByDeviceTypeId("+devicetype_id+")'>删除</a>"; 
				 td_devicetype_id.id = devicetype_id; 
				 tr.appendChild(td_devicetype_id); 
				 
				 var td_vendor_add = window.parent.document.createElement("td");
				 td_vendor_add.innerHTML = vendor_add;
				 td_vendor_add.id = devicetype_id +"##"+ vendor_id +"##"+ device_model_id;
				 tr.appendChild(td_vendor_add);
				 
				 var td_device_model = window.parent.document.createElement("td");
				 td_device_model.innerHTML = device_model;
				 tr.appendChild(td_device_model);
				 
				 var td_hardwareversion = window.parent.document.createElement("td");
				 td_hardwareversion.innerHTML = hardwareversion;
				 tr.appendChild(td_hardwareversion);
				 
				 var td_softwareversion = window.parent.document.createElement("td");
				 td_softwareversion.innerHTML = softwareversion;
				 tr.appendChild(td_softwareversion);
				 
				 var tbody = window.parent.document.getElementById("editionTbody");
				 tbody.appendChild(tr);
			}
			if(rows >= 1){
				window.parent.document.getElementById("editionTable").style.display="block"; 
			}
		}
	}
	
	function deleteByDeviceTypeId(devicetype_id){
		var tableT = window.parent.document.getElementById("editionTable"); 
		var rows = tableT.rows.length;
		
		for(var i=1 ; i<rows-1 ; i++){
			var elementId = tableT.rows[i].cells[0].id;
			if(elementId == devicetype_id){
				tableT.deleteRow(i);
				var elements = $("[name='edition'][checked]");
				for(var j=0 ; j<elements.length ; j++){
					var elementId = elements[j].id;
					if(elementId.indexOf("#") != -1 && (elementId.split("#")[0]) == devicetype_id){
						if(elements[j].checked){
							elements[j].checked=false;
							$("[name='allEdition']").attr("checked",false);
						}
					}
				}
			}
		}
		rows = tableT.rows.length;
		if(rows <=2 ){
			window.parent.document.getElementById("editionTable").style.display="none"; 
		}
	}


</script>

</head>

<body>
<table class="listtable" id="tableC">
	<caption>查询结果</caption>
	<thead>
		<tr>
			<th align="center" width="10%"><input type='checkbox' name='allEdition' onclick='javascript:selectAllEdition(this)'></th>
			<th align="center" width="15%">厂商</th>
			<th align="center" width="25%">适用型号</th>
			<th align="center" width="25%">硬件</th>
			<th align="center" width="25%">软件</th>
		</tr>
	</thead>
	<tbody>
		<s:if test="deviceList!=null">
			<s:if test="deviceList.size()>0">
				<s:iterator value="deviceList">
					<tr bgcolor="#FFFFFF">
				<td><input type='checkbox' name='edition' 
				id='<s:property value="devicetype_id" />#<s:property value="vendor_add" />#<s:property value="device_model" />#<s:property value="hardwareversion" />#<s:property value="softwareversion" />'
				onclick="javascript:selectEdition(this,'<s:property value="devicetype_id" />',
														'<s:property value="vendor_add" />',
														'<s:property value="device_model" />',
														'<s:property value="hardwareversion" />',
														'<s:property value="softwareversion" />',
														'<s:property value="vendor_id" />',
														'<s:property value="device_model_id" />')"></td>
				<td><s:property value="vendor_add" /></td>
				<td><s:property value="device_model" /></td>
				<td><s:property value="hardwareversion" /></td>
				<td><s:property value="softwareversion" /></td>
			</tr>
				</s:iterator>
			</s:if>
			<s:else>
				<tr>
					<td colspan=11>没有查询到相关设备！</td>
				</tr>
			</s:else>
		</s:if>
		<s:else>
			<tr>
				<td colspan=11>没有查询到相关设备！</td>
			</tr>
		</s:else>
	</tbody>

	<tfoot>
		<tr>
			<td colspan="11" align="right"><lk:pages
				url="/gtms/stb/resource/stbDeviceTypeInfo!editionList.action?bootadv=1" styleClass=""
				showType="" isGoTo="true" changeNum="true" /></td>
		</tr>
	</tfoot>

</table>
<br>



</body>
<script>


  function detailDevice(devicetype_id,specId){
   window.open("<s:url value='/itms/resource/deviceTypeInfo!queryDetail.action'/>?deviceTypeId="+devicetype_id+"&detailSpecId="+specId,"","left=20,top=20,height=450,width=700,resizable=yes scrollbars=yes,status=yes,toolbar=no,menubar=no,location=no");
    }


function editDeviceOld(device_type_id,vendor_id,device_model_id,
				   specversion,hardwareversion,softwareversion,
				   is_check,rela_dev_type_id,type_id,ip_type,is_normal,spec_id)
{     
  if(is_check=="" ){
	  is_check=-2;
	 
  }
  if(rela_dev_type_id=="" ){
	  rela_dev_type_id=-1;
  }

	window.parent.document.getElementsByName("vendor_add")[0].value=vendor_id;
	parent.change_model('deviceModel',device_model_id);
	window.parent.document.getElementsByName("speversion")[0].value=specversion;
	window.parent.document.getElementsByName("hard_version_add")[0].value=hardwareversion;	
	window.parent.document.getElementsByName("soft_version_add")[0].value=softwareversion;	
	window.parent.document.getElementsByName("is_check_add")[0].value=is_check;	
    window.parent.document.getElementsByName("rela_dev_type_add")[0].value=rela_dev_type_id;	
    window.parent.document.getElementsByName("specId")[0].value=spec_id;	
	window.parent.document.getElementById("updateId").value=device_type_id;
	if(ip_type==0){
		 window.parent.document.getElementsByName("ipType")[0].checked="checked";
	}else{
		window.parent.document.getElementsByName("ipType")[1].checked="checked";
	}
	if(is_normal==1){
		window.parent.document.getElementsByName("isNormal")[0].checked="checked";
	}else
		window.parent.document.getElementsByName("isNormal")[1].checked="checked";
	window.parent.document.getElementById("actLabel").innerHTML="编辑";
	parent.queryTypeName(type_id);
	parent.disableLabel(true);
	parent.showAddPart(true);
		
}




function delDevice(id,gw_type)
{
	if(!delWarn())
		return;
	var url = "<s:url value="/itms/resource/deviceTypeInfo!deleteDevice.action"/>";
	
	$.post(url,{
		deleteID:id,
		gw_type:gw_type
	},function(ajax){
		var result = parseInt(ajax);
		if(result == 0)
		{
			alert("该设备版本下有相关设备，不允许删除！");
			return;
		}
		if(result > 0)
		{
			alert("设备型号删除成功");
			
			// 普通方式提交
			var form = window.parent.document.getElementById("mainForm");
			form.action = "<s:url value="/itms/resource/deviceTypeInfo!queryList.action"/>";
			parent.reset();
            //form.target = "dataForm";
			form.submit();
		}
		else
		{
			alert("设备型号删除失败！");
		}
	});
}

function delWarn(){
	if(confirm("真的要删除该设备版本吗？\n本操作所删除的不能恢复！！！")){
		return true;
	}
	else{
		return false;
	}
}

function checkDevice(id)
{
	var url = "<s:url value="/itms/resource/deviceTypeInfo!updateIsCheck.action"/>";
	$.post(url,{
		deviceTypeId:id
	},function(ajax){
		var result = parseInt(ajax);
		if(result > 0)
		{
			alert("审核设备成功");
			
			// 普通方式提交
			var form = window.parent.document.getElementById("mainForm");
			form.action = "<s:url value="/itms/resource/deviceTypeInfo!queryList.action"/>";
			//form.target = "dataForm";
			form.submit();
		}
		else
		{
			alert("审核设备失败");
		}
	});
}

/*
function closeWin(){
	window.open("about:blank","_self").close();
}
*/
</script>
</html>