package com.xinlan.imageeditlibrary.editimage.model;

import java.util.List;

/**
 * Created by User on 11-08-2017.
 */

public class ImageEditingData {


    /**
     * stickerImageURL : http://174.66.76.164/manageapp/File/Images/
     * stickerData : {"stickerCategoryData":[{"stickerCategoryName":"Sticker for Diwali","stickerCategoryURL":"https://appstickers-cdn.appadvice.com/1165419227/819351665/3a25e50b9951bef6d74429928d18718d-0.png","children":[{"stickerImageURL":"http://dl.stickershop.line.naver.jp/products/0/0/1/3498/android/main.png"},{"stickerImageURL":"http://cdn.apk-cloud.com/detail/screenshot/PQTYqBeHZYORBPefvRkq_16vgfhwjYnQJfzk9qGjkKXT3zP4hB_yN-1BRWcYL-CNoIc=h900.png"}]},{"stickerCategoryName":"Sticker for Diwali","stickerCategoryURL":"https://mir-s3-cdn-cf.behance.net/project_modules/disp/5861a625752471.5634a32f1ce36.png","children":[{"stickerImageURL":"http://123emoji.com/wp-content/uploads/2016/08/Rilakkuma_Xmas_New-Year-Stickers_6697.png"},{"stickerImageURL":"http://cdn.apk-cloud.com/detail/screenshot/PQTYqBeHZYORBPefvRkq_16vgfhwjYnQJfzk9qGjkKXT3zP4hB_yN-1BRWcYL-CNoIc=h900.png"}]}]}
     */

    private String stickerImageURL;
    private StickerDataBean stickerData;

    public String getStickerImageURL() {
        return stickerImageURL;
    }

    public void setStickerImageURL(String stickerImageURL) {
        this.stickerImageURL = stickerImageURL;
    }

    public StickerDataBean getStickerData() {
        return stickerData;
    }

    public void setStickerData(StickerDataBean stickerData) {
        this.stickerData = stickerData;
    }

    public static class StickerDataBean {
        private List<StickerCategoryDataBean> stickerCategoryData;

        public List<StickerCategoryDataBean> getStickerCategoryData() {
            return stickerCategoryData;
        }

        public void setStickerCategoryData(List<StickerCategoryDataBean> stickerCategoryData) {
            this.stickerCategoryData = stickerCategoryData;
        }

        public static class StickerCategoryDataBean {
            /**
             * stickerCategoryName : Sticker for Diwali
             * stickerCategoryURL : https://appstickers-cdn.appadvice.com/1165419227/819351665/3a25e50b9951bef6d74429928d18718d-0.png
             * children : [{"stickerImageURL":"http://dl.stickershop.line.naver.jp/products/0/0/1/3498/android/main.png"},{"stickerImageURL":"http://cdn.apk-cloud.com/detail/screenshot/PQTYqBeHZYORBPefvRkq_16vgfhwjYnQJfzk9qGjkKXT3zP4hB_yN-1BRWcYL-CNoIc=h900.png"}]
             */

            private String stickerCategoryName;
            private String stickerCategoryURL;
            private List<ChildrenBean> children;

            public String getStickerCategoryName() {
                return stickerCategoryName;
            }

            public void setStickerCategoryName(String stickerCategoryName) {
                this.stickerCategoryName = stickerCategoryName;
            }

            public String getStickerCategoryURL() {
                return stickerCategoryURL;
            }

            public void setStickerCategoryURL(String stickerCategoryURL) {
                this.stickerCategoryURL = stickerCategoryURL;
            }

            public List<ChildrenBean> getChildren() {
                return children;
            }

            public void setChildren(List<ChildrenBean> children) {
                this.children = children;
            }

            public static class ChildrenBean {
                /**
                 * stickerImageURL : http://dl.stickershop.line.naver.jp/products/0/0/1/3498/android/main.png
                 */

                private String stickerImageURL;

                public String getStickerImageURL() {
                    return stickerImageURL;
                }

                public void setStickerImageURL(String stickerImageURL) {
                    this.stickerImageURL = stickerImageURL;
                }
            }
        }
    }
}
