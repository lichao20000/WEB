<%@ include file="../timelater.jsp"%>
<%@ page import="java.util.*"%>
<jsp:useBean id="areaManage" scope="request" class="com.linkage.litms.system.dbimpl.AreaManageSyb"/>
<%@ page contentType="text/html;charset=GBK"%>
<%
request.setCharacterEncoding("GBK");
String area_pid = request.getParameter("area_pid");
ArrayList m_AreaPidList = new ArrayList();
ArrayList m_AreaIdList = new ArrayList();

m_AreaPidList.clear();
m_AreaIdList.clear();
if(user.getAccount().equals("admin")){
	
	m_AreaPidList.add("0");
}else{
	m_AreaPidList.add(area_pid);
}

m_AreaIdList = areaManage.getAreaIdsByAreaPid(m_AreaPidList,m_AreaIdList);
m_AreaIdList.add(String.valueOf(user.getAreaId()));

String m_AreaIdStr = m_AreaIdList.toString();
m_AreaIdList.clear();
m_AreaIdList = null;

m_AreaPidList.clear();
m_AreaPidList = null;
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<HTML>
<HEAD>
<TITLE>选择区域</TITLE>
<META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=gb_2312">
<link rel="stylesheet" href="../css/css_green.css" type="text/css">

</HEAD>

<BODY scrolling="no" topmargin="0">
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
<TR><TD>
	<TABLE width="95%" border=0 cellspacing=0 cellpadding=0 align="center">
		<TR>
			<TD bgcolor=#999999>
			  <TABLE border=0 cellspacing=1 cellpadding=2 width="100%" id="idData">
                <TR> 
                  <TD bgcolor=#ffffff align=center width="45%"> 
                    <table width="80%" border="0" align="center">
                      <tr> 
                        <td> 
													<script language="JavaScript" src="../Js/tree_maker.js"></script>
													<script language="JavaScript" src="../Js/tree_res.js"></script>
													<div id="idTreeView" XMLSrc="../Resource/area_xml.jsp?" onselectstart='return false' style="overflow:auto;width:<%=request.getParameter("width")%>;height:400;padding:2pt 2pt 2pt 2pt" > 
													<script language="JavaScript">
														init_tree();
													</script>													
												    </div>
                        </td>
                      </tr>
                    </table>
                  </TD>
                </TR>
                <TR> 
                  <TD class=green_foot align=right colspan="3"> 
                    <div align="center"> 
                    	<INPUT TYPE="hidden" value="" name=area_name id=area_name class=btn>
                    	<INPUT TYPE="hidden" value="" name=area_id id=area_id class=btn>
                      <INPUT TYPE="button" value="选择区域" onclick=selectAreaItem() class=btn>
                      <INPUT TYPE="hidden" value="<%=m_AreaIdStr%>" name=areaIdMore id=areaIdMore class=btn>
                      &nbsp; &nbsp; </div>
                  </TD>
                </TR>
              </TABLE>
		  </TD>
		</TR>
	</TABLE>
</TD></TR>
<TR>
    <TD HEIGHT=20>&nbsp;<IFRAME ID=childFrm SRC="" STYLE="display:none"></IFRAME></TD>
  </TR>
</TABLE>
</BODY>
</HTML>


<SCRIPT LANGUAGE="JavaScript">
<!--
function selectAreaItem(){
	if(tree.getSelect()){
		var area_id = document.all("area_id").value;
		var area_name = document.all("area_name").value;
		var areaIdMore = document.all("areaIdMore").value;
		
		if(areaIdMore.indexOf("["+ area_id +",")!=-1 || areaIdMore.indexOf(", "+ area_id +"]")!=-1 || areaIdMore.indexOf(", "+ area_id +",")!=-1){
			try{
				opener.document.all("area_name").value = area_name;
				opener.document.all("area_id").value = area_id;
			}catch(e){

			}
			var returnObj = new Object();
			returnObj.area_name = area_name;
			returnObj.area_id = area_id;
			window.returnValue = returnObj;
			window.close();	
		}else{
			alert("您选择区域不在权限范围以内");
		}
	}else{
		alert("请先选择区域节点，然后点击确定选择按钮");
	}
}

//点击结点时变更隐藏参数内容
tree.callback_click=function my_click(nodeID){
	//alert(nodeID);
  var node = tree.getNode(nodeID);
	var v = node.value;
	
	//判断是否为根节点
	if(v == 1){
		return true;
	}
	
	var area_id = v.substring(0,v.indexOf(","));
	document.all("area_id").value = area_id;
	document.all("area_name").value = node.text;
	
  return true;//返回true调用默认的click,false 取消click
}
	
//-->
</SCRIPT>
