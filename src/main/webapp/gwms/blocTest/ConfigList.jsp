

<%@ include file="/timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<%--
	/**
	 * 
	 *
	 * @author wuchao
	 * @version 1.0
	 * @since 2011-7-24 
	 * 
	 * <br>
	 * 
	 */
 --%>
 
 <title>e8-c业务查询</title>

	
<SCRIPT LANGUAGE="JavaScript">	
	
	$(function(){
		parent.dyniframesize();
	});


	function save()
	{
		alert("=======");
		trimAll();
		alert("trimall");
		if(!CheckForm())
			return;
		var url = "<s:url value="/gwms/blocTest/DeviceInfoQuery!add.action"/>";
		var access_type = $("select[@name='access_type']").val();
		alert(access_type);
	    var device_type = $("select[@name='device_type']").val();
	    alert(device_type);
	    var is_card_apart = $("select[@name='is_card_apart']").val();
	    alert(is_card_apart);
	    var wan_name = $("input[@name='wan_name']").val();
	    alert(wan_name);
	    var wan_num = $("input[@name='wan_num']").val();
	    alert(wan_num);
	    var wan_can = $("input[@name='wan_can']").val();
	    var lan_name = $("input[@name='lan_name']").val();
	    var lan_num = $("input[@name='lan_num']").val();
	    var lan_can = $("input[@name='lan_can']").val();
	    var wlan_name = $("input[@name='wlan_name']").val();
	    var wlan_num = $("input[@name='wlan_num']").val();
	    var wlan_can = $("input[@name='wlan_can']").val();
	    var voip_name = $("input[@name='voip_name']").val();
	    var voip_num = $("input[@name='voip_num']").val();
	    var voip_can = $("input[@name='voip_can']").val();
	    var voip_protocol = $("select[@name='voip_protocol']").val();
	    var wireless_type = $("select[@name='wireless_type']").val();
	    var wireless_num = $("input[@name='wireless_num']").val();
		var wireless_size = $("input[@name='wireless_size']").val();
        var devicetype_id = $("input[@name='devicetype_id']").val();
		


		$.post(url,{
			access_type:access_type,
			device_type:device_type,
			is_card_apart:is_card_apart,
			wan_name:wan_name,
			wan_num:wan_num,
			wan_can:wan_can,
			lan_name:lan_name,
			lan_num:lan_num,
			lan_can:lan_can,
			wlan_name:wlan_name,
			wlan_num:wlan_num,
			wlan_can:wlan_can,
			voip_name:voip_name,
			voip_num:voip_num,
			voip_can:voip_can,
			voip_protocol:voip_protocol,
			wireless_type:wireless_type,
			wireless_num:wireless_num,
			wireless_size:wireless_size,
			devicetype_id:devicetype_id
			
		},function(ajax){
			alert(ajax);
			
			if(ajax.indexOf("成功") != -1)
			{
				// 普通方式提交
				var form = document.getElementById("mainForm");
				form.action = "<s:url value="/gwms/blocTest/DeviceInfoQuery!add.action"/>";
				//form.target = "dataForm";
				form.submit();
			}
		});
		//showAddPart(false);
	}


	function CheckForm(){   
		   temp =document.all("access_type").value;
		   if(temp=="-1" || temp=="")
		   {
		     alert("请选择上行方式！");
		     return false;
		   }
		   temp =document.all("device_type").value;
		   if(temp=="-1" || temp=="")
		   {
		     alert("请选择设备类型！");
		     return false;
		   }
		   temp =document.all("is_card_apart").value;
		   if(temp=="-1" || temp=="")
		   {
		     alert("请选择是否机卡分离！");
		     return false;
		   }
		   temp =document.all("wan_name").value;
		   if(temp=="-1" || temp=="")
		   {
		     alert("请填写wan口名称！");
		     return false;
		   }
		   temp =document.all("wan_num").value;
		   if(temp=="-1" || temp=="")
		   {
		     alert("请填写wan口数量！");
		     return false;
		   }temp =document.all("wan_can").value;
		   if(temp=="-1" || temp=="")
		   {
		     alert("请填写特wan口处理能力！");
		     return false;
		   }

		   temp =document.all("lan_name").value;
		   if(temp=="-1" || temp=="")
		   {
		     alert("请填写lan口名称！");
		     return false;
		   }
		   temp =document.all("lan_num").value;
		   if(temp=="-1" || temp=="")
		   {
		     alert("请填写lan口数量！");
		     return false;
		   }temp =document.all("lan_can").value;
		   if(temp=="-1" || temp=="")
		   {
		     alert("请填写特lan口处理能力！");
		     return false;
		   }
		   temp =document.all("wlan_name").value;
		   if(temp=="-1" || temp=="")
		   {
		     alert("请填写wlan口名称！");
		     return false;
		   }
		   temp =document.all("wlan_num").value;
		   if(temp=="-1" || temp=="")
		   {
		     alert("请填写wlan口数量！");
		     return false;
		   }temp =document.all("wlan_can").value;
		   if(temp=="-1" || temp=="")
		   {
		     alert("请填写特wlan口处理能力！");
		     return false;
		   }

		   temp =document.all("voip_name").value;
		   if(temp=="-1" || temp=="")
		   {
		     alert("请填写语音口名称！");
		     return false;
		   }
		   temp =document.all("voip_num").value;
		   if(temp=="-1" || temp=="")
		   {
		     alert("请填写语音口数量！");
		     return false;
		   }temp =document.all("voip_can").value;
		   if(temp=="-1" || temp=="")
		   {
		     alert("请填写特语音处理能力！");
		     return false;
		   }
		 temp =document.all("voip_protocol").value;
		   if(temp=="-1" || temp=="")
		   {
		     alert("请选择语音协议！");
		     return false;
		   }
		 temp =document.all("wireless_type").value;
		   if(temp=="-1" || temp=="")
		   {
		     alert("请选择天线类型！");
		     return false;
		   }
		 temp =document.all("wireless_num").value;
		   if(temp=="-1" || temp=="")
		   {
		     alert("请填写天线根数！");
		     return false;
		   }
		 temp =document.all("wireless_size").value;
		   if(temp=="-1" || temp=="")
		   {
		     alert("请填写天线尺寸！");
		     return false;
		   }
		 return true;
		}














	//全部trim
	function trimAll()
	{
		var inputs = document.getElementsByTagName("input");
		for(var i=0;i<inputs.length;i++)
		{
			var input = inputs[i];
			if(/text/gi.test(input.type))
			{
				input.value = trim(input.value);
			}
		}
	}

	</SCRIPT>

