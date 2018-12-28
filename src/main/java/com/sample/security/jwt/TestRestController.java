package com.sample.security.jwt;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/rest/api")
public class TestRestController {

    public List<String> getAllEmployeeNames()
    {
        return Arrays.asList("Mahmoud","Mohamed","Eltaieb","Ahmed");
    }
}
