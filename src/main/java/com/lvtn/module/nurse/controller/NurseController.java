package com.lvtn.module.nurse.controller;

import com.lvtn.module.nurse.service.NurseService;
import com.lvtn.module.patient.service.PatientService;
import com.lvtn.module.shared.common.ApiSharedMesssage;
import com.lvtn.module.shared.dto.*;
import com.lvtn.module.shared.mapper.StatsMapper;
import com.lvtn.module.shared.model.RoomType;
import com.lvtn.module.shared.model.TestType;
import com.lvtn.module.shared.repository.PatientRepository;
import com.lvtn.module.shared.service.*;
import com.lvtn.platform.common.PageResponse;
import com.lvtn.platform.common.Response;
import com.lvtn.platform.exception.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/nurse")
@CrossOrigin
@Slf4j
public class NurseController {
    @Autowired
    PatientRepository patientRepository;

    @Autowired
    StatsService statsService;

    @Autowired
    PatientCommonService patientCommonService;

    @Autowired
    EmployeeCommonService employeeCommonService;


    @Autowired
    NurseService nurseService;

    @Autowired
    StatsMapper statsMapper;

    @Autowired
    SickbedService sickbedService;

    @Autowired
    RoomTypeService roomTypeService;

    @Autowired
    NotificationService notificationService;

    @Autowired
    TestTypeService testTypeService;

    @Autowired
    PatientService patientService;


    @GetMapping(value = "/patient/count")
    public Response<List<NumberOfPatientsDto>> countPatientList(Principal user) {
        try {
            return Response.success(employeeCommonService.countNumberOfPatient(user.getName(), "ROLE_NURSE"));
        }catch (ApiException apiException){
            return Response.error(apiException.getCode(), apiException.getMessage());

        }
    }

    @PostMapping("/prescription/get")
    public Response<PageResponse<PrescriptionResponseDto>> getPrescriptionList(@RequestBody @Valid PrescriptionListRequestDto prescriptionListRequestDto, Principal user){
        try{
            return Response.success(nurseService.getPrescription(prescriptionListRequestDto,user.getName()));
        }
        catch (ApiException apiException){
            return Response.error(apiException.getCode(), apiException.getMessage());
        }
    }

    @PostMapping("/prescription/check/{prescription_id}")
    public Response<Void> checkPrescription(@RequestBody @Valid CheckDto checkDto, @PathVariable Long prescription_id){
        try{
            nurseService.checkPrescription(checkDto.isCheck(),prescription_id);
            return Response.success(ApiSharedMesssage.SUCCESS);
        }
        catch (ApiException apiException){
            return Response.error(apiException.getCode(), apiException.getMessage());
        }
    }

    @GetMapping("/medicine_batch/get/{prescription_id}")
    public Response<PrescriptionDto> getMedicineBatchHistory(@PathVariable Long prescription_id){
        try{
            return Response.success(nurseService.getMedicineBatchHistory(prescription_id));
        }
        catch (ApiException apiException){
            return Response.error(apiException.getCode(), apiException.getMessage());
        }
    }

    @PostMapping("/requirement/get/{username}")
    public Response<PageResponse<RequirementDto>> getPatientRequirementList(@RequestBody @Valid RequirementListRequestDto requirementListRequestDto, @PathVariable String username){
        try{
            return Response.success(patientService.getRequirementList(requirementListRequestDto,username));
        }
        catch (ApiException apiException){
            return Response.error(apiException.getCode(), apiException.getMessage());
        }
    }

    @PutMapping("/patient_status/update/{username}")
    public Response<PatientInfoDto> changePatientStatus(@RequestBody @Valid PatientStatusDto patientStatusDto, @PathVariable String username){
        try{
            return Response.success(nurseService.changePatientStatus(patientStatusDto,username));
        }
        catch (ApiException apiException){
            return Response.error(apiException.getCode(), apiException.getMessage());
        }
    }

    @PostMapping("/requirement/get")
    public Response<PageResponse<RequirementDto>> getRequirementList(@RequestBody @Valid RequirementListRequestDto requirementListRequestDto, Principal user){
        try{
            return Response.success(nurseService.getRequirementList(requirementListRequestDto,user.getName()));
        }
        catch (ApiException apiException){
            return Response.error(apiException.getCode(), apiException.getMessage());
        }
    }

