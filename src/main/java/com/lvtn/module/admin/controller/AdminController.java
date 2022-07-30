package com.lvtn.module.admin.controller;

import com.lvtn.module.admin.dto.*;
import com.lvtn.module.shared.common.ApiSharedMesssage;
import com.lvtn.module.shared.dto.*;
import com.lvtn.module.shared.model.*;
import com.lvtn.module.shared.service.*;
import com.lvtn.module.admin.service.AdminService;
import com.lvtn.module.user.dto.ImportPatientDto;
import com.lvtn.module.user.service.UserService;
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
@RequestMapping("/api/admin")
@CrossOrigin
@Slf4j
public class AdminController {

    @Autowired
    AdminService adminService;

    @Autowired
    SickbedService sickbedService;

    @Autowired
    RoomTypeService roomTypeService;

    @Autowired
    RoomService roomService;

    @Autowired
    FloorService floorService;

    @Autowired
    BuildingService buildingService;

    @Autowired
    MedicineService medicineService;

    @Autowired
    MedicalDeviceTypeService medicalDeviceTypeService;

    @Autowired
    MedicalDeviceService medicalDeviceService;

    @Autowired
    CovidHospitalService covidHospitalService;

    @Autowired
    EmployeeCommonService employeeCommonService;

    @Autowired
    PatientCommonService patientCommonService;

    @Autowired
    UserService userService;


    @PostMapping("/signup")
    public Response<EmployeeDto> adminSignup(@Valid @RequestBody UserSignupDto userSignupDto) {
        try {
            return Response.success(adminService.signupAdmin(userSignupDto));
        } catch (ApiException apiException) {
            return Response.error(apiException.getCode(), apiException.getMessage());
        }
    }

    @PostMapping("/medicine_batch/list")
    public Response<List<MedicineBatchDto>> listMedicineBatch(@Valid @RequestBody MedicineBatchDto medicineBatchDto) {
        try {
            return Response.success(adminService.getMedicineBatchList(medicineBatchDto.getMedicineName()));
        } catch (ApiException apiException) {
            return Response.error(apiException.getCode(), apiException.getMessage());
        }
    }

    @PostMapping("/medicine_batch/add")
    public Response<MedicineBatchDto> addMedicineBatch(@Valid @RequestBody MedicineBatchDto medicineBatchDto) {
        try {
            return Response.success(adminService.addMedicineBatch(medicineBatchDto));
        } catch (ApiException apiException) {
            return Response.error(apiException.getCode(), apiException.getMessage());
        }
    }


    @PutMapping("/medicine_batch/update")
    public Response<MedicineBatchDto> updateMedicineBatch(@Valid @RequestBody MedicineBatchDto medicineBatchDto) {
        try {
            return Response.success(adminService.updateMedicineBatch(medicineBatchDto));
        } catch (ApiException apiException) {
            return Response.error(apiException.getCode(), apiException.getMessage());
        }
    }

    @PostMapping("/medicine_batch/delete")
    public Response<Void> deleteMedicineBatch(@Valid @RequestBody MedicineBatchDeleteRequestDto medicineBatchDeleteRequestDto) {
        try {
            adminService.deleteMedicineBatch(medicineBatchDeleteRequestDto);
            return Response.success(ApiSharedMesssage.DELETE_SUCCESSFUL);
        } catch (ApiException apiException) {
            return Response.error(apiException.getCode(), apiException.getMessage());
        }
    }

    @PostMapping("/medicine/add")
    public Response<Medicine> addMedicine(@Valid @RequestBody Medicine medicine) {
        try {
            return Response.success(medicineService.addMedicine(medicine));
        } catch (ApiException apiException) {
            return Response.error(apiException.getCode(), apiException.getMessage());
        }
    }


    @PutMapping("/medicine/update")
    public Response<Medicine> updateMedicine(@Valid @RequestBody Medicine medicine) {
        try {
            return Response.success(medicineService.updateMedicine(medicine));
        } catch (ApiException apiException) {
            return Response.error(apiException.getCode(), apiException.getMessage());
        }
    }

