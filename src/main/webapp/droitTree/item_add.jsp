<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%request.setCharacterEncoding("GBK");%>
<html>
<head>
<title>���ܵ�����</title>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
</head>

<script type="text/javascript">
<!--
function itemAdd(flag,name){
	if(flag){
		window.alert("���ܣ�"+ name +" ��ӳɹ�");
	}else{
		window.alert("���ܣ�"+ name +" ���ʧ��");
	}
}

//-->
</script>

<body bgcolor="#FFFFFF" text="#000000">
<form name="form1" method="post" action="item_save.jsp" target="childFrm">
  <table width="100%" border="1" cellspacing="0" cellpadding="0">
    <tr> 
      <td colspan="2" height="14"> 
        <div align="center">���ܵ㶨��</div>
      </td>
    </tr>
    <tr> 
      <td width="46%">���ܵ�����</td>
      <td width="54%"> 
        <input type="text" name="item_name">
      </td>
    </tr>
    <tr> 
      <td width="46%">���ܵ�����</td>
      <td width="54%"> 
        <input type="text" name="item_url">
      </td>
    </tr>

    <tr> 
      <td>����˵��</td>
      <td>
        <input type="text" name="item_desc">
      </td>
    </tr>
    <tr> 
      <td>�Ƿ�ɼ�</td>
      <td>
        <input type="radio" name="item_visual" value="1" checked>
        �ɼ�
        <input type="radio" name="item_visual" value="0">
        ���ɼ�</td>
    </tr>
    <tr> 
      <td colspan="2"> 
        <div align="center"> 
          <input type="submit" name="Submit" value="�� ��">
          <input type="reset" name="Submit2" value="ȡ ��">
          <input type="hidden" name="action" value="add">
        </div>
      </td>
    </tr>    
  </table>
</form>
<iframe id=childFrm name=childFrm src="" style="display:none"></iframe>
</body>
</html>
