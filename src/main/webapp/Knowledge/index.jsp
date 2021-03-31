<%@page contentType="text/html;charset=GBK" pageEncoding="GBK"%>
<%@include file="../timelater.jsp"%>
<%
request.setCharacterEncoding("GBK");
String key = request.getParameter("key");
String searchSubjectOnly_str = request.getParameter("searchSubjectOnly");
String creator = request.getParameter("creator");
String start_str = request.getParameter("start");
String end_str = request.getParameter("end");

if(key==null)
    key = "";
boolean searchSubjectOnly;
if("false".equals(searchSubjectOnly_str))
    searchSubjectOnly = false;
else
    searchSubjectOnly = true;
if(creator==null)
    creator = "";
long start = 0;
try{
    start = Long.parseLong(start_str);
}
catch(Exception e){
}
if(start==0)
    start_str = "";
else
    start_str = new SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date(start));
long end = 0;
try{
    end = Long.parseLong(end_str);
}
catch(Exception e){
}
if(end==0)
    end_str = "";
else
    end_str = new SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date(end));
%>
<%@ include file="../head.jsp"%>
<%@ include file="../toolbar.jsp"%>


<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.linkage.litms.knowledge.KnowledgeCommon"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.linkage.litms.knowledge.Knowledge"%>
<script language="javascript"><!--
function redirect(){
    var p = document.getElementById("page").value;    
    if(IsNull(p,"页数") && IsNumber(p,"页数")){
        window.location="?key=<%=key%>&searchSubjectOnly=<%=searchSubjectOnly%>&creator=<%=creator%>&start=<%=start%>&end=<%=end%>&p="+p;
    }
}

function checkForm(){
    var obj = document.frm;
    obj.startDate.value = Trim(obj.startDate.value);
    obj.endDate.value = Trim(obj.endDate.value);
    if(obj.startDate.value!="" && !checkDate_(obj.startDate.value)){
        alert("开始日期不正确。");
        return false;
    }
    else if(obj.endDate.value!="" && !checkDate_(obj.endDate.value)){
        alert("结束日期不正确。");
        return false;
    }
    else{
        if(obj.startDate.value!="")
            obj.start.value = DateToLong(obj.startDate.value);
        if(obj.endDate.value!="")
            obj.end.value = DateToLong(obj.endDate.value);

        if(obj.startDate.value!="" && obj.endDate.value!="" && obj.start.value>obj.end.value){
            alert("开始日期不能大于结束日期。");
            return false;
        }
    }
    return true;
}

function DateToLong(str){
    var pos = str.indexOf("-");
    str = str.substring(pos+1)+"-"+str.substring(0,pos);
    return new Date(str).getTime();
}

function isNumber(str){
    for(var i=0;i<str.length;i++)
        if(str.charAt(i)<'0'||str.charAt(i)>'9')
            return false;
    return true;
}

function checkDate_(date_){
    try{
        //第一个"-"的位置
        var loc=date_.indexOf("-");
        //无"-"
        if(loc==-1)
            return false;
        //年
        var str=date_.substring(0,loc);
        //年不是四位或不是数值
        if(str.length!=4 || !isNumber(str))
            return false;
        var year=parseInt(str,10);
        //第二个"-"的位置
        var loc2=date_.indexOf("-",loc+1);
        //无第二个"-"
        if(loc2==-1)
            return false;
        //月
        str=date_.substring(loc+1,loc2);
        //月超过2位或不是数值
        if(str.length>2 || !isNumber(str))
            return false;
        var month=parseInt(str,10);
        //月<1或月>12
        if(month<1||month>12)
            return false;
        //第二个"-"不是最后一个"-"
        if(loc2!=date_.lastIndexOf("-"))
            return false;
        //日
        str=date_.substring(loc2+1);
        //日超过2位或不是数值
        if(str.length>2 || !isNumber(str))
            return false;
        var day=parseInt(str,10);
        //该月天数
        var dayInMonth;
        switch(month){
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                dayInMonth=31;
                break;
            case 2:
                //不能被4整除 或 能被100整除不能被400整除
                if(year%4!=0||year%100==0&&year%400!=0)
                        dayInMonth=28;
                else
                        dayInMonth=29;
                break;
            default:
                dayInMonth=30;
        }
        //天小于0或天大于该月天数
        if(day<0||day>dayInMonth)
            return false;
    }
    catch(e){
        return false;
    }
    return true;
}
//--></script>
<body>
<br>
<table align="center" border=0 cellspacing=1 cellpadding=2 width="96%" bgcolor="#000000">
<tr bgcolor=#FFFFFF><td colspan="3">
    <table width="100%">
    <tr><td>
    <!-- 
    <input type="button" onclick="javascript:window.location='edit.jsp';" value="添  加" class=jianbian style="width:60px">
     -->
    </td>
    <form name="frm" action="index.jsp" method="post" onsubmit="javascript:return checkForm();">
    <td align=right>
    关键字：<input type="text" name="key" value="<%=key%>" class=bk>
    <select name="searchSubjectOnly">
        <option value="true">仅搜索主题</option>
        <option value="false">搜索主题和内容</option>
    </select>
    创建者：<select name="creator">
        <option value="">全部</option>
