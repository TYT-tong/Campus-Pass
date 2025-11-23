package com.cp.system.mapper;

import com.cp.system.domain.SysUserPost;

import java.util.List;

/**
 * 用户与岗位关联表 数据访问层
 *
 * @author cp
 */
public interface SysUserPostMapper
{

    /**
     * 批量新增用户岗位信息
     *
     * @param userPostList 用户岗位列表
     * @return 结果
     */
    public int batchUserPost(List<SysUserPost> userPostList);

    void deleteUserPostByUserId(Long userId);

    void deleteUserPost(Long[] userIds);
}
