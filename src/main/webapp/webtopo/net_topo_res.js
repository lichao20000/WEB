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
netMenu[netMenu.length]=new String("ADDOBJECT");//��������
netMenu[netMenu.length]=new String("ADDLINK");//��������
netMenu[netMenu.length]=new String("GOUP");//������һ��
netMenu[netMenu.length]=new String("SAVEIMG");//��������ͼ
netMenu[netMenu.length]=new String("WEBTOPO_DEVICE_DATA");//��Ԫ��ѯ
netMenu[netMenu.length]=new String("LOADTOPO");//��������
netMenu[netMenu.length]=new String("WARN_VOICE_CONFIG");//��������
//netMenu[netMenu.length]=new String("VIEW_ALL_DEVICE");//��ʾ������Ԫ
//netMenu[netMenu.length]=new String("VIEW_MANAGED_DEVICE");//��ʾ������Ԫ
netMenu[netMenu.length]=new String("CONFIG_TOPOLAYER");//����Topo��
netMenu[netMenu.length]=new String("WARN_ATUO_FRESH");//���ø澯�Զ�ˢ��
netMenu[netMenu.length]=new String("WARN_FRESH_BY_HAND");//�澯�ֶ�ˢ��



//�ҽ��˵���Դ
/*
	��Բ˵������˵��:
	����ò˵������κεط����Ե����ʾ��������"";
	����ò˵���������е��豸������,����"*",��Ե����Ķ���
	����ò˵���������е��豸������,����"***",��Զ���Ķ���
	����ò˵������ͨ����,������"0"
	����ò˵���Դ�ȷ���豸����,������"unkdevSeg"
	����ò˵�������еĵ����豸,������"device";
	����ò˵���Զ����ȷ���豸,������"device_confirm";
	����ò˵���Ե�����ȷ���豸,������"device_confirm_single";
	����ò˵���Զ����ȷ���豸,������"device_confirmed";
	����ò˵���Ե�����ȷ���豸,������"device_confirmed_single";
	����ò˵�����ض����豸���ͣ���������豸���͵����к�;
	����ò˵���Ը澯,������"-3";

*/
var arrRMenu = new Array();
arrRMenu[arrRMenu.length] = new RMenuRes("","GOUP","������һ��","getParentTopo.jsp","childFrm","","������һ��Topo","");
arrRMenu[arrRMenu.length] = new RMenuRes("0","GODOWN","������һ��","getChildTopo.jsp","childFrm","","������һ��Topo","");
arrRMenu[arrRMenu.length] = new RMenuRes("device","","-","","","","","");
arrRMenu[arrRMenu.length] = new RMenuRes("device","OBJECTATTRIBUTE","����","showDevDetail","javascript","","�鿴��ǰ��������","");
arrRMenu[arrRMenu.length] = new RMenuRes("device","DEVICETOOL","ʵ�ù���","","","","","","RSMenu1");
	var RSMenu1=new Array();
	RSMenu1[RSMenu1.length]=new RMenuRes("device","PINGIP","Ping","pingIp","javascript","","Ping","");
	RSMenu1[RSMenu1.length]=new RMenuRes("device","PINGIP","Telnet","telnetLink","javascript","","Telnet","");
	RSMenu1[RSMenu1.length]=new RMenuRes("device","PINGIP","Tracert","tracertLink","javascript","","Tracert","");
