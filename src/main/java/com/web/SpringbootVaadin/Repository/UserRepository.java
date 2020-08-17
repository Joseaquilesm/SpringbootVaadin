package com.web.SpringbootVaadin.Repository;

import com.web.SpringbootVaadin.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    @Query("select usuario from User usuario where usuario.mail = :mail and usuario.password = :password")
    User findByMailAndPassword(@Param("mail") String email, @Param("password") String password);

    @Query("select count(usuario) from User usuario")
    Integer contar();

    @Query(value = "select * from User m offset(?1) limit(?2)", nativeQuery = true)
    List<User> paginar(int offset, int limit);
}