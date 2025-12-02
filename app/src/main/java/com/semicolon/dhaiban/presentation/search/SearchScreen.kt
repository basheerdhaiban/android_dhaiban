package com.semicolon.dhaiban.presentation.search

import ProductScreen
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.semicolon.dhaiban.R
import com.semicolon.dhaiban.designSystem.DhaibanTheme
import com.semicolon.dhaiban.designSystem.Theme
import com.semicolon.dhaiban.designSystem.composables.AppBarWithIcon
import com.semicolon.dhaiban.designSystem.composables.CoilImage
import com.semicolon.dhaiban.designSystem.composables.modifier.shimmerEffect
import com.semicolon.dhaiban.presentation.app.AppScreenModel
import com.semicolon.dhaiban.presentation.home.ProductUiState
import com.semicolon.dhaiban.presentation.notification.NotificationScreen
import com.semicolon.dhaiban.presentation.utils.Constants
import kotlinx.coroutines.flow.collectLatest

import android.Manifest
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.compose.material3.Button
import androidx.core.content.FileProvider
import androidx.lifecycle.compose.LocalLifecycleOwner
import coil.compose.rememberAsyncImagePainter
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import org.koin.ext.clearQuotes
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.UUID

class SearchScreen : Screen {
    @Composable
    override fun Content() {
        val screenModel = getScreenModel<SearchScreenModel>()
        val appScreenModel = getScreenModel<AppScreenModel>()
        val state: SearchScreenUiState by screenModel.state.collectAsState()
        val navigator = LocalNavigator.currentOrThrow
        val searchProducts by screenModel.searchProducts.collectAsState()

        LaunchedEffect(Unit) {
            appScreenModel.setCurrentScreen(Constants.SEARCH_SCREEN)
            screenModel.effect.collectLatest { effect ->
                when (effect) {
                    SearchScreenUiEffect.OnNavigateBack -> if (navigator.canPop) navigator.pop()
                    is SearchScreenUiEffect.OnNavigateToProductDetailsScreen ->
                        navigator.push(ProductScreen(effect.productId))

                    SearchScreenUiEffect.OnNavigateToNotificationScreen ->
                        navigator.push(NotificationScreen())

                    SearchScreenUiEffect.OnNavigateBack -> {}
                    SearchScreenUiEffect.OnNavigateToNotificationScreen -> {}
                }
            }
        }
        SearchScreenContent(
            searchProducts = searchProducts,
            state = state,
            screenModel = screenModel
        )
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun SearchScreenContent(
    searchProducts: List<ProductUiState>,
    state: SearchScreenUiState,
    screenModel: SearchScreenInteractionListener
) {
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }

    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
    ) { uri ->
        imageUri = uri
        val inputStream = imageUri?.let { context.contentResolver.openInputStream(it) }
        val imageByteArray = inputStream?.readBytes()
        screenModel.searchForProductByImage(imageByteArray)
//        screenModel.searchForProduct(imageByteArray = imageByteArray, query = null)
    }

