function drawMenuBar(barId){
	document.body.insertAdjacentHTML("beforeEnd","<div id='overline' class='CM_OverLine' style='left:100px;top:300px;width:100px;overflow:hidden'><table border=0 cellpadding=0 cellspacing=0 width='100%'><tr height=1><td width='100%' style='background-color:#EFEDDE' ></td></tr><tr height=1><td width='100%' style='background-color:menu' ></td></tr></table></div>");

	var mnuMain = new CMenu("mnuMain");    
	mnuMain.menu[0] = new menu("mnuMainSiteAdmin","������Ϊ����","������Ϊ����","","","",0,"mnuSiteAdmin");
	mnuMain.menu[1] = new menu("mnuMainNL","��ҵ��ȫ���","��ҵ��ȫ���","","","",0,"mnuShow");
	mnuMain.menu[2] = new menu("mnuMainSystem","ά������","ά������","","","",0,"mnuSystem");
//=============================================================================================================

	drawMainMenu(mnuMain,"100%",27,barId); 

	var mnuShow = new CMenu("mnuShow");
	mnuShow.menu[0] = new menu("mnuShow1","��ҵ��ȫ�¼�","��ҵ��ȫ�¼�",imagePath+"images/ico2_1.gif","","",0,"enetsecurity");
	mnuShow.menu[1] = new menu("mnuShow2","Ա����ȫ�¼�","Ա����ȫ�¼�",imagePath+"images/ico2_2.gif","","",0,"staffnetsecurity");
	mnuShow.menu[2] = new menu("mnuShow3","��ȫ�¼�����","��ȫ�¼�����",imagePath+"images/ico2_3.gif","","",0,"securitydetail");
	drawSubMenu(mnuShow);


	var enetsecurity = new CMenu("enetsecurity");
	enetsecurity.menu[0] = new menu("mnuShow221","��ȫ�¼�ͳ��","��ȫ�¼�ͳ��",imagePath+"images/ico2_1_1.gif","","",1,actionPath+"SafeReport.action?deviceid="+deviceid+"&remark="+customname);
	enetsecurity.menu[1] = new menu("mnuShow222","�����¼�ͳ��","�����¼�ͳ��",imagePath+"images/ico2_1_2.gif","","",1,actionPath+"VirusReport.action?deviceid="+deviceid+"&remark="+customname);
	enetsecurity.menu[2] = new menu("mnuShow223","�����ʼ�ͳ��","�����ʼ�ͳ��",imagePath+"images/ico2_1_3.gif","","",1,actionPath+"TrashMailReport.action?deviceid="+deviceid+"&remark="+customname);
	enetsecurity.menu[3] = new menu("mnuShow224","�����¼�ͳ��","�����¼�ͳ��",imagePath+"images/ico2_1_4.gif","","",1,actionPath+"AttackReport.action?deviceid="+deviceid+"&remark="+customname);
	enetsecurity.menu[4] = new menu("mnuShow225","�����¼�ͳ��","�����¼�ͳ��",imagePath+"images/ico2_1_5.gif","","",1,actionPath+"FilterReport.action?deviceid="+deviceid+"&remark="+customname);
	drawSubMenu(enetsecurity);


	var staffnetsecurity = new CMenu("staffnetsecurity");
	staffnetsecurity.menu[0] = new menu("mnuShow22100","�����û�����","�����û�����",imagePath+"images/ico2_2_1.gif","","",1,actionPath+"UserTopReport!virusTop.action?device_id="+deviceid+"&remark="+customname);
	staffnetsecurity.menu[1] = new menu("mnuShow22200","�����ʼ��û�����","�����ʼ��û�����",imagePath+"images/ico2_2_2.gif","","",1,actionPath+"UserTopReport!asmailTop.action?device_id="+deviceid+"&remark="+customname);
	staffnetsecurity.menu[2] = new menu("mnuShow22300","�����û�����","�����û�����",imagePath+"images/ico2_2_3.gif","","",1,actionPath+"UserTopReport!attackTop.action?device_id="+deviceid+"&remark="+customname);
	staffnetsecurity.menu[3] = new menu("mnuShow22400","�����û�����","�����û�����",imagePath+"images/ico2_2_4.gif","","",1,actionPath+"UserTopReport!filterTop.action?device_id="+deviceid+"&remark="+customname);
 
	drawSubMenu(staffnetsecurity);


	var securitydetail = new CMenu("securitydetail");
	securitydetail.menu[0] = new menu("mnuShow22111","��������","��������",imagePath+"images/ico2_3_1.gif","","",1,actionPath+"entSecStat!getVirusOrginData.action?deviceId="+deviceid);
	securitydetail.menu[1] = new menu("mnuShow22211","�����ʼ�����","�����ʼ�����",imagePath+"images/ico2_3_2.gif","","",1,actionPath+"entSecStat!getSpamMailOrginData.action?deviceId="+deviceid);
	securitydetail.menu[2] = new menu("mnuShow22311","��������","��������",imagePath+"images/ico2_3_3.gif","","",1,actionPath+"entSecStat!getAttackOrginData.action?deviceId="+deviceid);
	securitydetail.menu[3] = new menu("mnuShow22411","��������","��������",imagePath+"images/ico2_3_4.gif","","",1,actionPath+"entSecStat!getFilterOrginData.action?deviceId="+deviceid);
 
	drawSubMenu(securitydetail);

//===============================================================================
	var mnuSiteAdmin = new CMenu("mnuSiteAdmin");
	mnuSiteAdmin.menu[0] = new menu("mnuShow21","��ҵ������Ϊ","��ҵ������Ϊ",imagePath+"images/ico1_1.gif","","",0,"enetanalyse");
	mnuSiteAdmin.menu[1] = new menu("mnuShow22","Ա��������Ϊ","Ա��������Ϊ",imagePath+"images/ico1_2.gif","","",0,"staffnetanalyse");
	mnuSiteAdmin.menu[2] = new menu("mnuShow23","Ա����������","Ա����������",imagePath+"images/ico1_3.gif","","",0,"staffnetdetail");
	drawSubMenu(mnuSiteAdmin);


	var enetanalyse = new CMenu("enetanalyse");
	enetanalyse.menu[0] = new menu("mnuShow2211","������������","������������",imagePath+"images/ico1_1_1.gif","","",1,actionPath+"NetFluxAnalyse!toNetFluxAnalyse.action?deviceid="+deviceid+"&remark="+customname);
	enetanalyse.menu[1] = new menu("mnuShow2221","WEB���TopN","WEB���TopN",imagePath+"images/ico1_1_2.gif","","",1,actionPath+"EntWebTopn!toEntWebTopn.action?deviceid="+deviceid+"&remark="+customname);
	enetanalyse.menu[2] = new menu("mnuShow2231","FTPվ��TopN","FTPվ��TopN",imagePath+"images/ico1_1_3.gif","","",1,actionPath+"EntFtpTopn!toEntFtpSiteTopn.action?deviceid="+deviceid+"&remark="+customname);
	drawSubMenu(enetanalyse);

	var staffnetanalyse = new CMenu("staffnetanalyse");
	staffnetanalyse.menu[0] = new menu("mnuShow22122","������Ϊ����","������Ϊ����",imagePath+"images/ico1_2_1.gif","","",1,actionPath+"PBTopN.action?deviceid="+deviceid+"&remark="+customname);
	staffnetanalyse.menu[1] = new menu("mnuShow22222","WEB���TopN","WEB���TopN",imagePath+"images/ico1_2_2.gif","","",1,actionPath+"PBWebTopN.action?deviceid="+deviceid+"&remark="+customname);
	staffnetanalyse.menu[2] = new menu("mnuShow22322","�ʼ��շ�TopN","�ʼ��շ�TopN",imagePath+"images/ico1_2_3.gif","","",1,actionPath+"PBMailTopN.action?deviceid="+deviceid+"&remark="+customname);
	//staffnetanalyse.menu[3] = new menu("mnuShow22422","��ʱͨѶTopN","��ʱͨѶTopN",imagePath+"images/ico1_2_4.gif","","",1,"");	
	drawSubMenu(staffnetanalyse);

	var staffnetdetail = new CMenu("staffnetdetail");
	staffnetdetail.menu[0] = new menu("mnuShow221000","HTTP����","HTTP����",imagePath+"images/ico1_3_1.gif","","",1,actionPath+"PBDetailAction.action?deviceid="+deviceid+"&prot_type=0");
	staffnetdetail.menu[1] = new menu("mnuShow222000","SMTP����","SMTP����",imagePath+"images/ico1_3_2.gif","","",1,actionPath+"PBDetailAction.action?deviceid="+deviceid+"&prot_type=2");
	staffnetdetail.menu[2] = new menu("mnuShow222020","POP3����","POP3����",imagePath+"images/ico1_3_3.gif","","",1,actionPath+"PBDetailAction.action?deviceid="+deviceid+"&prot_type=3");
/*	staffnetdetail.menu[3] = new menu("mnuShow22230","FTP����","FTP����",imagePath+"images/ico1_3_4.gif","","",1,"");
	staffnetdetail.menu[4] = new menu("mnuShow222400","IMAP����","IMAP����",imagePath+"images/ico1_3_5.gif","","",1,"");*/
	drawSubMenu(staffnetdetail);

	//======================================================================

	var mnuSystem = new CMenu("mnuSystem");
	mnuSystem.menu[0] = new menu("mnuShow001","���ܱ���","���ܱ���",imagePath+"images/ico3_1.gif","","",0,"snbb");
	mnuSystem.menu[1] = new menu("mnuShow002","���ϱ���","���ϱ���",imagePath+"images/ico3_2.gif","","",0,"gzbb");
 
	drawSubMenu(mnuSystem);


	var snbb = new CMenu("snbb");
	snbb.menu[0] = new menu("mnuShow00221","�豸CPU���ܱ���","�豸CPU���ܱ���",imagePath+"images/ico3_1_1.gif","","",1,actionPath+"SgwPerformance.action?device_id="+deviceid+"&class1=1&desc="+customname);
	snbb.menu[1] = new menu("mnuShow00222","�豸�ڴ����ܱ���","�豸�ڴ����ܱ���",imagePath+"images/ico3_1_2.gif","","",1,actionPath+"SgwPerformance.action?device_id="+deviceid+"&class1=2&desc="+customname);
	snbb.menu[2] = new menu("mnuShow00223","�豸����������","�豸����������",imagePath+"images/ico3_1_3.gif","","",1,actionPath+"SgwPerformance.action?device_id="+deviceid+"&class1=8&desc="+customname);
 	snbb.menu[3] = new menu("mnuShow00224","�豸����ʱ�ӱ���","�豸����ʱ�ӱ���",imagePath+"images/ico3_2_1.gif","","",1,actionPath+"SgwPerformance.action?device_id="+deviceid+"&desc="+customname);

	drawSubMenu(snbb);

	var gzbb = new CMenu("gzbb");
	gzbb.menu[0] = new menu("mnuShow111110","�豸���ϱ���","�豸���ϱ���",imagePath+"images/ico3_2_1.gif","","",1,"");
 
	drawSubMenu(gzbb);	
	 
}