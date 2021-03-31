package com.linkage.litms.webtopo;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import RemoteDB.AlarmEvent;
import RemoteDB.AlarmNum;
import RemoteDB.GatherIDEvent;
import RemoteDB.SQLException;
import RemoteDB.VPN_DragEvent;
import RemoteDB.WebPosition;

import com.linkage.litms.common.corba.WebCorba;

public class VpnScheduler {
	private static Logger m_logger = LoggerFactory.getLogger(VpnScheduler.class);
	
	private WebCorba corba = null;

	// vpnTopoMasterControl corba对象
	private Object object = null;

	// vpnTopo corba对象
	// private Object vpn_object = null;

	private String passwordString = null;

	private static RemoteDB.DataManager data_manager = null;

	private static RemoteDB.QOSManager qos_manager = null;

	private static RemoteDB.HostObjectManager host_objectmanager = null;

	private static RemoteDB.HostSystemManager host_systemmanager = null;

	private static RemoteDB.TopoManager topo_manager = null;

	private static RemoteDB.VPN_WebTopoManager vpn_webtopomanager = null;

	private static RemoteDB.ControlManager control_manager = null;

	private static RemoteDB.UserViewManager user_viewmanager = null;

	private static RemoteDB.WebTopoManager web_topomanager = null;

	private static RemoteDB.ViewManager view_manager = null;

	private static RemoteDB.QueueingDeviceConf queueing_deviceconf = null;

	private static RemoteDB.dataSource datasource = null;

	public VpnScheduler() {
		// TODO Auto-generated constructor stub
		corba = new WebCorba("VpnTopo", null, null);
		object = corba.getIDLCorba("VpnTopo");
	}

	/**
	 * 初始化密码
	 * 
	 * @param username
	 * @param password
	 */
	void initPasswd(String username, String password) {
		if (passwordString == null) {
			try {
				passwordString = ((RemoteDB.DB) object).ConnectToDb(username,
						password);
			} catch (Exception e) {
				// e.printStackTrace();

				if (corba.refreshCorba("java")) {
					object = corba.getIDLCorba("VpnTopo");

					try {
						passwordString = ((RemoteDB.DB) object).ConnectToDb(
								username, password);
					} catch (SQLException e1) {
						m_logger.error(e1.getMessage());
					}
				}
			}
		}
	}

	/**
	 * 获取与object_id同一层的所有topo数据
	 * 
	 * @param area_id
	 * @param user_id
	 * @param object_id
	 * @return byte[]
	 */
	public byte[] getSameStreamData(String area_id, String user_id,
			String object_id) {
		byte[] data = null;

		try {
			data = this.getVPNWebTopoManager().getSameStreamData(area_id,
					user_id, object_id);
		} catch (Exception e) {
			m_logger.error(e.getMessage());

			this.reloadRemoteDB();
			data = this.getVPNWebTopoManager().getSameStreamData(area_id,
					user_id, object_id);
		}

		return data;
	}

	/**
	 * 获取父ID为pid的所有topo数据
	 * 
	 * @param area_id
	 * @param user_id
	 * @param parent_id
	 * @return byte[]
	 */
	public byte[] getChildStreamData(String area_id, String user_id,
			String parent_id) {
		byte[] data = null;

		try {
			data = this.getVPNWebTopoManager().getChildStreamData(area_id,
					user_id, parent_id);
		} catch (Exception e) {
			m_logger.error(e.getMessage());

			this.reloadRemoteDB();
			data = this.getVPNWebTopoManager().getChildStreamData(area_id,
					user_id, parent_id);
		}

		return data;
	}

	/**
	 * 获取父ID为pid的所有topo数据
	 * 
	 * @param area_id
	 * @return byte[]
	 */
	public byte[] getVPNTreeData(String area_id) {
		byte[] data = null;

		try {
			data = this.getVPNWebTopoManager().getVPNTreeData(area_id);
		} catch (Exception e) {
			m_logger.error(e.getMessage());

			this.reloadRemoteDB();
			try {
				data = this.getVPNWebTopoManager().getVPNTreeData(area_id);
			} catch (Exception e2) {
				m_logger.error(e2.getMessage());
			}
		}

		return data;
	}

