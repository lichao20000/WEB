<%@ include file="../../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<link rel="stylesheet" href="<s:url value="/css/css_blue.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css/css_green.css"/>"
	type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/jquery.js"/>"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/edittable.js"/>"></SCRIPT>
<script type="text/javascript"
			src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<script type="text/javascript">
var gw_type = '<s:property value="gw_type"/>';
var instname = '<%= LipossGlobals.getLipossProperty("InstArea.ShortName")%>';

$(function() 
		   {
			 var isFirst = '<s:property value="no_query"/>' ;
				 if(isFirst!='true'){
					$("input[@name='button']").attr("disabled", false); 
		    }
		});



function ToExcel() {
	var page="../../gwms/resource/queryDevice!getInfoExcelUnconfirmDevice.action?"
		+ "&starttime=" + document.frm.starttime.value
		+ "&endtime=" + document.frm.endtime.value
		+ "&timeType=" + document.frm.timeType.value
		+ "&device_serialnumber=" + $.trim(document.frm.device_serialnumber.value)
		+ "&loopback_ip=" + document.frm.loopback_ip.value;
	document.all("childFrm").src=page;
	//window.open(page);
}

function do_test() {
	
	var device_serialnumber = $.trim(document.frm.device_serialnumber.value);
	var loopback_ip = $.trim(document.frm.loopback_ip.value);
	
	if("nx_lt" == instname || device_serialnumber.length != 0 || loopback_ip.length != 0 ){
	}else{ // �������������û��ֵ������ִ�в�ѯ
			alert("�豸���кš��豸IP��ַ��������һ����");
			document.frm.device_serialnumber.focus();
			return false;
	}
	if(device_serialnumber.length<6&&device_serialnumber.length>0){
		alert("�������������6λ�豸���кŽ��в�ѯ��");
		document.frm.device_serialnumber.focus();
		return false;
	}
	
	document.frm.gw_type.value = gw_type;
	
	$("input[@name='button']").attr("disabled", true); 
	
	frm.submit();
}

