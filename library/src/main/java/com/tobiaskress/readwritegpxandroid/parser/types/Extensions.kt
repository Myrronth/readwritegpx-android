package com.tobiaskress.readwritegpxandroid.parser.types

import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlSerializer

/**
 * Not yet implemented.
 *
 * You can add extend GPX by adding your own elements from another schema here.
 */
object Extensions {

    internal fun serialize(
        xmlSerializer: XmlSerializer,
        elementName: String,
        namespace: String?
    ) {
        xmlSerializer.startTag(namespace, elementName)
        xmlSerializer.endTag(namespace, elementName)
    }

    internal fun parse(
        parser: XmlPullParser,
        elementName: String,
        namespace: String?,
        @Suppress("UNUSED_PARAMETER") skip: (parser: XmlPullParser) -> Unit,
        @Suppress("UNUSED_PARAMETER") loopMustContinue: (next: Int) -> Boolean
    ) {
        parser.require(XmlPullParser.START_TAG, namespace, elementName)

        skip(parser)

        parser.require(XmlPullParser.END_TAG, namespace, elementName)
    }
}
