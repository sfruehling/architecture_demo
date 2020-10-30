package com.example.demo.db;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DataBaseAccessorTest {


    private DataBaseAccessor dataBaseAccessor = new DataBaseAccessor();

    @Test
    void name() {
        assertThat(dataBaseAccessor.doComplexDBOperation()).isEqualTo("Hallo Du");
    }
}
