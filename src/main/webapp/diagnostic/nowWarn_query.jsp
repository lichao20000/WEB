<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="java.util.*"%>
<meta http-equiv="refresh" content="300">

<%
	request.setCharacterEncoding("GBK");
	Map eventMap = new  HashMap();
	eventMap.put("0 BOOTSTRAP","�״�����");
	eventMap.put("1 BOOT","�豸����");
	eventMap.put("2 PERIODIC","����֪ͨ");
	eventMap.put("3 SCHEDULED","��ʱ֪ͨ");
	eventMap.put("4 VALUE CHANGE","�����仯");
	eventMap.put("6 CONNECTION REQUEST","��������");
	eventMap.put("7 TRANSFER COMPLETE","�������");
	eventMap.put("8 DIAGNOSTICS COMPLETE","������");
	eventMap.put("X CT-COM ACCOUNTCHANGE","����ά���ʺ�");
	eventMap.put("M Reboot","�豸����");
	eventMap.put("M Upload","�ϴ��ļ�");
	eventMap.put("M Download","��������");
	eventMap.put("M DeleteObject","ɾ��ʵ��");
	eventMap.put("M SetParameterValues","��������");

	String mysql="select b.oui,b.device_serialnumber,b.time,b.eventcode,b.commandkey,b.descr,b.auto_check from tab_event b where 1=1 order by b.time desc ";
	
	Cursor cursor=DataSetBean.getCursor(mysql,100);
	Map fields=cursor.getNext();
	String strData="";
	
	if(fields!=null)
	{
		while(fields!=null)
		{   
		    int auto = Integer.parseInt((String)fields.get("auto_check".toLowerCase()));
		  
		    String status = "";//�澯״̬��
		    long time = Long.parseLong((String)fields.get("time".toLowerCase()));
		    String date = StringUtils.formatDate("yyyy-MM-dd HH:mm:ss",time);
		    if(auto == 2){
		    //�ֹ�ȷ��
		      status = "<img name=check src=../images/check1.gif>";
		    }
		    if(auto == 1){
		    //�Զ�ȷ��
		      status = "<img name=check src=../images/check.gif>";
		    }
		   
		    if( auto == 0){
		  //    status = "δȷ��";
		      strData +="<tr style='cursor:hand;background:#F4F4FF' onmouseover=\"this.style.background='#CECECE';this.style.color='#006790';\" onmouseout=\"this.style.background='#F4F4FF';this.style.color='#000000';\" ondblclick=\"javascript:CallSetValue('"
			        +fields.get("oui".toLowerCase())
			        +"','"
			        +fields.get("device_serialnumber".toLowerCase())
			        +"','"
			        +fields.get("time".toLowerCase())
			        +"','"
			        +fields.get("eventcode".toLowerCase())
			        +"','"
			        +fields.get("commandkey ".toLowerCase())
			        +"','"
			        +status
			        +"')\" title=\"1���̹�:�Զ�ȷ��;����:�ֹ�ȷ��;�հ�:δȷ��;\n2��˫���ֹ�ȷ�ϸ澯 \"><td bgcolor='pink' align=left id=device_name nowrap>"
			        +fields.get("device_serialnumber".toLowerCase())
			        +"</td><td bgcolor='pink' align=left id=time nowrap>"
			        +date
			        +"</td><td bgcolor='pink' align=left id=eventcode nowrap>"
			        + eventMap.get((String)fields.get("eventcode".toLowerCase()))
			        +"</td><td bgcolor='pink' align=left id=commandkey nowrap> "
			        +fields.get("commandkey".toLowerCase())
			        +"</td><td bgcolor='pink' align=left id=descr nowrap><textarea cols='30' rows='2' class='input-textarea'>"
			        + fields.get("descr".toLowerCase())
			        +"</textarea></td><td bgcolor='pink' align=center id=status nowrap>"
			        +status
			        
			        +"</td></tr>";
		      
		    }else{
		     
		     // status = "��ȷ��";
		      strData +="<tr style='cursor:hand;background:#F4F4FF' onmouseover=\"this.style.background='#CECECE';this.style.color='#006790';\" onmouseout=\"this.style.background='#F4F4FF';this.style.color='#000000';\" ondblclick=\"javascript:CallSetValue('"
			        +fields.get("oui".toLowerCase())
			        +"','"
			        +fields.get("device_serialnumber".toLowerCase())
			        +"','"
			        +fields.get("time".toLowerCase())
			        +"','"
			        + fields.get("eventcode".toLowerCase())
			        +"','"
			        +fields.get("commandkey ".toLowerCase())
			        +"','"
			        + status
			        +"')\" title=\"1���̹�:�Զ�ȷ��;����:�ֹ�ȷ��;�հ�:δȷ��;\n2���澯�Ѿ�ȷ�ϣ�\"><td class=column1 align=left id=device_name nowrap>"
			        +fields.get("device_serialnumber".toLowerCase())
			        +"</td><td class=column1 align=left id=time nowrap>"
			        + date
			        +"</td><td class=column1 align=left id=eventcode nowrap>"
			        + eventMap.get((String)fields.get("eventcode".toLowerCase()))
			        +"</td><td class=column1 align=left id=commandkey nowrap> "
			        +fields.get("commandkey".toLowerCase())
			        +"</td><td class=column1 align=left id=descr nowrap><textarea cols='30' rows='2' class='input-textarea'>"
			        + fields.get("descr".toLowerCase())
			        +"</textarea></td><td class=column1 align=center id=status nowrap> "
			        + status
			        
			        +"</td></tr>";
		      
		    }
        
			fields=cursor.getNext();
		}
	}
	else
	{
		strData="<tr><td colspan=6 class=column1>û�л�ȡ���澯��Ϣ</td></tr>";
	}
