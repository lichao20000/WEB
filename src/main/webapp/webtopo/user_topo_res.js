//------------------------------------------------------------
// Copyright (c) 2004-2005 linkage. All rights reserved.
// Version 1.0.03
// Ahthor Haiteng.Yu
//------------------------------------------------------------
/**
 * �˵����ݽṹ
 * keyword���˵������ؼ���
 * name���˵�����
 * link���˵�����
 * desc���˵�����
*/
function RMenuRes(type,keyword, name, link,target, icon,desc, oper){
	this._type = type;
	this._keyword = keyword;
	this._name    = name;
	this._link	  = link;
	this._target = target;
	this._icon = icon;
	this._desc	  = desc;
	this._oper   = oper;
	if(arguments.length>8){
		this._submenu = arguments[8];
	}
}



//������ͼ�Ŀ�ݲ˵�
var netMenu=new Array();
netMenu[0]=new String("ADDOBJECT");
netMenu[1]=new String("ADDLINK");
netMenu[2]=new String("LOADTOPO");
netMenu[3]=new String("REDISCOVERTOPO");
netMenu[4]=new String("INCREMENTFINDTOPO");
netMenu[5]=new String("SAVETOPO");
netMenu[6]=new String("GOUP");
netMenu[7]=new String("VIEW_ALL_DEVICE");
netMenu[8]=new String("VIEW_MANAGED_DEVICE");
//�ֶ�ˢ�¡��Զ�ˢ�� Ȩ�޿���
//netMenu[9]=new String("WARN_ATUO_FRESH");
//netMenu[10]=new String("WARN_FRESH_BY_HAND");


//�ҽ��˵���Դ
/*
	��Բ˵������˵��:
	����ò˵������κεط����Ե����ʾ��������"";
	����ò˵���������е��豸�����Ρ���·,����"*",��Ե����Ķ���
	����ò˵���������е��豸�����Ρ���·,����"***",��Զ���Ķ���
	����ò˵��������,������"0"
	����ò˵�������е��豸,������"device";
	����ò˵�������е������豸,������"device_net";
	����ò˵�������е���������,������"device_host";
	����ò˵����cpe�豸��������"device_cpe"
	����ò˵�����ض����豸���ͣ���������豸���͵����к�;
	����ò˵������·,������"link";	
	����ò˵���Ը澯,������"-3";
	����ò˵���Զ�����κ���Ԫ��������"net";
	���⴦��
	�����Ԫ��������⴦��
	����ò˵���Ե������λ�����Ԫ��������"singlenet"

*/
var arrRMenu = new Array();
arrRMenu[0] = new RMenuRes("","GOUP","������һ��","getParentTopo.jsp","childFrm","","������һ��Topo","");
arrRMenu[1] = new RMenuRes("0","GODOWN","������һ��","getChildTopo.jsp","childFrm","","������һ��Topo","");
arrRMenu[2] = new RMenuRes("*","","-","","","","","");
arrRMenu[3] = new RMenuRes("device_net","OBJECTATTRIBUTE","����","showDevDetail","javascript","","�鿴��ǰ��������","");

