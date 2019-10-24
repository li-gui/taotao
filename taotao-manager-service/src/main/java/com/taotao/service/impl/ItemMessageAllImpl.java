package com.taotao.service.impl;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.item.pojo.Item;
import com.taotao.pojo.TbItem;
import com.taotao.service.ItemMessageAll;
import com.taotao.service.ItemService;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;


@Service
public class ItemMessageAllImpl implements ItemMessageAll{
	 @Autowired 
	 private ItemService itemService;
	 
	 @Autowired
	 private FreeMarkerConfigurer config;
	 
	public TaotaoResult genHtmlFreemarker() {	
		// 获取freemarker的核心对象，使用spring整合的对象获取
		Configuration configuration = config.getConfiguration();
		// 使用核心对象，获取模板
				try {
					int i=0;
					Template template = configuration.getTemplate("item.ftl");
					//获取数据
					List<TbItem> allItem = itemService.getAllItem();
					for(TbItem tbitem :allItem){
						Item item =new Item(tbitem);
						Map model = new HashMap<>();
						 model.put("item", item);
						 //输出
						 Writer writer = new FileWriter(new File("F:/freemarker/item/"+item.getId()+".html"));
//				          调用模板对象的process方法输出文件。
						template.process(model, writer);
//						关闭流。
						i++;
						writer.close();
					}								
					System.out.println("导入静态页面："+i+"个");
					 
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (TemplateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return TaotaoResult.ok();

	}
	}

