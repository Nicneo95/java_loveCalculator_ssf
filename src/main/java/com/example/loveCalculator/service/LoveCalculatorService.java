package com.example.loveCalculator.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.loveCalculator.model.Person;

@Qualifier("LoveCalculatorService")
@Service
public class LoveCalculatorService {

    private static final String LOVE_CALCULATOR_URL = "https://love-calculator.p.rapidapi.com/getPercentage";

    public Optional<Person> getPerson (String fname, String sname) throws Exception {
        String apiKey = "d853fa0bcdmshe5b311aed1ccb96p134f87jsndab8508802ff";
        String personUrl = UriComponentsBuilder
                            .fromUriString(LOVE_CALCULATOR_URL)
                            .queryParam("fname", fname)
                            .queryParam("sname", sname)
                            .toUriString();
        System.out.println(personUrl);

        HttpRequest request = HttpRequest.newBuilder()
		                    .uri(URI.create(LOVE_CALCULATOR_URL))
		                    .header("X-RapidAPI-Key", apiKey)
		                    .header("X-RapidAPI-Host", "love-calculator.p.rapidapi.com")
		                    .method("GET", HttpRequest.BodyPublishers.noBody())
		                    .build();

        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
        Person p = Person.create(response.body());

        if(p != null)
            return Optional.of(p);                        
        return Optional.empty();
    }
}
