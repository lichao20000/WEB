<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page import="java.util.Map,com.linkage.module.gwms.dao.gw.EventLevelLefDAO"%>
<%
String srcType = request.getParameter("type");
if(null==srcType)
{
	srcType = "";
}
EventLevelLefDAO eventLevelLefDAO=new EventLevelLefDAO();
Map warnMap = eventLevelLefDAO.getWarnLevel();
request.setAttribute("warnMap",warnMap);
%>
<SCRIPT LANGUAGE="JavaScript">
<!--

function CheckForm()
{
	if (frm.exp_name.selectedIndex==0)
	{
		window.alert("û��ѡ�����ܱ���ʽ����ѡ��");
		return false;
	}

	if (!IsNull(frm.samp_distance.value.trim(),"�������"))
	{
		return false;
	}	

	if (!IsNumber2(frm.samp_distance.value.trim(),"�����������ȷ��ʽ"))
	{
		return false;
	}

	if (frm.compSign_1.selectedIndex != 0) 
	{
		
		if (!IsNull(frm.fixedness_value1.value.trim(),"�����˹̶���ֵһ���̶���ֵһ��ֵ"))
		{
			return false;
		}

		if (!isFloatNumber(frm.fixedness_value1.value.trim(),"�̶���ֵһ��ֵ����ȷ��ʽ")) 
		{
			return false;
		}

		if (!IsNegative(frm.fixedness_value1.value.trim(),"�̶���ֵһ��ֵ����ȷ��ʽ")) {
			return false;	
		}
		
		if (!IsNull(frm.seriesOverstep_value1.value.trim(),"�����˹̶���ֵһ������������ֵһ�Ĵ���")) 
		{
			return false;
		}
		
		if (!IsNumber2(frm.seriesOverstep_value1.value.trim(),"����������ֵһ�Ĵ�������ȷ��ʽ"))
		{
			return false;
		}

		if (frm.send_warn1.selectedIndex==-1) 
		{
			window.alert("û��ѡ��̶���ֵһ�����澯�ļ�����ѡ��");
			return false;
		}

		if (frm.renew_warn1.selectedIndex==-1)
		{
			window.alert("û��ѡ��̶���ֵһ�ָ��澯�ļ�����ѡ��");
			return false;
		}
	}
	else {
		frm.fixedness_value1.value = "0";
		frm.seriesOverstep_value1.value = "1";
	}

	
	if (frm.compSign_2.selectedIndex != 0) 
	{
		
		if (!IsNull(frm.fixedness_value2.value.trim(),"�����˹̶���ֵ�����̶���ֵ����ֵ"))
		{
			return false;
		}

		if (!isFloatNumber(frm.fixedness_value2.value.trim(),"�̶���ֵ����ֵ����ȷ��ʽ"))
			{
			return false;
		}

		if (!IsNegative(frm.fixedness_value2.value.trim(),"�̶���ֵ����ֵ����ȷ��ʽ")) {
			return false;	
		}

		if (!IsNull(frm.seriesOverstep_value2.value.trim(),"�����˹̶���ֵ��������������ֵ���Ĵ���"))
		{
			return false;
		}

		if (!IsNumber2(frm.seriesOverstep_value2.value.trim(),"����������ֵ���Ĵ�����ȷ��ʽ"))
		{
			return false;
		}

		if (frm.send_warn2.selectedIndex==-1) 
		{
			window.alert("û��ѡ��̶���ֵ�������澯�ļ�����ѡ��");
			return false;
		}

		if (frm.renew_warn2.selectedIndex==-1) 
		{
			window.alert("û��ѡ��̶���ֵ���ָ��澯�ļ�����ѡ��");
			return false;
		}
	}
	else {
		frm.fixedness_value2.value = "0";
		frm.seriesOverstep_value2.value = "1";

	}

	if (frm.dynamic_OperateSign.selectedIndex != 0)
    {

		if (!IsNull(frm.benchmark_Value.value.trim(),"�����˶�̬��ֵ��ǰ���������Ϊ��׼ֵ��ֵ"))
		{
			return false;
		}

		if (!IsNumber2(frm.benchmark_Value.value.trim(),"��̬��ֵǰ���������Ϊ��׼ֵ��ֵ����ȷ��ʽ")) 
		{
			return false;
		}
		
		if (!IsNull(frm.valve_Percent.value.trim(),"�����˶�̬��ֵ����̬��ֵ��ֵ"))
		{
			return false;
		}

		if (!isFloatNumber(frm.valve_Percent.value.trim(),"��̬��ֵ�ĵ�ֵ����ȷ��ʽ"))
		{
			return false;
		}

		if (!IsNegative(frm.valve_Percent.value.trim(),"��̬��ֵ��ֵ����ȷ��ʽ")) {
			return false;	
		}
		
		if (!IsNull(frm.achieve_Percent2.value.trim(),"�����˶�̬��ֵ������������̬��ֵ�Ĵ���"))
		{
			return false;
		}

		if (!IsNumber2(frm.achieve_Percent2.value.trim(),"����������̬��ֵ�Ĵ�������ȷ��ʽ"))
		{
			return false;
		}

		if (frm.sdynamic_send_warn.selectedIndex==-1) 
		{
			window.alert("û��ѡ��̬��ֵ�����澯�ļ�����ѡ��");
			return false;
		}
 
		if (frm.sdynamic_renew_warn.selectedIndex==-1)
		{
			window.alert("û��ѡ��̬��ֵ�ָ��澯�ļ�����ѡ��");
			return false;
		}
	}
	else {
		frm.benchmark_Value.value = "1";
		frm.valve_Percent.value = "0";
		frm.achieve_Percent2.value = "1";
	}

	if (frm.mutation_OperateSign.selectedIndex != 0)
	{
		
		if (!IsNull(frm.overstep_Percent.value.trim(),"������ͻ�䷧ֵ��ͻ�䷧ֵ��ֵ"))
		{
			return false;
		}

		if (!isFloatNumber(frm.overstep_Percent.value.trim(),"ͻ�䷧ֵ��ֵ����ȷ��ʽ"))
		{
			return false;
		}

		if (!IsNegative(frm.overstep_Percent.value.trim(),"ͻ�䷧ֵ��ֵ����ȷ��ʽ")) {
			return false;	
		}

		if (!IsNull(frm.achieve_Percent3.value.trim(),"������ͻ�䷧ֵ,��������ͻ�䷧ֵ�Ĵ���"))
		{
			return false;
		}

		if (!IsNumber2(frm.achieve_Percent3.value.trim(),"��������ͻ�䷧ֵ�Ĵ�������ȷ��ʽ"))
		{
			return false;
		}
		
		if(frm.send_warn3.selectedIndex==-1) {
			window.alert("û��ѡ��ͻ�䷧ֵ�������澯����");
			return false;
		}
	}
	else {
		frm.overstep_Percent.value = "0";
		frm.achieve_Percent3.value = "1";

	}
	
	//var obj = document.frm;
	var hguser=document.all("hguser").value;
    var telephone= document.all("telephone").value;
    var checkradios = document.all("checkType");
    var checkType = "";
    for(var i=0;i<checkradios.length;i++)
    {
      if(checkradios[i].checked)
	  {
	    checkType = checkradios[i].value;
	    break;
	  }
    }
   	if(checkType==0&&document.frm.city_id.value == -1){
			alert("��ѡ�����أ�");
			document.frm.city_id.focus();
			return false;
	}
  	if(checkType==0&&document.frm.vendor_id.value == -1){
		alert("��ѡ���̣�");
		document.frm.vendor_id.focus();
		return false;
 	}
 	if(checkType==0&&document.frm.devicetype_id.value == -1){
		alert("��ѡ���豸�ͺţ�");
		document.frm.devicetype_id.focus();
		return false;
 	}
 	if(checkType==0&&document.frm.softwareversion.value == -1){
			alert("��ѡ���豸�汾��");
			document.frm.softwareversion.focus();
			return false;
		}
		
	if(checkType==1&&""==hguser&&""==telephone)
	{
	  alert("����д�û�����绰���룡");
	  document.all("hguser").focus();
	  return false;
	}
	
	else if(!CheckChkBox())
     {
	  alert("��ѡ������豸��");
	  return false;
    } 

	frm.action="testPmIsBeing.jsp";
	frm.submit();
	
	
		
}
function IsNegative(strValue,strMsg) 
{
	var bz = true;
	var index = strValue.indexOf("-");

	if (index == 0)
		bz = false;
	
	if(!bz)
		alert(strMsg+"ӦΪ����");

	return bz;
}


