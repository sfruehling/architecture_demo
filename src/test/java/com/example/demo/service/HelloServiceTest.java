package com.example.demo.service;

import com.example.demo.db.DataBaseAccessor;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class HelloServiceTest {

    private DataBaseAccessor dataBaseAccessor = mock(DataBaseAccessor.class);
    private HelloService helloService = new HelloService(dataBaseAccessor);

    @Test
    void returnsHello() {
        when(dataBaseAccessor.doComplexDBOperation()).thenReturn("hallo");
        assertThat(helloService.sayServiceHallo()).isEqualTo("hallo");

        verify(dataBaseAccessor, times(1)).doComplexDBOperation();
    }
}
