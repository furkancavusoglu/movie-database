package io.javabrains.moviecatalogservice.recourses;

import com.netflix.discovery.DiscoveryClient;
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

//        UserRating ratings = restTemplate.getForObject("http://ratings-data-service/ratingsdata/user/" + userId,
//                UserRating.class); //localhost:8083

        UserRating ratings = builder.build()
                .get()
                .uri("http://ratings-data-service/ratingsdata/user/" + userId)
                .retrieve()
                .bodyToMono(UserRating.class)
                .block();

        return ratings.getRatings().stream()
                .map(rating -> {
                    // For each movie id, call movie info service and get details  localhost:8082

                   //  Movie movie = restTemplate.getForObject("http://movie-info-service/movies/" + rating.getMovieId(), Movie.class);

                    Movie movie = builder.build()
                            .get()
                            .uri("http://movie-info-service/movies/" + rating.getMovieId())
                            .retrieve()
                            .bodyToMono(Movie.class)
                            .block();
                    return new CatalogItem(movie.getName(), movie.getDescription(), rating.getRating());
                })
                .collect(Collectors.toList());
    }
}