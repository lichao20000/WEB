/*
 * @(#)DbUser.java	1.00 1/18/2006
 *
 * Copyright 2005 联创科技.版权所有
 */
package com.linkage.litms.system.dbimpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.linkage.commons.db.DBUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.util.EncryptionUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.litms.common.database.PrepareSQL;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.system.User;
import com.linkage.litms.system.UserNotFoundException;
import com.linkage.module.gwms.Global;

/**
 * 用数据库的方式实现User接口
 * 
 * @author yuht
 * @version 1.00, 1/18/2006
 * @since Liposs 2.1
 */
public class DbUser implements User {
	private static Logger logger = LoggerFactory.getLogger(DbUser.class);
	
    private static final int INSERT_OPER = 1;

    private static final int UPDATE_OPER = 2;

    private long acc_oid;

    private String account = null;

    private String passwd = null;

    private long creator;
    
    private String minPwdLength = null;

    private String init_page = null;
    
    private String parentid = null;

    private String city_id = null;

    private String custom_id = null;

    private Date creationDate;

    private Date lastLoginDate;

    private long area_id;
    
    private Map iorMap = null;

    private long role_id;

    private long[] roleId;

    private Map map = null;

    private PrepareSQL pSQL = null;

    /**
     * 创建用户账号信息
     */
    private String INSERT_USER = "insert into tab_accounts (acc_oid,acc_loginname,"
            + "acc_password,creator,creationdate,acc_last_login) values (?,?,?,?,?,?)";

    /**
     * 修改用户账号信息
     */
    private String UPDATE_USER = "update tab_accounts set acc_password=?,"
            + "acc_last_login=? where acc_oid=?";

    /**
     * 修改用户默认topo层(parentid)
     */
    private String UPDATE_PARENT_ID = "update tab_accounts set parentid=? where acc_oid=?";
    
    /**
     * 创建用户资料信息
     */
    private String INSERT_USERINO = "insert into tab_persons (per_acc_oid,"
            + "per_searchcode,per_name,per_lastname,per_gender,per_title,per_jobtitle,"
            + "per_phone,per_mobile,per_email,per_dep_oid,per_remark,per_birthdate,per_city) values"
            + "(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

    /**
     * 修改用户资料信息
     */
    private String UPDATE_USERINFO = "update tab_persons set per_searchcode=?,"
            + "per_name=?,per_lastname=?,per_gender=?,per_title=?,per_jobtitle=?,"
            + "per_phone=?,per_mobile=?,per_email=?,per_dep_oid=?,per_remark=?,"
            + "per_birthdate=?,per_city=? where per_acc_oid=?";

    /**
     * 创建用户角色
     */
    private String INSERT_ROLE = "insert into tab_acc_role (acc_oid,role_id) values(?,?)";

    /**
     * 修改用户角色
     */
    private String UPDATE_ROLE = "update tab_acc_role set role_id=? where acc_oid=?";

    /**
     * 创建用户域
     */
    private String UPDATE_AREA = "update tab_acc_area set area_id=? where acc_oid=?";

    /**
     * 修改用户域
     */
    private String INSERT_AREA = "insert into tab_acc_area (area_id,acc_oid) "
            + "values(?,?)";

    /**
     * 通过用户账号ID载入用户账号信息
     */
    private String LOAD_USER_BY_ID = "select * from tab_accounts where acc_oid=?";

    /**
     * 根据区域编号和用户名定位用户信息
     */
    private String LOAD_USER_BY_ACCANDAREA = "select * from tab_accounts where acc_loginname=? "
            + "and acc_oid in (select acc_oid from tab_acc_area where area_id=?)";
    /**
     * 根据区域编号和用户名定位用户信息
     */
    private String LOAD_USER_BY_ACCANDAREA_JS_CAS = " select enName,city_id from tab_excel_syn_accounts where enName =? ";

    /**
     * 通过用户账号ID载入用户资料信息
     */
    private String LOAD_USERINFO_BY_ID = "select * from tab_persons where per_acc_oid=?";

    /**
     * 通过用户账号ID载入用户所属角色ID
     */
    private String LOAD_ROLE_BY_ID = "select * from tab_acc_role where acc_oid=? order by role_id";

    /**
     * 通过用户账号ID载入用户管理域的信息列表
     */
    private String LOAD_AREAS_BY_ID = "select * from tab_acc_area where acc_oid=?";
    
    
    /**
     * 载入ior信息sql
     */
    private String LOAD_IOR = "select * from tab_ior";

