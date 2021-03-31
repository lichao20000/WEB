<%@ include file="../timelater.jsp"%>
<%@ page
	import="com.linkage.litms.common.database.*,java.util.List,java.util.ArrayList"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<jsp:useBean id="ServiceAct" scope="request"
	class="com.linkage.litms.resource.ServiceAct" />
<%@ page contentType="text/html;charset=GBK"%>


<%
    String gw_type = request.getParameter("gw_type");

	List cityList = new ArrayList();
	Cursor cursor = ServiceAct.getCityList(request);
	String startTime = request.getParameter("startTime");
	if (null == startTime || "null".equals(startTime)) {
		startTime = "";
	}
	String endTime = request.getParameter("endTime");
	if (null == endTime || "null".equals(endTime)) {
		endTime = "";
	}
	String start_Time = request.getParameter("start_Time");
	if (null == start_Time || "null".equals(start_Time)) {
		start_Time = "";
	}
	String end_Time = request.getParameter("end_Time");
	if (null == end_Time || "null".equals(end_Time)) {
		end_Time = "";
	}
	
	// rela_dev_type_id
	String vendorId = request.getParameter("vendorId"); 
	String modelId = request.getParameter("modelId");
	String cityId = request.getParameter("cityId");
	String deviceTypeId = request.getParameter("deviceTypeId");
	String strCityId = ServiceAct.getCityHtml(request,cityId);
	String strVendorId = ServiceAct.getVendorHtml(vendorId,gw_type);
	String strModelId = ServiceAct.getModelHtml(vendorId, modelId,gw_type);
	
	int count = cursor.getRecordSize() + 4;
%>

<script type="text/javascript" src="../Js/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript">
//�鿴��ϸ��Ϣ
function detail(city_id,vendor_id,model_id,gw_type){
    var count_Time = document.form1.start_Time.value;
    var page = "";
   	page="DeviceListByModel.jsp?cityId="+city_id+"&vendorId="+vendor_id + "&gw_type=" + gw_type + "&modelId="+model_id+"&countTime=" + count_Time;
   	window.open(page,"","left=20,top=20,width=800,height=600,resizable=no,scrollbars=yes");
}

function checkform(){
	
	var count_Time = document.form1.start_Time.value;
	if(count_Time == ""){
		alert("��ѡ����Ҫͳ�Ƶ��·ݣ���");
		return false;
	}
	document.getElementById("countTime").value = count_Time;
	document.form1.submit();
}

function change_select(str){
	if("deviceModel"==str){
		var url = "<s:url value="/gwms/share/gwDeviceQuery!getDeviceModel.action"/>";
		var vendorId = $("select[@name='vendorId']").val();
		if("-1"==vendorId){
			$("select[@name='modelId']").html("<option value='-1'>==����ѡ����==</option>");
			$("select[@name='deviceTypeId']").html("<option value='-1'>==����ѡ���豸�ͺ�==</option>");
			return;
		}
		$.post(url,{
			gwShare_vendorId:vendorId
		},function(ajax){
			parseMessage(ajax,$("select[@name='modelId']"));
			$("select[@name='deviceTypeId']").html("<option value='-1'>==����ѡ���豸�ͺ�==</option>");
		});
	}
	if("deviceType"==str){
		var url = "<s:url value="/gwms/share/gwDeviceQuery!getDevicetype.action"/>";
		var vendorId = $("select[@name='vendorId']").val();
		var deviceModelId = $("select[@name='modelId']").val();
		if("-1"==deviceModelId){
			$("select[@name='deviceTypeId']").html("<option value='-1'>==����ѡ���豸�ͺ�==</option>");
			return;
		}
		$.post(url,{
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

</SCRIPT>
<%@ include file="../head.jsp"%>
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<link href="<s:url value="../css/css_green.css"/>" rel="stylesheet"
	type="text/css">
<form name="form1" action="DeviceStatByModel.jsp"><input
	type="hidden" name="gw_type" value='<%=gw_type%>' />
	<input type="hidden" name="countTime" id="countTime" value=''/>
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD HEIGHT=20>&nbsp;</TD>
	</TR>
	<TR>
		<TD>
		<TABLE width="95%" border=0 cellspacing=0 cellpadding=0 align="center">
			<TR>
				<TD>
				<TABLE width="100%" height="30" border="0" cellspacing="0"
					cellpadding="0" class="green_gargtd">
					<TR>
						<TD width="164" align="center" class="title_bigwhite">���ͺ�ͳ��(����)</TD>
					</TR>
				</TABLE>
				</TD>
			</TR>
			<TR>
				<TD>
				<TABLE width="100%" height="30" border="0" cellspacing=1
					cellpadding=2 bgcolor=#999999>
					<TR>
						<td class=column bgcolor="#FFFFFF">���� <select name="cityId"
							class="bk" onchange="change_select('cityId')"><%=strCityId%></select></td>
						<td class=column bgcolor="#FFFFFF">���� <select name="vendorId"
							class="bk" onchange="change_select('deviceModel')"><%=strVendorId%></select></td>
						<td class=column bgcolor="#FFFFFF">�ͺ�<select name="modelId" class="bk"
							onchange="change_select('deviceType')"><%=strModelId%></select>
							</td>
                    
					</TR>
					<TR>
					
						<td class=column bgcolor="#FFFFFF">ͳ���·� <input type="text"
							name="start_Time" value='<%=start_Time%>' size="12" readonly
							class=bk> <input type="hidden" name="startTime"
							value='<%=startTime%>' readonly class=bk> <img name="shortDateimg"
							onClick="WdatePicker({el:document.form1.start_Time,dateFmt:'yyyy-MM',skin:'whyGreen'})"
							src="../images/dateButton.png" width="15" height="12" border="0"
							alt="ѡ��" /> (YYYY-MM) <font style="color:red">*</font></td>
                           <td class=column bgcolor="#FFFFFF" colspan=2 align="center"><input type="button" name="btn0"
							value=" �� ѯ " onclick="checkform()" class="jianbian">
						</td>
					</TR>
				</TABLE>
				</TD>
			</TR>
			<tr>
				<td height="20"></td>
			</tr>
			<TR>
				<TD bgcolor=#999999>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
					<%
					if(start_Time != null){
					%>
					<%=ServiceAct.getHtmlDeviceByModel(request)%>
					<TR id="exportId">
						<TD class=column colspan="<%=count%>" height="30"><a
							href="DeviceStatByModel_prt.jsp?isPrt=1&gw_type=<%=gw_type%>&countTime=<%=start_Time%>&endTime=<%=endTime%>&vendorId=<%=vendorId%>&modelId=<%=modelId%>&cityId=<%=cityId%>"
							alt="������ǰҳ���ݵ�Excel��">&nbsp;&nbsp;&nbsp;������Excel</a></TD>
					</TR>
					<%
					}
					%>
				</TABLE>
				</TD>
			</TR>
		</TABLE>
		</TD>
	</TR>
</TABLE>
</form>
<%@ include file="../foot.jsp"%>
<%
	ServiceAct = null;
%>