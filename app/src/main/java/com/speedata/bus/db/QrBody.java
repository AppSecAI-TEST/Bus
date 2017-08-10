package com.speedata.bus.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

/**
 * ----------Dragon be here!----------/
 * 　　　┏┓　　　┏┓
 * 　　┏┛┻━━━┛┻┓
 * 　　┃　　　　　　　┃
 * 　　┃　　　━　　　┃
 * 　　┃　┳┛　┗┳　┃
 * 　　┃　　　　　　　┃
 * 　　┃　　　┻　　　┃
 * 　　┃　　　　　　　┃
 * 　　┗━┓　　　┏━┛
 * 　　　　┃　　　┃神兽保佑
 * 　　　　┃　　　┃代码无BUG！
 * 　　　　┃　　　┗━━━┓
 * 　　　　┃　　　　　　　┣┓
 * 　　　　┃　　　　　　　┏┛
 * 　　　　┗┓┓┏━┳┓┏┛
 * 　　　　　┃┫┫　┃┫┫
 * 　　　　　┗┻┛　┗┻┛
 * ━━━━━━神兽出没━━━━━━
 *
 * @author :Reginer in  2017/8/7 6:48.
 *         联系方式:QQ:282921012
 *         功能描述:
 */
@Entity
public class QrBody {
    @Id(autoincrement = true)
    private Long id;
    @Unique
    private String body;
    private String cityId;
    private boolean isUploading;
    @Generated(hash = 1841636613)
    public QrBody(Long id, String body, String cityId, boolean isUploading) {
        this.id = id;
        this.body = body;
        this.cityId = cityId;
        this.isUploading = isUploading;
    }
    public QrBody( String body, String cityId, boolean isUploading) {
        this.body = body;
        this.cityId = cityId;
        this.isUploading = isUploading;
    }
    @Generated(hash = 418605835)
    public QrBody() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getBody() {
        return this.body;
    }
    public void setBody(String body) {
        this.body = body;
    }
    public String getCityId() {
        return this.cityId;
    }
    public void setCityId(String cityId) {
        this.cityId = cityId;
    }
    public boolean getIsUploading() {
        return this.isUploading;
    }
    public void setIsUploading(boolean isUploading) {
        this.isUploading = isUploading;
    }
}
