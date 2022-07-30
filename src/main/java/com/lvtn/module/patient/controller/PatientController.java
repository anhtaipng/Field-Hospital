package com.lvtn.module.patient.controller;

import com.lvtn.module.patient.dto.PatientSignupDto;
import com.lvtn.module.patient.service.PatientService;
import com.lvtn.module.shared.common.ApiSharedMesssage;
import com.lvtn.module.shared.dto.*;
import com.lvtn.module.shared.model.TestType;
import com.lvtn.module.shared.model.User;
import com.lvtn.module.shared.repository.UserRepository;
import com.lvtn.module.shared.service.NotificationService;
import com.lvtn.module.shared.service.PatientCommonService;
import com.lvtn.module.shared.service.TestTypeService;
import com.lvtn.module.user.common.ApiClientMessage;
import com.lvtn.module.user.service.implementation.SmsService;
import com.lvtn.platform.common.PageResponse;
import com.lvtn.platform.common.Response;
import com.lvtn.platform.exception.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/patient")
@CrossOrigin
@Slf4j
public class PatientController {

    @Autowired
    PatientService patientService;

    @Autowired
    PatientCommonService patientCommonService;

    @Autowired
    TestTypeService testTypeService;

    @Autowired
    NotificationService notificationService;

    @Autowired
    SmsService service;

    @Autowired
    UserRepository userRepository;

    @PostMapping("/signup")
    public Response<PatientInfoDto> patientSignup(@Valid @RequestBody PatientSignupDto patientSignupDto){
        try{
            return Response.success(patientService.signupPatient(patientSignupDto));
        }
        catch (ApiException apiException){
            return Response.error(apiException.getCode(), apiException.getMessage());
        }
    }

    @PostMapping("/signup_otp")
    public Response<Void> patientOtp(@Valid @RequestBody PatientSignupDto patientSignupDto){
        try{
            User existed = userRepository.findUserByUsername(patientSignupDto.getCmnd());
            if (existed != null) {
                throw new ApiException(ApiClientMessage.ACCOUNT_EXISTED);
            }
            if (patientSignupDto.getCmnd().trim().length()!=9 && patientSignupDto.getCmnd().trim().length()!=12) {
                throw new ApiException(123111,"CMND phải có 9 ký tự hoặc CCCD phải có 12 ký tự");
            }
            service.send(patientSignupDto);
        }
        catch (ApiException apiException) {
            return Response.error(apiException.getCode(), apiException.getMessage());
        }
        return Response.success(ApiSharedMesssage.SUCCESS);
    }

    @GetMapping("/info/get")
    public Response<PatientInfoDto> getPatientInfo(Principal user){
        try{
            return Response.success(patientCommonService.getPatientInfo(user.getName()));
        }
        catch (ApiException apiException){
            return Response.error(apiException.getCode(), apiException.getMessage());
        }
    }

    @PutMapping("/info/update")
    public Response<PatientInfoDto> updatePatientInfo(@RequestBody @Valid PatientInfoDto patientInfoDto,Principal user){
        try{
            return Response.success(patientCommonService.updatePatientInfo(patientInfoDto,user.getName()));
        }
        catch (ApiException apiException){
            return Response.error(apiException.getCode(), apiException.getMessage());
        }
    }

    @GetMapping("/contact/get")
    public Response<PatientContactResponseDto> getPatientContact(Principal user){
        try{
            return Response.success(patientCommonService.getPatientContact(user.getName()));
        }
        catch (ApiException apiException){
            return Response.error(apiException.getCode(), apiException.getMessage());
        }
    }

    @GetMapping("/location/get")
    public Response<PatientLocationDto> getPatientCurrentLocation(Principal user){
        try{
            return Response.success(patientCommonService.getPatientCurrentLocation(user.getName()));
        }
        catch (ApiException apiException){
            return Response.error(apiException.getCode(), apiException.getMessage());
        }
    }

    @PutMapping("/location/update")
    public Response<PatientLocationDto> updatePatientCurrentLocation(@RequestBody @Valid PatientLocationDto patientLocationDto,Principal user){
        try{
            return Response.success(patientCommonService.updateCurrentLocation(patientLocationDto,user.getName()));
        }
        catch (ApiException apiException){
            return Response.error(apiException.getCode(), apiException.getMessage());
        }
    }

    @GetMapping(value = "/test_type/get")
    public Response<List<TestType>> getAllTestType() {
        try {
            return Response.success(testTypeService.get());
        }catch (ApiException apiException){
            return Response.error(apiException.getCode(), apiException.getMessage());

        }
    }

    @PutMapping("/dependent/update")
    public Response<DependentDto> updatePatientDependent(@RequestBody @Valid DependentDto dependentDto, Principal user){
        try{
            return Response.success(patientCommonService.updatePatientDependent(dependentDto,user.getName()));
        }
        catch (ApiException apiException){
            return Response.error(apiException.getCode(), apiException.getMessage());
        }
    }

