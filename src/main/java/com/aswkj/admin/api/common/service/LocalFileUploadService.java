package com.aswkj.admin.api.common.service;


import com.aswkj.admin.api.common.model.LocalStoreModel;
import org.springframework.web.multipart.MultipartFile;


public interface LocalFileUploadService {

  LocalStoreModel upload(MultipartFile file);

  void deleteFile(LocalStoreModel localStoreModel);

}
