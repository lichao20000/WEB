package com.linkage.litms.webtopo;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import RemoteDB.ControlManager;
import RemoteDB.VPN_DragEvent;
import RemoteDB.WebPosition;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.litms.common.util.Encoder;
import com.linkage.litms.common.util.GZIPHandler;

public class VpnControlManager {
	private static Logger logger = LoggerFactory.getLogger(VpnControlManager.class);
	
	private static ControlManager manager = null;

	private String account = null;

	private String passwd = null;

	private VpnScheduler vpnScheduler = null;

	/**
	 * 构造函数
	 * 
	 * @param account
	 * @param passwd
	 */
	public VpnControlManager(String account, String passwd) {
		this.account = account;
		this.passwd = passwd;

		rebindManager();
	}

	/**
	 * 绑定corba对象
	 */
	public void rebindManager() {
		vpnScheduler = new VpnScheduler();
		vpnScheduler.reloadRemoteDB();
		manager = vpnScheduler.getControlManager(account, passwd);
	}

	/**
	 * 发送司配置规则到Pmee
	 * 
	 * @param deviceid
	 * @param expressionID
	 * @param gatherflag
	 * @return boolean
	 */
	public boolean SendToPmee(String deviceid, String expressionID,
			boolean gatherflag) {
		boolean b = true;
		String strSQL1 = "update pm_map_instance set collect=1 where device_id=? and expressionid=?";
		String strSQL2 = "update pm_map_instance set collect=0 where device_id=? and expressionid=?";
		String[] ids = new String[1];
		ids[0] = deviceid;

		if (gatherflag) {
			PrepareSQL pSQL = new PrepareSQL(strSQL1);
			pSQL.setStringExt(1, deviceid, true);
			pSQL.setStringExt(2, expressionID, false);
			logger.debug("collect=1:" + pSQL.getSQL());

			if (DataSetBean.executeUpdate(pSQL.getSQL()) >= 0) {

				try {
					if (manager.StartGatherData((String) expressionID, ids) == 0) {
						logger.warn("调用后台接口成功！");
					} else {
						logger.warn("调用后台接口失败！");
						b = false;
					}
				} catch (Exception ex) {
					logger.error(ex.getMessage());

					rebindManager();
					if (manager.StartGatherData((String) expressionID, ids) == 0) {
						logger.warn("调用后台接口成功！");
					} else {
						logger.warn("调用后台接口失败！");
						b = false;
					}
				}
			}
		} else {
			PrepareSQL pSQL = new PrepareSQL(strSQL2);
			pSQL.setStringExt(1, deviceid, true);
			pSQL.setStringExt(2, expressionID, false);
			logger.debug("collect=2:" + pSQL.getSQL());

			if (DataSetBean.executeUpdate(pSQL.getSQL()) >= 0) {

				try {
					if (manager.StopGatherData((String) expressionID, ids) == 0) {
						logger.warn("调用后台接口成功！");
					} else {
						logger.warn("调用后台接口失败！");
						b = false;
					}
				} catch (Exception ex) {
					logger.error(ex.getMessage());

					rebindManager();
					if (manager.StopGatherData((String) expressionID, ids) == 0) {
						logger.warn("调用后台接口成功！");
					} else {
						logger.warn("调用后台接口失败！");
						b = false;
					}
				}
			}
		}

		return b;

	}

	/**
	 * 删除选择网元
	 * 
	 * @param ids
	 * @return boolean
	 */
	public boolean delSelectDev(String[] ids) {
		boolean b = true;

		try {
			if (manager.PmeeReadDevices(ids) == 0) {
				logger.warn("调用后台接口成功！");
			} else {
				logger.warn("调用后台接口失败");
				b = false;
			}
		} catch (Exception ex) {
			ex.printStackTrace();

			rebindManager();
			if (manager.PmeeReadDevices(ids) == 0) {
				logger.warn("调用后台接口成功！");
			} else {
				logger.warn("调用后台接口失败");
				b = false;
			}
		}

		return b;
	}

