import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController

@Composable
fun Query(navController: NavHostController) {
    val databaseRef = FirebaseDatabase.getInstance(/*Adatbázishoz vezető URL*/).getReference("Parking system/Slots")		//Parkolóhelyek azonosítóinak és állapotainak elérési útvonala az adatbázisban
    val availableRef = FirebaseDatabase.getInstance(/*Adatbázishoz vezető URL*/).getReference("Parking system/Available")		//Elérhető parkolóhelyek elérési útvonala az adatbázisban
    val parkingList = remember { mutableStateOf(emptyList<Parking>()) }		//Parkolóhelyek adatait tároló lista
    val available = remember { mutableIntStateOf(0) }		//Elérhető parkolóhelyek számát tároló változó
    val cols = 2

	//Parkolóhelyek azonosítóinak és állapotaiknak lekérdezése az adatbázisból
    val eventListener = object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            val updatedParkingList = mutableListOf<Parking>()		//Helyi lista létrehozása

            for (snapshot in dataSnapshot.children) {		//Parkolóhelyek adatinak lekérdezése ciklus segítségével
                val id = snapshot.key
                val data = snapshot.getValue(Boolean::class.java)
                val parkingSlot = Parking(id, data)
                updatedParkingList.add(parkingSlot)		//Parkolóhelyek adatainak eltárolása helyi listában
            }

            parkingList.value = updatedParkingList		//Lekérdezett adatok eltárolása listában
        }

        override fun onCancelled(error: DatabaseError) {
            println("Database Error: ${error.message}")
        }
    }
    databaseRef.addValueEventListener(eventListener)
	
	//Szabad parkolóhelyek számának lekérdezése az adatbázisból
    val availableEventListener = object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            val data = dataSnapshot.getValue(Int::class.java)
            available.intValue = data!!		//Lekérdezett érték eltárolása változóban
        }

        override fun onCancelled(error: DatabaseError) {
            println("Database Error: ${error.message}")
        }
    }
    availableRef.addValueEventListener(availableEventListener)

    val rows = parkingList.value.size/cols

    if(parkingList.value.isNotEmpty()) ParkingSystem(parkingList.value, rows, available, navController)		//Parkolót kirajzoló függvény meghívása
}
