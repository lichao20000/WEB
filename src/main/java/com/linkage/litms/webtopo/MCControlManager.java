package com.linkage.litms.webtopo;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import RemoteDB.ControlManager;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.corba.WebCorbaInst;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.litms.common.util.Encoder;
import com.linkage.litms.common.util.GZIPHandler;

public class MCControlManager extends DataManagerControl {
	/** log */
	private static Logger logger = LoggerFactory.getLogger(MCControlManager.class);
	private static ControlManager manager = null;

	private String account = null;

	private String passwd = null;

	public MCControlManager() {

	}

	public int NoteDevices(String[] device_ids) {
		int flag = -1;
		try {
			flag = 1;
			manager.readDevices(device_ids);
		} catch (Exception e) {
			rebindManager();
			try {
				flag = 1;
				manager.readDevices(device_ids);
			} catch (Exception ex) {
				flag = -1;
			}
		}

		return flag;

	}

	public MCControlManager(String account, String passwd) {
		this.account = account;
		this.passwd = passwd;
		if (manager == null) {

			try {
				if (WebCorbaInst.passwordString == null) {
					WebCorbaInst.passwordString = dbInstance.ConnectToDb(
							this.account, this.passwd);
				}

				manager = dbInstance
						.createControlManager(WebCorbaInst.passwordString);
			} catch (Exception e) {
				rebindManager();
				e.printStackTrace();
			}
		}

	}

