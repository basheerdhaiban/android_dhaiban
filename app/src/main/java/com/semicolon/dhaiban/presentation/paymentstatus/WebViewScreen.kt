package com.semicolon.dhaiban.presentation.paymentstatus

import android.annotation.SuppressLint
import android.view.ViewGroup
import android.webkit.ClientCertRequest
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import com.semicolon.dhaiban.designSystem.DhaibanTheme


/*@SuppressLint("SetJavaScriptEnabled")
@Composable
fun WebViewScreen(urlToLoad: String) {
    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { context ->
            WebView(context).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                webViewClient = WebViewClient() // Handle redirects within the WebView
                settings.javaScriptEnabled = true // Enable JavaScript if needed
                loadUrl(urlToLoad)
            }
        },
        update = { webView ->
            println("Web View was successful")
            // You can update the URL dynamically here if needed
            // webView.loadUrl(urlToLoad)
        }
    )
}*/


@SuppressLint("SetJavaScriptEnabled")
@Composable
fun WebViewScreen(
    url: String,
    onHtmlRetrieved: (String) -> Unit = {},
    onRequestIntercepted: (WebResourceRequest?) -> Unit = {},
    onReceivedClientCertRequest: (ClientCertRequest?) -> Unit = {}
) {
    var webView: WebView? by remember { mutableStateOf(null) }

    AndroidView(factory = { context ->
        WebView(context).apply {
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true
            webViewClient = object : WebViewClient() {

                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    // Get full HTML after page load
                    view?.evaluateJavascript(
                        "(function() { return document.documentElement.outerHTML; })();"
                    ) { html ->
                        onHtmlRetrieved(html)
                    }
                }

                override fun onReceivedClientCertRequest(
                    view: WebView?,
                    request: ClientCertRequest?
                ) {
                    super.onReceivedClientCertRequest(view, request)
                    onReceivedClientCertRequest(request)
                }

                override fun shouldInterceptRequest(
                    view: WebView?,
                    request: WebResourceRequest?
                ): WebResourceResponse? {
                    onRequestIntercepted(request)
                    return super.shouldInterceptRequest(view, request)
                }
            }
            loadUrl(url)
            webView = this
        }
    }, update = {
        if (it.url != url) it.loadUrl(url)
    })
}

@Preview(showBackground = true)
@Composable
fun WebViewScreenPreview() {
    DhaibanTheme {
        WebViewScreen(
            url = "",
            onHtmlRetrieved = {},
            onRequestIntercepted = {},
            onReceivedClientCertRequest = {}
        )
    }
}