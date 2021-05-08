package dev.sasikanth.gaze.di.misc

import android.app.Activity
import androidx.fragment.app.Fragment
import dev.sasikanth.gaze.di.app.AppComponent

interface ComponentProvider {
    val component: AppComponent
}

val Activity.injector get() = (application as ComponentProvider).component
val Fragment.injector get() = (requireActivity().application as ComponentProvider).component
