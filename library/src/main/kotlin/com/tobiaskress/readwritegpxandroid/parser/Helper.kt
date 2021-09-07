package com.tobiaskress.readwritegpxandroid.parser

import android.net.Uri
import org.xmlpull.v1.XmlPullParser
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

private fun ReadWriteGpx.Companion.readText(parser: XmlPullParser): String {
    var result = ""

    if (parser.next() == XmlPullParser.TEXT) {
        result = parser.text
        parser.nextTag()
    }

    return result
}

internal fun ReadWriteGpx.Companion.readText(
    parser: XmlPullParser,
    elementName: String,
    namespace: String?
): String {
    parser.require(XmlPullParser.START_TAG, namespace, elementName)
    val text = ReadWriteGpx.readText(parser)
    parser.require(XmlPullParser.END_TAG, namespace, elementName)

    return text
}

internal fun ReadWriteGpx.Companion.readTextAsInt(
    parser: XmlPullParser,
    elementName: String,
    namespace: String?
): Int {
    parser.require(XmlPullParser.START_TAG, namespace, elementName)
    val int = ReadWriteGpx.readText(parser).toInt()
    parser.require(XmlPullParser.END_TAG, namespace, elementName)

    return int
}

internal fun ReadWriteGpx.Companion.readTextAsUInt(
    parser: XmlPullParser,
    elementName: String,
    namespace: String?
): UInt {
    parser.require(XmlPullParser.START_TAG, namespace, elementName)
    val uInt = ReadWriteGpx.readText(parser).toUInt()
    parser.require(XmlPullParser.END_TAG, namespace, elementName)

    return uInt
}

internal fun ReadWriteGpx.Companion.readTextAsDouble(
    parser: XmlPullParser,
    elementName: String,
    namespace: String?
): Double {
    parser.require(XmlPullParser.START_TAG, namespace, elementName)
    val double = ReadWriteGpx.readText(parser).toDouble()
    parser.require(XmlPullParser.END_TAG, namespace, elementName)

    return double
}

internal fun ReadWriteGpx.Companion.readTextAsUri(
    parser: XmlPullParser,
    elementName: String,
    namespace: String?
): Uri {
    parser.require(XmlPullParser.START_TAG, namespace, elementName)
    val uri = Uri.parse(ReadWriteGpx.readText(parser))
    parser.require(XmlPullParser.END_TAG, namespace, elementName)

    return uri
}

internal fun ReadWriteGpx.Companion.readTextAsOffsetTime(
    parser: XmlPullParser,
    elementName: String,
    namespace: String?
): OffsetDateTime {
    parser.require(XmlPullParser.START_TAG, namespace, elementName)
    val offsetDateTime = OffsetDateTime.parse(ReadWriteGpx.readText(parser))
    parser.require(XmlPullParser.END_TAG, namespace, elementName)

    return offsetDateTime
}

internal fun ReadWriteGpx.Companion.readTextAsLocalTime(
    parser: XmlPullParser,
    elementName: String,
    namespace: String?
): LocalDateTime {
    parser.require(XmlPullParser.START_TAG, namespace, elementName)
    val localDateTime = LocalDateTime.parse(ReadWriteGpx.readText(parser), DateTimeFormatter.ISO_DATE_TIME)
    parser.require(XmlPullParser.END_TAG, namespace, elementName)

    return localDateTime
}
