package com.himanshu.cryptotrackerapp.ui.cryptoList

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.himanshu.cryptoapp.models.CryptoCurrencyListItem
import com.himanshu.cryptotrackerapp.ui.theme.CryptoTrackerAppTheme
import dagger.hilt.android.AndroidEntryPoint

const val TAG = "MainActivity"

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CryptoTrackerAppTheme {
                val viewModel: CryptoListViewModel = hiltViewModel()
                val itemList by viewModel.list.collectAsState()
                val isLoading by viewModel.loading.collectAsState()

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = { TopBar() }) { innerPadding ->

                    val listState = rememberLazyListState()

                    if (isLoading && itemList.isEmpty()) {

                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    } else {

                        Column(
                            modifier = Modifier.padding(innerPadding),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            SearchBox(viewModel)

                            LazyColumn(
                                modifier = Modifier.padding(top = 5.dp), state = listState
                            ) {

                                items(itemList) { item ->
                                    CryptoCard(item) {
                                        Log.i(TAG, "Item clicked ${it?.name}")
                                    }
                                }
                            }
                        }
                    }

                    val isBottomReached by remember() {
                        derivedStateOf {
                            val lastVisibleItemIndex =
                                listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index
                            lastVisibleItemIndex == (itemList.size - 1)
                        }
                    }
                    LaunchedEffect(isBottomReached) {

                        val lastVisibleItemIndex =
                            listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index

                        if (lastVisibleItemIndex == itemList.size - 1 || itemList.isEmpty()) {
                            Log.i(TAG, "Last index reached")
                            viewModel.getCryptocurrencies()
                        }
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalCoilApi::class)
@Composable
fun CryptoCard(
    item: CryptoCurrencyListItem? = null, onCardClick: (CryptoCurrencyListItem?) -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable { onCardClick(item) }
            .padding(horizontal = 15.dp, vertical = 10.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(Color.Gray),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Absolute.SpaceBetween,
    ) {

        val url = "https://s2.coinmarketcap.com/static/img/coins/128x128/${item!!.id}.png"
        Image(
            painter = rememberImagePainter(data = url),
            modifier = Modifier
                .padding(start = 20.dp)
                .size(60.dp)
                .clip(CircleShape),
            contentDescription = null
        )

        Column(
            modifier = Modifier
                .wrapContentHeight()
                .padding(horizontal = 20.dp, vertical = 5.dp),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextCard(item.name)
            TextCard(item.symbol)

        }

        Column(
            modifier = Modifier
                .wrapContentHeight()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.End
        ) {
            TextCard(item.quotes[0].name)
            TextCard(item.quotes[0].percentChange24h.toString().substring(0, 6))
        }
    }
}

@Composable
fun TextCard(text: String) {
    Text(
        modifier = Modifier.padding(vertical = 10.dp),
        text = text,
        color = Color.Black,
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold
    )
}

@Preview
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar() {

    TopAppBar(title = {
        Text(
            text = "Cryptocurrencies List",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold
        )
    })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBox(viewModel: CryptoListViewModel) {

    SearchBar(
        modifier = Modifier.wrapContentHeight(),
        query = viewModel.searchQuery.collectAsState().value,
        onQueryChange = { viewModel.updateSearchQuery(it) },
        onSearch = {},
        active = false,
        onActiveChange = {},
        placeholder = {
            Text(text = "Search Cryptocurrencies")
        },
        trailingIcon = {
            Icon(
                    modifier = Modifier.size(40.dp),
                imageVector = Icons.Filled.Search,
                tint = MaterialTheme.colorScheme.onSurface,
                contentDescription = null
            )
        },
        content = {}
    )
}
