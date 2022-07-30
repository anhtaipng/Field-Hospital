package com.lvtn.module.shared.service;

import com.lvtn.module.shared.model.TestType;

import java.util.List;

public interface TestTypeService {
    public TestType create(TestType testType);
    public List<TestType> get();

}