	/**
	 * 获取QosManager corba对象
	 * 
	 * @return QosManager
	 */
	public RemoteDB.QOSManager getQosManager() {
		try {
			if (object != null && qos_manager == null) {
				qos_manager = ((RemoteDB.DB) object).createQOSManager("");
			} else if (object == null) {
				if (corba.refreshCorba("java")) {
					try {
						object = corba.getIDLCorba("VpnTopo");
						qos_manager = ((RemoteDB.DB) object)
								.createQOSManager("");

						return qos_manager;
					} catch (SQLException e1) {
						m_logger.error(e1.getMessage());
					}
				}
			}

			return qos_manager;
		} catch (SQLException e) {
			m_logger.error(e.getMessage());

			if (corba.refreshCorba("java")) {
				try {
					object = corba.getIDLCorba("VpnTopo");
					qos_manager = ((RemoteDB.DB) object).createQOSManager("");

					return qos_manager;
				} catch (SQLException e1) {
					m_logger.error(e1.getMessage());
				}
			}
		}

		return null;
	}

	/**
	 * 获取QosManager corba对象
	 * 
	 * @return QosManager
	 */
	public RemoteDB.QOSManager getQosManager2() {
		try {
			if (object != null && qos_manager == null) {
				qos_manager = ((RemoteDB.DB) object).createQOSManager("");
			} else if (object == null) {
				if (corba.refreshCorba("java")) {
					try {
						object = corba.getIDLCorba("VpnTopo");
						qos_manager = ((RemoteDB.DB) object)
								.createQOSManager("");

						return qos_manager;
					} catch (SQLException e1) {
						m_logger.error(e1.getMessage());
					}
				}
			}

			return qos_manager;
		} catch (SQLException e) {
			if (corba.refreshCorba("java")) {
				try {
					object = corba.getIDLCorba("VpnTopo");
					qos_manager = ((RemoteDB.DB) object).createQOSManager("");

					return qos_manager;
				} catch (SQLException e1) {
					m_logger.error(e1.getMessage());
				}
			}
		}

		return null;
	}

	/**
	 * 出现方法调用错误，一般只有一种可能：就是前台向后抬申请的corba对象不存在了，
	 * 但是此对象仍然被用来构造方法，故出现调用实现现象，此时只需要重新调用此corba对象 的构造方法，然后重新调用方法，应该就能解决问题。
	 */
	public void reloadRemoteDB() {
		object = null;
	}

	/**
	 * 获取QosQueue corba对象
	 * 
	 * @return RemoteDB.QueueingDeviceConf
	 */
	public RemoteDB.QueueingDeviceConf getQosQueueManager() {
		try {
			if (object != null && queueing_deviceconf == null) {
				queueing_deviceconf = ((RemoteDB.DB) object)
						.createQosQueueManager("");
			} else if (object == null) {
				if (corba.refreshCorba("java")) {
					try {
						object = corba.getIDLCorba("VpnTopo");
						queueing_deviceconf = ((RemoteDB.DB) object)
								.createQosQueueManager("");
					} catch (SQLException e1) {
						m_logger.error(e1.getMessage());
					}
				}
			}

			return queueing_deviceconf;
		} catch (SQLException e) {
			if (corba.refreshCorba("java")) {
				try {
					object = corba.getIDLCorba("VpnTopo");
					return ((RemoteDB.DB) object).createQosQueueManager("");
				} catch (SQLException e1) {
					m_logger.error(e1.getMessage());
				}
			}
		}

		return null;
	}

	/**
	 * 获取QosQueue corba对象
	 * 
	 * @return RemoteDB.QueueingDeviceConf
	 */
	public RemoteDB.QueueingDeviceConf getQosQueueManager2() {
		try {
			if (object != null && queueing_deviceconf == null) {
				queueing_deviceconf = ((RemoteDB.DB) object)
						.createQosQueueManager("");
			} else if (object == null) {
				if (corba.refreshCorba("java")) {
					try {
						object = corba.getIDLCorba("VpnTopo");
						queueing_deviceconf = ((RemoteDB.DB) object)
								.createQosQueueManager("");
					} catch (SQLException e1) {
						m_logger.error(e1.getMessage());
					}
				}
			}

			return queueing_deviceconf;
		} catch (SQLException e) {
			if (corba.refreshCorba("java")) {
				try {
					object = corba.getIDLCorba("VpnTopo");
					queueing_deviceconf = ((RemoteDB.DB) object)
							.createQosQueueManager("");

					return queueing_deviceconf;
				} catch (SQLException e1) {
					m_logger.error(e1.getMessage());
				}
			}
		}

		return null;
	}

