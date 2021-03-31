<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ include file="../head.jsp"%>
<jsp:useBean id="DeviceAct" scope="request" class="com.linkage.litms.resource.DeviceAct" />
<%
	String gatherList = DeviceAct.getGatherList(session, "", "", false);
	String pid=request.getParameter("pid");
%>
<SCRIPT LANGUAGE="JavaScript">
//增加读口令
function addCommunity()
{
	var page = "./addComminuty.jsp?refresh="+Math.random();	
	var vReturnValue = window.showModalDialog(page,"","dialogHeight: 400px; dialogWidth: 600px; dialogTop: px; dialogLeft: px; edge: Raised; center: Yes; help: No; resizable: No; status: No;scroll:No;");	
	var obj = document.all("community");	
	if(typeof(vReturnValue) == "object")
	{		
		//有口令存在得情况下,要检查口令是否重复
		if(typeof(obj.options)!="undefined"&&obj.options.length>0)
		{
			var flag = false;
			for(var i=0;i<obj.options.length;i++)
			{
				if(obj.options[i].value==vReturnValue.text1)
				{
					flag =true;
					break;
				}
			}

			//加了重复得读口令
			if(flag)
			{
				alert("口令已存在!");
				return false;
			}		
		}		
		var oOption = document.createElement("OPTION");
		oOption.value=vReturnValue.text1;
		oOption.text=vReturnValue.text2;			
		obj.options.add(oOption); 		
		return true;
	}
	return true;

}


//删除读口令
function deleteCommunity()
{
	var obj = document.all("community");
	if(obj.selectedIndex=="undefined")
	{
		alert("请选择要删除得读口令");
		return false;
	}
	obj.options[obj.selectedIndex].removeNode();
	return true;	
}


//发现设备
function find()
{	
	var comm="";
	var obj = document.all("community");
	for(var i=0;i<obj.options.length;i++)
	{
		if(i==0)
		{
			comm=obj.options[0].value
		}
		else
		{
			comm+=","+obj.options[i].value;
		}  
	}
	if(!IsNull(comm,"设备口令"))
	{
		return false;
	}	

	var gatherIDObj =document.all("gather_id");
	if(gatherIDObj.options[gatherIDObj.selectedIndex].value=='-1')
	{
		alert("请选择采集点！");
		return false;
	}

    var segmentRadioObj=document.all("selectSegment");
	var selectSegment="0";
	for(var i=0;i<segmentRadioObj.length;i++)
	{
		if(segmentRadioObj[i].checked)
		{
			selectSegment=segmentRadioObj[i].value;
			break;
		}
	}

    //选择起始、终止IP来发现设备
	if(selectSegment=="0")
	{
		if(!IsNull(document.all("startip").value,"起始IP")||!IsIPAddr(document.all("startip").value))
		{
			return false;
		}

		if(!IsNull(document.all("endip").value,"终止IP")||!IsIPAddr(document.all("endip").value))
		{
			return false;
		}
	
		var startip=document.all("startip").value;
		var endip=document.all("endip").value;
		var startipArray=startip.split(".");
		var endipArray=endip.split(".");
		if(startipArray[0]!=192||startipArray[1]!=168||endipArray[0]!=192||endipArray[1]!=168)
		{	
   			alert("起始地址、终止地址必须是C类地址！");
   			return false;
		}
		startip="";
		endip="";
		for(var i=0;i<4;i++)
		{
  			if(startipArray[i].length==1)
  			{
    			startipArray[i]="00"+startipArray[i];
  			}
  			else if(startipArray[i].length==2)
  			{
				startipArray[i]="0"+startipArray[i];
  			}
  			startip+=startipArray[i];
  			if(endipArray[i].length==1)
  			{
    			endipArray[i]="00"+endipArray[i];
  			}
  			else if(endipArray[i].length==2)
  			{
				endipArray[i]="0"+endipArray[i];
  			}
  			endip+=endipArray[i];
		}
	
		if(startip>endip)
		{
			alert("起始地址不能大于终止地址");
			return false;
		}
	}
	//根据网段来发现设备
	else
	{
		if(!IsNull(document.all("segment").value,"网段地址"))
		{
			return false;
		}
	}
	
	
	showMsgDlg();
	var inetmaskObj = document.all("inetmask");
	var page="./IP_Browser_devicefind.jsp?comm="+comm+"&gather_id="+gatherIDObj.options[gatherIDObj.selectedIndex].value
		+"&startip="+document.all("startip").value+"&endip="+document.all("endip").value+"&segment="+document.all("segment").value
		+"&inetmask="+inetmaskObj.options[inetmaskObj.selectedIndex].value;
	//alert(page);
	document.all("childFrm").src=page;
	
}

function showMsgDlg(){
	w = document.body.clientWidth;
	h = document.body.clientHeight;

	l = (w-250)/2;
	t = (h-60)/2;
	PendingMessage.style.left = l;
	PendingMessage.style.top  = t;
	PendingMessage.style.display="";
}

function closeMsgDlg(){
	PendingMessage.style.display="none";
}

