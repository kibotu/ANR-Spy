package pk.farimarwat.anrspy

interface ANRSpyListener {

    fun onWait(ms: Long) {}

    fun onAnrStackTrace(stackstrace: Array<StackTraceElement>) {}

    fun onAnrDetected(details: String, stackTrace: Array<StackTraceElement>) {}
}