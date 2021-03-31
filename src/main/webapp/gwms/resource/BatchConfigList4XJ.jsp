<%@  page language="java" contentType="text/html; charset=GBK"
          pageEncoding="GBK" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="lk" uri="/linkage" %>
<ms:inArea areaCode="xj_dx" notInMode="false">

    <html>
    <head>
    <meta http-equiv="Content-Type" content="text/html; charset=GBK">
    <title>批量采集配置任务</title>

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
    var bln = window.confirm("需要更新的数量大于40W,建议晚上执行!(点击是继续更新)");
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
    alert("修改成功");
    window.reload();
    }else if("0" == ajax){
    alert("修改失败");
    }
    });
    }


    function doDel(task_id){
    var url = "<s:url value='/gwms/resource/batchConfig!del.action'/>";
    $.post(url,{
    task_id:task_id
    },function(ajax){
    if("1" == ajax){
    alert("删除成功");
    window.parent.query();
    }else if("0" == ajax){
    alert("删除失败");
    }
    });
    }


    function doStop(task_id){
    var url = "<s:url value='/gwms/resource/batchConfigNodeACT!stopTask.action'/>";
    $.post(url,{
    task_id:task_id
    },function(ajax){
    if("1" == ajax){
    alert("任务停止成功");
    window.location.reload()
    }else {
    alert("任务停止失败");
    }
    });
    }


    function stop(task_id){
    var stopUserName=prompt("用户名","请在此输入用户名");
    var stopPass=prompt("密码","请在此输入密码");
    var url = "<s:url value='/gwms/resource/batchConfigNodeACT!checkStopUser.action'/>";
    $.post(url,{
    stopUserName:stopUserName,
    stopPass:stopPass
    },function(ajax){
    if("1" == ajax){
    alert("用户信息校验成功");
    var bln = window.confirm("是否停止采集？");
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
    var bln = window.confirm("是否开始采集？");
    if(bln == true) {
    $.post(url,{
    task_id:task_id
    },function(ajax){
    if("1" == ajax){
    alert("任务启动成功");
    window.location.reload()
    }else {
    alert("任务启动失败");
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
    <caption>查询结果</caption>
    <thead>
    <tr>
    <th>任务ID</th>
    <th>地州</th>
    <th>终端数量</th>
    <th>定制时间</th>
    <th>采集状态</th>
    <th>操作</th>
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
                        <a href="javascript:start('<s:property value="task_id" />')">开始</a>
                        <a href="javascript:stop('<s:property value="task_id" />')">停止</a>
                    </td>
                </tr>
            </s:iterator>
        </s:if>
        <s:else>
            <tr>
                <td colspan=11>没有查询到相关信息！</td>
            </tr>
        </s:else>
    </s:if>
    <s:else>
        <tr>
            <td colspan=11>没有查询到相关信息！</td>
        </tr>
    </s:else>
    </tbody>


    </table>
    </body>
    </html>