package action.confTaskView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.paramConfig.ConnObj;
import com.linkage.litms.paramConfig.NodeObj;
import com.linkage.litms.paramConfig.ParamInfoAct;
import com.linkage.litms.paramConfig.PvcObj;

/**
 * @author Jason(3412)
 * @date 2008-12-24
 */
public class ManageWanConnEditAction {
	/** log */
	private static Logger logger = LoggerFactory.getLogger(ManageWanConnEditAction.class);

	private String device_id = "";
	private String wan_id = "";
	private String wan_conn_id = "";
	private String wan_conn_sess_id = "";
	private String sess_type = "";
	private String enable = "";
	private String name = "";
	private String conn_type = "";
	private String serv_list = "";
	private String bind_port = "";
	private String username = "";
	private String password = "";
	private String ip_type = "";
	private String ip = "";
	private String mask = "";
	private String gateway = "";
	private String dns = "";
	private String enable_old = "";
	private String name_old = "";
	private String conn_type_old = "";
	private String serv_list_old = "";
	private String bind_port_old = "";
	private String username_old = "";
	private String password_old = "";
	private String ip_type_old = "";
	private String ip_old = "";
	private String mask_old = "";
	private String gateway_old = "";
	private String dns_old = "";
	
	private String vpi = "";
	private String vci = "";
	private String vlan = "";
	private String pvc_type = "";
	
	private String ajax;

