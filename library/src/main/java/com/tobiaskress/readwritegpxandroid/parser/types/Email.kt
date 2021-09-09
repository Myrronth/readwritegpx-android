package com.tobiaskress.readwritegpxandroid.parser.types

import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlSerializer

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

    internal fun serialize(
        xmlSerializer: XmlSerializer,
        elementName: String,
        namespace: String?
    ) {
        xmlSerializer.startTag(namespace, elementName)
        xmlSerializer.attribute(namespace, ATTRIBUTE_ID, id)
        xmlSerializer.attribute(namespace, ATTRIBUTE_DOMAIN, domain)
        xmlSerializer.endTag(namespace, elementName)
    }

    companion object {

        private const val ATTRIBUTE_ID = "id"
        private const val ATTRIBUTE_DOMAIN = "domain"

        internal fun parse(
            parser: XmlPullParser,
            elementName: String,
            namespace: String?,
            @Suppress("UNUSED_PARAMETER") skip: (parser: XmlPullParser) -> Unit,
            @Suppress("UNUSED_PARAMETER") loopMustContinue: (next: Int) -> Boolean
        ): Email {
            parser.require(XmlPullParser.START_TAG, namespace, elementName)

            val id = parser.getAttributeValue(namespace, ATTRIBUTE_ID)
            val domain = parser.getAttributeValue(namespace, ATTRIBUTE_DOMAIN)

            val nullStrings: MutableList<String> = mutableListOf()
            if (id == null) nullStrings.add(ATTRIBUTE_ID)
            if (domain == null) nullStrings.add(ATTRIBUTE_DOMAIN)

            @Suppress("ComplexCondition")
            if (nullStrings.size > 0) {
                throw NullPointerException(
                    "Attributes ${
                        nullStrings.joinToString(", ", prefix = "'", postfix = "'")
                    } have to be set for '$elementName'."
                )
            }

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
