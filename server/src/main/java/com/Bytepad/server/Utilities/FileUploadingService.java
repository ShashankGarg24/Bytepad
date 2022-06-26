package com.Bytepad.server.Utilities;


import com.Bytepad.server.DTOs.DuplicateFileChecker;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class FileUploadingService {

    private String filePath;

    /**
     * this method uploads the file in the given paper directory that resides in the 'resource' folder of the project directory
     * and returns the path of the file which is already available in the respective folder after skipping it.
     * @param file
     * @param paperDirectory
     * @return
     * @throws Exception
     */
    public DuplicateFileChecker fileUpload(MultipartFile file, String paperDirectory)throws Exception{

        try {
            filePath = paperDirectory + file.getOriginalFilename();
            Files.copy(file.getInputStream(), Paths.get(filePath));

            return new DuplicateFileChecker(filePath, false);
        }
        catch (FileAlreadyExistsException e){
            System.out.println(e.getMessage());
            return new DuplicateFileChecker(filePath, true);
        }

    }
}
