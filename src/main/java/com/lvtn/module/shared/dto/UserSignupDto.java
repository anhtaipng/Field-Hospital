package com.lvtn.module.shared.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserSignupDto {
    @NotNull(message = "Xin vui lòng nhập tên người dùng")
    private String name;
    @NotNull(message = "Xin vui lòng nhập số điện thoại")
    private String phone;
    @NotNull(message = "Xin vui lòng nhập CMND")
    @Pattern(regexp = "^[0-9]*$",message = "Chứng minh nhân dân chỉ chứa số")
    private String cmnd;
    @NotNull(message = "Xin vui lòng nhập mật khẩu")
    @Size(min = 6,message = "Mật khẩu phải có ít nhất 6 ký tự")
    private String password;
    private String shift;
}
