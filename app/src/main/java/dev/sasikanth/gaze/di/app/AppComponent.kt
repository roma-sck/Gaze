package dev.sasikanth.gaze.di.app

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import dev.sasikanth.gaze.ui.MainActivity
import dev.sasikanth.gaze.ui.MainViewModel
import dev.sasikanth.gaze.ui.pages.grid.PicturesGridFragment
import dev.sasikanth.gaze.ui.pages.viewer.PictureInformationSheet
import dev.sasikanth.gaze.ui.pages.viewer.ViewerFragment
import javax.inject.Singleton

@Singleton
@Component(modules = [DataModule::class, DateFormatterModule::class])
interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }

    val mainViewModel: MainViewModel.Factory

    fun inject(mainActivity: MainActivity)
    fun inject(picturesGridFragment: PicturesGridFragment)
    fun inject(viewerFragment: ViewerFragment)
    fun inject(pictureInformationSheet: PictureInformationSheet)
}
