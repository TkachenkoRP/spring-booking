package ru.tkachenko.springbooking.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "hotels")
public class Hotel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String title;
    private String city;
    private String address;
    @Column(name = "distance_from_city_center")
    private double distanceFromCityCenter;
    private double rating;
    @Column(name = "number_of_ratings")
    private int numberOfRatings;
}