	/**
	 * @param ids
	 * @return boolean
	 */
	public boolean editInstance(String[] ids) {
		boolean b = true;

		try {
			if (manager.PmeeReadDevices(ids) == 0) {
				logger.warn("调用后台接口成功");
			} else {
				logger.warn("调用后台接口失败");
				b = false;
			}
		} catch (Exception ex) {
			logger.error(ex.getMessage());

			rebindManager();
			if (manager.PmeeReadDevices(ids) == 0) {
				logger.warn("调用后台接口成功");
			} else {
				logger.warn("调用后台接口失败");
				b = false;
			}
		}
		return b;
	}

	/**
	 * 性能后台通知
	 * 
	 * @param ids
	 * @return int
	 */
	public int CallPmee(String[] ids) {
		int flag = 0;
		try {
			flag = manager.PmeeReadDevices(ids);
		} catch (Exception e) {
			logger.error(e.getMessage());

			rebindManager();
			flag = manager.PmeeReadDevices(ids);
		}

		return flag;
	}

	/**
	 * @param interval:1
	 *            重新发现;2:增量发现;
	 * @param hop:hop为跳数(保留)
	 * @param seed:seed为种子节点(保留)
	 * @param gatherid:针对增量发现，存放的是要增量发现的采集点编号的数组
	 * @return -1：正在接收数据，不能重新开始发现 -2: 通知后台发现拓扑失败 -3: 从数据库中读取数据出错
	 */

	public int NewTopo(int interval, int hop, String seed, String[] gatherid) {
		int flag = 0;
		try {
			flag = manager.InformStartNew(interval, hop, seed, gatherid);
		} catch (Exception ex) {
			rebindManager();
			flag = manager.InformStartNew(interval, hop, seed, gatherid);
		}
		return flag;
	}

	/**
	 * 打开拓扑图
	 * 
	 * @param topName
	 *            ：拓扑图的名称
	 * @return -1:后台正在接受数据不能打开； -2:拓扑发现进程没有启动，请先启动拓扑发现进程 -3:从数据库中读取数据出错
	 *         -4:刷新客户端出错
	 */
	public int OpenTopo(String topName) {
		int flag = 0;
		try {
			flag = manager
					.InformOpenName(Encoder.ChineseStringToAscii(topName));
		} catch (Exception e) {
			rebindManager();
			flag = manager
					.InformOpenName(Encoder.ChineseStringToAscii(topName));
		}
		return flag;
	}

	/**
	 * 拓扑图另存为
	 * 
	 * @param topName：拓扑图名称
	 * @return int
	 */

	public int SaveTopoAS(String topName) {
		int flag = 0;
		try {
			flag = manager.InformSaveAsName(Encoder
					.ChineseStringToAscii(topName));
		} catch (Exception ex) {
			rebindManager();
			flag = manager.InformSaveAsName(Encoder
					.ChineseStringToAscii(topName));
		}
		return flag;
	}

	/**
	 * 保存拓扑
	 * 
	 * @return 0:保存拓扑成功 -2:后台有其它用户正在保存拓扑
	 */
	public int SaveTopo() {
		int flag = 0;
		try {
			flag = manager.InformSaveName();
		} catch (Exception e) {
			rebindManager();
			flag = manager.InformSaveName();
		}
		return flag;
	}