function DelDevice(device_id){
	if(!confirm("���Ҫɾ���������豸��\n��������ɾ���Ĳ��ָܻ�������")){
		return false;
	}
	var url = "../../Resource/DeviceSave.jsp";
	var pars = "device_id=" + device_id;
	pars += "&gw_type=" + <s:property value="infoType" />;
	pars += "&tt=" + new Date().getTime();
	pars += "&_action=delete";
	var gw_type = <s:property value="infoType" />;
	var deluser;
	if(confirm("�Ƿ���Ҫɾ���豸��Ӧ���û��ʺ�")){
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
function ConfirmDev(device_id){
	if(!confirm("ȷ��Ҫȷ�ϸ��豸������")){
		return false;
	}
	var url = "../../Resource/DeviceSave.jsp";
	var gw_type = <s:property value="infoType" />;
	var _action = "status";
	$.post(url,{
		device_id:device_id,
		gw_type:gw_type,
		tt:new Date().getTime(),
		_action:_action
	},function(ajax){	
		alert($.trim(ajax));
		//do_test();
		//resetFrm();
		document.frm.gw_type.value = gw_type;
		frm.submit();
	});
	return true;
}

function EditDevice(device_id){
	var strpage = "../../Resource/AddDeviceForm.jsp?_action=update&device_id=" + device_id;
	window.location.href=strpage;
}
function DetailDevice(device_id){
	var strpage = "../../Resource/DeviceShow.jsp?device_id=" + device_id;
	window.open(strpage,"","left=20,top=20,width=900,height=650,resizable=yes,scrollbars=yes");
}

function refresh(){
	window.location.href=window.location.href;
}

//reset
function resetFrm() {
	document.frm.starttime.value="";
	document.frm.endtime.value="";
	document.frm.device_serialnumber.value="";
	document.frm.loopback_ip.value="";
	document.frm.timeType.value="-1";
	document.frm.all_select.value="";
}

//ȫѡ
function SelectAll(_this,_name){
    var device_serialnumber = $.trim(document.frm.device_serialnumber.value);
    var loopback_ip = $.trim(document.frm.loopback_ip.value);
	var chkArr = document.all(_name);
	if(_this.checked){
		if(typeof(chkArr) == "object" ) {
			if(typeof(chkArr.length) != "undefined") {
				for(var i=0; i<chkArr.length; i++){
					chkArr[i].checked = true;
					if("nx_lt" == instname)
					{
					    chkArr[i].disabled = true;
                    }
				}
			} else {
				chkArr.checked = true;
			}
			if("nx_lt" == instname && "" == device_serialnumber && "" == loopback_ip)
			{
			    document.frm.all_select.value = true;
			}
		}
	}else{
		if(typeof(chkArr) == "object" ) {
			if(typeof(chkArr.length) != "undefined") {
				for(var i=0; i<chkArr.length; i++){
					chkArr[i].checked = false;
					if("nx_lt" == instname)
                    {
                        chkArr[i].disabled = false;
                    }
				}
			} else {
				chkArr.checked = false;
			}
			if("nx_lt" == instname && "" == device_serialnumber && "" == loopback_ip)
            {
                document.frm.all_select.value = false;
            }
		}
	}
}

//���ݸ�ѡ��ѡ��״̬����ȡ�豸id
function getDeviceIDByCheck(_name){
	var arrObj = document.all(_name);
	var strDeviceIDs = "";
	if(typeof(arrObj.length) == "undefined"){
		if(arrObj.checked){
			strDeviceIDs = arrObj.value + ",";
		}
	}else{
		for(var i=0;i<arrObj.length;i++){
			if(arrObj[i].checked){
				strDeviceIDs += arrObj[i].value + ",";
			}
		}
	}
	strDeviceIDs = strDeviceIDs.substring(0, strDeviceIDs.length-1);
	return strDeviceIDs;
}

//����ȷ���豸
function ConfirmDevs(_name){
	var strDeviceIDs = getDeviceIDByCheck(_name);
	var device_id = strDeviceIDs.split(",");
	var all_select = $.trim(document.frm.all_select.value);
	if(strDeviceIDs == ""){
		alert("��ѡ��Ҫȷ�ϵ��豸");
		return false;
	}
	if(!confirm("ȷ��Ҫȷ��ѡ�е��豸��")){
		return false;
	}
	var url = "../../Resource/DeviceSave.jsp";
	var gw_type = <s:property value="infoType" />;
	var _action = "status";
	$.post(url,{
		device_id:device_id,
		gw_type:gw_type,
		tt:new Date().getTime(),
		all_select:all_select,
		_action:_action
	},function(ajax){	
		alert($.trim(ajax));
		//resetFrm();
		frm.submit();
	});
	return true;
}

</script>

<form name="frm"
	action="<s:url value='/gwms/resource/queryDevice!unconfirmDevice.action"'/>"
	method="POST">

	<input type="hidden" name="gw_type" value="" >
    <input type="hidden" name="all_select" value="" >
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
							�豸��Դ
						</TD>
						<td>
							&nbsp;
							<img src="<s:url value="/images/attention_2.gif"/>" width="15"
								height="12">
							&nbsp; δȷ���豸�б�,ѡ��ʱ������ȷ����Ҫ��ѯ��ʱ�䡣
						</td>
					</TR>
				</TABLE>
			</TD>
		</TR>
		<tr>
			<td bgcolor=#999999>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
					<tr>
						<th colspan="4">
							δȷ���豸��ѯ
						</th>
					</tr>
					<TR bgcolor=#ffffff>
						<td class="column" width='15%' align="right">
							�豸���кţ�
						</td>
						<td width='35%' align="left">
							<input name="device_serialnumber" type="text" class='bk'
								value="<s:property value='device_serialnumber'/>">
								<%if(!"nx_lt".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))) {%>
								<font color="red">*</font>
								<%} %>
						</td>
						<td class="column" width='15%' align="right">
							�豸IP��
						</td>
						<td width='35%' align="left">
							<input name="loopback_ip" type="text" class='bk'
								value="<s:property value='loopback_ip'/>">
								<%if(!"nx_lt".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))) {%>
								<font color="red">*</font>
								<%} %>
						</td>
					</TR>
					<TR bgcolor=#ffffff>
						<td class="column" width='15%' align="right">
							��ʼʱ��
						</td>
						<td width='35%' align="left">
							<input type="text" name="starttime" class='bk' readonly
								value="<s:property value='starttime'/>">
							<img name="shortDateimg"
										onClick="WdatePicker({el:document.frm.starttime,dateFmt:'yyyy-MM-dd',skin:'whyGreen'})"
										src="../../images/dateButton.png" width="15" height="12"
										border="0" alt="ѡ��">
						</td>
						<td class="column" width='15%' align="right">
							����ʱ��
						</td>
						<td width='35%' align="left">
							<input type="text" name="endtime" class='bk' readonly
								value="<s:property value='endtime'/>">
							<img name="shortDateimg"
										onClick="WdatePicker({el:document.frm.endtime,dateFmt:'yyyy-MM-dd',skin:'whyGreen'})"
										src="../../images/dateButton.png" width="15" height="12"
										border="0" alt="ѡ��">
						</td>
					</TR>
					<TR bgcolor=#ffffff>
						<td class="column" width='15%' align="right">
							��ѯʱ������
						</td>
						<td width='35%' align="left" colspan="3">
							<select name="timeType" class="bk">
								<option value="-1"
									<s:property value='"-1".equals(timeType)?"selected":""'/>>
									==��ѡ��==
								</option>
								<option value="1"
									<s:property value='"1".equals(timeType)||(!"2".equals(timeType)&&!"-1".equals(timeType))?"selected":""'/>>
									==�ϱ�ʱ��==
								</option>
								<option value="2"
									<s:property value='"2".equals(timeType)?"selected":""'/>>
									==��ʱ��==
								</option>
							</select>
						</td>

					</TR>

					<TR>
						<td class="green_foot" colspan="4" align="right">
							<input class="btn" name="button" type="button"
								onclick="do_test();" value=" �� ѯ ">
							<INPUT CLASS="btn" TYPE="button" value=" �� �� "
								onclick="resetFrm()">
							<s:if test='#session.isReport=="1"'>
								<INPUT TYPE="hidden" value=" �� �� " class=btn onclick="ToExcel()">
							</s:if>
						</TD>
					</TR>

				</TABLE>
			</TD>
		</TR>
		<tr>
			<td bgcolor=#ffffff>
				&nbsp;
			</td>
		</tr>
		<tr>
			<td>
				<table width="100%" border=0 cellspacing=1 cellpadding=2
					bgcolor=#999999 id=userTable>
					<tr>
						<td class="green_title">
							<input type=checkbox onclick="SelectAll(this,'chkCheck')">
							ȫѡ
						</td>
						<s:iterator value="title" status="status">
							<td class="green_title">
								<s:property value="title[#status.index]" />
							</td>

						</s:iterator>
						<td class="green_title" align='center' width="10%">
							����
						</td>
					</tr>
					<s:if test="data.size()>0">
						<s:iterator value="data">
							<tr bgcolor="#ffffff">
								<s:if test="infoType == 1">
									<TD class=column2 align='center' nowrap>
										<input type=checkbox name=chkCheck
											value='<s:property value="device_id"/>'>
									</TD>
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
									<td class=column nowrap align="center">
										<s:property value="complete_time" />
									</td>
									<td class=column nowrap align="center">
										<!-- <IMG SRC="<s:url value="/images/edit.gif"/>" BORDER='0'
											ALT='�༭'
											onclick="EditDevice('<s:property value="device_id"/>')"
											style='cursor: hand'>
										<IMG SRC="<s:url value="/images/del.gif"/>" BORDER='0'
											ALT='ɾ��'
											onclick="DelDevice('<s:property value="device_id"/>')"
											style='cursor: hand'> -->
										<IMG SRC="<s:url value="/images/view.gif"/>" BORDER="0"
											ALT="��ϸ��Ϣ"
											onclick="DetailDevice('<s:property value="device_id"/>')"
											style="cursor: hand">
										<IMG SRC="<s:url value="/images/check1.gif"/>" BORDER="0"
											ALT="ȷ���豸"
											onclick="ConfirmDev('<s:property value="device_id"/>')"
											style="cursor: hand">
									</td>
								</s:if>
								<s:else>
									<TD class=column2 align='center' nowrap>
										<input type=checkbox name=chkCheck
											value='<s:property value="device_id"/>'>
									</TD>
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
									<td class=column nowrap align="center">
										<s:property value="complete_time" />
									</td>
									<td class=column nowrap align="center">
										<!-- <IMG SRC="<s:url value="/images/edit.gif"/>" BORDER='0'
											ALT='�༭'
											onclick="EditDevice('<s:property value="device_id"/>')"
											style='cursor: hand'>
										<IMG SRC="<s:url value="/images/del.gif"/>" BORDER='0'
											ALT='ɾ��'
											onclick="DelDevice('<s:property value="device_id"/>')"
											style='cursor: hand'> -->
										<IMG SRC="<s:url value="/images/view.gif"/>" BORDER="0"
											ALT="��ϸ��Ϣ"
											onclick="DetailDevice('<s:property value="device_id"/>')"
											style="cursor: hand">
										<IMG SRC="<s:url value="/images/check1.gif"/>" BORDER="0"
											ALT="ȷ���豸"
											onclick="ConfirmDev('<s:property value="device_id"/>')"
											style="cursor: hand">
									</td>
								</s:else>
							</tr>
						</s:iterator>
					</s:if>
					<s:else>

						<tr>
							<td colspan=8 align=left class=column>
								ϵͳû����ص��豸��Ϣ!
							</td>
						</tr>

					</s:else>
					<tr>
						<td align="right" class=column2 colspan="8">
						[������<s:property value="total"/>]
							<lk:pages
								url="/gwms/resource/queryDevice!gopageUnconfirmDevice.action"
								styleClass="" showType="" isGoTo="true" changeNum="true" />
						</td>
					</tr>
					<tr>
						<td class=column2 colspan="8" height="25">
							&nbsp;&nbsp;
							<a href=javascript: // onclick="ConfirmDevs('chkCheck')"><strong>����ȷ��</strong>
							</a>
						</td>
					</tr>
				</table>
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
