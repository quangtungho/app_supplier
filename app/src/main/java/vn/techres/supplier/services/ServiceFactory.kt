package vn.techres.supplier.services

import android.annotation.SuppressLint
import android.content.Context
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import vn.techres.supplier.helper.AppConfig
import vn.techres.supplier.helper.CacheManager
import vn.techres.supplier.helper.WriteLog
import vn.techres.supplier.model.entity.CurrentConfigNode
import vn.techres.supplier.model.entity.CurrentUser
import java.io.IOException
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

/**
 * Created by tuan.nguyen on 19/01/17.
 */
class ServiceFactory private constructor() {
    private val cacheManager: CacheManager = CacheManager.getInstance()
    private var context: Context? = null

    private val httpInterceptor: Interceptor
        get() = object : Interceptor {
            @SuppressLint("DefaultLocale")
            @Throws(IOException::class)
            override fun intercept(chain: Interceptor.Chain): Response {
                var request = chain.request()

                val newRequestBuilder = request.newBuilder()
                    .header("Accept", "application/json")
                val maxStale = 60 * 60 * 24 * 5
                newRequestBuilder.header(
                    "Authorization",
                    CurrentUser.getAccessToken(context!!) + ""
                )
                //newRequestBuilder.header("Authorization", "")
                newRequestBuilder.addHeader("Cache-Control", "max-stale=$maxStale")
                request = newRequestBuilder.build()
                val t1 = System.nanoTime()
                WriteLog.i(
                    "Interceptor REQ", String.format(
                        "%s on %s%n%s",
                        request.url, chain.connection(), request.headers
                    )
                )

                val response: Response = chain.proceed(request).newBuilder()
                    .build()

                // error code: 20
                if (response.code != 200)
                    WriteLog.e("error code", response.code.toString() + "")


                val t2 = System.nanoTime()
                WriteLog.i(
                    "Interceptor RESP:", String.format(
                        "%s in %.1fms%n%s",
                        response.request.url, (t2 - t1) / 1e6, response.headers
                    )
                )
                return response
            }
        }

    private val httpInterceptorNode: Interceptor
        get() = object : Interceptor {
            @SuppressLint("DefaultLocale")
            @Throws(IOException::class)
            override fun intercept(chain: Interceptor.Chain): Response {
                var request = chain.request()

                val newRequestBuilder = request.newBuilder()
                    .header("Accept", "application/json")
                val maxStale = 60 * 60 * 24 * 5
                newRequestBuilder.header(
                    "Authorization",
                    CurrentUser.getAccessTokenNodeJs(context!!) + ""
                )
                //newRequestBuilder.header("Authorization", "")
                newRequestBuilder.addHeader("Cache-Control", "max-stale=$maxStale")
                request = newRequestBuilder.build()
                val t1 = System.nanoTime()
                WriteLog.i(
                    "Interceptor REQ", String.format(
                        "%s on %s%n%s",
                        request.url, chain.connection(), request.headers
                    )
                )

                val response: Response = chain.proceed(request).newBuilder()
                    .build()
                WriteLog.e("error code", response.code.toString() + "")
                val t2 = System.nanoTime()
                WriteLog.i(
                    "Interceptor RESP:", String.format(
                        "%s in %.1fms%n%s",
                        response.request.url, (t2 - t1) / 1e6, response.headers
                    )
                )
                return response
            }
        }

    private val httpInterceptorLoginNode: Interceptor
        get() = object : Interceptor {
            @SuppressLint("DefaultLocale")
            @Throws(IOException::class)
            override fun intercept(chain: Interceptor.Chain): Response {
                var request = chain.request()
                val newRequestBuilder = request.newBuilder()
                    .header("Accept", "application/json")
                val maxStale = 60 * 60 * 24 * 5
                val configNodeJs = CurrentConfigNode.getConfigNode(context!!)
                newRequestBuilder.header(
                    "Authorization", "Basic " + configNodeJs.api_key.toString() + ""
                )

                newRequestBuilder.addHeader("Cache-Control", "max-stale=$maxStale")
                request = newRequestBuilder.build()
                val t1 = System.nanoTime()
                WriteLog.i(
                    "Interceptor REQ", String.format(
                        "%s on %s%n%s",
                        request.url, chain.connection(), request.headers
                    )
                )

                val response: Response = chain.proceed(request).newBuilder()
                    .build()
                WriteLog.e("error code", response.code.toString() + "")
                val t2 = System.nanoTime()
                WriteLog.i(
                    "Interceptor RESP:", String.format(
                        "%s in %.1fms%n%s",
                        response.request.url, (t2 - t1) / 1e6, response.headers
                    )
                )
                return response
            }
        }

    fun getRetrofit(endPoint: String): Retrofit {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        val authenticator = Authenticator()

        val trustAllCerts =
            arrayOf<TrustManager>(
                object : X509TrustManager {
                    @SuppressLint("TrustAllX509TrustManager")
                    @Throws(CertificateException::class)
                    override fun checkClientTrusted(
                        chain: Array<X509Certificate>,
                        authType: String,
                    ) {
                    }

                    @SuppressLint("TrustAllX509TrustManager")
                    @Throws(CertificateException::class)
                    override fun checkServerTrusted(
                        chain: Array<X509Certificate>,
                        authType: String,
                    ) {
                    }

                    override fun getAcceptedIssuers(): Array<X509Certificate> {
                        return arrayOf()
                    }
                }
            )

        // Install the all-trusting trust manager
        val sslContext = SSLContext.getInstance("SSL")
        sslContext.init(null, trustAllCerts, SecureRandom())
        // Create an ssl socket factory with our all-trusting manager
        val sslSocketFactory = sslContext.socketFactory

        val clientBuilder = OkHttpClient.Builder()
        clientBuilder.addNetworkInterceptor(httpInterceptor)
        clientBuilder.addInterceptor(interceptor)
        clientBuilder.connectTimeout(60, TimeUnit.SECONDS)
        clientBuilder.readTimeout(60, TimeUnit.SECONDS)
        clientBuilder.writeTimeout(60, TimeUnit.SECONDS)
        clientBuilder.authenticator(authenticator)

        return Retrofit.Builder()
            .baseUrl(endPoint)
            .client(clientBuilder.build())
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }

