package com.xuecheng.content.service;

import com.xuecheng.content.model.dto.CoursePreviewDto;
import com.xuecheng.content.model.po.CoursePublish;

import java.io.File;
import java.io.IOException;

/**
 * @Author : dongguohui
 * @description : 课程预览、发布接口
 */
public interface CoursePublishService {
    /**
     * @description 获取课程预览信息
     * @param courseId 课程id
     * @return com.xuecheng.content.model.dto.CoursePreviewDto
     * @author dongguohui
     */
    public CoursePreviewDto getCoursePreviewInfo(Long courseId);

    /**
     * @description 提交审核
     * @param courseId  课程id
     * @return void
     * @author dongguohui
     */
    public void commitAudit(Long companyId,Long courseId);

    /**
     * @description 课程发布接口
     * @param companyId 机构id
     * @param courseId 课程id
     * @return void
     * @author dongguohui
     */
    public void publish(Long companyId,Long courseId);

    /**
     * @description 课程静态化
     * @param courseId  课程id
     * @return File 静态化文件
     * @author dongguohui
     */
    public File generateCourseHtml(Long courseId);

    /**
     * @description 上传课程静态化页面
     * @param file  静态化文件
     * @return void
     * @author dongguohui
     */
    public void  uploadCourseHtml(Long courseId,File file) throws IOException;


    /**
     * @param courseId
     * @return CoursePublish
     * @description 根据课程Id查询课程发布情况
     * @author dongguohui
     */
    public CoursePublish getCoursePublish(Long courseId);

}
