package web.user.dto;

import query.model.user.UserEntity;

public final class UserDtoFactory {

    public static UserDto create(UserEntity user) {
        return UserDto.builder()
                .id(user.getId().toString())
                .username(user.getUsername())
                .userType(user.getUserType().name())
                .email(user.getEmail().getEmail())
                .phoneNumber(user.getPhoneNumber().getPhoneNumber())
                .activated(user.isActivated())
                .deleted(user.isDeleted())
                .build();
    }
}
