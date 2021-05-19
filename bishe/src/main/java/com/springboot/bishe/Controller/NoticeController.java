package com.springboot.bishe.Controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.springboot.bishe.common.DataGridView;
import com.springboot.bishe.domain.Notice;
import com.springboot.bishe.domain.User;
import com.springboot.bishe.service.NoticeService;
import com.springboot.bishe.vo.NoticeVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

/**
 *  系统公告控制器
 */
@RestController
@RequestMapping("/notice")
public class NoticeController {
	
	@Autowired
	private NoticeService noticeService;
	
	
	/**
	 * 查询
	 */
	@RequestMapping("loadAllNotice")
	public DataGridView loadAllNotice(NoticeVo noticeVo) {
		IPage<Notice> page=new Page<>(noticeVo.getPage(), noticeVo.getLimit());
		QueryWrapper<Notice> queryWrapper=new QueryWrapper<>();
		queryWrapper.like(StringUtils.isNotBlank(noticeVo.getTitle()), "title", noticeVo.getTitle());
		queryWrapper.like(StringUtils.isNotBlank(noticeVo.getUsername()), "username", noticeVo.getUsername());
		queryWrapper.ge(noticeVo.getStartTime()!=null, "createtime", noticeVo.getStartTime());
		queryWrapper.le(noticeVo.getEndTime()!=null, "createtime", noticeVo.getEndTime());
		queryWrapper.orderByDesc("createtime");
		this.noticeService.page(page, queryWrapper);
		return new DataGridView(page.getTotal(), page.getRecords());
	}
	
	
	/**
	 * 添加
	 */
	@RequestMapping("addNotice")
	public DataGridView addNotice(NoticeVo noticeVo, HttpSession session) {
		try {
			noticeVo.setCreatetime(new Date());
			User user = (User) session.getAttribute("user");
			noticeVo.setUsername(user.getUsername());
			this.noticeService.save(noticeVo);
			return DataGridView.success("添加成功");
		} catch (Exception e) {
			e.printStackTrace();
			return DataGridView.fail("添加失败");
		}
	}
	/**
	 * 修改
	 */
	@RequestMapping("updateNotice")
	public DataGridView updateNotice(NoticeVo noticeVo) {
		try {
			this.noticeService.updateById(noticeVo);
			return DataGridView.success("修改成功");
		} catch (Exception e) {
			e.printStackTrace();
			return DataGridView.fail("修改失败");
		}
	}
	
	/**
	 * 删除
	 */
	@RequestMapping("deleteNotice")
	public DataGridView deleteNotice(Integer id) {
		try {
			this.noticeService.removeById(id);
			return DataGridView.success("删除成功");
		} catch (Exception e) {
			e.printStackTrace();
			return DataGridView.fail("删除失败");
		}
	}
	
	
	/**
	 * 批量删除
	 */
	@RequestMapping("batchDeleteNotice")
	public DataGridView batchDeleteNotice(NoticeVo noticeVo) {
		try {
			Collection<Serializable> idList=new ArrayList<Serializable>();
			for (Integer id : noticeVo.getIds()) {
				idList.add(id);
			}
			this.noticeService.removeByIds(idList);
			return DataGridView.success("删除成功");
		} catch (Exception e) {
			e.printStackTrace();
			return DataGridView.fail("删除失败");
		}
	}
}

