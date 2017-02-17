用shape的时候需要注意，在低版本手机上，如果没有设置solid，会默认填充黑色。

FLAG_LAYOUT_NO_LIMITS,虽然可以允许在screen外绘制，但是在4.4及以下系统上绘制完后会复原。

绘制区域超过本view，需要显示在父view上，需要设置父view的clipChildren为true。

使用Preference的时候需要注意，如果不希望Preference和自己维护的SharePreference冲突，需要setPersistent为false。

Android中的文案内容中的单引号，要特别注意加上转意字符。

Android不能直接使用com.android.internal包中的资源，但是添加相应jar包以后就可以使用，不需要像网上说的使用反射的方法，只需要在gradle中添加： provided files("${SDK_DIR}/platforms/${PLATFORMS_VER}/data/layoutlib.jar")
