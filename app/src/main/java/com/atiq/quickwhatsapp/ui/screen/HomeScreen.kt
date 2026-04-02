package com.atiq.quickwhatsapp.ui.screen

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.atiq.quickwhatsapp.R
import com.atiq.quickwhatsapp.ui.theme.WhatsAppGreen
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.atiq.quickwhatsapp.ui.component.CountryCodePicker
import com.atiq.quickwhatsapp.ui.component.MessageTemplateRow
import com.atiq.quickwhatsapp.ui.component.PhoneInputField
import com.atiq.quickwhatsapp.ui.component.RecentNumbersList
import com.atiq.quickwhatsapp.ui.component.WaBusinessToggle
import com.atiq.quickwhatsapp.utils.PhoneNumberUtils
import com.atiq.quickwhatsapp.utils.WhatsAppLauncher
import com.atiq.quickwhatsapp.viewmodel.HomeError
import com.atiq.quickwhatsapp.viewmodel.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context: Context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.error) {
        uiState.error?.let { error ->
            val message = when (error) {
                HomeError.InvalidNumber -> "Invalid phone number"
                HomeError.WhatsAppNotInstalled -> "WhatsApp is not installed"
                HomeError.BusinessNotInstalled -> "WhatsApp Business is not installed"
            }
            snackbarHostState.showSnackbar(message)
            viewModel.onDismissError()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(R.drawable.ic_logo),
                            contentDescription = null,
                            modifier = Modifier.size(32.dp),
                            tint = Color.Unspecified
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "QuickWhatsApp",
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = WhatsAppGreen
                )
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
                .imePadding(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { Spacer(modifier = Modifier.height(4.dp)) }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CountryCodePicker(
                        selected = uiState.selectedCountry,
                        onCountrySelected = viewModel::onCountrySelected,
                        modifier = Modifier.width(140.dp)
                    )
                    PhoneInputField(
                        value = uiState.phoneInput,
                        onValueChange = viewModel::onPhoneInputChanged,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            item {
                WaBusinessToggle(
                    isBusinessMode = uiState.isBusinessMode,
                    onToggle = viewModel::onBusinessModeToggled
                )
            }

            item {
                MessageTemplateRow(
                    templates = uiState.templates,
                    selected = uiState.selectedTemplate,
                    onSelect = viewModel::onTemplateSelected,
                    onDelete = viewModel::onDeleteTemplate
                )
            }

            item {
                Button(
                    onClick = { viewModel.onOpenChat(context) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = WhatsAppGreen)
                ) {
                    Icon(Icons.Default.Send, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Open WhatsApp Chat",
                        modifier = Modifier.padding(vertical = 4.dp),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }

            if (uiState.recents.isNotEmpty()) {
                item {
                    Text("Recents", style = MaterialTheme.typography.titleMedium)
                }
                item {
                    RecentNumbersList(
                        recents = uiState.recents,
                        onOpen = { recent ->
                            val url = PhoneNumberUtils.buildWaUrl(recent.dialCode, recent.number)
                            WhatsAppLauncher.open(context, url, uiState.isBusinessMode)
                        },
                        onDelete = viewModel::onDeleteRecent
                    )
                }
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }
        }
    }

    SnackbarHost(
        hostState = snackbarHostState,
        modifier = Modifier
            .align(Alignment.TopCenter)
            .padding(top = 8.dp)
    )
    }
}