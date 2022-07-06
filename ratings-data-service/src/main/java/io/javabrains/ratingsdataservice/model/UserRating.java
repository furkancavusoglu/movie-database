package io.javabrains.ratingsdataservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRating {
        private String userId;
        private List<Rating> ratings;

        public void initData(String userId){
                this.setUserId(userId);
                this.setRatings(Arrays.asList(
                        new Rating("100",3),
                        new Rating("200",4)
                ));
        }
}
