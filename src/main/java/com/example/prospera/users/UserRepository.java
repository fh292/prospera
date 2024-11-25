package com.example.prospera.users;
import org.apache.catalina.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface UserRepository extends CrudRepository<UserEntity, Long>{
    Optional<UserEntity> findByEmail(String email);

}
