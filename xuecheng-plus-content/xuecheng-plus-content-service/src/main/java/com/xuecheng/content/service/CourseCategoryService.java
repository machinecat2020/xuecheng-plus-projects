package com.xuecheng.content.service;

import com.xuecheng.content.model.dto.CourseCategoryTreeDto;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @Author : dongguohui
 * @description : 查询树形目录
 */
public interface CourseCategoryService {
    /**
     * 课程分类树形结构查询
     *
     * @return
     */
    public List<CourseCategoryTreeDto> queryTreeNodes(String id);

}
