<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html; charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ include file="../head.jsp"%>
<%@ include file="../toolbar.jsp"%>
<%
	/* String gwType = request.getParameter("gw_type"); */
	
	// ��ȡ��è������Ϣ��ʽ query_type��1�����ѯITMS�ӿڣ�2�����ѯ���Žӿڣ�3�����ѯ���ݿ�
	String query_type = request.getParameter("query_type");
	// ���δ��ȡ�����ݣ�����next_handle�����Ƿ���������ӿڣ�2���������һ���ӿڣ�����2�Ļ������ѯ���ݿ⣨�Ѿ���ѯ���⣩
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
			<td valign="middle"><span id="txtLoading" style="font-size:14px;font-family: ����">���Եȡ�����������</span></td>
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
									��ͥ��������
								</td>
								<td nowrap>
									<img src="../images/attention_2.gif" width="15" height="12">
									ѡ���豸չʾ��������ͼ��
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
													�豸����
													<%-- <input type="hidden" name ="gw_type" value="<%=gwType %>"/> --%>
												</td>
												<td id="tdDeviceCityName" width="35%">
												</td>
												<td nowrap align="right" class=column width="15%">
													�豸���к�
													<input type="hidden" name="device_id" value="">
													<input type="hidden" name="query_type" value="<%=query_type %>" />
													<input type="hidden" name="next_handle" value="<%=next_handle %>" />
												</td>
												<td id="tdDeviceSn" width="35%">
												</td>
											</TR>
											<TR bgcolor="#FFFFFF">
												<TD colspan="4" align="right" class="green_foot">
													<input type="button" class=jianbian onclick="javascript:query()" value=" �� ȡ " />
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
								<th colspan="2">�豸������Ϣ</th>
							</tr>
							<tr>
								<td class=column align="right" width="40%">�豸���к�</td>
								<td align="center" id="sn"></td>
							</tr>
							<tr>
								<td class=column align="right" width="40%">LOID</td>
								<td align="center" id="loid"></td>
							</tr>
							<tr>
								<td class=column align="right" width="40%">����</td>
								<td align="center" id="vendor"></td>
							</tr>
							<tr>
								<td class=column align="right" width="40%">�ͺ�</td>
								<td align="center" id="model"></td>
							</tr>
							<tr>
								<td class=column align="right" width="40%">Ӳ���汾</td>
								<td align="center" id="hardwareversion"></td>
							</tr>
							<tr>
								<td class=column align="right" width="40%">����汾</td>
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
								<td class=column align="right" width="40%">�绰</td>
								<td align="center" id="phone"></td>
							</tr>
						</table>
						�������ݻ�ȡʱ�䣺<span id="timestamp"></span>
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

// ��ȡ����������Ϣ
var query = function(){
	// У��
	var __device_id = $("input[@name='device_id']").val();

	if(__device_id == null || __device_id == ""){
		alert("���Ȳ�ѯ�豸!");
		return;
	}
	showMsgDlg();
	// ��������
	$.ajax({
		type:"Post",
		url:"<s:url value="/gwms/resource/FamilyNetTopnACT!query.action"/>",
		data:$("#frm").serialize(),
		dataType:'json',
		success:function(data){
			closeMsgDlg();
			$("#topo")[0].style.display = "block";
			// ��è��Ϣ
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
			
			// �¹���Ϣ
			var topo_info = data.topo_info;
			getTopo(gw_info,topo_info);			
		},
		error:function(e){
			closeMsgDlg();
			alert("�������쳣");
			console.info("e",e);
		}
	});
}

