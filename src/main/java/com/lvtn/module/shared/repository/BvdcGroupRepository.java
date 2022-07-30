package com.lvtn.module.shared.repository;

import com.lvtn.module.shared.enumeration.GroupType;
import com.lvtn.module.shared.model.Authority;
import com.lvtn.module.shared.model.BvdcGroup;
import com.lvtn.module.shared.model.Doctor;
import com.lvtn.module.shared.model.Nurse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BvdcGroupRepository extends JpaRepository<BvdcGroup,Long> {
    BvdcGroup findByGroupTypeAndDoctors(GroupType groupType, Doctor doctor);
    BvdcGroup findByGroupTypeAndNurses(GroupType groupType, Nurse nurse);
    List<BvdcGroup> findByNurses(Nurse nurse);
    List<BvdcGroup> findByDoctors(Doctor doctor);
    @Query("select bvdc_group.id" +
            " from " +
            " BvdcGroup bvdc_group " +
            " left join Patient p on bvdc_group.id = p.bvdcGroup.id where bvdc_group.groupType = ?1" +
            " group by bvdc_group.id order by count(p.id) asc")
    Page<Long> getAvailableGroup(GroupType groupType, Pageable pageable);
    @Query("select doctors" +
            " from " +
            " BvdcGroup bvdc_group " +
            " join bvdc_group.doctors doctors" +
            " left join bvdc_group.patient patient" +
            " group by doctors order by count(patient) asc")
    Page<Doctor> selectGroup(Pageable pageable);
    Page<BvdcGroup> findByGroupTypeAndPatientIsNull(GroupType groupType, Pageable pageable);
}
