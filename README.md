# HiShare
个人建议：在HiShare的基础上在业务层进行一次封装

##Gradle
```groovy
//Add it in your root build.gradle at the end of repositories:
allprojects {
		repositories {
			maven { url 'https://jitpack.io' }
		}
	}
//Add the dependency
dependencies {
	        implementation 'com.github.ooftf:HiShare:1.0.0'
	}
```
## 初始化
```java
HiShare.init(this);
HiShare.initWXShare("微信appid");
HiShare.initTencentShare("腾讯appid");
```

## 配置
### 微信分享配置
在对应包名（{applicationId}.wxapi）下新建 WXEntryActivity 继承 WXEntrysShareActivity
在AndroidManifest中注册该Activity
```xml
<activity
            android:name=".wxapi.WXEntryActivity"
            android:exported="true"/>
```

### 腾讯分享配置
```xml
<activity
            android:name="com.tencent.tauth.AuthActivity"
            android:noHistory="true"
            android:launchMode="singleTask" >
            <intent-filter>
                <action android:name="android.intent.action.VIEW" />
                <category android:name="android.intent.category.DEFAULT" />
                <category android:name="android.intent.category.BROWSABLE" />
                <data android:scheme="tencent你的appid" />
            </intent-filter>
</activity>
<activity
            android:name="com.tencent.connect.common.AssistActivity"
            android:screenOrientation="behind"
            android:theme="@android:style/Theme.Translucent.NoTitleBar"
            android:configChanges="orientation|keyboardHidden">
</activity>
```
如果使用了腾讯分享 就要在onActivityResult中调用下述代码，不然会导致收不到QQ分享的回调
```java
Hishare.onActivityResult(requestCode,resultCode,data)
```
### 调用分享
```java
 HiShare.ShareParams sp = new HiShare.ShareParams(targetUrl,title,content,imageUrl,image);
 HiShare.share(activity,shareType,sp, listener);
```

