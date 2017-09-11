package com.financial.quantgroup.v2.adapter

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.view.ViewGroup

import java.util.ArrayList
import java.util.Arrays

/**
 * 不可编辑fragment条目的viewpager的adapter
 * @author cz
 */
class DeveloperFragmentPagerAdapter constructor(fm: FragmentManager, fragments: Array<Fragment>, titles: Array<CharSequence>? = null) : FragmentPagerAdapter(fm) {

    private val fragments: MutableList<Fragment>
    private val titles: MutableList<CharSequence>
    init {
        this.fragments = ArrayList<Fragment>()
        this.fragments.addAll(fragments.toList())
        this.titles = ArrayList<CharSequence>()
        if (null != titles) {
            this.titles.addAll(Arrays.asList(*titles))
        }
        if(!this.titles.isEmpty()&&this.titles.size!=this.fragments.size){
            throw IllegalArgumentException("title.size:${this.titles.size} != fragments.size:${this.fragments.size}")
        }
    }
    override fun getPageTitle(position: Int): CharSequence =titles[position]

    override fun getItem(position: Int): Fragment =fragments[position]

    override fun getCount(): Int =fragments.size

    override fun finishUpdate(container: ViewGroup?) {
        try {
            super.finishUpdate(container)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun forEach(action:(Fragment)->Unit)=fragments.forEach(action)
}