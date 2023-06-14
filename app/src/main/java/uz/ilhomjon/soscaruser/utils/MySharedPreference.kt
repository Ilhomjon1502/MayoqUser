import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import uz.ilhomjon.soscaruser.models.User

object MySharedPreference {
    private const val PREFERENCES_NAME = "my_preferences"
    private lateinit var sharedPreferences: SharedPreferences

    fun init(context: Context) {
        sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
    }


    fun setUser(user: User) {
        val editor = sharedPreferences.edit()
        editor.putString("user", userToJson(user))
        editor.apply()
    }

    fun getUser(): User = jsonToUser(sharedPreferences.getString("user", "{}") ?: "")

    fun jsonToUser(string: String): User {
        val gson = Gson()
        val type = object : TypeToken<User>() {}.type
        return gson.fromJson(string, type)
    }

    fun userToJson(user: User): String = Gson().toJson(user)

}
