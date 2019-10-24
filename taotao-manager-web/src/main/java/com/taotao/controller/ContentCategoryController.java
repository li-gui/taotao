package com.taotao.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.pojo.EasyUITreeNode;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.content.service.ContentCategoryService;

/**
 * 内容分类的处理controller
 */
@Controller
public class ContentCategoryController {
	@Autowired
	private ContentCategoryService service;
	/**
	 * url : '/content/category/list',
		animate: true,
		method : "GET",
		参数: id
	 */
	@RequestMapping(value="/content/category/list",method=RequestMethod.GET)
	@ResponseBody
	public List<EasyUITreeNode> getContentCategoryList(@RequestParam(value="id",defaultValue="0") Long parentId){
		//1.引入服务
		//2.注入服务
		//3调用
		return service.getContentCategoryList(parentId);
	}
	

	///content/category/create
	//method=post
	//参数：
	//parentId：就是新增节点的父节点的Id
	//name：新增节点的文本
	//返回值taotaoresult 包含分类的id
	/**
	 * 添加节点
	 */
	@RequestMapping(value="/content/category/create",method=RequestMethod.POST)
	@ResponseBody
	public TaotaoResult createContentCategory(Long parentId, String name){
		return service.createContentCategory(parentId, name);
	}
	/*
	 * 
	 * 重命名
	 */
	
	 @RequestMapping(value="/content/category/update",method=RequestMethod.POST)
	    @ResponseBody
	    public void update(Long id, String name) {
		service.upadateContentCategory(id, name);
	    }
	 /*
	  * 
	  * 删除
	  */
	 @RequestMapping(value="content/category/delete/",method=RequestMethod.POST)
	    @ResponseBody
	    public TaotaoResult update(Long id) {
		 TaotaoResult result = service.deleteContentCategory(id);
	        return result;
	    }
}
