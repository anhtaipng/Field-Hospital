package com.lvtn;

import com.lvtn.module.admin.service.AdminService;
import com.lvtn.module.patient.service.PatientService;
import com.lvtn.module.shared.dto.UserSignupDto;
import com.lvtn.module.shared.enumeration.PatientStatusType;
import com.lvtn.module.shared.enumeration.SickbedStatus;
import com.lvtn.module.shared.model.*;
import com.lvtn.module.shared.repository.*;
import com.lvtn.module.shared.service.CovidHospitalService;
import com.lvtn.module.shared.service.MedicineService;
import com.lvtn.module.shared.service.TestTypeService;
import com.lvtn.module.user.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@SpringBootApplication
public class BvdcApplication {

    public static void main(String[] args) {
        SpringApplication.run(BvdcApplication.class, args);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


//    @Bean
    CommandLineRunner run(CovidHospitalService covidHospitalService)  {
        return args -> {
            covidHospitalService.addCovidHospital(new CovidHospital("Bệnh viện dã chiến Cũ Chi","Điều trị bệnh nhân nhẹ",true));
            covidHospitalService.addCovidHospital(new CovidHospital("Bệnh viện điều trị Covid-19 Cũ Chi","Điều trị bệnh nhân nhẹ - nặng; bệnh lý kèm theo thận nhân tạo, can thiệp phẫn thuật cấp cứu, cấp cứu sản phụ khoa, nhi khoa",true));
            covidHospitalService.addCovidHospital(new CovidHospital("Bệnh viện điều trị Covid-19 Cần Giờ","Điều trị bệnh nhân nhẹ; thận mạn cần chạy thận nhân tạo",true));
            covidHospitalService.addCovidHospital(new CovidHospital("Bệnh viện điều trị Covid-19 Trưng Vương","Điều trị bệnh nhân có bệnh lý đi kèm cần can thiệp kỹ thuật điều trị chuyên khoa sâu",true));
            covidHospitalService.addCovidHospital(new CovidHospital("Bệnh viện điều trị Covid-19 Phạm Ngọc Thạch","Bệnh viện chuyển đổi theo mô hình 'tách đôi bệnh viện'. Điều trị bệnh nhân nặng suy hô hấp",true));
            covidHospitalService.addCovidHospital(new CovidHospital("Bệnh viện Nhi Đồng Thành Phố","Điều trị trẻ em có diễn biến nặng, cần can thiệp hồi sức hô hấp, tuần hoàn ngoài cơ thể (ECMO),...",true));
            covidHospitalService.addCovidHospital(new CovidHospital("Bệnh viện Nhi Đồng 2","Điều trị trẻ em có diễn biến nặng, can thiệp hô hấp, tuần hoàn ngoài cơ thể (ECMO),...",true));
            covidHospitalService.addCovidHospital(new CovidHospital("Bệnh viện Bệnh Nhiệt Đới TP.HCM","Điều trị bệnh nhân diễn biến nặng, can thiệp hồi sức hô hấp, tuần hoàn ngoài cơ thể (ECMO)",true));
            covidHospitalService.addCovidHospital(new CovidHospital("Bệnh viện Chợ Rẫy","Điều trị bệnh nhân nặng",true));
        };
    }


    //@Bean
    CommandLineRunner run(UserService userService, PatientService patientService, AdminService adminService,
                          TestTypeService testTypeService, PatientStatusRepository patientStatusRepository, MedicineService medicineService) {
        return args -> {
            patientStatusRepository.save(new PatientStatus(PatientStatusType.NAM_VIEN));
            userService.saveAuthority(new Authority(null, "ROLE_ADMIN"));
            userService.saveAuthority(new Authority(null, "ROLE_PATIENT"));
            userService.saveAuthority(new Authority(null, "ROLE_DOCTOR"));
            userService.saveAuthority(new Authority(null, "ROLE_NURSE"));
            adminService.signupAdmin(new UserSignupDto("Admin", "1", "1", "1", null));
//            patientService.signupPatient(new UserSignupDto("Patient", "2", "2", "2", null));
            adminService.signupDoctor(new UserSignupDto("Doctor", "3", "3", "3", "sáng"));
            adminService.signupNurse(new UserSignupDto("Nurse", "4", "4", "4", "tối"));

            adminService.createStatsType(new StatisticType("SpO2", 94.0F, 98.0F, 100.0F, null, "%"));
            adminService.createStatsType(new StatisticType("Nhiệt độ cơ thể", 35.5F, 36.5F, 37.5F, 38.5F, "°C"));
            adminService.createStatsType(new StatisticType("Nhịp thở", 20.F, 30.0F, 40F, 50F, "nhịp/phút"));
            adminService.createStatsType(new StatisticType("Nhịp tim", 40F, 60.0F, 120.0F, 150F, "nhịp/phút"));

            testTypeService.create(new TestType("Test nhanh"));
            testTypeService.create(new TestType("Test PCR Realtime"));

            for (int i = 1000; i < 1100; i++) {
                String str = String.valueOf(i);
//                patientService.signupPatient(new UserSignupDto("Patient Anh Tai " + str, "08864" + str, str, str, null));
                String str1000 = String.valueOf(i + 1000);
                adminService.signupNurse(new UserSignupDto("Nurse Anh Tai " + str1000, "08864" + str1000, str1000, str1000, (i % 2 == 0) ? "sáng" : "tối"));
                String str2000 = String.valueOf(i + 2000);
                adminService.signupDoctor(new UserSignupDto("Doctor Anh Tai " + str2000, "08864" + str2000, str2000, str2000, (i % 2 == 0) ? "sáng" : "tối"));
                medicineService.addMedicine(new Medicine("Thuốc số " + str, (i % 4 == 0) ? "viên" : (i % 4 == 1) ? "lọ" : (i % 4 == 2) ? "vỉ" : "liều"));
            }

        };
    }

    //@Bean
    CommandLineRunner run(BuildingRepository buildingRepository, FloorRepository floorRepository, RoomTypeRepository roomTypeRepository,
                          RoomRepository roomRepository, SickbedRepository sickbedRepository) {
        return args -> {
            Building building = new Building("1");
            buildingRepository.save(building);
            Floor floor = new Floor("1", building);
            floorRepository.save(floor);
            RoomType roomType = new RoomType("1");
            roomTypeRepository.save(roomType);
            Room room = new Room("1", floor, roomType);
            roomRepository.saveRoom(room.getRoomNo(), room.getFloor().getFloorNo(), room.getFloor().getBuilding().getBuildingName(), room.getRoomType().getRoomType());
            Sickbed sickbed = new Sickbed("2", room, SickbedStatus.EMPTY,null);
            sickbedRepository.saveSickbed(sickbed.getSickBedNo(), sickbed.getRoom().getRoomNo(), sickbed.getRoom().getFloor().getFloorNo(), sickbed.getRoom().getFloor().getBuilding().getBuildingName(), String.valueOf(SickbedStatus.USED));
            System.out.println("Sickbed = " + sickbedRepository.findById(new SickbedPK("1", room)));
        };
    }


}
