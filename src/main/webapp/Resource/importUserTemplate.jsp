<%
response.setContentType("text/plain");
response.setHeader("Content-disposition","attachment; filename=example.csv" );
out.print("oui,�豸���к�,����ʺţ����,������루���,Adsl�󶨵绰");
out.print(",��ͨʱ��(��ʽ��2008-01-15 12:00:00),������пɴ�����,���г�ŵ����");
out.print(",�����û�������,VlanID��,VpiID�ţ����,VciID�ţ����,�������ƣ����� �����������뱣֤���ƺ�ϵͳ��һ�£�");
out.print(",�����ʶ�������������뱣֤���ƺ�ϵͳ��һ�£�,С����ʶ�������������뱣֤���ƺ�ϵͳ��һ�£�,��ϵ��,ҵ�����ͣ�id ���");
out.print(",��ϵ�绰,�û����ͣ����� 0����˾�ͻ���1�����ɿͻ���2�����˿ͻ���,�û�״̬������ 1:���� 2:��ͣ 3:���� 4:�����豸��");
out.println();
%>
<%@ page contentType="text/html;charset=GBK"%>

