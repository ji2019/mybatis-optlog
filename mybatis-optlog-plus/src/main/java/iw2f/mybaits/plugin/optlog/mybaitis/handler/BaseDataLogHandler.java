package iw2f.mybaits.plugin.optlog.mybaitis.handler;

import java.sql.Timestamp;
import java.util.*;

import iw2f.mybaits.plugin.optlog.mybaitis.bo.EditBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import iw2f.mybaits.plugin.optlog.mybaitis.bo.CompareResult;
import iw2f.mybaits.plugin.optlog.mybaitis.bo.FieldInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 
 * @Description 数据日志基础信息及处理
 * @author wangjc
 * @date 2019年10月11日 下午3:04:09
 *
 */
@Getter
@AllArgsConstructor
public abstract class BaseDataLogHandler {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * 数据基础信息
	 */
	private BasicInfo basicInfo;

	/**
	 * 
	 * @Description 对比两个对象
	 * @author wangjc
	 * @return List<List<CompareResult>>
	 * @param oldObjs
	 *            旧对象
	 * @param newObj
	 *            新对象
	 * @return
	 * @throws IllegalAccessException
	 * @date 2019年10月11日 下午3:02:59
	 */
	protected List<EditBo> compareTowObject(List<Map<String, Object>> oldObjs, Map<String, Object> newObj) {
		List<Map<String,Object>> primaryKey = new ArrayList<Map<String,Object>>();

		List<EditBo> editBoList = new ArrayList<>();
		for (Map<String, Object> oldObj : oldObjs) {
			EditBo editBo = new EditBo();
			// 单个对象比较结果
			List<CompareResult> cor = new ArrayList<>();
			for (String key : newObj.keySet()) {
				String dataType = "";
				// 匹配字段注释
				Optional<FieldInfo> o = this.basicInfo.getFieldInfos().stream().filter(f -> key.equals(f.getJFieldName())).findFirst();
				if (!o.isPresent()) {
					continue;
				}
				FieldInfo fi = o.get();
				dataType = fi.getDataType();
				String fieldName = fi.getFieldName();
				if ("datetime".equals(dataType)) {
					System.out.println(dataType);
				}
				Object o1 = oldObj.get(key);
				Object o2 = convertSqlType(newObj.get(key), dataType);
				if (!compareTwo(o1, o2)) {
					CompareResult r = new CompareResult();
					r.setFieldName(key);
					r.setOldValue(o1);
					r.setNewValue(o2);
					// 匹配字段注释
					// Optional<FieldInfo> o = this.basicInfo.getFieldInfos().stream()
					// .filter(f -> r.getFieldName().equals(f.getJFieldName())).findFirst();
					if (o.isPresent()) {
						r.setFieldComment(fi.getComment());
					}
					cor.add(r);
				}
				if(fi.getPrimaryKey()){
					Map<String,Object> pkObject = new HashMap<String,Object>();
					pkObject.put(fieldName,o1);
					primaryKey.add(pkObject);
				}
			}
			editBo.setPrimaryKey(primaryKey);
			editBo.setModifyField(cor);
			editBoList.add(editBo);
		}
		return editBoList;
	}

	/**
	 * 
	 * @Description 对比两个数据是否内容相同
	 * @author wangjc
	 * @return boolean
	 * @param object1
	 * @param object2
	 * @return
	 * @date 2019年10月11日 下午3:03:46
	 */
	private boolean compareTwo(Object object1, Object object2) {
		if (object1 == null && object2 == null) {
			return true;
		}
		if (object1 == null && object2 != null) {
			return false;
		}
		if (object1.equals(object2)) {
			return true;
		}
		return false;
	}

	private Object convertSqlType(Object obj, String dataType) {
		if ("bigint".equals(dataType)) {
			return Long.parseLong(obj.toString());
		} else if ("int".equals(dataType)) {
			return Integer.parseInt(obj.toString());
		} else if ("datetime".equals(dataType)) {
			if (obj instanceof Timestamp) {
				return obj;
			} else if (obj instanceof Date) {
				return new Timestamp(((Date) obj).getTime());
			} else {
				return obj;
			}
		} else if ("varchar".equals(dataType) || "text".equals(dataType)) {
			String val = obj.toString().substring(1);
			val = val.substring(0, val.length() - 1);
			return val;
		} else {
			logger.info("SqlDataType   ===== 》》    {} ", dataType);
			return obj;
		}
	}
}