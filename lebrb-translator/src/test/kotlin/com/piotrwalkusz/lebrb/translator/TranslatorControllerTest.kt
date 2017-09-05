package com.piotrwalkusz.lebrb.translator

import com.piotrwalkusz.lebrb.common.Language
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@RunWith(SpringRunner::class)
@WebMvcTest
class TranslatorControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var translatorService: TranslatorService

    @Test
    fun translate_one_word() {
        given(translatorService.translate(listOf("jump"), Language.ENGLISH, Language.POLISH)).willReturn(listOf("skok"))

        mockMvc.perform(post("/translate")
                .content("""{"words":["jump"],"from":"ENGLISH","to":"POLISH"}""")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk)
                .andExpect(content().json("""{"words":["skok"]}"""))
    }

    @Test
    fun translate_many_word() {
        given(translatorService.translate(listOf("jump", "dog", "cat"), Language.ENGLISH, Language.POLISH))
                .willReturn(listOf("skok", "pies", "kot"))

        mockMvc.perform(post("/translate")
                .content("""{"words":["jump","dog","cat"],"from":"ENGLISH","to":"POLISH"}""")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk)
                .andExpect(content().json("""{"words":["skok","pies","kot"]}"""))
    }
}