<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK" isELIgnored="false"  %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<jsp:useBean id="DeviceAct" scope="request"
	class="com.linkage.litms.resource.DeviceAct" />
<jsp:useBean id="DeviceDAO" scope="request"
	class="com.linkage.module.gwms.dao.gw.DeviceDAO" />
<%@ page import="com.linkage.litms.common.database.*"%>
<%@ include file="../head.jsp"%>
<%@ include file="../toolbar.jsp"%>
<%@taglib prefix="ms" uri="/ailk-itms-web-tags"%>
<%@page import="com.linkage.litms.LipossGlobals"%>

<%
	request.setCharacterEncoding("GBK");
	String strCityList = DeviceAct.getCityListSelf(false, "", "", request);
	String strVendorList = DeviceAct.getVendorList(true, "", "");
	String gw_type = request.getParameter("gw_type");
	//�����û����˹�����
	//String strUserDomains = DeviceAct.QueryArea(request);
	String strDevTypeList = com.linkage.litms.common.util.FormUtil.createListBox(
			DeviceDAO.getDevType(), "type_name", "type_name", false, "", "");
	String InstArea=LipossGlobals.getLipossProperty("InstArea.ShortName");
%>
<script type="text/javascript" src="../Js/jquery.js"></script>
<script type="text/javascript" src="../Js/My97DatePicker/WdatePicker.js"></script>
<SCRIPT LANGUAGE="JavaScript" SRC="../Js/prototype.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript">
//����ʱ��
var starttimetemp = "";
var endtimetemp = "";

function showChild(parname){
	if(parname=="vendor_id"){
	    var type=1;		
		var o = $("vendor_id");
		var id = o.options[o.selectedIndex].value;
		var url = "getDeviceChild.jsp";
		var pars = "vendor_id=" + id;
		pars +="&type="+type;
		var myAjax
			= new Ajax.Request(
							url,
							{method:"post",parameters:pars,
							onComplete:function(req){getData(type,req);},
							onFailure:showError}						
						   );
		$("sp_DeviceVersion").innerHTML = "";
	}
    if(parname=="device_model_id")
    {
        var type=2;
		o =$("device_model_id");
		id=o.options[o.selectedIndex].value;
		var url = "getDeviceChild.jsp";
		pars ="device_model_id="+id;
		pars +="&type="+type;
		var myAjax
			= new Ajax.Request(
							url,
							{method:"post",parameters:pars,
							onComplete:function(req){getData(type,req);},
							onFailure:showError}						
						   );
    }
}


function getData(type ,request)
{
 //alert("wp!");
 if(type==1)
 {
    $("sp_DeviceModel").innerHTML = " �豸�ͺţ�" + request.responseText;
 }
 else if(type==2)
 {
    $("sp_DeviceVersion").innerHTML = " �豸�汾��" + request.responseText;
 }
}
function getDeviceModel(request){
	$("sp_DeviceModel").innerHTML = " �豸�ͺţ�" + request.responseText;
}
//Debug
function showError(request){
	alert(request.responseText);
}
function CheckForm(){
	document.getElementById("queryBtn").disabled=true;
	//�豸���к�
	var device_serial = document.frm.device_serial.value;
	//�豸IP
	var loopback_ip = document.frm.loopback_ip.value;
	
	//����
	var address = document.frm.address.value;
	//�豸MAC��ַ
	var mac = document.frm.cpe_mc.value;
	//�豸LOID
	var loid = document.frm.device_id_ex.value;
	
	var queryTypeValue = document.frm.queryType.value;
	//var queryTypeValue = $("input[@name='queryType']");
	//�м���ʡ���Ǳ��������豸���кţ�����Ip���еĲ��ǣ��������һ�£���ʱ���������Ÿ߼���ѯ������
	
	if(null != queryTypeValue && "�� �� �� ѯ" == queryTypeValue)
	{
		//2����������һ��
		if(device_serial.length == 0 && loopback_ip.length == 0)
		{
		alert("��ѡ��������������һ����"); 	
		document.getElementById("queryBtn").disabled=false;
			document.frm.device_serial.focus();
			return false;
		}
	}
	
	if(null != queryTypeValue && "�� �� �� ѯ" == queryTypeValue){
		// ��ʼʱ��
		var starttime = document.getElementsByName("starttime")[0].value;
		
		// �����ĸ�����ֻҪ��һ��������ֵ���Ϳ���ִ�в�ѯ
		if(device_serial.length != 0 || loopback_ip.length != 0 || mac.length != 0 || loid.length != 0){
		}else{ // ����ĸ�������û��ֵ���ٿ���һ���ϱ�ʱ��Ŀ�ʼʱ����û��ֵ�������ֵҲ����ִ�в�ѯ��������ִ�в�ѯ
			if(null != starttime && "" != starttime){
			}else{
				alert("�豸���кš��豸IP��ַ���豸MC��ַ���豸LOID���ϱ���ʼʱ���������һ����");
				document.getElementById("queryBtn").disabled=false;
				document.frm.device_serial.focus();
				return false;
			}
		}
		
		
	}
	//��������豸���кţ��ͱ���Ϸ�
	if(device_serial.length>0 && device_serial.length<6){
		alert("�������������6λ�豸���кŽ��в�ѯ��");
		document.getElementById("queryBtn").disabled=false;
		document.frm.device_serial.focus();
		return false;
	}
	
	return true;
}

