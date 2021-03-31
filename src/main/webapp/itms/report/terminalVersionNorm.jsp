<%@ include file="../../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>" type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/jquery.js"/>"></SCRIPT>
<script type="text/javascript" src="../../Js/My97DatePicker/WdatePicker.js"></script>
<script language="JavaScript">
var gw_type = '<s:property value="gw_type"/>';
function init(){
	// ��ʼ������
	gwShare_change_select("city","00");
	gwShare_change_select("vendor","-1");
}
function doQuery(){
	var vendorId = $.trim($("select[@name='vendorId']").val());            // �豸����ID
	var deviceModelId = $.trim($("select[@name='deviceModelId']").val());  // �豸�ͺ�ID
	var cityId = $.trim($("select[@name='cityId']").val());                // �豸����
	var accessType = $.trim($("select[@name='accessType']").val());        //���н��뷽ʽ
	var userType = $.trim($("select[@name='userType']").val());             //�ն�����
	var isActive = $.trim($("select[@name='isActive']").val());             //�ն�����
    var starttime = $.trim($("input[@name='starttime']").val());           // ��ʼʱ��(ע��ʱ��)
    var endtime = $.trim($("input[@name='endtime']").val());               // ����ʱ��(ע��ʱ��)
    if(cityId == "-1"){
         alert("��ѡ������");
         return false;
    }
    $("tr[@id='trData']").show();
    $("div[@id='QueryData']").html("����ͳ�ƣ����Ե�....");
    $("button[@name='button']").attr("disabled", true);
    var url = '<s:url value='/itms/report/terminalVersionNormQuery!queryList.action'/>'; 
	$.post(url,{
		vendorId:vendorId,
		deviceModelId:deviceModelId,
		cityId:cityId,
		accessType:accessType,
		userType:userType,
		isActive:isActive,
		starttime:starttime,
		endtime:endtime,
		gw_type:gw_type
	},function(ajax){
	    $("div[@id='QueryData']").html("");
		$("div[@id='QueryData']").append(ajax);
		 $("button[@name='button']").attr("disabled", false);
	});
	
}

