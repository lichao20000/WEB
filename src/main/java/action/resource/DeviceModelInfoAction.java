package action.resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.PageQueryActionSupport;
import com.linkage.litms.resource.DeviceAct;
import com.linkage.module.gwms.Global;

import dao.resource.DeviceModelDao;

/**
 * @author Jason(3412)
 * @date 2008-11-10
 */
public class DeviceModelInfoAction extends PageQueryActionSupport{
	private static Logger logger = LoggerFactory.getLogger(DeviceModelInfoAction.class);
	private String actionType;
	private String strVendorList;
	private String deviceModelName;
	private String vendorId;
	private String oui;
	private String ouiId;
	private String deviceModelId;
	private List deviceModelList;
	private List stbDeviceModelList;
	private String vendorAlias = "vendor_id";
	private String ethernum;
	private String etherrate;
	private String strBack;
	private int id;
	private List stbDeviceVendorList;
	private String deviceModelQry;

	private String vendor_name,vendor_add,telephone,staff_id,remark;

	private String ajax;

    // 导出数据
	private List<Map> data;
	// 导出文件列标题
	private String[] title;
	// 导出文件列
	private String[] column;
	// 导出文件名
	private String fileName;
	
	private DeviceModelDao modelDao;
	
	public String execute(){
		//每页显示15条
		numperpage = 15;
		//操作结果反馈，提示用户
		strBack = "";
		if("add".equals(actionType)){
			if(Global.HLJDX.equals(Global.instAreaShortName)){
				String strTmp = modelDao.addDeviceModelForOracle(deviceModelName, oui);
				if("".equals(strTmp)){
					strBack = "添加失败";
				}else {
					strBack = "添加成功，型号ID:" + strTmp;
				}
			}
			else 
			{
				//添加设备型号，是调用存储过程的，比较特殊
				String strTmp = modelDao.addDeviceModel(deviceModelName, oui);
				if("".equals(strTmp)){
					strBack = "添加失败";
				}else {
					strBack = "添加成功，型号ID:" + strTmp;
				}
			}
		}else if("edit".equals(actionType)){
			modelDao.updateDeviceModel(oui, deviceModelName, deviceModelId);
			strBack = "编辑成功";
		}else if("del".equals(actionType)){
			modelDao.delDeviceModelType(deviceModelId);
			modelDao.delDeviceModel(deviceModelId);
			strBack = "删除成功";
		}
		//获取型号列表
		List list= QueryPage(modelDao.getAllModelList());
		this.setDeviceModelList(list);
		
		//初始化厂商OUI列表
		this.setStrVendorList(new DeviceAct().getVendorList(true,"",vendorAlias));
		//执行完之后，置空
		actionType = "";
		return "modelInfo";
	}
    /**
     *@描述 山西联通管理光猫的设备型号功能
     *@参数  []
     *@返回值  java.lang.String
     *@创建人  lsr
     *@创建时间  2020/3/3
     *@throws
     *@修改人和其它信息
     */
    public String executeSxlt(){
        //每页显示15条
        numperpage = 15;
        //操作结果反馈，提示用户
        if("add".equals(actionType)){
        	/*Map<String, Object> map = modelDao.addModel(deviceModelName, oui, ouiId);// 这里的oui就是vendor_id ouiId就是oui
			ajax = JSONObject.toJSONString(map);*/
        	//添加设备型号，是调用存储过程的，比较特殊
			String strTmp = modelDao.addDeviceModel(deviceModelName, oui);
			if("".equals(strTmp)){
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("code", "1");
				map.put("message", "添加失败");
				ajax = JSONObject.toJSONString(map);
			}else {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("code", "1");
				map.put("message", "添加成功，型号ID:" + strTmp);
				ajax = JSONObject.toJSONString(map);
			}
			return "ajax";
        }else if("edit".equals(actionType)){
        	/*Map<String, Object> map = modelDao.updateDeviceModel(oui, deviceModelName, deviceModelId);
            if(id == 0){
            	modelDao.addDeviceOui(ouiId,deviceModelId,"1");
			}else{
				modelDao.updateDeviceOui(id,ouiId);
			}
            ajax = JSONObject.toJSONString(map);*/
            
            modelDao.updateDeviceModel(oui, deviceModelName, deviceModelId);
            Map<String, Object> map = new HashMap<String, Object>();
			map.put("code", "1");
			map.put("message", "编辑成功.");
			strBack = "编辑成功";
			ajax = JSONObject.toJSONString(map);
			return "ajax";
        }else if("del".equals(actionType)){
        	/*Map<String, Object> map = modelDao.delModel(deviceModelId, id);
            ajax = JSONObject.toJSONString(map);*/
            modelDao.delDeviceModelType(deviceModelId);
			modelDao.delDeviceModel(deviceModelId);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("code", "1");
			map.put("message", "删除成功.");
			ajax = JSONObject.toJSONString(map);
			strBack = "删除成功";
			return "ajax";
        }
        //获取型号列表
        List list= QueryPage(modelDao.getAllModelListSxlt(vendor_add, deviceModelQry));
        this.setDeviceModelList(list);

        //初始化厂商OUI列表
        this.setStrVendorList(new DeviceAct().getVendorList(true,"",vendorAlias));
		
        //执行完之后，置空
        actionType = "";
        return "modelInfoSxlt";
    }

