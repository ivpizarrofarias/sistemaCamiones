package cl.ipfsoftware.bakend.model.mapper;


import cl.ipfsoftware.bakend.model.dto.TripDTO;
import cl.ipfsoftware.bakend.model.entities.Viaje;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring", uses = {Viaje.class, TripDTO.class})
public interface TripMapper {

    @Mappings({
            @Mapping(source = "idViaje", target = "tripId"),
            @Mapping(source = "origen", target = "origin")
    })
    TripDTO toTripDTO(Viaje viaje);

    @Mappings({
            @Mapping(source = "tripId", target = "idViaje"),
            @Mapping(source = "origin", target = "origen")
    })
    Viaje toViaje(TripDTO tripDTO);
}
