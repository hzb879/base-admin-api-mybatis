package com.aswkj.admin.api.common.service;

import com.aswkj.admin.api.common.model.QiniuBucketInfoModel;
import com.aswkj.admin.api.common.model.QiniuImageStoreModel;
import com.qiniu.common.QiniuException;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Map;

public interface QiniuUploadService {

  QiniuImageStoreModel uploadFile(File file) throws QiniuException;

  QiniuImageStoreModel uploadFile(File file, String namespace) throws QiniuException;

  QiniuImageStoreModel uploadFile(String bucket, File file) throws QiniuException;

  QiniuImageStoreModel uploadFile(String bucket, File file, String namespace) throws QiniuException;

  QiniuImageStoreModel uploadFile(String bucket, String key, File file) throws QiniuException;

  QiniuImageStoreModel uploadFile(String bucket, String namespace, String key, File file) throws QiniuException;

  QiniuImageStoreModel uploadBytes(byte[] data) throws QiniuException;

  QiniuImageStoreModel uploadBytes(String bucket, byte[] data) throws QiniuException;

  QiniuImageStoreModel uploadBytes(String bucket, String namespace, byte[] data) throws QiniuException;

  QiniuImageStoreModel uploadBytes(byte[] data, String namespace) throws QiniuException;

  QiniuImageStoreModel uploadMultipartFile(String bucket, MultipartFile file);

  QiniuImageStoreModel uploadMultipartFile(String bucket, String namespace, MultipartFile file);

  QiniuImageStoreModel uploadMultipartFile(MultipartFile file, String namespace);

  QiniuImageStoreModel uploadMultipartFile(MultipartFile file);

  Map<String, QiniuBucketInfoModel> getQiniuBucketInfoModelMap() throws QiniuException;

  void clearQiniuBucketInfoModelMapCache();

  void resetBucketSettingMap() throws QiniuException;

}
