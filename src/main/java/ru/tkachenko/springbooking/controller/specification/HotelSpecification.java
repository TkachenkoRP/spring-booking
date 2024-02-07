package ru.tkachenko.springbooking.controller.specification;

import org.springframework.data.jpa.domain.Specification;
import ru.tkachenko.springbooking.dto.HotelFilter;
import ru.tkachenko.springbooking.model.Hotel;

public interface HotelSpecification {
    static Specification<Hotel> withFilter(HotelFilter filter) {
        return Specification.where(byId(filter.getId()))
                .and(byName(filter.getName()))
                .and(byTitle(filter.getTitle()))
                .and(byCity(filter.getCity()))
                .and(byAddress(filter.getAddress()))
                .and(byDistance(filter.getDistance()))
                .and(byRating(filter.getRating()))
                .and(byNumberOfRatings(filter.getNumberOfRatings()));
    }

    static Specification<Hotel> byId(Long id) {
        return (root, query, criteriaBuilder) -> {
            if (id == null) {
                return null;
            }
            return criteriaBuilder.equal(root.get(Hotel.Fields.id), id);
        };
    }

    static Specification<Hotel> byName(String name) {
        return (root, query, criteriaBuilder) -> {
            if (name == null) {
                return null;
            }
            return criteriaBuilder.equal(root.get(Hotel.Fields.name), name);
        };
    }

    static Specification<Hotel> byTitle(String title) {
        return (root, query, criteriaBuilder) -> {
            if (title == null) {
                return null;
            }
            return criteriaBuilder.equal(root.get(Hotel.Fields.title), title);
        };
    }

    static Specification<Hotel> byCity(String city) {
        return (root, query, criteriaBuilder) -> {
            if (city == null) {
                return null;
            }
            return criteriaBuilder.equal(root.get(Hotel.Fields.city), city);
        };
    }

    static Specification<Hotel> byAddress(String address) {
        return (root, query, criteriaBuilder) -> {
            if (address == null) {
                return null;
            }
            return criteriaBuilder.equal(root.get(Hotel.Fields.address), address);
        };
    }

    static Specification<Hotel> byDistance(Double distance) {
        return (root, query, criteriaBuilder) -> {
            if (distance == null) {
                return null;
            }
            return criteriaBuilder.lessThanOrEqualTo(root.get(Hotel.Fields.distanceFromCityCenter), distance);
        };
    }

    static Specification<Hotel> byRating(Double rating) {
        return (root, query, criteriaBuilder) -> {
            if (rating == null) {
                return null;
            }
            return criteriaBuilder.greaterThanOrEqualTo(root.get(Hotel.Fields.rating), rating);
        };
    }

    static Specification<Hotel> byNumberOfRatings(Integer numberOfRatings) {
        return (root, query, criteriaBuilder) -> {
            if (numberOfRatings == null) {
                return null;
            }
            return criteriaBuilder.greaterThanOrEqualTo(root.get(Hotel.Fields.numberOfRatings), numberOfRatings);
        };
    }
}