	public String edit() {
		
		ConnObj connObj = new ConnObj();
		NodeObj[] nodeObj = new NodeObj[19];
		
		nodeObj[0] = new NodeObj();
		nodeObj[1] = new NodeObj();
		nodeObj[2] = new NodeObj();
		nodeObj[3] = new NodeObj();
		nodeObj[4] = new NodeObj();
		nodeObj[5] = new NodeObj();
		nodeObj[6] = new NodeObj();
		nodeObj[7] = new NodeObj();
		nodeObj[8] = new NodeObj();
		nodeObj[9] = new NodeObj();
		nodeObj[10] = new NodeObj();
		nodeObj[11] = new NodeObj();
		nodeObj[12] = new NodeObj();
		nodeObj[13] = new NodeObj();
		nodeObj[14] = new NodeObj();
		nodeObj[15] = new NodeObj();
		nodeObj[16] = new NodeObj();
		nodeObj[17] = new NodeObj();
		nodeObj[18] = new NodeObj();
		
		String isModify = "1";
		String isNoModify = "0";
		
		connObj.setDevice_id(device_id);
		connObj.setWan_id(wan_id);
		connObj.setWan_conn_id(wan_conn_id);
		connObj.setWan_conn_sess_id(wan_conn_sess_id);
		connObj.setSess_type(sess_type);
		
		logger.debug("ManageWanConnEditAction:device_id=>"+device_id);
		logger.debug("ManageWanConnEditAction:wan_id=>"+wan_id);
		logger.debug("ManageWanConnEditAction:wan_conn_id=>"+wan_conn_id);
		logger.debug("ManageWanConnEditAction:wan_conn_sess_id=>"+wan_conn_sess_id);
		logger.debug("ManageWanConnEditAction:sess_type=>"+sess_type);
		
		if(!enable.equals(enable_old)){
			nodeObj[0].setName("enable");
			nodeObj[0].setValue(enable);
			nodeObj[0].setIsModified(isModify);
		}else{
			nodeObj[0].setName("enable");
			nodeObj[0].setIsModified(isNoModify);
		}

		logger.debug("ManageWanConnEditAction:nodeObj[0]=>"+nodeObj[0].getName());
		logger.debug("ManageWanConnEditAction:nodeObj[0]=>"+nodeObj[0].getValue());
		logger.debug("ManageWanConnEditAction:nodeObj[0]=>"+nodeObj[0].getIsModified());
		
		if(!name.equals(name_old)){
			nodeObj[1].setName("name");
			nodeObj[1].setValue(name);
			nodeObj[1].setIsModified(isModify);
		}else{
			nodeObj[1].setName("name");
			nodeObj[1].setIsModified(isNoModify);
		}
		logger.debug("ManageWanConnEditAction:nodeObj[1]=>"+nodeObj[1].getName());
		logger.debug("ManageWanConnEditAction:nodeObj[1]=>"+nodeObj[1].getValue());
		logger.debug("ManageWanConnEditAction:nodeObj[1]=>"+nodeObj[1].getIsModified());
		
		if(!conn_type.equals(conn_type_old)){
			nodeObj[2].setName("conn_type");
			nodeObj[2].setValue(conn_type);
			nodeObj[2].setIsModified(isModify);
		}else{
			nodeObj[2].setName("conn_type");
			nodeObj[2].setIsModified(isNoModify);
		}
		
		logger.debug("ManageWanConnEditAction:nodeObj[2]=>"+nodeObj[2].getName());
		logger.debug("ManageWanConnEditAction:nodeObj[2]=>"+nodeObj[2].getValue());
		logger.debug("ManageWanConnEditAction:nodeObj[2]=>"+nodeObj[2].getIsModified());
		
		if(!serv_list.equals(serv_list_old)){
			nodeObj[3].setName("serv_list");
			nodeObj[3].setValue(serv_list);
			nodeObj[3].setIsModified(isModify);
		}else{
			nodeObj[3].setName("serv_list");
			nodeObj[3].setIsModified(isNoModify);
		}
		
		logger.debug("ManageWanConnEditAction:nodeObj[3]=>"+nodeObj[3].getName());
		logger.debug("ManageWanConnEditAction:nodeObj[3]=>"+nodeObj[3].getValue());
		logger.debug("ManageWanConnEditAction:nodeObj[3]=>"+nodeObj[3].getIsModified());
		
		if(!bind_port.equals(bind_port_old)){
			nodeObj[4].setName("bind_port");
			nodeObj[4].setValue(bind_port);
			nodeObj[4].setIsModified(isModify);
		}else{
			nodeObj[4].setName("bind_port");
			nodeObj[4].setIsModified(isNoModify);
		}
		logger.debug("ManageWanConnEditAction:bind_port_old=>"+bind_port_old);
		logger.debug("ManageWanConnEditAction:nodeObj[4]=>"+nodeObj[4].getName());
		logger.debug("ManageWanConnEditAction:nodeObj[4]=>"+nodeObj[4].getValue());
		logger.debug("ManageWanConnEditAction:nodeObj[4]=>"+nodeObj[4].getIsModified());
		
		if(!username.equals(username_old)){
			nodeObj[5].setName("username");
			nodeObj[5].setValue(username);
			nodeObj[5].setIsModified(isModify);
		}else{
			nodeObj[5].setName("username");
			nodeObj[5].setIsModified(isNoModify);
		}
		
		logger.debug("ManageWanConnEditAction:nodeObj[5]=>"+nodeObj[5].getName());
		logger.debug("ManageWanConnEditAction:nodeObj[5]=>"+nodeObj[5].getValue());
		logger.debug("ManageWanConnEditAction:nodeObj[5]=>"+nodeObj[5].getIsModified());
		
		if(!password.equals(password_old)){
			nodeObj[6].setName("password");
			nodeObj[6].setValue(password);
			nodeObj[6].setIsModified(isModify);
		}else{
			nodeObj[6].setName("password");
			nodeObj[6].setIsModified(isNoModify);
		}
		logger.debug("ManageWanConnEditAction:nodeObj[6]=>"+nodeObj[6].getName());
		logger.debug("ManageWanConnEditAction:nodeObj[6]=>"+nodeObj[6].getValue());
		logger.debug("ManageWanConnEditAction:nodeObj[6]=>"+nodeObj[6].getIsModified());
		
		if(!ip_type.equals(ip_type_old)){
			nodeObj[7].setName("ip_type");
			nodeObj[7].setValue(ip_type);
			nodeObj[7].setIsModified(isModify);
		}else{
			nodeObj[7].setName("ip_type");
			nodeObj[7].setIsModified(isNoModify);
		}
		logger.debug("ManageWanConnEditAction:nodeObj[7]=>"+nodeObj[7].getName());
		logger.debug("ManageWanConnEditAction:nodeObj[7]=>"+nodeObj[7].getValue());
		logger.debug("ManageWanConnEditAction:nodeObj[7]=>"+nodeObj[7].getIsModified());
		
		if(!ip.equals(ip_old)){
			nodeObj[8].setName("ip");
			nodeObj[8].setValue(ip);
			nodeObj[8].setIsModified(isModify);
		}else{
			nodeObj[8].setName("ip");
			nodeObj[8].setIsModified(isNoModify);
		}
		logger.debug("ManageWanConnEditAction:nodeObj[8]=>"+nodeObj[8].getName());
		logger.debug("ManageWanConnEditAction:nodeObj[8]=>"+nodeObj[8].getValue());
		logger.debug("ManageWanConnEditAction:nodeObj[8]=>"+nodeObj[8].getIsModified());
		
		if(!mask.equals(mask_old)){
			nodeObj[9].setName("mask");
			nodeObj[9].setValue(mask);
			nodeObj[9].setIsModified(isModify);
		}else{
			nodeObj[9].setName("mask");
			nodeObj[9].setIsModified(isNoModify);
		}
		logger.debug("ManageWanConnEditAction:nodeObj[9]=>"+nodeObj[9].getName());
		logger.debug("ManageWanConnEditAction:nodeObj[9]=>"+nodeObj[9].getValue());
		logger.debug("ManageWanConnEditAction:nodeObj[9]=>"+nodeObj[9].getIsModified());
		
		if(!gateway.equals(gateway_old)){
			nodeObj[10].setName("gateway");
			nodeObj[10].setValue(gateway);
			nodeObj[10].setIsModified(isModify);
		}else{
			nodeObj[10].setName("gateway");
			nodeObj[10].setIsModified(isNoModify);
		}
		logger.debug("ManageWanConnEditAction:nodeObj[10]=>"+nodeObj[10].getName());
		logger.debug("ManageWanConnEditAction:nodeObj[10]=>"+nodeObj[10].getValue());
		logger.debug("ManageWanConnEditAction:nodeObj[10]=>"+nodeObj[10].getIsModified());
		
		if(!dns.equals(dns_old)){
			nodeObj[11].setName("dns");
			nodeObj[11].setValue(dns);
			nodeObj[11].setIsModified(isModify);
		}else{
			nodeObj[11].setName("dns");
			nodeObj[11].setIsModified(isNoModify);
		}
		logger.debug("ManageWanConnEditAction:nodeObj[11]=>"+nodeObj[11].getName());
		logger.debug("ManageWanConnEditAction:nodeObj[11]=>"+nodeObj[11].getValue());
		logger.debug("ManageWanConnEditAction:nodeObj[11]=>"+nodeObj[11].getIsModified());
		
		connObj.setNodeObj(nodeObj);
		
		if ((new ParamInfoAct(true)).updateConnection(device_id, wan_id, wan_conn_id,connObj)) {
			ajax = "结点更新成功,请点击【获取连接】获取最新值！";
		}else{
			ajax = "结点更新失败";
		}
		return "ajax";
	}