    /**
     * 带参数构造函数，通过acc_oid载入用户信息
     * 
     * @param acc_oid
     *            用户账号ID
     */
    public DbUser(long acc_oid) throws UserNotFoundException {
        if (pSQL == null)
            pSQL = new PrepareSQL();

        this.acc_oid = acc_oid;

        loadFromDb();
    }

    /**
     * 带参数构造函数，通过account和area_id载入用户信息
     * 
     * @param account
     *            用户账号
     * @param area_id
     *            用户所在域ID
     */
    public DbUser(String account, long area_id) throws UserNotFoundException {
        if (pSQL == null)
            pSQL = new PrepareSQL();

        this.account = account;
        this.area_id = area_id;

        loadFromDb();
    }

    public DbUser(String account, long area_id, boolean cas) throws UserNotFoundException {
        if (pSQL == null)
            pSQL = new PrepareSQL();

        this.account = account;
        this.area_id = area_id;

        loadFromDb_CAS();
    }

    /**
     * 带参数构造函数，根据参数创建用户
     * 
     * @param account
     *            用户账号
     * @param passwd
     *            用户密码
     * @param creator
     *            用户创建者
     * @param role_id
     *            用户所属角色ID
     * @param area_id
     *            用户管理域ID
     */
    public DbUser(String account, String passwd, long creator, long role_id,
            long area_id) {
        if (pSQL == null)
            pSQL = new PrepareSQL();

        this.account = account;
        this.passwd = passwd;
        this.role_id = role_id;
        this.area_id = area_id;
        this.creator = creator;

        insertIntoDb();
    }

    /**
     * 带参数构造函数，根据参数创建用户和用户资料
     * 
     * @param account
     *            用户账号
     * @param passwd
     *            用户密码
     * @param creator
     *            用户创建者
     * @param _map
     *            用户资料Map
     * @param role_id
     *            用户所属角色ID
     * @param area_id
     *            用户管理域ID
     * @param _map
     *            用户资料信息
     */
    public DbUser(String account, String passwd, long creator, long role_id,
            long area_id, Map _map) {
        if (pSQL == null)
            pSQL = new PrepareSQL();

        this.account = account;
        this.passwd = passwd;
        this.creator = creator;
        this.role_id = role_id;
        this.area_id = area_id;
        this.map = _map;
        insertIntoDb();
    }

    private void insertIntoDb() {
        pSQL.setSQL(INSERT_USER);
        this.acc_oid = DataSetBean.getMaxId("tab_accounts", "acc_oid");
        this.creationDate = new Date();
        this.lastLoginDate = this.creationDate;
        pSQL.setLong(1, acc_oid);
        pSQL.setString(2, account);
        // AHDX_ITMS-REQ-20180423YQW-001（WEB登录密码加密存储改造） 
        if (LipossGlobals.inArea(Global.AHDX)) {
        	pSQL.setString(2, EncryptionUtil.encryption(passwd));
        }
        else {
        	pSQL.setString(2, passwd);
        }
        pSQL.setLong(4, creator);
        pSQL.setDate(5, creationDate);
        pSQL.setDate(6, lastLoginDate);

        ArrayList list = new ArrayList();
        list.add(pSQL.getSQL());
        list.add(getInsertMapOfSQL());
        list.add(getInsertRoleIdOfSQL());
        list.add(getSaveAreasOfSQL(INSERT_OPER));

        String[] arr_SQL = (String[]) list.toArray();
        list.clear();
        DataSetBean.doBatch(arr_SQL);
    }

    /**
     * 编辑用户账号和资料等相关信息后，将更新的信息保存到数据库中。
     * 
     */
    public void saveIntoDb() {
        pSQL.setSQL(UPDATE_USER);
        pSQL.setString(1, passwd);
        pSQL.setDate(2, lastLoginDate);
        pSQL.setLong(3, acc_oid);

        ArrayList list = new ArrayList();
        list.add(pSQL.getSQL());
        list.add(getUpdateMapOfSQL());
        list.add(getUpdateRoleIdOfSQL());
        list.add(getSaveAreasOfSQL(UPDATE_OPER));

        String[] arr_SQL = (String[]) list.toArray();
        list.clear();
        DataSetBean.doBatch(arr_SQL);
    }