	/**
	 * 获取DataManager corba对象
	 * 
	 * @return DataManager
	 */
	public RemoteDB.DataManager getDataManager(String username, String password) {
		try {
			if (object != null && data_manager == null) {
				initPasswd(username, password);
				data_manager = ((RemoteDB.DB) object)
						.createDataManager(passwordString);
			} else if (object == null) {
				if (corba.refreshCorba("java")) {
					try {
						object = corba.getIDLCorba("VpnTopo");
						initPasswd(username, password);
						data_manager = ((RemoteDB.DB) object)
								.createDataManager(passwordString);
					} catch (SQLException e1) {
						m_logger.error(e1.getMessage());
					}
				}
			}

			return data_manager;
		} catch (SQLException e) {
			if (corba.refreshCorba("java")) {
				try {
					object = corba.getIDLCorba("VpnTopo");
					initPasswd(username, password);
					data_manager = ((RemoteDB.DB) object)
							.createDataManager(passwordString);

					return data_manager;
				} catch (SQLException e1) {
					m_logger.error(e1.getMessage());
				}
			}
		}

		return null;
	}

	/**
	 * 获取DataManager corba对象
	 * 
	 * @param username
	 * @param password
	 * @return RemoteDB.DataManager
	 */
	public RemoteDB.DataManager getDataManager2(String username, String password) {
		try {
			if (object != null && data_manager == null) {
				data_manager = ((RemoteDB.DB) object)
						.createDataManager(passwordString);
			} else if (object == null) {
				if (corba.refreshCorba("java")) {
					try {
						object = corba.getIDLCorba("VpnTopo");
						initPasswd(username, password);
						data_manager = ((RemoteDB.DB) object)
								.createDataManager(passwordString);
					} catch (SQLException e1) {
						m_logger.error(e1.getMessage());
					}
				}
			}

			return data_manager;
		} catch (SQLException e) {
			m_logger.error(e.getMessage());

			if (corba.refreshCorba("java")) {
				try {
					object = corba.getIDLCorba("VpnTopo");
					initPasswd(username, password);
					data_manager = ((RemoteDB.DB) object)
							.createDataManager(passwordString);

					return data_manager;
				} catch (SQLException e1) {
					m_logger.error(e1.getMessage());
				}
			}
		}

		return null;
	}

	/**
	 * 获取dataSource对象
	 * 
	 * @return RemoteDB.dataSource
	 */
	public RemoteDB.dataSource getDataSource(String username, String password) {

		try {
			if (object != null && datasource == null) {
				initPasswd(username, password);
				datasource = ((RemoteDB.DB) object)
						.createDataSourceServent(passwordString);
			} else if (object == null) {
				if (corba.refreshCorba("java")) {
					try {
						object = corba.getIDLCorba("VpnTopo");
						initPasswd(username, password);
						datasource = ((RemoteDB.DB) object)
								.createDataSourceServent(passwordString);
					} catch (SQLException e1) {
						m_logger.error(e1.getMessage());
					}
				}
			}

			return datasource;
		} catch (SQLException e) {
			if (corba.refreshCorba("java")) {
				try {
					object = corba.getIDLCorba("VpnTopo");
					initPasswd(username, password);
					datasource = ((RemoteDB.DB) object)
							.createDataSourceServent(passwordString);

					return datasource;
				} catch (SQLException e1) {
					m_logger.error(e1.getMessage());
				}
			}
		}

		return null;
	}

	/**
	 * 获取dataSource对象
	 * 
	 * @return RemoteDB.dataSource
	 */
	public RemoteDB.dataSource getDataSource2(String username, String password) {
		try {
			if (object != null && datasource == null) {
				datasource = ((RemoteDB.DB) object)
						.createDataSourceServent(passwordString);
			} else if (object == null) {
				if (corba.refreshCorba("java")) {
					try {
						object = corba.getIDLCorba("VpnTopo");
						initPasswd(username, password);
						datasource = ((RemoteDB.DB) object)
								.createDataSourceServent(passwordString);
					} catch (SQLException e1) {
						m_logger.error(e1.getMessage());
					}
				}
			}

			return datasource;
		} catch (SQLException e) {
			if (corba.refreshCorba("java")) {
				try {
					object = corba.getIDLCorba("VpnTopo");
					initPasswd(username, password);
					datasource = ((RemoteDB.DB) object)
							.createDataSourceServent(passwordString);

					return datasource;
				} catch (SQLException e1) {
					m_logger.error(e1.getMessage());
				}
			}
		}

		return null;
	}