arrRMenu[4] = new RMenuRes("device_net","OBJECTATTRIBUTE","�˿���Ϣ","showPortInfo","javascript","","�鿴��ǰ����˿���Ϣ","");
arrRMenu[5] = new RMenuRes("device_net","OBJECTATTRIBUTE","�豸����","showFlux","javascript","","�鿴��ǰ��������","");
arrRMenu[6] = new RMenuRes("device_net","OBJECTATTRIBUTE","��ʾ�豸����","showPmee","javascript","","�鿴��ǰ�豸����","");
//�澯�б��ҽ��˵�
arrRMenu[7] = new RMenuRes("-3","CHECKALARM","ȷ�ϸ澯","ack_Warn","javascript","","ȷ�ϸ澯","");
arrRMenu[8] = new RMenuRes("-3","DELETEALARM","ɾ���澯","remove_Warn","javascript","","ɾ���澯","");
arrRMenu[9] = new RMenuRes("-3","CHECKSTOREALARM","ȷ�ϲ����","ack_Store_Warn","javascript","","ȷ�ϸ澯�����","");
arrRMenu[10] = new RMenuRes("device_cpe","CIRCUITDIAGRAM","��·ͼ","showCircuit","javascript","","�鿴��ǰ�豸�ĵ�·ͼ","");
arrRMenu[11] = new RMenuRes("device","DEVICEALARM","�豸�澯","showDeviceWarn","javascript","","�鿴��ǰ�豸�ĸ澯","");
arrRMenu[12] = new RMenuRes("device_net","GODOWN","ʵ�ù���","","","","","","RSMenu1");
arrRMenu[13] = new RMenuRes("***","OPERATION#","����","","","","���豸���й���","","RSMenu2");
arrRMenu[14] = new RMenuRes("link","LINKOPERATION","��·����","showLinkInfo","javascript","","����·���в���","");
arrRMenu[15] = new RMenuRes("link","LINKOPERATION","��·����","editLinkInfo","javascript","","����·���в���","");
arrRMenu[16] = new RMenuRes("-3","FINDDEVICEOBJ","��λ�豸����","findDevObj","javascript","","��λ�豸����","");
arrRMenu[17] = new RMenuRes("0","GODOWN","��ʾ�û���Ϣ","ViewUserInfo","javascript","","��ʾ�û���Ϣ","");
arrRMenu[18] = new RMenuRes("0","GODOWN","�޸��û���Ϣ","UpdateUserInfo","javascript","","�޸��û���Ϣ","");
arrRMenu[19] = new RMenuRes("0","GODOWN","�޸��û�ͼ��","UpdateUserIcon","javascript","","�޸��û�ͼ��","");
arrRMenu[20] = new RMenuRes("0","GODOWN","ɾ���û�ͼ��","DeleteUserIcon","javascript","","ɾ���û�ͼ��","");
arrRMenu[21] = new RMenuRes("0","GODOWN","����û�ͼ��","AddUserIcon","javascript","","����û�ͼ��","");
arrRMenu[22] = new RMenuRes("0","GODOWN","����豸ͼ��","AddDeviceIcon","javascript","","����豸ͼ��","");
arrRMenu[23] = new RMenuRes("device_host","GOUP","��ʾ������","HOST_ShowObjectAttr","javascript","","��ʾ��ǰ��������","");
arrRMenu[24] = new RMenuRes("device_host","GOUP","���ܲ鿴","HOST_perfView","javascript","","���ܲ鿴","");
//arrRMenu[25] = new RMenuRes("device_host","GOUP","��������","HOST_perfConfig","javascript","","������������","");

var RSMenu1= new Array();
RSMenu1[0]=new RMenuRes("device_net","GODOWN","Ping","pingIp","javascript","","Ping","");
RSMenu1[1]=new RMenuRes("device_net","GODOWN","Telnet","telnetLink","javascript","","Telnet","");

var RSMenu2=new Array();
RSMenu2[0] = new RMenuRes("netmanager1","MANAGEDEVICEWIN#","������Ԫ","ManagerDeviceWin","javascript","","������Ԫ","");
RSMenu2[1] = new RMenuRes("netmanager2","UNMANAGEDEVICEWIN#","ȡ������","UnManagerDeviceWin","javascript","","ȡ��������Ԫ","");
RSMenu2[2] = new RMenuRes("***","","-","","","","","");
RSMenu2[3] = new RMenuRes("net","CUT"," �� �� ","CutNet","javascript","","������Ԫ","");
RSMenu2[4] = new RMenuRes("net1","PASTE"," ճ �� ","PasteNet","javascript","","������Ԫ","");
RSMenu2[5] = new RMenuRes("net1","CANCELCUT","ȡ������","CancelCut","javascript","","������Ԫ","");
RSMenu2[6] = new RMenuRes("net","DELETEOBJECT","ɾ������","DeleteObj","javascript","","ɾ��ѡ��Ķ���","");
RSMenu2[7] = new RMenuRes("link","DELETELINK","ɾ����·","DeleteLinkObj","javascript","","ɾ��ѡ����豸","");
RSMenu2[8] = new RMenuRes("***","","-","","","","","");
RSMenu2[9] = new RMenuRes("device_net","MODIFYTYPE","�޸��豸����","ModifyDeviceType","javascript","","�޸��豸����","");
RSMenu2[10] = new RMenuRes("singlenet","MODIFYATTRIBUTE","�޸�����","ModifyDeviceAttribute","javascript","","�޸��豸����","");
RSMenu2[11] = new RMenuRes("singlenet","SETCUSTOMINOF","�����ͻ�","SetCustomInfo","javascript","","�����ͻ���Ϣ","");