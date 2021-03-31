<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ include file="../head.jsp"%>
<%@ include file="../toolbar.jsp"%>

<SCRIPT LANGUAGE="JavaScript">
<!--

var isCall=0;
var iTimerID;

function CallPro()
{
	switch(parseInt(isCall,10))
	{
		case 1:
		{
			window.alert("记录已被删除！");
			window.clearInterval(iTimerID);	
			
			location.href="sysParameterConfig.jsp";
			isCall=0;
			break;

		}
	}

}

function showpage(uniformid)
{
	switch (uniformid)
	{
		case 1:
		{
			document.all("td1").className="button_onblue";
			document.all("td2").className="button_outblue";
			document.all("td3").className="button_outblue";
			document.all("td4").className="button_outblue";
			document.all("td5").className="button_outblue";
			document.all("test1").style.display="";
			document.all("test2").style.display="none";
			document.all("test3").style.display="none";
			document.all("test4").style.display="none";
			document.all("test5").style.display="none";

			break;
		}
		case 2:
		{
			document.all("td1").className="button_outblue";
			document.all("td2").className="button_onblue";
			document.all("td3").className="button_outblue";
			document.all("td4").className="button_outblue";
			document.all("td5").className="button_outblue";
			document.all("test1").style.display="none";
			document.all("test2").style.display="";
			document.all("test3").style.display="none";
			document.all("test4").style.display="none";
			document.all("test5").style.display="none";

			break;
		}	
		case 3:
		{
			document.all("td1").className="button_outblue";
			document.all("td2").className="button_outblue";
			document.all("td3").className="button_onblue";
			document.all("td4").className="button_outblue";
			document.all("td5").className="button_outblue";
			document.all("test1").style.display="none";
			document.all("test2").style.display="none";
			document.all("test3").style.display="";
			document.all("test4").style.display="none";
			document.all("test5").style.display="none";

			break;
		}
		case 4:
		{
			document.all("td1").className="button_outblue";
			document.all("td2").className="button_outblue";
			document.all("td3").className="button_outblue";
			document.all("td4").className="button_onblue";
			document.all("td5").className="button_outblue";
			document.all("test1").style.display="none";
			document.all("test2").style.display="none";
			document.all("test3").style.display="none";
			document.all("test4").style.display="";
			document.all("test5").style.display="none";
			break;
		}
		case 5:
		{
			document.all("td1").className="button_outblue";
			document.all("td2").className="button_outblue";
			document.all("td3").className="button_outblue";
			document.all("td4").className="button_outblue";
			document.all("td5").className="button_onblue";
			document.all("test1").style.display="none";
			document.all("test2").style.display="none";
			document.all("test3").style.display="none";
			document.all("test4").style.display="none";
			document.all("test5").style.display="";
			break;
		}
	}
	
}

function showChild(parname)
{
	if (parname=="objectType1")
	{
		var o = document.all("objectType1");
		var id = o.options[o.selectedIndex].value;	
		document.all("childFrm").src = "attrgroup_listModel.jsp?dxlx="+ id;	
	}
	else if (parname=="objectType2")
	{
		var o = document.all("objectType2");
		var id = o.options[o.selectedIndex].value;
		document.all("childFrm").src = "process_state.jsp?dxlx="+ id;
	}
	
}

