package com.javainnovations.advancedweb.lecture3.example1.service.security;

import com.javainnovations.advancedweb.lecture3.example1.model.dto.AppUser;
import com.javainnovations.advancedweb.lecture3.example1.service.template.DatabaseTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Arrays;

@Service
public class CustomReactiveUserDetailsService implements ReactiveUserDetailsService {

    @Autowired
    DatabaseTemplate db;

//    public CustomReactiveUserDetailsService(UserRepository userRepository) {
//        this.userRepository = userRepository;
//    }

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        Mono<UserDetails> userMono = db.findByEmail(username)
                .map((AppUser user) ->
                        {


                            System.out.println(new User(user.getEmail(), user.getPassword(),
                                    AuthorityUtils.commaSeparatedStringToAuthorityList(user.getRoleId().toString())));

                               return (UserDetails) new User(user.getEmail(), user.getPassword(),
                                       AuthorityUtils.commaSeparatedStringToAuthorityList(user.getRoleId().toString()));
                        }

                )
                .switchIfEmpty(Mono.just(
                        (UserDetails)new User("BAD", "BAD", AuthorityUtils.commaSeparatedStringToAuthorityList("0"))
                ));
        return userMono;

//                .switchIfEmpty(Mono.defer(() -> Mono.error(new UsernameNotFoundException("User Not Found"))))
//                .map(user -> new User(user.getUsername(), user.getPassword(),
//                        AuthorityUtils.commaSeparatedStringToAuthorityList(user.getRoles())));
    }
}
