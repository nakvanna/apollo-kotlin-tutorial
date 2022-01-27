package com.example.rocketreserver

import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import apolloClient
import com.example.rocketreserver.databinding.LoginFragmentBinding

class LoginFragment : Fragment() {
    private lateinit var binding: LoginFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = LoginFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.submitProgressBar.visibility = View.GONE
        binding.submit.setOnClickListener {
            val email = binding.email.text.toString()
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.emailLayout.error = getString(R.string.invalid_email)
                return@setOnClickListener
            }

            binding.submitProgressBar.visibility = View.VISIBLE
            binding.submit.visibility = View.GONE

            lifecycleScope.launchWhenResumed {
                val response = try {
                    apolloClient(requireContext()).mutation(LoginMutation(email = email)).execute()
                } catch (e: Exception) {
                    Log.i("Login", "Login Error")
                    null
                }

                val login = response?.data?.login
                Log.i("Log", "Result: ${response?.data}")
                if (login == null || response.hasErrors()) {
                    binding.submitProgressBar.visibility = View.GONE
                    binding.submit.visibility = View.VISIBLE
                    println("ON login == null || response.hasErrors()")
                    return@launchWhenResumed
                }

                login.token?.let { token -> User.setToken(requireContext(), token) }
                findNavController().popBackStack()
            }
        }
    }
}
