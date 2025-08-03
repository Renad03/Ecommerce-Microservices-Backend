//package com.example.microservices.wallet_microservice.user;
//
//import com.example.microservices.wallet_microservice.wallet.Wallet;
//import com.example.microservices.wallet_microservice.wallet.WalletRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.Optional;
//
//@Service
//public class UserService {
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private WalletRepository walletRepository;
//
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//
//    public User registerUser(User user) {
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
//        User savedUser = userRepository.save(user);
//
//        // Create wallet for new user
//        Wallet wallet = new Wallet();
//        wallet.setUser(savedUser);
//        wallet.setBalance(0.0);
//        walletRepository.save(wallet);
//
//        return savedUser;
//    }
//
//    public Optional<User> findByUsername(String username) {
//        return userRepository.findByUsername(username);
//    }
//
//    public boolean authenticateUser(String username, String rawPassword) {
//        return userRepository.findByUsername(username)
//                .map(user -> passwordEncoder.matches(rawPassword, user.getPassword()))
//                .orElse(false);
//    }
//
//    public List<User> getAllUsers(){
//      return userRepository.findAll();
//    }
//}
package com.example.microservices.wallet_microservice.user.service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.microservices.wallet_microservice.user.dto.UpdateUserDto;
import com.example.microservices.wallet_microservice.user.model.User;
import com.example.microservices.wallet_microservice.user.repository.UserRepository;
import com.example.microservices.wallet_microservice.wallet.model.Wallet;
import com.example.microservices.wallet_microservice.wallet.repository.WalletRepository;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private final PasswordEncoder passwordEncoder;

    public UserService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Registers a new user and creates a wallet with a zero balance.
     * @param user The user to register.
     * @return The saved user entity.
     */
    public User registerUser(User user) {
        // Encode the password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);

        // Create and link a wallet for the user
        Wallet wallet = new Wallet();
        wallet.setUser(savedUser);
        wallet.setBalance(0.0);
        walletRepository.save(wallet);

        return savedUser;
    }

    /**
     * Finds a user by their username.
     * @param username The username to search.
     * @return Optional containing the user if found.
     */
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * Authenticates a user using raw password comparison.
     * @param username The username of the user.
     * @param rawPassword The plaintext password.
     * @return true if authenticated, false otherwise.
     */
    public boolean authenticateUser(String username, String rawPassword) {
        return userRepository.findByUsername(username)
                .map(user -> passwordEncoder.matches(rawPassword, user.getPassword()))
                .orElse(false);
    }

    /**
     * Retrieves all users in the system.
     * @return List of users.
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Updates a user if they exist.
     * @param dto The user with updated info.
     * @return The updated user or null if user not found.
     */
    public User updateUser(Long id, UpdateUserDto dto) {
        Optional<User> existingUser = userRepository.findById(id);
        if (existingUser.isPresent()) {
            User user = existingUser.get();

            if (dto.getUsername() != null) {
                user.setUsername(dto.getUsername());
            }

            if (dto.getEmail() != null) {
                user.setEmail(dto.getEmail());
            }

            if (dto.getPassword() != null && !passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
                user.setPassword(passwordEncoder.encode(dto.getPassword()));
            }

            return userRepository.save(user);
        }
        return null;
    }


    /**
     * Deletes a user and their associated wallet.
     *
     * @param id The ID of the user to delete.
     * @return true if the user and wallet were successfully deleted, false otherwise.
     */
    @Transactional
    public boolean deleteUser(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            walletRepository.deleteByUserId(user.get().getId());
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }



}