function cancel()
{
 closeMsgDlg();
 document.all("childFrm").src="";
}

function isSegment()
{	
	var obj =document.all("selectSegment");	
	for(var i=0;i<obj.length;i++)
	{		
		//选中根据起始、终止IP来发现设备
		if(obj[i].checked&&obj[i].value=="0")
		{
			tr0.style.display="";
			tr1.style.display="none";
			document.all("segment").value="";
			break;
		}
		else if(obj[i].checked&&obj[i].value=="1")
		{
			tr1.style.display="";
			tr0.style.display="none";
			document.all("startip").value="";
			document.all("endip").value="";
			break;
		}
	}
}

</SCRIPT>
<form name="frm" method="post" action="Ip_Browser_submit.jsp" target="childFrm">
<input type="hidden" name="pid" value="<%=pid%>">
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">	
	<tr>
		<td align=center>
			<table width="95%" height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
				<tr>
					<td width="162" align="center" class="title_bigwhite">
						IP Browser
					</td>
				</tr>
			</table>
		</td>
	</tr>	
	<TR>
		<TD align=center>
			<table border=0 cellspacing=1 cellpadding=2 width="95%" bgcolor=#999999>
				<tr>
					<th colspan=4>设备发现</th>
				</tr>
				<tr bgcolor="#FFFFFF">
					<td colspan=4 align=center>
						<input type="radio" name="selectSegment" value="0" checked onclick="isSegment()">根据起始、终止IP来发现设备&nbsp;&nbsp;
						<input type="radio" name="selectSegment" value="1" onclick="isSegment()">根据网段来发现设备
					</td>
				</tr>
				<tr bgcolor="#FFFFFF" id="tr0">
					<td width=15% align=right>起始IP:</td>
					<td width=35% align=left><input type=text name="startip" value=""></td>
					<td width=15% align=right>终止IP:</td>
					<td width=35% align=left><input type=text name="endip" value=""></td>					
				</tr>	
				<tr bgcolor="#FFFFFF" id="tr1" style="display:none">
					<td width=15% align=right>网段地址:</td>
					<td width=35% align=left><input type=text name="segment" value=""></td>
					<td width=50% align=left colspan=2>
						子网掩码:<select name="inetmask">
							<option value="28">28位</option>
							<option value="29">29位</option>
							<option value="30">30位</option>
							<option value="31">31位</option>							
						</select></td>					
					</td>	
				</tr>
				<tr bgcolor="#FFFFFF">
				     <td width=15% align=right >设备口令设置:</td>
					 <td colspan=3>
						<TABLE border=0 cellspacing=0 cellpadding=0 width="100%" align=left>						
						<tr>
							<td width="45%">
								<span>
									<select name="community" size="3" style="width:420;height:100" multiple>								
									</select>									
								</span>
							</td>
							<td width="10%">
								<div>
									<input type=button value="添加" name="add" onclick=" return addCommunity()" class=btn>
									<br>
									<br>
									<input type=button value="删除" name="delete" onclick=" return deleteCommunity()" class=btn>
									<br>
								</div>
							</td>
							<td  width="45%"><div>口令格式：<br>安全级别||鉴权用户||鉴权协议||鉴权密钥||加解密协议||私钥||v1/v2读口令</div></td>
						</tr>
						</table>
					</td>
					</tr>
				<tr bgcolor="#FFFFFF">					
						<td width=15% align=right >采集点:</td>
						<td colspan=3><%=gatherList%></td>					
				</tr>
				<%if(null!=pid&&!"null".equals(pid)) {%>
				<tr bgcolor="#FFFFFF">
					<td colspan=4  align=right class=green_foot>
						<input type="button" value="查看" onclick="find()" class=btn>&nbsp;&nbsp;
						<input type="button" value="取消" onclick="cancel()" class=btn>						
					</td>
				</tr>
				<%}%>
			</table>
		</TD>
	</TR>
	<TR>
		<TD HEIGHT=20>&nbsp;</TD>
	</TR>
	<TR>
		<TD align=center>
			<div id="data"></div>
		</TD>
	</TR>
</TABLE>
</form>
<div id="PendingMessage"
	style="position:absolute;z-index:3;top:0px;left:0px;width:250;height:60;border-width:1;border-style:ridge;background-color:#eeeeee;padding-top:10px;display:none">
<center>
<table border="0">
	<tr>
		<td valign="middle"><img src="../images/cursor_hourglas.gif"
			border="0" WIDTH="30" HEIGHT="30"></td>
		<td>&nbsp;&nbsp;</td>
		<td valign="middle"><span id=txtLoading
			style="font-size:14px;font-family: 宋体">载入数据，请稍候······</span></td>
	</tr>
</table>
</center>
</div>
<IFRAME id ="childFrm" name=childFrm SRC="" STYLE="display:none;width:500;height:500"></IFRAME>
<SCRIPT LANGUAGE="JavaScript">
<%if(null==pid||"null".equals(pid)){%>
alert("在当前层次不能进行IP Browser发现，请进入下一层");
window.close();
<%}%>
</SCRIPT>
					


        	
