<%--
Author      : 王森博
Date		: 2010-4-15
Desc		: 手动的批量软件升级
--%>
<%@ include file="../../timelater.jsp"%>
<%@ page contentType="text/html; charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ include file="../head.jsp"%>
<%
 String gwType = request.getParameter("gw_type");
 %>
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value='/Js/jquery.js'/>"></SCRIPT>
<script type="text/javascript" src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<link rel="stylesheet" href="<s:url value='/css/css_green.css'/>"
	type="text/css">
<SCRIPT LANGUAGE="JavaScript">
$(function(){
	gwShare_change_select("vendor","-1");
});
var deviceIds = "";
var returnValthis ="init";
var max = '<s:property value="maxActive" escapeHtml="false" />';


function gwShare_change_select(type,selectvalue){
	switch (type){
		case "vendor":
			var url = "<s:url value="/gwms/share/gwDeviceQuery!getVendor.action"/>";
			$.post(url,{
			},function(ajax){
				gwShare_parseMessage(type,ajax,$("select[@name='gwShare_vendorId']"),selectvalue);
				$("select[@name='gwShare_deviceModelId']").html("<option value='-1'>==请先选择设备厂商==</option>");
				//$("select[@name='gwShare_devicetypeId']").html("<option value='-1'>==请先选择设备型号==</option>");
			});
			break;
		case "deviceModel":
			var url = "<s:url value="/gwms/share/gwDeviceQuery!getDeviceModel.action"/>";
			var vendorId = $("select[@name='gwShare_vendorId']").val();
			if("-1"==vendorId){
				$("select[@name='gwShare_deviceModelId']").html("<option value='-1'>==请先选择厂商==</option>");
				//$("select[@name='gwShare_devicetypeId']").html("<option value='-1'>==请先选择设备型号==</option>");
				break;
			}
			$.post(url,{
				gwShare_vendorId:vendorId
			},function(ajax){
				gwShare_parseMessage(type,ajax,$("select[@name='gwShare_deviceModelId']"),selectvalue);
				//$("select[@name='gwShare_devicetypeId']").html("<option value='-1'>==请先选择设备型号==</option>");
			});
			break;
			case "devicetype":
			var url = "<s:url value="/gwms/share/gwDeviceQuery!getDevicetype.action"/>";
			var vendorId = $("select[@name='gwShare_vendorId']").val();
			var deviceModelId = $("select[@name='gwShare_deviceModelId']").val();
			var gwShare_isBatch = "hello";
			if("-1"==deviceModelId){
				$("select[@name='gwShare_devicetypeId']").html("<option value='-1'>==请先选择设备型号==</option>");
				break;
			}
			$.post(url,{
				gwShare_vendorId:vendorId,
				gwShare_deviceModelId:deviceModelId,
				isBatch:gwShare_isBatch
			},function(ajax){
				gwShare_parseMessage(type,ajax,$("select[@name='gwShare_devicetypeId']"),selectvalue);
			});
			break;
		default:
			alert("未知查询选项！");
			break;
	}
}

function gwShare_parseMessage(type,ajax,field,selectvalue){
	var flag = true;
	if(""==ajax){
		return;
	}
	var lineData = ajax.split("#");
	if(!typeof(lineData) || !typeof(lineData.length)){
		return false;
	}
	field.html("");
	option = "<option value='-1' selected>==请选择==</option>";

	field.append(option);

	for(var i=0;i<lineData.length;i++){
		var oneElement = lineData[i].split("$");
		var xValue = oneElement[0];
		var xText = oneElement[1];
		if(selectvalue==xValue){
			flag = false;
			//根据每组value和text标记的值创建一个option对象
			option = "<option value='"+xValue+"' selected>=="+xText+"==</option>";
		}else{
			//根据每组value和text标记的值创建一个option对象
			option = "<option value='"+xValue+"'>=="+xText+"==</option>";
		}
		try{
			field.append(option);
		}catch(e){
			alert("设备型号检索失败！");
		}
	}

	if(flag){
		field.attr("value","-1");
	}
}


var cqSoftCitys = "";
function ChkAllCity(){
	if ($('#cityAll').attr('checked')) {
		cqSoftCitys = "";
		$("input:checkbox[name='city']").each(function() { // 遍历name=test的多选框
			$(this).attr("checked",true);
			cqSoftCitys = cqSoftCitys+""+$(this).val()+",";
		});
	}
	else{
		cqSoftCitys = "";
		$("input:checkbox[name='city']").each(function() { // 遍历name=test的多选框
			$(this).attr("checked",false);
		});
	}
	$("#cqSoftCitys").val(cqSoftCitys);
}

