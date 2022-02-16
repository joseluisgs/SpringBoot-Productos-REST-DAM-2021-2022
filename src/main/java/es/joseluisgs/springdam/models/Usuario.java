package es.joseluisgs.springdam.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "usuarios")
@EntityListeners(AuditingEntityListener.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
// Implementa UserDetails, neccesario para usar el servicio de usuarios de SrpingBoot(Spring security), ver CustomUserDetailsService
public class Usuario implements UserDetails {

    private static final long serialVersionUID = 6189678452627071360L;

    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true)
    @NotBlank(message = "Username no puede estar vacío")
    private String username;

    @NotNull(message = "Password no puede ser nulo")
    private String password;

    private String avatar;

    @NotNull(message = "FullName no puede ser nulo")
    private String fullName;

    @Email(regexp = ".*@.*\\..*", message = "Email debe ser un email valido")
    private String email;

    // Conjunto de permisos que tiene
    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<UsuarioRol> roles;

    @CreatedDate
    private LocalDateTime createdAt;

    @Builder.Default
    private LocalDateTime lastPasswordChangeAt = LocalDateTime.now();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream().map(ur -> new SimpleGrantedAuthority("ROLE_" + ur.name())).collect(Collectors.toList());
    }

    /**
     * No vamos a gestionar la expiración de cuentas. De hacerse, se tendría que dar
     * cuerpo a este método
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * No vamos a gestionar el bloqueo de cuentas. De hacerse, se tendría que dar
     * cuerpo a este método
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * No vamos a gestionar la expiración de cuentas. De hacerse, se tendría que dar
     * cuerpo a este método
     */

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }


    /**
     * No vamos a gestionar el bloqueo de cuentas. De hacerse, se tendría que dar
     * cuerpo a este método
     */
    @Override
    public boolean isEnabled() {
        return true;
    }

}