    @PostMapping("/dependent/add")
    public Response<DependentDto> addPatientDependent(@RequestBody @Valid DependentDto dependentDto, Principal user){
        try{
            return Response.success(patientCommonService.addPatientDependent(dependentDto,user.getName()));
        }
        catch (ApiException apiException){
            return Response.error(apiException.getCode(), apiException.getMessage());
        }
    }

    @PostMapping("/dependent/delete")
    public Response<Void> deletePatientDependent(@RequestBody @Valid DependentDto dependentDto, Principal user){
        try{
            patientCommonService.deletePatientDependent(dependentDto,user.getName());
            return Response.success(ApiSharedMesssage.DELETE_SUCCESSFUL);
        }
        catch (ApiException apiException){
            return Response.error(apiException.getCode(), apiException.getMessage());
        }
    }

    @GetMapping("/prescription/get")
    public Response<PrescriptionResponseDto> getPatientCurrentPrescription(Principal user){
        try{
            return Response.success(patientCommonService.getCurrentPrescription(user.getName()));
        }
        catch (ApiException apiException){
            return Response.error(apiException.getCode(), apiException.getMessage());
        }
    }

    @PostMapping("/examination/get")
    public Response<PageResponse<ExaminationDto>> getPatientExaminations(@RequestBody @Valid ExaminationListRequestDto examinationListRequestDto, Principal user){
        try{
            return Response.success(patientCommonService.getExamination(examinationListRequestDto,user.getName()));
        }
        catch (ApiException apiException){
            return Response.error(apiException.getCode(), apiException.getMessage());
        }
    }

    @PostMapping("/requirement/get")
    public Response<PageResponse<RequirementDto>> getRequirementList(@RequestBody @Valid RequirementListRequestDto requirementListRequestDto, Principal user){
        try{
            return Response.success(patientService.getRequirementList(requirementListRequestDto,user.getName()));
        }
        catch (ApiException apiException){
            return Response.error(apiException.getCode(), apiException.getMessage());
        }
    }

    @PostMapping("/notification/get")
    public Response<NotificationResponseDto> getNotifications(@RequestBody @Valid ListRequestDto listRequestDto, Principal user){
        try{
            return Response.success(notificationService.getNotification(listRequestDto,user.getName()));
        }
        catch (ApiException apiException){
            return Response.error(apiException.getCode(), apiException.getMessage());
        }
    }

    @PutMapping("/notification/check/{id}")
    public Response<NotificationDto> checkNotification(@RequestBody @Valid CheckDto checkDto, @PathVariable Long id){
        try{
            return Response.success(notificationService.checkNotification(checkDto,id));
        }
        catch (ApiException apiException){
            return Response.error(apiException.getCode(), apiException.getMessage());
        }
    }

    @PutMapping("/notification/check_all")
    public Response<Void> checkAllNotification(Principal user){
        try{
            notificationService.readAll(user.getName());
            return Response.success(ApiSharedMesssage.SUCCESS);
        }
        catch (ApiException apiException){
            return Response.error(apiException.getCode(), apiException.getMessage());
        }
    }

    @GetMapping("/allergy/get")
    public Response<List<AllergyDto>> getAllergy(Principal user){
        try{
            return Response.success(patientCommonService.getAllergy(user.getName()));
        }
        catch (ApiException apiException){
            return Response.error(apiException.getCode(), apiException.getMessage());
        }
    }

    @GetMapping("/sickness/get")
    public Response<List<SicknessDto>> getSickness(Principal user){
        try{
            return Response.success(patientCommonService.getSickness(user.getName()));
        }
        catch (ApiException apiException){
            return Response.error(apiException.getCode(), apiException.getMessage());
        }
    }

    @GetMapping("/surgery/get")
    public Response<List<SurgeryDto>> getSurgery(Principal user){
        try{
            return Response.success(patientCommonService.getSurgery(user.getName()));
        }
        catch (ApiException apiException){
            return Response.error(apiException.getCode(), apiException.getMessage());
        }
    }

    @GetMapping("/vaccine/get")
    public Response<List<VaccineDto>> getVaccine(Principal user){
        try{
            return Response.success(patientCommonService.getVaccine(user.getName()));
        }
        catch (ApiException apiException){
            return Response.error(apiException.getCode(), apiException.getMessage());
        }
    }

    @PostMapping("/allergy/add")
    public Response<AllergyDto> addAllergy(@RequestBody @Valid AllergyDto allergyDto,Principal user){
        try{
            return Response.success(patientCommonService.addAllergy(allergyDto,user.getName()));
        }
        catch (ApiException apiException){
            return Response.error(apiException.getCode(), apiException.getMessage());
        }
    }

