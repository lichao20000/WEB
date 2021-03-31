<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>MACǰ׺���ƻ�������Ϣ����</title>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<SCRIPT type="text/javascript" SRC="<s:url value="/Js/CheckFormForm.js"/>"></SCRIPT>

<script type="text/javascript">
	// ���̸ı䴥��
	function vendorChange(){
		var vendorId = $("select[@name='vendorId']").val();
		var url = "<s:url value='/gtms/stb/resource/PreMacDeviceACT!getDeviceModelS.action'/>";
		
		$.post(url,{
			vendorId:vendorId
			},function(ajax){
				if(ajax!=""){
					var lineData = ajax.split("#");
					if(typeof(lineData)&&typeof(lineData.length)){
						$("select[@id='deviceModelId']").empty();
						var optionValue = "<option value='-1' >��ѡ���ͺ�</option>";
						$("select[@id='deviceModelId']").append(optionValue);
						for(var i=0;i<lineData.length;i++){
							var oneElement = lineData[i].split("$");
							var xValue = oneElement[0];
							var xText = oneElement[1];
							var optionValue = "<option value='"+xValue+"' >"+xText+"</option>  ";
							$("select[@id='deviceModelId']").append(optionValue);
						}
					}else{
						$("select[@id='deviceModelId']").empty();
						var optionValue = "<option value='-1' >��ѡ���ͺ�</option>";
						$("select[@id='deviceModelId']").append(optionValue);
					}
				}else{
					$("select[@id='deviceModelId']").empty();
					var optionValue = "<option value='-1' >��ѡ���ͺ�</option>";
					$("select[@id='deviceModelId']").append(optionValue);
				}
				
				$("select[@id='softwareversion']").empty();
				var optionValue1 = "<option value='-1' >��ѡ������汾</option>";
				$("select[@id='softwareversion']").append(optionValue1);
				
				$("select[@id='hardwareversion']").empty();
				var optionValue2 = "<option value='-1' >��ѡ��Ӳ���汾</option>";
				$("select[@id='hardwareversion']").append(optionValue2);
			});
	}

	// �ͺŸı䴥��
	function deviceModelChange(){
		var vendorId = $("select[@name='vendorId']").val();
		var deviceModelId = $("select[@name='deviceModelId']").val();
		
		var url = "<s:url value='/gtms/stb/resource/PreMacDeviceACT!getSoftwareversionS.action'/>";
		
		$.post(url,{
			vendorId:vendorId,
			deviceModelId:deviceModelId
			},function(ajax){
				if(ajax!=""){
					var lineData = ajax.split("#");
					if(typeof(lineData)&&typeof(lineData.length)){
						$("select[@id='softwareversion']").empty();
						var optionValue = "<option value='-1' >��ѡ������汾</option>";
						$("select[@id='softwareversion']").append(optionValue);
						for(var i=0;i<lineData.length;i++){
							var optionValue = "<option value='"+lineData[i]+"' >"+lineData[i]+"</option>  ";
							$("select[@id='softwareversion']").append(optionValue);
						}
					}else{
						$("select[@id='softwareversion']").empty();
						var optionValue = "<option value='-1' >��ѡ������汾</option>";
						$("select[@id='softwareversion']").append(optionValue);
					}
				}else{
					$("select[@id='softwareversion']").empty();
					var optionValue = "<option value='-1' >��ѡ������汾</option>";
					$("select[@id='softwareversion']").append(optionValue);
				}
				
				$("select[@id='hardwareversion']").empty();
				var optionValue1 = "<option value='-1' >��ѡ��Ӳ���汾</option>";
				$("select[@id='hardwareversion']").append(optionValue1);
			});
	}

	//����汾�ı䴥��
	function softwareversionChange(){
		var vendorId = $("select[@name='vendorId']").val();
		var deviceModelId = $("select[@name='deviceModelId']").val();
		var softwareversion = $("select[@name='softwareversion']").val();
		var url = "<s:url value='/gtms/stb/resource/PreMacDeviceACT!getHardwareversionS.action'/>";
		
		$.post(url,{
			vendorId:vendorId,
			deviceModelId:deviceModelId,
			softwareversion:softwareversion
			},function(ajax){
				if(ajax!=""){
					var lineData = ajax.split("#");
					if(typeof(lineData)&&typeof(lineData.length)){
						$("select[@id='hardwareversion']").empty();
						var optionValue = "<option value='-1' >��ѡ��Ӳ���汾</option>";
						$("select[@id='hardwareversion']").append(optionValue);
						for(var i=0;i<lineData.length;i++){
							var optionValue = "<option value='"+lineData[i]+"' >"+lineData[i]+"</option>  ";
							$("select[@id='hardwareversion']").append(optionValue);
						}
					}else{
						$("select[@id='hardwareversion']").empty();
						var optionValue = "<option value='-1' >��ѡ��Ӳ���汾</option>";
						$("select[@id='hardwareversion']").append(optionValue);
					}
				}else{
					$("select[@id='hardwareversion']").empty();
					var optionValue = "<option value='-1' >��ѡ��Ӳ���汾</option>";
					$("select[@id='hardwareversion']").append(optionValue);
				}
			});
	}
	
	function save()
	{
		var platformId = $("select[@name='platformId']").val();
		var vendorId = $("select[@name='vendorId']").val();
		var deviceModelId = $("select[@name='deviceModelId']").val();
		var softwareversion = $("select[@name='softwareversion']").val();
		var hardwareversion = $("select[@name='hardwareversion']").val();
		var preMac=Trim($("input[@name='preMac']").val());
		
		if("-1" == vendorId){
			alert("��ѡ���̣�");
			return false;
		}
		if("-1" == deviceModelId){
			alert("��ѡ���ͺţ�");
			return false;
		}
		if("-1" == softwareversion){
			alert("��ѡ������汾��");
			return false;
		}
		if("-1" == hardwareversion){
			alert("��ѡ��Ӳ���汾��");
			return false;
		}
		if("" == preMac){
			alert("MACǰ׺����Ϊ�գ�");
			return false;
		}else if(!reg_verify(preMac)){
			alert("MAC��ʽ���ԣ�");
			return false;
		}
		
		var url = "<s:url value='/gtms/stb/resource/PreMacDeviceACT!add.action'/>";
		$.post(url, {
			vendorId : vendorId,
			deviceModelId : deviceModelId,
			hardwareversion : hardwareversion,
			softwareversion : softwareversion,
			preMac : preMac
			}, function(ajax) {
				if("2" == ajax){
					alert("MACǰ׺�Ѿ����ڣ���ȷ�����Ƿ���ȷ��");
				}else{
					alert("��ӳɹ���");
					window.location.reload();
				}
		});
	}
	
	function reg_verify(addr){
	    var reg = /^[\dA-F]+$/;

	    if(addr.match(reg)) {
	        return true;
	    } else {
	         return false;
	    }
	}
