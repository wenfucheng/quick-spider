package com.fangxuele.spiderproject.dto;

import java.util.List;

/**
 * 京东图书评价信息json解析DTO
 * Created by wfc on 2017/8/27.
 */
public class JingDongCommentDTO {

	private List<CommentsCount> CommentsCount;

	public static class CommentsCount{
		private String SkuId;
		private String ProductId;
		private String ShowCount;
		private String ShowCountStr;
		private String CommentCountStr;
		private String CommentCount;
		private String AverageScore;
		private String GoodCountStr;
		private String GoodCount;
		private String AfterCount;
		private String OneYear;
		private String AfterCountStr;
		private String GoodRate;
		private String GoodRateShow;
		private String GoodRateStyle;
		private String GeneralCountStr;
		private String GeneralCount;
		private String GeneralRate;
		private String GeneralRateShow;
		private String GeneralRateStyle;
		private String PoorCount;
		private String PoorRate;
		private String PoorRateShow;
		private String PoorRateStyle;
		public String getSkuId() {
			return SkuId;
		}

		public void setSkuId(String skuId) {
			SkuId = skuId;
		}

		public String getProductId() {
			return ProductId;
		}

		public void setProductId(String productId) {
			ProductId = productId;
		}

		public String getShowCount() {
			return ShowCount;
		}

		public void setShowCount(String showCount) {
			ShowCount = showCount;
		}

		public String getShowCountStr() {
			return ShowCountStr;
		}

		public void setShowCountStr(String showCountStr) {
			ShowCountStr = showCountStr;
		}

		public String getCommentCountStr() {
			return CommentCountStr;
		}

		public void setCommentCountStr(String commentCountStr) {
			CommentCountStr = commentCountStr;
		}

		public String getCommentCount() {
			return CommentCount;
		}

		public void setCommentCount(String commentCount) {
			CommentCount = commentCount;
		}

		public String getAverageScore() {
			return AverageScore;
		}

		public void setAverageScore(String averageScore) {
			AverageScore = averageScore;
		}

		public String getGoodCountStr() {
			return GoodCountStr;
		}

		public void setGoodCountStr(String goodCountStr) {
			GoodCountStr = goodCountStr;
		}

		public String getGoodCount() {
			return GoodCount;
		}

		public void setGoodCount(String goodCount) {
			GoodCount = goodCount;
		}

		public String getAfterCount() {
			return AfterCount;
		}

		public void setAfterCount(String afterCount) {
			AfterCount = afterCount;
		}

		public String getOneYear() {
			return OneYear;
		}

		public void setOneYear(String oneYear) {
			OneYear = oneYear;
		}

		public String getAfterCountStr() {
			return AfterCountStr;
		}

		public void setAfterCountStr(String afterCountStr) {
			AfterCountStr = afterCountStr;
		}

		public String getGoodRate() {
			return GoodRate;
		}

		public void setGoodRate(String goodRate) {
			GoodRate = goodRate;
		}

		public String getGoodRateShow() {
			return GoodRateShow;
		}

		public void setGoodRateShow(String goodRateShow) {
			GoodRateShow = goodRateShow;
		}

		public String getGoodRateStyle() {
			return GoodRateStyle;
		}

		public void setGoodRateStyle(String goodRateStyle) {
			GoodRateStyle = goodRateStyle;
		}

		public String getGeneralCountStr() {
			return GeneralCountStr;
		}

		public void setGeneralCountStr(String generalCountStr) {
			GeneralCountStr = generalCountStr;
		}

		public String getGeneralCount() {
			return GeneralCount;
		}

		public void setGeneralCount(String generalCount) {
			GeneralCount = generalCount;
		}

		public String getGeneralRate() {
			return GeneralRate;
		}

		public void setGeneralRate(String generalRate) {
			GeneralRate = generalRate;
		}

		public String getGeneralRateShow() {
			return GeneralRateShow;
		}

		public void setGeneralRateShow(String generalRateShow) {
			GeneralRateShow = generalRateShow;
		}

		public String getGeneralRateStyle() {
			return GeneralRateStyle;
		}

		public void setGeneralRateStyle(String generalRateStyle) {
			GeneralRateStyle = generalRateStyle;
		}

		public String getPoorCount() {
			return PoorCount;
		}

		public void setPoorCount(String poorCount) {
			PoorCount = poorCount;
		}

		public String getPoorRate() {
			return PoorRate;
		}

		public void setPoorRate(String poorRate) {
			PoorRate = poorRate;
		}

		public String getPoorRateShow() {
			return PoorRateShow;
		}

		public void setPoorRateShow(String poorRateShow) {
			PoorRateShow = poorRateShow;
		}

		public String getPoorRateStyle() {
			return PoorRateStyle;
		}

		public void setPoorRateStyle(String poorRateStyle) {
			PoorRateStyle = poorRateStyle;
		}
	}

	public List<JingDongCommentDTO.CommentsCount> getCommentsCount() {
		return CommentsCount;
	}

	public void setCommentsCount(List<JingDongCommentDTO.CommentsCount> commentsCount) {
		CommentsCount = commentsCount;
	}
}
