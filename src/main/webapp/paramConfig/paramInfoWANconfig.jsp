<%--
FileName	: paramInfoWANconfig.jsp
Date		: 2008年12月23日
Desc		: WAN配置.
--%>
<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html; charset=GBK"%>
<%
			request.setCharacterEncoding("gbk");
%>
<%@ include file="../head.jsp"%>
<link href="../css/css_green.css" rel="stylesheet" type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="../Js/jquery.js"></SCRIPT>

<SCRIPT LANGUAGE="JavaScript">
$(function(){
	gwShare_setGaoji();
});

function query2db()
{
	var device_id=$("input[@name='device_id_old_']").val();
	if(device_id == null || ""==device_id){
		alert("请选择设备");
		return false;
	}
	var page="";
	page="showWANParam.jsp?device_id="+device_id+ "&flag_boolean=true&refresh="+Math.random();
	document.all("childFrm1").src = page;
}

function query2device()
{
	var device_id=$("input[@name='device_id_old_']").val();
	if(device_id == null || ""==device_id){
		alert("请选择设备");
		return false;
	}
	var page="";
	page="showWANParam.jsp?device_id="+device_id+ "&flag_boolean=false&refresh="+Math.random()+"&gwType="+<%=request.getParameter("gw_type")%>;
	document.all("childFrm1").src = page;
}

function edit(device_id,wan_id,wan_conn_id,wan_conn_sess_id,sess_type,gather_time,enable,name,conn_type,serv_list,bind_port,username,password,ip_type,ip,mask,gateway,dns_enab,dns)
{
	var tb2 = document.getElementById("tb2");
	var trs = tb2.getElementsByTagName("tr");
	
	var showTemp_ = "";
	
	if("1"==sess_type){
		showTemp_ = "ppp";
	}else{
		showTemp_ = "ip";
	}
	
	//如果连接类型为IP时，显示相关信息，如果连接类型为PPP时，且conn_type为路由时，用户名和密码显示
	for (var i=0; i<trs.length; i++) {
		if("all"==trs[i].isShow||"edit"==trs[i].isShow){
			trs[i].style.display = "";
		}else if(showTemp_==trs[i].isShow){
			if("1"==sess_type){
				if("IP_Routed"==conn_type){
					trs[i].style.display = "";
				}else{
					trs[i].style.display = "none";
				}
				this.conn_type_style.style.display="";
			}else{
				trs[i].style.display = "";
			}
		}else{
			trs[i].style.display = "none";
		}
	}
	
	var checkradios = document.all("enable");
	
	for(var i=0;i<checkradios.length;i++)
    {
      if(enable==checkradios[i].value)
	  {
	    checkradios[i].checked=true;
	    break;
	  }
    }
    
    if("ip"==showTemp_){
    	if("DHCP"==ip_type){
	    	this.ip_style.style.display="none";
			this.mask_style.style.display="none";
			this.gateway_style.style.display="none";
			this.dns_style.style.display="none";
	    }else{
	    	this.ip_style.style.display="";
			this.mask_style.style.display="";
			this.gateway_style.style.display="";
			this.dns_style.style.display="";
	    }
    }

	var bind_port_first = bind_port.split(",");
	var checkbox_id = document.frm.bind_port;
	for(var k=0;k<checkbox_id.length;k++)
	{		
		checkbox_id[k].checked = false;
	}
	if(typeof(bind_port_first)!="undefined" && bind_port_first.length>0)
	{
		for(var i=0;i<bind_port_first.length;i++)
		{
			for(var j=0;j<checkbox_id.length;j++)
			{
				if(checkbox_id[j].value==bind_port_first[i])
				{
					checkbox_id[j].checked = true;
				}
			}
		}
	}
    
	document.all("device_id_").value = device_id;
	document.all("wan_id_").value = wan_id;
	document.all("wan_conn_id_").value =wan_conn_id;
	document.all("wan_conn_sess_id_").value =wan_conn_sess_id;
	document.all("sess_type_").value =sess_type;
	document.all("name").value =name;
	document.all("conn_type").value =conn_type;
	if("INTERNET/TR069"==serv_list){
		serv_list="TR069&INTERNET";
	}
	document.all("serv_list").value =serv_list;
	document.all("username").value =username;
	document.all("password").value =password;
	document.all("ip_type").value =ip_type;
	document.all("ip").value =ip;
	document.all("mask").value =mask;
	document.all("gateway").value =gateway;
	document.all("dns_enab").value =dns_enab;
	document.all("dns").value =dns;
	
	document.all("enable_old").value =enable;
	document.all("name_old").value =name;
	document.all("conn_type_old").value =conn_type;
	document.all("serv_list_old").value =serv_list;
	document.all("bind_port_old").value =bind_port;
	document.all("username_old").value =username;
	document.all("password_old").value =password;
	document.all("ip_type_old").value =ip_type;
	document.all("ip_old").value =ip;
	document.all("mask_old").value =mask;
	document.all("gateway_old").value =gateway;
	document.all("dns_enab_old").value =dns_enab;
	document.all("dns_old").value =dns;
}


