package iw2f.mybaits.plugin.optlog.mybaitis.bo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OptLogBo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String logId;// 日志ID

	private String userId;// 用户名字

	private String userName;// 用户名字

	private String userIp;// 用户IP

	private String method;// GET POST

	private String jmethodDesc;// 方法描述

	private String jmethod; // 请求方法

	private String param;

	private Date createTime;// 记录日期

	private String requestUrl; // 请求路径

	private int responseStatus; // 响应状态码

	private List<OptBo> opts = new ArrayList<OptBo>();// 操作

}