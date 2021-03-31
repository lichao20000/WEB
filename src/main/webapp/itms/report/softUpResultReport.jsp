<%@ include file="/timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="ms" uri="/ailk-itms-web-tags"%>

<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>" type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/jquery.js"/>"></SCRIPT>
<script type="text/javascript" src="../../Js/My97DatePicker/WdatePicker.js"></script>

<script language="JavaScript">
$(function(){
	gwShare_change_select("vendor","-1");
});

var gw_type = '<s:property value="gw_type"/>';
var isSoftUp = '<s:property value="isSoftUp"/>';
function doQuery()
{
	var cityId = $.trim($("select[@name='cityId']").val());
    var starttime = $.trim($("input[@name='starttime']").val());
    var endtime = $.trim($("input[@name='endtime']").val());
    
    var vendorId="";
    var deviceModelId="";
    <%if (LipossGlobals.inArea("sd_dx") || LipossGlobals.inArea("jx_dx")) { %>
    	vendorId=$("select[@name='gwShare_vendorId']").val()
    	deviceModelId=$("select[@name='gwShare_deviceModelId']").val()
    <%}%>
    
    if(cityId == "-1"){
         alert("��ѡ������");
         return false;
    }
    $("tr[@id='trData']").show();
    $("button[@name='button']").attr("disabled", true);
    $("div[@id='QueryData']").html("����ͳ�ƣ����Ե�....");
    var url = '<s:url value='/itms/report/softUpResult.action'/>'; 
	$.post(url,{
		cityId:cityId,
		starttime:starttime,
		endtime:endtime,
		gw_type:gw_type,
		isSoftUp:isSoftUp,
		vendorId:vendorId,
		deviceModelId:deviceModelId
	},function(ajax){	
	    $("div[@id='QueryData']").html("");
		$("div[@id='QueryData']").append(ajax);
		$("button[@name='button']").attr("disabled", false);
	});
}

function countBycityId(cityId,starttime,endtime,vendorId,deviceModelId)
{
    $("tr[@id='trData']").show();
    $("input[@name='button']").attr("disabled", true);
    $("div[@id='QueryData']").html("����ͳ�ƣ����Ե�....");
    var url = '<s:url value='/itms/report/softUpResult.action'/>'; 
	$.post(url,{
		cityId:cityId,
		starttime:starttime,
		endtime:endtime,
		gw_type:gw_type,
		isSoftUp:isSoftUp,
		vendorId:vendorId,
		deviceModelId:deviceModelId
	},function(ajax){	
	    $("div[@id='QueryData']").html("");
		$("div[@id='QueryData']").append(ajax);
		$("input[@name='button']").attr("disabled", false);
	});
}

function ToExcel(cityId,starttime1,endtime1,vendorId,deviceModelId) 
{
	var page="<s:url value='/itms/report/softUpResult!getExcel.action'/>?"
		+ "&starttime1=" + starttime1
		+ "&endtime1=" + endtime1
		+ "&cityId=" + cityId
		+ "&isSoftUp=" + isSoftUp
		+ "&gw_type=" +gw_type
		+ "&vendorId="+vendorId
		+ "&deviceModelId="+deviceModelId;
	document.all("childFrm").src=page;
}

