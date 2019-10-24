package com.taotao.content.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.util.JsonUtils;
import com.taotao.content.jedis.JedisClient;
import com.taotao.content.service.ContentService;
import com.taotao.mapper.TbContentMapper;
import com.taotao.pojo.TbContent;
import com.taotao.pojo.TbContentExample;
import com.taotao.pojo.TbContentExample.Criteria;


@Service
public class ContentServiceImpl implements ContentService {
	@Autowired
	private JedisClient client;
	@Autowired
	private TbContentMapper mapper;
	@Value("$(CONTENT_KEY)")
	private String CONTENT_KEY;
	@Override
	public TaotaoResult saveContent(TbContent content) {
		//1.注入mapper
		
		//2.补全其他的属性
		content.setCreated(new Date());
		content.setUpdated(content.getCreated());
		//3.插入内容表中
		mapper.insertSelective(content);
		//从redis缓存中删除key
				try {
					client.hdel(CONTENT_KEY, content.getCategoryId()+"");
					System.out.println("save缓存同步启动，删除key-->"+content.getCategoryId());
				} catch (Exception e) {			
					e.printStackTrace();
				}
		return TaotaoResult.ok();
	}

	@Override
	public EasyUIDataGridResult getContentList(long categoryId, int page, int rows) {
		  //根据categoryId查询
        TbContentExample example=new TbContentExample();
        Criteria  criteria=example.createCriteria();
        criteria.andCategoryIdEqualTo(categoryId);
       
        //分页管理
        PageHelper.startPage(page, rows);
        List<TbContent> list=mapper.selectByExample(example);
        //5.获取分页的信息
  		PageInfo<TbContent> info = new PageInfo<>(list);
        EasyUIDataGridResult result = new EasyUIDataGridResult();
     
        result.setRows(info.getList());
        result.setTotal((int)info.getTotal());
        return result;
	}
	@Override
	public TaotaoResult deleteContent(List<Long> ids){
		for (Long id : ids) {
			
			// 创建查询条件，根据id更新
			TbContentExample example=new TbContentExample();
	        Criteria  criteria=example.createCriteria();
			criteria.andIdEqualTo(id);
			// 第一个参数 是要修改的部分值组成的对象，其中有些属性为null则表示该项不修改。
			// 第二个参数 是一个对应的查询条件的类， 通过这个类可以实现 order by 和一部分的where 条件。
			
			//从redis缓存中删除key
			try {
				client.hdel(CONTENT_KEY, mapper.selectByPrimaryKey(id).getCategoryId()+"");
				System.out.println("delete缓存同步启动，删除key-->"+mapper.selectByPrimaryKey(id).getCategoryId());
			} catch (Exception e) {			
				e.printStackTrace();
			}
			mapper.deleteByExample(example);
		}
		return TaotaoResult.ok();
	}
	@Override
	public TaotaoResult updateContent(TbContent content) {
		//updateByPrimaryKeySelective()如果传入obj对象中的某个属性值为null，则不进行数据库对应字段的更新。
		mapper.updateByPrimaryKeySelective(content);
		
		//从redis缓存中删除key
		try {
			client.hdel(CONTENT_KEY, content.getCategoryId()+"");
			System.out.println("update缓存同步启动，删除key-->"+content.getCategoryId());
		} catch (Exception e) {			
			e.printStackTrace();
		}
		//5.返回taotaoresult
		return TaotaoResult.ok();
	}

	
	@Override
	public List<TbContent> getContentListByCatId(Long categoryId) {
				//添加缓存不能影响正常的业务逻辑
		
		//判断 是否redis中有数据  如果有   直接从redis中获取数据 返回
		try {
			String jsonstr = client.hget(CONTENT_KEY, categoryId+"");//从redis数据库中获取内容分类下的所有的内容。
			//如果存在，说明有缓存
			if(StringUtils.isNotBlank(jsonstr)){
			System.out.println("这里有缓存啦！key值为-->"+categoryId+"////value值为-->"+jsonstr.toString());
				return JsonUtils.jsonToList(jsonstr, TbContent.class);
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		
		//2.创建example
		TbContentExample example = new TbContentExample();
		//3.设置查询的条件
		example.createCriteria().andCategoryIdEqualTo(categoryId);//select × from tbcontent where category_id=1
		//4.执行查询
		List<TbContent> list = mapper.selectByExample(example);
		//返回
		
		
		//将数据写入到redis数据库中   
		
		// 注入jedisclient 
		// 调用方法写入redis   key - value
		try {
			System.out.println("没有缓存！！！！！！去数据库操作");
			client.hset(CONTENT_KEY, categoryId+"", JsonUtils.objectToJson(list));
		} catch (Exception e) {
			e.printStackTrace();
		}
				
		return list;
	}
	
}
