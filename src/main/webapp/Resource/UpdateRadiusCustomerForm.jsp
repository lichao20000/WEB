<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.linkage.litms.resource.*,com.linkage.litms.common.database.*,java.util.*,com.linkage.litms.common.util.StringUtils" %>
<jsp:useBean id="DeviceAct" scope="request" class="com.linkage.litms.resource.DeviceAct"/>
<jsp:useBean id="AdslAct" scope="request" class="com.linkage.litms.resource.AdslAct"/>

<%@ page import="com.linkage.module.gwms.dao.tabquery.CityDAO" %>

<%
request.setCharacterEncoding("GBK");
String str_user_id = request.getParameter("user_id");
String username = request.getParameter("username");
String strSQL  = "select * from cus_radiuscustomer where user_id="+ str_user_id+" and username = '" + username + "'";
Map userMap = DataSetBean.getRecord(strSQL);
String city_id = (String)userMap.get("city_id");
String office_id = (String)userMap.get("office_id");
String zone_id = (String)userMap.get("zone_id");
String city_id1 = user.getCityId();
List cityIdList = CityDAO.getAllNextCityIdsByCityPid(city_id1);
String m_CityIdQuery = StringUtils.weave(cityIdList);
String strCityList,strOfficeList,strZoneList;
Cursor cursor;
strCityList = DeviceAct.getCityListSelf(false,city_id,"",request);
strOfficeList = DeviceAct.getOfficeList(false,office_id,m_CityIdQuery,"");
strZoneList = DeviceAct.getZoneList(false,zone_id,"");
%>
<%@ include file="../head.jsp"%>
<SCRIPT LANGUAGE="JavaScript">

function selDev()
{
	var retValue = window.showModalDialog("./selDeviceList_snmp.jsp?device_serialnumber=" + document.frm.adsl_ser.value,window + "&tt=" + new Date().getTime(),"scroll=yes;dialogHeight:400px;dialogWidth:800px");
	if (typeof(retValue) != 'undefined'){		
		var temp = retValue.split('/');
		document.frm.device_id.value = temp[0];
		document.frm.adsl_ser.value = temp[1];		
	}
}


	function EC(leaf,obj){
		pobj = obj.offsetParent;
		oTRs = pobj.getElementsByTagName("TR");
		var m_bShow; 
		for(var i=0; i<oTRs.length; i++){
			if(oTRs[i].leaf == leaf){
				m_bShow = (oTRs[i].style.display=="none");
				oTRs[i].style.display = m_bShow?"":"none";
			}
		}
		sobj = obj.getElementsByTagName("IMG");
		if(m_bShow) {
			sobj[0].src = "images/up_enabled.gif";
			obj.className="yellow_title";
		}
		else{
			sobj[0].src = "images/down_enabled.gif";
			obj.className="green_title";
		}
	}
function CheckForm(){
	var obj = document.frm;

	if(!IsAccount(obj.username.value,'�û���')){
		obj.username.focus();
		obj.username.select();
		return false;
	}
	else if(obj.city_id.value == -1){
		alert("��ѡ�����أ�");
		obj.city_id.focus();
		return false;
	} else if(Trim(obj.adsl_ser.value) == ""){
		alert("�������豸���кţ�");
		obj.adsl_ser.focus();
		return false;
	}
	else if(Trim(obj.ipaddress.value)!="" && !IsIPAddr(obj.ipaddress.value)){
		obj.ipaddress.focus();
		obj.ipaddress.select();
		return false;
	}
	else if(obj.bwlevel.value!="" && (obj.bwlevel.value<0 || obj.bwlevel.value>100)){
		alert("����ϸ���Ӧ��0%��100%֮��");
		obj.bwlevel.focus();
		obj.bwlevel.select();
		return false;
	}
/**	
	else if(!IsNull(obj.bandwidth.value,"����") || !IsNumber(obj.bandwidth.value,"����")){		
		obj.bandwidth.focus();
		obj.bandwidth.select();
		return false;
	}

	else if(obj.bandwidth.value<=0){
		alert("����Ӧ����0");
		obj.bandwidth.focus();
		obj.bandwidth.select();
		return false;
	}
*/
	else if(!IsNull(obj.staff_id.value,"Ա������")){
		obj.staff_id.focus();
		obj.staff_id.select();
		return false;
	}
	else{
		document.frm.hidopendate.value = DateToDes(document.frm.opendate.value);
		document.frm.hidonlinedate.value = DateToDes(document.frm.onlinedate.value);
		document.frm.hidpausedate.value = DateToDes(document.frm.pausedate.value);
		document.frm.hidclosedate.value = DateToDes(document.frm.closedate.value);
		document.frm.hidupdatetime.value = DateToDes(document.frm.updatetime.value);
		return true;
	}	
}

