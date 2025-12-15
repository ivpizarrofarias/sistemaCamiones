package cl.ipfsoftware.bakend.model.mapper;

import cl.ipfsoftware.sistemacamiones.model.dto.*;
import cl.ipfsoftware.sistemacamiones.model.entities.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring", uses = { Cliente.class, ClientDTO.class, Nave.class, ShipDTO.class,
        Puerto.class, PortDTO.class, Viaje.class, TripDTO.class, Contenedor.class,ContainerDTO.class,
        TransporteTerrestre.class,GroundTransportDTO.class })
public interface ValuationMapper {

    // Mapeo de Valorización (entidad) a ValuationDTO
    @Mappings({
            @Mapping(source = "idValorizacion", target = "valuationId"),
            @Mapping(source = "fecha", target = "valuationDate"),
            @Mapping(source = "valor", target = "value"),
            @Mapping(source = "cliente.idCliente", target = "clientId"),
            @Mapping(source = "nave.idNave", target = "shipId"),
            @Mapping(source = "puerto.idPuerto", target = "portId"),
            @Mapping(source = "viaje.idViaje", target = "tripId"),
            @Mapping(source = "contenedor.idContenedor", target = "containerId"),
            @Mapping(source = "transporteTerrestre.idTransporte", target = "terrestrialTransportId")
    })
    ValuationDTO toValuationDTO(Valorizacion valorizacion);

    // Mapeo de ValuationDTO a Valorización (entidad)
    @Mappings({
            @Mapping(source = "valuationId", target = "idValorizacion"),
            @Mapping(source = "valuationDate", target = "fecha"),
            @Mapping(source = "value", target = "valor"),
            @Mapping(source = "clientId", target = "cliente.idCliente"),
            @Mapping(source = "shipId", target = "nave.idNave"),
            @Mapping(source = "portId", target = "puerto.idPuerto"),
            @Mapping(source = "tripId", target = "viaje.idViaje"),
            @Mapping(source = "containerId", target = "contenedor.idContenedor"),
            @Mapping(source = "terrestrialTransportId", target = "transporteTerrestre.idTransporte")
    })
    Valorizacion toValuation(ValuationDTO valuationDTO);
}
