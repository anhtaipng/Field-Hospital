package com.lvtn.module.shared.service.implementation;

import com.lvtn.module.shared.common.ApiSharedMesssage;
import com.lvtn.module.shared.dto.BuildingDto;
import com.lvtn.module.shared.mapper.SickbedMapper;
import com.lvtn.module.shared.model.Building;
import com.lvtn.module.shared.model.CovidHospital;
import com.lvtn.module.shared.repository.BuildingRepository;
import com.lvtn.module.shared.repository.CovidHospitalRepository;
import com.lvtn.module.shared.service.BuildingService;
import com.lvtn.module.shared.service.CovidHospitalService;
import com.lvtn.platform.exception.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CovidHospitalServiceImpl implements CovidHospitalService {
    @Autowired
    CovidHospitalRepository covidHospitalRepository;

    @Override
    public CovidHospital addCovidHospital(CovidHospital covidHospital) {
        if (covidHospitalRepository.findById(covidHospital.getName()).isPresent()) {
            throw new ApiException(ApiSharedMesssage.HOSPITAL_EXISTED);
        }
        return covidHospitalRepository.save(covidHospital);
    }

    @Override
    public CovidHospital updateCovidHospital(CovidHospital covidHospital) {
        if (covidHospitalRepository.findById(covidHospital.getName()).isEmpty()) {
            throw new ApiException(ApiSharedMesssage.HOSPITAL_NOT_FOUND);
        }
        return covidHospitalRepository.save(covidHospital);
    }

    @Override
    public void deleteCovidHospital(CovidHospital covidHospital) {
        if (covidHospitalRepository.findById(covidHospital.getName()).isEmpty()) {
            throw new ApiException(ApiSharedMesssage.HOSPITAL_NOT_FOUND);
        }
        covidHospitalRepository.delete(covidHospital);
    }

    @Override
    public List<CovidHospital> getCovidHospitalList() {
        return covidHospitalRepository.findAll();
    }

    @Override
    public List<CovidHospital> getCovidHospitalEnableList() {
        return covidHospitalRepository.findAllByEnableTrue();
    }
}
