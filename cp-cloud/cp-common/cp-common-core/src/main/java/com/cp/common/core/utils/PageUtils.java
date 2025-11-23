package com.cp.common.core.utils;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cp.common.core.utils.sql.SqlUtil;
import com.cp.common.core.web.page.PageDomain;
import com.cp.common.core.web.page.TableSupport;

/**
 * 基于 MyBatis-Plus 的分页工具
 * @author tyt15
 */
public class PageUtils {
    public static void startPage() {
        // 保留方法签名以兼容旧调用，无需操作
    }

    public static void clearPage() {
        // 保留方法签名以兼容旧调用，无需操作
    }

    /**
     * 构建 MP 的 Page 对象
     */
    public static <T> Page<T> buildPage() {
        PageDomain pd = TableSupport.buildPageRequest();
        long pageNum = pd.getPageNum() != null ? pd.getPageNum() : 1;
        long pageSize = pd.getPageSize() != null ? pd.getPageSize() : 10;
        Page<T> page = new Page<>(pageNum, pageSize);
        String orderBy = SqlUtil.escapeOrderBySql(pd.getOrderBy());
        if (orderBy != null && !orderBy.trim().isEmpty()) {
            String[] parts = orderBy.trim().split("\\s+");
            String column = parts[0];
            boolean asc = parts.length < 2 || "asc".equalsIgnoreCase(parts[1]);
            page.addOrder(asc ? OrderItem.asc(column) : OrderItem.desc(column));
        }
        return page;
    }
}
