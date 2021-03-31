var ICPMenuRes = new Array(5);

var SubICPMenuRes;
 
	SubICPMenuRes    = new Array(4);
	SubICPMenuRes[0] = new Menu('SYS.UG.MANAGE','新建用户','AddUserForm.jsp','新建用户','USER_ADD');
	SubICPMenuRes[1] = new Menu('SYS.UG.MANAGE','新建用户组','AddGroupForm.jsp','新建用户组','GROUP_ADD');
	SubICPMenuRes[2] = new Menu('SYS.UG.MANAGE','用户列表','UserList.jsp','用户列表','USER_READ');
	SubICPMenuRes[3] = new Menu('SYS.UG.MANAGE','用户组列表','GroupList.jsp','用户组列表','GROUP_READ');
ICPMenuRes[0]  = new Menu('SYS.UG.MANAGE','用户/用户组管理','','用户/用户组管理','UANDG_READ',SubICPMenuRes);

	SubICPMenuRes    = new Array(2);
	SubICPMenuRes[0] = new Menu('SYS.RP.MANAGE','新建角色','AddRoleForm.jsp','新建角色','ROLE_ADD');
	SubICPMenuRes[1] = new Menu('SYS.RP.MANAGE','角色列表','RoleList.jsp','角色列表','ROLE_READ');
//	SubICPMenuRes[2] = new Menu('SYS.RP.MANAGE','操作列表','OperatorList.jsp','操作列表','OPERATOR_READ');
ICPMenuRes[1]  = new Menu('SYS.RP.MANAGE','角色/权限管理','','角色/权限管理','RANDO_READ',SubICPMenuRes);

	SubICPMenuRes    = new Array(4);
	SubICPMenuRes[0] = new Menu('SYS.SET.MANAGE','修改密码','ModifyPwdForm.jsp','修改密码','');
	SubICPMenuRes[1] = new Menu('SYS.SET.MANAGE','设置快速通道','EditShortcutForm.jsp','设置快速通道','');
	SubICPMenuRes[2] = new Menu('SYS.SET.MANAGE','-','','-','CODE_ALL');
	SubICPMenuRes[3] = new Menu('SYS.SET.MANAGE','系统类型代码设置','CodeList.jsp','系统类型代码设置','CODE_ALL');	
//	SubICPMenuRes[3] = new Menu('SYS.SET.MANAGE','系统数据清除设置','tab_clear.jsp','系统数据清除设置','TAB_CLEAR');	
//	SubICPMenuRes[4] = new Menu('SYS.SET.MANAGE','系统数据备份设置','tab_backup.jsp','系统数据备份设置','TAB_BACKUP');	
//	SubICPMenuRes[6] = new Menu('SYS.SET.MANAGE','网络设备备份配置','DeviceConfigList.jsp','网络设备备份配置','TAB_DEVICEBACKUP');	
ICPMenuRes[2]  = new Menu('SYS.SET.MANAGE','系统设置','','系统设置','',SubICPMenuRes);


	SubICPMenuRes    = new Array(5);
	SubICPMenuRes[0] = new Menu('SYS.ALL.CONFIG','考核数据统一配置','AllConfig.jsp','考核数据统一配置','TAB_ASCONFIG');
	SubICPMenuRes[1] = new Menu('SYS.ALL.CONFIG','设备资源统一配置','DeviceConfig.jsp','设备资源统一配置','TAB_DEVICECONFIG');
	SubICPMenuRes[2] = new Menu("SYS.ALL.CONFIG","-","","","");
	SubICPMenuRes[3] = new Menu("SYS.ALL.CONFIG","应用服务利用率配置","service_conf_add.jsp","应用服务利用率配置","OPER_SVR_CONF_READ");
	SubICPMenuRes[4] = new Menu("SYS.ALL.CONFIG","非考核时间段配置","service_date_add.jsp","非考核时间段配置","OPER_SVR_CONF_READ");
	
	
ICPMenuRes[3]  = new Menu('SYS.ALL.CONFIG','统一配置','','统一配置','ALLCONFIG',SubICPMenuRes);

ICPMenuRes[4]  = new Menu('SYS.SET.PROCESS','后台进程运行状态','AllProcessStatus.jsp','查看后台进程运行状态','ALLPROCESS_READ');


//设置主题LOGO
setICPBanner("../images/system_banner.jpg","系统管理","index.jsp","_top");


//设置ICPMenu颜色
//setICPMenuColor("#003399","#FFFFFF","#FF0000");

//设置公司LOGO
setLogo("images/logo.gif","联创科技股份有限公司","http://www.lianchuang.com/","_blank");
isFrameWork = true;