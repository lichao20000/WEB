<%@ include file="../../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ include file="../head.jsp"%>
<%@ include file="../../toolbar.jsp"%> 
<%@taglib prefix="lk" uri="/linkage"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>光功率采集阀值设定</title>
<style type="text/css">
	.inputNoBorder{
		border-style: none;
		font-size: 9pt;
		color: #4B505F;
		text-decoration: none;
		font-weight: bold;
		line-height: 14px;
		text-align: center;
		background-color: #E1EEEE;
	}
	.addInput{
		background-color: #E1EEEE;
		text-align: center;
	}
</style>
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript">
    var vendorId = null;
    var vendorName = null;
    var txPower = null;
    var rxPower = null;
	function selVendor(oselect){
		vendorId = oselect.value;
		vendorName = oselect.options[oselect.selectedIndex].text;
		vendorName = URLDecoder.decode(vendorName,"GBK");
	}
	function focusVendor(oselect){
		vendorId = oselect.value;
		if(vendorId == "-1"){
			alert("请选择厂商！");
		}
	}
	function getVal1(txInput){
		txPower = txInput.value;
		if(txPower == ""){
			alert("接受光功率不能为空!");
			return;
		}else{
			if(isNaN(txPower)){
				alert("请输入的接受光功率不是数字，请重新输入!");
				return;
			}
		}
		
	}
	function getVal2(rxInput){
		rxPower = rxInput.value;
		if(rxPower == ""){
			alert("采集光功率不能为空!");
			return;
		}else{
			if(isNaN(rxPower)){
				alert("请输入的采集光功率不是数字，请重新输入!");
				return;
			}
		}
		
	}
	//1.编辑保存2.新增保存
	function operRow(cellObj,operType,vendor_id,tx_power,rx_power){
		//获取保存按钮所在的行对象
	    var trObj = cellObj.parentNode.parentNode;   
	 	var url = "<s:url value='/itms/resource/VersionQuery!operDroop.action'/>";
	 	if(operType == 1){
	 		for (i = 2;i < 4;i++){
		 		//依次将单元格中的文本框样式
				trObj.cells[i].firstChild.className = "inputNoBorder";
				trObj.cells[i].firstChild.readOnly = true ;
			}
			$.post(url,{
		 		operType:operType,
		 		vendor_id:vendor_id,
		 		tx_power:txPower,
		 		rx_power:rxPower
			},function(){});
	 	}else{
	 		if(vendorId == "-1" || vendorId == null){
	 			alert("请选择厂商！");
	 			return;
	 		}else{
			 	var tx_power = $("#tx_power").val();
			 	var rx_power = $("#rx_power").val(); 
			 	if(tx_power == "" || tx_power == null){
			 		alert("请输入发送光功率!");
			 		return false;
			 	}else if(rx_power == "" || rx_power == null){
			 		alert("请输入接受光功率!");
			 		return false;
			 	}else{
			 		for (i = 1;i < 4;i++){
				 		//依次将单元格中的文本框样式
						trObj.cells[i].firstChild.className = "inputNoBorder";
						trObj.cells[i].firstChild.readOnly = true ;
					}
			 		$.post(url,{
				 		operType:operType,
				 		vendor_id:vendorId,
				 		vendor_name:vendorName,
				 		tx_power:tx_power,
				 		rx_power:rx_power
					},function(){
					});
				 	if(vendor_id != "-1"){
				 		window.location.reload();
				 	}
			 	}
	 		}
	 	}
		cellObj.value = "编辑";
		//cellObj.setAttribute("onclick","editRow(this)");
		cellObj.onclick= function (){
			editRow(this);
		}
	}
	function editRow(cellObj,vendor_id,tx_power,rx_power){
		var trObj = cellObj.parentNode.parentNode;
	 	for (var i = 2;i < 4;i++){
			trObj.cells[i].firstChild.readOnly = false;
			trObj.cells[i].firstChild.className = "";
			trObj.cells[i].firstChild.value = "";
		}
		cellObj.value = "保存";
		//cellObj.setAttribute("onclick","saveRow(this)");
		cellObj.onclick= function (){
			operRow(this,1,vendor_id,tx_power,rx_power);
		}
	}
	function addRow(tableID){
		   var addTable = document.getElementById(tableID);
		   //返回表格现有行数
		   var rowNums = addTable.rows.length;
		   var count = rowNums - 2;
		   //插入新行
		   var newRow = addTable.insertRow(rowNums-1);
		   var col1 = newRow.insertCell(0);
		   col1.className = "inputNoBorder";
		   col1.innerHTML = count;
		   var col2 = newRow.insertCell(1);
		   col2.innerHTML = $("#vendorList").html();
		   col2.className = "inputNoBorder";
		   var col3 = newRow.insertCell(2);
		   col3.className = "inputNoBorder";
		   col3.innerHTML = "<input id='tx_power' name='tx_power' type='text' value='' onblur='getVal1(this);'/>";
		   var col4 = newRow.insertCell(3);
		   col4.className = "inputNoBorder";
		   col4.innerHTML = "<input id='rx_power' name='rx_power' type='text' value='' onblur='getVal2(this);'/>";
		   var col5 = newRow.insertCell(4);
		   col5.className = "addInput";
		   col5.innerHTML = "<input name='save' type='button' value='保存' onclick='operRow(this,2)'/>  <input name='del' type='button' value='删除' onclick=\"delRow('droop',this)\" />"  ;
	}
	function delRow(tableID,cellObj,vendor_id){
		if (confirm("确定要删除该设备光功率吗？")){
			var trObj = cellObj.parentNode.parentNode;
		 	document.getElementById(tableID).deleteRow(trObj.rowIndex);
		 	//var vendor_id = $.trim($("input[@name='vendorId']").val());
		 	var url = "<s:url value='/itms/resource/VersionQuery!delDroop.action'/>";
		 		$.post(url,{
			 		vendor_id:vendor_id
				},function(){
				});
	 	}
	}
