<%@ include file="../timelater.jsp"%>
<%@ page import="com.linkage.litms.common.database.*,java.util.List,java.util.ArrayList"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<meta http-equiv="x-ua-compatible" content="IE=7" >
<%@taglib prefix="ms" uri="/ailk-itms-web-tags"%>
<jsp:useBean id="qualityAct" scope="request" class="com.linkage.litms.resource.QualityServiceAction" />
<%@ page contentType="text/html;charset=GBK"%>

<%
	List cityList = new ArrayList();
	Cursor cursor = qualityAct.getCityList(request);
	
	String startTime = qualityAct.getString(request.getParameter("startTime"));
	String endTime = qualityAct.getString(request.getParameter("endTime"));
	String start_Time = qualityAct.getString(request.getParameter("start_Time"));
	String end_Time = qualityAct.getString(request.getParameter("end_Time"));

	// rela_dev_type_id
	String vendorId = request.getParameter("vendorId"); 
	String modelId = request.getParameter("modelId");
	String deviceTypeId = request.getParameter("deviceTypeId");
	String gw_type = request.getParameter("gw_type");
	//String sqlid = request.getParameter("sqlid");
		//os_jiank
	String cityList1 = qualityAct.getCityId(cursor);
	String strVendorId = qualityAct.getVendorHtml();
	
	
	//String strModelId = qualityAct.getModelHtml(vendorId, modelId, gw_type);
	//String strDeviceTypeId = qualityAct.getDeviceTypeHtml(modelId, deviceTypeId, gw_type);
	String is_normal = request.getParameter("is_normal");
	String isBind = request.getParameter("isBind");
	/* JLDX-REQ-20180606-JIANGHAO6-002 ����������� */
	//String use_state = request.getParameter("use_state");
	
	String rela_dev_type_id = request.getParameter("rela_dev_type_id");
	int count = cursor.getRecordSize() + 4;
%>

<script type="text/javascript" src="../Js/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript">

function checkform(){
	var start_Time = document.form1.start_Time.value;
	var end_Time = document.form1.end_Time.value;
	//var use_state = document.form1.use_state.value;
	if(""!=start_Time && null!=start_Time){
		document.form1.startTime.value = strTime2Second(start_Time+" 0:0:0");
	}
	if(""!=end_Time && null!=end_Time){
		document.form1.endTime.value = strTime2Second(end_Time+" 23:59:59");
	}
	var deviceTypeId = $("select[@name='deviceTypeId']").val();
	$("#trData").show();
	$("#QueryData").html("<font style='display:block; text-align:center;font-size:20px' >���ڲ�ѯ�����Ե�....</font>");
    $("#queryButGrey").attr("disabled", true);
    var form = document.getElementById("form1");
    form.action="qualityReportList.jsp";
  	form.submit();
}

