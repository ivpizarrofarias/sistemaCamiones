package cl.ipfsoftware.bakend.model.mapper;


import cl.ipfsoftware.bakend.model.dto.DriverDTO;
import cl.ipfsoftware.bakend.model.entities.Chofer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring", uses = {Chofer.class, DriverDTO.class})
public interface DriverMapper {
    @Mappings({
            @Mapping(source = "idChofer", target = "driverId"),
            @Mapping(source = "nombre", target = "driverName"),
            @Mapping(source = "rut", target = "driverRut"),
            @Mapping(source = "numeroLicencia", target = "driverLicenseNumber")
    })
    DriverDTO toDriverDTO(Chofer chofer);

    @Mappings({
            @Mapping(source = "driverId", target = "idChofer"),
            @Mapping(source = "driverName", target = "nombre"),
            @Mapping(source = "driverRut", target = "rut"),
            @Mapping(source = "driverLicenseNumber", target = "numeroLicencia")
    })
    Chofer toChofer(DriverDTO driverDTO);
}
