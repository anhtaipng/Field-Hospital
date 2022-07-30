package com.lvtn.module.shared.service.implementation;

import com.lvtn.module.shared.common.ApiSharedMesssage;
import com.lvtn.module.shared.model.StatisticType;
import com.lvtn.module.shared.model.TestType;
import com.lvtn.module.shared.repository.TestTypeRepository;
import com.lvtn.module.shared.service.TestTypeService;
import com.lvtn.platform.exception.ApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class TestTypeServiceImpl implements TestTypeService {
    private final TestTypeRepository testTypeRepository;

    @Override
    public TestType create(TestType testType) {
        TestType testType1 = testTypeRepository.findByTestType(testType.getTestType());
        if (testType1 != null){
            throw new ApiException(ApiSharedMesssage.STATISTIC_TYPE_EXIST);
        }
        testTypeRepository.save(testType);
        return testType;
    }

    @Override
    public List<TestType> get() {
        return testTypeRepository.findAll();
    }
}
