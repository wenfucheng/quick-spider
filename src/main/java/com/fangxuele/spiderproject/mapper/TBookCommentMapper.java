package com.fangxuele.spiderproject.mapper;

import com.fangxuele.spiderproject.domain.TBookComment;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface TBookCommentMapper {
    int deleteByPrimaryKey(Long id);

    int insert(TBookComment record);

    int insertSelective(TBookComment record);

    TBookComment selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TBookComment record);

    int updateByPrimaryKey(TBookComment record);

    List<TBookComment> getCommentByBookId(Long bookId);

    String getBookGoodRatingByBookId(String bookId);

    List<Map<String, Object>> getCommentList(Map<String, Object> paraMap);

    List<TBookComment> selectByCustomerIdAndBookId(@Param("customerId") Long customerId, @Param("bookId") Long bookId);
}