package com.piotrwalkusz.lebrb.wordscounter

import com.piotrwalkusz.lebrb.common.Language
import com.piotrwalkusz.lebrb.file.FileService
import com.piotrwalkusz.lebrb.file.FileService.FileToTextConverterNotFoundException
import com.piotrwalkusz.lebrb.file.FileToTextConversionException
import com.piotrwalkusz.lebrb.security.UserRepository
import com.piotrwalkusz.lebrb.wordscounter.dictionary.WordsRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.mvc.support.RedirectAttributes

@Controller
class WordsCounterController {

    @Autowired private WordsCounterService wordsCounterService
    @Autowired private FileService fileService
    @Autowired private WordsRepository wordsRepository
    @Autowired private UserRepository userRepository

    @GetMapping('/wordscounter')
    def countWords() {
        'form'
    }

    @PostMapping('/wordscounter')
    def countWords(@RequestParam('file') MultipartFile file, RedirectAttributes redirectAttributes) {
        def error = null
        def result = null

        try {
            def reader = fileService.convertFileToText(file.inputStream, file.contentType)
            result = wordsCounterService.countWords(reader, Language.English)
        }
        catch (FileToTextConverterNotFoundException ignored) {
            error = 'The file has invalid extension'
        }
        catch (FileToTextConversionException ignored) {
            error = 'The file has invalid content'
        }

        def auth = SecurityContextHolder.context.authentication
        if (auth && auth.principal != 'anonymousUser') {
            def user = userRepository.findByUsernameAndFetchKnownWords(auth.principal.username as String)
            def englishWords = wordsRepository.getWordsByLanguage(Language.English)
            def knownWords = []
            for (int wordIndex : user.knownWords[Language.English]) {
                knownWords.add(englishWords[wordIndex])
            }
            redirectAttributes.addFlashAttribute('knownWords', knownWords)
        }

        redirectAttributes.addFlashAttribute('error', error)
        redirectAttributes.addFlashAttribute('result', result)

        'redirect:/wordscounter/result'
    }
}
