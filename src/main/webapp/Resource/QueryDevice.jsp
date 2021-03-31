<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK" isELIgnored="false"  %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<jsp:useBean id="DeviceAct" scope="request"
	class="com.linkage.litms.resource.DeviceAct" />
<jsp:useBean id="DeviceDAO" scope="request"
	class="com.linkage.module.gwms.dao.gw.DeviceDAO" />
<%@ page import="com.linkage.litms.common.database.*"%>
<%@ include file="../head.jsp"%>
<%@ include file="../toolbar.jsp"%>
<%@taglib prefix="ms" uri="/ailk-itms-web-tags"%>
<%@page import="com.linkage.litms.LipossGlobals"%>

<%
	request.setCharacterEncoding("GBK");
	String strCityList = DeviceAct.getCityListSelf(false, "", "", request);
	String strVendorList = DeviceAct.getVendorList(true, "", "");
	String gw_type = request.getParameter("gw_type");
	//根据用户过滤管理域
	//String strUserDomains = DeviceAct.QueryArea(request);
	String strDevTypeList = com.linkage.litms.common.util.FormUtil.createListBox(
			DeviceDAO.getDevType(), "type_name", "type_name", false, "", "");
	String InstArea=LipossGlobals.getLipossProperty("InstArea.ShortName");
%>
<script type="text/javascript" src="../Js/jquery.js"></script>
<script type="text/javascript" src="../Js/My97DatePicker/WdatePicker.js"></script>
<SCRIPT LANGUAGE="JavaScript" SRC="../Js/prototype.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript">
//缓存时间
var starttimetemp = "";
var endtimetemp = "";

function showChild(parname){
	if(parname=="vendor_id"){
	    var type=1;		
		var o = $("vendor_id");
		var id = o.options[o.selectedIndex].value;
		var url = "getDeviceChild.jsp";
		var pars = "vendor_id=" + id;
		pars +="&type="+type;
		var myAjax
			= new Ajax.Request(
							url,
							{method:"post",parameters:pars,
							onComplete:function(req){getData(type,req);},
							onFailure:showError}						
						   );
		$("sp_DeviceVersion").innerHTML = "";
	}
    if(parname=="device_model_id")
    {
        var type=2;
		o =$("device_model_id");
		id=o.options[o.selectedIndex].value;
		var url = "getDeviceChild.jsp";
		pars ="device_model_id="+id;
		pars +="&type="+type;
		var myAjax
			= new Ajax.Request(
							url,
							{method:"post",parameters:pars,
							onComplete:function(req){getData(type,req);},
							onFailure:showError}						
						   );
    }
}


function getData(type ,request)
{
 //alert("wp!");
 if(type==1)
 {
    $("sp_DeviceModel").innerHTML = " 设备型号：" + request.responseText;
 }
 else if(type==2)
 {
    $("sp_DeviceVersion").innerHTML = " 设备版本：" + request.responseText;
 }
}
function getDeviceModel(request){
	$("sp_DeviceModel").innerHTML = " 设备型号：" + request.responseText;
}
//Debug
function showError(request){
	alert(request.responseText);
}
function CheckForm(){
	document.getElementById("queryBtn").disabled=true;
	//设备序列号
	var device_serial = document.frm.device_serial.value;
	//设备IP
	var loopback_ip = document.frm.loopback_ip.value;
	
	//地域
	var address = document.frm.address.value;
	//设备MAC地址
	var mac = document.frm.cpe_mc.value;
	//设备LOID
	var loid = document.frm.device_id_ex.value;
	
	var queryTypeValue = document.frm.queryType.value;
	//var queryTypeValue = $("input[@name='queryType']");
	//有几个省份是必须输入设备序列号，或者Ip，有的不是，这边区分一下，暂时黑龙江电信高级查询不限制
	
	if(null != queryTypeValue && "高 级 查 询" == queryTypeValue)
	{
		//2个参数至少一个
		if(device_serial.length == 0 && loopback_ip.length == 0)
		{
		alert("必选参数请至少输入一个！"); 	
		document.getElementById("queryBtn").disabled=false;
			document.frm.device_serial.focus();
			return false;
		}
	}
	
	if(null != queryTypeValue && "简 单 查 询" == queryTypeValue){
		// 开始时间
		var starttime = document.getElementsByName("starttime")[0].value;
		
		// 以下四个参数只要有一个参数有值，就可以执行查询
		if(device_serial.length != 0 || loopback_ip.length != 0 || mac.length != 0 || loid.length != 0){
		}else{ // 如果四个参数都没有值，再看第一次上报时间的开始时间有没有值，如果有值也可以执行查询，否则不能执行查询
			if(null != starttime && "" != starttime){
			}else{
				alert("设备序列号、设备IP地址、设备MC地址、设备LOID、上报开始时间必须输入一个！");
				document.getElementById("queryBtn").disabled=false;
				document.frm.device_serial.focus();
				return false;
			}
		}
		
		
	}
	//如果输了设备序列号，就必须合法
	if(device_serial.length>0 && device_serial.length<6){
		alert("请至少输入最后6位设备序列号进行查询！");
		document.getElementById("queryBtn").disabled=false;
		document.frm.device_serial.focus();
		return false;
	}
	
	return true;
}

