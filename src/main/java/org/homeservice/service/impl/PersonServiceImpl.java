package org.homeservice.service.impl;

import org.homeservice.entity.Person;
import org.homeservice.repository.PersonRepository;
import org.homeservice.service.PersonService;
import org.homeservice.service.base.BaseServiceImpl;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Scope("singleton")
public class PersonServiceImpl extends BaseServiceImpl<Person,Long, PersonRepository> implements PersonService {
    protected PersonServiceImpl(PersonRepository repository) {
        super(repository);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findPersonByUsername(username)
                .orElseThrow(()-> new UsernameNotFoundException("User not found."));
    }
}
