
<SCRIPT LANGUAGE="JavaScript">
<!--

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

function CheckForm()
{

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

		if (frm.send_warn1.selectedIndex==0) 
		{
			window.alert("û��ѡ��̶���ֵһ�����澯�ļ�����ѡ��");
			return false;
		}

		if (frm.renew_warn1.selectedIndex==0)
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

		if (frm.send_warn2.selectedIndex==0) 
		{
			window.alert("û��ѡ��̶���ֵ�������澯�ļ�����ѡ��");
			return false;
		}

		if (frm.renew_warn2.selectedIndex==0) 
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

		if (frm.sdynamic_send_warn.selectedIndex==0) 
		{
			window.alert("û��ѡ��̬��ֵ�����澯�ļ�����ѡ��");
			return false;
		}
 
		if (frm.sdynamic_renew_warn.selectedIndex==0)
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
		
		if(frm.send_warn3.selectedIndex==0) {
			window.alert("û��ѡ��ͻ�䷧ֵ�������澯����");
			return false;
		}
	}
	else {
		frm.overstep_Percent.value = "0";
		frm.achieve_Percent3.value = "1";

	}
	frm.action="pm_EditInstance.jsp";
	frm.submit();
	iTimerID = window.setInterval("CallPro()",1000);
}

//-->
</SCRIPT>

<%
	String str_mintype = (String)map_instance.get("mintype");
	int mintype = Integer.parseInt(str_mintype.trim());
	String minthres = (String)map_instance.get("minthres");
	String mindesc = (String)map_instance.get("mindesc");
	String mincount = (String)map_instance.get("mincount");
	String str_minwarninglevel = (String)map_instance.get("minwarninglevel");
	int minwarninglevel = Integer.parseInt(str_minwarninglevel.trim());
	String str_minreinstatelevel = (String)map_instance.get("minreinstatelevel");
	int minreinstatelevel = Integer.parseInt(str_minreinstatelevel.trim());
	String str_maxtype = (String)map_instance.get("maxtype");
	int maxtype = Integer.parseInt(str_maxtype.trim());
	String maxthres = (String)map_instance.get("maxthres");
	String maxdesc = (String)map_instance.get("maxdesc");
	String maxcount = (String)map_instance.get("maxcount");
	String str_maxwarninglevel = (String)map_instance.get("maxwarninglevel");
	int maxwarninglevel = Integer.parseInt(str_maxwarninglevel.trim());
	String str_maxreinstatelevel = (String)map_instance.get("maxreinstatelevel");
	int maxreinstatelevel = Integer.parseInt(str_maxreinstatelevel.trim());
	String str_dynatype = (String)map_instance.get("dynatype");
	int dynatype = Integer.parseInt(str_dynatype.trim());
	String beforeday = (String)map_instance.get("beforeday"); 
	String dynathres = (String)map_instance.get("dynathres");
	String dynacount = (String)map_instance.get("dynacount");
	String dynadesc = (String)map_instance.get("dynadesc");
	String str_dynawarninglevel = (String)map_instance.get("dynawarninglevel");
	int dynawarninglevel = Integer.parseInt(str_dynawarninglevel.trim());
	String str_dynareinstatelevel = (String)map_instance.get("dynareinstatelevel");
	int dynareinstatelevel = Integer.parseInt(str_dynareinstatelevel.trim());
	String str_mutationtype = (String)map_instance.get("mutationtype");
	int mutationtype = Integer.parseInt(str_mutationtype.trim());
	String mutationthres = (String)map_instance.get("mutationthres");
	String mutationcount = (String)map_instance.get("mutationcount");
	String mutationdesc = (String)map_instance.get("mutationdesc");
	String str_mutationwarninglevel = (String)map_instance.get("mutationwarninglevel");
	int mutationwarninglevel = Integer.parseInt(str_mutationwarninglevel.trim());
%>

