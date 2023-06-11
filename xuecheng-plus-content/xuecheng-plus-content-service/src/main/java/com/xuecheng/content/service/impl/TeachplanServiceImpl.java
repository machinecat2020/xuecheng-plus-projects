package com.xuecheng.content.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xuecheng.base.execption.XueChengPlusException;
import com.xuecheng.content.mapper.TeachplanMapper;
import com.xuecheng.content.mapper.TeachplanMediaMapper;
import com.xuecheng.content.model.dto.BindTeachplanMediaDto;
import com.xuecheng.content.model.dto.SaveTeachplanDto;
import com.xuecheng.content.model.dto.TeachplanDto;
import com.xuecheng.content.model.po.Teachplan;
import com.xuecheng.content.model.po.TeachplanMedia;
import com.xuecheng.content.service.TeachplanService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.OptionalDouble;
import java.util.stream.Collectors;

/**
 * @Author : dongguohui
 * @description :
 */
@Service
public class TeachplanServiceImpl implements TeachplanService {
    @Autowired
    private TeachplanMapper teachplanMapper;
    @Autowired
    private TeachplanMediaMapper teachplanMediaMapper;
    @Autowired
    private TeachplanService teachplanService;

    @Override
    public List<TeachplanDto> findTeachplanTree(long courseId) {
        return teachplanMapper.selectTreeNodes(courseId);
    }

    @Transactional
    @Override
    public void saveTeachplan(SaveTeachplanDto teachplanDto) {
        //课程计划id
        Long id = teachplanDto.getId();
        //修改课程计划
        if(id != null){ //说明是课程已存在，属于修改课程
            Teachplan teachplan = teachplanMapper.selectById(id);
            BeanUtils.copyProperties(teachplanDto,teachplan);
            teachplanMapper.updateById(teachplan);
        }else{//说明课程不存在，属于新增课程
            //取出同父同级别的课程计划数量
            // TODO 这种方式存在一个问题，比如我有四个同级，odrerby字段分别为：1234，但是我删除1，这时新增同级取出数量加一orderby为4和原本的orderby为4的冲突
            int count = getTeachplanCount(teachplanDto.getCourseId(), teachplanDto.getParentid());
            Teachplan teachplanNew = new Teachplan();
            //设置排序号
            teachplanNew.setOrderby(count+1);
            BeanUtils.copyProperties(teachplanDto,teachplanNew);

            teachplanMapper.insert(teachplanNew);
        }

    }

