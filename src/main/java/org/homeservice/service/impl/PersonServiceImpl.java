package org.homeservice.service.impl;

import org.homeservice.entity.Person;
import org.homeservice.repository.PersonRepository;
import org.homeservice.service.PersonService;
import org.homeservice.service.base.BaseServiceImpl;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Scope("singleton")
public class PersonServiceImpl extends BaseServiceImpl<Person,Long, PersonRepository> implements PersonService {
    protected PersonServiceImpl(PersonRepository repository) {
        super(repository);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return loadByUsername(username)
                .orElseThrow(()-> new UsernameNotFoundException("User not found."));
    }

    @Override
    public Optional<Person> loadByUsername(String username) {
        return repository.findPersonByUsername(username);
    }

    @Override
    public boolean isExistsByUsername(String username) {
        return repository.findPersonByUsername(username).isPresent();
    }
}
