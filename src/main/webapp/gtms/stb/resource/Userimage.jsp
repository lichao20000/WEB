<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head lang="en">
<meta charset="UTF-8">
<!-- <meta http-equiv="X-UA-Compatible" content="IE=EmulateIE9" /> -->
<meta name="viewport"
	content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1, user-scalable=yes">
<title>详细页面</title> <link rel="stylesheet"
	href="../../../iconfont/iconfont.css">
<link href="../../../css1/detail.css" type="text/css" rel="stylesheet">
<script src="../../../Js/jquery-1.11.3.min.js"></script>
<script type="text/javascript" src="<s:url value='/Js/jquery.js'/>"></script>
<script type="text/javascript" src="/Js/jsDate/WdatePicker.js"></script>
<SCRIPT LANGUAGE="JavaScript" SRC="/Js/CheckFormForm.js"></SCRIPT>
<script type="text/javascript"
	src="<s:url value="/Js/jQeuryExtend-linkage.js"/>"></script>
</head>
<body>
<form
	action="<s:url value= '/gtms/stb/resource/UserImage!querydevice.action'/>"
	name="frm" id="frm">
<div class="box">
<div class="content">

<div class="detail">
<input type="hidden" id="ftpenable" name="ftpenable" value="">
<table width="100%">
<thead>
<td colspan="4">用户查询</td>
</thead>
<tr>
<td>查询方式</td>
<td colspan="3"> <ul class="queryType" id="queryType">
<li onclick="test()">按设备</li>
<li onclick="test1()">按用户</li>
</ul>
</td>
</tr>
<tr>
<td>LOID（设备序列号)</td>
<td colspan="3">
<input type="text" value="" name="UserName" id="UserName">
<button type="button" id="jqueryBtn" onclick="query()"
	class="btn-get ml100">查 询</button></td>
