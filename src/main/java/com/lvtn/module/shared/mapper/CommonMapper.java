package com.lvtn.module.shared.mapper;

import com.google.common.base.Strings;
import com.lvtn.module.admin.mapper.AdminMapper;
import com.lvtn.module.shared.dto.*;
import com.lvtn.module.shared.enumeration.PatientStatusType;
import com.lvtn.module.shared.enumeration.RequirementStatus;
import com.lvtn.module.shared.model.*;
import com.lvtn.module.shared.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CommonMapper {

    @Autowired
    PatientMapper patientMapper;

    @Autowired
    PrescriptionDetailRepository prescriptionDetailRepository;

    @Autowired
    TestTypeRepository testTypeRepository;

    @Autowired
    StatisticTypeRepository statisticTypeRepository;

    @Autowired
    PatientStatusRepository patientStatusRepository;

    @Autowired
    SickbedRepository sickbedRepository;

    @Autowired
    PatientLocationRepository patientLocationRepository;

    @Autowired
    MedicalDeviceRepository medicalDeviceRepository;

    @Autowired
    CovidHospitalRepository covidHospitalRepository;

    @Autowired
    AdminMapper adminMapper;

    @Autowired
    PatientRepository patientRepository;

    public MedicineRequirement mapMedicineRequirementDtoToMedicineRequirement(Doctor doctor,MedicineRequirementDto medicineRequirementDto) {
        MedicineRequirement medicineRequirement = new MedicineRequirement();
        medicineRequirement.setMedicineName(medicineRequirementDto.getMedicineName());
        medicineRequirement.setRequirementTime(medicineRequirementDto.getRequirementTime());
        medicineRequirement.setStatus(medicineRequirementDto.getStatus());
        medicineRequirement.setId(medicineRequirementDto.getId());
        medicineRequirement.setQuantity(medicineRequirementDto.getQuantity());
        medicineRequirement.setDoctor(doctor);
        return medicineRequirement;
    }

    public MedicineRequirementDto mapMedicineRequirementToMedicineRequirementDto(MedicineRequirement medicineRequirement) {
        MedicineRequirementDto medicineRequirementDto  = new MedicineRequirementDto();
        medicineRequirementDto.setMedicineName(medicineRequirement.getMedicineName());
        medicineRequirementDto.setRequirementTime(medicineRequirement.getRequirementTime());
        medicineRequirementDto.setStatus(medicineRequirement.getStatus());
        medicineRequirementDto.setId(medicineRequirement.getId());
        medicineRequirementDto.setQuantity(medicineRequirement.getQuantity());
        medicineRequirementDto.setDoctorCMND(medicineRequirement.getDoctor().getUser().getUsername());
        return medicineRequirementDto;
    }

    public PatientLocationDto mapSickbedToPatientLocationResponseDto(Sickbed sickbed){
        if (sickbed!=null) {
            PatientLocationDto patientLocationDto = new PatientLocationDto();
            patientLocationDto.setSickbedNo(sickbed.getSickBedNo());
            patientLocationDto.setRoomNo(sickbed.getRoom().getRoomNo());
            patientLocationDto.setRoomType(sickbed.getRoom().getRoomType().getRoomType());
            patientLocationDto.setFloorNo(sickbed.getRoom().getFloor().getFloorNo());
            patientLocationDto.setBuildingName(sickbed.getRoom().getFloor().getBuilding().getBuildingName());
            patientLocationDto.setSickbedStatus(sickbed.getSickbedStatus());
            if (sickbed.getMedicalDevices()!=null) {
                List<MedicalDeviceDto> medicalDeviceDtoList = sickbed.getMedicalDevices().stream().map(this::mapMedicalDeviceToMedicalDeviceDto).collect(Collectors.toList());
                patientLocationDto.setMedicalDeviceDtoList(medicalDeviceDtoList);
            }
            return patientLocationDto;
        }
        return null;
    }

    public Sickbed mapPatientLocationDtoToSickbed(PatientLocationDto patientLocationDto){
        Building building = new Building();
        building.setBuildingName(patientLocationDto.getBuildingName());
        Floor floor = new Floor();
        floor.setBuilding(building);
        floor.setFloorNo(patientLocationDto.getFloorNo());
        RoomType roomType = new RoomType();
        roomType.setRoomType(roomType.getRoomType());
        Room room = new Room();
        room.setFloor(floor);
        room.setRoomType(roomType);
        room.setRoomNo(patientLocationDto.getRoomNo());
        Sickbed sickbed = new Sickbed();
        sickbed.setSickbedStatus(patientLocationDto.getSickbedStatus());
        sickbed.setRoom(room);
        return sickbed;
    }

    public PatientLocation mapPatientLocationDtoToPatientLocation(Patient patient, Sickbed sickbed,PatientLocationDto patientLocationDto){
        PatientLocation patientLocation = new PatientLocation();
        patientLocation.setPatient(patient);
        patientLocation.setSickbed(sickbed);
        patientLocation.setStartTime(patientLocationDto.getStartTime());
        patientLocation.setEndTime(patientLocationDto.getEndTime());
        patientLocation.setId(patientLocationDto.getId());
        return patientLocation;
    }

    public DependentDto mapDependentToDependentDto(Dependent dependent){
        DependentDto dependentDto = new DependentDto();
        dependentDto.setEmail(dependent.getEmail());
        dependentDto.setName(dependent.getName());
        dependentDto.setRelationShip(dependent.getRelationShip());
        dependentDto.setPhone(dependent.getPhone());
        return dependentDto;
    }

    public Dependent mapDependentDtoToDependent(Patient patient,DependentDto dependentDto){
        Dependent dependent = new Dependent();
        dependent.setEmail(dependentDto.getEmail());
        dependent.setName(dependentDto.getName());
        dependent.setRelationShip(dependentDto.getRelationShip());
        dependent.setPhone(dependentDto.getPhone());
        dependent.setPatient(patient);
        return dependent;
    }

    public PatientLocationDto mapPatientLocationToPatientLocationResponseDto(PatientLocation patientLocation) {
        PatientLocationDto patientLocationDto = new PatientLocationDto();
        patientLocationDto.setStartTime(patientLocation.getStartTime());
        patientLocationDto.setEndTime(patientLocation.getEndTime());
        patientLocationDto.setBuildingName(patientLocation.getSickbed().getRoom().getFloor().getBuilding().getBuildingName());
        patientLocationDto.setFloorNo(patientLocation.getSickbed().getRoom().getFloor().getFloorNo());
        patientLocationDto.setRoomNo(patientLocation.getSickbed().getRoom().getRoomNo());
        patientLocationDto.setRoomType(patientLocation.getSickbed().getRoom().getRoomType().getRoomType());
        patientLocationDto.setSickbedNo(patientLocation.getSickbed().getSickBedNo());
        patientLocationDto.setId(patientLocation.getId());
        patientLocationDto.setSickbedStatus(patientLocation.getSickbed().getSickbedStatus());
        return patientLocationDto;
    }


    public RequirementDto mapRequirementToRequirementResponseDto(Requirement requirement){
        RequirementDto requirementResponseDto = new RequirementDto();
        requirementResponseDto.setRequirementType(requirement.getRequirementType());
        requirementResponseDto.setCreateTime(requirement.getCreateTime());
        if (requirement.getSickbed()!=null) {
            requirementResponseDto.setPatientLocation(this.mapSickbedToPatientLocationResponseDto(requirement.getSickbed()));
        }
        requirementResponseDto.setExecutionTime(requirement.getExecutionTime());
        if (requirement.getTestType()!=null) {
            requirementResponseDto.setTestType(requirement.getTestType().getTestType());
        }
        requirementResponseDto.setStatus(requirement.getStatus());
        if (requirement.getPatientStatus()!=null) {
            requirementResponseDto.setPatientStatusType(requirement.getPatientStatus().getPatientStatusType());
        }
        if (requirement.getCovidHospital()!=null) {
            requirementResponseDto.setCovidHospital(requirement.getCovidHospital().getName());
        }
        if (requirement.getStatisticTypes()!=null) {
            requirementResponseDto.setStatisticType(requirement.getStatisticTypes().getStatisticType());
        }
        if (requirement.getPatient().getBvdcGroup()!=null) {
            requirementResponseDto.setGroupType(requirement.getPatient().getBvdcGroup().getGroupType());
        }
        requirementResponseDto.setPatientCmnd(requirement.getPatient().getUser().getUsername());
        requirementResponseDto.setPatientName(requirement.getPatient().getUser().getName());
        return requirementResponseDto;
    }

    public PrescriptionDetailDto mapPrescriptionDetailToPrescriptionDetailResponseDto(PrescriptionDetail prescriptionDetail){
        PrescriptionDetailDto prescriptionDetailResponseDto = new PrescriptionDetailDto();
        prescriptionDetailResponseDto.setAmount(prescriptionDetail.getAmount());
        prescriptionDetailResponseDto.setMedicine(prescriptionDetail.getMedicine());
        prescriptionDetailResponseDto.setFromDay(prescriptionDetail.getFromDay());
        prescriptionDetailResponseDto.setNote(prescriptionDetail.getNote());
        prescriptionDetailResponseDto.setQuantity(prescriptionDetail.getQuantity());
        prescriptionDetailResponseDto.setToDay(prescriptionDetail.getToDay());
        return prescriptionDetailResponseDto;
    }

    public PrescriptionResponseDto mapPrescriptionToPrescriptionResponseDto(Prescription prescription, List<PrescriptionDetail> prescriptionDetailList,Examination examination){
        PrescriptionResponseDto prescriptionResponseDto = new PrescriptionResponseDto();
        if (examination!=null) {
            prescriptionResponseDto.setDiagnostic(examination.getDiagnostic());
            prescriptionResponseDto.setNote(examination.getNote());
            prescriptionResponseDto.setDoctorName(examination.getDoctor().getUser().getName());
            prescriptionResponseDto.setDoctorCmnd(examination.getDoctor().getUser().getUsername());
        }
        prescriptionResponseDto.setPatientCmnd(prescription.getPatient().getUser().getUsername());
        prescriptionResponseDto.setPatientName(prescription.getPatient().getUser().getName());
        prescriptionResponseDto.setTimeCreated(prescription.getCreateTime());
        prescriptionResponseDto.setId(prescription.getId());
        prescriptionResponseDto.setCheck(prescription.getTook());
        if (prescriptionDetailList!=null) {
            prescriptionResponseDto.setPrescriptionList(prescriptionDetailList.stream().map(this::mapPrescriptionDetailToPrescriptionDetailResponseDto).collect(Collectors.toList()));
        }
        return prescriptionResponseDto;
    }

    public PrescriptionResponseDto mapPrescriptionToPrescriptionResponseDto(Prescription prescription){
        PrescriptionResponseDto prescriptionResponseDto = new PrescriptionResponseDto();
        prescriptionResponseDto.setPatientCmnd(prescription.getPatient().getUser().getUsername());
        prescriptionResponseDto.setPatientName(prescription.getPatient().getUser().getName());
        prescriptionResponseDto.setTimeCreated(prescription.getCreateTime());
        prescriptionResponseDto.setId(prescription.getId());
        prescriptionResponseDto.setCheck(prescription.getTook());
        return prescriptionResponseDto;
    }

    public ExaminationDto mapExaminationToExaminationResponseDto(Examination examination){
        ExaminationDto examinationResponseDto = new ExaminationDto();
        examinationResponseDto.setGroupType(examination.getGroupType());
        examinationResponseDto.setExaminationTime(examination.getExaminationTime());
        examinationResponseDto.setDiagnostic(examination.getDiagnostic());
        examinationResponseDto.setDoctorName(examination.getDoctor().getUser().getName());
        examinationResponseDto.setPatientName(examination.getPatient().getUser().getName());
        examinationResponseDto.setPatient(examination.getDoctor().getUser().getUsername());
        examinationResponseDto.setDoctor(examination.getPatient().getUser().getUsername());
        examinationResponseDto.setPatient(examination.getPatient().getUser().getUsername());
        if (examination.getRequirements()!=null) {
            examinationResponseDto.setRequirements(examination.getRequirements().stream().map(this::mapRequirementToRequirementResponseDto).collect(Collectors.toList()));
        } else {
            examinationResponseDto.setRequirements(null);
        }
        if (examination.getPrescription()!=null) {
            examinationResponseDto.setPrescriptionDetail(prescriptionDetailRepository.findByPrescription(examination.getPrescription()).stream().map(
                    this::mapPrescriptionDetailToPrescriptionDetailResponseDto
            ).collect(Collectors.toList()));
        }
        examinationResponseDto.setSymptoms(examination.getSymptoms());
        examinationResponseDto.setNote(examination.getNote());
        return examinationResponseDto;
    }

    public Requirement mapRequirementDtoToRequirement(RequirementDto requirementDto, Patient patient, Date createTime){
        Requirement requirement = new Requirement();
        requirement.setRequirementType(requirementDto.getRequirementType());
        requirement.setExecutionTime(requirementDto.getExecutionTime());
        if (requirementDto.getTestType() != null) {
            requirement.setTestType(testTypeRepository.getById(requirementDto.getTestType()));
        }
        if (requirementDto.getPatientStatusType()!=null) {
            if (requirementDto.getPatientStatusType().equals(PatientStatusType.CHUYEN_VIEN)) {
                patient.setCurrentPatientStatus(PatientStatusType.REQUEST_CHUYEN_VIEN);
                patientRepository.save(patient);
            } else if (requirementDto.getPatientStatusType().equals(PatientStatusType.XUAT_VIEN)) {
                patient.setCurrentPatientStatus(PatientStatusType.REQUEST_XUAT_VIEN);
                patientRepository.save(patient);
            } else if (requirementDto.getPatientStatusType().equals(PatientStatusType.TU_VONG)) {
                patient.setCurrentPatientStatus(PatientStatusType.REQUEST_TU_VONG);
                patientRepository.save(patient);
            }
            requirement.setPatientStatus(patientStatusRepository.getById(requirementDto.getPatientStatusType()));
            if (requirementDto.getPatientStatusType().equals(patient.getPatientStatus().getPatientStatusType())) {
                requirementDto.setStatus(RequirementStatus.HOAN_THANH);
                requirementDto.setExecutionTime(new Date());
            }
        }
        if (requirementDto.getCovidHospital()!=null) {
            requirement.setCovidHospital(covidHospitalRepository.getById(requirementDto.getCovidHospital()));
        }
        if (requirementDto.getStatisticType()!=null) {
            if (requirementDto.getStatisticType().equals("Huyết áp")) {
                requirement.setStatisticTypes(statisticTypeRepository.getById("Huyết áp tâm thu"));
            } else {
                requirement.setStatisticTypes(statisticTypeRepository.getById(requirementDto.getStatisticType()));
            }
        }
        if (requirementDto.getCreateTime()!=null) {
            requirement.setCreateTime(requirementDto.getCreateTime());
        } else if (createTime!=null) {
            requirement.setCreateTime(createTime);
        } else {
            requirement.setCreateTime(new Date());
        }
        if (requirementDto.getPatientLocation()!=null) {
            requirement.setSickbed(sickbedRepository.getById(mapPatientLocationDtoToSickbedPK(requirementDto.getPatientLocation())));
            PatientLocation currentPatientLocation = patientLocationRepository.findByPatientAndEndTimeIsNull(patient);
            if (currentPatientLocation!=null && currentPatientLocation.getSickbed().getRoom().getRoomType().getRoomType().equals(requirementDto.getPatientLocation().getRoomType())) {
                requirementDto.setStatus(RequirementStatus.HOAN_THANH);
            }
        }
        if (requirementDto.getStatus()!=null) {
            requirement.setStatus(requirementDto.getStatus());
        }else {
            requirement.setStatus(RequirementStatus.DANG_THUC_HIEN);
        }
        requirement.setPatient(patient);
        return requirement;
    }

    public PrescriptionDetail mapPrescriptionDetailDtoToPrescriptionDetail(PrescriptionDetailDto prescriptionDetailDto,Medicine medicine, Prescription prescription){
        PrescriptionDetail prescriptionDetail = new PrescriptionDetail();
        prescriptionDetail.setAmount(prescriptionDetailDto.getAmount());
        prescriptionDetail.setMedicine(medicine);
        prescriptionDetail.setPrescription(prescription);
        prescriptionDetail.setFromDay(prescriptionDetailDto.getFromDay());
        prescriptionDetail.setNote(prescriptionDetailDto.getNote());
        prescriptionDetail.setQuantity(prescriptionDetailDto.getQuantity());
        prescriptionDetail.setToDay(prescriptionDetailDto.getToDay());
        return prescriptionDetail;
    }

    public Examination mapExaminationDtoToExamination(Doctor doctor, Patient patient, Prescription prescription, ExaminationDto examinationDto){
        Examination examination = new Examination();
        examination.setPrescription(prescription);
        examination.setExaminationTime(examinationDto.getExaminationTime());
        examination.setDiagnostic(examinationDto.getDiagnostic());
        examination.setDoctor(doctor);
        examination.setPatient(patient);
        examination.setGroupType(examinationDto.getGroupType());
        if (examinationDto.getRequirements()!=null) {
            examination.setRequirements(examinationDto.getRequirements().stream().map(requirementDto -> mapRequirementDtoToRequirement(requirementDto, patient, examinationDto.getExaminationTime())).collect(Collectors.toList()));
        } else {
            examination.setRequirements(null);
        }
        examination.setSymptoms(examinationDto.getSymptoms());
        examination.setNote(examinationDto.getNote());
        return examination;
    }

    public SickbedPK mapPatientLocationDtoToSickbedPK(PatientLocationDto patientLocationDto){
        Building building = new Building();
        building.setBuildingName(patientLocationDto.getBuildingName());
        Floor floor = new Floor();
        floor.setFloorNo(patientLocationDto.getFloorNo());
        floor.setBuilding(building);
        RoomType roomType = new RoomType();
        roomType.setRoomType(patientLocationDto.getRoomType());
        Room room = new Room();
        room.setRoomNo(patientLocationDto.getRoomNo());
        room.setRoomType(roomType);
        room.setFloor(floor);
        SickbedPK sickbedPK = new SickbedPK();
        sickbedPK.setSickBedNo(patientLocationDto.getSickbedNo());
        sickbedPK.setRoom(room);
        return sickbedPK;
    }

    public Notification mapNotificationDtoToNotification(NotificationDto notificationDto,User user){
       Notification notification = new Notification();
       notification.setId(notificationDto.getId());
       notification.setDescription(notificationDto.getDescription());
       notification.setTitle(notificationDto.getTitle());
       notification.setStatus(false);
       notification.setCreateTime(new Date());
       notification.setUser(user);
       notification.setNotificationType(notificationDto.getNotificationType());
       return notification;
    }

    public NotificationDto mapNotificationToNotificationDto(Notification notification){
        NotificationDto notificationDto = new NotificationDto();
        notificationDto.setCmnd(notification.getUser().getUsername());
        notificationDto.setDescription(notification.getDescription());
        notificationDto.setTitle(notification.getTitle());
        notificationDto.setCreateTime(notification.getCreateTime());
        notificationDto.setStatus(notification.isStatus());
        notificationDto.setId(notification.getId());
        notificationDto.setNotificationType(notification.getNotificationType());
        return notificationDto;
    }


    public MedicineBatchHistoryDto mapMedicineBatchHistoryToMedicineBatchHistoryDto(MedicineBatchHistory medicineBatchHistory){
        MedicineBatchHistoryDto medicineBatchHistoryDto = new MedicineBatchHistoryDto();
        medicineBatchHistoryDto.setMedicineBatch(adminMapper.mapMedicineBatchToMedicineBatchDto(medicineBatchHistory.getMedicineBatch()));
        medicineBatchHistoryDto.setId(medicineBatchHistory.getId());
        medicineBatchHistoryDto.setQuantityDeducted(medicineBatchHistory.getQuantityDeducted());
        medicineBatchHistoryDto.setTimeCreated(medicineBatchHistory.getTimeCreated());
        return medicineBatchHistoryDto;
    }

    public PrescriptionDto mapPrescriptionBatchToPrescriptionDto(Prescription prescription,List<MedicineBatchHistory> medicineBatchHistoryList){
        PrescriptionDto prescriptionDto = new PrescriptionDto();
        prescriptionDto.setPrescriptionId(prescription.getId());
        prescriptionDto.setCheck(prescription.getTook());
        prescriptionDto.setTimeCreated(prescription.getCreateTime());
        prescriptionDto.setMedicineBatchHistoryList(medicineBatchHistoryList.stream().map(this::mapMedicineBatchHistoryToMedicineBatchHistoryDto).collect(Collectors.toList()));
        return prescriptionDto;
    }

    public MedicalDeviceDto mapMedicalDeviceToMedicalDeviceDto(MedicalDevice medicalDevice){
        MedicalDeviceDto medicalDeviceDto = new MedicalDeviceDto();
        medicalDeviceDto.setId(medicalDevice.getId());
        medicalDeviceDto.setMedicalDeviceType(medicalDevice.getMedicalDeviceType().getMedicalDeviceType());
        medicalDeviceDto.setMedicalDeviceNumber(medicalDevice.getMedicalDeviceNumber());
        medicalDeviceDto.setBuildingName(medicalDevice.getSickbed().getRoom().getFloor().getBuilding().getBuildingName());
        medicalDeviceDto.setFloorNo(medicalDevice.getSickbed().getRoom().getFloor().getFloorNo());
        medicalDeviceDto.setRoomNo(medicalDevice.getSickbed().getRoom().getRoomNo());
        medicalDeviceDto.setSickbedNo(medicalDevice.getSickbed().getSickBedNo());
        return medicalDeviceDto;
    }

}