<%@ include file="/toolbar.jsp"%>
<%@ include file="/itms/resource/DeviceType_Info_util.jsp"%>


<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<tr>
		<td HEIGHT=20>&nbsp;</td>
	</Tr>
	<tr>
		<td>
 <FORM id="addForm" name="addForm" target="" method="post" action="" >
	
	<TABLE width="98%" border=0 cellspacing=0 cellpadding=0 align="center" id="addTable" style="display: none">
	  <TR>
			<TD bgcolor=#999999>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
					<TR>
						<TH colspan="4" align="center"><SPAN id="actLabel">设备版本配置</SPAN></TH>
					</TR>
					<TR bgcolor="#FFFFFF" id="vendor_idID">
						<TD class=column align="right" >设备厂商</TD>
						<TD colspan=3>
							<select name="access_type" class="bk">
							<option value="1">DSL</option>
							<option value="2">Ethernet</option>
							<option value="3">PON</option>
							<option value="4">GPON</option>
							</select>
						<font color="#FF0000">*</font></TD>
						
						<TD align="right" class=column width="15%" >设备类型</TD>
						<TD align="left" width="35%">
							<select name="device_type" class="bk">
							<option value="1">标准型e8-B网关</option>
							<option value="2">普及型e8-B网关</option>
							<option value="3">标准型e8-C网关</option>
							<option value="4">AP外置型e8-C网关</option>
							<option value="5">其他ITMS管理的终端类型</option>
							</select>
						<font color="#FF0000">*</font></TD>
						
				</TR>
					
				<TR bgcolor="#FFFFFF">
		<TD align="right" class=column width="15%" >wan口名称</TD>
						<TD align="left" width="35%"><INPUT TYPE="text" NAME="wan_name" maxlength=30 class=bk size=20 value="<s:property value="wan_name"/>">     <s:property value="wan_name"/>      &nbsp;<font color="#FF0000">*</font></TD>
					<TD align="right" class=column width="15%">wan口数量</TD>
						<TD align="left" width="35%"><INPUT TYPE="text" NAME="wan_num" maxlength=30 class=bk size=20 value="<s:property value="wan_name"/>">&nbsp;        <s:property value="lan_name"/>                     <font color="#FF0000">*</font></TD>		
					</TR>	
					
	<TR bgcolor="#FFFFFF" id="gwShare_tr21" STYLE="">
<TD align="right" class=column width="15%" >wan口处理能力</TD>
<TD align="left" width="35%"><INPUT TYPE="text" NAME="wan_can" maxlength=30 class=bk size=20 value="<s:property value="wan_can"/>">&nbsp;<font color="#FF0000">*</font></TD>
<TD align="right" class=column width="15%">lan口名称</TD>
<TD align="left" width="35%"><INPUT TYPE="text" NAME="lan_name" maxlength=30 class=bk size=20 value="<s:property value="lan_name"/>">&nbsp;<font color="#FF0000">*</font></TD>
</TR>
<TR bgcolor="#FFFFFF" id="gwShare_tr21" STYLE="">
<TD align="right" class=column width="15%">lan口数量</TD>
<TD align="left" width="35%"><INPUT TYPE="text" NAME="lan_name" maxlength=30 class=bk size=20 value="<s:property value="lan_num"/>">&nbsp;<font color="#FF0000">*</font></TD>
<TD align="right" class=column width="15%">lan口处理能力</TD>
<TD align="left" width="35%"><INPUT TYPE="text" NAME="lan_num" maxlength=30 class=bk size=20 value="<s:property value="lan_can"/>">&nbsp;<font color="#FF0000">*</font></TD>
</TR>	

