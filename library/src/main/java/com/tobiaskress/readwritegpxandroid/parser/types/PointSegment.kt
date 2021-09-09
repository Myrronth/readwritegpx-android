package com.tobiaskress.readwritegpxandroid.parser.types

import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlSerializer

/**
 * An ordered sequence of points. (for polygons or polylines, e.g.)
 */
@Suppress("Unused")
data class PointSegment(
    /**
     * Ordered list of geographic points.
     */
    val points: List<Point> = listOf()
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

        xmlSerializer.endTag(namespace, elementName)
    }

    companion object {

        private const val ELEMENT_POINT = "pt"

        internal fun parse(
            parser: XmlPullParser,
            elementName: String,
            namespace: String?,
            skip: (parser: XmlPullParser) -> Unit,
            loopMustContinue: (next: Int) -> Boolean
        ): PointSegment {
            parser.require(XmlPullParser.START_TAG, namespace, elementName)

            val points: MutableList<Point> = mutableListOf()

            while (loopMustContinue(parser.next())) {
                if (parser.eventType != XmlPullParser.START_TAG) {
                    continue
                }

                when (parser.name) {
                    ELEMENT_POINT -> {
                        points.add(Point.parse(parser, ELEMENT_POINT, namespace, skip, loopMustContinue))
                    }
                    else -> skip(parser)
                }
            }

            parser.require(XmlPullParser.END_TAG, namespace, elementName)

            return PointSegment(
                points = points.toList()
            )
        }
    }
}