function NewForm(parname)
{	var o = null;
	if (parname=="test1")
	{
		var page = "host_NewSysConfig.jsp";
		var otherpra = "channelmode=0,directories=0,location=0,menubar=0,resizable=no,scrollbars=no,status=0,toolbar=0,height=174,width=350,top=200,left=365";
		window.open(page,"新建系统参数",otherpra);
	}
	else if (parname=="test2")
	{
		var page = "host_NewObjType.jsp";
		var otherpra = "channelmode=0,directories=0,location=0,menubar=0,resizable=no,scrollbars=no,status=0,toolbar=0,height=174,width=350,top=200,left=365";
		window.open(page,"新建对象类型",otherpra);
	}
	else if (parname=="test3")
	{
		var page = "host_NewWarnLevel.jsp";
		var otherpra = "channelmode=0,directories=0,location=0,menubar=0,resizable=no,scrollbars=no,status=0,toolbar=0,height=198,width=351,top=200,left=365";
		window.open(page,"新建告警等级",otherpra);
	}
	else if (parname=="test4")
	{
		o = document.all("objectType1");
		if (o.value == -1)
		{
			alert("请选择一种对象类型！");
			return false;
		}
		o = document.all("select_four");
		
		if (o==null)
		{
			alert("没有检索到当前类型的采集属性，不能为该类型定义新的属性组！");
			return false;
		}
		o = document.all("objectType1");
		var lxbh = o.value;
		var lxmc = o.options[o.selectedIndex].text;
		var page = "host_NewAttributeGroup.jsp?lxbh=" + lxbh + "&lxmc=" + lxmc;
		var otherpra = "channelmode=0,directories=0,location=0,menubar=0,resizable=no,scrollbars=no,status=0,toolbar=0,height=172,width=350,top=200,left=365";
		window.open(page,"新建属性组",otherpra);
	}
	else if (parname=="test5")
	{
		o = document.all("objectType2");
		if (o.value == -1)
		{
			alert("请选择一种对象类型！");
			return false;
		}
		var dxlx = o.value;
		var page = "host_NewProcess_state.jsp?dxlx=" + dxlx;
		var otherpra = "channelmode=0,directories=0,location=0,menubar=0,resizable=no,scrollbars=no,status=0,toolbar=0,height=146,width=350,top=200,left=365";
		window.open(page,"新建进程状态",otherpra);
	}
}

