package com.taotao.content.service.impl;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taotao.common.pojo.EasyUITreeNode;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.content.service.ContentCategoryService;
import com.taotao.mapper.TbContentCategoryMapper;
import com.taotao.pojo.TbContentCategory;
import com.taotao.pojo.TbContentCategoryExample;
import com.taotao.pojo.TbContentCategoryExample.Criteria;
@Service
public class ContentCategoryServiceImpl implements ContentCategoryService {
	@Autowired
		private TbContentCategoryMapper mapper;
		@Override
		/*
		 * 显示所以节点
		 */
		public List<EasyUITreeNode> getContentCategoryList(Long parentId) {
			//1.注入mapper
			//2.创建example
			TbContentCategoryExample example = new TbContentCategoryExample();
			//3.设置条件
			Criteria criteria = example.createCriteria();
			criteria.andParentIdEqualTo(parentId);//select * from tbcontentcategory where parent_id=parentId
			//4.执行查询
			List<TbContentCategory> list = mapper.selectByExample(example);
			//5.转成EasyUITreeNode 列表
			//
			List<EasyUITreeNode> nodes = new ArrayList<>();
			for (TbContentCategory tbContentCategory : list) {
				EasyUITreeNode node = new EasyUITreeNode();
				node.setId(tbContentCategory.getId());
				node.setState(tbContentCategory.getIsParent()?"closed":"open");
				node.setText(tbContentCategory.getName());//分类名称
		
				nodes.add(node);
			}
			//6.返回
			return nodes;
}
		/*
		 * 添加
		 * 
		 */
		@Override
		public TaotaoResult createContentCategory(Long parentId, String name) {
			//1.构建对象  补全其他的属性
			TbContentCategory category = new TbContentCategory();
			category.setCreated(new Date());
			category.setIsParent(false);//新增的节点都是叶子节点
			category.setName(name);
			category.setParentId(parentId);
			category.setSortOrder(1);
			category.setStatus(1);
			category.setUpdated(category.getCreated());
			//2.插入contentcategory表数据
			mapper.insertSelective(category);
			//3.返回taotaoresult 包含内容分类的id   需要主键返回
			
			//判断如果要添加的节点的父节点本身叶子节点  需要更新其为父节点
			TbContentCategory parent = mapper.selectByPrimaryKey(parentId);
			if(parent.getIsParent()==false){//原本就是叶子节点
				parent.setIsParent(true);
				mapper.updateByPrimaryKeySelective(parent);//更新节点的is_parent属性为true
			}
			
			return TaotaoResult.ok(category);
		}
		 /**
	     * 重命名分类
	     */
	    @Override
	    public void upadateContentCategory(long id, String name) {
	        // 创建一个tb_content_category表对应的pojo对象
	        TbContentCategory node = mapper.selectByPrimaryKey(id);
	        // 更新名字
	        node.setName(name);
	        mapper.updateByPrimaryKey(node);
	    }

	    
	    /**
	     * 删除分类
	     */
	    @Override
	    public TaotaoResult deleteContentCategory(long id) {
	        TbContentCategory contentCategory = mapper.selectByPrimaryKey(id);
	        if (contentCategory.getIsParent()) {
	            List<EasyUITreeNode> list = getContentCategoryList(id);
	            // 递归删除
	            for (EasyUITreeNode tbcontentCategory : list) {
	                deleteContentCategory(tbcontentCategory.getId());
	            }
	        }
	            if (getContentCategoryList(contentCategory.getParentId()).size() == 1) {
	                TbContentCategory parentCategory = mapper.selectByPrimaryKey(contentCategory.getParentId());
	                parentCategory.setIsParent(false);
	                mapper.updateByPrimaryKey(parentCategory);
	            }
	            mapper.deleteByPrimaryKey(id);
//返回值是TaotaoResult.ok()，前台的Ajax会与后台的数据时时刷新同步，如果要传递数据到前台，TaotaoResult.ok(实体类（json）)
	            
	            return TaotaoResult.ok();

	    }}
