package com.leishmaniapp.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.mindrot.jbcrypt.BCrypt

/**
 * Functional wrapper class around password
 * Inlined: Doesn't generate runtime overhead
 *
 * Password is automatically hashed with BCrypt
 * This class is NOT serializable
 */

@Entity
data class PasswordRoom(
    @PrimaryKey(autoGenerate = false)
    val value: String
)


class Password(value: String) {
    val value: String by lazy {
        BCrypt.hashpw(value, salt)
    }

    companion object {
        val salt: String by lazy {
            BCrypt.gensalt()
        }
    }

    override fun toString(): String = """
        Password(value=${value})
    """.trimIndent()

    override fun equals(other: Any?): Boolean =
        (other is Password) && (other.value == this.value)

    override fun hashCode(): Int = value.hashCode()
}