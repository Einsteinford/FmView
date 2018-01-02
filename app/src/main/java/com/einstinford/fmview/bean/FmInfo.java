package com.einstinford.fmview.bean;

import java.io.Serializable;

/**
 * 电台详情
 *
 * @author Administrator
 */
public class FmInfo implements Serializable {
    private static final long serialVersionUID = 4214231599815564622L;
    public String id = "";//每期电台ID,
    public String title = "";//"电台名称",
    public String radioid = "";//电台ID,
    public String mhz;//赫兹名称
    public String radio_date = "";//节目日期
    public String large_cover = "";//"当前播放节目大图",
    public String small_cover = "";//"当前播放节目小图",
    public String thumb_cover = "";//"当前播放节目缩略图",
    public String areaname = "";//"国家台",
    public String dailytitle = "";//"每期电台名称（客户端显示）",
    public String shortdesc = "";//"短简介",
    public String longdesc = "";//"长简介",
    public int now_listenCount;//收听量,
    public int share_count = 0;//分享量,
    public int fav_count = 0;//收藏量,
    public String reward_fee = "";//"打赏金额",
    public String linkurl = "";//"链接",
    public int radio_count = 0;//电台数量,
    public String now_programtitle = "";//"当前播放节目名称",
    public String now_programlinkurl = "";//"当前播放节目链接",
    public String now_programdailyid = "";//当前播放节目ID,
    public String now_programid = "";//节目ID,
    public String now_listencount = "";//当前播放节目收听量,
    public String now_begintime = "";//"当前播放节目开始时间",
    public String now_endtime = "";//"当前播放节目结束时间"
    public String now_livelinkurl;
    public String begintime;
    public String endtime;

    public String adclassid_1; //中广告位
    public String adclassid_2; //中广告位

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRadioid() {
        return radioid;
    }

    public void setRadioid(String radioid) {
        this.radioid = radioid;
    }

    public String getMhz() {
        return mhz;
    }

    public void setMhz(String mhz) {
        this.mhz = mhz;
    }

    public String getRadio_date() {
        return radio_date;
    }

    public void setRadio_date(String radio_date) {
        this.radio_date = radio_date;
    }

    public String getLarge_cover() {
        return large_cover;
    }

    public void setLarge_cover(String large_cover) {
        this.large_cover = large_cover;
    }

    public String getSmall_cover() {
        return small_cover;
    }

    public void setSmall_cover(String small_cover) {
        this.small_cover = small_cover;
    }

    public String getThumb_cover() {
        return thumb_cover;
    }

    public void setThumb_cover(String thumb_cover) {
        this.thumb_cover = thumb_cover;
    }

    public String getAreaname() {
        return areaname;
    }

    public void setAreaname(String areaname) {
        this.areaname = areaname;
    }

    public String getDailytitle() {
        return dailytitle;
    }

    public void setDailytitle(String dailytitle) {
        this.dailytitle = dailytitle;
    }

    public String getShortdesc() {
        return shortdesc;
    }

    public void setShortdesc(String shortdesc) {
        this.shortdesc = shortdesc;
    }

    public String getLongdesc() {
        return longdesc;
    }

    public void setLongdesc(String longdesc) {
        this.longdesc = longdesc;
    }

    public int getNow_listenCount() {
        return now_listenCount;
    }

    public void setNow_listenCount(int now_listenCount) {
        this.now_listenCount = now_listenCount;
    }

    public int getShare_count() {
        return share_count;
    }

    public void setShare_count(int share_count) {
        this.share_count = share_count;
    }

    public int getFav_count() {
        return fav_count;
    }

    public void setFav_count(int fav_count) {
        this.fav_count = fav_count;
    }

    public String getReward_fee() {
        return reward_fee;
    }

    public void setReward_fee(String reward_fee) {
        this.reward_fee = reward_fee;
    }

    public String getLinkurl() {
        return linkurl;
    }

    public void setLinkurl(String linkurl) {
        this.linkurl = linkurl;
    }

    public int getRadio_count() {
        return radio_count;
    }

    public void setRadio_count(int radio_count) {
        this.radio_count = radio_count;
    }

    public String getNow_programtitle() {
        return now_programtitle;
    }

    public void setNow_programtitle(String now_programtitle) {
        this.now_programtitle = now_programtitle;
    }

    public String getNow_programlinkurl() {
        return now_programlinkurl;
    }

    public void setNow_programlinkurl(String now_programlinkurl) {
        this.now_programlinkurl = now_programlinkurl;
    }

    public String getNow_programdailyid() {
        return now_programdailyid;
    }

    public void setNow_programdailyid(String now_programdailyid) {
        this.now_programdailyid = now_programdailyid;
    }

    public String getNow_programid() {
        return now_programid;
    }

    public void setNow_programid(String now_programid) {
        this.now_programid = now_programid;
    }

    public String getNow_listencount() {
        return now_listencount;
    }

    public void setNow_listencount(String now_listencount) {
        this.now_listencount = now_listencount;
    }

    public String getNow_begintime() {
        return now_begintime;
    }

    public void setNow_begintime(String now_begintime) {
        this.now_begintime = now_begintime;
    }

    public String getNow_endtime() {
        return now_endtime;
    }

    public void setNow_endtime(String now_endtime) {
        this.now_endtime = now_endtime;
    }

    public String getNow_livelinkurl() {
        return now_livelinkurl;
    }

    public void setNow_livelinkurl(String now_livelinkurl) {
        this.now_livelinkurl = now_livelinkurl;
    }

    public String getBegintime() {
        return begintime;
    }

    public void setBegintime(String begintime) {
        this.begintime = begintime;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public String getAdclassid_1() {
        return adclassid_1;
    }

    public void setAdclassid_1(String adclassid_1) {
        this.adclassid_1 = adclassid_1;
    }

    public String getAdclassid_2() {
        return adclassid_2;
    }

    public void setAdclassid_2(String adclassid_2) {
        this.adclassid_2 = adclassid_2;
    }
}
