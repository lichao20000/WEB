<%
response.setContentType("text/plain");
response.setHeader("Content-disposition","attachment; filename=workMemorandumTemplete.csv" );
out.print("�ն˳���,�ն�����,�ն��ͺ�,Ӳ���汾");
out.print(",����汾,��������");
out.print(",������ϵ��,������ϵ��ʽ,�Ӵ���Ա,��ʼʱ��(yyyy-MM-dd HH:mm:ss),����ʱ��(yyyy-MM-dd HH:mm:ss)");
out.print(",��������,���״̬(0:δ���1������2:�����),��ע");
out.println();
%>
<%@ page contentType="text/html;charset=GBK"%>

