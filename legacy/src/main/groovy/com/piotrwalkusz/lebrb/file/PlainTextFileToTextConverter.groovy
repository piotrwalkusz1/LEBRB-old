package com.piotrwalkusz.lebrb.file

import org.springframework.http.MediaType
import org.springframework.stereotype.Component

@Component
class PlainTextFileToTextConverter implements FileToTextConverter {

    @Override
    MediaType getSupportedFileType() {
        MediaType.TEXT_PLAIN
    }

    @Override
    Reader convert(InputStream stream) {
        stream.newReader()
    }
}
