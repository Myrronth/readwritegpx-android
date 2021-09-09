package com.tobiaskress.readwritegpxandroid.parser.types

import com.tobiaskress.readwritegpxandroid.parser.GpxParser
import com.tobiaskress.readwritegpxandroid.parser.readTextAsDouble
import com.tobiaskress.readwritegpxandroid.parser.readTextAsOffsetTime
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlSerializer
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

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

    internal fun serialize(
        xmlSerializer: XmlSerializer,
        elementName: String,
        namespace: String?
    ) {
        xmlSerializer.startTag(namespace, elementName)

        xmlSerializer.attribute(
            namespace,
            ATTRIBUTE_LATITUDE,
            latitude.decimalDegrees.toBigDecimal().toPlainString()
        )

        xmlSerializer.attribute(
            namespace,
            ATTRIBUTE_LONGITUDE,
            longitude.decimalDegrees.toBigDecimal().toPlainString()
        )

        elevation?.let {
            xmlSerializer.startTag(namespace, ELEMENT_ELEVATION)
            xmlSerializer.text(it.toBigDecimal().toPlainString())
            xmlSerializer.endTag(namespace, ELEMENT_ELEVATION)
        }

        time?.let {
            xmlSerializer.startTag(namespace, ELEMENT_TIME)
            xmlSerializer.text(it.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME))
            xmlSerializer.endTag(namespace, ELEMENT_TIME)
        }

        xmlSerializer.endTag(namespace, elementName)
    }

    companion object {

        private const val ATTRIBUTE_LATITUDE = "latitude"
        private const val ATTRIBUTE_LONGITUDE = "longitude"
        private const val ELEMENT_ELEVATION = "ele"
        private const val ELEMENT_TIME = "time"

        internal fun parse(
            parser: XmlPullParser,
            elementName: String,
            namespace: String?,
            skip: (parser: XmlPullParser) -> Unit,
            loopMustContinue: (next: Int) -> Boolean
        ): Point {
            parser.require(XmlPullParser.START_TAG, namespace, elementName)

            val latitudeString = parser.getAttributeValue(namespace, ATTRIBUTE_LATITUDE)
            val longitudeString = parser.getAttributeValue(namespace, ATTRIBUTE_LONGITUDE)

            val nullStrings: MutableList<String> = mutableListOf()
            if (latitudeString == null) nullStrings.add(ATTRIBUTE_LATITUDE)
            if (longitudeString == null) nullStrings.add(ATTRIBUTE_LONGITUDE)

            @Suppress("ComplexCondition")
            if (nullStrings.size > 0) {
                throw NullPointerException(
                    "Attributes ${
                        nullStrings.joinToString(", ", prefix = "'", postfix = "'")
                    } have to be set for '$elementName'."
                )
            }

            val latitude = Latitude(latitudeString.toDouble())
            val longitude = Longitude(longitudeString.toDouble())
            var elevation: Double? = null
            var time: OffsetDateTime? = null

            while (loopMustContinue(parser.next())) {
                if (parser.eventType != XmlPullParser.START_TAG) {
                    continue
                }

                when (parser.name) {
                    ELEMENT_ELEVATION -> {
                        elevation = GpxParser.readTextAsDouble(parser, ELEMENT_ELEVATION, namespace)
                    }
                    ELEMENT_TIME -> {
                        time = GpxParser.readTextAsOffsetTime(parser, ELEMENT_TIME, namespace)
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
