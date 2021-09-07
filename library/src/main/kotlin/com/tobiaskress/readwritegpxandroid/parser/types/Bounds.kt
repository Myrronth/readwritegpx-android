package com.tobiaskress.readwritegpxandroid.parser.types

import org.xmlpull.v1.XmlPullParser

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

    companion object {

        private const val ATTRIBUTE_MINIMUM_LATITUDE = "minlat"
        private const val ATTRIBUTE_MINIMUM_LONGITUDE = "minlon"
        private const val ATTRIBUTE_MAXIMUM_LATITUDE = "maxlat"
        private const val ATTRIBUTE_MAXIMUM_LONGITUDE = "maxlon"

        internal fun read(
            parser: XmlPullParser,
            elementName: String,
            namespace: String?,
            @Suppress("UNUSED_PARAMETER") skip: (parser: XmlPullParser) -> Unit,
            @Suppress("UNUSED_PARAMETER") loopMustContinue: (next: Int) -> Boolean
        ): Bounds {
            parser.require(XmlPullParser.START_TAG, namespace, elementName)

            val minimumLatitude = Latitude(parser.getAttributeValue(namespace, ATTRIBUTE_MINIMUM_LATITUDE)
                .toDouble())
            val minimumLongitude = Longitude(parser.getAttributeValue(namespace, ATTRIBUTE_MINIMUM_LONGITUDE)
                .toDouble())
            val maximumLatitude = Latitude(parser.getAttributeValue(namespace, ATTRIBUTE_MAXIMUM_LATITUDE)
                .toDouble())
            val maximumLongitude = Longitude(parser.getAttributeValue(namespace, ATTRIBUTE_MAXIMUM_LONGITUDE)
                .toDouble())

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