	/**
	 *@描述 山西联通管理机顶盒的设备型号功能
	 *@参数  []
	 *@返回值  java.lang.String
	 *@创建人  lsr
	 *@创建时间  2019/11/18
	 *@throws
	 *@修改人和其它信息
	 */
	public String stbExecute() {
		//每页显示15条
		numperpage = 15;
		
		if ("add".equals(actionType)) {
			/*Map<String, Object> map = modelDao.addStbModel(deviceModelName, oui, ouiId);// 这里的oui就是vendor_id ouiId就是oui
			ajax = JSONObject.toJSONString(map);*/

			String strTmp = modelDao.addStbDeviceModel(deviceModelName, oui);
			if("".equals(strTmp)){
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("code", "1");
				map.put("message", "添加失败");
				ajax = JSONObject.toJSONString(map);
			}else {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("code", "1");
				map.put("message", "添加成功，型号ID:" + strTmp);
				ajax = JSONObject.toJSONString(map);
			}
			return "ajax";
		} else if ("edit".equals(actionType)) {
			/*Map<String, Object> map = modelDao.updateStbDeviceModel(oui, deviceModelName, deviceModelId);
			// 以往数据不包含 oui,需要添加 oui 存入 tab_gw_device_init_oui 表
			if(id == 0){
				modelDao.addDeviceOui(ouiId,deviceModelId,"4");
			}else{
				modelDao.updateDeviceOui(id,ouiId);
			}
			ajax = JSONObject.toJSONString(map);
			return "ajax";*/
			
			modelDao.updateStbDeviceModel(oui, deviceModelName, deviceModelId);
            Map<String, Object> map = new HashMap<String, Object>();
			map.put("code", "1");
			map.put("message", "编辑成功.");
			strBack = "编辑成功";
			ajax = JSONObject.toJSONString(map);
			return "ajax";
		} else if ("del".equals(actionType)) {
			/*Map<String, Object> map = modelDao.delStbModel(deviceModelId, id);
            ajax = JSONObject.toJSONString(map);
			return "ajax";*/
			
			modelDao.delStbDeviceModel(deviceModelId);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("code", "1");
			map.put("message", "删除成功.");
			ajax = JSONObject.toJSONString(map);
			strBack = "删除成功";
			return "ajax";
		}
		//获取型号列表
		List list = QueryPage(modelDao.getStbAllModelList(vendor_add, deviceModelQry));
		this.setStbDeviceModelList(list);

		//初始化厂商OUI列表
		this.setStrVendorList(new DeviceAct().getStbVendorList(true, "", vendorAlias));
		//执行完之后，置空
		actionType = "";
		return "stbModelInfo";
	}

