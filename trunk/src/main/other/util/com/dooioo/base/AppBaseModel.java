package com.dooioo.base;

import com.dooioo.util.AppUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;
import java.util.TreeSet;

/**
 * 为所有的实体操作类基类，提供更新时必要的操作控制和创建时的公用字段信息
 * @author liguohui
 *
 */
public abstract class AppBaseModel implements Serializable {

	/**
	 * 表示已删除
	 */
	public static final int DELETED = 1;
	/**
	 * 表示未删除
	 */
	public static final int DELETED_NO = 0;

	/**
	 * 正向标记, xxxFlag 域使用时表达正向意义
	 */
	public static final int FLAG_YES = 1;

	/**
	 * 负向标记, xxxFlag 域使用时表达负向意义
	 */
	public static final int FLAG_NO = 0;
	
	/**
	 * 创建人
	 */
	private Integer creator;
	/**
	 * 创建人名称
	 */
	private String creatorName;
	
	/**
	 * 更新人
	 */
	private Integer updator;
	/**
	 * 更新人名称
	 */
	private String updatorName;
	/**
	 * 删除标记，0未删除，1已删除
	 */
	private int deleteFlag = 0;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 更新时间
	 */
	private Date updateTime;
	/**
	 * 总页数
	 */
	private Integer pageSize;
	/**
	 * 当前页
	 */
	private Integer pageNo;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -268422548146191491L;
	/**
	 * @return the creator
	 */
	public Integer getCreator() {
		return creator;
	}

	/**
	 * @param creator the creator to set
	 */
	public void setCreator(Integer creator) {
		this.creator = creator;
		this.addField("creator");
	}

	/**
	 * @return the updator
	 */
	public Integer getUpdator() {
		return updator;
	}

	/**
	 * @param updator the updator to set
	 */
	public void setUpdator(Integer updator) {
		this.updator = updator;
		this.addField("updator");
	}

	/**
	 * @return the deleteFlag
	 */
	public Integer getDeleteFlag() {
		return deleteFlag;
	}

	/**
	 * @param deleteFlag the deleteFlag to set
	 */
	public void setDeleteFlag(int deleteFlag) {
		this.deleteFlag = deleteFlag;
		this.addField("deleteFlag");
	}

	/**
	 * @return the createTime
	 */
	public Date getCreateTime() {
		return createTime;
	}

	/**
	 * @param createTime the createTime to set
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
		this.addField("createTime");
	}

	/**
	 * @return the creatorName
	 */
	public String getCreatorName() {
		return creatorName;
	}

	/**
	 * @param creatorName the creatorName to set
	 */
	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
		this.addField("creatorName");
	}

	/**
	 * @return the updatorName
	 */
	public String getUpdatorName() {
		return updatorName;
	}

	/**
	 * @param updatorName the updatorName to set
	 */
	public void setUpdatorName(String updatorName) {
		this.updatorName = updatorName;
		this.addField("updatorName");
	}

	/**
	 * @return the updateTime
	 */
	public Date getUpdateTime() {
		return updateTime;
	}

	/**
	 * @param updateTime the updateTime to set
	 */
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
		this.addField("updateTime");
	}
	

	/**
	 * 公司编码
	 */
	private Integer companyId;

	/**
	 * 设置公司编码
	 */
	public void setCompanyId(Integer companyId) {
		addField("companyId");
		this.companyId = companyId;
	}

	/**
	 * 公司编码
	 */
	public Integer getCompanyId() {
		return this.companyId;
	}

	/**
	 * 子公司
	 */
	private String subCompany;

	/**
	 * 设置子公司
	 */
	public void setSubCompany(String subCompany) {
		addField("subCompany");
		this.subCompany = subCompany;
	}

	/**
	 * 子公司
	 */
	public String getSubCompany() {
		return this.subCompany;
	}

	/**
	 * @return the fields
	 */
	public Set<String> getFields() {
		return fields;
	}

	/**
	 * @param fields the fields to set
	 */
	public void setFields(Set<String> fields) {
		this.fields = fields;
	}

	/**
	 * 需要更新的字段集合
	 */
	private Set<String> fields = new TreeSet<>();

	/**
	 * 更新时，避免不必要的更新，比如数据库有10个字段，页面仅关心两个字段，某次操作仅对两个字段进行更新；
	 * 系统要求都用到此方法处理
	 * @param field 字段名
	 */
	public void addField(String field) {
		fields.add(field);
	}

	/**
	 * 检查更新字段是否包括参数值
	 * @param field 字段的名字
	 * @return 包含返回 true, 否则返回 false
	 */
	public boolean contains(String field) {
		return !AppUtils.isEmpty(field) && fields.contains(field);
	}

	public AppBaseModel getMe() {
		return this;
	}

	/**
	 * 返回实体数据的唯一标识
	 */
	public abstract Serializable getEntityId();


	public Integer getPageNo() {
		return pageNo;
	}

	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
}
