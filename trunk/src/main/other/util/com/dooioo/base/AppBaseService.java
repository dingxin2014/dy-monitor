package com.dooioo.base;

import com.dooioo.web.common.Paginate;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.github.miemiedev.mybatis.paginator.domain.Paginator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AppBaseService {

    private static final Log logger = LogFactory.getLog(AppBaseService.class);

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
        logger.info("status change:"+this.getClass().getCanonicalName()+"--"+status);
    }

    /**
     * 0表示正常运行，1表示结束运行
     */
    private int status = 0;

    public Paginate createPaginate(int pageIndex, int pageSize, List dataList) {
        Paginate paginate = new Paginate(pageIndex, pageSize);

        if (CollectionUtils.isEmpty(dataList)) {
            paginate.setPageList(Collections.emptyList());
        } else {
            paginate.setPageList(dataList);
        }

        if (dataList instanceof PageList) {
            Paginator paginator = ((PageList) dataList).getPaginator();
            if (paginator != null)
                paginate.setTotalCount(paginator.getTotalCount());
        }

        if (logger.isDebugEnabled()) {
            logger.debug("totalCount:" + paginate.getTotalCount() + "\ttotalPages:"
                    + paginate.getTotalPage() + "\tpageIndex:" + pageIndex
                    + "\tpageSize:" + pageSize);
        }
        return paginate;
    }

    /**
     * 对批量操作的集合数据进行分页
     *
     * @param batchList 批量操作的对象集合
     * @param pageSize  每页数据条数，根据对象的字段数量确定,计算规则:2100/字段数量
     * @return 返回一个List, 里面是分页后的List集合
     */
    public List<List> pagingBatchOperationList(List batchList, int pageSize) {
        List<List> returnList = new ArrayList<List>();
        if (CollectionUtils.isEmpty(batchList)) {
            return returnList;
        }
        int beginIndex = 0;
        int allSize = batchList.size();
        int step = allSize % pageSize == 0 ? allSize / pageSize : allSize / pageSize + 1;
        int toIndex = 0;
        for (int i = 0; i < step; i++) {
            toIndex = (i + 1) * pageSize;
            if (toIndex > allSize) {
                toIndex = allSize;
            }
            beginIndex = i * pageSize;
            returnList.add(batchList.subList(beginIndex, toIndex));
        }
        return returnList;
    }

}
