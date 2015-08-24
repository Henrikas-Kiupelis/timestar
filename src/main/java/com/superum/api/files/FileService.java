package com.superum.api.files;

import com.superum.api.files.exception.FileNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * Manages file methods, such as uploading, deleting, reading, etc
 */
@Service
public interface FileService {

	/**
	 * Retrieves a file from pictures folder, by its name
	 * 
	 * @param pictureName - name of the file to be retrieved
	 * @return file with the specified name, from the pictures folder
	 * @throws FileNotFoundException if no such file was found
	 */
	File getPicture(String pictureName);

	/**
	 * Retrieves a file from documents folder, by its name
	 * 
	 * @param documentName - name of the file to be retrieved
	 * @return file with the specified name, from the documents folder
	 * @throws FileNotFoundException if no such file was found
	 */
	File getDocument(String documentName);

	/**
	 * Saves a file<p>
	 * 
	 * The folders which can be saved to are limited (for example, documents/pictures only)<p>
	 * 
	 * The name is adjusted to avoid duplication by inserting current timestamp (long value) into the name; this adjusted name is returned
	 * 
	 * @param folder - folder to save the file into
	 * @param name - name to use when saving the file
	 * @param file - the file to be saved
	 * @return adjusted name of the saved file, which can be used to retrieve it
	 * @throws IOException if the saving failed
	 */
	String saveFile(String folder, String name, MultipartFile file) throws IOException;

	/**
	 * Deletes a file from pictures folder, by its name
	 * 
	 * @param pictureName - name of the file to be deleted
	 * @throws FileNotFoundException if no such file was found
	 */
    void deletePicture(String pictureName);

	/**
	 * Deletes a file from documents folder, by its name
	 * 
	 * @param documentName - name of the file to be deleted
	 * @throws FileNotFoundException if no such file was found
	 */
    void deleteDocument(String documentName);

}
