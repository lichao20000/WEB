//------------------------------------------------------------
// Copyright (c) 2004-2005 linkage. All rights reserved.
// Version 1.0.03
// Ahthor Haiteng.Yu
//------------------------------------------------------------
/**
 * 菜单数据结构
 * keyword：菜单所属关键字
 * name：菜单名称
 * link：菜单连接
 * desc：菜单描述
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



//网络视图的快捷菜单
var netMenu=new Array();
netMenu[netMenu.length]=new String("ADDOBJECT");//新增对象
netMenu[netMenu.length]=new String("ADDLINK");//新增对象
netMenu[netMenu.length]=new String("GOUP");//返回上一层
netMenu[netMenu.length]=new String("SAVEIMG");//导出拓扑图
netMenu[netMenu.length]=new String("WEBTOPO_DEVICE_DATA");//网元查询
netMenu[netMenu.length]=new String("LOADTOPO");//保存拓扑
netMenu[netMenu.length]=new String("WARN_VOICE_CONFIG");//保存拓扑
//netMenu[netMenu.length]=new String("VIEW_ALL_DEVICE");//显示所有网元
//netMenu[netMenu.length]=new String("VIEW_MANAGED_DEVICE");//显示被管网元
netMenu[netMenu.length]=new String("CONFIG_TOPOLAYER");//设置Topo层
netMenu[netMenu.length]=new String("WARN_ATUO_FRESH");//设置告警自动刷新
netMenu[netMenu.length]=new String("WARN_FRESH_BY_HAND");//告警手动刷新



//右健菜单资源
/*
	针对菜单情况的说明:
	如果该菜单是在任何地方可以点击显示，则添入"";
	如果该菜单是针对所有的设备、网段,则用"*",针对单个的对象
	如果该菜单是针对所有的设备、网段,则用"***",针对多个的对象
	如果该菜单针对普通网段,则填入"0"
	如果该菜单针对待确认设备网段,则填入"unkdevSeg"
	如果该菜单针对所有的单个设备,则填入"device";
	如果该菜单针对多个待确认设备,则填入"device_confirm";
	如果该菜单针对单个待确认设备,则填入"device_confirm_single";
	如果该菜单针对多个已确认设备,则填入"device_confirmed";
	如果该菜单针对单个已确认设备,则填入"device_confirmed_single";
	如果该菜单针对特定的设备类型，则填入该设备类型的序列号;
	如果该菜单针对告警,则填入"-3";

*/
var arrRMenu = new Array();
arrRMenu[arrRMenu.length] = new RMenuRes("","GOUP","返回上一层","getParentTopo.jsp","childFrm","","返回上一层Topo","");
arrRMenu[arrRMenu.length] = new RMenuRes("0","GODOWN","进入下一层","getChildTopo.jsp","childFrm","","进入下一层Topo","");
arrRMenu[arrRMenu.length] = new RMenuRes("device","","-","","","","","");
arrRMenu[arrRMenu.length] = new RMenuRes("device","OBJECTATTRIBUTE","属性","showDevDetail","javascript","","查看当前对象属性","");
arrRMenu[arrRMenu.length] = new RMenuRes("device","DEVICETOOL","实用工具","","","","","","RSMenu1");
	var RSMenu1=new Array();
	RSMenu1[RSMenu1.length]=new RMenuRes("device","PINGIP","Ping","pingIp","javascript","","Ping","");
	RSMenu1[RSMenu1.length]=new RMenuRes("device","PINGIP","Telnet","telnetLink","javascript","","Telnet","");
	RSMenu1[RSMenu1.length]=new RMenuRes("device","PINGIP","Tracert","tracertLink","javascript","","Tracert","");
