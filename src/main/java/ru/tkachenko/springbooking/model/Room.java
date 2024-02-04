package ru.tkachenko.springbooking.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "rooms")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private int number;
    private double price;
    private Byte capacity;
    @Column(name = "unavailable_dates")
    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
    private List<UnavailableDate> unavailableDates;
    @ManyToOne
    @JoinColumn(name = "hotel_id")
    private Hotel hotel;
}
