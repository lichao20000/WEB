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
netMenu[0]=new String("HOST_ADDOBJECT");
netMenu[1]=new String("HOST_SAVETOPO");
netMenu[2]=new String("HOST_GOUP");



//�ҽ��˵���Դ
//�ҽ��˵���Դ
/*
	��Բ˵������˵��:
	����ò˵������κεط����Ե����ʾ��������"";
	����ò˵���������е��豸�����Ρ���·,����"*",��Ե����Ķ���
	����ò˵���������е��豸�����Ρ���·,����"***",��Զ���Ķ���
	����ò˵��������,������"1"
	����ò˵�������е��豸,������"node";
	����ò˵����cpe�豸��������"device_cpe"
	����ò˵�����ض����豸���ͣ���������豸���͵����к�;
	����ò˵������·,������"link";	
	����ò˵���Ը澯,������"-3";
	����ò˵�������κ���Ԫ��������"nodes";
	���⴦��
	�����Ԫ��������⴦��

*/
var arrRMenu = new Array();
arrRMenu[0] = new RMenuRes("","HOST_GOUP","������һ��","getParentTopo.jsp","childFrm","","������һ��Topo","");
arrRMenu[1] = new RMenuRes("1","HOST_GODOWN","������һ��","getChildTopo.jsp","childFrm","","������һ��Topo","");
arrRMenu[2] = new RMenuRes("*","","-","","","","","");
arrRMenu[3] = new RMenuRes("node","HOST_OBJECTATTRIBUTE","��������","HOST_ShowObjectAttr","javascript","","�鿴��ǰ��������","");

//�澯�б��ҽ��˵�

arrRMenu[4] = new RMenuRes("-3","CHECKALARM","ȷ�ϸ澯","ack_Warn","javascript","","ȷ�ϸ澯","");
arrRMenu[5] = new RMenuRes("-3","DELETEALARM","ɾ���澯","remove_Warn","javascript","","ɾ���澯","");
//arrRMenu[6] = new RMenuRes("-3","ALARMQUERY","�澯����","query_Warn","javascript","","�澯����","");
arrRMenu[6] = new RMenuRes("-3","CHECKSTOREALARM","ȷ�ϲ����","ack_Store_Warn","javascript","","ȷ�ϸ澯�����","");
arrRMenu[7] = new RMenuRes("node","HOST_EDITATTRIBUTE","�޸Ķ���","HOST_EditObjectAttr","javascript","","�޸ĵ�ǰ��������","");
arrRMenu[8] = new RMenuRes("nodes","HOST_DELETEOBJECT","ɾ������","HOST_DelObject","javascript","","ɾ��ѡ��Ķ���","");
arrRMenu[9] = new RMenuRes("node","HOST_PERFCONFIG","��������","HOST_perfConfig","javascript","","������������","");
arrRMenu[10] = new RMenuRes("node","HOST_WARNCONFIG","�澯����","HOST_warnConfig","javascript","","���и澯����","");
arrRMenu[11] = new RMenuRes("node","HOST_PERFVIEW","���ܲ鿴","HOST_perfView","javascript","","���ܲ鿴","");
arrRMenu[12] = new RMenuRes("node","HOST_WARN","����澯","showDeviceWarn","javascript","","�鿴��ǰ�豸�ĸ澯","");
arrRMenu[13] = new RMenuRes("-3","FINDDEVICEOBJ","��λ�豸����","findDevObj","javascript","","��λ�豸����","");
