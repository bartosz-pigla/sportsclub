package web.adminApi.sportObject.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public final class SportObjectDto {

    private String name;
    private String description;
    private AddressDto address;
    private String imageUrl;
    private String sportsclubName;
}
