package ru.tkachenko.springbooking.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import ru.tkachenko.springbooking.dto.UpsertUserRequest;
import ru.tkachenko.springbooking.dto.UserResponse;
import ru.tkachenko.springbooking.model.User;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
    User requestToEntity(UpsertUserRequest request);

    User requestToEntity(Long id, UpsertUserRequest request);

    UpsertUserRequest entityToUpsertRequest(User user);

    UserResponse entityToResponse(User user);
}
