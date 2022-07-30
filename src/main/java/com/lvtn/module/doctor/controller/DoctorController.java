package com.lvtn.module.doctor.controller;

import com.lvtn.module.admin.service.AdminService;
import com.lvtn.module.doctor.service.DoctorService;
import com.lvtn.module.patient.service.PatientService;
import com.lvtn.module.shared.common.ApiSharedMesssage;
import com.lvtn.module.shared.dto.*;
import com.lvtn.module.shared.enumeration.GroupType;
import com.lvtn.module.shared.mapper.StatsMapper;
import com.lvtn.module.shared.model.CovidHospital;
import com.lvtn.module.shared.model.RoomType;
import com.lvtn.module.shared.model.Symptom;
import com.lvtn.module.shared.model.TestType;
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
@RequestMapping("/api/doctor")
@CrossOrigin
@Slf4j
public class DoctorController {

    @Autowired
    DoctorService doctorService;

    @Autowired
    EmployeeCommonService employeeCommonService;

    @Autowired
    PatientCommonService patientCommonService;

    @Autowired
    StatsService statsService;

    @Autowired
    StatsMapper statsMapper;

    @Autowired
    AdminService adminService;

    @Autowired
    SickbedService sickbedService;

    @Autowired
    RoomTypeService roomTypeService;

    @Autowired
    CovidHospitalService covidHospitalService;

    @Autowired
    TestTypeService testTypeService;

    @Autowired
    PatientService patientService;

    @Autowired
    MedicineService medicineService;

    @Autowired
    NotificationService notificationService;

    @PostMapping(value = "/patient/get")
    public Response<PageResponse<PatientInfoDto>> getPatientList(
            @RequestBody @Valid PatientListRequestDto patientListRequestDto, Principal user) {
        try {
            return Response.success(doctorService.getPatientList(patientListRequestDto, user.getName()));
        }catch (ApiException apiException){
            return Response.error(apiException.getCode(), apiException.getMessage());

        }
    }

    @GetMapping(value = "/patient/count")
    public Response<List<NumberOfPatientsDto>> countPatientList(Principal user) {
        try {
            return Response.success(employeeCommonService.countNumberOfPatient(user.getName(), "ROLE_DOCTOR"));
        }catch (ApiException apiException){
            return Response.error(apiException.getCode(), apiException.getMessage());

        }
    }

