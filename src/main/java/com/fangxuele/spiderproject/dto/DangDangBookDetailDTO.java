package com.fangxuele.spiderproject.dto;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wfc on 2017/9/15.
 */
public class DangDangBookDetailDTO implements Serializable{

	private static final long serialVersionUID = 9075202961537544168L;

	private Data data;

	public static class Data{
		private String html;

		private List<NavigationLabel> navigationLabels;

		public String getHtml() {
			return html;
		}

		public void setHtml(String html) {
			this.html = html;
		}

		public List<NavigationLabel> getNavigationLabels() {
			return navigationLabels;
		}

		public void setNavigationLabels(List<NavigationLabel> navigationLabels) {
			this.navigationLabels = navigationLabels;
		}
	}

	public static class NavigationLabel{
		private String index;

		private String ddName;

		private String columnName;

		public String getIndex() {
			return index;
		}

		public void setIndex(String index) {
			this.index = index;
		}

		public String getDdName() {
			return ddName;
		}

		public void setDdName(String ddName) {
			this.ddName = ddName;
		}

		public String getColumnName() {
			return columnName;
		}

		public void setColumnName(String columnName) {
			this.columnName = columnName;
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
