package com.tobiaskress.readwritegpxandroid.parser.types

import com.tobiaskress.readwritegpxandroid.parser.ReadWriteGpx
import com.tobiaskress.readwritegpxandroid.parser.readTextAsDouble
import com.tobiaskress.readwritegpxandroid.parser.readTextAsOffsetTime
import org.xmlpull.v1.XmlPullParser
import java.time.OffsetDateTime

/**
 * A geographic point with optional elevation and time. Available for use by other schemas.
 */
data class Point(
    /**
     * The latitude of the point. Decimal degrees, WGS84 datum.
     */
    val latitude: Latitude,

    /**
     * The longitude of the point. Decimal degrees, WGS84 datum.
     */
    val longitude: Longitude,

    /**
     * Elevation (in meters) of the point.
     */
    val elevation: Double? = null,

    /**
     * The time that the point was recorded.
     */
    val time: OffsetDateTime? = null,
) {

    companion object {

        private const val ATTRIBUTE_LATITUDE = "latitude"
        private const val ATTRIBUTE_LONGITUDE = "longitude"
        private const val ELEMENT_ELEVATION = "ele"
        private const val ELEMENT_TIME = "time"

        internal fun read(
            parser: XmlPullParser,
            elementName: String,
            namespace: String?,
            skip: (parser: XmlPullParser) -> Unit,
            loopMustContinue: (next: Int) -> Boolean
        ): Point {
            parser.require(XmlPullParser.START_TAG, namespace, elementName)

            val latitude = Latitude(parser.getAttributeValue(namespace, ATTRIBUTE_LATITUDE).toDouble())
            val longitude = Longitude(parser.getAttributeValue(namespace, ATTRIBUTE_LONGITUDE).toDouble())
            var elevation: Double? = null
            var time: OffsetDateTime? = null

            while (loopMustContinue(parser.next())) {
                if (parser.eventType != XmlPullParser.START_TAG) {
                    continue
                }

                when (parser.name) {
                    ELEMENT_ELEVATION -> {
                        elevation = ReadWriteGpx.readTextAsDouble(parser, ELEMENT_ELEVATION, namespace)
                    }
                    ELEMENT_TIME -> {
                        time = ReadWriteGpx.readTextAsOffsetTime(parser, ELEMENT_TIME, namespace)
                    }
                    else -> skip(parser)
                }
            }

            parser.require(XmlPullParser.END_TAG, namespace, elementName)

            return Point(
                latitude = latitude,
                longitude = longitude,
                elevation = elevation,
                time = time
            )
        }
    }
}
