package org.homeservice.service;

import org.homeservice.entity.Person;
import org.homeservice.service.base.BaseService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface PersonService extends BaseService<Person, Long>, UserDetailsService {

    Optional<Person> loadByUsername(String username);


    boolean isExistsByUsername(String username);
}
