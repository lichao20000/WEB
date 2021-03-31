package action.splitpage;

import java.util.Iterator;
import java.util.Map;

import org.apache.struts2.dispatcher.HttpParameters;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;


/**
 * 用于查询翻页功能的拦截器，解析并传递查询需要的参数
 * @author 陈仲民（5243）
 * @version 1.0
 * @version 2.0 支持传递数组型参数
 * @since 2007-12-20
 */

public class SplitPageInterceptor extends AbstractInterceptor {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3425927181038784176L;
	
	//参数间分隔符
	private String code1 = "_a_a_";
	
	//参数与值的分割符
	private String code2 = "_b_b_";
	
	private String code3 = String.valueOf((char)3);

	/**
	 * 查询参数拦截器
	 * @param invocation
	 */
	public String intercept(ActionInvocation invocation) throws Exception {
		
		//获取当前请求的action
		Object action = invocation.getAction();
		
		//获取当前请求的参数
		HttpParameters parameters = invocation.getInvocationContext().getParameters();
		Map paramMap = parameters.toMap();
		
		//必须是实现接口SplitPageInterface的方法才处理
		if (action instanceof splitPageAction){
			
			//如果有参数paramList_splitPage则解析，没有则将其他参数连成一串放入paramList_splitPage
			if (paramMap.containsKey("paramList_splitPage")){
				String[] splitParam = (String[])paramMap.get("paramList_splitPage");
				
				//解析参数字符串
				if (splitParam != null && splitParam.length > 0){
					
					//参数之间是以code1分割的，解析成参数数组paramList
					String[] paramList = splitParam[0].split(code1);
					
					if (paramList != null && paramList.length > 0){
						int len = paramList.length;
						
						//具体参数信息，包括参数名和参数值
						String[] param = null;
						
						for (int i=0;i<len;i++){
							if (paramList[i] != null && !"".equals(paramList[i])){
								
								//参数名和参数值是以code2分割的
								param = paramList[i].split(code2);
								
								if (param != null && param.length > 1){
									
									//若有“,”则表示是数组类型的参数
									if (param[1].indexOf(",") != -1){
										paramMap.put(param[0], param[1].split(","));
									}
									else{
										paramMap.put(param[0], param[1]);
									}
								}
							}
						}
					}
				}
			}
			else{
				//临时字符串
				String[] tmp = new String[1];
				tmp[0] = "";
				
				
				
				//循环取出参数
				for (Iterator iterator = paramMap.entrySet().iterator(); iterator.hasNext();) {
					//取值
					Map.Entry entry = (Map.Entry) iterator.next();
					String name = entry.getKey().toString();
					String[] value = (String[])paramMap.get(name);
					
					//参数名不是curPage_splitPage、num_splitPage、maxPage_splitPage、paramList_splitPage的全部取出来存到paramList_splitPage中
					if (!"curPage_splitPage".equals(name) && !"num_splitPage".equals(name) 
							&& !"maxPage_splitPage".equals(name) && !"paramList_splitPage".equals(name)
							&& !"pageIndex_splitPage".equals(name)){
						
						//将参数组合成字符串
						tmp[0] += name + code2 + value[0];
						
						//若参数为字符串则用逗号分割
						if (value.length > 1){
							int len = value.length;
							for (int j=1;j<len;j++){
								tmp[0] += "," + value[j];
							}
						}
						tmp[0] += code1;
					}
				}
				//将参数放入堆栈
				paramMap.put("paramList_splitPage", tmp);
			}
			
		}
		
		//将参数map放入堆栈中
		// struts2由2.3.X ==>2.5.26后 需要将传参类型转换 2020/12/11 by zhangyu25
		invocation.getInvocationContext().setParameters(parameters.create(paramMap).build());
		
		return invocation.invoke();
	}

}
