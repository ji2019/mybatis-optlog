package iw2f.mybaits.plugin.optlog.mybaitis.handler;

import iw2f.mybaits.plugin.optlog.mybaitis.BasicInfo;
import lombok.Getter;

/**
 * 数据删除信息
 *
 */
@Getter
public class DeleteInfo extends BaseDataLogHandler {

	/**
	 * 删除对象
	 */
	private Object deleteObj;

	public DeleteInfo(BasicInfo basicInfo, Object deleteObj) {
		super(basicInfo);
		this.deleteObj = deleteObj;
	}

}