	public void setDataManagerNull() {
		data_manager = null;
	}

	/**
	 * 获取HostObjectManager corba对象
	 * 
	 * @return HostObjectManager
	 */
	public RemoteDB.HostObjectManager getHostObjectManager2() {
		try {
			if (object != null && host_objectmanager == null) {
				host_objectmanager = ((RemoteDB.DB) object)
						.createHostObjectManager("");
			} else if (object == null) {
				if (corba.refreshCorba("java")) {
					try {
						object = corba.getIDLCorba("VpnTopo");
						host_objectmanager = ((RemoteDB.DB) object)
								.createHostObjectManager("");
					} catch (SQLException e1) {
						m_logger.error(e1.getMessage());
					}
				}
			}

			return host_objectmanager;
		} catch (SQLException e) {
			if (corba.refreshCorba("java")) {
				try {
					object = corba.getIDLCorba("VpnTopo");
					host_objectmanager = ((RemoteDB.DB) object)
							.createHostObjectManager("");

					return host_objectmanager;
				} catch (SQLException e1) {
					m_logger.error(e1.getMessage());
				}
			}
		}

		return null;
	}

	/**
	 * 获取HostObjectManager corba对象
	 * 
	 * @return HostObjectManager
	 */
	public RemoteDB.HostObjectManager getHostObjectManager() {
		try {
			if (object != null && host_objectmanager == null) {
				host_objectmanager = ((RemoteDB.DB) object)
						.createHostObjectManager("");
			} else if (object == null) {
				if (corba.refreshCorba("java")) {
					try {
						object = corba.getIDLCorba("VpnTopo");
						host_objectmanager = ((RemoteDB.DB) object)
								.createHostObjectManager("");
					} catch (SQLException e1) {
						m_logger.error(e1.getMessage());
					}
				}
			}

			return host_objectmanager;
		} catch (SQLException e) {
			if (corba.refreshCorba("java")) {
				try {
					object = corba.getIDLCorba("VpnTopo");
					host_objectmanager = ((RemoteDB.DB) object)
							.createHostObjectManager("");

					return host_objectmanager;
				} catch (SQLException e1) {
					m_logger.error(e1.getMessage());
				}
			}
		}

		return null;
	}

	/**
	 * 获取HostSystemManager corba对象
	 * 
	 * @return HostSystemManager
	 */
	public RemoteDB.HostSystemManager getHostSystemManager() {
		try {
			if (object != null && host_systemmanager == null) {
				host_systemmanager = ((RemoteDB.DB) object)
						.createHostSystemManager("");
			} else if (object == null) {
				if (corba.refreshCorba("java")) {
					try {
						object = corba.getIDLCorba("VpnTopo");
						host_systemmanager = ((RemoteDB.DB) object)
								.createHostSystemManager("");
					} catch (SQLException e1) {
						m_logger.error(e1.getMessage());
					}
				}
			}

			return host_systemmanager;
		} catch (SQLException e) {
			if (corba.refreshCorba("java")) {
				try {
					object = corba.getIDLCorba("VpnTopo");
					host_systemmanager = ((RemoteDB.DB) object)
							.createHostSystemManager("");

					return host_systemmanager;
				} catch (SQLException e1) {
					m_logger.error(e1.getMessage());
				}
			}
		}

		return null;
	}

	/**
	 * 获取HostSystemManager corba对象
	 * 
	 * @return HostSystemManager
	 */
	public RemoteDB.HostSystemManager getHostSystemManager2() {
		try {
			if (object != null && host_systemmanager == null) {
				host_systemmanager = ((RemoteDB.DB) object)
						.createHostSystemManager("");
			} else if (object == null) {
				if (corba.refreshCorba("java")) {
					try {
						object = corba.getIDLCorba("VpnTopo");
						host_systemmanager = ((RemoteDB.DB) object)
								.createHostSystemManager("");
					} catch (SQLException e1) {
						m_logger.error(e1.getMessage());
					}
				}
			}

			return host_systemmanager;
		} catch (SQLException e) {
			if (corba.refreshCorba("java")) {
				try {
					object = corba.getIDLCorba("VpnTopo");
					host_systemmanager = ((RemoteDB.DB) object)
							.createHostSystemManager("");

					return host_systemmanager;
				} catch (SQLException e1) {
					m_logger.error(e1.getMessage());
				}
			}
		}

		return null;
	}

