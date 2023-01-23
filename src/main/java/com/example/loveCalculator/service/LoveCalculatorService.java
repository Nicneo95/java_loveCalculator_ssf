package com.example.loveCalculator.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.loveCalculator.model.Person;

// service tasks, such as executing business logic, interacting with databases, or handling web requests 
@Service
public class LoveCalculatorService {

    private static final String LOVE_CALCULATOR_URL = "https://love-calculator.p.rapidapi.com/getPercentage";

    private static final String RESULT_ENTITY = "resultlist";

    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    public Optional<Person> getPerson(String fname, String sname) throws Exception {

        String personUrl = UriComponentsBuilder
                .fromUriString(LOVE_CALCULATOR_URL)
                .queryParam("fname", fname)
                .queryParam("sname", sname)
                .toUriString();
        System.out.println(personUrl);
        String API_KEY = "d853fa0bcdmshe5b311aed1ccb96p134f87jsndab8508802ff";
        String API_HOST = "love-calculator.p.rapidapi.com";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(personUrl))
                .header("X-RapidAPI-Key", API_KEY)
                .header("X-RapidAPI-Host", API_HOST)
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
        Person p = Person.create(response.body());

        if (p != null)
            return Optional.of(p);
        return Optional.empty();
    }

    public void save(final Person p) {
        redisTemplate.opsForList()
                .leftPush(RESULT_ENTITY, p.getId());
        redisTemplate.opsForHash()
                .put(RESULT_ENTITY + "_Map", p.getId(), p);
    }

    public List<Person> findAll() {
        List<Object> fromResultList = redisTemplate.opsForList()
                .range(RESULT_ENTITY, 0, -1);
        List<Person> listOfResults = redisTemplate.opsForHash()
                .multiGet(RESULT_ENTITY + "_Map", fromResultList)
                .stream()
                .filter(Person.class::isInstance)
                .map(Person.class::cast)
                .toList();

        return listOfResults;
    }
}
