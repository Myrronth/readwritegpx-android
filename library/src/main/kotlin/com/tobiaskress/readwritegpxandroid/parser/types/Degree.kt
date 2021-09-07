package com.tobiaskress.readwritegpxandroid.parser.types

import com.tobiaskress.readwritegpxandroid.parser.exceptions.NumberOutOfBoundsException

private const val MIN = 0.0
private const val MAX = 360.0

/**
 * Used for bearing, heading, course. Units are decimal degrees, true (not magnetic).
 *
 * @throws NumberOutOfBoundsException When the latitude is less than 0.0 or greater than 360.0 degrees.
 */
class Degree @Throws(NumberOutOfBoundsException::class) constructor(decimalDegrees: Double) {
    var decimalDegrees: Double = decimalDegrees
        private set(value) {
            if (value < MIN) {
                throw NumberOutOfBoundsException("degree has to be at least $MIN decimal degrees.")
            }

            if (value > MAX) {
                throw NumberOutOfBoundsException("degree has to be at most $MAX decimal degrees.")
            }

            field = value
        }

    //region Mimic data class

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Degree) return false
        if (decimalDegrees != other.decimalDegrees) return false

        return true
    }

    override fun hashCode(): Int {
        return decimalDegrees.hashCode()
    }

    override fun toString(): String = """
    |Degree [
    |  degree: $decimalDegrees
    |]
    """.trimMargin()

    //endregion
}
