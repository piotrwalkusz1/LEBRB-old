package com.piotrwalkusz.lebrb.file

import org.junit.Test
import org.springframework.http.MediaType

import static org.junit.Assert.assertEquals

class FileServiceTest {

    private static class MockTxtConverter implements FileToTextConverter {
        @Override
        MediaType getSupportedFileType() { MediaType.TEXT_PLAIN }
        @Override Reader convert(InputStream stream) { new StringReader('txt') }
    }

    private static class MockPdfConverter implements FileToTextConverter {
        @Override
        MediaType getSupportedFileType() { MediaType.APPLICATION_PDF }
        @Override Reader convert(InputStream stream) { new StringReader('pdf') }
    }

    private def fileService = new FileService(converters: [new MockTxtConverter(), new MockPdfConverter()])

    @Test
    void 'converter exists'() {
        assertEquals('txt', fileService.convertFileToText(null, MediaType.TEXT_PLAIN_VALUE).text)
        assertEquals('pdf', fileService.convertFileToText(null, MediaType.APPLICATION_PDF_VALUE).text)
    }

    @Test(expected = FileService.FileToTextConverterNotFoundException)
    void 'converter doesn\'t exist'() {
        fileService.convertFileToText(null, MediaType.IMAGE_PNG_VALUE)
    }
}