arrRMenu[arrRMenu.length] = new RMenuRes("link","DELETELINK","删除链路","DeleteLinkObj","javascript","","删除选择的设备","");
arrRMenu[arrRMenu.length] = new RMenuRes("link","LINK_FLUX","链路流量","showLinkInfo","javascript","","对链路进行操作","");
arrRMenu[arrRMenu.length] = new RMenuRes("link","DELETELINK","链路样式","editLinkCss","javascript","","链路样式配置","");	
arrRMenu[arrRMenu.length] = new RMenuRes("device","OPERATION","设备信息","","","","察看设备信息","","RSMenu6");
	var RSMenu6=new Array();
	RSMenu6[RSMenu6.length] = new RMenuRes("*","","系统信息","showSysInfo","javascript","","查看当前设备的系统信息","");
	RSMenu6[RSMenu6.length] = new RMenuRes("*","","设备流量","showFlux","javascript","","查看当前设备的流量","");
	RSMenu6[RSMenu6.length] = new RMenuRes("*","","设备性能","showPmee","javascript","","查看当前设备的性能","");
	RSMenu6[RSMenu6.length] = new RMenuRes("*","","端口信息","showPortInfo","javascript","","查看当前设备的端口信息","");
	RSMenu6[RSMenu6.length] = new RMenuRes("*","","设备告警","showDeviceWarn","javascript","","查看当前设备的告警","");
	RSMenu6[RSMenu6.length] = new RMenuRes("*","","路由信息","showDeviceRoute","javascript","","查看路由信息","");
	RSMenu6[RSMenu6.length] = new RMenuRes("*","","设备状态信息","showDeviceStatus","javascript","","查看当前设备的端口,速率,状态等信息","");
	RSMenu6[RSMenu6.length] = new RMenuRes("*","","设备性能监控","showFluxAndPmee","javascript","","查看当前设备的性能和流量等信息","");
	RSMenu6[RSMenu6.length] = new RMenuRes("*","","EVDO卡信息","showEVDOInfo","javascript","","查看当前EVDO的卡信息","");
arrRMenu[arrRMenu.length] = new RMenuRes("device","DEVICECONFIG","设备配置","","","","对设备进行配置","","RSMenu7");
	var RSMenu7=[];
	RSMenu7[RSMenu7.length]=new RMenuRes("device","CAPABILITYCONFIG","性能配置","ConfigPerf","javascript","","性能配置","");
	RSMenu7[RSMenu7.length]=new RMenuRes("device","FLUXCONFIG","流量配置","ConfigFlux","javascript","","流量配置","");
	RSMenu7[RSMenu7.length]=new RMenuRes("device","IPCHECKCONFIG","IPCHECK配置","ConfigIpCheck","javascript","","IPCHECK配置","");
arrRMenu[arrRMenu.length] = new RMenuRes("device","STATUSWATCH","状态监控","","","","对设备进行状态监控","","RSMenu5");
	var RSMenu5=new Array();
	RSMenu5[RSMenu5.length] = new RMenuRes("device","TESTCONNECTION","测试连接","TestConnection","javascript","","测试设备连接状态","");
	RSMenu5[RSMenu5.length] = new RMenuRes("device","RPCMethod","获取RPC方法","RPCMethod","javascript","","获取RPC方法","");

arrRMenu[arrRMenu.length] = new RMenuRes("device","WARCONFIRM","故障诊断","","","","对设备进行诊断","","RSMenu3");
	var RSMenu3=new Array();
	RSMenu3[RSMenu3.length] = new RMenuRes("*","PINGCONFIRM","Ping检测","pingTest","javascript","","Ping检测","");
	RSMenu3[RSMenu3.length] = new RMenuRes("*","ATMF5LOOPCONFIRM","ATMF5LOOP检测","ATMTest","javascript","","ATMF5LOOP检测","");
	RSMenu3[RSMenu3.length] = new RMenuRes("*","DSLCONFIRM","DSL检测","DSLTest","javascript","DSLTest","DSL检测","");
	RSMenu3[RSMenu3.length] = new RMenuRes("*","DEVICERESET","恢复出厂设置","deviceReset","javascript","","恢复出厂设置","");
	RSMenu3[RSMenu3.length] = new RMenuRes("*","DEVICEREBOOT","重启设备","deviceReboot","javascript","","重启设备","");
