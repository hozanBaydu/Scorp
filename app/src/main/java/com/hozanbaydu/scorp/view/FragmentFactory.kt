package com.hozanbaydu.scorp.view

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.bumptech.glide.RequestManager
import com.hozanbaydu.scorp.adapter.MainAdapter
import javax.inject.Inject

class FragmentFactory @Inject constructor(
    var mainAdapter: MainAdapter,
    private val glide : RequestManager,

    ): FragmentFactory() {

    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {

        return when(className){
            MainFragment::class.java.name -> MainFragment(mainAdapter)
            else -> return super.instantiate(classLoader, className)
        }
    }
}