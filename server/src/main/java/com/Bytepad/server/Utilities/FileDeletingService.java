package com.Bytepad.server.Utilities;

import org.springframework.stereotype.Service;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class FileDeletingService {

    /**
     * this function deletes the document, residing at the given document location
     * @param documentLocation
     * @throws IOException
     */

    public void deleteDocument(String documentLocation) throws IOException {

        Files.deleteIfExists(Paths.get(documentLocation));

    }


}