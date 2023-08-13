package com.xuecheng.media.service;

import com.xuecheng.base.model.PageParams;
import com.xuecheng.base.model.PageResult;
import com.xuecheng.base.model.RestResponse;
import com.xuecheng.media.model.dto.QueryMediaParamsDto;
import com.xuecheng.media.model.dto.UploadFileParamsDto;
import com.xuecheng.media.model.dto.UploadFileResultDto;
import com.xuecheng.media.model.po.MediaFiles;

import java.io.File;

/**
 * @description 媒资文件管理业务类
 * @author dongguohui
 */
public interface MediaFileService {

   /**
    * @param mediaId
    * @return MediaFiles
    */
   public MediaFiles getFileById(String mediaId);

   /**
    * @description 媒资文件查询方法
    * @param pageParams 分页参数
    * @param queryMediaParamsDto 查询条件
    * @return com.xuecheng.base.model.PageResult<com.xuecheng.media.model.po.MediaFiles>
    * @author dongguohui
   */
   public PageResult<MediaFiles> queryMediaFiels(Long companyId,PageParams pageParams, QueryMediaParamsDto queryMediaParamsDto);

   /**
    * 上传文件
    * @param companyId 机构id
    * @param uploadFileParamsDto 上传文件信息
    * @param localFilePath 文件磁盘路径
    * @param objectName 对象名,传入则按照objectname目录进行上传，否则按照年月日
    * @return 文件信息
    * @author dongguohui
    */
   public UploadFileResultDto uploadFile(Long companyId, UploadFileParamsDto uploadFileParamsDto, String localFilePath,String objectName);

   public MediaFiles addMediaFilesToDb(Long companyId,String fileMd5,UploadFileParamsDto uploadFileParamsDto,String bucket,String objectName);


   /**
    * @description 检查文件是否存在
    * @param fileMd5 文件的md5
    * @return com.xuecheng.base.model.RestResponse<java.lang.Boolean> false不存在，true存在
    * @author dongguohui
    */
   public RestResponse<Boolean> checkFile(String fileMd5);

   /**
    * @description 检查分块是否存在
    * @param fileMd5  文件的md5
    * @param chunkIndex  分块序号
    * @return com.xuecheng.base.model.RestResponse<java.lang.Boolean> false不存在，true存在
    * @author dongguohui
    */
   public RestResponse<Boolean> checkChunk(String fileMd5, int chunkIndex);

   /**
    * @description 上传分块
    * @param fileMd5  文件md5
    * @param chunk  分块序号
    * @param localChunkFilePath  分块文件本地路径
    * @return com.xuecheng.base.model.RestResponse
    * @author dongguohui
    */
   public RestResponse uploadChunk(String fileMd5,int chunk,String localChunkFilePath);

   /**
    * @description 合并分块
    * @param companyId  机构id
    * @param fileMd5  文件md5
    * @param chunkTotal 分块总和
    * @param uploadFileParamsDto 文件信息
    * @return com.xuecheng.base.model.RestResponse
    * @author dongguohui
    */
   public RestResponse mergechunks(Long companyId,String fileMd5,int chunkTotal,UploadFileParamsDto uploadFileParamsDto);

   /**
    *  开启一个任务
    * @param id 任务id
    * @return true开启任务成功，false开启任务失败
    */
   public boolean startTask(long id);

   /**
    * 从minio下载文件
    *
    * @param bucket 桶
    * @param objectName 对象名称
    * @return 下载后的文件
    */
   public File downloadFileFromMinIO(String bucket, String objectName);

   /**
    * 将文件上传到minio
    *
    * @param localFilePath 文件本地路径
    * @param mimeType      媒体类型
    * @param bucket        桶
    * @param objectName    对象名
    * @return boolean
    */
   public boolean addMediaFilesToMinIO(String localFilePath, String mimeType, String bucket, String objectName);



}