    @PostMapping(value = "/patient/get")
    public Response<PageResponse<PatientInfoDto>> getPatientList(
            @RequestBody @Valid PatientListRequestDto patientListRequestDto, Principal user) {
        try {
            return Response.success(nurseService.getPatientList(patientListRequestDto, user.getName()));
        }catch (ApiException apiException){
            return Response.error(apiException.getCode(), apiException.getMessage());

        }
    }

    @GetMapping(value = "/patient/get/{username}")
    public Response<PatientInfoDto> getPatient(@PathVariable String username) {
        try {
            return Response.success(patientCommonService.getPatientInfo(username));
        }catch (ApiException apiException){
            return Response.error(apiException.getCode(), apiException.getMessage());

        }
    }

    @PostMapping(value = "/sickbed_available/get")
    public Response<PageResponse<PatientLocationDto>> getSickbedAvailableWithRoomType(
            @RequestBody @Valid SickbedListRequestDto sickbedListRequestDto) {
        try {
            return Response.success(sickbedService.getSickbedAvailableWithRoomType(sickbedListRequestDto));
        }catch (ApiException apiException){
            return Response.error(apiException.getCode(), apiException.getMessage());

        }
    }

    @GetMapping(value = "/room_type/get")
    public Response<List<RoomType>> getAllRoomType() {
        try {
            return Response.success(roomTypeService.getRoomType());
        }catch (ApiException apiException){
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

    @PostMapping("/stats/add/{username}")
    public Response<StatsResponseDto> createStats(@Valid @RequestBody StatsCreateRequest statsCreateRequest, @PathVariable String username){
        try{

            return Response.success(statsService.createStats(statsCreateRequest,username));
        }
        catch (ApiException apiException){
            return Response.error(apiException.getCode(), apiException.getMessage());
        }
    }

    @GetMapping("/contact/get/{username}")
    public Response<PatientContactResponseDto> getPatientContact(@PathVariable String username){
        try{
            return Response.success(patientCommonService.getPatientContact(username));
        }
        catch (ApiException apiException){
            return Response.error(apiException.getCode(), apiException.getMessage());
        }
    }

    @GetMapping("/location/get/{username}")
    public Response<PatientLocationDto> getPatientCurrentLocation(@PathVariable String username){
        try{
            return Response.success(patientCommonService.getPatientCurrentLocation(username));
        }
        catch (ApiException apiException){
            return Response.error(apiException.getCode(), apiException.getMessage());
        }
    }

    @PutMapping("/location/update/{username}")
    public Response<PatientLocationDto> updatePatientCurrentLocation(@RequestBody @Valid PatientLocationDto patientLocationDto,@PathVariable String username) {
        try {
            return Response.success(patientCommonService.updateCurrentLocation(patientLocationDto, username));
        } catch (ApiException apiException) {
            return Response.error(apiException.getCode(), apiException.getMessage());
        }
    }

    @GetMapping("/stats/get_first_all/{username}")
    public Response<List<AllStatsResponseDto>> getFirstAll(@PathVariable String username){
        try{
            return Response.success(statsMapper.mapStatisticResultToAllStatsResponseDto(statsService.getFirstStatsByType(username)));
        }
        catch (ApiException apiException){
            return Response.error(apiException.getCode(), apiException.getMessage());
        }
    }

    @PostMapping("/stats/get/{username}")
    public Response<StatisticResultResponse> getStatsByType(@Valid @RequestBody StatisticResultRequest statisticResultRequest, @PathVariable String username){
        try{
            return Response.success(statsService.getStatsByType(statisticResultRequest,username));
        }
        catch (ApiException apiException){
            return Response.error(apiException.getCode(), apiException.getMessage());
        }
    }

    @GetMapping("/prescription/get/{username}")
    public Response<PrescriptionResponseDto> getPatientCurrentPrescription(@PathVariable String username){
        try{
            return Response.success(patientCommonService.getCurrentPrescription(username));
        }
        catch (ApiException apiException){
            return Response.error(apiException.getCode(), apiException.getMessage());
        }
    }

    @PostMapping("/examination/get/{username}")
    public Response<PageResponse<ExaminationDto>> getPatientExaminations(@RequestBody @Valid ExaminationListRequestDto examinationListRequestDto, @PathVariable String username){
        try{
            return Response.success(patientCommonService.getExamination(examinationListRequestDto,username));
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


    @GetMapping(value = "/info/get")
    public Response<EmployeeDto> getNursesInfo(Principal user) {
        try {
            return Response.success(employeeCommonService.getEmployeeInfo(user.getName()));
        }catch (ApiException apiException){
            return Response.error(apiException.getCode(), apiException.getMessage());
        }
    }

    @PutMapping("/info/update")
    public Response<EmployeeDto> updateNurseInfo(@RequestBody @Valid EmployeeDto employeeDto,Principal user) {
        try {
            return Response.success(employeeCommonService.updateEmployeeInfo(employeeDto, user.getName()));
        } catch (ApiException apiException) {
            return Response.error(apiException.getCode(), apiException.getMessage());
        }
    }

    @PutMapping("/patient/info/update/{username}")
    public Response<PatientInfoDto> updatePatientInfo(@RequestBody @Valid PatientInfoDto patientInfoDto,@PathVariable String username){
        try{
            return Response.success(patientCommonService.updatePatientInfo(patientInfoDto,username));
        }
        catch (ApiException apiException){
            return Response.error(apiException.getCode(), apiException.getMessage());
        }
    }

    @PostMapping("/testresult/get/{username}")
    public Response<PageResponse<TestResultDto>> getTestResult(@Valid @RequestBody TestResultRequest testResultRequest,@PathVariable String username){
        try{
            return Response.success(patientCommonService.getTestResult(testResultRequest,username));
        }
        catch (ApiException apiException){
            return Response.error(apiException.getCode(), apiException.getMessage());
        }
    }

    @PostMapping("/testresult/add/{username}")
    public Response<TestResultDto> addTestResul(@RequestBody @Valid TestResultDto testResultDto ,@PathVariable String username){
        try{
            return Response.success(patientCommonService.addTestResult(testResultDto, username));
        }
        catch (ApiException apiException){
            return Response.error(apiException.getCode(), apiException.getMessage());
        }
    }

    @PutMapping("/testresult/update/{username}")
    public Response<TestResultDto> updateTestResul(@RequestBody @Valid TestResultDto testResultDto ,@PathVariable String username){
        try{
            return Response.success(patientCommonService.updateTestResult(testResultDto, username));
        }
        catch (ApiException apiException){
            return Response.error(apiException.getCode(), apiException.getMessage());
        }
    }

    @PostMapping("/testresult/delete/")
    public Response<TestResultDto> deleteTestResult(@RequestBody @Valid TestResultDto testResultDto ){
        try{
            return Response.success(patientCommonService.deleteTestResult(testResultDto.getId()));
        }
        catch (ApiException apiException){
            return Response.error(apiException.getCode(), apiException.getMessage());
        }
    }

    @GetMapping("/allergy/get/{username}")
    public Response<List<AllergyDto>> getAllergy(@PathVariable String username){
        try{
            return Response.success(patientCommonService.getAllergy(username));
        }
        catch (ApiException apiException){
            return Response.error(apiException.getCode(), apiException.getMessage());
        }
    }

    @GetMapping("/sickness/get/{username}")
    public Response<List<SicknessDto>> getSickness(@PathVariable String username){
        try{
            return Response.success(patientCommonService.getSickness(username));
        }
        catch (ApiException apiException){
            return Response.error(apiException.getCode(), apiException.getMessage());
        }
    }

    @GetMapping("/surgery/get/{username}")
    public Response<List<SurgeryDto>> getSurgery(@PathVariable String username){
        try{
            return Response.success(patientCommonService.getSurgery(username));
        }
        catch (ApiException apiException){
            return Response.error(apiException.getCode(), apiException.getMessage());
        }
    }

    @GetMapping("/vaccine/get/{username}")
    public Response<List<VaccineDto>> getVaccine(@PathVariable String username){
        try{
            return Response.success(patientCommonService.getVaccine(username));
        }
        catch (ApiException apiException){
            return Response.error(apiException.getCode(), apiException.getMessage());
        }
    }

    @PostMapping("/allergy/add/{username}")
    public Response<AllergyDto> addAllergy(@RequestBody @Valid AllergyDto allergyDto,@PathVariable String username){
        try{
            return Response.success(patientCommonService.addAllergy(allergyDto,username));
        }
        catch (ApiException apiException){
            return Response.error(apiException.getCode(), apiException.getMessage());
        }
    }

    @PutMapping("/allergy/update/{username}")
    public Response<AllergyDto> updateAllergy(@RequestBody @Valid AllergyDto allergyDto,@PathVariable String username){
        try{
            return Response.success(patientCommonService.updateAllergy(allergyDto,username));
        }
        catch (ApiException apiException){
            return Response.error(apiException.getCode(), apiException.getMessage());
        }
    }

    @PostMapping("/allergy/delete/{username}")
    public Response<AllergyDto> deleteAllergy(@RequestBody @Valid AllergyDto allergyDto){
        try{
            return Response.success(patientCommonService.deleteAllergy(allergyDto.getId()));
        }
        catch (ApiException apiException){
            return Response.error(apiException.getCode(), apiException.getMessage());
        }
    }

    @PostMapping("/sickness/add/{username}")
    public Response<SicknessDto> addSickness(@RequestBody @Valid SicknessDto sicknessDto, @PathVariable String username){
        try{
            return Response.success(patientCommonService.addSickness(sicknessDto,username));
        }
        catch (ApiException apiException){
            return Response.error(apiException.getCode(), apiException.getMessage());
        }
    }

    @PutMapping("/sickness/update/{username}")
    public Response<SicknessDto> updateSickness(@RequestBody @Valid SicknessDto sicknessDto,@PathVariable String username){
        try{
            return Response.success(patientCommonService.updateSickness(sicknessDto,username));
        }
        catch (ApiException apiException){
            return Response.error(apiException.getCode(), apiException.getMessage());
        }
    }

    @PostMapping("/sickness/delete/{username}")
    public Response<SicknessDto> deleteSickness(@RequestBody @Valid SicknessDto sicknessDto){
        try{
            return Response.success(patientCommonService.deleteSickness(sicknessDto.getId()));
        }
        catch (ApiException apiException){
            return Response.error(apiException.getCode(), apiException.getMessage());
        }
    }

    @PostMapping("/stats/delete/{username}")
    public Response<Void> deleteStats(@RequestBody @Valid StatsResponseDto statsResponseDto){
        try{
            statsService.deleteStats(statsResponseDto);
            return Response.success(ApiSharedMesssage.SUCCESS);
        }
        catch (ApiException apiException){
            return Response.error(apiException.getCode(), apiException.getMessage());
        }
    }

    @PostMapping("/surgery/add/{username}")
    public Response<SurgeryDto> addSurgery(@RequestBody @Valid SurgeryDto surgeryDto,@PathVariable String username){
        try{
            return Response.success(patientCommonService.addSurgery(surgeryDto,username));
        }
        catch (ApiException apiException){
            return Response.error(apiException.getCode(), apiException.getMessage());
        }
    }

    @PutMapping("/surgery/update/{username}")
    public Response<SurgeryDto> updateSurgery(@RequestBody @Valid SurgeryDto surgeryDto,@PathVariable String username){
        try{
            return Response.success(patientCommonService.updateSurgery(surgeryDto,username));
        }
        catch (ApiException apiException){
            return Response.error(apiException.getCode(), apiException.getMessage());
        }
    }

    @PostMapping("/surgery/delete/{username}")
    public Response<SurgeryDto> deleteSurgery(@RequestBody @Valid SurgeryDto surgeryDto){
        try{
            return Response.success(patientCommonService.deleteSurgery(surgeryDto.getId()));
        }
        catch (ApiException apiException){
            return Response.error(apiException.getCode(), apiException.getMessage());
        }
    }

    @PostMapping("/vaccine/add/{username}")
    public Response<VaccineDto> addVaccine(@RequestBody @Valid VaccineDto vaccineDto,@PathVariable String username){
        try{
            return Response.success(patientCommonService.addVaccine(vaccineDto,username));
        }
        catch (ApiException apiException){
            return Response.error(apiException.getCode(), apiException.getMessage());
        }
    }

    @PutMapping("/vaccine/update/{username}")
    public Response<VaccineDto> updateVaccine(@RequestBody @Valid VaccineDto vaccineDto,@PathVariable String username){
        try{
            return Response.success(patientCommonService.updateVaccine(vaccineDto,username));
        }
        catch (ApiException apiException){
            return Response.error(apiException.getCode(), apiException.getMessage());
        }
    }

    @PostMapping("/vaccine/delete/{username}")
    public Response<VaccineDto> deleteVaccine(@RequestBody @Valid VaccineDto vaccineDto){
        try{
            return Response.success(patientCommonService.deleteVaccine(vaccineDto.getId()));
        }
        catch (ApiException apiException){
            return Response.error(apiException.getCode(), apiException.getMessage());
        }
    }




}
