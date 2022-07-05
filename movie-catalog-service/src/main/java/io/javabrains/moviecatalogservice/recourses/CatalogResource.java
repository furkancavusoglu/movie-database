package io.javabrains.moviecatalogservice.recourses;

import io.javabrains.moviecatalogservice.models.CatalogItem;
import io.javabrains.moviecatalogservice.models.Movie;
import io.javabrains.moviecatalogservice.models.Rating;
import io.javabrains.moviecatalogservice.models.UserRating;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/catalog")
public class CatalogResource {
    RestTemplate restTemplate;
    WebClient.Builder builder;

    public CatalogResource(RestTemplate restTemplate, WebClient.Builder builder) {
        this.restTemplate = restTemplate;
        this.builder = builder;
    }

    @GetMapping("/{userId}")
    List<CatalogItem> getCatalog(@PathVariable String userId) {

//        UserRating ratings = restTemplate.getForObject("http://localhost:8083/ratingsdata/users/" + userId,
//                UserRating.class);

        UserRating ratings2 = builder.build()
                .get()
                .uri("http://localhost:8083/ratingsdata/users/" + userId)
                .retrieve()
                .bodyToMono(UserRating.class)
                .block();

        return ratings2.getRatings().stream()
                .map(rating -> {
                    // Movie movie = restTemplate.getForObject("http://localhost:8082/movies/" + rating.getMovieId(), Movie.class);
                    //For each movie id, call movie info service and get details
                    Movie movie = builder.build()
                            .get()
                            .uri("http://localhost:8082/movies/" + rating.getMovieId())
                            .retrieve()
                            .bodyToMono(Movie.class)
                            .block();
                    //Put them all together
                    return new CatalogItem(movie.getName(), "Description", rating.getRating());
                })
                .collect(Collectors.toList());
    }
}