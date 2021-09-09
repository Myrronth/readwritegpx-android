package com.tobiaskress.readwritegpxandroid.parser.types

import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlSerializer

/**
 * Two lat/lon pairs defining the extent of an element.
 */
data class Bounds(
    /**
     * The minimum latitude.
     */
    val minimumLatitude: Latitude,

    /**
     * The minimum longitude.
     */
    val minimumLongitude: Longitude,

    /**
     * The maximum latitude.
     */
    val maximumLatitude: Latitude,

    /**
     * The maximum longitude.
     */
    val maximumLongitude: Longitude
) {

    internal fun serialize(
        xmlSerializer: XmlSerializer,
        elementName: String,
        namespace: String?
    ) {
        xmlSerializer.startTag(namespace, elementName)

        xmlSerializer.attribute(
            namespace,
            ATTRIBUTE_MINIMUM_LATITUDE,
            minimumLatitude.decimalDegrees.toBigDecimal().toPlainString()
        )

        xmlSerializer.attribute(
            namespace,
            ATTRIBUTE_MINIMUM_LONGITUDE,
            minimumLongitude.decimalDegrees.toBigDecimal().toPlainString()
        )

        xmlSerializer.attribute(
            namespace,
            ATTRIBUTE_MAXIMUM_LATITUDE,
            maximumLatitude.decimalDegrees.toBigDecimal().toPlainString()
        )

        xmlSerializer.attribute(
            namespace,
            ATTRIBUTE_MAXIMUM_LONGITUDE,
            maximumLongitude.decimalDegrees.toBigDecimal().toPlainString()
        )

        xmlSerializer.endTag(namespace, elementName)
    }

    companion object {

        private const val ATTRIBUTE_MINIMUM_LATITUDE = "minlat"
        private const val ATTRIBUTE_MINIMUM_LONGITUDE = "minlon"
        private const val ATTRIBUTE_MAXIMUM_LATITUDE = "maxlat"
        private const val ATTRIBUTE_MAXIMUM_LONGITUDE = "maxlon"

        internal fun parse(
            parser: XmlPullParser,
            elementName: String,
            namespace: String?,
            @Suppress("UNUSED_PARAMETER") skip: (parser: XmlPullParser) -> Unit,
            @Suppress("UNUSED_PARAMETER") loopMustContinue: (next: Int) -> Boolean
        ): Bounds {
            parser.require(XmlPullParser.START_TAG, namespace, elementName)

            val minimumLatitudeString = parser.getAttributeValue(namespace, ATTRIBUTE_MINIMUM_LATITUDE)
            val minimumLongitudeString = parser.getAttributeValue(namespace, ATTRIBUTE_MINIMUM_LONGITUDE)
            val maximumLatitudeString = parser.getAttributeValue(namespace, ATTRIBUTE_MAXIMUM_LATITUDE)
            val maximumLongitudeString = parser.getAttributeValue(namespace, ATTRIBUTE_MAXIMUM_LONGITUDE)

            val nullStrings: MutableList<String> = mutableListOf()
            if (minimumLatitudeString == null) nullStrings.add(ATTRIBUTE_MINIMUM_LATITUDE)
            if (minimumLongitudeString == null) nullStrings.add(ATTRIBUTE_MINIMUM_LONGITUDE)
            if (maximumLatitudeString == null) nullStrings.add(ATTRIBUTE_MAXIMUM_LATITUDE)
            if (maximumLongitudeString == null) nullStrings.add(ATTRIBUTE_MAXIMUM_LONGITUDE)

            @Suppress("ComplexCondition")
            if (nullStrings.size > 0) {
                throw NullPointerException(
                    "Attributes ${
                        nullStrings.joinToString(", ", prefix = "'", postfix = "'")
                    } have to be set for '$elementName'."
                )
            }

            val minimumLatitude = Latitude(minimumLatitudeString.toDouble())
            val minimumLongitude = Longitude(minimumLongitudeString.toDouble())
            val maximumLatitude = Latitude(maximumLatitudeString.toDouble())
            val maximumLongitude = Longitude(maximumLongitudeString.toDouble())

            // Bounds element is self closed, advance the parser to next event
            parser.next()

            parser.require(XmlPullParser.END_TAG, namespace, elementName)

            return Bounds(
                minimumLatitude = minimumLatitude,
                minimumLongitude = minimumLongitude,
                maximumLatitude = maximumLatitude,
                maximumLongitude = maximumLongitude
            )
        }
    }
}
