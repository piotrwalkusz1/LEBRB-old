package com.piotrwalkusz.lebrb.translator

import com.fasterxml.jackson.annotation.JsonRawValue
import com.fasterxml.jackson.annotation.JsonUnwrapped
import com.piotrwalkusz.lebrb.common.Language
import org.springframework.web.bind.annotation.*

@RestController
class TranslatorController(private val translatorService: TranslatorService) {

    class TranslateRequest(var words: List<String> = emptyList(),
                           var from: Language = Language.ENGLISH,
                           var to: Language = Language.ENGLISH)

    class TranslateResponse(val words: List<String>)

    @PostMapping("/translate")
    fun translate(@RequestBody request: TranslateRequest): TranslateResponse {
        return TranslateResponse(translatorService.translate(request.words, request.from, request.to))
    }
}