function AmendForm(parname)
{
	var len = document.frm.elements.length;
	var flag = false;
	var j = 0;
	var select_Value = new String();
	var array_select = new Array();
	
	if (parname=="test1")
	{
		for(i=0; i<len; i++){
			if(document.frm.elements[i].name=='select_one')
			{
				if (document.frm.elements[i].checked)
				{
					flag=true;
					break;
				}
			}		
		}

		if(!flag)
		{
			window.alert("请选择一条记录！");
			return false;	
		}

		for (i=0;i<len;i++)
		{
			if (document.frm.elements[i].name=='select_one')
			{
				if (document.frm.elements[i].checked)
				{
					j++;
					
					if (j>1)
					{
						alert("只能对一条记录修改！");
						j = 0;
						return false;
					}
					select_Value = document.frm.elements[i].value;
					select_Value = select_Value.substring(0,select_Value.length);					
					array_select = select_Value.split("#");
					var csm = array_select[0];					
					var csz = array_select[1];
					var cssm = array_select[2];
				}
			}
		}
		j = 0;
		var page = "host_AmendSysConfig.jsp?csm=" + csm + "&csz=" + csz + "&cssm=" + cssm;
		var otherpra = "channelmode=0,directories=0,location=0,menubar=0,resizable=no,scrollbars=no,status=0,toolbar=0,height=174,width=350,top=200,left=365";
		window.open(page,"修改系统参数",otherpra);
	}
	else if (parname=="test2")
	{
		
		for(i=0; i<len; i++){
			if(document.frm.elements[i].name=='select_two')
			{
				if (document.frm.elements[i].checked)
				{
					flag=true;
					break;
				}
			}		
		}

		if(!flag)
		{
			window.alert("请选择一条记录！");
			return false;	
		}

		for (i=0;i<len;i++)
		{
			if (document.frm.elements[i].name=='select_two')
			{
				if (document.frm.elements[i].checked)
				{
					j++;
					
					if (j>1)
					{
						alert("只能对一条记录修改！");
						j = 0;
						return false;
					}
					select_Value = document.frm.elements[i].value;
					array_select = select_Value.split("#");
					var lxbh = array_select[0];					
					var lxmc = array_select[1];
					var lxsm = array_select[2];
				}
			}
		}
		j = 0;
		var page = "host_AmendObjType.jsp?lxbh=" + lxbh + "&lxmc=" + lxmc + "&lxsm=" + lxsm;
		var otherpra = "channelmode=0,directories=0,location=0,menubar=0,resizable=no,scrollbars=no,status=0,toolbar=0,height=174,width=350,top=200,left=365";
		window.open(page,"修改对象类型",otherpra);
	}
	else if (parname=="test3")
	{
		for(i=0; i<len; i++){
			if(document.frm.elements[i].name=='select_three')
			{
				if (document.frm.elements[i].checked)
				{
					flag=true;
					break;
				}
			}		
		}

		if(!flag)
		{
			window.alert("请选择一条记录！");
			return false;	
		}

		for (i=0;i<len;i++)
		{
			if (document.frm.elements[i].name=='select_three')
			{
				if (document.frm.elements[i].checked)
				{
					j++;
					
					if (j>1)
					{
						alert("只能对一条记录修改！");
						j = 0;
						return false;
					}
					select_Value = document.frm.elements[i].value;
					array_select = select_Value.split("#");
					var gjbh = array_select[0];					
					var gjdj = array_select[1];
					var gjxx = array_select[2];
					var zdcl = array_select[3];
					var cldz = array_select[4];
				}
			}
		}
		j = 0;
		var page = "host_AmendWarnLevel.jsp?gjbh=" + gjbh + "&gjdj=" + gjdj + "&gjxx=" + gjxx + "&zdcl=" + zdcl + "&cldz=" + cldz;
		var otherpra = "channelmode=0,directories=0,location=0,menubar=0,resizable=no,scrollbars=no,status=0,toolbar=0,height=198,width=351,top=200,left=365";
		window.open(page,"修改告警等级",otherpra);
	}
	else if (parname=="test4")
	{
		for(i=0; i<len; i++){
			if(document.frm.elements[i].name=='select_four')
			{
				if (document.frm.elements[i].checked)
				{
					flag=true;
					break;
				}
			}		
		}

		if(!flag)
		{
			window.alert("请选择一条记录！");
			return false;	
		}

		for (i=0;i<len;i++)
		{
			if (document.frm.elements[i].name=='select_four')
			{
				if (document.frm.elements[i].checked)
				{
					j++;
					
					if (j>1)
					{
						alert("只能对一条记录修改！");
						j = 0;
						return false;
					}
					select_Value = document.frm.elements[i].value;
					array_select = select_Value.split("#");
					var dxlx = array_select[0];					
					var zbh = array_select[1];
					var zmc = array_select[2];
					var zsm = array_select[3];					
				}
			}
		}
		j = 0;
		var o = document.all("objectType1");		
		var dxmc = o.options[o.selectedIndex].text;
		
		var page = "host_AmendAttributeGroup.jsp?dxlx=" + dxlx + "&zbh=" + zbh + "&zmc=" + zmc + "&zsm=" + zsm + "&dxmc=" + dxmc;
		var otherpra = "channelmode=0,directories=0,location=0,menubar=0,resizable=no,scrollbars=no,status=0,toolbar=0,height=174,width=350,top=200,left=365";
		window.open(page,"修改属性组",otherpra);
	}
	else if (parname=="test5")
	{
		for(i=0; i<len; i++){
			if(document.frm.elements[i].name=='select_five')
			{
				if (document.frm.elements[i].checked)
				{
					flag=true;
					break;
				}
			}		
		}

		if(!flag)
		{
			window.alert("请选择一条记录！");
			return false;	
		}

		for (i=0;i<len;i++)
		{
			if (document.frm.elements[i].name=='select_five')
			{
				if (document.frm.elements[i].checked)
				{
					j++;
					
					if (j>1)
					{
						alert("只能对一条记录修改！");
						j = 0;
						return false;
					}
					select_Value = document.frm.elements[i].value;
					array_select = select_Value.split("#");
					var dxlx = array_select[0];					
					var ztbh = array_select[1];
					var ztmc = array_select[2];								
				}
			}
		}
		j = 0;					
		
		var page = "host_AmendProcess_state.jsp?dxlx=" + dxlx + "&ztbh=" + ztbh + "&ztmc=" + ztmc;
		var otherpra = "channelmode=0,directories=0,location=0,menubar=0,resizable=no,scrollbars=no,status=0,toolbar=0,height=146,width=350,top=200,left=365";
		window.open(page,"修改进程状态",otherpra);
	}
}

