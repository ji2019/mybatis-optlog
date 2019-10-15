package iw2f.mybaits.plugin.optlog.mybaitis.handler;

/**
 * 
 * @Description 数据日志处理
 * @author wangjc
 * @date 2019年10月11日 下午2:47:01
 */
public interface DataLogHandler {

	/**
	 * 插入处理
	 * 
	 * @Description 插入处理
	 * @author wangjc
	 * @return void
	 * @param insertInfo
	 *            插入数据信息
	 * @date 2019年10月11日 下午5:37:32
	 */
	void insertHandler(InsertInfo insertInfo);

	/**
	 * 
	 * @Description 更新处理
	 * @author wangjc
	 * @return void
	 * @param updateInfo
	 *            更新数据信息
	 * @date 2019年10月11日 下午5:37:56
	 */
	void updateHandler(UpdateInfo updateInfo);

	/**
	 * 
	 * @Description 删除处理
	 * @author wangjc
	 * @return void
	 * @param deleteInfo
	 *            删除数据信息
	 * @date 2019年10月11日 下午5:38:18
	 */
	void deleteHandler(DeleteInfo deleteInfo);
}