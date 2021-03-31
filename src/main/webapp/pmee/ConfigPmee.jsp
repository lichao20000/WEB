<%@ page language="java" contentType="text/html; charset=GBK"
    pageEncoding="GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%--
/**
 * 配置采集项
 * REQ:XXXX-XXXX-XXXX
 * BUG:XXXX-XXXX-XXXX
 * @author BENYP(5260) E-mail:BENYP@LIANCHUANG.COM
 * @version 1.0
 * @since 2008-5-22 11:07:41
 *
 * 版权：南京联创科技 网管科技部
 *
 */
 --%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>配置采集项</title>
<script type="text/javascript"  src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript"  src="<s:url value="/pmee/jquery.PmeeConfig.js"/>"></script>
<script type="text/javascript">
	//页面初始化
	$(function(){
		//查询方式切换
		$("input[@name='checkType']").click(function(){
			$.ToggleQuery($(this).val());
		});
		//属地change事件
		$("select[@name='city_id']").change(function(){
			$("select[@name='vendor_id']").val("");
			if($(this).val()==""){
				alert("请选择属地!");
				$(this).select();
				$(this).focus();
				return false;
			}
		});
		//厂商change事件
		$("select[@name='vendor_id']").change(function(){
			$("select[@name='device_model']").val("");
			if($(this).val()==""){
				alert("请选择厂商!");
				$(this).focus();
				return;
			}
			$.VendorChange($(this).val());
			$.post(
				"<s:url value="/pmee/DevPmeeConfig!getExpression.action"/>",
				{
					vendor_id:"'"+$(this).val()+"'"
				},
				function(data){
					$("select[@name='exp_name']").html(data);
				}
			);

		});
		//设备型号change事件
		$("select[@name='device_model']").change(function(){
			$("select[@name='version']").val("");
			if($(this).val()==""){
				alert("请选择设备型号!");
				$(this).focus();
				return;
			}
			$.ModelChange($("select[@name='vendor_id']").val(),$(this).val());
		});
		//设备版本change事件
		$("select[@name='version']").change(function(){
			$("#div_device span").html("");
			if($(this).val()==""){
				alert("请选择设备版本!");
				$(this).focus();
				return;
			}
			$.VersionChange($("select[@name='city_id']").val(),$("select[@name='vendor_id']").val(),$(this).val());
		});
		//按用户查询
		$("input[@name='btn_user']").click(function(){
			if($.trim($("input[@name='username']").val())=="" && $.trim($("input[@name='phone']").val())==""){
				alert("请输入用户名或电话号码查询!");
				$("input[@name='username']").focus();
				return false;
			}
			$.post(
				"<s:url value="/pmee/DevPmeeConfig!getDevByUser.action"/>",
				{
					username:$("input[@name='username']").val(),
					phone:$("input[@name='phone']").val()
				},
				function(data){
					$("#div_device span").html(data);
				}
			);
		});
		//按IP查询
		$("input[@name='btn_ip']").click(function(){
			if($.trim($("input[@name='serial']").val())=="" && $.trim($("input[@name='ip']").val())==""){
				alert("请输入设备序列号或设备IP查询!");
				$("input[@name='serial']").focus();
				return false;
			}
			$.post(
				"<s:url value="/pmee/DevPmeeConfig!getDevByIP.action"/>",
				{
					serial:$("input[@name='serial']").val(),
					ip:$("input[@name='ip']").val()
				},
				function(data){
					$("#div_device span").html(data);
				}
			);
		});
		//保存
		$("input[@name='save']").click(function(){
			if($("input[@name='device'][@checked]").length<=0){
				alert("请选择设备!");
				return false;
			}else if($("select[@name='exp_name']").val()==""){
				alert("请选择性能表达式!");
				$("select[@name='exp_name']").focus();
				return false;
			}else if($.trim($("input[@name='samp_distance']").val())==""){
				alert("请输入采样间隔!");
				$("input[@name='samp_distance']").focus();
				return false;
			}
			var device_id=getDevID();
			$.TestConfig($("select[@name='exp_name'] option[@selected]").html(),$("select[@name='exp_name']").val(),device_id);
		});
		//查询配置结果
		$("input[@name='query']").click(function(){
			if($("input[@name='device'][@checked]").length<=0){
				alert("请选择设备!");
				return false;
			}
			$("table[@name='result']").show();
			$("tbody[@name='tbody']").html("<tr bgcolor='#FFFFFF'><td colspan='8'>正在载入数据,请等待......</td></tr>");
			$.post(
					"<s:url value="/pmee/DevPmeeConfig!getConfigResult.action"/>",
				{
					device_id:getDevID()
				},
				function(data){
					$("tbody[@name='tbody']").html(data);
				}
			);
		});
		$("input[@name='chk_all']").click(function(){
			var chk=$(this).attr("checked");
			chk=typeof(chk)=="undefined"?false:chk;
			$("input[@name='device']").each(function(){
				$(this).attr("checked",chk);
			});
		});
	});
	//获取选中设备的设备序列号
	function getSerial(){
		var sel="";
		$("input[@name='device'][@checked]").each(function(){
			sel+="-/-"+$(this).attr("dev_serial");
		});
		return sel==""?"":sel.substring(3);
	}
	//获取选中设备的ID
	function getDevID(){
		var did="";
		$("input[@name='device'][@checked]").each(function(){
			did+=",'"+$(this).val()+"'";
		});
		return did==""?"":did.substring(1);
	}
	//获取设备OUI
	function getDevOUI(){
		var did="";
		$("input[@name='device'][@checked]").each(function(){
			did+=",'"+$(this).attr("oui")+"'";
		});
		return did==""?"":did.substring(1);
	}
	//获取性能表达式
	function getExp(company){
		if($("input[@name='checkType'][@checked]").val()=="0"){
			return false;
		}
		$.post(
			"<s:url value="/pmee/DevPmeeConfig!getExpression.action"/>",
			{
				vendor_id:getDevOUI()
			},
			function(data){
				$("select[@name='exp_name']").html(data);
			}
		);
	}
	//删除性能表达式
	function Del(device_id,expressionid,target){
		if(window.confirm("确认删除?")){
			$.post(
				"<s:url value="/pmee/DevPmeeConfig!DelExpressionID.action"/>",
				{
					device_id:device_id,
					expressionid:expressionid
				},
				function(data){
					if(eval(data)==true){
						alert("删除成功");
						target.parent().parent().remove();
					}else{
						alert("删除失败");
					}
				}
			);
		}
	}
