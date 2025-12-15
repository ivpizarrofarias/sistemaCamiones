package cl.ipfsoftware.bakend.model.mapper;


import cl.ipfsoftware.bakend.model.dto.*;
import cl.ipfsoftware.bakend.model.entities.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring", uses = { Contenedor.class, ContainerDTO.class,
        Camion.class, TruckDTO.class, Chofer.class, DriverDTO.class, Nave.class, ShipDTO.class, Puerto.class, PortDTO.class,
TransporteTerrestre.class,GroundTransportDTO.class})
public interface MovementMapper {

    // Mapeo de Movimiento (entidad) a MovementDTO
    @Mappings({
            @Mapping(source = "idMovimiento", target = "movementId"),
            @Mapping(source = "fechaEmision", target = "emissionDate"),
            @Mapping(source = "numeroDocumento", target = "documentNumber"),
            @Mapping(source = "tipoDocumento", target = "documentType"),
            @Mapping(source = "estadoFisico", target = "physicalState"),
            @Mapping(source = "tipoTamanio", target = "sizeType"),
            @Mapping(source = "fechaHoraEntrada", target = "entryDateTime"),
            @Mapping(source = "fechaHoraSalida", target = "exitDateTime"),
            @Mapping(source = "camion.idCamion", target = "truckId"),
            @Mapping(source = "chofer.idChofer", target = "driverId"),
            @Mapping(source = "cliente.idCliente", target = "clientId"),
            @Mapping(source = "nave.idNave", target = "shipId"),
            @Mapping(source = "puerto.idPuerto", target = "portId"),
            @Mapping(source = "transporteTerrestre.idTransporte", target = "terrestrialTransportId"),
            @Mapping(source = "contenedor.idContenedor", target = "containerId")
    })
    MovementDTO toMovementDTO(Movimiento movimiento);

    // Mapeo de MovementDTO a Movimiento (entidad)
    @Mappings({
            @Mapping(source = "movementId", target = "idMovimiento"),
            @Mapping(source = "emissionDate", target = "fechaEmision"),
            @Mapping(source = "documentNumber", target = "numeroDocumento"),
            @Mapping(source = "documentType", target = "tipoDocumento"),
            @Mapping(source = "physicalState", target = "estadoFisico"),
            @Mapping(source = "sizeType", target = "tipoTamanio"),
            @Mapping(source = "entryDateTime", target = "fechaHoraEntrada"),
            @Mapping(source = "exitDateTime", target = "fechaHoraSalida"),
            @Mapping(source = "truckId", target = "camion.idCamion"),
            @Mapping(source = "driverId", target = "chofer.idChofer"),
            @Mapping(source = "clientId", target = "cliente.idCliente"),
            @Mapping(source = "shipId", target = "nave.idNave"),
            @Mapping(source = "portId", target = "puerto.idPuerto"),
            @Mapping(source = "terrestrialTransportId", target = "transporteTerrestre.idTransporte"),
            @Mapping(source = "containerId", target = "contenedor.idContenedor")
    })
    Movimiento toMovement(MovementDTO movementDTO);
}