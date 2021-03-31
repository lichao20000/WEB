<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ page import ="com.linkage.litms.vipms.action.BaseCustomerAct" %>

<%
	request.setCharacterEncoding("GBK");
	
	String customname=request.getParameter("customname");
	customname=(customname==null)?"":customname;
	BaseCustomerAct bca=new BaseCustomerAct();

	Cursor cursor=bca.getCustomerList(request);

	Map fields=cursor.getNext();
	


%>
<%@ include file="../head.jsp"%>
<TITLE>　</TITLE>

<SCRIPT LANGUAGE="JavaScript" src="../Js/CheckForm.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript">
<!--
	function SEL()
	{
		var value=getRadioValue("radio_sel");
		if(typeof(value)=="undefined")
		{
			alert("请选择大客户");
			return;
		}
		var values=value.split("|||");
		opener.setUserProperty(values[0],values[1]);
		window.close();
	
	}

	function getRadioValue(radioName) {
	  var collection;	 
	  collection = document.all[radioName];
	  if(typeof(collection.length)!="undefined")
		{
		  for (i=0;i<collection.length;i++) {
			 if (collection[i].checked)
			  {
				return(collection[i].value);
			  }
		  }
		}
		else
		{
			return collection.value;
		
		}
	}


//-->
</SCRIPT>
<%@ include file="../toolbar.jsp"%>

<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">

<TR><TD>
	<FORM NAME="frm" METHOD="post" action="SelVpnUser.jsp">
         <table width="100%" height="30" border="0" align="center" cellpadding="0" cellspacing="0"  class="blue_gargtd">
          <tr> 
            <td width="162"><div align="center" class="title_bigwhite">选择大客户</div></td>
            <td><img src="../images/attention_2.gif" width="15" height="12">&nbsp;双击选择大客户</td>           
          </tr>
        </table>
        <TABLE width="100%" border=0 cellspacing=0 cellpadding=0 align="center">
			<TR bgcolor=#FFFFFF>
				<TD>请输入用户名:<input type="text" class=bk name="customname" value="<%=customname%>">
					<input type="button" class="jianbian" name="query" value="查询" onclick="javascript:frm.submit();">
				</TD>
				
			</TR>
			<TR>
				<TD bgcolor=#999999>
					<div style="height:200;overflow:auto;">
					<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
					<TR class="blue_title"> 
					  <TD>选择</TD>
					  <TD>客户编号</TD>
					  <TD>客户名称</TD>					 
					</TR>
					<%
						while(fields!=null)
						{
							out.println("<tr bgcolor=#FFFFFF>");
							out.println("<td><input type='radio' name='radio_sel' value='"+fields.get("customid")+"|||"+fields.get("customname")+"' ondblclick=\"javascript:SEL();\"></td>");
							out.println("<td>"+fields.get("customid")+"</td>");
							out.println("<td>"+fields.get("customname")+"</td>");
							out.println("</tr>");
						
							fields=cursor.getNext();
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
	
									
                
		
			</TD>
		</TR>
	</TABLE>
	</FORM>
</TD></TR>
<TR><TD HEIGHT=20>&nbsp;<IFRAME ID=childFrm SRC="" STYLE="display:none"></IFRAME></TD></TR>
</TABLE>
<%@ include file="../foot.jsp"%>
