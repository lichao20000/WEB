<%@page contentType="text/html;charset=GBK"%>
<%@ include file="../../timelater.jsp"%>
<%
	request.setCharacterEncoding("GBK");
	String tipMsg = "";
	if(session.getAttribute("nDay")!=null){
		int nDay = (Integer)session.getAttribute("nDay");
		if(nDay > 3 && nDay <= 7 ){
			tipMsg = "alert('您的密码还有"+ nDay +"天过期')";
		}
		//session.removeAttribute("nDay");
	}
%>
<HTML>
	<HEAD>
		<TITLE><%=com.linkage.litms.LipossGlobals.getLipossName()%></TITLE>
	</HEAD>
<script type="text/javascript">
window.onload=function(){
	
	var passwordexpire='<%=session.getAttribute("passwordexpire")%>';
	if(passwordexpire=='true'){
		var host = window.location.host;
		var href = window.location.href;
		var pathName = window.location.pathname;
		var index=find(pathName,'/',1);
		var path = pathName.substring(0,index);
		var url = "http://" + host + path + "/system/OuterModifyPwdForm.jsp"; 
		if(href.indexOf("https") != -1)
		{
			url = "https://" + host + path + "/system/OuterModifyPwdForm.jsp";
		}
		location.href=url;
	}else{
			<%=tipMsg %>
	}
}


function find(str,cha,num){
    var x=str.indexOf(cha);
    for(var i=0;i<num;i++){
        x=str.indexOf(cha,x+1);
    }
    return x;
    }
    
function resize()
{
	if(leftPage.document.body.clientWidth>1)
		rightPage.showImg.style.display='none';
}
</script>

	<FRAMESET ROWS="57,*" border="0" FRAMESPACING="0">
		<FRAME NORESIZE=true SRC="top.jsp" scrolling="no" frameborder=0
			TOPMARGIN="0" LEFTMARGIN="0" MARGINHEIGHT="0" MARGINWIDTH="0"></frame>
		<FRAMESET COLS="185,*" id="frame" title="改变大小" bordercolor="#639ace"
			border="5" frameborder="yes" FRAMESPACING="0" TOPMARGIN="0"
			LEFTMARGIN="0" MARGINHEIGHT="0" MARGINWIDTH="0">
			<FRAME SRC="html/left.jsp" id="leftPage" TOPMARGIN="0" LEFTMARGIN="0"
				scrolling="no" MARGINHEIGHT="0" MARGINWIDTH="0" FRAMEBORDER="0"
				onresize="resize()"></FRAME>
			<FRAME SRC="html/right.jsp" id="rightPage" TOPMARGIN="0"
				LEFTMARGIN="0" scrolling="no" MARGINHEIGHT="0" MARGINWIDTH="0"
				FRAMEBORDER="no" bordercolor="#639ace"></FRAME>
		</FRAMESET>
	</FRAMESET>
	
</HTML>