function openDev(cityId,starttime1,endtime1,status,resultId,isMgr,vendorId,deviceModelId)
{
	var page="<s:url value='/itms/report/softUpResult!getDev.action'/>?"
		+ "cityId=" + cityId 
		+ "&starttime1=" +starttime1
		+ "&endtime1=" +endtime1
		+ "&status=" +status
		+ "&resultId=" +resultId
		+ "&isMgr=" +isMgr
		+ "&isSoftUp=" + isSoftUp
		+ "&gw_type=" +gw_type
		+ "&vendorId="+vendorId
		+ "&deviceModelId="+deviceModelId;
	window.open(page,"","left=50,top=50,height=550,width=900,toolbar=no,menubar=no,location=no,resizable=yes scrollbars=yes");
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
function gwShare_change_select(type,selectvalue)
{
	var gwType = $("input[@name='gw_type']").val();
	switch (type)
	{
		case "vendor":
			var url;
			if("4" == gwType){
				//�����в�ѯ���̱�
				url = "<s:url value="/gtms/stb/share/shareDeviceQuery!getVendor.action"/>";
			}else{
				url = "<s:url value="/gwms/share/gwDeviceQuery!getVendor.action"/>";
			}
			$.post(url,{
			},function(ajax){
				gwShare_parseMessage(ajax,$("select[@name='gwShare_vendorId']"),selectvalue);
				$("select[@name='gwShare_deviceModelId']").html("<option value='-1'>==����ѡ���豸����==</option>");
				$("select[@name='gwShare_devicetypeId']").html("<option value='-1'>==����ѡ���豸�ͺ�==</option>");
			});
			break;
		case "deviceModel":
			var url;
			if("4" == gwType){
				//�����в�ѯ�豸�ͺ�
				url = "<s:url value="/gtms/stb/share/shareDeviceQuery!getDeviceModel.action"/>";
			}else{
				url = "<s:url value="/gwms/share/gwDeviceQuery!getDeviceModel.action"/>";
			}
			var vendorId = $("select[@name='gwShare_vendorId']").val();
			if("-1"==vendorId){
				$("select[@name='gwShare_deviceModelId']").html("<option value='-1'>==����ѡ����==</option>");
				$("select[@name='gwShare_devicetypeId']").html("<option value='-1'>==����ѡ���豸�ͺ�==</option>");
				break;
			}
			$.post(url,{
				gwShare_vendorId:vendorId
			},function(ajax){
				gwShare_parseMessage(ajax,$("select[@name='gwShare_deviceModelId']"),selectvalue);
				$("select[@name='gwShare_devicetypeId']").html("<option value='-1'>==����ѡ���豸�ͺ�==</option>");
			});
			break;
		case "devicetype":
			var url;
			if("4" == gwType){
				//�����в�ѯ�汾��
				url = "<s:url value="/gtms/stb/share/shareDeviceQuery!getDevicetype.action"/>";
			}else{
				url = "<s:url value="/gwms/share/gwDeviceQuery!getDevicetype.action"/>";
			}
			var vendorId = $("select[@name='gwShare_vendorId']").val();
			var deviceModelId = $("select[@name='gwShare_deviceModelId']").val();
			if("-1"==deviceModelId){
				$("select[@name='gwShare_devicetypeId']").html("<option value='-1'>==����ѡ���豸�ͺ�==</option>");
				break;
			}
			$.post(url,{
				gwShare_vendorId:vendorId,
				gwShare_deviceModelId:deviceModelId
			},function(ajax){
				gwShare_parseMessage(ajax,$("select[@name='gwShare_devicetypeId']"),selectvalue);
			});
			break;	
		default:
			alert("δ֪��ѯѡ�");
			break;
	}	
}

//������ѯ�豸�ͺŷ���ֵ�ķ���
function gwShare_parseMessage(ajax,field,selectvalue)
{
	if(""==ajax){
		return;
	}
	
	var flag = true;
	
	var lineData = ajax.split("#");
	if(!typeof(lineData) || !typeof(lineData.length)){
		return false;
	}
	field.html("");
	option = "<option value='-1' selected>==��ѡ��==</option>";
	field.append(option);
	for(var i=0;i<lineData.length;i++)
	{
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
						�������ͳ��
					</th>
					<td>
						<img src="<s:url value="/images/attention_2.gif"/>" width="15" height="12">
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
						<th colspan=4>�������ͳ��</th>
					</tr>
					
					<ms:inArea areaCode="sd_dx,jx_dx" notInMode="false">
					<TR>
						<TD align="right" class=column width="15%">��    ��</TD>
						<TD width="35%">
							<select name="gwShare_vendorId" class="bk" onchange="gwShare_change_select('deviceModel','-1')">
								<option value="-1">==��ѡ��==</option>
							</select>
						</TD>
						<TD align="right" class=column width="15%">�豸�ͺ�</TD>
						<TD align="left" width="35%">
							<select name="gwShare_deviceModelId" class="bk" onchange="gwShare_change_select('devicetype','-1')">
								<option value="-1">����ѡ����</option>
							</select>
						</TD>
					</TR>
					</ms:inArea>
					
					<tr bgcolor=#ffffff>
						<td class=column align=center width="15%">
							��ʼʱ��
						</td>
						<td>
							<input type="text" name="starttime" class='bk' readonly
								value="<s:property value='starttime'/>">
							<img name="shortDateimg"
									onClick="WdatePicker({el:document.frm.starttime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
									src="../../images/dateButton.png" width="15" height="12"
									border="0" alt="ѡ��" />
						</td>
						<td class=column align=center width="15%">
							����ʱ��
						</td>
						<td>
							<input type="text" name="endtime" class='bk' readonly
								value="<s:property value='endtime'/>">
							<img name="shortDateimg"
									onClick="WdatePicker({el:document.frm.endtime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
									src="../../images/dateButton.png" width="15" height="12"
									border="0" alt="ѡ��" />					
						</td>
					</tr>
					<tr bgcolor=#ffffff>
						<td class=column align=center width="15%">
							�� ��
						</td>
						<td colspan="3">
							<s:select list="cityList" name="cityId" headerKey="-1"
								headerValue="��ѡ������" listKey="city_id" listValue="city_name"
								value="cityId" cssClass="bk"></s:select>
						</td>
					</tr>
					<tr bgcolor=#ffffff>
						<td class=foot colspan=4 align=right>
							<button onclick="doQuery()" name="button">&nbsp;ͳ ��&nbsp;</button>
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
