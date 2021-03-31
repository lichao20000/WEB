<%@ include file="../timelater.jsp"%>
<%@ include file="../head.jsp"%>
<%@ include file="../toolbar.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@page import="com.linkage.litms.LipossGlobals"%>
<jsp:useBean id="areaManage" scope="request" class="com.linkage.litms.system.dbimpl.AreaManageSyb"/>
<script type="text/javascript" src="../Js/jquery.js"></script>
<%
long acc_oid = user.getId();

String InstArea = LipossGlobals.getLipossProperty("InstArea.ShortName");
//��ʼ����ǰʱ�� 
DateTimeUtil dateUtil = new DateTimeUtil();   
String time = dateUtil.getTime();
String today = dateUtil.getYear()+"-"+dateUtil.getMonth()+"-"+dateUtil.getDay();

LogItem iteminfos = LogItem.getInstance();
Cursor cursor = iteminfos.getItemInfosByUserID(user.getId());
//String operation_nameStr = FormUtil.createListBox(cursor,"item_name","item_name",false,"","operation_nameSelect");

//cursor.Reset();
//String operation_objectStr = FormUtil.createListBox(cursor,"item_url","item_url",false,"","operation_objectSelect");

cursor = null;   


//20200507 ɽ����ͨ �û�����Ĭ��ѡ��admin.com(��һ��)
String defaultAreaId = "";
String defaultAreaName = "";
if("sx_lt".equals(InstArea)){
	// ����
	long userAreaId = user.getAreaId();
	List<Map> areaList = null;
	if(user.getAccount().equals("admin")){//���ж���ʾ
		areaList = areaManage.getAreaAll();
	}else{
		areaList = areaManage.getAreaById(Integer.parseInt(String.valueOf(userAreaId)));
	}
	if(areaList.size() > 0){
		defaultAreaId = String.valueOf(areaList.get(0).get("area_id"));
		defaultAreaName = String.valueOf(areaList.get(0).get("area_name"));
	}
}

%> 
<%@page import="com.linkage.litms.common.util.DateTimeUtil"%>
<%@page import="com.linkage.litms.common.util.FormUtil"%>
<%@page import="com.linkage.litms.common.database.Cursor"%>
<style>
NOBR.BT
{
	padding-right: 6px;
	padding-left: 6px;
	padding-top: 2px;
	border-top: #ffffff 1px solid;
	border-bottom: #ffffff 1px solid;
	border-right: #ffffff 1px solid;
	border-left: #ffffff 1px solid;
	cursor:hand;
}

NOBR.BTOver{
	padding-right: 6px;
	padding-left: 6px;
	padding-top: 2px;
	border-top: #316AC5 1px solid;
	border-bottom: #316AC5 1px solid;
	border-right: #316AC5 1px solid;
	border-left: #316AC5 1px solid;
	cursor:hand;
	background-color: #C1D2EE;
}
</style>
<SCRIPT LANGUAGE="JavaScript" src="../Js/code.js"></SCRIPT>
<script language="JavaScript">

function checkForm1()
{    
	var InstArea = "<%=InstArea%>";
    //����
	var objectS_time = document.all.frm.start_day.value;
	var objectE_time = document.all.frm.end_day.value;	
	//alert("objectS_time:"+objectS_time);
	
	var start_time =document.all.frm.start_time.value;	
	if(""==start_time)
	{
	  alert("����д��ʼʱ�䣡");
	  return false;
	}
	
	var end_time =document.all.frm.end_time.value;
	if(""==end_time)
	{
	  alert("����д����ʱ�䣡");
	  return false;
	}
	
	if(!IsDate1(objectS_time))
	{
		alert("��������ȷ�Ŀ�ʼ���ڣ�");
		return false;
	}
	
	if(!IsDate1(objectE_time))
	{
		alert("��������ȷ�Ľ������ڣ�");
		return false;
	}	
	if(formatDate(objectS_time,start_time)>formatDate(objectE_time,end_time))
	{
		alert("��ʼʱ�䲻�ܴ��ڽ���ʱ�䣡");
		return false;
	}
	if(periodMonth(objectS_time,objectE_time)>3)
	{
	 alert("����ʱ�䲻�ܳ�����ʼʱ��3���£�");
	 return false;
	}
	var area_name = document.all.frm.area_name.value;
	var userSelect = document.all.frm.userSelect.value;	
	if (InstArea != "nx_dx") {
		if(""==area_name)
		{
		 alert("��ѡ���û�����");
		 return false;
		}
		if("-1"==userSelect || ""==userSelect || "null"==userSelect )
		{
		 alert("��ѡ���û���");
		 return false;
		}
	}

	return true;
//    var area_name = document.all("area_name").value;
//    var acc_oid = document.all("userSelect").value;
//    var operation_name = document.all("operation_nameSelect").value;    
//    var device_oui = document.all("device_oui").value;
//    var device_serialnumber = document.all("device_serialnumber").value;
//    if((area_name!=""&&acc_oid!="-1")||operation_name!="-1"||(device_serialnumber!=""&&device_oui!=""))
//    {
//      return true;
//    }
//    else
//    {
//      alert("�û�����������������ƻ��豸OUI|���кţ���ѡ��һ����");
//      return false;
//    }	   
}

