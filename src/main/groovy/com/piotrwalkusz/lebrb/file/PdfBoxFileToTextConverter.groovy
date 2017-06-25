package com.piotrwalkusz.lebrb.file

import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.text.PDFTextStripper
import org.springframework.http.MediaType
import org.springframework.stereotype.Component

@Component
class PdfBoxFileToTextConverter implements FileToTextConverter {

    @Override
    MediaType getSupportedFileType() {
        MediaType.APPLICATION_PDF
    }

    @Override
    Reader convert(InputStream stream) {
        try {
            PDDocument doc = PDDocument.load(stream)
            new StringReader(new PDFTextStripper().getText(doc))
        }
        catch (IOException ex) {
            throw new FileToTextConversionException(ex)
        }
    }
}