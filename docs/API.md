# API Documentation - Consumer Integration Guide

This guide explains how to integrate and consume the Template Feature module API in your Android applications.

## 📦 Installation

### Gradle Dependency

Add the API module to your app's `build.gradle.kts`:

```kotlin
dependencies {
    implementation("com.example.modules:template-feature-api:1.0.15")
}
```

### Version Catalog (Recommended)

In your `gradle/libs.versions.toml`:

```toml
[versions]
templateFeature = "1.0.15"

[libraries]
template-feature-api = { group = "com.example.modules", name = "template-feature-api", version.ref = "templateFeature" }
```

In your app's `build.gradle.kts`:
```kotlin
dependencies {
    implementation(libs.template.feature.api)
}
```

## 🚀 Quick Integration

### Basic Setup

1. **Add Hilt Support** (if not already configured):
```kotlin
// app/build.gradle.kts
plugins {
    id("dagger.hilt.android.plugin")
    id("kotlin-kapt")
}

dependencies {
    implementation("com.google.dagger:hilt-android:2.48")
    kapt("com.google.dagger:hilt-compiler:2.48")
}
```

2. **Initialize in Application Class**:
```kotlin
@HiltAndroidApp
class MyApplication : Application()
```

3. **Add to Activity**:
```kotlin
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Your activity code
    }
}
```

### Navigation Integration

#### Simple Integration

```kotlin
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.modules.template_feature.api.navigation.TemplateFeatureNavigation

@Composable
fun MyApp() {
    val navController = rememberNavController()
    
    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            HomeScreen(
                onNavigateToTemplateFeature = {
                    navController.navigate("template_feature")
                }
            )
        }
        
        composable("template_feature") {
            TemplateFeatureNavigation(navController = navController)
        }
    }
}
```

#### Advanced Integration with Deep Links

```kotlin
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    
    NavHost(
        navController = navController,
        startDestination = "main"
    ) {
        // Your app's main navigation
        composable("main") { 
            MainScreen(
                onOpenTemplateFeatures = { 
                    navController.navigate("features") 
                }
            )
        }
        
        // Template feature navigation
        composable(
            route = "features",
            deepLinks = listOf(navDeepLink { uriPattern = "myapp://features" })
        ) {
            TemplateFeatureNavigation(navController = navController)
        }
        
        // Direct navigation to specific feature
        composable(
            route = "features/{featureId}",
            arguments = listOf(navArgument("featureId") { type = NavType.StringType }),
            deepLinks = listOf(navDeepLink { uriPattern = "myapp://features/{featureId}" })
        ) { backStackEntry ->
            val featureId = backStackEntry.arguments?.getString("featureId")
            if (featureId != null) {
                TemplateFeatureNavigation(navController = navController)
                // Navigation will handle the deep link automatically
            }
        }
    }
}
```

## 🔌 API Reference

### TemplateFeatureModuleApi Interface

The main API interface provides access to all module functionality:

```kotlin
interface TemplateFeatureModuleApi {
    
    /**
     * Get the main navigation composable for the template feature module
     * Provides a complete navigation experience with screens for listing,
     * creating, and editing template features.
     * 
     * @param navController Optional navigation controller. If null, creates internal controller.
     */
    @Composable
    fun TemplateFeatureNavigation(navController: NavHostController? = null)

    /**
     * Check if the module is initialized and ready to use
     * Useful for conditional UI rendering or feature flags
     * 
     * @return true if module is ready, false otherwise
     */
    fun isInitialized(): Boolean
}
```

### Accessing the API

#### Method 1: Direct Import (Recommended)

```kotlin
import com.example.modules.template_feature.api.navigation.TemplateFeatureNavigation

@Composable
fun MyScreen() {
    TemplateFeatureNavigation()
}
```

#### Method 2: Via Builder Pattern

```kotlin
import com.example.modules.template_feature.api.TemplateFeatureModuleBuilder

@Composable
fun MyScreen() {
    val templateFeatureApi = TemplateFeatureModuleBuilder.create()
    
    if (templateFeatureApi.isInitialized()) {
        templateFeatureApi.TemplateFeatureNavigation()
    } else {
        // Show loading or fallback UI
        CircularProgressIndicator()
    }
}
```