	/**
	 * 粘贴网元对象
	 * 
	 * @param parentid
	 * @param objs
	 * @param topouser_id
	 * @return int
	 */
	public int PasteObjs(String topoType, String parentid, String objs,
			String topouser_id) {
		String[] temp = objs.split(",");
		VPN_DragEvent dragEvent = null;
		VpnScheduler VpnScheduler = null;

		try {
			// 构造对象坐标队列
			WebPosition[] webPosition = new WebPosition[temp.length];

			for (int k = 0; k < temp.length; k++) {
				webPosition[k] = new WebPosition();

				webPosition[k].id = temp[k];
			}

			// 构造拖动结构
			dragEvent = new VPN_DragEvent();
			dragEvent.vid = topoType;
			dragEvent.pid = parentid;
			dragEvent.user_id = topouser_id;
			dragEvent.list = webPosition;

			vpnScheduler = new VpnScheduler();
			vpnScheduler.getVPNWebTopoManager().ModifyObjectsLayer(dragEvent);

			// clear
			dragEvent = null;
			webPosition = null;
			temp = null;
		} catch (Exception e) {
			e.printStackTrace();

			vpnScheduler.reloadRemoteDB();
			vpnScheduler.getVPNWebTopoManager().ModifyObjectsLayer(dragEvent);
		}

		return 1;
	}

	/**
	 * 删除网元对象
	 * 
	 * @param objs
	 * @return int
	 */
	public int DelObjs(String objs) {
		String[] temp = objs.split(",");

		int flag = 0;

		try {
			flag = manager.DeleteObjects(temp);
		} catch (Exception e) {
			rebindManager();
			flag = manager.DeleteObjects(temp);
		}

		return flag;
	}

	/**
	 * 增加链路对象
	 * 
	 * @param area_id：域id
	 * @param linkID：链路对象的id
	 * @param fromid：左端设备的id
	 * @param toid：右端设备的id
	 * @param gather_id：采集点编号
	 * @param parent_id：父对象编号
	 * @return -1:添加失败 1：添加成功
	 */
	public int CreateLink(String area_id, String vpn_auto_id, String fromid,
			String toid, String parent_id) {
		int flag = 1;
		StringBuffer sb = new StringBuffer();
		sb.append("<?xml version=\"1.0\" encoding=\"gb2312\"?>");
		sb.append("<WebTopo>");
		sb.append("   <NetView id=\"5\">");
		sb.append("      <Nodes>");
		sb.append("          <Link parentid=\"").append(parent_id).append(
				"\" from=\"").append(fromid).append("\" to=\"").append(toid)
				.append("\" status=\"").append("1").append("\" vpn_auto_id=\"")
				.append(vpn_auto_id).append("\" />");
		sb.append("      </Nodes>");
		sb.append("    </NetView>");
		sb.append("</WebTopo>");
		logger.debug("CreateLink:\n" + sb.toString());
		byte[] data = null;
		try {
			data = manager.AddNewObjectForWebTopo(area_id, sb.toString());
		} catch (Exception e) {
			rebindManager();
			data = manager.AddNewObjectForWebTopo(area_id, sb.toString());
		}
		if (data == null || data.length == 0) {
			flag = -1;
		}
		return flag;
	}

	/**
	 * create device.
	 * 
	 * @param area_id
	 * @param id
	 * @param device_id
	 * @param labelname
	 * @param device_ip
	 * @param gather_id
	 * @param parent_id
	 * @param readcom
	 * @param x
	 * @param y
	 * @param serial
	 * @return
	 * @author hemc
	 * @date 2007-2-11
	 */
	public String CreateDevice(String area_id, String device_ip,
			String company, String device_model, String readcom, String title,
			String gather_id, String vpn_auto_id, String parent_id, String x,
			String y) {
		StringBuffer sb = new StringBuffer();
		String icon = "laptop.png";
		sb.append("<?xml version=\"1.0\" encoding=\"gb2312\"?>");
		sb.append("<WebTopo>");
		sb.append("   <NetView id=\"5\">");
		sb.append("      <Nodes>");
		sb.append("          <Device parentid=\"").append(parent_id).append(
				"\" device_ip=\"").append(device_ip).append("\" title=\"")
				.append(title).append("\" gather_id=\"").append(gather_id)
				.append("\" company=\"").append(company).append(
						"\" device_model=\"").append(device_model).append(
						"\" readcom=\"").append(readcom).append("\" x=\"")
				.append(x).append("\"  icon=\"").append(icon).append("\" y=\"")
				.append(y).append("\" vpn_auto_id=\"").append(vpn_auto_id)
				.append("\" type=\"1").append("\" />");
		sb.append("      </Nodes>");
		sb.append("    </NetView>");
		sb.append("</WebTopo>");
		logger.debug("CreateDevice:\n" + sb.toString());
		byte[] data = null;
		try {
			data = manager.AddNewObjectForWebTopo(area_id, Encoder
					.ChineseStringToAscii(sb.toString()));
		} catch (Exception e) {
			rebindManager();
			data = manager.AddNewObjectForWebTopo(area_id, Encoder
					.ChineseStringToAscii(sb.toString()));
		}
		String retData = "";
		if (data != null && data.length > 0) {
			retData = GZIPHandler.Decompress(data);
		}
		return retData;
	}