function delWan_conn(ths, wan_conn){
	//alert(ths);
	if(!confirm("确实要从设备上删除该结点吗？")){
		return false;
	}
	var aWan_conn = wan_conn.split("|");
	if(!typeof(aWan_conn) || !typeof(aWan_conn.length)){
		alert("删除的结点不存在");
		return false;
	}
	var obj = ths;
	var device_id = aWan_conn[0];
	var wan_id = aWan_conn[1];
	var wan_conn_id = aWan_conn[2];
	var url = "manageWanConn!delWanConn.action";
	//document.all("pvc_del").isDisabled = true;
	obj.disabled = true;
	//alert(device_id+wan_id+wan_conn_id);
	$.post(url,{
      deviceId:device_id,
      wanId:wan_id,
      wanConnId:wan_conn_id
    },function(mesg){
    	alert(mesg);
    	obj.disabled = false;
    });
}

function delWan_conn_session(ths, wan_conn_session){
	if(!confirm("确实要从设备上删除该结点吗？")){
		return false;
	}
	var aWan_conn_session = wan_conn_session.split("|");
	if(!typeof(aWan_conn_session) || !typeof(aWan_conn_session.length)){
		alert("删除的结点不存在");
		return false;
	}
	var obj = ths;
	var device_id = aWan_conn_session[0];
	var wan_id = aWan_conn_session[1];
	var wan_conn_id = aWan_conn_session[2];
	var wan_conn_session_typ = aWan_conn_session[3];
	var wan_conn_session_id = aWan_conn_session[4];
	var url = "manageWanConn!delWanConnSession.action";
	//document.all("pvc_del").isDisabled = true;
	obj.disabled = true;
	//alert(device_id+wan_id+wan_conn_id+wan_conn_session_typ+wan_conn_session_id);
	$.post(url,{
      deviceId:device_id,
      wanId:wan_id,
      wanConnId:wan_conn_id,
      wanConnSessionType:wan_conn_session_typ,
      wanConnSessionId:wan_conn_session_id
    },function(mesg){
    	alert(mesg);
    	obj.disabled = false;
    });
}

function getPvc_conn(device_id,wan_id,wan_conn_id,div_flag,ppp_conn_num,ip_conn_num){
	
	var flag_boolean = "true";
	
	if(window.confirm("需要从设备上取？")){
		flag_boolean = "false";  
	}else{
		flag_boolean = "true";
	}
	var page="flag_boolean";
	page="showWANParamConn.jsp?device_id="+device_id+"&wan_id="+wan_id+"&wan_conn_id="+wan_conn_id+"&flag_boolean="+flag_boolean+"&div_flag="+div_flag+"&ppp_conn_num="+ppp_conn_num+"&ip_conn_num="+ip_conn_num+"&refresh="+Math.random();
	document.all("wan_childFrm").src = page;
}

