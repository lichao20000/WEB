<%@ page contentType="text/html; charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page import="com.linkage.litms.system.dbimpl.*"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>���������ʽ</title>
<link rel="stylesheet" href="<s:url value='/css/inmp/css/css_green.css'/>"
	type="text/css">

<script type="text/javascript" src="<s:url value="/Js/inmp/jquery.js"/>"></script>

<%
	String gwType = request.getParameter("gw_type");
	LogItem.getInstance().writeItemLog(request, 1, "", "���������ʽ","in");
 %>
<SCRIPT LANGUAGE="JavaScript">
var gw_type = "<%= gwType%>";
		var deviceId="";
		var cityId="";
		var deviceserialnumber = "";
		var oui = "";
		var pvc = "";
		var vlan = "";
		var accessType = "";
		var user_id = "";
	$(function(){
		gwShare_setGaoji();
		
		
	});
	function addWanHtml() {
      
	//	var url = "<s:url value='/gwms/config/wanACT!addLanInit.action'/>";
	 var url = "<s:url value='/inmp/bss/changeConnectionType!addLanInit.action'/>";
		$.post(url,{
			deviceId:deviceId,
			type:"1",
			gw_type:gw_type
		},function(ajax){
			$("td[@id='lanInter']").html(ajax);
			
		});
	}
	function reInter(){
		$("td[@id='lanInter']").html("���ڻ�ȡ�˿�...");
		
		//var url = "<s:url value='/gwms/config/wanACT!addLanInit.action'/>";
       var url = "<s:url value='/inmp/bss/changeConnectionType!addLanInit.action'/>";
	    $.post(url,{
	    	deviceId:deviceId,
	    	type:"0"
		},function(ajax){
			$("td[@id='lanInter']").html(ajax);
			});
}

