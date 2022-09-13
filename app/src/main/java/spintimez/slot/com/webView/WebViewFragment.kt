package spintimez.slot.com.webView

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.CookieManager
import android.webkit.DownloadListener
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import spintimez.slot.com.MainActivity
import spintimez.slot.com.databinding.FragmentWebviewBinding
import spintimez.slot.com.util.logEvent
import spintimez.slot.com.util.logFragment
import com.google.firebase.analytics.FirebaseAnalytics

var webViewFragment: WebViewFragment? = null

class WebViewFragment : Fragment() {

    lateinit var binding: FragmentWebviewBinding
    lateinit var webView: WebView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        webViewFragment = this
        onBackPressed()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentWebviewBinding.inflate(inflater)

        webView = binding.webView.apply {
            with(settings) {
                savePassword = true
                saveFormData = true
                mixedContentMode = 0
                setSupportZoom(false)
                useWideViewPort = true
                allowFileAccess = true
                databaseEnabled = true
                useWideViewPort = true
                setAppCacheEnabled(true)
                domStorageEnabled = true
                javaScriptEnabled = true
                allowContentAccess = true
                loadWithOverviewMode = true
                loadsImagesAutomatically = true
                allowFileAccessFromFileURLs = true
                cacheMode = WebSettings.LOAD_DEFAULT
                allowUniversalAccessFromFileURLs = true
                javaScriptCanOpenWindowsAutomatically = true
                userAgentString = userAgentString.replaceAfter(")", "")
            }

            isFocusable = true
            isSaveEnabled = true
            isFocusableInTouchMode = true

            webChromeClient = WebViewChromeClient(this@WebViewFragment)
            webViewClient = WebViewClient(requireContext())

            CookieManager.getInstance().setAcceptCookie(true)
            CookieManager.getInstance().setAcceptThirdPartyCookies(this, true)

            setDownloadListener(getDownloadListener())
            loadUrl(MainActivity.webViewURL)

            logEvent(requireContext(), "open_web")
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        FirebaseAnalytics.getInstance(requireContext()).logFragment(this)
    }

    override fun onResume() {
        super.onResume()
        webView.onResume()
    }

    override fun onPause() {
        webView.onPause()
        super.onPause()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == WebViewChromeClient.REQUEST_SELECT_FILE) {
            if (WebViewChromeClient.uploadMessage == null) return
            WebViewChromeClient.uploadMessage!!.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(resultCode, data))
            WebViewChromeClient.uploadMessage = null
        }
    }



    private fun getDownloadListener() = DownloadListener { url, _, _, _, _ ->
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        requireActivity().startActivity(intent)
    }

    private fun onBackPressed() {
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            if (webView.canGoBack()) webView.goBack()
        }
    }

}