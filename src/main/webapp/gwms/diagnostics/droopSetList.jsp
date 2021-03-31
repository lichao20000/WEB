<%@ include file="../../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ include file="../head.jsp"%>
<%@ include file="../../toolbar.jsp"%> 
<%@taglib prefix="lk" uri="/linkage"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>�⹦�ʲɼ���ֵ�趨</title>
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
			alert("��ѡ���̣�");
		}
	}
	function getVal1(txInput){
		txPower = txInput.value;
		if(txPower == ""){
			alert("���ܹ⹦�ʲ���Ϊ��!");
			return;
		}else{
			if(isNaN(txPower)){
				alert("������Ľ��ܹ⹦�ʲ������֣�����������!");
				return;
			}
		}
		
	}
	function getVal2(rxInput){
		rxPower = rxInput.value;
		if(rxPower == ""){
			alert("�ɼ��⹦�ʲ���Ϊ��!");
			return;
		}else{
			if(isNaN(rxPower)){
				alert("������Ĳɼ��⹦�ʲ������֣�����������!");
				return;
			}
		}
		
	}
	//1.�༭����2.��������
	function operRow(cellObj,operType,vendor_id,tx_power,rx_power){
		//��ȡ���水ť���ڵ��ж���
	    var trObj = cellObj.parentNode.parentNode;   
	 	var url = "<s:url value='/itms/resource/VersionQuery!operDroop.action'/>";
	 	if(operType == 1){
	 		for (i = 2;i < 4;i++){
		 		//���ν���Ԫ���е��ı�����ʽ
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
	 			alert("��ѡ���̣�");
	 			return;
	 		}else{
			 	var tx_power = $("#tx_power").val();
			 	var rx_power = $("#rx_power").val(); 
			 	if(tx_power == "" || tx_power == null){
			 		alert("�����뷢�͹⹦��!");
			 		return false;
			 	}else if(rx_power == "" || rx_power == null){
			 		alert("��������ܹ⹦��!");
			 		return false;
			 	}else{
			 		for (i = 1;i < 4;i++){
				 		//���ν���Ԫ���е��ı�����ʽ
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
		cellObj.value = "�༭";
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
		cellObj.value = "����";
		//cellObj.setAttribute("onclick","saveRow(this)");
		cellObj.onclick= function (){
			operRow(this,1,vendor_id,tx_power,rx_power);
		}
	}
	function addRow(tableID){
		   var addTable = document.getElementById(tableID);
		   //���ر����������
		   var rowNums = addTable.rows.length;
		   var count = rowNums - 2;
		   //��������
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
		   col5.innerHTML = "<input name='save' type='button' value='����' onclick='operRow(this,2)'/>  <input name='del' type='button' value='ɾ��' onclick=\"delRow('droop',this)\" />"  ;
	}
	function delRow(tableID,cellObj,vendor_id){
		if (confirm("ȷ��Ҫɾ�����豸�⹦����")){
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
	<div id="vendorList" style="display: none;"><s:select list="vendorList" name="vendor_id" headerKey="-1" headerValue="��ѡ����" 
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
							�⹦���趨
						</td>
						<td>
							<img src="../../images/attention_2.gif" width="15" height="12">
							��'<font color="#FF0000">*</font>'�ı�������д��ѡ��
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
										<th bgcolor="#ffffff" colspan="5" >�⹦�ʲɼ���ֵ�趨��</th>
									</tr>
									<tr class="green_title2">
										<td width="20%">���</td>
										<td width="20%">����</td>
										<td width="20%">���͹⹦�ʷ�ֵ</td>
										<td width="20%">���ܹ⹦�ʷ�ֵ</td>
										<td width="20%">����</td>
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
													<input type="button" value="�༭" onclick="editRow(this,'<s:property value="vendor_id" />','<s:property value="tx_power" />','<s:property value="rx_power" />')" />
													<input type="button" value="ɾ��" onclick="delRow('droop',this,'<s:property value="vendor_id" />')" />
												</td>
											</tr>
										</s:iterator>
									</s:if>
									<tr id="add">
										<th align="center" class="green_foot">
											<a href='javascript:addRow("droop");'>����+</a>
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