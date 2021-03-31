package bio.hgwip;

public interface IPGlobal
{
	// 参数错误
	public static final int PARAM_ERROR = -1;

	// 成功
	public static final int SUNCCESS = 0;

	// 地址存在
	public static final int SUBNET_EXIST = -2;

	// 数据库操作失败
	public static final int DBOPERATION_FAIL = -3;

	// 省局用户
	public static final int SZX_USER = 0;

	// 地市用户
	public static final int CITY_USER = 1;

	/**
	 * 分配状态
	 */
	public static final int NOT_ASSIGN = 0;

	public static final int ASSIGN_TO_CITY = 1;

	public static final int ASSIGN_TO_USER = 3;

	public static final int ASSIGN_TO_NET = 4;

	public static final int WAIT_APPROVE = 5;

	/**
	 * 邮件状态
	 */
	public static final int WAIT_SENDMAIL = 0;

	public static final int WAIT_RECEIVEMAIL = 1;

	public static final int NOT_SENDMAIL = 2;

	public static final int REGISTER_SUCCESS = 3;

	public static final int REGISTER_FAIL = 4;

	public static final int UNREGISTER_SUCCESS = 5;

	public static final int UNREGISTER_FAIL = 6;

	/**
	 * 同意状态
	 */
	public static final int AGREE = 0;

	public static final int DISAGREE = 1;

	public static final int NOT_CHECK = 2;

	/**
	 * 地市可以分配的IP地址个数（默认值）
	 */
	public static final int MAX_ASSIGNNUMBER = 16;

	/**
	 * 子网划分限制
	 */
	public static final int MAX_LEVEL = 8;

	public static final int MAX_NETNUMBER = 256;

	// 用户划分子网，指定网段不能划分
	public static final int IS_FORBID = 1;

	// 用户划分子网，划分的层数超过八层，子网数超过256，不允许划分
	public static final int OVER_MAXLEVEl = 2;

	/**
	 * 用户信息导入方式
	 */
	public static final int HAND_IMPORT = 0;

	public static final int FILE_IMPORT = 1;

	/**
	 * 上传文件路径
	 */
	public static final String FILE_PATH = "/opt/bak";

	/**
	 * 上传文件子路径:网络IP申请
	 */
	public static final String FILE_PATH_NET = FILE_PATH + "/ip";

	/**
	 * 上传文件子路径:专线用户IP申请
	 */
	public static final String FILE_PATH_SPL = FILE_PATH + "/spl";
	
	/**
	 *用途为这个的网段，地市用户不能对其进行操作
	 */
	public static final String NET_PURPOSE="网络";	
}