    @Transactional
    @Override
    public void removeTeachplan(Long teachplanId) {
        Teachplan teachplan = teachplanMapper.selectById(teachplanId);
        if(teachplan == null){
            throw new XueChengPlusException("要删除的课程计划不存在！！！");
        }
        //一级节点章
        if(teachplan.getParentid() == 0){
            //查询章节下面小节个数
            LambdaQueryWrapper<Teachplan> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Teachplan::getParentid,teachplanId);
            int counts = teachplanMapper.selectCount(queryWrapper);
            // 删除大章节，大章节下有小章节时不允许删除
            if(counts != 0){
                throw new XueChengPlusException("课程计划信息还有子级信息，无法操作");
            }else{
                // 删除大章节，大章节下没有小章节时可以正常删除
                teachplanMapper.deleteById(teachplanId);
            }
        }else {
            // 删除小章节, 同时需要将teachplan_media关联想表信息删除
            teachplanMapper.deleteById(teachplanId);
            teachplanMediaMapper.deleteById(teachplanId);
        }
    }

    @Transactional
    @Override
    public void moveTeachplanOrder(String move, Long teachplanId) {
        Teachplan teachplan = teachplanMapper.selectById(teachplanId);
        if(teachplan == null){
            throw new XueChengPlusException("要移动的条目不存在，请重试！！！");
        }
        LambdaQueryWrapper<Teachplan> lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.eq(Teachplan::getParentid,teachplan.getParentid());
        lambdaQueryWrapper.eq(Teachplan::getCourseId,teachplan.getCourseId());
        List<Teachplan> teachplanList = teachplanMapper.selectList(lambdaQueryWrapper);
        // 升序排列
        List<Teachplan> teachplanListUp = teachplanList.stream().sorted(Comparator.comparing(Teachplan::getOrderby)).collect(Collectors.toList());
        //获得升序序列中最后一个小于目标序号的Teachplan，如果自身就是最上面则teachplanBefore为null
        Teachplan teachplanBefore = null;
        for(Teachplan teachplan1 : teachplanListUp){
            if(teachplan1.getOrderby() == teachplan.getOrderby()){
                break;
            }
            teachplanBefore = teachplan1;
        }
        // 降序排列
        List<Teachplan> teachplanListDown = teachplanList.stream().sorted(Comparator.comparing(Teachplan::getOrderby).reversed()).collect(Collectors.toList());
        //获得降序序列中第一个大于目标序号的Teachplan，如果自身就是最下面则teachplanAfter为null
        Teachplan teachplanAfter = null;
        for(Teachplan teachplan1 : teachplanListDown){
            if(teachplan1.getOrderby() == teachplan.getOrderby()){
                break;
            }
            teachplanAfter = teachplan1;
        }
        // 上升则交换与上面的那个的排序值，要是没有上面则不交换（自己就是最上面）
        if(move.equals("moveup")){
            // 说明不是最上面的一个章或者节
            if(teachplanBefore != null){
                int orderNumber = teachplanBefore.getOrderby();
                teachplanBefore.setOrderby(teachplan.getOrderby());
                teachplan.setOrderby(orderNumber);
                teachplanMapper.updateById(teachplan);
                teachplanMapper.updateById(teachplanBefore);
            }
        }

        // 下降则交换与下面的那个的排序值，要是没有下面则不交换（自己就是最下面）
        if(move.equals("movedown")){
            // 说明不是最下面的一个章或者节
            if(teachplanAfter != null){
                int orderNumber = teachplanAfter.getOrderby();
                teachplanAfter.setOrderby(teachplan.getOrderby());
                teachplan.setOrderby(orderNumber);
                teachplanMapper.updateById(teachplan);
                teachplanMapper.updateById(teachplanAfter);
            }
        }
    }

    @Transactional
    @Override
    public TeachplanMedia associationMedia(BindTeachplanMediaDto bindTeachplanMediaDto) {
        //教学计划id
        Long teachplanId = bindTeachplanMediaDto.getTeachplanId();
        Teachplan teachplan = teachplanMapper.selectById(teachplanId);
        if(teachplan==null){
            XueChengPlusException.cast("教学计划不存在");
        }
        Integer grade = teachplan.getGrade();
        if(grade!=2){
            XueChengPlusException.cast("只允许第二级教学计划绑定媒资文件");
        }
        //课程id
        Long courseId = teachplan.getCourseId();

        //先删除原来该教学计划绑定的媒资
        teachplanMediaMapper.delete(new LambdaQueryWrapper<TeachplanMedia>().eq(TeachplanMedia::getTeachplanId,teachplanId));

        //再添加教学计划与媒资的绑定关系
        TeachplanMedia teachplanMedia = new TeachplanMedia();
        teachplanMedia.setCourseId(courseId);
        teachplanMedia.setTeachplanId(teachplanId);
        teachplanMedia.setMediaFilename(bindTeachplanMediaDto.getFileName());
        teachplanMedia.setMediaId(bindTeachplanMediaDto.getMediaId());
        teachplanMedia.setCreateDate(LocalDateTime.now());
        teachplanMediaMapper.insert(teachplanMedia);
        return teachplanMedia;
    }

    @Transactional
    @Override
    public void deleteAssociationMedia(Long teachplanId,String mediaId) {
        if (teachplanId == null || mediaId == null) {
            XueChengPlusException.cast("非法的解除绑定请求");
        }

        //教学计划id
        Teachplan teachplan = teachplanMapper.selectById(teachplanId);
        if (teachplan == null) {
            XueChengPlusException.cast("教学计划不存在");
        }
        Integer grade = teachplan.getGrade();
        if (grade != 2) {
            XueChengPlusException.cast("只允许第二级教学计划绑定媒资文件");
        }
        LambdaQueryWrapper<TeachplanMedia> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(TeachplanMedia::getMediaId, mediaId);
        lambdaQueryWrapper.eq(TeachplanMedia::getTeachplanId, teachplanId);
        int counts = teachplanMediaMapper.delete(lambdaQueryWrapper);
        if (counts <= 0) {
            throw new XueChengPlusException("解除绑定失败");
        }
    }


    /**
     * @param courseId 课程id
     * @param parentId 父课程计划id
     * @return int 最新排序号
     * @description 获取最新的排序号
     */
    private int getTeachplanCount(long courseId, long parentId) {
        LambdaQueryWrapper<Teachplan> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Teachplan::getCourseId,courseId);
        queryWrapper.eq(Teachplan::getParentid,parentId);
        Integer count = teachplanMapper.selectCount(queryWrapper);
        return count;
    }

}