	public String add() {
		
		int vci_vci ;
		int int_sess_type = Integer.parseInt(sess_type);
		
		if("vpi/vci".equals(pvc_type)){
			vci_vci = 0;
		}else{
			vci_vci = 1;
		}
		
		PvcObj pvcObj = new PvcObj();
		ConnObj connObj = new ConnObj();
		
		pvcObj.setDevice_id(device_id);
		pvcObj.setWan_id(wan_id);
		pvcObj.setVci_id(vci);
		pvcObj.setVpi_id(vpi);
		pvcObj.setVlan_id(vlan);
		
		logger.debug("ManageWanConnEditAction:add:vci_vci=>"+vci_vci);
		logger.debug("ManageWanConnEditAction:add:int_sess_type=>"+int_sess_type);
		logger.debug("ManageWanConnEditAction:add:vci=>"+vci);
		logger.debug("ManageWanConnEditAction:add:vpi=>"+vpi);
		logger.debug("ManageWanConnEditAction:add:vlan=>"+vlan);
		
		NodeObj[] nodeObj = new NodeObj[11];
		
		nodeObj[0] = new NodeObj();
		nodeObj[1] = new NodeObj();
		nodeObj[2] = new NodeObj();
		nodeObj[3] = new NodeObj();
		nodeObj[4] = new NodeObj();
		nodeObj[5] = new NodeObj();
		nodeObj[6] = new NodeObj();
		nodeObj[7] = new NodeObj();
		nodeObj[8] = new NodeObj();
		nodeObj[9] = new NodeObj();
		nodeObj[10] = new NodeObj();
		
		String isModify = "1";
		
		connObj.setDevice_id(device_id);
		connObj.setWan_id(wan_id);
		connObj.setSess_type(sess_type);
		
		logger.debug("ManageWanConnEditAction:add:device_id=>"+device_id);
		logger.debug("ManageWanConnEditAction:add:wan_id=>"+wan_id);
		logger.debug("ManageWanConnEditAction:add:sess_type=>"+sess_type);
		
		nodeObj[0].setName("enable");
		nodeObj[0].setValue(enable);
		nodeObj[0].setIsModified(isModify);

		logger.debug("ManageWanConnEditAction:add:nodeObj[0]=>"+nodeObj[0].getName());
		logger.debug("ManageWanConnEditAction:add:nodeObj[0]=>"+nodeObj[0].getValue());
		logger.debug("ManageWanConnEditAction:add:nodeObj[0]=>"+nodeObj[0].getIsModified());
		
		nodeObj[1].setName("conn_type");
		nodeObj[1].setValue(conn_type);
		nodeObj[1].setIsModified(isModify);
		
		logger.debug("ManageWanConnEditAction:add:nodeObj[1]=>"+nodeObj[1].getName());
		logger.debug("ManageWanConnEditAction:add:nodeObj[1]=>"+nodeObj[1].getValue());
		logger.debug("ManageWanConnEditAction:add:nodeObj[1]=>"+nodeObj[1].getIsModified());
		
		nodeObj[2].setName("serv_list");
		nodeObj[2].setValue(serv_list);
		nodeObj[2].setIsModified(isModify);
		
		logger.debug("ManageWanConnEditAction:add:nodeObj[2]=>"+nodeObj[2].getName());
		logger.debug("ManageWanConnEditAction:add:nodeObj[2]=>"+nodeObj[2].getValue());
		logger.debug("ManageWanConnEditAction:add:nodeObj[2]=>"+nodeObj[2].getIsModified());
		
		nodeObj[3].setName("bind_port");
		nodeObj[3].setValue(bind_port);
		nodeObj[3].setIsModified(isModify);
		
		logger.debug("ManageWanConnEditAction:add:nodeObj[3]=>"+nodeObj[3].getName());
		logger.debug("ManageWanConnEditAction:add:nodeObj[3]=>"+nodeObj[3].getValue());
		logger.debug("ManageWanConnEditAction:add:nodeObj[3]=>"+nodeObj[3].getIsModified());
		
		nodeObj[4].setName("username");
		nodeObj[4].setValue(username);
		nodeObj[4].setIsModified(isModify);
		
		logger.debug("ManageWanConnEditAction:add:nodeObj[4]=>"+nodeObj[4].getName());
		logger.debug("ManageWanConnEditAction:add:nodeObj[4]=>"+nodeObj[4].getValue());
		logger.debug("ManageWanConnEditAction:add:nodeObj[4]=>"+nodeObj[4].getIsModified());
		
		nodeObj[5].setName("password");
		nodeObj[5].setValue(password);
		nodeObj[5].setIsModified(isModify);

		logger.debug("ManageWanConnEditAction:add:nodeObj[5]=>"+nodeObj[5].getName());
		logger.debug("ManageWanConnEditAction:add:nodeObj[5]=>"+nodeObj[5].getValue());
		logger.debug("ManageWanConnEditAction:add:nodeObj[5]=>"+nodeObj[5].getIsModified());
		
		nodeObj[6].setName("ip_type");
		nodeObj[6].setValue(ip_type);
		nodeObj[6].setIsModified(isModify);

		logger.debug("ManageWanConnEditAction:add:nodeObj[6]=>"+nodeObj[6].getName());
		logger.debug("ManageWanConnEditAction:add:nodeObj[6]=>"+nodeObj[6].getValue());
		logger.debug("ManageWanConnEditAction:add:nodeObj[6]=>"+nodeObj[6].getIsModified());
		
		nodeObj[7].setName("ip");
		nodeObj[7].setValue(ip);
		nodeObj[7].setIsModified(isModify);
		
		logger.debug("ManageWanConnEditAction:add:nodeObj[7]=>"+nodeObj[7].getName());
		logger.debug("ManageWanConnEditAction:add:nodeObj[7]=>"+nodeObj[7].getValue());
		logger.debug("ManageWanConnEditAction:add:nodeObj[7]=>"+nodeObj[7].getIsModified());
		
		nodeObj[8].setName("mask");
		nodeObj[8].setValue(mask);
		nodeObj[8].setIsModified(isModify);
		
		logger.debug("ManageWanConnEditAction:add:nodeObj[8]=>"+nodeObj[8].getName());
		logger.debug("ManageWanConnEditAction:add:nodeObj[8]=>"+nodeObj[8].getValue());
		logger.debug("ManageWanConnEditAction:add:nodeObj[8]=>"+nodeObj[8].getIsModified());
		
		nodeObj[9].setName("gateway");
		nodeObj[9].setValue(gateway);
		nodeObj[9].setIsModified(isModify);
		
		logger.debug("ManageWanConnEditAction:add:nodeObj[9]=>"+nodeObj[9].getName());
		logger.debug("ManageWanConnEditAction:add:nodeObj[9]=>"+nodeObj[9].getValue());
		logger.debug("ManageWanConnEditAction:add:nodeObj[9]=>"+nodeObj[9].getIsModified());
		
		nodeObj[10].setName("dns");
		nodeObj[10].setValue(dns);
		nodeObj[10].setIsModified(isModify);
		
		logger.debug("ManageWanConnEditAction:add:nodeObj[10]=>"+nodeObj[10].getName());
		logger.debug("ManageWanConnEditAction:add:nodeObj[10]=>"+nodeObj[10].getValue());
		logger.debug("ManageWanConnEditAction:add:nodeObj[10]=>"+nodeObj[10].getIsModified());
		
		connObj.setNodeObj(nodeObj);
		
		if ((new ParamInfoAct(true)).AddConn(pvcObj, connObj, vci_vci,int_sess_type)) {
			ajax = "结点新增成功！";
		}else{
			ajax = "结点新增失败";
		}
		return "ajax";
	}

