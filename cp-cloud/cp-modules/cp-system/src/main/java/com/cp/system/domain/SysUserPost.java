package com.cp.system.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户和岗位关联 sys_user_post
 * 
 * @author cp
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class SysUserPost
{
    /** 用户ID */
    private Long userId;
    
    /** 岗位ID */
    private Long postId;

    
}