function countBycityId(cityId,accessType,userType,starttime,endtime,vendorId,deviceModelId,isActive){

    $("tr[@id='trData']").show();
    $("div[@id='QueryData']").html("����ͳ�ƣ����Ե�....");
    var url = '<s:url value='/itms/report/terminalVersionNormQuery!queryList.action'/>'; 
	$.post(url,{
		cityId:cityId,
		accessType:accessType,
		userType:userType,
		starttime:starttime,
		endtime:endtime,
		vendorId:vendorId,
		deviceModelId:deviceModelId,
		isActive:isActive,
		gw_type:gw_type
	},function(ajax){	
	    $("div[@id='QueryData']").html("");
		$("div[@id='QueryData']").append(ajax);
	});
}
function ToExcel(cityId,accessType,userType,starttime1,endtime1,vendorId,deviceModelId,deviceTypeId,isActive) {
	var page="<s:url value='/itms/report/terminalVersionNormQuery!getAllResultExcel.action'/>?"
		+ "cityId=" + cityId
		+"&accessType="+accessType
		+"&userType="+userType
		+ "&starttime1=" + starttime1
		+ "&endtime1=" + endtime1
		+ "&vendorId=" + vendorId
		+ "&deviceModelId=" + deviceModelId
		+ "&isActive=" + isActive
		+ "&gw_type=" + gw_type;
	document.all("childFrm").src=page;
}
function openHgw(flag,cityId,accessType,userType, starttime1, endtime1, vendorId, deviceModelId,isActive){
  
	var page="<s:url value='/itms/report/terminalVersionNormQuery!queryDeviceList.action'/>?";
	var url = page
			+ "cityId=" + cityId 
			+"&accessType="+accessType
			+"&userType="+userType
			+ "&starttime1=" +starttime1 
			+ "&endtime1=" +endtime1
			+ "&vendorId=" +vendorId
			+ "&deviceModelId=" +deviceModelId
			+ "&isActive=" +isActive
			+ "&flag="+flag  
			+ "&gw_type=" + gw_type;
	window.open(url,"","left=50,top=50,height=550,width=900,toolbar=no,menubar=no,location=no,resizable=yes scrollbars=yes");
}
/*------------------------------------------------------------------------------
//������:		deviceSelect_change_select
//����  :	type 
	            vendor      �����豸����
	            deviceModel �����豸�ͺ�
	            devicetype  �����豸�汾
//����  :	����ҳ������̡��ͺż�����
//����ֵ:		
//˵��  :	
//����  :	Create 2009-12-25 of By qxq
------------------------------------------------------------------------------*/
function gwShare_change_select(type,selectvalue){
	switch (type){
		case "city":
			var url = "<s:url value="/gwms/share/gwDeviceQuery!getCityNextChild.action"/>";
			$.post(url,{
			},function(ajax){
				gwShare_parseMessage(ajax,$("select[@name='cityId']"),selectvalue);
			});
			break;
		case "vendor":
			var url = "<s:url value="/gwms/share/gwDeviceQuery!getVendor.action"/>";
			$.post(url,{
			},function(ajax){
				gwShare_parseMessage(ajax,$("select[@name='vendorId']"),selectvalue);
				$("select[@name='deviceModelId']").html("<option value='-1'>==����ѡ���豸����==</option>");
				$("select[@name='deviceTypeId']").html("<option value='-1'>==����ѡ���豸�ͺ�==</option>");
			});
			break;
		case "deviceModel":
			var url = "<s:url value="/gwms/share/gwDeviceQuery!getDeviceModel.action"/>";
			var vendorId = $("select[@name='vendorId']").val();
			if("-1"==vendorId){
				$("select[@name='deviceModelId']").html("<option value='-1'>==����ѡ����==</option>");
				$("select[@name='deviceTypeId']").html("<option value='-1'>==����ѡ���豸�ͺ�==</option>");
				break;
			}
			$.post(url,{
				gwShare_vendorId:vendorId
			},function(ajax){
				gwShare_parseMessage(ajax,$("select[@name='deviceModelId']"),selectvalue);
				$("select[@name='deviceTypeId']").html("<option value='-1'>==����ѡ���豸�ͺ�==</option>");
			});
			break;
		case "devicetype":
			var url = "<s:url value="/gwms/share/gwDeviceQuery!getDevicetype.action"/>";
			var vendorId = $("select[@name='vendorId']").val();
			var deviceModelId = $("select[@name='deviceModelId']").val();
			if("-1"==deviceModelId){
				$("select[@name='deviceTypeId']").html("<option value='-1'>==����ѡ���豸�ͺ�==</option>");
				break;
			}
			$.post(url,{
				gwShare_vendorId:vendorId,
				gwShare_deviceModelId:deviceModelId
			},function(ajax){
				gwShare_parseMessage(ajax,$("select[@name='deviceTypeId']"),selectvalue);
			});
			break;	
		default:
			alert("δ֪��ѯѡ�");
			break;
	}	
}

/*------------------------------------------------------------------------------
//������:		deviceSelect_parseMessage
//����  :	ajax 
            	������XXX$XXX#XXX$XXX
            field
            	��Ҫ���ص�jquery����
//����  :	����ajax���ز���
//����ֵ:		
//˵��  :	
//����  :	Create 2009-12-25 of By qxq
------------------------------------------------------------------------------*/
//������ѯ�豸�ͺŷ���ֵ�ķ���
function gwShare_parseMessage(ajax,field,selectvalue){
	var flag = true;
	if(""==ajax){
		return;
	}
	var lineData = ajax.split("#");
	if(!typeof(lineData) || !typeof(lineData.length)){
		return false;
	}
	field.html("");
	option = "<option value='-1' selected>==��ѡ��==</option>";
	field.append(option);
	for(var i=0;i<lineData.length;i++){
		var oneElement = lineData[i].split("$");
		var xValue = oneElement[0];
		var xText = oneElement[1];
		if(selectvalue==xValue){
			flag = false;
			//����ÿ��value��text��ǵ�ֵ����һ��option����
			option = "<option value='"+xValue+"' selected>=="+xText+"==</option>";
		}else{
			//����ÿ��value��text��ǵ�ֵ����һ��option����
			option = "<option value='"+xValue+"'>=="+xText+"==</option>";
		}
		try{
			field.append(option);
		}catch(e){
			alert("�豸�ͺż���ʧ�ܣ�");
		}
	}
	if(flag){
		field.attr("value","-1");
	}
}





