package com.piotrwalkusz.lebrb.file

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class FileService {

    static class FileToTextConverterNotFoundException extends RuntimeException {
        FileToTextConverterNotFoundException() {}

        FileToTextConverterNotFoundException(String message) { super(message) }
    }

    @Autowired Collection<FileToTextConverter> converters

    Reader convertFileToText(InputStream stream, String mimeType) {
        def converter = converters.find { it.getSupportedFileType().toString() == mimeType }

        if (!converter)
            throw new FileToTextConverterNotFoundException("Converter to $mimeType not found")

        converter.convert(stream)
    }
}
