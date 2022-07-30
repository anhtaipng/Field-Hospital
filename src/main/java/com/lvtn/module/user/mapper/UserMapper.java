package com.lvtn.module.user.mapper;

import com.lvtn.module.shared.model.*;
import com.lvtn.module.user.dto.ImportPatientDto;
import com.lvtn.module.user.dto.UserDto;
import com.lvtn.module.user.dto.UserDtoResponse;
import com.lvtn.module.user.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;

@Component
public class UserMapper {
    public UserDto mapUserToUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setUsername(user.getUsername());
        userDto.setBirthDate(user.getBirthDate());
        userDto.setName(user.getName());
        userDto.setAvatarURL(user.getAvatarURL());
        userDto.setGender(user.getGender());
        userDto.setPhone(user.getPhone());
        userDto.setEmail(user.getEmail());
        userDto.setRoleName(UserService.getRoleName());
        userDto.setFcmToken(user.getFcmToken());
        return userDto;
    }

    public User mapUserDtoToUser(UserDto userDto) {
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setPassword(userDto.getPassword());
        user.setEmail(userDto.getEmail());
        user.setName(userDto.getName());
        user.setBirthDate(userDto.getBirthDate());
        user.setGender(userDto.getGender());
        user.setPhone(userDto.getPhone());
        return user;
    }

    public UserDtoResponse mapUserToUserDtoResponse(User user) {
        UserDtoResponse userDtoResponse = new UserDtoResponse();

        BeanUtils.copyProperties(user, userDtoResponse);
        userDtoResponse.setRoleName(UserService.getRoleName());


        return userDtoResponse;
    }

    public ImportPatient mapImportPatientDtoToImportPatient(ImportPatientDto importPatientDto) {
        ImportPatient importPatient = new ImportPatient();

        BeanUtils.copyProperties(importPatientDto, importPatient);

        importPatient.setAdded(false);
        String sicknesses = "";
        for (int i = 0; i < importPatientDto.getSickness().size(); i++){
            sicknesses = sicknesses + importPatientDto.getSickness().get(i);
            if (i!=importPatientDto.getSickness().size()-1){
                sicknesses = sicknesses + "_";
            }
        }
        importPatient.setSicknesses(sicknesses);

        return importPatient;
    }

    public ImportPatientDto mapImportPatientToImportPatientDto(ImportPatient importPatient){
        ImportPatientDto importPatientDto = new ImportPatientDto();

        BeanUtils.copyProperties(importPatient, importPatientDto);

        String[] buffer = importPatient.getSicknesses().split("_");
        ArrayList<String> sickness = new ArrayList<>(Arrays.asList(buffer));
        importPatientDto.setSickness(sickness);

        return importPatientDto;
    }

}
