package com.piotrwalkusz.lebrb.security

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service
class UserService {

    class UserServiceException extends RuntimeException {
        UserServiceException() {}
        UserServiceException(Throwable cause) { super(cause) }
        UserServiceException(String message) { super(message) }
    }

    @Autowired UserRepository userRepository

    @Autowired AuthenticationManager authenticationManager

    void registerUser(String username, String password) {
        def userEntity = new UserEntity(username: username, password: password)
        try {
            userRepository.save(userEntity)
        }
        catch (DataIntegrityViolationException ignored) {
            throw new UserServiceException(ignored)
        }
    }

    void loginUser(String username, String password) {
        try {
            SecurityContextHolder.context.authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password))
        }
        catch (BadCredentialsException ignored) {
            throw new UserServiceException('Invalid username or password')
        }
    }
}