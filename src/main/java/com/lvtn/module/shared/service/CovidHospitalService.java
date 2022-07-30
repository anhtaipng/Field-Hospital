package com.lvtn.module.shared.service;

import com.lvtn.module.shared.dto.BuildingDto;
import com.lvtn.module.shared.model.Building;
import com.lvtn.module.shared.model.CovidHospital;

import java.util.List;

public interface CovidHospitalService {
    List<CovidHospital> getCovidHospitalList();
    List<CovidHospital> getCovidHospitalEnableList();
    CovidHospital addCovidHospital(CovidHospital covidHospital);
    CovidHospital updateCovidHospital(CovidHospital covidHospital);
    void deleteCovidHospital(CovidHospital covidHospital);
}
