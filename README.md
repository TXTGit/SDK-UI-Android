kandy sdk demo# SDK-UI-Android


SDKDemo ���ټ����ĵ���Android��




���ɲ���
��Ӵ���

ѡ��android studio�����library���̿���Ŀ������Ŀ���̽�������
������ʾ��


�ڹ�����Ŀ�м̳�library�����е�application�µ�BaseKandyApplication��

������ϲ��������ɺ�Android�˵���Ŀ��
�������
Libraey���̰�control�����TxtKandy����SDKDemo ʵ���������࣬���а����ˣ�
	AccessKandy:�˺ŵ�¼��
	KandyCall: kandy�ĳ�ʼ���Լ���Ե��ͨ����
	TxtMpvCallManmger:mpv��Ƶ��������ࣻ
	ConnectCall:ҳ�������ת�ࣻ
	DataMpvConnect:�������ݻ��洢�ࣻ
	MediaPlayControl�����������ࣻ
	

��
����appid �� appsecert ��ʼ��kandySDK
	

��application�г�ʼ�������ֱ�Ϊ��key��secre��,hosturi��ʹ��Ĭ��ʱ������Ϊnull
TxtKandy.getKandyCall().initKandy(this,null,null,null);

�û���¼
�ο�AccessKandy 
TxtKandy.getAccessKandy().userLogin(user,passward,callback)
User:��¼�û�����
Passward:�û����룻
Callback:�ص�
����call
�ο�SupportViewController
TxtKandy.getKandyCall().showDoCallDialog(MainActivity.this,isVideo);


isVideo:�������ͣ��Ƿ�����Ƶ�Ự
����MPV ����
�ο�TxtMpvCallManmger

TxtKandy.getConnnectCall().skipDoCallMpv(MainActivity.this);

����mpv��Ƶ����Ự��mpv��ع���ģ����ο�TxtMpvCallManmger