    @PostMapping("/medicine/delete")
    public Response<Void> deleteMedicine(@Valid @RequestBody Medicine medicine) {
        try {
            medicineService.deleteMedicine(medicine);
            return Response.success(ApiSharedMesssage.DELETE_SUCCESSFUL);
        } catch (ApiException apiException) {
            return Response.error(apiException.getCode(), apiException.getMessage());
        }
    }

    @GetMapping("building/get")
    public Response<List<BuildingDto>> getBuildingList(){
        try {
            return Response.success(buildingService.getBuildingList());
        } catch (ApiException apiException) {
            return Response.error(apiException.getCode(), apiException.getMessage());
        }
    }

    @PostMapping("/building/add")
    public Response<Building> addBuilding(@Valid @RequestBody Building building) {
        try {
            return Response.success(buildingService.addBuilding(building));
        } catch (ApiException apiException) {
            return Response.error(apiException.getCode(), apiException.getMessage());
        }
    }

    @PutMapping("/building/update")
    public Response<Building> updateBuilding(@Valid @RequestBody Building building) {
        try {
            return Response.success(buildingService.updateBuilding(building));
        } catch (ApiException apiException) {
            return Response.error(apiException.getCode(), apiException.getMessage());
        }
    }

    @PostMapping("/building/delete")
    public Response<Void> deleteBuilding(@Valid @RequestBody Building building) {
        try {
            buildingService.deleteBuilding(building);
            return Response.success(ApiSharedMesssage.DELETE_SUCCESSFUL);
        } catch (ApiException apiException) {
            return Response.error(apiException.getCode(), apiException.getMessage());
        }
    }

    @PostMapping("/floor/get")
    public Response<List<FloorDto>> getFloorList(@Valid @RequestBody Building building) {
        try {
            return Response.success(floorService.getFloorList(building.getBuildingName()));
        } catch (ApiException apiException) {
            return Response.error(apiException.getCode(), apiException.getMessage());
        }
    }

    @PostMapping("/floor/add")
    public Response<FloorDto> addFloor(@Valid @RequestBody FloorDto floorDto) {
        try {
            return Response.success(floorService.addFloor(floorDto));
        } catch (ApiException apiException) {
            return Response.error(apiException.getCode(), apiException.getMessage());
        }
    }

    @PutMapping("/floor/update")
    public Response<FloorDto> updateFloor(@Valid @RequestBody FloorDto floorDto) {
        try {
            return Response.success(floorService.updateFloor(floorDto));
        } catch (ApiException apiException) {
            return Response.error(apiException.getCode(), apiException.getMessage());
        }
    }

    @PostMapping("/floor/delete")
    public Response<Void> deleteFloor(@Valid @RequestBody FloorDto floorDto) {
        try {
            floorService.deleteFloor(floorDto);
            return Response.success(ApiSharedMesssage.DELETE_SUCCESSFUL);
        } catch (ApiException apiException) {
            return Response.error(apiException.getCode(), apiException.getMessage());
        }
    }

    @GetMapping("room_type/get")
    public Response<List<RoomType>> getRoomTypeList(){
        try {
            return Response.success(roomTypeService.getRoomType());
        } catch (ApiException apiException) {
            return Response.error(apiException.getCode(), apiException.getMessage());
        }
    }

    @PostMapping("/room_type/add")
    public Response<RoomType> addRoomType(@Valid @RequestBody RoomType roomType) {
        try {
            return Response.success(roomTypeService.addRoomType(roomType));
        } catch (ApiException apiException) {
            return Response.error(apiException.getCode(), apiException.getMessage());
        }
    }

    @PutMapping("/room_type/update")
    public Response<RoomType> updateRoomType(@Valid @RequestBody RoomType roomType) {
        try {
            return Response.success(roomTypeService.updateRoomType(roomType));
        } catch (ApiException apiException) {
            return Response.error(apiException.getCode(), apiException.getMessage());
        }
    }

