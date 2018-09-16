package web.sportsclub.dto;

import query.model.embeddable.Address;
import query.model.sportsclub.SportsclubEntity;
import web.sportObject.dto.AddressDto;

public final class SportsclubDtoFactory {

    public static SportsclubDto create(SportsclubEntity sportsclub) {
        Address address = sportsclub.getAddress();

        return SportsclubDto.builder()
                .id(sportsclub.getId().toString())
                .name(sportsclub.getName())
                .description(sportsclub.getDescription())
                .address(new AddressDto(
                        address.getStreet(),
                        address.getCity().getCity(),
                        address.getCoordinates().getLatitude(),
                        address.getCoordinates().getLongitude()
                ))
                .build();
    }
}
