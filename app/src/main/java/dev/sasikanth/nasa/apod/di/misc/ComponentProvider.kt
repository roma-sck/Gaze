package dev.sasikanth.nasa.apod.di.misc

import android.app.Activity
import androidx.fragment.app.Fragment
import dev.sasikanth.nasa.apod.di.app.AppComponent

interface ComponentProvider {
    val component: AppComponent
}

val Activity.injector get() = (application as ComponentProvider).component
val Fragment.injector get() = (requireActivity().application as ComponentProvider).component
