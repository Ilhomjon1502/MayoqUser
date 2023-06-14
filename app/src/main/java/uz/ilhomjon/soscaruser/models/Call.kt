package uz.ilhomjon.soscaruser.models

class Call {

    var id: String? = null
    var user_id: String? = null
    var worker_id: String? = null
    var start_time: String? = null
    var end_time: String? = null

    constructor(
        id: String?,
        user_id: String?,
        worker_id: String?,
        start_time: String?,
        end_time: String?
    ) {
        this.id = id
        this.user_id = user_id
        this.worker_id = worker_id
        this.start_time = start_time
        this.end_time = end_time
    }

    constructor()

    override fun toString(): String {
        return "Call(id=$id, user_id=$user_id, worker_id=$worker_id, start_time=$start_time, end_time=$end_time)"
    }


}