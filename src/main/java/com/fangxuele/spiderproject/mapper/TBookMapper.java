package com.fangxuele.spiderproject.mapper;

import com.fangxuele.spiderproject.domain.TBook;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface TBookMapper {
    int deleteByPrimaryKey(Long id);

    int insert(TBook record);

    int insertSelective(TBook record);

    TBook selectByPrimaryKey(Long id);

    Map<String, Object> selectBookDetailMapByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TBook record);

    int updateByPrimaryKey(TBook record);

    List<TBook> selectByIsbn(String isbn);

    List<Map<String, Object>> getBookList(Map<String, Object> paraMap);

    List<Map<String, Object>> getBookInfoByIds(@Param("bookIds") List<String> bookIds);

    List<String> getIsbnsByBookIds(@Param("bookIds") List<String> bookIds);

    List<Map<String, Object>> selectAllCreator();

	BigDecimal getPriceByBookIds(@Param("bookIds") List<String> bookIds);

    Long getCountByCategoryId(Long categoryId);

    List<TBook> findAll();

    Long selectMaxBookId();


}