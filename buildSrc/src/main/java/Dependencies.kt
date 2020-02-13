object Versions {

    const val kotlinLib = "1.3.50"
    const val minSDK = 19
    const val targetSDK = 29
    const val compileSDK = 29
    const val realm = "6.1.0"
    const val retrofit = "2.5.0"
    const val gson = "2.8.5"
    const val gsonConverter = "2.5.0"
    const val httpLogging = "3.12.0"
    const val koin = "2.0.1"
    const val rxJavaAdapter = retrofit
    const val appCompat = "1.0.2"
    const val materialComponents = "1.0.0"
    const val constraintLayout = "1.1.3"
    const val reactiveNetwork= "3.0.3"
    const val recyclerview = "1.1.0"
    const val mockito = "2.8.47"
    const val shimmer = "0.1.0@aar"
    const val picasso = "2.71828"
    const val mockk = "1.9.3"
    const val archCoreTesting = "2.1.0"
}

object LibDeps {

    val retrofit = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
    val gson = "com.google.code.gson:gson:${Versions.gson}"
    val gsonConverter = "com.squareup.retrofit2:converter-gson:${Versions.gsonConverter}"
    val httpLogging = "com.squareup.okhttp3:logging-interceptor:${Versions.httpLogging}"
    val koin = "org.koin:koin-android:${Versions.koin}"
    val rxJavaAdapter = "com.squareup.retrofit2:adapter-rxjava2:${Versions.rxJavaAdapter}"
    val reactiveNetwork = "com.github.pwittchen:reactivenetwork-rx2:${Versions.reactiveNetwork}"
    val mockito = "org.mockito:mockito-core:${Versions.mockito}"
    val mockitoAndroid = "org.mockito:mockito-android:${Versions.mockito}"
    val koinTest = "org.koin:koin-test:${Versions.koin}"
    val mockk = "io.mockk:mockk:${Versions.mockk}"
    val archCoreTesting = "androidx.arch.core:core-testing:${Versions.archCoreTesting}"
}

object AppDeps {

    val koinViewModel = "org.koin:koin-androidx-viewmodel:${Versions.koin}"
    val appCompat = "androidx.appcompat:appcompat:${Versions.appCompat}"
    val materialComponents = "com.google.android.material:material:${Versions.materialComponents}"
    val constraintLayout = "androidx.constraintlayout:constraintlayout:${Versions.constraintLayout}"
    val recyclerView = "androidx.recyclerview:recyclerview:${Versions.recyclerview}"
    val shimmerAnimation = "com.facebook.shimmer:shimmer:${Versions.shimmer}"
    val picasso = "com.squareup.picasso:picasso:${Versions.picasso}"
}