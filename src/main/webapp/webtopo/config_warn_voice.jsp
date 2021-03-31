<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ page import ="com.linkage.litms.webtopo.warn.WarnVoiceManager,
				com.linkage.litms.common.FileViewer" %>
<%@ page import="java.util.*" %>
<%
request.setCharacterEncoding("GBK");

WarnVoiceManager manager=new WarnVoiceManager(request);
Cursor cursor=manager.getWarnVoiceCur();
Map fields=cursor.getNext();
Map warnMap=manager.getWarnLevel();
FileViewer f=manager.getSoundFile();
String strData="";
if(fields == null){
	strData = "<TR ><TD COLSPAN=6 HEIGHT=30 bgcolor=#FFFFFF>您暂时没有告警声音配置</TD></TR>";
}
else{		
	String voicetype=null;
	while(fields != null){
		voicetype=(String)fields.get("voicetype");
		voicetype=(voicetype.compareTo("0")==0)?"发声一次":"一直发声";
		strData += "<TR bgcolor=#FFFFFF>";
		strData += "<TD >"+ (String)warnMap.get((String)fields.get("warnlevel")) + "</TD>";
		strData += "<TD >"+ fields.get("warnvoice") + "</TD>";
		strData += "<TD >"+ voicetype + "</TD>";		
		strData += "<TD align=center><A HREF=\"javascript:Edit('"+fields.get("inid")+"','"+fields.get("warnlevel")+"','"+fields.get("warnvoice")+"','"+fields.get("voicetype")+"');\">编辑</A> | <A HREF=\"javascript:Del('"+(String)fields.get("inid")+"');\">删除</A> | <IMG SRC=\"../images/sound_voice.gif\" WIDTH=\"19\" HEIGHT=\"15\" BORDER=\"0\" style=\"cursor:hand\" onclick=\"javascript:testMusicByName('"+fields.get("warnvoice")+"');\" ALT=\"试听\"></TD>";
		strData += "</TR>";		
		
		fields = cursor.getNext();
	}
	
}


%>
<TITLE>告警声音配置</TITLE>
<link href="../css/css_green.css" rel="stylesheet" type="text/css">
<SCRIPT LANGUAGE="JavaScript" src="../Js/CheckForm.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript">
<!--
var isCall=0;
var iTimerID;
var isSel=0;

function CallPro()
{	
	switch(parseInt(isCall,10))
	{
		case 1:
		{
			window.alert("配置成功!");
			window.clearInterval(iTimerID);						
			location.href="config_warn_voice.jsp";

			break;
		}
		case -1:
		{
			window.alert("数据库操作失败,请重新操作!");
			window.clearInterval(iTimerID);
			frm.button1.disabled=false;
			isCall=0;
			break;
		}
		
		case -100:
		{
			window.alert("您配置的告警级别已经配置，请重新操作!");
			window.clearInterval(iTimerID);
			frm.button1.disabled=false;
			isCall=0;
			break;
		}	
	}

}

function Del(_id)
{
	if(window.confirm(("您确认删除该配置吗?")))
	{
		isCPE="-1";
		var page="config_warn_voice_save.jsp?inid="+_id+"&action=delete";
		document.all("childFrm").src=page;
		iTimerID = window.setInterval("CallPro()",1000);
	}
}

function Add(){
	document.frm.reset();
	document.frm.action.value="add";
	actLabel.innerHTML = '添加配置信息';	
	ClearAll();
}

function Edit(_id,_warn_level,_warnvoice,_voicetype){
	frm.inid.value=_id;
	document.frm.action.value="edit";
	actLabel.innerHTML = '编辑配置信息';	
	isSel=0;
	iTimerID = window.setInterval("CallSel('"+_warn_level+"','"+_warnvoice+"','"+_voicetype+"')",1000);	
}

function CallSel(_warn_level,_warnvoice,_voicetype)
{
	
		
	for(var i=0;i<frm.warnlevel.options.length;i++)
	{			
		if(frm.warnlevel.options[i].value==_warn_level)
		{				
			frm.warnlevel.options[i].selected=true;			
			break;
		}

	}
	

	for(var i=0;i<frm.warnvoice.options.length;i++)
	{
		//alert(frm.drt_mid.options[i].value);
		if(frm.warnvoice.options[i].value==_warnvoice)
		{
			
			frm.warnvoice.options[i].selected=true;			
			break;
		}

	}

	for(var i=0;i<frm.voicetype.length;i++)
	{
		if(frm.voicetype[i].value==_voicetype)
		{
			frm.voicetype[i].checked=true;
			break;
		
		}
	
	}

	
	window.clearInterval(iTimerID);	

}