    @PutMapping("/allergy/update")
    public Response<AllergyDto> updateAllergy(@RequestBody @Valid AllergyDto allergyDto,Principal user){
        try{
            return Response.success(patientCommonService.updateAllergy(allergyDto,user.getName()));
        }
        catch (ApiException apiException){
            return Response.error(apiException.getCode(), apiException.getMessage());
        }
    }

    @PostMapping("/allergy/delete")
    public Response<AllergyDto> deleteAllergy(@RequestBody @Valid AllergyDto allergyDto){
        try{
            return Response.success(patientCommonService.deleteAllergy(allergyDto.getId()));
        }
        catch (ApiException apiException){
            return Response.error(apiException.getCode(), apiException.getMessage());
        }
    }

    @PostMapping("/sickness/add")
    public Response<SicknessDto> addSickness(@RequestBody @Valid SicknessDto sicknessDto, Principal user){
        try{
            return Response.success(patientCommonService.addSickness(sicknessDto,user.getName()));
        }
        catch (ApiException apiException){
            return Response.error(apiException.getCode(), apiException.getMessage());
        }
    }

    @PutMapping("/sickness/update")
    public Response<SicknessDto> updateSickness(@RequestBody @Valid SicknessDto sicknessDto,Principal user){
        try{
            return Response.success(patientCommonService.updateSickness(sicknessDto,user.getName()));
        }
        catch (ApiException apiException){
            return Response.error(apiException.getCode(), apiException.getMessage());
        }
    }

    @PostMapping("/sickness/delete")
    public Response<SicknessDto> deleteSickness(@RequestBody @Valid SicknessDto sicknessDto){
        try{
            return Response.success(patientCommonService.deleteSickness(sicknessDto.getId()));
        }
        catch (ApiException apiException){
            return Response.error(apiException.getCode(), apiException.getMessage());
        }
    }

    @PostMapping("/surgery/add")
    public Response<SurgeryDto> addSurgery(@RequestBody @Valid SurgeryDto surgeryDto,Principal user){
        try{
            return Response.success(patientCommonService.addSurgery(surgeryDto,user.getName()));
        }
        catch (ApiException apiException){
            return Response.error(apiException.getCode(), apiException.getMessage());
        }
    }

    @PutMapping("/surgery/update")
    public Response<SurgeryDto> updateSurgery(@RequestBody @Valid SurgeryDto surgeryDto,Principal user){
        try{
            return Response.success(patientCommonService.updateSurgery(surgeryDto,user.getName()));
        }
        catch (ApiException apiException){
            return Response.error(apiException.getCode(), apiException.getMessage());
        }
    }

    @PostMapping("/surgery/delete")
    public Response<SurgeryDto> deleteSurgery(@RequestBody @Valid SurgeryDto surgeryDto){
        try{
            return Response.success(patientCommonService.deleteSurgery(surgeryDto.getId()));
        }
        catch (ApiException apiException){
            return Response.error(apiException.getCode(), apiException.getMessage());
        }
    }

    @PostMapping("/vaccine/add")
    public Response<VaccineDto> addVaccine(@RequestBody @Valid VaccineDto vaccineDto,Principal user){
        try{
            return Response.success(patientCommonService.addVaccine(vaccineDto,user.getName()));
        }
        catch (ApiException apiException){
            return Response.error(apiException.getCode(), apiException.getMessage());
        }
    }

    @PutMapping("/vaccine/update")
    public Response<VaccineDto> updateVaccine(@RequestBody @Valid VaccineDto vaccineDto,Principal user){
        try{
            return Response.success(patientCommonService.updateVaccine(vaccineDto,user.getName()));
        }
        catch (ApiException apiException){
            return Response.error(apiException.getCode(), apiException.getMessage());
        }
    }

    @PostMapping("/vaccine/delete")
    public Response<VaccineDto> deleteVaccine(@RequestBody @Valid VaccineDto vaccineDto){
        try{
            return Response.success(patientCommonService.deleteVaccine(vaccineDto.getId()));
        }
        catch (ApiException apiException){
            return Response.error(apiException.getCode(), apiException.getMessage());
        }
    }

    @PostMapping("/testresult/get")
    public Response<PageResponse<TestResultDto>> getTestResult(@RequestBody @Valid TestResultRequest testResultRequest,Principal user){
        try{
            return Response.success(patientCommonService.getTestResult(testResultRequest,user.getName()));
        }
        catch (ApiException apiException){
            return Response.error(apiException.getCode(), apiException.getMessage());
        }
    }

    @PostMapping("/support")
    public Response<Void> requestSupport(@RequestBody @Valid StringDto supportUsername,Principal user){
        try{
            patientCommonService.support(supportUsername.getStr(),user.getName());
            return Response.success();
        }
        catch (ApiException apiException){
            return Response.error(apiException.getCode(), apiException.getMessage());
        }
    }




}