    /**
     * 用户修改密码后保存到数据库中
     * 
     */
    public void modifyPassWd() {
        pSQL.setSQL(UPDATE_USER);
        pSQL.setString(1, passwd);
        pSQL.setDate(2, lastLoginDate);
        pSQL.setLong(3, acc_oid);

        DataSetBean.executeUpdate(pSQL.getSQL());
    }
    /**
     * 用户设置初始化topo层，将parentid值保存到数据库中
     * @return
     */    
    public int modifyParentID(){
        pSQL.setSQL(UPDATE_PARENT_ID);
        pSQL.setString(1, parentid);
        pSQL.setLong(2, acc_oid);
        
        return DataSetBean.executeUpdate(pSQL.getSQL());
    }

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void loadFromDb_CAS() throws UserNotFoundException
	{
		logger.debug("account==========>" + account);
		pSQL.setSQL(LOAD_USER_BY_ACCANDAREA_JS_CAS);
		pSQL.setString(1, account);
		Map fields = DataSetBean.getRecord(pSQL.getSQL());
		if (fields != null)
		{
			this.account = String.valueOf(fields.get("enName"));
			this.city_id = String.valueOf(fields.get("city_id"));
			this.iorMap = loadIorMap();
			this.map =  new HashMap();
			this.map.put("per_city", this.city_id);
		}
		else
		{
			if (account == null)
			{
				throw new UserNotFoundException("Failed to read user with ID " + acc_oid
						+ " from database.");
			}
			throw new UserNotFoundException("Failed to read user " + account
					+ " from database.");
		}
	}

    private void loadFromDb() throws UserNotFoundException {
        logger.debug("account==========>" + account);
        if (account != null) {
            // teledb
            if (DBUtil.GetDB() == Global.DB_MYSQL) {
                LOAD_USER_BY_ACCANDAREA = "select acc_oid, acc_loginname, acc_password, creator, passwordminlenth, creationdate, " +
                        "acc_last_login, init_page, parentid from tab_accounts where acc_loginname=? "
                        + "and acc_oid in (select acc_oid from tab_acc_area where area_id=?)";
            }
            pSQL.setSQL(LOAD_USER_BY_ACCANDAREA);
            pSQL.setString(1, account);
            pSQL.setLong(2, area_id);
        } else {
            // teledb
            if (DBUtil.GetDB() == Global.DB_MYSQL) {
                LOAD_USER_BY_ID = "select acc_oid, acc_loginname, acc_password, creator, passwordminlenth, creationdate, " +
                        "acc_last_login, init_page, parentid from tab_accounts where acc_oid=?";
            }
            pSQL.setSQL(LOAD_USER_BY_ID);
            pSQL.setLong(1, acc_oid);
        }
        Map fields = DataSetBean.getRecord(pSQL.getSQL());
        if (fields != null) {
            this.acc_oid = Long.parseLong((String) fields.get("acc_oid"));
            this.account = (String) fields.get("acc_loginname");
            this.passwd = (String) fields.get("acc_password");
            this.creator = Long.parseLong((String) fields.get("creator"));
            //设置密码长度
            setMinPwdLength((String) fields.get("passwordminlenth"));
            DateTimeUtil dtUtil = new DateTimeUtil((String) fields
                    .get("creationdate"));
            this.creationDate = dtUtil.getDateTime();
            dtUtil = new DateTimeUtil((String) fields.get("acc_last_login"));
            this.lastLoginDate = dtUtil.getDateTime();
            this.init_page = (String) fields.get("init_page");
            this.parentid = (String) fields.get("parentid");
            this.map = loadMapFromDb();
            this.city_id = String.valueOf(map.get("per_city"));
            this.custom_id = String.valueOf(map.get("per_customid"));
            this.role_id = loadRoleIdFromDb();
            this.area_id = loadAreaListFromDb();
            this.iorMap = loadIorMap();
		} else {
			if (account == null) {
				throw new UserNotFoundException("Failed to read user with ID "
						+ acc_oid + " from database.");
			}
			throw new UserNotFoundException("Failed to read user " + account
					+ " from database.");
		}
    }

