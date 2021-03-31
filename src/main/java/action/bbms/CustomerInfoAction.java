package action.bbms;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.litms.common.StaticTypeCommon;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.util.StringUtil;

import dao.bbms.CustomerInfoDAO;

/**
 * 客户资料信息管理
 * 
 * @author 陈仲民（5243）;alex(yanhj@)
 * @version 1.0
 * @since 2008-6-3
 * @category 资源管理
 */
@SuppressWarnings("unchecked")
public class CustomerInfoAction extends splitPageAction implements
		ServletRequestAware {

	/** serial */
	private static final long serialVersionUID = -8417109046307680604L;
	/** log */
	private static Logger logger = LoggerFactory
			.getLogger(CustomerInfoAction.class);
	// dao
	private CustomerInfoDAO customerInfo;
	// request取登陆帐号使用
	private HttpServletRequest request;
	// 客户ID
	private String customer_id;
	// 客户账号
	private String customer_account;
	// 地市
	private String city_id;
	// 局向
	private String office_id;
	// 小区
	private String zone_id;
	// 客户名称
	private String customer_name;
	// 客户密码
	private String customer_pwd;
	// 客户类型
	private String customer_type;
	// 客户规模
	private String customer_size;
	// 客户地址
	private String customer_address;
	// 联系人姓名
	private String linkman;
	// 联系人电话
	private String linkphone;
	// 客户状态:1开通,2暂停,3,销户
	private String customer_state;
	// 属地下拉框
	private List cityList;
	// 局向下拉框
	private List officeList;
	// 小区下拉框
	private List zoneList;
	// 客户资料列表
	private List customerList;
	// 客户资料信息
	private Map customerMap;
	// 操作结果
	private String msg;
	// 导出文件标题
	private String[] title;
	// 导出文件列名
	private String[] column;
	// 数据集
	private List data;
	// 导出文件名
	private String fileName;
	// 结果显示类型
	private String showtype;
	// 用户数量
	private int user_num;
	// 帐号设备信息列表
	private List userInfoList;
	/** mail */
	private String email;

	private String ajax;

	/**
	 * @return the ajax
	 */
	public String getAjax() {
		return ajax;
	}

	/**
	 * @param ajax
	 *            the ajax to set
	 */
	public void setAjax(String ajax) {
		this.ajax = ajax;
	}

	public String getCustomer_account() {
		return customer_account;
	}

	public void setCustomer_account(String customer_account) {
		this.customer_account = customer_account;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List getUserInfoList() {
		return userInfoList;
	}

	public void setUserInfoList(List userInfoList) {
		this.userInfoList = userInfoList;
	}

	public int getUser_num() {
		return user_num;
	}

	public void setUser_num(int user_num) {
		this.user_num = user_num;
	}

	public String getShowtype() {
		return showtype;
	}

	public void setShowtype(String showtype) {
		this.showtype = showtype;
	}

	public String[] getTitle() {
		return title;
	}

	public void setTitle(String[] title) {
		this.title = title;
	}

	public String[] getColumn() {
		return column;
	}

	public void setColumn(String[] column) {
		this.column = column;
	}

	public List getData() {
		return data;
	}

	public void setData(List data) {
		this.data = data;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Map getCustomerMap() {
		return customerMap;
	}

	public List getCustomerList() {
		return customerList;
	}

	public String getMsg() {
		return msg;
	}

	public void setServletRequest(HttpServletRequest arg0) {
		this.request = arg0;
	}

	public List getCityList() {
		return cityList;
	}

	public List getOfficeList() {
		return officeList;
	}

	public List getZoneList() {
		return zoneList;
	}

	public String getCustomer_id() {
		return customer_id;
	}

	public void setCustomer_id(String customer_id) {
		this.customer_id = customer_id;
	}

	public String getCity_id() {
		return city_id;
	}

	public void setCity_id(String city_id) {
		this.city_id = city_id;
	}

	public String getOffice_id() {
		return office_id;
	}

	public void setOffice_id(String office_id) {
		this.office_id = office_id;
	}

	public String getZone_id() {
		return zone_id;
	}

	public void setZone_id(String zone_id) {
		this.zone_id = zone_id;
	}

	public String getCustomer_name() {
		return customer_name;
	}

	public void setCustomer_name(String customer_name) {
		this.customer_name = customer_name;
	}

	public String getCustomer_pwd() {
		return customer_pwd;
	}

	public void setCustomer_pwd(String customer_pwd) {
		this.customer_pwd = customer_pwd;
	}

	public String getCustomer_type() {
		return customer_type;
	}

	public void setCustomer_type(String customer_type) {
		this.customer_type = customer_type;
	}

	public String getCustomer_size() {
		return customer_size;
	}

	public void setCustomer_size(String customer_size) {
		this.customer_size = customer_size;
	}

	public String getCustomer_address() {
		return customer_address;
	}

	public void setCustomer_address(String customer_address) {
		this.customer_address = customer_address;
	}

	public String getLinkman() {
		return linkman;
	}

	public void setLinkman(String linkman) {
		this.linkman = linkman;
	}

	public String getLinkphone() {
		return linkphone;
	}

	public void setLinkphone(String linkphone) {
		this.linkphone = linkphone;
	}

	public String getCustomer_state() {
		return customer_state;
	}

	public void setCustomer_state(String customer_state) {
		this.customer_state = customer_state;
	}

	public void setCustomerInfo(CustomerInfoDAO customerInfo) {
		this.customerInfo = customerInfo;
	}

	/**
	 * 
	 */
	public String execute() throws Exception {
		customerInfo.test();
		return SUCCESS;
	}

	/**
	 * 新增客户资料页面
	 * 
	 * @return
	 * @throws Exception
	 */
	public String addForm() throws Exception {
		// request
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		String curCity = curUser.getCityId();
		customer_id = String.valueOf(System.currentTimeMillis());
		logger.debug("customer_id={}", customer_id);
		// 获取属地、局向、小区信息
		cityList = CityDAO.getAllNextCityListByCityPid(curCity);
		officeList = customerInfo.getOfficeList();
		zoneList = customerInfo.getZoneList();

		return "addForm";
	}

	/**
	 * 新增客户操作
	 * 
	 * @return
	 * @throws Exception
	 */
	public String addCustomer() throws Exception {
		logger.debug("addCustomer()");

		// 新增操作
		if (StringUtil.IsEmpty(customer_id, true)) {
			customer_id = StaticTypeCommon.generateId();
			logger.debug("customer_id={}", customer_id);
		}
		int ret = customerInfo.addCustomer(customer_id, customer_account,
				city_id, office_id, zone_id, customer_name, customer_pwd,
				customer_type, customer_size, customer_address, linkman,
				linkphone, customer_state, email);
		if (ret > 0) {
			msg = "新增客户资料成功！";
		} else if (ret == -11) {
			msg = "客户账号已存在，请更改客户账号重试";
		} else {
			msg = "新增客户资料失败，请重试！";
		}
		showtype = "1";
		return "resultForm";
	}

	/**
	 * 编辑客户资料页面
	 * 
	 * @return
	 * @throws Exception
	 */
	public String editFrom() throws Exception {
		// request
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		String curCity = curUser.getCityId();
		// 获取属地、局向、小区信息
		cityList = CityDAO.getAllNextCityListByCityPid(curCity);
		officeList = customerInfo.getOfficeList();
		zoneList = customerInfo.getZoneList();
		// 获取当前客户资料信息
		customerMap = customerInfo.getCustomerInfo(customer_id);
		user_num = customerInfo.getUserNum((String) customerMap
				.get("customer_id"));
		return "editForm";
	}

	/**
	 * 编辑客户资料操作
	 * 
	 * @return
	 * @throws Exception
	 */
	public String editCustomer() throws Exception {
		// 编辑操作
		int ret = customerInfo.updCustomer(customer_id, city_id, office_id,
				zone_id, customer_name, customer_pwd, customer_type,
				customer_size, customer_address, linkman, linkphone,
				customer_state, email);
		if (ret > 0) {
			msg = "编辑客户资料成功！";
		} else {
			msg = "编辑客户资料失败，请重试！";
		}
		showtype = "1";
		return "resultForm";
	}

	/**
	 * 删除客户资料操作
	 * 
	 * @return
	 * @throws Exception
	 */
	public String delCustomer() throws Exception {
		// request
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		String curCity = curUser.getCityId();

		int num = customerInfo.getUserNumByAll(customer_id);

		if (0 < num) {
			msg = "请先删除该客户的业务账号！";
		} else {
			// 删除操作
			int ret = customerInfo.delCustomer(customer_id);
			if (ret > 0) {
				msg = "删除客户资料成功！";
			} else {
				msg = "删除客户资料失败，请重试！";
			}
		}

		// 查询列表
		customerList = customerInfo.queryCustomer(curPage_splitPage,
				num_splitPage, null, null, curCity);
		maxPage_splitPage = customerInfo.getCustomerCount(num_splitPage, null,
				null, curCity);
		return "list";
	}

	/**
	 * 查询页面初始化
	 * 
	 * @return
	 * @throws Exception
	 */
	public String queryForm() throws Exception {

		return "queryForm";
	}

	/**
	 * 查询客户资料结果列表
	 * 
	 * @return
	 * @throws Exception
	 */
	public String queryData() throws Exception {
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		String curCity = curUser.getCityId();
		customerList = customerInfo.queryCustomer(curPage_splitPage,
				num_splitPage, customer_id, customer_name, curCity);
		maxPage_splitPage = customerInfo.getCustomerCount(num_splitPage,
				customer_id, customer_name, curCity);
		return "list";
	}

	/**
	 * 翻页方法
	 * 
	 * @return
	 * @throws Exception
	 */
	public String goPage() throws Exception {
		// request
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		String curCity = curUser.getCityId();
		customerList = customerInfo.queryCustomer(curPage_splitPage,
				num_splitPage, customer_id, customer_name, curCity);
		maxPage_splitPage = customerInfo.getCustomerCount(num_splitPage,
				customer_id, customer_name, curCity);
		return "list";
	}

	/**
	 * 设备详细信息数据
	 * 
	 * @return
	 * @throws Exception
	 */
	public String detailInfo() throws Exception {
		customerMap = customerInfo.getCustomerDetail(customer_id);
		userInfoList = customerInfo.getUserByCutomer(customer_id);
		user_num = customerInfo.getUserNum((String) customerMap
				.get("customer_id"));
		return "detail";
	}

	public String exportExcel() throws Exception {
		title = customerInfo.getTitle();
		column = customerInfo.getColumn();
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		String curCity = curUser.getCityId();
		data = customerInfo.getExportData(customer_id, customer_name, curCity);
		fileName = "customerInfo";
		return "excel";
	}

	/**
	 * 确认客户ID是否已经存在
	 */
	public String comfirmCustomerId() throws Exception {

		int num = customerInfo.comfirmCustomerId(customer_id);

		if (num > 0) {
			this.ajax = "true";
		} else {
			this.ajax = "false";
		}

		return "ajax";
	}
}
