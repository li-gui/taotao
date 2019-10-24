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

public class ItemdeleteMessageListener implements MessageListener {

	@Autowired
	private SearchDao searchdao;
		@Override
		public void onMessage(Message message) {
			ActiveMQDestination queues = null;
			try {
				queues = (ActiveMQDestination)message.getJMSDestination();
			} catch (JMSException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		
		 if(queues.getPhysicalName().equalsIgnoreCase("item-delete-topic"))
         {
			//判断消息类型是不是TextMessage
			if(message instanceof TextMessage){
				
				TextMessage textMessage = (TextMessage)message;
				String itemId;
				
					//通过id查询数据库
					try {
						itemId=textMessage.getText();
						searchdao.deleteSearchItmeById(itemId);
					} catch (JMSException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					
			}
         }
		}
		}
