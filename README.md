# HiShare
个人建议：在HiShare的基础上在业务层进行一次封装
# 初始化
HiShare.init(this);
HiShare.initWXShare("微信appid");
HiShare.initTencentShare("腾讯appid");
## 配置
# 微信分享配置
在对应包名（{applicationId}.wxapi）下新建 WXEntryActivity 继承 WXEntrysShareActivity
在AndroidManifest中注册该Activity
# 腾讯分享配置