function editWanConn(e){
	
	var device_id=document.all("device_id_").value;
	var wan_id=document.all("wan_id_").value;
	var wan_conn_id=document.all("wan_conn_id_").value;
	var wan_conn_sess_id=document.all("wan_conn_sess_id_").value;
	var sess_type=document.all("sess_type_").value;
	
	var checkradios = document.all("enable");
	var enable;
	for(var i=0;i<checkradios.length;i++)
    {
      if(checkradios[i].checked)
	  {
	    enable=checkradios[i].value;
	    break;
	  }
    }
	
	var bind_port = "";
	var tmp_flag = "1";
	
	var checkbox_id = document.frm.bind_port;
	for(var i=0;i<checkbox_id.length;i++)
	{
		if(checkbox_id[i].checked)
		{
			if("1"==tmp_flag){
	      		bind_port = checkbox_id[i].value;
	      		tmp_flag = "2"
	      	}else{
	      		bind_port = bind_port + "," + checkbox_id[i].value;
	      	}
		}
	}
	var name=document.all("name").value;
	var serv_list=document.all("serv_list").value;
	var username=document.all("username").value;
	var password=document.all("password").value;
	var ip_type=document.all("ip_type").value;
	var ip=document.all("ip").value;
	var mask=document.all("mask").value;
	var gateway=document.all("gateway").value;
	var dns=document.all("dns").value;
	var enable_old=document.all("enable_old").value;
	var name_old=document.all("name_old").value;
	var conn_type_old=document.all("conn_type_old").value;
	var serv_list_old=document.all("serv_list_old").value;
	var bind_port_old=document.all("bind_port_old").value;
	var username_old=document.all("username_old").value;
	var password_old=document.all("password_old").value;
	var ip_type_old=document.all("ip_type_old").value;
	var ip_old=document.all("ip_old").value;
	var mask_old=document.all("mask_old").value;
	var gateway_old=document.all("gateway_old").value;
	var dns_old=document.all("dns_old").value;
	var conn_type;
	
	if("1"==sess_type){	
		conn_type=document.all("conn_type").value;
	}else{
		conn_type=document.all("conn_type_old").value;
	}
	
	e.disabled = true;
	
	var url = "manageWanConnEdit!edit.action";
	
	$.post(url,{
		device_id:device_id,
		wan_id:wan_id,
		wan_conn_id:wan_conn_id,
		wan_conn_sess_id:wan_conn_sess_id,
		sess_type:sess_type,
		enable:enable,
		name:name,
		conn_type:conn_type,
		serv_list:serv_list,
		bind_port:bind_port,
		username:username,
		password:password,
		ip_type:ip_type,
		ip:ip,
		mask:mask,
		gateway:gateway,
		dns:dns,
		enable_old:enable_old,
		name_old:name_old,
		conn_type_old:conn_type_old,
		serv_list_old:serv_list_old,
		bind_port_old:bind_port_old,
		username_old:username_old,
		password_old:password_old,
		ip_type_old:ip_type_old,
		ip_old:ip_old,
		mask_old:mask_old,
		gateway_old:gateway_old,
		dns_old:dns_old
    },function(mesg){
    	alert(mesg);
    	e.disabled = false;
    });
}

function serv_list_cl(e){
	if("DHCP"==e){
		document.all("ip").value = document.all("ip_old").value
		document.all("mask").value = document.all("mask_old").value
		document.all("gateway").value = document.all("gateway_old").value
		document.all("dns").value = document.all("dns_old").value
		
    	this.ip_style.style.display="none";
		this.mask_style.style.display="none";
		this.gateway_style.style.display="none";
		this.dns_style.style.display="none";
    }else{
    	this.ip_style.style.display="";
		this.mask_style.style.display="";
		this.gateway_style.style.display="";
		this.dns_style.style.display="";
    }
}

function conn_type_cl(e){
	if("IP_Routed"==e){
		this.username_style.style.display="";
		this.password_style.style.display="";
    }else{
		this.username_style.style.display="none";
		this.password_style.style.display="none";
    }
}

function add(device_id,pvc_type,wan_id){
	
	document.all("pvc_type").value = pvc_type;
	document.all("device_id_").value = device_id;
	document.all("wan_id_").value = wan_id;
	
	var tb2 = document.getElementById("tb2");
	var trs = tb2.getElementsByTagName("tr");
	
	for (var i=0; i<trs.length; i++) {
		if("add"==trs[i].isShow){
			trs[i].style.display = "";
		}else {
			trs[i].style.display = "none";
		}
	}
	
	if("vpi/vci"==pvc_type){
		this.vpi_vci_style.style.display="";
		this.vlan_style.style.display="none";
	}else{
		this.vpi_vci_style.style.display="none";
		this.vlan_style.style.display="";
	}
	
	document.all("sess_type").value = "1";
	add_show("1");
}

