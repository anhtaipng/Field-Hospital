package com.lvtn.module.user.service;


import com.lvtn.module.shared.dto.ResetPasswordDto;
import com.lvtn.module.shared.dto.UserChangePasswordDto;
import com.lvtn.module.shared.dto.UserSignupDto;
import com.lvtn.module.shared.model.ImportPatient;
import com.lvtn.module.shared.model.User;
import com.lvtn.module.user.dto.ImportPatientDto;
import com.lvtn.module.user.dto.UserDto;
import com.lvtn.module.shared.model.Authority;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.security.Principal;
import java.util.List;

public interface UserService extends UserDetailsService {
    User signupUser(UserSignupDto userSignupDto, String roleName);
    User changePassword(UserChangePasswordDto userSignupDto, Principal user);
    void resetPassword(ResetPasswordDto info);
    User getUser(String username);

    UserDto updateUserProfile(UserDto userDto, String username);
    UserDto loadUserProfileByUsername(String username);
    Authority saveAuthority(Authority authority);

    User updateFcmToken(String fcmToken,String username);
    User removeFcmToken(String fcmToken,String username);

    void importPatient(ImportPatientDto importPatientDto);
    void addImportPatient(ImportPatientDto importPatientDto);
    void deleteImportPatient(Long idImportPatient);
    List<ImportPatientDto> listImportPatientIsReady();

    static String getRoleName() {
        SecurityContext context = SecurityContextHolder.getContext();
        if (context == null) {
            return "";
        }

        Authentication authentication = context.getAuthentication();
        if (authentication == null) {
            return "";
        }

        for (GrantedAuthority auth : authentication.getAuthorities()) {
            return auth.getAuthority();
        }
        return "";
    }
}
