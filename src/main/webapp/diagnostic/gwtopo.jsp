<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html; charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ include file="../head.jsp"%>
<%@ include file="../toolbar.jsp"%>
<%
	/* String gwType = request.getParameter("gw_type"); */
	
	// 获取光猫数据信息方式 query_type：1代表查询ITMS接口；2代表查询集团接口；3代表查询数据库
	String query_type = request.getParameter("query_type");
	// 如果未获取到数据，根据next_handle决定是否调用其他接口，2代表调用另一个接口，不是2的话代表查询数据库（已经查询除外）
	String next_handle = request.getParameter("next_handle");
 %>
<link href="<s:url value="/css/css_green.css"/>" rel="stylesheet" type="text/css">
<link rel="stylesheet" href="../css/vis.min.css">
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value='/Js/jquery.js'/>"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value='/Js/vis.min.js'/>"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript">
$(function(){
	gwShare_setGaoji();
	document.getElementsByName("gwShare_jiadan")[0].style.display="none";
	document.getElementsByName("gwShare_import")[0].style.display="none";
	document.getElementsByName("gwShare_up_import")[0].style.display="none";
});

function deviceResult(returnVal){
		
	$("tr[@id='trDeviceResult']").css("display","");

	$("td[@id='tdDeviceSn']").html("");
	$("td[@id='tdDeviceCityName']").html("");
	
	$("input[@name='device_id']").val(returnVal[2][0][0]);
	$("td[@id='tdDeviceSn']").append(returnVal[2][0][1]+" -"+returnVal[2][0][2]);
	$("td[@id='tdDeviceCityName']").append(returnVal[2][0][5]);	
	
}

</SCRIPT>
<div id="PendingMessage"
	style="position:absolute;z-index:3;top:240px;left:250px;width:250;height:60;border-width:1;border-style:ridge;background-color:#eeeeee;padding-top:10px;display:none">
	<center>
	<table border="0">
		<tr>
			<td valign="middle"><img src="../images/cursor_hourglas.gif"
				border="0" WIDTH="30" HEIGHT="30"></td>
			<td>&nbsp;&nbsp;</td>
			<td valign="middle"><span id="txtLoading" style="font-size:14px;font-family: 宋体">请稍等······</span></td>
		</tr>
	</table>
	</center>
</div>
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD HEIGHT=20>
			&nbsp;
		</TD>
	</TR>
	<TR>
		<TD>

			<table width="98%" border="0" align="center" cellpadding="0"
				cellspacing="0" class="text">
				<tr>
					<td>
						<table width="100%" height="30" border="0" cellspacing="0"
							cellpadding="0" class="green_gargtd">
							<tr>
								<td width="162" align="center" class="title_bigwhite" nowrap>
									家庭网络拓扑
								</td>
								<td nowrap>
									<img src="../images/attention_2.gif" width="15" height="12">
									选择设备展示网络拓扑图。
								</td>
							</tr>
						</table>
					</td>
				</tr>

				<TR bgcolor="#FFFFFF">
					<td colspan="4">
						<%@ include file="/gwms/share/gwShareDeviceQuery.jsp"%>
					</td>
				</TR>
				<tr>
					<td colspan="4">
						<FORM NAME="frm" METHOD="post" id="frm">
							<TABLE width="100%" border=0 cellspacing=0 cellpadding=0
								align="center">
								<TR>
									<TD bgcolor=#999999>
										<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
											<TR bgcolor="#FFFFFF" id="trDeviceResult"
												style="display: none">
												<td nowrap align="right" class=column width="15%">
													设备属地
													<%-- <input type="hidden" name ="gw_type" value="<%=gwType %>"/> --%>
												</td>
												<td id="tdDeviceCityName" width="35%">
												</td>
												<td nowrap align="right" class=column width="15%">
													设备序列号
													<input type="hidden" name="device_id" value="">
													<input type="hidden" name="query_type" value="<%=query_type %>" />
													<input type="hidden" name="next_handle" value="<%=next_handle %>" />
												</td>
												<td id="tdDeviceSn" width="35%">
												</td>
											</TR>
											<TR bgcolor="#FFFFFF">
												<TD colspan="4" align="right" class="green_foot">
													<input type="button" class=jianbian onclick="javascript:query()" value=" 获 取 " />
												</TD>
											</TR>
										</TABLE>
									</TD>
								</TR>
							</TABLE>
						</FORM>
					</td>
				</tr>
			</table>
		</TD>
	</TR>
	<tr id = "topo" style="display: none">
		<td>
			<table style="margin-left: 10px">
				<tr>
					<td height="300px" width="90%">
						<div style="width: 100%;height: 100%" id="mynetwork"></div>
					</td>
					<td>
						<table style="width: 240px;height: 300px;margin-right: 10px;" border="1px" cellspacing="0" cellpadding="0" rules="rows">
							<tr>
								<th colspan="2">设备基本信息</th>
							</tr>
							<tr>
								<td class=column align="right" width="40%">设备序列号</td>
								<td align="center" id="sn"></td>
							</tr>
							<tr>
								<td class=column align="right" width="40%">LOID</td>
								<td align="center" id="loid"></td>
							</tr>
							<tr>
								<td class=column align="right" width="40%">厂商</td>
								<td align="center" id="vendor"></td>
							</tr>
							<tr>
								<td class=column align="right" width="40%">型号</td>
								<td align="center" id="model"></td>
							</tr>
							<tr>
								<td class=column align="right" width="40%">硬件版本</td>
								<td align="center" id="hardwareversion"></td>
							</tr>
							<tr>
								<td class=column align="right" width="40%">软件版本</td>
								<td align="center" id="softwareversion"></td>
							</tr>
							<tr>
								<td class=column align="right" width="40%">MAC</td>
								<td align="center" id="mac"></td>
							</tr>
							<tr>
								<td class=column align="right" width="40%">IP</td>
								<td align="center" id="loopback_ip"></td>
							</tr>
							<tr>
								<td class=column align="right" width="40%">电话</td>
								<td align="center" id="phone"></td>
							</tr>
						</table>
						拓扑数据获取时间：<span id="timestamp"></span>
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<TR>
		<TD HEIGHT=20>
			&nbsp;
			<IFRAME ID=childFrm  SRC="" STYLE="display: none"></IFRAME>
			<IFRAME ID=childFrm1 SRC="" STYLE="display: none"></IFRAME>
			<IFRAME ID=childFrm2 SRC="" STYLE="display: none"></IFRAME>
		</TD>
	</TR>
