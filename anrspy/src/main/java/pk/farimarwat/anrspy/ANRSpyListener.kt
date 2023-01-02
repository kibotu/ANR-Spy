package pk.farimarwat.anrspy

interface ANRSpyListener {

    fun onWait(ms: Long) = Unit

    fun onAnrStackTrace(stacksTrace: Array<StackTraceElement>) = Unit

    fun onAnrDetected(details: String, stackTrace: Array<StackTraceElement>) = Unit
}