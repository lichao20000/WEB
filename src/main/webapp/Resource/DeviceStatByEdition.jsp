<%@ include file="../timelater.jsp"%>
<%@ page
	import="com.linkage.litms.common.database.*,java.util.List,java.util.ArrayList"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<meta http-equiv="x-ua-compatible" content="IE=7" >
<%@taglib prefix="ms" uri="/ailk-itms-web-tags"%>
<jsp:useBean id="ServiceAct" scope="request"
	class="com.linkage.litms.resource.ServiceAct" />
<%@ page contentType="text/html;charset=GBK"%>

<%
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
	
	String recentStartTime = request.getParameter("recentStartTime");
	if (null == recentStartTime || "null".equals(recentStartTime)) {
		recentStartTime = "";
	}
	String recentEndTime = request.getParameter("recentEndTime");
	if (null == recentEndTime || "null".equals(recentEndTime)) {
		recentEndTime = "";
	}
	String recent_start_Time = request.getParameter("recent_start_Time");
	if (null == recent_start_Time || "null".equals(recent_start_Time)) {
		recent_start_Time = "";
	}
	String recent_end_Time = request.getParameter("recent_end_Time");
	if (null == recent_end_Time || "null".equals(recent_end_Time)) {
		recent_end_Time = "";
	}
	
	// rela_dev_type_id
	String vendorId = request.getParameter("vendorId"); 
	String modelId = request.getParameter("modelId");
	String deviceTypeId = request.getParameter("deviceTypeId");
	String gw_type = request.getParameter("gw_type");
	String sqlid = request.getParameter("sqlid");
		//os_jiank
	String cityList1=ServiceAct.getCityId(cursor);
	String strVendorId = ServiceAct.getVendorHtml(vendorId,gw_type);
	String strModelId = ServiceAct.getModelHtml(vendorId, modelId,gw_type);
	String strDeviceTypeId = ServiceAct.getDeviceTypeHtml(modelId,
			deviceTypeId,gw_type);
	String is_normal=request.getParameter("is_normal");
	String isBind = request.getParameter("isBind");
	/* JLDX-REQ-20180606-JIANGHAO6-002 ����������� */
	String use_state = request.getParameter("use_state");
	
	String rela_dev_type_id = request.getParameter("rela_dev_type_id");
	int count = cursor.getRecordSize() + 4;
%>

<script type="text/javascript" src="../Js/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="<s:url value='/Js/jquery.js'/>"></script>
<SCRIPT LANGUAGE="JavaScript">
<%-- $(function(){
	var rela_dev_type_id=<%=rela_dev_type_id%>;
	var is_normal=<%=is_normal%>; 
	var mySelect = document.getElementById("is_normal");
	var mySelect =$("#is_normal");
	if(!!mySelect){
		for (var i = 0; i < mySelect.length; i++) {
			if (mySelect.options[i].value == is_normal){
				mySelect.options[i].selected=true;   
			}
		}
	}
	$("select[@name='rela_dev_type_id']").val(rela_dev_type_id);

}); --%>

function checkform(){
	var instArea = $('#instArea').val();
	var start_Time = document.form1.start_Time.value;
	var end_Time = document.form1.end_Time.value;
	var use_state = document.form1.use_state.value;
	if(""!=start_Time && null!=start_Time){
		document.form1.startTime.value = strTime2Second(start_Time+" 0:0:0");
	}
	if(""!=end_Time && null!=end_Time){
		document.form1.endTime.value = strTime2Second(end_Time+" 23:59:59");
	}
	if("jx_dx"==instArea)
	{
		var recent_start_Time = document.form1.recent_start_Time.value;
		var recent_end_Time = document.form1.recent_end_Time.value;
		if(""!=recent_start_Time && null!=recent_start_Time){
			document.form1.recentStartTime.value = strTime2Second(recent_start_Time+" 0:0:0");
		}else{
			document.form1.recentStartTime.value="";
		}
		if(""!=recent_end_Time && null!=recent_end_Time){
			document.form1.recentEndTime.value = strTime2Second(recent_end_Time+" 23:59:59");
		}else{
			document.form1.recentEndTime.value="";
		}
	}
	var deviceTypeId = $("select[@name='deviceTypeId']").val();
	$("#trData").show();
	$("#QueryData").html("<font style='display:block; text-align:center;font-size:20px' >���ڲ�ѯ�����Ե�....</font>");
    $("#queryButGrey").attr("disabled", true);
    var form = document.getElementById("form1");
   // form.attr("action","DeviceStatByEditionList.jsp?devicetypeid='"+deviceTypeId+"'");
    document.form1.action="DeviceStatByEditionList.jsp?devicetypeid='"+deviceTypeId+"'";
    //  var url="DeviceStatByEditionList.jsp?devicetypeid='"+ +"'";
   // var form = document.getElementById("form1");
  	document.form1.submit();
}

