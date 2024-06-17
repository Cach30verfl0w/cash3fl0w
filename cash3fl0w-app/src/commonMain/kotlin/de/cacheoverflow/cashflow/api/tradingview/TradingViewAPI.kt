/*
 * This project is licensed with GNU General Public License 2.0
 * Copyright (c) 2024 Cach30verfl0w <cach0verfl0w@gmail.com>
 */

package de.cacheoverflow.cashflow.api.tradingview

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.request
import io.ktor.client.request.setBody
import io.ktor.http.HttpMethod
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.serialDescriptor
import kotlinx.serialization.encodeToString
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.encodeStructure
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.serializer

/**
 * This class represents the scan filter. This filter can be used to filter out companies in the
 * market scan to acquire only companies with more specific parameters.
 *
 * @author Cedric Hammes
 * @since  17/06/2024
 */
@Serializable(with = ScanFilterSerializer::class)
data class ScanFilter(val left: String, val operation: String, val type: String, val right: Any)

/**
 * This is a specific serializer for the [ScanFilter] data class. This allows to serialize this
 * structure with the parameter typed with [Any]. This class is only being used internally.
 *
 * @author Cedric Hammes
 * @since  17/06/2024
 */
private class ScanFilterSerializer: KSerializer<ScanFilter> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("Packet") {
        element("left", serialDescriptor<String>())
        element("operation", serialDescriptor<String>())
        element("right", buildClassSerialDescriptor("Any"))
    }

    @Suppress("UNCHECKED_CAST")
    private val dataTypeSerializers: Map<String, KSerializer<Any>> = mapOf(
        "String" to serializer<String>(),
        "Int" to serializer<Int>()
    ).mapValues { (_, v) -> v as KSerializer<Any> }

    override fun serialize(encoder: Encoder, value: ScanFilter)
    = encoder.encodeStructure(descriptor) {
        encodeStringElement(descriptor, 0, value.left)
        encodeStringElement(descriptor, 1, value.operation)
        encodeSerializableElement(
            descriptor,
            2,
            checkNotNull(dataTypeSerializers[value.type]),
            value.right
        )
    }

    override fun deserialize(decoder: Decoder): ScanFilter {
        // We don't need to deserialize them anyway
        throw UnsupportedOperationException("Deserialization of ScanFilters is not implemented")
    }
}

/**
 * This data class contains information that is used in the scan markets request. This parameters
 * specify the columns returned to the user and the filters applied.
 *
 * @author Cedric Hammes
 * @since  17/06/2024
 */
@Serializable
data class ScanRequestParameters(
    val columns: List<String>,
    val filter: List<ScanFilter>? = null,
    val range: IntArray = intArrayOf(0, 100)
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ScanRequestParameters

        if (columns != other.columns) return false
        if (filter != other.filter) return false
        if (!range.contentEquals(other.range)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = columns.hashCode()
        result = 31 * result + (filter?.hashCode() ?: 0)
        result = 31 * result + range.contentHashCode()
        return result
    }
}

/**
 * This is a simple API wrapper for the TradingView API. TradingView is a social media network and
 * analysis platform for traders and investors. I got some values for this library form other libs
 * around.
 *
 * This API is used by a background service to acquire a list of companies in a specific market to
 * allow the user to choose between them and acquire notifications about news on the market and
 * more. We also give this app the ability to track market prices etc.
 *
 * @see https://github.com/shner-elmo/TradingView-Screener/tree/master
 * @see https://en.wikipedia.org/wiki/TradingView
 * @see https://www.tradingview.com/
 *
 * @author Cedric Hammes
 * @since  17/06/2024
 */
class TradingViewAPI(private val client: HttpClient) {

    /**
     * This method sends a request to TradingView to receive a list of companies in the specified
     * market with some information about them like market capitalization or revenue.
     *
     * @param market The name of the market represented by the [EnumMarket] enum
     * @return       A list of companies with information about them
     *
     * @author Cedric Hammes
     * @since  17/06/2024
     */
    suspend fun scanMarket(market: EnumMarket, columns: List<String> = defaultColumns) {
        val url = "https://scanner.tradingview.com/${market.toString().lowercase()}/scan"
        val listData = checkNotNull(Json.parseToJsonElement(client.request(url) {
            setBody(Json.encodeToString(ScanRequestParameters(columns)))
            method = HttpMethod.Post
        }.body<String>()).jsonObject["data"]).jsonArray

        for (company in listData) {
            // Get exchange and company short
            val companyObject = company.jsonObject
            val (exchange, companyShort) = checkNotNull(companyObject["s"]).jsonPrimitive.toString()
                .split(":").let { Pair(it[0], it[1]) }

            // Handle data
            val companyData = checkNotNull(companyObject["d"]).jsonArray
            for (dataField in companyData) {
                val data = dataField.jsonPrimitive
                // TODO: Handle data
            }
        }
    }

    companion object {
        private val defaultColumns = arrayListOf(
            "description",
            "market_cap_basic", // Market capitalization
            "country",
            "industry",
            "net_income",
            "dps_common_stock_prim_issue_fy", // Dividends per Share
            "earnings_per_share_basic_ttm", // Earnings per Share (EPS)
            "total_revenue"
        )
    }

}
