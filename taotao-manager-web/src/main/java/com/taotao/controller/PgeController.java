package com.taotao.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 显示页面
 */
@Controller
public class PgeController {
	@RequestMapping("/")
	public String showIndex(){
		return "index";
	}
	//显示商品的查询的页面
	//  url:/item-list
	@RequestMapping("/{page}")
	public String showPage(@PathVariable String page){
		return page;
	}
}
