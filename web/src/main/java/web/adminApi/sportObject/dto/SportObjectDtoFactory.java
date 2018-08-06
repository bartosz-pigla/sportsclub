package web.adminApi.sportObject.dto;

import query.model.sportobject.SportObjectEntity;
import web.common.dto.AddressDto;

public final class SportObjectDtoFactory {

    public static SportObjectDto create(SportObjectEntity sportObjectEntity) {
        return SportObjectDto.builder()
                .name(sportObjectEntity.getName())
                .description(sportObjectEntity.getDescription())
                .address(new AddressDto(
                        sportObjectEntity.getAddress().getStreet(),
                        sportObjectEntity.getAddress().getCity().getCity(),
                        sportObjectEntity.getAddress().getCoordinates().getLatitude(),
                        sportObjectEntity.getAddress().getCoordinates().getLongitude()))
                .imageUrl(sportObjectEntity.getImage().toString())
                .sportsclubName(sportObjectEntity.getHeadquarter().getName()).build();
    }
}