    // For camera permission
    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)
    // For gallery permission (READ_EXTERNAL_STORAGE)
    val galleryPermissionState = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        rememberPermissionState(Manifest.permission.READ_MEDIA_IMAGES) // For Android 13+ (API 33+)
    } else {
        rememberPermissionState(Manifest.permission.READ_EXTERNAL_STORAGE)
    }

    // Create a file to save the camera image
    val photoFile by remember { mutableStateOf(createImageFile(context)) }
    val photoUri by remember { mutableStateOf(getFileUri(context = context, file = photoFile)) }

    // Launcher for camera
    val singleTakePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success ->
            if (success) {
                imageUri = photoUri
                imageUri?.let {
                    val compressedByteArray = compressImageFromUri(context, it)
                    screenModel.searchForProductByImage(compressedByteArray)
                }

            }
        }
    )


    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AppBarWithIcon(
            title = Theme.strings.search,
            onClickUpButton = screenModel::onClickBackButton,
            onClickNotification = screenModel::onClickNotification
        )
        Column(modifier = Modifier.fillMaxWidth()) {
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(56.dp),
                value = state.queryValue,
                onValueChange = {
                    screenModel.onChangeSearchValue(it)
                },
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Theme.colors.primary,
                    unfocusedIndicatorColor = Theme.colors.primary,
                    unfocusedContainerColor = Theme.colors.background,
                    disabledContainerColor = Theme.colors.background,
                    disabledIndicatorColor = Theme.colors.primary,
                    focusedContainerColor = Theme.colors.splashBackground,
                    cursorColor = Theme.colors.mediumBrown,
                    selectionColors = TextSelectionColors(
                        handleColor = Theme.colors.mediumBrown,
                        backgroundColor = Theme.colors.darkBeige
                    )
                ),
                shape = RoundedCornerShape(16.dp),
                placeholder = {
                    Text(Theme.strings.search, style = Theme.typography.body,
                        color = Theme.colors.black38)
                },
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.search_icon),
                        contentDescription = "search icon",
                        tint = LocalContentColor.current.copy(alpha = 0.5f)
                    )
                },
                trailingIcon = {
                    IconButton(
                        onClick = {
                            state.queryValue.clearQuotes()
                            showDialog = true
                        }) {
                        Icon(
                            painter = painterResource(id = R.drawable.search_by_image), // Add an image icon
                            modifier = Modifier.size(25.dp),
                            contentDescription = "Select Image"
                        )
                    }
                },
            )

            Spacer(modifier = Modifier.height(16.dp))
            AnimatedContent(targetState = state.isLoading, label = "") { loading ->
                Log.e("onSearchForProductSuccess", "searchResult: ${searchProducts.size}")
                Log.e("onSearchForProductSuccess", "LoadingStatus: ${loading}")
                if (loading) {
                    SearchLoadingContent()
                } else {
                    AnimatedContent(
                        targetState = searchProducts.isNotEmpty(),
                        label = ""
                    ) { isThereProducts ->
                        if (isThereProducts) {
                            if (searchProducts.size == 0) return@AnimatedContent
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .fillMaxHeight()
                            ) {
                                items(items = searchProducts, key = { it.id }) { product ->
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(60.dp)
                                            .clickable {
                                                screenModel.onClickProduct(product.id)
                                            }, contentAlignment = Alignment.Center
                                    ) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                modifier = Modifier.padding(start = 16.dp),
                                                text = product.title
                                            )
                                            CoilImage(
                                                modifier = Modifier
                                                    .padding(end = 16.dp)
                                                    .size(50.dp),
                                                url = product.imageUrl,
                                                contentDescription = "Product Image",
                                                shape = CircleShape,
                                                scaleType = ContentScale.FillBounds
                                            )
                                        }
                                    }
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(1.dp)
                                            .background(Theme.colors.silver.copy(alpha = 0.5f))
                                    )
                                }
                            }
                        } else if (!isThereProducts&&state.queryValue!="") {
                            Box(
                                modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
                            ) {
                                Column {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.Center
                                    ) {
                                        Image(
                                            painter = painterResource(id = R.drawable.empty_search),
                                            contentDescription = "No Data Icon",
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(6.dp))

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.Center
                                    ) {
                                        //TODO add text
                                        Text(
                                            text = "No Results Found",
                                            style = Theme.typography.body.copy(
                                                fontWeight = FontWeight(
                                                    400
                                                )
                                            ),
                                            textAlign = TextAlign.Start,
                                            color = Theme.colors.dimGray
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.Center
                                    ) {
                                        Text(
                                            text = "No Results Found. Please try again",
                                            style = Theme.typography.body.copy(
                                                fontWeight = FontWeight(
                                                    400
                                                )
                                            ),
                                            textAlign = TextAlign.Start,
                                            color = Theme.colors.dimGray
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(16.dp))

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.Center
                                    ) {
//                                        ButtonForEmptyState("Go back"){
//                                            screenModel.onClickBackButton()
//                                        }


                                    }
                                }
                            }
                        }
                    }
                }

            }
        }

        CameraGalleryDialog(
            showDialog = showDialog,
            onDismiss = { showDialog = false },
            onGalleryClick = {
                singlePhotoPickerLauncher.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
            },
            onCameraClick = {
                if (cameraPermissionState.status.isGranted) {
                    singleTakePictureLauncher.launch(photoUri)
                } else {
                    cameraPermissionState.launchPermissionRequest()
                }
            }
        )