    @PostMapping("/room_type/delete")
    public Response<Void> deleteRoomType(@Valid @RequestBody RoomType roomType) {
        try {
            roomTypeService.deleteRoomType(roomType);
            return Response.success(ApiSharedMesssage.DELETE_SUCCESSFUL);
        } catch (ApiException apiException) {
            return Response.error(apiException.getCode(), apiException.getMessage());
        }
    }

    @PostMapping("/room/get")
    public Response<List<RoomDto>> getRoomList(@Valid @RequestBody FloorDto floorDto) {
        try {
            return Response.success(roomService.getRoomList(floorDto));
        } catch (ApiException apiException) {
            return Response.error(apiException.getCode(), apiException.getMessage());
        }
    }

    @PostMapping("/room/add")
    public Response<Integer> addRoom(@Valid @RequestBody RoomDto roomDto) {
        try {
            return Response.success(roomService.addRoom(roomDto));
        } catch (ApiException apiException) {
            return Response.error(apiException.getCode(), apiException.getMessage());
        }
    }

    @PutMapping("/room/update")
    public Response<Integer> updateRoom(@Valid @RequestBody RoomDto roomDto) {
        try {
            return Response.success(roomService.updateRoom(roomDto));
        } catch (ApiException apiException) {
            return Response.error(apiException.getCode(), apiException.getMessage());
        }
    }

    @PostMapping("/room/delete")
    public Response<Void> deleteRoom(@Valid @RequestBody RoomDto roomDto) {
        try {
            roomService.deleteRoom(roomDto);
            return Response.success(ApiSharedMesssage.DELETE_SUCCESSFUL);
        } catch (ApiException apiException) {
            return Response.error(apiException.getCode(), apiException.getMessage());
        }
    }

    @PostMapping("/sickbed/get")
    public Response<List<SickbedDto>> getSickbedList(@Valid @RequestBody RoomDto roomDto) {
        try {
            return Response.success(sickbedService.getSickbedList(roomDto));
        } catch (ApiException apiException) {
            return Response.error(apiException.getCode(), apiException.getMessage());
        }
    }

    @PostMapping("/sickbed/add")
    public Response<Integer> addSickbed(@Valid @RequestBody SickbedDto sickbedDto) {
        try {
            return Response.success(sickbedService.addSickbed(sickbedDto));
        } catch (ApiException apiException) {
            return Response.error(apiException.getCode(), apiException.getMessage());
        }
    }

    @PutMapping("/sickbed/update")
    public Response<Integer> updateSickbed(@Valid @RequestBody SickbedDto sickbedDto) {
        try {
            return Response.success(sickbedService.updateSickbed(sickbedDto));
        } catch (ApiException apiException) {
            return Response.error(apiException.getCode(), apiException.getMessage());
        }
    }

    @PostMapping("/sickbed/delete")
    public Response<Void> deleteSickbed(@Valid @RequestBody SickbedDto sickbedDto) {
        try {
            sickbedService.deleteSickbed(sickbedDto);
            return Response.success(ApiSharedMesssage.DELETE_SUCCESSFUL);
        } catch (ApiException apiException) {
            return Response.error(apiException.getCode(), apiException.getMessage());
        }
    }

    @PostMapping("/sickbed/enable")
    public Response<Void> enableSickbed(@Valid @RequestBody SickbedDto sickbedDto) {
        try {
            sickbedService.enableSickbed(sickbedDto);
            return Response.success(ApiSharedMesssage.DELETE_SUCCESSFUL);
        } catch (ApiException apiException) {
            return Response.error(apiException.getCode(), apiException.getMessage());
        }
    }

    @PostMapping("/sickbed/disable")
    public Response<Void> disableSickbed(@Valid @RequestBody SickbedDto sickbedDto) {
        try {
            sickbedService.disableSickbed(sickbedDto);
            return Response.success(ApiSharedMesssage.DELETE_SUCCESSFUL);
        } catch (ApiException apiException) {
            return Response.error(apiException.getCode(), apiException.getMessage());
        }
    }

