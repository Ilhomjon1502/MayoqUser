package uz.ilhomjon.soscaruser.models

class User {
    var login: String? = null
    var parol: String? = null
    var fullName: String? = null
    var bfd: String? = null
    var address: String? = null
    var history: String? = null
    var number: String? = null
    var phoneNumber: String? = null
    var imageLink: String? = null

    constructor(
        login: String?,
        parol: String?,
        fullName: String?,
        bfd: String?,
        address: String?,
        history: String?,
        number: String?,
        phoneNumber: String?,
        imageLink: String?
    ) {
        this.login = login
        this.parol = parol
        this.fullName = fullName
        this.bfd = bfd
        this.address = address
        this.history = history
        this.number = number
        this.phoneNumber = phoneNumber
        this.imageLink = imageLink
    }

    constructor()

    override fun toString(): String {
        return "User(login=$login, parol=$parol, fullName=$fullName, bfd=$bfd, address=$address, history=$history, number=$number, phoneNumber=$phoneNumber, imageLink=$imageLink)"
    }


}

