package com.superum.db.files;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class FileController {

	@RequestMapping(value="/{folder}/upload", method=RequestMethod.POST)
	@ResponseBody 
	public String addPicture(@PathVariable String folder, @RequestParam("name") String name, @RequestParam("file") MultipartFile file) {
		if (file.isEmpty())
			return "Please upload non-empty file!";
		
		 try {
             return fileService.saveFile(folder, name, file);
         } catch (IOException e) {
        	 e.printStackTrace();
             return "Picture upload failed. Please try again!";
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
	
	@RequestMapping(value = "/pictures/delete/{pictureName:.+}", method = RequestMethod.GET)
	@ResponseBody
	public String deletePicture(@PathVariable String pictureName) {
	    return fileService.deletePicture(pictureName); 
	}
	
	@RequestMapping(value = "/documents/delete/{documentName:.+}", method = RequestMethod.GET)
	@ResponseBody
	public String deleteDocument(@PathVariable String documentName) {
	    return fileService.deleteDocument(documentName); 
	}
	
	// CONSTRUCTORS
	
	@Autowired
	public FileController(FileService fileService) {
		this.fileService = fileService;
	}
	
	// PRIVATE
	
	private final FileService fileService;

}
