package cl.ipfsoftware.bakend.model.mapper;


import cl.ipfsoftware.bakend.model.dto.UserDTO;
import cl.ipfsoftware.bakend.model.entities.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mappings({
            @Mapping(source = "idUsuario", target = "userId"),
            @Mapping(source = "nombre", target = "firstName"),
            @Mapping(source = "paterno", target = "paternalLastName"),
            @Mapping(source = "materno", target = "maternalLastName"),
            @Mapping(source = "correo", target = "email"),
            @Mapping(source = "clave", target = "password"),
            @Mapping(source = "rolUsuario", target = "userRole"),
            @Mapping(source = "fechaCreacion", target = "creationDate")
    })
    UserDTO toUserDTO(Usuario usuario);

    @Mappings({
            @Mapping(source = "userId", target = "idUsuario"),
            @Mapping(source = "firstName", target = "nombre"),
            @Mapping(source = "paternalLastName", target = "paterno"),
            @Mapping(source = "maternalLastName", target = "materno"),
            @Mapping(source = "email", target = "correo"),
            @Mapping(source = "password", target = "clave"),
            @Mapping(source = "userRole", target = "rolUsuario"),
            @Mapping(target = "fechaCreacion", expression = "java(setCreationDate(userDTO))")
    })
    Usuario toUser(UserDTO userDTO);

    @Named("setCreationDate")
    default LocalDateTime setCreationDate(UserDTO userDTO) {
        // Si es creación, usa fecha actual. Si es actualización, mantiene la existente
        return userDTO.getCreationDate() != null ? userDTO.getCreationDate() : LocalDateTime.now();
    }
}