package com.revo.eshop.userservice.repositories;


import com.revo.eshop.userservice.domain.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
    User findByEmail(String email);

    User findByUsername(String username);

    User findByUserId(String userId);
}
