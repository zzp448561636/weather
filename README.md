模仿墨迹天气app

目前实现界面：
1、广告界面
2、定位界面
3、编辑城市界面

运用的相关技术
1、自定义圆形进度条
2、volley网络通信
3、本地数据库读写的基础操作
4、本地文件读写的基础操作
5、运用coordinatorLayout等新控件实现页面顶部图像内容的折叠
6、百度定位SDK进行定位
7、android6.0之后，某些权限例如定位需要用户同意，不再像之前只要manifest中申请，所以增加了权限申请，使用github上开源项目easypermission
8、使用overridePendingTransition设置动画，某些机型需要在设置中打开动画效果，API16以后可以通过ActivityOptions和ActivityOptionsCompat设置跳转动画
9、recyclerView中item的拖拽