	/**
	 * create segment.
	 * 
	 * @param area_id
	 * @param id
	 * @param labelname
	 * @param gather_id
	 * @param parent_id
	 * @param x
	 * @param y
	 * @return
	 * @author hemc
	 * @date 2007-2-12
	 */
	public String CreateSegment(String area_id, String title,
			String network_id, String netmask, String vpn_auto_id,
			String parent_id, String x, String y) {
		StringBuffer sb = new StringBuffer();
		String icon = "network_cloud.png";
		sb.append("<?xml version=\"1.0\" encoding=\"gb2312\"?>");
		sb.append("<WebTopo>");
		sb.append("   <NetView id=\"5\">");
		sb.append("      <Nodes>");
		sb.append("          <Device parentid=\"").append(parent_id).append(
				"\"  title=\"").append(title).append("\"  network_id=\"")
				.append(network_id).append("\"  netmask=\"").append(netmask)
				.append("\"  vpn_auto_id=\"").append(vpn_auto_id).append(
						"\"  icon=\"").append(icon).append("\"  x=\"")
				.append(x).append("\" y=\"").append(y).append("\" type=\"0")
				.append("\"  />");
		sb.append("      </Nodes>");
		sb.append("    </NetView>");
		sb.append("</WebTopo>");
		logger.debug("CreateSegment:\n" + sb.toString());
		byte[] data = null;
		try {
			data = manager.AddNewObjectForWebTopo(area_id, Encoder
					.ChineseStringToAscii(sb.toString()));
		} catch (Exception e) {
			rebindManager();
			data = manager.AddNewObjectForWebTopo(area_id, Encoder
					.ChineseStringToAscii(sb.toString()));
		}
		String retData = "";
		if (data != null && data.length > 0) {
			retData = GZIPHandler.Decompress(data);
		}
		return retData;
	}

	/**
	 * 创建用户组
	 * 
	 * @param area_id
	 * @param title
	 * @param gather_id
	 * @param parent_id
	 * @param x
	 * @param y
	 * @return
	 */
	public String CreateUserGroup(String area_id, String title,
			String parent_id, String x, String y) {
		StringBuffer sb = new StringBuffer();
		String icon = "usergroup1.png";
		sb.append("<?xml version=\"1.0\" encoding=\"gb2312\"?>");
		sb.append("<WebTopo>");
		sb.append("   <NetView id=\"5\">");
		sb.append("      <Nodes>");
		sb.append("          <Device parentid=\"").append(parent_id).append(
				"\" title=\"").append(title).append("\" icon=\"").append(icon)
				.append("\" x=\"").append(x).append("\" y=\"").append(y)
				.append("\" type=\"2").append("\" />");
		sb.append("      </Nodes>");
		sb.append("    </NetView>");
		sb.append("</WebTopo>");
		logger.debug("CreateUserGroup:\n" + sb.toString());
		byte[] data = null;
		try {
			data = manager.AddNewObjectForWebTopo(area_id, Encoder
					.ChineseStringToAscii(sb.toString()));
		} catch (Exception e) {
			rebindManager();
			data = manager.AddNewObjectForWebTopo(area_id, Encoder
					.ChineseStringToAscii(sb.toString()));
		}
		String retData = "";
		if (data != null && data.length > 0) {
			retData = GZIPHandler.Decompress(data);
		}
		return retData;
	}

