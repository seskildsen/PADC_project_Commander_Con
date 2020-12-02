package com.padc.demo.user.service;

import com.padc.demo.core.IService;
import com.padc.demo.core.security.SecurityConfig;
import com.padc.demo.core.enums.Role;
import com.padc.demo.deck.domain.Deck;
import com.padc.demo.user.domain.User;
import com.padc.demo.user.repository.IUserRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService implements IService<User>{

    private final IUserRepository iUserRepository;
    private final SecurityConfig securityConfig;

    public UserService(IUserRepository iUserRepository, SecurityConfig securityConfig) {
        this.iUserRepository = iUserRepository;
        this.securityConfig = securityConfig;
    }

    @Override
    public void save(User user) {
        user.setPassword(securityConfig.getEncoder().encode(user.getPassword()));
        //participant as default (role 3)
        user.setRole(Role.ROLE_PARTICIPANT);
        iUserRepository.save(user);
    }

    /**
     * Returns the User that matches the id that was given, but if the database didn't have a match,
     * then throw a EntityNotFoundException that can be catches later.
     * Optional takes care of not give a NullPointerException, because the return maybe non-null value
     * from the database is in the Optional container.
     * @param id
     * @return User
     */
    @Override
    public User findById(Long id){
        Optional<User> user = iUserRepository.findById(id);
        /*The double colon operator :: is used to call a method/constructor
        by referrring to the class. Syntax: <<Class name>> :: <<method or constructor>>*/
        return user.orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public List<User> findAll() {
        List<User> listen = new ArrayList<>();
        for(User u: iUserRepository.findAll()){
            listen.add(u);
        }
        return listen;
    }

    @Override
    public void deleteByID(Long id) {
        iUserRepository.deleteById(id);
    }
}
