package com.piotrwalkusz.lebrb.file

import org.junit.Test
import org.springframework.http.MediaType

import static org.junit.Assert.assertEquals

class PdfBoxFileToTextConverterTest {

    private def converter = new PdfBoxFileToTextConverter()

    @Test
    void 'get a supported file type'() {
        assertEquals(MediaType.APPLICATION_PDF, converter.getSupportedFileType())
    }

    @Test
    void 'convert an empty pdf file'() {
        def file = getClass().getResourceAsStream('empty.pdf')
        assertEquals('\n', converter.convert(file).text)
    }

    @Test
    void 'convert a valid pdf file'() {
        def file = getClass().getResourceAsStream('normal.pdf')
        assertEquals('Text in pdf file\n', converter.convert(file).text)
    }

    @Test(expected = FileToTextConversionException)
    void 'convert an invalid pdf file'() {
        def file = new ByteArrayInputStream(new byte[1000])
        converter.convert(file)
    }
}
