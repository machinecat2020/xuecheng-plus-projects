package com.xuecheng.content.api;

import com.xuecheng.content.model.dto.*;
import com.xuecheng.content.service.TeachplanService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author : dongguohui
 * @description : 课程计划编辑接口
 */
@Api(value = "课程计划编辑接口",tags = "课程计划编辑接口")
@RestController
public class TeachplanController {
    @Autowired
    TeachplanService teachplanService;

    @ApiOperation("查询课程计划树形结构")
    @ApiImplicitParam(value = "courseId",name = "课程Id",required = true,dataType = "Long",paramType = "path")
    @GetMapping("/teachplan/{courseId}/tree-nodes")
    public List<TeachplanDto> getTreeNodes(@PathVariable Long courseId){
        return teachplanService.findTeachplanTree(courseId);
    }

    @ApiOperation("课程计划创建或修改")
    @PostMapping("/teachplan")
    public void saveTeachplan( @RequestBody SaveTeachplanDto teachplan){
        teachplanService.saveTeachplan(teachplan);
    }

    @ApiOperation("根据课程id删除课程计划")
    @DeleteMapping("/teachplan/{teachplanId}")
    public void deleteTeachplan(@PathVariable Long teachplanId){
        teachplanService.removeTeachplan(teachplanId);
    }

    @ApiOperation("根据课程计划id对课程计划进行移动")
    @PostMapping ("/teachplan/{move}/{teachplanId}")
    public void moveTeachplan(@PathVariable("move") String move,@PathVariable("teachplanId") Long teachplanId){
        teachplanService.moveTeachplanOrder(move,teachplanId);
    }

    @ApiOperation(value = "课程计划和媒资信息绑定")
    @PostMapping("/teachplan/association/media")
    public void associationMedia(@RequestBody BindTeachplanMediaDto bindTeachplanMediaDto){
        teachplanService.associationMedia(bindTeachplanMediaDto);
    }

    @ApiOperation(value = "解除课程计划和媒资信息绑定")
    @DeleteMapping ("/teachplan/association/media/{teachplanId}/{mediaId}")
    public void deleteAssociationMedia(@PathVariable("teachplanId") Long teachplanId,@PathVariable("mediaId") String mediaId){

        teachplanService.deleteAssociationMedia(teachplanId,mediaId);
    }

}
