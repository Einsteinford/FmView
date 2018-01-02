package com.einstinford.fmview;

/**
 * Created by lt on 2017/3/22.
 * <p>
 * Event管理类
 */

public class EventManager {

    public static class FmScrollEvent {
        private int position;

        public FmScrollEvent(int position) {
            this.position = position;
        }

        public int getPosition() {
            return position;
        }
    }

    public static class FmDateChangeEvent {
        private String date;

        public FmDateChangeEvent(String date) {
            this.date = date;
        }

        public String getDate() {
            return date;
        }
    }

    public static class FmAreaIdChangeEvent {
        private String areaId;
        private String areaName;

        public FmAreaIdChangeEvent(String areaId , String areaName) {
            this.areaId = areaId;
            this.areaName = areaName;
        }

        public String getAreaId() {
            return areaId;
        }

        public String getAreaName() {
            return areaName;
        }
    }

    public static class FmChannelChangeEvent {
        public FmChannelChangeEvent() {
        }
    }

}