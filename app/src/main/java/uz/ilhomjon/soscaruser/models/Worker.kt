package uz.ilhomjon.soscaruser.models

class Worker {

    var id:String?=null
    var login:String?=null
    var parol:String?=null
    var name:String?=null
    var surname:String?=null
    var position:String?=null
    var phone_number:String?=null
    var workplace:String?=null

    constructor(
        id: String?,
        login: String?,
        parol: String?,
        name: String?,
        surname: String?,
        position: String?,
        phone_number: String?,
        workplace: String?
    ) {
        this.id = id
        this.login = login
        this.parol = parol
        this.name = name
        this.surname = surname
        this.position = position
        this.phone_number = phone_number
        this.workplace = workplace
    }

    constructor()

    override fun toString(): String {
        return "Worker(id=$id, login=$login, parol=$parol, name=$name, surname=$surname, position=$position, phone_number=$phone_number, workplace=$workplace)"
    }


}