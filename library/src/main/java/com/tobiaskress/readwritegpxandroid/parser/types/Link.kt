package com.tobiaskress.readwritegpxandroid.parser.types

import android.net.Uri
import com.tobiaskress.readwritegpxandroid.parser.GpxParser
import com.tobiaskress.readwritegpxandroid.parser.readText
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlSerializer

/**
 * A link to an external resource (Web page, digital photo, video clip, etc) with additional information.
 */
data class Link(
    /**
     * URL of hyperlink.
     */
    val href: Uri,

    /**
     * Text of hyperlink.
     */
    val text: String? = null,

    /**
     * Mime type of content (image/jpeg)
     */
    val type: String? = null
) {

    internal fun serialize(
        xmlSerializer: XmlSerializer,
        elementName: String,
        namespace: String?
    ) {
        xmlSerializer.startTag(namespace, elementName)
        xmlSerializer.attribute(namespace, ATTRIBUTE_HREF, href.toString())

        text?.takeIf { it.isNotEmpty() }?.let {
            xmlSerializer.startTag(namespace, ELEMENT_TEXT)
            xmlSerializer.text(it)
            xmlSerializer.endTag(namespace, ELEMENT_TEXT)
        }

        type?.takeIf { it.isNotEmpty() }?.let {
            xmlSerializer.startTag(namespace, ELEMENT_TYPE)
            xmlSerializer.text(it)
            xmlSerializer.endTag(namespace, ELEMENT_TYPE)
        }

        xmlSerializer.endTag(namespace, elementName)
    }

    companion object {

        private const val ATTRIBUTE_HREF = "href"
        private const val ELEMENT_TEXT = "text"
        private const val ELEMENT_TYPE = "type"

        internal fun parse(
            parser: XmlPullParser,
            elementName: String,
            namespace: String?,
            skip: (parser: XmlPullParser) -> Unit,
            loopMustContinue: (next: Int) -> Boolean
        ): Link {
            parser.require(XmlPullParser.START_TAG, namespace, elementName)

            val hrefString = parser.getAttributeValue(namespace, ATTRIBUTE_HREF)

            @Suppress("ComplexCondition")
            if (hrefString == null) {
                throw NullPointerException("Attribute $ATTRIBUTE_HREF has to be set for '$elementName'.")
            }

            val href = Uri.parse(hrefString)
            var text: String? = null
            var type: String? = null

            while (loopMustContinue(parser.next())) {
                if (parser.eventType != XmlPullParser.START_TAG) {
                    continue
                }

                when (parser.name) {
                    ELEMENT_TEXT -> text = GpxParser.readText(parser, ELEMENT_TEXT, namespace)
                    ELEMENT_TYPE -> type = GpxParser.readText(parser, ELEMENT_TYPE, namespace)
                    else -> skip(parser)
                }
            }

            parser.require(XmlPullParser.END_TAG, namespace, elementName)

            return Link(
                href = href,
                text = text,
                type = type
            )
        }
    }
}
