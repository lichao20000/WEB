<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%
request.setCharacterEncoding("GBK");

QueryPage qryp = new QueryPage();
Cursor cursor=null;
Map fields=null;
String strSQL="";
String strData = "";
String stroffset = request.getParameter("offset");
String sysname=request.getParameter("sys");

strSQL="select distinct tab_sys from tab_conf_table";
cursor=DataSetBean.getCursor(strSQL);
fields=cursor.getNext();
String sel="<option value=''>������ϵͳ</option>";
if(fields!=null)
{
	while(fields!=null)
	{
		String sys=(String) fields.get("tab_sys");
		String select="";
		if(sysname!=null && sysname.compareTo(sys)==0)
		{
			select="selected";
		}

		sel +="<option value="+sys+" "+select+">"+sys+"</option>";
		fields=cursor.getNext();
	}
}

//ѡ����ϵͳ
if(stroffset ==null)
{
	sysname=(sysname==null || sysname.compareTo("null")==0)?"":sysname;
	strSQL = "select a.tab_desc,a.tab_sys,a.tbname_type,a.tab_name,a.tab_type,b.isclear,b.date_type,b.date_value,b.remark from tab_conf_table a left outer join tab_data_clear b on a.tab_name=b.tab_name   where a.tab_sys like '%"+ sysname +"%' and a.tab_oper<>2 order by a.tab_sys,a.tab_type";
	session.putValue("sysSQL",strSQL);
}
//�����һҳ
else
{
	strSQL=(String)session.getValue("sysSQL");
	if (strSQL==null)
	{
		sysname=(sysname==null || sysname.compareTo("null")==0)?"":sysname;
		strSQL = "select a.tab_desc,a.tab_sys,a.tbname_type,a.tab_name,a.tab_type,b.isclear,b.date_type,b.date_value,b.remark from tab_conf_table a left outer join tab_data_clear b on a.tab_name=b.tab_name   where a.tab_sys like '%"+ sysname +"%' and a.tab_oper<>2 order by a.tab_sys,a.tab_type";
		session.putValue("sysSQL",strSQL);	
	}
}





HashMap tabType=new HashMap();
tabType.put("1","�̶�һ�ű�");
tabType.put("2","ÿ��һ�ű�");
tabType.put("3","ÿ��һ�ű�");
tabType.put("4","ÿ��һ�ű�");
tabType.put("5","ÿ��һ�ű�");
tabType.put("6","��������");


int pagelen = 15;
int offset;

if(stroffset == null|| stroffset.compareTo("null")==0) offset = 1;
else offset = Integer.parseInt(stroffset);

qryp.initPage(strSQL,offset,pagelen);
String strBar = qryp.getPageBar();

cursor = DataSetBean.getCursor(strSQL,offset,pagelen);
fields = cursor.getNext();

