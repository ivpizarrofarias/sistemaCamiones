package cl.ipfsoftware.bakend.model.mapper;


import cl.ipfsoftware.bakend.model.dto.ContainerDTO;
import cl.ipfsoftware.bakend.model.entities.Contenedor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring", uses = {Contenedor.class, ContainerDTO.class})
public interface ContainerMapper {

    @Mappings({
            @Mapping(source = "idContenedor", target = "containerId"), // Mapeo de idContenedor (español) a containerId (inglés)
            @Mapping(source = "codigoContenedor", target = "containerCode") // Mapeo de codigoContenedor (español) a containerCode (inglés)
    })
    ContainerDTO toContainerDTO(Contenedor contenedor);

    @Mappings({
            @Mapping(source = "containerId", target = "idContenedor"), // Mapeo de containerId (inglés) a idContenedor (español)
            @Mapping(source = "containerCode", target = "codigoContenedor") // Mapeo de containerCode (inglés) a codigoContenedor (español)
    })
    Contenedor toContenedor(ContainerDTO containerDTO);
}