    fun getRetrofitNode(endPoint: String): Retrofit {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        val authenticator = Authenticator()

        val trustAllCerts =
            arrayOf<TrustManager>(
                object : X509TrustManager {
                    @SuppressLint("TrustAllX509TrustManager")
                    @Throws(CertificateException::class)
                    override fun checkClientTrusted(
                        chain: Array<X509Certificate>,
                        authType: String,
                    ) {
                    }

                    @SuppressLint("TrustAllX509TrustManager")
                    @Throws(CertificateException::class)
                    override fun checkServerTrusted(
                        chain: Array<X509Certificate>,
                        authType: String,
                    ) {
                    }

                    override fun getAcceptedIssuers(): Array<X509Certificate> {
                        return arrayOf()
                    }
                }
            )

        // Install the all-trusting trust manager
        val sslContext = SSLContext.getInstance("SSL")
        sslContext.init(null, trustAllCerts, SecureRandom())
        // Create an ssl socket factory with our all-trusting manager
        val sslSocketFactory = sslContext.socketFactory

        val clientBuilder = OkHttpClient.Builder()
        clientBuilder.addNetworkInterceptor(httpInterceptorNode)
        clientBuilder.addInterceptor(interceptor)
        clientBuilder.connectTimeout(60, TimeUnit.SECONDS)
        clientBuilder.readTimeout(60, TimeUnit.SECONDS)
        clientBuilder.writeTimeout(60, TimeUnit.SECONDS)
        clientBuilder.authenticator(authenticator)

        return Retrofit.Builder()
            .baseUrl(endPoint)
            .client(clientBuilder.build())
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }

    fun getRetrofitLoginNode(endPoint: String): Retrofit {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        val authenticator = Authenticator()

        val trustAllCerts =
            arrayOf<TrustManager>(
                object : X509TrustManager {
                    @SuppressLint("TrustAllX509TrustManager")
                    @Throws(CertificateException::class)
                    override fun checkClientTrusted(
                        chain: Array<X509Certificate>,
                        authType: String,
                    ) {
                    }

                    @SuppressLint("TrustAllX509TrustManager")
                    @Throws(CertificateException::class)
                    override fun checkServerTrusted(
                        chain: Array<X509Certificate>,
                        authType: String,
                    ) {
                    }

                    override fun getAcceptedIssuers(): Array<X509Certificate> {
                        return arrayOf()
                    }
                }
            )

        val clientBuilder = OkHttpClient.Builder()
        clientBuilder.addNetworkInterceptor(httpInterceptorLoginNode)
        clientBuilder.addInterceptor(interceptor)
        clientBuilder.connectTimeout(60, TimeUnit.SECONDS)
        clientBuilder.readTimeout(60, TimeUnit.SECONDS)
        clientBuilder.writeTimeout(60, TimeUnit.SECONDS)
        clientBuilder.authenticator(authenticator)

        return Retrofit.Builder()
            .baseUrl(endPoint)
            .client(clientBuilder.build())
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }

    fun setContext(context: Context) {
        this.context = context
    }

    companion object {
        private val cache: Cache? = null

        @SuppressLint("StaticFieldLeak")
        private var serviceFactory: ServiceFactory? = null
        private var retrofit: Retrofit? = null
        private var retrofitNodeJs: Retrofit? = null
        private var retrofitLoginNode: Retrofit? = null
        private var retrofitRefeshNode: Retrofit? = null

        val instance: ServiceFactory
            get() {
                if (serviceFactory == null) serviceFactory = ServiceFactory()
                return this.serviceFactory!!
            }

        /**
         * Creates a retrofit service from an arbitrary class (clazz)
         *
         * @param clazz    Java interface of the retrofit service
         * @param endPoint REST endpoint url
         * @return retrofit service with defined endpoint
         */
        private var urlApi = ""
        private var urlApiNodeJs = ""
        private var urlApiNodeLogin = ""
        private var urlApiNodeRefesh = ""

        fun <T> createRetrofitService(clazz: Class<T>, context: Context): T {
            instance.setContext(context)
            retrofit = instance.getRetrofit(AppConfig.getBaseURL())
            return retrofit!!.create(clazz)
        }

        fun <T> createRetrofitServiceNode(clazz: Class<T>, context: Context): T {
            instance.setContext(context)
            retrofitNodeJs = instance.getRetrofitNode(AppConfig.getBaseURL())
            return retrofitNodeJs!!.create(clazz)
        }

        fun <T> createRetrofitLoginNode(clazz: Class<T>, context: Context): T {
            if (retrofitLoginNode == null) {
                instance.setContext(context)
                retrofitLoginNode = instance.getRetrofitLoginNode(AppConfig.getBaseURL())
            }

            return retrofitLoginNode!!.create(clazz)
        }
    }
}
