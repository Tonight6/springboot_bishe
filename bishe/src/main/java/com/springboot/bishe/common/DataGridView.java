package com.springboot.bishe.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 * json数据实体
 * @author LJH
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataGridView {

	// 状态码 -1-成功 200-失败  -2为验证码错误
	private Integer code=0;

	// 提示信息
	private String msg="";

	// 记录返回数据总数
	private Long count=0L;

	// 用户要返回给浏览器的普通数据
	private Object data;

	// 用户要返回给浏览器的json数据
	private Map<String, Object> map = new HashMap<String, Object>();

	public DataGridView(Long count, Object data) {
		super();
		this.count = count;
		this.data = data;
	}
	public DataGridView(Object data) {
		super();
		this.data = data;
	}




	/**
	 * 处理成功
	 *
	 * @return
	 */
	public static DataGridView success() {
		DataGridView result = new DataGridView();
		result.setCode(Constast.OK);
		return result;
	}

	/**
	 * 处理成功
	 *
	 * @return
	 */
	public static DataGridView success(String msg) {
		DataGridView result = new DataGridView();
		result.setCode(Constast.OK);
		result.setMsg(msg);
		return result;
	}



	/**
	 * 添加要返回的json数据
	 *
	 * @param key
	 * @param value
	 * @return
	 */
	public DataGridView add(String key, Object value) {
		this.getMap().put(key, value);
		return this;
	}

	/**
	 * 处理失败
	 *
	 * @return
	 */
	public static DataGridView fail() {
		DataGridView result = new DataGridView();
		result.setCode(Constast.ERROR);
		result.setMsg("处理失败！");
		return result;
	}



	/**
	 * 处理失败
	 *
	 * @return
	 */
	public static DataGridView fail(String msg) {
		DataGridView result = new DataGridView();
		result.setCode(Constast.ERROR);
		result.setMsg(msg);
		return result;
	}
}