function formatDate(strValue,strHMS)
  {    
	var str1=strValue.substring(strValue.indexOf("-")+1,strValue.lastIndexOf("-"));
		str1=(str1.length==1)?"0"+str1:str1; //��
	var str2=strValue.substring(strValue.lastIndexOf("-")+1);
		str2=(str2.length==1)?"0"+str2:str2; //��
	strValue=strValue.substring(0,strValue.indexOf("-")+1)+str1+"-"+str2;	
	if(strHMS.length>0){
		var str3=strHMS.substring(strHMS.indexOf(":")+1,strHMS.lastIndexOf(":"));
		str3=(str3.length==1)?"0"+str3:str3;
		var str4=strHMS.substring(strHMS.lastIndexOf(":")+1);
		str4=(str4.length==1)?"0"+str4:str4;
		var str5=strHMS.substring(0,strHMS.indexOf(":"));
		str5=(str5.length==1)?"0"+str5:str5;
		strValue=strValue+" "+str5+":"+str3+":"+str4;
	}
	return strValue;
  }
  
function periodMonth(beginDate,endDate)
{
  //��
  var beginStr1=beginDate.substring(0,beginDate.indexOf("-"));  
  //��
  var beginStr2=beginDate.substring(beginDate.indexOf("-")+1,beginDate.lastIndexOf("-"));
  
  //��
  var endStr1=endDate.substring(0,endDate.indexOf("-"));  
  //��
  var endStr2=endDate.substring(endDate.indexOf("-")+1,endDate.lastIndexOf("-"));
  
  var months = (endStr1-beginStr1)*12+(endStr2-beginStr2);
  return months;
}

function IsDate1(strValue){
	var v = strValue;
	var tmpStr1,tmpStr2,tmpStr3;
	var pos = v.indexOf("-");
	if(pos == -1){
		alert("���ڸ�ʽ�����ϣ���ȷ��ʽ�ǣ���-��-��");
		return false;
	}

	tmpStr1 = v.substring(0,pos);

	if(tmpStr1.length !=4 || parseInt(tmpStr1)<1){
		alert("���ڸ�ʽ�����ϣ���ȷ��ʽ�ǣ���-��-��");
		return false;
	}

	v = v.substring(pos+1,v.length);
	pos = v.indexOf("-");
	if(pos == -1){
		alert("���ڸ�ʽ�����ϣ���ȷ��ʽ�ǣ���-��-��");
		return false;
	}
	tmpStr2 = v.substring(0,pos);
	if(tmpStr2.length >2 || parseInt(tmpStr2) > 12 || parseInt(tmpStr2)<1){
		alert("���ڸ�ʽ�����ϣ���ȷ��ʽ�ǣ���-��-��");
		return false;
	}

	tmpStr3=v.substring(pos+1);
	if(tmpStr3.length >2 || parseInt(tmpStr3) > 31 || parseInt(tmpStr3)<1){
		alert("���ڸ�ʽ�����ϣ���ȷ��ʽ�ǣ���-��-��");
		return false;
	}
	return true;
}

