package com.example.spring315.service;

import com.example.spring315.dto.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.RestTemplate;

import java.io.InputStream;
import java.util.Arrays;
import java.util.logging.Logger;

@Service
public class RestTemplateUserService {
    private final RestTemplate restTemplate;
    private final String url;
    private final Logger logger = Logger.getLogger(RestTemplateUserService.class.getName());

    @Autowired
    public RestTemplateUserService(RestTemplate restTemplate, @Value("http://94.198.50.185:7081/api/users") String url) {
        this.restTemplate = restTemplate;
        this.url = url;
    }


    public void test() {
        //Ключ ответа
        StringBuilder answer = new StringBuilder();
        //Получаем всех пользователей
        ResponseEntity<User[]> response = restTemplate.getForEntity(url, User[].class);
        User[] users = response.getBody();
        logger.info(response.toString());
        logger.info(Arrays.toString(users));

        //session_id
        String session_id = response.getHeaders().get(HttpHeaders.SET_COOKIE).get(0).split(";")[0];
        if (session_id == null) {
            logger.warning("session_id is null!!!");
            return;
        }
        logger.info(session_id);

        //Новый пользователь
        User postUser = new User();
        postUser.setId(3L);
        postUser.setName("James");
        postUser.setLastName("Brown");
        postUser.setAge((byte) 30);

        //Создаем
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add(HttpHeaders.COOKIE, session_id);
        logger.info("header " + headers.toString());
        HttpEntity<User> request = new HttpEntity<>(postUser, headers);
        ResponseEntity<String> responsePost = restTemplate.exchange(url, HttpMethod.POST, request, String.class);
        logger.info(responsePost.getBody());
        logger.info(responsePost.toString());

        //Ключ после создания
        answer.append(responsePost.getBody());

        //Изменяем
        postUser.setName("Thomas");
        postUser.setLastName("Shelby");
        logger.info("edit:"+postUser);
        request = new HttpEntity<>(postUser, headers);
        ResponseEntity<String> responsePUT = restTemplate.exchange(url, HttpMethod.PUT, request, String.class);
        logger.info(responsePUT.getBody());
        logger.info(responsePUT.toString());
        answer.append(responsePUT.getBody());

        //Удаляем
        ResponseEntity<String> responseDEL = restTemplate.exchange(url + "/" + postUser.getId(), HttpMethod.DELETE, request, String.class);
        logger.info(responseDEL.getBody());
        logger.info(responseDEL.toString());
        answer.append(responseDEL.getBody());

        logger.info("Answer: " + answer);

    }


}