function change_select(str){
	var gw_type= document.form1.gw_type.value;
	if("deviceModel"==str){
		var url = "<s:url value="/gwms/share/gwDeviceQuery!getDeviceModelQuality.action"/>";
		var vendorId = $("select[@name='vendorId']").val();
		if("-1"==vendorId){
			$("select[@name='modelId']").html("<option value='-1'>==����ѡ����==</option>");
			$("select[@name='deviceTypeId']").html("<option value='-1'>==����ѡ���豸�ͺ�==</option>");
			return;
		}
		$.post(url,{
			gw_type:gw_type,
			gwShare_vendorId:vendorId
		},function(ajax){
			parseMessage(ajax, $("select[@name='modelId']"));
			$("select[@name='deviceTypeId']").html("<option value='-1'>==����ѡ���豸�ͺ�==</option>");
		});
	}
	if("deviceType"==str){
		var url = "<s:url value="/gwms/share/gwDeviceQuery!getDevicetypeQuality.action"/>";
		var vendorId = $("select[@name='vendorId']").val();
		var deviceModelId = $("select[@name='modelId']").val();
		if("-1"==deviceModelId){
			$("select[@name='deviceTypeId']").html("<option value='-1'>==����ѡ���豸�ͺ�==</option>");
			return;
		}
		$.post(url,{
			gw_type:gw_type,
			gwShare_vendorId:vendorId,
			gwShare_deviceModelId:deviceModelId
		},function(ajax){
			parseMessage(ajax,$("select[@name='deviceTypeId']"));
		});
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
function parseMessage(ajax,field){
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
		option = "<option value='"+xValue+"' selected>=="+xText+"==</option>";
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

function strTime2Second(dateStr){
	
	var temp = dateStr.split(' ')
	var date = temp[0].split('-');   
    var time = temp[1].split(':'); 
	
	var reqReplyDate = new Date();
	reqReplyDate.setYear(date[0]);
    reqReplyDate.setMonth(date[1]-1);
    reqReplyDate.setDate(date[2]);
    reqReplyDate.setHours(time[0]);
    reqReplyDate.setMinutes(time[1]);
    reqReplyDate.setSeconds(time[2]);

	return Math.floor(reqReplyDate.getTime()/1000);
}

//** iframe�Զ���Ӧҳ�� **//
//������ϣ������ҳ��߶��Զ������߶ȵ�iframe�����Ƶ��б�
//�ö��Ű�ÿ��iframe��ID�ָ�. ����: ["myframe1", "myframe2"]������ֻ��һ�����壬���ö��š�
//����iframe��ID
var iframeids=["dataForm"]

//����û����������֧��iframe�Ƿ�iframe���� yes ��ʾ���أ�no��ʾ������
var iframehide="yes"

function dyniframesize() 
{
	var dyniframe=new Array()
	for (i=0; i<iframeids.length; i++)
	{
		if (document.getElementById)
		{
			//�Զ�����iframe�߶�
			dyniframe[dyniframe.length] = document.getElementById(iframeids[i]);
			if (dyniframe[i] && !window.opera)
			{
   			dyniframe[i].style.display="block"
   			//����û����������NetScape
   			if (dyniframe[i].contentDocument && dyniframe[i].contentDocument.body.offsetHeight)
    				dyniframe[i].height = dyniframe[i].contentDocument.body.offsetHeight; 
    			//����û����������IE
   			else if (dyniframe[i].Document && dyniframe[i].Document.body.scrollHeight) 
    				dyniframe[i].height = dyniframe[i].Document.body.scrollHeight;
 			 }
 		}
		//�����趨�Ĳ���������֧��iframe�����������ʾ����
		if ((document.all || document.getElementById) && iframehide=="no")
		{
			var tempobj=document.all? document.all[iframeids[i]] : document.getElementById(iframeids[i])
  		tempobj.style.display="block"
		}
	}
}

$(function(){
	dyniframesize();
});
$(function(){
	var instArea = $('#instArea').val();
	var gw_type=$('#gw_type').val();
});

$(window).resize(function(){
	dyniframesize();
}); 
function useQuery(){
	$("#queryButGrey").attr("disabled", false);
}
function IFrameReSizeWidth(iframename) {

����var pTar = document.getElementById(iframename);

����if (pTar) {  //ff

����if (pTar.contentDocument && pTar.contentDocument.body.offsetWidth) {

����pTar.width = pTar.contentDocument.body.offsetWidth;

����}  //ie

����else if (pTar.Document && pTar.Document.body.scrollWidth) {

����pTar.width = pTar.Document.body.scrollWidth;

����}

����}
		
����}
</SCRIPT>
<%@ include file="../head.jsp"%>
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<form name="form1" action="" method="post" target="dataForm" >
<input type="hidden" name="gw_type"  id="gw_type" value='<%=gw_type%>' /> 
<input type="hidden" name="isBind" value='<%=isBind%>' />
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD HEIGHT=20>&nbsp;</TD>
	</TR>
	<TR>
		<TD>
		<TABLE width="100%" border=0 cellspacing=0 cellpadding=0 align="center">
			<TR>
				<TD>
				<TABLE width="100%" height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
					<TR>
						<TD width="164" align="center" class="title_bigwhite">��è�汾ͳ�Ʊ���</TD>
					</TR>
				</TABLE>
				</TD>
			</TR>
			<TR>
				<TD>
				<TABLE width="100%" height="30" border="0" cellspacing=1 cellpadding=2 bgcolor=#999999>
					<TR BGCOLOR=#ffffff>
						<TD class=column align="right">����</TD>
						<TD>
							<select id="vendorId" name="vendorId"
								class="bk" onchange="change_select('deviceModel')"><%=strVendorId%></select>
						</TD>
						<TD class=column align="right">�ͺ�</TD>
						<TD>
							<select id="modelId" name="modelId" class="bk"
								onchange="change_select('deviceType')"><option value='-1'>==����ѡ����==</option></select>
						</TD>
						<TD class=column align="right">�汾</TD>
						<TD>
							<select name="deviceTypeId" class="bk"><option value='-1'>==����ѡ���ͺ�==</option></select>
						</TD>
					</TR>
					
					<TR BGCOLOR=#ffffff>
						<TD class=column align="right">��ʼʱ��</TD>
						<TD>
							<input type="text" name="start_Time" value='<%=startTime%>' size="12" readonly class=bk> 
							<input type="hidden" name="startTime" value='<%=startTime%>' readonly class=bk> 
							<img name="shortDateimg" onClick="WdatePicker({el:document.form1.start_Time,dateFmt:'yyyy-MM-dd',skin:'whyGreen'})"
								src="../images/dateButton.png" width="15" height="12" border="0" alt="ѡ��" /> (YYYY-MM-DD) 
						</TD>
						<TD class=column align="right">����ʱ��</TD>
						<TD  colspan="3">
							<input type="text" name="end_Time" value='<%=end_Time%>' size="12" readonly class=bk> 
                       		<input type="hidden" name="endTime" value='<%=endTime%>' readonly class=bk> 
                       		<img name="shortDateimg" onClick="WdatePicker({el:document.form1.end_Time,dateFmt:'yyyy-MM-dd',skin:'whyGreen'})"
								src="../images/dateButton.png" width="15" height="12" border="0" alt="ѡ��" /> (YYYY-MM-DD)
						</TD>
						<TD class=column align="right" style="display: none">�Ƿ���������</TD>
						<TD style="display: none">
							<select name="is_esurfing" id="is_esurfing" class="bk">
								<option value="-1">==��ѡ��==</option>
								<option value="1">��������</option>
							</select>
						</TD>
					</TR>
					<TR BGCOLOR=#ffffff style="display: none">
						<TD class=column align="right">�豸����</TD>
						<TD>
							<select name="rela_dev_type_id" class="bk"  >
							<option value="-1">==��ѡ��==</option>
							<option value="1">e8-b</option>
							<option value="2">e8-c</option>
							</select>
						</TD>
						<TD class=column align="right"> �Ƿ�����</TD>
						<TD>
							<select name="is_normal" id="is_normal" class="bk"  >
							<option value="-1">==��ѡ��==</option>
							<option value="1">��</option>
							<option value="0">��</option>
							</select>
						</TD>
						<TD></TD>
						<TD>
						</TD>
					</TR>
					<TR BGCOLOR=#ffffff>
						<td class=column bgcolor="#FFFFFF" style="display: none" name="city" id="city">
							����<select name="city_id" id="city_id" class="bk"><%=cityList1%></select>
						</td>
						<td colspan="6" align="right">
							<input type="submit" name="btn0" id="queryButGrey"  
								value=" �� ѯ " class="jianbian" onclick="javascript:checkform()">
						</td>
					</TR>
				</TABLE>
				</TD>
			</TR>
			<tr>
				<td height="20"></td>
			</tr>
			<tr id="trData" style="display: none">
				<td class="colum">
					<div id="QueryData" style="width: 100%; z-index: 1; top: 100px">
						����ͳ�ƣ����Ե�....
					</div>
				</td>
			</tr>
			<TR>
				<TD bgcolor=#999999>
					<iframe id="dataForm" name="dataForm" height="0" frameborder="0" scrolling="yes" width="100%" src="">
					</iframe>
				</TD>
			</TR>
		</TABLE>
		<input type="hidden" id="instArea" value="<%=LipossGlobals.getLipossProperty("InstArea.ShortName") %>">
		</TD>
	</TR>
</TABLE>
</form>
<%@ include file="../foot.jsp"%>
<%
	qualityAct = null;
%>