function DelForm(parname)
{
	var len = document.frm.elements.length;
	var flag = false;
	var j = 0;
	var select_Value = new String();
	var array_select = new Array();
		
	if (parname=="test1")
	{
		
		for(i=0; i<len; i++){
			if(document.frm.elements[i].name=='select_one')
			{
				if (document.frm.elements[i].checked)
				{
					flag=true;
					break;
				}
			}		
		}

		if(!flag)
		{
			window.alert("请选择一条记录！");
			return false;	
		}

		for (i=0;i<len;i++)
		{
			if (document.frm.elements[i].name=='select_one')
			{
				if (document.frm.elements[i].checked)
				{
					j++;
					
					if (j>1)
					{
						alert("只能对一条记录删除！");
						j = 0;
						return false;
					}
					select_Value = document.frm.elements[i].value;
					select_Value = select_Value.substring(0,select_Value.length);					
					array_select = select_Value.split("#");
					var csm = array_select[0];					
				}
			}
		}
		j = 0;
		
		if (confirm("您已经选择删除参数"+csm+"，继续执行吗？"))
		{
			frm.action="action_host_SysConfig.jsp?action=delete&csm=" + csm;
			frm.submit();
			iTimerID = window.setInterval("CallPro()",1000);
		}
		else
		{
			return;
		}
	}
	else if (parname == "test2")
	{
		for(i=0; i<len; i++){
			if(document.frm.elements[i].name=='select_two')
			{
				if (document.frm.elements[i].checked)
				{
					flag=true;
					break;
				}
			}		
		}

		if(!flag)
		{
			window.alert("请选择一条记录！");
			return false;	
		}

		for (i=0;i<len;i++)
		{
			if (document.frm.elements[i].name=='select_two')
			{
				if (document.frm.elements[i].checked)
				{
					j++;
					
					if (j>1)
					{
						alert("只能对一条记录删除！");
						j = 0;
						return false;
					}
					select_Value = document.frm.elements[i].value;				
					array_select = select_Value.split("#");
					var lxbh = array_select[0];					
				}
			}
		}
		j = 0;
		
		if (confirm("您已经选择删除对象编号"+lxbh+"，继续执行吗？"))
		{
			frm.action="action_host_ObjType.jsp?action=delete&lxbh=" + lxbh;
			frm.submit();
			iTimerID = window.setInterval("CallPro()",1000);
		}
		else
		{
			return;
		}
	}
	else if (parname == "test3")
	{
		for(i=0; i<len; i++){
			if(document.frm.elements[i].name=='select_three')
			{
				if (document.frm.elements[i].checked)
				{
					flag=true;
					break;
				}
			}		
		}

		if(!flag)
		{
			window.alert("请选择一条记录！");
			return false;	
		}

		for (i=0;i<len;i++)
		{
			if (document.frm.elements[i].name=='select_three')
			{
				if (document.frm.elements[i].checked)
				{
					j++;
					
					if (j>1)
					{
						alert("只能对一条记录删除！");
						j = 0;
						return false;
					}
					select_Value = document.frm.elements[i].value;				
					array_select = select_Value.split("#");
					var gjbh = array_select[0];					
				}
			}
		}
		j = 0;
		
		if (confirm("您已经选择删除编号"+gjbh+"的告警，继续执行吗？"))
		{
			frm.action="action_host_WarnLevel.jsp?action=delete&gjbh=" + gjbh;
			frm.submit();
			iTimerID = window.setInterval("CallPro()",1000);
		}
		else
		{
			return;
		}
	}
	else if (parname == "test4")
	{
		for(i=0; i<len; i++){
			if(document.frm.elements[i].name=='select_four')
			{
				if (document.frm.elements[i].checked)
				{
					flag=true;
					break;
				}
			}		
		}

		if(!flag)
		{
			window.alert("请选择一条记录！");
			return false;	
		}

		for (i=0;i<len;i++)
		{
			if (document.frm.elements[i].name=='select_four')
			{
				if (document.frm.elements[i].checked)
				{
					j++;
					
					if (j>1)
					{
						alert("只能对一条记录删除！");
						j = 0;
						return false;
					}
					select_Value = document.frm.elements[i].value;				
					array_select = select_Value.split("#");
					var dxlx = array_select[0];
					var zbh = array_select[1];
					var zmc = array_select[2];					
				}
			}
		}
		j = 0;
		
		if (confirm("您已经选择删除属性组"+zmc+"，继续执行吗？"))
		{
			frm.action="action_host_AttributeGroup.jsp?action=delete&dxlx=" + dxlx + "&zbh=" + zbh;
			frm.submit();
			iTimerID = window.setInterval("CallPro()",1000);
		}
		else
		{
			return;
		}
	}
	else if (parname=="test5")
	{
		for(i=0; i<len; i++){
			if(document.frm.elements[i].name=='select_five')
			{
				if (document.frm.elements[i].checked)
				{
					flag=true;
					break;
				}
			}		
		}

		if(!flag)
		{
			window.alert("请选择一条记录！");
			return false;	
		}

		for (i=0;i<len;i++)
		{
			if (document.frm.elements[i].name=='select_five')
			{
				if (document.frm.elements[i].checked)
				{
					j++;
					
					if (j>1)
					{
						alert("只能对一条记录删除！");
						j = 0;
						return false;
					}
					select_Value = document.frm.elements[i].value;				
					array_select = select_Value.split("#");
					var dxlx = array_select[0];
					var ztbh = array_select[1];					
				}
			}
		}
		j = 0;
		
		if (confirm("您已经选择删除状态值："+ztbh+"，继续执行吗？"))
		{
			frm.action="action_host_Process_state.jsp?action=delete&dxlx=" + dxlx + "&ztbh=" + ztbh;
			frm.submit();
			iTimerID = window.setInterval("CallPro()",1000);
		}
		else
		{
			return;
		}
	}
}
//-->
</SCRIPT>

