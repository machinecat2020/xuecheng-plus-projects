package com.xuecheng.content.service;

import com.xuecheng.content.model.po.CourseTeacher;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author : dongguohui
 * @description : 师资管理
 */
public interface CourseTeacherService {
    /**
     * @description 获取课程关联的教师目录
     * @param courseId 课程id
     * @return 课程相关教师列表
     */
    public List<CourseTeacher> courseTeacherList(Long courseId);

    /**
     * @decription 教师信息保存与编辑接口
     * @param courseTeacher 教师课程关系表
     * @return CourseTeacher
     */
    public CourseTeacher saveAndEditTeacher(CourseTeacher courseTeacher);

    /**
     * @decription 教师信息编辑接口
     * @param courseTeacher 教师课程关系表
     * @return CourseTeacher
     */
    public CourseTeacher changeTeacher(CourseTeacher courseTeacher);

    /**
     * @decription 删除课程教师信息接口
     * @param courseId 课程id
     * @param teacherId 教师id
     * @return void
     */
    public void removeCourseTeacher(Long courseId, Long teacherId);
}