	public String getAjax() {
		return ajax;
	}

	public void setAjax(String ajax) {
		this.ajax = ajax;
	}


	public String getBind_port() {
		return bind_port;
	}


	public void setBind_port(String bind_port) {
		this.bind_port = bind_port;
	}


	public String getBind_port_old() {
		return bind_port_old;
	}


	public void setBind_port_old(String bind_port_old) {
		this.bind_port_old = bind_port_old;
	}


	public String getConn_type() {
		return conn_type;
	}


	public void setConn_type(String conn_type) {
		this.conn_type = conn_type;
	}


	public String getConn_type_old() {
		return conn_type_old;
	}


	public void setConn_type_old(String conn_type_old) {
		this.conn_type_old = conn_type_old;
	}


	public String getDevice_id() {
		return device_id;
	}


	public void setDevice_id(String device_id) {
		this.device_id = device_id;
	}


	public String getDns() {
		return dns;
	}


	public void setDns(String dns) {
		this.dns = dns;
	}


	public String getDns_old() {
		return dns_old;
	}


	public void setDns_old(String dns_old) {
		this.dns_old = dns_old;
	}


	public String getEnable() {
		return enable;
	}


	public void setEnable(String enable) {
		this.enable = enable;
	}


	public String getEnable_old() {
		return enable_old;
	}


