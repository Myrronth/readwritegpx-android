package com.tobiaskress.readwritegpxandroid.parser.types

import com.tobiaskress.readwritegpxandroid.parser.GpxParser
import com.tobiaskress.readwritegpxandroid.parser.helper.readText
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlSerializer

/**
 * A person or organization.
 */
data class Person(
    /**
     * Name of person or organization.
     */
    val name: String? = null,

    /**
     * Email address.
     */
    val email: Email? = null,

    /**
     * Link to Web site or other external information about person.
     */
    val link: Link? = null
) {

    internal fun serialize(
        xmlSerializer: XmlSerializer,
        elementName: String,
        namespace: String?
    ) {
        xmlSerializer.startTag(namespace, elementName)

        name?.takeIf { it.isNotEmpty() }?.let {
            xmlSerializer.startTag(namespace, ELEMENT_NAME)
            xmlSerializer.text(it)
            xmlSerializer.endTag(namespace, ELEMENT_NAME)
        }

        email?.serialize(xmlSerializer, ELEMENT_EMAIL, namespace)
        link?.serialize(xmlSerializer, ELEMENT_LINK, namespace)

        xmlSerializer.endTag(namespace, elementName)
    }

    companion object {

        private const val ELEMENT_NAME = "name"
        private const val ELEMENT_EMAIL = "email"
        private const val ELEMENT_LINK = "link"

        internal fun parse(
            parser: XmlPullParser,
            elementName: String,
            namespace: String?,
            skip: (parser: XmlPullParser) -> Unit,
            loopMustContinue: (next: Int) -> Boolean
        ): Person {
            parser.require(XmlPullParser.START_TAG, namespace, elementName)

            var name: String? = null
            var email: Email? = null
            var link: Link? = null

            while (loopMustContinue(parser.next())) {
                if (parser.eventType != XmlPullParser.START_TAG) {
                    continue
                }

                when (parser.name) {
                    ELEMENT_NAME -> {
                        name = GpxParser.readText(parser, ELEMENT_NAME, namespace)
                    }
                    ELEMENT_EMAIL -> {
                        email = Email.parse(parser, ELEMENT_EMAIL, namespace, skip, loopMustContinue)
                    }
                    ELEMENT_LINK -> {
                        link = Link.parse(parser, ELEMENT_LINK, namespace, skip, loopMustContinue)
                    }
                    else -> skip(parser)
                }
            }

            parser.require(XmlPullParser.END_TAG, namespace, elementName)

            return Person(
                name = name,
                email = email,
                link = link
            )
        }
    }
}
