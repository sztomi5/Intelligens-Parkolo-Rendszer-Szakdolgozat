import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@Composable
fun ParkingSystem(parkingList: List<Parking>, rows: Int, available: MutableState<Int>, navController: NavHostController) {
    val firstHalf = parkingList.chunked(rows).firstOrNull() ?: emptyList()		//Parkolóhelyek listájának első fele
    val secondHalf = parkingList.chunked(rows).getOrNull(1) ?: emptyList()		//Parkolóhelyek listájának második fele

    val configuration = LocalConfiguration.current
    var orientation = configuration.orientation		//Készülék orientációjának eltároló változó
	
	//Készülék orientációjának változását kezelő függvény
    LaunchedEffect(configuration) {
        snapshotFlow { configuration.orientation }
            .collect { orientation = it }
    }

    when (orientation) {
        Configuration.ORIENTATION_PORTRAIT -> {
            Portrait(firstHalf, secondHalf, rows, available, navController)		//Parkoló portré módban való megjelenítése
        }
        else -> {
            Landscape(firstHalf, secondHalf, rows, available, navController)		//Parkoló fekvő módban való megjelenítése
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Portrait(firstHalf: List<Parking>, secondHalf: List<Parking>, rows: Int, available: MutableState<Int>, navController: NavHostController){
    Column{
		//Felső címsor létrehozása
        TopAppBar(
            title = {
                Text(
                    text = "(" + available.value + "/" + (rows*2)+ ") $PSTitleString",
                    fontWeight = FontWeight.Bold
                )
            },
            navigationIcon = {
                IconButton(onClick = { }) {
                    Icon(
                        imageVector = appBarIcon,
                        contentDescription = "Vissza ikon",
                        modifier = appBarIconModifier
                    )
                }
            },
            colors = TopAppBarDefaults.smallTopAppBarColors(
                containerColor = appBarContainerColor,
                navigationIconContentColor = appBarNavIconColor,
                titleContentColor = appBarTextColor
            )
        )
		
		//Parkoló létrehozása
        LazyColumn(modifier = Modifier.padding(5.dp)) {
            item {
                LazyRow {
                    item {
						//Áruházat jelölő cella létrehozása
                        Column(
                            modifier = Modifier
                                .width(125.dp)
                                .height((92 * rows).dp)
                                .background(color = Color.Cyan),
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = storeString,
                                modifier = Modifier.align(CenterHorizontally),
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                            )
                        }

                        Spacer(modifier = Modifier.width(spacerSize))
						
						//Parkolóhelyek első oszlopának létrehozása
                        Column {
                            for (parkingSlot in firstHalf.asReversed()) {
                                Column(
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        text = parkingSlot.name!!,
                                        modifier = Modifier
                                            .background(
                                                color = if (parkingSlot.state!!) nonoccupiedBackgroundColor
                                                        else occupiedBackgroundColor
                                            )
                                            .border(width = 1.dp, color = Color.Black)
                                            .width(lyingSlotWidth)
                                            .height(lyingSlotHeight)
                                            .padding(10.dp),
                                        textAlign = TextAlign.Center,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (parkingSlot.state) nonoccupiedTextColor
                                                else occupiedTextColor
                                    )
                                    Spacer(modifier = Modifier.height(spacerSize))
                                }
                            }
                        }

                        Spacer(modifier = Modifier.width(spacerSize))
						
						//Parkolót kettészelő út létrehozása
                        Column(
                            modifier = Modifier
                                .width(125.dp)
                                .height((92 * rows).dp)
                                .background(color = Color.Gray)
                                .padding(10.dp),
                            verticalArrangement = Arrangement.Bottom
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.arrowup),
                                contentDescription = "Nyíl ikon",
                                modifier = Modifier
                                    .size(45.dp)
                                    .align(CenterHorizontally)
                            )
                        }

                        Spacer(modifier = Modifier.width(spacerSize))
						
						//Parkolóhelyek második oszlopának létrehozása
                        Column {
                            for (parkingSlot in secondHalf.asReversed()) {
                                Column(
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        text = parkingSlot.name!!,
                                        modifier = Modifier
                                            .background(
                                                color = if (parkingSlot.state!!) nonoccupiedBackgroundColor
                                                        else occupiedBackgroundColor
                                            )
                                            .border(width = 1.dp, color = Color.Black)
                                            .width(lyingSlotWidth)
                                            .height(lyingSlotHeight)
                                            .padding(10.dp),
                                        textAlign = TextAlign.Center,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (parkingSlot.state) nonoccupiedTextColor
                                                else occupiedTextColor
                                    )
                                    Spacer(modifier = Modifier.height(spacerSize))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Landscape(firstHalf: List<Parking>, secondHalf: List<Parking>, rows: Int, available: MutableState<Int>, navController: NavHostController){
    Column {
		//Felső címsor létrehozása
        TopAppBar(
            title = {
                Text(
                    text = "(" + available.value + "/" + (rows*2)+ ") $PSTitleString",
                    fontWeight = FontWeight.Bold
                )
            },
            navigationIcon = {
                IconButton(onClick = { }) {
                    Icon(
                        imageVector = appBarIcon,
                        contentDescription = "Vissza ikon",
                        modifier = appBarIconModifier
                    )
                }
            },
            colors = TopAppBarDefaults.smallTopAppBarColors(
                containerColor = appBarContainerColor,
                navigationIconContentColor = appBarNavIconColor,
                titleContentColor = appBarTextColor
            )
        )

        LazyRow(modifier = Modifier.padding(5.dp)) {
            item {
                LazyColumn {
                    item {
						//Áruházat jelölő cella létrehozása
                        Row(
                            modifier = Modifier
                                .width((102 * rows).dp)
                                .height(100.dp)
                                .background(color = Color.Cyan),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = storeString,
                                modifier = Modifier
                                    .rotate(0F)
                                    .align(CenterVertically),
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                            )
                        }

                        Spacer(modifier = Modifier.height(spacerSize))

						//Parkolóhelyek első sorának létrehozása
                        Row {
                            for (parkingSlot in firstHalf) {
                                Row(
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        text = parkingSlot.name!!,
                                        modifier = Modifier
                                            .background(
                                                color = if (parkingSlot.state!!) nonoccupiedBackgroundColor
                                                        else occupiedBackgroundColor
                                            )
                                            .border(width = 1.dp, color = Color.Black)
                                            .width(standingSlotWidth)
                                            .height(standingSlotHeight)
                                            .padding(5.dp),
                                        textAlign = TextAlign.Center,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (parkingSlot.state) nonoccupiedTextColor
                                                else occupiedTextColor
                                    )
                                    Spacer(modifier = Modifier.width(spacerSize))
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(spacerSize))
						
						//Parkolót kettészelő út létrehozása
                        Row(
                            modifier = Modifier
                                .width((102 * rows).dp)
                                .height(125.dp)
                                .background(color = Color.Gray)
                                .padding(15.dp, 5.dp)
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.arrowright),
                                contentDescription = "Nyíl ikon",
                                modifier = Modifier
                                    .size(45.dp)
                                    .align(CenterVertically)
                            )
                        }

                        Spacer(modifier = Modifier.height(spacerSize))
						
						//Parkolóhelyek második sorának létrehozása
                        Row {
                            for (parkingSlot in secondHalf) {
                                Row(
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        text = parkingSlot.name!!,
                                        modifier = Modifier
                                            .background(
                                                color = if (parkingSlot.state!!) nonoccupiedBackgroundColor
                                                        else occupiedBackgroundColor
                                            )
                                            .border(width = 1.dp, color = Color.Black)
                                            .width(standingSlotWidth)
                                            .height(standingSlotHeight)
                                            .padding(5.dp),
                                        textAlign = TextAlign.Center,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (parkingSlot.state) nonoccupiedTextColor
                                                else occupiedTextColor
                                    )
                                    Spacer(modifier = Modifier.width(spacerSize))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}