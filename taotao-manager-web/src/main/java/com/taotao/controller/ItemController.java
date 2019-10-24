package com.taotao.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbItem;
import com.taotao.service.ItemService;
import com.taotao.web.util.FastDFSClient;

@Controller
public class ItemController {
	@Value("${TAOTAO_IMAGE_SERVER_URL}")
	private String TAOTAO_IMAGE_SERVER_URL;
	
	
	@Autowired
	private ItemService itemservice;
	//url:/item/list
	//method:get
	//参数：page,rows
	//返回值:json
	@RequestMapping(value="/item/list",method=RequestMethod.GET)
	@ResponseBody//以json的形式
	public EasyUIDataGridResult getItemList(Integer page,Integer rows){
		//1.引入服务
		//2.注入服务
		//3.调用服务的方法
		return itemservice.getItemList(page, rows);
	}
	
	/**
	 * @ResponseBody  
	 *    默认的content-type:application/json;charset=utf-8  google浏览器是支持
	 * 	//使用火狐浏览器 使用kindeditor的时候不支持 content-type:application/json;charset=utf-8 
	 *   //解决：设置content-type:text/plain;charset=utf-8  都支持
	 * @param uploadFile
	 * @return
	 */
	
	//上传图片的方法
	@RequestMapping("/pic/upload")
	@ResponseBody
	public Map<String, Object> uploadImage(MultipartFile uploadFile){
		try {
			// 1.获取元文件的扩展名
			String originalFilename = uploadFile.getOriginalFilename();
			System.out.println("文件的扩展名："+originalFilename);
			String extName = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
			// 2.获取文件的字节数组
			byte[] bytes = uploadFile.getBytes();
			// 3.通过fastdfsclient的方法上传图片
			FastDFSClient client = new FastDFSClient("F:/mars/taotao-manager-web/src/main/resources/resource/fastdfs.conf");
//			extName 文件扩展名，不包含（.）
			String string = client.uploadFile(bytes, extName);
			//拼接成完整的URL
			String path = "http://192.168.25.133/"+string;
			System.out.println("上传-->"+path);
			// 4.成功时，设置map
			Map<String, Object> map = new HashMap<>();
			map.put("error", 0);
			map.put("url", path);
			// 6.返回map
			return map;
		} catch (Exception e) {
			// 5.失败时，设置map
			Map<String, Object> map = new HashMap<>();
			map.put("error", 1);
			map.put("message", "上传失败le");
			return map;
		}
	}
	
	
	//添加商品的方法
	//url:：/item/save
	//参数：tbitem ,desc
	//返回值 json
	//method:post
	
	@RequestMapping(value="/item/save",method=RequestMethod.POST)
	@ResponseBody
	public TaotaoResult saveItem(TbItem item,String desc){
//		//1.引入服务
		//2.注入服务
		//3.调用
		return itemservice.saveItem(item, desc);
	} 
	
	
	
	//编辑商品的方法
	@RequestMapping(value="/rest/item/update",method=RequestMethod.POST)
	@ResponseBody
	public TaotaoResult updateItem(TbItem item,String desc){

		return itemservice.updateItem(item, desc);
	} 
	/**
	 * 更新商品状态 
	 */
	@RequestMapping("/rest/item/{method}")
	@ResponseBody
	public TaotaoResult updateItemStatus(@RequestParam(value="ids")List<Long> ids,@PathVariable String method) {
		TaotaoResult result = itemservice.updateItemStatus(ids,method);
		return result;
	}
	/**
	 * 显示商品规格
	*/
	@RequestMapping(value="/item/param/list",method=RequestMethod.GET)
	@ResponseBody//以json的形式
	public EasyUIDataGridResult getItemParamList(Integer page,Integer rows){
		System.out.println(page+"-----"+rows);
		//1.引入服务
		//2.注入服务
		//3.调用服务的方法
		return itemservice.getItemParamList(page, rows);
	}
}