function add_show(e){
	value_clear();
	var tb2 = document.getElementById("tb2");
	var trs = tb2.getElementsByTagName("tr");
	var ip_type = document.all("ip_type").value;
	var conn_type = document.all("conn_type").value;
	var pvc_type = document.all("pvc_type").value;
	var showTemp_ = "";
	
	if("1"==e){
		showTemp_ = "ppp";
	}else{
		showTemp_ = "ip";
	}
	
	//如果连接类型为IP时，显示相关信息，如果连接类型为PPP时，且conn_type为路由时，用户名和密码显示
	for (var i=0; i<trs.length; i++) {
		if("all"==trs[i].isShow||"add"==trs[i].isShow){
			trs[i].style.display = "";
		}else if(showTemp_==trs[i].isShow){
			trs[i].style.display = "";
		}else{
			trs[i].style.display = "none";
		}
	}
    
    if("vpi/vci"==pvc_type){
		this.vpi_vci_style.style.display="";
		this.vlan_style.style.display="none";
	}else{
		this.vpi_vci_style.style.display="none";
		this.vlan_style.style.display="";
	}
    
    if("ip"==showTemp_){
    	if("DHCP"==ip_type){
	    	this.ip_style.style.display="none";
			this.mask_style.style.display="none";
			this.gateway_style.style.display="none";
			this.dns_style.style.display="none";
	    }else{
	    	this.ip_style.style.display="";
			this.mask_style.style.display="";
			this.gateway_style.style.display="";
			this.dns_style.style.display="";
	    }
    }
}

function value_clear(){
	var checkbox_id = document.frm.bind_port;
	for(var i=0;i<checkbox_id.length;i++)
	{
		checkbox_id[i].checked = false;
	}
	
	var checkradios = document.all("enable");
	for(var i=0;i<checkradios.length;i++)
    {
      if("1"==checkradios[i].value)
	  {
	    checkradios[i].checked=true;
	    break;
	  }
    }
	
	document.frm.conn_type.value = "IP_Routed";
	document.frm.serv_list.value = "TR069";
	document.frm.username.value = "";
	document.frm.password.value = "";
	document.frm.ip_type.value = "DHCP";
	document.frm.ip.value = "";
	document.frm.mask.value = "";
	document.frm.gateway.value = "";
	document.frm.dns.value = "";
}

function addWanConn(e){

	var vpi = document.frm.vpi.value;
	var vci = document.frm.vci.value;
	var vlan = document.frm.vlan.value;

	var device_id = document.frm.device_id_.value;
	var wan_id = document.frm.wan_id_.value;
	var pvc_type = document.frm.pvc_type.value;
	var sess_type = document.frm.sess_type.value;
	var conn_type = document.frm.conn_type.value;
	var serv_list = document.frm.serv_list.value;
	var username = document.frm.username.value;
	var password = document.frm.password.value;
	var ip_type = document.frm.ip_type.value;
	var ip = document.frm.ip.value
	var mask = document.frm.mask.value;
	var gateway = document.frm.gateway.value;
	var dns = document.frm.dns.value;
	
	var bind_port = "";
	var tmp_flag = "1";
	
	var checkbox_id = document.frm.bind_port;
	for(var i=0;i<checkbox_id.length;i++)
	{
		if(checkbox_id[i].checked)
		{
			if("1"==tmp_flag){
	      		bind_port = checkbox_id[i].value;
	      		tmp_flag = "2"
	      	}else{
	      		bind_port = bind_port + "," + checkbox_id[i].value;
	      	}
		}
	}
	
	var checkradios = document.all("enable");
	var enable;
	for(var i=0;i<checkradios.length;i++)
    {
      if(checkradios[i].checked)
	  {
	    enable=checkradios[i].value;
	    break;
	  }
    }
	
	if("vpi/vci"==pvc_type){
		if(""==vpi || null==vpi){
			alert("请输入VPI");
			return false;
		}
		if(""==vci || null==vci){
			alert("请输入VCI");
			return false;
		}
	}else{
		if(""==vlan || null==vlan){
			alert("请输入WALN");
			return false;
		}
	}

	if("1"==sess_type&&"IP_Routed"==conn_type){
		if(""==username || null==username){
			alert("请输入用户名");
			return false;
		}
		if(""==password || null==password){
			alert("请输入密码");
			return false;
		}
	}
	
	if("2"==sess_type&&"STATIC"==ip_type){
		if(""==ip || null==ip){
			alert("请输入地址");
			return false;
		}
		if(""==mask || null==mask){
			alert("请输入MASK");
			return false;
		}
		if(""==gateway || null==gateway){
			alert("请输入网关");
			return false;
		}
		if(""==dns || null==dns){
			alert("请输入DNS");
			return false;
		}
	}
	
	e.disabled = true;
	
	var url = "manageWanConnEdit!add.action";
	
	$.post(url,{
		vpi:vpi,
		vci:vci,
		vlan:vlan,
		device_id:device_id,
		wan_id:wan_id,
		pvc_type:pvc_type,
		sess_type:sess_type,
		enable:enable,
		conn_type:conn_type,
		serv_list:serv_list,
		bind_port:bind_port,
		username:username,
		password:password,
		ip_type:ip_type,
		ip:ip,
		mask:mask,
		gateway:gateway,
		dns:dns
    },function(mesg){
    	alert(mesg);
    	e.disabled = false;
    });
	
}
function deviceResult(returnVal){
		
		$("tr[@id='trDeviceResult']").css("display","");

		$("td[@id='tdDeviceSn']").html("");
		$("td[@id='tdDeviceCityName']").html("");
		
		for(var i=0;i<returnVal[2].length;i++){
			$("input[@name='device_id_old_']").val(returnVal[2][i][0]);
			$("td[@id='tdDeviceSn']").append(returnVal[2][i][1]+" -"+returnVal[2][i][2]);
			$("td[@id='tdDeviceCityName']").append(returnVal[2][i][5]);		
		}
	}
