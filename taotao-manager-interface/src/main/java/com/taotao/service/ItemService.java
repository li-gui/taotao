package com.taotao.service;

import java.util.List;

import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;

/**
 * 商品相关的处理的service
 */
public interface ItemService {
	
	/**
	 *  根据当前的页码和每页 的行数进行分页查询
	 */
	public EasyUIDataGridResult getItemList(Integer page,Integer rows);
	/**
	 * 添加商品基本数据和描述数据
	 */
	public TaotaoResult saveItem(TbItem item,String desc);
	public TaotaoResult updateItem(TbItem item, String desc);
	/**
	 * 更新商品状态
	*/
	TaotaoResult updateItemStatus(List<Long> ids,String method);
	/**
	 * 显示商品规格
	*/
	public EasyUIDataGridResult getItemParamList(Integer page, Integer rows);
	/**
	 *  根据商品的id查询商品的数据
	 */
	public TbItem getItemId (Long itemId);
	/**
	 *  根据商品的id查询商品描述
	 */
	public TbItemDesc getItemDescById (Long itemId);
	/**
	 *  根据商品的查询全部商品的数据
	 */
	public List<TbItem> getAllItem ();

}