	/**
	 * 获取TopoManager corba对象
	 * 
	 * @return TopoManager
	 */
	public RemoteDB.TopoManager getTopoManager() {
		try {
			if (object != null && topo_manager == null) {
				topo_manager = ((RemoteDB.DB) object).createTopoManager("");
			} else if (object == null) {
				if (corba.refreshCorba("java")) {
					try {
						object = corba.getIDLCorba("VpnTopo");
						topo_manager = ((RemoteDB.DB) object)
								.createTopoManager("");
					} catch (SQLException e1) {
						m_logger.error(e1.getMessage());
					}
				}
			}

			return topo_manager;
		} catch (SQLException e) {
			if (corba.refreshCorba("java")) {
				try {
					object = corba.getIDLCorba("VpnTopo");
					topo_manager = ((RemoteDB.DB) object).createTopoManager("");

					return topo_manager;
				} catch (SQLException e1) {
					m_logger.error(e1.getMessage());
				}
			}
		}

		return null;
	}

	/**
	 * 获取TopoManager corba对象
	 * 
	 * @return TopoManager
	 */
	public RemoteDB.TopoManager getTopoManager2() {
		try {
			if (object != null && topo_manager == null) {
				topo_manager = ((RemoteDB.DB) object).createTopoManager("");
			} else if (object == null) {
				if (corba.refreshCorba("java")) {
					try {
						object = corba.getIDLCorba("VpnTopo");
						topo_manager = ((RemoteDB.DB) object)
								.createTopoManager("");
					} catch (SQLException e1) {
						m_logger.error(e1.getMessage());
					}
				}
			}

			return topo_manager;
		} catch (SQLException e) {
			if (corba.refreshCorba("java")) {
				try {
					object = corba.getIDLCorba("VpnTopo");
					topo_manager = ((RemoteDB.DB) object).createTopoManager("");

					return topo_manager;
				} catch (SQLException e1) {
					m_logger.error(e1.getMessage());
				}
			}
		}

		return null;
	}

	/**
	 * 获取ViewManager corba对象
	 * 
	 * @return ViewManager
	 */
	public RemoteDB.ViewManager getViewManager() {
		try {
			if (object != null && view_manager == null) {
				view_manager = ((RemoteDB.DB) object).createViewManager("");
			} else if (object == null) {
				if (corba.refreshCorba("java")) {
					try {
						object = corba.getIDLCorba("VpnTopo");
						view_manager = ((RemoteDB.DB) object)
								.createViewManager("");
					} catch (SQLException e1) {
						m_logger.error(e1.getMessage());
					}
				}
			}

			return view_manager;
		} catch (SQLException e) {
			if (corba.refreshCorba("java")) {
				try {
					object = corba.getIDLCorba("VpnTopo");
					view_manager = ((RemoteDB.DB) object).createViewManager("");

					return view_manager;
				} catch (SQLException e1) {
					m_logger.error(e1.getMessage());
				}
			}
		}

		return null;
	}

	/**
	 * 获取ViewManager corba对象
	 * 
	 * @return ViewManager
	 */
	public RemoteDB.ViewManager getViewManager2() {
		try {
			if (object != null && view_manager == null) {
				view_manager = ((RemoteDB.DB) object).createViewManager("");
			} else if (object == null) {
				if (corba.refreshCorba("java")) {
					try {
						object = corba.getIDLCorba("VpnTopo");
						view_manager = ((RemoteDB.DB) object)
								.createViewManager("");
					} catch (SQLException e1) {
						m_logger.error(e1.getMessage());
					}
				}
			}

			return view_manager;
		} catch (SQLException e) {
			if (corba.refreshCorba("java")) {
				try {
					object = corba.getIDLCorba("VpnTopo");
					view_manager = ((RemoteDB.DB) object).createViewManager("");

					return view_manager;
				} catch (SQLException e1) {
					m_logger.error(e1.getMessage());
				}
			}
		}

		return null;
	}

	/**
	 * 获取UserViewManager corba对象
	 * 
	 * @return UserViewManager
	 */
	public RemoteDB.UserViewManager getUserViewManager() {
		try {
			if (object != null && user_viewmanager == null) {
				user_viewmanager = ((RemoteDB.DB) object)
						.createUserViewManager("");
			} else if (object == null) {
				if (corba.refreshCorba("java")) {
					try {
						object = corba.getIDLCorba("VpnTopo");
						user_viewmanager = ((RemoteDB.DB) object)
								.createUserViewManager("");
					} catch (SQLException e1) {
						m_logger.error(e1.getMessage());
					}
				}
			}

			return user_viewmanager;
		} catch (SQLException e) {
			if (corba.refreshCorba("java")) {
				try {
					object = corba.getIDLCorba("VpnTopo");
					user_viewmanager = ((RemoteDB.DB) object)
							.createUserViewManager("");

					return user_viewmanager;
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					m_logger.error(e1.getMessage());
				}
			}
		}

		return null;
	}

