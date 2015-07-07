package com.superum.db.files;

import java.io.File;
import java.io.IOException;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface FileService {

	File getPicture(String pictureName);

	File getDocument(String documentName);

	String saveFile(String folder, String name, MultipartFile file) throws IOException;

}