    @PostMapping(value = "/cancel/{username}")
    public Response<Void> cancelXuatVien(@PathVariable String username) {
        try {
            doctorService.cancelXuatVien(username);
            return Response.success(ApiSharedMesssage.SUCCESS);
        } catch (ApiException apiException){
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

    @PostMapping("/requirement/get/{username}")
    public Response<PageResponse<RequirementDto>> getPatientRequirementList(@RequestBody @Valid RequirementListRequestDto requirementListRequestDto, @PathVariable String username){
        try{
            return Response.success(patientService.getRequirementList(requirementListRequestDto,username));
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

    @GetMapping(value = "/covid_hospital/get")
    public Response<List<CovidHospital>> getAllCovidHospital() {
        try {
            return Response.success(covidHospitalService.getCovidHospitalEnableList());
        }catch (ApiException apiException){
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

    @PutMapping("/update_group_type/{username}")
    public Response<PatientInfoDto> changeGroupType(@Valid @RequestBody GroupType newGroupType, @PathVariable String username, Principal user) {
        try{
            return Response.success(doctorService.changeGroupType(newGroupType,username,user.getName()));
        }
        catch (ApiException apiException){
            return Response.error(apiException.getCode(), apiException.getMessage());
        }
    }

    @PostMapping("/request_patient_status/{username}")
    public Response<RequirementDto> requestChangePatientStatus(@Valid @RequestBody PatientStatusDto patientStatusDto, @PathVariable String username, Principal user) {
        try{
            return Response.success(doctorService.requestChangePatientStatus(patientStatusDto,username,user.getName()));
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

    @GetMapping(value = "/medicine/get")
    public Response<List<MedicineDto>> getMedicineList() {
        try {
            return Response.success(medicineService.getMedicineList());
        }catch (ApiException apiException){
            return Response.error(apiException.getCode(), apiException.getMessage());

        }
    }


    @GetMapping("/symptom/get")
    public Response<List<Symptom>> getSymptom(){
        try{
            return Response.success(adminService.getSymptom());
        }
        catch (ApiException apiException){
            return Response.error(apiException.getCode(), apiException.getMessage());
        }
    }

    @PostMapping("/testresult/get/{username}")
    public Response<PageResponse<TestResultDto>> getTestResult(@RequestBody @Valid TestResultRequest testResultRequest,@PathVariable String username){
        try{
            return Response.success(patientCommonService.getTestResult(testResultRequest,username));
        }
        catch (ApiException apiException){
            return Response.error(apiException.getCode(), apiException.getMessage());
        }
    }

    @PostMapping(value = "/examination/get")
    public Response<PageResponse<ExaminationDto>> getExaminationList(
            @RequestBody @Valid ExaminationListRequestDto examinationListRequestDto, Principal user) {
        try {
            return Response.success(doctorService.getExaminationList(examinationListRequestDto, user.getName()));
        } catch (ApiException apiException) {
            return Response.error(apiException.getCode(), apiException.getMessage());
        }
    }

    @PostMapping(value = "/examination/add")
    public Response<ExaminationDto> addExamination(
            @RequestBody ExaminationDto examinationDto, Principal user) {
        try {
            return Response.success(doctorService.createExamination(examinationDto,user.getName()));
        } catch (ApiException apiException) {
            return Response.error(apiException.getCode(), apiException.getMessage());
        }
    }

    @PostMapping("/requirement/get")
    public Response<PageResponse<RequirementDto>> getRequirementList(@RequestBody @Valid RequirementListRequestDto requirementListRequestDto, Principal user){
        try{
            return Response.success(doctorService.getRequirementList(requirementListRequestDto,user.getName()));
        }
        catch (ApiException apiException){
            return Response.error(apiException.getCode(), apiException.getMessage());
        }
    }

    @PostMapping("/negative_result/get")
    public Response<PageResponse<TestResultDto>> getNegativeResult(@RequestBody @Valid ListRequestDto listRequestDto, Principal user){
        try{
            return Response.success(doctorService.getNegativePatientList(listRequestDto,user.getName()));
        }
        catch (ApiException apiException){
            return Response.error(apiException.getCode(), apiException.getMessage());
        }
    }

    @PostMapping(value = "/medicine_requirement/get")
    public Response<PageResponse<MedicineRequirementDto>> getMedicineRequirementList(
            @RequestBody @Valid RequirementListRequestDto requirementListRequestDto, Principal user) {
        try {
            return Response.success(doctorService.getMedicineRequirementList(requirementListRequestDto,user.getName()));
        } catch (ApiException apiException) {
            return Response.error(apiException.getCode(), apiException.getMessage());
        }
    }

    @PostMapping(value = "/medicine_requirement/add")
    public Response<MedicineRequirementDto> addMedicineRequirement(
            @RequestBody @Valid MedicineRequirementDto medicineRequirementDto, Principal user) {
        try {
            return Response.success(doctorService.saveMedicineRequirement(medicineRequirementDto,user.getName()));
        } catch (ApiException apiException) {
            return Response.error(apiException.getCode(), apiException.getMessage());
        }
    }

    @GetMapping(value = "/info/get")
    public Response<EmployeeDto> getDoctorInfo(Principal user) {
        try {
            return Response.success(employeeCommonService.getEmployeeInfo(user.getName()));
        }catch (ApiException apiException){
            return Response.error(apiException.getCode(), apiException.getMessage());
        }
    }

    @PutMapping("/info/update")
    public Response<EmployeeDto> updateDoctorInfo(@RequestBody @Valid EmployeeDto employeeDto, Principal user) {
        try {
            return Response.success(employeeCommonService.updateEmployeeInfo(employeeDto, user.getName()));
        } catch (ApiException apiException) {
            return Response.error(apiException.getCode(), apiException.getMessage());
        }
    }
}
