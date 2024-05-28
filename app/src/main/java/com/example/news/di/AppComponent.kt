package com.example.news.di

import android.content.Context
import com.example.news.common.di.DIComponent
import com.example.news.data.service.AuthService
import com.example.news.data.service.CloudStorageService
import com.example.news.data.service.FirebaseService
import com.example.news.data.service.FirestoreService
import com.example.news.data.service.NewsService
import com.example.news.domain.repository.AuthRepository
import com.example.news.ui.auth.AuthActivity
import com.example.news.ui.auth.login.LoginFragment
import com.example.news.ui.auth.reset_password.ForgetPasswordError
import com.example.news.ui.auth.reset_password.ForgetPasswordFragment
import com.example.news.ui.auth.signup.SignUpFragment
import com.example.news.ui.common.BaseFragment
import com.example.news.ui.home.HomeActivity
import com.example.news.ui.home.details.HomeDetailsFragment
import com.example.news.ui.home.favourite.FavouriteFragment
import com.example.news.ui.home.main.HomeFragment
import com.example.news.ui.home.show_image.HomeShowImageFragment
import com.example.news.ui.profile.ProfileActivity
import com.example.news.ui.profile.edit.ProfileEditFragment
import com.example.news.ui.profile.languages.LanguagesFragment
import com.example.news.ui.profile.main.ProfileFragment
import com.example.news.ui.profile.sources.SourcesFragment
import com.example.news.ui.splash.SplashActivity
import com.example.news.ui.verification.VerificationActivity
import dagger.BindsInstance
import dagger.Component
import java.time.format.SignStyle
import javax.inject.Singleton

@Component(
    modules = [
        AppModule::class
    ]
)

@Singleton
interface AppComponent : DIComponent {

    fun inject(activity: AuthActivity)

    fun inject(activity: HomeActivity)

    fun inject(activity: ProfileActivity)

    fun inject(activity: SplashActivity)

    fun inject(activity: VerificationActivity)

    fun inject(fragment: LoginFragment)

    fun inject(fragment: ForgetPasswordFragment)

    fun inject(fragment: SignUpFragment)

    fun inject(fragment: HomeDetailsFragment)

    fun inject(fragment: FavouriteFragment)

    fun inject(fragment: HomeFragment)

    fun inject(fragment: HomeShowImageFragment)

    fun inject(fragment: ProfileEditFragment)

    fun inject(fragment: LanguagesFragment)

    fun inject(fragment: ProfileFragment)

    fun inject(fragment: SourcesFragment)

    fun authService(): AuthService

    fun storageService() : CloudStorageService

    fun firestoreService() : FirebaseService

    fun newsService() : NewsService

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance context: Context
        ): AppComponent
    }
}

object AppComponentHolder {
    private var component: AppComponent? = null

    fun get(): AppComponent {
        return component ?: throw IllegalStateException("Component must be set")
    }

    fun set(component: AppComponent?) {
        this.component = component
    }

    fun build(context: Context): AppComponent {
        return DaggerAppComponent.factory().create(context)
    }
}