//        TakePictureWithPermission()

    }
}

@Composable
fun TakePictureWithPermission() {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var showCamera by remember { mutableStateOf(false) }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) showCamera = true
        else Toast.makeText(context, "Camera permission denied", Toast.LENGTH_SHORT).show()
    }

    // Create temp file to store the image
    val photoUri = remember {
        val file = File(
            context.cacheDir,
            "${UUID.randomUUID()}.jpg"
        )
        FileProvider.getUriForFile(
            context,
            "${context.packageName}.provider",
            file
        )
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            imageUri = photoUri
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(onClick = {
            permissionLauncher.launch(Manifest.permission.CAMERA)
        }) {
            Text("Take Picture")
        }

        Spacer(modifier = Modifier.height(24.dp))

        imageUri?.let {
            Text("Image saved at:\n$it", textAlign = TextAlign.Center)
            Image(
                painter = rememberAsyncImagePainter(it),
                contentDescription = "Captured Image",
                modifier = Modifier.size(250.dp)
            )
        }
    }

    LaunchedEffect(showCamera) {
        if (showCamera) {
            showCamera = false
            cameraLauncher.launch(photoUri)
        }
    }
}

@Composable
private fun SearchLoadingContent() {
    Column {
        repeat(15) {
            Row(
                modifier = Modifier
                    .height(60.dp)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Box(
                    modifier = Modifier
                        .width(40.dp)
                        .height(4.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .shimmerEffect()
                )
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .shimmerEffect()
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SearchScreenContentPreview() {
    val dummyListener = object : SearchScreenInteractionListener {
        override fun onClickBackButton() {}
        override fun onChangeSearchValue(queryText: String) {}
        override fun onClickProduct(productId: Int) {}
        override fun onClickNotification() {}
        override fun searchForProductByImage(byteArray: ByteArray?) {}
        override fun searchForProduct(query: String?, imageByteArray: ByteArray?) {}
    }
    DhaibanTheme {
        SearchScreenContent(
            searchProducts = emptyList(),
            state = SearchScreenUiState(),
            screenModel = dummyListener
        )
    }
}


fun compressImageFromUri(context: Context, uri: Uri, quality: Int = 50): ByteArray {
    val inputStream = context.contentResolver.openInputStream(uri)
    val originalBitmap = BitmapFactory.decodeStream(inputStream)
    inputStream?.close()

    val outputStream = ByteArrayOutputStream()
    originalBitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
    return outputStream.toByteArray()
}
// Helper function to create a URI for the camera to save the image
private fun getFileUri(context: Context, file: File): Uri {
//    val file = createImageFile(context)
    val uri = FileProvider.getUriForFile(
        context,
        "${context.packageName}.provider", // Must match manifest
        file
    )
    return uri
//    val imageFile = File.createTempFile(
//        "temp_image",
//        ".jpg",
//        context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
//    )
//    return FileProvider.getUriForFile(
//        context,
//        "${context.packageName}.provider",
//        imageFile
//    )
}
// Helper function to create a file for the camera image
private fun createImageFile(context: Context): File {
    val fileName = File(
        context.cacheDir,
        "${UUID.randomUUID()}.jpg"
    )
    return fileName

//    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
//    val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
//    return File.createTempFile(
//        "JPEG_${timeStamp}_",
//        ".jpg",
//        storageDir
//    )
}