/**
 * 转换查询参数方式
 */
function changeQueryArgs()
{
	
	var queryTypeValue = document.frm.queryType.value;
	//alert(queryTypeValue);
	if(null != queryTypeValue && "高 级 查 询" == queryTypeValue)
	{
		//var time = document.getElementsByName("starttime")[0].value;
		//if(null == time || "" == time)
		//{
			//读取缓存时间
			//document.getElementsByName("starttime")[0].value = starttimetemp;
			document.getElementsByName("starttime")[0].value = "";  // modify by zhangchy 2011-10-24 应新疆要求starttime默认值不要了
			document.getElementsByName("endtime")[0].value = endtimetemp;
			//initDate();
		//}
		//alert("修改前" + queryTypeValue);
		document.frm.queryType.value = "简 单 查 询";
		//alert("修改后" + document.frm.queryType.value);
		//隐藏多余的参数
		document.getElementById("args2").style.display = "";
		document.getElementById("args3").style.display = "";
		document.getElementById("args4").style.display = "";
		document.getElementById("args5").style.display = "";
		document.getElementById("args6").style.display = "";
		//打开关键参数标注
		//$("tr[@id='red1']").css("display","none");
		//$("tr[@id='red2']").css("display","none");
		return true;
	}
	
	if(null != queryTypeValue && "简 单 查 询" == queryTypeValue)
	{
		
		//alert("修改前" + queryTypeValue);
		document.frm.queryType.value = "高 级 查 询";
		//alert("修改后" + document.frm.queryType.value);
		//打开所有参数
		document.getElementById("args2").style.display = "none";
		document.getElementById("args3").style.display = "none";
		document.getElementById("args4").style.display = "none";
		document.getElementById("args5").style.display = "none";
		document.getElementById("args6").style.display = "none";
		//打开关键参数标注
		//$("tr[@id='red1']").css("display","");
		//$("tr[@id='red2']").css("display","");
		
		//缓存时间，并且清空时间
		starttimetemp = document.getElementsByName("starttime")[0].value;
		document.getElementsByName("starttime")[0].value = null;
		endtimetemp = document.getElementsByName("endtime")[0].value;
		document.getElementsByName("endtime")[0].value = null;
		
		//alert("asd");
		return true;
	}
}

function initDate()
{
	//初始化时间  开启 by zhangcong 2011-06-02
	var theday=new Date();
	var day=theday.getDate();
	var month=theday.getMonth()+1;
	var year=theday.getFullYear();
	var hour = theday.getHours();
	var mimute = theday.getMinutes();
	var second = theday.getSeconds();

	//alert("22");
	//modify by zhangcong 开始时间默认为当年的第一天2011-06-02
	//document.getElementsByName("starttime")[0].value=year+"-1-1 00:00:00";
	//$("input[@name='starttime']").val(year+"-1-1 00:00:00");

	//alert("333");
	//modify by zhangcong 结束时间默认为当天
	//document.getElementsByName("endtime")[0].value=year+"-" + month + "-" + day + " " + hour +":"+ mimute +":" + second;
	//$("input[@name='endtime']").val(year+"-" + month + "-" + day + " " + hour +":"+ mimute +":" + second);
	//$("input[@name='endtime']").val($.now("-",true));

	//缓存时间
	starttimetemp = year+"-1-1 00:00:00";
	endtimetemp = year+"-" + month + "-" + day + " " + hour +":"+ mimute +":" + second;
	//alert("444");
}