#### Method 3: Dependency Injection

```kotlin
@HiltViewModel
class MyViewModel @Inject constructor(
    private val templateFeatureApi: TemplateFeatureModuleApi
) : ViewModel() {
    
    fun checkModuleStatus(): Boolean {
        return templateFeatureApi.isInitialized()
    }
}

@Composable
fun MyScreen(viewModel: MyViewModel = hiltViewModel()) {
    if (viewModel.checkModuleStatus()) {
        // Module is ready
    }
}
```

## 🎨 UI Integration Examples

### Basic Feature List Integration

```kotlin
@Composable
fun DashboardScreen() {
    Column {
        Text(
            text = "My App Dashboard",
            style = MaterialTheme.typography.headlineLarge
        )
        
        Card(
            onClick = { /* Navigate to template features */ },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Icon(
                    imageVector = Icons.Default.List,
                    contentDescription = null
                )
                Text(
                    text = "Template Features",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "Manage your template features",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
```

### Bottom Navigation Integration

```kotlin
@Composable
fun MainScreen() {
    var selectedItem by remember { mutableStateOf(0) }
    val items = listOf("Home", "Features", "Settings")
    val navController = rememberNavController()
    
    Scaffold(
        bottomBar = {
            NavigationBar {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = { 
                            when (index) {
                                0 -> Icon(Icons.Default.Home, contentDescription = null)
                                1 -> Icon(Icons.Default.List, contentDescription = null)
                                2 -> Icon(Icons.Default.Settings, contentDescription = null)
                            }
                        },
                        label = { Text(item) },
                        selected = selectedItem == index,
                        onClick = { 
                            selectedItem = index
                            when (index) {
                                0 -> navController.navigate("home")
                                1 -> navController.navigate("features")
                                2 -> navController.navigate("settings")
                            }
                        }
                    )
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(paddingValues)
        ) {
            composable("home") { HomeContent() }
            composable("features") { 
                TemplateFeatureNavigation(navController = navController)
            }
            composable("settings") { SettingsContent() }
        }
    }
}
```

### Drawer Navigation Integration

```kotlin
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppWithDrawer() {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val navController = rememberNavController()
    
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Text(
                    text = "My App",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.titleLarge
                )
                HorizontalDivider()
                
                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = null) },
                    label = { Text("Home") },
                    selected = false,
                    onClick = { 
                        navController.navigate("home")
                        scope.launch { drawerState.close() }
                    }
                )
                
                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.List, contentDescription = null) },
                    label = { Text("Template Features") },
                    selected = false,
                    onClick = { 
                        navController.navigate("features")
                        scope.launch { drawerState.close() }
                    }
                )
            }
        }
    ) {
        NavHost(
            navController = navController,
            startDestination = "home"
        ) {
            composable("home") { 
                HomeScreen(
                    onOpenDrawer = { scope.launch { drawerState.open() } }
                )
            }
            composable("features") { 
                TemplateFeatureNavigation(navController = navController)
            }
        }
    }
}
```

## 🔧 Configuration Options

### Theme Integration

The module uses Material 3 theming and will automatically adapt to your app's theme:

```kotlin
@Composable
fun MyApp() {
    MyAppTheme {  // Your app's theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            AppNavigation()  // Template feature will use your theme
        }
    }
}
```

### Custom Theme Override

If you need to customize the module's appearance:

```kotlin
@Composable
fun CustomThemedTemplateFeature() {
    MaterialTheme(
        colorScheme = MyCustomColorScheme,
        typography = MyCustomTypography
    ) {
        TemplateFeatureNavigation()
    }
}
```

## 🚨 Error Handling

### Module Initialization Check

```kotlin
@Composable
fun SafeTemplateFeatureIntegration() {
    val moduleApi = TemplateFeatureModuleBuilder.create()
    
    when {
        moduleApi.isInitialized() -> {
            TemplateFeatureNavigation()
        }
        else -> {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = "Module not available",
                    tint = MaterialTheme.colorScheme.error
                )
                Text(
                    text = "Template Features unavailable",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.error
                )
                Text(
                    text = "Please try again later",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}
```

### Network Error Handling

