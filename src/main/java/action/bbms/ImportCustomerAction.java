package action.bbms;

import java.io.File;

import com.opensymphony.xwork2.ActionSupport;

import dao.bbms.ImportCustomerDAO;

/**
 * 客户资料导入
 * 
 * @author 陈仲民（5243）
 * @version 1.0
 * @since 2008-6-3
 * @category 资源管理
 */
@SuppressWarnings("unchecked")
public class ImportCustomerAction extends ActionSupport
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3499965120264821779L;
	//dao
	private ImportCustomerDAO importCustomer;
	//file
	private File customerFile;
	//返回信息
	private String msg;
	public String getMsg()
	{
		return msg;
	}
	public void setMsg(String msg)
	{
		this.msg = msg;
	}
	public void setImportCustomer(ImportCustomerDAO importCustomer)
	{
		this.importCustomer = importCustomer;
	}
	public void setCustomerFile(File customerFile)
	{
		this.customerFile = customerFile;
	}
	/**
	 * 
	 */
	public String execute() throws Exception
	{
		int ret = importCustomer.importData(customerFile);
		if (ret == 1){
			msg = "导入客户资料成功！";
		}
		else{
			msg = "导入客户资料失败！";
		}
		return SUCCESS;
	}
	
}