if(fields == null){
	strData = "<TR ><TD COLSPAN=5 HEIGHT=30 class=column>ϵͳû��������������Ϣ</TD></TR>";
}
else{	
	while(fields != null){
		String isclear=(String) fields.get("ISCLEAR");
		String isconf="<font color=red>δ����</font>";
		String clearData="";
		String strConf="����";
		
		if(isclear!=null && isclear.length()>0)
		{
			isconf="<font color=green>������</font>";
			strConf="�޸�����";
			if(Integer.parseInt(isclear)==0)
			{
				clearData +="|<a HREF=tab_clearSave.jsp?action=update&isclear=1&sys="+sysname+"&offset="+offset+"&tab_name="+ fields.get("TAB_NAME") +" onclick='return delWarn(1);'>��������</a>";
				isconf += "|<font color=red>δ����</font>";
			}
			else
			{
				clearData +="|<a HREF=tab_clearSave.jsp?action=update&isclear=0&sys="+sysname+"&offset="+offset+"&tab_name="+ fields.get("TAB_NAME") +" onclick='return delWarn(0);'>���ٹر�</a>";
				isconf += "|<font color=green>������</font>";
			}

		}
		strData += "<TR>";
		strData += "<TD class=column1 align=left>"+ (String)fields.get("tab_desc") + "</TD>";
		strData += "<TD class=column1 align=center>"+ (String)fields.get("tab_sys") + "</TD>";
		strData += "<TD class=column1 align=center>"+ tabType.get((String)fields.get("tab_type")) + "</TD>";
		strData += "<TD class=column2 align=center>"+ isconf + "</TD>";
		strData += "<TD class=column2 align=center><A HREF=\"javascript:Edit('"+fields.get("tab_desc")+"','"+fields.get("tab_sys")+"','"+fields.get("tbname_type")+"','"+fields.get("tab_name")+"','"+fields.get("tab_type")+"','"+fields.get("isclear")+"','"+fields.get("date_type")+"','"+fields.get("date_value")+"','"+ fields.get("remark")+"');\">"+strConf+"</A>"+clearData+"</TD>";
		strData += "</TR>";		
		fields = cursor.getNext();
	}
	strData += "<TR BGCOLOR=#FFFFFF><TD COLSPAN=5 align=right>" + strBar + "</TD></TR>";
}
tabType.clear();
%>
<%@ include file="../head.jsp"%>
<%@page import="com.linkage.litms.common.database.QueryPage"%>
<%@page import="com.linkage.litms.common.database.Cursor"%>
<%@page import="com.linkage.litms.common.database.DataSetBean"%>
<%@page import="java.util.HashMap"%>
<SCRIPT LANGUAGE="JavaScript">
<!--
	function Edit(_tab_desc,_tab_sys,_tbname_type,_tab_name,_tab_type,_isclear,_date_type,_date_value,_remark)
	{
		frm.tab_desc.value=_tab_desc;
		frm.tab_sys.value=_tab_sys;
		frm.tab_name.value=_tab_name;
		_date_value=(_date_value=="null" || _date_value.length==0)?"":_date_value;
		_date_type=(_date_type=="null" || _date_type.length==0)?0:_date_type;
		frm.date_value.value=_date_value;
		frm.remark.value=_remark;		
		document.frm.action.value="edit";
		actLabel.innerHTML = '�޸�';
		//����ǹ̶������������е�λ����ʾ
		if(parseInt(_tbname_type,10)==0)
		{
			var items=new Array("��","��","��","��");
			addOption(items,1);
		}
		//����Ƕ�̬����������Ҫ�����ж�
		else
		{
			switch(parseInt(_tab_type,10))
			{
				
				//ѡ������ձ�����ֻ������Ϊ��λ���б�������
				case 4:
				{
					var items=new Array("��");
					addOption(items,2);
					break;
				}
				//ѡ������ܱ�����ֻ������Ϊ��λ���б�������
				case 3:
				{
					var items=new Array("��");
					addOption(items,3);
					break;
				}
				//ѡ������±�����ֻ������Ϊ��λ���б�������
				case 5:
				{
					var items=new Array("��");
					addOption(items,1);
					break;
				}
				//ѡ�����Сʱ����ԭʼ���ݱ������������ʱ�����ĸ���λ��������
				default :
				{
					var items=new Array("��","��","��","��");
					addOption(items,1);
					break;
				}
			}
		}
		
		//alert(_date_type);
		for(var i=0;i<frm.date_type.options.length;i++)
		{
			if(frm.date_type.options[i].value==_date_type)
			{
				frm.date_type.options[i].selected=true;
				break;
			}
		
		}
		//frm.date_type.options[_date_type].selected=true;

		if(_isclear!=null && _isclear.length>0 && _isclear!='null' && parseInt(_isclear,10)==1)
		{
			frm.isclear.checked=true;
		}
		else
		{
			frm.isclear.checked=false;
		}

		//return false;
	}

	function delWarn(flag){
		var msg="���Ҫ�������������ã�";
		if(flag==0)
		{
			msg="���Ҫ���ٹرո����ã�";
		}
		if(confirm(msg)){
			return true;
		}
		else{
			return false;
		}
	}

	function CheckForm(){
		var obj = document.frm;
		var s=obj.date_type.selectedIndex;
		if(s==0)
		{
			window.alert("��ѡ�������ݵ�ʱ�䵥λ!");
			return false;
		}
		else if(!IsNull(obj.date_value.value,'������ʱ��'))
		{
			obj.date_value.focus();
			obj.date_value.select();
			return false;
		}
		else if(!IsNumber(obj.date_value.value,'����ʱ��'))
		{
			obj.date_value.focus();
			obj.date_value.select();
			return false;			
		}
		return true;

		
	}
	
	function removeOptionAll()
	{	
		var s=frm.date_type.options.length;
		for(var i=0;i<s;i++)
		{
			frm.date_type.remove(1);				
		}			
	}

	function addOption(items,index)
	{		
		removeOptionAll();
		var otype=frm.date_type;
		
		for(var i=0;i<items.length;i++)
		{
			var oOption = document.createElement("OPTION");
			otype.options.add(oOption);
			oOption.innerText = items[i];
			oOption.value = index;
			index++;
		}
		
	}

	function sysChange()
	{
		var s=frm.sys.selectedIndex;
		var sysName=frm.sys.options[s].value;
		var page="tab_clear.jsp?sys="+sysName;
		window.navigate(page);
	
	}



