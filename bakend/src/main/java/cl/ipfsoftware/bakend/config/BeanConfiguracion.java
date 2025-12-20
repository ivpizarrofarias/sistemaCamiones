package cl.ipfsoftware.bakend.config;

import cl.ipfsoftware.bakend.model.mapper.UserMapper;
import cl.ipfsoftware.bakend.persistence.repositories.UsuarioRepository;
import cl.ipfsoftware.bakend.service.UserService;
import cl.ipfsoftware.bakend.service.impl.UserServiceImpl;
import jakarta.validation.Validator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguracion {

    @Bean
    public UserService userService(
            UsuarioRepository usuarioRepository,
            UserMapper userMapper,
            Validator validator) {

        return new UserServiceImpl(
                usuarioRepository,
                userMapper,
                validator
        );
    }
}