	/**
	 * 创建接入点
	 * 
	 * @param area_id
	 * @param parent_id
	 * @param title
	 * @param vpn_auto_id
	 * @param vrf_name
	 * @param pe_device_id
	 * @param pe_port
	 * @param export_id
	 * @param import_id
	 * @param rd
	 * @param x
	 * @param y
	 * @return
	 */
	public String CreateAppoint(String area_id, String parent_id, String title,
			String vpn_auto_id, String vrf_name, String pe_device_id,
			String pe_port, String export_rt, String import_rt, String rd,
			String x, String y) {
		StringBuffer sb = new StringBuffer();
		String icon = "accesspoint1.png";
		sb.append("<?xml version=\"1.0\" encoding=\"gb2312\"?>");
		sb.append("<WebTopo>");
		sb.append("   <NetView id=\"5\">");
		sb.append("      <Nodes>");
		sb.append("          <Device parentid=\"").append(parent_id).append(
				"\" pe_device_id=\"").append(pe_device_id)
				.append("\" title=\"").append(title).append("\" pe_port=\"")
				.append(pe_port).append("\" x=\"").append(x).append("\" y=\"")
				.append(y).append("\" vpn_auto_id=\"").append(vpn_auto_id)
				.append("\" icon=\"").append(icon).append("\" vrf_name=\"")
				.append(vrf_name).append("\" export_rt=\"").append(export_rt)
				.append("\" import_rt=\"").append(import_rt).append("\" rd=\"")
				.append(rd).append("\" type=\"4").append("\" />");
		sb.append("      </Nodes>");
		sb.append("    </NetView>");
		sb.append("</WebTopo>");
		logger.debug("CreateAppoint:\n" + sb.toString());
		byte[] data = null;
		try {
			data = manager.AddNewObjectForWebTopo(area_id, Encoder
					.ChineseStringToAscii(sb.toString()));
		} catch (Exception e) {
			rebindManager();
			data = manager.AddNewObjectForWebTopo(area_id, Encoder
					.ChineseStringToAscii(sb.toString()));
		}
		String retData = "";
		if (data != null && data.length > 0) {
			retData = GZIPHandler.Decompress(data);
		}
		return retData;
	}

	/**
	 * 创建VPN用户
	 * 
	 * @param area_id
	 * @param title
	 * @param parent_id
	 * @param x
	 * @param y
	 * @return
	 */
	public String CreateUser(String area_id, String title, String parent_id,
			String x, String y) {
		StringBuffer sb = new StringBuffer();
		String icon = "customer1.png";
		sb.append("<?xml version=\"1.0\" encoding=\"gb2312\"?>");
		sb.append("<WebTopo>");
		sb.append("   <NetView id=\"5\">");
		sb.append("      <Nodes>");
		sb.append("          <Device parentid=\"").append(parent_id).append(
				"\" title=\"").append(title).append("\" icon=\"").append(icon)
				.append("\" x=\"").append(x).append("\" y=\"").append(y)
				.append("\" type=\"3").append("\" />");
		sb.append("      </Nodes>");
		sb.append("    </NetView>");
		sb.append("</WebTopo>");
		logger.debug("CreateUser:\n" + sb.toString());
		byte[] data = null;
		try {
			data = manager.AddNewObjectForWebTopo(area_id, Encoder
					.ChineseStringToAscii(sb.toString()));
		} catch (Exception e) {
			rebindManager();
			data = manager.AddNewObjectForWebTopo(area_id, Encoder
					.ChineseStringToAscii(sb.toString()));
		}
		String retData = "";
		if (data != null && data.length > 0) {
			retData = GZIPHandler.Decompress(data);
		}
		return retData;
	}
}
