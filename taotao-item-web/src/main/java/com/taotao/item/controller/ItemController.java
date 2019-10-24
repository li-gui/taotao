package com.taotao.item.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.item.pojo.Item;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;
import com.taotao.service.ItemService;

@Controller
public class ItemController {
	
 @Autowired 
 private ItemService itemService;
 

 
	@RequestMapping("/item/{Itemid}")
public String getItem(@PathVariable Long Itemid ,Model model){
		TbItem Tbitem = itemService.getItemId(Itemid);
		TbItemDesc itemDesc = itemService.getItemDescById(Itemid);
		//将Tbitem转成item
		  Item item =new Item(Tbitem);
		  model.addAttribute("item", item);
		  model.addAttribute("itemDesc", itemDesc);
	return "item";
}
}