</script>
<link href="<s:url value="/css/css_green.css"/>" rel="stylesheet" type="text/css">

</head>
<body>
	<table border=0 cellspacing=0 cellpadding=0 width="100%">
	<tr>
		<td class=column1>
			<table width="98%" border="0" align="center" cellpadding="0" cellspacing="0">
				<tr>
					<td>
						<table width="100%" height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
							<tr>
								<td width="162">
									<div align="center" class="title_bigwhite">设备性能</div>
								</td>
								<td>
									<img src="<s:url value="/images/attention_2.gif"/>" width="15" height="12">
									<input type="radio" value="0" name="checkType" checked id="c_0"><label for="c_0">按属地和型号</label>&nbsp;&nbsp;
									<input type="radio" value="1" name="checkType" id="c_1"><label for="c_1">按用户</label>&nbsp;&nbsp;
									<input type="radio" value="2" name="checkType" id="c_2"><label for="c_2">按设备</label>
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<th colspan=4>
						性能配置
					</th>
				</tr>
				<tr>
				<td bgcolor="#FFFFFF">
					<table width="100%" border=0 align="center" cellpadding="1" cellspacing="1" bgcolor="#999999" class="text">
						<tr bgcolor="#FFFFFF" id="tr1">
							<td align="right" width="10%">属地:</td>
							<td align="left" width="30%">
								<select name="city_id" class="bk">
									<option value="" >==请选择==</option>
									<s:iterator var="citylist" value="CityList">
										<option value="<s:property value="#citylist.city_id"/>">
											==<s:property value="#citylist.city_name"/>==
										</option>
									</s:iterator>
								</select>
							</td>
							<td align="right" width="10%">厂商:</td>
							<td align="left" width="30%">
								<select name="vendor_id" class="bk">
									<option value="">==请选择==</option>
									<s:iterator var="vendorlist" value="VendorList">
										<option value="<s:property value="#vendorlist.vendor_id"/>">
											==<s:property value="#vendorlist.vendor_add+'('+#vendorlist.vendor_id+')'"/>==
										</option>
									</s:iterator>
								</select>
							</td>
						</tr>
						<tr bgcolor="#FFFFFF" id="tr2">
							<td align="right" width="10%">设备型号:</td>
							<td width="30%">
								<select name="device_model" class="bk">
									<option value="">--请先选择厂商--</option>
								</select>
							</td>
							<td align="right" width="10%">设备版本:</td>
							<td width="30%">
								<select name="version" class="bk">
									<option value="">--请先选择设备型号--</option>
								</select>
							</td>
						</tr>
						<tr bgcolor="#FFFFFF" id="tr3" style="display:none">
							<td align="right" width="10%">用户名:</td>
							<td width="30%">
								<input type="text" name="username" value="" class=bk>
							</td>
							<td align="right" width="10%">用户电话号码:</td>
							<td width="30%">
								<input type="text" name="phone" value="" class=bk>
								<input type="button" name="btn_user" class=btn value="查询">
							</td>
						</tr>
						<tr bgcolor="#FFFFFF" id="tr4" style="display:none">
							<td align="right" width="10%">设备序列号:</td>
							<td width="30%">
								<input type="text" name="serial" value="" class=bk>
							</td>
							<td align="right" width="10%">设备域名或IP:</td>
							<td width="30%">
								<input type="text" name="ip" value="" class=bk>
								<input type="button" name="btn_ip" class=btn value=" 查 询 ">
							</td>
						</tr>
						<tr bgcolor="#FFFFFF">
							<td align="right" width="10%">设备列表: <br><label for="chk_all_id">全选</label><input id="chk_all_id" type="checkbox" name="chk_all"></td>
							<td colspan="3">
								<div id="div_device" style="width: 95%; height: 100px; z-index: 1; top: 100px; overflow: scroll">
									<span>请选择设备！</span>
								</div>
							</td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td height="20"></td>
			</tr>
			<tr>
				<th colspan=4>
					性能配置参数
				</th>
			</tr>
			<tr>
				<td bgcolor="#FFFFFF">
					<table width="100%" border=0 align="center" cellpadding="1" cellspacing="1" bgcolor="#999999" class="text">
						<tr>
							<td class=column1 align=center width="100">性能表达式</td>
							<td class=column2 align=left width="200" colspan="3">
								<div id="pmdiv">
									<select name="exp_name" onchange="Pm_Name()" class="bk">
										<option value="">请选择此设备的性能表达式</option>
									</select>
								</div>
							</td>
						  </tr>
						  <tr>
							<td class="column1" align="center" width="100">采样间隔</td>
							<td class="column2" align="left">
								<input name="samp_distance" type="text" class="bk" value="900" style="width:100">
							</td>
							<td class="column1" align="center" width="100">原始数据是否入库</td>
							<td class="column2" align="left">
								<select name="ruku" class="bk">
							  		<option value="0">否</option>
							  		<option value="1">是</option>
								</select>
							</td>
						  </tr>
					</table>
				</td>
			</tr>
			<tr>
				<td height="20"></td>
			</tr>
			<tr>
				<td align="center">
					<input type="button" name="save" value=" 保 存 ">&nbsp;&nbsp;
					<input type="button" name="query"  value="查看配置结果">
				</td>
			</tr>
	</table>
	<tr>
		<td height="20"></td>
	</tr>
	<table name="result" width="98%" border=0 align="center" cellpadding="1" cellspacing="1" bgcolor="#999999" class="text" style="display:none;">
			<tr>
				<th colspan=8>设备性能配置结果</th>
			</tr>
			<tr>
				<th>设备名称</th>
				<th>设备序列号</th>
				<th>表达式名称</th>
				<th>分类</th>
				<th>采样间隔</th>
				<th>配置结果</th>
				<th>失败原因描述</th>
				<th>操作</th>
			</tr>
			<tbody name="tbody">

			</tbody>
		</table>
	</td>
</tr>
</table>
</body>
</html>
