package com.dooioo.base;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * Title: [子系统名称]_[模块名]
 * </p>
 * <p>
 * Description: dao公用类
 * </p>
 * 
 * @author Jack Lee
 * @version v1.0
 * @author (lastest modification by Jack Lee)
 * @since 2015年10月26日下午5:28:31
 */
public interface AppBaseDao<T> {
	/**
	 * <p>
	 * Description:新增一条数据
	 * </p>
	 * 
	 * @param t 保存数据model
	 * @return 保存记录数
	 */
	int save(T t);

	/**
	 * <p>
	 * Description:修改一条数据，必须含有id
	 * </p>
	 * 
	 * @param t 更新数据model
	 * @return 更新记录数
	 */
	int update(T t);

	/**
	 * <p>
	 * Description:根据id删除一条数据
	 * </p>
	 * 
	 * @param t 删除数据model
	 */
	void delete(T t);

	List<T> findWithPage(Map<String, Object> where, PageBounds pageBounds);

	T get(Serializable id);
//
//	List<T> getBySelective(T t);
//
//	List<T> getBySelective(Map<String, Object> where);

}
