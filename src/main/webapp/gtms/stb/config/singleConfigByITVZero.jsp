<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="../../../timelater.jsp"%>
<%@ page contentType="text/html; charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ include file="../../../head.jsp"%>
<%
 String flag = request.getParameter("flag");
 %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>ITV��̨���������ڵ�</title>
<link href="<s:url value="/css/css_green.css"/>" rel="stylesheet" type="text/css" />
<link href="<s:url value="/css/listview.css"/>" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/CheckForm.js"/>"></script>
</head>
<body>
	<table border="0" cellspacing="0" cellpadding="0" width="100%">
		<tr>
			<td HEIGHT="20">
				&nbsp;
			</td>
		</tr>
		<tr>
			<td>
				<table width="98%" border="0" align="center" cellpadding="0" cellspacing="0" class="text">
					<tr>
						<td>
							<table width="100%" height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
								<tr>
									<td width="162" align="left" class="title_bigwhite" nowrap>
										&nbsp;ITV��̨���������ڵ�
									</td>
								</tr>
							</table>
						</td>
					</tr>
					<tr bgcolor="#FFFFFF">
						<td colspan="4">
							<%@ include file="../share/gwShareDeviceQuery_ITV.jsp"%>
							
						</td>
					</tr>
					<tr bgcolor="#FFFFFF">
						<td>
							<input type="hidden" name="param" value="" />
							<table id="table_showConfig" width="100%" border="0" cellspacing="0" cellpadding="0" align="center" class="querytable">
								<tr>
									<th  align="center" width="100%">
											��  ��   ��  ��
									</th>
								</tr>
								<tr bgcolor="#FFFFFF">
									<td colspan="4">
										<div id="selectedDev">
											���ѯ�豸��
										</div>
									</td>
								</tr>
					<TR id="trDeviceResult" style="display: none">
					<td>
					<table border="0px">
					<tr>
					<td class=title_2 width="25%">
							�豸���к�
						</td>
						<td id="tdDeviceSn" width="25%">
						</td>
						<td class=title_2 width="25%">
							����
						</td>
						<td id="tdDeviceCityName" width="25%">
						</td>
					</tr>
					</table>
					</td>
					</tr>
								<tr>
									<td bgcolor="#999999">
										<table border="0" cellspacing="1" cellpadding="2" width="100%">
											<tr bgcolor="#FFFFFF">
												<td nowrap align="right" class="column" width="20%">
													�����ڵ�·��
												</td>
												<td width="70%" colspan="3" >
													<input type="text" id="paramNodePath0" name="paramNodePath0" style="width:800px;"> 
												</td>
											</tr>
											<tr bgcolor="#FFFFFF">
												<td align="right" class="column" width="20%">
													����ֵ
												</td>
												<td  width="30%" >
													<input type="text" id="paramValue0" name="paramValue0" value=""/>
												</td>
												<td  align="right" class="column" width="30%">
													��������
												</td>
												<td  width="70%" >
													<select name="paramType0" id="paramType0" class="bk">
													 	<option value="-1">==��ѡ��==</option>
													 	<option value="1">string</option>
													 	<option value="2">int</option>
													 	<option value="3">unsignedInt</option>
													 	<option value="4">boolean</option>
													 </select>
												</td>
											</tr>
											<tr bgcolor="#FFFFFF">
												<td nowrap align="right" class="column" width="20%">
													�����ڵ�·��
												</td>
												<td width="70%" colspan="3" >
													<input type="text" id="paramNodePath1" name="paramNodePath1" style="width:800px;"> 
												</td>
											</tr>
											<tr bgcolor="#FFFFFF">
												<td align="right" class="column" width="20%">
													����ֵ
												</td>
												<td  width="30%" >
													<input type="text" id="paramValue1" name="paramValue1" value=""/>
												</td>
												<td  align="right" class="column" width="30%">
													��������
												</td>
												<td  width="70%" >
													<select name="paramType1" id="paramType1" class="bk">
													 	<option value="-1">==��ѡ��==</option>
													 	<option value="1">string</option>
													 	<option value="2">int</option>
													 	<option value="3">unsignedInt</option>
													 	<option value="4">boolean</option>
													 </select>
												</td>
											</tr>
											<tr bgcolor="#FFFFFF">
												<td nowrap align="right" class="column" width="20%">
													�����ڵ�·��
												</td>
												<td width="70%" colspan="3" >
													<input type="text" id="paramNodePath2" name="paramNodePath2" style="width:800px;"> 
												</td>
											</tr>
											<tr bgcolor="#FFFFFF">
												<td align="right" class="column" width="20%">
													����ֵ
												</td>
												<td  width="30%" >
													<input type="text" id="paramValue2" name="paramValue2" value=""/>
												</td>
												<td  align="right" class="column" width="30%">
													��������
												</td>
												<td  width="70%" >
													<select name="paramType2" id="paramType2" class="bk">
													 	<option value="-1">==��ѡ��==</option>
													 	<option value="1">string</option>
													 	<option value="2">int</option>
													 	<option value="3">unsignedInt</option>
													 	<option value="4">boolean</option>
													 </select>
												</td>
											</tr>
											
											<tr bgcolor="#FFFFFF">
												<td nowrap align="right" class="column" width="20%">
													�����ڵ�·��
												</td>
												<td width="70%" colspan="3" >
													<input type="text" id="paramNodePath3" name="paramNodePath3" style="width:800px;"> 
												</td>
											</tr>
											<tr bgcolor="#FFFFFF">
												<td align="right" class="column" width="20%">
													����ֵ
												</td>
												<td  width="30%" >
													<input type="text" id="paramValue3" name="paramValue3" value=""/>
												</td>
												<td  align="right" class="column" width="30%">
													��������
												</td>
												<td  width="70%" >
													<select name="paramType3" id="paramType3" class="bk">
													 	<option value="-1">==��ѡ��==</option>
													 	<option value="1">string</option>
													 	<option value="2">int</option>
													 	<option value="3">unsignedInt</option>
													 	<option value="4">boolean</option>
													 </select>
												</td>
											</tr>
											<tr bgcolor="#FFFFFF">
												<td colspan="4" align="right" class="green_foot">
													<button type="button" id="exeButton" name="exeButton"
														onclick="doExecute();" class=btn>
																&nbsp;ִ&nbsp;�� &nbsp;
													</button>
												</td>
											</tr>
											<tr bgcolor="#FFFFFF">
												<td colspan="4" align="left" class="green_foot">
													<div id="resultDIV" ></div>
												</td>
											</tr>
										</table>
									</td>
								</tr>
							</table>
						</td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