function ChkCity(){
	cqSoftCitys = "";
	var i = 0;
	$("input:checkbox[name='city']:checked").each(function() { // 遍历name=test的多选框
		i++;
		cqSoftCitys = cqSoftCitys+""+$(this).val()+",";  // 每一个被选中项的值
	});
	if(i>=40){
		$("#cityAll").attr("checked",true);
	}
	$("#cqSoftCitys").val(cqSoftCitys);
}
function ChkMode(){
	var mode = "";
	$("input:checkbox[name='mode1']:checked").each(function() { // 遍历name=test的多选框
		mode = mode+""+$(this).val()+",";  // 每一个被选中项的值
	});
	if(mode==""){
		mode="1,";
	}
	$("#mode").val(mode);
}


//点击增加的时候增加一组
var flag = 0;
function doAdd(){
	flag++ ;
	//复制paramAddDiv0里的元素
	$("#fatherDiv").append($("#paramAddDiv0").clone(true).attr("id","paramAddDiv"+flag));
	//$("#paramAddDiv"+flag).each(function(){
		//将克隆过来的输入框全部置空
		$("#paramAddDiv"+flag).find("input[type='text']").val("");
		$("#paramAddDiv"+flag).find("select[name='paramType0']").val("");
		//更改克隆过来div的各个输入框的id
		$("#paramAddDiv"+flag).find("input[name='paramNodePath0']").attr("id","paramNodePath"+flag).attr("name","paramNodePath"+flag);
		$("#paramAddDiv"+flag).find("input[name='paramValue0']").attr("id","paramValue"+flag).attr("name","paramValue"+flag);
		$("#paramAddDiv"+flag).find("select[name='paramType0']").attr("id","paramType"+flag).attr("name","paramType"+flag);
	//});
}

//动态的删除删除掉一组,并且只删除最后一组
function doDelete(){
   if($("#fatherDiv div").size()>1){
   		$("#fatherDiv div:last-child").remove();
   }else{
   		alert("这是最后一行，无法再删除");
   		return;
   }
    flag--;
}



function CheckForm(){
	if(cqSoftCitys==""){
		alert("请选择属地");
		return false;
	}
	if($("select[@name='gwShare_vendorId']").val()=="-1"){
		alert("请选择厂商！");
		return false;
	}
	var rtnflag;
 	$("#fatherDiv div").each(function(i){
		//遍历配置
		var tempPath = $("#paramNodePath"+i).val();
		var tempValue = $("#paramValue"+i).val();
		var tempType = $("#paramType"+i).val();
		if(tempPath==""){
			alert("第"+(i+1)+"个参数节点路径为空！");
			rtnflag = "false";
		}
		if(tempValue==""){
			alert("第"+(i+1)+"个参数值为空！")
			rtnflag = "false";
		}
		if(tempType=="-1"){
			alert("第"+(i+1)+"个参数类型为空！")
			rtnflag = "false";
		}
    });
 	if(rtnflag=="false"){
 		return false;
 	}
 	 var paramNodePath = "";
	 var paramValue = "";
	 var paramType = "";
 	$("#fatherDiv div").each(function(i){
		//遍历配置
		paramNodePath += "," + $("#paramNodePath"+i).val();
		paramValue    += "," + $("#paramValue"+i).val();
		paramType     += "," + $("#paramType"+i).val();
    });
 	$("#paramPath").val(paramNodePath);
 	$("#paramValue").val(paramValue);
 	$("#paramType").val(paramType);
 	if($("#mode").val()==""){
 		$("#mode").val("1,");
	}
	return true;
}
</SCRIPT>
<%@ include file="../../toolbar.jsp"%>

