package com.rofiqoff.news.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding

abstract class BaseActivity<T> : AppCompatActivity() {

    private var _binding: ViewBinding? = null
    abstract val bindingInflater: (LayoutInflater) -> ViewBinding

    @Suppress("UNCHECKED_CAST")
    protected val binding: T
        get() = _binding as T

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = bindingInflater.invoke(layoutInflater)
        setContentView(requireNotNull(_binding).root)
        initView()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    abstract fun initView()

    fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}