function areaSelect(){
	var width = 360;
	var page = "./areaSelect_log.jsp?area_pid=<%=user.getAreaId()%>&width="+ width;
	var returnObj = window.showModalDialog(page,"win","left=20,top=20,width="+ width +",height=450,resizable=no,scrollbars=no");		
	if(!has_showModalDialog) return;
	if(null!=returnObj)
	{
		deviceResult(returnObj);
	}
}

function deviceResult(returnObj)
{    
	document.all("area_name").value = returnObj.area_name;
   document.all("area_id").value =returnObj.area_id;
   if(document.all("area_name").value!="")
   {
     showChild();
   }
}

function showChild()
{    
    var area_name = document.all("area_name").value;
    
    if(area_name!='')
    {
       document.all("childFrm").src ="userByArea.jsp?area_name="+area_name+"&acc_oid=<%=acc_oid%>";
    } 
    
}

function getOperName() {
	var logtype = document.all("logtype").value;
	if (logtype == 1) {
		document.all("operation_nameSelect").disabled = false;
	} else {
		document.all("operation_nameSelect").value = "-1";
		document.all("operation_nameSelect").disabled = true
	}

}


function doBak() {
	if(!checkForm1()){
		return;
	}
	
	var obj = document.frm;
	if(obj.tab_name.value ==""){
		alert("�����������");
		obj.tab_name.focus();
		return false;
	}

	document.all("childFrm").src ="logSelBak.jsp?tab_name_zh=oper_log&tab_name=" + obj.tab_name.value + "&device_serialnumber=" + obj.device_serialnumber.value + "&start_day=" + obj.start_day.value + "&start_time=" + obj.start_time.value + "&end_day=" + obj.end_day.value + "&end_time=" + obj.end_time.value + "&logtype=" + obj.logtype.value + "&userSelect=" + obj.userSelect.value + "&operation_nameSelect=" + obj.operation_nameSelect.value + "&logtype=" + obj.logtype.value + "&data_type=2&action=add";
	
}

</script>

<form name="frm" method="post" action="log_select_submit.jsp" onsubmit="return checkForm1()">

