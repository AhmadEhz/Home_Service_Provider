package org.homeservice.service;

import org.homeservice.entity.Person;
import org.homeservice.service.base.BaseService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public interface PersonService extends BaseService<Person, Long>, UserDetailsService {
}
