<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ page import ="com.linkage.litms.webtopo.snmpgather.ReadDevicePortInfo" %>

<%
	request.setCharacterEncoding("GBK");
	
	String device_id= request.getParameter("device_id");
	String seltype=request.getParameter("seltype");
	seltype=(seltype==null)?"1":seltype;

	ReadDevicePortInfo rpi=new ReadDevicePortInfo();
	rpi.setDevice_ID(device_id);
	rpi.setAccountInfo(user.getAccount(),user.getPasswd());
	rpi.setValueType(Integer.parseInt(seltype));

	ArrayList dataList=rpi.getDeviceInfo();
	String script="setTypeSelected('"+seltype+"');";

%>
<%@ include file="../head.jsp"%>
<%@page import="java.util.ArrayList"%>
<TITLE>　</TITLE>

<SCRIPT LANGUAGE="JavaScript" src="../Js/CheckForm.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript">
<!--
	function SEL()
	{
		var value=getRadioValue("radio_sel");	
	
		if(typeof(value)=="undefined")
		{
			alert("请选择端口");
			return;
		}
		opener.setPortProperty(frm.seltype.selectedIndex,value);
		window.close();
	
	}

	function getRadioValue(radioName) {
	  var collection;	 
	  collection = document.all[radioName];
	  for (i=0;i<collection.length;i++) {
		 if (collection[i].checked)
		  {
			return(collection[i].value);
		  }
	  }
	}

	function setTypeSelected(index)
	{
		frm.seltype.options[index].selected=true;	
	
	}



	


//-->
</SCRIPT>
<%@ include file="../toolbar.jsp"%>

<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">

<TR><TD>
	<FORM NAME="frm" METHOD="post" action="SelDevPort.jsp">
         <table width="100%" height="30" border="0" align="center" cellpadding="0" cellspacing="0"  class="blue_gargtd">
          <tr> 
            <td width="162"><div align="center" class="title_bigwhite">选择端口</div></td>
            <td><img src="../images/attention_2.gif" width="15" height="12">&nbsp;双击选择端口</td>           
          </tr>
        </table>
        <TABLE width="100%" border=0 cellspacing=0 cellpadding=0 align="center">
			<TR bgcolor=#FFFFFF>
				<TD>
				<input type="hidden" name="device_id" value="<%=device_id%>">
				请选择端口标识类型:
				<select name="seltype" onchange="javascript:frm.submit();">
					<option value="0">端口索引</option>
					<option value="1">端口描述</option>
					<option value="2">端口名字</option>
					<option value="3">端口IP</option>
				</select>
					
				</TD>
				
			</TR>
			<TR>
				<TD bgcolor=#999999>
					<div style="height:200;overflow:auto;">
					<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
					<TR class="blue_title"> 
					  <TD>选择</TD>
					  <TD>端口标识</TD>					  				 
					</TR>
					<%
						for(int i=0;i<dataList.size();i++)
						{
							out.println("<tr bgcolor=#FFFFFF>");
							out.println("<td><input type='radio' name='radio_sel' value='"+dataList.get(i)+"' ondblclick=\"javascript:SEL();\"></td>");
							out.println("<td>"+dataList.get(i)+"</td>");
							out.println("</tr>");
						
						}
						
					
					%>
					
					</TABLE>
					<div>
				</TD>
			</TR>

			<TR bgcolor="#FFFFFF" > 
                 <TD colspan="4" align="right" CLASS="blue_foot"> 
                    <INPUT TYPE="button" value="选　择" class="jianbian" name="sel"  onclick="javascript:SEL();">
				</TD>
			</TR>
		</TABLE>
	</FORM>
</TD></TR>
<TR><TD HEIGHT=20>&nbsp;<IFRAME ID=childFrm SRC="" STYLE="display:none"></IFRAME></TD></TR>
</TABLE>
<SCRIPT LANGUAGE="JavaScript">
<!--
	<%=script%>
//-->
</SCRIPT>
<%@ include file="../foot.jsp"%>