<%
	request.setCharacterEncoding("GBK");
	SysConfAct sca = new SysConfAct();
	Cursor cursor_sysConAct = sca.getSysConfCursor();
	Map map_sysConAct = cursor_sysConAct.getNext();

	ObjectTypeAct hoa = new ObjectTypeAct();
	Cursor cursor_objType = hoa.getObjType();
	Map map_objType = cursor_objType.getNext();

	AlertDefAct ada = new AlertDefAct();
	Cursor cursor_alertDef = ada.getAlertCursor();
	Map map_alertDef = cursor_alertDef.getNext();
%>

<form name="frm" method="post" target="childFrm">
<TABLE border=0 cellspacing=0 cellpadding=0 width="95%" align=center>
	 <tr><TD HEIGHT=21>&nbsp;</TD></tr>
  <tr>
	<TH>系统参数配置</TH>
  </tr>
  <tr>
	<td height="20" bgcolor=#ffffff></td>
  </tr>
  
  <tr>
    <TD class="column1"> 
      <table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" valign="middle">
        <tr> 
          <TH width="159" height="25" class="button_onblue" id="td1" onClick="location.href='javascript:showpage(1);'">系统参数</TH>
          <TH width="159" height="25" class="button_outblue" id="td2" onClick="location.href='javascript:showpage(2);'">对象类型</TH>
          <TH width="159" height="25" class="button_outblue" id="td3" onClick="location.href='javascript:showpage(3);'">告警等级</TH>
		  <TH width="159" height="25" class="button_outblue" id="td4" onClick="location.href='javascript:showpage(4);'">属性组</TH>
		  <TH width="159" height="25" class="button_outblue" id="td5" onClick="location.href='javascript:showpage(5);'">进程状态</TH>
          <td align="left"></td>
        </tr>
        <tr> 
          <td height="3" colspan="6" align="center" class="blue_tag_line"></td>
        </tr>
      </table>
    </TD>
  </tr>
  
  <tr>
	<TD id="test1" style="display:" bgcolor=#ffffff>
	  <table width="100%"  border="0" cellspacing="0" cellpadding="0">
	    <tr>
		  <td height="5" bgcolor=#ffffff>
		</tr>
		<tr>
		  <td bgcolor=#000000>
		   <table width="100%" border="0" cellspacing="1" cellpadding="2">
		   <tr>
			 <td class=column1 align=center>名称</td>
		     <td class=column1 align=center>值</td>
		     <td class=column1 align=center>描述</td>
		     <td class=column1 align=center>选择</td>
		   </tr>
		   <%
		     if (map_sysConAct == null) {
				out.println("<tr>");
				out.println("<td class=column1>&nbsp;</td>");
				out.println("<td class=column1>&nbsp;</td>");
				out.println("<td class=column1>&nbsp;</td>");
				out.println("<td class=column1>&nbsp;</td>");
				out.println("</tr>");
			 }
			 else {
				String csm = null;
				String csz = null;
				String cssm = null;
				String selectone_value = new String();				

				while (map_sysConAct != null) {
					csm = (String)map_sysConAct.get("csm");					
					csz = (String)map_sysConAct.get("csz");	
					cssm = (String)map_sysConAct.get("cssm");
					selectone_value = "'" + csm + "#" + csz + "#" + cssm + "'"; 
					out.println("<tr>");
					out.println("<td class=column1 align=center>" + csm + "</td>");
					out.println("<td class=column1 align=center>" + csz + "</td>");
					out.println("<td class=column1 align=center>" + cssm + "</td>");
					out.println("<td class=column1 align=center><input name=select_one type=checkbox value=" + selectone_value + "></td>");
					out.println("</tr>");
					map_sysConAct = cursor_sysConAct.getNext();
				}
				cursor_sysConAct.Reset();
			 }
		   %>
			<tr>
	          <td class=blue_foot align=right colspan="4">
			  <input type="button" value=" 新 建 " class="jianbian" onclick="NewForm('test1')">&nbsp;&nbsp;&nbsp;<input type="button" value=" 修 改 " class="jianbian" onclick="AmendForm('test1')">&nbsp;&nbsp;&nbsp;<input type="button" value=" 删 除 " class="jianbian" onclick="DelForm('test1')">&nbsp;&nbsp;&nbsp;
			</td>
		  </tr>
		</table>
	  </td>
	</tr>
  </table>
