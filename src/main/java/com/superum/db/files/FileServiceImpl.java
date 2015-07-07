package com.superum.db.files;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.superum.db.files.exception.FileNotFoundException;

@Service
public class FileServiceImpl implements FileService {

	@Override
	public File getPicture(String pictureName) {
		LOG.debug("Trying to find file '{}' in folder '{}'", pictureName, PICTURE_FOLDER);
		
		Path path = Paths.get(PICTURE_FOLDER, pictureName);
		LOG.debug("Path constructred: {}", path);
		
		File file = path.toFile();
		LOG.debug("File constructred: {}", file);
		
		if (file.exists())
			return file;
		
		throw new FileNotFoundException("Couldn't find file: " + pictureName);
	}

	@Override
	public File getDocument(String documentName) {
		LOG.debug("Trying to find file '{}' in folder '{}'", documentName, DOCUMENT_FOLDER);
		
		Path path = Paths.get(DOCUMENT_FOLDER, documentName);
		LOG.debug("Path constructred: {}", path);
		
		File file = path.toFile();
		LOG.debug("File constructred: {}", file);
		
		if (file.exists())
			return file;
		
		throw new FileNotFoundException("Couldn't find file: " + documentName);
	}
	
	@Override
	public String saveFile(String folder, String name, MultipartFile multiPartFile) throws IOException {
		if (!(folder.equals(PICTURE_FOLDER) || folder.equals(DOCUMENT_FOLDER)))
			throw new FileNotFoundException("Couldn't find folder: " + folder);
		
		if (folder.equals(PICTURE_FOLDER) && multiPartFile.getSize() > 1024 * 1024)
			return "Please upload picture that is less than 1MB in size!";
		
		String contentType = multiPartFile.getOriginalFilename();
		if (contentType == null)
			contentType = "";
		
		int fileTypeIndex = contentType.lastIndexOf('.');
		contentType = fileTypeIndex < 0 ? "" : contentType.substring(fileTypeIndex);
		if (!contentType.isEmpty() && name.endsWith(contentType))
			name = name.substring(0, name.lastIndexOf(contentType));
		
		String updatedName;
		File file;
		do {
			updatedName = name + System.currentTimeMillis() + contentType;
			LOG.debug("Trying to save file '{}' in folder '{}'", name, folder);
			
			Path path = Paths.get(folder, updatedName);
			LOG.debug("Path constructred: {}", path);
			
			file = path.toFile();
			LOG.debug("File constructred: {}", file);
		} while (file.exists());
		
		BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(file));
		byte[] bytes = multiPartFile.getBytes();
        stream.write(bytes);
        stream.close();
        
		return updatedName;
	}

	// PRIVATE
	
	private static final String PICTURE_FOLDER = "pictures";
	private static final String DOCUMENT_FOLDER = "documents";
	
	private static final Logger LOG = LoggerFactory.getLogger(FileServiceImpl.class);

}