</script>

<br>
<TABLE>
	<tr>
		<td>
			<table class="green_gargtd">
				<tr>
					<th>
						�豸�汾�淶��ͳ��
					</th>
					<td>
						<img src="<s:url value="/images/attention_2.gif"/>" width="15" height="12">
						ʱ��Ϊ����ʱ��
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td>
			<form name=frm>
				<table class="querytable">
					<tr>
						<th colspan=4>
							�豸�汾�淶��ͳ��
						</th>
						
					</tr>
					<TR bgcolor="#FFFFFF" id="gwShare_tr21" STYLE="">
						<TD align="right" class=column width="15%">
							�豸����
						</TD>
						<TD align="left" width="35%">
                               <select name="vendorId" class="bk" onchange="gwShare_change_select('deviceModel','-1')">
								<option value="-1">==��ѡ��==</option>
							</select>
						</TD>
						<TD align="right" class=column width="15%">
							�豸�ͺ�
						</TD>
						<TD width="35%">
                               <select name="deviceModelId" class="bk" onchange="gwShare_change_select('devicetype','-1')">
								<option value="-1">����ѡ����</option>
							</select>
						</TD>
					</TR>
					<TR bgcolor="#FFFFFF" id="gwShare_tr21" STYLE="">
						<td class="column" align="right" width="15%">
							�豸����
						</td>
						<td width="35%" >
							<select name="cityId" class="bk">
								<option value="-1">==��ѡ��==</option>
							</select>
							&nbsp;<font style="color:red">*</font>
						</td>
						<td class="column" align="right" width="15%">���뷽ʽ</td>
						<td width="35%">
						    <SELECT name="accessType" class=bk>
						    	<option value="-1">==��ѡ��==</option>
								<option value="1">ADSL����</option>
								<option value="2">LAN����</option>
								<option value="3">EPON����</option>
								<option value="4">GPON����</option>
							</SELECT>
						</td>
					</TR>
					<TR bgcolor="#FFFFFF" id="gwShare_tr21" STYLE="">
							<td class="column" align="right" width="15%">
								�ն�����
							</td>
							<td width="35%">
								<SELECT name="userType">
									<option value="-1">==��ѡ��==</option>
									<option value="1">E8-B</option>
									<option value="2">E8-C</option>
									
								</SELECT>
							</td>
							<td class="column" align="right" width="15%">
								��Ծ�û�
							</td>
							<td width="35%">
								<SELECT name="isActive">
								<option value="-1">==��ѡ��==</option>
								<option value="1">��</option>
								<option value="0">��</option>

						</SELECT>
						</td>
					</TR>
					<tr bgcolor=#ffffff>
						<td class=column align=center width="15%">
							��ʼʱ��
						</td>
						<td>
							<input type="text" name="starttime" class='bk' readonly value="<s:property value='starttime'/>">
							<img name="shortDateimg"
								onClick="WdatePicker({el:document.frm.starttime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
								src="../../images/dateButton.png" width="15" height="12"
								border="0" alt="ѡ��" />
						</td>
						<td class=column align=center width="15%">
							����ʱ��
						</td>
						<td>
							<input type="text" name="endtime" class='bk' readonly value="<s:property value='endtime'/>">
							<img name="shortDateimg"
								onClick="WdatePicker({el:document.frm.endtime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
								src="../../images/dateButton.png" width="15" height="12"
								border="0" alt="ѡ��" />
						</td>
					</tr>
					<tr bgcolor=#ffffff>
						<td class=foot colspan=4 align=right>
							<button onclick="doQuery()" name="button">
								&nbsp;ͳ ��&nbsp;
							</button>
						</td>
					</tr>
				</table>
			</form>
		</td>
	</tr>

	<tr id="trData" style="display: none">
		<td class="colum">
			<div id="QueryData" style="width: 100%; z-index: 1; top: 100px">
				����ͳ�ƣ����Ե�....
			</div>
		</td>
	</tr>
	<tr>
		<td height="20">
		</td>
	</tr>
</TABLE>

<%@ include file="/foot.jsp"%>

<SCRIPT LANGUAGE="JavaScript">
	init();
</SCRIPT>

