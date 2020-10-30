package com.example.demo.facade;

import com.example.demo.service.HelloService;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class HelloFacadeTest {

    private HelloService helloService = mock(HelloService.class);
    private HelloFacade helloFacade = new HelloFacade(helloService);

    @Test
    void returnsHelloDu() {
        when(helloService.sayServiceHallo()).thenReturn("Hallo Du");

        assertThat(helloFacade.sayHello()).isEqualTo("Hallo Du");
        verify(helloService, times(1)).sayServiceHallo();
    }
}
