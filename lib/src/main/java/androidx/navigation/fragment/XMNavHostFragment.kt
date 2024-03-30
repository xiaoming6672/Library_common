package androidx.navigation.fragment

import android.content.Context
import android.content.ContextWrapper
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedDispatcherOwner
import androidx.annotation.CallSuper
import androidx.annotation.NavigationRes
import androidx.annotation.RestrictTo
import androidx.core.content.res.use
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.navigation.NavController
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.Navigation
import androidx.navigation.Navigator
import androidx.navigation.plusAssign

/**
 * 复制NavHostFragment功能，将创建FragmentNavigator的时候替换成自己的
 *
 * @author ZhangXiaoMing 2024-03-29 23:11 周五
 */
open class XMNavHostFragment : Fragment(), NavHost {

    private var navHostController: NavHostController? = null
    private var isPrimaryBeforeOnCreate: Boolean? = null
    private var viewParent: View? = null

    // State that will be saved and restored
    private var graphId = 0
    private var defaultNavHost = false

    /**
     * The [navigation controller][NavController] for this navigation host.
     * This method will return null until this host fragment's [onCreate] has
     * been called and it has had an opportunity to restore from a previous
     * instance state.
     *
     * @return this host's navigation controller
     * @throws IllegalStateException if called before [onCreate]
     */
    final override val navController: NavController
        get() {
            checkNotNull(navHostController) { "NavController is not available before onCreate()" }
            return navHostController as NavHostController
        }

    @CallSuper
    override fun onAttach(context: Context) {
        super.onAttach(context)
        // TODO This feature should probably be a first-class feature of the Fragment system,
        // but it can stay here until we can add the necessary attr resources to
        // the fragment lib.
        if (defaultNavHost) {
            parentFragmentManager.beginTransaction()
                .setPrimaryNavigationFragment(this)
                .commit()
        }
    }

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        var context = requireContext()
        navHostController = NavHostController(context)
        navHostController!!.setLifecycleOwner(this)
        while (context is ContextWrapper) {
            if (context is OnBackPressedDispatcherOwner) {
                navHostController!!.setOnBackPressedDispatcher(
                    (context as OnBackPressedDispatcherOwner).onBackPressedDispatcher
                )
                // Otherwise, caller must register a dispatcher on the controller explicitly
                // by overriding onCreateNavHostController()
                break
            }
            context = context.baseContext
        }
        // Set the default state - this will be updated whenever
        // onPrimaryNavigationFragmentChanged() is called
        navHostController!!.enableOnBackPressed(
            isPrimaryBeforeOnCreate != null && isPrimaryBeforeOnCreate as Boolean
        )
        isPrimaryBeforeOnCreate = null
        navHostController!!.setViewModelStore(viewModelStore)
        onCreateNavHostController(navHostController!!)
        var navState: Bundle? = null
        if (savedInstanceState != null) {
            navState = savedInstanceState.getBundle(KEY_NAV_CONTROLLER_STATE)
            if (savedInstanceState.getBoolean(KEY_DEFAULT_NAV_HOST, false)) {
                defaultNavHost = true
                parentFragmentManager.beginTransaction()
                    .setPrimaryNavigationFragment(this)
                    .commit()
            }
            graphId = savedInstanceState.getInt(KEY_GRAPH_ID)
        }
        if (navState != null) {
            // Navigation controller state overrides arguments
            navHostController!!.restoreState(navState)
        }
        if (graphId != 0) {
            // Set from onInflate()
            navHostController!!.setGraph(graphId)
        } else {
            // See if it was set by NavHostFragment.create()
            val args = arguments
            val graphId = args?.getInt(KEY_GRAPH_ID) ?: 0
            val startDestinationArgs = args?.getBundle(KEY_START_DESTINATION_ARGS)
            if (graphId != 0) {
                navHostController!!.setGraph(graphId, startDestinationArgs)
            }
        }

