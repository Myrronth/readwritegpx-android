package com.tobiaskress.readwritegpxandroid.parser.types

import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlSerializer

/**
 * GPX documents contain a [metadata] header, followed by [waypoints], [routes], and [tracks]. You can add your own
 * elements to the [extensions] section of the GPX document.
 */
data class Gpx(
    /**
     * You must include the version number in your GPX document.
     */
    val version: String,

    /**
     * You must include the name or URL of the software that created your GPX document. This allows others to inform
     * the creator of a GPX instance document that fails to validate.
     */
    val creator: String,

    /**
     * Metadata about the file.
     */
    val metadata: Metadata? = null,

    /**
     * A list of waypoints.
     */
    val waypoints: List<Waypoint> = listOf(),

    /**
     * A list of routes.
     */
    val routes: List<Route> = listOf(),

    /**
     * A list of tracks.
     */
    val tracks: List<Track> = listOf(),

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
        xmlSerializer.attribute(namespace, "xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance")
        xmlSerializer.attribute(namespace, "xmlns", "http://www.topografix.com/GPX/1/1")
        xmlSerializer.attribute(
            namespace,
            "xsi:schemaLocation",
            "http://www.topografix.com/GPX/1/1 http://www.topografix.com/GPX/1/1/gpx.xsd"
        )
        xmlSerializer.attribute(namespace, ATTRIBUTE_VERSION, version)
        xmlSerializer.attribute(namespace, ATTRIBUTE_CREATOR, creator)

        metadata?.serialize(xmlSerializer, ELEMENT_METADATA, namespace)

        waypoints.forEach {
            it.serialize(xmlSerializer, ELEMENT_WAYPOINT, namespace)
        }

        routes.forEach {
            it.serialize(xmlSerializer, ELEMENT_ROUTES, namespace)
        }

        tracks.forEach {
            it.serialize(xmlSerializer, ELEMENT_TRACKS, namespace)
        }

        extensions?.serialize(xmlSerializer, ELEMENT_EXTENSIONS, namespace)

        xmlSerializer.endTag(namespace, elementName)
    }

    companion object {

        private const val ATTRIBUTE_VERSION = "version"
        private const val ATTRIBUTE_CREATOR = "creator"
        private const val ELEMENT_METADATA = "metadata"
        private const val ELEMENT_WAYPOINT = "wpt"
        private const val ELEMENT_ROUTES = "rte"
        private const val ELEMENT_TRACKS = "trk"
        private const val ELEMENT_EXTENSIONS = "extensions"

        internal fun parse(
            parser: XmlPullParser,
            elementName: String,
            namespace: String?,
            skip: (parser: XmlPullParser) -> Unit,
            loopMustContinue: (next: Int) -> Boolean
        ): Gpx {
            parser.require(XmlPullParser.START_TAG, namespace, elementName)

            val version = parser.getAttributeValue(namespace, ATTRIBUTE_VERSION)
            val creator = parser.getAttributeValue(namespace, ATTRIBUTE_CREATOR)

            val nullStrings: MutableList<String> = mutableListOf()
            if (version == null) nullStrings.add(ATTRIBUTE_VERSION)
            if (creator == null) nullStrings.add(ATTRIBUTE_CREATOR)

            @Suppress("ComplexCondition")
            if (nullStrings.size > 0) {
                throw NullPointerException(
                    "Attributes ${
                        nullStrings.joinToString(", ", prefix = "'", postfix = "'")
                    } have to be set for '$elementName'."
                )
            }

            var metadata: Metadata? = null
            val waypoints: MutableList<Waypoint> = mutableListOf()
            val routes: MutableList<Route> = mutableListOf()
            val tracks: MutableList<Track> = mutableListOf()
            val extensions: Extensions? = null

            while (loopMustContinue(parser.next())) {
                if (parser.eventType != XmlPullParser.START_TAG) {
                    continue
                }

                when (parser.name) {
                    ELEMENT_METADATA -> {
                        metadata = Metadata.parse(parser, ELEMENT_METADATA, namespace, skip, loopMustContinue)
                    }
                    ELEMENT_WAYPOINT -> {
                        waypoints.add(Waypoint.parse(parser, ELEMENT_WAYPOINT, namespace, skip, loopMustContinue))
                    }
                    ELEMENT_ROUTES -> {
                        routes.add(Route.parse(parser, ELEMENT_ROUTES, namespace, skip, loopMustContinue))
                    }
                    ELEMENT_TRACKS -> {
                        tracks.add(Track.parse(parser, ELEMENT_TRACKS, namespace, skip, loopMustContinue))
                    }
                    ELEMENT_EXTENSIONS -> {
                        Extensions.parse(parser, ELEMENT_EXTENSIONS, namespace, skip, loopMustContinue)
                    }
                    else -> skip(parser)
                }
            }

            parser.require(XmlPullParser.END_TAG, namespace, elementName)

            return Gpx(
                version = version,
                creator = creator,
                metadata = metadata,
                waypoints = waypoints.toList(),
                routes = routes.toList(),
                tracks = tracks.toList(),
                extensions = extensions
            )
        }
    }
}
