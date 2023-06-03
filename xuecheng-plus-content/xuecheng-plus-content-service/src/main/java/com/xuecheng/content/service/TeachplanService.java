package com.xuecheng.content.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xuecheng.content.model.dto.SaveTeachplanDto;
import com.xuecheng.content.model.dto.TeachplanDto;
import com.xuecheng.content.model.po.Teachplan;

import java.util.List;

/**
 * @Author : dongguohui
 * @description : 课程基本信息管理业务接口
 */
public interface TeachplanService {
    /**
     * @description 查询课程计划树型结构
     * @param courseId  课程id
     * @return List<TeachplanDto>
     */
    public List<TeachplanDto> findTeachplanTree(long courseId);

    /**
     * @description 只在课程计划
     * @param teachplanDto  课程计划信息
     * @return void
     */
    public void saveTeachplan(SaveTeachplanDto teachplanDto);

    /**
     * @description 删除课程计划
     * @param teachplanId 课程计划
     * @return void
     */
    public void removeTeachplan(Long teachplanId);

    /**
     * @description 移动教学计划的排序，包括小节顺序和章顺序
     * @param move 移动类型，包括上移和下移
     * @param teachplanId 要移动的教学计划id
     * @return void
     */
    public void moveTeachplanOrder(String move, Long teachplanId);
}
