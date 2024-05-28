package com.example.myapplication

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.myapplication.ui.theme.Purple40

fun isNetworkAvailable(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    val network = connectivityManager.activeNetwork
    val networkCapabilities = connectivityManager.getNetworkCapabilities(network)

    return networkCapabilities != null &&
            (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                    networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR))
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WelcomeScreen() {
    val viewModel : MainViewModel = viewModel()

    //Collecting states from ViewModel
    val searchText by viewModel.searchText.collectAsState()
    val isSearching by viewModel.isSearching.collectAsState()
    val repositoriesList by viewModel.repositories.collectAsState()
    val activity = LocalContext.current as Activity
    val isConnected = isNetworkAvailable(activity)
    val isLoading by viewModel.isLoading.collectAsState()

    val composition by rememberLottieComposition(
        spec = LottieCompositionSpec.RawRes(
            resId = R.raw.nonetwork
        )
    )
    LaunchedEffect(Unit, block = {
        viewModel.fetchSearch()
    })

    Scaffold(
        topBar = {
            SearchBar(
                query = searchText,//text showed on SearchBar
                onQueryChange = viewModel::onSearchTextChange, //update the value of searchText
                onSearch = viewModel::onSearchTextChange, //the callback to be invoked when the input service triggers the ImeAction.Search action
                active = isSearching, //whether the user is searching or not
                onActiveChange = { viewModel.onToogleSearch() }, //the callback to be invoked when this search bar's active state is changed
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                LazyColumn {
                    items(repositoriesList) { country ->
                        Text(
                            text =  country.fullName.trimIndent(),
                            modifier = Modifier.padding(
                                start = 8.dp,
                                top = 4.dp,
                                end = 8.dp,
                                bottom = 4.dp)
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            if (isConnected) {
                if (isLoading){
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth()
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .size(30.dp)
                                .align(Alignment.Center),
                            color = Purple40,
                            strokeWidth = 2.dp
                        )
                    }
                }else{
                    LazyColumn {
                        items(repositoriesList) { item ->

                            Card(
                                modifier = Modifier
                                    .fillMaxWidth() .clickable {

                                    }
                                    .padding(horizontal = 8.dp, vertical = 8.dp) // Add top and bottom padding
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {

                                    Box(
                                        modifier = Modifier
                                            .size(64.dp)
                                            .padding(4.dp)
                                            .clip(CircleShape), // Clip the Box to a circle shape
                                        contentAlignment = Alignment.Center
                                    ) {
                                        AsyncImage(
                                            model = ImageRequest.Builder(LocalContext.current)
                                                .data(item.owner.avatarUrl)
                                                .crossfade(true)
                                                .build(),
                                            contentDescription = null
                                        )
                                    }

                                    Column(modifier = Modifier
                                        .weight(1f)
                                        .padding(3.dp)) { // Add weight to expand the Column
                                        Text(
                                            text = item.fullName.trimIndent(),
                                            style = TextStyle(
                                                fontSize = 18.sp,
                                                fontWeight = FontWeight.Bold
                                            ),
                                            modifier = Modifier.padding(bottom = 4.dp),
                                            maxLines = 1, // Display text on a single line
                                            overflow = TextOverflow.Ellipsis // Add ellipsis if text overflows
                                        )

                                        Row(modifier = Modifier.fillMaxWidth()) {
                                            Text(
                                                text = "Users:",
                                                style = TextStyle(fontSize = 14.sp),
                                                maxLines = 1, // Display text on a single line
                                                overflow = TextOverflow.Ellipsis, // Add ellipsis if text overflows
                                                color = Color.Black
                                            )
                                            Spacer(modifier = Modifier.height(4.dp)) // Add vertical spacing between "Following" and following URL

                                            ClickableText(
                                                text = buildAnnotatedString {
                                                    append(item.url)
                                                },
                                                onClick = {
                                                    // Handle URL click action here
                                                    // For example, open the URL in a browser
                                                    // You can replace this with your desired behavior
                                                    //openUrlInBrowser(item.owner.followingUrl)
                                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(item.owner.followingUrl))
                                                    activity.startActivity(intent)
                                                },
                                                style = TextStyle(
                                                    fontSize = 14.sp,
                                                    color = Color.Blue
                                                ),
                                                modifier = Modifier.padding(bottom = 4.dp),
                                                maxLines = 1, // Display text on a single line
                                                overflow = TextOverflow.Ellipsis // Add ellipsis if text overflows
                                            )
                                        }

                                        Row(modifier = Modifier.fillMaxWidth()) {
                                            Text(
                                                text = "Followers:",
                                                style = TextStyle(fontSize = 14.sp),
                                                maxLines = 1, // Display text on a single line
                                                overflow = TextOverflow.Ellipsis, // Add ellipsis if text overflows
                                                color = Color.Black
                                            )

                                            Spacer(modifier = Modifier.height(4.dp)) // Add vertical spacing between "Following" and following URL

                                            ClickableText(
                                                text = buildAnnotatedString {
                                                    append(item.owner.followersUrl)
                                                },
                                                onClick = {
                                                    // Handle URL click action here
                                                    // For example, open the URL in a browser
                                                    // You can replace this with your desired behavior
                                                    //openUrlInBrowser(item.owner.followingUrl)
                                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(item.owner.followingUrl))
                                                    activity.startActivity(intent)
                                                },
                                                style = TextStyle(
                                                    fontSize = 14.sp,
                                                    color = Color.Blue
                                                ),
                                                modifier = Modifier.padding(bottom = 4.dp),
                                                maxLines = 1, // Display text on a single line
                                                overflow = TextOverflow.Ellipsis // Add ellipsis if text overflows
                                            )
                                        }

                                        Row(modifier = Modifier.fillMaxWidth()) {
                                            Text(
                                                text = "Following:",
                                                style = TextStyle(fontSize = 14.sp),
                                                maxLines = 1, // Display text on a single line
                                                overflow = TextOverflow.Ellipsis, // Add ellipsis if text overflows
                                                color = Color.Black
                                            )

                                            Spacer(modifier = Modifier.height(4.dp)) // Add vertical spacing between "Following" and following URL

                                            ClickableText(
                                                text = buildAnnotatedString {
                                                    append(item.owner.followingUrl)
                                                },
                                                onClick = {
                                                    // Handle URL click action here
                                                    // For example, open the URL in a browser
                                                    // You can replace this with your desired behavior
                                                    //openUrlInBrowser(item.owner.followingUrl)
                                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(item.owner.followingUrl))
                                                    activity.startActivity(intent)
                                                },
                                                style = TextStyle(
                                                    fontSize = 14.sp,
                                                    color = Color.Blue
                                                ),
                                                modifier = Modifier.padding(bottom = 4.dp),
                                                maxLines = 1, // Display text on a single line
                                                overflow = TextOverflow.Ellipsis // Add ellipsis if text overflows
                                            )
                                        }

                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                LottieAnimation(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .padding(start = 15.dp),
                    alignment = Alignment.TopCenter,
                    composition = composition,
                    iterations = 1
                )
            }
        }
    }


}