function change_select(str){
	var gw_type= document.form1.gw_type.value;
	if("deviceModel"==str){
		var url = "<s:url value='/gwms/share/gwDeviceQuery!getDeviceModel.action'/>";
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
			parseMessage(ajax,$("select[@name='modelId']"));
			$("select[@name='deviceTypeId']").html("<option value='-1'>==����ѡ���豸�ͺ�==</option>");
		});
	}
	if("deviceType"==str){
		var url = "<s:url value='/gwms/share/gwDeviceQuery!getDevicetype.action'/>";
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
	if("jx_dx"==instArea)
	{
	$('#isEsurfing').show();
	}
	if("jl_dx"==instArea && gw_type==4)
	{
	//$("input[@id='chbx_isMactchSQL']").css("display","");
	$('#city').css("display","");
	$("td[@id='rela_dev_type']").css("display","none");
	
	$("td[@id='start']").css("display","");
	$("td[@id='showend']").css("display","");
	$("td[@id='show']").css("display","none");
	$("td[@id='show1']").css("display","none");
	$("td[@id='show2']").css("display","");
	/* $("td[@id='normal']").css("display","none);
	$("td[@id='start']").css("display","none);
	$("td[@id='end']").css("display","none); */
	//S('#rela_dev_type').css("display","none");
	}
	if("jl_dx"==instArea && gw_type==1)
	{
	//$("input[@id='chbx_isMactchSQL']").css("display","");
	$('#city').css("display","none");
	$("td[@id='rela_dev_type']").css("display","");
	
	$("td[@id='start']").css("display","");
	$("td[@id='showend']").css("display","");
	$("td[@id='show']").css("display","");
	$("td[@id='show1']").css("display","");
	$("td[@id='show2']").css("display","");
	
	/* $("td[@id='normal']").css("display","none);
	$("td[@id='start']").css("display","none);
	$("td[@id='end']").css("display","none); */
	//S('#rela_dev_type').css("display","none");
	}
});

$(window).resize(function(){
	dyniframesize();
}); 
function useQuery(){
	$("#queryButGrey").attr("disabled", false);
}
function IFrameReSizeWidth(iframename) {
   var pTar = document.getElementById(iframename);
   if (pTar) {  //ff
       if (pTar.contentDocument && pTar.contentDocument.body.offsetWidth) {
	         pTar.width = pTar.contentDocument.body.offsetWidth;
	       }  //ie
       else if (pTar.Document && pTar.Document.body.scrollWidth) {
	           pTar.width = pTar.Document.body.scrollWidth;
         }
     }
   }
</SCRIPT>
<%@ include file="../head.jsp"%>
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<link href="<s:url value="../css/css_green.css"/>" rel="stylesheet"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>" type="text/css">
	
