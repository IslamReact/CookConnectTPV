package com.islamelmrabet.cookconnect.ui.screens.onBoardingScreens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.islamelmrabet.cookconnect.R
import com.islamelmrabet.cookconnect.model.localModels.PageModel
import com.islamelmrabet.cookconnect.navigation.Routes
import com.islamelmrabet.cookconnect.tools.BasicButton
import com.islamelmrabet.cookconnect.tools.BasicLongButton
import com.islamelmrabet.cookconnect.viewModel.PreferencesViewModel
import kotlinx.coroutines.launch

/**
 * Composable screen FirstOnBoardingScreen
 *
 * @param navController
 * @param preferencesViewModel
 */
@OptIn(ExperimentalPagerApi::class)
@Composable
fun FirstOnBoardingScreen(
    navController: NavHostController,
    preferencesViewModel: PreferencesViewModel
) {
    val lessRoundedShape = RoundedCornerShape(8.dp)
    val primaryColor = MaterialTheme.colorScheme.primary

    val buttonColors = ButtonDefaults.buttonColors(
        containerColor = primaryColor
    )

    val isFirstTime by preferencesViewModel.firstTime.observeAsState(false)
    val coroutineScope = rememberCoroutineScope()
    val pagerState = rememberPagerState()
    val pages = listOf(
        PageModel(
            "ITemPOS.",
            stringResource(id = R.string.lottie_animation_1),
            "Descubre cómo nuestra aplicación puede facilitar la gestión de tu restaurante. Con nuestra herramienta intuitiva, puedes gestionar mesas, tomar pedidos y supervisar el inventario de manera eficiente. ¡Simplifica tus operaciones diarias y mejora la experiencia de tus clientes!"
        ),
        PageModel(
            "ITemPOS.",
            stringResource(id = R.string.lottie_animation_2),
            "Asigna mesas y toma pedidos en tiempo real con nuestra interfaz fácil de usar. Los camareros pueden agregar pedidos y los cocineros pueden ver los pedidos directamente desde sus dispositivos. Asegúrate de que todos los pedidos se procesen rápidamente y sin errores."
        ),
        PageModel(
            "ITemPOS.",
            stringResource(id = R.string.lottie_animation_3),
            "Genera facturas de manera rápida y lleva un control preciso del inventario. Nuestra aplicación te ayuda a mantener el stock actualizado y te alerta cuando necesites reabastecer. Optimiza tus recursos y maximiza tus ganancias."
        )
    )

    Column(modifier = Modifier.fillMaxSize()) {
        HorizontalPager(
            state = pagerState,
            count = pages.size,
            modifier = Modifier.weight(1f),
            userScrollEnabled = true
        ) { page ->
            OnBoardingPageScreen(page = pages[page])
        }
        Row(
            Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(pagerState.pageCount) { iteration ->
                val color =
                    if (pagerState.currentPage == iteration) MaterialTheme.colorScheme.primary else Color.LightGray
                Box(
                    modifier = Modifier
                        .padding(2.dp)
                        .height(10.dp)
                        .width(50.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(color)
                        .size(16.dp)
                )
            }
        }
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            if (pagerState.currentPage == 1) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    BasicButton(
                        buttonText = "Back",
                        onClick = {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(pagerState.currentPage - 1)
                            }
                        },
                        lessRoundedShape = lessRoundedShape,
                        buttonColors = buttonColors,
                    )
                    BasicButton(
                        buttonText = "Next",
                        onClick = {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(pagerState.currentPage + 1)
                            }
                        },
                        lessRoundedShape = lessRoundedShape,
                        buttonColors = buttonColors,
                    )
                }
            } else {
                if (pagerState.currentPage != 0) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        BasicLongButton(
                            buttonText = "Get Started",
                            onClick = {
                                preferencesViewModel.onUserNameChanged(true)
                                navController.popBackStack()
                                preferencesViewModel.saveUser(isFirstTime)
                                navController.navigate(Routes.WelcomeScreen.route) {
                                    popUpTo(Routes.WelcomeScreen.route)
                                }
                            },
                            lessRoundedShape = lessRoundedShape,
                            buttonColors = buttonColors,
                            enabled = true
                        )
                    }
                }
                if (pagerState.currentPage != pages.size - 1) {
                    BasicLongButton(
                        buttonText = "Next",
                        onClick = {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(pagerState.currentPage + 1)
                            }
                        },
                        lessRoundedShape = lessRoundedShape,
                        buttonColors = buttonColors,
                        enabled = true
                    )
                }
            }
        }
    }
}

/**
 * This method defines how the horizontal Pager content will be displayed.
 *
 * @param page
 */
@Composable
fun OnBoardingPageScreen(page: PageModel) {
    val composition by rememberLottieComposition(LottieCompositionSpec.Url(page.animationFile))

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painterResource(id = R.drawable._cropped),
                contentDescription = "",
            )
            Text(
                text = page.title,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 16.dp)
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        LottieAnimation(
            composition = composition,
            iterations = LottieConstants.IterateForever,
            modifier = Modifier.size(350.dp)
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = page.description,
            textAlign = TextAlign.Justify,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            softWrap = true
        )
    }
}