	public void setEnable_old(String enable_old) {
		this.enable_old = enable_old;
	}


	public String getGateway() {
		return gateway;
	}


	public void setGateway(String gateway) {
		this.gateway = gateway;
	}


	public String getGateway_old() {
		return gateway_old;
	}


	public void setGateway_old(String gateway_old) {
		this.gateway_old = gateway_old;
	}


	public String getIp() {
		return ip;
	}


	public void setIp(String ip) {
		this.ip = ip;
	}


	public String getIp_old() {
		return ip_old;
	}


	public void setIp_old(String ip_old) {
		this.ip_old = ip_old;
	}


	public String getIp_type() {
		return ip_type;
	}


	public void setIp_type(String ip_type) {
		this.ip_type = ip_type;
	}


	public String getIp_type_old() {
		return ip_type_old;
	}


	public void setIp_type_old(String ip_type_old) {
		this.ip_type_old = ip_type_old;
	}


	public String getMask() {
		return mask;
	}


	public void setMask(String mask) {
		this.mask = mask;
	}


	public String getMask_old() {
		return mask_old;
	}


	public void setMask_old(String mask_old) {
		this.mask_old = mask_old;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getName_old() {
		return name_old;
	}


	public void setName_old(String name_old) {
		this.name_old = name_old;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public String getPassword_old() {
		return password_old;
	}


	public void setPassword_old(String password_old) {
		this.password_old = password_old;
	}


	public String getServ_list() {
		return serv_list;
	}


	public void setServ_list(String serv_list) {
		this.serv_list = serv_list;
	}


	public String getServ_list_old() {
		return serv_list_old;
	}


	public void setServ_list_old(String serv_list_old) {
		this.serv_list_old = serv_list_old;
	}


	public String getSess_type() {
		return sess_type;
	}


	public void setSess_type(String sess_type) {
		this.sess_type = sess_type;
	}


	public String getUsername() {
		return username;
	}


	public void setUsername(String username) {
		this.username = username;
	}


	public String getUsername_old() {
		return username_old;
	}


	public void setUsername_old(String username_old) {
		this.username_old = username_old;
	}


	public String getWan_conn_id() {
		return wan_conn_id;
	}


	public void setWan_conn_id(String wan_conn_id) {
		this.wan_conn_id = wan_conn_id;
	}


	public String getWan_conn_sess_id() {
		return wan_conn_sess_id;
	}


	public void setWan_conn_sess_id(String wan_conn_sess_id) {
		this.wan_conn_sess_id = wan_conn_sess_id;
	}


	public String getWan_id() {
		return wan_id;
	}


	public void setWan_id(String wan_id) {
		this.wan_id = wan_id;
	}

	public String getPvc_type() {
		return pvc_type;
	}

	public void setPvc_type(String pvc_type) {
		this.pvc_type = pvc_type;
	}

	public String getVci() {
		return vci;
	}

	public void setVci(String vci) {
		this.vci = vci;
	}

	public String getVlan() {
		return vlan;
	}

	public void setVlan(String vlan) {
		this.vlan = vlan;
	}

	public String getVpi() {
		return vpi;
	}

	public void setVpi(String vpi) {
		this.vpi = vpi;
	}

}
