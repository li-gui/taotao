package com.taotao.listener;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.activemq.command.ActiveMQDestination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import com.taotao.item.pojo.Item;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;
import com.taotao.service.ItemService;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;



public class ItemMessageListener implements MessageListener {

	 @Autowired 
	 private ItemService itemService;
	 
	 @Autowired
	 private FreeMarkerConfigurer config;
	@Override
	public void onMessage(Message message) {
		ActiveMQDestination queues = null;
		try {
			queues = (ActiveMQDestination)message.getJMSDestination();
			System.out.println("Freemarker:Topic地址："+queues.getPhysicalName());
		} catch (JMSException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	
	 if(queues.getPhysicalName().equalsIgnoreCase("item-change-topic"))
     {
		if (message instanceof TextMessage) {
			// 获取消息的内容
			TextMessage textMessage = (TextMessage) message;
			
				Long Itemid;
				try {
					Itemid=Long.parseLong(textMessage.getText());
					TbItem Tbitem = itemService.getItemId(Itemid);
					Item item =new Item(Tbitem);
					TbItemDesc itemDesc = itemService.getItemDescById(Itemid);
					
					//生成静态页面  准备好模版 和 数据集
					genHtmlFreemarker( item, itemDesc);
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JMSException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		}
	}
	private void genHtmlFreemarker(Item item, TbItemDesc itemDesc) {
		// 获取freemarker的核心对象，使用spring整合的对象获取
		Configuration configuration = config.getConfiguration();
		// 使用核心对象，获取模板
				try {
					Template template = configuration.getTemplate("item.ftl");
					 Map model = new HashMap<>();
					 model.put("item", item);
					 model.put("itemdesc", itemDesc);
					 //输出
					 Writer writer = new FileWriter(new File("F:/freemarker/item/"+item.getId()+".html"));
//				          调用模板对象的process方法输出文件。
						template.process(model, writer);
//						关闭流。
						writer.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (TemplateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

	}
	}

