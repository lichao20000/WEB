<%@ include file="../../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<link rel="stylesheet" href="<s:url value="/css/css_green.css"/>"
	type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/jquery.js"/>"></SCRIPT>
<script type="text/javascript"
			src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/edittable.js"/>"></SCRIPT>
<%
String isJs= LipossGlobals.getLipossProperty("InstArea.ShortName");
%>
<script type="text/javascript">

    $(function() 
		   {
			 var isFirst = '<s:property value="no_query"/>' ;
			 var isJs= "<%=isJs%>";
		     if(isFirst!='true'&& isJs=='js_dx'){
					$("input[@name='button']").attr("disabled", false); 
		    }
		});

function ToExcel() {
	var page="../../gwms/resource/queryDevice!getInfoExcel.action?bindState="+$("input[@name='bindState'][@checked]").val()
		+ "&starttime=" + document.frm.starttime.value
		+ "&endtime=" + document.frm.endtime.value
		+ "&timeType=" + document.frm.timeType.value;
	document.all("childFrm").src=page;
	//window.open(page);
}

function do_test() {
	if($("input[@name='bindState'][@checked]").val()==""){
		alert("请选择绑定状态！");
		return false;
	}
	$("input[@name='button']").attr("disabled", true); 
	$("tr[@id='trData']").show();
    $("div[@id='QueryData']").html("正在查询，请稍等....");
	frm.submit();
}

function DelDevice(device_id){
	if(!confirm("真的要删除该网络设备吗？\n本操作所删除的不能恢复！！！")){
		return false;
	}
	var url = "../../Resource/DeviceSave.jsp";
	var pars = "device_id=" + device_id;
	pars += "&gw_type=" + <s:property value="infoType" />;
	pars += "&tt=" + new Date().getTime();
	pars += "&_action=delete";
	var gw_type = <s:property value="infoType" />;
	var deluser;
	if(confirm("是否需要删除设备对应的用户帐号")){
		deluser = true;
	}else{
		deluser = true;
	}
	var _action = "delete";
	$.post(url,{
		device_id:device_id,
		gw_type:gw_type,
		tt:new Date().getTime(),
		_action:_action,
		deluser:deluser
	},function(ajax){	
		eval(ajax);
	});
	return true;
}

function EditDevice(device_id){
	var strpage = "../../Resource/AddDeviceForm.jsp?_action=update&device_id=" + device_id+"&gw_type=" + <s:property value="infoType" />;
	window.location.href=strpage;
}
function DetailDevice(device_id){
	var strpage = "../../Resource/DeviceShow.jsp?device_id=" + device_id;
	window.open(strpage,"","left=20,top=20,width=900,height=650,resizable=yes,scrollbars=yes");
}

function refresh(){
	window.location.href=window.location.href;
}
//-->
</script>

