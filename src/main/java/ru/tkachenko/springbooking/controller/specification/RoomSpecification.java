package ru.tkachenko.springbooking.controller.specification;

import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;
import ru.tkachenko.springbooking.dto.RoomFilter;
import ru.tkachenko.springbooking.model.Booking;
import ru.tkachenko.springbooking.model.Hotel;
import ru.tkachenko.springbooking.model.Room;

import java.time.LocalDate;

public interface RoomSpecification {
    static Specification<Room> withFilter(RoomFilter filter) {
        return Specification.where(byId(filter.getId()))
                .and(byName(filter.getName()))
                .and(byPrice(filter.getMinPrice(), filter.getMaxPrice()))
                .and(byCountGuest(filter.getCountGuest()))
                .and(byDate(filter.getArrivalDate(), filter.getDepartureDate()))
                .and(byHotelId(filter.getHotelId()));
    }

    static Specification<Room> byId(Long id) {
        return (root, query, criteriaBuilder) -> {
            if (id == null) {
                return null;
            }
            return criteriaBuilder.equal(root.get(Room.Fields.id), id);
        };
    }

    static Specification<Room> byName(String name) {
        return (root, query, criteriaBuilder) -> {
            if (name == null) {
                return null;
            }
            return criteriaBuilder.like(root.get(Room.Fields.name), "%" + name + "%");
        };
    }

    static Specification<Room> byPrice(Double minPrice, Double maxPrice) {
        return (root, query, criteriaBuilder) -> {
            if (minPrice == null && maxPrice == null) {
                return null;
            }
            if (minPrice == null) {
                return criteriaBuilder.lessThanOrEqualTo(root.get(Room.Fields.price), maxPrice);
            }
            if (maxPrice == null) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get(Room.Fields.price), minPrice);
            }

            return criteriaBuilder.between(root.get(Room.Fields.price), minPrice, maxPrice);
        };
    }

    static Specification<Room> byCountGuest(Integer countGuest) {
        return (root, query, criteriaBuilder) -> {
            if (countGuest == null) {
                return null;
            }
            return criteriaBuilder.equal(root.get(Room.Fields.capacity), countGuest);
        };
    }

    static Specification<Room> byDate(String from, String to) {
        return (root, query, criteriaBuilder) -> {
            if (from == null || to == null) {
                return null;
            }
            LocalDate fromDate = LocalDate.parse(from);
            LocalDate toDate = LocalDate.parse(to);

            Predicate isBookedOnDateRange = criteriaBuilder.or(
                    criteriaBuilder.between(root.get(Room.Fields.bookings).get(Booking.Fields.arrivalDate), fromDate, toDate),
                    criteriaBuilder.between(root.get(Room.Fields.bookings).get(Booking.Fields.departureDate), fromDate, toDate),
                    criteriaBuilder.and(
                            criteriaBuilder.lessThanOrEqualTo(root.get(Room.Fields.bookings).get(Booking.Fields.arrivalDate), fromDate),
                            criteriaBuilder.greaterThanOrEqualTo(root.get(Room.Fields.bookings).get(Booking.Fields.departureDate), toDate)
                    )
            );

            Subquery<Long> bookedRoomsSubquery = query.subquery(Long.class);
            Root<Booking> bookingRoot = bookedRoomsSubquery.from(Booking.class);
            bookedRoomsSubquery.select(bookingRoot.get(Booking.Fields.room).get(Room.Fields.id));
            bookedRoomsSubquery.where(isBookedOnDateRange);

            return criteriaBuilder.not(root.get(Room.Fields.id).in(bookedRoomsSubquery));
        };
    }

    static Specification<Room> byHotelId(Long hotelId) {
        return (root, query, criteriaBuilder) -> {
            if (hotelId == null) {
                return null;
            }
            return criteriaBuilder.equal(root.get(Room.Fields.hotel).get(Hotel.Fields.id), hotelId);
        };
    }
}