</SCRIPT>
<%@ include file="../toolbar.jsp"%>
<FORM NAME="frm" METHOD="post" >
<input type="hidden" name="pvc_type" value=""/>
<input type="hidden" name="device_id_old_"/>
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD HEIGHT=20>
			&nbsp;
		</TD>
	</TR>
	<TR>
		<TD>
			<table width="100%" height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
				<tr>
					<td width="162" align="center" class="title_bigwhite" nowrap>
						WAN配置
					</td>
					<td nowrap>
						<img src="../images/attention_2.gif" width="15" height="12">
					</td>
				</tr>
			</table>
			<TABLE width="100%" border=0 cellspacing=0 cellpadding=0 align="center">
				<TR>
					<TD bgcolor=#999999>
						<TABLE id="tb1" border=0 cellspacing=1 cellpadding=2 width="100%">
								<TR bgcolor="#FFFFFF">
									<td colspan="4">
										<%@ include file="/gwms/share/gwShareDeviceQuery.jsp"%>
									</td>
								</TR>
								<TR bgcolor="#FFFFFF" id="trDeviceResult" style="display: none">
									<td nowrap align="right" class=column>
										设备属地
									</td>
									<td id="tdDeviceCityName">
									</td>
									<td nowrap align="right" class=column>
										设备序列号
									</td>
									<td id="tdDeviceSn">
									</td>

								</TR>
								<TR>
								<TD colspan="4" align="right" CLASS="green_foot">
									<INPUT TYPE="button" value="数据库中查询" class=btn onclick="query2db()">
									&nbsp;
									<INPUT TYPE="button" value="设备实时获取" class=btn onclick="query2device()">
								</TD>
							</TR>
						</TABLE>
					</TD>
				</TR>
			</TABLE>
			<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
				<tr>
					<TD  bgcolor=#999999>
						<div id="div_WAN_PARAM" >
							<span></span>
						</div>
					</td>
				</tr>
			</TABLE>
			<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
				<input type="hidden" name="device_id_" />
				<input type="hidden" name="wan_id_" />
				<input type="hidden" name="wan_conn_id_" />
				<input type="hidden" name="wan_conn_sess_id_" />
				<input type="hidden" name="sess_type_" />
				<TR>
					<TD bgcolor=#999999>
						<TABLE id="tb2" border=0 cellspacing=1 cellpadding=2 width="100%">
							<TR id="add_style" isShow="add"  STYLE="display:none">
								<TH align="center" colspan=2 valign="center"><B><span id="actLabel">新增</span></B></TH>
							</TR>
							<TR id="edit_style" isShow="edit"  STYLE="display:none">
								<TH align="center" colspan=2 valign="center"><B><span id="actLabel">修改</span></B></TH>
							</TR>
							<TR id="vpi_vci_style" bgcolor="#FFFFFF" isShow="add_" STYLE="display:none">
								<TD width="30%" class=column align="right">VPI/VCI</TD>
								<td width="70%"> 
									<input type="text" name="vpi" maxlength=3 class=bk size=3/>/
									<input type="text" name="vci" maxlength=3 class=bk size=3/>
									<font color="red">*</font>
								</td>
							</TR>
							<TR id="vlan_style" bgcolor="#FFFFFF" isShow="add_" STYLE="display:none">
								<TD width="30%" class=column align="right">VALN</TD>
								<td width="70%"> 
									<input type="text" name="vlan" maxlength=20 class=bk size=20/>
									<font color="red">*</font>
								</td>
							</TR>
							<TR id="sess_type_style" bgcolor="#FFFFFF" isShow="add" STYLE="display:none">
								<TD width="30%" class=column align="right">session类型</TD>
								<td width="70%"> 
									<SELECT name="sess_type" class="bk" onclick="add_show(this.value)">
										<OPTION value="1">PPP</OPTION>
										<OPTION value="2">IP</OPTION>
									</SELECT>
								</td>
							</TR>
							<TR id="enable_style" bgcolor="#FFFFFF" isShow="all" STYLE="display:none">
								<TD width="30%" class=column align="right">是否可用</TD>
								<TD width="70%">
									<input type="hidden" name="enable_old">
									<input type="radio" value="0" name="enable"/>否
									<input type="radio" value="1" name="enable"/>是
								</TD>
							</TR>
							<TR id="name_style" bgcolor="#FFFFFF" isShow="edit" STYLE="display:none">
								<TD class=column align="right">连接名称</TD>
								<TD>
									<input type="hidden" name="name_old">
									<INPUT TYPE="text" NAME="name" maxlength=20 class=bk size=20 readonly>
								</TD>
							</TR>
							<TR id="conn_type_style" bgcolor="#FFFFFF" isShow="ppp" STYLE="display:none">
								<TD class=column align="right">连接类型</TD>
								<TD >
									<input type="hidden" name="conn_type_old">
									<SELECT name="conn_type" class="bk" onclick="conn_type_cl(this.value)">
										<OPTION value="IP_Routed">IP_Routed</OPTION>
										<OPTION value="PPPoE_Bridged">PPPoE_Bridged</OPTION>
									</SELECT>
								</TD>
							</TR>
							<TR id="serv_list_style" bgcolor="#FFFFFF" isShow="all" STYLE="display:none">
								<TD class=column align="right">服务类型</TD>
								<TD >
									<input type="hidden" name="serv_list_old">
									<SELECT name="serv_list" class="bk" >
										<OPTION value="TR069">TR069</OPTION>
										<OPTION value="INTERNET">INTERNET</OPTION>
										<OPTION value="TR069&INTERNET">TR069&INTERNET</OPTION>
										<OPTION value="OTHER">OTHER</OPTION>
									</SELECT>
								</TD>
							</TR>
							<TR id="bind_port_style" bgcolor="#FFFFFF" isShow="all" STYLE="display:none">
								<TD class=column align="right">绑定端口</TD>
								<TD>
									<input type="hidden" name="bind_port_old">
									<INPUT TYPE="checkbox" NAME="bind_port" class=bk value="InternetGatewayDevice.LANDevice.1.LANEthernetInterfaceConfig.1.">LAN1
									<INPUT TYPE="checkbox" NAME="bind_port" class=bk value="InternetGatewayDevice.LANDevice.1.LANEthernetInterfaceConfig.2.">LAN2
									<INPUT TYPE="checkbox" NAME="bind_port" class=bk value="InternetGatewayDevice.LANDevice.1.LANEthernetInterfaceConfig.3.">LAN3
									<INPUT TYPE="checkbox" NAME="bind_port" class=bk value="InternetGatewayDevice.LANDevice.1.LANEthernetInterfaceConfig.4.">LAN4
									<INPUT TYPE="checkbox" NAME="bind_port" class=bk value="InternetGatewayDevice.LANDevice.1.WLANConfiguration.1">WLAN1
									<INPUT TYPE="checkbox" NAME="bind_port" class=bk value="InternetGatewayDevice.LANDevice.1.WLANConfiguration.2">WLAN2
									<INPUT TYPE="checkbox" NAME="bind_port" class=bk value="InternetGatewayDevice.LANDevice.1.WLANConfiguration.3">WLAN3
									<INPUT TYPE="checkbox" NAME="bind_port" class=bk value="InternetGatewayDevice.LANDevice.1.WLANConfiguration.4">WLAN4
									<!--  <INPUT TYPE="text" NAME="bind_port" maxlength=20 class=bk size=20>-->
								</TD>
							</TR>
							<TR id="username_style" bgcolor="#FFFFFF" isShow="ppp" STYLE="display:none">
								<TD class=column align="right">用户名</TD>
								<TD >
									<input type="hidden" name="username_old">
									<INPUT TYPE="text" NAME="username" maxlength=20 class=bk size=20>
									<font color="red">*</font>
								</TD>
							</TR>
							<TR id="password_style" bgcolor="#FFFFFF"  isShow="ppp" STYLE="display:none">
								<TD class=column align="right">密码</TD>
								<TD >
									<input type="hidden" name="password_old">
									<INPUT TYPE="text" NAME="password" maxlength=20 class=bk size=20>
									<font color="red">*</font>
								</TD>
							</TR>
							<TR id="ip_type_style" bgcolor="#FFFFFF" isShow="ip"  STYLE="display:none">
								<TD class=column align="right">地址类型</TD>
								<TD >
									<input type="hidden" name="ip_type_old">
									<SELECT name="ip_type" class="bk" onclick="serv_list_cl(this.value)">
										<OPTION value="DHCP">DHCP</OPTION>
										<OPTION value="Static">Static</OPTION>
									</SELECT>
								</TD>
							</TR>
							<TR id="ip_style" bgcolor="#FFFFFF" isShow="ip" STYLE="display:none">
								<TD class=column align="right">地址</TD>
								<TD >
									<input type="hidden" name="ip_old">
									<INPUT TYPE="text" NAME="ip" maxlength=20 class=bk size=20>
									<font color="red">*.*.*.*格式</font>
								</TD>
							</TR>
							<TR id="mask_style" bgcolor="#FFFFFF" isShow="ip" STYLE="display:none">
								<TD class=column align="right">mask</TD>
								<TD >
									<input type="hidden" name="mask_old">
									<INPUT TYPE="text" NAME="mask" maxlength=20 class=bk size=20>
									<font color="red">*.*.*.*格式</font>
								</TD>
							</TR>
							<TR id="gateway_style" bgcolor="#FFFFFF" isShow="ip" STYLE="display:none">
								<TD class=column align="right">网关</TD>
								<TD >
									<input type="hidden" name="gateway_old">
									<INPUT TYPE="text" NAME="gateway" maxlength=20 class=bk size=20>
									<font color="red">*.*.*.*格式</font>
								</TD>
							</TR>
							<TR id="dns_enab_style" bgcolor="#FFFFFF" isShow="ip_" STYLE="display:none">
								<TD class=column align="right">DNS开启</TD>
								<TD >
									<input type="hidden" name="dns_enab_old">
									<INPUT TYPE="text" NAME="dns_enab" maxlength=40 class=bk size=40>
								</TD>
							</TR>
							<TR id="dns_style" bgcolor="#FFFFFF" isShow="ip" STYLE="display:none">
								<TD class=column align="right">DNS</TD>
								<TD >
									<input type="hidden" name="dns_old">
									<INPUT TYPE="text" NAME="dns" maxlength=40 class=bk size=40>
									<font color="red">*.*.*.*格式，多个DNS中间用“，”隔开</font>
								</TD>
							</TR>
							<TR id="edit_submit_style" isShow="edit" STYLE="display:none">
								<TD colspan="2" align="right" class=green_foot>	
									<INPUT TYPE="button" value=" 更 新 " onclick="editWanConn(this)" class=btn>&nbsp;&nbsp; 
								</TD>
							</TR>
							<TR id="add_submit_style" isShow="add" STYLE="display:none">
								<TD colspan="2" align="right" class=green_foot>	
									<INPUT TYPE="button" value=" 新 增 " onclick="addWanConn(this)" class=btn>&nbsp;&nbsp; 
								</TD>
							</TR>
						</TABLE>
					</td>
				</tr>
			</table>
		</TD>
	</TR>
	<TR>
		<TD HEIGHT=20>
			&nbsp;
			<IFRAME ID=childFrm SRC="" STYLE="display:none"></IFRAME>
			<IFRAME ID=childFrm1 SRC="" STYLE="display:none"></IFRAME>
			<IFRAME ID=wan_childFrm SRC="" STYLE="display:none"></IFRAME>
		</TD>
	</TR>
</TABLE>
</FORM>
<%@ include file="../foot.jsp"%>
