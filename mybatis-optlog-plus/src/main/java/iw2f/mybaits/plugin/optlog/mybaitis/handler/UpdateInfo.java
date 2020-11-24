package iw2f.mybaits.plugin.optlog.mybaitis.handler;

import java.util.List;
import java.util.Map;

import iw2f.mybaits.plugin.optlog.mybaitis.bo.CompareResult;
import iw2f.mybaits.plugin.optlog.mybaitis.bo.EditBo;
import lombok.Getter;

/**
 * 
 * @Description 数据更新信息
 * @author wangjc
 * @date 2019年10月12日 上午11:37:33
 *
 */
@Getter
public class UpdateInfo extends BaseDataLogHandler {

	/**
	 * 更新前对象
	 */
	private List<Map<String, Object>> oldObj;
	/**
	 * 更新对象
	 */
	private Map<String, Object> newObj;

	public UpdateInfo(BasicInfo basicInfo, List<Map<String, Object>> oldObj, Map<String, Object> newObj) {
		super(basicInfo);
		this.oldObj = oldObj;
		this.newObj = newObj;
	}

	public List<EditBo> getCompareResult() throws IllegalAccessException {
		return compareTowObject(this.oldObj, this.newObj);
	}
}