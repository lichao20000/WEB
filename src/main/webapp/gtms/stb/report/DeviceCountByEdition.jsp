<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<jsp:useBean id="DeviceCountByEditionACT" scope="request"
	class="com.linkage.module.gtms.stb.report.act.DeviceCountByEditionACT" />

<script type="text/javascript"
	src="<s:url value="/Js/jsDate/WdatePicker.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet"
	type="text/css">
<link href="<s:url value="/css2/global.css"/>" rel="stylesheet"
	type="text/css">
<%
	String cityId = request.getParameter("cityId");
	String vendorId = request.getParameter("vendorId");
	if(vendorId==null||"".equals(vendorId)){
		vendorId ="-1";
	}
	String modelId = request.getParameter("modelId");
	if(modelId==null||"".equals(modelId)){
		modelId ="-1";
	}
	String deviceTypeId = request.getParameter("deviceTypeId");
	if(deviceTypeId==null||"".equals(deviceTypeId)){
		deviceTypeId ="-1";
	}
	String queryFlag = request.getParameter("queryFlag");
%>
<SCRIPT LANGUAGE="JavaScript">
var isQuery = 0 ;
function detail(city_id,vendor_id,deviceModel_id,devicetype_id, gw_type,noChildCity){
	var page="<s:url value='/gtms/stb/resource/gwDeviceQueryStb!queryDeviceList.action'/>?cityId="+city_id+"&devicetypeId="+devicetype_id + "&queryType=2&noChildCity="+noChildCity
	+"&vendorId="+vendor_id
	+"&deviceModelId="+deviceModel_id;
	//alert(page);
	window.open(page,"","left=20,top=20,width=800,height=600,resizable=no,scrollbars=yes");
}


$(function(){
	deviceSelect_change_select("city","<%=cityId%>");
	var vendorId = "<%=vendorId%>";
	deviceSelect_change_select("vendor",vendorId);
	if(vendorId!="-1"){
		var modelId = "<%=modelId%>";
		setTimeout('deviceSelect_change_select("deviceModel","<%=modelId%>")', 1000);
		//deviceSelect_change_select("deviceModel",modelId);
		if(modelId!="-1"){
			setTimeout('deviceSelect_change_select("devicetype","<%=deviceTypeId%>")', 2000);
		}
	}
	
});

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
function deviceSelect_change_select(type,selectvalue){
	switch (type){
		case "city":
			var url = "<s:url value='/gtms/stb/resource/gwDeviceQueryStb!getCityNextChild.action'/>";
			$.post(url,{
			},function(ajax){
				deviceSelect_parseMessage(ajax,$("select[@name='cityId']"),selectvalue);
			});
			break;
		case "vendor":
			var url = "<s:url value='/gtms/stb/resource/gwDeviceQueryStb!getVendor.action'/>";
			$.post(url,{
			},function(ajax){
				deviceSelect_parseMessage(ajax,$("select[@name='vendorId']"),selectvalue);
				$("select[@name='deviceModelId']").html("<option value='-1'>==����ѡ���豸����==</option>");
				$("select[@name='deviceTypeId']").html("<option value='-1'>==����ѡ���豸�ͺ�==</option>");
			});
			break;
		case "deviceModel":
			var url = "<s:url value='/gtms/stb/resource/gwDeviceQueryStb!getDeviceModel.action'/>";
			var vendorId = $("select[@name='vendorId']").val();
			$.post(url,{
				vendorId:vendorId
			},function(ajax){
				deviceSelect_parseMessage(ajax,$("select[@name='modelId']"),selectvalue);
				$("select[@name='deviceTypeId']").html("<option value='-1'>==����ѡ���豸�ͺ�==</option>");
			});
			break;
		case "devicetype":
			var url = "<s:url value='/gtms/stb/resource/gwDeviceQueryStb!getDevicetype.action'/>";
			var vendorId = $("select[@name='vendorId']").val();
			var deviceModelId = $("select[@name='modelId']").val();
			$.post(url,{
				vendorId:vendorId,
				deviceModelId:deviceModelId
			},function(ajax){
				deviceSelect_parseMessage(ajax,$("select[@name='deviceTypeId']"),selectvalue);
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
function deviceSelect_parseMessage(ajax,field,selectvalue){
	var flag = true;
	if(""==ajax){
		return;
	}
	var lineData = ajax.split("#");
	if(!typeof(lineData) || !typeof(lineData.length)){
		return false;
	}
	field.html("");	
	if(selectvalue=="-1"){
		field.append("<option value='-1' selected>==��ѡ��==</option>");
	}else{
		field.append("<option value='-1'>==��ѡ��==</option>");
	}
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

//-->
</SCRIPT>
<TABLE width="98%" align="center" class="querytable">
	<TR>
		<TD>
			<IMG height=20 src="<s:url value='/images/bite_2.gif'/>" width=24>
			����ǰ��λ�ã����汾ͳ��
		</TD>
	</TR>
</TABLE>
<form name="form1" action="DeviceCountByEdition.jsp" method="post">
	<TABLE width="98%" class="querytable" align="center">
		<TR>
			<TH class="title_1">
				���汾ͳ��
			</TH>
		</TR>

		<TR>
			<td>
				����
				<select name="vendorId" class="bk"
					onchange="deviceSelect_change_select('deviceModel','-1')"
					style="width: 200px">
					<option value="-1">
						==��ѡ��==
					</option>
				</select>
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; �ͺ�
				<select name="modelId" class="bk"
					onchange="deviceSelect_change_select('devicetype','-1')"
					style="width: 200px">
					<option value="-1">
						==����ѡ����==
					</option>
				</select>
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; �汾
				<select name="deviceTypeId" class="bk" " style="width: 200px">
					<option value="-1">
						==����ѡ���豸�ͺ�==
					</option>
				</select>
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; ����
				<select name="cityId" class="bk" style="width: 100px">
					<option value="-1">
						==��ѡ��==
					</option>
				</select>
				<input type="hidden" name="queryFlag" value="query" />
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<input type="submit" name="btn0" value=" �� �� " class="jianbian"
					onclick="javascript:checkform()">
			</td>
		</TR>
		<tr>
			<td height="20">
			</td>
		</tr>
		<TR>
			<TD>
				<TABLE class='listtable' width='100%'>
							<%
								if ("query".equals(queryFlag))
	{
							%>
						  	 <%=DeviceCountByEditionACT.getHtmlDeviceByEdition(request)%>
						    <%}else{}%>
				</TABLE>
			</TD>
		</TR>
	</TABLE>
</form>
