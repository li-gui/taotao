package com.taotao.search.listener;

import java.io.IOException;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.activemq.command.ActiveMQDestination;
import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.beans.factory.annotation.Autowired;

import com.taotao.search.dao.SearchDao;

public class ItemChangeMessageListener implements MessageListener{
@Autowired
private SearchDao searchdao;
	@Override
	public void onMessage(Message message) {
		
			ActiveMQDestination queues = null;
			try {
				queues = (ActiveMQDestination)message.getJMSDestination();
				System.out.println("Topic地址："+queues.getPhysicalName());
			} catch (JMSException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		
		 if(queues.getPhysicalName().equalsIgnoreCase("item-change-topic"))
         {
		//判断消息类型是不是TextMessage
		if(message instanceof TextMessage){
			
			TextMessage textMessage = (TextMessage)message;
			Long itemId;
			try {
				//通过id查询数据库
				itemId=Long.parseLong(textMessage.getText());
				try {
					searchdao.updateSearchItmeById(itemId);
					
				} catch (SolrServerException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			} catch (JMSException e) {
				e.printStackTrace();
			}
		}
         }
	}
	}


