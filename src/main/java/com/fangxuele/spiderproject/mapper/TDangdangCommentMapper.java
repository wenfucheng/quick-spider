package com.fangxuele.spiderproject.mapper;

import com.fangxuele.spiderproject.domain.TDangdangComment;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TDangdangCommentMapper {
    int deleteByPrimaryKey(Long id);

    int insert(TDangdangComment record);

    int insertSelective(TDangdangComment record);

    TDangdangComment selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TDangdangComment record);

    int updateByPrimaryKey(TDangdangComment record);

    List<Long> getRepeatId();

    void deleteByIds(@Param("ids") List<Long> repeatIds);

    List<String> findJingDongIsbn();
}