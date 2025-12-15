package cl.ipfsoftware.bakend.model.mapper;


import cl.ipfsoftware.bakend.model.dto.ShipDTO;
import cl.ipfsoftware.bakend.model.entities.Nave;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring", uses = {Nave.class, ShipDTO.class})
public interface ShipMapper {
    @Mappings({
            @Mapping(source = "idNave", target = "shipId"),
            @Mapping(source = "nombreBarco", target = "shipName"),
            @Mapping(source = "numeroViaje", target = "voyageNumber"),
            @Mapping(source = "naviera", target = "shippingLine")
    })
    ShipDTO toShipDTO(Nave ship);

    // Mapeo de ShipDTO a Ship
    @Mappings({
            @Mapping(source = "shipId", target = "idNave"),
            @Mapping(source = "shipName", target = "nombreBarco"),
            @Mapping(source = "voyageNumber", target = "numeroViaje"),
            @Mapping(source = "shippingLine", target = "naviera")
    })
    Nave toShip(ShipDTO shipDTO);
}
