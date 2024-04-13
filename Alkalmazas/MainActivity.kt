import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.compose.rememberNavController
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import hu.parkolorendszer.parkolas.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                val navController = rememberNavController()		//Felületek közötti navigációs változó
                val con = remember { mutableStateOf(true) }		//Kapcsolódás állapotát eltároló változó

				//1500 ms-os késleltetés
                Handler(Looper.getMainLooper()).postDelayed({
                    val connectedRef = FirebaseDatabase.getInstance(/*Adatbázishoz vezető URL*/).getReference(".info/connected")		//Adatbázissal való kapcsolat státusza

					//Adatbázishoz kapcsolódás ellenőrzése
                    connectedRef.addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            val connected = dataSnapshot.getValue(Boolean::class.java) ?: false
                            con.value = connected
                            if (connected) {
                                println("Sikeres kapcsolódás a Firebase Realtime Database-hez")
                            } else {
                                println("Nem sikerült kapcsolódni a Firebase Realtime Database-hez")
                           }
                        }

                        override fun onCancelled(databaseError: DatabaseError) {
                            println("Hiba történt a kapcsolódás során: " + databaseError.message)
                        }
                    })
                }, 1500)

                if(con.value) Navigation(navController)		//Navigációs függvény meghívása
                else ConFailed()		//Kapcsolódás sikertelenségét jelző függvény
            }
        }
    }
}
