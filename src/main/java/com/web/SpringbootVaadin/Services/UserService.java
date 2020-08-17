package com.web.SpringbootVaadin.Services;


import com.web.SpringbootVaadin.Entity.User;
import com.web.SpringbootVaadin.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository usuarioRepository;

    public User crearUsuario(int id, String name, String mail, String password) throws Exception {
        return usuarioRepository.save(new User(id, name, mail, password));
    }

    public void editarUsuario(User usuario) throws Exception {
        usuarioRepository.save(usuario);

    }

    public boolean validarUsuario(String mail, String password) {
        User usuario = usuarioRepository.findByMailAndPassword(mail, password);
        return (usuario != null);
    }

    public List<User> listarUsuarios() {
        return usuarioRepository.findAll();
    }


    public List<User> listarUsuariosPaginados(int offset, int limit){
        return usuarioRepository.paginar(offset, limit);
    }


    @Transactional
    public int contarUsuario() {
        return usuarioRepository.contar()+1;
    }

    @Transactional
    public void eliminarUsuario(int usuarioID){
        usuarioRepository.deleteById(usuarioID);
    }

    @Transactional
    public User buscarUsuario(int usuarioID){
        return usuarioRepository.getOne(usuarioID);
    }

}


