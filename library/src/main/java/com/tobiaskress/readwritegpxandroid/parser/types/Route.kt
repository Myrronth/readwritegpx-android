package com.tobiaskress.readwritegpxandroid.parser.types

import com.tobiaskress.readwritegpxandroid.parser.ReadWriteGpx
import com.tobiaskress.readwritegpxandroid.parser.readText
import com.tobiaskress.readwritegpxandroid.parser.readTextAsUInt
import org.xmlpull.v1.XmlPullParser

/**
 * Represents route - an ordered list of waypoints representing a series of turn points leading to a destination.
 */
data class Route(
    /**
     * GPS name of route.
     */
    val name: String? = null,

    /**
     * GPS comment for route.
     */
    val comment: String? = null,

    /**
     * Text description of route for user. Not sent to GPS.
     */
    val description: String? = null,

    /**
     * Source of data. Included to give user some idea of reliability and accuracy of data.
     */
    val source: String? = null,

    /**
     * Links to external information about the route.
     */
    val links: List<Link> = listOf(),

    /**
     * GPS route number.
     */
    val number: UInt? = null,

    /**
     * Type (classification) of route.
     */
    val type: String? = null,

    /**
     * Not yet implemented.
     *
     * You can add extend GPX by adding your own elements from another schema here.
     */
    val extensions: Extensions? = null,

    /**
     * A list of route points.
     */
    val points: List<Waypoint> = listOf(),
) {

    companion object {

        private const val ELEMENT_NAME = "name"
        private const val ELEMENT_COMMENT = "cmt"
        private const val ELEMENT_DESCRIPTION = "desc"
        private const val ELEMENT_SOURCE = "src"
        private const val ELEMENT_LINK = "link"
        private const val ELEMENT_NUMBER = "number"
        private const val ELEMENT_TYPE = "type"
        private const val ELEMENT_EXTENSIONS = "extensions"
        private const val ELEMENT_POINT = "rtept"

        internal fun read(
            parser: XmlPullParser,
            elementName: String,
            namespace: String?,
            skip: (parser: XmlPullParser) -> Unit,
            loopMustContinue: (next: Int) -> Boolean
        ): Route {
            parser.require(XmlPullParser.START_TAG, namespace, elementName)

            var name: String? = null
            var comment: String? = null
            var description: String? = null
            var source: String? = null
            val links: MutableList<Link> = mutableListOf()
            var type: String? = null
            var number: UInt? = null
            val extensions: Extensions? = null
            val points: MutableList<Waypoint> = mutableListOf()

            while (loopMustContinue(parser.next())) {
                if (parser.eventType != XmlPullParser.START_TAG) {
                    continue
                }

                when (parser.name) {
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
                    ELEMENT_NUMBER -> {
                        number = ReadWriteGpx.readTextAsUInt(parser, ELEMENT_NUMBER, namespace)
                    }
                    ELEMENT_TYPE -> {
                        type = ReadWriteGpx.readText(parser, ELEMENT_TYPE, namespace)
                    }
                    ELEMENT_EXTENSIONS -> {
                        Extensions.read(parser, ELEMENT_EXTENSIONS, namespace, skip, loopMustContinue)
                    }
                    ELEMENT_POINT -> {
                        points.add(Waypoint.read(parser, ELEMENT_POINT, namespace, skip, loopMustContinue))
                    }
                    else -> skip(parser)
                }
            }

            parser.require(XmlPullParser.END_TAG, namespace, elementName)

            return Route(
                name = name,
                comment = comment,
                description = description,
                source = source,
                links = links.toList(),
                number = number,
                type = type,
                extensions = extensions,
                points = points.toList()
            )
        }
    }
}
