package it.uniroma3.siw.booking.service;

import it.uniroma3.siw.booking.constants.AuthenticationProvider;
import it.uniroma3.siw.booking.constants.Role;
import it.uniroma3.siw.booking.model.Credentials;
import it.uniroma3.siw.booking.model.User;
import it.uniroma3.siw.booking.oauth.CustomOAuth2User;
import it.uniroma3.siw.booking.repository.CredentialsRepository;
import it.uniroma3.siw.booking.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
public class UserService {

    protected final UserRepository userRepository;

    protected final CredentialsRepository credentialsRepository;

    @Autowired
    public UserService(UserRepository userRepository, CredentialsRepository credentialsRepository) {
        this.userRepository = userRepository;
        this.credentialsRepository = credentialsRepository;
    }


    @Transactional
    public User getUser(Long id) {
        Optional<User> result = this.userRepository.findById(id);
        return result.orElse(null);
    }


    @Transactional
    public void saveUser(User user) {
        this.userRepository.save(user);
    }


    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }


    @Transactional
    public void registerOAuthUser(CustomOAuth2User oAuth2User) {
        User user = new User();
        user.setName(oAuth2User.getFirstName());
        user.setSurname(oAuth2User.getLastName());
        user.setEmail(oAuth2User.getEmail());
        this.saveUser(user);
        Credentials credentials = new Credentials();
        credentials.setOAuthProvider(AuthenticationProvider.OAUTH);
        credentials.setUsername(oAuth2User.getLogin());
        credentials.setRole(Role.DEFAULT);
        credentials.setUser(user);
        credentialsRepository.save(credentials);
        user.setCredentials(credentials);
        this.saveUser(user);
    }

    public boolean existsByUsername(String loginName) {
        return credentialsRepository.existsByUsername(loginName);
    }
}
