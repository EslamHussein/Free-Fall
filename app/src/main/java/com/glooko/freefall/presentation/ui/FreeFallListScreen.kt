package com.glooko.freefall.presentation.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.glooko.freefall.R
import com.glooko.freefall.base.ui.EmptyItem
import com.glooko.freefall.base.ui.ErrorItem
import com.glooko.freefall.base.ui.LoadingItem
import com.glooko.freefall.data.localdatasource.FreeFallEntity
import com.glooko.freefall.presentation.FreeFallViewModel
import kotlinx.coroutines.flow.Flow
import org.koin.androidx.compose.getViewModel

@Composable
fun FreeFallScreen(viewModel: FreeFallViewModel = getViewModel()) {
    Scaffold(topBar = {
        AppBar()
    }) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 4.dp),
            color = MaterialTheme.colors.background,
        ) {
            FreeFallList(freeFallList = viewModel.allEvents)
            AlertDialogSample(viewModel.state.value.showAlert) {
                viewModel.dismissAlert()
            }
        }
    }
}

@Composable
fun AppBar() {
    TopAppBar(
        title = { Text(text = stringResource(id = R.string.app_name)) },
    )
}

@Composable
fun FreeFallList(freeFallList: Flow<PagingData<FreeFallEntity>>) {
    val lazyMovieItems: LazyPagingItems<FreeFallEntity> = freeFallList.collectAsLazyPagingItems()

    LazyColumn {
        if (lazyMovieItems.itemCount > 0) {
            items(lazyMovieItems) { freeFall ->
                freeFall?.let {
                    FreeFallItem(it)
                }
            }
        } else {
            item {
                EmptyItem(message = stringResource(id = R.string.empty_msg))
            }
        }

        lazyMovieItems.apply {
            when {
                loadState.refresh is LoadState.Loading -> {
                    item { LoadingItem() }
                }
                loadState.refresh is
                        LoadState.Error || loadState.append is
                        LoadState.Error -> {
                    item {
                        ErrorItem(message = stringResource(id = R.string.unknown_error))
                    }
                }
            }
        }
    }
}

@Composable
fun FreeFallItem(freeFall: FreeFallEntity) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp), elevation = 4.dp
    ) {
        Row(modifier = Modifier.padding(4.dp), verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(R.drawable.freefall),
                contentDescription = "",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(64.dp)
                    .padding(4.dp)
                    .clip(CircleShape)
                    .border(
                        width = 2.dp, Color.Gray,
                        CircleShape
                    )
            )
            Text(modifier = Modifier.padding(4.dp), text = freeFall.timeStamp)
        }
    }
}

@Composable
fun AlertDialogSample(show: Boolean, onDismiss: () -> Unit) {
    MaterialTheme {
        Column {
            if (show) {
                AlertDialog(
                    onDismissRequest = {
                        onDismiss()
                    },
                    title = {
                        Text(text = stringResource(R.string.alert_title_msg_free_fall))
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                onDismiss()
                            }) {
                            Text(stringResource(id = R.string.dismiss))
                        }
                    }
                )
            }
        }

    }
}