function CheckForm(){
		if(deviceId == ""){
			alert("���Ȳ�ѯ�豸!");
			return false;
		}
		return true;
	}
	
	function deviceResult(returnVal){
		
		deviceId=returnVal[2][0][0];
		document.all("txtdeviceId").value=returnVal[2][0][0];
		document.all("txtdeviceSn").value=returnVal[2][0][2];
		document.all("txtoui").value=returnVal[2][0][1];
		deviceserialnumber = returnVal[2][0][2];  // add by zhangchy 2011-08-23 ���ں�̨ҵ���·�
		oui = returnVal[2][0][1]; // add by zhangchy 2011-08-23 ���ں�̨ҵ���·�
		$("div[@id='selectedDev']").html("<strong>�������豸���к�:"+returnVal[2][0][2]+"</strong>");
		checkBindAndService(deviceId);
		this.tr1.style.display="";
		document.all("deviceResult").style.display="none";
		document.all("btnDevRes").value="չ����ѯ";
		document.all("routeAccount").value="";
		document.all("routePasswd").value="";
		document.getElementById("tr002").style.display = "none";
		$('#config').attr("disabled",false);
		addWanHtml();
	}
	
	function checkBindAndService(deviceId){
		document.all("pvcVlanValue").style.display="";
		   document.all("pvcVlanValue1").style.display="none";
		var url = "<s:url value='/inmp/bss/changeConnectionType!checkBindAndService.action'/>";
			$.post(url,{
	              deviceId:deviceId,
	              gw_type:gw_type
	           },function(ajax){
	        	   var result = ajax.split(";");
                    if(result[6]=='0'){ 
                       $("select[@name='connType']").html("");              //wan_type 1:�Ž� 2��·�� 
                       $("select[@name='connType']").html("<option value='1'>·�ɸ��Ž�</option>");
                       $("#line1").css("display","none");
                     }
                    else{
                    	 $("select[@name='connType']").html("");
                    	 $("select[@name='connType']").html("<option value='2'>�ŽӸ�·��</option>");
                    	 $("#line1").css("display","");
                    }
                    /**
                     if(result[0]=="0"){
	        		   document.all("changeConnType").style.display="";
	        	   	   document.all("checkBindResult").style.display="";
	        	   	   $("div[@id='checkBindDIV']").html("<strong>"+result[1]+"</strong>");
	        	     }else {
	        	     **/
	        	   	   $("div[@id='checkBindDIV']").html("");
	        		   accessType=result[0];
	        		   //vlan=result[2]  // ע��by zhangchy 2011-08-23 ��Ϊ��pvcVlanValue��ֵд����INPUT��ֻ����ʽ��������"����"��ʱ��ȡһ��INPUT���ֵ���˴��Ͳ���Ҫ�ˣ��̽���ע��
	        		   document.all("changeConnType").style.display="";
	        		   document.all("checkBindResult").style.display="";
	        		   document.all("routeAccount").value = result[3];  // �û���   // add by zhangchy 2011-08-23  ���û�������ǰ̨չʾ
	        		   document.all("routePasswd").value = result[4];   // ����     // add by zhangchy 2011-08-23  ���������ǰ̨չʾ
	        		   user_id = result[5];     // add by zhangchy 2011-08-23 
	        		   
	        		   if(result[0]=="1"){
	        			   document.all("pvcVlanValue").style.display="none";
	        			   document.all("pvcVlanValue1").style.display="";
		        		   $("div[@id='pvcVlanDiv']").html("PVC:");
		        		   $("td[@id='accessType']").html("ADSL");
		        		   var item = null;
		        		   var selectTag = document.getElementById("pvcSelect"); 
		        		   var colls = selectTag.options;   
		        		   if(colls.length > 0 && isClearOption()){
		                       clearOptions(colls);
		                  }
		        		   var pvcArr = result[2].split("#");
		        		   for(var i = 0 ; i < pvcArr.length ; i++){
		        			   item = new Option(pvcArr[i],pvcArr[i]); 
		        			   selectTag.options.add(item); 
		        		   }
		        	   }else if(result[0]=="2"){
		        		   $("div[@id='pvcVlanDiv']").html("vlanֵ:");
		        		   $("td[@id='accessType']").html("LAN");
		        		   //$("td[@id='pvcVlanValue']").html(result[2]);
		        		   document.all("pvcVlanValue2").value = result[2];
		        	   }else if(result[0]=="3" || result[0]=="4"){
		        		   $("div[@id='pvcVlanDiv']").html("vlanֵ:");
		        		   $("td[@id='accessType']").html("PON");
		        		   //$("td[@id='pvcVlanValue']").html(result[2]);
		        		   document.all("pvcVlanValue2").value = result[2];
		        	   }
	       		  // }
	        		  
	            });
	}
	 //���options����
    function clearOptions(colls){
        var length = colls.length;
        for(var i=length-1;i>=0;i--){
               colls.remove(i);
           }
       }
	 
	 function showAccessType(accessType,accessTypeName)
	 {
		 //alert(accessType + "++++" + accessTypeName);
		 accessType = accessType;
		 $("td[@id='accessType']").html(accessTypeName);
		 //user_id = "<s:property value="user_id" />"
	 }

	 //����ʺ�ѡ����Ӧ�¼�
	 function usernameOnclick(username,passwd,pvcNow,user_id_a,wan_type,accessTypeName)
	 {
		 //alert("+username+" + username+"+passwd+" + passwd+"+pvcNow+" + pvcNow+"+user_id+" + user_id_a+"+wan_type+" + wan_type);
		 user_id = user_id_a;
		 accessType = wan_type;
		 
		 document.all("changeConnType").style.display="";
		 
		 //��ʾ��������
		 document.all("routeAccount").value = username;
		 document.all("routePasswd").value = passwd;
		 
		 //$("td[@id='accessType']").html(accessTypeName);
		  
  		 document.all("pvcVlanValue2").value = pvcNow;
		 if(wan_type=="1"){
			   document.all("pvcVlanValue").style.display="none";
			   document.all("pvcVlanValue1").style.display="";
  		   $("div[@id='pvcVlanDiv']").html("PVC");
  		   //$("td[@id='accessType']").html("ADSL");
  		   var item = null;
  		   var selectTag = document.getElementById("pvcSelect"); 
  		   var colls = selectTag.options;   
  		   if(colls.length > 0){
                 clearOptions(colls);
            }
  		   //��ȡPVC
  		 var url = "<s:url value='/inmp/bss/changeConnectionType!getAllPVCConfig.action'/>";
			$.post(url,{
	              deviceId:deviceId
	           },function(ajax){
	        	   //alert(ajax);
  		   			var pvcArr = (pvcNow + "#" + ajax).split("#");
  		   			for(var i = 0 ; i < pvcArr.length ; i++){
  			   			item = new Option(pvcArr[i],pvcArr[i]); 
  			   			selectTag.options.add(item); 
  		   			}
	           });
  	   }else{
  		   $("div[@id='pvcVlanDiv']").html("vlan");
  	   }
	 }
	 
	function txtSelectDevice()
	{		
		if("none"==document.all("deviceResult").style.display){
			document.all("deviceResult").style.display="";
			document.all("btnDevRes").value="���ز�ѯ";
		}else{
			document.all("deviceResult").style.display="none";
			document.all("btnDevRes").value="չ����ѯ";
		}
	}
	/**
	function chgConnType() {
		obj = document.all;
		if (obj.connType.value == "1") {
			line1.style.display = "none";
		} else {
			line1.style.display = "";
		}
	}
	**/
	
	function ExecMod(){

		var flag = "false";
		var bindPort = "";
	$("[name='LAN'][checked]").each(function(){
		if("true"==flag){
			bindPort+=",";
		}
    	bindPort+=$(this).val();
    	flag="true";
    })
	$("[name='WLAN'][checked]").each(function(){
		if("true"==flag){
			bindPort+=",";
		}
    	bindPort+=$(this).val();
    	flag="true";
    })

	pvc = document.getElementById("pvcSelect").value;
		if(deviceId == ""){
			alert("���Ȳ�ѯ�豸!");
			return false;
		}
		connType = document.all("connType").value;
		//doType = document.all("doType").value;  // ע��by zhangchy 2011-08-24 ҳ��Ҫ��ȥ��ִ�з�ʽ������ע�ʹ˶�
		routeAccount = document.all("routeAccount").value;
		routePasswd = document.all("routePasswd").value;
		vlan = document.all("pvcVlanValue2").value;  //  add by zhangchy 2011-08-23 
		if(connType=="2"){
			if(routeAccount==""){
				alert("������·���˺�");
				document.frm.routeAccount.focus();
				return false;
			}
			if(routePasswd==""){
				alert("������·������");
				document.frm.routePasswd.focus();
				return false;
			}
		}
		$('#config').attr("disabled",true);
       var url = "<s:url value='/inmp/bss/changeConnectionType!changeConnectionType.action'/>";
     

    	$.post(url,{
              deviceId:deviceId,
              connType:connType,
              //doType:doType,    // ע��by zhangchy 2011-08-24 ҳ��Ҫ��ȥ��ִ�з�ʽ������ע�ʹ˶�
              routeAccount:routeAccount,
              routePasswd:routePasswd,
              pvc:pvc,
              vlan:vlan,
              accessType:accessType,
              user_id:user_id,
              deviceserialnumber:deviceserialnumber,
              oui:oui,
              bindPort:bindPort,
              gw_type:gw_type
           },function(ajax){
        	   //$('#config').attr("disabled",false);
	           	var s = ajax.split(";");
			    //if(s[0]=="-1"){
			        alert(s[1]);
				//}
					
            });
		document.getElementById("tr002").style.display = "";
				
    }
