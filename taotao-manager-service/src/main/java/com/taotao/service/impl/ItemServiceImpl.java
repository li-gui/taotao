package com.taotao.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.activemq.command.ActiveMQDestination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.util.IDUtils;
import com.taotao.mapper.TbItemCatMapper;
import com.taotao.mapper.TbItemDescMapper;
import com.taotao.mapper.TbItemMapper;
import com.taotao.mapper.TbItemParamItemMapper;
import com.taotao.mapper.TbItemParamMapper;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemCat;
import com.taotao.pojo.TbItemCatExample;
import com.taotao.pojo.TbItemDesc;
import com.taotao.pojo.TbItemDescExample;
import com.taotao.pojo.TbItemExample;
import com.taotao.pojo.TbItemExample.Criteria;
import com.taotao.pojo.TbItemParam;
import com.taotao.pojo.TbItemParamExample;
import com.taotao.service.ItemService;

@Service
public class ItemServiceImpl implements ItemService {
@Autowired 
private TbItemParamMapper param;
@Autowired 
private TbItemCatMapper ItemCat;
@Autowired
private TbItemMapper mapper;
@Autowired
private TbItemDescMapper descmapper;
@Autowired 
private JmsTemplate jmstemplate; 	
@Autowired
private Destination destination;

	@Override
	public EasyUIDataGridResult getItemList(Integer page, Integer rows) {
		//1.设置分页的信息 使用pagehelper
		if(page==null)page=1;
		if(rows==null)rows=30;
		PageHelper.startPage(page, rows);
		//2.注入mapper
		//3.创建example 对象 不需要设置查询条件
		
		TbItemExample example = new TbItemExample();
		//4.根据mapper调用查询所有数据的方法
		List<TbItem> list = mapper.selectByExample(example);
		
		//5.获取分页的信息
		PageInfo<TbItem> info = new PageInfo<>(list);
		//6.封装到EasyUIDataGridResult
		EasyUIDataGridResult result = new EasyUIDataGridResult();
		result.setTotal((int)info.getTotal());
		 result.setRows(info.getList());
		//7.返回
		return result;
	}
	
	@Override
	public EasyUIDataGridResult getItemParamList(Integer page, Integer rows) {
		//1.设置分页的信息 使用pagehelper
		if(page==null)page=1;
		if(rows==null)rows=30;
		PageHelper.startPage(page, rows);
		
		TbItemParamExample example = new TbItemParamExample();
//		需检索的字段中包含大字段类型时，必须用selectByExampleWithBLOBs，不检索大字段时，用selectByExample就足够了
         List<TbItemParam> list = param.selectByExampleWithBLOBs(example);
		
		for(TbItemParam a:list){
			Long cid=a.getItemCatId();
			String catname=ItemCat.selectByPrimaryKey(cid).getName();
			a.setItemCatName(catname);
		}
		
		//5.获取分页的信息
		PageInfo<TbItemParam> info = new PageInfo<>(list);
		//6.封装到EasyUIDataGridResult
		EasyUIDataGridResult result = new EasyUIDataGridResult();
		result.setTotal((int)info.getTotal());
		 result.setRows(info.getList());
		//7.返回
		return result;
	}
	
	
	
	@Override
	public TaotaoResult saveItem(TbItem item, String desc) {
		//生成商品的id
		long itemId = IDUtils.genItemId();
		//1.补全item 的其他属性
		item.setId(itemId);
		item.setCreated(new Date());
		//1-正常，2-下架，3-删除',
		item.setStatus((byte) 1);
		item.setUpdated(item.getCreated());
		//2.插入到item表 商品的基本信息表
		mapper.insertSelective(item);
		//3.补全商品描述中的属性
		TbItemDesc desc2 = new TbItemDesc();
		desc2.setItemDesc(desc);
		desc2.setItemId(itemId);
		desc2.setCreated(item.getCreated());
		desc2.setUpdated(item.getCreated());
		//4.插入商品描述数据
			//注入tbitemdesc的mapper
		descmapper.insertSelective(desc2);
		
		//ActiveMQ发送消息
		ActiveMQDestination activeMQDestination=(ActiveMQDestination) destination;
		jmstemplate.send(activeMQDestination.getCompositeDestinations()[1], new MessageCreator() {
			@Override
			public Message createMessage(Session session) throws JMSException {
				return session.createTextMessage(itemId+"");
			}
		});
		//5.返回taotaoresult
		return TaotaoResult.ok();
	}
	
