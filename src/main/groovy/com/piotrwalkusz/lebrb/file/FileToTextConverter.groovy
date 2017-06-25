package com.piotrwalkusz.lebrb.file

import org.springframework.http.MediaType

interface FileToTextConverter {

    MediaType getSupportedFileType()

    Reader convert(InputStream stream)
}