</SCRIPT>

</head>

<body>

<TABLE border=0 align="center" cellspacing=0 cellpadding=0 width="98%">
	<TR>
		<TD HEIGHT=20>&nbsp;</TD>
	</TR>
	<TR>
		<TD>
		<table width="100%" height="30" border="0" cellpadding="0"
			cellspacing="0" class="green_gargtd">
			<tr>
				<td width="162" align="center" class="title_bigwhite">���������ʽ</td>
				<td><img
					src="<s:url value="/images/inmp/attention_2.gif"/>" width="15"
					height="12" /></td>
			</tr>
		</table>
		</TD>
	</TR>
	<TR bgcolor="#FFFFFF">
		<td colspan="4">
		<table id="deviceResult" width="100%" border=0 cellspacing=0
			cellpadding=0 align="center" valign="middle" STYLE="display: ">
			<tr bgcolor=#ffffff>
				<td class=column colspan="4"><%@ include
					file="../bss/gwShareDeviceQuery.jsp"%>
				</td>
			</tr>
		</table>
		</td>
	</TR>
	<TR id=tr1 style="display: none">
		<td>
		<form name="frm">
		<table id="checkBindResult" border=0 cellspacing=1 cellpadding=2
			width="100%" bgcolor=#999999 style="display: ">
			<TR>
				<TH colspan="4" align="center">���������ʽ</TH>
			</TR>
			<tr bgcolor="#FFFFFF">
				<td width="50%" colspan="4">
				<div id="selectedDev">���ѯ�豸��</div>
				<input type="hidden" name="txtdeviceId" value="" /> <input
					type="hidden" name="txtdeviceSn" value="" /> <input type="hidden"
					name="txtoui" value="" /></td>
			</tr>
			<tr bgcolor="#FFFFFF">
				<td width="50%" colspan="2">
				<div id="checkBindDIV"></div>
				</td>
				<td align="right" width="50%" colspan="2"><input type="button"
					name="btnDevRes" class=jianbian value="���ز�ѯ"
					onclick="txtSelectDevice()" /></td>
			</tr>
		</table>
		<table id="changeConnType" border=0 cellspacing=1 cellpadding=2
			width="100%" bgcolor=#999999 style="display: ">
			<TR>
				<TH colspan="4" align="center">�û�������Ϣ</TH>
			</TR>
			<tr bgcolor=#ffffff>
				<td class=column width="15%" align="right">������ʽ</td>
				<td align="left" width="35%"><select name="connType" class="bk"
					onChange="chgConnType()">
					<!-- <option value="1" selected>�ŽӸ�·��</option>
					 <option value="2">
											·�ɸ��Ž�
										</option>
										<option value="3">
											�ŽӼ�·��
										</option>
										<option value="4">
											·�ɼ��Ž�
										</option>
										<option value="5">
											�޸�·���û�������
										</option> --> 
				</select></td>
				<td nowrap class=column align="right">���з�ʽ��</td>
				<td id="accessType" nowrap></td>
				<!-- <td class=column align="right" width="15%">
									ִ�з�ʽ
								</td>
								<td align="left" width="35%">
									<select name="doType" class="bk">
										<option value="modify" selected>
											ֱ���޸�
										</option>
										<option value="deleteadd">
											ɾ���ټ�
										</option>
									</select>
								</td> -->
			</tr>
			<tr bgcolor="#FFFFFF" id="line1" style="display: ">
				<td nowrap class=column align="right">�û���</td>
				<td nowrap><input type="text" name="routeAccount" class="bk"
					value=""> &nbsp;&nbsp; <font color=red>*</font></td>
				<td nowrap class=column align="right">����</td>
				<td><input type="text" name="routePasswd" class="bk" value="">
				&nbsp;&nbsp; <font color=red>*</font></td>
			</tr>
			<tr bgcolor="#FFFFFF" id="line2" style="display: ">
				<td nowrap class=column align="right">
				<div id="pvcVlanDiv">PVC</div>
				</td>
				<td id="pvcVlanValue" style="display: "><input type="text"
					name="pvcVlanValue2" class="bk" readOnly="readonly" value="">
				</td>
				<td id="pvcVlanValue1" style="display: none"><select name="pvc"
					id="pvcSelect" class="bk">
				</select></td>

				<TD class=column align="right" width="15%"><img
					src="<s:url value="/images/inmp/refresh.png" />" border="0" alt="ˢ��"
					onclick="reInter()"> �󶨶˿�:</TD>
				<TD width="35%" id="lanInter">���ڻ�ȡ�˿�...</TD>
				<!-- 				<td nowrap class=column align="right">
								</td>
								<td nowrap >
								</td>-->
			</tr>
			<tr bgcolor=#ffffff>
				<td class=foot colspan=4 align=right>
				<button id="config" onclick="ExecMod()">&nbsp;�� ��&nbsp;</button>
				</td>
			</tr>
		</table>
		</form>
		</td>
	</TR>
	<TR bgcolor="#FFFFFF" id="tr002" style="display: none">
		<td valign="top" class=column>
		<div id="div_strategy" style="width: 100%; z-index: 1; top: 100px">
		</div>
		</td>
	</TR>
</TABLE>
</body>
</html>
<%@ include file="../foot.jsp"%>