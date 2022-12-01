package dev.dk.currency.exchange.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.statement.HttpResponse
import io.ktor.http.URLProtocol
import io.ktor.http.path
import kotlinx.serialization.json.Json

class ApiClient(private val json: Json, httpClientEngine: HttpClientEngine) {

    private val TIMEOUT_DEFAULT_CONFIGURATION = 30_000L

    val httpClient = HttpClient(httpClientEngine) {
//        request {
//            timeout {
//                requestTimeoutMillis = TIMEOUT_DEFAULT_CONFIGURATION
//            }
//            //host = appBaseUrl
//        }
        install(ContentNegotiation) {
            Json {
                prettyPrint = true
                isLenient = true
            }
        }
        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) {
                    co.touchlab.kermit.Logger.v { message }
                }
            }
            level = LogLevel.ALL
        }

        defaultRequest {
            //host = appBaseUrl
            header("Content-Type", "application/json")
            header("Accept", "application/json")
        }

        //followRedirects = false
    }

    suspend inline fun <reified T : Any> GET(
        route: String,
        queryPair: List<Pair<String, String>>? = null,
    ): HttpResponse = httpClient.get {
        url {
            host = "openexchangerates.org"
            protocol = URLProtocol.HTTPS
            path(route)
            queryPair?.forEach { pair ->
                parameters.append(pair.first, pair.second)
            }
        }
    }

}