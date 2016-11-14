package com.dooioo.monitor.job;


/**
 * 自定义协议
 * @author dingxin
 *
 */
public interface IProtocol {
	
	static final String SUCCESS = "success";
	
	static final String FAIL = "fail";
	
	static final String YES = "yes";
	
	static final String NO = "no";
	
	static final String CRLF = "\r\n";
	
	/**
	 * 自定义协议信息主体前缀
	 * S-C 服务端向客户端 C-S客户端向服务端
	 * @author dingxin
	 *
	 */
	enum IProtocolPrefix{
		/**
		 * 连接   C->S
		 */
		Connect(2001,"连接"),
		/**
		 * 获取Job List  S->C
		 */
		JobList(2002,"获取Job List"),
		/**
		 * 修改Job  S->C
		 */
		ModifyJob(2003,"修改Job"),
		/**
		 * 请求AppCode  S->C
		 */
		AskAppCode(2004,"请求AppCode"),
//		/**
//		 * 回复是否可以运行 S－>C
//		 */
//		ReplyRun(2005,"回复是否可以运行"),
//		/**
//		 * 客户端节点是否运行JOB C->S
//		 */
//		WhetherRun(2006,"是否运行"),
		/**
		 * 非命令回复，提示性质回复 S<->C
		 */
		ReplyTip(2007,"回复"),
		/**
		 * 终止JOB 
		 */
		SuspendJob(2008,"终止JOB"),
		/**
		 * 恢复JOB
		 */
		ResumeJob(2009,"恢复JOB")
		;
		
		private int code;
		private String remark;
		
		IProtocolPrefix(int code,String remark){
			this.code = code;
			this.remark = remark;
		}

		/**
		 * @return the code
		 */
		public int getCode() {
			return code;
		}

		/**
		 * @return the remark
		 */
		public String getRemark() {
			return remark;
		}
		
		public static IProtocolPrefix getIProtocolPrefixByCode(int code){
			IProtocolPrefix[] iProtocolPrefixs = IProtocolPrefix.values();
			for(IProtocolPrefix iProtocolPrefix :iProtocolPrefixs){
				if(iProtocolPrefix.getCode() == code){
					return iProtocolPrefix;
				}
			}
			return null;
		}
		
	}
	
	
	
}