        // We purposefully run this last as this will trigger the onCreate() of
        // child fragments, which may be relying on having the NavController already
        // created and having its state restored by that point.
        super.onCreate(savedInstanceState)
    }

    /**
     * Callback for when the [NavHostController] is created. If you support
     * any custom destination types, their [Navigator] should be added here to
     * ensure it is available before the navigation graph is inflated / set.
     *
     * This provides direct access to the host specific
     * methods available on [NavHostController] such as
     * [NavHostController.setOnBackPressedDispatcher].
     *
     * By default, this adds a [DialogFragmentNavigator] and
     * [FragmentNavigator].
     *
     * This is only called once in [onCreate] and should not be called directly
     * by subclasses.
     *
     * @param navHostController The newly created [NavHostController] that will
     *     be returned by [getNavController] after
     */
    @Suppress("DEPRECATION")
    @CallSuper
    protected open fun onCreateNavHostController(navHostController: NavHostController) {
        onCreateNavController(navHostController)
    }

    /**
     * Callback for when the [NavController][getNavController] is created. If
     * you support any custom destination types, their [Navigator] should be
     * added here to ensure it is available before the navigation graph is
     * inflated / set.
     *
     * By default, this adds a [DialogFragmentNavigator] and
     * [FragmentNavigator].
     *
     * This is only called once in [onCreate] and should not be called directly
     * by subclasses.
     *
     * @param navController The newly created [NavController].
     */
    @Suppress("DEPRECATION")
    @CallSuper
    @Deprecated(
        """Override {@link #onCreateNavHostController(NavHostController)} to gain
      access to the full {@link NavHostController} that is created by this NavHostFragment."""
    )
    protected open fun onCreateNavController(navController: NavController) {
        navController.navigatorProvider +=
            DialogFragmentNavigator(requireContext(), childFragmentManager)
        navController.navigatorProvider.addNavigator(createFragmentNavigator())
    }

    @CallSuper
    override fun onPrimaryNavigationFragmentChanged(isPrimaryNavigationFragment: Boolean) {
        if (navHostController != null) {
            navHostController?.enableOnBackPressed(isPrimaryNavigationFragment)
        } else {
            isPrimaryBeforeOnCreate = isPrimaryNavigationFragment
        }
    }

    /**
     * Create the FragmentNavigator that this NavHostFragment will use. By
     * default, this uses [FragmentNavigator], which replaces the entire
     * contents of the NavHostFragment.
     *
     * This is only called once in [onCreate] and should not be called directly
     * by subclasses.
     *
     * @return a new instance of a FragmentNavigator
     */
    @Deprecated("Use {@link #onCreateNavController(NavController)}")
    protected open fun createFragmentNavigator(): Navigator<out FragmentNavigator.Destination> {
        return XMFragmentNavigator(requireContext(), childFragmentManager, containerId)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val containerView = FragmentContainerView(inflater.context)
        // When added via XML, this has no effect (since this FragmentContainerView is given the ID
        // automatically), but this ensures that the View exists as part of this Fragment's View
        // hierarchy in cases where the NavHostFragment is added programmatically as is required
        // for child fragment transactions
        containerView.id = containerId
        return containerView
    }

    /**
     * We specifically can't use [View.NO_ID] as the container ID (as we use
     * [androidx.fragment.app.FragmentTransaction.add] under the hood), so we
     * need to make sure we return a valid ID when asked for the container ID.
     *
     * @return a valid ID to be used to contain child fragments
     */
    private val containerId: Int
        get() {
            val id = id
            return if (id != 0 && id != View.NO_ID) {
                id
            } else R.id.nav_host_fragment_container
            // Fallback to using our own ID if this Fragment wasn't added via
            // add(containerViewId, Fragment)
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        check(view is ViewGroup) { "created host view $view is not a ViewGroup" }
        Navigation.setViewNavController(view, navHostController)
        // When added programmatically, we need to set the NavController on the parent - i.e.,
        // the View that has the ID matching this NavHostFragment.
        if (view.getParent() != null) {
            viewParent = view.getParent() as View
            if (viewParent!!.id == id) {
                Navigation.setViewNavController(viewParent!!, navHostController)
            }
        }
    }

    @CallSuper
    override fun onInflate(
        context: Context,
        attrs: AttributeSet,
        savedInstanceState: Bundle?
    ) {
        super.onInflate(context, attrs, savedInstanceState)
        context.obtainStyledAttributes(
            attrs,
            androidx.navigation.R.styleable.NavHost
        ).use { navHost ->
            val graphId = navHost.getResourceId(
                androidx.navigation.R.styleable.NavHost_navGraph, 0
            )
            if (graphId != 0) {
                this.graphId = graphId
            }
        }
        context.obtainStyledAttributes(attrs, R.styleable.NavHostFragment).use { array ->
            val defaultHost = array.getBoolean(R.styleable.NavHostFragment_defaultNavHost, false)
            if (defaultHost) {
                defaultNavHost = true
            }
        }
    }

    @CallSuper
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val navState = navHostController!!.saveState()
        if (navState != null) {
            outState.putBundle(KEY_NAV_CONTROLLER_STATE, navState)
        }
        if (defaultNavHost) {
            outState.putBoolean(KEY_DEFAULT_NAV_HOST, true)
        }
        if (graphId != 0) {
            outState.putInt(KEY_GRAPH_ID, graphId)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewParent?.let { it ->
            if (Navigation.findNavController(it) === navHostController) {
                Navigation.setViewNavController(it, null)
            }
        }
        viewParent = null
    }

    companion object {
        /** @hide */
        @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
        const val KEY_GRAPH_ID: String = "android-support-nav:fragment:graphId"

        /** @hide */
        @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
        const val KEY_START_DESTINATION_ARGS: String =
            "android-support-nav:fragment:startDestinationArgs"
        private const val KEY_NAV_CONTROLLER_STATE =
            "android-support-nav:fragment:navControllerState"
        private const val KEY_DEFAULT_NAV_HOST = "android-support-nav:fragment:defaultHost"

        /**
         * Find a [NavController] given a local [Fragment].
         *
         * This method will locate the [NavController] associated with
         * this Fragment, looking first for a [NavHostFragment] along
         * the given Fragment's parent chain. If a [NavController]
         * is not found, this method will look for one along
         * this Fragment's [view hierarchy][Fragment.getView]
         * as specified by [Navigation.findNavController].
         *
         * @param fragment the locally scoped Fragment for navigation
         * @return the locally scoped [NavController] for navigating from this
         *     [Fragment]
         * @throws IllegalStateException if the given Fragment does not correspond
         *     with a [NavHost] or is not within a NavHost.
         */
        @JvmStatic
        fun findNavController(fragment: Fragment): NavController {
            var findFragment: Fragment? = fragment
            while (findFragment != null) {
                if (findFragment is XMNavHostFragment) {
                    return findFragment.navHostController as NavController
                }
                val primaryNavFragment = findFragment.parentFragmentManager
                    .primaryNavigationFragment
                if (primaryNavFragment is XMNavHostFragment) {
                    return primaryNavFragment.navHostController as NavController
                }
                findFragment = findFragment.parentFragment
            }

            // Try looking for one associated with the view instead, if applicable
            val view = fragment.view
            if (view != null) {
                return Navigation.findNavController(view)
            }

            // For DialogFragments, look at the dialog's decor view
            val dialogDecorView = (fragment as? DialogFragment)?.dialog?.window?.decorView
            if (dialogDecorView != null) {
                return Navigation.findNavController(dialogDecorView)
            }
            throw IllegalStateException("Fragment $fragment does not have a NavController set")
        }

        /**
         * Create a new NavHostFragment instance with an inflated [NavGraph]
         * resource.
         *
         * @param graphResId Resource id of the navigation graph to inflate.
         * @param startDestinationArgs Arguments to send to the start destination
         *     of the graph.
         * @return A new NavHostFragment instance.
         */
        @JvmOverloads
        @JvmStatic
        fun create(
            @NavigationRes graphResId: Int,
            startDestinationArgs: Bundle? = null
        ): NavHostFragment {
            var b: Bundle? = null
            if (graphResId != 0) {
                b = Bundle()
                b.putInt(KEY_GRAPH_ID, graphResId)
            }
            if (startDestinationArgs != null) {
                if (b == null) {
                    b = Bundle()
                }
                b.putBundle(KEY_START_DESTINATION_ARGS, startDestinationArgs)
            }
            val result = NavHostFragment()
            if (b != null) {
                result.arguments = b
            }
            return result
        }
    }
}
