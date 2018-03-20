package com.fangxuele.spiderproject.cache;

public interface RedisCacheConstant {

    /**
     * 缓存KEY的分隔符
     */
    String KeySeparator = ":";

    /**
     * 有序集合的顺序表示，升序，是递增的顺序
     */
    int ASC = 0;

    /**
     * 有序集合的顺序表示，降序，是递减的顺序
     */
    int DESC = 1;

    /**
     * 当当爬取成功评价图书id集合（List结构）
     */
    String DANG_DANG_SUCCESS_BOOK_ID = "DangDangSuccessBookId";

    /**
     * 当当爬取失败评价图书id集合（List结构）
     */
    String DANG_DANG_FAIL_BOOK_ID = "DangDangFailBookId";


    /**
     * 当当爬取部分成功评价图书id集合（set结构）
     */
    String DANG_DANG_PART_SUCCESS_BOOK_ID = "DangDangPartSuccessBookId";


}