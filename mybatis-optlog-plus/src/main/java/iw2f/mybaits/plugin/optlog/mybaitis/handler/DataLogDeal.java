package iw2f.mybaits.plugin.optlog.mybaitis.handler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.mysql.cj.x.protobuf.MysqlxDatatypes;
import iw2f.mybaits.plugin.optlog.mybaitis.bo.EditBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import iw2f.mybaits.plugin.optlog.mybaitis.bo.CompareResult;
import iw2f.mybaits.plugin.optlog.mybaitis.bo.OptBo;
import iw2f.mybaits.plugin.optlog.utils.LogContext;
import lombok.SneakyThrows;

/**
 * 
 * @Description TODO
 * @author wangjc
 * @date 2019年10月11日 下午5:38:48
 *
 */
@Service
public class DataLogDeal implements DataLogHandler {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.gymexpress.histar.logs.mybaitis.DataLogHandler#insertHandler(com.
	 * gymexpress.histar.logs.mybaitis.bo.InsertInfo)
	 */
	@SneakyThrows
	public void insertHandler(InsertInfo insertInfo) {
		StringBuilder sb = new StringBuilder();
		sb.append("插入");
		sb.append(insertInfo.getBasicInfo().getTbName());
		sb.append("表");
		OptBo opt = new OptBo();
		opt.setData(insertInfo.getInsertObj());
		opt.setDesc(sb.toString());
		LogContext.optLog().getOpts().add(opt);
		logger.debug("          " + sb.toString());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.gymexpress.histar.logs.mybaitis.DataLogHandler#updateHandler(com.
	 * gymexpress.histar.logs.mybaitis.bo.UpdateInfo)
	 */
	@SneakyThrows
	public void updateHandler(UpdateInfo updateInfo) {
		List<EditBo> cr = updateInfo.getCompareResult();
		StringBuilder sb = new StringBuilder();
		sb.append("更新");
		sb.append(updateInfo.getBasicInfo().getTbName());
		sb.append("表");
		sb.append(cr.size() + "条数据");
		OptBo opt = new OptBo();
		List<List<String>> updateer = new ArrayList<List<String>>();
		for (int i = 0; i < cr.size(); i++) {
			List<String> no1 = new ArrayList<String>();
			EditBo editBo = cr.get(i);
			List<CompareResult> e = editBo.getModifyColumns();
			no1.clear();
			StringBuilder modifyContent = new StringBuilder("ModifyContent:");
			for(Iterator<CompareResult> it = e.iterator();it.hasNext();){
				CompareResult r = it.next();
				modifyContent.append("把《" + r.getFieldComment() + "》从<" + r.getOldValue() + ">改成<" + r.getNewValue() + ">");
				if(it.hasNext())
					modifyContent.append(",");
			}
			no1.add(modifyContent.toString());
			Map<String,Object> primaryKeys = editBo.getPrimaryKeys();
			no1.add("PrimaryKeys:"+ JSON.toJSONString(primaryKeys));
			updateer.add(no1);
		}
		opt.setData(updateer);
		opt.setDesc(sb.toString());
		LogContext.optLog().getOpts().add(opt);
		logger.debug("          " + sb.toString());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.gymexpress.histar.logs.mybaitis.DataLogHandler#deleteHandler(com.
	 * gymexpress.histar.logs.mybaitis.bo.DeleteInfo)
	 */
	@SneakyThrows
	public void deleteHandler(DeleteInfo deleteInfo) {
		StringBuilder sb = new StringBuilder();
		sb.append("删除");
		sb.append(deleteInfo.getBasicInfo().getTbName());
		sb.append("表");
		OptBo opt = new OptBo();
		opt.setData(deleteInfo.getDeleteObj());
		opt.setDesc(sb.toString());
		LogContext.optLog().getOpts().add(opt);
		logger.debug("          " + sb.toString());
	}
}