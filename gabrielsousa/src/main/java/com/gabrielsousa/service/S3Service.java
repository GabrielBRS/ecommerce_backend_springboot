package com.gabrielsousa.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import com.gabrielsousa.service.exception.FileException;

@Service
public class S3Service {

	private Logger LOG = LoggerFactory.getLogger(S3Service.class);
	
	@Autowired
	private AmazonS3 s3client;

	@Value("${s3.bucket}")
	private String bucketName;
	
//	public URI uploadFile(MultipartFile multipartFile) {
//		try {
//			String fileName = multipartFile.getOriginalFilename();
//			InputStream is = multipartFile.getInputStream();
//			String contentType = multipartFile.getContentType();
//			return uploadFile(is, fileName, contentType);
//		} catch (IOException e) {
//			throw new FileException("Erro de IO: " + e.getMessage());
//		}
//	}

	public URI uploadFile(InputStream is, String fileName, String contentType) {
		try {
			ObjectMetadata meta = new ObjectMetadata();
			meta.setContentType(contentType);
			LOG.info("Iniciando upload");
			s3client.putObject(bucketName, fileName, is, meta);
			LOG.info("Upload finalizado");
			return s3client.getUrl(bucketName, fileName).toURI();
		} catch (URISyntaxException e) {
			throw new FileException("Erro ao converter URL para URI");
		}
	}

	//Método para mockar dados
	
//	public void uploadFile(String localFilePath) {
//		
//		try {
//			File file = new File(localFilePath);
//			LOG.info("Iniciando upload");
//			s3client.putObject(new PutObjectRequest(bucketName,"test", file));
//			LOG.info("Upload finalizado");
//		} catch (AmazonServiceException e) {
//			LOG.info("AmazonServiceException: " + e.getErrorCode());
//			LOG.info("Status code: " + e.getErrorCode());
//		} catch (AmazonClientException e) {
//			LOG.info("AmazonClientException: " + e.getMessage());
//		}
//	}
	
//	public String uploadFile(MultipartFile file) {
//		File fileObj = convertMultiPartFileToFile(file);
//		String fileName = System.currentTimeMillis()+""+file.getOriginalFilename();
//		s3client.putObject(new PutObjectRequest(bucketName, fileName, fileObj));
//		fileObj.delete();
//		return "File uploaded: " + fileName;
//	}
//	
//	public byte[] downloadFile(String fileName) {
//		S3Object s3Object = s3client.getObject(bucketName, fileName);
//		S3ObjectInputStream inputStream = s3Object.getObjectContent();
//		try {
//			byte[] content = IOUtils.toByteArray(inputStream);
//			return content;
//		}catch(IOException e) {
//			e.printStackTrace();
//		}
//		return null;
//	}
//	
//	public String deleteFile(String fileName) {
//		s3client.deleteObject(bucketName, fileName);
//		return fileName + " removed ...";
//	}
//	
//	private File convertMultiPartFileToFile(MultipartFile file) {
//		
//		File convertedFile = new File(file.getOriginalFilename());
//		try (FileOutputStream fos = new FileOutputStream(convertedFile)){
//			fos.write(file.getBytes());
//		}catch (IOException e) {
//			LOG.error("error", e);
//		}
//		return convertedFile;
//	}
}
