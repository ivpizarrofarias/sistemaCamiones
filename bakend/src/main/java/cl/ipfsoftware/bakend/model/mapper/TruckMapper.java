package cl.ipfsoftware.bakend.model.mapper;

import cl.ipfsoftware.bakend.model.dto.TruckDTO;
import cl.ipfsoftware.bakend.model.entities.Camion;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring", uses = {Camion.class, TruckDTO.class})
public interface TruckMapper {

    @Mappings({
            @Mapping(source = "idCamion", target = "truckId"),
            @Mapping(source = "patente", target = "licensePlate")
    })
    TruckDTO toTruckDTO(Camion camion);

    @Mappings({
            @Mapping(source = "truckId", target = "idCamion"),
            @Mapping(source = "licensePlate", target = "patente")
    })
    Camion toCamion(TruckDTO truckDTO);
}