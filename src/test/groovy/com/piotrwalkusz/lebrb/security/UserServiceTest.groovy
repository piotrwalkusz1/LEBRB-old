package com.piotrwalkusz.lebrb.security

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.test.context.junit4.SpringRunner

import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertTrue

@RunWith(SpringRunner)
@SpringBootTest
class UserServiceTest {

    @Autowired private UserService userService
    @Autowired private UserRepository userRepository

    @Before
    void setup() {
        userRepository.deleteAll()
    }

    @Test
    void 'register a new user'() {
        userService.registerUser('mylogin', 'secret')

        def user = userRepository.findByUsername('mylogin')
        assertEquals('mylogin', user.username)
        assertEquals('secret', user.password)
    }

    @Test(expected = UserService.UserServiceException)
    void 'register fail if a username is already taken'() {
        userService.registerUser('mylogin', 'secret')
        userService.registerUser('mylogin', 'password')
    }

    @Test
    void 'successful login'() {
        userService.registerUser('mylogin', 'secret')

        userService.loginUser('mylogin', 'secret')

        def auth = SecurityContextHolder.context.authentication
        assertTrue(auth.authenticated)
        assertEquals('mylogin', auth.principal.username)
    }

    @Test(expected = UserService.UserServiceException)
    void 'login with an invalid username'() {
        userService.registerUser('mylogin', 'secret')

        userService.loginUser('notmylogin', 'secret')
    }

    @Test(expected = UserService.UserServiceException)
    void 'login with an invalid password'() {
        userService.registerUser('mylogin', 'secret')

        userService.loginUser('mylogin', 'invalidpassword')
    }
}
