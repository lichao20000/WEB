<%@ include file="../timelater.jsp"%>

<%@ page contentType="text/html;charset=GBK"%>

<title>编辑设备实例</title>

<SCRIPT LANGUAGE="JavaScript">
<!--

var isCall=0;
var iTimerID;

function CallPro()
{
	switch (parseInt(isCall,10)) 
	{
		case 0:
		{
			alert("性能定义操作失败，请重新操作！");
			window.clearInterval(iTimerID);	
			isCall=0;
			break;
		}
		case 1:
		{
			alert("实例修改成功，调用后台接口成功！");
			window.clearInterval(iTimerID);	
			isCall=0;
			break;
		}
		case -1:
		{
			alert("实例修改成功，调用后台接口失败！");
			window.clearInterval(iTimerID);	
			isCall=0;
			break;
		}
	}
}
//-->
</SCRIPT>
<%@ include file="../head.jsp"%>
<%@ include file="../toolbar.jsp"%>


<form name="frm" method="post" target="childFrm">
  <table width="100%"  border="0" align="center" cellpadding="0" cellspacing="0">
    <tr>
      <td>	
		<table width="100%"  border="0" cellspacing="0" cellpadding="2">
		  <TR>
			<TH >性能实例管理</TH>
		  </TR>
		  
		  <tr>
			<td class=column1> <%@ include file="./pm_instanceHead.jsp"%> 
			</td>
		  </tr>

		  <tr>
			<td class=column1><%@ include file="./pm_instanceTabel.jsp"%></td>
		  </tr>
		</table>	  
	   </td>
      </tr>
      <tr>
	 <td><IFRAME ID=childFrm name="childFrm" STYLE="display:none"></IFRAME></td>
    </tr>
  </table>
</form>
<%@ include file="../openfoot.jsp"%>