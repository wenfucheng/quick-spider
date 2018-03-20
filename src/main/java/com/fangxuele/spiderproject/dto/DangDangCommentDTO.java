package com.fangxuele.spiderproject.dto;

/**
 * 当当图书评价信息json解析DTO
 * Created by wfc on 2017/8/25.
 */
public class DangDangCommentDTO {

	private Data data;

	public static class Data{
		private Summary summary;

		private String html;

		public Summary getSummary() {
			return summary;
		}

		public void setSummary(Summary summary) {
			this.summary = summary;
		}

		public String getHtml() {
			return html;
		}

		public void setHtml(String html) {
			this.html = html;
		}
	}

	public static class Summary{
		private String main_product_id;
		private Integer total_comment_num;
		private Integer total_score_count;
		private Integer total_crazy_count;
		private Integer total_indifferent_count;
		private Integer total_detest_count;
		private Integer total_auto_count;
		private Integer total_image_count;
		private String average_score ;
		private String average_score_eliminate_default ;
		private String favorable_rate;
		private Integer pageCount;
		private String goodRate;
		private Integer pageIndex;
		private Integer autoCount;

		public String getMain_product_id() {
			return main_product_id;
		}

		public void setMain_product_id(String main_product_id) {
			this.main_product_id = main_product_id;
		}

		public Integer getTotal_comment_num() {
			return total_comment_num;
		}

		public void setTotal_comment_num(Integer total_comment_num) {
			this.total_comment_num = total_comment_num;
		}

		public Integer getTotal_score_count() {
			return total_score_count;
		}

		public void setTotal_score_count(Integer total_score_count) {
			this.total_score_count = total_score_count;
		}

		public Integer getTotal_crazy_count() {
			return total_crazy_count;
		}

		public void setTotal_crazy_count(Integer total_crazy_count) {
			this.total_crazy_count = total_crazy_count;
		}

		public Integer getTotal_indifferent_count() {
			return total_indifferent_count;
		}

		public void setTotal_indifferent_count(Integer total_indifferent_count) {
			this.total_indifferent_count = total_indifferent_count;
		}

		public Integer getTotal_detest_count() {
			return total_detest_count;
		}

		public void setTotal_detest_count(Integer total_detest_count) {
			this.total_detest_count = total_detest_count;
		}

		public Integer getTotal_auto_count() {
			return total_auto_count;
		}

		public void setTotal_auto_count(Integer total_auto_count) {
			this.total_auto_count = total_auto_count;
		}

		public Integer getTotal_image_count() {
			return total_image_count;
		}

		public void setTotal_image_count(Integer total_image_count) {
			this.total_image_count = total_image_count;
		}

		public String getAverage_score() {
			return average_score;
		}

		public void setAverage_score(String average_score) {
			this.average_score = average_score;
		}

		public String getAverage_score_eliminate_default() {
			return average_score_eliminate_default;
		}

		public void setAverage_score_eliminate_default(String average_score_eliminate_default) {
			this.average_score_eliminate_default = average_score_eliminate_default;
		}

		public String getFavorable_rate() {
			return favorable_rate;
		}

		public void setFavorable_rate(String favorable_rate) {
			this.favorable_rate = favorable_rate;
		}

		public Integer getPageCount() {
			return pageCount;
		}

		public void setPageCount(Integer pageCount) {
			this.pageCount = pageCount;
		}

		public String getGoodRate() {
			return goodRate;
		}

		public void setGoodRate(String goodRate) {
			this.goodRate = goodRate;
		}

		public Integer getPageIndex() {
			return pageIndex;
		}

		public void setPageIndex(Integer pageIndex) {
			this.pageIndex = pageIndex;
		}

		public Integer getAutoCount() {
			return autoCount;
		}

		public void setAutoCount(Integer autoCount) {
			this.autoCount = autoCount;
		}
	}

	private String elapse;

	private String errMsg;

	private String location;

	public Data getData() {
		return data;
	}

	public void setData(Data data) {
		this.data = data;
	}

	public String getElapse() {
		return elapse;
	}

	public void setElapse(String elapse) {
		this.elapse = elapse;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}
}
