package com.tobiaskress.readwritegpxandroid.parser.types

import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlSerializer

/**
 * A Track Segment holds a list of Track Points which are logically connected in order. To represent a single GPS
 * track where GPS reception was lost, or the GPS receiver was turned off, start a new Track Segment for each
 * continuous span of track data.
 */
data class TrackSegment(
    /**
     * A Track Point holds the coordinates, elevation, timestamp, and metadata for a single point in a track.
     */
    val points: List<Waypoint> = listOf(),

    /**
     * Not yet implemented.
     *
     * You can add extend GPX by adding your own elements from another schema here.
     */
    val extensions: Extensions? = null
) {

    internal fun serialize(
        xmlSerializer: XmlSerializer,
        elementName: String,
        namespace: String?
    ) {
        xmlSerializer.startTag(namespace, elementName)

        points.forEach {
            it.serialize(xmlSerializer, ELEMENT_POINT, namespace)
        }

        extensions?.serialize(xmlSerializer, ELEMENT_EXTENSIONS, namespace)

        xmlSerializer.endTag(namespace, elementName)
    }

    companion object {

        private const val ELEMENT_POINT = "trkpt"
        private const val ELEMENT_EXTENSIONS = "extensions"

        internal fun parse(
            parser: XmlPullParser,
            elementName: String,
            namespace: String?,
            skip: (parser: XmlPullParser) -> Unit,
            loopMustContinue: (next: Int) -> Boolean
        ): TrackSegment {
            parser.require(XmlPullParser.START_TAG, namespace, elementName)

            val points: MutableList<Waypoint> = mutableListOf()
            val extensions: Extensions? = null

            while (loopMustContinue(parser.next())) {
                if (parser.eventType != XmlPullParser.START_TAG) {
                    continue
                }

                when (parser.name) {
                    ELEMENT_POINT -> {
                        points.add(Waypoint.parse(parser, ELEMENT_POINT, namespace, skip, loopMustContinue))
                    }
                    ELEMENT_EXTENSIONS -> {
                        Extensions.parse(parser, ELEMENT_EXTENSIONS, namespace, skip, loopMustContinue)
                    }
                    else -> skip(parser)
                }
            }

            parser.require(XmlPullParser.END_TAG, namespace, elementName)

            return TrackSegment(
                points = points.toList(),
                extensions = extensions
            )
        }
    }
}
