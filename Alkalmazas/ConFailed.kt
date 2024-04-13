import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ConFailed(){
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(15.dp),
            shape = RoundedCornerShape(20.dp),
            elevation = 5.dp
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Text(
                    text = "Az adatbázishoz kapcsolódás sikertelen",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Kérjük ellenőrizze az internetkapcsolatát!",
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "A felület automatikusan frissülni fog."
                )
            }
        }
    }
}