The module handles network errors internally, but you can provide fallback UI:

```kotlin
@Composable
fun RobustIntegration() {
    var hasError by remember { mutableStateOf(false) }
    
    if (hasError) {
        ErrorScreen(
            message = "Unable to load template features",
            onRetry = { hasError = false }
        )
    } else {
        TemplateFeatureNavigation()
    }
}
```

## 📱 Platform Considerations

### Minimum Requirements

- **Android SDK**: API 24 (Android 7.0)
- **Compile SDK**: API 35
- **Kotlin**: 1.9.0+
- **Compose BOM**: 2024.02.00+

### ProGuard/R8 Configuration

The module includes consumer ProGuard rules automatically. No additional configuration needed.

### Permissions

No special permissions required. The module handles its own local storage and network requests.

## 🔄 Lifecycle Integration

### Activity Lifecycle

```kotlin
@AndroidEntryPoint
class FeatureActivity : ComponentActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        setContent {
            MyAppTheme {
                TemplateFeatureNavigation()
            }
        }
    }
}
```

### Fragment Integration

```kotlin
@AndroidEntryPoint
class FeatureFragment : Fragment() {
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                MyAppTheme {
                    TemplateFeatureNavigation()
                }
            }
        }
    }
}
```

## 📊 Performance Considerations

### Lazy Loading

The module uses lazy loading internally. Features are loaded on-demand:

```kotlin
@Composable
fun OptimizedIntegration() {
    // Module initializes only when first accessed
    LaunchedEffect(Unit) {
        // Pre-warm the module if needed
        TemplateFeatureModuleBuilder.create().isInitialized()
    }
    
    TemplateFeatureNavigation()
}
```

### Memory Management

The module automatically manages its resources. No manual cleanup required.

## 🧪 Testing Integration

### Unit Testing

Test your integration code:

```kotlin
@Test
fun `should initialize template feature module`() {
    val moduleApi = TemplateFeatureModuleBuilder.create()
    assertThat(moduleApi.isInitialized()).isTrue()
}
```

### UI Testing

Test navigation integration:

```kotlin
@Test
fun `should navigate to template features`() {
    composeTestRule.setContent {
        TestNavigation()
    }
    
    composeTestRule.onNodeWithText("Template Features").performClick()
    composeTestRule.onNodeWithText("Features").assertIsDisplayed()
}
```

## 📝 Migration Guide

### From v1.0.14 to v1.0.15

No breaking changes. Update dependency version:

```kotlin
implementation("com.example.modules:template-feature-api:1.0.15")
```

### Version Compatibility

| API Version | Min Android SDK | Compose BOM |
|-------------|----------------|-------------|
| 1.0.15      | API 24         | 2024.02.00  |
| 1.0.14      | API 24         | 2024.02.00  |
| 1.0.13      | API 24         | 2024.02.00  |

## 🆘 Troubleshooting

### Common Issues

#### Module Not Initializing
```kotlin
// Check if Hilt is properly configured
@HiltAndroidApp
class MyApplication : Application()

// Ensure activity is annotated
@AndroidEntryPoint
class MainActivity : ComponentActivity()
```

#### Navigation Issues
```kotlin
// Ensure NavController is passed correctly
TemplateFeatureNavigation(navController = navController)

// Or let module create its own controller
TemplateFeatureNavigation()
```

#### Theme Issues
```kotlin
// Wrap in your app theme
MyAppTheme {
    TemplateFeatureNavigation()
}
```

### Getting Help

1. Check the [Architecture Documentation](ARCHITECTURE.md)
2. Review the [Development Guide](DEVELOPMENT.md) 
3. Look at sample integration in `sample-app/`
4. File issues with complete reproduction steps

## 📄 API Changelog

### v1.0.15
- Added ProGuard consumer rules for implementation hiding
- Improved error handling in navigation
- Updated dependencies

### v1.0.14
- Initial stable API release
- Complete Clean Architecture implementation
- Comprehensive unit test coverage

---

This documentation provides everything needed to integrate the Template Feature module into your Android application. For advanced use cases and development details, see the [Development Guide](DEVELOPMENT.md) and [Architecture Documentation](ARCHITECTURE.md).