## Lever

[![](https://jitpack.io/v/suransea/lever.svg)](https://jitpack.io/#suransea/lever)

Android 常用工具合集


#### Gradle

Step 1. Add the JitPack repository to your build file

Add it in your root build.gradle at the end of repositories:

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}

Step 2. Add the dependency

	dependencies {
	        implementation 'com.github.suransea:lever:1.0.8'
	}


#### 初始化

使用 `Lever.init(getApplicationContext());` 进行工具初始化

#### Toasts

从字符串

```java
Toasts.of("content").showShort();
```

从字符串资源

```java
Toasts.of(R.string.content).showShort()
```

自定义视图

```java
Toasts.of(view).showShort();
```

长时间

```java
Toasts.of(R.string.content).showLong();
```

非UI线程

```java
Toasts.of(R.string.content).prepare().showShort();
```

其他参数

```java
Toasts.of(R.string.content)
        .margin(1f, 1f)
        .gravity(Gravity.CENTER, 0, 0)
        .showShort();
```

#### 二维码

快速生成

```java
Bitmap qrCode = QRCodes.from("content").toBitmap();
```

带logo

```java
Bitmap qrCodeWithLogo = QRCodes.from("content").toBitmap(logoBitmap);
```

添加logo

```java
Bitmap qrCodeWithLogo = QRCodes.addLogo(qrCodeBitmap, logoBitmap);
```

其他参数

```java
QRCodes.from("content")
        .errorCorrection("L")         //容错率: L, M, Q, H
        .colorBackground(Color.WHITE) //背景色
        .colorPrimary(Color.BLUE)     //前景色
        .margin(1)                    //边距
        .width(100)                   //宽
        .height(100)                  //高
        .size(100)                    //大小, 等价于 ".width(100).height(100)"
        .characterSet("UTF-8")        //编码方式
        .toBitmap();
```

识别二维码

```java
String content = QRCodes.from(bitmap).toString();
```

#### 状态栏

隐藏状态栏

```java
StatusBars.of(activity).hide();
```

明暗/全透明

```java
StatusBars.of(activity).setBrightness(true).setFullTransparent();
```

#### 剪贴板

```java
Clipboards.getContent();          //获取文本内容
Clipboards.setContent("content"); //设置文本内容
```

#### DensityUtils

```java
dp2px, sp2px, px2dp, px2sp
```

#### 通知

获取通知权限是否开启

```java
boolean allowed = Notifications.isPermissionGranted();
```

打开通知设置界面

```java
Notifications.openNotificationSetting();
```

发送通知
```java
Notifications.send(tag, id, notification); //发送通知
```

#### Packages

判断是否安装

```java
Packages.isInstalled(packageName);
Packages.isInstalled(Packages.WECHAT);
```

打开应用信息页

```java
Packages.openAppDetailSetting(); //此应用
Packages.openAppDetailSetting(packageName); //指定包名
```

打开应用

```java
Packages.openApp(packageName);
```

#### Preferences

```java
Preferences.of(context);
Preferences.of(context, name); //指定名称
Preferences.app(); //ApplicationContext获取的SharedPreference
```

#### Permissions

```java
Permissions.with(this)
        .request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        .subscribe();
```

#### Screens

获取宽高

```java
Screens.getWidth();
Screens.getHeight();
```

截图

```java
Screens.snapshot(activity);
```

#### Bitmaps

从视图绘制

```java
Bitmaps.from(view);
Bitmaps.from(activity);
```

保存bitmap为文件

```java
Bitmaps.save(bitmap, targetFile, Bitmap.CompressFormat.PNG)
        .compose(SchedulerTransformers.<File>android())
        .subscribe(new Consumer<File>() {
            @Override
            public void accept(File saved) throws Exception {
                //保存成功
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                //处理异常
            }
        });
```

#### SchedulerTransformers

```java
SchedulerTransformers.android(); //订阅于IO线程, 观察于UI线程
SchedulerTransformers.io(); //订阅&观察于IO线程
```

> 逐步更新中...