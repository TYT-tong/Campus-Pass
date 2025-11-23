package com.cp.system.domain.vo;

import java.util.List;
import lombok.Data;

/**
 * @author tyt15
 */
@Data
public class RoleCardVO {
    private Long id;
    private String name;
    private String description;
    private List<String> tags;
    private String badgeColor;
    private String cardBgColor;
    private String cardBorderColor;
}

