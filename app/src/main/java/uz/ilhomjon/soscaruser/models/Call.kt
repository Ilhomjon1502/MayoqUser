package uz.ilhomjon.soscaruser.models

class Call {

    var id: String? = null
    var user_id: String? = null
    var worker_id: String? = null
    var start_time: String? = null
    var end_time: String? = null
    var user_location: String? = null
    var worker_location: String? = null

    constructor(
        id: String?,
        user_id: String?,
        worker_id: String?,
        start_time: String?,
        end_time: String?,
        user_location: String?,
        worker_location: String?
    ) {
        this.id = id
        this.user_id = user_id
        this.worker_id = worker_id
        this.start_time = start_time
        this.end_time = end_time
        this.user_location = user_location
        this.worker_location = worker_location
    }

    constructor()

    override fun toString(): String {
        return "Call(id=$id, user_id=$user_id, worker_id=$worker_id, start_time=$start_time, end_time=$end_time, user_location=$user_location, worker_location=$worker_location)"
    }


}