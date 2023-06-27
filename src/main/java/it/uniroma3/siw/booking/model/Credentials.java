package it.uniroma3.siw.booking.model;

import it.uniroma3.siw.booking.constants.AuthenticationProvider;
import it.uniroma3.siw.booking.constants.Role;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Credentials {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String username;

    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToOne(cascade = CascadeType.ALL)
    private User user;

    @Enumerated(EnumType.STRING)
    private AuthenticationProvider oAuthProvider;


}