</TABLE>
<%@ include file="../foot.jsp"%>


<Script LANGUAGE="JavaScript">

// 获取网络拓扑信息
var query = function(){
	// 校验
	var __device_id = $("input[@name='device_id']").val();

	if(__device_id == null || __device_id == ""){
		alert("请先查询设备!");
		return;
	}
	showMsgDlg();
	// 发送请求
	$.ajax({
		type:"Post",
		url:"<s:url value="/gwms/resource/FamilyNetTopnACT!query.action"/>",
		data:$("#frm").serialize(),
		dataType:'json',
		success:function(data){
			closeMsgDlg();
			$("#topo")[0].style.display = "block";
			// 光猫信息
			var gw_info = data.gw_info;
			
			$("#sn")[0].innerText = gw_info.sn;
			$("#loid")[0].innerText = gw_info.loid;
			$("#vendor")[0].innerText = gw_info.vendor;
			$("#model")[0].innerText = gw_info.model;
			$("#hardwareversion")[0].innerText = gw_info.hardwareversion;
			$("#softwareversion")[0].innerText = gw_info.softwareversion;
			$("#mac")[0].innerText = gw_info.mac;
			$("#loopback_ip")[0].innerText = gw_info.loopback_ip;
			$("#phone")[0].innerText = gw_info.phone;
			$("#timestamp")[0].innerText = dateFormat(data.timestamp);
			
			// 下挂信息
			var topo_info = data.topo_info;
			getTopo(gw_info,topo_info);			
		},
		error:function(e){
			closeMsgDlg();
			alert("服务器异常");
			console.info("e",e);
		}
	});
}

//拓扑图
function getTopo(gw_info,topo_info){
	
	var catImage = getCatImage(gw_info,topo_info);
	var onlineLanPortArr = getOnLineLanPort(gw_info,topo_info);
	//定义需要生成的节点
	var allnodes = [
        {
        	id: gw_info.mac, 
        	label: "天翼网关" 
        		+ '\r\n——————————————\r\n' 
        		+ "已连接LAN口：" 
        		+ onlineLanPortArr.join("、"),
        	title: "MAC："+gw_info.mac,
        	image: catImage, 
        	shape: 'image'
        }
	 ];  
	 
	//定义节点连接线
	var alledges = [];
	
	//var topoMap = {};
	
	// 遍历topo信息
	for (var i = 0; i < topo_info.length; i++) {
		var item = topo_info[i];
		
		var nodeLable = getNodeLable(item);
		
		// 节点
		var node = {
			id: item.mac,
			label: nodeLable,
			/* title: '系统：' + item.os, */
			image: '',
			shape: 'image'
		};
		// 连接线
		var edge = {
			id: item.mac,
			from: item.par_mac, 
			to: item.mac, 
			title: '',
			dashes:false
		}
		 
		//设备类型，不同设备展示相应的图片
		if(item.dev_type == 'PC'){// 电脑
	 		node.image = '../images/topo/computer.png';
 		}else if(item.dev_type == 'elink'){// 手机
 			node.image = '../images/topo/wifi.png';
 		}else if(item.dev_type == 'phone'){// 手机
 			node.image = '../images/topo/phone.png';
 		}else{// 其他设备
 			node.image = '../images/topo/ipad.png';
 		}
		
		 // 有线
	 	 if(item.acc_type == 0){
	 		edge.title = '端口：LAN'+item.acc_port;
	 	 // 无线
	 	 }else{
	 		edge.title = '无线连接';
	 		edge.dashes = true;
	 	 }
	 	 
	 	var mac = item.mac;
	 	//topoMap[mac] = item;
	 	 
		allnodes.push(node);
		alledges.push(edge);
	}
	
    // 创建节点对象
    var nodes = new vis.DataSet(allnodes);

    // 创建连线对象
    var edges = new vis.DataSet(alledges);

    // 创建一个网络拓扑图
    var container = document.getElementById('mynetwork');
    var data = {nodes: nodes, edges: edges};
    var options = {
    	interaction:{
    		navigationButtons:true,
    		hover:true
    	},
    	edges:{
    		shadow: true,
    		smooth: true,
    		arrows: {
    			to: {
    				enabled: true, scaleFactor:0.5, type:'arrow'
    			}
            },
    		arrowStrikethrough: false,
    		width: 0.5
    	},
    	nodes:{
    		chosen: true,
    		font: {
    		      size: 6, // px
    		      face: 'arial',
    		      background: '#d6efff',
    		      strokeWidth: 0, // px
    		      strokeColor: '#ffffff',
    		      align: 'left'
    		},
    		size: 15
    	},
    	physics:{
    		enabled: true,
    		repulsion: {
    		      centralGravity: 0.2,
    		      springLength: 200,
    		      springConstant: 0.05,
    		      nodeDistance: 100,
    		      damping: 0.09
    		},
    		hierarchicalRepulsion: {
    		      centralGravity: 0.0,
    		      springLength: 100,
    		      springConstant: 0.01,
    		      nodeDistance: 120
    		}
    	}
    	
    };
    var network = new vis.Network(container, data, options);
    
    // 节点单击
   /*  network.on("selectNode",function(params){
    	var mac = params.nodes; 
    	if(mac == catMac){
    		return;
    	}
    	$scope.dev = topoMap[mac];
    	$("#details_topo").modal("show");
    }); */
}