	private void rebindManager() {
		rebind();
		try {
			String passwordString = dbInstance.ConnectToDb(this.account,
					this.passwd);
			logger.debug(passwordString);
			manager = dbInstance.createControlManager(passwordString);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

	public int InformServiceConfUpdate(int way, String[] array_gather_id) {

		try {
			manager.InformServiceConfUpdate(way, array_gather_id);
		} catch (Exception e) {
			try {
				rebindManager();
				manager.InformServiceConfUpdate(way, array_gather_id);
			} catch (Exception ex) {
				return -1;
			}
		}

		return 1;
	}

	/**
	 * 对象是否存在
	 * @param gather_id
	 * @param device_ip
	 * @param objectType  1：设备
	 * @return
	 */
	private boolean objectIsExsit(String OUI,String device_serialnum,int objectType)
	{
		boolean isExsit= false;
		try
		{
			isExsit = manager.ObjectExist(Encoder.ChineseStringToAscii(OUI),
							Encoder.ChineseStringToAscii(device_serialnum), objectType);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			rebindManager();
			isExsit = manager.ObjectExist(Encoder.ChineseStringToAscii(OUI),
							Encoder.ChineseStringToAscii(device_serialnum), objectType);
		}
		return isExsit;
		
	}
	
	/**
	 * 判断要新增的对象是否可以正常添加
	 * @param type：0:网段；1：正常的设备
	 * @param OUI：设备oui
	 * @param device_serialnum：设备device_serialnum
	 * @return
	 * "device_id":正常添加设备，返回由MC提供的未用的设备编号
	 * "-1":该对象在MC中存在 或在数据库中存在，且device_status为1或0 不允许添加
	 * "0":数据库操作失败
	 */
	public String AddObjectJudge(int type, String OUI, String device_serialnum,
			String serial) {
		String retStr = "";
		//如果是网段
		if (type == 0) {
			try {
				retStr = manager.GetUnusedSegmentSerial(1);
			} catch (Exception e) {
				rebindManager();
				retStr = manager.GetUnusedSegmentSerial(1);
			}
		}
		//如果是设备的话，则需要判断
		//如果设备在系统中已存在
		//当device_status为-1时 update device_status为1后返回device_id
		//当device_status不为-1时 （1或0）返回"-1"
		else {
			if (objectIsExsit(OUI,device_serialnum, 1)) 
				//设备在MC中已存在
				return "-1";

			//设备在MC中不存在
			String mysql = "select device_id,device_status from tab_gw_device where oui=? "
					+ " and device_serialnumber=?";
			PrepareSQL pSQL = new PrepareSQL();
			pSQL.setSQL(mysql);
			pSQL.setString(1, OUI);
			pSQL.setString(2, device_serialnum);
			logger.debug(pSQL.getSQL());
			Map map = DataSetBean.getRecord(pSQL.getSQL());

			//设备在数据库中不存在 从MC端获取设备ID
			if (map == null || map.size() == 0) {
				try {
					retStr = manager.GetUnusedDeviceSerial(1);
				} catch (Exception e) {
					rebindManager();
					retStr = manager.GetUnusedDeviceSerial(1);
				}
			} else {//设备在数据库中存在
				retStr = (String) map.get("device_id");
				String device_status = (String) map.get("device_status");
				if ("-1".equals(device_status)) {
					mysql = "update tab_gw_device set device_status=1 where device_id='"
							+ retStr + "'";
					PrepareSQL psql = new PrepareSQL(mysql);
					psql.getSQL();
					if (DataSetBean.executeUpdate(mysql) > 0)
						// 更新数据库成功 返回device_id
						return retStr;
					// 更新数据库不成功
					return "0";
				}
				// 该对象在数据库中已经存在，且device_status为1或0 不允许添加
				return retStr;
			}
		}

		return retStr;

	}

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
					ex.printStackTrace();
					logger.warn("调用后台接口失败！");
					b = false;
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
					ex.printStackTrace();
					logger.warn("调用后台接口失败！");
					b = false;
				}
			}
		}

		return b;

	}

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
			logger.warn("调用后台接口失败！");
			b = false;
		}

		return b;
	}

	public boolean editInstance(String[] ids) {
		boolean b = true;

		try {

			if (manager.PmeeReadDevices(ids) == 0) {
				logger.warn("调用后台接口成功");
			} else {
				logger.warn("调用后台接口失败");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.warn("调用后台接口失败");
		}
		return b;
	}

	public int CallPmee(String[] ids) {
		int flag = 0;
		try {
			flag = manager.PmeeReadDevices(ids);
		} catch (Exception e) {
			rebindManager();
			flag = manager.PmeeReadDevices(ids);
		}

		return flag;
	}

	public void i_ChangeSDAList(String gather_id, short[] stationid) {

		for (int i = 0; i < stationid.length; i++) {
			logger.debug("stationid=====" + stationid[i]);
		}
		try {
			manager.I_ChangeSDAList(gather_id, stationid);
			logger.warn("后台调用成功！");
		} catch (Exception e) {
			rebindManager();
			logger.warn("后台调用失败！");
		}
	}

	/**
	 *
	 * @param interval:1 重新发现;2:增量发现;
	 * @param hop:hop为跳数(保留)
	 * @param seed:seed为种子节点(保留)
	 * @param gatherid:针对增量发现，存放的是要增量发现的采集点编号的数组
	 * @return
	 * -1：正在接收数据，不能重新开始发现
	 * -2: 通知后台发现拓扑失败
	 * -3: 从数据库中读取数据出错
	 *
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
	 * @param topName ：拓扑图的名称
	 * @return
	 * -1:后台正在接受数据不能打开；
	 * -2:拓扑发现进程没有启动，请先启动拓扑发现进程
	 * -3:从数据库中读取数据出错
	 * -4:刷新客户端出错
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
	 * @param topName：拓扑图名称
	 * @return
	 *
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
	 * @return
	 * 0:保存拓扑成功
	 * -2:后台有其它用户正在保存拓扑
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
	 * 粘贴设备
	 * @param parentid
	 * @param ids
	 * @return
	 */
	public int PasteObjs(String parentid, String objs) {
		String[] temp = objs.split(",");
		String[] ids = new String[temp.length + 1];
		ids[0] = this.account;
		//ids[0]=Encoder.ChineseStringToAscii("AlarmEventObserverWanghfNew132.228.40.146");
		for (int i = 0; i < temp.length; i++) {
			ids[i + 1] = Encoder.ChineseStringToAscii(temp[i]);
		}
		int flag = 0;
		try {
			flag = manager.SetParentChild(parentid, ids);
		} catch (Exception e) {
			rebindManager();
			flag = manager.SetParentChild(parentid, ids);
		}

		return flag;
	}

	/**
	 * 删除设备
	 * @param objID
	 * @param type
	 * @return
	 */
	public int DelObjs(String objs) {
		String[] temp = objs.split(",");
		String[] ids = new String[temp.length];
		for (int i = 0; i < temp.length; i++) {
			ids[i] = temp[i];
		}
		return DelObjs(ids);
	}
    /**
     * 通知MC删除设备
     * @param arrObj
     * @return
     */
    public int DelObjs(String[] arrObj){
        int flag = -1;
        try {
            flag = manager.DeleteObjects(arrObj);
        } catch (Exception e) {
            rebindManager();
            if(manager != null)
                flag = manager.DeleteObjects(arrObj);
        }
        return flag;
    }

	/**
	 * 增加链路对象
     * @param area_id：域id
	 * @param linkID：链路对象的id
	 * @param fromid：左端设备的id
	 * @param toid：右端设备的id
	 * @param gather_id：采集点编号
	 * @param parent_id：父对象编号
	 * @return
	 * -1:添加失败
	 * 1：添加成功
	 */
	public int CreateLink(String area_id,String linkID, String fromid, String toid,
			String gather_id, String parent_id) {
		int flag = 1;
		StringBuffer sb = new StringBuffer();
		sb.append("<?xml version=\"1.0\" encoding=\"gb2312\"?>");
		sb.append("<WebTopo>");
		sb.append("   <NetView id=\"1\">");
		sb.append("      <Links>");
		sb.append("          <Link id=\"").append(linkID).append("\" from=\"")
				.append(fromid).append("\" to=\"").append(toid).append(
						"\" gather_id=\"\" parent_id=\"").append(parent_id)
				.append("\" />");
		sb.append("      </Links>");
		sb.append("    </NetView>");
		sb.append("</WebTopo>");
//		logger.debug("linkID:" + area_id + "|" +linkID);
//        logger.warn(sb.toString());
		byte[] data = null;
		try {
			//data = manager.AddNewObjectForWebTopo(linkID,Encoder.ChineseStringToAscii(sb.toString()));
            data = manager.AddNewObjectForWebTopo(area_id + "|" +linkID,sb.toString());
		} catch (Exception e) {
			rebindManager();
            data = manager.AddNewObjectForWebTopo(area_id + "|" +linkID,sb.toString());
			//data = manager.AddNewObjectForWebTopo(linkID,Encoder.ChineseStringToAscii(sb.toString()));
		}
		if (data == null || data.length == 0) {
			flag = -1;
		}
		return flag;

	}
	
	/**
	 * create device.
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
	 * @author yanhj
	 * @date 2006-11-21
	 */
	public String CreateDevice(String area_id, String id, String device_id, String labelname,
			String device_ip, String gather_id, String parent_id,
			String oui, String x, String y, String devicetype_id,String port,String path,String device_serialnum) {
		StringBuffer sb = new StringBuffer();
		sb.append("<?xml version=\"1.0\" encoding=\"gb2312\"?>");
		sb.append("<WebTopo>");
		sb.append("   <NetView id=\"1\">");
		sb.append("      <Devices>");
		sb.append("          <Device id=\"").append(id).append(
				"\" device_id=\"").append(device_id).append("\" labelname=\"")
				.append(labelname).append("\" gather_id=\"").append(gather_id)
				.append("\" parent_id=\"").append(parent_id).append("\" ip=\"")
				.append(device_ip).append("\" oui=\"").append(oui)
				.append("\" x=\"").append(x).append("\" y=\"").append(y)
                .append("\" port=\"").append(port).append("\" path=\"").append(path)
                .append("\" serialNum=\"").append(device_serialnum)
				.append("\" devicetype_id=\"").append(devicetype_id).append("\" />");
		sb.append("      </Devices>");
		sb.append("    </NetView>");
		sb.append("</WebTopo>");

		byte[] data = null;
		try {
			data = manager.AddNewObjectForWebTopo(area_id + "|" + id, Encoder.ChineseStringToAscii(sb.toString()));
		} catch (Exception e) {
			rebindManager();
			data = manager.AddNewObjectForWebTopo(area_id + "|" + id, Encoder.ChineseStringToAscii(sb.toString()));
		}
		String retData = "";
		if (data != null && data.length > 0) {
			retData = GZIPHandler.Decompress(data);
		}
		return retData;
	}
	
	/**
	 * create segment.
	 * @param area_id
	 * @param id
	 * @param labelname
	 * @param gather_id
	 * @param parent_id
	 * @param x
	 * @param y
	 * @return
	 * @author yanhj
	 * @date 2006-11-21
	 */
	public String CreateSegment(String area_id, String id, String labelname, String gather_id,
			String parent_id, String x, String y) {
		StringBuffer sb = new StringBuffer();
		sb.append("<?xml version=\"1.0\" encoding=\"gb2312\"?>");
		sb.append("<WebTopo>");
		sb.append("   <NetView id=\"1\">");
		sb.append("      <Segments>");
		sb.append("          <Segment id=\"").append(id).append(
				"\"  labelname=\"").append(labelname).append("\" gather_id=\"")
				.append(gather_id).append("\" parent_id=\"").append(parent_id)
				.append("\"  x=\"").append(x).append("\" y=\"").append(y)
				.append("\"  />");
		sb.append("      </Segments>");
		sb.append("    </NetView>");
		sb.append("</WebTopo>");

		byte[] data = null;
		try {
			data = manager.AddNewObjectForWebTopo(area_id + "|" + id, Encoder.ChineseStringToAscii(sb.toString()));
		} catch (Exception e) {
			rebindManager();
			data = manager.AddNewObjectForWebTopo(area_id + "|" + id, Encoder.ChineseStringToAscii(sb.toString()));
		}
		String retData = "";
		if (data != null && data.length > 0) {
			retData = GZIPHandler.Decompress(data);
		}
		return retData;

	}

	/**
	 * 通知后台管理某些设备
	 * @param objID 中间以","隔开
	 * @param type 0:代表将设备有管理转成不管理;1: 代表将设备有不管理转成管理
	 * @return
	 */
	public boolean ManageObjects(String objID, String type) {
		if (objID == null || objID.length() == 0) {
			return false;
		}
		String[] str_objs = objID.split(",");
		if (type.compareTo("0") == 0) {
			try {
				manager.UnmanageObjects(str_objs);
				return true;
			} catch (Exception e) {
				try {
					rebindManager();
					manager.UnmanageObjects(str_objs);
					return true;
				} catch (Exception ex) {
					return false;
				}
			}
		} else {
			try {
				manager.ManageObjects(str_objs);
				return true;
			} catch (Exception e) {
				try {
					rebindManager();
					manager.ManageObjects(str_objs);
					return true;
				} catch (Exception ex) {
					return false;
				}

			}
		}
	}

	public long ModifyDeviceAttr(String device_id, int type, String name) {
		int flag = -1;
		try {
			flag = manager.Modify(Encoder.ChineseStringToAscii(device_id),
					type, Encoder.ChineseStringToAscii(name));
		} catch (Exception e) {
			rebindManager();
			flag = manager.Modify(Encoder.ChineseStringToAscii(device_id),
					type, Encoder.ChineseStringToAscii(name));
		}

		return flag;
	}

	/**
	 * 设置对象的大客户属性，如果是网段的话，则选取所有的下属设备进行修改
	 * @param obj_id
	 * @param customid
	 * @return
	 */
	public boolean setDevCustomid(String obj_id, String customid) {
		boolean flag = false;
		logger.debug(obj_id + "," + customid);
		try {
			flag = manager.SetCustomIdByObjectId(obj_id, Encoder
					.ChineseStringToAscii(customid));
			logger.debug("flag:" + flag);
		} catch (Exception e) {
			e.printStackTrace();
			rebindManager();
			try {
				flag = manager.SetCustomIdByObjectId(obj_id, Encoder
						.ChineseStringToAscii(customid));
			} catch (Exception ey) {
				flag = false;
			}
		}

		return flag;
	}

	/**
	 * 通知后台设备是否cpe设备的更新
	 * @param device_id 设备编码
	 * @param iscpe false:代表不是cpe设备；true 代表是cpe设备
	 * @return
	 * false 代表通知失败；true 代表通知成功
	 */
	public boolean CallCPE(String device_id, boolean iscpe) {
		boolean flag = true;
		try {
			flag = manager.SetStateOfisCPE(device_id, iscpe);
			logger.debug("flag1 is:" + flag);
		} catch (Exception e) {

			rebindManager();
			try {
				flag = manager.SetStateOfisCPE(device_id, iscpe);

			} catch (Exception ex) {
				ex.printStackTrace();
				flag = false;
			}
		}
		return flag;
	}

	/**
	 * 通知后台域信息发生了变化
	 * @param areaid
	 * @param typeid
	 * @return
	 */
	public int CallRegionChange(String areaid, String typeid) {
		int flag = -1;
		try {
			manager.regionChange(areaid, typeid);
			flag = 0;
		} catch (Exception e) {
			rebindManager();
			try {
				manager.regionChange(areaid, typeid);
				flag = 0;
			} catch (Exception e1) {
				flag = -1;
				e1.printStackTrace();
			}
		}

		return flag;
	}
	/**
	 * 通知后台MC重新加载数据
	 * 1:重新加载设备图标 2:重新加载拓扑图 3:删除所有的告警 4:重新加载权限
	 * @param device_id
	 * @param flag
	 * @return
	 */
	public int InformControl(int type) {
		int flag = -1;
		try {
			manager.InformControl(type);
			flag = 0;
		} catch (Exception e) {
			rebindManager();
			try {
			    manager.InformControl(type);
				flag = 0;
			} catch (Exception e1) {
				flag = -1;
				e1.printStackTrace();
			}
		}

		return flag;
	}
	/**
	 * 通知后台设备状态发生了变化
	 * @param device_id
	 * @param flag
	 * @return
	 */
	public int reloadDeviceAraeInfo(String[] device_id) {
		int flag = -1;
		try {
			manager.reloadDeviceAraeInfo(device_id);
			flag = 0;
		} catch (Exception e) {
			rebindManager();
			try {
				manager.reloadDeviceAraeInfo(device_id);
				flag = 0;
			} catch (Exception e1) {
				flag = -1;
				e1.printStackTrace();
			}
		}

		return flag;
	}
}