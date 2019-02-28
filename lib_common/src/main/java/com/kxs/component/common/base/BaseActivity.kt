package com.kxs.component.common.base

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.MotionEvent
import android.view.WindowManager
import com.kxs.component.common.constant.Constant
import com.kxs.component.common.utils.CommonUtil
import com.kxs.component.common.utils.KeyBoardUtil
import com.kxs.component.common.utils.Preference


abstract class BaseActivity : AppCompatActivity() {

    /**
     * 是否登录
     */
    protected var isLogin: Boolean by Preference(Constant.LOGIN_KEY, false)


    /**
     * 提示View
     */
    protected lateinit var mWindowManager: WindowManager
    protected lateinit var mLayoutParams: WindowManager.LayoutParams

    /**
     * 布局文件id
     */
    protected abstract fun attachLayoutRes(): Int

    /**
     * 初始化数据
     */
    abstract fun initData()

    /**
     * 初始化 View
     */
    abstract fun initView()

    /**
     * 开始请求
     */
    abstract fun start()


    /**
     * 无网状态—>有网状态 的自动重连操作，子类可重写该方法
     */
    open fun doReConnected() {
        start()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        // AutoDensityUtil.setCustomDensity(this, App.instance)
        super.onCreate(savedInstanceState)
        setContentView(attachLayoutRes())
        initData()
        initView()
        start()
    }

    override fun onResume() {

        super.onResume()


        // 在无网络情况下打开APP时，系统不会发送网络状况变更的Intent，需要自己手动检查

        // 1.第一次进入界面会导致 start() 方法走两次
        // 2.后台切换到前台时，会调用 start() 方法执行相应的操作
        // 此处不应该调用，删掉，修改 #13
        // checkNetwork(hasNetwork)

    }



    protected fun initToolbar(toolbar: Toolbar, homeAsUpEnabled: Boolean, title: String) {
        toolbar?.title = title
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(homeAsUpEnabled)
    }



    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (ev?.action == MotionEvent.ACTION_UP) {
            val v = currentFocus
            // 如果不是落在EditText区域，则需要关闭输入法
            if (KeyBoardUtil.isHideKeyboard(v, ev)) {
                KeyBoardUtil.hideKeyBoard(this, v)
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        // Fragment 逐个出栈
        val count = supportFragmentManager.backStackEntryCount
        if (count == 0) {
            super.onBackPressed()
        } else {
            supportFragmentManager.popBackStack()
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        CommonUtil.fixInputMethodManagerLeak(this)
    }

}