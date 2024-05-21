package br.com.ulkiorra.apimudafacil.service;

import br.com.ulkiorra.apimudafacil.model.User;
import br.com.ulkiorra.apimudafacil.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User registerUser(User user) {
        user.setSenha(passwordEncoder.encode(user.getSenha()));
        return userRepository.save(user);
    }

    public User authenticateUser(String email, String senha) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent() && passwordEncoder.matches(senha, user.get().getSenha())) {
            return user.get();
        } else {
            return null;
        }
    }

    public User updateUser(Long id, User userDetails) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        user.setNome(userDetails.getNome());
        user.setTelefone(userDetails.getTelefone());
        user.setEmail(userDetails.getEmail());
        if (!userDetails.getSenha().isEmpty()) {
            user.setSenha(passwordEncoder.encode(userDetails.getSenha()));
        }
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}