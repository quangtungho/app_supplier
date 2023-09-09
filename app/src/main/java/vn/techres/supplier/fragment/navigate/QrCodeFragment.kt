package vn.techres.supplier.fragment.navigate

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.ScanMode
import vn.techres.supplier.R
import vn.techres.supplier.base.BaseBindingFragment
import vn.techres.supplier.databinding.FragmentQrCodeBinding

/**
 * ================================================
 * Created by HOQUANGTUNG on 2021
 * ================================================
 */
class QrCodeFragment : BaseBindingFragment<FragmentQrCodeBinding>(FragmentQrCodeBinding::inflate) {
    val tagName: String = QrCodeFragment::class.java.name
    private val CAMERAPERSON = 101
    private lateinit var codeScanner: CodeScanner
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onBody()
    }
    private fun onBody() {
        mainActivity!!.setLoading(false)
        val animFragment = AnimationUtils.loadAnimation(context, R.anim.fragment_alpha_show)
        binding!!.constrainLayoutMain.startAnimation(animFragment)
        mainActivity!!.setHeader(getString(R.string.txt_qr_code))
        mainActivity!!.setBackClick(true)
        setupPermissions()
        codeScanner()
    }
    private fun codeScanner() {
        codeScanner = CodeScanner(mainActivity!!, binding!!.scannerView)
        codeScanner.apply {
            camera = CodeScanner.CAMERA_BACK
            formats = CodeScanner.ALL_FORMATS
            autoFocusMode = AutoFocusMode.SAFE
            scanMode = ScanMode.CONTINUOUS
            isAutoFocusEnabled = true
            isFlashEnabled = false

            binding!!.scannerView.setOnClickListener {
                codeScanner.startPreview()
            }
        }
    }
    private fun setupPermissions() {
        val permission =
            ContextCompat.checkSelfPermission(mainActivity!!, android.Manifest.permission.CAMERA)
        if (permission != PackageManager.PERMISSION_GRANTED) {
            makeRequest()
        }
    }
    private fun makeRequest() {
        ActivityCompat.requestPermissions(
            mainActivity!!,
            arrayOf(android.Manifest.permission.CAMERA),
            CAMERAPERSON
        )
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            CAMERAPERSON -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(mainActivity!!, "abc", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }
    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }
    override fun onBackPress() {
        mainActivity!!.supportFragmentManager.popBackStack()
    }
}