</body>
<script type="text/javascript">
var deviceIds = "";
var param = "";
function deviceResult(returnVal){
	$("#exeButton").attr("disabled",false);
	deviceIds ="";
	$("table[@id='table_showConfig']").css("display","");
	$("#resultDIV").html("");
	var totalNum = returnVal[0];
	if(returnVal[0]==0){
		totalNum = returnVal[2].length;
		$("div[@id='selectedDev']").html("<font size=2><strong>�������豸��Ŀ:" + returnVal[2].length + "</strong></font>");
		for(var i=0;i<returnVal[2].length;i++){
			deviceIds = deviceIds + returnVal[2][i][0]+",";		
			$("tr[@id='trDeviceResult']").css("display","");
			$("td[@id='tdDeviceSn']").html(returnVal[2][i][1]);
			$("td[@id='tdDeviceSn']").append("-");
			$("td[@id='tdDeviceSn']").append(returnVal[2][i][2]);
			$("td[@id='tdDeviceCityName']").html(returnVal[2][i][5])
		}
		
	}else{
		$("div[@id='selectedDev']").html("<font size=2><strong>�������豸��Ŀ:" + returnVal[0] + "</strong></font>");
		deviceIds = "0";
		param = returnVal[1];
	}
	// �ж�δ�����Ե�����
	var url = "<s:url value='/gtms/stb/config/batchCustomNodeConfig!queryUndoNum.action'/>"; 
	var maxNum = 50000;
	$.post(url,function(ajax){
      var num = parseInt(ajax);
      if(num > maxNum){
      	alert("�����������Ѵﵽ���ޣ������������ã�");
     	$("#exeButton").attr("disabled",true);
      	return;
      }
   });
}
function checkForm(){
	for(var i = 0;i <= 3;i++){
		//��������	
		var tempPath = $("#paramNodePath"+i).val();
		var tempValue = $("#paramValue"+i).val();
		var tempType = $("#paramType"+i).val();
		
		if(tempPath == ""){
			alert("��"+(i+1)+"�������ڵ�·��Ϊ�գ�");
			return false;
		}
		if(tempValue == ""){
			alert("��" + (i + 1)+"������ֵΪ�գ�")
			return false;
		}
		if(tempType == "-1"){
			alert("��" + (i + 1) + "����������Ϊ�գ�")
			return false;
		}
	}
    return true;
}
//ִ������
function doExecute(){
   var flag = <%=flag%>;
   var url = "<s:url value='/gtms/stb/config/batchCustomNodeConfig!doConfigByITV.action'/>"; 
   var paramNodePath = ""; 
   var paramValue = "";
   var paramType = "";
	for(var i = 0;i <= 3;i++){
		//��������	
		paramNodePath += "," + $("#paramNodePath"+i).val();
		paramValue += "," + $("#paramValue"+i).val();
		paramType += "," + $("#paramType"+i).val();
	}
    if(checkForm()){
        $.post(
	        url,{
	        deviceIds : deviceIds,
	        param:param,
	        paramNodePath : paramNodePath,
	        paramValue : paramValue,
	        paramType : paramType,
	        flag : flag
         },function(ajax){
	       	$("#resultDIV").html("");
	       	$("#doButton").attr("disabled",false);
	       	if(ajax == 1){
	             $("#resultDIV").append("��ִ̨�гɹ�");
	       	}else{
	            $("#resultDIV").append("��ִ̨��ʧ��");
	       	}
        });
    }
}
</script>
</html>
<%@ include file="../../../foot.jsp"%>