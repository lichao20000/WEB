package com.linkage.litms.common.corba;

import java.util.Map;

import org.omg.CORBA.ORB;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import MidWare.DevAreaObject;
import MidWare.DeviceListObject;
import MidWare.DeviceObject;
import MidWare.OperatorObject;
import MidWare.ServiceBussiness;
import MidWare.ServiceBussinessObject;
import MidWare.ServiceUpgradeObject;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.database.DataSetBean;

public class MidWareCorba {

	/** log */
	private static Logger logger = LoggerFactory.getLogger(MidWareCorba.class);
	/**
	 * 
	 */
	public MidWareCorba() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * 增加设备接口
	 * @param deviceListObject
	 * @return
	 */
	public MidWare.DeviceObjectRep[] addDevice(
			DeviceListObject[] deviceListObject) {
		MidWare.DeviceObjectRep[] deviceObjectRepArr = null;
		try {
			if (null != deviceListObject && deviceListObject.length > 0) {

				org.omg.CORBA.Object objRef = (org.omg.CORBA.Object) LipossGlobals.G_corbaObject
						.get("MidWare,MidWare_Poa");
				MidWare.MidWareInter mw = MidWare.MidWareInterHelper
						.narrow(objRef);
				if (mw == null) {
					logger.warn("调用中间件接口失败，请检查MidWare程序是否正在运行。");
					return null;
				}
				deviceObjectRepArr = mw.AddDevice(deviceListObject);
			}
		} catch (Exception e) {
			rebindCorba();
			reAddDevice(deviceListObject);
			e.printStackTrace();
		}
		return deviceObjectRepArr;
	}

