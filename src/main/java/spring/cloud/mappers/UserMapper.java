package spring.cloud.mappers;

import org.mapstruct.Mapper;
import spring.cloud.dtos.users.RegisterUserRequest;
import spring.cloud.entities.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toUser(RegisterUserRequest request);
}