<form name="frm"
	action="<s:url value='/gwms/resource/queryDevice!init.action'/>"
	method="POST">
	<input type="hidden" name="gw_type2" value=>
	<input type="hidden" name="gw_type" value="<s:property value='gw_type'/>">
	<table width="98%" border=0 cellspacing=0 cellpadding=0 align="center">
		<tr>
			<td height="20"></td>
		</tr>
		<TR>
			<TD>
				<TABLE width="100%" height="30" border="0" cellspacing="0"
					cellpadding="0" class="green_gargtd">
					<TR>
						<TD width="164" align="center" class="title_bigwhite">
							设备资源
						</TD>
						<td>

						</td>
					</TR>
				</TABLE>
			</TD>
		</TR>
		<tr>
			<td>
				<table width="100%" border=0 cellspacing=1 cellpadding=2
					bgcolor=#999999>
					<tr bgcolor="#ffffff" >
						<td width='15%' class="column" align="right">
							绑定状态
						</td>
						<td width='35%'>
							<input type="radio" value="2" name="bindState"
								<s:property value='"2".equals(bindState)?"checked":""'/>>
							已绑定
							<input type="radio" value="1" name="bindState"
								<s:property value='"1".equals(bindState)?"checked":""'/>>
							未绑定&nbsp;
						</td>
						<TD class="column" width='15%' align="right">
							时间类型
						</td>
						<td width='35%'>
							<select name="timeType" class="bk">
								<option value="-1"
									<s:property value='"-1".equals(timeType)?"selected":""'/>>
									==请选择==
								</option>
								<option value="1"
									<s:property value='"1".equals(timeType)||(!"2".equals(timeType)&&!"-1".equals(timeType))?"selected":""'/>>
									==上报时间==
								</option>
								<s:if test="bindState==2">
									<option value="2"
										<s:property value='"2".equals(timeType)?"selected":""'/>>
										==绑定时间==
									</option>
								</s:if>
							</select>

						</TD>
					</tr>
					<tr bgcolor=#ffffff>
						<td class="column" align="right">
							开始时间
						</td>
						<td>
							<input type="text" name="starttime" class='bk' readonly
								value="<s:property value='starttime'/>">
							<img name="shortDateimg"
										onClick="WdatePicker({el:document.frm.starttime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
										src="../../images/dateButton.png" width="15" height="12"
										border="0" alt="选择">
						</td>
						<td class="column" align="right">
							结束时间
						</td>
						<td>
							<input type="text" name="endtime" class='bk' readonly
								value="<s:property value='endtime'/>">
							<img name="shortDateimg"
										onClick="WdatePicker({el:document.frm.endtime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
										src="../../images/dateButton.png" width="15" height="12"
										border="0" alt="选择">
						</td>
					</tr>
					<TR>
						<td align="right" class='green_foot' colspan="4">
							<input name="button" type="button" onclick="do_test();"
								class="jianbian" value=" 查 询 ">
						</td>
					</TR>
				</table>
			</td>
		</tr>
		<tr id="trData" style="display: none">
				<td class="colum">
					<div id="QueryData" style="width: 100%; z-index: 1; top: 100px">
						正在查询，请稍等....</div>
				</td>
			</tr>
		<tr>
			<td>
				<table width="100%" border=0 cellspacing=1 cellpadding=2
					bgcolor=#999999 id=userTable>
					<TR class=column>
						<TH colspan="9" align="center">
							<s:if test="bindState==2">已绑定设备列表</s:if>
							<s:else>未绑定设备列表</s:else>
						</TH>
					</TR>
					<tr>
						<s:iterator value="title" status="status">
							<td class="green_title">
								<s:property value="title[#status.index]" />
							</td>

						</s:iterator>
						<td class="green_title" align='center' width="10%">
							操作
						</td>
					</tr>
					<s:if test="data.size()>0">
						<s:iterator value="data">
							<tr bgcolor="#ffffff">
								<s:if test="infoType == 1">
									<td class=column nowrap align="center">
										<s:property value="city_name" />
									</td>
									<td class=column nowrap align="center">
										<s:property value="vendor_add" />
									</td>
									<td class=column nowrap align="center">
										<s:property value="device_model" />
									</td>
									<td class=column nowrap align="center">
										<s:property value="device" />
									</td>
									<td class=column nowrap align="center">
										<s:property value="loopback_ip" />
									</td>
									<s:if test="bindState==2">
										<td class=column nowrap align="center">
											<s:property value="username" />
										</td>
										<td class=column nowrap align="center">
											<s:property value="binddate" />
										</td>
									</s:if>
									<td class=column nowrap align="center">
										<s:property value="complete_time" />
									</td>

									<td class=column nowrap align="center">
										<IMG SRC="<s:url value="/images/edit.gif"/>" BORDER='0'
											ALT='编辑'
											onclick="EditDevice('<s:property value="device_id"/>')"
											style='cursor: hand'>
										<IMG SRC="<s:url value="/images/del.gif"/>" BORDER='0'
											ALT='删除'
											onclick="DelDevice('<s:property value="device_id"/>')"
											style='cursor: hand'>
										<IMG SRC="<s:url value="/images/view.gif"/>" BORDER="0"
											ALT="详细信息"
											onclick="DetailDevice('<s:property value="device_id"/>')"
											style="cursor: hand">
									</td>
								</s:if>
								<s:else>
									<td class=column nowrap align="center">
										<s:property value="city_name" />
									</td>
									<td class=column nowrap align="center">
										<s:property value="vendor_add" />
									</td>
									<td class=column nowrap align="center">
										<s:property value="device_model" />
									</td>
									<td class=column nowrap align="center">
										<s:property value="device" />
									</td>
									<td class=column nowrap align="center">
										<s:property value="loopback_ip" />
									</td>
									<s:if test="bindState==2">
										<td class=column nowrap align="center">
											<s:property value="username" />
										</td>
										<td class=column nowrap align="center">
											<s:property value="binddate" />
										</td>
									</s:if>
									<td class=column nowrap align="center">
										<s:property value="complete_time" />
									</td>

									<td class=column nowrap align="center">
										<IMG SRC="<s:url value="/images/edit.gif"/>" BORDER='0'
											ALT='编辑'
											onclick="EditDevice('<s:property value="device_id"/>')"
											style='cursor: hand'>
										<IMG SRC="<s:url value="/images/del.gif"/>" BORDER='0'
											ALT='删除'
											onclick="DelDevice('<s:property value="device_id"/>')"
											style='cursor: hand'>
										<IMG SRC="<s:url value="/images/view.gif"/>" BORDER="0"
											ALT="详细信息"
											onclick="DetailDevice('<s:property value="device_id"/>')"
											style="cursor: hand">
									</td>
								</s:else>
							</tr>
						</s:iterator>
					</s:if>
					<s:else>

						<tr>
							<td colspan=<s:property value='"2".equals(bindState)?"9":"7"'/>
								align=left class=column>
								系统没有相关的设备信息!
							</td>
						</tr>

					</s:else>

				</table>
			</td>
		</tr>
		<s:if test='#session.isReport=="1"'>
		<tr>
			<td align=left>
				<IMG SRC="<s:url value="/images/excel.gif"/>" BORDER='0' ALT='导出列表'
					style='cursor: hand' onclick="ToExcel()">
			</td>
		</tr>
		</s:if>
		<tr>
			<td align="right">
				<lk:pages url="/gwms/resource/queryDevice!goPage.action"
					styleClass="" showType="" isGoTo="true" changeNum="true" />
			</td>
		</tr>
		<tr STYLE="display: none">
			<s:if test="infoType == 1">
				<td>
					<iframe id="childFrm" src=""></iframe>
				</td>
			</s:if>
			<s:else>
				<td>
					<iframe id="childFrm" src=""></iframe>
				</td>
			</s:else>
		</tr>
		<TR>
			<TD HEIGHT=20 align="center">
				<div id="_process"></div>
			</TD>
		</TR>
	</table>
</form>

<%@ include file="../foot.jsp"%>