    @GetMapping(value = "/info/get")
    public Response<EmployeeDto> getAdminInfo(Principal user) {
        try {
            return Response.success(employeeCommonService.getEmployeeInfo(user.getName()));
        } catch (ApiException apiException) {
            return Response.error(apiException.getCode(), apiException.getMessage());
        }
    }

    @PutMapping("/info/update")
    public Response<EmployeeDto> updateAdminInfo(@RequestBody @Valid EmployeeDto employeeDto, Principal user) {
        try {
            return Response.success(employeeCommonService.updateEmployeeInfo(employeeDto, user.getName()));
        } catch (ApiException apiException) {
            return Response.error(apiException.getCode(), apiException.getMessage());
        }
    }

    @PostMapping(value = "/patient/get")
    public Response<PageResponse<PatientInfoDto>> getPatientList(
            @RequestBody @Valid PatientListRequestDto patientListRequestDto, Principal user) {
        try {
            return Response.success(adminService.getPatientList(patientListRequestDto, user.getName()));
        } catch (ApiException apiException) {
            return Response.error(apiException.getCode(), apiException.getMessage());

        }
    }

    @PostMapping(value = "/medicine/get")
    public Response<PageResponse<MedicineDto>> getMedicineList(
            @RequestBody @Valid MedicineListRequestDto medicineListRequestDto) {
        try {
            return Response.success(medicineService.getMedicineList(medicineListRequestDto));
        } catch (ApiException apiException) {
            return Response.error(apiException.getCode(), apiException.getMessage());

        }
    }

    @PostMapping(value = "/medicine/get/quantity")
    public Response<Long> getAvailableQuantityMedicine(@RequestBody @Valid Medicine medicine) {
        try {
            return Response.success(medicineService.getAvailableQuantityMedicine(medicine.getMedicineName()));
        } catch (ApiException apiException) {
            return Response.error(apiException.getCode(), apiException.getMessage());
        }
    }

    @GetMapping(value = "/stats_type/get")
    public Response<List<StatisticType>> getListStatsType() {
        try {
            return Response.success(adminService.getStatsTypeList());
        } catch (ApiException apiException) {
            return Response.error(apiException.getCode(), apiException.getMessage());
        }
    }

    @PostMapping(value = "/stats_type/add")
    public Response<StatisticType> addStatsType(@RequestBody @Valid StatisticType statisticType) {
        try {
            return Response.success(adminService.createStatsType(statisticType));
        } catch (ApiException apiException) {
            return Response.error(apiException.getCode(), apiException.getMessage());
        }
    }

    @PostMapping(value = "/stats_type/update")
    public Response<StatisticType> updateStatsType(@RequestBody @Valid StatisticType statisticType) {
        try {
            return Response.success(adminService.updateStatsType(statisticType));
        } catch (ApiException apiException) {
            return Response.error(apiException.getCode(), apiException.getMessage());
        }
    }

    @PostMapping(value = "/stats_type/delete")
    public Response<Void> deleteStatsType(@RequestBody @Valid StatisticType statisticType) {
        try {
            adminService.deleteStatsType(statisticType);
            return Response.success(ApiSharedMesssage.DELETE_SUCCESSFUL);
        } catch (ApiException apiException) {
            return Response.error(apiException.getCode(), apiException.getMessage());
        }
    }

    @PostMapping(value = "/doctor/get")
    public Response<PageResponse<DoctorDto>> getDoctorList(
            @RequestBody @Valid DoctorListRequestDto doctorListRequestDto) {
        try {
            return Response.success(adminService.getDoctorList(doctorListRequestDto));
        } catch (ApiException apiException) {
            return Response.error(apiException.getCode(), apiException.getMessage());

        }
    }

    @PostMapping(value = "/nurse/get")
    public Response<PageResponse<NurseDto>> getNurseList(
            @RequestBody @Valid NurseListRequestDto nurseListRequestDto) {
        try {
            return Response.success(adminService.getNurseList(nurseListRequestDto));
        } catch (ApiException apiException) {
            return Response.error(apiException.getCode(), apiException.getMessage());

        }
    }

