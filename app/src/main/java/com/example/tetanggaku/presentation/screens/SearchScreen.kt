package com.example.tetanggaku.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tetanggaku.presentation.components.BottomNavigationBar
import com.example.tetanggaku.presentation.viewmodels.*

private val TealPrimary = Color(0xFF2D7A7A)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    selectedTab: HomeTab = HomeTab.HOME,
    onBack: () -> Unit = {},
    onJobClick: (String) -> Unit = {},
    onHomeClick: () -> Unit = {},
    onJobsClick: () -> Unit = {},
    onChatClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    viewModel: SearchViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Search Services") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = TealPrimary,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        bottomBar = {
            BottomNavigationBar(
                selectedTab = selectedTab,
                onHomeClick = onHomeClick,
                onJobsClick = onJobsClick,
                onChatClick = onChatClick,
                onProfileClick = onProfileClick
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color.White)
        ) {
            // Search field
            OutlinedTextField(
                value = uiState.searchQuery,
                onValueChange = { viewModel.updateSearchQuery(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                placeholder = { Text("Search for services...") },
                leadingIcon = {
                    Icon(Icons.Filled.Search, "Search")
                },
                trailingIcon = {
                    if (uiState.searchQuery.isNotEmpty()) {
                        IconButton(onClick = { viewModel.clearSearch() }) {
                            Icon(Icons.Filled.Clear, "Clear")
                        }
                    }
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = { viewModel.search() }),
                singleLine = true
            )
            
            // Recent searches
            if (uiState.recentSearches.isNotEmpty() && !uiState.hasSearched) {
                Text(
                    "Recent Searches",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(uiState.recentSearches) { search ->
                        FilterChip(
                            selected = false,
                            onClick = { viewModel.selectRecentSearch(search) },
                            label = { Text(search) }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
            
            // Results or states
            when {
                uiState.isLoading -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = TealPrimary)
                    }
                }
                uiState.error != null -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(uiState.error!!, color = Color.Red)
                    }
                }
                uiState.hasSearched && uiState.searchResults.isEmpty() -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                Icons.Filled.Search,
                                contentDescription = null,
                                modifier = Modifier.size(64.dp),
                                tint = Color.Gray
                            )
                            Spacer(Modifier.height(16.dp))
                            Text("No results found", fontSize = 16.sp, color = Color.Gray)
                        }
                    }
                }
                uiState.searchResults.isNotEmpty() -> {
                    LazyColumn(contentPadding = PaddingValues(16.dp)) {
                        items(uiState.searchResults) { job ->
                            JobResultCard(job, onClick = { onJobClick(job.id) })
                            Spacer(Modifier.height(12.dp))
                        }
                    }
                }
                else -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Search for services", color = Color.Gray)
                    }
                }
            }
        }
    }
}

@Composable
private fun JobResultCard(job: JobItem, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(job.title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Spacer(Modifier.height(4.dp))
            Text(job.description, fontSize = 14.sp, color = Color.Gray, maxLines = 2)
            Spacer(Modifier.height(8.dp))
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(job.price, fontWeight = FontWeight.Bold, color = TealPrimary)
                Row {
                    Icon(Icons.Filled.Star, null, Modifier.size(16.dp), tint = Color(0xFFFBBF24))
                    Text(" ${job.rating}", fontSize = 14.sp)
                }
            }
        }
    }
}