function showpage(uniformid)
{
	var page="sys_report_searchForm.jsp?querytype="+uniformid;
	switch (uniformid)
	{
		case 1:
		{
			document.all("td1").className="button_onblue";
			document.all("td2").className="button_outblue";
			document.all("td3").className="button_outblue";
			document.all("test1").style.display="";
			document.all("test2").style.display="none";
			document.all("test3").style.display="none";

			break;
		}
		case 2:
		{
			document.all("td1").className="button_outblue";
			document.all("td2").className="button_onblue";
			document.all("td3").className="button_outblue";
			document.all("test1").style.display="none";
			document.all("test2").style.display="";
			document.all("test3").style.display="none";

			break;
		}	
		case 3:
		{
			document.all("td1").className="button_outblue";
			document.all("td2").className="button_outblue";
			document.all("td3").className="button_onblue";
			document.all("test1").style.display="none";
			document.all("test2").style.display="none";
			document.all("test3").style.display="";

			break;
		}	
	}
	
}
//-->
</SCRIPT>
<form>
<input type="hidden" name="type" value='<%=srcType %>'>
<TABLE border=0 cellspacing=0 cellpadding=0 width="98%" align=center>
  <tr style="display:none">
    <TD class="column1"> 
      <table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" valign="middle">
        <tr> 
          <TH width="159" height="25" class="button_onblue" id="td1" onClick="location.href='javascript:showpage(1);'">�̶���ֵ����</TH>
          <TH width="159" height="25" class="button_outblue" id="td2" onClick="location.href='javascript:showpage(2);'">��̬��ֵ����</TH>
          <TH width="159" height="25" class="button_outblue" id="td3" onClick="location.href='javascript:showpage(3);'">ͻ�䷧ֵ����</TH>
          <td align="left"></td>
        </tr>
        <tr> 
          <td height="3" colspan="4" align="center" class="blue_tag_line"></td>
        </tr>
      </table>
    </TD>
  </tr>
  
  <TR style="display:none">
    <TD id="test1" style="display:" bgcolor=#999999>
		<table width="100%" border="0" align="center" cellpadding="2" cellspacing="1" valign="middle">
		  <tr>
			<td class=column1 align=center>�Ƚϲ�����һ</td>
			<td class=column2 align=left>
			 <select name="compSign_1">
              <option value="0" selected>��ʹ��</option>
              <option value="1">����</option>
              <option value="2">���ڵ���</option>
              <option value="3">С��</option>
              <option value="4">С�ڵ���</option>
              <option value="5">����</option>
              <option value="6">������</option>
            </select></td>
			<td class=column1 align=center>�̶���ֵһ</td>
			<td class=column2 align=left><input name="fixedness_value1" type="text"  style="width:180"></td>
		  </tr>
		  <tr>
			<td class=column1 align=center>�̶���ֵһ����</td>
			<td colspan="3" class=column1 align=left><input name="fixedness_value1desc" type="text" style="width:503"></td>
		  </tr>
		  <tr>
			<td class=column1 align=center>����������ֵһ���Σ�</td>
			<td class=column2 align=left><input name="seriesOverstep_value1" type="text"  style="width:180" value=1></td>
			<td class=column1 align=left colspan="2">�������澯��</td>
		  </tr>
		  <tr>
			<td class=column1 align=center>�����澯����</td>
			<td class=column2 align=left>
			<!--<select name="send_warn1">
			  <option value="0" selected>��ѡ��</option>
			  <option value="1">������־</option>
			  <option value="2">��ʾ�澯</option>
			  <option value="3">һ��澯</option>
			  <option value="4">���ظ澯</option>
			  <option value="5">�����澯</option>
			</select>-->
			<s:select list="#request.warnMap" listKey="key" listValue="value" name="send_warn1" theme="simple" headerKey="-1" headerValue="��ѡ��" value="-1"/>
			</td>
			<td class=column1 align=center>�ָ��澯����</td>
			<td class=column2 align=left>
			<!--<select name="renew_warn1">
			  <option value="0" selected>��ѡ��</option>
			  <option value="1">������־</option>
			  <option value="2">��ʾ�澯</option>
			  <option value="3">һ��澯</option>
			  <option value="4">���ظ澯</option>
			  <option value="5">�����澯</option>
			</select>-->
			<s:select list="#request.warnMap" listKey="key" listValue="value" name="renew_warn1" theme="simple" headerKey="-1" headerValue="��ѡ��"  value="-1"/>
			</td>
		  </tr>
		  <tr>
			<td class=column1 align=center>�Ƚϲ�������</td>
			<td class=column2 align=left>
			<select name="compSign_2">
              <option value="0" selected>��ʹ��</option>
              <option value="1">����</option>
              <option value="2">���ڵ���</option>
              <option value="3">С��</option>
              <option value="4">С�ڵ���</option>
              <option value="5">����</option>
              <option value="6">������</option>
            </select>
			</td>
			<td class=column1 align=center>�̶���ֵ��</td>
			<td class=column2 align=left><input name="fixedness_value2" type="text" style="width:180"></td>
		  </tr>
		  <tr>
			<td class=column1 align=center>�̶���ֵ������</td>
			<td colspan="3" class=column2 align=left><input name="fixedness_value2desc" type="text" style="width:180"></td>
		  </tr>
		  <tr>
			<td class=column1 align=center>����������ֵ�����Σ�</td>
			<td class=column2 align=left><input name="seriesOverstep_value2" type="text" style="width:180" value=1></td>
			<td class=column1 align=left colspan="2">�������澯��</td>
		  </tr>
		  <tr>
			<td class=column1 align=center>�����澯����</td>
			<td class=column2 align=left>
			<!--<select name="send_warn2">
			  <option value="0" selected>��ѡ��</option>
			  <option value="1" >������־</option>
			  <option value="2">��ʾ�澯</option>
			  <option value="3">һ��澯</option>
			  <option value="4">���ظ澯</option>
			  <option value="5">�����澯</option>
			</select>-->
			<s:select list="#request.warnMap" listKey="key" listValue="value" name="send_warn2" theme="simple" headerKey="-1" headerValue="��ѡ��"  value="-1"/>
			</td>
			<td class=column1 align=center>�ָ��澯����</td>
			<td class=column2 align=left>
			<!--<select name="renew_warn2">
			  <option value="0" selected>��ѡ��</option>
			  <option value="1">������־</option>
			  <option value="2">��ʾ�澯</option>
			  <option value="3">һ��澯</option>
			  <option value="4">���ظ澯</option>
			  <option value="5">�����澯</option>
			</select>-->
			<s:select list="#request.warnMap" listKey="key" listValue="value" name="renew_warn2" theme="simple" headerKey="-1" headerValue="��ѡ��"  value="-1"/>
			</td>
		  </tr>
		 
		</table>
	</TD>
 </TR>
 
	<tr>
		<td colspan="4" align="center"><input type="button" value=" �� �� " onclick="javascript:CheckForm();">&nbsp;&nbsp;&nbsp;<input type="button" value=" �� �� " onClick="javascript:window.close();">
		</td>
	 </tr>
	 
	 
  <TR style="display:none">
    <TD id="test2" style="display:none" bgcolor=#000000>
		<table width="100%"  border="0" cellspacing="1" cellpadding="2" valign="middle" align="center">
		  <tr>
			<td class=column1 align=center>��̬��ֵ������</td>
			<td class=column2 align=left>
			 <select name="dynamic_OperateSign">
              <option value="0" selected>��ʹ��</option>
              <option value="1">�仯�ʴ���</option>
              <option value="2">�仯�ʴ��ڵ���</option>
              <option value="3">�仯��С��</option>
              <option value="4">�仯��С�ڵ���</option>
              <option value="5">�仯�ʵ���</option>
              <option value="6">�仯�ʲ�����</option>
            </select>			
			</td>
			<td class=column1 align=center>���ݵĻ�׼ֵ��ǰ���죩</td>
			<td class=column2 align=left><input name="benchmark_Value" type="text" style="width:150" value=1></td>
		  </tr>
		  <tr>
			<td class=column1 align=center>��ֵ�ٷֱȣ�����</td>
			<td colspan="3" class=column2 align=left><input name="valve_Percent" type="text" style="width:502"></td>
		  </tr>
		  <tr>
			<td class=column1 align=center>�ﵽ��ֵ�ٷֱȣ��Σ�</td>
			<td class=column2 align=left><input name="achieve_Percent2" type="text" style="width:150" value=1></td>
			<td colspan="2" class=column2 align=left>�������澯��</td>
		  </tr>
		  <tr>
			<td class=column1 align=center>��̬��ֵ����</td>
			<td colspan="3" class=column2 align=left><input name="dynamic_Valve_desc" type="text" style="width:502"></td>
		  </tr>
		  <tr>
			<td class=column1 align=center>�����澯����</td>
			<td class=column2 align=left>
			<!--<select name="sdynamic_send_warn">
			  <option value="0" selected>��ѡ��</option>
			  <option value="1" >������־</option>
			  <option value="2">��ʾ�澯</option>
			  <option value="3">һ��澯</option>
			  <option value="4">���ظ澯</option>
			  <option value="5">�����澯</option>
			</select>-->
			<s:select list="#request.warnMap" listKey="key" listValue="value" name="sdynamic_send_warn" theme="simple" headerKey="-1" headerValue="��ѡ��"  value="-1"/>
			</td>
			<td class=column1 align=center>�ָ��澯����</td>
			<td class=column2 align=left>
			<!--<select name="sdynamic_renew_warn">
			  <option value="0" selected>��ѡ��</option>
			  <option value="1" >������־</option>
			  <option value="2">��ʾ�澯</option>
			  <option value="3">һ��澯</option>
			  <option value="4">���ظ澯</option>
			  <option value="5">�����澯</option>
			</select>-->
			<s:select list="#request.warnMap" listKey="key" listValue="value" name="sdynamic_renew_warn" theme="simple" headerKey="-1" headerValue="��ѡ��"  value="-1"/>
			</td>
		  </tr>
		  <tr>
			<td colspan="4" class=column1 align=left>��ע��&nbsp;&nbsp;�仯�� �� �����ɵ���ֵ����׼ֵ��/��׼ֵ�� * 100</td>
		  </tr>
		   <tr>
			<td colspan="4" class=blue_foot align="center"><input type="button" value=" �� �� " class="jianbian" onclick="javascript:CheckForm();">&nbsp;&nbsp;&nbsp;<input type="button" value=" �� �� " onClick="javascript:window.close();" class="jianbian">
			</td>
		  </tr>
		</table>

	</TD>
 </TR>

  <TR style="display:none">
    <TD id="test3" style="display:none" bgcolor=#000000>
		<table width="100%"  border="0" cellspacing="1" cellpadding="2" valign="middle" align="center">
		  <tr>
			<td class=column1 align=center>ͻ�䷧ֵ������</td>
			<td class=column2 align=left>
			<select name="mutation_OperateSign">
              <option value="0" selected>��ʹ��</option>
              <option value="1">�仯�ʾ���ֵ����</option>
              <option value="2">�仯�ʾ���ֵ���ڵ���</option>
              <option value="3">�仯�ʾ���ֵС��</option>
              <option value="4">�仯�ʾ���ֵС�ڵ���</option>
              <option value="5">�仯�ʾ���ֵ����</option>
              <option value="6">�仯�ʾ���ֵ������</option>
            </select>			
			</td>
			<td class=column1 align=center>�����ٷֱȣ�����</td>
			<td class=column2 align=left><input name="overstep_Percent" type="text" style="width:165"></td>
		  </tr>
		  <tr>
			<td class=column1 align=center>�ﵽ��ֵ�ٷֱȣ��Σ�</td>
			<td class=column2 align=left><input name="achieve_Percent3" type="text" style="width:165" value=1></td>
			<td colspan="2" class=column2 align=left>�������澯��</td>
		  </tr>
		  <tr>
			<td class=column1 align=center>ͻ�䷧ֵ����</td>
			<td colspan="3" class=column2 align=left><input name="mutation_Valve_desc" type="text" style="width:500"></td>
		  </tr>
		  <tr>
			<td class=column1 align=center>�����澯����</td>
			<td colspan="3" class=column2 align=left>
			<!--<select name="send_warn3">
			  <option value="0" selected>��ѡ��</option>
			  <option value="1" >������־</option>
			  <option value="2">��ʾ�澯</option>
			  <option value="3">һ��澯</option>
			  <option value="4">���ظ澯</option>
			  <option value="5">�����澯</option>
			</select>-->
			<s:select list="#request.warnMap" listKey="key" listValue="value" name="send_warn3" theme="simple" headerKey="-1" headerValue="��ѡ��"  value="-1"/>
			</td>
		  </tr>
		  <tr>
			<td colspan="4" class=column1 align=left>��ע��&nbsp;&nbsp;�仯�ʾ���ֵ �� |�����ɵ���ֵ���ϴβɼ�ֵ��/�ϴβɼ�ֵ�� * 100|</td>
		  </tr>
		   <tr>
			<td colspan="4" class=blue_foot align="center"><input type="button" value=" �� �� " class="jianbian" onclick="javascript:CheckForm();">&nbsp;&nbsp;&nbsp;<input type="button" value=" �� �� " onClick="javascript:window.close();" class="jianbian">
			</td>
		  </tr>
		</table>

	</TD>
 </TR> 
</TABLE>
</form>