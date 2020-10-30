package com.example.demo.service;

import com.example.demo.db.DataBaseAccessor;
import org.springframework.stereotype.Component;

@Component
public class HelloService {
    private DataBaseAccessor dataBaseAccessor;

    public HelloService(DataBaseAccessor dataBaseAccessor) {
        this.dataBaseAccessor = dataBaseAccessor;
    }

    public String sayServiceHallo() {
        return dataBaseAccessor.doComplexDBOperation();
    }
}
