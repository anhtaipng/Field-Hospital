package com.lvtn.module.shared.repository;

import com.lvtn.module.shared.model.Doctor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BvdcGroupRepositoryTest {

    @Autowired
    BvdcGroupRepository bvdcGroupRepository;
    @Autowired
    DoctorRepository doctorRepository;

    @Test
    void selectGroup() {
//        Pageable pageable = PageRequest.of(0,1);
////        List<Doctor> doctorList = doctorRepository.findAll();
////        System.out.println(doctorList);
//        List<Doctor> doctors = bvdcGroupRepository.selectGroup();
//        System.out.println(doctors);
    }
}