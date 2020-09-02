### Steps

1 Add permission

```xml
<uses-permission android:name="android.permission.INTERNET" />
```

2 Add dependencies. To use `by viewModels()` we need `java 1.8`.

```bash
...

android {
    ...

    compileOptions {
        sourceCompatibility JavaVersion.VERSION_1_8
        targetCompatibility JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }
}

dependencies {
    ...

    // LifeCycle
    implementation 'androidx.lifecycle:lifecycle-common:2.2.0'
    implementation 'androidx.lifecycle:lifecycle-runtime:2.2.0'
    implementation 'android.arch.lifecycle:extensions:2.2.0'
    implementation 'androidx.lifecycle:lifecycle-livedata-ktx:2.2.0'
    implementation 'androidx.lifecycle:lifecycle-viewmodel-ktx:2.2.0'
    implementation "androidx.activity:activity-ktx:1.1.0"

    // Retrofit
    implementation 'com.squareup.retrofit2:retrofit:2.9.0'
    implementation 'com.google.code.gson:gson:2.8.6'
    implementation 'com.squareup.retrofit2:converter-gson:2.9.0'
    implementation 'com.squareup.okhttp3:logging-interceptor:3.12.0'

    // Coroutines
    implementation 'org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.8'
}
```

3 API endpoint:

- Base = https://rickandmortyapi.com/api/
- GET = character

```bash
https://rickandmortyapi.com/api/character/
```

3 Analyze the response from the API. In this case, we are looking for `results`.

```json
{
	info: {
		count: 671,
		pages: 34,
		next: "https://rickandmortyapi.com/api/character/?page=2",
		prev: null
	},
	results: [
	{
		id: 1,
		name: "Rick Sanchez",
		status: "Alive",
		species: "Human",
		type: "",
		gender: "Male",
		origin: {
			name: "Earth (C-137)",
			url: "https://rickandmortyapi.com/api/location/1"
		},
		location: {
			name: "Earth (Replacement Dimension)",
			url: "https://rickandmortyapi.com/api/location/20"
		},
		image: "https://rickandmortyapi.com/api/character/avatar/1.jpeg",
		...	
	}
	{
		id: 2,
		name: "Morty Smith",
		status: "Alive",

		...
```

4 Create `data/RickAndMortyCharacter`

```kotlin
data class RickAndMortyCharacter(
    val name: String,
    val status: String,
    val image: String
)
```

5 Create `data/RickAndMortyCharacters`. Use `results` to consume the correct data.

```kotlin
data class RickAndMortyCharacters(val results: List<RickAndMortyCharacter>)
```

6 Create `data/ApiService`

```kotlin
import retrofit2.http.GET

interface ApiService {
    @GET("character")
    suspend fun getRickAndMortyCharacters(): RickAndMortyCharacters
}
```

7 Create `data/ServiceBuilder`

```kotlin
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ServiceBuilder {
    private const val BASE_URL = "https://rickandmortyapi.com/api/"

    fun getApiService(): ApiService {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(ApiService::class.java)
    }
}
```

8 Create `data/Repository`

```kotlin
class Repository(private val apiService: ApiService) {
    suspend fun getRickAndMortyCharacters() = apiService.getRickAndMortyCharacters()
}
```

9 Create `ui/MainViewModel`

```kotlin
import ...data.Repository
import ...data.RickAndMortyCharacters
import ...data.ServiceBuilder
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import kotlinx.coroutines.Dispatchers.IO

class MainViewModel: ViewModel() {
    private val repository: Repository = Repository(ServiceBuilder.getApiService())
    val data: LiveData<RickAndMortyCharacters> = liveData(IO) {
        val retrievedData = repository.getRickAndMortyCharacters()
        emit(retrievedData)
    }
}
```

10 Move `MainActivity` inside `ui`

11 Update `activity_main.xml`

```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".ui.MainActivity">

    <TextView
        android:id="@+id/textView"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintLeft_toLeftOf="parent"
        app:layout_constraintRight_toRightOf="parent"
        app:layout_constraintTop_toTopOf="parent" />

</androidx.constraintlayout.widget.ConstraintLayout>
```

12 Update `MainActivity`

```kotlin
import ...R
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        startDataObservation()
    }

    private fun startDataObservation() {
        viewModel.data.observe(this, Observer { rickAndMortyCharacters ->
            textView.text = "Fetched: ${rickAndMortyCharacters.results.size} characters"
            // println(rickAndMortyCharacters.results.toTypedArray().contentToString())
        })
    }
}
```
