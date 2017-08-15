kandy sdk demo# SDK-UI-Android


SDKDemo 快速集成文档【Android】




集成步骤
添加代码

![](./sdkdoc/1.png)

选择android studio下面的library工程库项目导入项目工程进行引用
如下所示：

![](./sdkdoc/2.png)


在工程项目中继承library工程中的application下的BaseKandyApplication。

完成如上操作及集成好Android端的项目。
代码调用
Libraey工程包control下面的TxtKandy类是SDKDemo 实例化控制类，其中包括了：</br>
	AccessKandy:账号登录；</br>
	KandyCall: kandy的初始化以及点对点的通话；</br>
	TxtMpvCallManmger:mpv视频会议控制类；</br>
	ConnectCall:页面控制跳转类；</br>
	DataMpvConnect:本地数据化存储类；</br>
	MediaPlayControl：铃声控制类；</br>
	

。
设置appid 和 appsecert 初始化kandySDK

![](./sdkdoc/3.png)
	

在application中初始化参数分别为：key、secre、hosturi.使用默认时参数置为null.</br>
参考如下：</br>
TxtKandy.getKandyCall().initKandy(this,null,null,null);</br>

用户登录</br>
参考AccessKandy </br>
TxtKandy.getAccessKandy().userLogin(user,passward,callback)</br>
User:登录用户名；</br>
Passward:用户密码；</br>
Callback:回调</br>



拨打call</br>
参考包名为call下的KadnyCall类
TxtKandy.getKandyCall().showDoCallDialog(MainActivity.this,isVideo);</br></br>
isVideo:布尔类型：是否是视频会话</br>



发起MPV 会议</br>
参考TxtMpvCallManmger</br>
TxtKandy.getConnnectCall().skipDoCallMpv(MainActivity.this);</br>
开启mpv视频会议会话，mpv相关功能模块请参考TxtMpvCallManmger</br>