<%
ArrayList list = KnowledgeCommon.getUserList();
for(int i=0;i<list.size();i++){
    String username = (String)list.get(i);
%>
        <option value="<%=username%>"><%=username%></option>
<%
}
%>
    </select>
<script language="javascript"><!--
document.frm.searchSubjectOnly.value="<%=searchSubjectOnly%>";
document.frm.creator.value="<%=creator%>";
//--></script>
    开始日期：<input type="text" name="startDate" value="<%=start_str%>" class=bk style="width:75px">
    <input type="button" value="▼" class="btn" onClick="showCalendar('day',event)">
    <input type="hidden" name="start">
    结束日期：<input type="text" name="endDate" value="<%=end_str%>" class=bk style="width:75px">
    <input type="button" value="▼" class="btn" onClick="showCalendar('day',event)">
    <input type="hidden" name="end">
    <input type="submit" value="搜  索" class="btn" style="width:60px">
    </td>
    </tr>
    </table>
</td></tr>
    <tr>
    <th width="70%">主题</th>
    <th width="15%">创建人</th>
    <th width="15%">创建时间</th>
    </tr>
<%
list = KnowledgeCommon.getKnowledgeList(key,searchSubjectOnly,creator,start/1000,end/1000);

int count = list.size();
//每页显示记录数目
final int PAGESIZE = 30;
//总页数
int pagecount = count/PAGESIZE;
if(count%PAGESIZE!=0)
    pagecount++;
//当前页
int p = 0;
try{
    p = Integer.parseInt(request.getParameter("p"));
}
catch(Exception e){
}

if(pagecount==0)
    p = 0;
else if(p<1)
    p = 1;
else if(p>pagecount)
    p = pagecount;
if(p!=0){
    for(int i=(p-1)*PAGESIZE;i<p*PAGESIZE&&i<count;i++){
        Knowledge knowledge = (Knowledge)list.get(i);
        java.util.Date date = new java.util.Date(knowledge.createtime*1000);
        String str = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
%>
        <tr bgcolor="#FFFFFF">
        <td><a href="view.jsp?serialno=<%=knowledge.serialno%>&gather_id=<%=knowledge.gather_id%>"><%=knowledge.subject%></a></td>
        <td align="center"><%=knowledge.creator%></td>
        <td align="center"><%=str%></td>
        </tr>
<%
    }
}
%>
        <tr bgcolor="#FFFFFF">
        <td colspan=3 align=right>
	<b><%=PAGESIZE%>条/页&nbsp;共<%=pagecount%>页&nbsp;第<%=p%>页
<%
if(pagecount<=1){
%>
    首页
<%
}
else{
%>
    <a href="?key=<%=key%>&searchSubjectOnly=<%=searchSubjectOnly%>&creator=<%=creator%>&start=<%=start%>&end=<%=end%>&p=1">首页</a>
<%
}
if(p<=1){
%>
    上一页
<%
}
else{
%>
    <a href="?key=<%=key%>&searchSubjectOnly=<%=searchSubjectOnly%>&creator=<%=creator%>&start=<%=start%>&end=<%=end%>&p=<%=p-1%>">上一页</a>
<%
}
if(p==pagecount || pagecount<=1){
%>
    下一页
<%
}
else{
%>
    <a href="?key=<%=key%>&searchSubjectOnly=<%=searchSubjectOnly%>&creator=<%=creator%>&start=<%=start%>&end=<%=end%>&p=<%=p+1%>">下一页</a>
<%
}
if(pagecount<=1){
%>
    尾页
<%
}
else{
%>
    <a href="?key=<%=key%>&searchSubjectOnly=<%=searchSubjectOnly%>&creator=<%=creator%>&start=<%=start%>&end=<%=end%>&p=<%=pagecount%>">尾页</a>
<%
}
%>
	</b><input class="btn" type="submit" value="转到" onclick="javascript:redirect();">
        <input class="bk" type="text" id="page" size="3">
	<b>页</b>
        </td>
        </tr>
</table>
<br>
<%@include file="../foot.jsp"%>
