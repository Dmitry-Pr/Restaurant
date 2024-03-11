package data

import kotlinx.coroutines.Job

object Jobs {
    private val runningJobs = mutableMapOf<Int, Job>()
    fun addJob(id: Int, job: Job) {
        runningJobs[id] = job
    }
    fun stopJob(id: Int) {
        runningJobs[id]?.cancel()
        runningJobs.remove(id)
    }
}