package com.cp.personnel.domain.dto;

import lombok.Data;

/**
 * @author tyt15
 */
@Data
public class PerStudentQueryDTO {
    private String keyword;
    private String grade;
    private String className;
    private String status;
}