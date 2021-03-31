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
    if(IsNull(p,"ҳ��") && IsNumber(p,"ҳ��")){
        window.location="?key=<%=key%>&searchSubjectOnly=<%=searchSubjectOnly%>&creator=<%=creator%>&start=<%=start%>&end=<%=end%>&p="+p;
    }
}

function checkForm(){
    var obj = document.frm;
    obj.startDate.value = Trim(obj.startDate.value);
    obj.endDate.value = Trim(obj.endDate.value);
    if(obj.startDate.value!="" && !checkDate_(obj.startDate.value)){
        alert("��ʼ���ڲ���ȷ��");
        return false;
    }
    else if(obj.endDate.value!="" && !checkDate_(obj.endDate.value)){
        alert("�������ڲ���ȷ��");
        return false;
    }
    else{
        if(obj.startDate.value!="")
            obj.start.value = DateToLong(obj.startDate.value);
        if(obj.endDate.value!="")
            obj.end.value = DateToLong(obj.endDate.value);

        if(obj.startDate.value!="" && obj.endDate.value!="" && obj.start.value>obj.end.value){
            alert("��ʼ���ڲ��ܴ��ڽ������ڡ�");
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
        //��һ��"-"��λ��
        var loc=date_.indexOf("-");
        //��"-"
        if(loc==-1)
            return false;
        //��
        var str=date_.substring(0,loc);
        //�겻����λ������ֵ
        if(str.length!=4 || !isNumber(str))
            return false;
        var year=parseInt(str,10);
        //�ڶ���"-"��λ��
        var loc2=date_.indexOf("-",loc+1);
        //�޵ڶ���"-"
        if(loc2==-1)
            return false;
        //��
        str=date_.substring(loc+1,loc2);
        //�³���2λ������ֵ
        if(str.length>2 || !isNumber(str))
            return false;
        var month=parseInt(str,10);
        //��<1����>12
        if(month<1||month>12)
            return false;
        //�ڶ���"-"�������һ��"-"
        if(loc2!=date_.lastIndexOf("-"))
            return false;
        //��
        str=date_.substring(loc2+1);
        //�ճ���2λ������ֵ
        if(str.length>2 || !isNumber(str))
            return false;
        var day=parseInt(str,10);
        //��������
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
                //���ܱ�4���� �� �ܱ�100�������ܱ�400����
                if(year%4!=0||year%100==0&&year%400!=0)
                        dayInMonth=28;
                else
                        dayInMonth=29;
                break;
            default:
                dayInMonth=30;
        }
        //��С��0������ڸ�������
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
    <input type="button" onclick="javascript:window.location='edit.jsp';" value="��  ��" class=jianbian style="width:60px">
     -->
    </td>
    <form name="frm" action="index.jsp" method="post" onsubmit="javascript:return checkForm();">
    <td align=right>
    �ؼ��֣�<input type="text" name="key" value="<%=key%>" class=bk>
    <select name="searchSubjectOnly">
        <option value="true">����������</option>
        <option value="false">�������������</option>
    </select>
    �����ߣ�<select name="creator">
        <option value="">ȫ��</option>
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
    ��ʼ���ڣ�<input type="text" name="startDate" value="<%=start_str%>" class=bk style="width:75px">
    <input type="button" value="��" class="btn" onClick="showCalendar('day',event)">
    <input type="hidden" name="start">
    �������ڣ�<input type="text" name="endDate" value="<%=end_str%>" class=bk style="width:75px">
    <input type="button" value="��" class="btn" onClick="showCalendar('day',event)">
    <input type="hidden" name="end">
    <input type="submit" value="��  ��" class="btn" style="width:60px">
    </td>
    </tr>
    </table>
</td></tr>
    <tr>
    <th width="70%">����</th>
    <th width="15%">������</th>
    <th width="15%">����ʱ��</th>
    </tr>
<%
list = KnowledgeCommon.getKnowledgeList(key,searchSubjectOnly,creator,start/1000,end/1000);

int count = list.size();
//ÿҳ��ʾ��¼��Ŀ
final int PAGESIZE = 30;
//��ҳ��
int pagecount = count/PAGESIZE;
if(count%PAGESIZE!=0)
    pagecount++;
//��ǰҳ
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
	<b><%=PAGESIZE%>��/ҳ&nbsp;��<%=pagecount%>ҳ&nbsp;��<%=p%>ҳ
<%
if(pagecount<=1){
%>
    ��ҳ
<%
}
else{
%>
    <a href="?key=<%=key%>&searchSubjectOnly=<%=searchSubjectOnly%>&creator=<%=creator%>&start=<%=start%>&end=<%=end%>&p=1">��ҳ</a>
<%
}
if(p<=1){
%>
    ��һҳ
<%
}
else{
%>
    <a href="?key=<%=key%>&searchSubjectOnly=<%=searchSubjectOnly%>&creator=<%=creator%>&start=<%=start%>&end=<%=end%>&p=<%=p-1%>">��һҳ</a>
<%
}
if(p==pagecount || pagecount<=1){
%>
    ��һҳ
<%
}
else{
%>
    <a href="?key=<%=key%>&searchSubjectOnly=<%=searchSubjectOnly%>&creator=<%=creator%>&start=<%=start%>&end=<%=end%>&p=<%=p+1%>">��һҳ</a>
<%
}
if(pagecount<=1){
%>
    βҳ
<%
}
else{
%>
    <a href="?key=<%=key%>&searchSubjectOnly=<%=searchSubjectOnly%>&creator=<%=creator%>&start=<%=start%>&end=<%=end%>&p=<%=pagecount%>">βҳ</a>
<%
}
%>
	</b><input class="btn" type="submit" value="ת��" onclick="javascript:redirect();">
        <input class="bk" type="text" id="page" size="3">
	<b>ҳ</b>
        </td>
        </tr>
</table>
<br>
<%@include file="../foot.jsp"%>
