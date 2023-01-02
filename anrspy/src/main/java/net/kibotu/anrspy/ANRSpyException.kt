package net.kibotu.anrspy

class ANRSpyException(title: String, stacktrace: Array<StackTraceElement>) : Throwable(title) {

    init {
        stackTrace = stacktrace
    }
}