<TR bgcolor="#FFFFFF" id="gwShare_tr21" STYLE="">				
 <TD align="right" class=column width="15%">语音口名称</TD>
<TD align="left" width="35%"><INPUT TYPE="text" NAME="voip_name" maxlength=30 class=bk size=20 value="<s:property value="voip_name"/>">&nbsp;<font color="#FF0000">*</font></TD>
<TD align="right" class=column width="15%" >语音口数量</TD>				
<TD align="left" width="35%"><INPUT TYPE="text" NAME="voip_num" maxlength=30 class=bk size=20 value="<s:property value="voip_num"/>">&nbsp;<font color="#FF0000">*</font></TD>
</TR>	
<TR bgcolor="#FFFFFF" id="gwShare_tr21" STYLE="">				
 <TD align="right" class=column width="15%">语音处理能力</TD>
<TD align="left" width="35%"><INPUT TYPE="text" NAME="voip_can" maxlength=30 class=bk size=20 value="<s:property value="voip_can"/>">&nbsp;<font color="#FF0000">*</font></TD>
<TD align="right" class=column width="15%" >wlan口名称</TD>				
<TD align="left" width="35%"><INPUT TYPE="text" NAME="wlan_name" maxlength=30 class=bk size=20 value="<s:property value="wlan_name"/>">&nbsp;<font color="#FF0000">*</font></TD>
</TR>	
<TR bgcolor="#FFFFFF" id="gwShare_tr21" STYLE="">
<TD align="right" class=column width="15%">wlan口数量</TD>
<TD align="left" width="35%"><INPUT TYPE="text" NAME="wlan_num" maxlength=30 class=bk size=20 value="<s:property value="wlan_num"/>">&nbsp;<font color="#FF0000">*</font></TD>
<TD align="right" class=column width="15%" >wlan口处理能力</TD>
<TD align="left" width="35%"><INPUT TYPE="text" NAME="wlan_can" maxlength=30 class=bk size=20 value="<s:property value="wlan_can"/>">&nbsp;<font color="#FF0000">*</font></TD>
</TR>	
<TR bgcolor="#FFFFFF" id="gwShare_tr21" STYLE="">
<TD align="right" class=column width="15%" >天线根数</TD>
<TD align="left" width="35%"><INPUT TYPE="text" NAME="wireless_num" maxlength=10 class=bk size=20 value="<s:property value="wireless_num"/>">&nbsp;<font color="#FF0000">*</font></TD>
<TD align="right" class=column width="15%" >天线尺寸</TD>
<TD align="left" width="35%"><INPUT TYPE="text" NAME="wireless_size" maxlength=10 class=bk size=20 value="<s:property value="wireless_size"/>">&nbsp;<font color="#FF0000">*</font></TD>
</TR>				
					
	<TR bgcolor="#FFFFFF" id="gwShare_tr21">
<TD align="right" class=column width="15%" >是否机卡分离</TD>
						<TD align="left" width="35%">
							<select name="is_card_apart" class="bk"  onchange="change_model('deviceModel','-1')">
							<option value="0">否</option>
							<option value="1">是</option>
							</select>
						<font color="#FF0000">*</font></TD>
<TD align="right" class=column width="15%" >天线类型</TD>
							<TD align="left" width="35%">
								<select name="wireless_type" class="bk">
									<option value="0">内置</option>
									<option value="1">外置</option>
						</select>
						</TD>
</TR>

	
					<TR bgcolor="#FFFFFF" id="gwShare_tr21">
						<TD align="right" class=column width="15%">语音协议</TD>
						<TD align="left" width="35%">
								<select name="voip_protocol" class="bk">
									<option value="0">IMS SIP\软交换SIP\H.248</option>
									<option value="1">eIMS SIP</option>
									<option value="2">软交换</option>
									<option value="3">eH.248</option>
								</select>
						</TD>
							<TD colspan="4" align="right" CLASS=green_foot>
						<input type='hidden' id="updateId" value="-1" />
							<INPUT TYPE="button" onclick="javascript:save()" value=" 保 存 " class=jianbian>&nbsp;&nbsp;
							<INPUT TYPE="hidden" name="action" value="add">							
							<input type="hidden" name="devicetype_id" value="<s:property value="devicetype_id"/>">							
						</TD>
			</TR>				
	</TABLE>
			</TD>
		</TR>
	</TABLE>
	</FORM>
</td></tr>

<TR>
		<TD HEIGHT=20><IFRAME ID=childFrm SRC="" STYLE="display: none"></IFRAME>&nbsp;</TD>
	</TR>

</table>