%>

<%@page import="com.linkage.litms.common.database.Cursor"%>
<%@page import="com.linkage.litms.common.util.*"%>
<%@page import="java.util.Map"%>
<%@page import="com.linkage.litms.common.database.DataSetBean"%>
<SCRIPT LANGUAGE="JavaScript">


	function CallSetValue(oui,device_serialnumber,time,eventcode,commandkey,status)
	{

	    if(status == 0 ){
	      var page="changewarn.jsp?oui="+oui+"&device_serialnumber="+device_serialnumber+"&time="+ time+"&eventcode="+eventcode+"&commandkey="+commandkey;
			
			window.location=page;
			
	    }
	      
	}


</SCRIPT>
<%@ include file="../head.jsp"%>
<form name="form1" method="post" >
<TABLE width="98%" border=0 cellspacing=0 cellpadding=0 width="100%">
<TR><TD HEIGHT=20>&nbsp;</TD></TR>
	<tr><td>
		<table width="100%" height="30" border="0" align="center"
			cellpadding="0" cellspacing="0" class="green_gargtd">
			<tr>
				<td width="162">
				<div align="center" class="title_bigwhite">�¼�����</div>
				</td>
				<td><img src="../images/attention_2.gif" width="15" height="12"> ��ʾ���100��ʵʱ�¼�</td>
			</tr>
		</table>
	</td></tr>
<TR><TD>
		<TABLE border=0 cellspacing=1 cellpadding=2 bgcolor=#999999 width="100%">
						<TR><TH colspan="6">ʵʱ��ѯ</TH></TR>
						<TR>
							<TD bgcolor="#F4F4FF" colspan="6" >�澯��Ϣ��˫��ѡ��<BR>
							&nbsp; &nbsp;1���̹�  ��ʾ�Զ�ȷ��; ����  ��ʾ�ֹ�ȷ��;�հ�  ��ʾδȷ��;<br>
							&nbsp; &nbsp;2��˫����ʽ�ֹ�ȷ�ϸ澯 </TD>
						</TR>
						<TR>
							<TD class="green_title2">�豸��Ϣ</TD>
							<TD class="green_title2">ʱ��</TD>
							<TD class="green_title2">�澯����</TD>
							<TD class="green_title2">�ؼ���</TD>	
							<TD class="green_title2">����</TD>
							<TD class="green_title2">�澯״̬��</TD>
						</TR>
						<%=strData%>
						<TR>
							<TD class="green_title2">�豸��Ϣ</TD>
							<TD class="green_title2">ʱ��</TD>
							<TD class="green_title2">�澯����</TD>
							<TD class="green_title2">�ؼ���</TD>	
							<TD class="green_title2">����</TD>
							<TD class="green_title2">�澯״̬��</TD>
						</TR>
		</TABLE>

</TD>
</TR>
<TR>
		<TD HEIGHT=10>&nbsp;<IFRAME ID=childFrm SRC="about:blank" STYLE="display:none"></IFRAME>
		<IFRAME ID=childFrm2 SRC="about:blank" STYLE="display:none"></IFRAME>
		<IFRAME ID=childFrm3 SRC="" STYLE="display:none"></IFRAME>
		</TD>
	</TR>
</TABLE>
</form>
<%@ include file="../foot.jsp"%>