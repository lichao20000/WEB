<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.linkage.litms.common.database.*"%>
<%
	String device_id = (String) request.getParameter("device_id");
	String gather_id = "";
	String sql = "select gather_id from tab_gw_device where device_id='"
			+ device_id + "'";
	Map fields = DataSetBean.getRecord(sql);
	if (fields != null) {
		gather_id = (String) fields.get("gather_id");
	}
%>
<%@ include file="../head.jsp"%>
<link href="../css/css_green.css" rel="stylesheet" type="text/css">
<SCRIPT LANGUAGE="JavaScript">

     function ExecMod(){
     	if(CheckForm()){
        	var deviceID = document.frm.device_id.value;
   		    var gatherID = document.frm.gather_id.value;
        	page = "jt_device_webtopo_from2_save.jsp?device_id="+deviceID+ "&gather_id="+gatherID+"&NumberOfRepetions=" +document.frm.NumberOfRepetions.value+ "&Timeout=" +document.frm.Timeout.value;
			page+="&TT="+new Date();
			document.all("div_ping").innerHTML = "����������Ͻ���������ĵȴ�....";
			document.all("childFrm").src = page;
		}else{
			return false;
		}	
	}
	function CheckForm(){
		var rule = /^[0-9]*[1-9][0-9]*$/;//������ʽ��/��/֮��    	
		var obj = document.frm;
		if(obj.ATMF5.value=="-1"){
			alert("��ѡ��WAN����!");
			obj.ATMF5.focus();
			return false;
		}
		if(!IsNull(obj.Timeout.value,'��ʱʱ��')){
			obj.Timeout.focus();
			return false;
		}
		else if(obj.Timeout.value!="" && !IsNumber(obj.Timeout.value,"��ʱʱ��")){	
			obj.Timeout.focus();
			obj.Timeout.select();
			return false;
	    }			
	    else if(!IsNull(obj.NumberOfRepetions.value,'����Ŀ')){
			obj.NumberOfRepetitions.focus();
			return false;
		}	
	    else if(!rule.test(obj.NumberOfRepetions.value)){
        	alert("����Ŀ���������0������");
        	return false;
        }
		else{
			return true;
	    }			
	}
	function getInterfaceAct(){
		var _device_id = "<%=device_id%>";
		var _gather_id = "<%=gather_id%>";
		getPingInterface(_device_id,_gather_id);
	}
	
	
	function getPingInterface(_device_id,_gather_id){
		//document.getElementById("Interface").onfocus = null;
		var page = "../diagnostic/getATMF5LOOPInterface.jsp";
		page += "?device_id="+_device_id;
		page += "&gather_id="+_gather_id;
		page += "&TT="+new Date();
		document.all("childFrm").src = page;
	}
	function setPingInterface(html){
		if(html == ""){
			alert("��ȡʧ��,�豸��æ���߷������Ӵ���!");
		}else{
			document.all("divPingInterface").innerHTML = html;
		}
	}
</SCRIPT>
<%@ include file="../toolbar.jsp"%>
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD HEIGHT=20>
			&nbsp;
		</TD>
	</TR>
	<TR>
		<TD>
			<FORM NAME="frm" METHOD="POST" ACTION=""
				onSubmit="return CheckForm();">
				<table width="95%" border="0" align="center" cellpadding="0"
					cellspacing="0">
					<tr>
						<td>
							<table width="100%" height="30" border="0" cellspacing="0"
								cellpadding="0" class="green_gargtd">
								<tr>
									<td width="162">
										<div align="center" class="title_bigwhite">
											�豸���
										</div>
									</td>
									<td>
										<img src="../images/attention_2.gif" width="15" height="12">
										ע����
										<font color="#FF0000">*</font>��Ϊ��ѡ�
									</td>

								</tr>
							</table>
						</td>
					</tr>
					<tr>
						<td bgcolor="#FFFFFF">
							<table width="100%" border=0 align="center" cellpadding="1"
								cellspacing="1" bgcolor="#999999" class="text">
								<TR bgcolor="#FFFFFF">
									<TH colspan="4">
										ATMF5LOOP����
									</TH>
								</TR>
								<tr bgcolor="#FFFFFF">
									<td nowrap class=column>
										<div align="center">
											WAN����
										</div>
									</td>
									<td class=column nowrap colspan="3">
										<span id="divATMF5Interface"> <select name="ATMF5"
												class="bk">
												<option value="-1">
													==���ȡWAN����==
												</option>
											</select> </span> &nbsp;&nbsp;
										<input type="button" value=" �� ȡ " onclick="getInterfaceAct()">
									</td>
								</tr>
								<tr bgcolor="#FFFFFF">
									<td nowrap class=column>
										<div align="center">
											��ʱʱ��(MS)
										</div>
									</td>
									<td class=column>
										<input type="text" name="Timeout" class="bk" size="16" value="2000">
									</td>
									<td nowrap class=column>
										<div align="center">
											����Ŀ
										</div>
									</td>
									<td class=column>
										<input type="text" name="NumberOfRepetions" class="bk"
											size="16" value="2">
									</td>
								</tr>
								<tr bgcolor="#FFFFFF">
									<td colspan="4" class="blue_foot">
										<div align="right">
											<table width="100%" border="0" cellspacing="0"
												cellpadding="0">
												<tr>
													<TH>
														<div align="right">
															<INPUT TYPE="button" value="�� ��" class=btn
																onclick="ExecMod()">
															&nbsp;&nbsp;
															<INPUT TYPE="hidden" name="action" value="add">
															&nbsp;&nbsp;
															<INPUT TYPE="hidden" name="device_id"
																value="<%=device_id%>">
															&nbsp;&nbsp;
															<INPUT TYPE="hidden" name="gather_id"
																value="<%=gather_id%>">
															&nbsp;&nbsp;
														</div>
													</TH>
												</tr>
											</table>
										</div>
									</td>
								</tr>
								<TR bgcolor="#FFFFFF">
									<TD align="center" nowrap class=column>
										��Ͻ��
									</TD>
									<td colspan="3" valign="top" class=column>
										<div id="div_ping"
											style="width:100%; height:120px; z-index:1; top: 100px; overflow:scroll"></div>
									</td>
								</TR>
							</table>
						</td>
					</tr>
				</table>
			</FORM>
		</TD>
	</TR>
	<TR>
		<TD HEIGHT=20>
			<IFRAME ID=childFrm SRC="" STYLE="display:none"></IFRAME>
			<IFRAME ID=childFrm1 SRC="" STYLE="display:none"></IFRAME>
			&nbsp;
		</TD>
	</TR>
</TABLE>
<%@ include file="../foot.jsp"%>
