package com.taotao.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
/**
 * 处理内容表相关的
 * @title ContentController.java
 * <p>description</p>
 * <p>company: www.itheima.com</p>
 * @author ljh 
 * @version 1.0
 */
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.content.service.ContentService;
import com.taotao.pojo.TbContent;
import com.taotao.pojo.TbItem;
@Controller
public class ContentController {
	@Autowired
	private ContentService contentservcie;
	
	//	$.post("/content/save",$("#contentAddForm").serialize(), function(data){
	//返回值是JSON
	@RequestMapping(value="/content/save",method=RequestMethod.POST)
	@ResponseBody
	public TaotaoResult saveContent(TbContent tContent){
		return contentservcie.saveContent(tContent);
	}
	
	
	/**
	 *  内容管理列表查询*****
	 */
	@RequestMapping("/content/query/list")
	@ResponseBody
	    public  EasyUIDataGridResult getContentList(long categoryId, int page, int rows) {
		EasyUIDataGridResult content=contentservcie.getContentList(categoryId,page,rows);
	    return content;
	}
	/**
	 *  内容管理列表删除*****
	 */
	 @RequestMapping(value="/content/delete",method=RequestMethod.POST)
	@ResponseBody
	    public  TaotaoResult DeleteContent(@RequestParam(value="ids")List<Long> ids) {
		
		TaotaoResult content=contentservcie.deleteContent(ids);
	    return content;
	}
	/**
	 * 编辑内容管理
	 */
		@RequestMapping(value="/rest/content/edit",method=RequestMethod.POST)
		@ResponseBody
		public TaotaoResult updateItem(TbContent content){
			return contentservcie.updateContent(content);
		} 
}
