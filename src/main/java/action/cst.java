package action;

/**
 * 
 * @author 王志猛(5194) tel:13701409234
 * @version 1.0
 * @since 2007-08-29
 * @category 常量定义接口，其他action应该使用静态导入该actin接口，使用该接口中定义的常量
 */
public interface cst
{
	/** **************action code定义*************************** */
	/**
	 * 增加code
	 */
	static final String ADD = "add";
	/**
	 * 跳转到添加页面
	 */
	static final String TADD="tadd";
	/**
	 * 跳转到编辑页面
	 */
	static final String TEDIT="tedit";
	/**
	 * 导出EXCEL
	 */
	static final String PRINT = "print";
	/**
	 * 编辑code
	 */
	static final String EDIT = "edit";
	/**
	 * 操作成功code
	 */
	static final String OK = "ok";
	/**
	 * ajax操作code
	 */
	static final String AJAX = "ajax";
	/**
	 * 删除code
	 */
	static final String DEL = "del";
	/**
	 * 操作失败code
	 */
	static final String FAIL = "fail";
	/**
	 * 重载code
	 */
	static final String RELOAD = "reload";
	/** **************其他action使用的常量定义*************************** */
	
	static final String DETAIL = "detail";
	
	static final String LIST = "list";
}