    public long getId() {
        return acc_oid;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public void setCreator(long creator) {
        this.creator = creator;
    }
    
    public void setMinPwdLength(String length){
    	this.minPwdLength = length;
    	if(null==length||"".equals(length))
    	{
    		this.minPwdLength =null;
    	}
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public void setLastLoginDate(Date acc_last_login) {
        this.lastLoginDate = acc_last_login;
    }

    public String getPasswd() {
        return passwd;
    }

    public long getCreator() {
        return creator;
    }
    
    public String getMinPwdLength(){
    	return minPwdLength;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public Date getLastLoginDate() {
        return lastLoginDate;
    }

    public void setInitPage(String init_page) {
        this.init_page = init_page;
    }

    public String getInitPage() {
        return init_page;
    }

    public long getAreaId() {
        return area_id;
    }

    public int setParentID(String parentid) {
        this.parentid = parentid;
        return modifyParentID();
    }
    public String getParentID() {
        return this.parentid;
    }
    private long loadAreaListFromDb() {
        // teledb
        if (DBUtil.GetDB() == Global.DB_MYSQL) {
            LOAD_AREAS_BY_ID = "select area_id from tab_acc_area where acc_oid=?";
        }
        pSQL.setSQL(LOAD_AREAS_BY_ID);
        pSQL.setLong(1, acc_oid);
        Map fields = DataSetBean.getRecord(pSQL.getSQL());

        long re = 0L;
        if (fields != null)
            re = Long.parseLong((String) fields.get("area_id"));

        return re;
    }
    
     //add by lizhaojun 将所有的ior信息存放到内存当中
    private Map loadIorMap(){
    	Map iorMap = new HashMap();
        // teledb
        if (DBUtil.GetDB() == Global.DB_MYSQL) {
            LOAD_IOR = "select object_name, object_poa, ior from tab_ior";
        }
    	pSQL.setSQL(LOAD_IOR);
    	Cursor cursor = DataSetBean.getCursor(pSQL.getSQL()); 
    	Map fields = cursor.getNext();
    	if(fields != null){
    		while(fields != null){
    			String object_name= (String)fields.get("object_name");
    			String object_poa= (String)fields.get("object_poa");
    			String ior = (String)fields.get("ior");
    			String mapKey = object_name + "," + object_poa;
    			iorMap.put(mapKey,ior);
    			fields = cursor.getNext();   			
    		}
    		
    	}
    	logger.debug("===========================iorMap==========================" + iorMap.size());
    	return iorMap;
    }

    public long getRoleId() {
        return role_id;
    }

    // 获得当前用户的角色数组 -----> add by lizhaojun 2006-12-8
    // 原因：一个用户可以对应多个角色。
    public long[] getRole_Id() {
        // teledb
        if (DBUtil.GetDB() == Global.DB_MYSQL) {
            LOAD_ROLE_BY_ID = "select role_id from tab_acc_role where acc_oid=? order by role_id";
        }

        pSQL.setSQL(LOAD_ROLE_BY_ID);
        pSQL.setLong(1, acc_oid);
        Cursor cursor = DataSetBean.getCursor(pSQL.getSQL());
        Map fields = cursor.getNext();
        int n = cursor.getRecordSize();
        roleId = new long[n];
        if (fields != null) {
            for (int i = 0; i < cursor.getRecordSize(); i++) {
                roleId[i] = Long.parseLong((String) fields.get("role_id"));
                logger.debug("roleId[" + i + "] ====" + roleId[i]);
                fields = cursor.getNext();
            }
        }
        return roleId;
    }

    private long loadRoleIdFromDb() {
        long re = 0;
        // teledb
        if (DBUtil.GetDB() == Global.DB_MYSQL) {
            LOAD_ROLE_BY_ID = "select role_id from tab_acc_role where acc_oid=? order by role_id";
        }
        pSQL.setSQL(LOAD_ROLE_BY_ID);
        pSQL.setLong(1, acc_oid);
        Map fields = DataSetBean.getRecord(pSQL.getSQL());
        if (fields != null) {
            re = Long.parseLong((String) fields.get("role_id"));
        }
        return re;
    }

    public void setAreaId(long area_id) {
        this.area_id = area_id;
    }

    private String getSaveAreasOfSQL(int type) {
        if (type == INSERT_OPER) {
            pSQL.setSQL(INSERT_AREA);
        } else if (type == UPDATE_OPER) {
            pSQL.setSQL(UPDATE_AREA);
        }

        pSQL.setLong(1, area_id);
        pSQL.setLong(2, acc_oid);

        return pSQL.getSQL();
    }

    public void setRole(long role_id) {
        this.role_id = role_id;
    }

    private String getInsertRoleIdOfSQL() {
        pSQL.setSQL(INSERT_ROLE);
        pSQL.setLong(1, acc_oid);
        pSQL.setLong(2, role_id);

        return pSQL.getSQL();
    }

    private String getUpdateRoleIdOfSQL() {
        pSQL.setSQL(UPDATE_ROLE);
        pSQL.setLong(1, role_id);
        pSQL.setLong(2, acc_oid);

        return pSQL.getSQL();
    }

    public Map getUserInfo() {
        return this.map;
    }

    private Map loadMapFromDb() {
        // teledb
        if (DBUtil.GetDB() == Global.DB_MYSQL) {
        	if(LipossGlobals.isGSDX()) {
        		LOAD_USERINFO_BY_ID = "select per_city from tab_persons where per_acc_oid=?";
        	}else {
        		LOAD_USERINFO_BY_ID = "select per_city, per_customid from tab_persons where per_acc_oid=?";
        	}
        }
        pSQL.setSQL(LOAD_USERINFO_BY_ID);
        pSQL.setLong(1, acc_oid);
        return DataSetBean.getRecord(pSQL.getSQL());
    }

    public void setUserInfo(Map _map) {
        this.map = _map;
    }

    private String getInsertMapOfSQL() {
        if (map == null)
            return "";
        pSQL.setSQL(INSERT_USERINO);

        pSQL.setLong(1, acc_oid);
        pSQL.setString(2, (String) map.get("per_searchcode"));
        pSQL.setString(3, (String) map.get("per_name"));
        pSQL.setString(4, (String) map.get("per_lastname"));
        pSQL.setString(5, (String) map.get("per_gender"));
        pSQL.setString(6, (String) map.get("per_title"));
        pSQL.setString(7, (String) map.get("per_jobtitle"));
        pSQL.setString(8, (String) map.get("per_phone"));
        pSQL.setString(9, (String) map.get("per_mobile"));
        pSQL.setString(10, (String) map.get("per_email"));
        pSQL.setInt(11, Integer.parseInt((String) map.get("per_dep_oid")));
        pSQL.setString(12, (String) map.get("per_remark"));
        pSQL.setDate(13, (Date) map.get("per_birthdate"));
        pSQL.setString(14, (String) map.get("per_city"));

        return pSQL.getSQL();
    }

    private String getUpdateMapOfSQL() {
        if (map == null)
            return "";
        pSQL.setSQL(UPDATE_USERINFO);

        pSQL.setString(1, (String) map.get("per_searchcode"));
        pSQL.setString(2, (String) map.get("per_name"));
        pSQL.setString(3, (String) map.get("per_lastname"));
        pSQL.setString(4, (String) map.get("per_gender"));
        pSQL.setString(5, (String) map.get("per_title"));
        pSQL.setString(6, (String) map.get("per_jobtitle"));
        pSQL.setString(7, (String) map.get("per_phone"));
        pSQL.setString(8, (String) map.get("per_mobile"));
        pSQL.setString(9, (String) map.get("per_email"));
        pSQL.setInt(10, Integer.parseInt((String) map.get("per_dep_oid")));
        pSQL.setString(11, (String) map.get("per_remark"));
        pSQL.setDate(12, (Date) map.get("per_birthdate"));
        pSQL.setString(13, (String) map.get("per_city"));
        pSQL.setLong(14, acc_oid);

        return pSQL.getSQL();
    }

    public static void main(String[] args) {
        try {
            DbUser user = new DbUser("admin", 1);
            logger.debug("role_id: " + user.getCustomId());
            System.exit(0);
        } catch (UserNotFoundException e) {
            // TODO 自动生成 catch 块
            e.printStackTrace();
        }
    }

    public String getCityId() {
        return this.city_id;
    }
    // add by lizhaojun 获取当前的ior信息
    public String getIor(String object_name,String object_poaname){
    	String strIor = "";
    	String tmpKey = object_name+"," + object_poaname;
    	strIor = (String)iorMap.get(tmpKey);
    	return strIor;   	
    }

    public String getCustomId() {
        return this.custom_id;
    }
    
    	/**
	 * 根据登陆用户获取角色信息
	 * add wangfeng
	 * @return List （用户角色列表）
	 */
	private List loadRoleIdFromDb2() {
		ArrayList result = new ArrayList();
        // teledb
        if (DBUtil.GetDB() == Global.DB_MYSQL) {
            LOAD_ROLE_BY_ID = "select role_id from tab_acc_role where acc_oid=? order by role_id";
        }
		pSQL.setSQL(LOAD_ROLE_BY_ID);
		pSQL.setLong(1, acc_oid);
		Cursor cursor = DataSetBean.getCursor(pSQL.getSQL());
		logger.debug("size" + cursor.getRecordSize());
		Map fields = cursor.getNext();
		while (fields != null) {
			result.add(fields.get("role_id"));
			fields = cursor.getNext();
		}
		return result;
	}
	public List getRole_list() {
		return loadRoleIdFromDb2();
	}
	public boolean isAdmin(){
        //根据域id是否等于1作为管理员判断标志
	    return this.area_id == 1 ? true : false;
    }
}
 