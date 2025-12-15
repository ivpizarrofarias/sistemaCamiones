package cl.ipfsoftware.bakend.model.mapper;


import cl.ipfsoftware.bakend.model.dto.ClientDTO;
import cl.ipfsoftware.bakend.model.entities.Cliente;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring", uses = {Cliente.class, ClientDTO.class})
public interface ClientMapper {
    @Mappings({
            @Mapping(source = "idCliente", target = "clientId"),
            @Mapping(source = "nombre", target = "clientName"),
            @Mapping(source = "email", target = "clientEmail")

    })
    ClientDTO toClientDTO(Cliente cliente);

    @Mappings({
            @Mapping(source = "clientId", target = "idCliente"),
            @Mapping(source = "clientName", target = "nombre"),
            @Mapping(source = "clientEmail", target = "email")

    })
    Cliente toCliente(ClientDTO clientDTO);
}
