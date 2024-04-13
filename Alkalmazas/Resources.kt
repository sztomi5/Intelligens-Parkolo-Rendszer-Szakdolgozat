import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

//Navigáció útvonalak
val parkingSystemRouteString: String = "parkingSystem"

//Fejléc szövegek
val PSTitleString: String = "Parkoló rendszer"

//Oldal szövegek
val storeString: String = "Áruház"

//Fejléc stílusok
val appBarIcon: ImageVector = Icons.Filled.KeyboardArrowUp
val appBarIconModifier: Modifier = Modifier.rotate(270F).size(30.dp)
val appBarContainerColor: Color = Color.Black
val appBarNavIconColor: Color = Color.White
val appBarTextColor: Color = Color.White

//Parkoló hely stílusok
val occupiedBackgroundColor: Color = Color.Red
val nonoccupiedBackgroundColor: Color = Color.Green
val occupiedTextColor: Color = Color.White
val nonoccupiedTextColor: Color = Color.Black
val standingSlotHeight: Dp = 175.dp
val standingSlotWidth: Dp = 100.dp
val lyingSlotHeight: Dp = 90.dp
val lyingSlotWidth: Dp = 160.dp
val spacerSize: Dp = 2.dp