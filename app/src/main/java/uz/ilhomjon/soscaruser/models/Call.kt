package uz.ilhomjon.soscaruser.models

class Call {

    var id: String? = null
    var user_id: String? = null
    var worker_id: String? = null
    var start_time: String? = null
    var end_time: String? = null
    var chaqiruv_turi:String?=null
    var user_location_lat: String? = null
    var user_location_long: String? = null
    var worker_location_lat: String? = null
    var worker_location_long: String? = null
    var ism_familiya:String?=null
    var medical_history:String?=null


    constructor(
        id: String?,
        user_id: String?,
        worker_id: String?,
        start_time: String?,
        end_time: String?,
        chaqiruv_turi: String?,
        user_location_lat: String?,
        user_location_long: String?,
        worker_location_lat: String?,
        worker_location_long: String?,
        ism_familiya: String?,
        medical_history: String?
    ) {
        this.id = id
        this.user_id = user_id
        this.worker_id = worker_id
        this.start_time = start_time
        this.end_time = end_time
        this.chaqiruv_turi = chaqiruv_turi
        this.user_location_lat = user_location_lat
        this.user_location_long = user_location_long
        this.worker_location_lat = worker_location_lat
        this.worker_location_long = worker_location_long
        this.ism_familiya = ism_familiya
        this.medical_history = medical_history
    }

    constructor()

    override fun toString(): String {
        return "Call(id=$id, user_id=$user_id, worker_id=$worker_id, start_time=$start_time, end_time=$end_time, chaqiruv_turi=$chaqiruv_turi, user_location_lat=$user_location_lat, user_location_long=$user_location_long, worker_location_lat=$worker_location_lat, worker_location_long=$worker_location_long, ism_familiya=$ism_familiya, medical_history=$medical_history)"
    }


}