<table width="98%" border=0 align="center" cellpadding="0" cellspacing="0" class="text">
	<TR>
		<TD HEIGHT=20>&nbsp;</TD>
	</TR>
	<tr>
		<td>
			<table width="100%" height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
				<tr>
					<td width="162" align="center" class="title_bigwhite">
						��־����
					</td>
					<td>
						<img src="../images/attention_2.gif" width="15" height="12">
						ϵͳ��־��ѯҳ��
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<TR><TD >
	    <TABLE border=0 cellspacing=1 cellpadding=2 bgcolor="#999999" width="100%">
	        <tr bgcolor="#FFFFFF" class="blue_title">                
                  <TH colspan=4>��־��ѯ</TH>
            </tr>
            <tr bgcolor="#FFFFFF">
               <td width="15%"  align='right'>
                    ��ʼʱ�䣺
               </td>
               <td width="35%">
                    <input type="text" name="start_day" class=bk value="<%=today%>" size=10 readonly>
                    <input type="button" value="��" class=jianbian onClick="showCalendar('day',event)">
                    <input type="text" name="start_time" class=bk value="00:00:00" size=8>
               </td>
               <td width="15%" align='right'>
                    ����ʱ�䣺
               </td>
               <td width="35%">
                    <input type="text" name="end_day" class=bk value="<%=today%>" size=10 readonly>
                    <input type="button" value="��" class=jianbian onClick="showCalendar('day',event)">
                    <input type="text" name="end_time" class=bk value="<%=time %>" size=8>
               </td>
            </tr>
            <tr bgcolor="#FFFFFF">
               <td width="15%" align='right'>
                   �û�����
               </td>
               <td width="35%">
                  <INPUT TYPE="text" NAME="area_name" id="area_name" value="" readOnly class="bk"  onclick="areaSelect();" onchange="showChild();">&nbsp;<font color="#FF0000">*</font>
				  <INPUT TYPE="hidden" NAME="area_id" id="area_id" value="-1"><nobr class="BT" onmouseover="this.className='BTOver'" onmouseout="this.className='BT'" onclick="areaSelect();"><IMG SRC="images/search.gif" WIDTH="15" HEIGHT="12" BORDER="0" ALT="ѡ������" valign="middle">&nbsp;ѡ��</nobr>
               </td>
               <td width="15%" align='right'>
                   �û���
               </td>
               <td width="35%">
                   <span id ="userSpan"><select name ="userSelect" class="bk"><option value="-1">��ѡ���û�����</option></select></span>
               </td>
            </tr>
            <tr bgcolor="#FFFFFF">
               <td width="15%" align='right'>
                   ��־���ͣ�
               </td>
               <td width="35%">
                  <!-- <select name="logtype" class="bk" onchange="getOperName(this)"> -->
                 <select name="logtype" class="bk">
                     <option value="1" selected>ϵͳ������־</option>
                     <%
                         if(!"sx_lt".equals(InstArea) && !"jl_lt".equals(InstArea)){
                     %>
                     <option value="2" >�豸������־</option>
                     <option value="3" >������־</option>
                     <option value="4" >�ӿ���־</option>
                     <%}%>
                 </select>
              </td>
              <td width="15%" align='right'>
                  �������ƣ�
              </td>
              <td width="35%">
                 <select name=operation_nameSelect class="bk">
                      <option value="-1" selected>��ѡ��</option>
                     <option value="1" >��ѯ</option>
                     <option value="2" >����</option>
                     <option value="3" >���</option>
                     <option value="4" >��¼</option>
                     <option value="0" >����</option>
                 </select>
              </td>
           </tr>
          <%
               if("jl_lt".equals(InstArea)){
          %>
          <tr bgcolor="#FFFFFF">
                <td width="15%" align='right'>
                   	����ʽ��
               </td>
               <td width="35%">
                 <select name="ordertype" class="bk">
                     <option value='1' selected>����</option>
                     <option value='2'>����</option>
                  </select>
               </td> 
                <td width="15%" align='right'>
                	
                </td>
                <td width="35%">
                	<input type="hidden" name="device_serialnumber" value="" class="bk" size=25> 
                </td>                       
            </tr>
          <%
               } else {
          %>
           <tr bgcolor="#FFFFFF">
           
               <td width="15%" align='right'>
                  �豸���кţ�
              </td>
              <td width="35%">
                <!-- <input type="text" name="device_oui" value="" class="bk" size=6>&nbsp;-&nbsp; --><input type="text" name="device_serialnumber" value="" class="bk" size=25>&nbsp;֧��ģ��ƥ��
               </td>
                
                <td width="15%" align='right'>
                   ����ʽ��
               </td>
               <td width="35%">
                 <select name="ordertype" class="bk">
                     <option value='1' selected>����</option>
                     <option value='2'>����</option>
                  </select>
               </td>                            
            </tr>
            <%
               }
          	%>
			<TR BGCOLOR=#ffffff > 
			  <TD align=right class=column>���ݱ���</TD>
			  <TD colspan="">
				<INPUT name="tab_name" type="text" class="bk" value=""  size=25>&nbsp;<font color="#FF0000">*</font>
			  </TD>
			 <TD align=right class=column colspan=2></TD>
			</TR>
            <tr bgcolor="#FFFFFF">
                  <td colspan=4 class=green_foot align=right>
                    <input TYPE="submit" name="cmdOK" value=" �� ѯ ">&nbsp;&nbsp;
					<INPUT TYPE="button" VALUE=" �� �� " ONCLICK="javascript:doBak();">
                  </td>
            </tr>
        </TABLE>
       </TD>
    </TR>    
</table> 
<IFRAME name="childFrm" ID=childFrm SRC="" STYLE="display:none;width:500;height:200"></IFRAME>	
</FORM>
<BR><BR>


<SCRIPT LANGUAGE="JavaScript">
	document.frm.tab_name.value='log_' + '<%=acc_oid%>' + "_" + (new Date()).getTime();
	
	//20200506 ɽ����ͨ �û�����Ĭ��ѡ��admin.com����һ����
	var defaultAreaId='<%=defaultAreaId%>'
	var defaultAreaName='<%=defaultAreaName%>'
	if(defaultAreaId != ''){
		$("#area_id").val(defaultAreaId);
	}
	if(defaultAreaName != ''){
		$("#area_name").val(defaultAreaName);
		showChild();
	}
	
</SCRIPT>
<%@ include file="../foot.jsp"%> 
            