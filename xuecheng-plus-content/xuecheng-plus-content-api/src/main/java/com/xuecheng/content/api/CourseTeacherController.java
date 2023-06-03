package com.xuecheng.content.api;

import com.xuecheng.content.model.dto.CourseBaseInfoDto;
import com.xuecheng.content.model.dto.EditCourseDto;
import com.xuecheng.content.model.dto.SaveTeachplanDto;
import com.xuecheng.content.model.po.CourseTeacher;
import com.xuecheng.content.service.CourseTeacherService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author : dongguohui
 * @description : 师资管理
 */
@Api(value = "课程师资信息接口",tags = "课程师资信息接口")
@RestController
public class CourseTeacherController {
    @Autowired
    CourseTeacherService courseTeacherService;


    @ApiOperation("查询教师列表")
    @GetMapping("/courseTeacher/list/{courseId}")
    public List<CourseTeacher> getCourseTeacherList(@PathVariable("courseId") Long courseId){
       return courseTeacherService.courseTeacherList(courseId);
    }

    @ApiOperation("添加教师")
    @PostMapping ("/courseTeacher")
    public CourseTeacher insertAndEditCourseTeacher(@RequestBody CourseTeacher courseTeacher){
        return courseTeacherService.saveAndEditTeacher(courseTeacher);
    }

    /**
     * @description 该接口无流量，前端界面好像没有定义put请求方式，修改已与添加合并
     * @param courseTeacher
     * @return
     */
    @ApiOperation("修改教师基础信息")
    @PutMapping("/courseTeacher")
    public CourseTeacher modifyCourseTeacher(@RequestBody CourseTeacher courseTeacher){
        return courseTeacherService.changeTeacher(courseTeacher);
    }

    @ApiOperation("删除教师课程基础信息")
    @DeleteMapping ("/courseTeacher/course/{courseId}/{teacherId}")
    public void deleteCourseTeacher(@PathVariable("courseId") Long courseId,@PathVariable("teacherId") Long teacherId){
        courseTeacherService.removeCourseTeacher(courseId,teacherId);
    }
}