/**
 * ת����ѯ������ʽ
 */
function changeQueryArgs()
{
	
	var queryTypeValue = document.frm.queryType.value;
	//alert(queryTypeValue);
	if(null != queryTypeValue && "�� �� �� ѯ" == queryTypeValue)
	{
		//var time = document.getElementsByName("starttime")[0].value;
		//if(null == time || "" == time)
		//{
			//��ȡ����ʱ��
			//document.getElementsByName("starttime")[0].value = starttimetemp;
			document.getElementsByName("starttime")[0].value = "";  // modify by zhangchy 2011-10-24 Ӧ�½�Ҫ��starttimeĬ��ֵ��Ҫ��
			document.getElementsByName("endtime")[0].value = endtimetemp;
			//initDate();
		//}
		//alert("�޸�ǰ" + queryTypeValue);
		document.frm.queryType.value = "�� �� �� ѯ";
		//alert("�޸ĺ�" + document.frm.queryType.value);
		//���ض���Ĳ���
		document.getElementById("args2").style.display = "";
		document.getElementById("args3").style.display = "";
		document.getElementById("args4").style.display = "";
		document.getElementById("args5").style.display = "";
		document.getElementById("args6").style.display = "";
		//�򿪹ؼ�������ע
		//$("tr[@id='red1']").css("display","none");
		//$("tr[@id='red2']").css("display","none");
		return true;
	}
	
	if(null != queryTypeValue && "�� �� �� ѯ" == queryTypeValue)
	{
		
		//alert("�޸�ǰ" + queryTypeValue);
		document.frm.queryType.value = "�� �� �� ѯ";
		//alert("�޸ĺ�" + document.frm.queryType.value);
		//�����в���
		document.getElementById("args2").style.display = "none";
		document.getElementById("args3").style.display = "none";
		document.getElementById("args4").style.display = "none";
		document.getElementById("args5").style.display = "none";
		document.getElementById("args6").style.display = "none";
		//�򿪹ؼ�������ע
		//$("tr[@id='red1']").css("display","");
		//$("tr[@id='red2']").css("display","");
		
		//����ʱ�䣬�������ʱ��
		starttimetemp = document.getElementsByName("starttime")[0].value;
		document.getElementsByName("starttime")[0].value = null;
		endtimetemp = document.getElementsByName("endtime")[0].value;
		document.getElementsByName("endtime")[0].value = null;
		
		//alert("asd");
		return true;
	}
}

function initDate()
{
	//��ʼ��ʱ��  ���� by zhangcong 2011-06-02
	var theday=new Date();
	var day=theday.getDate();
	var month=theday.getMonth()+1;
	var year=theday.getFullYear();
	var hour = theday.getHours();
	var mimute = theday.getMinutes();
	var second = theday.getSeconds();

	//alert("22");
	//modify by zhangcong ��ʼʱ��Ĭ��Ϊ����ĵ�һ��2011-06-02
	//document.getElementsByName("starttime")[0].value=year+"-1-1 00:00:00";
	//$("input[@name='starttime']").val(year+"-1-1 00:00:00");

	//alert("333");
	//modify by zhangcong ����ʱ��Ĭ��Ϊ����
	//document.getElementsByName("endtime")[0].value=year+"-" + month + "-" + day + " " + hour +":"+ mimute +":" + second;
	//$("input[@name='endtime']").val(year+"-" + month + "-" + day + " " + hour +":"+ mimute +":" + second);
	//$("input[@name='endtime']").val($.now("-",true));

	//����ʱ��
	starttimetemp = year+"-1-1 00:00:00";
	endtimetemp = year+"-" + month + "-" + day + " " + hour +":"+ mimute +":" + second;
	//alert("444");
}

