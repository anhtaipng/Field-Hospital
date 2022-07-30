package com.lvtn.module.shared.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserChangePasswordDto {
    @NotNull(message = "Xin vui lòng nhập mật khẩu củ")
    private String oldPassword;
    @NotNull(message = "Xin vui lòng nhập mật khẩu mới")
    @Size(min = 6,message = "Mật khẩu mới phải có ít nhất 6 ký tự")
    private String newPassword;
}
