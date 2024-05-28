package com.example.news.ui.profile.main

import android.content.Intent
import android.graphics.Outline
import android.os.Bundle
import android.view.View
import android.view.ViewOutlineProvider
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.WindowCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.news.R
import com.example.news.databinding.FragmentProfileBinding
import com.example.news.di.AppComponentHolder
import com.example.news.ui.auth.AuthActivity
import com.example.news.ui.common.AppBarStateChangeListener
import com.example.news.ui.common.BaseFragment
import com.example.news.ui.common.RecyclerItemDecorator
import com.example.news.ui.profile.main.adapter.ProfileAdapter
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject


class ProfileFragment : BaseFragment<FragmentProfileBinding>() {
    @Inject
    lateinit var viewModelFactory: ProfileViewModelFactory
    private val viewModel: ProfileViewModel by viewModels { viewModelFactory }
    private val adapter: ProfileAdapter = ProfileAdapter()

    override fun createViewBinding(): FragmentProfileBinding {
        return FragmentProfileBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AppComponentHolder.get().inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun initUi() {
        setTranslucentStatusBar(true)
        with(viewBinding) {
            val decoration = RecyclerItemDecorator(
                ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.profile_info_divider,
                    null
                )!!
            )
            infoRecyclerView.addItemDecoration(decoration)
            infoRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            infoRecyclerView.adapter = adapter

            viewModel.loadProfile()

            (activity as AppCompatActivity).setSupportActionBar(toolbar)
            (activity as AppCompatActivity).supportActionBar?.title = ""
            toolbar.setNavigationOnClickListener {
                (activity as AppCompatActivity).onBackPressedDispatcher.onBackPressed()
            }

            profileImage.setOnClickListener {
                val bundle = Bundle()
                bundle.putString("imageUrl", viewModel.image.value)

                this@ProfileFragment.findNavController()
                    .navigate(
                        R.id.action_profileFragment_to_profileShowImageFragment,
                        bundle
                    )
            }

            editButton.setOnClickListener {
                this@ProfileFragment.findNavController()
                    .navigate(R.id.action_profileFragment_to_profileEditFragment)
            }

            sourcesButton.setOnClickListener {
                this@ProfileFragment.findNavController()
                    .navigate(R.id.action_profileFragment_to_sourcesFragment)
            }

            languagesButton.setOnClickListener {
                this@ProfileFragment.findNavController()
                    .navigate(R.id.action_profileFragment_to_languagesFragment)
            }

            exitButton.setOnClickListener {
                val message = resources.getText(R.string.profile_dialog_exit).toString()
                ProfileDialogFragment(message) {
                    viewModel.logout()
                }.show(
                    childFragmentManager,
                    ProfileDialogFragment.TAG
                )
            }
            profileAppBar.addOnOffsetChangedListener(object : AppBarStateChangeListener() {
                override fun onStateChanged(appBarLayout: AppBarLayout?, state: State?) {
                    if (state == State.COLLAPSED) {
                        toolbar.setBackgroundColor(resources.getColor(R.color.blue_700, null))
                    }
                    if (state == State.IDLE)
                        toolbar.setBackgroundColor(resources.getColor(R.color.blue_700_08, null))
                }
            })

            collapsingToolbar.setStatusBarScrimColor(resources.getColor(R.color.blue_700, null))

            profileImage.outlineProvider = object : ViewOutlineProvider() {
                override fun getOutline(view: View?, outline: Outline?) {
                    val corner = 48f
                    outline?.setRoundRect(0, -corner.toInt(), view!!.width, view.height, corner)
                }
            }
            profileImage.clipToOutline = true
        }
    }

    override fun onDestroyView() {
        setTranslucentStatusBar(false)
        super.onDestroyView()
    }

    private fun setTranslucentStatusBar(isTranslucent: Boolean) {
        val window = requireActivity().window
        if (isTranslucent) {
            WindowCompat.setDecorFitsSystemWindows(window, false)
            window.statusBarColor = resources.getColor(R.color.blue_700_08, null)
        } else {
            window.statusBarColor = resources.getColor(R.color.blue_700, null)
            WindowCompat.setDecorFitsSystemWindows(window, true)
        }
    }

    override fun observeData() {
        viewModel.errorEvent.observe(viewLifecycleOwner) {
            val snackBar = Snackbar.make(
                requireContext(),
                viewBinding.root,
                resources.getText(it),
                Snackbar.LENGTH_SHORT
            )
            snackBar.show()
        }

        viewModel.goToAuthEvent.observe(viewLifecycleOwner) {
            val intent = Intent(requireContext(), AuthActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            requireActivity().finish()
        }

        viewModel.profileLiveData.observe(viewLifecycleOwner) {
            adapter.setItems(it)
        }

        viewModel.image.observe(viewLifecycleOwner) {
            with(viewBinding) {
                Glide.with(profileImage)
                    .load(it)
                    .placeholder(R.drawable.avatar_placeholder)
                    .into(profileImage)
            }
        }
    }
}