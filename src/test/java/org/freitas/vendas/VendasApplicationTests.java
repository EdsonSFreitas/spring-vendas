package org.freitas.vendas;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import net.bytebuddy.utility.RandomString;
import org.apache.commons.lang3.RandomStringUtils;
import org.assertj.core.util.Strings;
import org.freitas.vendas.domain.dto.RegisterRequest;
import org.freitas.vendas.domain.enums.Role;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = VendasApplication.class)
class VendasApplicationTests {

    private final TestRestTemplate restTemplate;
    @LocalServerPort
    int randomServerPort;


    @Autowired
    public VendasApplicationTests(TestRestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private String getBaseUrl() {
        return "http://localhost:" + randomServerPort + "/api/v1.0/auth/register";
    }

    private RegisterRequest getRegisterRequest() {
        RegisterRequest usuario = new RegisterRequest();
        usuario.setLogin(RandomStringUtils.randomAlphabetic(10));
        usuario.setPassword("123qweQwqe%");
        usuario.setRole(Role.ROLE_USER);
        return usuario;
    }

    private URI getUri() throws URISyntaxException {
        return new URI(getBaseUrl());
    }

    @Test
    void verifyIfStatusCodeEqual201() throws URISyntaxException {
        RegisterRequest usuario = getRegisterRequest();
        HttpEntity<RegisterRequest> request = new HttpEntity<>(usuario);
        ResponseEntity<String> result = this.restTemplate.postForEntity(getBaseUrl(), request, String.class);
        Assert.assertEquals(201, result.getStatusCodeValue());

    }

    @Test
    void verifyIfResponseContainsToken() throws URISyntaxException, JsonProcessingException {
        RegisterRequest usuario = getRegisterRequest();
        ObjectMapper objectMapper = new ObjectMapper();
        ResponseEntity<String> result = this.restTemplate.postForEntity(getBaseUrl(), usuario, String.class);
        //assertTrue(JsonPath.read(result.getBody(), "$.token") != null);
        assertNotNull(JsonPath.read(result.getBody(), "$.token"));
    }

    @Test
    void verifyIfTokenIsNotEmpty() throws URISyntaxException, JsonProcessingException {
        RegisterRequest usuario = getRegisterRequest();
        ObjectMapper objectMapper = new ObjectMapper();
        ResponseEntity<String> result = this.restTemplate.postForEntity(getBaseUrl(), usuario, String.class);
        //assertTrue(!Strings.isNullOrEmpty(JsonPath.read(result.getBody(), "$.token")));
        assertFalse(Strings.isNullOrEmpty(JsonPath.read(result.getBody(), "$.token")));

    }


	/*	@BeforeClass
	public static void runBeforeAllTestMethods() {
		createPersonUrl = "http://localhost:8082/spring-rest/createPerson";
		updatePersonUrl = "http://localhost:8082/spring-rest/updatePerson";

		restTemplate = new RestTemplate();
		headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		personJsonObject = new JSONObject();
		personJsonObject.put("id", 1);
		personJsonObject.put("name", "John");
	}*/

}