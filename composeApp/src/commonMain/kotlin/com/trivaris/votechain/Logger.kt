package com.trivaris.votechain

enum class Logger(private val colorCode: String) {
    INFO("\u001B[34m"),    // Blue
    DEBUG("\u001B[35m"),   // Magenta
    PEER("\u001B[36m"),    // Cyan
    SERVER("\u001B[33m"),  // Yellow
    NETWORK("\u001B[32m"), // Green
    CONFIG("\u001B[31m");  // Red

    fun log(vararg messages: Any) {
        val message = messages.joinToString(separator = "\n${this.colored().padEnd(padding)} ")
        log(this, message = message)
    }

    private fun colored(): String =
        "[${this.colorCode}${this.name}\u001B[0m]"

    companion object {
        private val padding = Logger.entries.maxOf { it.name.length } + 12

        fun log(vararg levels: Logger, message: Any) {
            val prefix = if (Config.data.showLogLevels) levels.joinToString(separator = "/") { it.colored() }.padEnd(padding) else ""
            println("$prefix $message")
        }
    }
}