</TD>
</tr>

  <tr>
	<TD id="test2" style="display:none" bgcolor=#ffffff>
	   <table width="100%"  border="0" cellspacing="0" cellpadding="0">
	    <tr>
		  <td height="4" bgcolor=#ffffff>
		</tr>
		<tr>
		  <td bgcolor=#000000>
		   <table width="100%" border="0" cellspacing="1" cellpadding="2">
		   <tr>
			 <td class=column1 align=center>编号</td>
			 <td class=column1 align=center>名称</td>
			 <td class=column1 align=center>描述</td>
			 <td class=column1 align=center>选择</td>
		   </tr>
			<%
				if (map_objType == null) {
					out.println("<tr>");
					out.println("<td class=column1>&nbsp;</td>");
					out.println("<td class=column1>&nbsp;</td>");
					out.println("<td class=column1>&nbsp;</td>");
					out.println("<td class=column1>&nbsp;</td>");
					out.println("</tr>");
				}
				else {
					String lxbh = null;
					String lxmc = null;
					String lxsm = null;
					String selecttwo_value = new String();

					while (map_objType != null) {
						lxbh = (String)map_objType.get("lxbh");
						lxmc = (String)map_objType.get("lxmc");
						lxsm = (String)map_objType.get("lxsm");
						selecttwo_value = lxbh + "#" + lxmc + "#" + lxsm;

						out.println("<tr>");
						out.println("<td class=column1 align=center>" + lxbh + "</td>");
						out.println("<td class=column1 align=center>" + lxmc + "</td>");
						out.println("<td class=column1 align=center>" + lxsm + "</td>");
						out.println("<td class=column1 align=center><input name=select_two type=checkbox value='" + selecttwo_value + "'></td>");
						out.println("</tr>");
						map_objType = cursor_objType.getNext();
					}
					cursor_objType.Reset();
				}
			%>
			<tr>
	          <td class=blue_foot align=right colspan="4">
			  <input type="button" value=" 新 建 " class="jianbian" onclick="NewForm('test2')">&nbsp;&nbsp;&nbsp;<input type="button" value=" 修 改 " class="jianbian" onclick="AmendForm('test2')">&nbsp;&nbsp;&nbsp;<input type="button" value=" 删 除 " class="jianbian" onclick="DelForm('test2')">&nbsp;&nbsp;&nbsp;
			</td>
		  </tr>
		  </table>
		  </td>
		</tr>		
	  </table>
	</TD>
	</tr>

  <tr>
	<TD id="test3" style="display:none" bgcolor=#ffffff>
	<table width="100%"  border="0" cellspacing="0" cellpadding="0">
	    <tr>
		  <td height="3" bgcolor=#ffffff>
		</tr>
		<tr>
		  <td bgcolor=#000000>
		   <table width="100%" border="0" cellspacing="1" cellpadding="2">
			 <tr>
		       <td class=column1 align=center>等级</td>
		       <td class=column1 align=center>描述</td>
		       <td class=column1 align=center>是否自动处理</td>
		       <td class=column1 align=center>处理动作</td>
		       <td class=column1 align=center>选择</td>	
	        </tr>
		    <%
				if (map_alertDef == null) {
					out.println("<tr>");
					out.println("<td class=column1>&nbsp;</td>");
					out.println("<td class=column1>&nbsp;</td>");
					out.println("<td class=column1>&nbsp;</td>");
					out.println("<td class=column1>&nbsp;</td>");
					out.println("<td class=column1>&nbsp;</td>");
					out.println("</tr>");
				}
				else {
					String gjbh = null;
					String gjdj = null;
					String gjxx = null;
					String zdcl = null;
					String cldz = null;
					String selectthree_value = new String();
		
					while (map_alertDef != null) {
						gjbh = (String)map_alertDef.get("gjbh");
						gjdj = (String)map_alertDef.get("gjdj");
						gjxx = (String)map_alertDef.get("gjxx");
						zdcl = (String)map_alertDef.get("zdcl");
						cldz = (String)map_alertDef.get("cldz");
						selectthree_value = gjbh + "#" + gjdj + "#" + gjxx + "#" + zdcl + "#" + cldz;

						out.println("<tr>");
						out.println("<td class=column1 align=center>" + gjdj + "</td>");
						out.println("<td class=column1 align=center>" + gjxx + "</td>");
						
						if (zdcl.trim().equals("0")) {
							out.println("<td class=column1 align=center><input name=zdcl type=checkbox disabled>");
						}
						else {
							out.println("<td class=column1 align=center><input name=zdcl type=checkbox checked disabled>");
						}
						out.println("<td class=column1 align=center>" + cldz + "</td>");
						out.println("<td class=column1 align=center><input name=select_three type=checkbox value='" + selectthree_value + "'></td>");
						out.println("</tr>");
						map_alertDef = cursor_alertDef.getNext();
					}
					cursor_alertDef.Reset();
				}
			%>
			<tr>
	          <td class=blue_foot align=right colspan="5">
			  <input type="button" value=" 新 建 " class="jianbian"  onclick="NewForm('test3')">&nbsp;&nbsp;&nbsp;<input type="button" value=" 修 改 " class="jianbian"  onclick="AmendForm('test3')">&nbsp;&nbsp;&nbsp;<input type="button" value=" 删 除 " class="jianbian" onclick="DelForm('test3')">&nbsp;&nbsp;&nbsp;
			</td>
		  </tr>
		  </table>
		 </td>
	    </tr>		
      </table>
	</TD>
  </tr>

  <tr>
	<TD id="test4" style="display:none" bgcolor=#ffffff>
	  <table width="100%"  border="0" cellspacing="0" cellpadding="0">
		<tr>
		  <td height="2" bgcolor=#ffffff >
		</tr>
		<tr>
		  <td bgcolor=#000000>
		    <table width="100%" border="0" cellspacing="1" cellpadding="2">
		       <tr>
			     <td width="15%" align=center class=column1>对象类型</td>
				 <td width="85%" align=left class=column1>
				   <select name="objectType1" onchange="showChild('objectType1')">
					 <option value="-1">==请选择对象类型==</option>
					<%
						Cursor cur_objType = hoa.getObjType();
						map_objType = cur_objType.getNext();

						if (map_objType == null) {
							out.println("<option value=1>无数据</option>");
						}
						else {

							while (map_objType != null) {
								out.println("<option value=" + (String)map_objType.get("lxbh") + ">" + (String)map_objType.get("lxmc") + "</option>");
								map_objType = cur_objType.getNext();
							}
							cur_objType.Reset();
						}
					%>
				   </select>
				 </td>
			   </tr>
			</table>
		  </td>
		</tr>
		<tr>
		  <td>
			 <span id=attrgroup_list width=100% style="left:0px;top:0px;" border="0" cellspacing="0"></span>		    
		   </td>
		 </tr>

		 <tr>
		  <td bgcolor=#000000>
			<table width="100%" border="0" cellspacing="1" cellpadding="2">
			  <tr>
				<td class=blue_foot align=right>
				  <input type="button" value=" 新 建 " class="jianbian" onclick="NewForm('test4')">&nbsp;&nbsp;&nbsp;<input type="button" value=" 修 改 " class="jianbian" onclick="AmendForm('test4')">&nbsp;&nbsp;&nbsp;<input type="button" value=" 删 除 " class="jianbian" onclick="DelForm('test4')">&nbsp;&nbsp;&nbsp;
				</td>
			  </tr>  	
			</table>
		  </td>	
		</tr>
	    </table>
      </TD>
	</tr>
	
	<tr>
	   <TD id="test5" style="display:none" bgcolor=#ffffff>
		 <table width="100%"  border="0" cellspacing="0" cellpadding="0">
		  <tr>
		   <td height="1" bgcolor=#ffffff >
		  </tr>
		  <tr>
		   <td bgcolor=#000000>
			 <table width="100%" border="0" cellspacing="1" cellpadding="2">
			   <tr>
				<td width="15%" class=column1 align=center>对象类型</td>
				<td class=column1>
				<select name="objectType2" onchange="showChild('objectType2')">
				 <option value="-1">==请选择对象类型==</option>
					<%
						Cursor cur_objType2 = hoa.getObjType();
						map_objType = cur_objType2.getNext();

						if (map_objType == null) {
							out.println("<option value=1>无数据</option>");
						}
						else {
							while (map_objType != null) {
								out.println("<option value=" + (String)map_objType.get("lxbh") + ">" + (String)map_objType.get("lxmc") + "</option>");
								map_objType = cur_objType2.getNext();
							}
							cur_objType2.Reset();
						}
					%>
			    </select>
				 </td>
			   </tr>
			</table>
		  </td>
		</tr>
		<tr>
		  <td>
			    <div id=process_state></div>			  	    
		   </td>
		 </tr>

		 <tr>
		  <td bgcolor=#000000>
			<table width="100%" border="0" cellspacing="1" cellpadding="2">
			  <tr>
				<td class=blue_foot align=right>
				  <input type="button" value=" 新 建 " class="jianbian" onclick="NewForm('test5')">&nbsp;&nbsp;&nbsp;<input type="button" value=" 修 改 " class="jianbian" onclick="AmendForm('test5')">&nbsp;&nbsp;&nbsp;<input type="button" value=" 删 除 " class="jianbian" onclick="DelForm('test5')">&nbsp;&nbsp;&nbsp;
				</td>
			  </tr>  	
			</table>
		  </td>	
		</tr>
	    </table>
      </TD>
	</tr>

	<tr><td><IFRAME ID=childFrm name="childFrm" SRC="" STYLE="display:none"></IFRAME></td></tr>
</TABLE>
</form>
<%@ include file="../foot.jsp"%> 