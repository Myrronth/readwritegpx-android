package com.tobiaskress.readwritegpxandroid.parser.types

import android.net.Uri
import com.tobiaskress.readwritegpxandroid.parser.ReadWriteGpx
import com.tobiaskress.readwritegpxandroid.parser.readText
import org.xmlpull.v1.XmlPullParser

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

    companion object {

        private const val ATTRIBUTE_HREF = "href"
        private const val ELEMENT_TEXT = "text"
        private const val ELEMENT_TYPE = "type"

        internal fun read(
            parser: XmlPullParser,
            elementName: String,
            namespace: String?,
            skip: (parser: XmlPullParser) -> Unit,
            loopMustContinue: (next: Int) -> Boolean
        ): Link {
            parser.require(XmlPullParser.START_TAG, namespace, elementName)

            val href: Uri = Uri.parse(parser.getAttributeValue(namespace, ATTRIBUTE_HREF))
            var text: String? = null
            var type: String? = null

            while (loopMustContinue(parser.next())) {
                if (parser.eventType != XmlPullParser.START_TAG) {
                    continue
                }

                when (parser.name) {
                    ELEMENT_TEXT -> text = ReadWriteGpx.readText(parser, ELEMENT_TEXT, namespace)
                    ELEMENT_TYPE -> type = ReadWriteGpx.readText(parser, ELEMENT_TYPE, namespace)
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
