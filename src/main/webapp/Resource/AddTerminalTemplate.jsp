<%@ page language="java" contentType="text/html; charset=GBK"
    pageEncoding="GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%--
/**
 * 新增定制终端模板
 *
 * @author Duangr(5250) tel：13770931606
 * @version 1.0
 * @since 2008-6-11 上午09:50:07
 *
 * 版权：南京联创科技 网管科技部
 *
 */
 --%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>新增定制终端模板</title>
<link href="<s:url value="/css/css_green.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/jQeuryExtend-linkage.js"/>"></script>
<style type="text/css">
</style>
<script language="JavaScript">
<!--
	var wanPort = null;
	var checkflag = true;// checkForm 用到的标志位,主要是each方法使用
	var curWanPortNum = null; // 存放当前最大支持WAN口数
	$(function(){
		var flag = "<s:property value='flag' />";
		var action = "<s:property value='action' />";
		if(action=="1"){
			if(flag == "1")
				alert("新增模板成功");
			else
				alert("新增模板失败");
		}
		wanPort = "<tr name=\"wanPort\">"+$("#infoTemplate").html()+"</tr>";
		wanPort = wanPort.replace(/_test_/g,"")
		//alert(wanPort);
	});

	// 表单验证
	function CheckForm(){
		checkflag = true;
		if($("input[@name='template_name']").val()==""){
			alert("请输入模板名称");
			$("input[@name='template_name']").focus();
			return false;
		}
		if($("select[@name='vendor_id']").val()=="-1"){
			alert("请选择厂商类型");
			$("select[@name='vendor_id']").focus();
			return false;
		}
		if($("select[@name='device_type']").val()=="-1"){
			alert("请选择定制终端类型");
			$("select[@name='device_type']").focus();
			return false;
		}
		if($("select[@name='access_style_id']").val()=="-1"){
			alert("请选择管理方式");
			$("select[@name='access_style_id']").focus();
			return false;
		}
		if($("input[@name='max_lan_num']").val()==""){
			alert("请输入最大支持LAN端口数");
			$("input[@name='max_lan_num']").focus();
			return false;
		}
		if($("input[@name='max_ssid_num']").val()==""){
			alert("请输入最大支持SSID数");
			$("input[@name='max_ssid_num']").focus();
			return false;
		}
		if($("input[@name='max_wan_num']").val()==""){
			alert("请输入最大支持WAN端口数");
			$("input[@name='max_wan_num']").focus();
			return false;
		}
		$("input[@name='addresspool']").each(function (){
			var obj = $(this);
			if(obj.val()==""){
				alert("请输入WAN端口支持最大地址数");
				obj.focus();
				checkflag = false;
				return false;
			}
		});
		if(checkflag == false)
			return false;
		$("select[@name='wan_type']").each(function (){
			var obj = $(this);
			if(obj.val()=="-1"){
				alert("请选择WAN端口连接方式");
				obj.focus();
				checkflag = false;
				return false;
			}
		});
		if(checkflag == false)
			return false;
		return true;
	}
	// 失去焦点事件
	function showInfo(){
		try{
			var v = $("#max_wan_num").val();
			v = parseInt(v,10);
			//输入的WAN口数与当前WAN口数相同,不需要联动变化
			if(curWanPortNum == v)
				return;
			if(v>100){
				if(!confirm("是否继续运行?\n\n您输入的[最大支持WAN端口数]比较大,将会导致浏览器运行速度减慢.\n如果继续运行,您的计算机将可能停止响应.")){
					$("#max_wan_num").val(curWanPortNum);
					return;
				}
			}
			$("tr[@name='wanPort']").remove();
			if(v>0){
				for(var i=1;i<v+1;i++){
					$("#infoTemplate").before(wanPort.replace(/portNum/g,i));
				}
				curWanPortNum = v;
			}else{
				curWanPortNum = 0;
			}
		}catch(e){
			//alert(e);
		}
	}
	// 返回的模板列表页面
	function goList(){
		window.location="<s:url value='/Resource/terminalTemplate!showList.action' />";
	}
	// 限制仅能输入数字
	function onlyNum(v){
		v.value=v.value.replace(/[^\d]/g,"");
	}