function init(){
    var type=<%=gw_type %>;
    
    if(type==3){
            
            document.getElementById("args1").style.display = "block";
    }else{
            document.getElementById("args1").style.display = "none";
    }
}

window.onload=init;

</SCRIPT>

<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD HEIGHT=20>
			&nbsp;
		</TD>
	</TR>
	<TR>
		<TD>
			<FORM NAME="frm" METHOD="get" ACTION="QueryDeviceList.jsp" onsubmit="return CheckForm()" > 
				<TABLE width="95%" border=0 cellspacing=0 cellpadding=0
					align="center">
					<tr>
						<td>
							<table width="100%" height="30" border="0" cellspacing="0"
								cellpadding="0" class="green_gargtd">
								<tr>
									<td width="162" align="center" class="title_bigwhite">
										设备资源
									</td>
									<td>
										<img src="../images/attention_2.gif" width="15" height="12">
										对设备信息进行查询
									</td>
								</tr>
							</table>
						</td>
					</tr>

					<TR>
						<TD bgcolor=#999999>
							<TABLE border=0 cellspacing=1 cellpadding=2 width="100%"
								id="outTable">
								<TR>
									<TH bgcolor="#ffffff" colspan="4" align="center">
										设备查询
									</TH>
								</TR>
								<TR>
									<TD class="column" align="right" height="23" width="15%">
										设备序列号
									</TD>
									<TD bgcolor="#ffffff" align="left" height="23" width="35%">
										<input id="devSn" type="text" value="" name="device_serial" class="bk">
										<font color="red">*</font>
									</TD>
									<TD class="column" align="right" height="23" width="15%">
										设备IP地址
									</TD>
									<TD bgcolor="#ffffff" align="left" height="23" width="35%">
										<input type="text" value="" name="loopback_ip" class="bk">
										<font color="red">*</font>
									</TD>
								</TR>
								
									<TR  id="args1">
									
											<TD class="column" align="right" height="23">
													客户类型
											</TD>
											<TD bgcolor="#ffffff" align="left" height="23" colspan="3">
													<select class="bk"  name="he_type">
														<option value="0">==请选择==</option>
														<option value="1">==家庭用户==</option>
														<option value="2">==企业用户==</option>
													</select>
											</TD>
											<TD class="column" align="right" height="23"  >
											<input type="button" value="判断" onclick="init()" />
											</TD>
									 </TR>
								
								<!-- 此处分割 -->
								<TR id="args2" STYLE="display:none">
									<TD class="column" align="right" height="23">
										设备MC地址
									</TD>
									<TD bgcolor="#ffffff" align="left" height="23">
										<input type="text" value="" name="cpe_mc" class="bk">
										<input type="hidden" value="<%=LipossGlobals.getLipossProperty("InstArea.ShortName") %>" name="address" >
										<font color="red">*</font>
									</TD>
									
									<TD class="column" align="right" height="23">
										<ms:inArea areaCode="sx_lt">
											唯一标识
										</ms:inArea>
										<ms:inArea areaCode="sx_lt" notInMode="true">
											LOID
										</ms:inArea>
									</TD>
									<TD bgcolor="#ffffff" align="left" height="23">
										<input id="loid" type="text" value="" name="device_id_ex" class="bk">
										<font color="red">*</font>
									</TD>
								</TR>
								<TR id="args3" STYLE="display:none">
									<TD class="column" align="right" height="23">
										最近一次上报时间
									</TD>
									<TD bgcolor="#ffffff" align="left" height="23">
										<input type="text" name="starttime" class='bk' readonly
											value="<s:property value='starttime' />">
										<img name="shortDateimg"
											onClick="WdatePicker({el:document.frm.starttime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
											src="../images/dateButton.png" width="15" height="12"
											border="0" alt="选择">
											<font color="red">*</font>
									</TD>
									<TD class="column" align="right" height="23" width="15%">
										&nbsp;至&nbsp;
									</TD>
									<TD bgcolor="#ffffff" align="left" height="23" width="35%">
										<input type="text" name="endtime" class='bk' readonly
											value="<s:property value='endtime' />">
										<img name="shortDateimg"
											onClick="WdatePicker({el:document.frm.endtime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
											src="../images/dateButton.png" width="15" height="12"
											border="0" alt="选择">
											<font color="red"></font>
									</TD>
								</TR>
								<TR id="args4" STYLE="display:none">
									<!-- <TD class="column" align="right" height="23" >管理域</TD>
							<TD bgcolor="#ffffff" align="left" height="23" >
							strUserDomains-->
									<TD class="column" align="right" height="23">
										上线状态
									</TD>
									<TD bgcolor="#ffffff" align="left" height="23">
										<select name="status" class="bk">
											<option value="-1">
												==请选择==
											</option>
											<option value="0">
												下线
											</option>
											<option value="1">
												在线
											</option>
											<option value="2">
												未知
											</option>
										</select>
									</TD>
									<% if("gs_dx".equals(InstArea)){%>
									<TD class="column" align="right" height="23">
										设备版本类型
									</TD>
									<TD bgcolor="#ffffff" align="left" height="23">
									
									<select name="device_version_type" class="bk">
											<option value="-1" >==请选择==</option>
											<option value="1">E8-C</option>
											<option value="2">PON融合</option>
											<option value="3">10GPON</option>
											<option value="4">政企网关</option>
											<option value="5">天翼网关1.0</option>
											<option value="6">天翼网关2.0</option>
											<option value="7">天翼网关3.0</option>
										</select>
										</TD>
									<%}else{ %>
									<TD class="column" align="right" height="23">
										终端类型
									</TD>
									<TD bgcolor="#ffffff" align="left" height="23">
									<%=strDevTypeList%>
									</TD>
									
									<%} %>
									
								</TR>
								<TR id="args5" STYLE="display:none">
									<TD class="column" align="right" height="23">
										属地
									</TD>
									<TD bgcolor="#ffffff" align="left" height="23">
										<%=strCityList%>
									</TD>
									<TD class="column" align="right" height="23">
										终端状态
									</TD>
									<TD bgcolor="#ffffff" align="left" height="23">
										<select class="bk" name="device_status">
											<option value="-1">全部</option>
											<option value="1">已确认</option>
											<option value="0">未确认</option>
										</select>
									</TD>
								</TR>
								<TR id="args6" STYLE="display:none">
									<TD class="column" align="right" height="23">
										厂商
									</TD>
									<TD bgcolor="#ffffff" align="left" height="23" nowrap  colspan="3" >
										<div>
											<span><%=strVendorList%></span> &nbsp;
											<span id="sp_DeviceModel"></span> &nbsp;
											<span id="sp_DeviceVersion"></span>
										</div>
									</TD>
								</TR>
								
								<TR>
									<TD class="column" align="right" height="23" colspan=4>
										<input type="hidden" name="gw_type" value="<%=gw_type%>">
										<input type=submit name="queryBtn" id="queryBtn" class=jianbian value=" 查 询 ">&nbsp; 
										<ms:inArea areaCode="js_dx" notInMode="true">
											<input type=button name="queryType" class=jianbian value="高 级 查 询" onClick="changeQueryArgs()">
										</ms:inArea>
									</TD>
								</TR>
							</TABLE>
						</TD>
					</TR>
				</TABLE>
			</FORM>
		</TD>
	</TR>
	<TR>
		<TD HEIGHT=20>
			<IFRAME name=childFrm SRC="" STYLE="display: none"></IFRAME>
			&nbsp;
		</TD>
	</TR>
</TABLE>
<script type="text/javascript">

	//alert("111");
	document.getElementsByName("starttime")[0].value = "";
	document.getElementsByName("endtime")[0].value = "";
	initDate();
</script>
<%@ include file="../foot.jsp"%>