//获取已经连接的lan口
function getOnLineLanPort(gw_info,topo_info){
	var portArr = [];
	for (var i = 0; i < topo_info.length; i++) {
		var item = topo_info[i];
		if(item.par_mac == gw_info.mac){
			portArr.push(item.acc_port);
		}
	}
	portArr.sort(compare);// [1,2,3,4]
	return portArr;
}

// 根据LAN口连接情况高亮显示相应的端口
function getCatImage(gw_info,topo_info){
	
	var catImage = '../images/topo/cat.png';
	
	var portArr = getOnLineLanPort(gw_info,topo_info);
	if(portArr.length == 0){
		return catImage;
	}
	var port = '';
	for (var i = 0; i < portArr.length; i++) {
		port += portArr[i];// 1234
	}
	if((port + 0) > 1234){// 如果lan口大于4个，统一显示4个
		catImage = '../images/topo/cat1234.png';
	}else{
		catImage = '../images/topo/cat' + port + '.png';
	}
	
	return catImage;
}

function compare(v1,v2){
    return v1-v2;
};

//拼接节点标签
function getNodeLable(item){
	// 标签校验 有值就展示
	var nodeLable = '';
	// 分隔
	var separate = '\r\n--------------------------------\r\n';
	// model
	if(item.model !='' && item.model != undefined){
		nodeLable += item.model+'\r\n————————————\r\n';
	}
	// mac
	if(item.mac != '' && item.mac != undefined){
		nodeLable += 'MAC：'+ item.mac;
	}
	// vendor
	if(item.vendor != '' && item.vendor != undefined){
		nodeLable += separate + '厂商：'+ item.vendor;
	}
	// 有线设备lan口
	if(item.acc_type == 0){
		nodeLable += separate + '端口：LAN'+item.acc_port;
	}
	// ip
	if(item.ip != '' && item.ip != undefined){
		nodeLable += separate + 'IP：'+ item.ip;
	}
	// 在线状态
	if(item.active != '' && item.active != undefined){
		nodeLable += separate + '在线状态：'+ isActive(item.active);
	}
	//在线时长
	if(item.online_time != 0){
		nodeLable += separate + '在线时长：'+ secondFormat(item.online_time);
	}
	return nodeLable;
}


// 时间戳转化为日期
function dateFormat(timestamp) {     
	return new Date(parseInt(timestamp) * 1000).toLocaleString().replace(/:\d{1,2}$/,' ');     
}

// 秒数转时分秒
function secondFormat(second){
	var min=Math.floor(second%3600);
	return Math.floor(second/3600) + "时" + Math.floor(min/60) + "分"+ second%60 + "秒";
}

// 根据0或1 判断在线离线
function isActive(active){
	if(active == 1){
		return '在线';
	}else if(active == 0){
		return '离线';
	}else{
		return active;
	}
}

function showMsgDlg(){
	w = document.body.clientWidth;
	h = document.body.clientHeight;

	l = (w-250)/2;
	t = (h-60)/2;
	PendingMessage.style.left = l;
	PendingMessage.style.top  = t;
	PendingMessage.style.display="";
}
function closeMsgDlg(){
	PendingMessage.style.display="none";
}
// 支持IE9以上
$(function(){
	var fonts = document.getElementsByTagName('font');
	for (var i = 0; i < fonts.length; i++) {
		var fontStr = fonts[i].innerText;
		if(fontStr.indexOf("IE") != -1){
			fonts[i].innerText = "IE9 ";
		}
	}
})
</Script>