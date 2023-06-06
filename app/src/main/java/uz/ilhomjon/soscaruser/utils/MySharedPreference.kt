import android.content.Context
import android.content.SharedPreferences

object MySharedPreference {
    private const val PREFERENCES_NAME = "my_preferences"
    private lateinit var sharedPreferences: SharedPreferences

    fun init(context: Context) {
        sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
    }

    fun setLogin(username: String) {
        val editor = sharedPreferences.edit()
        editor.putString("login", username)
        editor.apply()
    }

    fun getLogin(): String {
        return sharedPreferences.getString("login", "") ?: ""
    }

    fun setPassword(password:String) {
        val editor = sharedPreferences.edit()
        editor.putString("password", password)
        editor.apply()
    }

    fun getPassword(): String {
        return sharedPreferences.getString("password", "")?:""
    }
}
