<%--
FileName	: IpLineSave.jsp
Author		: liuli
Date		: 2007-3-06
Note		: �ʺ����ӡ��޸ġ�ɾ������
--%>
<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.linkage.litms.common.corba.WebCorba" %>
<%@ page import="com.linkage.litms.resource.DeviceAct" %>

<%
			request.setCharacterEncoding("GBK");
			String strMsg = null;
			String strSQL = null;
			String Str_kinds = "";
			String[] Str_kind = null;
			ArrayList Str_sql = new ArrayList();
			String str_ruler_name = "";
			String str_event_oid = "";
			String str_crorate = "";
			String str_device_model = "";
			String str_EditTime = "";
			String str_start_ip = "";
			String str_over_ip = "";
			String str_ruler_desc = "";
			String str_radiobutton = "";
			String str_dengji = "";
			String str_dengjixi = "";
			String str_radiobutton1 = "";
			String str_vendor_id = "";
			String device_model_id = "";
			//���˷�ʽ
			String attr_flag = "";
			//��������
			String savetime = "";

			String strruler_id = request.getParameter("rule_id");
			str_ruler_name = request.getParameter("ruler_name");
			str_event_oid = request.getParameter("event_oid");
			
			str_crorate = request.getParameter("creat_name");
			
			//�豸�ͺ�
			str_device_model = request.getParameter("devicetype_id");
			//�豸����
			str_vendor_id = request.getParameter("vendor_id");
			//�豸�ͺ�id
			device_model_id = DeviceAct.getDeviceModelID(str_vendor_id, str_device_model);
			
			Str_kind = request.getParameterValues("Mycheckbox1");
			for (int i = 0; i < Str_kind.length; i++) {
				
					Str_kinds += ";" +Str_kind[i] ;

			}if(!"".equals(Str_kinds)) Str_kinds = Str_kinds + ";"; 
							
			str_EditTime = request.getParameter("ruler_time");
			
			str_start_ip = request.getParameter("start_ip");
			str_over_ip = request.getParameter("over_ip");
			
			str_ruler_desc = request.getParameter("ruler_desc");
			
			str_radiobutton = request.getParameter("radiobutton");
	
			str_dengji = request.getParameter("dengji");
			str_dengjixi = request.getParameter("dengjixi");
			str_radiobutton1 = request.getParameter("radiobutton1");
			
			String[] str_ipport = request.getParameterValues("ipport");
			
			//���˷�ʽ
			attr_flag = request.getParameter("attr_flag");
			//��������
			savetime = request.getParameter("savetime");
			long timeNum = 0;
			if (savetime != null && !"".equals(savetime)){
				timeNum = Long.parseLong(savetime)*24*60*60;
			}

			//�޸Ĳ���
			//�ж��Ƿ��ظ�
			strSQL = "update fault_filter_ruler set  event_rule_name='"+str_ruler_name
					+"',event_oid='"+str_event_oid+"',device_model='"+device_model_id
					+"',creator_name='"+str_crorate+"',device_ip_min='"+str_start_ip
					+"',device_ip_max='"+str_over_ip+"',gather_id='"+Str_kinds
					+"',event_level_oper="+str_dengji+",event_level_id="+str_dengjixi
					+",rule_time='"+str_EditTime+"',attr_logic="+str_radiobutton1
					+",is_active="+str_radiobutton+",fault_desc='"+str_ruler_desc
					+"',attr_flag=" + attr_flag + ",savetime=" + timeNum 
					+ " where  rule_id="+ strruler_id +" ";
			strSQL = StringUtils.replace(strSQL, "='-1',","='',");
			strSQL = StringUtils.replace(strSQL, "=-1,", "=NULL,");
			strSQL = StringUtils.replace(strSQL, "=,", "=NULL,");
			
			strSQL = StringUtils.replace(strSQL, "='-1' where","='' where");
			strSQL = StringUtils.replace(strSQL, "=-1 where", "=NULL where");
			strSQL = StringUtils.replace(strSQL, "= where","=NULL where");
			Str_sql.add(strSQL);
			if(str_ipport !=null){
			
			strSQL = "  delete from  fault_filter_attr_info where  rule_id="+ strruler_id +" ";
			Str_sql.add(strSQL);
					for (int i = 0; i < str_ipport.length; i++) {
						
						String tmpipport = (String) str_ipport[i];
						String[] str = tmpipport.split("\\|");
						
				strSQL = "  insert into fault_filter_attr_info (rule_id,child_rule_id,event_attr_oid,event_attr_value,attr_oper) values("+strruler_id+","+i+",'"+str[0]+"','" + str[2] + "'," + str[1] + ")";
								
						strSQL = StringUtils.replace(strSQL, "='-1',","='',");
						strSQL = StringUtils.replace(strSQL, "=-1,", "=NULL,");
						strSQL = StringUtils.replace(strSQL, "=,", "=NULL,");
						
						strSQL = StringUtils.replace(strSQL, "='-1' where","='' where");
						strSQL = StringUtils.replace(strSQL, "=-1 where", "=NULL where");
						strSQL = StringUtils.replace(strSQL, "= where","=NULL where");
				Str_sql.add(strSQL);
				}
			}
		
			try{
				if (strMsg == null || strSQL != null) {
				 

					int iCode[] = DataSetBean.doBatch(Str_sql);
					if (iCode != null && iCode.length > 0) {
							strMsg = "�澯���˹�����ӳɹ���";
	
						WebCorba corba = null;
			
						AlarmFilter.O_AlarmFilter AlarmFilter = null;
						
						    String _s = "";
							List _tem = curUser.getUserProcesses();
							for (int i = 0; i < _tem.size(); i++){
						
								_s = (String) _tem.get(i);

								try{
									corba = new WebCorba("EventCorrelation", _s, "1");
		
									AlarmFilter = (AlarmFilter.O_AlarmFilter)corba.getIDLCorba("EventCorrelation");
		
									if(null!=AlarmFilter){
										try{
											AlarmFilter.I_NotifyRuler("xiaoxf>>>event_rule_name=="+Encoder.ChineseStringToAscii(str_ruler_name));
										}catch(Exception e){
											try{	
												corba.refreshCorba("c");
												AlarmFilter = (AlarmFilter.O_AlarmFilter)corba.getIDLCorba("EventCorrelation");
												AlarmFilter.I_NotifyRuler("xiaoxf>>>event_rule_name=="+Encoder.ChineseStringToAscii(str_ruler_name));
											}catch(Exception e1){
												e1.printStackTrace();
											}
										}
									  }
							  }catch(Exception e1){
										e1.printStackTrace();
							  	}
							}
						} else 	strMsg = "�澯���˹���༭����ʧ�ܣ��뷵�����Ի��Ժ����ԣ�";
					}
				}catch(Exception e){
					e.printStackTrace();
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
	<TR>
		<TD HEIGHT=20>&nbsp;</TD>
	</TR>
	<TR>
		<TD>
		<TABLE width="50%" border=0 cellspacing=0 cellpadding=0 align="center">
			<TR>
				<TD bgcolor=#000000>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
					<TR>
						<TH align="center">�༭�澯���˹��������ʾ��Ϣ</TH>
					</TR>
					<TR height="50">
						<TD align=center valign=middle class=column><%out.println(strMsg);%></TD>
					</TR>
					<TR>
						<TD class=foot align="right">
						<INPUT TYPE="button" NAME="cmdJump" onclick="GoList('event_guolv_from.jsp')" value=" �� �� " class=btn>
						<INPUT TYPE="button" NAME="cmdJump" onclick="javascript:history.go(-1);" value=" �� �� " class=btn>
						</TD>
					</TR>
				</TABLE>
				</TD>
			</TR>
		</TABLE>
		</TD>
	</TR>
	<TR>
		<TD HEIGHT=20>&nbsp;</TD>
	</TR>
</TABLE>
<%@ include file="../foot.jsp"%>
