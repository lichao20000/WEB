<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.linkage.litms.resource.*,com.linkage.litms.common.database.*,java.util.*,com.linkage.litms.common.util.StringUtils" %>
<jsp:useBean id="DeviceAct" scope="request" class="com.linkage.litms.resource.DeviceAct"/>
<jsp:useBean id="AdslAct" scope="request" class="com.linkage.litms.resource.AdslAct"/>

<%@ page import="com.linkage.module.gwms.dao.tabquery.CityDAO" %>

<%
request.setCharacterEncoding("GBK");
String city_id1 = user.getCityId();

List cityIdList = CityDAO.getAllNextCityIdsByCityPid(city_id1);
String m_CityIdQuery = StringUtils.weave(cityIdList);
String strSQL  = "";
String strCityList, strAdslList,strOfficeList,strZoneList,strResourceList,strVendorList,strDeviceList;
Cursor cursor;
strCityList = DeviceAct.getCityListSelf(false,"","",request);
strOfficeList = DeviceAct.getOfficeList(false,"",m_CityIdQuery,"");
strZoneList = DeviceAct.getZoneList(false,"","");
strAdslList = AdslAct.getGatherInfoForm(false,"","");
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
	if(!IsAccount(obj.username.value,'用户名')){
		obj.username.focus();
		obj.username.select();
		return false;
	}
	 else if(obj.city_id.value == -1){
		alert("请选择属地！");
		obj.city_id.focus();
		return false;
	}
	else if(Trim(obj.adsl_ser.value) == ""){
		alert("请填写设备序列号！");
		obj.adsl_ser.focus();
		obj.adsl_ser.select();
		return false;
	}
	else if(Trim(obj.ipaddress.value)!="" && !IsIPAddr(obj.ipaddress.value)){
		obj.ipaddress.focus();
		obj.ipaddress.select();
		return false;
	}
	else if(obj.bwlevel.value!="" && (obj.bwlevel.value<0 || obj.bwlevel.value>100)){
		alert("带宽合格率应在0%和100%之间");
		obj.bwlevel.focus();
		obj.bwlevel.select();
		return false;
	}
	else if(!IsNull(obj.staff_id.value,"员工代码")){
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
			var cmd =window.confirm("\"" + text+"\" 已经加入，是否继续？");
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
					<TD width="162" align="center" class="title_bigwhite">用户资源</TD>
					<TD>
						<IMG src="../images/attention_2.gif" width="15"	height="12"> 带'<font color="#FF0000">*</font>'的表单必须填写或选择
					</TD>
				</TR>
			</TABLE>
			</TD>
		</TR>
		<TR>
			<TD bgcolor=#999999>
				
              <TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
                <TR> 
                  <TH colspan="4" align="center">添加企业网关ADSL用户</TH>
                </TR>
            	<TR>
					<TD colspan="4" align="center" class=column>
						必填区
					</TD>
				</TR>
                <TR bgcolor="#FFFFFF"> 
                  <TD class=column align="right" width="20%" nowrap>用户帐户</TD>
                  <TD width="35%" nowrap> 
                    <INPUT TYPE="text" NAME="username" maxlength=30 class=bk>&nbsp;&nbsp;<font color="#FF0000">*</font>
				  </TD>
                  <TD class=column align="right" nowrap width="18%" nowrap>属地标识</TD>
                  <TD nowrap><%=strCityList%>&nbsp;&nbsp;<font color="#FF0000">*</font></TD>
                </TR>

				<TR bgcolor="#FFFFFF"> 

                  
                  <TD class=column align="right" nowrap>ADSL设备序列号</TD>
                  <TD colspan="3" nowrap> 
                    <INPUT TYPE="text" NAME="adsl_ser" maxlength=50 class=bk readonly> &nbsp;&nbsp;<IMG onClick="selDev()" SRC="../images/search.gif" WIDTH="15"
											HEIGHT="12" BORDER="0" ALT="选择">&nbsp;&nbsp;<font color="#FF0000">*</font>
											<input type="hidden" name="device_id" value="">
                  </TD>
                </TR>
                <TR class="green_title" onclick="EC('suggestedContent',this);">
					<TD colspan="4" >
						<TABLE CELLPADDING="0" CELLSPACING="0" WIDTH="100%" BORDER="0">
						<TR class=column>
							<TD>
								<font size="2">建议填写区</font>
							</TD>
							<TD align="right">
								<IMG SRC="images/down_enabled.gif" WIDTH="7" HEIGHT="9" BORDER="0" ALT="">&nbsp;
							</TD>
						</TR>
						</TABLE>
					</TD>
				</TR>
				
                 <TR bgcolor="#FFFFFF" leaf="suggestedContent" style="display:none"> 
                  <TD class=column align="right">VPIID号</TD>
                  <TD> 
                    <INPUT TYPE="text" NAME="vpiid" maxlength=6 class=bk>&nbsp;&nbsp;
                  </TD>
                  <TD class=column align="right">VCIID号</TD>
                  <TD> 
                    <INPUT TYPE="text" NAME="vciid" maxlength=6 class=bk>&nbsp;&nbsp;
                  </TD>
                </TR>               
                
                
                <TR bgcolor="#FFFFFF" leaf="suggestedContent" style="display:none"> 
                  <TD class=column align="right" >安装住址</TD>
                  <TD colspan=""> 
                    <INPUT TYPE="text" NAME="address" maxlength=100 class=bk>
                  </TD>
                  <TD class=column align="right">局向标识</TD>
                  <TD><%=strOfficeList%></TD>
                </TR>  
                 <TR bgcolor="#FFFFFF" leaf="suggestedContent" style="display:none"> 
                  <TD class=column align="right">小区标识</TD>
                  <TD><%=strZoneList%></TD>
                  <TD class=column align="right">VIP卡号</TD>
                  <TD> 
                    <INPUT TYPE="text" NAME="vipcardno" maxlength=30 class=bk>
                  </TD>
                </TR>                            
               <TR bgcolor="#FFFFFF" leaf="suggestedContent" style="display:none"> 
                  <TD class=column align="right">用户实名</TD>
                  <TD> 
                    <INPUT TYPE="text" NAME="realname" maxlength=50 class=bk>
                  </TD>
                  <TD class=column align="right">性别</TD>
                  <TD> 
                    <SELECT NAME="sex" class=bk>
                      <option value="男">男</option>
                      <option value="女">女</option>
                    </SELECT>
                  </TD>
                </TR>
                <TR bgcolor="#FFFFFF" leaf="suggestedContent" style="display:none"> 
                  <TD class=column align="right">协议编号</TD>
                  <TD> 
                    <INPUT TYPE="text" NAME="contractno" maxlength=30 class=bk>
                  </TD>
                  <TD class=column align="right">联系人</TD>
                  <TD> 
                    <INPUT TYPE="text" NAME="linkman" maxlength=20 class=bk>
                  </TD>
                </TR>
                <TR bgcolor="#FFFFFF" leaf="suggestedContent" style="display:none"> 
                  <TD class=column align="right">ADSL入网证号</TD>
                  <TD> 
                    <INPUT TYPE="text" NAME="adsl_card" maxlength=30 class=bk>
                  </TD>
                  <TD class=column align="right">ADSL设备型号</TD>
                  <TD> 
                    <INPUT TYPE="text" NAME="adsl_dev" maxlength=30 class=bk>
                  </TD>
                </TR>
                <TR bgcolor="#FFFFFF" leaf="suggestedContent" style="display:none"> 
                  <TD class=column align="right">Adsl绑定电话</TD>
                  <TD> 
                    <INPUT TYPE="text" NAME="phonenumber" maxlength=15 class=bk>
                    </TD>
                  <TD class=column align="right">所在电缆ID</TD>
                  <TD> 
                    <INPUT TYPE="text" NAME="cableid" maxlength=10 class=bk>
                  </TD>
                </TR>
                <TR bgcolor="#FFFFFF" leaf="suggestedContent" style="display:none"> 
                  <TD class=column align="right">联系人身份证号</TD>
                  <TD> 
                    <INPUT TYPE="text" NAME="linkman_credno" maxlength=20 class=bk>
                  </TD>
                  <TD class=column align="right">联系电话</TD>
                  <TD> 
                    <INPUT TYPE="text" NAME="linkphone" maxlength=20 class=bk>
                  </TD>
                </TR>
                <TR bgcolor="#FFFFFF" leaf="suggestedContent" style="display:none"> 
                  <TD class=column align="right">代办人</TD>
                  <TD> 
                    <INPUT TYPE="text" NAME="agent" maxlength=20 class=bk>
                  </TD>
                  <TD class=column align="right">代办人身份证号</TD>
                  <TD> 
                    <INPUT TYPE="text" NAME="agent_credno" maxlength=20 class=bk>
                  </TD>
                </TR>
                <TR bgcolor="#FFFFFF" leaf="suggestedContent" style="display:none"> 
                  <TD class=column align="right">代办人联系电话</TD>
                  <TD> 
                    <INPUT TYPE="text" NAME="agentphone" maxlength=20 class=bk>
                  </TD>
                  <TD class=column align="right">带宽</TD>
                  <TD> 
                    <INPUT TYPE="text" NAME="bandwidth" maxlength=10 class=bk>
                    (kbps)</TD>
                </TR>
                <TR bgcolor="#FFFFFF" leaf="suggestedContent" style="display:none"> 
                  <TD class=column align="right">开户时间</TD>
                  <TD> 
                    <INPUT TYPE="text" NAME="opendate" class=bk>
                    <INPUT TYPE="button" value="▼" class=btn onclick="showCalendar('day',event)">
                    <INPUT TYPE="hidden" NAME="hidopendate" class=bk>
                    </TD>
                  <TD class=column align="right">开通时间</TD>
                  <TD> 
                    <INPUT TYPE="text" NAME="onlinedate" class=bk>
                    <INPUT TYPE="button" value="▼" class=btn onclick="showCalendar('day',event)">
                    <INPUT TYPE="hidden" NAME="hidonlinedate" class=bk>
                    </TD>
                </TR>
                <TR bgcolor="#FFFFFF" leaf="suggestedContent" style="display:none"> 
                  <TD class=column align="right">暂停时间</TD>
                  <TD> 
                    <INPUT TYPE="text" NAME="pausedate" class=bk>
                    <INPUT TYPE="button" value="▼" class=btn onclick="showCalendar('day',event)">
                    <INPUT TYPE="hidden" NAME="hidpausedate" class=bk>
                  </TD>
                  <TD class=column align="right">销户时间</TD>
                  <TD> 
                    <INPUT TYPE="text" NAME="closedate" class=bk>
                    <INPUT TYPE="button" value="▼" class=btn onclick="showCalendar('day',event)">
                    <INPUT TYPE="hidden" NAME="hidclosedate" class=bk>
                  </TD>
                </TR>
                <TR bgcolor="#FFFFFF" leaf="suggestedContent" style="display:none"> 
                  <TD class=column align="right">更新时间</TD>
                  <TD> 
                    <INPUT TYPE="text" NAME="updatetime" class=bk>
                    <INPUT TYPE="button" value="▼" class=btn onclick="showCalendar('day',event)">
                    <INPUT TYPE="hidden" NAME="hidupdatetime" class=bk>
                  </TD>
                  <TD class=column align="right">是否代维</TD>
                  <TD colspan=""> 
                    <INPUT TYPE="radio" NAME="isrepair" value="0">
	                    否&nbsp;&nbsp; 
	                    <INPUT TYPE="radio" NAME="isrepair" value="1">
	                    是 </TD>
                </TR>                
                
                <TR class="green_title" onclick="EC('optionalContents',this);">
					<TD colspan="4">
						<TABLE CELLPADDING="0" CELLSPACING="0" WIDTH="100%" BORDER="0">
						<TR>
						<TD>
							<font size="2">选择填写区</font> 
						</TD>
						<TD align="right">
								<IMG SRC="images/down_enabled.gif" WIDTH="7" HEIGHT="9" BORDER="0" ALT="">&nbsp;
						</TD>
						</TR>
						</TABLE>
					</TD>
				</TR>
				<TR bgcolor="#FFFFFF" leaf="optionalContents" style="display:none"> 
                  <TD class=column align="right">用户IP地址</TD>
                  <TD> 
                    <INPUT TYPE="text" NAME="ipaddress" maxlength=15 class=bk>
                  </TD>
                  <TD class=column align="right">MAC地址</TD>
                  <TD> 
                    <INPUT TYPE="text" NAME="macaddress" maxlength=20 class=bk>
                  </TD>
                </TR>
                <TR bgcolor="#FFFFFF" leaf="optionalContents" style="display:none"> 
                  <TD class=column align="right">BAS接入设备标识</TD>
                  <TD> 
                    <%
					cursor = DeviceAct.getDeviceBasInfo(request);  
					Map fields = cursor.getNext();
					if(fields == null){
						out.println("<SELECT name=basdevice_id class=bk><option value='0'>请选择</option></SELECT>");
					}
					else{
						out.println("<SELECT name=basdevice_id class=bk>");
						out.println("<OPTION VALUE=0>====请选择====</OPTION>");
						while(fields != null){
							out.println("<option value='"+fields.get("device_id")+"'>"+fields.get("loopback_ip")+"/"+fields.get("device_name")+"</option>");

							fields = cursor.getNext();
						}
						out.println("</select>");
					}
				  %>
                  </TD>
                  <TD class=column align="right">BAS接入设备机架号</TD>
                  <TD> 
                    <INPUT TYPE="text" NAME="basdevice_shelf" maxlength=10  class=bk>
                  </TD>
                </TR>
                

                <TR bgcolor="#FFFFFF" leaf="optionalContents" style="display:none"> 
                  <TD class=column align="right">BAS接入设备框位号</TD>
                  <TD> 
                    <INPUT TYPE="text" NAME="basdevice_frame" maxlength=10  class=bk>
                  </TD>
                  <TD class=column align="right">BAS接入设备槽位号</TD>
                  <TD> 
                    <INPUT TYPE="text" NAME="basdevice_slot" maxlength=10 class=bk>
                  </TD>
                </TR>
                <TR bgcolor="#FFFFFF" leaf="optionalContents" style="display:none"> 
                  <TD class=column align="right">BAS接入设备端口</TD>
                  <TD> 
                    <INPUT TYPE="text" NAME="basdevice_port" maxlength=20  class=bk>
                  </TD>
                  <TD class=column align="right">VLANID号</TD>
                  <TD> 
                    <INPUT TYPE="text" NAME="vlanid" maxlength=6 class=bk>
                  </TD>
                </TR>
                
                
                <TR bgcolor="#FFFFFF" leaf="optionalContents" style="display:none"> 
                  <TD class=column align="right">DSLAM接入设备机架号</TD>
                  <TD colspan="3"> 
                    <input type="text" name="device_shelf" maxlength=4 value="0" class=bk>
                    &nbsp;</TD>
                </TR>
                <TR bgcolor="#FFFFFF" leaf="optionalContents" style="display:none"> 
                  <TD class=column align="right">DSLAM接入设备框位号</TD>
                  <TD> 
                    <INPUT TYPE="text" NAME="device_frame" maxlength=10  value="0" class=bk>
                     </TD>
                  <TD class=column align="right">DSLAM接入设备槽位号</TD>
                  <TD> 
                    <INPUT TYPE="text" NAME="device_slot" maxlength=10 value="0" class=bk>
                     </TD>
                </TR>
                <TR bgcolor="#FFFFFF" leaf="optionalContents" style="display:none"> 
                  <TD class=column align="right">DSLAM接入设备端口</TD>
                  <TD> 
                    <INPUT TYPE="text" NAME="device_port" maxlength=20 value="0" class=bk>
                   </TD>
                  <TD class=column align="right">工单号</TD>
                  <TD> 
                    <INPUT TYPE="text" NAME="workid" maxlength=20 class=bk>
                  </TD>
                </TR>
                <TR bgcolor="#FFFFFF" leaf="optionalContents" style="display:none"> 
                  <TD class=column align="right">用户线路编号</TD>
                  <TD> 
                    <INPUT TYPE="text" NAME="userline" maxlength=6 class=bk>
                  </TD>
                  <TD class=column align="right">ADSL横列</TD>
                  <TD> 
                    <INPUT TYPE="text" NAME="adsl_hl" maxlength=15 class=bk>
                  </TD>
                </TR>
                <TR bgcolor="#FFFFFF" leaf="optionalContents" style="display:none"> 
                  <TD class=column align="right">合同号</TD>
                  <TD> 
                    <INPUT TYPE="text" NAME="cotno" maxlength=16 class=bk>
                  </TD>
                  <TD class=column align="right">享受服务类型编码集</TD>
                  <TD> 
                    <INPUT TYPE="text" NAME="service_set" maxlength=200 class=bk>
                  </TD>
                </TR>
                <TR bgcolor="#FFFFFF" leaf="optionalContents" style="display:none">
				  <TD class=column align="right">员工代码</TD>
                  <TD colspan="3"> 
                    <INPUT TYPE="text" NAME="staff_id" maxlength=10 class=bk value="<%=user.getAccount()%>">
                    &nbsp;</TD>
                </TR>

                <TR bgcolor="#FFFFFF" leaf="optionalContents" style="display:none"> 
                  <TD class=column align="right" >备注</TD>
                  <TD colspan="3"> 
                    <INPUT TYPE="text" NAME="remark" maxlength=50 class=bk size=50>
                  </TD>
                </TR>
                <TR> 
                  <TD colspan="4" align="center" class=foot> 
                    <INPUT TYPE="submit" value=" 保 存 " class=btn>
                    &nbsp;&nbsp; 
                    <INPUT TYPE="reset" value=" 重 写 " class=btn>
                    <INPUT TYPE="hidden" name="action" value="add">
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
