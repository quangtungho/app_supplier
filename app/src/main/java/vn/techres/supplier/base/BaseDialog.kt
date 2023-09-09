package vn.techres.supplier.base

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import vn.techres.supplier.R

abstract class BaseDialog : Activity() {
    abstract val layoutResourceId: Int
    abstract fun loadControl()
    abstract fun onMapping()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutResourceId)
        onMapping()
        loadControl()
        if (findViewById<View?>(R.id.btnClear) != null) {
            val btnClear = findViewById<ImageView>(R.id.btnClear)
            btnClear.setOnClickListener { onBackPressed() }
        }
    }

    fun setTitleHeader(title: String?) {
        val header: TextView
        if (findViewById<View?>(R.id.txtTitleDialog) != null && title != null) {
            header = findViewById(R.id.txtTitleDialog)
            header.text = title
        }
    }

    fun closeKeyBoard(editText: EditText) {
        editText.requestFocus()
        val imm = this.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm?.hideSoftInputFromWindow(editText.windowToken, 0)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}