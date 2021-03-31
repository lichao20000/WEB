<%@  page language="java" contentType="text/html; charset=GBK"
          pageEncoding="GBK" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="lk" uri="/linkage" %>
<ms:inArea areaCode="xj_dx" notInMode="false">

    <html>
    <head>
    <meta http-equiv="Content-Type" content="text/html; charset=GBK">
    <title>�����ɼ���������</title>

    <link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
    <link rel="stylesheet" href="<s:url value="/css3/global.css"/>" type="text/css">
    <script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
    <script type="text/javascript" src="<s:url value="/Js/jQuerySplitPage-linkage.js"/>"></script>

    <script type="text/javascript">

    $(function(){
    parent.dyniframesize();
    });


    function updateStatus(task_id,type){

    var url = "<s:url value='/gwms/resource/batchConfig!updateCount.action'/>";
    $.post(url,{
    task_id:task_id,
    type:type
    },function(ajax){
    if("ok" == ajax){
    doUpdate(task_id,type);
    }else if("tooMuch" == ajax){
    var bln = window.confirm("��Ҫ���µ���������40W,��������ִ��!(����Ǽ�������)");
    if(bln == true) {
    doUpdate(task_id,type);
    }else{
    return false;
    }
    }
    });
    }


    function doUpdate(task_id,type){

    var url = "<s:url value='/gwms/resource/batchConfig!update.action'/>";
    $.post(url,{
    task_id:task_id,
    type:type
    },function(ajax){
    if("1" == ajax){
    alert("�޸ĳɹ�");
    window.reload();
    }else if("0" == ajax){
    alert("�޸�ʧ��");
    }
    });
    }


    function doDel(task_id){
    var url = "<s:url value='/gwms/resource/batchConfig!del.action'/>";
    $.post(url,{
    task_id:task_id
    },function(ajax){
    if("1" == ajax){
    alert("ɾ���ɹ�");
    window.parent.query();
    }else if("0" == ajax){
    alert("ɾ��ʧ��");
    }
    });
    }


    function doStop(task_id){
    var url = "<s:url value='/gwms/resource/batchConfigNodeACT!stopTask.action'/>";
    $.post(url,{
    task_id:task_id
    },function(ajax){
    if("1" == ajax){
    alert("����ֹͣ�ɹ�");
    window.location.reload()
    }else {
    alert("����ֹͣʧ��");
    }
    });
    }


    function stop(task_id){
    var stopUserName=prompt("�û���","���ڴ������û���");
    var stopPass=prompt("����","���ڴ���������");
    var url = "<s:url value='/gwms/resource/batchConfigNodeACT!checkStopUser.action'/>";
    $.post(url,{
    stopUserName:stopUserName,
    stopPass:stopPass
    },function(ajax){
    if("1" == ajax){
    alert("�û���ϢУ��ɹ�");
    var bln = window.confirm("�Ƿ�ֹͣ�ɼ���");
    if(bln == true) {
    doStop(task_id);
    }else{
    return false;
    }
    }else {
    alert(ajax);
    return false;
    }
    });

    }


    function start(task_id){
    var url = "<s:url value='/gwms/resource/batchConfigNodeACT!startTask.action'/>";
    var bln = window.confirm("�Ƿ�ʼ�ɼ���");
    if(bln == true) {
    $.post(url,{
    task_id:task_id
    },function(ajax){
    if("1" == ajax){
    alert("���������ɹ�");
    window.location.reload()
    }else {
    alert("��������ʧ��");
    }
    });
    }else{
    return false;
    }

    }
    </script>

    </head>

    <body >
    <FORM NAME="mainForm" id="mainForm" METHOD="post" ACTION="" target="dataForm">
    <INPUT TYPE="hidden" NAME="task_id" id="task_id" maxlength=60 class=bk size=20>
    </FORM>
    <table class="listtable">
    <caption>��ѯ���</caption>
    <thead>
    <tr>
    <th>����ID</th>
    <th>����</th>
    <th>�ն�����</th>
    <th>����ʱ��</th>
    <th>�ɼ�״̬</th>
    <th>����</th>
    </tr>
    </thead>
    <tbody>
    <s:if test="taskList!=null">
        <s:if test="taskList.size()>0">
            <s:iterator value="taskList">
                <tr>
                    <td align="center"><s:property value="task_id"/></td>
                    <td align="center"><s:property value="city_name"/></td>
                    <td align="center"><s:property value="devsum"/></td>
                    <td align="center"><s:property value="add_time"/></td>
                    <td align="center"><s:property value="result_desc"/></td>
                    <td align="center">
                        <a href="javascript:start('<s:property value="task_id" />')">��ʼ</a>
                        <a href="javascript:stop('<s:property value="task_id" />')">ֹͣ</a>
                    </td>
                </tr>
            </s:iterator>
        </s:if>
        <s:else>
            <tr>
                <td colspan=11>û�в�ѯ�������Ϣ��</td>
            </tr>
        </s:else>
    </s:if>
    <s:else>
        <tr>
            <td colspan=11>û�в�ѯ�������Ϣ��</td>
        </tr>
    </s:else>
    </tbody>


    </table>
    </body>
    </html>