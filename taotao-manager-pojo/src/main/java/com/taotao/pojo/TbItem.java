package com.taotao.pojo;

import java.io.Serializable;
import java.util.Date;

public class TbItem implements Serializable{
    private Long id;

    private String title;

    private String sellPoint;

    private Long price;

    private Integer num;

    private String barcode;

    private String image;

    private Long cid;

    private Byte status;

    private Date created;

    private Date updated;
    
    private String tbItemCatname1;
    private String tbItemCatname2;
    private String tbItemCatname3;
    
    private TbItemDesc  ItemDesc;


	public TbItemDesc getItemDesc() {
		return ItemDesc;
	}

	public void setItemDesc(TbItemDesc itemDesc) {
		ItemDesc = itemDesc;
	}

	public String getTbItemCatname1() {
		return tbItemCatname1;
	}

	public void setTbItemCatname1(String tbItemCatname1) {
		this.tbItemCatname1 = tbItemCatname1;
	}

	public String getTbItemCatname2() {
		return tbItemCatname2;
	}

	public void setTbItemCatname2(String tbItemCatname2) {
		this.tbItemCatname2 = tbItemCatname2;
	}

	public String getTbItemCatname3() {
		return tbItemCatname3;
	}

	public void setTbItemCatname3(String tbItemCatname3) {
		this.tbItemCatname3 = tbItemCatname3;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    @Override
	public String toString() {
		return "TbItem [id=" + id + ", title=" + title + ", sellPoint=" + sellPoint + ", price=" + price + ", num="
				+ num + ", barcode=" + barcode + ", image=" + image + ", cid=" + cid + ", status=" + status
				+ ", created=" + created + ", updated=" + updated + "]";
	}

	public String getSellPoint() {
        return sellPoint;
    }

    public void setSellPoint(String sellPoint) {
        this.sellPoint = sellPoint == null ? null : sellPoint.trim();
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode == null ? null : barcode.trim();
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image == null ? null : image.trim();
    }

    public Long getCid() {
        return cid;
    }

    public void setCid(Long cid) {
        this.cid = cid;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }
}