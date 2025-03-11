package com.weight.services;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageService {

	//@Value("${upload.img.colis.dir}")
	// private String uploadDir;
			 
    private String uploadColisDir="/var/images/colis";
    private String uploadProfileDir="/var/images/profile";
    private String defaultDir="/var/images/default";
    
    private String s3uploadColisDir="colis";
    private String s3uploadProfileDir="profile";
    private String s3defaultDir="default";
    
    private String fileNameDefault = "no_photo_default.jpeg";
    
	
	public String uploadImage(MultipartFile file,String type) throws IOException {
		String uploadDir = uploadColisDir;
		
		if(type.equals("profile"))
			uploadDir = uploadProfileDir;
		
        // Générer un nom de fichier unique
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path filePath = Paths.get(uploadDir, fileName);

        // Enregistrer le fichier dans le répertoire local
        Files.copy(file.getInputStream(), filePath);

        return fileName;
    }
	
	
	public byte[] downloadImage(String fileName,String type ) throws IOException {
		String uploadDir = uploadColisDir;
		Path filePath = Paths.get(defaultDir, fileNameDefault);
		
		if(type.equals("profile"))
			uploadDir = uploadProfileDir;
		
		if( Paths.get(uploadDir, fileName)!=null)
			filePath = Paths.get(uploadDir, fileName);
		
        return Files.readAllBytes(filePath);
    }
	
	public void deleteImage(String fileName,String type ) {

		String uploadDir = uploadColisDir;

		if(type.equals("profile"))
			uploadDir = uploadProfileDir;

		try {
			Path filePath = Paths.get(uploadDir, fileName);
			File file = filePath.toFile();
			if (file.exists()) {
				if (file.delete()) {
					System.out.println("Fichier supprimé avec succès: " + fileName);
				} else {
					System.out.println("Impossible de supprimer le fichier: " + fileName);
				}
			} else {
				System.out.println("Le fichier n'existe pas: " + fileName);
			}
		} catch (Exception ex) {
			System.out.println("Erreur lors de la suppression du fichier: " + ex.getMessage());
		}
	}
	 
	public byte[] downloadImageDefault() throws IOException {
		Path filePath = Paths.get(defaultDir, fileNameDefault);	
        return Files.readAllBytes(filePath);
    }
	
	
	//*****************************  GESTION AWS S3 *********************************************************//
	
	public String uploadImage(MultipartFile file,String type,S3Service s3Service) throws IOException {
		String uploadDir = s3uploadColisDir;
		
		if(type.equals("profile"))
			uploadDir = s3uploadProfileDir;

        // Enregistrer le fichier dans le répertoire local
        String fileName = s3Service.uploadFile(file, uploadDir);
        
        return fileName;
    }
	
	 // Télécharger une image depuis S3
    public File downloadImage(String fileName,String type,S3Service s3Service ) {
    	String uploadDir = s3uploadColisDir;
		if(type.equals("profile"))
			uploadDir = s3uploadProfileDir;
		
        File downloadedFile = s3Service.downloadFile(uploadDir,fileName);

        return downloadedFile;
    }
    
    
    // Télécharger une image depuis S3
    public File downloadImageDefault(S3Service s3Service ) {
		
        File downloadedFile = s3Service.downloadFile(s3defaultDir,fileNameDefault);

        return downloadedFile;
    }
    
    
    public void deleteImage(String fileName,String type,S3Service s3Service ) {

		String uploadDir = s3uploadColisDir;

		if(type.equals("profile"))
			uploadDir = s3uploadProfileDir;

		try {
			s3Service.deleteFile(uploadDir,fileName);
		} catch (Exception ex) {
			System.out.println("Erreur lors de la suppression du fichier: " + ex.getMessage());
		}
	}
   
}
