#  事件分发处理库

提取 EvnetUtil 及 EventBean ，方便以后直接替换事件分发的框架

## 一、使用方法

使用方式为变更，若将原有的引用方式升级注意一下几点：

1、全局替换  import com.hoge.android.cooperation.base.util.event.EventBean;
        为  import com.android.library.bean.EventBean;

2、全局替换  import com.hoge.android.cooperation.base.util.event.EventUtil;
	    为  import com.android.library.EventUtil;

## 二、使用场景

|工程名称|模块|工程地址|负责人|
|:-----:|:-----:|:-----:|:-----:|
| 阿米协同| 全局 | https://git.hoge.cn/workcall/Android.git |张雷|
| mxu| 全局 | https://git.hoge.cn/mxu/MXU4_Android |李徐荣|

## 三、版本修改记录

|修订人|修订日期|修订说明|文档版本|
|:-----:|:-----:|:-----:|:-----:|
|李徐荣 |2017年11月17日|取消build_third引用 |1.0.1   |
|王亚 |2017年11月27日|依赖版本整体修改Android Library支持AS3.0 |1.1.0   |