arrRMenu[arrRMenu.length] = new RMenuRes("link","DELETELINK","ɾ����·","DeleteLinkObj","javascript","","ɾ��ѡ����豸","");
arrRMenu[arrRMenu.length] = new RMenuRes("link","LINK_FLUX","��·����","showLinkInfo","javascript","","����·���в���","");
arrRMenu[arrRMenu.length] = new RMenuRes("link","DELETELINK","��·��ʽ","editLinkCss","javascript","","��·��ʽ����","");	
arrRMenu[arrRMenu.length] = new RMenuRes("device","OPERATION","�豸��Ϣ","","","","�쿴�豸��Ϣ","","RSMenu6");
	var RSMenu6=new Array();
	RSMenu6[RSMenu6.length] = new RMenuRes("*","","ϵͳ��Ϣ","showSysInfo","javascript","","�鿴��ǰ�豸��ϵͳ��Ϣ","");
	RSMenu6[RSMenu6.length] = new RMenuRes("*","","�豸����","showFlux","javascript","","�鿴��ǰ�豸������","");
	RSMenu6[RSMenu6.length] = new RMenuRes("*","","�豸����","showPmee","javascript","","�鿴��ǰ�豸������","");
	RSMenu6[RSMenu6.length] = new RMenuRes("*","","�˿���Ϣ","showPortInfo","javascript","","�鿴��ǰ�豸�Ķ˿���Ϣ","");
	RSMenu6[RSMenu6.length] = new RMenuRes("*","","�豸�澯","showDeviceWarn","javascript","","�鿴��ǰ�豸�ĸ澯","");
	RSMenu6[RSMenu6.length] = new RMenuRes("*","","·����Ϣ","showDeviceRoute","javascript","","�鿴·����Ϣ","");
	RSMenu6[RSMenu6.length] = new RMenuRes("*","","�豸״̬��Ϣ","showDeviceStatus","javascript","","�鿴��ǰ�豸�Ķ˿�,����,״̬����Ϣ","");
	RSMenu6[RSMenu6.length] = new RMenuRes("*","","�豸���ܼ��","showFluxAndPmee","javascript","","�鿴��ǰ�豸�����ܺ���������Ϣ","");
	RSMenu6[RSMenu6.length] = new RMenuRes("*","","EVDO����Ϣ","showEVDOInfo","javascript","","�鿴��ǰEVDO�Ŀ���Ϣ","");
arrRMenu[arrRMenu.length] = new RMenuRes("device","DEVICECONFIG","�豸����","","","","���豸��������","","RSMenu7");
	var RSMenu7=[];
	RSMenu7[RSMenu7.length]=new RMenuRes("device","CAPABILITYCONFIG","��������","ConfigPerf","javascript","","��������","");
	RSMenu7[RSMenu7.length]=new RMenuRes("device","FLUXCONFIG","��������","ConfigFlux","javascript","","��������","");
	RSMenu7[RSMenu7.length]=new RMenuRes("device","IPCHECKCONFIG","IPCHECK����","ConfigIpCheck","javascript","","IPCHECK����","");
arrRMenu[arrRMenu.length] = new RMenuRes("device","STATUSWATCH","״̬���","","","","���豸����״̬���","","RSMenu5");
	var RSMenu5=new Array();
	RSMenu5[RSMenu5.length] = new RMenuRes("device","TESTCONNECTION","��������","TestConnection","javascript","","�����豸����״̬","");
	RSMenu5[RSMenu5.length] = new RMenuRes("device","RPCMethod","��ȡRPC����","RPCMethod","javascript","","��ȡRPC����","");

arrRMenu[arrRMenu.length] = new RMenuRes("device","WARCONFIRM","�������","","","","���豸�������","","RSMenu3");
	var RSMenu3=new Array();
	RSMenu3[RSMenu3.length] = new RMenuRes("*","PINGCONFIRM","Ping���","pingTest","javascript","","Ping���","");
	RSMenu3[RSMenu3.length] = new RMenuRes("*","ATMF5LOOPCONFIRM","ATMF5LOOP���","ATMTest","javascript","","ATMF5LOOP���","");
	RSMenu3[RSMenu3.length] = new RMenuRes("*","DSLCONFIRM","DSL���","DSLTest","javascript","DSLTest","DSL���","");
	RSMenu3[RSMenu3.length] = new RMenuRes("*","DEVICERESET","�ָ���������","deviceReset","javascript","","�ָ���������","");
	RSMenu3[RSMenu3.length] = new RMenuRes("*","DEVICEREBOOT","�����豸","deviceReboot","javascript","","�����豸","");
