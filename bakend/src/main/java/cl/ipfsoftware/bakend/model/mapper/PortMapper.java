package cl.ipfsoftware.bakend.model.mapper;


import cl.ipfsoftware.bakend.model.dto.PortDTO;
import cl.ipfsoftware.bakend.model.entities.Puerto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring", uses = {Puerto.class, PortDTO.class})
public interface PortMapper {

    @Mappings({
            @Mapping(source = "idPuerto", target = "portId"),
            @Mapping(source = "nombre", target = "portName")
    })
    PortDTO toPortDTO(Puerto puerto);

    @Mappings({
            @Mapping(source = "portId", target = "idPuerto"),
            @Mapping(source = "portName", target = "nombre")
    })
    Puerto toPuerto(PortDTO portDTO);
}