<TABLE border=0 cellspacing=0 cellpadding=0 width="100%" align=center>
  <tr>
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
  
  <TR>
    <TD id="test1" style="display:" bgcolor=#000000>
		<table width="100%" border="0" align="center" cellpadding="2" cellspacing="1" valign="middle">
		  <tr>
			<td class=column1 align=center>�Ƚϲ�����һ</td>
			<td class=column2 align=left>
			 <select name="compSign_1">
				<%
					switch (mintype) {
						case 0: 
							out.println("<option value=0 selected>��ʹ��</option>");
							out.println("<option value=1>����</option>");
							out.println("<option value=2>���ڵ���</option>");
							out.println("<option value=3>С��</option>");
							out.println("<option value=4>С�ڵ���</option>");
							out.println("<option value=5>����</option>");
							out.println("<option value=6>������</option>");
							break;
						
						case 1: 
							out.println("<option value=0>��ʹ��</option>");
							out.println("<option value=1 selected>����</option>");
							out.println("<option value=2>���ڵ���</option>");
							out.println("<option value=3>С��</option>");
							out.println("<option value=4>С�ڵ���</option>");
							out.println("<option value=5>����</option>");
							out.println("<option value=6>������</option>");
							break;
						
						case 2:
							out.println("<option value=0>��ʹ��</option>");
							out.println("<option value=1>����</option>");
							out.println("<option value=2 selected>���ڵ���</option>");
							out.println("<option value=3>С��</option>");
							out.println("<option value=4>С�ڵ���</option>");
							out.println("<option value=5>����</option>");
							out.println("<option value=6>������</option>");
							break;
						
						case 3: 
							out.println("<option value=1>����</option>");
							out.println("<option value=2>���ڵ���</option>");
							out.println("<option value=3 selected>С��</option>");
							out.println("<option value=4>С�ڵ���</option>");
							out.println("<option value=5>����</option>");
							out.println("<option value=6>������</option>");
							break;
						
						case 4: 
							out.println("<option value=1>����</option>");
							out.println("<option value=2>���ڵ���</option>");
							out.println("<option value=3>С��</option>");
							out.println("<option value=4 selected>С�ڵ���</option>");
							out.println("<option value=5>����</option>");
							out.println("<option value=6>������</option>");
							break;
						
						case 5:
							out.println("<option value=1>����</option>");
							out.println("<option value=2>���ڵ���</option>");
							out.println("<option value=3>С��</option>");
							out.println("<option value=4>С�ڵ���</option>");
							out.println("<option value=5 selected>����</option>");
							out.println("<option value=6>������</option>");
							break;
						
						case 6: 
							out.println("<option value=1>����</option>");
							out.println("<option value=2>���ڵ���</option>");
							out.println("<option value=3>С��</option>");
							out.println("<option value=4>С�ڵ���</option>");
							out.println("<option value=5>����</option>");
							out.println("<option value=6 selected>������</option>");
							break;						
					}
				%>
            </select></td>
			<td class=column1 align=center>�̶���ֵһ</td>
			<td class=column2 align=left>
				<%
					out.println("<input name=fixedness_value1 type=text  style=width:180 value=" + minthres + "></td>");
				%>
		  </tr>
		  <tr>
			<td class=column1 align=center>�̶���ֵһ����</td>
			<td colspan="3" class=column1 align=left>
				<%
					out.println("<input name=fixedness_value1desc type=text style=width:503 value=" + mindesc + ">");
				%>
			</td>
		  </tr>
		  <tr>
			<td class=column1 align=center>����������ֵһ���Σ�</td>
			<td class=column2 align=left>
				<%
					out.println("<input name=seriesOverstep_value1 type=text  style=width:180 value=" + mincount + ">");
				%>
			</td>
			<td class=column1 align=left colspan="2">�������澯��</td>
		  </tr>
		  <tr>
			<td class=column1 align=center>�����澯����</td>
			<td class=column2 align=left>
			<select name="send_warn1">
				<%
					switch (minwarninglevel) {
						case 0: 
							out.println("<option value=0 selected>��ѡ��</option>");
							out.println("<option value=1>������־</option>");
							out.println("<option value=2>��ʾ�澯</option>");
							out.println("<option value=3>һ��澯</option>");
							out.println("<option value=4>���ظ澯</option>");
							out.println("<option value=5>�����澯</option>");
							break;
						
						case 1: 
							out.println("<option value=0>��ѡ��</option>");
							out.println("<option value=1 selected>������־</option>");
							out.println("<option value=2>��ʾ�澯</option>");
							out.println("<option value=3>һ��澯</option>");
							out.println("<option value=4>���ظ澯</option>");
							out.println("<option value=5>�����澯</option>");
							break;
						
						case 2: 
							out.println("<option value=0>��ѡ��</option>");
							out.println("<option value=1>������־</option>");
							out.println("<option value=2 selected>��ʾ�澯</option>");
							out.println("<option value=3>һ��澯</option>");
							out.println("<option value=4>���ظ澯</option>");
							out.println("<option value=5>�����澯</option>");
							break;
						
						case 3: 
							out.println("<option value=0>��ѡ��</option>");
							out.println("<option value=1>������־</option>");
							out.println("<option value=2>��ʾ�澯</option>");
							out.println("<option value=3 selected>һ��澯</option>");
							out.println("<option value=4>���ظ澯</option>");
							out.println("<option value=5>�����澯</option>");
							break;
						
						case 4: 
							out.println("<option value=0>��ѡ��</option>");
							out.println("<option value=1>������־</option>");
							out.println("<option value=2>��ʾ�澯</option>");
							out.println("<option value=3>һ��澯</option>");
							out.println("<option value=4 selected>���ظ澯</option>");
							out.println("<option value=5>�����澯</option>");
							break;
						
						case 5: 
							out.println("<option value=0>��ѡ��</option>");
							out.println("<option value=1>������־</option>");
							out.println("<option value=2>��ʾ�澯</option>");
							out.println("<option value=3>һ��澯</option>");
							out.println("<option value=4>���ظ澯</option>");
							out.println("<option value=5 selected>�����澯</option>");
							break;						
					}
				%>  
			</select></td>
			<td class=column1 align=center>�ָ��澯����</td>
			<td class=column2 align=left>
			<select name="renew_warn1">
				<%
					switch (minreinstatelevel) {
						case 0: 
							out.println("<option value=0 selected>��ѡ��</option>");
							out.println("<option value=1>������־</option>");
							out.println("<option value=2>��ʾ�澯</option>");
							out.println("<option value=3>һ��澯</option>");
							out.println("<option value=4>���ظ澯</option>");
							out.println("<option value=5>�����澯</option>");
							break;
						
						case 1: 
							out.println("<option value=0>��ѡ��</option>");
							out.println("<option value=1 selected>������־</option>");
							out.println("<option value=2>��ʾ�澯</option>");
							out.println("<option value=3>һ��澯</option>");
							out.println("<option value=4>���ظ澯</option>");
							out.println("<option value=5>�����澯</option>");
							break;
						
						case 2: 
							out.println("<option value=0>��ѡ��</option>");
							out.println("<option value=1>������־</option>");
							out.println("<option value=2 selected>��ʾ�澯</option>");
							out.println("<option value=3>һ��澯</option>");
							out.println("<option value=4>���ظ澯</option>");
							out.println("<option value=5>�����澯</option>");
							break;
						
						case 3: 
							out.println("<option value=0>��ѡ��</option>");
							out.println("<option value=1>������־</option>");
							out.println("<option value=2>��ʾ�澯</option>");
							out.println("<option value=3 selected>һ��澯</option>");
							out.println("<option value=4>���ظ澯</option>");
							out.println("<option value=5>�����澯</option>");
							break;
						
						case 4: 
							out.println("<option value=0>��ѡ��</option>");
							out.println("<option value=1>������־</option>");
							out.println("<option value=2>��ʾ�澯</option>");
							out.println("<option value=3>һ��澯</option>");
							out.println("<option value=4 selected>���ظ澯</option>");
							out.println("<option value=5>�����澯</option>");
							break;
						
						case 5:
							out.println("<option value=0>��ѡ��</option>");
							out.println("<option value=1>������־</option>");
							out.println("<option value=2>��ʾ�澯</option>");
							out.println("<option value=3>һ��澯</option>");
							out.println("<option value=4>���ظ澯</option>");
							out.println("<option value=5 selected>�����澯</option>");
							break;						
					}
				%>
			</select></td>
		  </tr>
		  <tr>
			<td class=column1 align=center>�Ƚϲ�������</td>
			<td class=column2 align=left>
			<select name="compSign_2">
				<%
					switch (maxtype) {
						case 0: 
							out.println("<option value=0 selected>��ʹ��</option>");
							out.println("<option value=1>����</option>");
							out.println("<option value=2>���ڵ���</option>");
							out.println("<option value=3>С��</option>");
							out.println("<option value=4>С�ڵ���</option>");
							out.println("<option value=5>����</option>");
							out.println("<option value=6>������</option>");
							break;
						
						case 1: 
							out.println("<option value=0>��ʹ��</option>");
							out.println("<option value=1 selected>����</option>");
							out.println("<option value=2>���ڵ���</option>");
							out.println("<option value=3>С��</option>");
							out.println("<option value=4>С�ڵ���</option>");
							out.println("<option value=5>����</option>");
							out.println("<option value=6>������</option>");
							break;
						
						case 2:
							out.println("<option value=0>��ʹ��</option>");
							out.println("<option value=1>����</option>");
							out.println("<option value=2 selected>���ڵ���</option>");
							out.println("<option value=3>С��</option>");
							out.println("<option value=4>С�ڵ���</option>");
							out.println("<option value=5>����</option>");
							out.println("<option value=6>������</option>");
							break;
						
						case 3: 
							out.println("<option value=1>����</option>");
							out.println("<option value=2>���ڵ���</option>");
							out.println("<option value=3 selected>С��</option>");
							out.println("<option value=4>С�ڵ���</option>");
							out.println("<option value=5>����</option>");
							out.println("<option value=6>������</option>");
							break;
						
						case 4: 
							out.println("<option value=1>����</option>");
							out.println("<option value=2>���ڵ���</option>");
							out.println("<option value=3>С��</option>");
							out.println("<option value=4 selected>С�ڵ���</option>");
							out.println("<option value=5>����</option>");
							out.println("<option value=6>������</option>");
							break;
						
						case 5: 
							out.println("<option value=1>����</option>");
							out.println("<option value=2>���ڵ���</option>");
							out.println("<option value=3>С��</option>");
							out.println("<option value=4>С�ڵ���</option>");
							out.println("<option value=5 selected>����</option>");
							out.println("<option value=6>������</option>");
							break;
						
						case 6: 
							out.println("<option value=1>����</option>");
							out.println("<option value=2>���ڵ���</option>");
							out.println("<option value=3>С��</option>");
							out.println("<option value=4>С�ڵ���</option>");
							out.println("<option value=5>����</option>");
							out.println("<option value=6 selected>������</option>");
							break;						
					}
				%>
              </select>
			</td>
			<td class=column1 align=center>�̶���ֵ��</td>
			<td class=column2 align=left>
				<%
					out.println("<input name=fixedness_value2 type=text style=width:180 value=" + maxthres + ">");
				%>
			</td>
		  </tr>
		  <tr>
			<td class=column1 align=center>�̶���ֵ������</td>
			<td colspan="3" class=column2 align=left>
				<%
					out.println("<input name=fixedness_value2desc type=text style=width:180 value=" + maxdesc + ">");
				%>
			</td>
		  </tr>
		  <tr>
			<td class=column1 align=center>����������ֵ�����Σ�</td>
			<td class=column2 align=left>
				<%
					out.println("<input name=seriesOverstep_value2 type=text style=width:180 value=" + maxcount + ">");
				%>
			</td>
			<td class=column1 align=left colspan="2">�������澯��</td>
		  </tr>
		  <tr>
			<td class=column1 align=center>�����澯����</td>
			<td class=column2 align=left>
			<select name="send_warn2">
				<%
					switch (maxwarninglevel) {
						case 0: 
							out.println("<option value=0 selected>��ѡ��</option>");
							out.println("<option value=1>������־</option>");
							out.println("<option value=2>��ʾ�澯</option>");
							out.println("<option value=3>һ��澯</option>");
							out.println("<option value=4>���ظ澯</option>");
							out.println("<option value=5>�����澯</option>");
							break;
						
						case 1: 
							out.println("<option value=0>��ѡ��</option>");
							out.println("<option value=1 selected>������־</option>");
							out.println("<option value=2>��ʾ�澯</option>");
							out.println("<option value=3>һ��澯</option>");
							out.println("<option value=4>���ظ澯</option>");
							out.println("<option value=5>�����澯</option>");
							break;
						
						case 2: 
							out.println("<option value=0>��ѡ��</option>");
							out.println("<option value=1>������־</option>");
							out.println("<option value=2 selected>��ʾ�澯</option>");
							out.println("<option value=3>һ��澯</option>");
							out.println("<option value=4>���ظ澯</option>");
							out.println("<option value=5>�����澯</option>");
							break;
						
						case 3: 
							out.println("<option value=0>��ѡ��</option>");
							out.println("<option value=1>������־</option>");
							out.println("<option value=2>��ʾ�澯</option>");
							out.println("<option value=3 selected>һ��澯</option>");
							out.println("<option value=4>���ظ澯</option>");
							out.println("<option value=5>�����澯</option>");
							break;
						
						case 4: 
							out.println("<option value=0>��ѡ��</option>");
							out.println("<option value=1>������־</option>");
							out.println("<option value=2>��ʾ�澯</option>");
							out.println("<option value=3>һ��澯</option>");
							out.println("<option value=4 selected>���ظ澯</option>");
							out.println("<option value=5>�����澯</option>");
							break;
						
						case 5: 
							out.println("<option value=0>��ѡ��</option>");
							out.println("<option value=1>������־</option>");
							out.println("<option value=2>��ʾ�澯</option>");
							out.println("<option value=3>һ��澯</option>");
							out.println("<option value=4>���ظ澯</option>");
							out.println("<option value=5 selected>�����澯</option>");
							break;							
					}
				%>
			</select>
			</td>
			<td class=column1 align=center>�ָ��澯����</td>
			<td class=column2 align=left>
			<select name="renew_warn2">
				<%
					switch (maxreinstatelevel) {
						case 0: 
							out.println("<option value=0 selected>��ѡ��</option>");
							out.println("<option value=1>������־</option>");
							out.println("<option value=2>��ʾ�澯</option>");
							out.println("<option value=3>һ��澯</option>");
							out.println("<option value=4>���ظ澯</option>");
							out.println("<option value=5>�����澯</option>");
							break;
						
						case 1: 
							out.println("<option value=0>��ѡ��</option>");
							out.println("<option value=1 selected>������־</option>");
							out.println("<option value=2>��ʾ�澯</option>");
							out.println("<option value=3>һ��澯</option>");
							out.println("<option value=4>���ظ澯</option>");
							out.println("<option value=5>�����澯</option>");
							break;
						
						case 2: 
							out.println("<option value=0>��ѡ��</option>");
							out.println("<option value=1>������־</option>");
							out.println("<option value=2 selected>��ʾ�澯</option>");
							out.println("<option value=3>һ��澯</option>");
							out.println("<option value=4>���ظ澯</option>");
							out.println("<option value=5>�����澯</option>");
							break;
						
						case 3: 
							out.println("<option value=0>��ѡ��</option>");
							out.println("<option value=1>������־</option>");
							out.println("<option value=2>��ʾ�澯</option>");
							out.println("<option value=3 selected>һ��澯</option>");
							out.println("<option value=4>���ظ澯</option>");
							out.println("<option value=5>�����澯</option>");
							break;
						
						case 4: 
							out.println("<option value=0>��ѡ��</option>");
							out.println("<option value=1>������־</option>");
							out.println("<option value=2>��ʾ�澯</option>");
							out.println("<option value=3>һ��澯</option>");
							out.println("<option value=4 selected>���ظ澯</option>");
							out.println("<option value=5>�����澯</option>");
							break;
						
						case 5: 
							out.println("<option value=0>��ѡ��</option>");
							out.println("<option value=1>������־</option>");
							out.println("<option value=2>��ʾ�澯</option>");
							out.println("<option value=3>һ��澯</option>");
							out.println("<option value=4>���ظ澯</option>");
							out.println("<option value=5 selected>�����澯</option>");
							break;						
					}
				%>
			</select>
			</td>
		  </tr>
		  <tr>
			<td colspan="4" class=blue_foot align="center"><input type="button" value=" �� �� " class="jianbian" onclick="javascript:CheckForm();">&nbsp;&nbsp;&nbsp;<input type="button" value=" �� �� " onClick="javascript:window.close();" class="jianbian">
			</td>
		  </tr>
		</table>
	</TD>
 </TR>

  <TR>
    <TD id="test2" style="display:none" bgcolor=#000000>
		<table width="100%"  border="0" cellspacing="1" cellpadding="2" valign="middle" align="center">
		  <tr>
			<td class=column1 align=center>��̬��ֵ������</td>
			<td class=column2 align=left>
			 <select name="dynamic_OperateSign">
				<%
					switch (dynatype) {
						case 0: 
							out.println("<option value=0 selected>��ʹ��</option>");
							out.println("<option value=1>����</option>");
							out.println("<option value=2>���ڵ���</option>");
							out.println("<option value=3>С��</option>");
							out.println("<option value=4>С�ڵ���</option>");
							out.println("<option value=5>����</option>");
							out.println("<option value=6>������</option>");
							break;
						
						case 1: 
							out.println("<option value=0>��ʹ��</option>");
							out.println("<option value=1 selected>����</option>");
							out.println("<option value=2>���ڵ���</option>");
							out.println("<option value=3>С��</option>");
							out.println("<option value=4>С�ڵ���</option>");
							out.println("<option value=5>����</option>");
							out.println("<option value=6>������</option>");
							break;
						
						case 2: 
							out.println("<option value=0>��ʹ��</option>");
							out.println("<option value=1>����</option>");
							out.println("<option value=2 selected>���ڵ���</option>");
							out.println("<option value=3>С��</option>");
							out.println("<option value=4>С�ڵ���</option>");
							out.println("<option value=5>����</option>");
							out.println("<option value=6>������</option>");
							break;
						
						case 3: 
							out.println("<option value=1>����</option>");
							out.println("<option value=2>���ڵ���</option>");
							out.println("<option value=3 selected>С��</option>");
							out.println("<option value=4>С�ڵ���</option>");
							out.println("<option value=5>����</option>");
							out.println("<option value=6>������</option>");
							break;
						
						case 4: 
							out.println("<option value=1>����</option>");
							out.println("<option value=2>���ڵ���</option>");
							out.println("<option value=3>С��</option>");
							out.println("<option value=4 selected>С�ڵ���</option>");
							out.println("<option value=5>����</option>");
							out.println("<option value=6>������</option>");
							break;
						
						case 5: 
							out.println("<option value=1>����</option>");
							out.println("<option value=2>���ڵ���</option>");
							out.println("<option value=3>С��</option>");
							out.println("<option value=4>С�ڵ���</option>");
							out.println("<option value=5 selected>����</option>");
							out.println("<option value=6>������</option>");
							break;
						
						case 6: 
							out.println("<option value=1>����</option>");
							out.println("<option value=2>���ڵ���</option>");
							out.println("<option value=3>С��</option>");
							out.println("<option value=4>С�ڵ���</option>");
							out.println("<option value=5>����</option>");
							out.println("<option value=6 selected>������</option>");
							break;						
					}
				%>
            </select>			
			</td>
			<td class=column1 align=center>���ݵĻ�׼ֵ��ǰ���죩</td>
			<td class=column2 align=left>
				<%
					out.println("<input name=benchmark_Value type=text style=width:150 value=" + beforeday + ">");
				%>
			</td>
		  </tr>
		  <tr>
			<td class=column1 align=center>��ֵ�ٷֱȣ�����</td>
			<td colspan="3" class=column2 align=left>
				<%
					out.println("<input name=valve_Percent type=text style=width:502 value=" + dynathres + ">");
				%>
			</td>
		  </tr>
		  <tr>
			<td class=column1 align=center>�ﵽ��ֵ�ٷֱȣ��Σ�</td>
			<td class=column2 align=left>
				<%
					out.println("<input name=achieve_Percent2 type=text style=width:150 value=" + dynacount + ">");
				%>			
			</td>
			<td colspan="2" class=column2 align=left>�������澯��</td>
		  </tr>
		  <tr>
			<td class=column1 align=center>��̬��ֵ����</td>
			<td colspan="3" class=column2 align=left>
				<%
					out.println("<input name=dynamic_Valve_desc type=text style=width:502 value=" + dynadesc + ">");
				%>			
			</td>
		  </tr>
		  <tr>
			<td class=column1 align=center>�����澯����</td>
			<td class=column2 align=left>
			<select name="sdynamic_send_warn">
				<%
					switch (dynawarninglevel) {
						case 0: 
							out.println("<option value=0 selected>��ѡ��</option>");
							out.println("<option value=1>������־</option>");
							out.println("<option value=2>��ʾ�澯</option>");
							out.println("<option value=3>һ��澯</option>");
							out.println("<option value=4>���ظ澯</option>");
							out.println("<option value=5>�����澯</option>");
							break;
						
						case 1: 
							out.println("<option value=0>��ѡ��</option>");
							out.println("<option value=1 selected>������־</option>");
							out.println("<option value=2>��ʾ�澯</option>");
							out.println("<option value=3>һ��澯</option>");
							out.println("<option value=4>���ظ澯</option>");
							out.println("<option value=5>�����澯</option>");
							break;
						
						case 2: 
							out.println("<option value=0>��ѡ��</option>");
							out.println("<option value=1>������־</option>");
							out.println("<option value=2 selected>��ʾ�澯</option>");
							out.println("<option value=3>һ��澯</option>");
							out.println("<option value=4>���ظ澯</option>");
							out.println("<option value=5>�����澯</option>");
							break;
						
						case 3: 
							out.println("<option value=0>��ѡ��</option>");
							out.println("<option value=1>������־</option>");
							out.println("<option value=2>��ʾ�澯</option>");
							out.println("<option value=3 selected>һ��澯</option>");
							out.println("<option value=4>���ظ澯</option>");
							out.println("<option value=5>�����澯</option>");
							break;
						
						case 4: 
							out.println("<option value=0>��ѡ��</option>");
							out.println("<option value=1>������־</option>");
							out.println("<option value=2>��ʾ�澯</option>");
							out.println("<option value=3>һ��澯</option>");
							out.println("<option value=4 selected>���ظ澯</option>");
							out.println("<option value=5>�����澯</option>");
							break;
						
						case 5: 
							out.println("<option value=0>��ѡ��</option>");
							out.println("<option value=1>������־</option>");
							out.println("<option value=2>��ʾ�澯</option>");
							out.println("<option value=3>һ��澯</option>");
							out.println("<option value=4>���ظ澯</option>");
							out.println("<option value=5 selected>�����澯</option>");
							break;							
					}
				%>			  
			</select>
			</td>
			<td class=column1 align=center>�ָ��澯����</td>
			<td class=column2 align=left>
			<select name="sdynamic_renew_warn">
				<%
					switch (dynareinstatelevel) {
						case 0: 
							out.println("<option value=0 selected>��ѡ��</option>");
							out.println("<option value=1>������־</option>");
							out.println("<option value=2>��ʾ�澯</option>");
							out.println("<option value=3>һ��澯</option>");
							out.println("<option value=4>���ظ澯</option>");
							out.println("<option value=5>�����澯</option>");
							break;
						
						case 1: 
							out.println("<option value=0>��ѡ��</option>");
							out.println("<option value=1 selected>������־</option>");
							out.println("<option value=2>��ʾ�澯</option>");
							out.println("<option value=3>һ��澯</option>");
							out.println("<option value=4>���ظ澯</option>");
							out.println("<option value=5>�����澯</option>");
							break;
						
						case 2: 
							out.println("<option value=0>��ѡ��</option>");
							out.println("<option value=1>������־</option>");
							out.println("<option value=2 selected>��ʾ�澯</option>");
							out.println("<option value=3>һ��澯</option>");
							out.println("<option value=4>���ظ澯</option>");
							out.println("<option value=5>�����澯</option>");
							break;
						
						case 3: 
							out.println("<option value=0>��ѡ��</option>");
							out.println("<option value=1>������־</option>");
							out.println("<option value=2>��ʾ�澯</option>");
							out.println("<option value=3 selected>һ��澯</option>");
							out.println("<option value=4>���ظ澯</option>");
							out.println("<option value=5>�����澯</option>");
							break;
						
						case 4: 
							out.println("<option value=0>��ѡ��</option>");
							out.println("<option value=1>������־</option>");
							out.println("<option value=2>��ʾ�澯</option>");
							out.println("<option value=3>һ��澯</option>");
							out.println("<option value=4 selected>���ظ澯</option>");
							out.println("<option value=5>�����澯</option>");
							break;
						
						case 5: 
							out.println("<option value=0>��ѡ��</option>");
							out.println("<option value=1>������־</option>");
							out.println("<option value=2>��ʾ�澯</option>");
							out.println("<option value=3>һ��澯</option>");
							out.println("<option value=4>���ظ澯</option>");
							out.println("<option value=5 selected>�����澯</option>");
							break;							
					}
				%>			  
			</select>
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

  <TR>
    <TD id="test3" style="display:none" bgcolor=#000000>
		<table width="100%"  border="0" cellspacing="1" cellpadding="2" valign="middle" align="center">
		  <tr>
			<td class=column1 align=center>ͻ�䷧ֵ������</td>
			<td class=column2 align=left>
			<select name="mutation_OperateSign">
				<%
					switch (mutationtype) {
						case 0:
							out.println("<option value=0 selected>��ʹ��</option>");
							out.println("<option value=1>����</option>");
							out.println("<option value=2>���ڵ���</option>");
							out.println("<option value=3>С��</option>");
							out.println("<option value=4>С�ڵ���</option>");
							out.println("<option value=5>����</option>");
							out.println("<option value=6>������</option>");
							break;
						
						case 1: 
							out.println("<option value=0>��ʹ��</option>");
							out.println("<option value=1 selected>����</option>");
							out.println("<option value=2>���ڵ���</option>");
							out.println("<option value=3>С��</option>");
							out.println("<option value=4>С�ڵ���</option>");
							out.println("<option value=5>����</option>");
							out.println("<option value=6>������</option>");
							break;
						
						case 2: 
							out.println("<option value=0>��ʹ��</option>");
							out.println("<option value=1>����</option>");
							out.println("<option value=2 selected>���ڵ���</option>");
							out.println("<option value=3>С��</option>");
							out.println("<option value=4>С�ڵ���</option>");
							out.println("<option value=5>����</option>");
							out.println("<option value=6>������</option>");
							break;
						
						case 3: 
							out.println("<option value=1>����</option>");
							out.println("<option value=2>���ڵ���</option>");
							out.println("<option value=3 selected>С��</option>");
							out.println("<option value=4>С�ڵ���</option>");
							out.println("<option value=5>����</option>");
							out.println("<option value=6>������</option>");
							break;
						
						case 4:
							out.println("<option value=1>����</option>");
							out.println("<option value=2>���ڵ���</option>");
							out.println("<option value=3>С��</option>");
							out.println("<option value=4 selected>С�ڵ���</option>");
							out.println("<option value=5>����</option>");
							out.println("<option value=6>������</option>");
							break;
						
						case 5: 
							out.println("<option value=1>����</option>");
							out.println("<option value=2>���ڵ���</option>");
							out.println("<option value=3>С��</option>");
							out.println("<option value=4>С�ڵ���</option>");
							out.println("<option value=5 selected>����</option>");
							out.println("<option value=6>������</option>");
							break;
						
						case 6: 
							out.println("<option value=1>����</option>");
							out.println("<option value=2>���ڵ���</option>");
							out.println("<option value=3>С��</option>");
							out.println("<option value=4>С�ڵ���</option>");
							out.println("<option value=5>����</option>");
							out.println("<option value=6 selected>������</option>");
							break;						
					}
				%>             
            </select>			
			</td>
			<td class=column1 align=center>�����ٷֱȣ�����</td>
			<td class=column2 align=left>
				<%
					out.println("<input name=overstep_Percent type=text style=width:165 value=" + mutationthres + ">");
				%>			
			</td>
		  </tr>
		  <tr>
			<td class=column1 align=center>�ﵽ��ֵ�ٷֱȣ��Σ�</td>
			<td class=column2 align=left>
				<%
					out.println("<input name=achieve_Percent3 type=text style=width:165 value=" + mutationcount + ">");
				%>
			
			</td>
			<td colspan="2" class=column2 align=left>�������澯��</td>
		  </tr>
		  <tr>
			<td class=column1 align=center>ͻ�䷧ֵ����</td>
			<td colspan="3" class=column2 align=left>
				<%
					out.println("<input name=mutation_Valve_desc type=text style=width:500 value=" + mutationdesc + ">");
				%>			
			</td>
		  </tr>
		  <tr>
			<td class=column1 align=center>�����澯����</td>
			<td colspan="3" class=column2 align=left>
			<select name="send_warn3">
				<%
					switch (mutationwarninglevel) {
						case 0: 
							out.println("<option value=0 selected>��ѡ��</option>");
							out.println("<option value=1>������־</option>");
							out.println("<option value=2>��ʾ�澯</option>");
							out.println("<option value=3>һ��澯</option>");
							out.println("<option value=4>���ظ澯</option>");
							out.println("<option value=5>�����澯</option>");
							break;
						
						case 1: 
							out.println("<option value=0>��ѡ��</option>");
							out.println("<option value=1 selected>������־</option>");
							out.println("<option value=2>��ʾ�澯</option>");
							out.println("<option value=3>һ��澯</option>");
							out.println("<option value=4>���ظ澯</option>");
							out.println("<option value=5>�����澯</option>");
							break;
						
						case 2: 
							out.println("<option value=0>��ѡ��</option>");
							out.println("<option value=1>������־</option>");
							out.println("<option value=2 selected>��ʾ�澯</option>");
							out.println("<option value=3>һ��澯</option>");
							out.println("<option value=4>���ظ澯</option>");
							out.println("<option value=5>�����澯</option>");
							break;
						
						case 3: 
							out.println("<option value=0>��ѡ��</option>");
							out.println("<option value=1>������־</option>");
							out.println("<option value=2>��ʾ�澯</option>");
							out.println("<option value=3 selected>һ��澯</option>");
							out.println("<option value=4>���ظ澯</option>");
							out.println("<option value=5>�����澯</option>");
							break;
						
						case 4: 
							out.println("<option value=0>��ѡ��</option>");
							out.println("<option value=1>������־</option>");
							out.println("<option value=2>��ʾ�澯</option>");
							out.println("<option value=3>һ��澯</option>");
							out.println("<option value=4 selected>���ظ澯</option>");
							out.println("<option value=5>�����澯</option>");
							break;
						
						case 5: 
							out.println("<option value=0>��ѡ��</option>");
							out.println("<option value=1>������־</option>");
							out.println("<option value=2>��ʾ�澯</option>");
							out.println("<option value=3>һ��澯</option>");
							out.println("<option value=4>���ظ澯</option>");
							out.println("<option value=5 selected>�����澯</option>");
							break;							
					}
				%>			
			</select>
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
