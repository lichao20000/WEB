<%--
FileName	: IpLineSave.jsp
Author		: liuli
Date		: 2007-3-06
Note		: �ʺ����ӡ��޸ġ�ɾ������
--%>
<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.linkage.litms.fault.Eventcobe" %>
<%
request.setCharacterEncoding("GBK");
String strMsg = null;
String strSQL=null;
String strAction = request.getParameter("action");
String[]  str_hid_allwarn=null;
if(strAction.equals("delete")){
	//ɾ������
	str_hid_allwarn = request.getParameterValues("Mycheckbox");

	for(int i=0;i<str_hid_allwarn.length;i++){
		String[] str_value = str_hid_allwarn[i].split("&");
		
		if(strSQL ==null){
			strSQL =" delete from fault_filter_multi where rule_id="+ str_value[0]+" ";	
		}else{
			strSQL += "  delete from fault_filter_multi  where rule_id="+ str_value[0]+" ";
		}
	}
	
}

if(strMsg==null || strSQL!=null){
	int iCode[] = DataSetBean.doBatch(strSQL);
	if(iCode != null && iCode.length> 0){		
		strMsg = "�澯���ع���ɾ���ɹ���";
		for(int i=0;i<str_hid_allwarn.length;i++){
			String[] str_value = str_hid_allwarn[i].split("&");			
			String[] gather_id = str_value[1].split(",");
			for (int j = 0; j < gather_id.length; j++) 
			{		              
				Eventcobe eventcobe = new Eventcobe(gather_id[j]);
				try {
					if(str_value[2].equals("1")){
						eventcobe.filterRuleDelete(str_value[0]);
						strMsg = "�澯���ع���ɾ���ɹ�,֪ͨ��̨�ɹ���";
					}
				} catch (Exception ex) {
					ex.printStackTrace();
					strMsg = "�澯���ع���ɾ���ɹ�,֪ͨ��̨ʧ�ܣ�";
					eventcobe.clearService();
				}
			}
		}
	}else{
		strMsg = "�澯���ع���ɾ��ʧ�ܣ��뷵�����Ի��Ժ����ԣ�";
    }
}
%>
<%@ include file="../head.jsp"%>
<SCRIPT LANGUAGE="JavaScript">
<!--
function GoList(page){
	this.location = page;
}
//-->
</SCRIPT>
<%@ include file="../toolbar.jsp"%>
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
<TR><TD HEIGHT=20>&nbsp;</TD></TR>
<TR><TD>
	<TABLE width="50%" border=0 cellspacing=0 cellpadding=0 align="center">
		<TR>
			<TD bgcolor=#000000>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
					<TR>
						<TH align="center">�澯���ع���ɾ��������ʾ��Ϣ</TH>
					</TR>
						<TR height="50">
							<TD align=center valign=middle class=column><%out.println(strMsg);%></TD>
						</TR>				
					<TR>
						<TD class=foot align="right">
						<INPUT TYPE="button" NAME="cmdJump" onclick="GoList('event_paichong_from.jsp')" value=" �� �� " class=btn>
						<INPUT TYPE="button" NAME="cmdBack" onclick="javascript:history.go(-1);" value=" �� �� " class=btn>
						</TD>
					</TR>
				</TABLE>
			</TD>
		</TR>
	</TABLE>
</TD></TR>
<TR><TD HEIGHT=20>&nbsp;</TD></TR>
</TABLE>
<%@ include file="../foot.jsp"%>