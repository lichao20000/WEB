<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>MAC前缀反推机顶盒信息新增</title>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<SCRIPT type="text/javascript" SRC="<s:url value="/Js/CheckFormForm.js"/>"></SCRIPT>

<script type="text/javascript">
	// 厂商改变触发
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
						var optionValue = "<option value='-1' >请选择型号</option>";
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
						var optionValue = "<option value='-1' >请选择型号</option>";
						$("select[@id='deviceModelId']").append(optionValue);
					}
				}else{
					$("select[@id='deviceModelId']").empty();
					var optionValue = "<option value='-1' >请选择型号</option>";
					$("select[@id='deviceModelId']").append(optionValue);
				}
				
				$("select[@id='softwareversion']").empty();
				var optionValue1 = "<option value='-1' >请选择软件版本</option>";
				$("select[@id='softwareversion']").append(optionValue1);
				
				$("select[@id='hardwareversion']").empty();
				var optionValue2 = "<option value='-1' >请选择硬件版本</option>";
				$("select[@id='hardwareversion']").append(optionValue2);
			});
	}

	// 型号改变触发
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
						var optionValue = "<option value='-1' >请选择软件版本</option>";
						$("select[@id='softwareversion']").append(optionValue);
						for(var i=0;i<lineData.length;i++){
							var optionValue = "<option value='"+lineData[i]+"' >"+lineData[i]+"</option>  ";
							$("select[@id='softwareversion']").append(optionValue);
						}
					}else{
						$("select[@id='softwareversion']").empty();
						var optionValue = "<option value='-1' >请选择软件版本</option>";
						$("select[@id='softwareversion']").append(optionValue);
					}
				}else{
					$("select[@id='softwareversion']").empty();
					var optionValue = "<option value='-1' >请选择软件版本</option>";
					$("select[@id='softwareversion']").append(optionValue);
				}
				
				$("select[@id='hardwareversion']").empty();
				var optionValue1 = "<option value='-1' >请选择硬件版本</option>";
				$("select[@id='hardwareversion']").append(optionValue1);
			});
	}

	//软件版本改变触发
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
						var optionValue = "<option value='-1' >请选择硬件版本</option>";
						$("select[@id='hardwareversion']").append(optionValue);
						for(var i=0;i<lineData.length;i++){
							var optionValue = "<option value='"+lineData[i]+"' >"+lineData[i]+"</option>  ";
							$("select[@id='hardwareversion']").append(optionValue);
						}
					}else{
						$("select[@id='hardwareversion']").empty();
						var optionValue = "<option value='-1' >请选择硬件版本</option>";
						$("select[@id='hardwareversion']").append(optionValue);
					}
				}else{
					$("select[@id='hardwareversion']").empty();
					var optionValue = "<option value='-1' >请选择硬件版本</option>";
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
			alert("请选择厂商！");
			return false;
		}
		if("-1" == deviceModelId){
			alert("请选择型号！");
			return false;
		}
		if("-1" == softwareversion){
			alert("请选择软件版本！");
			return false;
		}
		if("-1" == hardwareversion){
			alert("请选择硬件版本！");
			return false;
		}
		if("" == preMac){
			alert("MAC前缀不能为空！");
			return false;
		}else if(!reg_verify(preMac)){
			alert("MAC格式不对！");
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
					alert("MAC前缀已经存在，请确认其是否正确！");
				}else{
					alert("添加成功！");
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
					<th colspan="4">MAC前缀反推机顶盒信息新增</th>
				</tr>
				<TR>
					<TD class=column width="30%" align='right'>厂商</TD>
					<TD width="35%">
						<s:select list="vendorList" name="vendorId"
							headerKey="-1" headerValue="请选择厂商" listKey="vendor_id"
							listValue="vendor_add" value="vendorId" cssClass="bk"
							onchange="vendorChange()" theme="simple">
						</s:select>
					</TD>
				</TR>
				<TR>
					<TD class=column width="30%" align='right'>型号</TD>
					<TD width="35%">
						<select name="deviceModelId"  cssClass="bk"  id="deviceModelId" onchange="deviceModelChange()">
							<option value="-1">请选择型号</option>
						</select>
					</TD>
				</TR>
				<TR>
					<TD class=column width="30%" align='right'>软件版本</TD>
					<TD width="35%">
						<select name="softwareversion" cssClass="bk" 
							id="softwareversion" onchange="softwareversionChange()">
							<option value="-1">请选择软件版本</option>
						</select>
					</TD>
				</TR>
				<TR>
					<TD class=column width="30%" align='right'>硬件版本</TD>
					<TD width="35%">
						<select name="hardwareversion"  cssClass="bk"  id="hardwareversion">
							<option value="-1">请选择硬件版本</option>
						</select>
					</TD>
				</TR>
				<TR>
					<TD class=column width="30%" align='right'>MAC前缀</TD>
					<TD width="70%">
						<input type="text" name="preMac" id="preMac" class="bk" value="" size="20">
						<font color="red">格式为：11BBCCDD55，英文字母大写且不带:</font>
					</TD>
				</TR>
				<TR>
					<td colspan="2" align="center" class=foot>	
						<button onclick="save()">&nbsp;保存&nbsp;</button>
						&nbsp;&nbsp;&nbsp;&nbsp;
						<button onclick="javascript:window.close();">&nbsp;取消&nbsp;</button>
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