    @PostMapping("/signup/doctor")
    public Response<EmployeeDto> doctorSignup(@Valid @RequestBody UserSignupDto userSignupDto) {
        try {
            return Response.success(adminService.signupDoctor(userSignupDto));
        } catch (ApiException apiException) {
            return Response.error(apiException.getCode(), apiException.getMessage());
        }
    }

    @PostMapping("/doctor/get-non-group")
    public Response<List<DoctorDto>> doctorNonGroup(@Valid @RequestBody DoctorNonGroupRequestDto doctorNonGroupRequestDto) {
        try {
            return Response.success(adminService.getListDoctorNonGroup(doctorNonGroupRequestDto));
        } catch (ApiException apiException) {
            return Response.error(apiException.getCode(), apiException.getMessage());
        }
    }

    @PostMapping("/signup/nurse")
    public Response<EmployeeDto> nurseSignup(@Valid @RequestBody UserSignupDto userSignupDto) {
        try {
            return Response.success(adminService.signupNurse(userSignupDto));
        } catch (ApiException apiException) {
            return Response.error(apiException.getCode(), apiException.getMessage());
        }
    }

    @PostMapping("/nurse/get-non-group")
    public Response<List<NurseDto>> nurseNonGroup(@Valid @RequestBody NurseNonGroupRequestDto nurseNonGroupRequestDto) {
        try {
            return Response.success(adminService.getListNurseNonGroup(nurseNonGroupRequestDto));
        } catch (ApiException apiException) {
            return Response.error(apiException.getCode(), apiException.getMessage());
        }
    }

    @PostMapping("/bvdc_group/add")
    public Response<BvdcGroupResponseDto> addBvdcGroup(@Valid @RequestBody BvdcGroupDto bvdcGroupDto) {
        try {
            return Response.success(adminService.addBvdcGroup(bvdcGroupDto));
        } catch (ApiException apiException) {
            return Response.error(apiException.getCode(), apiException.getMessage());
        }
    }

    @PutMapping("/bvdc_group/update")
    public Response<BvdcGroupResponseDto> updateBvdcGroup(@Valid @RequestBody BvdcGroupDto bvdcGroupDto) {
        try {
            return Response.success(adminService.updateBvdcGroup(bvdcGroupDto));
        } catch (ApiException apiException) {
            return Response.error(apiException.getCode(), apiException.getMessage());
        }
    }

    @PostMapping("/bvdc_group/delete")
    public Response<Void> adeletevdcGroup(@Valid @RequestBody BvdcGroupDto bvdcGroupDto) {
        try {
            adminService.deleteBvdcGroup(bvdcGroupDto);
            return Response.success(ApiSharedMesssage.TRANSFER_SUCCESSFUL);
        } catch (ApiException apiException) {
            return Response.error(apiException.getCode(), apiException.getMessage());
        }
    }

    @PostMapping("/bvdc_group/get")
    public Response<PageResponse<BvdcGroupResponseDto>> getBvdcList(
            @RequestBody @Valid BvdcGroupListRequestDto bvdcGroupListRequestDto) {
        try {
            return Response.success(adminService.getBvdcGroupList(bvdcGroupListRequestDto));
        } catch (ApiException apiException) {
            return Response.error(apiException.getCode(), apiException.getMessage());

        }
    }

    @GetMapping("/bvdc_group/get")
    public Response<List<BvdcGroupResponseDto>> getListBvdc() {
        try {
            return Response.success(adminService.getListBvdcGroup());
        } catch (ApiException apiException) {
            return Response.error(apiException.getCode(), apiException.getMessage());

        }
    }

