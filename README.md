简单的BaseActivity和BaseAppView

BaseActivity有需要的时候，可以重写以下几个方法达到不同的效果：

    1.isStatusBarTransparent()实现沉浸式状态栏效果；

    2.getActivityOrientation()实现控制Activity的方向；

    3.getIntentData()实现解析Intent传递参数数据；


To get a Git project into your build:

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
	        implementation 'com.github.xiaoming6672:Library_common:Tag'
	}
