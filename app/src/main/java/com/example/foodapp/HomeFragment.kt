package com.example.foodapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.viewpager.widget.ViewPager

class HomeFragment : Fragment() {

    private lateinit var viewPager: ViewPager
    private val imagelist = arrayOf(R.drawable.img_8,R.drawable.img_9,R.drawable.img_10,R.drawable.img_11)
    private var currentPage = 0
    private val delayTime: Long = 2000 // 2 seconds
    private var handler: Handler = Handler()
    private lateinit var breakfastbutton : ImageButton
    private lateinit var lunchbutton : ImageButton
    private lateinit var snackbutton : ImageButton
    private lateinit var starterbutton : ImageButton
    private lateinit var dinnerbutton : ImageButton
    private lateinit var dessertbutton : ImageButton


    private val runnable: Runnable = object : Runnable {
        override fun run() {
            if (currentPage == imagelist.size) {
                currentPage = 0
            }
            viewPager.setCurrentItem(currentPage++, true)
            handler.postDelayed(this, delayTime)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        viewPager = view.findViewById(R.id.viewpager)

        breakfastbutton = view.findViewById(R.id.breakfast)
        lunchbutton = view.findViewById(R.id.lunch)
        snackbutton = view.findViewById(R.id.snack)
        starterbutton = view.findViewById(R.id.starters)
        dinnerbutton = view.findViewById(R.id.dinner)
        dessertbutton = view.findViewById(R.id.dessert)



        val adapter = ImagePagerAdapter(imagelist, requireContext())
        viewPager.adapter = adapter

        breakfastbutton.setOnClickListener{
            val intent = Intent(requireContext(),BreakfastActivity::class.java)
            startActivity(intent)
        }
        lunchbutton.setOnClickListener{
            val intent = Intent(requireContext(),LunchActivity::class.java)
            startActivity(intent)
        }
        snackbutton.setOnClickListener{
            val intent = Intent(requireContext(),SnackActivity::class.java)
            startActivity(intent)
        }
        starterbutton.setOnClickListener{
            val intent = Intent(requireContext(),StartersActivity::class.java)
            startActivity(intent)
        }
        dinnerbutton.setOnClickListener{
            val intent = Intent(requireContext(),DinnerActivity::class.java)
            startActivity(intent)
        }
        dessertbutton.setOnClickListener{
            val intent = Intent(requireContext(),DessertsActivity::class.java)
            startActivity(intent)
        }


        // Start automatic image slideshow
        handler.postDelayed(runnable, delayTime)

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Stop the slideshow when the fragment is destroyed or paused
        handler.removeCallbacks(runnable)
        }
}
