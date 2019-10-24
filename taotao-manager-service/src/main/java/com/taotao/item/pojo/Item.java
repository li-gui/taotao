package com.taotao.item.pojo;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import com.taotao.pojo.TbItem;

public class Item extends TbItem{
 
	public Item(TbItem item){
		BeanUtils.copyProperties(item, this);//将原来对象属性有的数据拷贝到item有的属性中
	}
	
	public String[] getImages(){
		//super 关键字是指定 调用父类方法
		if(StringUtils.isNoneBlank(super.getImage())){
			return super.getImage().split(",");
		}
		return null;
	}
}
