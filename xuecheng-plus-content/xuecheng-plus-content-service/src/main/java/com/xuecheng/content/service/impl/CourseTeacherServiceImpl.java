package com.xuecheng.content.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xuecheng.base.execption.XueChengPlusException;
import com.xuecheng.content.mapper.CourseTeacherMapper;
import com.xuecheng.content.mapper.TeachplanMapper;
import com.xuecheng.content.model.po.CourseTeacher;
import com.xuecheng.content.model.po.Teachplan;
import com.xuecheng.content.service.CourseTeacherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author : dongguohui
 * @description : 查询师资基本信息
 *
 */
@Slf4j
@Service
public class CourseTeacherServiceImpl implements CourseTeacherService {
    @Autowired
    CourseTeacherMapper courseTeacherMapper;


    @Override
    public List<CourseTeacher> courseTeacherList(Long courseId) {
        LambdaQueryWrapper<CourseTeacher> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(CourseTeacher::getCourseId,courseId);
        List<CourseTeacher> courseTeacherList = courseTeacherMapper.selectList(lambdaQueryWrapper);
        return courseTeacherList;
    }

    @Override
    public CourseTeacher saveAndEditTeacher(CourseTeacher courseTeacher) {
        Long teacherId = courseTeacher.getId();
        if(teacherId == null){
            CourseTeacher courseTeacherNew = new CourseTeacher();
            BeanUtils.copyProperties(courseTeacher,courseTeacherNew);
            int insertCounts = courseTeacherMapper.insert(courseTeacherNew);
            if(insertCounts <= 0){
                throw new XueChengPlusException("新增课程教师失败，请重试！！！");
            }
            Long id = courseTeacherNew.getId();
            CourseTeacher courseTeacher1 = courseTeacherMapper.selectById(id);
            if (courseTeacher1 == null){
                throw new XueChengPlusException("查询新增课程教师失败，请重试！！！");
            }
            return courseTeacher1;
        }else{
            CourseTeacher courseTeacherUpdate = courseTeacherMapper.selectById(courseTeacher.getId());
            BeanUtils.copyProperties(courseTeacher,courseTeacherUpdate);
            int counts = courseTeacherMapper.updateById(courseTeacherUpdate);
            if(counts <= 0){
                throw new XueChengPlusException("教师信息更新失败，请重试！！！");
            }
            return courseTeacherUpdate;
        }

    }

    @Override
    public CourseTeacher changeTeacher(CourseTeacher courseTeacher) {
        CourseTeacher courseTeacherUpdate = courseTeacherMapper.selectById(courseTeacher.getId());
        BeanUtils.copyProperties(courseTeacher,courseTeacherUpdate);
        int counts = courseTeacherMapper.updateById(courseTeacherUpdate);
        if(counts <= 0){
            throw new XueChengPlusException("教师信息更新失败，请重试！！！");
        }
        return courseTeacherUpdate;
    }

    @Override
    public void removeCourseTeacher(Long courseId, Long teacherId) {
        LambdaQueryWrapper<CourseTeacher> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(CourseTeacher::getCourseId,courseId);
        lambdaQueryWrapper.eq(CourseTeacher::getId,teacherId);
        int counts = courseTeacherMapper.delete(lambdaQueryWrapper);

        if(counts <= 0){
            throw new XueChengPlusException("删除失败，请重试！！！");
        }
    }
}