</tr>
<s:if test="getInformationList1!=null&&getInformationList1.size()>0">
<thead>
<td colspan="4">用户基本信息</td>
</thead>
<s:if test="getInformationList!=null">
	<s:if test="getInformationList.size()>0">
		<s:iterator value="getInformationList">
			<tr>
			<td>用户姓名</td>
			<td><s:property value="linkman" /></td>
			<td>用户住址</td>
			<td><s:property value="linkaddress" /></td>
			</tr>
			<tr>
			<td>身份证号码</td>
			<td><s:property value="linkman_credno" /></td>
			<td>联系电话</td>
			<td><s:property value="linkphone" /></td>
			</tr>
			<tr>
			<td>宽带账号</td>
			<td><s:property value="username" /></td>
			<td>签约速率</td>
			<td><s:property value="bandwidth" /></td>
			</tr>
			<tr>
			<td>逻辑ID</td>
			<td><s:property value="loid" /></td>
			<td>公客/商客</td>
			<td><s:property value="cust_type" /></td>
			</tr>

			<s:if test="list!=null&&list.size()>0">
				<thead id="7">
				<td colspan="4">用户组网络</td>
				</thead>
				<tr>
				<td colspan="4"> <div class="head"></div> <div class="v-line"></div>
				<s:if test="list!=null">
					<s:if test="list.size()>0">
						<s:iterator value="list">
							<div class="ftth">
							<div class="info">
							<table>
							<tr>
							<th>终端厂家：</th>
							<td><s:property value="vendor_name" /></td>
							</tr>
							<tr>
							<th>终端型号：</th>
							<td><s:property value="device_model" /></td>
							</tr>
							<tr>
							<th>硬件版本：</th>
							<td><s:property value="hardwareversion" /></td>
							</tr>
							<tr>
							<th>软件版本：</th>
							<td><s:property value="softwareversion" /></td>
							</tr>
							<tr>
							<th>终端注册状态:</th>
							<td>已注册</td>
							</tr>
							<tr>
							<th>管理Ip:</th>
							<td><s:property value="loopback_ip" /></td>
							</tr>
							<tr>
							<th>串码：</th>
							<td><s:property value="device_serialnumber" /></td>
							</tr>
							</table>
							</div>
							</div>
							<s:if test="(listStbShow!=null&&listStbShow.size()>0)||(companynamebig!=null&&companynamebig.size()>0)||(phoneinfoList!=null&&phoneinfoList.size()>0)||(otherList!=null&&otherList.size()>0)">
							<div class="v-line"></div>
							<div class="list">
							<div id="row">
							<s:if test="listStbShow!=null&&listStbShow.size()>0">
								<s:iterator value="listStbShow">
									<s:iterator value="top" var="test">
										<div class="shebei ">
										<div class="v-line"></div>
										<p class="img">
										<img src="../../../images/luyou.png">
										</p>
										<div class="item">
										<p>
										终端种类：<span>机顶盒</span>
										</p>
										<p>
										型号：<span><s:property value="#test.stb_type" /></span>
										</p>
										<P> MAC：<span><s:property value="#test.stb_mac" /></span>
										</P>
										<p>
										IP：<span><s:property value="#test.stb_ip" /></span>
										</p>
										<P> 连接LAN口：<span><s:property value="#test.LAN" /></span>
										</P>
										<P > 手机号码<span></span> </P>
										<P> 连接口最新字节数：<span></span> </P>
										<P> 采集到的次数：<span><s:property value="#test.tote" /></span>
										</P>
										</div>
										</div>
									</s:iterator>
								</s:iterator>
							</s:if>
							<s:if test="companynamebig!=null&&companynamebig.size()>0">
								<s:iterator value="companynamebig">
									<s:iterator value="top" var="inner">
										<div class="shebei">
										<div class="v-line"></div>
										<p class="img">
										<img src="../../../images/pc.png">
										</p>
										<div class="item">
										<p class="test">
										终端种类：<span><s:property value="#inner.company_name" /></span>
										</p>
										<P> MAC：<span><s:property value="#inner.mac_address" /></span> </P>
										<p>
										IP：<span><s:property value="#inner.ipaddress" /></span>
										</p>
										<P> 连接LAN口：<span><s:property value="#inner.LAN" /></span>
										</P>
										<P > 手机号码<span></span> </P>
										<P> 连接口最新字节数：<span></span> </P>
										<P> 采集到的次数：<span><s:property value="#inner.tote" /></span>
										</P>
										</div>
										</div>
									</s:iterator>
								</s:iterator>
							</s:if>
							<s:if test="phoneinfoList!=null&&phoneinfoList.size()>0">
								<s:iterator value="phoneinfoList">
									<s:iterator value="top" var="inner1">
										<div class="shebei">
										<div class="v-line"></div>
										<p class="img">
										<img src="../../../images/mobile.png">
										</p>
										<div class="item">
										<p>
										终端种类：<span>手机</span>
										</p>
										<P>MAC：<span><s:property value="#inner1.mac_address" /></span> </P>
										<p>
										IP：<span><s:property value="#inner1.ipaddress" /></span>
										</p>
										<P> 连接LAN口：<span><s:property value="#inner1.LAN" /></span>
										</P>
										<P ><a onclick="config('<s:property value="#inner1.net_account" />')">手机号码</a></P>
										<P> 连接口最新字节数：<span></span> </P>
										<P> 采集到的次数：<span><s:property value="#inner1.tote" /></span>
										</P>
										</div>
										</div>
									</s:iterator>
								</s:iterator>
							</s:if>
							<s:if test="otherList!=null&&otherList.size()>0">
								<s:iterator value="otherList">
									<div class="shebei ">
									<div class="v-line"></div>
									<p class="img">
									<img src="../../../images/pc.png">
									</p>
									<div class="item">
									<p>
									终端种类：<span>其他</span>
									</p>
									<P> MAC：<span><s:property value="mac" /></span> </P>
										<p>
										IP：<span><s:property value="ipaddress" /></span>
										</p>
										<P> 连接LAN口：<span><s:property value="LAN" /></span>
										</P>
										<P >手机号码<span></span></P>
										<P> 连接口最新字节数：<span></span> </P>
										<P> 采集到的次数：<span><s:property value="tote" /></span>
										</P>
									</div>
									</div>
								</s:iterator>
							</s:if>
							</div>

							</div>
							</s:if>
						</s:iterator>
					</s:if>
				</s:if></td>
				</tr>
			</s:if>
		</s:iterator>
	</s:if>
	<s:else>
		<tr>
		<td colspan=4>系统中没有查询出需要的信息!</td>
		</tr>
	</s:else>
</s:if>
<s:else>
	<tr>
	<td colspan=4>系统中没有查询出需要的信息!</td>
	</tr>
</s:else>
</s:if>
</table>
</form>
</div>
</div>
</div>
</div>
</body>
<script type="text/javascript">
function test()
{
	$("#ftpenable").val("1");
}
function test1()
{
	$("#ftpenable").val("0");
}
function config(netaccount)
{
	var url = "<s:url value='/gtms/stb/resource/UserImage!phonenumber.action'/>?netaccount="
		+ netaccount ;
	window.open(url, "","left=80,top=80,width=800,height=400,resizable=yes,scrollbars=yes");
	}
function query()
{
var ftpenable = $("#ftpenable").val();
if(ftpenable==""||ftpenable==null)
{
	alert("请选择查询方式");
 	return false;
}
var UserName = $("#UserName").val();
if(UserName==""||UserName==null)
 {
 alert("请填写LOID或者宽带主账号");
 return false;
 }
 if(ftpenable==1&&UserName.length<6)
	 {
	 alert("设备序列号不可以小于6位!");
	 return false;
	 }
document.frm.submit();
}
	$(function () {
    setWidth();
    function setWidth(){
        var size = $("#row .shebei").length;
        var width = size*225;
        $("#row").width(width);
    }
});

	$(".queryType li").click(function () {
        $(this).addClass("active").siblings().removeClass("active");
    })
	//文档加载好之后，取第一个给他添加start属性。取最后一个给他附加end属性
	$(document).ready(function(){
		if($("#row").children("div").length>1)
		{
		$("#row").children("div").eq(0).addClass("start");
		$("#row").children("div").eq($("#row").children("div").length-1).addClass("end");
		}else
			{
				$("#row").children("div").eq(0).addClass("only");
			}
	})
</script>
</html>
