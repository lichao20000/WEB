<%@ page contentType="text/html; charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ include file="../../head.jsp"%>
<%
	String gwType = request.getParameter("gw_type");
 %>
<link href="../../css/css_green.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript">
$(function(){
	gwShare_queryChange('2');   // ���ظ߼���ѯ
	
	$("input[@name='gwShare_queryType']").val("2");
	$("input[@name='gwShare_jiadan']").css("display","none");
});
     function ExecMod(){
     		if(CheckForm()){
     			var _device_id = $("input[@name='textDeviceId']").val();	
				if(_device_id == null || _device_id == ""){
					alert("���Ȳ�ѯ�豸!");
					return false;
				}
		        page = "jt_device_zendan_from4_save.jsp?device_id=" +_device_id + "&oid_type=2&type=1";
				document.all("div_ping").innerHTML = "����������Ͻ���������ĵȴ�....";
				document.all("childFrm").src = page;
			}else{
				return false;
			}	
          }
	
	function CheckForm(){
		var _device_id = $("input[@name='textDeviceId']").val();	
		if(_device_id == null || _device_id == ""){
			alert("���Ȳ�ѯ�豸!");
			return false;
		}	
		return true;		
	}
	
	function deviceResult(returnVal, city_id, vendor_id, device_model_id, devicetype_id){
		
		$("tr[@id='trDeviceResult']").css("display","");

		$("td[@id='tdDeviceCount']").html("&nbsp");
		$("input[@name='textDeviceCount']").val("");
		
		var deviceIds = new Array();
		deviceIds = returnVal.split("|");
		
		if("" == returnVal){
			$("input[@name='txtDeviceCount']").val(0);
			$("input[@name='txtDeviceIds']").val(returnVal);
			$("td[@id='tdDeviceCount']").html("0");
		}else{
			$("input[@name='txtDeviceCount']").val(deviceIds.length);
			$("input[@name='txtDeviceIds']").val(returnVal);
			$("td[@id='tdDeviceCount']").html(deviceIds.length);
		}
		
		$("tr[@id='trDeviceResult']").css("display","");
		$("td[@id='select_map']").html("���ڲ�ѯģ��...");
		queryDigitMap(city_id, vendor_id, device_model_id, devicetype_id, returnVal);
		
	}
	
	function queryDigitMap(city_id, vendor_id, device_model_id, devicetype_id, returnVal){
		$("tr[@id='tr002']").css("display","none");
		var url = "<s:url value='/itms/config/digitMapConfig!queryDigitMap.action'/>";
		$.post(url,{
           cityId:city_id, 
           vendorId:vendor_id, 
           deviceModelId:device_model_id, 
           deviceTypeId:devicetype_id
           },function(ajax){
	           $("td[@id='select_map']").html(ajax);
	           
	           if(returnVal == ""){  // ���û��ѯ���豸���򽫰�ť"�·�����"�ûң�������
	           	$("input[id='doButton']").attr("disabled", true);
	           }else{
		           if(ajax.indexOf('font') == -1){
			       	  $("input[id='doButton']").attr("disabled", false);
			       }else{
			       	  $("input[id='doButton']").attr("disabled", true);  // ���û�����������豸����ͼģ�壬�򽫰�ť"�·�����"�ûң�������
			       }
	           }
		       
         });
	}
	
	
function doConfig(){
	var map_id = $("select[@id='map_id']").val();
	var device_id = $("input[@name='txtDeviceIds']").val();
	var taskName = $("input[@name='taskName']").val();  // ��������
	if(device_id == ""){
		alert("���Ȳ�ѯ�豸��");
		return;
	}
	if(taskName == ""){
		alert("�������������ƣ�");
		return;
	}
	
	var url = "<s:url value='/itms/config/digitMapConfig!doConfigAll.action'/>";
		$.post(url,{
              map_id:map_id, 
              device_id:device_id,
              taskName:encodeURIComponent(taskName),
              gw_type: <%=gwType%>
           },function(ajax){
	           	var s = ajax.split(";");
			    if(s[0]=="-1"){
			        $("tr[@id='tr002']").css("display","");
					$("td[@id='td001']").html(s[1]);
				}
				if(s[0]=="1"){
					$("tr[@id='tr002']").css("display","");
					$("td[@id='td001']").html(s[1]);
				}
            });
}
	
	
</SCRIPT>