</script>
</head>

<body>
<form id="form">
<table>
	<tr>
		<td HEIGHT=20>&nbsp;</td>
	</tr>
	<tr>
		<td>
			<table class="querytable">
				<tr>
					<th colspan="4">MACǰ׺���ƻ�������Ϣ����</th>
				</tr>
				<TR>
					<TD class=column width="30%" align='right'>����</TD>
					<TD width="35%">
						<s:select list="vendorList" name="vendorId"
							headerKey="-1" headerValue="��ѡ����" listKey="vendor_id"
							listValue="vendor_add" value="vendorId" cssClass="bk"
							onchange="vendorChange()" theme="simple">
						</s:select>
					</TD>
				</TR>
				<TR>
					<TD class=column width="30%" align='right'>�ͺ�</TD>
					<TD width="35%">
						<select name="deviceModelId"  cssClass="bk"  id="deviceModelId" onchange="deviceModelChange()">
							<option value="-1">��ѡ���ͺ�</option>
						</select>
					</TD>
				</TR>
				<TR>
					<TD class=column width="30%" align='right'>����汾</TD>
					<TD width="35%">
						<select name="softwareversion" cssClass="bk" 
							id="softwareversion" onchange="softwareversionChange()">
							<option value="-1">��ѡ������汾</option>
						</select>
					</TD>
				</TR>
				<TR>
					<TD class=column width="30%" align='right'>Ӳ���汾</TD>
					<TD width="35%">
						<select name="hardwareversion"  cssClass="bk"  id="hardwareversion">
							<option value="-1">��ѡ��Ӳ���汾</option>
						</select>
					</TD>
				</TR>
				<TR>
					<TD class=column width="30%" align='right'>MACǰ׺</TD>
					<TD width="70%">
						<input type="text" name="preMac" id="preMac" class="bk" value="" size="20">
						<font color="red">��ʽΪ��11BBCCDD55��Ӣ����ĸ��д�Ҳ���:</font>
					</TD>
				</TR>
				<TR>
					<td colspan="2" align="center" class=foot>	
						<button onclick="save()">&nbsp;����&nbsp;</button>
						&nbsp;&nbsp;&nbsp;&nbsp;
						<button onclick="javascript:window.close();">&nbsp;ȡ��&nbsp;</button>
						&nbsp;&nbsp;&nbsp;&nbsp;
					</td> 
				</TR>
			</table>
		</td>
	</tr>
</table>
</form>
</body>
</html>
