<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.linkage.litms.resource.DeviceAct" %>
<%@ include file="../head.jsp"%>
<%--
	zhaixf(3412) 2008-04-11
	req:GDDX_ITMS-BUG-LIUW-200804024-002
--%>
<%
	//������Ϣ
	
	DeviceAct deviceAct = new DeviceAct();
	String 	strCityList = deviceAct.getCityListSelf(true, "", "", request);

 %>
<script type="text/javascript" src="../Js/jquery.js" /></script>
<SCRIPT LANGUAGE="JavaScript">
<!--

function showChild(param){

	if(param == "searchType"){
		if(document.frm.searchType.value == "0"){
			document.all("idsearch").innerHTML = "<input type='text' name='user_name' value=''  size='13' class='bk' ONKEYDOWN='doAction(this);'>";
		}
		else if(document.frm.searchType.value == "1"){
			document.all("idsearch").innerHTML = "<%=strCityList%>";
		}
		else if(document.frm.searchType.value == "2"){
			document.all("idsearch").innerHTML = "<input type='text' name='loopback_ip' value='' size='13' class='bk' ONKEYDOWN='doAction(this);'>";
		} else {
			document.all("idsearch").innerHTML = "";
		}
		
	}
	
	if(param == "city_id"){
		var page = "ListAll_user.jsp?city_id=" + document.frm.city_id.value + "&searchType="+ document.frm.searchType.value;
		document.all("childFrm").src= page;
	}
	
}

function doAction(){
	var initcode = event.keyCode;
	if(initcode == 13){
		var param = "";
		window.event.returnValue = false;
		
		if(document.frm.searchType.value == "0"){
			if(Trim(document.frm.user_name.value) == ""){
				alert("�������û��ʺţ�");
				document.frm.user_name.focus();
				return false;
			}
			param = "&user_name=" + document.frm.user_name.value;
		}
		else if(document.frm.searchType.value == "2"){
		
			if(Trim(document.frm.loopback_ip.value) == ""){
				alert("�������豸������");
				document.frm.loopback_ip.focus();
				return false;
			}
			param = "&loopback_ip=" + document.frm.loopback_ip.value;		
		} 
		
		var page = "ListAll_user.jsp?searchType=" + document.frm.searchType.value + param;
		document.all("childFrm").src = page;	
	}
}

function CheckForm(){
	var obj = document.frm;
	var username = obj.username.value;
	if(username == null || ""==username || $.trim(username).length==0){
		alert("����ѡ���û��˺�");
		obj.username.focus();
		return false;
	}
	return true;
}

//-->
</SCRIPT>
<link rel="stylesheet" href="../css/listview.css" type="text/css">
<FORM name="frm" action="User_serviceResult.jsp"  method="post" onSubmit="return CheckForm();" target="childFrm">
	<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
		<TR>
			<TD HEIGHT="20">
				&nbsp;
			</TD>
		</TR>
		<TR>
			<TD>
				<TABLE width="95%" border=0 cellspacing=0 cellpadding=0
					align="center">
					<TR>
						<TD>
							<table width="100%" height="30" border="0" cellspacing="0"
								cellpadding="0" class="green_gargtd">
								<tr>
									<td width="162">
										<div align="center" class="title_bigwhite">
											��������
										</div>
									</td>
									<td>
										<img src="../images/attention_2.gif" width="15" height="12">										
										ע������
										<font color=red>*</font>����,�����뷽ʽ��ѯ��ʱ�򣬻س��ύ��ѯ������
									</td>

								</tr>
							</table>
						</TD>
					</TR>
					<TR>
						<TD bgcolor=#999999>
							<TABLE border=0 cellspacing=1 cellpadding=2 width="100%" >						
								<TR bgcolor="#ffffff">
									<TH nowrap colspan="4">
										�û�ҵ��״̬��ѯ
									</TH>
								</TR>
								<TR bgcolor="#ffffff">
									<!--
									<TD align="right" width="15%" nowarp>
										��ѯ�û���ʽ ��
									</TD>
									<TD nowrap width="35%" nowarp>
										<select name="searchType" class="bk" onchange="showChild('searchType');">
											<option value="-1">==��ѡ��==</option>
											<option value="0">--���û�ģ����ѯ--</option>
											<option value="1">--�����ع���--</option>
											<option value="2">--���豸������ѯ--</option>
										</select>
										<span id="idsearch">
										</span>
									</TD>
									-->
									<TD align="right" width="35%" nowarp>
										�û��ʺ� ��
									</TD>
									<TD nowrap width="65%">
										<span id="iduser">
										<input type='text' name='username' value=''  size='13' class='bk' />
											<!-- 
											<select name="username" class="bk">
												<option value="-1">==�밴������ѯ==</option>
											</select> 
											-->
										</span>										
									</TD>									

								</TR>	
																						
								<TR bgcolor="#ffffff">
									<TD nowrap colspan="4" align="right" class="green_foot">
										<input type="submit" name="button" value=" �� ѯ ">
									</TD>
								</TR>							
							</TABLE>
						</TD>
					</TR>
				</TABLE>
			</TD>
		</TR>
			<TR><TD height="20">
			
 			</TD></TR>
			<TR><TD>
			<div id="id_div"></div>
  			</TD></TR>

		<TR>
			<TD HEIGHT=20>
				&nbsp;
				<IFRAME ID=childFrm name="childFrm" STYLE="display:none"></IFRAME>
			</TD>
		</TR>
	</TABLE>
</form>
<%@ include file="../foot.jsp"%>
