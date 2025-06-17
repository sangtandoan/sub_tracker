package com.sangtandoan.sub_tracker.user;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring")
public interface UserMapper {
  UserDto toDto(User user);

  @Mapping(target = "password", qualifiedByName = "stringToByteArray")
  @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
  User toEntity(NewUserRequest request);

  @Named("stringToByteArray")
  default byte[] stringToByteArray(String string) {
    return string != null ? string.getBytes() : null;
  }
}