</script>
</head>
<body>
	<div id="vendorList" style="display: none;"><s:select list="vendorList" name="vendor_id" headerKey="-1" headerValue="请选择厂商" 
		listKey="vendor_id" listValue="vendorName" value="vendor_id" onchange="selVendor(this);" onblur="focusVendor(this);" theme="simple"/></div>
	<table border="0" cellspacing="0" cellpadding="0" width="100%" >
		<tr>
			<td height="20">&nbsp;</td>
		</tr>
		<tr>
			<td>
				<table width="98%" height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd" align="center">
					<tr>
						<td width="162" align="center" class="title_bigwhite">
							光功率设定
						</td>
						<td>
							<img src="../../images/attention_2.gif" width="15" height="12">
							带'<font color="#FF0000">*</font>'的表单必须填写或选择
						</td>
					</tr>	
				</table>
			</td>
		</tr>
		<tr>
			<td>
	   			<form name="frm" method="post" action="" onsubmit="return checkForm()">
					<table width="98%" border="0" cellspacing="0" cellpadding="0" align="center">
						<tr>
							<td bgcolor="#999999">
								<table border="0" cellspacing="1" cellpadding="2" width="100%" id="droop">
									<tr>
										<th bgcolor="#ffffff" colspan="5" >光功率采集阀值设定表</th>
									</tr>
									<tr class="green_title2">
										<td width="20%">序号</td>
										<td width="20%">厂家</td>
										<td width="20%">发送光功率阀值</td>
										<td width="20%">接受光功率阀值</td>
										<td width="20%">操作</td>
									</tr>
									<s:if test="list.size()>0">
										<s:iterator value="list" status="status">
											<tr id="QueryData" class="green_title2">
												<td>
													<input class="inputNoBorder" name="seriId" type="text" value="<s:property value='#status.count'/>" readonly="readonly"/>
												</td>
												<td>
													<input type="hidden" name="vendorId" value="<s:property value='vendor_id'/>"/>
													<input class="inputNoBorder" name="vendor_name" type="text" value="<s:property value='vendor_name' />" readonly="readonly"/>
												</td>
												<td>
													<input id="txPower" class="inputNoBorder" name="tx_power" type="text" value="<s:property value='tx_power' />" readonly="readonly" onblur="getVal1(this);" />
												</td>
												<td>
													<input id="rxPower" class="inputNoBorder" name="rx_power" type="text" value="<s:property value='rx_power' />" readonly="readonly" onblur="getVal2(this);" />
												</td>
												<td>
													<input type="button" value="编辑" onclick="editRow(this,'<s:property value="vendor_id" />','<s:property value="tx_power" />','<s:property value="rx_power" />')" />
													<input type="button" value="删除" onclick="delRow('droop',this,'<s:property value="vendor_id" />')" />
												</td>
											</tr>
										</s:iterator>
									</s:if>
									<tr id="add">
										<th align="center" class="green_foot">
											<a href='javascript:addRow("droop");'>增加+</a>
										</th>
										<th class="green_foot">
										</th>
										<th class="green_foot">
										</th>
										<th class="green_foot">
										</th>
										<th class="green_foot">
										</th> 
									</tr>
								</table>
							</td>
						</tr>
					</table>
				</form>
			</td>
		</tr>
	</table>
</body>
</html>
<%@ include file="../../foot.jsp"%>