<body>
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD HEIGHT=20>
			&nbsp;
		</TD>
	</TR>
	<TR>
		<TD>
			<FORM NAME="frm" METHOD="POST" ACTION=""
				onSubmit="return CheckForm();">
				<table width="95%" border="0" align="center" cellpadding="0"
					cellspacing="0">
					<tr>
						<td>
							<table width="100%" height="30" border="0" cellspacing="0"
								cellpadding="0" class="green_gargtd">
								<tr>
									<td width="162">
										<div align="center" class="title_bigwhite">
											�����ն��·�����
										</div>
									</td>
								</tr>
							</table>
						</td>
					</tr>
					<tr>
						<td bgcolor="#FFFFFF">
							<table width="100%" border=0 align="center" cellpadding="1"
								cellspacing="1" bgcolor="#999999" class="text">
								<TR bgcolor="#FFFFFF">
									<td colspan="4">
										<%@ include file="deviceQuery.jsp"%>
									</td>
								</TR>
								<TR bgcolor="#FFFFFF" id="trDeviceResult" style="display: none" >
									<td nowrap align="right" class=column width="15%">
										�Ѳ�ѯ�豸�ĸ���
										<input type="hidden" name="txtDeviceIds" >
										<input type="hidden" name="txtDeviceCount" >
									</td>
									<td id="tdDeviceCount" width="35%">
									</td>
									<td nowrap align="right" class=column width="15%">��������</td>
									<td width="35%">
										<input type="text" name="taskName" value="" size="25" class="bk"/>&nbsp;<font color="red">*</font>
									</td>
								</TR>
								<TR bgcolor="#FFFFFF" id="trDeviceResult" style="display: none">
									<td nowrap align="right" class=column width="15%">
										��ͼģ������
									</td>
									<!--<td colspan=3>
										<s:select list="digitMapList" name="map_id" id="map_id" listKey="map_content" listValue="map_name"
										 cssClass="bk"></s:select>
									</td> -->
									 <td colspan=3 id="select_map">
										<font color=red>���Ȳ�ѯ�豸��</font>
									</td>
								</TR>
								<tr bgcolor="#FFFFFF">
									<td colspan="4">
										<table width="100%" border="0" cellspacing="0" cellpadding="0">
											<tr align="right" CLASS="green_foot">
												<td>
													<INPUT TYPE="button" value="�·�����" class=btn id="doButton" disabled
														onclick="doConfig()">
													&nbsp;&nbsp;
													<INPUT TYPE="hidden" name="action" value="add">
													&nbsp;&nbsp;
												</td>
											</tr>
										</table>
									</td>
								</tr>
								
								<tr bgcolor="#FFFFFF" id="tr002" style="display:none">
									<td colspan="4">
										<table width="100%" border="0" cellspacing="0" cellpadding="0">
											<TR bgcolor="#FFFFFF">
												<TH colspan="4">
													�ն��·����ý��
												</tH>
											</TR>	
											<TR bgcolor="#FFFFFF" >
												<td colspan="4" id="td001" valign="top" class=column align="center">
												</td>
											</TR>
										</table>
									</td>
								</tr>
							</table>
						</td>
					</tr>
				</table>
			</FORM>
		</TD>
	</TR>
	<TR>
		<TD HEIGHT=20>
			<IFRAME ID=childFrm SRC="" STYLE="display: none"></IFRAME>
			<IFRAME ID=childFrm1 SRC="" STYLE="display: none"></IFRAME>
			<IFRAME ID=childFrm2 SRC="" STYLE="display: none"></IFRAME>
			&nbsp;
		</TD>
	</TR>
</TABLE>
<%@ include file="../../foot.jsp"%>
</body>