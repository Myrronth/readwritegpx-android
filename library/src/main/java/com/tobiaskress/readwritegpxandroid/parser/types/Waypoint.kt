package com.tobiaskress.readwritegpxandroid.parser.types

import com.tobiaskress.readwritegpxandroid.parser.ReadWriteGpx
import com.tobiaskress.readwritegpxandroid.parser.readText
import com.tobiaskress.readwritegpxandroid.parser.readTextAsDouble
import com.tobiaskress.readwritegpxandroid.parser.readTextAsLocalTime
import com.tobiaskress.readwritegpxandroid.parser.readTextAsUInt
import org.xmlpull.v1.XmlPullParser
import java.time.LocalDateTime

/**
 * Represents a waypoint, point of interest, or named feature on a map.
 */
data class Waypoint(
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
     * Creation/modification timestamp for element. Date and time in are in Universal Coordinated Time (UTC), not
     * local time! Conforms to ISO 8601 specification for date/time representation. Fractional seconds are allowed
     * for millisecond timing in tracklogs.
     */
    val time: LocalDateTime? = null,

    /**
     * Magnetic variation (in degrees) at the point
     */
    val magneticVariation: Degree? = null,

    /**
     * Height (in meters) of geoid (mean sea level) above WGS84 earth ellipsoid. As defined in NMEA GGA message.
     */
    val geoidHeight: Double? = null,

    /**
     * The GPS name of the waypoint. This field will be transferred to and from the GPS. GPX does not place
     * restrictions on the length of this field or the characters contained in it. It is up to the receiving
     * application to validate the field before sending it to the GPS.
     */
    val name: String? = null,

    /**
     * GPS waypoint comment. Sent to GPS as comment.
     */
    val comment: String? = null,

    /**
     * A text description of the element. Holds additional information about the element intended for the user, not
     * the GPS.
     */
    val description: String? = null,

    /**
     * Source of data. Included to give user some idea of reliability and accuracy of data. "Garmin eTrex", "USGS
     * quad Boston North", e.g.
     */
    val source: String? = null,

    /**
     * Link to additional information about the waypoint.
     */
    val links: List<Link> = listOf(),

    /**
     * Text of GPS symbol name. For interchange with other programs, use the exact spelling of the symbol as
     * displayed on the GPS. If the GPS abbreviates words, spell them out.
     */
    val symbol: String? = null,

    /**
     * Type (classification) of the waypoint.
     */
    val type: String? = null,

    /**
     * Type of GPX fix.
     */
    val fix: Fix? = null,

    /**
     * Number of satellites used to calculate the GPX fix.
     */
    val numberOfSatellites: UInt? = null,

    /**
     * Horizontal dilution of precision.
     */
    val horizontalDilutionOfPrecision: Double? = null,

    /**
     * Vertical dilution of precision.
     */
    val verticalDilutionOfPrecision: Double? = null,

    /**
     * Position dilution of precision.
     */
    val positionDilutionOfPrecision: Double? = null,

    /**
     * Number of seconds since last DGPS update.
     */
    val ageOfDgpsData: Double? = null,

    /**
     * ID of DGPS station used in differential correction.
     */
    val dgpsId: DgpsStation? = null,

    /**
     * Not yet implemented.
     *
     * You can add extend GPX by adding your own elements from another schema here.
     */
    val extensions: Extensions? = null
) {

    companion object {

        private const val ATTRIBUTE_LATITUDE = "lat"
        private const val ATTRIBUTE_LONGITUDE = "lon"
        private const val ELEMENT_ELEVATION = "ele"
        private const val ELEMENT_TIME = "time"
        private const val ELEMENT_MAGNETIC_VARIATION = "magvar"
        private const val ELEMENT_GEOID_HEIGHT = "geoidheight"
        private const val ELEMENT_NAME = "name"
        private const val ELEMENT_COMMENT = "cmt"
        private const val ELEMENT_DESCRIPTION = "desc"
        private const val ELEMENT_SOURCE = "src"
        private const val ELEMENT_LINK = "link"
        private const val ELEMENT_SYMBOL = "sym"
        private const val ELEMENT_TYPE = "type"
        private const val ELEMENT_FIX = "fix"
        private const val ELEMENT_NUMBER_OF_SATELLITES = "sat"
        private const val ELEMENT_HORIZONTAL_DILUTION_OF_PRECISION = "hdop"
        private const val ELEMENT_VERTICAL_DILUTION_OF_PRECISION = "vdop"
        private const val ELEMENT_POSITION_DILUTION_OF_PRECISION = "pdop"
        private const val ELEMENT_AGE_OF_DGPS_DATA = "ageofdgpsdata"
        private const val ELEMENT_DGPS_ID = "dgpsid"
        private const val ELEMENT_EXTENSIONS = "extensions"

        @Suppress("ComplexMethod", "LongMethod")
        internal fun read(
            parser: XmlPullParser,
            elementName: String,
            namespace: String?,
            skip: (parser: XmlPullParser) -> Unit,
            loopMustContinue: (next: Int) -> Boolean
        ): Waypoint {
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
            var time: LocalDateTime? = null
            var magneticVariation: Degree? = null
            var geoidHeight: Double? = null
            var name: String? = null
            var comment: String? = null
            var description: String? = null
            var source: String? = null
            val links: MutableList<Link> = mutableListOf()
            var symbol: String? = null
            var type: String? = null
            var fix: Fix? = null
            var numberOfSatellites: UInt? = null
            var horizontalDilutionOfPrecision: Double? = null
            var verticalDilutionOfPrecision: Double? = null
            var positionDilutionOfPrecision: Double? = null
            var ageOfDgpsData: Double? = null
            var dgpsId: DgpsStation? = null
            val extensions: Extensions? = null

            while (loopMustContinue(parser.next())) {
                if (parser.eventType != XmlPullParser.START_TAG) {
                    continue
                }

                when (parser.name) {
                    ELEMENT_ELEVATION -> {
                        elevation = ReadWriteGpx.readTextAsDouble(parser, ELEMENT_ELEVATION, namespace)
                    }
                    ELEMENT_TIME -> {
                        time = ReadWriteGpx.readTextAsLocalTime(parser, ELEMENT_TIME, namespace)
                    }
                    ELEMENT_MAGNETIC_VARIATION -> {
                        magneticVariation = Degree(
                            ReadWriteGpx.readTextAsDouble(parser, ELEMENT_MAGNETIC_VARIATION, namespace)
                        )
                    }
                    ELEMENT_GEOID_HEIGHT -> {
                        geoidHeight = ReadWriteGpx.readTextAsDouble(parser, ELEMENT_GEOID_HEIGHT, namespace)
                    }
                    ELEMENT_NAME -> {
                        name = ReadWriteGpx.readText(parser, ELEMENT_NAME, namespace)
                    }
                    ELEMENT_COMMENT -> {
                        comment = ReadWriteGpx.readText(parser, ELEMENT_COMMENT, namespace)
                    }
                    ELEMENT_DESCRIPTION -> {
                        description = ReadWriteGpx.readText(parser, ELEMENT_DESCRIPTION, namespace)
                    }
                    ELEMENT_SOURCE -> {
                        source = ReadWriteGpx.readText(parser, ELEMENT_SOURCE, namespace)
                    }
                    ELEMENT_LINK -> {
                        links.add(Link.read(parser, ELEMENT_LINK, namespace, skip, loopMustContinue))
                    }
                    ELEMENT_SYMBOL -> {
                        symbol = ReadWriteGpx.readText(parser, ELEMENT_SYMBOL, namespace)
                    }
                    ELEMENT_TYPE -> {
                        type = ReadWriteGpx.readText(parser, ELEMENT_TYPE, namespace)
                    }
                    ELEMENT_FIX -> {
                        fix = Fix.valueOf(ReadWriteGpx.readText(parser, ELEMENT_FIX, namespace))
                    }
                    ELEMENT_NUMBER_OF_SATELLITES -> {
                        numberOfSatellites = ReadWriteGpx.readTextAsUInt(parser, ELEMENT_NUMBER_OF_SATELLITES, namespace)
                    }
                    ELEMENT_HORIZONTAL_DILUTION_OF_PRECISION -> {
                        horizontalDilutionOfPrecision = ReadWriteGpx.readTextAsDouble(
                            parser,
                            ELEMENT_HORIZONTAL_DILUTION_OF_PRECISION,
                            namespace
                        )
                    }
                    ELEMENT_VERTICAL_DILUTION_OF_PRECISION -> {
                        verticalDilutionOfPrecision = ReadWriteGpx.readTextAsDouble(
                            parser,
                            ELEMENT_VERTICAL_DILUTION_OF_PRECISION,
                            namespace
                        )
                    }
                    ELEMENT_POSITION_DILUTION_OF_PRECISION -> {
                        positionDilutionOfPrecision = ReadWriteGpx.readTextAsDouble(
                            parser,
                            ELEMENT_POSITION_DILUTION_OF_PRECISION,
                            namespace
                        )
                    }
                    ELEMENT_AGE_OF_DGPS_DATA -> {
                        ageOfDgpsData = ReadWriteGpx.readTextAsDouble(parser, ELEMENT_AGE_OF_DGPS_DATA, namespace)
                    }
                    ELEMENT_DGPS_ID -> {
                        dgpsId = DgpsStation(ReadWriteGpx.readTextAsUInt(parser, ELEMENT_DGPS_ID, namespace))
                    }
                    ELEMENT_EXTENSIONS -> {
                        Extensions.read(parser, ELEMENT_EXTENSIONS, namespace, skip, loopMustContinue)
                    }
                    else -> skip(parser)
                }
            }

            parser.require(XmlPullParser.END_TAG, namespace, elementName)

            return Waypoint(
                latitude = latitude,
                longitude = longitude,
                elevation = elevation,
                time = time,
                magneticVariation = magneticVariation,
                geoidHeight = geoidHeight,
                name = name,
                comment = comment,
                description = description,
                source = source,
                links = links.toList(),
                symbol = symbol,
                type = type,
                fix = fix,
                numberOfSatellites = numberOfSatellites,
                horizontalDilutionOfPrecision = horizontalDilutionOfPrecision,
                verticalDilutionOfPrecision = verticalDilutionOfPrecision,
                positionDilutionOfPrecision = positionDilutionOfPrecision,
                ageOfDgpsData = ageOfDgpsData,
                dgpsId = dgpsId,
                extensions = extensions
            )
        }
    }
}