//����ͼ
function getTopo(gw_info,topo_info){
	
	var catImage = getCatImage(gw_info,topo_info);
	var onlineLanPortArr = getOnLineLanPort(gw_info,topo_info);
	//������Ҫ���ɵĽڵ�
	var allnodes = [
        {
        	id: gw_info.mac, 
        	label: "��������" 
        		+ '\r\n����������������������������\r\n' 
        		+ "������LAN�ڣ�" 
        		+ onlineLanPortArr.join("��"),
        	title: "MAC��"+gw_info.mac,
        	image: catImage, 
        	shape: 'image'
        }
	 ];  
	 
	//����ڵ�������
	var alledges = [];
	
	//var topoMap = {};
	
	// ����topo��Ϣ
	for (var i = 0; i < topo_info.length; i++) {
		var item = topo_info[i];
		
		var nodeLable = getNodeLable(item);
		
		// �ڵ�
		var node = {
			id: item.mac,
			label: nodeLable,
			/* title: 'ϵͳ��' + item.os, */
			image: '',
			shape: 'image'
		};
		// ������
		var edge = {
			id: item.mac,
			from: item.par_mac, 
			to: item.mac, 
			title: '',
			dashes:false
		}
		 
		//�豸���ͣ���ͬ�豸չʾ��Ӧ��ͼƬ
		if(item.dev_type == 'PC'){// ����
	 		node.image = '../images/topo/computer.png';
 		}else if(item.dev_type == 'elink'){// �ֻ�
 			node.image = '../images/topo/wifi.png';
 		}else if(item.dev_type == 'phone'){// �ֻ�
 			node.image = '../images/topo/phone.png';
 		}else{// �����豸
 			node.image = '../images/topo/ipad.png';
 		}
		
		 // ����
	 	 if(item.acc_type == 0){
	 		edge.title = '�˿ڣ�LAN'+item.acc_port;
	 	 // ����
	 	 }else{
	 		edge.title = '��������';
	 		edge.dashes = true;
	 	 }
	 	 
	 	var mac = item.mac;
	 	//topoMap[mac] = item;
	 	 
		allnodes.push(node);
		alledges.push(edge);
	}
	
    // �����ڵ����
    var nodes = new vis.DataSet(allnodes);

    // �������߶���
    var edges = new vis.DataSet(alledges);

    // ����һ����������ͼ
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
    
    // �ڵ㵥��
   /*  network.on("selectNode",function(params){
    	var mac = params.nodes; 
    	if(mac == catMac){
    		return;
    	}
    	$scope.dev = topoMap[mac];
    	$("#details_topo").modal("show");
    }); */
}

//��ȡ�Ѿ����ӵ�lan��
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

// ����LAN���������������ʾ��Ӧ�Ķ˿�
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
	if((port + 0) > 1234){// ���lan�ڴ���4����ͳһ��ʾ4��
		catImage = '../images/topo/cat1234.png';
	}else{
		catImage = '../images/topo/cat' + port + '.png';
	}
	
	return catImage;
}

function compare(v1,v2){
    return v1-v2;
};

//ƴ�ӽڵ��ǩ
function getNodeLable(item){
	// ��ǩУ�� ��ֵ��չʾ
	var nodeLable = '';
	// �ָ�
	var separate = '\r\n--------------------------------\r\n';
	// model
	if(item.model !='' && item.model != undefined){
		nodeLable += item.model+'\r\n������������������������\r\n';
	}
	// mac
	if(item.mac != '' && item.mac != undefined){
		nodeLable += 'MAC��'+ item.mac;
	}
	// vendor
	if(item.vendor != '' && item.vendor != undefined){
		nodeLable += separate + '���̣�'+ item.vendor;
	}
	// �����豸lan��
	if(item.acc_type == 0){
		nodeLable += separate + '�˿ڣ�LAN'+item.acc_port;
	}
	// ip
	if(item.ip != '' && item.ip != undefined){
		nodeLable += separate + 'IP��'+ item.ip;
	}
	// ����״̬
	if(item.active != '' && item.active != undefined){
		nodeLable += separate + '����״̬��'+ isActive(item.active);
	}
	//����ʱ��
	if(item.online_time != 0){
		nodeLable += separate + '����ʱ����'+ secondFormat(item.online_time);
	}
	return nodeLable;
}


// ʱ���ת��Ϊ����
function dateFormat(timestamp) {     
	return new Date(parseInt(timestamp) * 1000).toLocaleString().replace(/:\d{1,2}$/,' ');     
}

// ����תʱ����
function secondFormat(second){
	var min=Math.floor(second%3600);
	return Math.floor(second/3600) + "ʱ" + Math.floor(min/60) + "��"+ second%60 + "��";
}

// ����0��1 �ж���������
function isActive(active){
	if(active == 1){
		return '����';
	}else if(active == 0){
		return '����';
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
// ֧��IE9����
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