package com.tobiaskress.readwritegpxandroid.parser.types

import com.tobiaskress.readwritegpxandroid.parser.GpxParser
import com.tobiaskress.readwritegpxandroid.parser.helper.readText
import com.tobiaskress.readwritegpxandroid.parser.helper.readTextAsUInt
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlSerializer

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

    internal fun serialize(
        xmlSerializer: XmlSerializer,
        elementName: String,
        namespace: String?
    ) {
        xmlSerializer.startTag(namespace, elementName)

        name?.takeIf { it.isNotEmpty() }?.let {
            xmlSerializer.startTag(namespace, ELEMENT_NAME)
            xmlSerializer.text(it)
            xmlSerializer.endTag(namespace, ELEMENT_NAME)
        }

        comment?.takeIf { it.isNotEmpty() }?.let {
            xmlSerializer.startTag(namespace, ELEMENT_COMMENT)
            xmlSerializer.text(it)
            xmlSerializer.endTag(namespace, ELEMENT_COMMENT)
        }

        description?.takeIf { it.isNotEmpty() }?.let {
            xmlSerializer.startTag(namespace, ELEMENT_DESCRIPTION)
            xmlSerializer.text(it)
            xmlSerializer.endTag(namespace, ELEMENT_DESCRIPTION)
        }

        source?.takeIf { it.isNotEmpty() }?.let {
            xmlSerializer.startTag(namespace, ELEMENT_SOURCE)
            xmlSerializer.text(it)
            xmlSerializer.endTag(namespace, ELEMENT_SOURCE)
        }

        links.forEach {
            it.serialize(xmlSerializer, ELEMENT_LINK, namespace)
        }

        number?.let {
            xmlSerializer.startTag(namespace, ELEMENT_NUMBER)
            xmlSerializer.text(it.toString())
            xmlSerializer.endTag(namespace, ELEMENT_NUMBER)
        }

        type?.takeIf { it.isNotEmpty() }?.let {
            xmlSerializer.startTag(namespace, ELEMENT_TYPE)
            xmlSerializer.text(it)
            xmlSerializer.endTag(namespace, ELEMENT_TYPE)
        }

        extensions?.serialize(xmlSerializer, ELEMENT_EXTENSIONS, namespace)

        points.forEach {
            it.serialize(xmlSerializer, ELEMENT_POINT, namespace)
        }

        xmlSerializer.endTag(namespace, elementName)
    }

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

        internal fun parse(
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
                        name = GpxParser.readText(parser, ELEMENT_NAME, namespace)
                    }
                    ELEMENT_COMMENT -> {
                        comment = GpxParser.readText(parser, ELEMENT_COMMENT, namespace)
                    }
                    ELEMENT_DESCRIPTION -> {
                        description = GpxParser.readText(parser, ELEMENT_DESCRIPTION, namespace)
                    }
                    ELEMENT_SOURCE -> {
                        source = GpxParser.readText(parser, ELEMENT_SOURCE, namespace)
                    }
                    ELEMENT_LINK -> {
                        links.add(Link.parse(parser, ELEMENT_LINK, namespace, skip, loopMustContinue))
                    }
                    ELEMENT_NUMBER -> {
                        number = GpxParser.readTextAsUInt(parser, ELEMENT_NUMBER, namespace)
                    }
                    ELEMENT_TYPE -> {
                        type = GpxParser.readText(parser, ELEMENT_TYPE, namespace)
                    }
                    ELEMENT_EXTENSIONS -> {
                        Extensions.parse(parser, ELEMENT_EXTENSIONS, namespace, skip, loopMustContinue)
                    }
                    ELEMENT_POINT -> {
                        points.add(Waypoint.parse(parser, ELEMENT_POINT, namespace, skip, loopMustContinue))
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