    @PostMapping("/bvdc_group/transfer")
    public Response<Void> transferPatient(
            @RequestBody @Valid TransferPatientDto transferPatientDto) {
        try {
            adminService.transferPatient(transferPatientDto.getId1(), transferPatientDto.getId2());
            return Response.success();
        } catch (ApiException apiException) {
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

    @GetMapping("/symptom/get")
    public Response<List<Symptom>> getListSymptom(){
        try {
            return Response.success(adminService.getSymptom());
        } catch (ApiException apiException) {
            return Response.error(apiException.getCode(), apiException.getMessage());
        }
    }

    @PostMapping("/symptom/add")
    public Response<Symptom> addSymptom(@Valid @RequestBody Symptom symptom) {
        try {
            return Response.success(adminService.addSymptom(symptom));
        } catch (ApiException apiException) {
            return Response.error(apiException.getCode(), apiException.getMessage());
        }
    }


    @PutMapping("/symptom/update")
    public Response<Symptom> updateSymptom(@Valid @RequestBody Symptom symptom) {
        try {
            return Response.success(adminService.updateSymptom(symptom));
        } catch (ApiException apiException) {
            return Response.error(apiException.getCode(), apiException.getMessage());
        }
    }

    @PostMapping("/symptom/delete")
    public Response<Void> deleteSymptom(@Valid @RequestBody Symptom symptom) {
        try {
            adminService.deleteSymptom(symptom);
            return Response.success(ApiSharedMesssage.DELETE_SUCCESSFUL);
        } catch (ApiException apiException) {
            return Response.error(apiException.getCode(), apiException.getMessage());
        }
    }

    @GetMapping("medical_device_type/get")
    public Response<List<MedicalDeviceType>> getMedicalDeviceTypeList(){
        try {
            return Response.success(medicalDeviceTypeService.getMedicalDeviceTypeList());
        } catch (ApiException apiException) {
            return Response.error(apiException.getCode(), apiException.getMessage());
        }
    }

    @PostMapping("/medical_device_type/add")
    public Response<MedicalDeviceType> addMedicalDeviceType(@Valid @RequestBody MedicalDeviceType medicalDeviceType) {
        try {
            return Response.success(medicalDeviceTypeService.addMedicalDeviceType(medicalDeviceType));
        } catch (ApiException apiException) {
            return Response.error(apiException.getCode(), apiException.getMessage());
        }
    }

    @PostMapping("/medical_device_type/delete")
    public Response<Void> deleteMedicalDeviceType(@Valid @RequestBody MedicalDeviceType medicalDeviceType) {
        try {
            medicalDeviceTypeService.deleteMedicalDeviceType(medicalDeviceType);
            return Response.success(ApiSharedMesssage.DELETE_SUCCESSFUL);
        } catch (ApiException apiException) {
            return Response.error(apiException.getCode(), apiException.getMessage());
        }
    }

    @PostMapping("/medical_device/get")
    public Response<PageResponse<MedicalDeviceDto>> getMedicalDeviceList(@Valid @RequestBody MedicalDeviceListRequestDto medicalDeviceListRequestDto) {
        try {
            return Response.success(medicalDeviceService.getMedicalDeviceList(medicalDeviceListRequestDto));
        } catch (ApiException apiException) {
            return Response.error(apiException.getCode(), apiException.getMessage());
        }
    }

    @PostMapping("/medical_device/add")
    public Response<Integer> addMedicalDevice(@Valid @RequestBody MedicalDeviceDto medicalDeviceDto) {
        try {
            return Response.success(medicalDeviceService.addMedicalDevice(medicalDeviceDto));
        } catch (ApiException apiException) {
            return Response.error(apiException.getCode(), apiException.getMessage());
        }
    }
    @PostMapping("/medical_device/update")
    public Response<Integer> updateMedicalDevice(@Valid @RequestBody MedicalDeviceDto medicalDeviceDto) {
        try {
            return Response.success(medicalDeviceService.updateMedicalDevice(medicalDeviceDto));
        } catch (ApiException apiException) {
            return Response.error(apiException.getCode(), apiException.getMessage());
        }
    }

    @PostMapping("/medical_device/delete")
    public Response<Void> deleteMedicalDevice(@Valid @RequestBody MedicalDeviceDto medicalDeviceDto) {
        try {
            medicalDeviceService.deleteMedicalDevice(medicalDeviceDto);
            return Response.success(ApiSharedMesssage.DELETE_SUCCESSFUL);
        } catch (ApiException apiException) {
            return Response.error(apiException.getCode(), apiException.getMessage());
        }
    }

    @GetMapping("covid_hospital/get")
    public Response<List<CovidHospital>> getCovidHospitalList(){
        try {
            return Response.success(covidHospitalService.getCovidHospitalList());
        } catch (ApiException apiException) {
            return Response.error(apiException.getCode(), apiException.getMessage());
        }
    }

    @PostMapping("/covid_hospital/add")
    public Response<CovidHospital> addCovidHospital(@Valid @RequestBody CovidHospital covidHospital) {
        try {
            return Response.success(covidHospitalService.addCovidHospital(covidHospital));
        } catch (ApiException apiException) {
            return Response.error(apiException.getCode(), apiException.getMessage());
        }
    }

    @PutMapping("/covid_hospital/update")
    public Response<CovidHospital> updateCovidHospital(@Valid @RequestBody CovidHospital covidHospital) {
        try {
            return Response.success(covidHospitalService.updateCovidHospital(covidHospital));
        } catch (ApiException apiException) {
            return Response.error(apiException.getCode(), apiException.getMessage());
        }
    }

    @PostMapping("/covid_hospital/delete")
    public Response<Void> deleteCovidHospital(@Valid @RequestBody CovidHospital covidHospital) {
        try {
            covidHospitalService.deleteCovidHospital(covidHospital);
            return Response.success(ApiSharedMesssage.DELETE_SUCCESSFUL);
        } catch (ApiException apiException) {
            return Response.error(apiException.getCode(), apiException.getMessage());
        }
    }

    @GetMapping("medical_record/{username}")
    public Response<MedicalRecordDto> getMedicalRecord (@PathVariable String username){
        try {
            return Response.success(adminService.getMedicalRecord(username));
        } catch (ApiException apiException) {
            return Response.error(apiException.getCode(), apiException.getMessage());
        }
    }

    @GetMapping("dashboard/patient")
    public Response<PatientDashboardDto> getPatientDashboard (){
        try {
            return Response.success(adminService.getPatientDashboard());
        } catch (ApiException apiException) {
            return Response.error(apiException.getCode(), apiException.getMessage());
        }
    }

    @GetMapping("dashboard/sickbed")
    public Response<SickbedDashboardDto> getSickbedDashboard (){
        try {
            return Response.success(adminService.getSickbedDashboard());
        } catch (ApiException apiException) {
            return Response.error(apiException.getCode(), apiException.getMessage());
        }
    }

    @GetMapping("dashboard/test_in_week")
    public Response<List<CountTestInAWeekDto>> getTestInWeek (){
        try {
            return Response.success(adminService.getCountTestInAWeek());
        } catch (ApiException apiException) {
            return Response.error(apiException.getCode(), apiException.getMessage());
        }
    }

    @GetMapping("importpatient/get")
    public Response<List<ImportPatientDto>> getImportPatient (){
        try {
            return Response.success(userService.listImportPatientIsReady());
        } catch (ApiException apiException) {
            return Response.error(apiException.getCode(), apiException.getMessage());
        }
    }

    @PostMapping("/importpatient/import")
    public Response<Void> importPatient(@Valid @RequestBody ImportPatientDto importPatientDto) {
        try {
            userService.importPatient(importPatientDto);
            return Response.success(ApiSharedMesssage.TRANSFER_SUCCESSFUL);
        } catch (ApiException apiException) {
            return Response.error(apiException.getCode(), apiException.getMessage());
        }
    }

    @PostMapping("/importpatient/delete")
    public Response<Void> deleteImportPatient(@Valid @RequestBody ImportPatientDto importPatientDto) {
        try {
            userService.deleteImportPatient(importPatientDto.getId());
            return Response.success(ApiSharedMesssage.DELETE_SUCCESSFUL);
        } catch (ApiException apiException) {
            return Response.error(apiException.getCode(), apiException.getMessage());
        }
    }





}
