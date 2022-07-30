package com.lvtn.module.admin.mapper;

import com.lvtn.module.admin.dto.*;
import com.lvtn.module.shared.model.BvdcGroup;
import com.lvtn.module.shared.model.Doctor;
import com.lvtn.module.shared.model.MedicineBatch;
import com.lvtn.module.shared.model.Nurse;
import com.lvtn.module.shared.repository.DoctorRepository;
import com.lvtn.module.shared.repository.MedicineRepository;
import com.lvtn.module.shared.repository.NurseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class AdminMapper {
    @Autowired
    MedicineRepository medicineRepository;
    @Autowired
    DoctorRepository doctorRepository;
    @Autowired
    NurseRepository nurseRepository;

    public MedicineBatch mapMedicineBatchDtoToMedicineBatch(MedicineBatchDto medicineBatchDto) {
        MedicineBatch medicineBatch = MedicineBatch.builder()
                .id(medicineBatchDto.getId())
                .expiredDate(medicineBatchDto.getExpiredDate())
                .inputDate(medicineBatchDto.getInputDate())
                .availableQuantity(medicineBatchDto.getAvailableQuantity())
                .batchNumber(medicineBatchDto.getBatchNumber())
                .medicineName(medicineRepository.getById(medicineBatchDto.getMedicineName())).build();
        return medicineBatch;
    }

    public MedicineBatchDto mapMedicineBatchToMedicineBatchDto(MedicineBatch medicineBatch) {
        MedicineBatchDto medicineBatchDto = MedicineBatchDto.builder()
                .id(medicineBatch.getId())
                .expiredDate(medicineBatch.getExpiredDate())
                .inputDate(medicineBatch.getInputDate())
                .availableQuantity(medicineBatch.getAvailableQuantity())
                .batchNumber(medicineBatch.getBatchNumber())
                .medicineName(medicineBatch.getMedicineName().getMedicineName()).build();
        return medicineBatchDto;
    }

    public BvdcGroup mapBvdcGroupDtoToBvdcGroup(BvdcGroupDto bvdcGroupDto) {
        BvdcGroup bvdcGroup = BvdcGroup.builder()
                .id(bvdcGroupDto.getId())
                .groupType(bvdcGroupDto.getGroupType())
                .doctors(bvdcGroupDto.getDoctors().stream().map(name -> doctorRepository.findDoctorByUser_Username(name)).collect(Collectors.toList()))
                .nurses(bvdcGroupDto.getNurses().stream().map(name -> nurseRepository.findNurseByUser_Username(name)).collect(Collectors.toList()))
                .build();
        return bvdcGroup;
    }

    public BvdcGroupDto mapBvdcGroupToBvdcGroupDto(BvdcGroup bvdcGroup) {
        BvdcGroupDto bvdcGroupDto = BvdcGroupDto.builder()
                .id(bvdcGroup.getId())
                .groupType(bvdcGroup.getGroupType())
                .doctors(bvdcGroup.getDoctors().stream().map(doctor -> doctor.getUser().getUsername()).collect(Collectors.toList()))
                .nurses(bvdcGroup.getNurses().stream().map(nurse -> nurse.getUser().getUsername()).collect(Collectors.toList()))
                .build();
        return bvdcGroupDto;
    }

    public BvdcGroupResponseDto mapBvdcGroupToBvdcGroupRespsonseDto(BvdcGroup bvdcGroup) {
        BvdcGroupResponseDto bvdcGroupResponseDto = BvdcGroupResponseDto.builder()
                .id(bvdcGroup.getId())
                .groupType(bvdcGroup.getGroupType())
                .doctors(bvdcGroup.getDoctors().stream().map(doctor -> doctor.getUser().getUsername()).collect(Collectors.toList()))
                .nurses(bvdcGroup.getNurses().stream().map(nurse -> nurse.getUser().getUsername()).collect(Collectors.toList()))
                .doctorsNames(bvdcGroup.getDoctors().stream().map(doctor -> doctor.getUser().getName()).collect(Collectors.toList()))
                .nursesNames(bvdcGroup.getNurses().stream().map(nurse -> nurse.getUser().getName()).collect(Collectors.toList()))
                .sumPatient((Objects.nonNull(bvdcGroup.getPatient())) ? bvdcGroup.getPatient().size() : 0)
                .build();
        return bvdcGroupResponseDto;
    }

    public DoctorDto mapDoctorToDoctorDto(Doctor doctor) {
        DoctorDto doctorDto = new DoctorDto();
        doctorDto.setId(doctor.getId());
        doctorDto.setShift(doctor.getShift());
        doctorDto.setBvdcGroups(doctor.getBvdcGroups());
        doctorDto.setUser(doctor.getUser());
        return doctorDto;
    }

    public NurseDto mapNurseToNurseDto(Nurse nurse) {
        NurseDto nurseDto = new NurseDto();
        nurseDto.setId(nurse.getId());
        nurseDto.setShift(nurse.getShift());
        nurseDto.setBvdcGroups(nurse.getBvdcGroups());
        nurseDto.setUser(nurse.getUser());
        return nurseDto;
    }

}