function CheckForm()
{
	if(frm.action.value=="add" || (frm.action.value=="edit" && window.confirm("您确认需要修改该信息吗?")))
	{
		frm.button1.disabled=true;
		frm.target="childFrm";
		frm.submit();
		iTimerID = window.setInterval("CallPro()",1000);		
	}
}





function ClearAll()
{
	
}


function testMusicByName(voicename)
{
	var page="testMusic.jsp?name="+voicename;
	//alert(page);
	document.all("childFrm").src=page;
}


function testMusic()
{
	var page="testMusic.jsp?name="+frm.warnvoice.value;
	//alert(page);
	document.all("childFrm").src=page;
}



//-->
</SCRIPT>

<TABLE border=0 cellspacing=0 cellpadding=0 width="90%" align="center">

<TR><TD>
	<FORM NAME="frm" METHOD="post"　 action="config_warn_voice_save.jsp"　target="childFrm">
        <table width="100%" height="30" border="0" align="center" cellpadding="0" cellspacing="0"  class="green_gargtd">
          <tr> 
            <td align="left" class="title_bigwhite">&nbsp;&nbsp;<font color="white"><b>告警声音配置</b></font></td>
			<td align="right"><input type="button" name="add1" value=" 增 加 " onClick="javascript:Add();" class="btn"></td>
			
          </tr>
        </table>
        <TABLE width="100%" border=0 cellspacing=0 cellpadding=0 align="center">
		<TR>
			<TD bgcolor=#999999>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
                <TR bgcolor="#FFFFFF" class="blue_title"> 
				  <th>告警级别</th>
                  <th>声音文件</th>
                  <th>提示发式</th>                  
                  <th>操作</th>
                </TR>
                <%=strData%> </TABLE>
			</TD>
		</TR>
	</TABLE>
	<BR>
	<TABLE width="100%" border=0 cellspacing=0 cellpadding=0 align="center">
		<TR>
			<TD bgcolor=#999999>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">					
                <TR bgcolor="#FFFFFF" class="blue_title"> 
                  <th colspan="2" align="center"><SPAN id="actLabel" style="border:0;">添加告警声音配置</SPAN></th>
				</TR>				
				
				<TR bgcolor="#FFFFFF" >
                  <TD width="33%" align=right>告警级别
				
                  </TD>
                  <TD class="" width="67%">
					<select name="warnlevel">
					<%
						Set set=warnMap.keySet();
						Iterator it=set.iterator();
						String key="";
						while(it.hasNext())
						{
							key=(String)it.next();
							out.println("<option value="+key+">"+warnMap.get(key)+"</option>");			
						}
					%>
					</select>
                  </TD>
                </TR>

				
				<tr bgcolor="#FFFFFF">
					<td width="33%" align=right>声音文件</td>
					<td class="" width="67%">
						<select name="warnvoice">
							<%
								while (f.nextFile()) {									
									out.println("<option value=\""+f.getFileName()+"\">"+f.getFileName()+"</option>");
								}	
								
							%>
						</select>

						<IMG SRC="../images/sound_voice.gif" WIDTH="19" HEIGHT="15" BORDER="0" style="cursor:hand" onclick="javascript:testMusic();" ALT="试听">
					</td>					
				</tr>
				
				<tr bgcolor="#FFFFFF">
					<td width="33%" align=right>选择提示方式</td>
					<td class="" width="67%">
						<input type="radio" name="voicetype" value="0" checked> 发声一次<input type="radio" name="voicetype" value="1"> 一直发声
					</td>					
				</tr>
				
				
										
                <TR bgcolor="#FFFFFF" > 
                  <TD colspan="4" align="right" CLASS="blue_foot"> 
                    <INPUT TYPE="button" value=" 保 存 " class="jianbian" name="button1"  onclick="javascript:CheckForm();">&nbsp;
							<INPUT TYPE="hidden" name="action" value="add">&nbsp;						
							<input type="hidden" name="inid">
							<INPUT TYPE="button" value=" 关　闭 " class="jianbian" onclick="javascript:window.close();">
						</TD>
					</TR>
				</TABLE>
			</TD>
		</TR>

	</TABLE>
	</FORM>
</TD></TR>
<TR><TD HEIGHT=20>&nbsp;<IFRAME id="childFrm" name="childFrm" SRC="" STYLE="display:none"></IFRAME></TD></TR>
</TABLE>
