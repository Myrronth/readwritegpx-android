package com.tobiaskress.readwritegpxandroid.parser.types

import android.net.Uri
import com.tobiaskress.readwritegpxandroid.parser.ReadWriteGpx
import com.tobiaskress.readwritegpxandroid.parser.readTextAsInt
import com.tobiaskress.readwritegpxandroid.parser.readTextAsUri
import org.xmlpull.v1.XmlPullParser

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

    companion object {

        private const val ATTRIBUTE_AUTHOR = "author"
        private const val ELEMENT_YEAR = "year"
        private const val ELEMENT_LICENSE = "license"

        internal fun read(
            parser: XmlPullParser,
            elementName: String,
            namespace: String?,
            skip: (parser: XmlPullParser) -> Unit,
            loopMustContinue: (next: Int) -> Boolean
        ): Copyright {
            parser.require(XmlPullParser.START_TAG, namespace, elementName)

            val author = parser.getAttributeValue(namespace, ATTRIBUTE_AUTHOR)

            @Suppress("ComplexCondition")
            if (author == null) {
                throw NullPointerException("Attribute '$ATTRIBUTE_AUTHOR' has to be set for '$elementName'.")
            }

            var year: Int? = null
            var license: Uri? = null

            while (loopMustContinue(parser.next())) {
                if (parser.eventType != XmlPullParser.START_TAG) {
                    continue
                }

                when (parser.name) {
                    ELEMENT_YEAR -> year = ReadWriteGpx.readTextAsInt(parser, ELEMENT_YEAR, namespace)
                    ELEMENT_LICENSE -> license = ReadWriteGpx.readTextAsUri(parser, ELEMENT_LICENSE, namespace)
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
