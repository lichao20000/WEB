var ICPMenuRes = new Array(5);

var SubICPMenuRes;
 
	SubICPMenuRes    = new Array(4);
	SubICPMenuRes[0] = new Menu('SYS.UG.MANAGE','�½��û�','AddUserForm.jsp','�½��û�','USER_ADD');
	SubICPMenuRes[1] = new Menu('SYS.UG.MANAGE','�½��û���','AddGroupForm.jsp','�½��û���','GROUP_ADD');
	SubICPMenuRes[2] = new Menu('SYS.UG.MANAGE','�û��б�','UserList.jsp','�û��б�','USER_READ');
	SubICPMenuRes[3] = new Menu('SYS.UG.MANAGE','�û����б�','GroupList.jsp','�û����б�','GROUP_READ');
ICPMenuRes[0]  = new Menu('SYS.UG.MANAGE','�û�/�û������','','�û�/�û������','UANDG_READ',SubICPMenuRes);

	SubICPMenuRes    = new Array(2);
	SubICPMenuRes[0] = new Menu('SYS.RP.MANAGE','�½���ɫ','AddRoleForm.jsp','�½���ɫ','ROLE_ADD');
	SubICPMenuRes[1] = new Menu('SYS.RP.MANAGE','��ɫ�б�','RoleList.jsp','��ɫ�б�','ROLE_READ');
//	SubICPMenuRes[2] = new Menu('SYS.RP.MANAGE','�����б�','OperatorList.jsp','�����б�','OPERATOR_READ');
ICPMenuRes[1]  = new Menu('SYS.RP.MANAGE','��ɫ/Ȩ�޹���','','��ɫ/Ȩ�޹���','RANDO_READ',SubICPMenuRes);

	SubICPMenuRes    = new Array(4);
	SubICPMenuRes[0] = new Menu('SYS.SET.MANAGE','�޸�����','ModifyPwdForm.jsp','�޸�����','');
	SubICPMenuRes[1] = new Menu('SYS.SET.MANAGE','���ÿ���ͨ��','EditShortcutForm.jsp','���ÿ���ͨ��','');
	SubICPMenuRes[2] = new Menu('SYS.SET.MANAGE','-','','-','CODE_ALL');
	SubICPMenuRes[3] = new Menu('SYS.SET.MANAGE','ϵͳ���ʹ�������','CodeList.jsp','ϵͳ���ʹ�������','CODE_ALL');	
//	SubICPMenuRes[3] = new Menu('SYS.SET.MANAGE','ϵͳ�����������','tab_clear.jsp','ϵͳ�����������','TAB_CLEAR');	
//	SubICPMenuRes[4] = new Menu('SYS.SET.MANAGE','ϵͳ���ݱ�������','tab_backup.jsp','ϵͳ���ݱ�������','TAB_BACKUP');	
//	SubICPMenuRes[6] = new Menu('SYS.SET.MANAGE','�����豸��������','DeviceConfigList.jsp','�����豸��������','TAB_DEVICEBACKUP');	
ICPMenuRes[2]  = new Menu('SYS.SET.MANAGE','ϵͳ����','','ϵͳ����','',SubICPMenuRes);


	SubICPMenuRes    = new Array(5);
	SubICPMenuRes[0] = new Menu('SYS.ALL.CONFIG','��������ͳһ����','AllConfig.jsp','��������ͳһ����','TAB_ASCONFIG');
	SubICPMenuRes[1] = new Menu('SYS.ALL.CONFIG','�豸��Դͳһ����','DeviceConfig.jsp','�豸��Դͳһ����','TAB_DEVICECONFIG');
	SubICPMenuRes[2] = new Menu("SYS.ALL.CONFIG","-","","","");
	SubICPMenuRes[3] = new Menu("SYS.ALL.CONFIG","Ӧ�÷�������������","service_conf_add.jsp","Ӧ�÷�������������","OPER_SVR_CONF_READ");
	SubICPMenuRes[4] = new Menu("SYS.ALL.CONFIG","�ǿ���ʱ�������","service_date_add.jsp","�ǿ���ʱ�������","OPER_SVR_CONF_READ");
	
	
ICPMenuRes[3]  = new Menu('SYS.ALL.CONFIG','ͳһ����','','ͳһ����','ALLCONFIG',SubICPMenuRes);

ICPMenuRes[4]  = new Menu('SYS.SET.PROCESS','��̨��������״̬','AllProcessStatus.jsp','�鿴��̨��������״̬','ALLPROCESS_READ');


//��������LOGO
setICPBanner("../images/system_banner.jpg","ϵͳ����","index.jsp","_top");


//����ICPMenu��ɫ
//setICPMenuColor("#003399","#FFFFFF","#FF0000");

//���ù�˾LOGO
setLogo("images/logo.gif","�����Ƽ��ɷ����޹�˾","http://www.lianchuang.com/","_blank");
isFrameWork = true;