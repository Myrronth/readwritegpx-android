package com.tobiaskress.readwritegpxandroid.parser.helper

import android.net.Uri
import com.tobiaskress.readwritegpxandroid.parser.GpxParser
import org.xmlpull.v1.XmlPullParser
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

private fun GpxParser.Companion.readText(parser: XmlPullParser): String {
    var result = ""

    if (parser.next() == XmlPullParser.TEXT) {
        result = parser.text
        parser.nextTag()
    }

    return result
}

internal fun GpxParser.Companion.readText(
    parser: XmlPullParser,
    elementName: String,
    namespace: String?
): String {
    parser.require(XmlPullParser.START_TAG, namespace, elementName)
    val text = GpxParser.readText(parser)
    parser.require(XmlPullParser.END_TAG, namespace, elementName)

    return text
}

internal fun GpxParser.Companion.readTextAsInt(
    parser: XmlPullParser,
    elementName: String,
    namespace: String?
): Int {
    parser.require(XmlPullParser.START_TAG, namespace, elementName)
    val int = GpxParser.readText(parser).toInt()
    parser.require(XmlPullParser.END_TAG, namespace, elementName)

    return int
}

internal fun GpxParser.Companion.readTextAsUInt(
    parser: XmlPullParser,
    elementName: String,
    namespace: String?
): UInt {
    parser.require(XmlPullParser.START_TAG, namespace, elementName)
    val uInt = GpxParser.readText(parser).toUInt()
    parser.require(XmlPullParser.END_TAG, namespace, elementName)

    return uInt
}

internal fun GpxParser.Companion.readTextAsDouble(
    parser: XmlPullParser,
    elementName: String,
    namespace: String?
): Double {
    parser.require(XmlPullParser.START_TAG, namespace, elementName)
    val double = GpxParser.readText(parser).toDouble()
    parser.require(XmlPullParser.END_TAG, namespace, elementName)

    return double
}

internal fun GpxParser.Companion.readTextAsUri(
    parser: XmlPullParser,
    elementName: String,
    namespace: String?
): Uri {
    parser.require(XmlPullParser.START_TAG, namespace, elementName)
    val uri = Uri.parse(GpxParser.readText(parser))
    parser.require(XmlPullParser.END_TAG, namespace, elementName)

    return uri
}

internal fun GpxParser.Companion.readTextAsOffsetTime(
    parser: XmlPullParser,
    elementName: String,
    namespace: String?
): OffsetDateTime {
    parser.require(XmlPullParser.START_TAG, namespace, elementName)
    val offsetDateTime = OffsetDateTime.parse(GpxParser.readText(parser))
    parser.require(XmlPullParser.END_TAG, namespace, elementName)

    return offsetDateTime
}

internal fun GpxParser.Companion.readTextAsLocalTime(
    parser: XmlPullParser,
    elementName: String,
    namespace: String?
): LocalDateTime {
    parser.require(XmlPullParser.START_TAG, namespace, elementName)
    val localDateTime = LocalDateTime.parse(GpxParser.readText(parser), DateTimeFormatter.ISO_DATE_TIME)
    parser.require(XmlPullParser.END_TAG, namespace, elementName)

    return localDateTime
}
