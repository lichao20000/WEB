<%--
modified by yanhj 2006-12-22 样式调整.
--%>

<%@ include file="../../timelater.jsp"%>
<%@page import="com.linkage.litms.system.dbimpl.*"%>
<%@page import="com.linkage.litms.system.*"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%
request.setCharacterEncoding("GBK");
%>
<SCRIPT LANGUAGE="JavaScript">
function show()
{
        parent.frame.cols="185,*";
        document.all("tdmenu").width="1";
        showImg.style.display="none";
}

var isNC6 = (document.getElementById && !document.all)?true:false;
var isIE = (document.all)?true:false;

function resize()
{
        try{
        var obj = document.getElementById("viewPage");
        if(isNC6) obj.height=window.innerHeight-20;
        window.top.frames[2].document.getElementById('tdmenu').width="1";
        }catch(e){}
}

function writeDate()
{
        date=new Date();
        year=date.getFullYear();
        if(year<2000) year+=1900;
        month=date.getMonth()+1;
        day=date.getDate();
        document.write(year);
        document.write(".");
        document.write(month>9?month:("0"+month));
        document.write(".");
        document.write(day>9?day:("0"+day));
}

function GoLeft(v) {
        var page = v;
        parent.leftFrame.location.href=page;
}

//用于转向不同目录树，切换主界面呈现
function openWin(tree_id,main_page){
    var mode = "<%=LipossGlobals.getLipossProperty("ClusterMode.mode")%>";
    var reportSysID = "<%=LipossGlobals.getLipossProperty("ClusterMode.reportSysID")%>";
    if('2'==mode&&tree_id==reportSysID)
    {
        window.open("<s:url value='/itms/report/ReportSubSystemAction.action'/>","",
            "left=100,top=100,width=750,height=450,resizable=no,scrollbars=yes");
    }
    else
    {
        //展开左边树形结构
        show();
        //treeview
        parent.window.frames[1].document.all('treeView').src="../treeview.jsp?tree_id="+ tree_id;
        <%--main page 如果是itv系统，则使用项目名相对路径，如果不是itv，则保留原逻辑 --%>
        <%
        String sName = LipossGlobals.getLipossName();
        if (sName != null && sName.toUpperCase().contains("ITV"))
        {
            out.print("document.all('viewPage').src=\"<s:url value='/'/>\" + main_page;");
        }
        else
        {
             out.print("document.all('viewPage').src='../'+ main_page;");
        }
        %>
    }
}

function hide()
{
        if(isIE)
        {
                var fm = window.top.document.getElementById('frame');
                fm.cols = "1,*";
                window.top.frames[2].document.getElementById('showImg').style.display='inline';
                window.top.frames[2].document.getElementById('tdmenu').width="209";
        }
}

function welcome(){
    var mode = "<%=LipossGlobals.getLipossProperty("ClusterMode.mode")%>";
    var reportSysID = "<%=LipossGlobals.getLipossProperty("ClusterMode.reportSysID")%>";
    document.all('viewPage').src = "../index_welcome.jsp";
    if('3'==mode)
    {
        openWin(reportSysID,"inside_baobiao.html");
    }
}

</SCRIPT>

<%@page import="java.util.Map"%>
<%@page import="com.linkage.litms.tree.Module"%>
<%@page import="com.linkage.litms.common.database.Cursor"%>
<html>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<!--<LINK REL="STYLESHEET" HREF="../css/main.css">-->
<link href="../../css/liulu.css" rel="stylesheet" type="text/css">
<link href="../../css/css_ico.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript">
function showBG(obj){
	
	 $(".moduleMenu table td").each(function(){
		    if($(obj).attr("id") == $(this).attr("id")){
		    	$(this).addClass("buttonover").removeAttr("onmouseout","");
		    	//$(this).addClass("buttonover").removeAttr("onMouseOut","");
 		    }else{
		    	$(this).attr("class","button");
		    	//$(this).attr("onMouseOut","this.className='button';");
		    	$(this).attr("onmouseout","this.className='button';");
		    }
	 });
}
 
</script>
<body onload='resize();welcome();' onresize='resize()' leftmargin=0>
<table border="0" cellpadding="0" cellspacing="0" width="100%" height="100%">
        <tr>
           <td height="30" background="../../images/button_line.jpg">
                <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td id ="tdmenu" width="209">
                        <IMG SRC="../img/showtoc.gif" title="显示菜单" id="showImg" BORDER=0
                                style="display:none;cursor: pointer; cursor: hand;"
                                onmouseover="status='显示菜单'" onmouseout="status=''" onclick="show()"></IMG>
                  </td>
                  <td class="moduleMenu">
                        <table border="0" cellspacing="0" cellpadding="0" align=left>
                                <tr>
                                <%
                                        //获得当前用户权限第一层权限主菜单
                                        Module module = new Module();
                                        Cursor lModule = module.getModuleInfoListByRoleId(String.valueOf(user.getRoleId()));
                                        if(lModule == null) lModule = new Cursor();
                                        Map mModule = lModule.getNext();
                                        while(mModule!=null){
                                %>
                                <% if(!LipossGlobals.inArea("nx_lt")){ %>
                                <td align="center" class=button height="38"
                                        onClick="openWin('<%=mModule.get("module_id") %>','<%=mModule.get("module_url") %>');"
                                        onMouseOver="this.className='buttonover';"
                                        onMouseOut="this.className='button';">
                                        <font style="filter:glow(color=#FFFFFF,strength=2);zoom:1;" align=center>
                                            <%=mModule.get("module_name")%></font>
                                </td>
                                <%} else{ %>
                                <td align="center" class=button height="38" id="<%=mModule.get("module_id") %>"
                                        onClick="openWin('<%=mModule.get("module_id") %>','<%=mModule.get("module_url") %>');showBG(this)"
                                        onMouseOver="this.className='buttonover';"
                                        onMouseOut="this.className='button';">
                                        <font style="filter:glow(color=#FFFFFF,strength=2);zoom:1;" align=center>
                                            <%=mModule.get("module_name")%></font>
                                 
                                <%} %>
                                <%
                                                mModule = lModule.getNext();
                                        }
                                        lModule.Reset();
                                        //clear
                                        module = null;
                                %>
              </tr>
                        </table>
                  </td>
          <td width="10">&nbsp;</td>
        </tr>
                </table></td>
          </tr>
        <tr>
                <td colspan="2" width="100%">
                        <iframe src="" id="viewPage"
                                frameborder=0 height=100% width=100% border="0" SCROLLING="AUTO">
                </td>
        </tr>
        <tr>
                <td colspan="2" >
                </td>
        </tr>
</table>

</body>
</html>