<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="java.util.*"%>
<meta http-equiv="refresh" content="300">

<%
	request.setCharacterEncoding("GBK");
	Map eventMap = new  HashMap();
	eventMap.put("0 BOOTSTRAP","首次连接");
	eventMap.put("1 BOOT","设备启动");
	eventMap.put("2 PERIODIC","周期通知");
	eventMap.put("3 SCHEDULED","定时通知");
	eventMap.put("4 VALUE CHANGE","参数变化");
	eventMap.put("6 CONNECTION REQUEST","连接请求");
	eventMap.put("7 TRANSFER COMPLETE","传输完成");
	eventMap.put("8 DIAGNOSTICS COMPLETE","诊断完成");
	eventMap.put("X CT-COM ACCOUNTCHANGE","配置维护帐号");
	eventMap.put("M Reboot","设备重启");
	eventMap.put("M Upload","上传文件");
	eventMap.put("M Download","下载配置");
	eventMap.put("M DeleteObject","删除实例");
	eventMap.put("M SetParameterValues","参数配置");

	String mysql="select b.oui,b.device_serialnumber,b.time,b.eventcode,b.commandkey,b.descr,b.auto_check from tab_event b where 1=1 order by b.time desc ";
	
	Cursor cursor=DataSetBean.getCursor(mysql,100);
	Map fields=cursor.getNext();
	String strData="";
	
	if(fields!=null)
	{
		while(fields!=null)
		{   
		    int auto = Integer.parseInt((String)fields.get("auto_check".toLowerCase()));
		  
		    String status = "";//告警状态栏
		    long time = Long.parseLong((String)fields.get("time".toLowerCase()));
		    String date = StringUtils.formatDate("yyyy-MM-dd HH:mm:ss",time);
		    if(auto == 2){
		    //手工确认
		      status = "<img name=check src=../images/check1.gif>";
		    }
		    if(auto == 1){
		    //自动确认
		      status = "<img name=check src=../images/check.gif>";
		    }
		   
		    if( auto == 0){
		  //    status = "未确认";
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
			        +"')\" title=\"1、绿勾:自动确认;蓝勾:手工确认;空白:未确认;\n2、双击手工确认告警 \"><td bgcolor='pink' align=left id=device_name nowrap>"
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
		     
		     // status = "已确认";
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
			        +"')\" title=\"1、绿勾:自动确认;蓝勾:手工确认;空白:未确认;\n2、告警已经确认！\"><td class=column1 align=left id=device_name nowrap>"
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
		strData="<tr><td colspan=6 class=column1>没有获取到告警信息</td></tr>";
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
				<div align="center" class="title_bigwhite">事件管理</div>
				</td>
				<td><img src="../images/attention_2.gif" width="15" height="12"> 显示最近100条实时事件</td>
			</tr>
		</table>
	</td></tr>
<TR><TD>
		<TABLE border=0 cellspacing=1 cellpadding=2 bgcolor=#999999 width="100%">
						<TR><TH colspan="6">实时查询</TH></TR>
						<TR>
							<TD bgcolor="#F4F4FF" colspan="6" >告警信息【双击选择】<BR>
							&nbsp; &nbsp;1、绿勾  表示自动确认; 蓝勾  表示手工确认;空白  表示未确认;<br>
							&nbsp; &nbsp;2、双击方式手工确认告警 </TD>
						</TR>
						<TR>
							<TD class="green_title2">设备信息</TD>
							<TD class="green_title2">时间</TD>
							<TD class="green_title2">告警代码</TD>
							<TD class="green_title2">关键字</TD>	
							<TD class="green_title2">描述</TD>
							<TD class="green_title2">告警状态栏</TD>
						</TR>
						<%=strData%>
						<TR>
							<TD class="green_title2">设备信息</TD>
							<TD class="green_title2">时间</TD>
							<TD class="green_title2">告警代码</TD>
							<TD class="green_title2">关键字</TD>	
							<TD class="green_title2">描述</TD>
							<TD class="green_title2">告警状态栏</TD>
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