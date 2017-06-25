package com.piotrwalkusz.lebrb.wordscounter

import com.piotrwalkusz.lebrb.common.Language
import com.piotrwalkusz.lebrb.file.FileService
import com.piotrwalkusz.lebrb.file.FileToTextConversionException
import com.piotrwalkusz.lebrb.security.UserEntity
import com.piotrwalkusz.lebrb.security.UserRepository
import com.piotrwalkusz.lebrb.security.UserService
import com.piotrwalkusz.lebrb.wordscounter.dictionary.WordsRepository
import com.piotrwalkusz.lebrb.wordscounter.dictionary.WordsRepositoryImpl
import org.hibernate.Hibernate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import spock.lang.Specification
import spock.mock.DetachedMockFactory

import javax.naming.Context

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*

@SpringBootTest
@AutoConfigureMockMvc
@Import(MockConfig)
class WordsCounterControllerTest extends Specification {

    @Autowired private AuthenticationManager authenticationManager
    @Autowired private UserRepository userRepository
    @Autowired private UserService userService
    @Autowired private WordsRepository mockWordsRepository
    @Autowired private FileService mockFileService
    @Autowired private WordsCounterService mockWordsCounterService

    @Autowired private MockMvc mockMvc

    void 'valid file'() {
        setup:
        def result = new WordsCounterResult()
        mockFileService.convertFileToText(_, _) >> null
        mockWordsCounterService.countWords(_, _) >> result

        expect:
        mockMvc.perform(MockMvcRequestBuilders.fileUpload('/wordscounter')
                .file('file', new byte[0]))
                .andExpect(flash().attributeCount(2))
                .andExpect(flash().attribute('error', null))
                .andExpect(flash().attribute('result', result))
    }

    void 'invalid extension'() {
        setup:
        mockFileService.convertFileToText(_, _) >> { throw new FileService.FileToTextConverterNotFoundException() }

        expect:
        mockMvc.perform(MockMvcRequestBuilders.fileUpload('/wordscounter')
                .file('file', new byte[0]))
                .andExpect(flash().attributeCount(2))
                .andExpect(flash().attribute('error', 'The file has invalid extension'))
                .andExpect(flash().attribute('result', null))
    }

    void 'invalid content'() {
        setup:
        mockFileService.convertFileToText(_, _) >> { throw new FileToTextConversionException() }

        expect:
        def mockSecurityMvc =
        mockMvc.perform(MockMvcRequestBuilders.fileUpload('/wordscounter')
                .file('file', new byte[0]))
                .andExpect(flash().attributeCount(2))
                .andExpect(flash().attribute('error', 'The file has invalid content'))
                .andExpect(flash().attribute('result', null))
    }

    void 'known words'() {
        setup:
        def result = new WordsCounterResult()
        mockWordsCounterService.countWords(_, _) >> result
        mockWordsRepository.getWordsByLanguage(Language.English) >> ['dog', 'cat', 'home', 'bank']
        userService.registerUser('login', 'password')
        def userEntity = userRepository.findByUsernameAndFetchKnownWords('login')
        userEntity.knownWords.put(Language.English, [0, 1, 3])
        userRepository.save(userEntity)

        expect:
        mockMvc.perform(MockMvcRequestBuilders.fileUpload('/wordscounter')
                .file('file', new byte[0]).with(user('login').password('password')))
                .andExpect(flash().attributeCount(3))
                .andExpect(flash().attribute('error', null))
                .andExpect(flash().attribute('result', result))
                .andExpect(flash().attribute('knownWords', ['dog', 'cat', 'bank']))
    }

    @TestConfiguration
    static class MockConfig {
        private DetachedMockFactory factory = new DetachedMockFactory()

        @Bean
        FileService fileService() {
            factory.Stub(FileService)
        }

        @Bean
        WordsCounterService wordsCounterService() {
            factory.Stub(WordsCounterService)
        }

        @Bean
        WordsRepository wordsRepositoryImpl() {
            factory.Stub(WordsRepositoryImpl)
        }
    }
}