<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD>
			<FORM NAME="frm" METHOD="post"
							ACTION="<s:url value="/gwms/resource/batchConfig!batchConfig.action"/>"
							onsubmit="return CheckForm()">
							<input type="hidden" name="deviceIds" value="" />
							<input type="hidden" name="param" value="" />
							<input type="hidden" name="gw_type" value="<%=gwType%>" />
							<input type="hidden" name="maxActive" id="maxActive"  />
							<input type="hidden" name="cqSoftCitys" id="cqSoftCitys"  />
							<input type="hidden" name="mode" id="mode"  />
							<input type="hidden" name="paramPath" id="paramPath"  />
							<input type="hidden" name="paramValue" id="paramValue"  />
							<input type="hidden" name="paramType" id="paramType"  />

			<table border=1 cellspacing=1 cellpadding=2 width="100%" align="center" class="text">
				<tr>
					<td>
						<table width="100%" height="30" border="0" cellspacing="0"
							cellpadding="0" class="green_gargtd">
							<tr>
								<td width="162" align="center" class="title_bigwhite" nowrap>
									批量配置
								</td>
								<td nowrap>
									<img src="<s:url value='/images/attention_2.gif'/>" width="15"
										height="12">

								</td>
							</tr>
						</table>
					</td>
				</tr>
							<TR bgcolor="#FFFFFF" id="gwShare_tr23"  >
								<TD align="right" class="column" width="15%">属    地</TD>
								<TD align="left" width="35%" class="column">
									<div id="cqsoftCity">
										<INPUT onclick="ChkAllCity()" type="checkbox" value="" id="cityAll">全选
										<INPUT type="checkbox" value="00" name="city" onclick="ChkCity()">省中心
										<INPUT type="checkbox" value="QL0" name="city" onclick="ChkCity()">酉阳
										<INPUT type="checkbox" value="CA0" name="city" onclick="ChkCity()">璧山
										<INPUT type="checkbox" value="QP0" name="city" onclick="ChkCity()">彭水
										<INPUT type="checkbox" value="QQ0" name="city" onclick="ChkCity()">黔江
										<INPUT type="checkbox" value="QS0" name="city" onclick="ChkCity()">石柱
										<INPUT type="checkbox" value="QX0" name="city" onclick="ChkCity()">秀山
										<INPUT type="checkbox" value="WC0" name="city" onclick="ChkCity()">城口
										<INPUT type="checkbox" value="WF0" name="city" onclick="ChkCity()">奉节
										<INPUT type="checkbox" value="WK0" name="city" onclick="ChkCity()">开州
										<INPUT type="checkbox" value="WL0" name="city" onclick="ChkCity()">梁平
										<INPUT type="checkbox" value="WS0" name="city" onclick="ChkCity()">巫山
										<INPUT type="checkbox" value="WW0" name="city" onclick="ChkCity()">万州
										<INPUT type="checkbox" value="WX0" name="city" onclick="ChkCity()">巫溪
										<INPUT type="checkbox" value="WY0" name="city" onclick="ChkCity()">云阳
										<INPUT type="checkbox" value="WZ0" name="city" onclick="ChkCity()">忠县
										<INPUT type="checkbox" value="CY0" name="city" onclick="ChkCity()">九龙坡
										<INPUT type="checkbox" value="CZ0" name="city" onclick="ChkCity()">垫江
										<INPUT type="checkbox" value="FF0" name="city" onclick="ChkCity()">丰都
										<INPUT type="checkbox" value="FH0" name="city" onclick="ChkCity()">涪陵
										<INPUT type="checkbox" value="FL0" name="city" onclick="ChkCity()">南川
										<INPUT type="checkbox" value="FW0" name="city" onclick="ChkCity()">武隆
										<INPUT type="checkbox" value="CB0" name="city" onclick="ChkCity()">巴南
										<INPUT type="checkbox" value="CC0" name="city" onclick="ChkCity()">长寿
										<INPUT type="checkbox" value="CD0" name="city" onclick="ChkCity()">大足
										<INPUT type="checkbox" value="CE0" name="city" onclick="ChkCity()">綦江
										<INPUT type="checkbox" value="CF0" name="city" onclick="ChkCity()">渝北
										<INPUT type="checkbox" value="CG0" name="city" onclick="ChkCity()">江北
										<INPUT type="checkbox" value="CH0" name="city" onclick="ChkCity()">永川
										<INPUT type="checkbox" value="CI0" name="city" onclick="ChkCity()">北碚
										<INPUT type="checkbox" value="CJ0" name="city" onclick="ChkCity()">两江新区
										<INPUT type="checkbox" value="CK0" name="city" onclick="ChkCity()">合川
										<INPUT type="checkbox" value="CL0" name="city" onclick="ChkCity()">潼南
										<INPUT type="checkbox" value="CM0" name="city" onclick="ChkCity()">大渡口
										<INPUT type="checkbox" value="CN0" name="city" onclick="ChkCity()">南岸
										<INPUT type="checkbox" value="CP0" name="city" onclick="ChkCity()">沙坪坝
										<INPUT type="checkbox" value="CR0" name="city" onclick="ChkCity()">荣昌
										<INPUT type="checkbox" value="CS0" name="city" onclick="ChkCity()">渝中
										<INPUT type="checkbox" value="CT0" name="city" onclick="ChkCity()">铜梁
										<INPUT type="checkbox" value="CU0" name="city" onclick="ChkCity()">江津
									</div>
								</TD>
								<TD align="right" class=column width="15%">厂    商</TD>
								<TD width="35%" class="column">
									<select name="gwShare_vendorId" class="bk" onchange="gwShare_change_select('deviceModel','-1')">
										<option value="-1">==请选择==</option>
									</select>
								</TD>
							</TR>
							<TR bgcolor="#FFFFFF" id="gwShare_tr22" >
								<TD align="right" class=column width="15%">设备型号</TD>
								<TD align="left" width="85%" class="column" colspan="3">
									<select name="gwShare_deviceModelId" class="bk" onchange="gwShare_change_select('devicetype','-1')">
										<option value="-1">请先选择厂商</option>
									</select>
								</TD>
								<%-- <TD align="right" class=column width="15%">设备版本</TD>
								<TD width="35%" class="column">
									<select name="gwShare_devicetypeId" class="bk"">
										<option value="-1">请先选择设备型号</option>
									</select>
								</TD> --%>
							</TR>
							<TR>
								<TD class=column width="15%" align='right'>
									开始时间
								</TD>
								<TD width="35%" class="column">
									<input type="text" name="startOpenDate" readonly class=bk
										value="<s:property value="startOpenDate" />">
									<img name="shortDateimg"
										onClick="WdatePicker({el:document.frm.startOpenDate,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
										src="../../images/dateButton.png" width="15" height="12"
										border="0" alt="选择">
									&nbsp;
									<font color="red"> *</font>
								</TD>
								<TD class=column width="15%" align='right'>
									结束时间
								</TD>
								<TD width="35%" class=column>
									<input type="text" name="endOpenDate" readonly class=bk
										value="<s:property value="endOpenDate" />">
									<img name="shortDateimg"
										onClick="WdatePicker({el:document.frm.endOpenDate,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
										src="../../images/dateButton.png" width="15" height="12"
										border="0" alt="选择">
									&nbsp;
									<font color="red"> *</font>
								</TD>
							</TR>
							<tr>
								<td colspan="4" align="right" class=column>
									<button type="button" id="doAddButton" onclick="doAdd();" class=btn>
										增加节点
									</button>
								</td>
							</tr>
							<%-- <TR bgcolor="#FFFFFF">
								<td nowrap align="right" class=column width="15%">
									<span ><a href="javascript:doDelete()">删除    |</a></span>参数节点路径
								</td>
								<td width="85%" colspan="3" >
									<input type="text" id="paramPath" name="paramPath" style="width:800px;"
										value="InternetGatewayDevice.Services.X_CT-COM_MWBAND.TotalTerminalNumber" >
								</td>
							</TR>
							<TR bgcolor="#FFFFFF">
								<td align="right" class=column width="15%">参数值</td>
								<td width="35%" >
									<input type="text" id="paramValue" name="paramValue" value=""/>
								</td>
								<td align="right" class=column width="15%">参数类型</td>
								<td width="35%" >
									<select name="paramType0" id="paramType0" cssClass="bk">
									 	<option value="-1">==请选择==</option>
									 	<option value="1">string</option>
									 	<option value="2">int</option>
									 	<option value="3">unsignedInt</option>
									 	<option value="4">boolean</option>
									 </select>
									 <input type="text" id="paramType" name="paramType" value=""/>
								</td>
							</TR> --%>
							<TR>
									<TD class=column colspan="4">
										 <div id="fatherDiv">
											<div id="paramAddDiv0">
												<table border=0 cellspacing=0 cellpadding=0 width="100%">
												<TR bgcolor="#FFFFFF">
													<td nowrap align="right" class=column width="20%">
														<span ><a href="javascript:doDelete()">删除    |</a></span>参数节点路径
													</td>
													<td width="70%" colspan="3" class=column>
														<input type="text" id="paramNodePath0" name="paramNodePath0" style="width:800px;"
															value="" >
													</td>
												</TR>
												<TR bgcolor="#FFFFFF">
													<td align="right" class=column width="20%">参数值</td>
													<td width="30%" class=column>
														<input type="text" id="paramValue0" name="paramValue0" value=""/>
													</td>
													<td align="right" class=column width="30%">参数类型</td>
													<td width="70%" class=column>
														<select name="paramType0" id="paramType0" cssClass="bk">
														 	<option value="-1">==请选择==</option>
														 	<option value="1">string</option>
														 	<option value="2">int</option>
														 	<option value="3">unsignedInt</option>
														 	<option value="4">boolean</option>
														 </select>
													</td>
												</TR>
												</table>
											</div>
										</div>
									</TD>
								</TR>
							<tr bgcolor="#FFFFFF" >
								<TD align="right" width="15%" class=column>
									配置策略方式(默认重启触发)：
								</TD>
								<TD width="85%" colspan="3" class=column>
									<INPUT type="checkbox" value="1" name="mode1" onclick="ChkMode()">终端启动
									<INPUT type="checkbox" value="2" name="mode1" onclick="ChkMode()">Inform周期上报信息
								</TD>
							</tr>
							<TR>
								<TD colspan="4" align="right" CLASS="green_foot">
									<INPUT TYPE="submit" value=" 执 行 " class=btn>
								</TD>
							</TR>
			</table>
			</FORM>
		</TD>
	</TR>
</TABLE>
<%@ include file="../foot.jsp"%>
