package es.joseluisgs.springdam.controllers.usuarios;

import es.joseluisgs.springdam.config.APIConfig;
import es.joseluisgs.springdam.config.security.jwt.JwtTokenProvider;
import es.joseluisgs.springdam.config.security.jwt.model.JwtUserResponse;
import es.joseluisgs.springdam.config.security.jwt.model.LoginRequest;
import es.joseluisgs.springdam.dto.usuarios.CreateUsuarioDTO;
import es.joseluisgs.springdam.dto.usuarios.GetUsuarioDTO;
import es.joseluisgs.springdam.mappers.UsuarioMapper;
import es.joseluisgs.springdam.models.Usuario;
import es.joseluisgs.springdam.models.UsuarioRol;
import es.joseluisgs.springdam.services.users.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.stream.Collectors;

@RestController

// Cuidado que se necesia la barra al final porque la estamos poniendo en los verbos
@RequestMapping(APIConfig.API_PATH + "/usuarios") // Sigue escucnado en el directorio API

// Inyeccion de dependencias usando Lombok y private final y no @Autowired, ver otros controladores
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final UsuarioMapper ususuarioMapper;

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;

    // Creamos el usuario
    @PostMapping("/")
    public GetUsuarioDTO nuevoUsuario(@RequestBody CreateUsuarioDTO newUser) {
        return ususuarioMapper.toDTO(usuarioService.nuevoUsuario(newUser));

    }

    // Petición me de datos del usuario
    // Equivalente en ponerlo en config, solo puede entrar si estamos auteticados
    // De esta forma podemos hacer las rutas espècíficas
    // @PreAuthorize("isAuthenticated()")
    @GetMapping("/me")
    public GetUsuarioDTO me(@AuthenticationPrincipal Usuario user) {
        return ususuarioMapper.toDTO(user);
    }

    // Metodo post para el login, todo traido de JwtAuthenticationController
    @PostMapping("/login")
    public JwtUserResponse login(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                loginRequest.getUsername(),
                                loginRequest.getPassword()

                        )
                );
        // Autenticamos al usuario, si lo es nos lo devuelve
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Devolvemos al usuario autenticado
        Usuario user = (Usuario) authentication.getPrincipal();

        // Generamos el token
        String jwtToken = tokenProvider.generateToken(authentication);

        // La respuesta que queremos
        return convertUserEntityAndTokenToJwtUserResponse(user, jwtToken);

    }

    // Convertimos un usuario en un jwtUserResponseDTO
    private JwtUserResponse convertUserEntityAndTokenToJwtUserResponse(Usuario user, String jwtToken) {
        return JwtUserResponse
                .jwtUserResponseBuilder()
                .fullName(user.getFullName())
                .email(user.getEmail())
                .username(user.getUsername())
                .avatar(user.getAvatar())
                .roles(user.getRoles().stream().map(UsuarioRol::name).collect(Collectors.toSet()))
                .token(jwtToken)
                .build();
    }

}
