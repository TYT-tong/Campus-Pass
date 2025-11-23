package com.cp.auth.domain;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
/**
 * @author tyt15
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SmsLoginBody {
  @NotBlank
  @Pattern(regexp = "^1[3-9]\\d{9}$")
  private String phone;
  @NotBlank
  @Pattern(regexp = "^\\d{6}$")
  private String code;

}