arrRMenu[arrRMenu.length] = new RMenuRes("unkdevSeg","CONFIRMDEVICE","确认所有设备","ConfirmAllDev","javascript","","确认待确认网段下的所有设备","");
arrRMenu[arrRMenu.length] = new RMenuRes("device_confirmed_single","DEVICESCOUT","参数监视","","","","确认设备参数监视","","RSMenu4");
	var RSMenu4=new Array();
	RSMenu4[RSMenu4.length] = new RMenuRes("*","GETPARAMETERMODULE","参数模型获取","getParaModule","javascript","","参数模型获取","");
	RSMenu4[RSMenu4.length] = new RMenuRes("*","PARAMETERQUERY","参数查询","paraQuery","javascript","","参数查询","");
	RSMenu4[RSMenu4.length] = new RMenuRes("*","PARAMETERCHANGECALL","参数变化上报","paraChangCall","javascript","","参数变化上报","");
  	RSMenu4[RSMenu4.length] = new RMenuRes("*","PARAMETERCHANGECALL","常见参数","getFamiliarPara","javascript","","常见参数","");
  	RSMenu4[RSMenu4.length] = new RMenuRes("*","PARAMETERCHANGECALL","vlan信息","getVlanInfo","javascript","","vlan信息","");
  	RSMenu4[RSMenu4.length] = new RMenuRes("*","PARAMETERCHANGECALL","DHCP配置信息","getDhcpInfo","javascript","","DHCP配置信息","");
  	RSMenu4[RSMenu4.length] = new RMenuRes("*","PARAMETERCHANGECALL","wlan信息","getwlanInfo","javascript","","vlan信息","");
	arrRMenu[arrRMenu.length] = new RMenuRes("***","OPERATION","操作","","","","对设备进行管理","","RSMenu2");
	var RSMenu2=new Array();
	//RSMenu2[0] = new RMenuRes("device","TESTCONNECTION","测试连接","TestConnection","javascript","","测试设备连接状态","");	
	//RSMenu2[RSMenu2.length] = new RMenuRes("netmanager1","MANAGEDEVICEWIN","管理网元","ManagerDeviceWin","javascript","","管理网元","");
	//RSMenu2[RSMenu2.length] = new RMenuRes("netmanager2","UNMANAGEDEVICEWIN","取消管理","UnManagerDeviceWin","javascript","","取消管理网元","");
	//RSMenu2[3] = new RMenuRes("device","RPCMethod","状态监控","RPCMethod","javascript","","状态监控","");
	RSMenu2[RSMenu2.length] = new RMenuRes("netCut","CUT"," 剪 切","CutNet","javascript","","剪切网元","");
	RSMenu2[RSMenu2.length] = new RMenuRes("netPase","PASTE"," 粘 贴 ","PasteNet","javascript","","剪切网元","");
	RSMenu2[RSMenu2.length] = new RMenuRes("netPase","CANCELCUT","取消剪切","CancelCut","javascript","","剪切网元","");
	//RSMenu2[RSMenu2.length] = new RMenuRes("netDelete","DELETEOBJECT","删除对象","DeleteObj","javascript","","删除选择的对象","");
	RSMenu2[RSMenu2.length] = new RMenuRes("device_confirm","","-","","","","","");
	RSMenu2[RSMenu2.length] = new RMenuRes("device_confirm","CONFIRMDEVICE","确认设备","ConfirmDevice","javascript","","确认选中设备","");
	RSMenu2[RSMenu2.length] = new RMenuRes("segment_confirm","CONFIRMDEVICE","确认所有设备","ConfirmDevice","javascript","","确认云图下所有","");
	RSMenu2[RSMenu2.length] = new RMenuRes("device","","-","","","","","");
	RSMenu2[RSMenu2.length] = new RMenuRes("device","MODIFYATTRIBUTE","修改属性","ModifyDeviceAttribute","javascript","","修改设备属性","");
	RSMenu2[RSMenu2.length] = new RMenuRes("device","MODIFYTYPE","修改设备类型","ModifyDeviceType","javascript","","修改设备类型","");
	RSMenu2[RSMenu2.length] = new RMenuRes("device","MODIFYTYPE","修改设备图标","ModifyDeviceIcon","javascript","","修改设备图标","");
arrRMenu[arrRMenu.length] = new RMenuRes("","GOUP","导入设备","importTopo","javascript","","返回上一层Topo","");
arrRMenu[arrRMenu.length] = new RMenuRes("-3","","确认告警","ack_Warn","javascript","","确认告警并入库","");
arrRMenu[arrRMenu.length] = new RMenuRes("-3","","定位设备对象","findDevObj","javascript","","定位设备对象","");

