package org.vlang.debugger.renderers

object VlangCTypeParser {
    /**
     * Example:
     *
     *    "Array_int" -> "int"
     *    "Array_Array_int" -> "Array_int"
     */
    fun parseArrayType(type: String): String {
        if (!type.startsWith("Array_")) {
            return type
        }

        return type.substring(6)
    }

    /**
     * Example:
     *
     *    "Map_int_string" -> "int", "string"
     *    "Map_main__Foo_string" -> "main__Foo", "string"
     */
    fun parseMapType(type: String): Pair<String, String> {
        if (!type.startsWith("Map_")) {
            return type to type
        }

        val tempType = type.replace("__", "$")

        // Map, main, Foo_string
        var parts = tempType.split('_', limit = 3)
        // Foo_string
        val thirdPart = parts[2]
        // mos likely key is enum
        if (thirdPart[0].isUpperCase()) {
            // Foo_string -> Foo, string
            val (remaining, key) = thirdPart.split('_', limit = 2)

            // Map, main_Foo, string
            parts = parts.subList(0, 1) + (parts[2] + "_" + remaining) + key
        }

        parts = parts.map { it.replace("$", "__") }

        // main_Foo, string
        return parts[1] to parts[2]
    }

    /**
     * Example:
     *
     *    "chan_int" -> "int"
     *    "chan_chan_int" -> "chan_int"
     */
    fun parseChannelType(type: String): String {
        if (!type.startsWith("chan_")) {
            return type
        }

        return type.substring(5)
    }

    /**
     * Example:
     *
     *    "int *" -> "int"
     *    "string *" -> "string"
     */
    fun parseCPointerType(type: String): String {
        if (!type.endsWith("*")) {
            return type
        }

        return type.removeSuffix("*").trim()
    }

    fun convertCNameToVName(name: String): String {
        return name.replace("__", ".")
    }
}
