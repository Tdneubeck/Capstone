package edu.missouri.collegerewards.util

import android.os.Bundle
import androidx.navigation.NavOptions
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update


object Navigator {

    val navigateState = MutableStateFlow(NavigationState())

    fun navigate(type: NavigationType, destination: Int?, bundle: Bundle? = null, navOptions: NavOptions? = null) {

        if (type == NavigationType.Auth) {
            navigateState.update { currentState ->
                currentState.copy(mainDestination = destination)
            }
        } else if (type == NavigationType.Content) {
            navigateState.update { currentState ->
                currentState.copy(
                    contentDestination = destination,
                    bundle = bundle,
                    navOptions = navOptions
                )
            }
        }
    }

    fun popBackStack(value: Boolean? = true) {
        navigateState.update { currentState ->
            currentState.copy(
                doPop = value,
            )
        }
    }

}

enum class NavigationType {
    Auth, Content
}

data class NavigationState(
    var mainDestination: Int? = null,
    var contentDestination: Int? = null,
    var bundle: Bundle? = null,
    var doPop: Boolean? = null,
    var navOptions: NavOptions? = null
)