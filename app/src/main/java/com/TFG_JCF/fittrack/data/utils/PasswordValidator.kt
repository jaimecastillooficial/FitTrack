package com.TFG_JCF.fittrack.data.utils


object PasswordValidator {

    fun validate(password: String): String? {
        if (password.length < 8) {
            return "La contraseña debe tener al menos 8 caracteres"
        }

        if (password.contains(" ")) {
            return "La contraseña no puede contener espacios"
        }

        if (!password.any { it.isUpperCase() }) {
            return "La contraseña debe incluir al menos una mayúscula"
        }

        if (!password.any { it.isLowerCase() }) {
            return "La contraseña debe incluir al menos una minúscula"
        }

        if (!password.any { it.isDigit() }) {
            return "La contraseña debe incluir al menos un número"
        }

        if (!password.any { !it.isLetterOrDigit() }) {
            return "La contraseña debe incluir al menos un carácter especial"
        }

        return null
    }
}