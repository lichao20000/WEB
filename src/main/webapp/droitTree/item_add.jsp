<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%request.setCharacterEncoding("GBK");%>
<html>
<head>
<title>功能点配置</title>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
</head>

<script type="text/javascript">
<!--
function itemAdd(flag,name){
	if(flag){
		window.alert("功能："+ name +" 添加成功");
	}else{
		window.alert("功能："+ name +" 添加失败");
	}
}

//-->
</script>

<body bgcolor="#FFFFFF" text="#000000">
<form name="form1" method="post" action="item_save.jsp" target="childFrm">
  <table width="100%" border="1" cellspacing="0" cellpadding="0">
    <tr> 
      <td colspan="2" height="14"> 
        <div align="center">功能点定义</div>
      </td>
    </tr>
    <tr> 
      <td width="46%">功能点名称</td>
      <td width="54%"> 
        <input type="text" name="item_name">
      </td>
    </tr>
    <tr> 
      <td width="46%">功能点链接</td>
      <td width="54%"> 
        <input type="text" name="item_url">
      </td>
    </tr>

    <tr> 
      <td>功能说明</td>
      <td>
        <input type="text" name="item_desc">
      </td>
    </tr>
    <tr> 
      <td>是否可见</td>
      <td>
        <input type="radio" name="item_visual" value="1" checked>
        可见
        <input type="radio" name="item_visual" value="0">
        不可见</td>
    </tr>
    <tr> 
      <td colspan="2"> 
        <div align="center"> 
          <input type="submit" name="Submit" value="增 加">
          <input type="reset" name="Submit2" value="取 消">
          <input type="hidden" name="action" value="add">
        </div>
      </td>
    </tr>    
  </table>
</form>
<iframe id=childFrm name=childFrm src="" style="display:none"></iframe>
</body>
</html>