	/**
	 * 获取UserViewManager corba对象
	 * 
	 * @return UserViewManager
	 */
	public RemoteDB.UserViewManager getUserViewManager2() {
		try {
			if (object != null && user_viewmanager == null) {
				user_viewmanager = ((RemoteDB.DB) object)
						.createUserViewManager("");
			} else if (object == null) {
				if (corba.refreshCorba("java")) {
					try {
						object = corba.getIDLCorba("VpnTopo");
						user_viewmanager = ((RemoteDB.DB) object)
								.createUserViewManager("");
					} catch (SQLException e1) {
						m_logger.error(e1.getMessage());
					}
				}
			}

			return user_viewmanager;
		} catch (SQLException e) {
			if (corba.refreshCorba("java")) {
				try {
					object = corba.getIDLCorba("VpnTopo");
					user_viewmanager = ((RemoteDB.DB) object)
							.createUserViewManager("");

					return user_viewmanager;
				} catch (SQLException e1) {
					m_logger.error(e1.getMessage());
				}
			}
		}

		return null;
	}

	/**
	 * 获取WebTopoManager corba对象
	 * 
	 * @return WebTopoManager
	 */
	public RemoteDB.WebTopoManager getWebTopoManager() {
		try {
			if (object != null && web_topomanager == null) {
				web_topomanager = ((RemoteDB.DB) object)
						.createWebTopoManager("");
			} else if (object == null) {
				if (corba.refreshCorba("java")) {
					try {
						object = corba.getIDLCorba("VpnTopo");
						web_topomanager = ((RemoteDB.DB) object)
								.createWebTopoManager("");
					} catch (SQLException e1) {
						m_logger.error(e1.getMessage());
					}
				}
			}

			return web_topomanager;
		} catch (SQLException e) {
			if (corba.refreshCorba("java")) {
				try {
					object = corba.getIDLCorba("VpnTopo");
					web_topomanager = ((RemoteDB.DB) object)
							.createWebTopoManager("");

					return web_topomanager;
				} catch (SQLException e1) {
					m_logger.error(e1.getMessage());
				}
			}
		}

		return null;
	}

	/**
	 * 获取WebTopoManager corba对象
	 * 
	 * @return WebTopoManager
	 */
	public RemoteDB.WebTopoManager getWebTopoManager2() {
		try {
			if (object != null && web_topomanager == null) {
				web_topomanager = ((RemoteDB.DB) object)
						.createWebTopoManager("");
			} else if (object == null) {
				if (corba.refreshCorba("java")) {
					try {
						object = corba.getIDLCorba("VpnTopo");
						web_topomanager = ((RemoteDB.DB) object)
								.createWebTopoManager("");
					} catch (SQLException e1) {
						m_logger.error(e1.getMessage());
					}
				}
			}

			return web_topomanager;
		} catch (SQLException e) {
			if (corba.refreshCorba("java")) {
				try {
					object = corba.getIDLCorba("VpnTopo");
					web_topomanager = ((RemoteDB.DB) object)
							.createWebTopoManager("");

					return web_topomanager;
				} catch (SQLException e1) {
					m_logger.error(e1.getMessage());
				}
			}
		}

		return null;
	}

	/**
	 * 获取VPN_WebTopoManager corba对象
	 * 
	 * @return VPN_WebTopoManager
	 */
	public RemoteDB.VPN_WebTopoManager getVPNWebTopoManager() {
		try {
			if (object != null && vpn_webtopomanager == null) {
				vpn_webtopomanager = ((RemoteDB.DB) object)
						.createVPNWebTopoManager("");
			} else if (object == null) {
				if (corba.refreshCorba("java")) {
					try {
						object = corba.getIDLCorba("VpnTopo");
						vpn_webtopomanager = ((RemoteDB.DB) object)
								.createVPNWebTopoManager("");
					} catch (Exception e1) {
						m_logger.error(e1.getMessage());
					}
				}
			}

			return vpn_webtopomanager;
		} catch (Exception e) {
			m_logger.error(e.getMessage());

			if (corba.refreshCorba("java")) {
				try {
					object = corba.getIDLCorba("VpnTopo");
					vpn_webtopomanager = ((RemoteDB.DB) object)
							.createVPNWebTopoManager("");

					return vpn_webtopomanager;
				} catch (Exception e1) {
					m_logger.error(e1.getMessage());
				}
			}
		}

		return null;
	}

