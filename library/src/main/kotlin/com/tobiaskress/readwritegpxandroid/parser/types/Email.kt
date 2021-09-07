package com.tobiaskress.readwritegpxandroid.parser.types

import org.xmlpull.v1.XmlPullParser

/**
 * An email address. Broken into two parts ([id] and [domain]) to help prevent email harvesting.
 */
data class Email(
    /**
     * id half of email address (billgates2004)
     */
    val id: String,

    /**
     * domain half of email address (hotmail.com)
     */
    val domain: String
) {

    /**
     * The email address. Joined [id] and [domain] using "@".
     */
    @Suppress("Unused")
    val emailString
        get() = "$id@$domain"

    companion object {

        private const val ATTRIBUTE_ID = "id"
        private const val ATTRIBUTE_DOMAIN = "domain"

        internal fun read(
            parser: XmlPullParser,
            elementName: String,
            namespace: String?,
            @Suppress("UNUSED_PARAMETER") skip: (parser: XmlPullParser) -> Unit,
            @Suppress("UNUSED_PARAMETER") loopMustContinue: (next: Int) -> Boolean
        ): Email {
            parser.require(XmlPullParser.START_TAG, namespace, elementName)

            val id = parser.getAttributeValue(namespace, ATTRIBUTE_ID)
            val domain = parser.getAttributeValue(namespace, ATTRIBUTE_DOMAIN)

            // Email element is self closed, advance the parser to next event
            parser.next()

            parser.require(XmlPullParser.END_TAG, namespace, elementName)

            return Email(
                id = id,
                domain = domain
            )
        }
    }
}
