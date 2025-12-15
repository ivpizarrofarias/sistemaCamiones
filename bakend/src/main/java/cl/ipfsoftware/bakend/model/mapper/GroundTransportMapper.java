package cl.ipfsoftware.bakend.model.mapper;


import cl.ipfsoftware.bakend.model.dto.GroundTransportDTO;
import cl.ipfsoftware.bakend.model.entities.TransporteTerrestre;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring", uses = {TransporteTerrestre.class, GroundTransportDTO.class})
public interface GroundTransportMapper {
    @Mappings({
            @Mapping(source = "idTransporte", target = "transportId"),
            @Mapping(source = "nombreTransportista", target = "transporterName")
    })
    GroundTransportDTO toGroundTransportDTO(TransporteTerrestre transporteTerrestre);

    @Mappings({
            @Mapping(source = "transportId", target = "idTransporte"),
            @Mapping(source = "transporterName", target = "nombreTransportista")
    })
    TransporteTerrestre toTransporteTerrestre(GroundTransportDTO groundTransportDTO);
}