	/**
	 * 获取VPN_WebTopoManager corba对象
	 * 
	 * @return VPN_WebTopoManager
	 */
	public RemoteDB.VPN_WebTopoManager getVPNWebTopoManager2() {
		try {
			if (object != null && vpn_webtopomanager == null) {
				vpn_webtopomanager = ((RemoteDB.DB) object)
						.createVPNWebTopoManager("");
			} else if (object == null) {
				if (corba.refreshCorba("java")) {
					try {
						object = corba.getIDLCorba("VpnTopo");
						vpn_webtopomanager = ((RemoteDB.DB) object)
								.createVPNWebTopoManager("");
					} catch (Exception e1) {
						m_logger.error(e1.getMessage());
					}
				}
			}

			return vpn_webtopomanager;
		} catch (Exception e) {
			m_logger.error(e.getMessage());

			if (corba.refreshCorba("java")) {
				try {
					object = corba.getIDLCorba("VpnTopo");
					vpn_webtopomanager = ((RemoteDB.DB) object)
							.createVPNWebTopoManager("");

					return vpn_webtopomanager;
				} catch (Exception e1) {
					m_logger.error(e1.getMessage());
				}
			}
		}

		return null;
	}

	/**
	 * 第一次获取告警（最新的200条）
	 * 
	 * @param area_id
	 *            登录域字符串
	 * @return 返回AlarmEvent结构数组
	 */
	public AlarmEvent[] getAllAlarm(String area_id, GatherIDEvent[] _gatherList) {
		return this.getVPNWebTopoManager().getAllAlarm(area_id, _gatherList);
	}

	/**
	 * 取得序列号大于指定序列号的告警信息
	 * 
	 * @param area_id
	 * @param user_id
	 *            用户内部id
	 * @param _gatherList
	 * @return 返回AlarmEvent结构数组
	 */
	public AlarmEvent[] getNewAlarm(String area_id, int ID,GatherIDEvent[] _gatherList) {
		return this.getVPNWebTopoManager().getNewAlarm(area_id,ID,_gatherList);
	}

	/**
	 * 获取具体层所有对象的告警数量
	 * 
	 * @param area_id
	 * @param user_id
	 *            VPN用户id
	 * @param arr_id
	 *            设备id数组
	 * @return
	 */
	public AlarmNum[] getAllAlarmNum(String area_id, String user_id,
			String[] arr_id) {
		return this.getVPNWebTopoManager().getAllAlarmNum(area_id, user_id,
				arr_id);
	}

	/**
	 * 获取ControlManager corba对象
	 * 
	 * @return ControlManager
	 */
	public RemoteDB.ControlManager getControlManager(String username,
			String password) {
		try {
			initPasswd(username, password);
			if (object != null && control_manager == null) {
				control_manager = ((RemoteDB.DB) object)
						.createControlManager(passwordString);
			} else if (object == null) {
				if (corba.refreshCorba("java")) {
					try {
						object = corba.getIDLCorba("VpnTopo");
						initPasswd(username, password);
						control_manager = ((RemoteDB.DB) object)
								.createControlManager(passwordString);
					} catch (SQLException e1) {
						m_logger.error(e1.getMessage());
					}
				}
			}

			return control_manager;
		} catch (SQLException e) {
			if (corba.refreshCorba("java")) {
				try {
					object = corba.getIDLCorba("VpnTopo");
					initPasswd(username, password);
					control_manager = ((RemoteDB.DB) object)
							.createControlManager(passwordString);

					return control_manager;
				} catch (SQLException e1) {
					m_logger.error(e1.getMessage());
				}
			}
		}

		return null;
	}