//-->
</SCRIPT>
<%@ include file="../toolbar.jsp"%>
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
<TR><TD HEIGHT=20>&nbsp;</TD></TR>
<TR><TD>
	<FORM NAME="frm" METHOD="post" ACTION="tab_clearSave.jsp" onsubmit="return CheckForm()">
	<TABLE width="90%" border=0 cellspacing=0 cellpadding=0 align="center">
		<TR>
			<TD bgcolor=#000000>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
					<TR>
						<TD bgcolor="#ffffff" colspan="5" >�����������Ϣ
						<select name="sys" class=bk onchange="javascript:sysChange();">
							<%=sel%>
						</select>
						</TD>
					</TR>
					<TR>
						<!-- <TD>�豸���</TD> -->
						<TH nowrap>��������</TH>						
						<TH nowrap>������ϵͳ</TH>
						<TH nowrap>������</TH>
						<TH nowrap>�������</TH>
						<TH nowrap>����</TH>
					</TR>
					<%=strData%>
				</TABLE>
			</TD>
		</TR>
	</TABLE>
	<BR>
	<TABLE width="90%" border=0 cellspacing=0 cellpadding=0 align="center">
		<TR>
			<TD bgcolor=#000000>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
					<TR>
						<TH colspan="4" align="center"><SPAN id="actLabel">���</SPAN><SPAN id="zoneLabel"></SPAN>��������Ϣ�޸�</TH>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right">����������</TD>
						<TD>
						<INPUT TYPE="text" NAME="tab_desc" maxlength=125 class=bk size=40  readonly>&nbsp;<font color="#FF0000">*</font></TD>
						<TD class=column align="right">������ϵͳ</TD>
						<TD>
						<INPUT TYPE="text" NAME="tab_sys" maxlength=125 class=bk size=40  readonly>&nbsp;<font color="#FF0000">*</font></TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right">�Ƿ���������</TD>
						<TD colspan=3>
							<input type=checkbox name="isclear" value=1 checked>�����Զ��������
						</TD>
											
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right">�������ݵ�ʱ�䵥λ</TD>
						<TD>
							<select name="date_type" class=bk>
								<option value=0>--��ѡ��--</option>
								
							</select>
						</TD>
						<TD class=column align="right">����ʱ��</TD>
						<TD>
						<INPUT TYPE="text" NAME="date_value" maxlength=125 class=bk size=40  >&nbsp;<font color="#FF0000">*</font></TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right">��ע</TD>
						<TD colspan=3>
							<INPUT TYPE="text" NAME="remark" maxlength=125 class=bk size=90>
						</TD>											
					</TR>
					<TR >
						<TD colspan="4" align="right" CLASS=foot>
							
							<INPUT TYPE="submit" value=" �� �� " class=btn>&nbsp;
							<INPUT TYPE="hidden" name="action" value="add">
							<INPUT TYPE="hidden" NAME="tab_name">
							<input type="hidden" name="offset" value=<%=offset%>>
							&nbsp;
							<INPUT TYPE="reset" value=" �� д " class=btn>
						</TD>
					</TR>
				</TABLE>
			</TD>
		</TR>
	</TABLE>
	<BR>
	<BR>
	<!--
	<TABLE width="90%" border=0 cellspacing=0 cellpadding=0 align="center">
		<TR>
			<TD bgcolor=#000000>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
					<TR bgcolor="#FFFFFF">
						<TD width="100%">
							<font color=red>˵��:</font>
							<br>
								<font color=blue>*</font> ���ڶ�̬���ɵı���,����$yyyy$��ʾ������$mm$��ʾ������$dd$��ʾ������$ww$��ʾ;
							<br>&nbsp;&nbsp;��:adsl_perf_data_year_month_day������adsl_perf_data_$yyyy$_$mm$_$day$��ʾ��
							<br>
								<font color=blue>*</font>
								ʱ������ɹ������������ɶ�̬���������������ơ�
							<br>&nbsp;&nbsp;��:adsl_perf_data_2004_12_7�ı������ɹ�����Ĭ�Ϲ���
							<br>&nbsp;&nbsp;��:adsl_perf_data_2004_12_07�ı������ɹ����ñ�׼�����ܱ�ı�������ʱ�������ɹ������ơ�
							
						</TD>
					</TR>
				</TABLE>
			</TD>
		</TR>
	</TABLE>
	-->
	</FORM>
</TD></TR>
<TR><TD HEIGHT=20>&nbsp;<IFRAME ID=childFrm SRC="" STYLE="display:none"></IFRAME></TD></TR>
</TABLE>
<%@ include file="../foot.jsp"%>