	@Override
	public TaotaoResult updateItem(TbItem item, String desc) {
		//updateByPrimaryKeySelective()如果传入obj对象中的某个属性值为null，则不进行数据库对应字段的更新。
		mapper.updateByPrimaryKeySelective(item);
		Long itemId=item.getId();
		//3.补全商品描述中的属性
		TbItemDesc desc2 = new TbItemDesc();
		desc2.setItemDesc(desc);
		desc2.setItemId(itemId);
		//4.插入商品描述数据
//			//注入tbitemdesc的mapper
		descmapper.updateByPrimaryKeySelective(desc2);
		
		//ActiveMQ发送消息
		/**
         * 将destination强制转换为ActiveMQDestination，在ActiveMQDestination对象中，
         *    通过getCompositeDestinations()方法获取destination队列数组：queue://queue1  queue://queue2
         *  
         */
		 ActiveMQDestination activeMQDestination=(ActiveMQDestination) destination;
				jmstemplate.send(activeMQDestination.getCompositeDestinations()[1], new MessageCreator() {
					@Override
					public Message createMessage(Session session) throws JMSException {
						return session.createTextMessage(itemId+"");
					}
				});
		//5.返回taotaoresult
		return TaotaoResult.ok();
	}
	
	
	/**
	 * 根据id，更新商品状态1-正常，2-下架，3-删除
	 */
	@Override
	public TaotaoResult updateItemStatus(List<Long> ids, String method) {
		TbItem item = new TbItem();
		if (method.equals("reshelf")) {
			// 正常，更新status=1即可
			item.setStatus((byte) 1);
			for(Long id:ids){			
				//ActiveMQ发送消息
				ActiveMQDestination activeMQDestination=(ActiveMQDestination) destination;
				System.out.println("地址："+activeMQDestination.getCompositeDestinations()[1]);//顺序反的
				jmstemplate.send(activeMQDestination.getCompositeDestinations()[1], new MessageCreator() {
					@Override
					public Message createMessage(Session session) throws JMSException {
						return session.createTextMessage(id+"");
					}
				});
				}
		} else if (method.equals("instock")) {
			// 下架，更新status=2即可
			item.setStatus((byte) 2);
			for(Long id:ids){			
			//ActiveMQ发送消息
				ActiveMQDestination activeMQDestination=(ActiveMQDestination) destination;
			jmstemplate.send(activeMQDestination.getCompositeDestinations()[0], new MessageCreator() {
				@Override
				public Message createMessage(Session session) throws JMSException {
					return session.createTextMessage(id+"");
				}
			});
			}
		} else if (method.equals("delete")) {
			// 删除，更新status=3即可
			item.setStatus((byte) 3);
			for(Long id:ids){			
				//ActiveMQ发送消息
				ActiveMQDestination activeMQDestination=(ActiveMQDestination) destination;
				System.out.println("地址："+activeMQDestination.getCompositeDestinations()[0]);//顺序反的
				jmstemplate.send(activeMQDestination.getCompositeDestinations()[0], new MessageCreator() {
					@Override
					public Message createMessage(Session session) throws JMSException {
						return session.createTextMessage(id+"");
					}
				});
				}
		}
		
		for (Long id : ids) {
			// 创建查询条件，根据id更新
			TbItemExample tbItemExample = new TbItemExample();
			Criteria criteria = tbItemExample.createCriteria();
			criteria.andIdEqualTo(id);
			// 第一个参数 是要修改的部分值组成的对象，其中有些属性为null则表示该项不修改。
			// 第二个参数 是一个对应的查询条件的类， 通过这个类可以实现 order by 和一部分的where 条件。
			mapper.updateByExampleSelective(item, tbItemExample);
		}
		return TaotaoResult.ok();
	}

	@Override
	public TbItem getItemId(Long itemId) {
	
	TbItem tbitem=mapper.selectByPrimaryKey(itemId);
	//孙节点itemCat3，父节点itemCat2,ye节点itemCat1,最好写成递归方式
	TbItemCat itemCat3=ItemCat.selectByPrimaryKey(tbitem.getCid());
	TbItemCat itemCat2 = ItemCat.selectByPrimaryKey(itemCat3.getParentId());
	TbItemCat itemCat1 = ItemCat.selectByPrimaryKey(itemCat2.getParentId());
	
    tbitem.setTbItemCatname3(itemCat3.getName());
    tbitem.setTbItemCatname2(itemCat2.getName());
    tbitem.setTbItemCatname1(itemCat1.getName());
		return tbitem;
	}

	@Override
	public TbItemDesc getItemDescById(Long itemId) {
		
		return descmapper.selectByPrimaryKey(itemId);
	}

	@Override
	public List<TbItem> getAllItem() {
		TbItemExample tbItemExample = new TbItemExample();
		List<TbItem> Tbitem=mapper.selectByExample(tbItemExample);
		for(TbItem tbitem :Tbitem){
			//孙节点itemCat3，父节点itemCat2,ye节点itemCat1,最好写成递归方式
			TbItemCat itemCat3=ItemCat.selectByPrimaryKey(tbitem.getCid());
			TbItemCat itemCat2 = ItemCat.selectByPrimaryKey(itemCat3.getParentId());
			TbItemCat itemCat1 = ItemCat.selectByPrimaryKey(itemCat2.getParentId());
			
		    tbitem.setTbItemCatname3(itemCat3.getName());
		    tbitem.setTbItemCatname2(itemCat2.getName());
		    tbitem.setTbItemCatname1(itemCat1.getName());
		    
			TbItemDesc allTbItemDesc = descmapper.selectByPrimaryKey(tbitem.getId());
				tbitem.setItemDesc(allTbItemDesc);
		}
		
		return Tbitem;
	}

}