	/**
	 *  增加设备重绑接口
	 * @param deviceListObject
	 * @return
	 */
	public MidWare.DeviceObjectRep[] reAddDevice(
			DeviceListObject[] deviceListObject) {
		MidWare.DeviceObjectRep[] deviceObjectRepArr = null;
		try {
			if (null != deviceListObject && deviceListObject.length > 0) {

				org.omg.CORBA.Object objRef = (org.omg.CORBA.Object) LipossGlobals.G_corbaObject
						.get("MidWare,MidWare_Poa");
				MidWare.MidWareInter mw = MidWare.MidWareInterHelper
						.narrow(objRef);
				if (mw == null) {
					logger.warn("调用中间件接口失败，请检查MidWare程序是否正在运行。");
					return null;
				}
				deviceObjectRepArr = mw.AddDevice(deviceListObject);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return deviceObjectRepArr;
	}

	/**
	 * 删除设备接口
	 * @param deviceObjectArr
	 * @return
	 */
	public MidWare.DeviceObjectRep[] delDevice(DeviceObject[] deviceObjectArr) {
		MidWare.DeviceObjectRep[] deviceObjectRepArr = null;
		try {
			if (null != deviceObjectArr && deviceObjectArr.length > 0) {

				org.omg.CORBA.Object objRef = (org.omg.CORBA.Object) LipossGlobals.G_corbaObject
						.get("MidWare,MidWare_Poa");
				MidWare.MidWareInter mw = MidWare.MidWareInterHelper
						.narrow(objRef);
				if (mw == null) {
					logger.warn("调用中间件接口失败，请检查MidWare程序是否正在运行。");
					return null;
				}
				deviceObjectRepArr = mw.DelDevice(deviceObjectArr);
			}
		} catch (Exception e) {
			rebindCorba();
			reDelDevice(deviceObjectArr);
			e.printStackTrace();
		}
		return deviceObjectRepArr;
	}

	/**
	 * 删除设备重绑接口
	 * @param deviceObjectArr
	 * @return
	 */

	public MidWare.DeviceObjectRep[] reDelDevice(DeviceObject[] deviceObjectArr) {
		MidWare.DeviceObjectRep[] deviceObjectRepArr = null;
		try {
			if (null != deviceObjectArr && deviceObjectArr.length > 0) {

				org.omg.CORBA.Object objRef = (org.omg.CORBA.Object) LipossGlobals.G_corbaObject
						.get("MidWare,MidWare_Poa");
				MidWare.MidWareInter mw = MidWare.MidWareInterHelper
						.narrow(objRef);
				if (mw == null) {
					logger.warn("调用中间件接口失败，请检查MidWare程序是否正在运行。");
					return null;
				}
				deviceObjectRepArr = mw.DelDevice(deviceObjectArr);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return deviceObjectRepArr;
	}

	/**
	 * 更改设备参数接口
	 * @param deviceListObjectArr
	 * @return
	 */
	public MidWare.DeviceObjectRep[] updateDevice(
			DeviceListObject[] deviceListObjectArr) {
		MidWare.DeviceObjectRep[] deviceObjectRepArr = null;
		try {
			if (null != deviceListObjectArr && deviceListObjectArr.length > 0) {

				org.omg.CORBA.Object objRef = (org.omg.CORBA.Object) LipossGlobals.G_corbaObject
						.get("MidWare,MidWare_Poa");
				MidWare.MidWareInter mw = MidWare.MidWareInterHelper
						.narrow(objRef);
				if (mw == null) {
					logger.warn("调用中间件接口失败，请检查MidWare程序是否正在运行。");
					return null;
				}
				deviceObjectRepArr = mw.UpdateDevice(deviceListObjectArr);
			}
		} catch (Exception e) {
			rebindCorba();
			reUpdateDevice(deviceListObjectArr);
			e.printStackTrace();
		}
		return deviceObjectRepArr;
	}

	/**
	 * 更改设备参数重绑接口
	 * @param deviceListObjectArr
	 * @return
	 */
	public MidWare.DeviceObjectRep[] reUpdateDevice(
			DeviceListObject[] deviceListObjectArr) {
		MidWare.DeviceObjectRep[] deviceObjectRepArr = null;
		try {
			if (null != deviceListObjectArr && deviceListObjectArr.length > 0) {

				org.omg.CORBA.Object objRef = (org.omg.CORBA.Object) LipossGlobals.G_corbaObject
						.get("MidWare,MidWare_Poa");
				MidWare.MidWareInter mw = MidWare.MidWareInterHelper
						.narrow(objRef);
				if (mw == null) {
					logger.warn("调用中间件接口失败，请检查MidWare程序是否正在运行。");
					return null;
				}
				deviceObjectRepArr = mw.UpdateDevice(deviceListObjectArr);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return deviceObjectRepArr;
	}

	/**
	 * 业务开通
	 * 
	 * @param serviceBussinessObject
	 * @param ior
	 * @return
	 */
	public MidWare.DeviceObjectRep[] openService(
			ServiceBussinessObject[] serviceBussinessObject) {
		MidWare.DeviceObjectRep[] deviceObjectRepArr = null;
		try {
			if (null != serviceBussinessObject
					&& serviceBussinessObject.length > 0) {

				org.omg.CORBA.Object objRef = (org.omg.CORBA.Object) LipossGlobals.G_corbaObject
						.get("MidWare,MidWare_Poa");
				MidWare.MidWareInter mw = MidWare.MidWareInterHelper
						.narrow(objRef);
				if (mw == null) {
					logger.warn("调用中间件接口失败，请检查MidWare程序是否正在运行。");
					return null;
				}
				deviceObjectRepArr = mw.OpenService(serviceBussinessObject);
			}
		} catch (Exception e) {
			rebindCorba();
			reOpenService(serviceBussinessObject);
			e.printStackTrace();
		}
		return deviceObjectRepArr;
	}

	/**
	 * 业务开通重绑接口
	 * 
	 * @param serviceBussinessObject
	 * @param gather_id
	 * @return
	 */
	public MidWare.DeviceObjectRep[] reOpenService(
			ServiceBussinessObject[] serviceBussinessObject) {
		MidWare.DeviceObjectRep[] deviceObjectRepArr = null;
		try {
			if (null != serviceBussinessObject
					&& serviceBussinessObject.length > 0) {
				org.omg.CORBA.Object objRef = (org.omg.CORBA.Object) LipossGlobals.G_corbaObject
						.get("MidWare,MidWare_Poa");
				MidWare.MidWareInter mw = MidWare.MidWareInterHelper
						.narrow(objRef);
				if (mw == null) {
					logger.warn("调用中间件接口失败，请检查MidWare程序是否正在运行。");
					return null;
				}
				deviceObjectRepArr = mw.OpenService(serviceBussinessObject);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return deviceObjectRepArr;
	}

	/**
	 * 业务暂停接口
	 * 
	 * @param serviceBussiness
	 * @param ior
	 * @return
	 */
	public MidWare.DeviceObjectRep[] stopService(
			ServiceBussiness[] serviceBussiness) {
		MidWare.DeviceObjectRep[] deviceObjectRepArr = null;
		try {
			logger.debug("serviceBussiness:" + serviceBussiness.length);
			if (null != serviceBussiness && serviceBussiness.length > 0) {				
				org.omg.CORBA.Object objRef = (org.omg.CORBA.Object) LipossGlobals.G_corbaObject
						.get("MidWare,MidWare_Poa");
				MidWare.MidWareInter mw = MidWare.MidWareInterHelper
						.narrow(objRef);
				if (mw == null) {
					logger.warn("调用中间件接口失败，请检查MidWare程序是否正在运行。");
					return null;
				}
				deviceObjectRepArr = mw.StopService(serviceBussiness);
			}
		} catch (Exception e) {
			rebindCorba();
			reStopService(serviceBussiness);
			e.printStackTrace();
		}
		return deviceObjectRepArr;
	}

	/**
	 * 业务暂停重绑接口
	 * 
	 * @param serviceBussinessObject
	 * @param gather_id
	 * @return
	 */
	public MidWare.DeviceObjectRep[] reStopService(
			ServiceBussiness[] serviceBussiness) {
		MidWare.DeviceObjectRep[] deviceObjectRep = null;
		try {
			if (null != serviceBussiness && serviceBussiness.length > 0) {
				
				org.omg.CORBA.Object objRef = (org.omg.CORBA.Object) LipossGlobals.G_corbaObject
						.get("MidWare,MidWare_Poa");
				MidWare.MidWareInter mw = MidWare.MidWareInterHelper
						.narrow(objRef);
				if (mw == null) {
					logger.warn("调用中间件接口失败，请检查MidWare程序是否正在运行。");
					return null;
				}
				deviceObjectRep = mw.StopService(serviceBussiness);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return deviceObjectRep;
	}

	/**
	 * 业务销户接口
	 * 
	 * @param serviceBussiness
	 * @param ior
	 * @return
	 */
	public MidWare.DeviceObjectRep[] cancelService(
			ServiceBussiness[] serviceBussiness) {
		MidWare.DeviceObjectRep[] deviceObjectRep = null;
		try {
			if (null != serviceBussiness && serviceBussiness.length > 0) {
				org.omg.CORBA.Object objRef = (org.omg.CORBA.Object) LipossGlobals.G_corbaObject
						.get("MidWare,MidWare_Poa");
				MidWare.MidWareInter mw = MidWare.MidWareInterHelper
						.narrow(objRef);
				if (mw == null) {
					logger.warn("调用中间件接口失败，请检查MidWare程序是否正在运行。");
					return null;
				}
				deviceObjectRep = mw.CancelService(serviceBussiness);
			}
		} catch (Exception e) {
			rebindCorba();
			reCancelService(serviceBussiness);
			e.printStackTrace();
		}
		return deviceObjectRep;
	}

	/**
	 * 业务销户重绑接口
	 * 
	 * @param serviceBussinessObject
	 * @param gather_id
	 * @return
	 */
	public MidWare.DeviceObjectRep[] reCancelService(
			ServiceBussiness[] serviceBussiness) {
		MidWare.DeviceObjectRep[] deviceObjectRep = null;
		try {
			if (null != serviceBussiness && serviceBussiness.length > 0) {
				org.omg.CORBA.Object objRef = (org.omg.CORBA.Object) LipossGlobals.G_corbaObject
						.get("MidWare,MidWare_Poa");
				MidWare.MidWareInter mw = MidWare.MidWareInterHelper
						.narrow(objRef);
				if (mw == null) {
					logger.warn("调用中间件接口失败，请检查MidWare程序是否正在运行。");
					return null;
				}
				deviceObjectRep = mw.CancelService(serviceBussiness);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return deviceObjectRep;
	}

	/**
	 * 业务恢复接口
	 * 
	 * @param serviceBussiness
	 * @param ior
	 * @return
	 */
	public MidWare.DeviceObjectRep[] recoverService(
			ServiceBussiness[] serviceBussiness) {
		MidWare.DeviceObjectRep[] deviceObjectRep = null;
		try {
			if (null != serviceBussiness && serviceBussiness.length > 0) {
				org.omg.CORBA.Object objRef = (org.omg.CORBA.Object) LipossGlobals.G_corbaObject
						.get("MidWare,MidWare_Poa");
				MidWare.MidWareInter mw = MidWare.MidWareInterHelper
						.narrow(objRef);
				if (mw == null) {
					logger.warn("调用中间件接口失败，请检查MidWare程序是否正在运行。");
					return null;
				}
				deviceObjectRep = mw.RecoverService(serviceBussiness);
			}
		} catch (Exception e) {
			rebindCorba();
			reRecoverService(serviceBussiness);
			e.printStackTrace();
		}
		return deviceObjectRep;
	}

	/**
	 * 业务恢复重绑接口
	 * 
	 * @param serviceBussinessObject
	 * @param gather_id
	 * @return
	 */
	public MidWare.DeviceObjectRep[] reRecoverService(
			ServiceBussiness[] serviceBussiness) {
		MidWare.DeviceObjectRep[] deviceObjectRep = null;
		try {
			if (null != serviceBussiness && serviceBussiness.length > 0) {
				org.omg.CORBA.Object objRef = (org.omg.CORBA.Object) LipossGlobals.G_corbaObject
						.get("MidWare,MidWare_Poa");
				MidWare.MidWareInter mw = MidWare.MidWareInterHelper
						.narrow(objRef);
				if (mw == null) {
					logger.warn("调用中间件接口失败，请检查MidWare程序是否正在运行。");
					return null;
				}
				deviceObjectRep = mw.RecoverService(serviceBussiness);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return deviceObjectRep;
	}

	/**
	 * 更改业务参数接口
	 * 
	 * @param serviceBussiness
	 * @param ior
	 * @return
	 */
	public MidWare.DeviceObjectRep[] modifyServiceParam(
			ServiceBussinessObject[] serviceBussinessObject) {
		MidWare.DeviceObjectRep[] deviceObjectRep = null;
		try {
			if (null != serviceBussinessObject
					&& serviceBussinessObject.length > 0) {
				org.omg.CORBA.Object objRef = (org.omg.CORBA.Object) LipossGlobals.G_corbaObject
						.get("MidWare,MidWare_Poa");
				MidWare.MidWareInter mw = MidWare.MidWareInterHelper
						.narrow(objRef);
				if (mw == null) {
					logger.warn("调用中间件接口失败，请检查MidWare程序是否正在运行。");
					return null;
				}
				deviceObjectRep = mw.ModifyServiceParam(serviceBussinessObject);
			}
		} catch (Exception e) {
			rebindCorba();
			reModifyServiceParam(serviceBussinessObject);
			e.printStackTrace();
		}
		return deviceObjectRep;
	}

	/**
	 * 更改业务参数重绑接口
	 * 
	 * @param serviceBussinessObject
	 * @param gather_id
	 * @return
	 */
	public MidWare.DeviceObjectRep[] reModifyServiceParam(
			ServiceBussinessObject[] serviceBussinessObject) {
		MidWare.DeviceObjectRep[] deviceObjectRep = null;
		try {
			if (null != serviceBussinessObject
					&& serviceBussinessObject.length > 0) {
				org.omg.CORBA.Object objRef = (org.omg.CORBA.Object) LipossGlobals.G_corbaObject
						.get("MidWare,MidWare_Poa");
				MidWare.MidWareInter mw = MidWare.MidWareInterHelper
						.narrow(objRef);
				if (mw == null) {
					logger.warn("调用中间件接口失败，请检查MidWare程序是否正在运行。");
					return null;
				}
				deviceObjectRep = mw.ModifyServiceParam(serviceBussinessObject);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return deviceObjectRep;
	}

	/**
	 * 业务模块升级接口
	 * 
	 * @param serviceUpgradeObject
	 * @return
	 */
	public MidWare.DeviceObjectRep[] upgradeService(
			ServiceUpgradeObject[] serviceUpgradeObject) {
		MidWare.DeviceObjectRep[] deviceObjectRep = null;
		try {
			if (null != serviceUpgradeObject && serviceUpgradeObject.length > 0) {
				org.omg.CORBA.Object objRef = (org.omg.CORBA.Object) LipossGlobals.G_corbaObject
						.get("MidWare,MidWare_Poa");
				MidWare.MidWareInter mw = MidWare.MidWareInterHelper
						.narrow(objRef);
				if (mw == null) {
					logger.warn("调用中间件接口失败，请检查MidWare程序是否正在运行。");
					return null;
				}
				deviceObjectRep = mw.UpgradeService(serviceUpgradeObject);
			}
		} catch (Exception e) {
			rebindCorba();
			reUpgradeService(serviceUpgradeObject);
			e.printStackTrace();
		}
		return deviceObjectRep;
	}

	/**
	 * 业务模块升级重绑接口
	 * 
	 * @param serviceUpgradeObject
	 * @return
	 */
	public MidWare.DeviceObjectRep[] reUpgradeService(
			ServiceUpgradeObject[] serviceUpgradeObject) {
		MidWare.DeviceObjectRep[] deviceObjectRep = null;
		try {
			if (null != serviceUpgradeObject && serviceUpgradeObject.length > 0) {
				org.omg.CORBA.Object objRef = (org.omg.CORBA.Object) LipossGlobals.G_corbaObject
						.get("MidWare,MidWare_Poa");
				MidWare.MidWareInter mw = MidWare.MidWareInterHelper
						.narrow(objRef);
				if (mw == null) {
					logger.warn("调用中间件接口失败，请检查MidWare程序是否正在运行。");
					return null;
				}
				deviceObjectRep = mw.UpgradeService(serviceUpgradeObject);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return deviceObjectRep;
	}

	/**
	 * 设备添加到域接口
	 * 
	 * @param devAreaObject
	 * @return
	 */
	public MidWare.DevAreaObjectRep[] addAreaDev(DevAreaObject[] devAreaObject) {
		MidWare.DevAreaObjectRep[] devAreaObjectRep = null;
		try {
			if (null != devAreaObject && devAreaObject.length > 0) {
				org.omg.CORBA.Object objRef = (org.omg.CORBA.Object) LipossGlobals.G_corbaObject
						.get("MidWare,MidWare_Poa");
				MidWare.MidWareInter mw = MidWare.MidWareInterHelper
						.narrow(objRef);
				if (mw == null) {
					logger.warn("调用中间件接口失败，请检查MidWare程序是否正在运行。");
					return null;
				}
				devAreaObjectRep = mw.AddAreaDev(devAreaObject);
			}
		} catch (Exception e) {
			rebindCorba();
			reAddAreaDev(devAreaObject);
			e.printStackTrace();
		}
		return devAreaObjectRep;
	}

	/**
	 * 设备添加到域重绑接口
	 * 
	 * @param devAreaObject
	 * @return
	 */
	public MidWare.DevAreaObjectRep[] reAddAreaDev(DevAreaObject[] devAreaObject) {
		MidWare.DevAreaObjectRep[] devAreaObjectRep = null;
		try {
			if (null != devAreaObject && devAreaObject.length > 0) {
				org.omg.CORBA.Object objRef = (org.omg.CORBA.Object) LipossGlobals.G_corbaObject
						.get("MidWare,MidWare_Poa");
				MidWare.MidWareInter mw = MidWare.MidWareInterHelper
						.narrow(objRef);
				if (mw == null) {
					logger.warn("调用中间件接口失败，请检查MidWare程序是否正在运行。");
					return null;
				}
				devAreaObjectRep = mw.AddAreaDev(devAreaObject);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return devAreaObjectRep;
	}

	/**
	 * 设备域删除接口
	 * 
	 * @param devAreaObject
	 * @return
	 */
	public MidWare.DevAreaObjectRep[] delAreaDev(DevAreaObject[] devAreaObject) {
		MidWare.DevAreaObjectRep[] devAreaObjectRep = null;
		try {
			if (null != devAreaObject && devAreaObject.length > 0) {
				org.omg.CORBA.Object objRef = (org.omg.CORBA.Object) LipossGlobals.G_corbaObject
						.get("MidWare,MidWare_Poa");
				MidWare.MidWareInter mw = MidWare.MidWareInterHelper
						.narrow(objRef);
				if (mw == null) {
					logger.warn("调用中间件接口失败，请检查MidWare程序是否正在运行。");
					return null;
				}
				devAreaObjectRep = mw.DelAreaDev(devAreaObject);
			}
		} catch (Exception e) {
			rebindCorba();
			reDelAreaDev(devAreaObject);
			e.printStackTrace();
		}
		return devAreaObjectRep;
	}

	/**
	 * 设备添加到域重绑接口
	 * 
	 * @param devAreaObject
	 * @return
	 */
	public MidWare.DevAreaObjectRep[] reDelAreaDev(DevAreaObject[] devAreaObject) {
		MidWare.DevAreaObjectRep[] devAreaObjectRep = null;
		try {
			if (null != devAreaObject && devAreaObject.length > 0) {
				org.omg.CORBA.Object objRef = (org.omg.CORBA.Object) LipossGlobals.G_corbaObject
						.get("MidWare,MidWare_Poa");
				MidWare.MidWareInter mw = MidWare.MidWareInterHelper
						.narrow(objRef);
				if (mw == null) {
					logger.warn("调用中间件接口失败，请检查MidWare程序是否正在运行。");
					return null;
				}
				devAreaObjectRep = mw.DelAreaDev(devAreaObject);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return devAreaObjectRep;
	}

	/**
	 * 更改操作员接口
	 * 
	 * @param operatorObject
	 * @return
	 */
	public MidWare.OperatorObjectRep[] modifyOperator(
			OperatorObject[] operatorObject) {
		MidWare.OperatorObjectRep[] operatorObjectRep = null;
		try {
			if (null != operatorObject && operatorObject.length > 0) {
				org.omg.CORBA.Object objRef = (org.omg.CORBA.Object) LipossGlobals.G_corbaObject
						.get("MidWare,MidWare_Poa");
				MidWare.MidWareInter mw = MidWare.MidWareInterHelper
						.narrow(objRef);
				if (mw == null) {
					logger.warn("调用中间件接口失败，请检查MidWare程序是否正在运行。");
					return null;
				}
				operatorObjectRep = mw.ModifyOperator(operatorObject);
			}
		} catch (Exception e) {
			rebindCorba();
			reModifyOperator(operatorObject);
			e.printStackTrace();
		}
		return operatorObjectRep;
	}

	/**
	 * 更改操作员重绑接口
	 * 
	 * @param operatorObject
	 * @return
	 */
	public MidWare.OperatorObjectRep[] reModifyOperator(
			OperatorObject[] operatorObject) {
		MidWare.OperatorObjectRep[] operatorObjectRep = null;
		try {
			if (null != operatorObject && operatorObject.length > 0) {
				org.omg.CORBA.Object objRef = (org.omg.CORBA.Object) LipossGlobals.G_corbaObject
						.get("MidWare,MidWare_Poa");
				MidWare.MidWareInter mw = MidWare.MidWareInterHelper
						.narrow(objRef);
				if (mw == null) {
					logger.warn("调用中间件接口失败，请检查MidWare程序是否正在运行。");
					return null;
				}
				operatorObjectRep = mw.ModifyOperator(operatorObject);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return operatorObjectRep;
	}

	/**
	 * 重新绑定corba接口
	 * 
	 */
	public void rebindCorba() {

		String sql = "select ior,object_name,object_poa from tab_ior where object_name='MidWare' and object_poa='MidWare_Poa'";
		String ior = "";
		String object_name = "";
		String object_poa = "";
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		Map fields = DataSetBean.getRecord(sql);
		if (null != fields) {
			ior = (String) fields.get("ior");
			object_name = (String) fields.get("object_name");
			object_poa = (String) fields.get("object_poa");

			String[] args = null;
			ORB orb = ORB.init(args, null);
			org.omg.CORBA.Object objRef = orb.string_to_object(ior);

			LipossGlobals.G_corbaObject.put(object_name + "," + object_poa,
					objRef);
		}

	}

}
