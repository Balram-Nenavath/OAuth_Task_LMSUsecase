package com.example.lmsusecase.controller;

import com.example.lmsusecase.dto.EmployeeDTO;
import com.example.lmsusecase.service.EmployeeService;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@RestController
//@RequestMapping("/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @PostMapping
    public ResponseEntity<EmployeeDTO> createEmployee(@RequestBody EmployeeDTO employeeDTO) {
        return new ResponseEntity<>(employeeService.addEmployee(employeeDTO), HttpStatus.CREATED);
    }

    @RequestMapping(value="/employees/getAll", method = RequestMethod.GET)
    public ResponseEntity<List<EmployeeDTO>> getAllEmployees() {

        List<EmployeeDTO> employeeDTOs = employeeService.getAllEmployees();
        return new ResponseEntity<>(employeeDTOs, HttpStatus.OK);
    }


    @RequestMapping(value="/employees/getById/{id}", method = RequestMethod.GET)
    public ResponseEntity<EmployeeDTO> getEmployeeById(@PathVariable("id") Long id) {
        EmployeeDTO employeeDTO = employeeService.getEmployeeById(id);
        if (employeeDTO != null) {
            return new ResponseEntity<>(employeeDTO, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/oauth/login")
    public String privateArea1(@RequestParam("code") String code) {
        ResponseEntity<String> response = null;
        System.out.println("Authorization Code------" + code);
        RestTemplate restTemplate = new RestTemplate();
        String credentials = "ram2910:secret";
        String encodedCredentials = new String(Base64.encodeBase64(credentials.getBytes()));

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.add("Authorization", "Basic " + encodedCredentials);

        HttpEntity<String> request = new HttpEntity<String>(headers);

        String access_token_url = "http://localhost:2023/oauth/token";
        access_token_url += "?code=" + code;
        access_token_url += "&grant_type=authorization_code";
        access_token_url += "&redirect_uri=http://localhost:2023/oauth/login";

        response = restTemplate.exchange(access_token_url, HttpMethod.POST, request, String.class);

        System.out.println("Access Token Response ---------" + response.getBody());

        return response.getBody();
//		return "Private endpoint";
    }

    @GetMapping(value = "/oauth/refresh")
    public String privateArea2(@RequestParam String refreshToken) {
        ResponseEntity<String> response = null;
        System.out.println("refresh token Code------");

        RestTemplate restTemplate = new RestTemplate();

        // According OAuth documentation we need to send the client id and secret key in
        // the header for authentication
        String credentials = "ram2910:secret";
        String encodedCredentials = new String(Base64.encodeBase64(credentials.getBytes()));

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.add("Authorization", "Basic " + encodedCredentials);

        HttpEntity<String> request = new HttpEntity<String>(headers);

        String access_token_url = "http://localhost:2023/oauth/token";
        access_token_url += "?grant_type=refresh_token";
//		access_token_url += "&redirect_uri=http://localhost:2023/oauth/login";
        access_token_url += "&refresh_token=" + refreshToken;
        access_token_url += "&client_id=" + "ram2910";
        access_token_url += "&client_secret=" + "secret";

        response = restTemplate.exchange(access_token_url, HttpMethod.POST, request, String.class);

        System.out.println("refresh Token Response ---------" + response.getBody());

        return response.getBody();
    }
}