	/**
	 * 获取ControlManager corba对象
	 * 
	 * @return ControlManager
	 */
	public RemoteDB.ControlManager getControlManager2(String username,
			String password) {
		try {
			initPasswd(username, password);
			if (object != null && control_manager == null) {
				control_manager = ((RemoteDB.DB) object)
						.createControlManager(passwordString);
			} else if (object == null) {
				if (corba.refreshCorba("java")) {
					try {
						object = corba.getIDLCorba("VpnTopo");
						initPasswd(username, password);
						control_manager = ((RemoteDB.DB) object)
								.createControlManager(passwordString);
					} catch (SQLException e1) {
						m_logger.error(e1.getMessage());
					}
				}
			}

			return control_manager;
		} catch (SQLException e) {
			if (corba.refreshCorba("java")) {
				try {
					object = corba.getIDLCorba("VpnTopo");
					initPasswd(username, password);
					control_manager = ((RemoteDB.DB) object)
							.createControlManager(passwordString);

					return control_manager;
				} catch (SQLException e1) {
					m_logger.error(e1.getMessage());
				}
			}
		}

		return null;
	}

	/**
	 * 对象当前层拖动
	 * 
	 * @param s
	 *            JS串形式的VPN_DragEvent结构
	 */
	public void ModifyObjectsPosition(String s) {
		VPN_DragEvent drag = this.getDragEventByJs(s);

		this.getVPNWebTopoManager().ModifyObjectsPosition(drag);

		// clear
		drag = null;
	}

	/**
	 * 将JS串解析成Corba接口中的 VPN_DragEvent结构
	 * 
	 * @param s
	 *            JS串形式的VPN_DragEvent结构
	 * @return 返回VPN_DragEvent结构
	 */
	private VPN_DragEvent getDragEventByJs(String s) {
		String[] arr = null, arr_tmp = null;
		VPN_DragEvent drag = null;
		if (s == null || s.length() == 0)
			return drag;

		drag = new VPN_DragEvent();
		arr = s.split(";");
		drag.pid = arr[1];
		drag.vid = arr[0];
		drag.user_id = arr[2];
		String tmp = arr[3];
		arr = tmp.split("@");

		// logger.debug(" VPNScheduler vid:"+drag.vid);
		// logger.debug(" VPNScheduler pid:"+drag.pid);
		// logger.debug(" VPNScheduler user_id:"+drag.user_id);

		WebPosition[] pos = new WebPosition[arr.length];
		for (int i = 0; i < arr.length; i++) {
			// logger.debug(arr[i]);
			arr_tmp = arr[i].split(",");
			pos[i] = new WebPosition();
			pos[i].id = arr_tmp[0];
			pos[i].x = Integer.parseInt(arr_tmp[1]);
			pos[i].y = Integer.parseInt(arr_tmp[2]);
		}

		drag.list = pos;

		return drag;
	}

	/**
	 * I_VerifyPasswd
	 * 
	 * @param gather_id
	 * @param ip
	 * @param logonmode
	 * @param username
	 * @param passwd
	 * @param enablecmd
	 * @param enablepwd
	 * @param hostprompt
	 * @return boolean
	 */
	public boolean I_VerifyPasswd(String gather_id, String ip, short logonmode,
			String username, String passwd, String enablecmd, String enablepwd,
			String hostprompt) {
		boolean flag = false;
		try {
			flag = this.getVPNWebTopoManager2().I_VerifyPasswd(gather_id, ip,
					logonmode, username, passwd, enablecmd, enablepwd,
					hostprompt);
		} catch (Exception e) {
			vpn_webtopomanager = null;
			e.printStackTrace();
			return false;
		}

		return flag;
	}

	/**
	 * 获取链路信息
	 * 
	 * @param link_id
	 *            链路id
	 * @param vpn_auto_id
	 *            vpn用户id
	 * @return 返回链路信息对象LinkInfo
	 */
	public RemoteDB.LinkInfo getLinkInfo(String vpn_auto_id, String link_id) {
		RemoteDB.LinkInfo linkInfo = null;
		try {
			linkInfo = this.getVPNWebTopoManager2().getLinkInfo(vpn_auto_id,
					link_id);
		} catch (Exception ex) {
			m_logger.error("getLinkInfo:" + ex);
			ex.printStackTrace();
			linkInfo = null;
		}
		return linkInfo;
	}

	/**
	 * 修改链路信息
	 * 
	 * @param link_id
	 *            链路id
	 * @param linkInfo
	 *            链路信息对象
	 * @return
	 */
	public boolean modifyLinkInfo(String link_id, RemoteDB.LinkInfo linkInfo) {
		boolean flag = false;
		try {
			if (this.getVPNWebTopoManager2().modifyLinkInfo(link_id, linkInfo) != -1) {
				flag = true;
			}
		} catch (Exception ex) {
			m_logger.error("modifyLinkInfo:" + ex);
			ex.printStackTrace();
		}
		return flag;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

	}

}
