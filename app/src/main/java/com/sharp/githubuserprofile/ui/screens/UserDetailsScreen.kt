package com.sharp.githubuserprofile.ui.screens

import android.annotation.SuppressLint
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.sharp.githubuserprofile.R
import com.sharp.githubuserprofile.data.remote.models.responses.UserInfo
import com.sharp.githubuserprofile.data.remote.models.responses.UserRepos
import com.sharp.githubuserprofile.data.remote.service.ResourceState
import com.sharp.githubuserprofile.ui.components.EmptyState
import com.sharp.githubuserprofile.ui.components.Loader
import com.sharp.githubuserprofile.ui.theme.GithubUserProfileTheme
import com.sharp.githubuserprofile.utils.AppUtilities.sortOptions
import com.sharp.githubuserprofile.viewmodel.UserInfoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun UserDetailsScreen(
    onClickBack: () -> Boolean,
    onRepoClick: (Int) -> Unit = {},
    userInfoViewModel: UserInfoViewModel,
    userId: String
) {

    val userInfo by userInfoViewModel.userInfo.collectAsState()
    val repositoryList by userInfoViewModel.userRepositories.collectAsState()

    val bottomSheet = remember {
        mutableStateOf(false)
    }
    var selectedSortType: Int = -1

    LaunchedEffect(Unit) {
        userInfoViewModel.getUserInfo(userId = userId)
        userInfoViewModel.getUserReposDetails(userId = userId)
    }

    Scaffold(topBar = {
        TopAppBar(
            modifier = Modifier.statusBarsPadding(),
            title = {
                Text(
                    text = stringResource(R.string.repo_detail)
                )
            },
            navigationIcon = {
                IconButton(onClick = { onClickBack() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(id = R.string.back)
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
                titleContentColor = MaterialTheme.colorScheme.onPrimary,
                navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
            )
        )

    }) { contentPadding ->
        val modalBottomSheetState = rememberModalBottomSheetState()
        if (bottomSheet.value) {
            ModalBottomSheet(
                onDismissRequest = { bottomSheet.value = false },
                sheetState = modalBottomSheetState,
                containerColor = MaterialTheme.colorScheme.primary,
                dragHandle = {
                },
                content = {
                    SortBottomLayout(
                        radioButtonOptions = sortOptions,
                        header = "Sort",
                        buttonText = "Apply",
                        onClickApply = {
                            if (it == 0) {
                                selectedSortType = 0
                                userInfoViewModel.sortReposListByForks()
                            } else {
                                selectedSortType = 1
                                userInfoViewModel.sortReposListByLastUpdated()
                            }
                            bottomSheet.value = false
                        },
                        onCloseClick = { bottomSheet.value = false },
                        selectedSortValue = {

                        },
                        previousSelectedSortValue = ""
                    )
                }
            )
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = contentPadding.calculateTopPadding())
        ) {
            Column(
                modifier = Modifier
                    .padding(
                        start = dimensionResource(R.dimen.margin_10),
                        top = dimensionResource(R.dimen.margin_15)
                    )
                    .fillMaxWidth()
            ) {
                when (userInfo) {
                    is ResourceState.Empty -> {}
                    is ResourceState.Loading -> {
                        Loader()
                    }

                    is ResourceState.Success -> {
                        val userInfoData = (userInfo as ResourceState.Success).data
                        UserInfo(userInfo = userInfoData)
                    }

                    is ResourceState.Error -> {}

                }

                when (repositoryList) {
                    is ResourceState.Empty -> {}
                    is ResourceState.Loading -> {
                        Loader()
                    }

                    is ResourceState.Success -> {
                        val repositoryListData = (repositoryList as ResourceState.Success).data
                        if (repositoryListData?.isNotEmpty() == true) {
                            RepositoryList(repositoryListData, onRepoClick, onSortClick = {
                                bottomSheet.value = true
                            })
                        } else {
                            EmptyState()
                        }
                    }

                    is ResourceState.Error -> {
                        EmptyState()
                    }
                }


            }

        }

    }

    /*MainContent(
        onRepoClick = onRepoClick,
        userInfoState = userInfo,
        repositoryListState = repositoryList,
    )*/

}

@Composable
fun SortBottomLayout(
    radioButtonOptions: List<String>,
    buttonText: String = "",
    header: String = "",
    onClickApply: (Int) -> Unit = {},
    onCloseClick: () -> Unit = {},
    selectedSortValue: (String) -> Unit,
    previousSelectedSortValue: String = "Forks"
) {
    /*val indexSelected = radioButtonOptions.indexOf(previousSelectedSortValue)
    val (selectedOption, onOptionSelected) = remember {
        mutableStateOf(
            radioButtonOptions[indexSelected]
        )
    }*/
    var sortIndex:Int = -1
    GithubUserProfileTheme {
        Surface(
            modifier = Modifier
                .fillMaxWidth(),
            color = MaterialTheme.colorScheme.primary,
            shape = RoundedCornerShape(
                topStart = dimensionResource(R.dimen.margin_10),
                topEnd = dimensionResource(R.dimen.margin_10)
            )
        ) {
            Column {
                Row(
                    modifier = Modifier.padding(
                        top = dimensionResource(R.dimen.margin_10),
                        start = dimensionResource(R.dimen.margin_10),
                        end = dimensionResource(R.dimen.margin_10),
                        bottom = dimensionResource(R.dimen.margin_10)
                    )
                ) {
                    Text(
                        text = header,
                        style = MaterialTheme.typography.headlineLarge,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Spacer(Modifier.weight(1f))
                    Text(
                        text = "Close",
                        style = MaterialTheme.typography.headlineLarge,
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.clickable {
                            onCloseClick()
                        }
                    )
                }
                Text(
                    text = radioButtonOptions.get(0),
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.onPrimary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.clickable {
                        sortIndex = 0
                        onClickApply(sortIndex)
                    }
                )
                Text(
                    text = radioButtonOptions.get(1),
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.onPrimary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.clickable {
                        sortIndex = 1
                        onClickApply(sortIndex)
                    }
                )
            }

        }
    }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MainContent(
    onRepoClick: (Int) -> Unit,
    userInfoState: ResourceState<UserInfo?>,
    repositoryListState: ResourceState<List<UserRepos>?>,
) {
    Column(
        modifier = Modifier
            .padding(
                start = dimensionResource(R.dimen.margin_10),
                top = dimensionResource(R.dimen.margin_15)
            )
            .fillMaxWidth()
    ) {
        when (userInfoState) {
            is ResourceState.Empty -> {}
            is ResourceState.Loading -> {
                Loader()
            }

            is ResourceState.Success -> {
                val userInfoData = userInfoState.data
                UserInfo(userInfo = userInfoData)
            }

            is ResourceState.Error -> {}

        }

        when (repositoryListState) {
            is ResourceState.Empty -> {}
            is ResourceState.Loading -> {
                Loader()
            }

            is ResourceState.Success -> {
                val repositoryListData = repositoryListState.data
                if (repositoryListData?.isNotEmpty() == true) {
                    RepositoryList(repositoryListData, onRepoClick, onSortClick = {})
                } else {
                    EmptyState()
                }
            }

            is ResourceState.Error -> {
                EmptyState()
            }
        }
    }
}

@Composable
fun UserInfo(userInfo: UserInfo?) {

    val translationY = remember { Animatable(500f) }

    LaunchedEffect(Unit) {
        translationY.animateTo(
            targetValue = 0f,
            animationSpec = tween(durationMillis = 500)
        )
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer(
                translationY = translationY.value
            ), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = rememberAsyncImagePainter(
                model = ImageRequest.Builder(LocalContext.current).data(userInfo?.avatarUrl)
                    .size(Size.ORIGINAL)
                    .crossfade(true).build()
            ),
            modifier = Modifier
                .padding(top = dimensionResource(id = R.dimen.margin_15))
                .align(Alignment.CenterHorizontally),
            contentDescription = stringResource(id = R.string.label_user_image),
        )
        Text(
            modifier = Modifier
                .padding(top = dimensionResource(id = R.dimen.margin_8))
                .align(Alignment.CenterHorizontally),
            text = ("Name:-" + userInfo?.name) ?: "",
            style = MaterialTheme.typography.bodyLarge,
            color = Color.Black
        )
        Text(
            modifier = Modifier
                .padding(top = dimensionResource(id = R.dimen.margin_8))
                .align(Alignment.CenterHorizontally),
            text = ("Username:-" + userInfo?.login) ?: "",
            style = MaterialTheme.typography.bodyLarge,
            color = Color.Black
        )
        Text(
            modifier = Modifier
                .padding(top = dimensionResource(id = R.dimen.margin_8))
                .align(Alignment.CenterHorizontally),
            text = ("Bio:-" + userInfo?.bio) ?: "",
            style = MaterialTheme.typography.bodyLarge,
            color = Color.Black
        )
        Text(
            modifier = Modifier
                .padding(top = dimensionResource(id = R.dimen.margin_8))
                .align(Alignment.CenterHorizontally),
            text = ("Location:-" + userInfo?.location) ?: "",
            style = MaterialTheme.typography.bodyLarge,
            color = Color.Black
        )
        Text(
            modifier = Modifier
                .padding(top = dimensionResource(id = R.dimen.margin_8))
                .align(Alignment.CenterHorizontally),
            text = ("Followers:-" + userInfo?.followers) ?: "",
            style = MaterialTheme.typography.bodyLarge,
            color = Color.Black
        )
        Text(
            modifier = Modifier
                .padding(top = dimensionResource(id = R.dimen.margin_8))
                .align(Alignment.CenterHorizontally),
            text = ("Repositories:-" + userInfo?.publicRepos) ?: "",
            style = MaterialTheme.typography.bodyLarge,
            color = Color.Black
        )
    }
}