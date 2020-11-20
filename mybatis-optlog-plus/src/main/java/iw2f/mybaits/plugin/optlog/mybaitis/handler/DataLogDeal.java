package iw2f.mybaits.plugin.optlog.mybaitis.handler;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import iw2f.mybaits.plugin.optlog.LogContext;
import iw2f.mybaits.plugin.optlog.mybaitis.bo.CompareResult;
import iw2f.mybaits.plugin.optlog.mybaitis.bo.OptBo;
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
		sb.append("插入 ");
		sb.append(insertInfo.getBasicInfo().getTbName());
		sb.append(" 表 ");
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
		List<List<CompareResult>> cr = updateInfo.getCompareResult();
		StringBuilder sb = new StringBuilder();
		sb.append("更新 ");
		sb.append(updateInfo.getBasicInfo().getTbName());
		sb.append(" 表 ");
		OptBo opt = new OptBo();
		List<List<String>> updateer = new ArrayList<List<String>>();
		List<String> no1 = new ArrayList<String>();
		for (int i = 0; i < cr.size(); i++) {
			List<CompareResult> e = cr.get(i);
			sb.append((i + 1) + "  ");
			no1.clear();
			e.forEach(r -> {
				String s = "把《" + r.getFieldComment() + "》从<" + r.getOldValue() + ">改成<" + r.getNewValue() + ">";
				no1.add(s);
			});
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
		sb.append("删除 ");
		sb.append(deleteInfo.getBasicInfo().getTbName());
		sb.append(" 表 ");
		OptBo opt = new OptBo();
		opt.setData(deleteInfo.getDeleteObj());
		opt.setDesc(sb.toString());
		LogContext.optLog().getOpts().add(opt);
		logger.debug("          " + sb.toString());
	}
}