package io.javabrains.moviecatalogservice.recourses;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import io.javabrains.moviecatalogservice.models.CatalogItem;
import io.javabrains.moviecatalogservice.models.Movie;
import io.javabrains.moviecatalogservice.models.Rating;
import io.javabrains.moviecatalogservice.models.UserRating;
import io.javabrains.moviecatalogservice.services.MovieInfo;
import io.javabrains.moviecatalogservice.services.UserRatingInfo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/catalog")
public class CatalogResource {
    RestTemplate restTemplate;
    WebClient.Builder builder;
    MovieInfo movieInfo;
    UserRatingInfo userRatingInfo;

    public CatalogResource(RestTemplate restTemplate, WebClient.Builder builder, MovieInfo movieInfo, UserRatingInfo userRatingInfo) {
        this.restTemplate = restTemplate;
        this.builder = builder;
        this.movieInfo = movieInfo;
        this.userRatingInfo = userRatingInfo;
    }

    @GetMapping("/{userId}")
    List<CatalogItem> getCatalog(@PathVariable String userId) {
        UserRating ratings = userRatingInfo.getUserRating(userId); //localhost:8083
        return ratings.getRatings().stream()
                .map(rating -> movieInfo.getCatalogItem(rating))
                .collect(Collectors.toList());
    }
}
// WebClient
//        UserRating ratings = builder.build()
//                .get()
//                .uri("http://ratings-data-service/ratingsdata/user/" + userId)
//                .retrieve()
//                .bodyToMono(UserRating.class)
//                .block();
//                    Movie movie = builder.build()
//                            .get()
//                            .uri("http://movie-info-service/movies/" + rating.getMovieId())
//                            .retrieve()
//                            .bodyToMono(Movie.class)
//                            .block();