arrRMenu[arrRMenu.length] = new RMenuRes("unkdevSeg","CONFIRMDEVICE","ȷ�������豸","ConfirmAllDev","javascript","","ȷ�ϴ�ȷ�������µ������豸","");
arrRMenu[arrRMenu.length] = new RMenuRes("device_confirmed_single","DEVICESCOUT","��������","","","","ȷ���豸��������","","RSMenu4");
	var RSMenu4=new Array();
	RSMenu4[RSMenu4.length] = new RMenuRes("*","GETPARAMETERMODULE","����ģ�ͻ�ȡ","getParaModule","javascript","","����ģ�ͻ�ȡ","");
	RSMenu4[RSMenu4.length] = new RMenuRes("*","PARAMETERQUERY","������ѯ","paraQuery","javascript","","������ѯ","");
	RSMenu4[RSMenu4.length] = new RMenuRes("*","PARAMETERCHANGECALL","�����仯�ϱ�","paraChangCall","javascript","","�����仯�ϱ�","");
  	RSMenu4[RSMenu4.length] = new RMenuRes("*","PARAMETERCHANGECALL","��������","getFamiliarPara","javascript","","��������","");
  	RSMenu4[RSMenu4.length] = new RMenuRes("*","PARAMETERCHANGECALL","vlan��Ϣ","getVlanInfo","javascript","","vlan��Ϣ","");
  	RSMenu4[RSMenu4.length] = new RMenuRes("*","PARAMETERCHANGECALL","DHCP������Ϣ","getDhcpInfo","javascript","","DHCP������Ϣ","");
  	RSMenu4[RSMenu4.length] = new RMenuRes("*","PARAMETERCHANGECALL","wlan��Ϣ","getwlanInfo","javascript","","vlan��Ϣ","");
	arrRMenu[arrRMenu.length] = new RMenuRes("***","OPERATION","����","","","","���豸���й���","","RSMenu2");
	var RSMenu2=new Array();
	//RSMenu2[0] = new RMenuRes("device","TESTCONNECTION","��������","TestConnection","javascript","","�����豸����״̬","");	
	//RSMenu2[RSMenu2.length] = new RMenuRes("netmanager1","MANAGEDEVICEWIN","������Ԫ","ManagerDeviceWin","javascript","","������Ԫ","");
	//RSMenu2[RSMenu2.length] = new RMenuRes("netmanager2","UNMANAGEDEVICEWIN","ȡ������","UnManagerDeviceWin","javascript","","ȡ��������Ԫ","");
	//RSMenu2[3] = new RMenuRes("device","RPCMethod","״̬���","RPCMethod","javascript","","״̬���","");
	RSMenu2[RSMenu2.length] = new RMenuRes("netCut","CUT"," �� ��","CutNet","javascript","","������Ԫ","");
	RSMenu2[RSMenu2.length] = new RMenuRes("netPase","PASTE"," ճ �� ","PasteNet","javascript","","������Ԫ","");
	RSMenu2[RSMenu2.length] = new RMenuRes("netPase","CANCELCUT","ȡ������","CancelCut","javascript","","������Ԫ","");
	//RSMenu2[RSMenu2.length] = new RMenuRes("netDelete","DELETEOBJECT","ɾ������","DeleteObj","javascript","","ɾ��ѡ��Ķ���","");
	RSMenu2[RSMenu2.length] = new RMenuRes("device_confirm","","-","","","","","");
	RSMenu2[RSMenu2.length] = new RMenuRes("device_confirm","CONFIRMDEVICE","ȷ���豸","ConfirmDevice","javascript","","ȷ��ѡ���豸","");
	RSMenu2[RSMenu2.length] = new RMenuRes("segment_confirm","CONFIRMDEVICE","ȷ�������豸","ConfirmDevice","javascript","","ȷ����ͼ������","");
	RSMenu2[RSMenu2.length] = new RMenuRes("device","","-","","","","","");
	RSMenu2[RSMenu2.length] = new RMenuRes("device","MODIFYATTRIBUTE","�޸�����","ModifyDeviceAttribute","javascript","","�޸��豸����","");
	RSMenu2[RSMenu2.length] = new RMenuRes("device","MODIFYTYPE","�޸��豸����","ModifyDeviceType","javascript","","�޸��豸����","");
	RSMenu2[RSMenu2.length] = new RMenuRes("device","MODIFYTYPE","�޸��豸ͼ��","ModifyDeviceIcon","javascript","","�޸��豸ͼ��","");
arrRMenu[arrRMenu.length] = new RMenuRes("","GOUP","�����豸","importTopo","javascript","","������һ��Topo","");
arrRMenu[arrRMenu.length] = new RMenuRes("-3","","ȷ�ϸ澯","ack_Warn","javascript","","ȷ�ϸ澯�����","");
arrRMenu[arrRMenu.length] = new RMenuRes("-3","","��λ�豸����","findDevObj","javascript","","��λ�豸����","");