//-->
</script>
</head>
<body>
<form name="speclinefrm" action="<s:url value="/Resource/terminalTemplate.action"/>" onSubmit="return CheckForm();"  method ="post" >
	<table width="98%" border=0 align="center" cellpadding="0" cellspacing="0">
		<tr><td height=20>&nbsp;</td></tr>
		<tr>
			<td>
				<table width="100%" height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
					<tr>
						<td width="162" align="center" class="title_bigwhite">
							新增定制终端模板
						</td>
						<td>
							<img src="../images/attention_2.gif" width="15" height="12">
							带'<font color="#FF0000">*</font>'的表单必须填写或选择
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td bgcolor="#999999">
				<table border=0 cellspacing=1 cellpadding=1 width="100%">
					<tr>
						<th colspan="4" align="center">新增定制终端模板
						</th>
					</tr>
					<tr>
						<td class="column" height="17" align="right" width="13%" nowrap>模板名称</td>
						<td class="column" height="17" width="29%">
							<input type="text" class="bk" name="template_name">
							<font color="#FF0000">*</font>
						</td>
						<td class="column" height="17" align="right" width="13%" nowrap>厂商类型</td>
						<td class="column" height="17" width="29%">
							<select class="bk" name="vendor_id">
								<option value="-1">==请选择==
								<s:iterator value="vendorList" var="vl"  status="status">
								<option value="<s:property value="#vl.vendor_id" />"><s:property value="#vl.vendor_name" />/<s:property value="#vl.vendor_add" />
								</s:iterator>
							</select>
							<font color="#FF0000">*</font>
						</td>
					</tr>
					<tr>
						<td class="column" height="17" align="right" width="13%" nowrap>定制终端类型</td>
						<td class="column" height="17" width="29%">
							<select class="bk" name="device_type">
								<option value="-1">==请选择==
								<option value="Navigator1-1">Navigator1-1
								<option value="Navigator1-2">Navigator1-2
								<option value="Navigator2-1">Navigator2-1
								<option value="Navigator2-2">Navigator2-2
							</select>
							<font color="#FF0000">*</font>
						</td>
						<td class="column" height="17" align="right" width="13%" nowrap>管理方式</td>
						<td class="column" height="17" width="29%">
							<select class="bk" name="access_style_id">
								<option value="-1">==请选择==
								<option value="0">ADSL接入
								<option value="1">LAN接入
							</select>
							<font color="#FF0000">*</font>
						</td>
					</tr>
					<tr>
						<td class="column" height="17" align="right" width="13%" nowrap>最大支持LAN端口数</td>
						<td class="column" height="17" width="29%">
							<input type="text" class="bk" name="max_lan_num" onkeyup="onlyNum(this);" >
							<font color="#FF0000">*</font>
						</td>
						<td class="column" height="17" align="right" width="13%" nowrap>最大支持SSID数</td>
						<td class="column" height="17" width="29%">
							<input type="text" class="bk" name="max_ssid_num"  onkeyup="onlyNum(this);">
							<font color="#FF0000">*</font>
						</td>
					</tr>
					<tr>
						<td class="column" height="17" align="right" width="13%" nowrap>最大支持WAN端口数</td>
						<td class="column" height="17" colspan="3" >
							<input type="text" class="bk" name="max_wan_num" id="max_wan_num" onblur="showInfo();"  onkeyup="onlyNum(this);">
							<font color="#FF0000">*</font>
						</td>
					</tr>
					<tr style="display:none" id="infoTemplate">
						<td class="column" height="17" align="right" width="13%" nowrap>WAN端口portNum支持最大地址数</td>
						<td class="column" height="17" width="29%">
							<input type="text" class="bk" name="addresspool_test_"  onkeyup="onlyNum(this);">
							<font color="#FF0000">*</font>
						</td>
						<td class="column" height="17" align="right" width="13%" nowrap>WAN端口portNum连接方式</td>
						<td class="column" height="17" width="29%">
							<select class="bk" name="wan_type_test_">
								<option value="-1">==请选择==
								<option value="0">桥接
								<option value="1">路由
								<option value="2">静态IP
								<option value="3">DHCP
							</select>
							<font color="#FF0000">*</font>
						</td>
					</tr>
					<tr>
						<td colspan="4" align="center" class="green_foot">
							<input type="submit" value=" 保 存 " class="btn"> &nbsp;&nbsp;
							<input type="reset" value=" 重 写 " class="btn"> &nbsp;&nbsp;
							<input type="button" value="返回列表" onclick="goList();" class="btn">
							<input type="hidden" name="action" value="1">
						</td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
</form>
</body>
</html>