<form name="form1" action="" id="form1" method="post" target="dataForm" >
<input type="hidden" name="gw_type"  id="gw_type" value='<%=gw_type%>' /> 
<input type="hidden" name="isBind" value='<%=isBind%>' />
<input type="hidden" name="sqlid" id="sqlid" value='<%=sqlid%>' />
<TABLE border=0 cellspacing=0 cellpadding=0 style="width: 100%">
	<TR>
		<TD HEIGHT=20>&nbsp;</TD>
	</TR>
	<TR>
		<TD>
		<TABLE width="100%" border=0 cellspacing=0 cellpadding=0 align="center">
			<TR>
				<TD>
				<TABLE width="100%" height="30" border="0" cellspacing="0"
					cellpadding="0" class="green_gargtd">
					<TR>
						<TD width="164" align="center" class="title_bigwhite">���汾ͳ��</TD>
						<td></td>
					</TR>
				</TABLE>
				</TD>
			</TR>
			<TR>
				<TD>
				<TABLE  class="querytable">
					<tr>
						<th colspan=4>
							<%if(gw_type.equals("1")){ %>
								��ͥ�����豸���汾ͳ��
							<%}if(gw_type.equals("4")){ %>
								�������豸���汾ͳ��
							<%}%>
						</th>
					</tr>
					<tr>
						<td class=column align="center" width="15%">
							����
						</td>
						<td width="35%">
							<select id="vendorId" name="vendorId" class="bk" onchange="change_select('deviceModel')" style="width: 225px">
								<%=strVendorId%>
							</select>
						</td>
						<td class=column align="center" width="15%">
							�ͺ�
						</td>
						<td width="35%">
							<select id="modelId" name="modelId" class="bk" onchange="change_select('deviceType')" style="width: 225px">
								<%=strModelId%>
							</select>
						</td>
					</tr>
					<tr>
						<td class=column align="center" width="15%">
							�汾
						</td>
						<td width="35%">
							<select name="deviceTypeId" id="deviceTypeId" class="bk" style="width: 225px">
								<%=strDeviceTypeId%>
							</select>
						</td>
						<td class=column align="center" width="15%" name="rela_dev_type" id="rela_dev_type">
							�豸����
						</td>
						<td width="35%">
							<select name="rela_dev_type_id" class="bk" style="width: 225px">
								<option value="-1">==��ѡ��==</option>
								<option value="1">e8-b</option>
								<option value="2">e8-c</option>
								<ms:inArea areaCode="jl_dx" notInMode="false">
									<option value="7">��me</option> 
							    </ms:inArea>
							</select>
						</td>
					</tr>
					<tr>
						<td class=column align="center" width="15%" id="start">
							��ʼʱ��
						</td>
						<td width="35%">
							<input type="text" name="start_Time" value='<%=start_Time%>' size="12" readonly class=bk  style="width: 225px">
							<input type="hidden" name="startTime" value='<%=startTime%>' readonly class=bk>
							<img name="shortDateimg" onClick="WdatePicker({el:document.form1.start_Time,dateFmt:'yyyy-MM-dd',skin:'whyGreen'})"
								src="../images/dateButton.png" width="15" height="12" border="0" alt="ѡ��" /> (YYYY-MM-DD) 
						</td>
						<td class=column align="center" width="15%" id="showend">
							����ʱ��
						</td>
						<td width="35%">
							<input type="text" name="end_Time" value='<%=end_Time%>' size="12" readonly class=bk style="width: 225px"> 
							<input type="hidden" name="endTime" value='<%=endTime%>' readonly class=bk> 
							<img name="shortDateimg" onClick="WdatePicker({el:document.form1.end_Time,dateFmt:'yyyy-MM-dd',skin:'whyGreen'})"
								src="../images/dateButton.png" width="15" height="12" border="0" alt="ѡ��" /> (YYYY-MM-DD)
						</td>
					</tr>
					<tr style="display: none">
						<td class=column align="center" name="city" id="city">
							����
						</td>
						<td width="35%">
							<select name="city_id" id="city_id" class="bk"  style="width: 225px">
								<%=cityList1%>
							</select> 
						</td>
					</tr>
					<tr>
						<td class=column align="center" width="15%" style="display:none;" id="show2">
							�������
						</td>
						<td width="35%" style="display:none;" id="show2">
							<select name="use_state" id="use_state" class="bk"  style="width: 225px" >
								<option value="-1">==��ѡ��==</option>
								<option value="0">δ��</option>
								<option value="1">����</option>
								<option value="2">ȫ��</option>
							</select>
						</td>
						<td class=column bgcolor="#FFFFFF" colspan="3" style="display:none;" id="show1">
						</td>
					</tr>
					
					<tr>
						<ms:inArea areaCode="cq_dx,sx_lt" notInMode="true">
						<td class=column align="center" width="15%" id="show">
							�Ƿ�����
						</td>
						<td width="35%">
							<select name="is_normal" id="is_normal" class="bk" style="width: 225px">
								<option value="-1">==��ѡ��==</option>
								<option value="1">��</option>
								<option value="0">��</option>
							</select>
						</td>
						</ms:inArea>
						<td class=column align="center" width="15%" style="display:none;" id="isEsurfing">
							�Ƿ���������
						</td>
						<td width="35%" style="display:none;" id="isEsurfing">
							<select name="is_esurfing" id="is_esurfing" class="bk" style="width: 225px">
								<option value="">==��ѡ��==</option>
								<option value="1">��������</option>
							</select>
						</td>
					</tr>
					<tr bgcolor=#ffffff>
						<td colspan="4" class=column bgcolor="#FFFFFF" ><input type="submit" name="btn0" id="queryButGrey" 
							value=" �� ѯ " class="jianbian" onclick="javascript:checkform()"></td>
					</tr>
					
					<ms:inArea areaCode="jx_dx" notInMode="false">
						<tr>
							<td class=column align="center" width="15%">
								�Ƿ�֧��IPV6
							</td>
							<td width="35%">
								<select name="ip_type" id="ip_type" class="bk" style="width: 225px">
									<option value="">==��ѡ��==</option>
									<option value="1">��</option>
									<option value="0">��</option>
								</select>
							</td>
							<td class=column align="center" width="15%">
								�Ƿ�֧��ǧ��
							</td>
							<td width="35%">
								<select name="gbbroadband" id="gbbroadband" class="bk" style="width: 225px">
									<option value="">==��ѡ��==</option>
									<option value="1">��</option>
									<option value="2">��</option>
								</select>
							</td>
						</tr>
						<tr>
							<td class=column align="center" width="15%">
								�Ƿ�֧�ֲ���
							</td>
							<td width="35%">
								<select name="is_speedTest" id="is_speedTest" class="bk"  style="width: 225px" >
									<option value="">==��ѡ��==</option>
									<option value="1">��</option>
									<option value="0">��</option>
								</select>
							</td>
							<td class=column align="center" width="15%">
								�Ƿ���Ч�û�
							</td>
							<td width="35%">
								<select name="is_ableUser" id="is_ableUser" class="bk"  style="width: 225px" >
									<option value="">==��ѡ��==</option>
									<option value="1">��</option>
									<option value="0">��</option>
								</select>
							</td>
						</tr>
						<tr>
							<td class=column align="center" width="15%">
								����ϱ�ʱ��
							</td>
							<td colspan="3">
								<input type="text" name="recent_start_Time" value='<%=recent_start_Time%>' readonly  class=bk>
								<input type="hidden" name="recentStartTime" value='<%=recentStartTime%>' readonly class=bk>
								<img name="shortDateimg" onClick="WdatePicker({el:document.form1.recent_start_Time,dateFmt:'yyyy-MM-dd',skin:'whyGreen'})"
									src="../images/dateButton.png" width="13" height="12" border="0" alt="ѡ��">&nbsp;~&nbsp; 
								<input type="text" name="recent_end_Time" value='<%=recent_end_Time%>' readonly class=bk>
								<input type="hidden" name="recentEndTime" value='<%=recentEndTime%>' readonly class=bk>
								<img name="shortDateimg" onClick="WdatePicker({el:document.form1.recent_end_Time,dateFmt:'yyyy-MM-dd',skin:'whyGreen'})"
									src="../images/dateButton.png" width="13" height="12" border="0" alt="ѡ��">
							</td>
							
						</tr>
					</ms:inArea>
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
<ms:inArea areaCode="sx_lt" notInMode="true">
<%@ include file="../foot.jsp"%>
</ms:inArea>
<%
	ServiceAct = null;
%>