function DateToDes(v){
	if(v != ""){
		pos = v.indexOf("-");
		if(pos != -1 && pos == 4){
			y = parseInt(v.substring(0,pos));
			v = v.substring(pos+1,v.length);
		}
		pos = v.indexOf("-");
		if(pos != -1){
			m = parseInt(v.substring(0,pos));
			v = v.substring(pos+1,v.length);
		}
		if(v.length>0)
			d = parseInt(v);

		dt = new Date(m+"/"+d+"/"+y);
		var s  = dt.getTime();
		return s/1000;
	}
	else
		return 0;
}

function getAllIpAddress(){
	var oSelect = document.all("ipaddress");
	var s="";
	for(var i=0;i<oSelect.options.length;i++){
		if(s==""){
			s = oSelect.options[i].Value;
		}
		else{
			s += ","+ oSelect.options[i].Value;
		}
	}
	return s;
}

var iPos = 0;
function addip(){
	if(IsIPAddr(document.frm.oneipaddress.value)){
		var sText = document.frm.oneipaddress.value;
		var oSelect = document.all("ipaddress");
		if(DoSearch(oSelect,sText)){
			var oOption = document.createElement("OPTION");
			oSelect.options.add(oOption);
			oOption.innerText = sText;
			oOption.Value = sText;
			oSelect.selectedIndex = iPos;
			iPos++;
			document.frm.oneipaddress.value = "";
		}
	}
}

function delip(){
	var oSelect = document.all("ipaddress");
	var oPos = oSelect.selectedIndex; 
	if(oPos!=-1){
		oSelect.remove(oPos);
		iPos--;
		oSelect.selectedIndex = 0;
	}
}

