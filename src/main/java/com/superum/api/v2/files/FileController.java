package com.superum.api.v2.files;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * <pre>
 * API v2
 * Manages all requests for files
 * Please refer to API file for documentation of particular methods
 * (you should not be calling these methods directly unless you know what you're doing)
 * </pre>
 */
@RestController
public class FileController {

	@RequestMapping(value="/{folder:pictures|documents}/upload", method=RequestMethod.POST)
	@ResponseBody 
	public String addPicture(@PathVariable String folder, @RequestParam("name") String name, @RequestParam("file") MultipartFile file) {
		if (file.isEmpty())
			return "Please upload non-empty file!";
		
		 try {
             return fileService.saveFile(folder, name, file);
         } catch (IOException e) {
        	 e.printStackTrace();
             return folder + " upload failed. Please try again!";
         }
	}
	
	@RequestMapping(value = "/pictures/{pictureName:.+}", method = RequestMethod.GET)
	@ResponseBody
	public FileSystemResource getPicture(@PathVariable String pictureName) {
	    return new FileSystemResource(fileService.getPicture(pictureName)); 
	}
	
	@RequestMapping(value = "/documents/{documentName:.+}", method = RequestMethod.GET, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	@ResponseBody
	public FileSystemResource getDocument(@PathVariable String documentName) {
	    return new FileSystemResource(fileService.getDocument(documentName)); 
	}
	
	@RequestMapping(value = "/pictures/delete/{pictureName:.+}", method = RequestMethod.DELETE)
	@ResponseBody
	public void deletePicture(@PathVariable String pictureName) {
	    fileService.deletePicture(pictureName);
	}
	
	@RequestMapping(value = "/documents/delete/{documentName:.+}", method = RequestMethod.DELETE)
	@ResponseBody
	public void deleteDocument(@PathVariable String documentName) {
	    fileService.deleteDocument(documentName);
	}
	
	// CONSTRUCTORS
	
	@Autowired
	public FileController(FileService fileService) {
		this.fileService = fileService;
	}
	
	// PRIVATE
	
	private final FileService fileService;

}
