<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
  <form action="" method="post">
      <input type="hidden" name="user" value="<s:property value="userName"/>" />
      <input type="hidden" name="code"  value="<s:property value="code"/>" />
      <input type="hidden" name="url"  value="<s:property value="url"/>" />
  </form>
  <script type="text/javascript">
     $(function() {
           $("form").attr("action",$("input[name='url']"));
           $("form").submit();
      })
  </script>
</body>
</html>