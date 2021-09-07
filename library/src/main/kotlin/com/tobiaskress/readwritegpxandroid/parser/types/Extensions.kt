package com.tobiaskress.readwritegpxandroid.parser.types

import org.xmlpull.v1.XmlPullParser

/**
 * Not yet implemented.
 *
 * You can add extend GPX by adding your own elements from another schema here.
 */
object Extensions {

    internal fun read(
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
