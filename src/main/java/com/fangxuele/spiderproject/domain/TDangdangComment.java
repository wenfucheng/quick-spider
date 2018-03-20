package com.fangxuele.spiderproject.domain;

import java.io.Serializable;

public class TDangdangComment implements Serializable {
    private Long id;

    private String title;

    private String isbn;

    private String url;

    private Integer totalCommentNum;

    private Integer totalCrazyCount;

    private Integer totalIndifferentCount;

    private Integer totalDetestCount;

    private String averageScore;

    private String goodRate;

    private String originalPrice;

    private String price;

    private Integer type;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn == null ? null : isbn.trim();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url == null ? null : url.trim();
    }

    public Integer getTotalCommentNum() {
        return totalCommentNum;
    }

    public void setTotalCommentNum(Integer totalCommentNum) {
        this.totalCommentNum = totalCommentNum;
    }

    public Integer getTotalCrazyCount() {
        return totalCrazyCount;
    }

    public void setTotalCrazyCount(Integer totalCrazyCount) {
        this.totalCrazyCount = totalCrazyCount;
    }

    public Integer getTotalIndifferentCount() {
        return totalIndifferentCount;
    }

    public void setTotalIndifferentCount(Integer totalIndifferentCount) {
        this.totalIndifferentCount = totalIndifferentCount;
    }

    public Integer getTotalDetestCount() {
        return totalDetestCount;
    }

    public void setTotalDetestCount(Integer totalDetestCount) {
        this.totalDetestCount = totalDetestCount;
    }

    public String getAverageScore() {
        return averageScore;
    }

    public void setAverageScore(String averageScore) {
        this.averageScore = averageScore == null ? null : averageScore.trim();
    }

    public String getGoodRate() {
        return goodRate;
    }

    public void setGoodRate(String goodRate) {
        this.goodRate = goodRate == null ? null : goodRate.trim();
    }

    public String getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(String originalPrice) {
        this.originalPrice = originalPrice == null ? null : originalPrice.trim();
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price == null ? null : price.trim();
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}