function DoSearch(obj,text){
	var bz=true;
	for(var i=0;i<obj.options.length;i++){
		if(obj.options[i].text==text){
			var cmd =window.confirm("\"" + text+"\" �Ѿ����룬�Ƿ������");
			if(cmd){bz=true;break;}
			else{bz=false;break;}
		}
	}
	if(bz){return true;}
	else{return false;}
}
</SCRIPT>
<%@ include file="../toolbar.jsp"%>
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
<TR><TD HEIGHT=20>&nbsp;</TD></TR>
<TR><TD>
    <FORM NAME="frm" METHOD="post" ACTION="RadiusCustomerSave.jsp" onsubmit="return CheckForm()">
	<TABLE width="95%" border=0 cellspacing=0 cellpadding=0 align="center">
		<TR>
			<TD>
			<TABLE width="100%" height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
				<TR>
					<TD width="162" align="center" class="title_bigwhite">�û���Դ</TD>
					<TD>
						<IMG src="../images/attention_2.gif" width="15"	height="12"> ��'<font color="#FF0000">*</font>'�ı�������д��ѡ��
					</TD>
				</TR>
			</TABLE>
			</TD>
		</TR>
		<TR>
			<TD bgcolor=#000000>
				
              <TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
                <TR> 
                  <TH colspan="4" align="center">�༭��ҵ����ADSL�ͻ�</TH>
                </TR>
            	<TR>
					<TD colspan="4" align="center" class=column>
						������
					</TD>
				</TR>
                <TR bgcolor="#FFFFFF"> 
                  <TD class=column align="right">�û��ʻ�</TD>
                  <TD> 
                    <INPUT TYPE="text" NAME="username" maxlength=30 class=bk value="<%=userMap.get("username")%>">&nbsp;&nbsp;<font color="#FF0000">*</font>
				  	<input type="hidden" name="old_username" value="<%=userMap.get("username")%>">
				  </TD>
                   <TD class=column align="right">���ر�ʶ</TD>
                  <TD><%=strCityList%>&nbsp;&nbsp;<font color="#FF0000">*</font></TD>
                </TR>
                <TR bgcolor="#FFFFFF"> 
                 
  				 <TD class=column align="right">ADSL�豸���к�</TD>
                  <TD colspan="3"> 
                    <INPUT TYPE="text" NAME="adsl_ser" maxlength=30 class=bk value="<%=userMap.get("adsl_ser")%>" > &nbsp;&nbsp;<IMG onClick="selDev()" SRC="../images/search.gif" WIDTH="15"
											HEIGHT="12" BORDER="0" ALT="ѡ��">
											<input type="hidden" name="device_id" value="<%=userMap.get("device_id")%>">&nbsp;&nbsp;<font color="#FF0000">*</font>
                  </TD>
                </TR>

                <TR class="green_title" onclick="EC('suggestedContent',this);">
					<TD colspan="4" >
						<TABLE CELLPADDING="0" CELLSPACING="0" WIDTH="100%" BORDER="0">
						<TR class=column>
							<TD>
								<font size="2">������д��</font>
							</TD>
							<TD align="right">
								<IMG SRC="images/down_enabled.gif" WIDTH="7" HEIGHT="9" BORDER="0" ALT="">&nbsp;
							</TD>
						</TR>
						</TABLE>
					</TD>
				</TR>
				<TR bgcolor="#FFFFFF" leaf="suggestedContent" style="display:none"> 
                  <TD class=column align="right">VPIID��</TD>
                  <TD> 
                    <INPUT TYPE="text" NAME="vpiid" maxlength=6 class=bk value="<%=userMap.get("vpiid")%>">
                  </TD>
                  <TD class=column align="right">VCIID��</TD>
                  <TD> 
                    <INPUT TYPE="text" NAME="vciid" maxlength=6 class=bk value="<%=userMap.get("vciid")%>">
                  </TD>
                </TR>
                <TR bgcolor="#FFFFFF" leaf="suggestedContent" style="display:none"> 
                  <TD class=column align="right" >��װסַ</TD>
                  <TD colspan=""> 
                    <INPUT TYPE="text" NAME="address" maxlength=100 class=bk value="<%=userMap.get("address")%>">
                  </TD>

                  <TD class=column align="right">�����ʶ</TD>
                  <TD><%=strOfficeList%></TD>
                </TR>  
                 <TR bgcolor="#FFFFFF" leaf="suggestedContent" style="display:none"> 
                  <TD class=column align="right">С����ʶ</TD>
                  <TD><%=strZoneList%></TD>
                  <TD class=column align="right">VIP����</TD>
                  <TD> 
                    <INPUT TYPE="text" NAME="vipcardno" maxlength=30 class=bk value="<%=userMap.get("vipcardno")%>">
                  </TD>
                </TR>                            
               <TR bgcolor="#FFFFFF" leaf="suggestedContent" style="display:none"> 
                  <TD class=column align="right">�û�ʵ��</TD>
                  <TD> 
                    <INPUT TYPE="text" NAME="realname" maxlength=50 class=bk value="<%=userMap.get("realname")%>">
                  </TD>
                  <TD class=column align="right">�Ա�</TD>
                  <TD> 
                  	<%
                  		String select1 = "";
                  		String select2 = "";
                  		String sex = (String)userMap.get("sex");
                  		if(sex.equals("��")){
                  			select1 = "selected";
                  		} else {
                  			select2 = "selected";
                  		}
                  	 %>
                    <SELECT NAME="sex" class=bk>
                      <option value="��"  <%=select1%>>��</option>
                      <option value="Ů"  <%=select2%>>Ů</option>
                    </SELECT>
                  </TD>
                </TR>


                <TR bgcolor="#FFFFFF" leaf="suggestedContent" style="display:none"> 
                  <TD class=column align="right">Э����</TD>
                  <TD> 
                    <INPUT TYPE="text" NAME="contractno" maxlength=30 class=bk value="<%=userMap.get("contractno")%>">
                  </TD>
                  <TD class=column align="right">��ϵ��</TD>
                  <TD> 
                    <INPUT TYPE="text" NAME="linkman" maxlength=20 class=bk value="<%=userMap.get("linkman")%>">
                  </TD>
                </TR>
                <TR bgcolor="#FFFFFF" leaf="suggestedContent" style="display:none"> 
                  <TD class=column align="right">ADSL����֤��</TD>
                  <TD> 
                    <INPUT TYPE="text" NAME="adsl_card" maxlength=30 class=bk value="<%=userMap.get("adsl_card")%>">
                  </TD>
                  <TD class=column align="right">ADSL�豸�ͺ�</TD>
                  <TD> 
                    <INPUT TYPE="text" NAME="adsl_dev" maxlength=30 class=bk value="<%=userMap.get("adsl_dev")%>">
                  </TD>
                </TR>
                <TR bgcolor="#FFFFFF" leaf="suggestedContent" style="display:none"> 
                  <TD class=column align="right">Adsl�󶨵绰</TD>
                  <TD> 
                    <INPUT TYPE="text" NAME="phonenumber" maxlength=15 class=bk value="<%=userMap.get("phonenumber")%>">
                    </TD>
                  <TD class=column align="right">���ڵ���ID</TD>
                  <TD> 
                    <INPUT TYPE="text" NAME="cableid" maxlength=10 class=bk value="<%=userMap.get("cableid")%>">
                  </TD>
                </TR>
                <TR bgcolor="#FFFFFF" leaf="suggestedContent" style="display:none"> 
                  <TD class=column align="right">��ϵ�����֤��</TD>
                  <TD> 
                    <INPUT TYPE="text" NAME="linkman_credno" maxlength=20 class=bk value="<%=userMap.get("linkman_credno")%>">
                  </TD>
                  <TD class=column align="right">��ϵ�绰</TD>
                  <TD> 
                    <INPUT TYPE="text" NAME="linkphone" maxlength=20 class=bk value="<%=userMap.get("linkphone")%>">
                  </TD>
                </TR>
                <TR bgcolor="#FFFFFF" leaf="suggestedContent" style="display:none"> 
                  <TD class=column align="right">������</TD>
                  <TD> 
                    <INPUT TYPE="text" NAME="agent" maxlength=20 class=bk value="<%=userMap.get("agent")%>">
                  </TD>
                  <TD class=column align="right">���������֤��</TD>
                  <TD> 
                    <INPUT TYPE="text" NAME="agent_credno" maxlength=20 class=bk value="<%=userMap.get("agent_credno")%>">
                  </TD>
                </TR>
                <TR bgcolor="#FFFFFF" leaf="suggestedContent" style="display:none"> 
                  <TD class=column align="right">��������ϵ�绰</TD>
                  <TD> 
                    <INPUT TYPE="text" NAME="agentphone" maxlength=20 class=bk value="<%=userMap.get("agentphone")%>">
                  </TD>
                  <TD class=column align="right">����</TD>
                  <TD> 
                    <INPUT TYPE="text" NAME="bandwidth" maxlength=10 class=bk value="<%=userMap.get("bandwidth")%>">
                    (kbps)</TD>
                </TR>
                <%
                  
                  	String opendate = (String)userMap.get("opendate");
                  	if(opendate!= null && !opendate.equals("")){
                  		opendate = StringUtils.formatDate("yyyy-MM-dd",Long.parseLong(opendate));
                  	}
                   	String onlinedate = (String)userMap.get("onlinedate");
                  	if(onlinedate!= null && !onlinedate.equals("")){
                  		onlinedate = StringUtils.formatDate("yyyy-MM-dd",Long.parseLong(onlinedate));
                  	}
                  	
                   	String closedate = (String)userMap.get("closedate");
                  	if(closedate!= null && !closedate.equals("")){
                  		closedate = StringUtils.formatDate("yyyy-MM-dd",Long.parseLong(closedate));
                  	}                   	
                   	String pausedate = (String)userMap.get("pausedate");
                  	if(pausedate!= null && !pausedate.equals("")){
                  		pausedate = StringUtils.formatDate("yyyy-MM-dd",Long.parseLong(pausedate));
                  	}                 	                 
                   	String updatetime = (String)userMap.get("updatetime");
                  	if(updatetime!= null && !updatetime.equals("")){
                  		updatetime = StringUtils.formatDate("yyyy-MM-dd",Long.parseLong(updatetime));
                  	}                 
                  
                   %>
                <TR bgcolor="#FFFFFF" leaf="suggestedContent" style="display:none"> 
                  <TD class=column align="right">����ʱ��</TD>

                  <TD> 
                    <INPUT TYPE="text" NAME="opendate" class=bk value="<%=opendate%>">
                    <INPUT TYPE="button" value="��" class=btn onclick="showCalendar('day',event)">
                    <INPUT TYPE="hidden" NAME="hidopendate" class=bk>
                    </TD>
                  <TD class=column align="right">��ͨʱ��</TD>
                  <TD> 
                    <INPUT TYPE="text" NAME="onlinedate" class=bk value="<%=onlinedate%>">
                    <INPUT TYPE="button" value="��" class=btn onclick="showCalendar('day',event)">
                    <INPUT TYPE="hidden" NAME="hidonlinedate" class=bk>
                    </TD>
                </TR>
                <TR bgcolor="#FFFFFF" leaf="suggestedContent" style="display:none"> 
                  <TD class=column align="right">��ͣʱ��</TD>
                  <TD> 
                    <INPUT TYPE="text" NAME="pausedate" class=bk value="<%=pausedate%>">
                    <INPUT TYPE="button" value="��" class=btn onclick="showCalendar('day',event)">
                    <INPUT TYPE="hidden" NAME="hidpausedate" class=bk>
                  </TD>
                  <TD class=column align="right">����ʱ��</TD>
                  <TD> 
                    <INPUT TYPE="text" NAME="closedate" class=bk value="<%=closedate%>">
                    <INPUT TYPE="button" value="��" class=btn onclick="showCalendar('day',event)">
                    <INPUT TYPE="hidden" NAME="hidclosedate" class=bk>
                  </TD>
                </TR>
                <TR bgcolor="#FFFFFF" leaf="suggestedContent" style="display:none"> 
                  <TD class=column align="right">����ʱ��</TD>
                  <TD> 
                    <INPUT TYPE="text" NAME="updatetime" class=bk value="<%=updatetime%>">
                    <INPUT TYPE="button" value="��" class=btn onclick="showCalendar('day',event)">
                    <INPUT TYPE="hidden" NAME="hidupdatetime" class=bk>
                  </TD>
                  <TD class=column align="right">�Ƿ��ά</TD>
                  
                  
                  <TD colspan=""> 
                  	<%
                  	
                  		String check1 = "";
                  		String check2 = "";
                  		String isrepair = (String)userMap.get("isrepair");
                  		if(isrepair.equals("0")){
                  			check1 = "checked";
                  		} else {
                  			check2 = "checked";
                  		}
                  	
                  	 %>
                    <INPUT TYPE="radio" NAME="isrepair"  <%=check1%>>
	                    ��&nbsp;&nbsp; 
	                    <INPUT TYPE="radio" NAME="isrepair" value="1" <%=check2%>>
	                    �� </TD>
                </TR>                
                
                <TR class="green_title" onclick="EC('optionalContents',this);">
					<TD colspan="4">
						<TABLE CELLPADDING="0" CELLSPACING="0" WIDTH="100%" BORDER="0">
						<TR>
						<TD>
							<font size="2">ѡ����д��</font> 
						</TD>
						<TD align="right">
								<IMG SRC="images/down_enabled.gif" WIDTH="7" HEIGHT="9" BORDER="0" ALT="">&nbsp;
						</TD>
						</TR>
						</TABLE>
					</TD>
				</TR>
				<TR bgcolor="#FFFFFF" leaf="optionalContents" style="display:none"> 
                  <TD class=column align="right">�û�IP��ַ</TD>
                  <TD> 
                    <INPUT TYPE="text" NAME="ipaddress" maxlength=15 class=bk value="<%=userMap.get("ipaddress")%>">
                  </TD>
                  <TD class=column align="right">MAC��ַ</TD>
                  <TD> 
                    <INPUT TYPE="text" NAME="macaddress" maxlength=20 class=bk value="<%=userMap.get("macaddress")%>">
                  </TD>
                </TR>
                <TR bgcolor="#FFFFFF" leaf="optionalContents" style="display:none"> 
                  <TD class=column align="right">BAS�����豸��ʶ</TD>
                  <TD> 
                    <%
                    
                    String basdevice_id = (String)userMap.get("basdevice_id");
					cursor = DeviceAct.getDeviceBasInfo(request);  
					
					Map fields = cursor.getNext();
					if(fields == null){
						out.println("<SELECT name=basdevice_id class=bk><option value='0'>==û�м�¼==</option></SELECT>");
					}
					else{
						out.println("<SELECT name=basdevice_id class=bk>");
						out.println("<OPTION VALUE=0>====��ѡ��====</OPTION>");
						String select = "";
						while(fields != null){
							
							if(basdevice_id.equals(fields.get("loopback_ip"))){
								select = "selected";
							}else{
								select= "";
							}
							out.println("<option value='"+fields.get("device_id")+"'  " + select + ">"+fields.get("loopback_ip")+"/"+fields.get("device_name")+"</option>");

							fields = cursor.getNext();
						}
						out.println("</select>");
					}
				  %>
                  </TD>
                  <TD class=column align="right">BAS�����豸���ܺ�</TD>
                  <TD> 
                    <INPUT TYPE="text" NAME="basdevice_shelf" maxlength=10  class=bk value="<%=userMap.get("basdevice_shelf")%>">
                  </TD>
                </TR>
                

                <TR bgcolor="#FFFFFF" leaf="optionalContents" style="display:none"> 
                  <TD class=column align="right">BAS�����豸��λ��</TD>
                  <TD> 
                    <INPUT TYPE="text" NAME="basdevice_frame" maxlength=10  class=bk value="<%=userMap.get("basdevice_frame")%>">
                  </TD>
                  <TD class=column align="right">BAS�����豸��λ��</TD>
                  <TD> 
                    <INPUT TYPE="text" NAME="basdevice_slot" maxlength=10 class=bk value="<%=userMap.get("basdevice_slot")%>">
                  </TD>
                </TR>
                <TR bgcolor="#FFFFFF" leaf="optionalContents" style="display:none"> 
                  <TD class=column align="right">BAS�����豸�˿�</TD>
                  <TD> 
                    <INPUT TYPE="text" NAME="basdevice_port" maxlength=20  class=bk value="<%=userMap.get("basdevice_port")%>">
                  </TD>
                  <TD class=column align="right">VLANID��</TD>
                  <TD> 
                    <INPUT TYPE="text" NAME="vlanid" maxlength=6 class=bk value="<%=userMap.get("vlanid")%>">
                  </TD>
                </TR>
                
                
                <TR bgcolor="#FFFFFF" leaf="optionalContents" style="display:none"> 
                  <TD class=column align="right">DSLAM�����豸���ܺ�</TD>
                  <TD colspan="3"> 
                    <input type="text" name="device_shelf" maxlength=4 class=bk value="<%=userMap.get("device_shelf")%>">
                    &nbsp;</TD>
                </TR>
                <TR bgcolor="#FFFFFF" leaf="optionalContents" style="display:none"> 
                  <TD class=column align="right">DSLAM�����豸��λ��</TD>
                  <TD> 
                    <INPUT TYPE="text" NAME="device_frame" maxlength=10  class=bk value="<%=userMap.get("device_frame")%>">
                     </TD>
                  <TD class=column align="right">DSLAM�����豸��λ��</TD>
                  <TD> 
                    <INPUT TYPE="text" NAME="device_slot" maxlength=10  class=bk value="<%=userMap.get("device_slot")%>">
                     </TD>
                </TR>
                <TR bgcolor="#FFFFFF" leaf="optionalContents" style="display:none"> 
                  <TD class=column align="right">DSLAM�����豸�˿�</TD>
                  <TD> 
                    <INPUT TYPE="text" NAME="device_port" maxlength=20  class=bk value="<%=userMap.get("device_port")%>">
                   </TD>
                  <TD class=column align="right">������</TD>
                  <TD> 
                    <INPUT TYPE="text" NAME="workid" maxlength=20 class=bk value="<%=userMap.get("workid")%>">
                  </TD>
                </TR>
                <TR bgcolor="#FFFFFF" leaf="optionalContents" style="display:none"> 
                  <TD class=column align="right">�û���·���</TD>
                  <TD> 
                    <INPUT TYPE="text" NAME="userline" maxlength=6 class=bk value="<%=userMap.get("userline")%>">
                  </TD>
                  <TD class=column align="right">ADSL����</TD>
                  <TD> 
                    <INPUT TYPE="text" NAME="adsl_hl" maxlength=15 class=bk value="<%=userMap.get("adsl_hl")%>">
                  </TD>
                </TR>
                <TR bgcolor="#FFFFFF" leaf="optionalContents" style="display:none"> 
                  <TD class=column align="right">��ͬ��</TD>
                  <TD> 
                    <INPUT TYPE="text" NAME="cotno" maxlength=16 class=bk value="<%=userMap.get("cotno")%>">
                  </TD>
                  <TD class=column align="right">���ܷ������ͱ��뼯</TD>
                  <TD> 
                    <INPUT TYPE="text" NAME="service_set" maxlength=200 class=bk value="<%=userMap.get("service_set")%>">
                  </TD>
                </TR>
                <TR bgcolor="#FFFFFF" leaf="optionalContents" style="display:none">
				  <TD class=column align="right">Ա������</TD>
                  <TD colspan="3"> 
                    <INPUT TYPE="text" NAME="staff_id" maxlength=10 class=bk value="<%=user.getAccount()%>">
                    &nbsp;</TD>
                </TR>

                <TR bgcolor="#FFFFFF" leaf="optionalContents" style="display:none"> 
                  <TD class=column align="right" >��ע</TD>
                  <TD colspan="3"> 
                    <INPUT TYPE="text" NAME="remark" maxlength=50 class=bk size=50 value="<%=userMap.get("remark")%>">
                  </TD>
                </TR>
                <TR> 
                  <TD colspan="4" align="center" class=foot> 
                    <INPUT TYPE="submit" value=" �� �� " class=btn>
                    &nbsp;&nbsp; 
                    <INPUT TYPE="reset" value=" �� д " class=btn>
                    <INPUT TYPE="hidden" name="action" value="update">
                    <INPUT TYPE="hidden" name="user_id" value="<%=str_user_id%>">
                  </TD>
                </TR>
              </TABLE>
			</TD>
		</TR>
	</TABLE>
    </FORM>
<TR><TD>&nbsp;</TD></TR>
<TR><TD HEIGHT=20><IFRAME ID=childFrm SRC="" STYLE="display:none"></IFRAME>
&nbsp;</TD></TR>
</TABLE>
