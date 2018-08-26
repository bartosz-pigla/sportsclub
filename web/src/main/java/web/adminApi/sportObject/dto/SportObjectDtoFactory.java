package web.adminApi.sportObject.dto;

import query.model.sportobject.SportObjectEntity;

public final class SportObjectDtoFactory {

    public static SportObjectDto create(SportObjectEntity object) {
        return SportObjectDto.builder()
                .id(object.getId().toString())
                .name(object.getName())
                .description(object.getDescription())
                .address(new AddressDto(
                        object.getAddress().getStreet(),
                        object.getAddress().getCity().getCity(),
                        object.getAddress().getCoordinates().getLatitude(),
                        object.getAddress().getCoordinates().getLongitude()))
                .imageUrl(object.getImageUrl().toString())
                .sportsclubId(object.getHeadquarter().getId().toString())
                .build();
    }
}
