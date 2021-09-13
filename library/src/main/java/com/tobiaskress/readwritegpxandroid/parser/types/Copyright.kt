package com.tobiaskress.readwritegpxandroid.parser.types

import android.net.Uri
import com.tobiaskress.readwritegpxandroid.parser.GpxParser
import com.tobiaskress.readwritegpxandroid.parser.helper.readTextAsInt
import com.tobiaskress.readwritegpxandroid.parser.helper.readTextAsUri
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlSerializer

/**
 * Information about the copyright holder and any license governing use of this file. By linking to an appropriate
 * license, you may place your data into the public domain or grant additional usage rights.
 */
data class Copyright(
    /**
     * Copyright holder (TopoSoft, Inc.)
     */
    val author: String,

    /**
     * Year of copyright.
     */
    val year: Int? = null,

    /**
     * Link to external file containing license text.
     */
    val license: Uri? = null
) {

    internal fun serialize(
        xmlSerializer: XmlSerializer,
        elementName: String,
        namespace: String?
    ) {
        xmlSerializer.startTag(namespace, elementName)
        xmlSerializer.attribute(namespace, ATTRIBUTE_AUTHOR, author)

        year?.let {
            xmlSerializer.startTag(namespace, ELEMENT_YEAR)
            xmlSerializer.text(it.toString())
            xmlSerializer.endTag(namespace, ELEMENT_YEAR)
        }

        license?.let {
            xmlSerializer.startTag(namespace, ELEMENT_LICENSE)
            xmlSerializer.text(it.toString())
            xmlSerializer.endTag(namespace, ELEMENT_LICENSE)
        }

        xmlSerializer.endTag(namespace, elementName)
    }

    companion object {

        private const val ATTRIBUTE_AUTHOR = "author"
        private const val ELEMENT_YEAR = "year"
        private const val ELEMENT_LICENSE = "license"

        internal fun parse(
            parser: XmlPullParser,
            elementName: String,
            namespace: String?,
            skip: (parser: XmlPullParser) -> Unit,
            loopMustContinue: (next: Int) -> Boolean
        ): Copyright {
            parser.require(XmlPullParser.START_TAG, namespace, elementName)

            val author = parser.getAttributeValue(namespace, ATTRIBUTE_AUTHOR)
                ?: throw NullPointerException("Attribute '$ATTRIBUTE_AUTHOR' has to be set for '$elementName'.")

            var year: Int? = null
            var license: Uri? = null

            while (loopMustContinue(parser.next())) {
                if (parser.eventType != XmlPullParser.START_TAG) {
                    continue
                }

                when (parser.name) {
                    ELEMENT_YEAR -> year = GpxParser.readTextAsInt(parser, ELEMENT_YEAR, namespace)
                    ELEMENT_LICENSE -> license = GpxParser.readTextAsUri(parser, ELEMENT_LICENSE, namespace)
                    else -> skip(parser)
                }
            }

            parser.require(XmlPullParser.END_TAG, namespace, elementName)

            return Copyright(
                author = author,
                year = year,
                license = license
            )
        }
    }
}