function init(){
    var type=<%=gw_type %>;
    
    if(type==3){
            
            document.getElementById("args1").style.display = "block";
    }else{
            document.getElementById("args1").style.display = "none";
    }
}

window.onload=init;

</SCRIPT>

<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD HEIGHT=20>
			&nbsp;
		</TD>
	</TR>
	<TR>
		<TD>
			<FORM NAME="frm" METHOD="get" ACTION="QueryDeviceList.jsp" onsubmit="return CheckForm()" > 
				<TABLE width="95%" border=0 cellspacing=0 cellpadding=0
					align="center">
					<tr>
						<td>
							<table width="100%" height="30" border="0" cellspacing="0"
								cellpadding="0" class="green_gargtd">
								<tr>
									<td width="162" align="center" class="title_bigwhite">
										�豸��Դ
									</td>
									<td>
										<img src="../images/attention_2.gif" width="15" height="12">
										���豸��Ϣ���в�ѯ
									</td>
								</tr>
							</table>
						</td>
					</tr>

					<TR>
						<TD bgcolor=#999999>
							<TABLE border=0 cellspacing=1 cellpadding=2 width="100%"
								id="outTable">
								<TR>
									<TH bgcolor="#ffffff" colspan="4" align="center">
										�豸��ѯ
									</TH>
								</TR>
								<TR>
									<TD class="column" align="right" height="23" width="15%">
										�豸���к�
									</TD>
									<TD bgcolor="#ffffff" align="left" height="23" width="35%">
										<input id="devSn" type="text" value="" name="device_serial" class="bk">
										<font color="red">*</font>
									</TD>
									<TD class="column" align="right" height="23" width="15%">
										�豸IP��ַ
									</TD>
									<TD bgcolor="#ffffff" align="left" height="23" width="35%">
										<input type="text" value="" name="loopback_ip" class="bk">
										<font color="red">*</font>
									</TD>
								</TR>
								
									<TR  id="args1">
									
											<TD class="column" align="right" height="23">
													�ͻ�����
											</TD>
											<TD bgcolor="#ffffff" align="left" height="23" colspan="3">
													<select class="bk"  name="he_type">
														<option value="0">==��ѡ��==</option>
														<option value="1">==��ͥ�û�==</option>
														<option value="2">==��ҵ�û�==</option>
													</select>
											</TD>
											<TD class="column" align="right" height="23"  >
											<input type="button" value="�ж�" onclick="init()" />
											</TD>
									 </TR>
								
								<!-- �˴��ָ� -->
								<TR id="args2" STYLE="display:none">
									<TD class="column" align="right" height="23">
										�豸MC��ַ
									</TD>
									<TD bgcolor="#ffffff" align="left" height="23">
										<input type="text" value="" name="cpe_mc" class="bk">
										<input type="hidden" value="<%=LipossGlobals.getLipossProperty("InstArea.ShortName") %>" name="address" >
										<font color="red">*</font>
									</TD>
									
									<TD class="column" align="right" height="23">
										<ms:inArea areaCode="sx_lt">
											Ψһ��ʶ
										</ms:inArea>
										<ms:inArea areaCode="sx_lt" notInMode="true">
											LOID
										</ms:inArea>
									</TD>
									<TD bgcolor="#ffffff" align="left" height="23">
										<input id="loid" type="text" value="" name="device_id_ex" class="bk">
										<font color="red">*</font>
									</TD>
								</TR>
								<TR id="args3" STYLE="display:none">
									<TD class="column" align="right" height="23">
										���һ���ϱ�ʱ��
									</TD>
									<TD bgcolor="#ffffff" align="left" height="23">
										<input type="text" name="starttime" class='bk' readonly
											value="<s:property value='starttime' />">
										<img name="shortDateimg"
											onClick="WdatePicker({el:document.frm.starttime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
											src="../images/dateButton.png" width="15" height="12"
											border="0" alt="ѡ��">
											<font color="red">*</font>
									</TD>
									<TD class="column" align="right" height="23" width="15%">
										&nbsp;��&nbsp;
									</TD>
									<TD bgcolor="#ffffff" align="left" height="23" width="35%">
										<input type="text" name="endtime" class='bk' readonly
											value="<s:property value='endtime' />">
										<img name="shortDateimg"
											onClick="WdatePicker({el:document.frm.endtime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
											src="../images/dateButton.png" width="15" height="12"
											border="0" alt="ѡ��">
											<font color="red"></font>
									</TD>
								</TR>
								<TR id="args4" STYLE="display:none">
									<!-- <TD class="column" align="right" height="23" >������</TD>
							<TD bgcolor="#ffffff" align="left" height="23" >
							strUserDomains-->
									<TD class="column" align="right" height="23">
										����״̬
									</TD>
									<TD bgcolor="#ffffff" align="left" height="23">
										<select name="status" class="bk">
											<option value="-1">
												==��ѡ��==
											</option>
											<option value="0">
												����
											</option>
											<option value="1">
												����
											</option>
											<option value="2">
												δ֪
											</option>
										</select>
									</TD>
									<% if("gs_dx".equals(InstArea)){%>
									<TD class="column" align="right" height="23">
										�豸�汾����
									</TD>
									<TD bgcolor="#ffffff" align="left" height="23">
									
									<select name="device_version_type" class="bk">
											<option value="-1" >==��ѡ��==</option>
											<option value="1">E8-C</option>
											<option value="2">PON�ں�</option>
											<option value="3">10GPON</option>
											<option value="4">��������</option>
											<option value="5">��������1.0</option>
											<option value="6">��������2.0</option>
											<option value="7">��������3.0</option>
										</select>
										</TD>
									<%}else{ %>
									<TD class="column" align="right" height="23">
										�ն�����
									</TD>
									<TD bgcolor="#ffffff" align="left" height="23">
									<%=strDevTypeList%>
									</TD>
									
									<%} %>
									
								</TR>
								<TR id="args5" STYLE="display:none">
									<TD class="column" align="right" height="23">
										����
									</TD>
									<TD bgcolor="#ffffff" align="left" height="23">
										<%=strCityList%>
									</TD>
									<TD class="column" align="right" height="23">
										�ն�״̬
									</TD>
									<TD bgcolor="#ffffff" align="left" height="23">
										<select class="bk" name="device_status">
											<option value="-1">ȫ��</option>
											<option value="1">��ȷ��</option>
											<option value="0">δȷ��</option>
										</select>
									</TD>
								</TR>
								<TR id="args6" STYLE="display:none">
									<TD class="column" align="right" height="23">
										����
									</TD>
									<TD bgcolor="#ffffff" align="left" height="23" nowrap  colspan="3" >
										<div>
											<span><%=strVendorList%></span> &nbsp;
											<span id="sp_DeviceModel"></span> &nbsp;
											<span id="sp_DeviceVersion"></span>
										</div>
									</TD>
								</TR>
								
								<TR>
									<TD class="column" align="right" height="23" colspan=4>
										<input type="hidden" name="gw_type" value="<%=gw_type%>">
										<input type=submit name="queryBtn" id="queryBtn" class=jianbian value=" �� ѯ ">&nbsp; 
										<ms:inArea areaCode="js_dx" notInMode="true">
											<input type=button name="queryType" class=jianbian value="�� �� �� ѯ" onClick="changeQueryArgs()">
										</ms:inArea>
									</TD>
								</TR>
							</TABLE>
						</TD>
					</TR>
				</TABLE>
			</FORM>
		</TD>
	</TR>
	<TR>
		<TD HEIGHT=20>
			<IFRAME name=childFrm SRC="" STYLE="display: none"></IFRAME>
			&nbsp;
		</TD>
	</TR>
</TABLE>
<script type="text/javascript">

	//alert("111");
	document.getElementsByName("starttime")[0].value = "";
	document.getElementsByName("endtime")[0].value = "";
	initDate();
</script>
<%@ include file="../foot.jsp"%>