	/**
	 *@描述 山西联通管理机顶盒的厂商功能
	 *@参数  []
	 *@返回值  java.lang.String
	 *@创建人  lsr
	 *@创建时间  2019/11/18
	 *@throws
	 *@修改人和其它信息
	 */
	public String StbVendorInfo() {
		//每页显示15条
		numperpage = 15;
		
		if ("add".equals(actionType)) {
			//添加设备厂商
			Map<String, Object> map = modelDao.addStbVendorInfo(vendor_name,vendor_add,telephone,staff_id,remark);
			ajax = JSONObject.toJSONString(map);
			return "ajax";
		} else if ("update".equals(actionType)) {
			Map<String, Object> map = modelDao.updateStbVendorInfo(vendorId,vendor_name,vendor_add,telephone,staff_id,remark);
			ajax = JSONObject.toJSONString(map);
			return "ajax";
		} else if ("delete".equals(actionType)) {
			Map<String, Object> map = modelDao.deleteStbVendorInfo(vendorId);
			ajax = JSONObject.toJSONString(map);
			return "ajax";
		}
		//获取型号列表
		List list = QueryPage(modelDao.getStbVendorInfoList(vendor_name, vendor_add));
		this.setStbDeviceVendorList(list);
		//执行完之后，置空
		actionType = "";
		return "stbVendorInfo";
	}

	/**
	 *@描述 山西联通管理光猫的厂商功能
	 *@参数  []
	 *@返回值  java.lang.String
	 *@创建人  lsr
	 *@创建时间  2019/11/18
	 *@throws
	 *@修改人和其它信息
	 */
	public String VendorInfoForSxlt() {
		
		//每页显示15条
		numperpage = 15;
		//操作结果反馈，提示用户
		strBack = "";
		if ("add".equals(actionType)) {
			//添加设备厂商
			Map<String, Object> map = modelDao.addVendorInfoForSxlt(vendor_name,vendor_add,telephone,staff_id,remark);
			ajax = JSONObject.toJSONString(map);
			return "ajax";
		} else if ("update".equals(actionType)) {
			Map<String, Object> map = modelDao.updateVendorInfoForSxlt(vendorId,vendor_name,vendor_add,telephone,staff_id,remark);
			ajax = JSONObject.toJSONString(map);
			return "ajax";
		} else if ("delete".equals(actionType)) {
			Map<String, Object> map = modelDao.deleteVendorInfoForSxlt(vendorId);
			ajax = JSONObject.toJSONString(map);
			return "ajax";
		}
		//获取厂商列表
		List list = QueryPage(modelDao.getVendorInfoListForSxlt(vendor_name, vendor_add));
		this.setStbDeviceVendorList(list);
		//执行完之后，置空
		actionType = "";
		return "VendorInfoForSxlt";
	}
	
	public String editEther4jl(){
		//初始化ether_num_rate表
		modelDao.initEther();
		//每页显示15条
		numperpage = 15;
		//操作结果反馈，提示用户
		strBack = "";
		if("edit".equals(actionType)){
			modelDao.updateEther(ethernum, etherrate, deviceModelId);
			strBack = "编辑成功";
		}
		//获取型号列表
		List list= QueryPage(modelDao.getAllModelList4jlEther());
		this.setDeviceModelList(list);
		
		//初始化厂商OUI列表
		this.setStrVendorList(new DeviceAct().getVendorList(true,"",vendorAlias));
		//执行完之后，置空
		actionType = "";
		return "modelInfo4jlEther";
	}
	
	public String getDetail(){
		Map<String,String> vendorMap = modelDao.getVendor(vendorId);
		vendorId = StringUtil.getStringValue(vendorMap,"vendor_add","");
		String[] rates = etherrate.split(",");
		deviceModelList = new ArrayList<HashMap<String,String>>();
		for(int i=0;i<rates.length;i++){
			Map<String,String> one = new HashMap<String,String>();
			one.put("port", "端口"+(i+1));
			one.put("rate", rates[i]);
			deviceModelList.add(one);
		}
		logger.warn("vendorId="+vendorId);
		logger.warn("deviceModelList="+deviceModelList);
		
		return "etherDetail";
	}
	
	
	public String getExcel()
	{
		logger.debug("getExcel()");
		fileName = "MODELINFO";
		title = new String[] { "型号ID", "厂商名称", "型号名称", "网口数量" ,"网口速率"};
		column = new String[] { "modelid", "vendorname", "modelname", "ethernum","etherrate" };
		
		data = modelDao.getAllModelList4jlEther();
		return "excel";
	}

	
	public String getDeviceModelName() {
		return deviceModelName;
	}

