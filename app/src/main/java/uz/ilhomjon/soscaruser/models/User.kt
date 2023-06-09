package uz.ilhomjon.soscaruser.models

class User{
    var login:String?=null
    var parol:String?=null
    var bfd:String?=null
    var address:String?=null
    var history:String?=null
    var phoneNumber:String?=null
    var imageLink:String?=null

    constructor(
        login: String?,
        parol: String?,
        bfd: String?,
        address: String?,
        history: String?,
        phoneNumber: String?,
        imageLink: String?
    ) {
        this.login = login
        this.parol = parol
        this.bfd = bfd
        this.address = address
        this.history = history
        this.phoneNumber = phoneNumber
        this.imageLink = imageLink
    }

    constructor()

    override fun toString(): String {
        return "User(login=$login, parol=$parol, bfd=$bfd, address=$address, history=$history, phoneNumber=$phoneNumber, imageLink=$imageLink)"
    }


}