	public void setDeviceModelName(String deviceModelName) {
		this.deviceModelName = deviceModelName;
	}

	public void setStrVendorList(String strVendorList) {
		this.strVendorList = strVendorList;
	}

	public String getStrVendorList() {
		return strVendorList;
	}


	public String getActionType() {
		return actionType;
	}


	public String getOui() {
		return oui;
	}


	public void setOui(String oui) {
		this.oui = oui;
	}


	public List getDeviceModelList() {
		return deviceModelList;
	}


	public void setDeviceModelList(List deviceModelList) {
		this.deviceModelList = deviceModelList;
	}


	public void setModelDao(DeviceModelDao modelDao) {
		this.modelDao = modelDao;
	}


	public String getVendorAlias() {
		return vendorAlias;
	}


	public void setDeviceModelId(String deviceModelId) {
		this.deviceModelId = deviceModelId;
	}


	public void setActionType(String actionType) {
		this.actionType = actionType;
	}


	public String getStrBack() {
		return strBack;
	}

	
	public String getEthernum()
	{
		return ethernum;
	}

	
	public void setEthernum(String ethernum)
	{
		this.ethernum = ethernum;
	}

	
	public String getEtherrate()
	{
		return etherrate;
	}

	
	public void setEtherrate(String etherrate)
	{
		this.etherrate = etherrate;
	}	
	
	/**
	 * @return the data
	 */
	public List<Map> getData()
	{
		return data;
	}

	/**
	 * @param data
	 *            the data to set
	 */
	public void setData(List<Map> data)
	{
		this.data = data;
	}

	/**
	 * @return the title
	 */
	public String[] getTitle()
	{
		return title;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String[] title)
	{
		this.title = title;
	}

	/**
	 * @return the column
	 */
	public String[] getColumn()
	{
		return column;
	}

	/**
	 * @param column
	 *            the column to set
	 */
	public void setColumn(String[] column)
	{
		this.column = column;
	}

	/**
	 * @return the fileName
	 */
	public String getFileName()
	{
		return fileName;
	}

	/**
	 * @param fileName
	 *            the fileName to set
	 */
	public void setFileName(String fileName)
	{
		this.fileName = fileName;
	}

	
	public void setVendorAlias(String vendorAlias)
	{
		this.vendorAlias = vendorAlias;
	}

	
	public String getVendorId()
	{
		return vendorId;
	}

	
	public void setVendorId(String vendorId)
	{
		this.vendorId = vendorId;
	}

	public String getOuiId() {
		return ouiId;
	}

	public void setOuiId(String ouiId) {
		this.ouiId = ouiId;
	}

	public List getStbDeviceModelList() {
		return stbDeviceModelList;
	}

	public void setStbDeviceModelList(List stbDeviceModelList) {
		this.stbDeviceModelList = stbDeviceModelList;
	}
	public List getStbDeviceVendorList() {
		return stbDeviceVendorList;
	}

	public void setStbDeviceVendorList(List stbDeviceVendorList) {
		this.stbDeviceVendorList = stbDeviceVendorList;
	}

	public String getVendor_name() {
		return vendor_name;
	}

	public void setVendor_name(String vendor_name) {
		this.vendor_name = vendor_name;
	}

	public String getVendor_add() {
		return vendor_add;
	}

	public void setVendor_add(String vendor_add) {
		this.vendor_add = vendor_add;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getStaff_id() {
		return staff_id;
	}

	public void setStaff_id(String staff_id) {
		this.staff_id = staff_id;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public String getAjax() {
		return ajax;
	}
	
	public void setAjax(String ajax) {
		this.ajax = ajax;
	}
	
	public String getDeviceModelQry()
	{
		return deviceModelQry;
	}
	
	public void setDeviceModelQry(String deviceModelQry)
	{
		this.deviceModelQry = deviceModelQry;
	}
	
}
